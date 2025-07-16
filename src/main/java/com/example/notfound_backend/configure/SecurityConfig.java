package com.example.notfound_backend.configure;

import com.example.notfound_backend.component.CustomAccessDeniedHandler;
import com.example.notfound_backend.component.CustomAuthEntryPoint;
import com.example.notfound_backend.jwt.JwtFilter;
import com.example.notfound_backend.jwt.JwtLoginFilter;
import com.example.notfound_backend.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthEntryPoint customAuthEntryPoint; // 인증실패 예외
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 메인 인증로직
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                // 요청별 권한
                .authorizeHttpRequests(requests -> {
//                    requests.requestMatchers("**").permitAll(); // 인증 무력화 (임시)
                    requests.requestMatchers("/","/api/join", "/api/login","/api/reissue","/api/naver","/api/kakao","/api/google","api/login/oauth2/code/*").permitAll();
                    requests.requestMatchers("/admin/**").hasRole("ADMIN");
                    requests.requestMatchers("/user/**").hasAnyRole("USER","ADMIN");
                    requests.anyRequest().authenticated();
                })
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowCredentials(true); // 쿠키허용
                    corsConfiguration.addAllowedHeader("*"); //클라이언트가 요청을 보낼때 보낼수 있는 헤더
                    corsConfiguration.setExposedHeaders(List.of("Authorization"));
                    corsConfiguration.addAllowedOrigin("http://localhost:3000");
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    return corsConfiguration;
                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(this.jwtUtil), JwtLoginFilter.class) // 로그인필터 앞에 Jwt필터 위치
                .addFilterAt(new JwtLoginFilter(authenticationManager(this.authenticationConfiguration), this.jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(this.customAuthEntryPoint);
                    exception.accessDeniedHandler(this.customAccessDeniedHandler);
                });
        return http.build();
    }
}
