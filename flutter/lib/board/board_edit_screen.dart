import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:provider/provider.dart';
import 'package:notfound_flutter/auth/token.dart';
import 'package:notfound_flutter/board/board_post.dart';
import 'package:notfound_flutter/board/board_provider.dart';
import 'package:notfound_flutter/auth/userInfo.dart'; // UserInfo 임포트 추가

import 'package:http_parser/http_parser.dart'; // MediaType 사용을 위해 임포트

class BoardEditScreen extends StatefulWidget {
  final String boardType;
  final BoardPost post;

  const BoardEditScreen({super.key, required this.boardType, required this.post});

  @override
  State<BoardEditScreen> createState() => _BoardEditScreenState();
}

class _BoardEditScreenState extends State<BoardEditScreen> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _titleController;
  late TextEditingController _contentController;

  @override
  void initState() {
    super.initState();
    _titleController = TextEditingController(text: widget.post.title);
    _contentController = TextEditingController(text: widget.post.body);
  }

  @override
  void dispose() {
    _titleController.dispose();
    _contentController.dispose();
    super.dispose();
  }

  Future<void> _updatePost() async {
    if (_formKey.currentState!.validate()) {
      final String title = _titleController.text;
      final String content = _contentController.text;
      final token = Provider.of<Token>(context, listen: false).accessToken;
      final userInfo = Provider.of<UserInfo>(context, listen: false);

      if (token.isEmpty || userInfo.username == null || userInfo.username!.isEmpty) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('로그인이 필요합니다.')),
        );
        return;
      }

      // TODO: 실제 서버 주소와 API 엔드포인트로 변경해야 합니다.
      final url = Uri.parse('http://404notfoundpage.duckdns.org:8080/api/${widget.boardType}/${widget.post.id}'); // 예시 API 엔드포인트

      try {
        // boardDTO 데이터 구성
        final boardDto = {
          'title': title,
          'body': content,
          'id': widget.post.id, // 게시글 ID 포함
          'author': userInfo.username!, // author 필드 추가
        };

        // boardDTO를 JSON 문자열로 변환
        final boardDtoJson = jsonEncode(boardDto);

        // MultipartRequest 생성
        var request = http.MultipartRequest('PUT', url); // PUT 요청으로 변경

        // Authorization 헤더 추가
        request.headers['Authorization'] = 'Bearer $token';

        // 'boardDTO'라는 이름으로 JSON 문자열을 application/json 타입으로 추가
        request.files.add(http.MultipartFile.fromString(
          'boardDTO', // 백엔드에서 기대하는 requestpart 이름
          boardDtoJson,
          contentType: MediaType('application', 'json'),
        ));

        var response = await request.send();

        // MultipartRequest의 응답은 StreamedResponse이므로, 응답 본문을 읽어야 합니다.
        final responseBody = await response.stream.bytesToString();

        // StreamedResponse를 http.Response처럼 처리하기 위해 새로운 Response 객체 생성
        final http.Response parsedResponse = http.Response(
          responseBody,
          response.statusCode,
          headers: response.headers,
        );

        if (!mounted) return;

        if (parsedResponse.statusCode == 200 || parsedResponse.statusCode == 201) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('게시글이 성공적으로 수정되었습니다.')),
          );
          // 게시글 목록 새로고침 (필요하다면)
          Provider.of<BoardProvider>(context, listen: false).refreshBoards(widget.boardType);
          Navigator.pop(context); // 이전 화면으로 돌아가기
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('게시글 수정 실패: ${parsedResponse.statusCode}')),
          );
          print('Error response body: ${utf8.decode(parsedResponse.bodyBytes)}');
        }
      } catch (e) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('게시글 수정 중 오류 발생: $e')),
        );
        print('Exception during post update: $e');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('게시글 수정'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
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
                controller: _contentController,
                decoration: const InputDecoration(
                  labelText: '내용',
                  border: OutlineInputBorder(),
                ),
                maxLines: 10,
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
                  onPressed: _updatePost,
                  child: const Text('수정 완료'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
