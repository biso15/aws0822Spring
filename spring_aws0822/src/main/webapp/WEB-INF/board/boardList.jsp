<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글목록</title>
<link href="${pageContext.request.contextPath}/resources/css/style2.css" type="text/css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
// 메세지
const msg = "${requestScope.msg}";
if (msg != null && msg != "") {
 alert(msg);
}
</script>
</head>
<body>
<header>
	<h2 class="mainTitle">글목록</h2>
	<form class="search" name="frm" action="${pageContext.request.contextPath}/board/boardList.aws" method="get">
		<select name="searchType">
			<option value="subject">제목</option>
			<option value="writer">작성자</option>
		</select>
		<input type="text" name="keyword">
		<button type="submit" class="btn">검색</button>
	</form>
</header>

<section>	
	<table class="listTable">
		<colgroup>
			<col width="8%">
			<col>
			<col width="10%">
			<col width="8%">
			<col width="8%">
			<col width="18%">
		</colgroup>
		<tr>
			<th>No</th>
			<th>제목</th>
			<th>작성자</th>
			<th>조회</th>
			<th>추천</th>
			<th>날짜</th>
		</tr>
		<c:forEach items="${requestScope.blist}" var="bv" varStatus="status">  <!-- status.index는 0부터 시작 -->
		<tr>
			<td>${requestScope.pm.totalCount - ((status.index) + (requestScope.pm.scri.page-1) * requestScope.pm.scri.perPageNum)}</td>
			<td class="title"><a href="${pageContext.request.contextPath}/board/boardContents.aws?bidx=${bv.bidx}">
				<c:forEach var="i" begin="1" end="${bv.level_}" step="1">
					<c:if test="${i > 1}">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:if>
					<c:if test="${i == bv.level_}">
						└ &nbsp;
					</c:if>
				</c:forEach>
				${bv.subject}
			</a></td>
			<td>${bv.writer}</td>
			<td class="viewcnt">${bv.viewcnt}</td>
			<td class="recom">${bv.recom}</td>
			<td>${bv.writeday}</td>
		</tr>
		</c:forEach>		
	</table>
	
	<div class="btnBox">
		<a class="btn aBtn" href="${pageContext.request.contextPath}/board/boardWrite.aws">글쓰기</a>
	</div>
	
	<div class="page">
		<c:set var="queryParam" value="keyword=${requestScope.pm.scri.keyword}&searchType=${requestScope.pm.scri.searchType}"></c:set>	
 		<ul>
 		<c:if test="${requestScope.pm.prev == true}">
			<li><a href="${pageContext.request.contextPath}/board/boardList.aws?page=${requestScope.pm.startPage - 1}&${queryParam}">◀</a></li>
		</c:if>
			
		<c:forEach var="i" begin="${requestScope.pm.startPage}" end="${requestScope.pm.endPage}" step="1">
			<li<c:if test="${i == requestScope.pm.scri.page}"> class="on"</c:if>>
			<a href="${pageContext.request.contextPath}/board/boardList.aws?page=${i}&${queryParam}">${i}</a></li>
		</c:forEach>
		
 		<c:if test="${requestScope.pm.next == true && requestScope.pm.endPage > 0}">  <!-- && requestScope.pm.endPage > 0 : 게시물이 0개일 경우 endPage가 0이 됨. 이때는 버튼이 없어야 함 -->
			<li><a href="${pageContext.request.contextPath}/board/boardList.aws?page=${requestScope.pm.endPage + 1}&${queryParam}">▶</a></li>
		</c:if>
		</ul>
	</div>
</section>

</body>
</html>