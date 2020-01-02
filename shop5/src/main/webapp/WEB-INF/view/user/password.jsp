<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>비밀번호 변경</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<script>
function inchk(f){
	if(f.pass.value==""){
		alert("현재 비밀번호를 입력하세요");
		f.pass.focus();
		return false;
	}
	if(f.chgpass.value == ""){
		alert("변경 비밀번호를 확인하세요");
		f.chgpass.focus();
		return false;
	}
	if(f.chgpass.value != f.chgpass2.value){
		alert("변경 비밀번호와 변경 비밀번호 재입력이 다릅니다.");
		f.chgpass2.value = "";
		f.chgpass2.focus();
		return false;
	}
}
</script>
</head>
<body>
<form action="password.shop" method="post" name="f" onsubmit="return inchk(this)">
	<!-- <input type="hidden" name="id2" value=""> -->
	<table>
		<caption>비밀번호 변경</caption>
		<tr>
			<th>현재 비밀번호</th>
			<td><input type="password" name="pass"/>
		</tr>
		<tr>
			<th>변경 비밀번호</th>
			<td><input type="password" name="chgpass"/>
		</tr>
		<tr>
			<th>변경 비밀번호 재입력</th>
			<td><input type="password" name="chgpass2"/>
		</tr>
		<tr>
		<td colspan="2"><input type="submit" value="비밀번호 변경"></td>
		</tr>
		</table>
</form>
</body>
</html>