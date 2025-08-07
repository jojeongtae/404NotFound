import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:notfound_flutter/auth/token.dart';

// 확장된 사용자 모델
class User {
  final String username;
  final String nickname;
  final String? grade;
  final String? phone;
  final int? point;
  final String? status;
  final String? address;
  final int? warning;
  final String role;

  User({
    required this.username,
    required this.nickname,
    this.grade,
    this.phone,
    this.point,
    this.status,
    this.address,
    this.warning,
    required this.role,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      username: json['username'] ?? 'N/A',
      nickname: json['nickname'] ?? 'N/A',
      grade: json['grade'],
      phone: json['phone'],
      point: json['point'],
      status: json['status'],
      address: json['address'],
      warning: json['warning'],
      role: json['role'] ?? 'N/A',
    );
  }
}

class UserListScreen extends StatefulWidget {
  const UserListScreen({super.key});

  @override
  State<UserListScreen> createState() => _UserListScreenState();
}

class _UserListScreenState extends State<UserListScreen> {
  late Future<List<User>> _userListFuture;

  @override
  void initState() {
    super.initState();
    // initState에서 context를 사용할 수 없으므로, Future를 즉시 할당하지 않습니다.
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    // Provider를 사용하기 위해 didChangeDependencies에서 Future를 초기화합니다.
    _userListFuture = _fetchUsers();
  }

  Future<List<User>> _fetchUsers() async {
    // Provider를 통해 토큰을 가져옵니다.
    final token = Provider.of<Token>(context, listen: false).accessToken;
    if (token.isEmpty) {
      // 사용자가 로그인하지 않은 경우를 처리합니다.
      throw Exception('인증 토큰이 없습니다. 로그인이 필요합니다.');
    }

    final url = Uri.parse('http://192.168.0.26:8080/api/admin/users');
    try {
      final response = await http.get(
        url,
        headers: {
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        final List<dynamic> responseData = json.decode(utf8.decode(response.bodyBytes));
        print('Server Response: $responseData'); // 서버 응답 출력
        return responseData.map((data) => User.fromJson(data)).toList();
      } else {
        // API 오류 응답을 처리합니다.
        throw Exception('사용자 목록을 불러오는데 실패했습니다: ${response.statusCode}');
      }
    } catch (e) {
      // 네트워크 오류 또는 기타 예외를 처리합니다.
      throw Exception('사용자 목록을 불러오는 중 오류가 발생했습니다: $e');
    }
  }

  // 상태 변경을 위한 API 호출
  Future<void> _updateUserStatus(String username, String status) async {
    final token = Provider.of<Token>(context, listen: false).accessToken;
    final url = Uri.parse('http://192.168.0.26:8080/api/admin/user-status/$username?status=$status');

    try {
      final response = await http.patch(
        url,
        headers: {
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('사용자 상태가 성공적으로 변경되었습니다.')),
        );
        // 상태 변경 후 목록을 새로고침합니다.
        setState(() {
          _userListFuture = _fetchUsers();
        });
      } else {
        throw Exception('상태 변경 실패: ${response.statusCode}');
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('상태 변경 중 오류 발생: $e')),
      );
    }
  }

  // 상태 변경 다이얼로그 표시
  void _showStatusChangeDialog(User user) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return SimpleDialog(
          title: Text('${user.nickname}님의 상태 변경'),
          children: <String>['ACTIVE', 'SUSPENDED', 'BANNED', 'INACTIVE']
              .map((String status) {
            return SimpleDialogOption(
              onPressed: () {
                Navigator.pop(context);
                _updateUserStatus(user.username, status);
              },
              child: Text(status),
            );
          }).toList(),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('관리자 - 유저 목록'),
      ),
      body: FutureBuilder<List<User>>(
        future: _userListFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('${snapshot.error}'));
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(child: Text('등록된 유저가 없습니다.'));
          } else {
            final users = snapshot.data!;
            return SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: DataTable(
                columns: const [
                  DataColumn(label: Text('아이디',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('닉네임',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('등급',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('전화번호',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('포인트',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('상태',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('주소',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('경고',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('상태 변경',style: TextStyle(color: Colors.white),)),
                ],
                rows: users.map((user) {
                  return DataRow(cells: [
                    DataCell(Text(user.username)),
                    DataCell(Text(user.nickname)),
                    DataCell(Text(user.grade ?? '-')),
                    DataCell(Text(user.phone ?? '-')),
                    DataCell(Text(user.point?.toString() ?? '-')),
                    DataCell(Text(user.status ?? '-')),
                    DataCell(Text(user.address ?? '-')),
                    DataCell(Text(user.warning?.toString() ?? '-')),
                    DataCell(
                      ElevatedButton(
                        onPressed: () => _showStatusChangeDialog(user),
                        child: const Text('변경'),
                      ),
                    ),
                  ]);
                }).toList(),
              ),
            );
          }
        },
      ),
    );
  }
}
