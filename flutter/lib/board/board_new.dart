import 'dart:io';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:http_parser/http_parser.dart';
import 'package:image_picker/image_picker.dart';
import 'package:provider/provider.dart';
import 'package:image/image.dart' as img;
import 'package:path_provider/path_provider.dart';

import '../auth/userInfo.dart';
import 'package:notfound_flutter/board/board_provider.dart'; // BoardProvider 임포트

class BoardNewPage extends StatefulWidget {
  final String boardType; // 게시판 타입을 받아서 API 경로에 사용

  const BoardNewPage({super.key, required this.boardType});

  @override
  State<BoardNewPage> createState() => _BoardNewPageState();
}

class _BoardNewPageState extends State<BoardNewPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _contentController = TextEditingController();
  XFile? _imgsrc;
  final ImagePicker _picker = ImagePicker();

  Future<void> _pickImage() async {
    final XFile? pickedFile = await _picker.pickImage(source: ImageSource.gallery);

    if (pickedFile != null) {
      final File imageFile = File(pickedFile.path);

      // 이미지 압축
      final img.Image? originalImage = img.decodeImage(await imageFile.readAsBytes());
      if (originalImage != null) {
        final img.Image resizedImage = img.copyResize(originalImage, width: 1024);
        final List<int> compressedImage = img.encodeJpg(resizedImage, quality: 85);

        // 압축된 이미지를 임시 파일로 저장
        final tempDir = await getTemporaryDirectory();
        final File compressedFile = await File('${tempDir.path}/temp_image.jpg').writeAsBytes(compressedImage);

        setState(() {
          _imgsrc = XFile(compressedFile.path);
        });
      } else {
        setState(() {
          _imgsrc = pickedFile;
        });
      }
    }
  }

  Future<void> _submitPost() async {
    if (_formKey.currentState!.validate()) {
      final String title = _titleController.text;
      final String content = _contentController.text;

      // TODO: 실제 서버 주소와 API 엔드포인트로 변경해야 합니다.
      // 백엔드 서버의 IP 주소와 포트 번호를 사용합니다.
      final url = Uri.parse('http://404notfoundpage.duckdns.org:8080/api/${widget.boardType}/new'); // 예시 API 엔드포인트

      try {
        final String author = Provider.of<UserInfo>(context, listen: false).username ?? 'Anonymous';

        // boardDTO 데이터 구성
        final boardDto = {
          'title': title,
          'body': content,
          'author': author, // 사용자 이름으로 변경
        };

        // boardDTO를 JSON 문자열로 변환
        final boardDtoJson = jsonEncode(boardDto);

        // MultipartRequest 생성
        var request = http.MultipartRequest('POST', url);

        // 'boardDTO'라는 이름으로 JSON 문자열을 application/json 타입으로 추가
        request.files.add(http.MultipartFile.fromString(
          'boardDTO', // 백엔드에서 기대하는 requestpart 이름
          boardDtoJson,
          contentType: MediaType('application', 'json'),
        ));

        // 파일도 함께 보내야 한다면 여기에 추가
        if (_imgsrc != null) {
          request.files.add(await http.MultipartFile.fromPath(
            'file', // 백엔드에서 파일을 받는 파라미터 이름
            _imgsrc!.path,
          ));
        }

        var response = await request.send();

        // MultipartRequest의 응답은 StreamedResponse이므로, 응답 본문을 읽어야 합니다.
        final responseBody = await response.stream.bytesToString();

        // StreamedResponse를 http.Response처럼 처리하기 위해 새로운 Response 객체 생성
        final http.Response parsedResponse = http.Response(
          responseBody,
          response.statusCode,
          headers: response.headers,
        );

        if (parsedResponse.statusCode == 200 || parsedResponse.statusCode == 201) {
          // 성공적으로 게시글이 작성되었을 때
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('게시글이 성공적으로 작성되었습니다.')),
          );
          // 게시글 목록 새로고침
          Provider.of<BoardProvider>(context, listen: false).refreshBoards(widget.boardType);
          Navigator.pop(context); // 이전 화면으로 돌아가기
        } else {
          // 게시글 작성 실패
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('게시글 작성 실패: ${parsedResponse.statusCode}')),
          );
          // 에러 응답 본문 확인 (디버깅용)
          print('Error response body: ${utf8.decode(parsedResponse.bodyBytes)}');
        }
      } catch (e) {
        // 네트워크 에러 등 예외 처리
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('게시글 작성 중 오류 발생: $e')),
        );
        print('Exception during post submission: $e');
      }
    }
  }

  @override
  void dispose() {
    _titleController.dispose();
    _contentController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('새 게시글 작성'),
      ),
      body: SafeArea(
        child: SingleChildScrollView(
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
              const SizedBox(height: 16.0),
              _imgsrc == null
                  ? const Text('선택된 이미지가 없습니다.')
                  : Image.file(File(_imgsrc!.path)),
              const SizedBox(height: 16.0),
              ElevatedButton(
                onPressed: _pickImage,
                child: const Text('이미지 선택'),
              ),
              const SizedBox(height: 24.0),
              Center(
                child: ElevatedButton(
                  onPressed: _submitPost,
                  child: const Text('게시글 작성'),
                ),
              ),
              const SizedBox(height: 50.0), // 하단 여백 추가
            ],
          ),
        ),
      ),
    )
    );
  }
}