<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>添加图书信息</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
</head>

<body>
	<h1 style="text-align: center">
		添加图书信息
	</h1>
	<div style="text-align: center">
		<form id="addBookInfo" action="/book/insert" method="post">
			<label for="title">标题：</label><input name="title" id="title" type="text" value=""><br><br>
			<label for="category">分类：</label><input name="category" id="category" type="text" value=""><br><br>
			<label for="price">价格：</label><input name="price" id="price" type="text" value=""><br><br>
			<input  type="submit"  value=" 提交 " id="submit"/>
		</form>
	</div>
	<script src="/js/jquery.min.js"></script>
<script type="text/javascript">
	$("#submit").click(function () {
		if ($("#title").val() === ''){
			alert("标题不能为空");
			return false;
		}
		if ($("#price").val() === ''){
			alert("价格不能为空")
			return false;
		}
	})
</script>
</body>
</html>
