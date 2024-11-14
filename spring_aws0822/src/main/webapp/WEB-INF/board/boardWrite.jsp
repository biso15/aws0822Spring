<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<link href="${pageContext.request.contextPath}/resources/css/style2.css" type="text/css" rel="stylesheet">
<script>
// 메세지
const msg = "${requestScope.msg}";
if (msg != null && msg != "") {
 alert(msg);
}

function check() {
	  
	  // 유효성 검사하기
	  let fm = document.frm;  // 문자객체 안에 form 객체 생성하기
	  
	  if (fm.subject.value == "") {
		  alert("제목을 입력해주세요");
		  fm.subject.focus();
		  return;
	  } else if (fm.contents.value == "") {
		  alert("내용을 입력해주세요");
		  fm.contents.focus();
		  return;
	  } else if (fm.writer.value == "") {
		  alert("작성자를 입력해주세요");
		  fm.writer.focus();
		  return;
	  } else if (fm.password.value == "") {
		  alert("비밀번호를 입력해주세요");
		  fm.password.focus();
		  return;
	  }
	  
	  let ans = confirm("저장하시겠습니까?");  // true 함수의 값을 참과 거짓 true, false로 나눈다
	  
	  if (ans == true) {
		  fm.action="${pageContext.request.contextPath}/board/boardWriteAction.aws";
		  fm.method="post";
		  fm.enctype="multipart/form-data";  // 인코딩 타입. 문자 뿐만 아니라 이미지같은 파일도 포함
		  fm.submit();
	  }
	  
	  return;
}

</script>
</head>
<body>
<header>
	<h2 class="mainTitle">글쓰기</h2>
</header>

<form name="frm">
	<table class="writeTable">
		<tr>
			<th>제목</th>
			<td><input type="text" name="subject"></td>
		</tr>
		<tr>
			<th>내용</th>
			<td><textarea name="contents" rows="6"></textarea></td>
		</tr>
		<tr>
			<th>작성자</th>
			<td><input type="text" name="writer"></td>
		</tr>
		<tr>
			<th>비밀번호</th>
			<td><input type="password" name="password"></td>
		</tr>
		<tr>
			<th>첨부파일</th>
			<td>
				<input type="file" name="attachfile" id="fileInput">
				<input type="text" class="fileInputName" readonly="readonly">
				<label for="fileInput" class="btn labelBtn">파일선택</label>
			</td>
		</tr>
	</table>
	
	<div class="btnBox">
		<button type="button" class="btn" onclick="check();">저장</button>
		<button type="button" class="btn" onclick="history.back();">취소</button>
	</div>	
</form>

<script>

const fileInput = document.getElementById("fileInput");

fileInput.addEventListener("change", function() {
	const fileInputName = document.querySelector(".fileInputName");
	const fileUrl = fileInput.value.split("\\");
	fileInputName.value = fileUrl[fileUrl.length - 1];
});

</script>
</body>
</html>