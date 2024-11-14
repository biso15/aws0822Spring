<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글수정</title>
<link href="${pageContext.request.contextPath}/resources/css/style2.css" rel="stylesheet">
<script>
// 메세지
const msg = "${requestScope.msg}";
if (msg != null && msg != "") {
 alert(msg);
} 

function check() {
	  
	  // 유효성 검사하기
	  let fm = document.frm;
	  
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
	  
	  let ans = confirm("저장하시겠습니까?");
	  
	  if (ans == true) {
		  fm.action="${pageContext.request.contextPath}/board/boardModifyAction.aws";
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
	<h2 class="mainTitle">글수정</h2>
</header>

<form name="frm">
	<input type="hidden" name="bidx" value="${requestScope.bv.bidx}">  <!-- bidx값이 수정할때 필요해서 hidden으로 안보이게 한 input에 넣어서 controller로 보낸다. -->
	<input type="hidden" class="isFileChange" name="isFileChange" value="false">  <!-- bidx값이 수정할때 필요해서 hidden으로 안보이게 한 input에 넣어서 controller로 보낸다. -->
	<table class="writeTable">
		<tr>
			<th>제목</th>
			<td><input type="text" name="subject" value="${requestScope.bv.subject}"></td>
		</tr>
		<tr>
			<th>내용</th>
			<td><textarea name="contents" rows="6">${requestScope.bv.contents}</textarea></td>
		</tr>
		<tr>
			<th>작성자</th>
			<td><input type="text" name="writer" value="${requestScope.bv.writer}"></td>
		</tr>
		<tr>
			<th>비밀번호</th>
			<td><input type="password" name="password"></td>
		</tr>
		<tr>
			<th>첨부파일</th>
			<td>			
				<input type="file" name="attachfile" id="fileInput">
				<input type="text" name="fileInputName" class="fileInputName" readonly="readonly">
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
const fileInputName = document.querySelector(".fileInputName");

fileInput.addEventListener("change", function() {
	const fileUrl = fileInput.value.split("\\");
	fileInputName.value = fileUrl[fileUrl.length - 1];
	
	const isFileChange = document.querySelector(".isFileChange");
	isFileChange.value="true";
});

function getOriginalFileName(fileName) {
	var idx = fileName.lastIndexOf("_")+1;
	return fileName.substr(idx);
}

fileInputName.value = getOriginalFileName("${requestScope.bv.filename}");

</script>
</body>
</html>