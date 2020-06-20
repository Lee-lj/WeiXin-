<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="jquery-3.2.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("button").click(function(){
			var url = "GetTicket";
			$.get(url,function(ticket){
				var src="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
				$("img").attr("src",src);
			});
		});
	});
</script>
</head>
<body> 
    
	<button>生成二维码</button><br>
	<img alt="" src="">
</body>
</body>
</html>