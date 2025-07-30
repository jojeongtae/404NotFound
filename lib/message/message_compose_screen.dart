import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import '../auth/token.dart';
import '../auth/userInfo.dart';

class MessageComposeScreen extends StatefulWidget {
  const MessageComposeScreen({super.key});

  @override
  State<MessageComposeScreen> createState() => _MessageComposeScreenState();
}

class _MessageComposeScreenState extends State<MessageComposeScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _receiverController = TextEditingController();
  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _messageController = TextEditingController();

  Future<String?> _getUsernameFromNickname(String nickname) async {
    final url = Uri.parse('http://192.168.0.26:8080/api/user/nickname/$nickname');
    try {
      final response = await http.get(url);
      if (response.statusCode == 200) {
        final Map<String, dynamic> responseData = json.decode(utf8.decode(response.bodyBytes));
        return responseData['username'];
      } else if (response.statusCode == 404) {
        // 닉네임을 찾을 수 없음
        return null;
      } else {
        print('닉네임으로 사용자 이름 조회 실패: ${response.statusCode}, ${utf8.decode(response.bodyBytes)}');
        return null;
      }
    } catch (e) {
      print('닉네임으로 사용자 이름 조회 중 오류 발생: $e');
      return null;
    }
  }

  Future<void> _sendMessage() async {
    if (_formKey.currentState!.validate()) {
      final String receiverNickname = _receiverController.text;
      final String title = _titleController.text;
      final String message = _messageController.text;

      final token = Provider.of<Token>(context, listen: false).accessToken;
      final author = Provider.of<UserInfo>(context, listen: false).username; // 현재 로그인된 사용자 이름

      if (author == null || author.isEmpty) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('로그인이 필요합니다.')),
        );
        return;
      }

      // Convert nickname to username
      final String? receiverUsername = await _getUsernameFromNickname(receiverNickname);

      if (receiverUsername == null) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('받는 사람의 닉네임을 찾을 수 없습니다. 정확한 닉네임을 입력해주세요.')),
        );
        return;
      }

      final url = Uri.parse('http://192.168.0.26:8080/api/message/send'); // TODO: 실제 백엔드 IP로 변경

      try {
        final response = await http.post(
          url,
          headers: {
            'Content-Type': 'application/json; charset=UTF-8',
            'Authorization': 'Bearer $token',
          },
          body: jsonEncode({
            'receiver': receiverUsername,
            'title': title,
            'message': message,
            'author': author, // 현재 로그인된 사용자 이름
          }),
        );

        if (response.statusCode == 200 || response.statusCode == 201) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('쪽지가 성공적으로 전송되었습니다.')),
          );
          Navigator.pop(context); // 이전 화면으로 돌아가기
        } else {
          final errorBody = utf8.decode(response.bodyBytes);
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('쪽지 전송 실패: ${response.statusCode} - $errorBody')),
          );
          print('쪽지 전송 실패 응답 본문: $errorBody');
        }
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('쪽지 전송 중 오류 발생: $e')),
        );
        print('쪽지 전송 중 예외 발생: $e');
      }
    }
  }

  @override
  void dispose() {
    _receiverController.dispose();
    _titleController.dispose();
    _messageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('쪽지 작성'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              TextFormField(
                controller: _receiverController,
                decoration: const InputDecoration(
                  labelText: '받는 사람 (닉네임)',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '받는 사람을 입력해주세요.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16.0),
              TextFormField(
                controller: _titleController,
                decoration: const InputDecoration(
                  labelText: '제목',
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '제목을 입력해주세요.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16.0),
              TextFormField(
                controller: _messageController,
                decoration: const InputDecoration(
                  labelText: '내용',
                  border: OutlineInputBorder(),
                ),
                maxLines: 5,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '내용을 입력해주세요.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 24.0),
              Center(
                child: ElevatedButton(
                  onPressed: _sendMessage,
                  child: const Text('쪽지 전송'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
