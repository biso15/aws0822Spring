<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글내용</title>
<link href="${pageContext.request.contextPath}/resources/css/style2.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
// 메세지
const msg = "${requestScope.msg}";
if (msg != null && msg != "") {
    alert(msg);
}

// 파일 다운로드시 썸네일을 다운받게 되므로 파일 이름에서 "s-"를 제거해야함
function checkImageType(fileName) {  // 패턴과 확장자가 일치하는 파일 이름을 리턴받음
	var pattern = /jpg$|gif$|png$|jpeg$/i;  // 자바스크립트 정규표현식
	return fileName.match(pattern);
}

function getOriginalFileName(fileName) {  // 원본 파일 이름 추출	
	var idx = fileName.lastIndexOf("_")+1  // DB에 있는 파일 이름 형식 : /2024/11/08/s-64ca590f-3e9e-4194-80f6-c6645e1f611f_rose.jpg
	return fileName.substr(idx);
}

function getImageLink(fileName) {	
	var front = fileName.substr(0,12);  // 0 ~ 12 전까지 추출. /2024/11/08/
	var end = fileName.substr(14);  // 14부터 끝까지 추출. 64ca590f-3e9e-4194-80f6-c6645e1f611f_rose.jpg
	return front + end;  // 13번 글자만 제외하고 추출
}

function download() {  // 주소 사이에 s-를 뺀 주소를 리턴
	var downloadImageName = getImageLink("${requestScope.bv.filename}");  // 썸네일 이미지 이름으로 원본 이미지 이름 변환
	var downLink = "${pageContext.request.contextPath}/board/displayFile.aws?fileName=" + downloadImageName + "&down=1";
	return downLink;	
}


