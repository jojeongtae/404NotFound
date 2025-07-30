import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:notfound_flutter/board/board_provider.dart';
import 'package:notfound_flutter/board/board_post.dart';
import 'package:http/http.dart' as http; // http 임포트 추가
import 'dart:convert'; // dart:convert 임포트 추가

import '../auth/userInfo.dart';
import '../auth/token.dart'; // Token 임포트 추가

class BoardScreen extends StatefulWidget {
  final String boardType;

  const BoardScreen({super.key, required this.boardType});

  @override
  State<BoardScreen> createState() => _BoardScreenState();
}

class _BoardScreenState extends State<BoardScreen> {
  final TextEditingController _searchController = TextEditingController();
  String _searchQuery = '';
  String _searchType = '제목'; // 검색 유형 상태 변수

  String _getCategoryDisplayName(String category) {
    switch (category) {
      case 'notice':
        return '공지게시판';
      case 'free':
        return '자유게시판';
      case 'food':
        return '음식게시판';
      case 'used':
        return '중고게시판';
      case 'info':
        return '정보게시판';
      case 'qna':
        return '질문게시판';
      default:
        return '기타';
    }
  }

  @override
  void initState() {
    super.initState();
    print('[_BoardScreen] initState 호출됨. fetchBoards 시작.'); // 디버깅용
    // 화면이 처음 로드될 때 게시글 목록을 가져옵니다.
    WidgetsBinding.instance.addPostFrameCallback((_) {
      Provider.of<BoardProvider>(
        context,
        listen: false,
      ).fetchBoards(widget.boardType);
    });
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  void _performSearch() {
    setState(() {
      _searchQuery = _searchController.text;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('${_getCategoryDisplayName(widget.boardType)}'),
        automaticallyImplyLeading: false, // 기본 뒤로가기 버튼 제거
        leading: Builder(
          builder: (BuildContext context) {
            return IconButton(
              icon: const Icon(Icons.menu), // 햄버거 메뉴 아이콘
              onPressed: () {
                Scaffold.of(context).openDrawer();
              },
            );
          },
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: () {
              Provider.of<BoardProvider>(
                context,
                listen: false,
              ).fetchBoards(widget.boardType);
            },
          ),
          IconButton(
            icon: const Icon(Icons.add),
            onPressed: () {
              Navigator.pushNamed(
                context,
                '/board_new',
                arguments: widget.boardType,
              );
            },
          ),
          IconButton(onPressed: (){
            Navigator.pushNamedAndRemoveUntil(
              context,
              '/',
                  (Route<dynamic> route) => false,
            );
          }, icon: Icon(Icons.home))
        ],
      ),
      body: Container(
        color: const Color(0xFF1A1A2A), // 눈에 띄는 배경색 추가
        child: Consumer<BoardProvider>(
          builder: (context, boardProvider, child) {
            if (boardProvider.isLoading) {
              return const Center(child: CircularProgressIndicator());
            } else if (boardProvider.errorMessage != null) {
              return Center(child: Text('오류: ${boardProvider.errorMessage}'));
            } else if (boardProvider.boards.isEmpty) {
              return const Center(child: Text('게시글이 없습니다.'));
            } else {
              final filteredBoards = boardProvider.boards.where((board) {
                final searchQuery = _searchQuery.toLowerCase();
                if (searchQuery.isEmpty) {
                  return true;
                }
                if (_searchType == '제목') {
                  return board.title.toLowerCase().contains(searchQuery);
                } else { // '작성자'
                  return board.authorNickname.toLowerCase().contains(searchQuery);
                }
              }).toList();

              return Column(
                children: [
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Row(
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(horizontal: 12.0),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(8.0),
                          ),
                          child: DropdownButton<String>(
                            value: _searchType,
                            dropdownColor: Colors.white,
                            onChanged: (String? newValue) {
                              setState(() {
                                _searchType = newValue!;
                              });
                            },
                            items: <String>['제목', '작성자']
                                .map<DropdownMenuItem<String>>((String value) {
                              return DropdownMenuItem<String>(
                                value: value,
                                child: Text(value),
                              );
                            }).toList(),
                            underline: Container(), // 밑줄 제거
                          ),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: TextField(
                            controller: _searchController,
                            style: const TextStyle(color: Colors.black), // 입력 텍스트 색상
                            decoration: InputDecoration(
                              hintText: '검색...',
                              hintStyle: const TextStyle(color: Colors.grey), // 힌트 글씨 색상
                              prefixIcon: const Icon(Icons.search, color: Colors.black), // 아이콘 색상
                              border: OutlineInputBorder(
                                borderRadius: BorderRadius.circular(8.0),
                              ),
                              filled: true,
                              fillColor: Colors.white,
                            ),
                          )
                        ),
                        const SizedBox(width: 8),
                        ElevatedButton(
                          onPressed: _performSearch,
                          child: const Text('검색'),
                        ),
                      ],
                    ),
                  ),
                  Expanded(
                    child: ListView.builder(
                      itemCount: filteredBoards.length,
                      itemBuilder: (context, index) {
                        final BoardPost post = filteredBoards[index];
                        return Card(
                          margin: const EdgeInsets.symmetric(
                            horizontal: 8,
                            vertical: 4,
                          ),
                          child: ListTile(
                            title: Text(post.title),
                            subtitle: Text(
                              '등급: ${post.displayGrade} | ${post.authorNickname} | 조회수: ${post.views}',
                            ),
                            trailing: Text(post.createdAt),
                            onTap: () {
                              final args = {
                                'id': post.id?.toString() ?? '', // 게시글 ID를 String으로 변환
                                'category': widget.boardType, // 현재 게시판 타입을 카테고리로 사용
                              };

                              Navigator.pushNamed(
                                context,
                                '/board_detail',
                                arguments: args,
                              );
                            },
                          ),
                        );
                      },
                    ),
                  ),
                ],
              );
            }
          },
        ),
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: <Widget>[
            Consumer<UserInfo>(
              builder: (context, userInfo, child) {
                return DrawerHeader(
                  decoration: const BoxDecoration(
                    color: Colors.white,
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      CircleAvatar(
                        radius: 40, // 원하는 크기로 조절
                        backgroundColor: Colors.grey,
                        child: Icon(FontAwesomeIcons.user, color: Colors.white, size: 40), // 아이콘 크기도 함께 조절
                      ),
                      SizedBox(height: 10), // 아이콘과 텍스트 사이 간격
                      Text(
                        userInfo.username == null || userInfo.username!.isEmpty
                            ? '로그인 해주세요'
                            : '등급 : ${userInfo.displayGrade} 닉네임 : ${userInfo.nickname}'
                        ,style: TextStyle(color: Colors.black),),
                      Text('주소 : ${userInfo.address ?? ''}',style: TextStyle(color: Colors.black)),
                    ],
                  ),
                );
              },
            ),
            ListTile(
              leading: const Icon(Icons.person),
              title: const Text('내 정보 수정'),
              onTap: () {
                Navigator.pop(context); // Drawer 닫기
                Navigator.pushNamed(context, '/profile_edit');
              },
            ),
            ListTile(
              leading: const Icon(Icons.campaign),
              title: const Text('공지게시판'),
              onTap: () {
                Navigator.pop(context); // Drawer 닫기
                Navigator.pushNamed(context, '/board_list', arguments: 'notice');
              },
            ),
            ListTile(
              leading: const Icon(Icons.article),
              title: const Text('자유게시판'),
              onTap: () {
                Navigator.pop(context); // Drawer 닫기
                Navigator.pushNamed(context, '/board_list', arguments: 'free');
              },
            ),
            ListTile(
              leading: const Icon(Icons.fastfood),
              title: const Text('음식게시판'),
              onTap: () {
                Navigator.pop(context); // Drawer 닫기
                Navigator.pushNamed(context, '/board_list', arguments: 'food');
              },
            ),
            ListTile(
              leading: const Icon(Icons.shopping_bag),
              title: const Text('중고게시판'),
              onTap: () {
                Navigator.pop(context); // Drawer 닫기
                Navigator.pushNamed(context, '/board_list', arguments: 'used');
              },
            ),
            ListTile(
              leading: const Icon(Icons.info),
              title: const Text('정보게시판'),
              onTap: () {
                Navigator.pop(context); // Drawer 닫기
                Navigator.pushNamed(context, '/board_list', arguments: 'info');
              },
            ),
            ListTile(
              leading: const Icon(Icons.help),
              title: const Text('질문게시판'),
              onTap: () {
                Navigator.pop(context); // Drawer 닫기
                Navigator.pushNamed(context, '/board_list', arguments: 'qna');
              },
            ),
            ListTile(
              leading: const Icon(Icons.mail),
              title: const Text('쪽지함'),
              onTap: () {
                Navigator.pop(context); // Drawer 닫기
                Navigator.pushNamed(context, '/message_list');
              },
            ),
            ListTile(
              leading: const Icon(Icons.logout),
              title: const Text('로그아웃'),
              onTap: () async {
                Navigator.pop(context); // Drawer 닫기
                // 백엔드 로그아웃 요청
                try {
                  final url = Uri.parse('http://192.168.0.26:8080/api/reissue'); // TODO: 실제 백엔드 로그아웃 엔드포인트로 변경
                  final token = Provider.of<Token>(context, listen: false).accessToken;
                  final response = await http.delete(
                    url,
                    headers: <String, String>{
                      'Content-Type': 'application/json; charset=UTF-8',
                      'Authorization': 'Bearer $token', // 현재 액세스 토큰을 포함하여 보냄
                    },
                  );

                  if (response.statusCode == 200) {
                    print('백엔드 로그아웃 성공');
                  } else {
                    print('백엔드 로그아웃 실패: ${response.statusCode}');
                    print('Error response body: ${utf8.decode(response.bodyBytes)}');
                  }
                } catch (e) {
                  print('백엔드 로그아웃 요청 중 오류 발생: $e');
                }

                // 로컬 토큰 및 사용자 정보 초기화
                Provider.of<Token>(context, listen: false).clear();
                Provider.of<UserInfo>(context, listen: false).clear();
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('로그아웃 되었습니다.')),
                );
                Navigator.pushNamedAndRemoveUntil(context, '/', (route) => false);
              },
            ),
          ],
        ),
      ),
    );
  }
}

