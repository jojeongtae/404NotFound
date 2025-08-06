import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:notfound_flutter/comment/comment_model.dart';
import 'package:notfound_flutter/auth/token.dart'; // 토큰 사용을 위해 임포트

class CommentProvider extends ChangeNotifier {
  List<Comment> _comments = [];
  bool _isLoading = false;
  String? _errorMessage;

  List<Comment> get comments => _comments;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  Future<void> fetchComments(String boardType, int postId) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      // TODO: 실제 서버 주소와 API 엔드포인트로 변경해야 합니다.
      final url = Uri.parse('http://404notfoundpage.duckdns.org:8080/api/${boardType}/comments/${postId}');
      final response = await http.get(url);

      if (response.statusCode == 200) {
        final List<dynamic> responseData = json.decode(utf8.decode(response.bodyBytes));
        final List<Comment> fetchedComments = responseData.map((json) => Comment.fromJson(json)).toList();
        _comments = _sortAndStructureComments(fetchedComments);
      } else {
        _errorMessage = '댓글 불러오기 실패: ${response.statusCode}';
        print('Error response body: ${utf8.decode(response.bodyBytes)}');
      }
    } catch (e) {
      _errorMessage = '댓글 불러오기 중 오류 발생: $e';
      print('Exception during fetching comments: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // 댓글 목록을 계층 구조로 정렬하고 재구성하는 헬퍼 메서드
  List<Comment> _sortAndStructureComments(List<Comment> flatComments) {
    final List<Comment> finalSortedList = [];

    // 모든 댓글을 ID로 빠르게 찾을 수 있도록 맵에 저장
    final Map<int?, Comment> commentMap = { for (var c in flatComments) c.id: c };

    // 루트 댓글 (parentId가 null인 댓글)만 추출하여 작성일 기준으로 정렬
    final List<Comment> rootComments = flatComments.where((c) => c.parentId == null).toList();
    rootComments.sort((a, b) => a.createdAt.compareTo(b.createdAt));

    // 재귀적으로 댓글과 그에 속한 대댓글들을 정렬된 리스트에 추가
    void addCommentAndReplies(Comment comment) {
      finalSortedList.add(comment);
      
      // 현재 댓글의 직접적인 대댓글들을 찾아 작성일 기준으로 정렬
      final List<Comment> directReplies = flatComments
          .where((c) => c.parentId == comment.id)
          .toList();
      directReplies.sort((a, b) => a.createdAt.compareTo(b.createdAt));

      for (var reply in directReplies) {
        addCommentAndReplies(reply); // 재귀 호출하여 대댓글과 그 하위 대댓글 추가
      }
    }

    // 정렬된 루트 댓글들을 순회하며 최종 리스트에 추가
    for (var rootComment in rootComments) {
      addCommentAndReplies(rootComment);
    }

    return finalSortedList;
  }

  Future<bool> addComment(
      String boardType, int postId, String content, String author, String accessToken, {int? parentId}) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      // TODO: 실제 서버 주소와 API 엔드포인트로 변경해야 합니다.
      final url = Uri.parse('http://404notfoundpage.duckdns.org:8080/api/${boardType}/comments/new');
      final Map<String, dynamic> body = {
        'content': content,
        'author': author,
        'boardId': postId,
      };
      if (parentId != null) {
        body['parentId'] = parentId;
      }

      final response = await http.post(
        url,
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer $accessToken',
        },
        body: jsonEncode(body),
      );

      if (response.statusCode == 200 || response.statusCode == 201) {
        // 댓글 추가 성공 후 댓글 목록 새로고침
        await fetchComments(boardType, postId);
        return true;
      } else {
        _errorMessage = '댓글 작성 실패: ${response.statusCode}';
        print('Error response body: ${utf8.decode(response.bodyBytes)}');
        return false;
      }
    } catch (e) {
      _errorMessage = '댓글 작성 중 오류 발생: $e';
      print('Exception during adding comment: $e');
      return false;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}