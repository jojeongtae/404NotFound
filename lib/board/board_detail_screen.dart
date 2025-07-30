import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:provider/provider.dart'; // Provider 임포트
import 'package:notfound_flutter/board/board_post.dart';
import 'package:notfound_flutter/comment/comment_provider.dart'; // CommentProvider 임포트
import 'package:notfound_flutter/auth/userInfo.dart'; // UserInfo 임포트
import 'package:notfound_flutter/auth/token.dart'; // Token 임포트
import 'package:notfound_flutter/board/board_provider.dart'; // BoardProvider 임포트 (게시글 삭제 후 목록 새로고침용)

class BoardDetailScreen extends StatefulWidget {
  final String boardType;
  final int postId;

  const BoardDetailScreen({
    super.key,
    required this.boardType,
    required this.postId,
  });

  @override
  State<BoardDetailScreen> createState() => _BoardDetailScreenState();
}

class _BoardDetailScreenState extends State<BoardDetailScreen> {
  BoardPost? _postDetail;
  bool _isLoading = true;
  String? _errorMessage;
  final TextEditingController _commentController =
      TextEditingController(); // 댓글 입력 컨트롤러
  int? _replyToCommentId; // 대댓글 대상 댓글의 ID
  String? _replyToCommentAuthor; // 대댓글 대상 댓글의 작성자 닉네임

  @override
  void initState() {
    super.initState();
    _fetchPostDetail();
  }

  @override
  void dispose() {
    _commentController.dispose();
    super.dispose();
  }

  Future<void> _fetchPostDetail() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      // TODO: 실제 서버 주소와 API 엔드포인트로 변경해야 합니다.
      final url = Uri.parse(
        'http://192.168.0.26:8080/api/${widget.boardType}/${widget.postId}',
      );
      final response = await http.get(url);

