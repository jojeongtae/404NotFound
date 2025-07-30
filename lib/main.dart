import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:notfound_flutter/admin/report_list_screen.dart';
import 'package:notfound_flutter/auth/token.dart';
import 'package:notfound_flutter/auth/userInfo.dart';
import 'package:notfound_flutter/auth/loginPage.dart';
import 'package:notfound_flutter/board/board_new.dart';
import 'package:notfound_flutter/page/mainLayout.dart';
import 'package:notfound_flutter/page/splash_screen.dart'; // SplashScreen 임포트

import 'package:provider/provider.dart';
import 'package:notfound_flutter/board/board_provider.dart';
import 'package:notfound_flutter/board/board_screen.dart';

import 'package:notfound_flutter/board/board_detail_screen.dart'; // BoardDetailScreen 임포트

import 'package:notfound_flutter/comment/comment_provider.dart'; // CommentProvider 임포트

import 'package:notfound_flutter/board/board_edit_screen.dart'; // BoardEditScreen 임포트
import 'package:notfound_flutter/board/board_post.dart'; // BoardPost 임포트 추가

import 'package:notfound_flutter/auth/profile_edit_screen.dart'; // ProfileEditScreen 임포트

import 'package:notfound_flutter/message/message_list_screen.dart'; // MessageListScreen 임포트
import 'package:notfound_flutter/message/message_compose_screen.dart'; // MessageComposeScreen 임포트

import 'package:notfound_flutter/admin/user_list_provider.dart';
import 'package:notfound_flutter/admin/user_list_screen.dart';

void main() {
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (context) => Token()),
        ChangeNotifierProvider(create: (context) => UserInfo()),
        ChangeNotifierProvider(create: (context) => BoardProvider()), // BoardProvider 추가
        ChangeNotifierProvider(create: (context) => CommentProvider()), // CommentProvider 추가
        ChangeNotifierProvider(create: (context) => UserListProvider()),
      ],
      child: const MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    // "The Glitch" 테마 색상 정의
    const primaryColor = Color(0xFF39FF14); // 네온 그린
    const backgroundColor = Color(0xFF1E1E1E); // 다크 그레이
    const textColor = Color(0xFFEAEAEA); // 밝은 회색
    const darkTextColor = Color(0xFF1E1E1E);

    // 기본 텍스트 테마
    final textTheme = GoogleFonts.firaCodeTextTheme(
      Theme.of(context).textTheme,
    ).copyWith(
      bodyLarge: const TextStyle(color: textColor),
      bodyMedium: const TextStyle(color: textColor),
      displayLarge: const TextStyle(color: textColor),
      displayMedium: const TextStyle(color: textColor),
      displaySmall: const TextStyle(color: textColor),
      headlineMedium: const TextStyle(color: textColor),
      headlineSmall: const TextStyle(color: textColor),
      titleLarge: const TextStyle(color: textColor),
    );

    return MaterialApp(
      title: 'NotFound',
      theme: ThemeData(
        primaryColor: primaryColor,
        scaffoldBackgroundColor: backgroundColor,
        textTheme: textTheme,
        appBarTheme: const AppBarTheme(
          backgroundColor: backgroundColor,
          foregroundColor: primaryColor,
          elevation: 0,
          titleTextStyle: TextStyle(
            fontFamily: 'FiraCode',
            color: primaryColor,
            fontSize: 20,
            fontWeight: FontWeight.bold,
            shadows: [
              Shadow(
                blurRadius: 10.0,
                color: primaryColor,
                offset: Offset(0, 0),
              ),
            ],
          ),
        ),
        elevatedButtonTheme: ElevatedButtonThemeData(
          style: ElevatedButton.styleFrom(
            backgroundColor: primaryColor,
            foregroundColor: darkTextColor,
            textStyle: const TextStyle(
              fontFamily: 'FiraCode',
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        inputDecorationTheme: InputDecorationTheme(
          filled: true,
          fillColor: const Color(0xFF2A2A2A), // 입력 필드 배경색
          labelStyle: const TextStyle(color: primaryColor), // 라벨 텍스트 색상
          hintStyle: const TextStyle(color: Color(0xFF666666)), // 힌트 텍스트 색상
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.0),
            borderSide: const BorderSide(color: Color(0xFF666666)), // 기본 테두리 색상
          ),
          enabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.0),
            borderSide: const BorderSide(color: Color(0xFF666666)),
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.0),
            borderSide: const BorderSide(color: primaryColor, width: 2.0), // 포커스 시 네온 그린 테두리
          ),
        ),
        cardTheme: CardThemeData(
          color: const Color(0xFF121212), // 카드 배경색을 더 어둡게
          margin: const EdgeInsets.all(8.0),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(10.0),
            side: BorderSide(color: primaryColor, width: 1.0), // 네온 그린 테두리
          ),
          elevation: 5,
        ),
        colorScheme: ColorScheme.fromSwatch(
          primarySwatch: Colors.green,
          brightness: Brightness.dark,
        ).copyWith(
          secondary: primaryColor,
        ),
      ),
      initialRoute: "/splash",
      routes: {
        '/splash': (context) => const SplashScreen(),
        '/': (context) => const MainLayout(),
        '/login': (context) => const LoginPage(),
        '/board_new': (context) => BoardNewPage(
            boardType: ModalRoute.of(context)!.settings.arguments as String),
        '/board_list': (context) => BoardScreen(
            boardType: ModalRoute.of(context)!.settings.arguments as String),
        '/board_detail': (context) {
          final args =
              ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;
          return BoardDetailScreen(
            boardType: args['category'] as String,
            postId: int.parse(args['id'] as String),
          );
        },
        '/board_edit': (context) {
          final args =
              ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;
          return BoardEditScreen(
            boardType: args['boardType'] as String,
            post: args['post'] as BoardPost,
          );
        },
        '/profile_edit': (context) => const ProfileEditScreen(),
        '/message_list': (context) => const MessageListScreen(),
        '/message_compose': (context) => const MessageComposeScreen(),
        '/user_list': (context) => const UserListScreen(),
        '/report_list': (context) => const ReportListScreen(),
      },
    );
  }
}
