var Convert = {
	/*把 JSON 对象转化为字符串 */
    ToJSONString: function(obj) {
        switch(typeof(obj)) 
        {
            case 'object':
                var ret = [];
                if (obj instanceof Array)  {
                    for (var i = 0, len = obj.length; i < len; i++) {
                        ret.push(Convert.ToJSONString(obj[i]));
                    }
                    return '[' + ret.join(',') + ']';
                } else if (obj instanceof RegExp) {
                    return obj.toString();
                } 
                else {
                    for (var a in obj){
                    	ret.push(a + ':' + Convert.ToJSONString(obj[a]));
                    }
                    return '{' + ret.join(',') + '}';
                }
            case 'function':
                return 'function() {}';
            case 'number':
                return obj.toString();
            case 'string':
                return "\"" + obj.replace(/(\\|\")/g, "\\$1").replace(/\n|\r|\t/g, function(a) {return ("\n"==a)?"\\n":("\r"==a)?"\\r":("\t"==a)?"\\t":"";}) + "\"";
            case 'boolean':
                return obj.toString();
            default:
            	if (isEmpty(obj)){
            		return "''";
            	}
                return obj.toString();
            
        }
    },
    /*获取可编辑表格所有更改的数据,返回json对象*/
    ToSaveJson:function(gridId){
    	var inserted = $('#'+gridId).datagrid('getChanges','inserted');
		var updated = $('#'+gridId).datagrid('getChanges','updated');
		var deleted = $('#'+gridId).datagrid('getChanges','deleted');
		var result ={'_inserted':inserted,'_updated':updated,'_deleted':deleted};
		return result;
    },
    /*获取checkbox可编辑表格所有更改的数据,返回json对象*/
    ToSaveCheckBoxJson:function(gridId){
    	var inserted = $('#'+gridId).datagrid('getChecked');
    	var result = {'_inserted':inserted};
    	return result;
    }
    ,
    /*获取可编辑表格所有更改的数据,返回格式为后台接收需要的格式*/
    ToSaveParam:function(gridId,parentId){
		var result = Convert.ToSaveJson(gridId);
		result = Convert.ToJSONString(result);
		var param = {'_easy_grid':result};
		if(parentId){
			param = {'_easy_grid':result,'parentId':parentId};
		}
		return param;
    },
    /*获取checkbox可编辑表格所有更改的数据,返回格式为后台接收需要的格式*/
    ToSaveCheckBoxParam:function(gridId,parentId){
		var result = Convert.ToSaveCheckBoxJson(gridId);
		result = Convert.ToJSONString(result);
		var param = {'_easy_grid':result};
		if(parentId){
			param = {'_easy_grid':result,'parentId':parentId};
		}
		return param;
    }
    ,
    /*将子表(可编辑表格)更改的数据集中到主表表单中,以便一次性保存*/
    setChildren2Form:function(formId,gridId){
    	var result = Convert.ToSaveJson(gridId);
    	var childrenData = Convert.ToJSONString(result);
    	if($('#_easy_grid').length == 0){
    		$('#'+formId).append('<input type="hidden" id="_easy_grid" name="_easy_grid">');
    	}
    	$('#_easy_grid').val(childrenData);
    },
    /*将表单中的对象序列化*/
    getJson4Form:function(formId){
    	var array = $('#'+formId).serializeArray();
    	var obj = {};
    	$.each(array,function(i,n){
    		var name = n.name;
    		var key = n.value;
    		var oldVal= obj[name];
    		if(oldVal==undefined || oldVal=="")
    		{
    			obj[name] = key;	
    		}
    		else
    		{
    			obj[name] = obj[name]+","+key;
    		}
    		
    	});
    	return obj;
    },
    search : function(searchFormId,gridId){
    	var param = Convert.getJson4Form(searchFormId);
		$('#'+gridId).datagrid('options').queryParams = param;
		$('#'+gridId).datagrid('reload');
    },
    getCheckedIds : function(treeId,type,perfix){
    	var nodes = $('#'+treeId).tree('getChecked');
    	var m = '';
    	for(var i=0; i<nodes.length; i++){
    		if (m != '') m += ',';
    		if (nodes[i].attributes.type==type || isEmpty(type)){
    			var nodeId= nodes[i].id;
    			if (!isEmpty(perfix)){
    				nodeId=nodeId.replace(perfix,'');
    			}
    			m += nodeId;
    		}
    	}
    	return m;
    },
    focusEditor:function(tableId,field,editIndex){
		var editor = $('#'+tableId).datagrid('getEditor', {index:editIndex,field:field});
		if (editor){
			editor.target.focus();
		} else {
			var editors = $('#'+tableId).datagrid('getEditors', editIndex);
			if (editors.length){
				editors[0].target.focus();
			}
		}
	},
    findValueByKey : function(jsonObject,keyField,keyValue,valueField){
		for(var i in jsonObject){
			if (jsonObject[i][keyField]==keyValue){
				return jsonObject[i][valueField];
			}
		}
	}
};
function isEmpty(str) {
	return (typeof (str) === "undefined" || str === null || (str.length === 0));
};

function fmoney(s, n) {
	n = n >= 0 && n <= 20 ? n : 2;
	s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
	var l = s.split(".")[0].split("").reverse();
	var r = n > 0 ? s.split(".")[1] : '';
	t = "";
	for (var i = 0; i < l.length; i++) {
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
	}
	var value = t.split("").reverse().join("");
	if (n > 0) {
		value += "." + r;
	}
	return value;
}

function moneyrate(s,n){
	var rate=parseFloat(s/n).toFixed(4);
	return  Math.round(rate*10000)/100+'%';
}

function rmoney(s) {
	return parseFloat(s.replace(/[^\d\.-]/g, ""));
}
function mobileHid(s){
	 if($('#mobileViewFlag').val() ){
		 return s;
	 }else{
		 var start = Math.floor((s.length/2))-Math.floor((s.length/4));
		 var end = Math.floor((s.length/2))+Math.floor((s.length/4));
		 var s2=s.substr(0,start);
		 for(var i =start; i<end;i++){
			 s2+='*';
		 }
		 s2+=s.substr(end);
		 return s2;
	 }
//	return s.replace(/(\d{3})(\d{4})(\d{4})/,"$1****$3");
};