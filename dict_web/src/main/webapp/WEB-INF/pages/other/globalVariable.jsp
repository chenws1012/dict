<%@page import="java.util.Date"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>

<%@ page language="java" pageEncoding="utf-8"%>
<% 
 /**基本上下文路径*/
 String path = request.getContextPath();  
 String basePath = request.getScheme() + "://"  
              + request.getServerName() + ":" + request.getServerPort() + path ;
 pageContext.setAttribute("HOME",basePath);

%>
<script>
 function resizeMain(){
  //浏览器宽度
  var clientHeight=$(window).height();
  var bodyHeight=$('body').height();
  var searchHeight=$('.searchItem').height();
  if (searchHeight==undefined){
   searchHeight=0;
  }else{
   searchHeight=searchHeight+21;
  }
  var fixHeight=$('.fix').height();
  var titleHeight=$('.title').height();
  if (titleHeight==undefined){
   titleHeight=0;
  }else{
   titleHeight=titleHeight+11;
  }
  var footHeight=$('.foot').height()+11;
  var nav=38;
  var offset=searchHeight+fixHeight+footHeight+titleHeight+nav+10;
  var scrollHeight=$(document).height();
  var height=bodyHeight-offset;
  if (scrollHeight>bodyHeight){
   height=scrollHeight-offset+25;
  }
  $('.mainTable_div').height(height);
 }
</script>