<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>��ǰ ��� ����</title>
</head>
<body>
<h2>��ǰ���</h2>
	<table>
		<tr>
			<th width="5%">��ǰ ID</th>
			<th width="55%">��ǰ��</th>
			<th width="20%">����</th>
			<th width="20%">��ǰ �̹���</th>
		</tr>
		<c:forEach items="${itemList}" var="item">
		<tr>
			<td>${item.id}</td>
			<td><a href="detail.shop?id=${item.id}">${item.name}</a></td>
			<td>${item.price}</td>
			<td><img src="img/${item.pictureUrl}" width="50"/></td>
		</tr>
		</c:forEach>
	</table>
</body>
</html>