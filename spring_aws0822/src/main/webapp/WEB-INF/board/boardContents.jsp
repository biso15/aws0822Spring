<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.myaws.myapp.domain.BoardVo"%>

<%
	BoardVo bv = (BoardVo)request.getAttribute("bv");  // 강제형변환. 양쪽 형을 맞춰준다.
	
	String memberName = "";
	if(session.getAttribute("memberName") != null) {
		memberName = (String)session.getAttribute("memberName");
	}
	  // 현재 로그인 사람과 댓글쓴 사람의 번호가 같을때만 버튼이 나타남
	int midx = 0;
	if(session.getAttribute("midx") != null) {
		midx = (int)session.getAttribute("midx");
	}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글내용</title>
<link href="/resources/css/style2.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script> 
function commentDel(cidx) {
	
	let ans = confirm("삭제하시겠습니까?");
	
	if(ans == true) {
		$.ajax({
			type: "get",  // 전송방식
			url: "<%=request.getContextPath()%>/comment/commentDeleteAction.aws?cidx="+cidx,
			dataType: "json",
			success: function(result) {
				// alert("전송성공");
				
				$.boardCommentList();
			},
			error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
				alert("전송실패");
			}
		});
	}
	
	return;
}



//jquery로 만드는 함수
 $.boardCommentList = function() {
	$.ajax({
		type: "get",  // 전송방식
		url: "<%=request.getContextPath()%>/comment/commentList.aws?bidx=<%=bv.getBidx()%>",
		dataType: "json",  // 받는 형식. json 타입은 문서에서 {"key값": "value값", "key값" : "value값"} 형식으로 구성
		success: function(result) {  // 결과가 넘어와서 성공했을 때 받는 영역
			// alert("전송성공 테스트");			
			
			var str = "<table class='replyTable'><tr><th>번호</th><th>작성자</th><th>내용</th><th>날짜</th><th>DEL</th></tr>";

			var strTr = "";
			var index = result.length;
			
			$(result).each(function() {
				
				var btnn = "";
				if (this.midx == "<%=midx%>") {
					if(this.delyn == "N") {
						btnn = "<button type='button' class='btn' onclick='commentDel(" + this.cidx + ")'>삭제</button>";
					}
				}
				
				strTr += "<tr><td class='cidx'>" + index-- + "</td>" + 
						"<td class='cwriter'>" + this.cwriter + "</td>" + 
						"<td class='ccontents'>" + this.ccontents + "</td>" + 
						"<td class='writeday'>" + this.writeday + "</td>" + 
						"<td class='delyn'>" + btnn + "</td></tr>";
			});
			
			str = str + strTr + "</table>";
			
			$("#commentListView").html(str);
		},
	    error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
			alert("전송실패 테스트");
		    /* console.log("Error Status: " + status);
		    console.log("Error Detail: " + error);
		    console.log("Response: " + xhr.responseText); */
		}
	});
}

//추천수 업데이트
$(document).ready(function() {

	$.boardCommentList();
	
 	$("#btn").click(function() {
		// alert("추천버튼 클릭");
		
		$.ajax({
			type: "get",  // bidx를 보내야 함
			url: "<%=request.getContextPath()%>/board/boardRecom.aws?bidx=<%=bv.getBidx()%>",
			dataType: "json",
			// data: {"bidx": bidx},  // get 방식으로 parameter로 넘긴다
			success: function(result) {
				// alert("전송성공");
				
				var str = "추천(" + result.recom + ")";
				$("#btn").val(str);
			},
			error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
				// alert("전송실패");
			}
		});
	 })
	
 	$("#cmtBtn").click(function() {
  		
		let loginCheck = "<%=midx%>";
		if(loginCheck == "" || loginCheck == null || loginCheck == "null" || loginCheck == 0) {
			alert("로그인을 해주세요");
			return;
		}
		
		let cwriter = $("#cwriter").val();
		let ccontents = $("#ccontents").val();
			  
		if (cwriter == "") {  // 페이지 접속시 로그인 체크를 해서 해당되는 경우는 거의 없지만, 혹시라도 우회해서 접속할 경우 거르기 위해 사용
			alert("로그인을 해주세요");
			$("#cwriter").focus();
			return;
			 
		} else if (ccontents == "") {
			alert("내용을 입력해주세요");
			$("#ccontents").focus();
			return;
		}
		
		$.ajax({
			type: "post",  // 전송방식
			url: "<%=request.getContextPath()%>/comment/commentWriteAction.aws",
			dataType: "json",  // 받는 형식. json 타입은 문서에서 {"key값": "value값", "key값" : "value값"} 형식으로 구성
			data: {"cwriter": cwriter, "ccontents": ccontents, "bidx": <%=bv.getBidx()%>, "midx": <%=midx%> },
			success: function(result) {  // 결과가 넘어와서 성공했을 때 받는 영역
				// alert("전송성공 테스트");
				$.boardCommentList();
				if(result.value == 1) {
					$("#ccontents").val("");
				}
			},
		    error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
				alert("전송실패 테스트");
			   /*  console.log("Error Status: " + status);
			    console.log("Error Detail: " + error);
			    console.log("Response: " + xhr.responseText); */
			}
		});
	})

});

</script>
</head>
<body>
<header>
	<h2 class="mainTitle">글내용</h2>
</header>

<article class="detailContents">
	<div class="detailTitle">
		<h2 class="contentTitle"><%=bv.getSubject()%> (조회수:<%=bv.getViewcnt()%>)</h2>		
		<input type="button" id="btn" value="추천(<%=bv.getRecom()%>)" class="btn">
	</div>
	<p class="write"><%=bv.getWriter()%> (<%=bv.getWriteday()%>)</p>
	
	<div class="content">
		<%=bv.getContents()%>
	</div>
	<% if(bv.getUploadedFilename() == null || bv.getUploadedFilename().equals("")) { } else { %>
	<img src="<%=request.getContextPath()%>/image/<%=bv.getUploadedFilename()%>" class="fileImage">
	<p><a href="<%=request.getContextPath()%>/board/boardDownload.aws?filename=<%=bv.getUploadedFilename()%>" class="fileDown">첨부파일다운로드</a></p>
	<% } %>
</article>
	
<div class="btnBox">
	<a class="btn aBtn" href="<%=request.getContextPath()%>/board/boardModify.aws?bidx=<%=bv.getBidx()%>">수정</a>
	<a class="btn aBtn" href="<%=request.getContextPath()%>/board/boardDelete.aws?bidx=<%=bv.getBidx()%>">삭제</a>
	<a class="btn aBtn" href="<%=request.getContextPath()%>/board/boardReply.aws?bidx=<%=bv.getBidx()%>">답변</a>
	<button type="button" class="btn" onclick="history.back();">목록</button>
</div>

<article class="commentContents">
	<form name="frm">
		<input type="text" name="cwriter" id="cwriter" class="commentWriter" value="<%-- <%=memberName%> --%>" readonly="readonly">
		<input type="text" name="ccontents" id="ccontents">
		<button type="button" class="replyBtn" id="cmtBtn">댓글쓰기</button>
	</form>
	
	<div id="commentListView"></div>	
<!-- 	<table class="replyTable">
		<tr>
			<th>번호</th>
			<th>작성자</th>
			<th>내용</th>
			<th>날짜</th>
			<th>DEL</th>
		</tr>
		<tr>
			<td class="cidx">1</td>
			<td class="cwriter">홍길동</td>
			<td class="ccontents">댓글입니다</td>
			<td class="writeday">2024-10-18</td>
			<td>sss</td>
		</tr>
	</table> -->
</article>

</body>
</html>