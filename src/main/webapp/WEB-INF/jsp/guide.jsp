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
		扫描二维码支付
	</h1>
	<h2>支付后获取《指导说明书密码》，请不要刷新本页面</h2><br>
	<h2>请充分考虑后自愿购买，</h2><br>
	<h2>内容不适合不劳而获者！</h2>
	<div>
		<img src="/qrcode/${orderId}" alt="">
	</div>
    <p>支付成功后，请勿刷新，等待几秒钟，然后 →<button id="checkAndGet">点击这里</button></p>
</body>
<script src="/js/jquery.min.js"></script>
<script type="text/javascript">
	$("#checkAndGet").click(function () {
		$.ajax({
			async: false,//同步，待请求完毕后再执行后面的代码
			type: "POST",
			url: '/order/check/${orderId}',
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			dataType: "json",
			crossDomain: true,
			success: function (data) {
				if(data.code==='0') {
					var passwd = data.passwd;
					window.location.href="/passwd/" + passwd;
				} else {
					alert("支付失败，请重试");
				}
			},
			error: function () {
				alert("系统异常")
			}
		})
	})
</script>
</html>
