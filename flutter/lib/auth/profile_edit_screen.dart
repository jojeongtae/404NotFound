import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:notfound_flutter/auth/userInfo.dart';
import 'package:notfound_flutter/auth/token.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class ProfileEditScreen extends StatefulWidget {
  const ProfileEditScreen({super.key});

  @override
  State<ProfileEditScreen> createState() => _ProfileEditScreenState();
}

class _ProfileEditScreenState extends State<ProfileEditScreen> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _usernameController;
  late TextEditingController _addressController;
  late TextEditingController _phoneController;
  late TextEditingController _nicknameController;

  @override
  void initState() {
    super.initState();
    final userInfo = Provider.of<UserInfo>(context, listen: false);
    _usernameController = TextEditingController(text: userInfo.username);
    _addressController = TextEditingController(text: userInfo.address);
    _phoneController = TextEditingController(text: userInfo.phone);
    _nicknameController = TextEditingController(text: userInfo.nickname);
  }

  @override
  void dispose() {
    _usernameController.dispose();
    _addressController.dispose();
    _phoneController.dispose();
    _nicknameController.dispose();
    super.dispose();
  }

  Future<void> _updateProfile() async {
    if (_formKey.currentState!.validate()) {
      final userInfo = Provider.of<UserInfo>(context, listen: false);
      final token = Provider.of<Token>(context, listen: false);

      if (token.accessToken.isEmpty) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('로그인이 필요합니다.')),
        );
        return;
      }

      // TODO: 실제 서버 주소와 API 엔드포인트로 변경해야 합니다。
      final url = Uri.parse('http://404notfoundpage.duckdns.org:8080/api/user/user-info'); // 예시 API 엔드포인트

      try {
        final response = await http.put(
          url,
          headers: <String, String>{
            'Content-Type': 'application/json; charset=UTF-8',
            'Authorization': 'Bearer ${token.accessToken}',
          },
          body: jsonEncode({
            'username': _usernameController.text,
            'address': _addressController.text,
            'phone': _phoneController.text,
            'nickname': _nicknameController.text,
          }),
        );

        if (!mounted) return;

        final String responseBody = utf8.decode(response.bodyBytes);

        if (response.statusCode == 200) {
          final Map<String, dynamic> responseData = json.decode(responseBody);
          userInfo.updateFromJson(responseData); // UserInfo Provider 업데이트

          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('프로필이 성공적으로 업데이트되었습니다.')),
          );
          Navigator.pop(context); // 이전 화면으로 돌아가기
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('프로필 업데이트 실패: ${response.statusCode}')),
          );
          print('Error response body: $responseBody');
        }
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('프로필 업데이트 중 오류 발생: $e')),
        );
        print('Exception during profile update: $e');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('프로필 수정'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: <Widget>[
              TextFormField(
                controller: _usernameController,
                decoration: const InputDecoration(
                  labelText: '사용자 이름',
                  border: OutlineInputBorder(),
                ),
                readOnly: true, // 사용자 이름은 수정 불가
              ),
              const SizedBox(height: 16.0),
              TextFormField(
                controller: _addressController,
                decoration: const InputDecoration(
                  labelText: '주소',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '주소를 입력해주세요.';
                  }
                  return null; // 유효한 값이면 에러 없음
                },

              ),
              const SizedBox(height: 16.0),
              TextFormField(
                controller: _phoneController,
                decoration: const InputDecoration(
                  labelText: '전화번호',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '전화번호를 입력해주세요.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16.0),
              TextFormField(
                controller: _nicknameController,
                decoration: const InputDecoration(
                  labelText: '닉네임',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '닉네임을 입력해주세요.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 24.0),
              ElevatedButton(
                onPressed: () => _updateProfile(),
                child: const Text('프로필 업데이트'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
