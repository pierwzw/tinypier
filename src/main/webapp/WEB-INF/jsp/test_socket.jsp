<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>扫一扫二维码</title>
	<meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<style type="text/css">
		h1, h2, div, p {
			text-align: center;
		}
	</style>
</head>

<body>
	<h1>
		test socket
	</h1>
	<h2>支付后获取学习资料密码，有问题请联系QQ:501311328</h2>
</body>
<script type="text/javascript">

	var websocket = null;
	// 判断当前浏览器是否支持WebSocket
	if ('WebSocket' in window) {
		// 创建WebSocket对象,连接服务器端点
		websocket = new WebSocket("ws://localhost:8089/ws");
	} else {
		alert('Not support websocket')
	}

	// 连接发生错误的回调方法
	websocket.onerror = function() {
		console.info("连接建立失败")
	};

	// 连接成功建立的回调方法
	websocket.onopen = function(event) {
		console.info("连接建立成功")
	}

	// 接收到消息的回调方法
	websocket.onmessage = function(event) {
		alert(event.data)
		console.info(event.data)
	}

	// 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，
	// 防止连接还没断开就关闭窗口，server端会抛异常。
	window.onbeforeunload = function() {
		websocket.close();
	}
</script>
</html>
