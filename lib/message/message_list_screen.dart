import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import '../auth/token.dart';
import '../auth/userInfo.dart';

class MessageListScreen extends StatefulWidget {
  const MessageListScreen({super.key});

  @override
  State<MessageListScreen> createState() => _MessageListScreenState();
}

class _MessageListScreenState extends State<MessageListScreen> {
  List<dynamic> _messages = [];
  bool _isLoading = true;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _fetchMessages();
  }

  Future<void> _fetchMessages() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    final token = Provider.of<Token>(context, listen: false).accessToken;
    final username = Provider.of<UserInfo>(context, listen: false).username;

    if (username == null || username.isEmpty) {
      setState(() {
        _errorMessage = '로그인이 필요합니다.';
        _isLoading = false;
      });
      return;
    }

    final url = Uri.parse('hthttp://404notfoundpage.duckdns.org:8080/api/message/receiver'); // TODO: 실제 백엔드 IP로 변경
    try {
      final response = await http.get(
        url,
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        final List<dynamic> messages = json.decode(utf8.decode(response.bodyBytes));
        setState(() {
          _messages = messages;
          _isLoading = false;
        });
      } else {
        setState(() {
          _errorMessage = '쪽지 목록을 불러오는데 실패했습니다: ${response.statusCode}';
          _isLoading = false;
        });
        print('쪽지 목록 요청 실패: ${response.statusCode}, ${utf8.decode(response.bodyBytes)}');
      }
    } catch (e) {
      setState(() {
        _errorMessage = '쪽지 목록 요청 중 오류 발생: $e';
        _isLoading = false;
      });
      print('쪽지 목록 요청 중 오류 발생: $e');
    }
  }

  Future<void> _deleteMessage(int messageId) async {
    final token = Provider.of<Token>(context, listen: false).accessToken;
    final url = Uri.parse('http://404notfoundpage.duckdns.org:8080/api/message/$messageId'); // Backend DELETE endpoint

    try {
      final response = await http.delete(
        url,
        headers: {
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 204) { // 204 No Content is typical for successful DELETE
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('쪽지가 성공적으로 삭제되었습니다.')),
        );
        // Remove the deleted message from the list
        setState(() {
          _messages.removeWhere((message) => message['id'] == messageId);
        });
      } else {
        final errorBody = utf8.decode(response.bodyBytes);
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('쪽지 삭제 실패: ${response.statusCode} - $errorBody')),
        );
        print('쪽지 삭제 실패 응답 본문: $errorBody');
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('쪽지 삭제 중 오류 발생: $e')),
      );
      print('쪽지 삭제 중 예외 발생: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('쪽지함'),
        actions: [
          IconButton(
            icon: const Icon(Icons.edit),
            onPressed: () {
              Navigator.pushNamed(context, '/message_compose');
            },
          ),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _errorMessage != null
              ? Center(child: Text(_errorMessage!))
              : _messages.isEmpty
                  ? const Center(child: Text('받은 쪽지가 없습니다.'))
                  : ListView.builder(
                      itemCount: _messages.length,
                      itemBuilder: (context, index) {
                        final message = _messages[index];
                        return Card(
                          margin: const EdgeInsets.symmetric(horizontal: 8.0, vertical: 4.0),
                          child: ListTile(
                            leading: const Icon(Icons.mail),
                            title: Text(message['title'] ?? '제목 없음'),
                            subtitle: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text('보낸 사람: ${message['authorNickname'] ?? message['author'] ?? '알 수 없음'}'),
                                Text('내용: ${message['message'] ?? '내용 없음'}'),
                                Text('받은 시간: ${message['createdAt'] != null ? DateTime.parse(message['createdAt']).toLocal().toString().split('.')[0] : '알 수 없음'}'),
                              ],
                            ),
                            trailing: IconButton(
                              icon: const Icon(Icons.delete, color: Colors.redAccent),
                              onPressed: () async {
                                final bool? confirmDelete = await showDialog<bool>(
                                  context: context,
                                  builder: (BuildContext context) {
                                    return AlertDialog(
                                      title: const Text('쪽지 삭제'),
                                      content: const Text('정말로 이 쪽지를 삭제하시겠습니까?'),
                                      actions: <Widget>[
                                        TextButton(
                                          onPressed: () => Navigator.of(context).pop(false),
                                          child: const Text('취소'),
                                        ),
                                        TextButton(
                                          onPressed: () => Navigator.of(context).pop(true),
                                          child: const Text('삭제'),
                                        ),
                                      ],
                                    );
                                  },
                                );
                                if (confirmDelete == true) {
                                  _deleteMessage(message['id']);
                                }
                              },
                            ),
                            onTap: () {
                              // TODO: 쪽지 상세 보기 기능 추가 (선택 사항)
                            },
                          ),
                        );
                      },
                    ),
    );
  }
}
