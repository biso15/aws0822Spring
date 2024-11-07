<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.myaws.myapp.domain.*" %>
    
<%

ArrayList<BoardVo> blist = (ArrayList<BoardVo>)request.getAttribute("blist");  // Object 타입이므로 ArrayList<BoardVo> 타입으로 형변환 한다.
// System.out.println("blist ==> " + blist);

PageMaker pm = (PageMaker)request.getAttribute("pm");

// 게시글 번호
int totalCount = pm.getTotalCount();

// 검색
String keyword = pm.getScri().getKeyword();
String searchType = pm.getScri().getSearchType();

String param = "keyword=" + keyword + "&searchType=" + searchType;

// 메세지
String msg = "";

if(request.getAttribute("msg") != null) {
	msg = (String)request.getAttribute("msg");
  	out.println("<script>alert('"+msg+"');</script>");
  }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글목록</title>
<link href="/resources/css/style2.css" type="text/css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
<header>
	<h2 class="mainTitle">글목록</h2>
	<form class="search" name="frm" action="<%=request.getContextPath()%>/board/boardList.aws" method="get">
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
		<% 
 		int num = totalCount - (pm.getScri().getPage() - 1) * pm.getScri().getPerPageNum();
		for(BoardVo bv : blist) {
			String lvlStr = "";
			for(int i = 1; i <= bv.getLevel_(); i++) {
				if (i > 1) {
					lvlStr += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";			
				}
				
				if (i == bv.getLevel_()) {
					lvlStr += "└ &nbsp;";
				}
			}
		%>
		<tr>
			<td><%=num%></td>
			<td class="title"><a href="<%=request.getContextPath()%>/board/boardContents.aws?bidx=<%=bv.getBidx()%>"><%=lvlStr%><%=bv.getSubject()%></a></td>
			<td><%=bv.getWriter()%></td>
			<td class="viewcnt"><%=bv.getViewcnt()%></td>
			<td class="recom"><%=bv.getRecom()%></td>
			<td><%=bv.getWriteday()%></td>
		</tr>
 		<% 
 			num --;
		}
		%>
	</table>
	
	<div class="btnBox">
		<a class="btn aBtn" href="<%=request.getContextPath()%>/board/boardWrite.aws">글쓰기</a>
	</div>
	
	<div class="page">
 		<ul>
		<% if(pm.isPrev() == true) { %>
			<li><a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getStartPage() - 1%>&<%=param%>">◀</a></li>
		<% } %>
		
		<% for(int i = pm.getStartPage(); i <= pm.getEndPage(); i++) { %>
			<li <% if(i == pm.getScri().getPage()) { %>class="on"<% } %>><a href="<%= request.getContextPath() %>/board/boardList.aws?page=<%=i%>&<%=param%>"><%= i %></a></li>
		<% } %>
		
		<% if(pm.isNext() == true && pm.getEndPage() > 0) { %>  <!-- && pm.getEndPage() > 0 : 게시물이 0개일 경우 endPage가 0이 됨. 이때는 버튼이 없어야 함 -->
			<li><a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getEndPage() + 1%>&<%=param%>">▶</a></li>
		<% } %>
		</ul>
	</div>
</section>

</body>
</html>