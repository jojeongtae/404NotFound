import 'package:flutter/material.dart';
import 'dart:async';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<double> _animation;

  @override
  void initState() {
    super.initState();

    _controller = AnimationController(
      duration: const Duration(milliseconds: 500), // 한 번 깜빡이는 주기 (페이드 인/아웃)
      vsync: this,
    )..repeat(reverse: true); // 애니메이션 반복, 매번 반전

    _animation = Tween(begin: 0.2, end: 1.0).animate(_controller); // 투명도를 0.2에서 1.0으로 애니메이션

    Timer(const Duration(seconds: 3), () {
      Navigator.of(context).pushReplacementNamed('/'); // 메인 화면으로 이동
    });
  }

  @override
  void dispose() {
    _controller.dispose(); // 컨트롤러 해제
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black, // 배경색을 검정색으로 설정
      body: Center(
        child: FadeTransition( // 깜빡이는 효과를 위해 FadeTransition 사용
          opacity: _animation,
          child: const Text(
            '404NotFound',
            style: TextStyle(
              fontSize: 40,
              fontWeight: FontWeight.bold,
              color: Color(0xFF39FF14), // 텍스트 색상을 빨간색으로 설정
              shadows: [
                Shadow(
                  blurRadius: 10.0,
                  color: Color(0xFF39FF14),
                  offset: Offset(0, 0),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
