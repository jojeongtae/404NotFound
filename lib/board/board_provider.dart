import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:notfound_flutter/board/board_post.dart'; // BoardPost 모델 사용

class BoardProvider extends ChangeNotifier {
  List<BoardPost> _boards = [];
  bool _isLoading = false;
  String? _errorMessage;

  List<BoardPost> get boards => _boards;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  Future<void> fetchBoards(String boardType) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      // TODO: 실제 서버 주소와 API 엔드포인트로 변경해야 합니다.
      final url = Uri.parse('http://192.168.0.26:8080/api/${boardType}/list'); // 예시 API 엔드포인트
      final response = await http.get(url);

      if (response.statusCode == 200) {
        final List<dynamic> responseData = json.decode(utf8.decode(response.bodyBytes));
        _boards = responseData.map((json) => BoardPost.fromJson(json)).toList();
      } else {
        _errorMessage = '게시글 불러오기 실패: ${response.statusCode}';
        print('Error response body: ${utf8.decode(response.bodyBytes)}');
      }
    } catch (e) {
      _errorMessage = '게시글 불러오기 중 오류 발생: $e';
      print('Exception during fetching boards: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // 게시글 작성 후 목록을 새로고침하기 위한 메서드
  void refreshBoards(String boardType) {
    fetchBoards(boardType);
  }
}
