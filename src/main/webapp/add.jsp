<%--
  Created by IntelliJ IDEA.
  User: wangyinuo
  Date: 2017/11/20
  Time: 9:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="/jquery-1.9.1.min.js"></script>
</head>
<body>
<h2>nihao wangshijiazu</h2>
<form id="form">
    <input type="text" name="name" value="基金组合"><br/>
    <input type="text" name="fundGroupDetailsList[0].fund_id" value="2"/>
    <input type="text" name="fundGroupDetailsList[0].proportion" value="0.2"/><br/>
    <input type="text" name="fundGroupDetailsList[1].fund_id" value="1"/>
    <input type="text" name="fundGroupDetailsList[1].proportion" value="0.8"/><br/>
</form>
<button onclick="submit()">提交</button>
<script>
    function submit(){
        $.ajax({
            url:'/insertFundGroup',
            type:'POST',
            data:$('form').serialize(),
            //dataType:'json',
            success:function(data){
                alert(data);
            }
        });
    }

</script>
</body>
</html>
