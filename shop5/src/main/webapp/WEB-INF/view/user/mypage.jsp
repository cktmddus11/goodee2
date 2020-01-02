<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" http-equiv="Content-Type" 
content="text/html;">
<title>MyPage</title>
<script type="text/javascript">
	$(document).ready(function(){
		// 기본 탭 설정값 
		$("#minfo").show(); //회원정보 보여줌
		$("#oinfo").hide(); //주문정보 숨겨줌
		$(".saleLine").each(function(){ // 주문상품 목록을 숨김
			$(this).hide();
		})
		$("#tab1").addClass("select"); // 회원정보, 주문정보 탭 누른거에 select클래스 추가 tab select -> 
		// 탭 색 파란색으로 하려고
	})
	function disp_div(id, tab){ // 회원정보 탭 누르면
		$(".info").each(function(){ 
			$(this).hide(); // id가 oinfo -> 주문 정보 영역 숨겨짐
		})
		$(".tab").each(function(){
			$(this).removeClass("select"); // 선택했었던 탭을 안선택되게 select지워서 풀음 
		})
		$("#"+id).show();
		$("#"+tab).addClass("select");
	}
	function list_disp(id){ // 주문정보 리스트
		$("#"+id).toggle(); // 행번호로 보엿다 안보였다?
	}
</script>
<style type="text/css">
	.select {
		paddint : 3px;
		background-color : #0000ff;
	}
	.select>a {
		color : #ffffff;
		text-decoration : none;
		font-weight : bold;
	}
</style>
</head>
<body>
	<table>
		<tr>
			<td id="tab1" class="tab">
				<a href="javascript:disp_div('minfo', 'tab1')">회원정보 보기</a>
			</td>
		<%-- 	<c:if test="${param.id != 'admin'}"> --%>
				<td id="tab2" class="tab">
					<a href="javascript:disp_div('oinfo', 'tab2')">주문정보 보기</a>
				</td>
		<%-- 	</c:if> --%>
		</tr>
	</table>
	<%-- oinfo : 주문 정보 출력 --%>
	<div id="oinfo" class="info" style="display : nene; width : 100%;">
		<table>
			<tr>
				<th>주문정보</th>
				<c:if test="${param.id == 'admin'}">
				<th>주문고객</th>
				</c:if>
				<th>주문일자</th>
				<th>총주문금액 </th>
			</tr>
			<c:forEach items="${salelist}" var="sale" varStatus="stat">
				<tr>
					<td align="center">
						<a href="javascript:list_disp('saleLine${stat.index}')">
							${sale.saleid}
						</a>
					</td> 
					<c:if test="${param.id == 'admin'}">
					<c:if test="${!empty sale.user}">
					<td>${sale.user.username}(${sale.user.userid}<%--sale.userid? 왜 돼?--%>)</td>
					</c:if>
					</c:if>
					<td align="center">
						<fmt:formatDate value="${sale.updatetime}" pattern="yyyy-MM-dd"/>
					</td>
					<td align="right">${sale.total}원</td>
				</tr>
				<tr id="saleLine${stat.index}" class="saleLine">
					<td colspan="4" align="center">
						<table>
							<tr>
								<th width="25%">상품명</th>
								<th width="25%">상품가격</th>
								<th width="25%">구매수량</th>
								<th width="25%">상품 총액</th>
							</tr>
							<c:forEach items="${sale.itemList}" var="saleItem">
								<tr>
									<td class="title">${saleItem.item.name}</td>
									<td>${saleItem.item.price}원</td>
									<td>${saleItem.quantity}개</td>
									<td>${saleItem.quantity * saleItem.item.price}원</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
<%-- minfo : 회원정보 출력 --%>
	<div id="minfo" class="info">
		<table>
			<tr>
				<td>아이디</td>
				<td>${user.userid}</td>
			</tr>
			<tr>
				<td>이름</td>
				<td>${user.username}</td>
			</tr>
			<tr>
				<td>우편번호</td>
				<td>${user.postcode}</td>
			</tr>
			<tr>
				<td>전화번호</td>
				<td>${user.phoneno}</td>
			</tr>
			<tr>
				<td>주소</td>
				<td>${user.address}</td>
			</tr>
			<tr>
				<td>이메일</td>
				<td>${user.email}</td>
			</tr>
			<tr>
				<td>생년월일</td>
				<td><fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/>
			</tr>
		</table>
		<br>
		<a href="update.shop?id=${user.userid}">[회원정보 수정]</a>&nbsp;
		<c:if test="${loginUser.userid != 'admin'}">
			<a href="delete.shop?id=${user.userid}">[회원탈퇴]</a>&nbsp;
		</c:if>
		<c:if test="${loginUser.userid=='admin'}">
			<a href="../admin/list.shop">[회원목록]</a>&nbsp;
		</c:if>
	</div>
	<br>

</body>
</html>