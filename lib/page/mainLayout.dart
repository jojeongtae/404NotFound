import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:notfound_flutter/auth/token.dart';
import 'package:notfound_flutter/auth/userInfo.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class MainLayout extends StatefulWidget {
  const MainLayout({super.key});

  @override
  State<MainLayout> createState() => _MainLayoutState();
}

class _MainLayoutState extends State<MainLayout> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("404NotFound"),
        centerTitle: true,
        leading: Builder(
          builder: (BuildContext context) {
            return IconButton(
              icon: const Icon(Icons.menu),
              onPressed: () {
                Scaffold.of(context).openDrawer();
              },
            );
          },
        ),
        actions: [
          Consumer<Token>(
            builder: (context, token, child) {
              if (token.accessToken.isEmpty) {
                return TextButton(
                  onPressed: () {
                    Navigator.pushNamed(context, '/login');
                  },
                  child: const Text(
                    "로그인",
                    style: TextStyle(color: Colors.white),
                  ),
                );
              } else {
                return const SizedBox.shrink();
              }
            },
          ),
        ],
      ),
      drawer: Drawer(
        backgroundColor: Colors.grey[900], // 전체 배경 회색
        child: SafeArea(
          child: Column(
            children: [
              // 윗부분 흰색 프로필 영역
              Consumer<UserInfo>(
                builder: (context, userInfo, child) {
                  return Container(
                    width: double.infinity,
                    color: Colors.white,
                    padding: const EdgeInsets.symmetric(vertical: 20),
                    child: Column(
                      children: [
                        CircleAvatar(
                          radius: 40,
                          backgroundColor: Colors.grey,
                          child: const Icon(
                            FontAwesomeIcons.user,
                            color: Colors.white,
                            size: 40,
                          ),
                        ),
                        const SizedBox(height: 10),
                        Text(
                          userInfo.username == null || userInfo.username!.isEmpty
                              ? '로그인 해주세요'
                              : '등급 : ${userInfo.displayGrade} 닉네임 : ${userInfo.nickname ?? ''}',
                          style: const TextStyle(color: Colors.black),
                        ),
                        Text(
                          userInfo.username == null || userInfo.username!.isEmpty
                              ? ""
                              : '주소 : ${userInfo.address ?? ''}',
                          style: const TextStyle(color: Colors.black),
                        ),
                      ],
                    ),
                  );
                },
              ),

              const SizedBox(height: 10),

              // 메뉴 영역
              Expanded(
                child: Consumer<UserInfo>(
                  builder: (context, userInfo, child) {
                    print('현재 role: ${userInfo.role}'); // 디버그 확인
                    return ListView(
                      children: [
                        ListTile(
                          leading: const Icon(Icons.person, color: Colors.white),
                          title: Text(
                            userInfo.role != null && userInfo.role == 'ROLE_ADMIN'
                                ? '유저 정보 조회'
                                : '내 정보 수정',
                            style: const TextStyle(color: Colors.white),
                          ),
                          onTap: () {
                            Navigator.pop(context);
                            if (userInfo.role != null &&
                                userInfo.role == 'ROLE_ADMIN') {
                              Navigator.pushNamed(context, '/user_list');
                            } else {
                              Navigator.pushNamed(context, '/profile_edit');
                            }
                          },
                        ),
                        if (userInfo.role == 'ROLE_ADMIN')
                          ListTile(
                            leading: const Icon(Icons.report, color: Colors.white),
                            title: const Text('신고 목록 조회',
                                style: TextStyle(color: Colors.white)),
                            onTap: () {
                              Navigator.pop(context);
                              Navigator.pushNamed(context, '/report_list');
                            },
                          ),
                        ListTile(
                          leading: const Icon(Icons.campaign, color: Colors.white),
                          title: const Text('공지게시판',
                              style: TextStyle(color: Colors.white)),
                          onTap: () {
                            Navigator.pop(context);
                            Navigator.pushNamed(
                              context,
                              '/board_list',
                              arguments: 'notice',
                            );
                          },
                        ),
                        ListTile(
                          leading: const Icon(Icons.article, color: Colors.white),
                          title: const Text('자유게시판',
                              style: TextStyle(color: Colors.white)),
                          onTap: () {
                            Navigator.pop(context);
                            Navigator.pushNamed(
                              context,
                              '/board_list',
                              arguments: 'free',
                            );
                          },
                        ),
                        ListTile(
                          leading: const Icon(Icons.fastfood, color: Colors.white),
                          title: const Text('음식게시판',
                              style: TextStyle(color: Colors.white)),
                          onTap: () {
                            Navigator.pop(context);
                            Navigator.pushNamed(
                              context,
                              '/board_list',
                              arguments: 'food',
                            );
                          },
                        ),
                        ListTile(
                          leading:
                          const Icon(Icons.shopping_bag, color: Colors.white),
                          title: const Text('중고게시판',
                              style: TextStyle(color: Colors.white)),
                          onTap: () {
                            Navigator.pop(context);
                            Navigator.pushNamed(
                              context,
                              '/board_list',
                              arguments: 'used',
                            );
                          },
                        ),
                        ListTile(
                          leading: const Icon(Icons.info, color: Colors.white),
                          title: const Text('정보게시판',
                              style: TextStyle(color: Colors.white)),
                          onTap: () {
                            Navigator.pop(context);
                            Navigator.pushNamed(
                              context,
                              '/board_list',
                              arguments: 'info',
                            );
                          },
                        ),
                        ListTile(
                          leading: const Icon(Icons.help, color: Colors.white),
                          title: const Text('질문게시판',
                              style: TextStyle(color: Colors.white)),
                          onTap: () {
                            Navigator.pop(context);
                            Navigator.pushNamed(
                              context,
                              '/board_list',
                              arguments: 'qna',
                            );
                          },
                        ),
                        ListTile(
                          leading: const Icon(Icons.mail, color: Colors.white),
                          title: const Text('쪽지함',
                              style: TextStyle(color: Colors.white)),
                          onTap: () {
                            Navigator.pop(context);
                            Navigator.pushNamed(context, '/message_list');
                          },
                        ),
                        ListTile(
                          leading: const Icon(Icons.logout, color: Colors.white),
                          title: const Text('로그아웃',
                              style: TextStyle(color: Colors.white)),
                          onTap: () async {
                            Navigator.pop(context);

                            try {
                              final url = Uri.parse(
                                'http://192.168.0.26:8080/api/reissue',
                              );
                              final token = Provider.of<Token>(
                                context,
                                listen: false,
                              ).accessToken;
                              final response = await http.delete(
                                url,
                                headers: <String, String>{
                                  'Content-Type':
                                  'application/json; charset=UTF-8',
                                  'Authorization': 'Bearer $token',
                                },
                              );

                              if (response.statusCode == 200) {
                                print('백엔드 로그아웃 성공');
                              } else {
                                print('백엔드 로그아웃 실패: ${response.statusCode}');
                              }
                            } catch (e) {
                              print('백엔드 로그아웃 요청 중 오류 발생: $e');
                            }

                            Provider.of<Token>(context, listen: false).clear();
                            Provider.of<UserInfo>(context, listen: false).clear();
                            ScaffoldMessenger.of(context).showSnackBar(
                              const SnackBar(content: Text('로그아웃 되었습니다.')),
                            );
                            Navigator.pushNamedAndRemoveUntil(
                              context,
                              '/',
                                  (route) => false,
                            );
                          },
                        ),
                      ],
                    );
                  },
                ),
              ),
            ],
          ),
        ),
      ),
      body: FutureBuilder(
        future: Future.wait([
          _fetchRanking('comments'),
          _fetchRanking('recommend'),
        ]),
        builder: (context, AsyncSnapshot<List<dynamic>> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('오류 발생: ${snapshot.error}'));
          } else if (snapshot.hasData) {
            final List<dynamic> commentsRanking = snapshot.data![0];
            final List<dynamic> recommendRanking = snapshot.data![1];

            return SingleChildScrollView(
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Text(
                      '댓글 랭킹 (Top 5)',
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 10),
                    commentsRanking.isEmpty
                        ? const Text('댓글 랭킹이 없습니다.')
                        : ListView.builder(
                            shrinkWrap: true,
                            physics: const NeverScrollableScrollPhysics(),
                            itemCount: commentsRanking.length,
                            itemBuilder: (context, index) {
                              final item = commentsRanking[index];
                              final String category = item['category'] ?? '기타';
                              final String displayCategory =
                                  _getCategoryDisplayName(category);
                              return ListTile(
                                leading: Text('${index + 1}.'),
                                title: Text(
                                  '[${displayCategory}] ${item['title'] ?? '제목 없음'}',
                                ),
                                subtitle: Text(
                                  '등급: ${_getGradeDisplayName(item['grade'] ?? '')} 닉네임: ${item['authorNickname'] ?? '알 수 없음'} | 댓글 수: ${item['count'] ?? 0}',
                                ),
                                onTap: () {
                                  Navigator.pushNamed(
                                    context,
                                    '/board_detail', // 게시글 상세 화면 라우트 이름
                                    arguments: {
                                      'id':
                                          item['id']?.toString() ??
                                          '', // 게시글 ID
                                      'category': category ?? '', // 게시글 카테고리
                                    },
                                  );
                                },
                              );
                            },
                          ),
                    const SizedBox(height: 30),
                    const Text(
                      '추천 랭킹 (Top 5)',
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 10),
                    recommendRanking.isEmpty
                        ? const Text('추천 랭킹이 없습니다.')
                        : ListView.builder(
                            shrinkWrap: true,
                            physics: const NeverScrollableScrollPhysics(),
                            itemCount: recommendRanking.length,
                            itemBuilder: (context, index) {
                              final item = recommendRanking[index];
                              final String category = item['category'] ?? '기타';
                              final String displayCategory =
                                  _getCategoryDisplayName(category);
                              return ListTile(
                                leading: Text('${index + 1}.'),
                                title: Text(
                                  '[${displayCategory}] ${item['title'] ?? '제목 없음'}',
                                ),
                                subtitle: Text(
                                  '등급: ${_getGradeDisplayName(item['grade'] ?? '')} 닉네임: ${item['authorNickname'] ?? '알 수 없음'} | 추천 수: ${item['count'] ?? 0}',
                                ),
                                onTap: () {
                                  Navigator.pushNamed(
                                    context,
                                    '/board_detail', // 게시글 상세 화면 라우트 이름
                                    arguments: {
                                      'id':
                                          item['id']?.toString() ??
                                          '', // 게시글 ID
                                      'category': category ?? '', // 게시글 카테고리
                                    },
                                  );
                                },
                              );
                            },
                          ),
                  ],
                ),
              ),
            );
          }
          return const Center(child: Text('데이터 없음'));
        },
      ),
    );
  }

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

  String _getGradeDisplayName(String grade) {
    switch (grade) {
      case '500 Internal Server Error (운영진)':
        return '👑 500';
      case '404 Not Found (신규)':
        return '🐣 404';
      case '200 OK (일반 회원)':
        return '👍 200';
      case '202 Accepted (활동 회원)':
        return '🚀 202';
      case '403 Forbidden (우수 회원)':
        return '💎 403';
      case '401 Unauthorized (손님)':
        return '👻 401';
      default:
        return '등급 없음';
    }
  }

  Future<List<dynamic>> _fetchRanking(String type) async {
    final url = Uri.parse(
      'http://192.168.0.26:8080/api/ranking/$type',
    ); // TODO: 실제 백엔드 IP로 변경
    try {
      final response = await http.get(url);
      if (response.statusCode == 200) {
        final jsonResponse = jsonDecode(utf8.decode(response.bodyBytes));
        return jsonResponse;
      } else {
        throw Exception('Failed to load ranking: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('랭킹 데이터를 가져오지 못했습니다.');
    }
  }
}
