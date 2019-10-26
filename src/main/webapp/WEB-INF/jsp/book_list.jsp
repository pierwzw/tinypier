<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>所有图书</title>
</head>
<body style="height: 100%">
<button id='button' name='button' type="button">刷新文件列表</button>
<table id="book_list">
	<thead>
	<tr>
		<th>no</th>
		<th>bookId</th>
		<th>标题</th>
		<th>价格</th>
		<th>密码</th>
		<th>操作</th>
	</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<p><span></span></p>
<script src="/static/js/jquery.min.js"></script>
<script src="/static/js/plugin/layer/layer.js"></script>
<script>
	function GetListItems() {
		$.ajax({
			type: "POST",
			url: "/book/display",
			contentType: "application/json; charset=utf-8",
			data: "{}",
			dataType: "json",
			success: function (result) {
				display(result);
			},
			"error": function (result) {
				$("span").empty()
				$("span").append(result.responseText)
			}
		});
	}
	function display(json){
		$("tbody").empty()
		var str = "";
		$.each(json, function(i, data) {
			str += "<tr>" +
			"<td>" + (i+1) + "</td>" +
			"<td>" + data.id + "</td>" +
			"<td>" + data.title + "</td>" +
			"<td>" + data.price + "</td>" +
			"<td>" + data.passwd + "</td>" +
			"<td>" + "<a href='javascript:void(0)' onclick='deleteBook(" + data.id + ")'>"+"删除"+"</a>" + "</td>" +
			"</tr>";
		});
		$("tbody").append(str);
	}
	$(document).ready(function(){
		GetListItems()
	});
	$("button").click(function(){
		GetListItems()
	});
	function deleteBook(id) {
		var w = layer.confirm('确定要删除吗？', {
					btn: ['确定', '取消'] //按钮
				},
				function() { //ENSURE
					location.href = "/book/delete/" + id;
				},
				function() { //CONTROL
					layer.close(w);
				});
	}
</script>
</body>
</html>