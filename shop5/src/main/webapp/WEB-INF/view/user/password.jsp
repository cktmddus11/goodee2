<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>��й�ȣ ����</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<script>
function inchk(f){
	if(f.pass.value==""){
		alert("���� ��й�ȣ�� �Է��ϼ���");
		f.pass.focus();
		return false;
	}
	if(f.chgpass.value == ""){
		alert("���� ��й�ȣ�� Ȯ���ϼ���");
		f.chgpass.focus();
		return false;
	}
	if(f.chgpass.value != f.chgpass2.value){
		alert("���� ��й�ȣ�� ���� ��й�ȣ ���Է��� �ٸ��ϴ�.");
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
		<caption>��й�ȣ ����</caption>
		<tr>
			<th>���� ��й�ȣ</th>
			<td><input type="password" name="pass"/>
		</tr>
		<tr>
			<th>���� ��й�ȣ</th>
			<td><input type="password" name="chgpass"/>
		</tr>
		<tr>
			<th>���� ��й�ȣ ���Է�</th>
			<td><input type="password" name="chgpass2"/>
		</tr>
		<tr>
		<td colspan="2"><input type="submit" value="��й�ȣ ����"></td>
		</tr>
		</table>
</form>
</body>
</html>