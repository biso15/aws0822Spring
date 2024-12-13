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
	  } else if (fm.receiverEmail.value == "") {
		  alert("받는사람의 메일주소를 입력해주세요");
		  fm.receiverEmail.focus();
		  return;
	  }
	  
	  let ans = confirm("발송하시겠습니까?");  // true 함수의 값을 참과 거짓 true, false로 나눈다
	  
	  if (ans == true) {
		  fm.action="${pageContext.request.contextPath}/email/emailWriteAction.aws";
		  fm.method="post";
		  fm.submit();
	  }
	  
	  return;
}

</script>
</head>
<body>
<header>
	<h2 class="mainTitle">메일 쓰기</h2>
</header>

<form name="frm">
	<table class="writeTable">
		<tr>
			<th>메일 제목</th>
			<td><input type="text" name="subject"></td>
		</tr>
		<tr>
			<th>메일 내용</th>
			<td><textarea name="contents" rows="6"></textarea></td>
		</tr>
		<tr>
			<th>보내는 메일 주소</th>
			<td><input type="text" name="senderEmail"></td>
		</tr>
		<tr>
			<th>받는 메일 주소</th>
			<td><input type="text" name="receiverEmail"></td>
		</tr>
	</table>
	
	<div class="btnBox">
		<button type="button" class="btn" onclick="check();">저장</button>
		<button type="button" class="btn" onclick="history.back();">취소</button>
	</div>	
</form>

</body>
</html>