import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:provider/provider.dart';
import 'package:notfound_flutter/auth/token.dart';
import 'package:notfound_flutter/auth/userInfo.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  Future<void> getUserInfoAndGrade(
      String username, String token, UserInfo userInfo) async {
    try {
      // 사용자 정보 요청
      final infoUrl = Uri.parse(
          'http://192.168.0.26:8080/api/user/user-info?username=$username');
      final infoRes = await http.get(infoUrl, headers: {
        'Authorization': 'Bearer $token',
      });
      print('[유저 정보 응답]: $infoRes');

      if (infoRes.statusCode == 200) {
        final infoData = json.decode(utf8.decode(infoRes.bodyBytes));
        print('[유저 정보 응답]: $infoData');
        userInfo.updateFromJson(infoData);
      } else {
        print('유저 정보 요청 실패: ${infoRes.statusCode}');
      }

      // 등급 요청
      final gradeUrl = Uri.parse(
          'http://192.168.0.26:8080/api/user/user-grade?username=$username');
      final gradeResponse = await http.get(gradeUrl, headers: {
        'Authorization': 'Bearer $token',
      });

      if (gradeResponse.statusCode == 200) {
        final gradeString = utf8.decode(gradeResponse.bodyBytes).trim();
        print('등급 정보: $gradeString');
        userInfo.grade = gradeString;
        userInfo.notifyListeners();
      } else {
        print('등급 요청 실패: ${gradeResponse.statusCode}');
      }
    } catch (e) {
      print('사용자 정보/등급 요청 중 오류: $e');
    }
  }

  Future<void> _login() async {
    if (_formKey.currentState!.validate()) {
      final String username = _usernameController.text;
      final String password = _passwordController.text;

      final url = Uri.parse('http://192.168.0.26:8080/api/login');

      try {
        final response = await http.post(
          url,
          headers: {'Content-Type': 'application/json; charset=UTF-8'},
          body: jsonEncode({'username': username, 'password': password}),
        );

        if (!mounted) return;

        if (response.statusCode == 200) {
          final responseData = json.decode(utf8.decode(response.bodyBytes));
          print('[로그인 응답]: $responseData');

          final tokenProvider = Provider.of<Token>(context, listen: false);
          final userInfoProvider = Provider.of<UserInfo>(context, listen: false);

          String? authHeader = response.headers['authorization'];
          tokenProvider.accessToken = (authHeader != null && authHeader.startsWith('Bearer '))
              ? authHeader.substring(7)
              : '';

          final String? role = responseData['role'];
          userInfoProvider.update(role: role,username: username); // ← 여기서 notifyListeners() 호출됨

          print("현재 role: ${userInfoProvider.role}");

          await getUserInfoAndGrade(
              username, tokenProvider.accessToken, userInfoProvider);

          ScaffoldMessenger.of(context)
              .showSnackBar(const SnackBar(content: Text('로그인 성공!')));
          Navigator.pop(context);
        } else {
          final errorBody = utf8.decode(response.bodyBytes);
          print('로그인 실패 응답 본문: $errorBody');
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('로그인 실패: ${response.statusCode}')),
          );
        }
      } catch (e) {
        print('로그인 중 예외 발생: $e');
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('로그인 중 오류 발생: $e')),
        );
      }
    }
  }

  @override
  void dispose() {
    _usernameController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('로그인')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              TextFormField(
                controller: _usernameController,
                decoration: const InputDecoration(
                  labelText: '사용자 이름',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '사용자 이름을 입력해주세요.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16.0),
              TextFormField(
                controller: _passwordController,
                obscureText: true,
                decoration: const InputDecoration(
                  labelText: '비밀번호',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '비밀번호를 입력해주세요.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 24.0),
              ElevatedButton(onPressed: _login, child: const Text('로그인')),
            ],
          ),
        ),
      ),
    );
  }
}
