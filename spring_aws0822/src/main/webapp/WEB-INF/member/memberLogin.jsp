<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	String msg = "";
	
	if(request.getAttribute("msg") != null) {
		msg = (String)request.getAttribute("msg");
	}
%>
<!DOCTYPE HTML>
<HTML>
 <HEAD>
  <TITLE> 로그인 페이지 </TITLE>
  <style>
	header { 
		width: 100%;
		height: 50px;
		text-align: center;
		--background-color: yellow; 
	}
	section {
		text-align: center;
		height: 200px;
		--background-color: olivedrab;
		border: 10px solid burlywood;
	}
	.id { float: left; width: 50%; height: 40px; margin-top: 30px; }
	.pw { float: left; width: 50%; height: 40px; margin-top: 30px; }
	.btn { clear: both; text-align: center; height: 40px; }
	input { margin-left: 20px; }
	footer {
		width: 100%;
		height: 150px;
		text-align: center;
		/* background-color: plum; */
		margin-top: 20px;
	}
  </style>
  
  <script>
  <%
	  if (msg !=""){
	  	out.println("alert('"+msg+"')");	
	  }
  %>
  
  function check() {
	  let memberid = document.getElementsByName("memberid");
	  let memberpwd = document.getElementsByName("memberpwd");

	  if (memberid[0].value == "") {
		  alert("아이디를 입력해주세요");
		  memberid[0].focus();
		  return;
	  } else if (memberpwd[0].value == "") {
		  alert("비밀번호를 입력해주세요");
		  memberpwd[0].focus();
		  return;
	  }
	  
	  let fm = document.frm;
	  fm.action = "<%=request.getContextPath()%>/member/memberLoginAction.aws";  // 가상경로지정. action은 처리하라는 의미
	  fm.method = "post";
	  fm.submit();
	  
	  return;
  }
  </script>
 </HEAD>

 <BODY>
	<header>로그인 페이지</header>
	<section>
		<form name="frm">

			<p class="id">아이디 <input type="text" name="memberid" id="memberid" style="width:200px" maxlength="30" value=""></p>
			
			<p class="pw">비밀번호 <input type="password" name="memberpwd" id="memberpwd" style="width:200px" maxlength="30"></p>
			
			<p class="btn"><input type="button" name="btn" value="로그인하기" onclick="check();"></p>

		</form>
	</section>
	<footer>
	made by jh.
	</footer>	  
 </BODY>
</HTML>
