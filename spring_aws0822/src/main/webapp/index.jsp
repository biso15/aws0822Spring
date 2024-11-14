<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>스프링 학습하기</title>
</head>
<body>

<c:if test="${!empty sessionScope.midx}">  <%-- <c:if test="${sessionScope.midx != null}"> 과 같다. --%>
	<p>${sessionScope.memberName}님 로그인되었습니다.</p>
	<a href="${pageContext.request.contextPath}/member/memberLogout.aws">로그아웃</a><br>
</c:if>

<c:if test="${empty sessionScope.midx}">
	<a href="${pageContext.request.contextPath}/member/memberJoin.aws">회원 가입 페이지</a><br>
	<a href="${pageContext.request.contextPath}/member/memberLogin.aws">회원 로그인 페이지</a><br>
</c:if>

<a href="${pageContext.request.contextPath}/member/memberList.aws">회원 목록가기</a><br>
<a href="${pageContext.request.contextPath}/board/boardList.aws">게시판 목록가기</a>
</body>
</html>