// 댓글 삭제
function commentDel(cidx) {
	
	let ans = confirm("삭제하시겠습니까?");
	
	if(ans == true) {
		$.ajax({
			type: "get",  // 전송방식
			url: "${pageContext.request.contextPath}/comment/" + cidx + "/commentDeleteAction.aws",
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
// 댓글 리스트 새로고침
$.boardCommentList = function() {

	let block = $("#block").val();
	
	$.ajax({
		type: "get",  // 전송방식
		<%-- url: "${pageContext.request.contextPath}/comment/commentList.aws?bidx=${requestScope.bv.getBidx()%>", --%>
		url: "${pageContext.request.contextPath}/comment/${requestScope.bv.bidx}/" +block+ "/commentList.aws",  /* RestFul 방식(Rest api 사용) */
		dataType: "json",  // 받는 형식. json 타입은 문서에서 {"key값": "value값", "key값" : "value값"} 형식으로 구성
		success: function(result) {  // 결과가 넘어와서 성공했을 때 받는 영역
			// alert("전송성공 테스트");			
			
			if(result.moreView == "N") {
				$("#moreBtn").css("display", "none");
				$("#blockPre").val($("#block").val());
			} else {
				$("#moreBtn").css("display", "block");
				$("#blockPre").val($("#block").val() - 1);
			}
			
			let nextBlock = result.nextBlock;	
			$("#block").val(nextBlock);
			
			var str = "<table class='replyTable'><tr><th>번호</th><th>작성자</th><th>내용</th><th>날짜</th><th>DEL</th></tr>";

			var strTr = "";
			var index = result.length;
			
			$(result.clist).each(function(index) {
				var btnn = "";
				if (this.midx == "${sessionScope.midx}") {
					if(this.delyn == "N") {
						btnn = "<button type='button' class='btn' onclick='commentDel(" + this.cidx + ")'>삭제</button>";
					}
				}
				
				var index = index + 1;
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


$(document).ready(function() {
	
	// 파일 이름 변경
	$("#dUrl").html(getOriginalFileName("${requestScope.bv.filename}") + " 다운받기");

	// 원본 파일 다운로드
	$("#dUrl").click(function() {
		$("#dUrl").attr("href", download());
		return;
	})

	$.boardCommentList();
	
	// 추천수 업데이트
 	$("#btn").click(function() {
		// alert("추천버튼 클릭");
		
		$.ajax({
			type: "get",  // bidx를 보내야 함
			url: "${pageContext.request.contextPath}/board/boardRecom.aws?bidx=${requestScope.bv.bidx}>",
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
	
	 
	 // 댓글 작성
 	$("#cmtBtn").click(function() {
  		
		let midx = "${sessionScope.midx}";
		if(midx == "" || midx == null || midx == "null" || midx == 0) {
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
			url: "${pageContext.request.contextPath}/comment/commentWriteAction.aws",
			dataType: "json",  // 받는 형식. json 타입은 문서에서 {"key값": "value값", "key값" : "value값"} 형식으로 구성
			data: {"cwriter": cwriter, "ccontents": ccontents, "bidx": "${requestScope.bv.bidx}", "midx": "${sessionScope.midx}"},
			success: function(result) {  // 결과가 넘어와서 성공했을 때 받는 영역
				
				// alert("전송성공 테스트");
				if(result.value == 1) {
					$("#ccontents").val("");
				}
				
				// 페이지 증가 상쇄
				if($("#blockPre").val() < $("#block").val()) {
					$("#block").val($("#block").val() - 1);
				}
				
				$.boardCommentList();
				
			},
		    error: function(xhr, status, error) {  // 결과가 실패했을 때 받는 영역
				alert("전송실패 테스트");
			    /* console.log("Error Status: " + status);
			    console.log("Error Detail: " + error);
			    console.log("Response: " + xhr.responseText);  */
			}
		});
	})
	
	 // 더보기
 	$("#more").click(function() {
 		$.boardCommentList();
 	});

});

</script>
</head>
<body>
<header>
	<h2 class="mainTitle">글내용</h2>
</header>

<article class="detailContents">
	<div class="detailTitle">
		<h2 class="contentTitle">${requestScope.bv.subject} (조회수:${requestScope.bv.viewcnt})</h2>		
		<input type="button" id="btn" value="추천(${requestScope.bv.recom})" class="btn">
	</div>
	<p class="write">${requestScope.bv.writer} (${requestScope.bv.writeday})</p>
	
	<div class="content">
		${requestScope.bv.contents}
	</div>
	
	<c:if test="${!empty requestScope.bv.filename}">
	<img src="${pageContext.request.contextPath}/board/displayFile.aws?fileName=${requestScope.bv.filename}" class="fileImage">  <!-- down의 값을 넘기지 않으면 기본값이 0이므로 이미지인 경우 미리보기로 나타남 -->
	<p><a href="#" id="dUrl" class="fileDown">첨부파일다운로드</a></p>
	</c:if>
</article>
	
<div class="btnBox">
	<a class="btn aBtn" href="${pageContext.request.contextPath}/board/boardModify.aws?bidx=${requestScope.bv.bidx}">수정</a>
	<a class="btn aBtn" href="${pageContext.request.contextPath}/board/boardDelete.aws?bidx=${requestScope.bv.bidx}">삭제</a>
	<a class="btn aBtn" href="${pageContext.request.contextPath}/board/boardReply.aws?bidx=${requestScope.bv.bidx}">답변</a>
	<button type="button" class="btn" onclick="history.back();">목록</button>
</div>

<article class="commentContents">
	<form name="frm">
		<input type="text" name="cwriter" id="cwriter" class="commentWriter" value="${sessionScope.memberName}" readonly="readonly">
		<input type="text" name="ccontents" id="ccontents">
		<button type="button" class="replyBtn" id="cmtBtn">댓글쓰기</button>
	</form>
	
	<div id="commentListView"></div>
	
	<input type="hidden" id="block" value="1">
	<input type="hidden" id="blockPre" value="1">
	<div id="moreBtn">
		<button type="button" id="more" class="btn moreBtn">더보기</button>
	</div>
</article>

</body>
</html>