      if (!mounted) return;

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseData = json.decode(
          utf8.decode(response.bodyBytes),
        );
        _postDetail = BoardPost.fromJson(responseData);
        // 게시글 상세 정보 로드 후 댓글 로드
        Provider.of<CommentProvider>(
          context,
          listen: false,
        ).fetchComments(widget.boardType, widget.postId);
      } else {
        _errorMessage = '게시글 상세 정보 불러오기 실패: ${response.statusCode}';
        print('Error response body: ${utf8.decode(response.bodyBytes)}');
      }
    } catch (e) {
      if (!mounted) return;
      _errorMessage = '게시글 상세 정보 불러오기 중 오류 발생: $e';
      print('Exception during fetching post detail: $e');
    } finally {
      if (!mounted) return;
      setState(() {
        _isLoading = false;
      });
    }
  }

  Future<void> _addComment() async {
    if (_commentController.text.isEmpty) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('댓글 내용을 입력해주세요.')));
      return;
    }

    final commentContent = _commentController.text;
    final userInfo = Provider.of<UserInfo>(context, listen: false);
    final token = Provider.of<Token>(context, listen: false);

    if (userInfo.username == null ||
        userInfo.username!.isEmpty ||
        token.accessToken.isEmpty) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('로그인이 필요합니다.')));
      return;
    }

    final commentProvider = Provider.of<CommentProvider>(
      context,
      listen: false,
    );
    final success = await commentProvider.addComment(
      widget.boardType,
      widget.postId,
      commentContent,
      userInfo.username!,
      token.accessToken,
      parentId: _replyToCommentId, // parentId 전달
    );

    if (!mounted) return;

    if (success) {
      _commentController.clear();
      setState(() {
        _replyToCommentId = null; // 댓글 작성 성공 시 parentId 초기화
        _replyToCommentAuthor = null; // 작성자 닉네임 초기화
      });
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('댓글이 성공적으로 작성되었습니다.')));
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('댓글 작성 실패: ${commentProvider.errorMessage}')),
      );
    }
  }

  Future<void> _deletePost() async {
    final token = Provider.of<Token>(context, listen: false).accessToken;

    if (token.isEmpty) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('로그인이 필요합니다.')));
      return;
    }

    // TODO: 실제 서버 주소와 API 엔드포인트로 변경해야 합니다.
    final url = Uri.parse(
      'http://192.168.0.26:8080/api/${widget.boardType}/${widget.postId}',
    ); // 예시 API 엔드포인트

    try {
      final response = await http.delete(
        url,
        headers: <String, String>{'Authorization': 'Bearer $token'},
      );

      if (!mounted) return;

      if (response.statusCode == 200 || response.statusCode == 204) {
        // 204 No Content도 성공으로 간주
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(const SnackBar(content: Text('게시글이 성공적으로 삭제되었습니다.')));
        // 게시글 목록 새로고침 (필요하다면)
        Provider.of<BoardProvider>(
          context,
          listen: false,
        ).refreshBoards(widget.boardType);
        Navigator.pop(context); // 이전 화면으로 돌아가기
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('게시글 삭제 실패: ${response.statusCode}')),
        );
        print('Error response body: ${utf8.decode(response.bodyBytes)}');
      }
    } catch (e) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('게시글 삭제 중 오류 발생: $e')));
      print('Exception during post deletion: $e');
    }
  }

  Future<void> _fetchReport(String reportedUser) async {
    final token = Provider.of<Token>(context, listen: false).accessToken;
    final userInfo = Provider.of<UserInfo>(context, listen: false);
    if (token.isEmpty ||
        userInfo.username == null ||
        userInfo.username!.isEmpty) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('로그인이 필요합니다.')));
      return;
    }
    try {
      final url = Uri.parse('http://192.168.0.26:8080/api/user/report');
      final body = jsonEncode({
        'reporter': userInfo.username,
        'reported': reportedUser,
        'reason' : "게시글 간단 신고",
        'targetId': _postDetail!.id,
        'targetTable' : 'board_${widget.boardType}'
      });
      final res = await http.post(
        url,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
        body: body,
      );
      print(res.body);
    } catch (e) {
      print(e);
    }
  }

  Future<void> _fetchRecommend() async {
    final token = Provider.of<Token>(context, listen: false).accessToken;
    final userInfo = Provider.of<UserInfo>(context, listen: false);

    if (token.isEmpty ||
        userInfo.username == null ||
        userInfo.username!.isEmpty) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('로그인이 필요합니다.')));
      return;
    }

    try {
      final url = Uri.parse(
        'http://192.168.0.26:8080/api/${widget.boardType}/${widget.postId}/recommend',
      );
      final response = await http.post(
        url,
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer $token',
        },
        body: jsonEncode(<String, String>{'username': userInfo.username!}),
      );

      if (!mounted) return;

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(const SnackBar(content: Text('게시글을 추천했습니다.')));
        _fetchPostDetail(); // 추천 수 새로고침
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('추천 실패: ${response.statusCode}')),
        );
        print('Error response body: ${utf8.decode(response.bodyBytes)}');
      }
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('추천 중 오류 발생: $e')));
      print('Exception during fetching recommend: $e');
    }
  }

  void _startReply(int commentId, String authorNickname) {
    setState(() {
      _replyToCommentId = commentId;
      _replyToCommentAuthor = authorNickname;
      _commentController.text = '@$authorNickname '; // 답글 대상 닉네임 자동 입력
      FocusScope.of(context).requestFocus(FocusNode()); // 키보드 올리기
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('게시글 상세'),
        actions: [
          Consumer<UserInfo>(
            builder: (context, userInfo, child) {
              // 게시글 작성자와 현재 로그인된 사용자가 같을 때만 수정/삭제 버튼 표시
              if (_postDetail != null &&
                  userInfo.username == _postDetail!.author) {
                return Row(
                  children: [
                    IconButton(
                      icon: const Icon(Icons.edit),
                      onPressed: () {
                        Navigator.pushNamed(
                          context,
                          '/board_edit',
                          arguments: {
                            'boardType': widget.boardType,
                            'post': _postDetail!,
                          },
                        ).then((_) {
                          // 수정 후 돌아왔을 때 게시글 상세 정보 새로고침
                          _fetchPostDetail();
                        });
                      },
                    ),
                    IconButton(
                      icon: const Icon(Icons.delete),
                      onPressed: () async {
                        // 삭제 확인 다이얼로그
                        final bool? confirmDelete = await showDialog<bool>(
                          context: context,
                          builder: (BuildContext context) {
                            return AlertDialog(
                              title: const Text('게시글 삭제'),
                              content: const Text('정말로 이 게시글을 삭제하시겠습니까?'),
                              actions: <Widget>[
                                TextButton(
                                  onPressed: () =>
                                      Navigator.of(context).pop(false),
                                  child: const Text('취소'),
                                ),
                                TextButton(
                                  onPressed: () =>
                                      Navigator.of(context).pop(true),
                                  child: const Text('삭제'),
                                ),
                              ],
                            );
                          },
                        );
                        if (confirmDelete == true) {
                          _deletePost();
                        }
                      },
                    ),
                  ],
                );
              }
              return Container(); // 권한이 없으면 아무것도 표시하지 않음
            },
          ),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _errorMessage != null
          ? Center(child: Text('오류: ${_errorMessage}'))
          : _postDetail == null
          ? const Center(child: Text('게시글을 찾을 수 없습니다.'))
          : SingleChildScrollView(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    _postDetail!.title,
                    style: const TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    '작성자: ${_postDetail!.authorNickname} | 조회수: ${_postDetail!.views} | 추천: ${_postDetail!.recommend}',
                    style: const TextStyle(color: Colors.grey),
                  ),
                  Text(
                    '작성일: ${_postDetail!.createdAt}',
                    style: const TextStyle(color: Colors.grey),
                  ),
                  const Divider(height: 24, thickness: 1),
                  Text(_postDetail!.body, style: const TextStyle(fontSize: 16)),
                  if (_postDetail!.imgSrc != null && _postDetail!.imgSrc!.isNotEmpty)
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 16.0),
                      child: Image.network(
                        'http://192.168.0.26:8080/${_postDetail!.imgSrc}', // 백엔드에서 제공하는 완전한 이미지 URL 사용
                        fit: BoxFit.cover,
                        errorBuilder: (context, error, stackTrace) {
                          print('Failed to load image from: ${_postDetail!.imgSrc}');
                          return const Text('이미지를 불러올 수 없습니다.');
                        },
                      ),
                    ),
                  const Divider(height: 24, thickness: 1),
                  widget.boardType == 'used' && _postDetail?.price != null
                      ? Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        '가격 : ${_postDetail!.price}',
                        style: const TextStyle(fontSize: 16),
                      ),
                      const Divider(height: 24, thickness: 1),
                    ],
                  )
                      : const SizedBox(),
                  Row(
                    children: [
                      ElevatedButton.icon(
                        onPressed: () {
                          _fetchRecommend();
                        },
                        icon: Icon(Icons.thumb_up,color: Colors.yellow,),
                        label: const Text('추천'),

                      ),
                      ElevatedButton.icon(
                          onPressed: () {
                            if (_postDetail?.author != null) {
                              _fetchReport(_postDetail!.author!);
                            } else {
                              ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(content: Text("신고 대상이 없습니다.")),
                              );
                            }
                          },
                        icon: Icon(Icons.campaign,color: Colors.red,),
                        label: const Text("신고"),
                      ),
                    ],
                  ),

                  const Divider(height: 24, thickness: 1),
                  const Text(
                    '댓글',
                    style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 16),
                  // 답글 대상 표시 및 취소 버튼
                  if (_replyToCommentId != null)
                    Padding(
                      padding: const EdgeInsets.only(bottom: 8.0),
                      child: Row(
                        children: [
                          Text(
                            '답글 대상: @$_replyToCommentAuthor',
                            style: const TextStyle(color: Colors.blue),
                          ),
                          const SizedBox(width: 8),
                          GestureDetector(
                            onTap: () {
                              setState(() {
                                _replyToCommentId = null;
                                _replyToCommentAuthor = null;
                                _commentController.clear();
                              });
                            },
                            child: const Icon(
                              Icons.cancel,
                              color: Colors.red,
                              size: 18,
                            ),
                          ),
                        ],
                      ),
                    ),
                  // 댓글 입력 필드
                  Row(
                    children: [
                      Expanded(
                        child: TextField(
                          controller: _commentController,
                          decoration: const InputDecoration(
                            hintText: '댓글을 입력하세요...',
                            border: OutlineInputBorder(),
                          ),
                        ),
                      ),
                      const SizedBox(width: 8),
                      ElevatedButton(
                        onPressed: _addComment,
                        child: const Text('등록'),
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                  // 댓글 목록
                  Consumer<CommentProvider>(
                    builder: (context, commentProvider, child) {
                      if (commentProvider.isLoading) {
                        return const Center(child: CircularProgressIndicator());
                      } else if (commentProvider.errorMessage != null) {
                        return Center(
                          child: Text('오류: ${commentProvider.errorMessage}'),
                        );
                      } else if (commentProvider.comments.isEmpty) {
                        return const Center(child: Text('댓글이 없습니다.'));
                      } else {
                        return ListView.builder(
                          shrinkWrap: true, // ListView가 Column 안에 있을 때 필요
                          physics:
                              const NeverScrollableScrollPhysics(), // 스크롤 방지
                          itemCount: commentProvider.comments.length,
                          itemBuilder: (context, index) {
                            final comment = commentProvider.comments[index];
                            // parentId에 따라 들여쓰기 적용
                            final double indentation = comment.parentId != null
                                ? 20.0
                                : 0.0;
                            return Padding(
                              padding: EdgeInsets.only(left: indentation),
                              child: Card(
                                margin: const EdgeInsets.symmetric(vertical: 4),
                                child: Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        comment.authorNickname,
                                        style: const TextStyle(
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                      const SizedBox(height: 4),
                                      Text(comment.content),
                                      const SizedBox(height: 4),
                                      Align(
                                        alignment: Alignment.bottomRight,
                                        child: Text(
                                          comment.createdAt,
                                          style: const TextStyle(
                                            color: Colors.grey,
                                            fontSize: 12,
                                          ),
                                        ),
                                      ),
                                      // 답글 버튼
                                      Align(
                                        alignment: Alignment.bottomRight,
                                        child: TextButton(
                                          onPressed: comment.id != null
                                              ? () => _startReply(
                                                  comment.id!,
                                                  comment.authorNickname,
                                                )
                                              : null,
                                          child: const Text('답글'),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ),
                            );
                          },
                        );
                      }
                    },
                  ),
                ],
              ),
            ),
    );
  }
}
