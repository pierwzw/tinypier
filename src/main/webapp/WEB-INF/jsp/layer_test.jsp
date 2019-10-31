<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>所有图书</title>
</head>
<body>
<button style="text-align: center" onclick="deleteBook()">刷新</button>
<script src="/js/jquery.min.js"></script>
<script src="/plugin/layer/layer.js"></script>
<script>

	function deleteBook() {
		var w = layer.confirm('确定要删除吗？', {
					btn: ['确定', '取消'] //按钮
				},
				function() { //ENSURE
					location.href = "";
				},
				function() { //CONTROL
					layer.close(w);
				});
	}
</script>
</body>
</html>