<%@ include file="/WEB-INF/pages/other/globalVariable.jsp"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>字典管理</title>
	<script type="text/javascript" src="${path}/js/ConvertUtil.js"></script>
	<link rel="stylesheet" type="text/css" href="${path}/js/jquery-easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${path}/js/jquery-easyui/themes/icon.css">
	<script type="text/javascript" src="${path}/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${path}/js/jquery-easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${path}/js/jquery-easyui/locale/easyui-lang-zh_CN.js.min.js"></script>
	<script type="text/javascript" src="${path}/js/jquery-validation/1.10.0/jquery.validate.min.js"></script>
</head>

<body style="margin: 0px;">
<div class=" clearfix main">
 <div class="mainR">
 <div class=" title"><h3>数据字典管理 </h3></div>
   <div class="searchItem">
	<form id="searchForm" action="app-module" method="post">
	    <table>
	    <tr>
	    <td>
	      	字典类型CD:<input type="text" name="dictTypeCd" />
	      	字典类型名称:<input type="text" name="dictTypeName" />
	      	是否有效:
	      	<select name="enableFlg">
	      	         <option value="">--All--</option>
	      	         <option value="true">是</option>
	      	         <option value="false">否</option>
	      	       </select>
	  	  	<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="Convert.search('searchForm','tt');">搜索</a> 
	  	  	<a href="#" iconCls="icon-add" class="easyui-linkbutton" onclick="editrow(0);">新增</a> 
	      	
		</td>
	
	    </tr>
	    </table>
	 </form>
	</div>
	<div  class="mainTable_div">
		<table id="tt" fit="true"
				 singleSelect="true" rownumbers="true"
				idField="id" url="${path}/dict/list">
			
		</table>
	</div>
<div id="w" class="easyui-window" style="width:900px;height:600px;">
</div>
</div>
</div>
<script type="text/javascript">

	var lastIndex;
	var inputUrl;
	$(function(){
		resizeMain();
		init();

	});
	function init(){
		$('#tt').datagrid({
			pagination:true,
			pageSize:50,
			pageList:[50,40,30,20,10],
			columns:[[
				{field:'dictTypeCd',title:'字典类型CD',sortable:true,width:150,height:30},
				{field:'dictTypeName',title:'字典类型名称',width:150},
				{field:'dispOrderNo',title:'显示序号',width:60,align:'center'	},
				{field:'enableFlg',title:'是否有效',width:70,align:'center'	},
				{field:'createdDate',title:'创建时间',  width:110,},
				{field:'updatedDate',title:'更新时间',width:110,align:'center',},
				{field:'action',title:'操作',width:120,align:'center',
					formatter:function(value,row,index){
						var tmp ='<a href="#" class="easyui-linkbuton l-btn" ><span class="l-btn-left" style="color:#444444">';
						var tmpEnd='</span>';	
						var id =row.id;
						var e = '<button onclick=editrow("'+id+'") >编辑</button>';
						var d = '<button onclick=deleterow("'+id+'") >删除</button>';
						return e+d;
					}
				}
			]],
			onLoadSuccess:function(){
				$('a.easyui-linkbutton').linkbutton();
			}
		});
		$('#w').window({
			title: '字典管理',
			modal:true,
			closed: true,
			collapsible : false,
			minimizable : false,
			cache:false,
			iconCls:"icon-save",
			onClose:function(){
				$('#tt').datagrid('reload');
			}
		});
	}
	function deleterow(id){
		$.messager.confirm('确认','你确认要删除该记录吗?',function(r){
			if (r){
				var row=$('#tt').datagrid('getSelected');
				var index=$('#tt').datagrid('getRowIndex',row);
				$('#tt').datagrid('deleteRow', index);
				var nextSelect=index>0?index-1:0;
				$('#tt').datagrid('selectRow', nextSelect);
				if(id){
					$.post("${path}/intra/dict.do?m=delete",{id:id} , function(result) {
						$('#tt').datagrid('acceptChanges');
					});
				}else{
					$('#tt').datagrid('acceptChanges');
				}
			}
		});
	}
	
	function editrow(id){
		var url="${path}/dict/detail?id="+id;
		$('#w').window({href:url});
		$('#w').window("open");
	}
	function clearForm(){
		$('#searchForm').form('clear');
	}
	
	var options;
	function saveDict(){
		$('#inputForm').form('submit', options);
	}
</script>
</body>
</html>