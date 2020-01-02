<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/view/jspHeader.jsp"  %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>websocket client</title>
<c:set var="port" value="${pageContext.request.localPort}" />
<c:set var="server" value="${pageContext.request.serverName}" />
<c:set var="path" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
   $(function() { // 문서 준비 완료
      var ws = new WebSocket("ws://${server}:${port}${path}/chatting.shop");
      // 서버와 연결 될 떄
   	  ws.onopen = function() {
         $("#chatStatus").text("info:connection opened")
         // chatInput 태그에 keydown이벤트 등록
         $("input[name=chatInput]").on("keydown",function(evt) {
          	// evt : keyevent 객체
        	 if(evt.keyCode == 13) { // enter 키의 코드값
               var msg = $("input[name=chatInput]").val();
               ws.send(msg); // 서버로 입력된 메시지 전송
               $("input[name=chatInput]").val(""); // 입력된 내용 지움
            }
         })
      }
      // 서버에서 메시지 수신한 경우
      ws.onmessage = function(event) {
         $("textarea").eq(0).prepend(event.data+"\n");
         //                    append 뒤에 붙여
         //                    prepend 앞에 메시지 출력
      }
      // 서버 연결 해제된 경우
      ws.onclose = function(event) {
         $("#chatStatus").text("info:connection close");
      }
   })
</script>
</head>
<body>
<p>
<div id="chatStatus"></div>
<textarea name="chatMsg" rows="15" cols="40"></textarea><br>
메시지 입력 : <input type="text" name="chatInput">
</body>
</html>