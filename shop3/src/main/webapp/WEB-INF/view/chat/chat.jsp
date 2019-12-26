<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>websocket client</title>
<c:set var="port" value="${pageContent.request.localPort }"/>
<c:set var="server" value="${pageContent.request.serverName }"/>
<c:set var="path" value="${pageContent.request.contextPath }"/>
<script type="text/javascript">
	$(function(){
		var ws = new WebSockiet("ws://${server}:${port}${path}/chatting.shop");
		w3.onopen = function(){
			$("#chatStatus").text("info:connection opened")
			$("input[name=chatInput]").on("keydown", function(evt){
				if(evt.keyCode == 13){
					var msg = $("input[name=chatInput]").val();
					ws.send(msg);
					$('input[name=chatInput]').val("");
				}
			})
		}
		ws.onmessage = function(event){
			$("textarea").eq(0).prepend(event.data+"\n");
		}
		ws.onclose = function(event){
			$("#chatStatus").text("info:connection close")
		}
	})
</script>
</head>
<body>
	<div id="chatStatus">
		<textarea name="chatMsg" rows="15" cols="40">
		</textarea>
		<br>메시지 입력 : <input type="text" name="chatInput">
	</div>
</body>
</html>