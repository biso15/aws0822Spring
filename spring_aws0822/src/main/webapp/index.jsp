<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>스프링 학습하기</title>
</head>
<body>
<%
	if(session.getAttribute("midx") != null) {
		out.print(session.getAttribute("memberName") + "님 로그인되었습니다. <br><a href='" + request.getContextPath() + "/member/memberLogout.aws'>로그아웃</a><br>");
	}
%>
<%
if(session.getAttribute("midx") == null) {
%>
<a href="<%=request.getContextPath() %>/member/memberJoin.aws">회원 가입 페이지</a><br>
<a href="<%=request.getContextPath() %>/member/memberLogin.aws">회원 로그인 페이지</a><br>
<%
}
%>
<a href="<%=request.getContextPath()%>/member/memberList.aws">회원 목록가기</a><br>
<a href="<%=request.getContextPath()%>/board/boardList.aws">게시판 목록가기</a>
</body>
</html>