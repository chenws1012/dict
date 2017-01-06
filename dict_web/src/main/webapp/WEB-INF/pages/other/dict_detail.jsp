<%@ include file="/WEB-INF/pages/other/globalVariable.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<div class="easyui-layout" fit="true">	
	<div region="center"  border="false">
		<form id="inputForm" method="post" class="editForm">
			<input type="hidden" name="id" value="${RESULT.id}" />
			<p>
			<label for="dictTypeCd">字典代码:</label>
			<input name="dictTypeCd" id="dictTypeCd" class="easyui-validatebox control" required="true"  type="text" value="${RESULT.dictTypeCd}"></input>
			</p>
			<p>
			<label for="dictTypeName">字典名称:</label>
			<input name="dictTypeName" id="dictTypeName" class="easyui-validatebox control" required="true" validType="length[0,50]" type="text" value="${RESULT.dictTypeName}"></input>
			</p>
			<p>
			<label>显示序号:</label>
			<input name="dispOrderNo" type="text"  class="easyui-numberbox control" max="100000" value="${RESULT.dispOrderNo}"></input>
			</p>
			<p>
			<label>有效:</label>
			<form:checkbox path="RESULT.enableFlg"  cssStyle="height:25px;line-height:25px;"/>
			</p>
			<p>
			<label style="vertical-align: top;">备注:</label>
			<textarea name="remark" class="easyui-validatebox control" validType="length[0,200]">${RESULT.remark}</textarea>
			</p>
		</form>
		<table id="tt2" 
			title="字典子表" singleSelect="true" rownumbers="true"
			idField="id" url="${path}/dict/listData?dictTypeId=${RESULT.id}">
		</table>
		
	</div>
	<div region="south" style="height:30px;line-height:30px;">
		<div class="toolbar">
			<c:if test="${RESULT.id !=null}">
				<a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="editrow('${RESULT.id}');">刷新</a>
			</c:if>
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveDict();">保存</a>
		</div>
	</div>
</div>


<script type="text/javascript">
	var lastIndex2=-1;
	var isExists=false;
	$(function(){
		initAppDictInput();
	});
	function initAppDictInput(){
		$('#tt2').datagrid({
			columns:[[
				{field:'dictCd',title:'代码',sortable:true,width:150,editor:{type:'validatebox',options:{required:'true',validType:'length[1,50]'}}},
				{field:'dictName',title:'名称',sortable:true,width:250,editor:{type:'validatebox',options:{required:'true'}}},
				{field:'i18n',title:'i18n',width:70,editor:{type:'validatebox',options:{validType:'length[1,50]'}}},
				{field:'dispOrderNo',title:'序号',align:'right',width:40,editor:{type:'numberbox',options:{max:'10'}}},
				{field:'remark',title:'备注',width:80,editor:{type:'validatebox',options:{validType:'length[0,200]'}},
					formatter:function(value,row,index){
						return '<div style="overflow: hidden; white-space: nowrap; text-overflow:ellipsis;" title="'+value+'">'+value+'</div>';
					}
				},
				{field:'enableFlg',title:'有效',width:60,align:'center',
					editor:{type:'checkbox',options:{on:'true',off:'false'}},
					formatter:function(value,row,index){
						if(value=='true')
							return '是';
						else 
							return '否';
					}
				},
				{field:'updatedDate',title:'更新时间',sortable:true,width:110,
					formatter:function(value,row,index){
						if(value){
							return '<div style="overflow: hidden; white-space: nowrap; text-overflow:ellipsis;" title="'+value+'">'+value+'</div>';
						}else{
							return '';
						}
					}
				},
				{field:'action',title:'操作',width:60,align:'center',
					formatter:function(value,row,index){
						var id =row.id;
						var d = '<a href="#" class="easyui-linkbutton" onclick=deleterow2('+index+',"'+id+'") >删除</a>';
						return d;
					}
				}
				]],
			toolbar:[{
				text:'新增',
				iconCls:'icon-add',
				handler:function(){
					var rowSel=$('#tt2').datagrid('getSelected', lastIndex2);
					$('#tt2').datagrid('endEdit', lastIndex2);
					var index = $('#tt2').datagrid('getData').total;
					if(!index)index = $('#tt2').datagrid('getRows').length;
					var field={
							id:'',
							dictCd:'',
							dictName:'',
							i18n:'',
							remark:'',
							enableFlg:'true',
							sequenceNo:index
						};
					$('#tt2').datagrid('appendRow',field);
					lastIndex2 = $('#tt2').datagrid('getRows').length-1;
					$('#tt2').datagrid('selectRow', lastIndex2);
					$('#tt2').datagrid('beginEdit', lastIndex2);
					Convert.focusEditor('tt2',field,lastIndex2);
				}
			}],
			onBeforeLoad:function(){
				$(this).datagrid('rejectChanges');
			},
			onDblClickCell:function(rowIndex,field){
				$('#tt2').datagrid('endEdit', lastIndex2);
				$('#tt2').datagrid('beginEdit', rowIndex);
				lastIndex2 = rowIndex;
				Convert.focusEditor('tt2',field,lastIndex2);
			},
			onClickCell:function(rowIndex,field){
				if (lastIndex2 != rowIndex){
					$('#tt2').datagrid('endEdit', lastIndex2);
					$('#tt2').datagrid('beginEdit', rowIndex);
				}
				lastIndex2 = rowIndex;
				Convert.focusEditor('tt2',field,lastIndex2);
			}
		});
		$("#inputForm").validate({
			rules: {
				dictTypeCd: {
					remote: "${path}/dict/isTypeExists?oldDictTypeCd=" + encodeURIComponent('${RESULT.dictTypeCd}')
				}
			},
			messages: {
				dictTypeCd: {
					remote: "已经存在！"
				}
			}
		});
	}
	options={
        url:'${path}/dict/save',
        onSubmit: function(){
        	var flag=$('#inputForm').form('validate');
        	if (flag){
        		var changeRows=$('#tt2').datagrid('getChanges','inserted','updated');
	        	for(var i=0;i<changeRows.length;i++){
	        		var rowIndex=$('#tt2').datagrid('getRowIndex',changeRows[i]);
	        		flag=$('#tt2').datagrid('validateRow',rowIndex);
	        		if (!flag){
	        			$('#tt2').datagrid('selectRow',rowIndex);
	        			$('#tt2').datagrid('beginEdit', rowIndex);
						break;
	        		}else{
	        			$('#tt2').datagrid('endEdit', rowIndex);
	        		}
	        	}
        	}
        	if(flag){
        		$('#tt2').datagrid('endEdit', lastIndex2);
		        Convert.setChildren2Form("inputForm","tt2");
        	}
        	return flag;
        },
        success:function(data){
        	if(data!='failure'){
	        	$.messager.alert('','保存成功');
	        	$('#w').window("close");
	    		$('#tt').datagrid('reload');
        	}else{
        		$.messager.alert('','保存失败');
        	}
        }
	};
	
	function deleterow2(index,id){
			if(id){
				$.messager.confirm('提示','确认删除该记录?',function(r){
					if (r){
						$.post("${path}/dict/deleteSub",{subId:id} , function(result) {
							$('#tt2').datagrid('deleteRow', index);
							var nextSelect=index>0?index-1:0;
							$('#tt2').datagrid('selectRow', nextSelect);
							$('#tt2').datagrid('acceptChanges');
						});
					}
				});
			}else{
				$('#tt2').datagrid('deleteRow', index);
				var nextSelect=index>0?index-1:0;
				$('#tt2').datagrid('selectRow', nextSelect);
			}
	}
</script>