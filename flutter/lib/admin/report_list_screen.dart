import 'package:flutter/material.dart';
import 'package:notfound_flutter/auth/userInfo.dart';
import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:notfound_flutter/auth/token.dart';
import 'package:notfound_flutter/admin/report.dart';

class ReportListScreen extends StatefulWidget {
  const ReportListScreen({super.key});

  @override
  State<ReportListScreen> createState() => _ReportListScreenState();
}

class _ReportListScreenState extends State<ReportListScreen> {
  late Future<List<Report>> _reportListFuture;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    _reportListFuture = _fetchReports();
  }

  Future<List<Report>> _fetchReports() async {
    final token = Provider.of<Token>(context, listen: false).accessToken;
    if (token.isEmpty) {
      throw Exception('인증 토큰이 없습니다. 로그인이 필요합니다.');
    }

    final url = Uri.parse('http://192.168.0.26:8080/api/admin/report-list'); // JS와 동일한 엔드포인트
    try {
      final response = await http.get(
        url,
        headers: {
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        final List<dynamic> responseData = json.decode(utf8.decode(response.bodyBytes));
        // JS 코드와 동일하게 PENDING 상태만 필터링
        return responseData.map((data) => Report.fromJson(data)).where((report) => report.status == 'PENDING').toList();
      } else {
        throw Exception('신고 목록을 불러오는데 실패했습니다: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('신고 목록을 불러오는 중 오류가 발생했습니다: $e');
    }
  }

  Future<void> _handleCancel(int reportId) async {
    final token = Provider.of<Token>(context, listen: false).accessToken;
    final userInfo = Provider.of<UserInfo>(context, listen: false); // UserInfo Provider 사용
    final username = userInfo.username; // 현재 로그인된 사용자 이름 가져오기

    if (username == null || username.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('사용자 정보를 찾을 수 없습니다.')),
      );
      return;
    }

    final url = Uri.parse('http://192.168.0.26:8080/api/user/report/$reportId?username=$username');
    try {
      final response = await http.delete(
        url,
        headers: {
          'Authorization': 'Bearer $token',
        },
      );
      print(response.body);
      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('신고 취소 완료')),
        );
        setState(() {
          _reportListFuture = _fetchReports(); // 목록 새로고침
        });
      } else {
        throw Exception('신고 취소 실패: ${response.statusCode}');
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('신고 취소 중 오류 발생: $e')),
      );
    }
  }

  Future<void> _handleProcessComplete(int reportId, String reporter, String reported) async {
    final token = Provider.of<Token>(context, listen: false).accessToken;
    final userInfo = Provider.of<UserInfo>(context, listen: false); // UserInfo Provider 사용
    final currentUsername = userInfo.username; // 현재 로그인된 사용자 이름 가져오기

    if (currentUsername == null || currentUsername.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('사용자 정보를 찾을 수 없습니다.')),
      );
      return;
    }

    final url = Uri.parse('http://192.168.0.26:8080/api/admin/report');
    final body = jsonEncode({
      'reportId': reportId,
      'status': 'ACCEPTED',
    });

    try {
      final response = await http.patch(
        url,
        body: body,
        headers: {
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('신고 처리 완료')),
        );

        // 신고 처리 완료 메시지 전송 (JS 코드 참고)
        final messageUrl = Uri.parse('http://192.168.0.26:8080/message/send');
        final messageBody = jsonEncode({
          'author': currentUsername,
          'receiver': reporter,
          'title': '신고접수안내',
          'message': '신고하신 내용이 접수되었습니다.',
        });

        await http.post(
          messageUrl,
          headers: {
            'Content-Type': 'application/json; charset=UTF-8',
            'Authorization': 'Bearer $token',
          },
          body: messageBody,
        );

        setState(() {
          _reportListFuture = _fetchReports(); // 목록 새로고침
        });
      } else {
        throw Exception('신고 처리 실패: ${response.statusCode}');
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('신고 처리 중 오류 발생: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('신고 목록'),
      ),
      body: FutureBuilder<List<Report>>(
        future: _reportListFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('${snapshot.error}'));
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(child: Text('신고된 게시글이 없습니다.'));
          } else {
            final reports = snapshot.data!;
            return SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: DataTable(
                columns: const [
                  DataColumn(label: Text('ID',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('신고자',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('피신고자',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('사유',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('상세',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('대상 테이블',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('대상 ID',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('상태',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('생성일',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('처리 시간',style: TextStyle(color: Colors.white),)),
                  DataColumn(label: Text('액션',style: TextStyle(color: Colors.white),)),
                ],
                rows: reports.map((report) {
                  return DataRow(cells: [
                    DataCell(Text(report.id.toString())),
                    DataCell(Text(report.reporter)),
                    DataCell(Text(report.reported)),
                    DataCell(Text(report.reason)),
                    DataCell(Text(report.description ?? '-')),
                    DataCell(Text(report.targetTable ?? '-')),
                    DataCell(Text(report.targetId?.toString() ?? '-')),
                    DataCell(Text(report.status)),
                    DataCell(Text(report.createdAt)),
                    DataCell(Text(report.updatedAt ?? '-')),
                    DataCell(
                      Row(
                        children: [
                          ElevatedButton(
                            onPressed: () => _handleCancel(report.id),
                            child: const Text('탈락'),
                          ),
                          const SizedBox(width: 8),
                          ElevatedButton(
                            onPressed: () => _handleProcessComplete(report.id, report.reporter, report.reported),
                            child: const Text('처리'),
                          ),
                        ],
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