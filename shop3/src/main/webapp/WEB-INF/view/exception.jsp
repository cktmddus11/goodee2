<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %> <!--  exceptiopn 에러 객체는
    에러 페이지에서만 받을 수 있어서 에러페이지 처리 한거임
  isErrorPage true로 처리함  -->
<script>
	alert("${exception.message}")
	location.href="${exception.url}";
</script>