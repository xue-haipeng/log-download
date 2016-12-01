/**
 * Created by Xue on 11/29/16.
 */
var form = $("<form>");
form.attr('style','display:none');
form.attr('target','');
form.attr('method','post');
form.attr('action','excelExport.jsp');

var input1 = $('<input>');
input1.attr('type','hidden');
input1.attr('name','resultListJson');
input1.attr('value',resultListJson);

var input2 = $('<input>');
input2.attr('type','hidden');
input2.attr('name','title');
input2.attr('value',title);

$('body').append(form);
form.append(input1);
form.append(input2);

form.submit();
form.remove();