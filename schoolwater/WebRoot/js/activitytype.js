// JavaScript Document
 		
var num = 1;
var isHasNull=0;   //����Ƿ��п�div���

$(function(){
    $("#activitytype-addtype1").click(function(){ 	
	    if(num <= 10 && isHasNull==0){      
	    	$("#typetable").find("tr:first").after("<tr><td>"+
			        //"<div class='changetype'  style='position:absolute;display:none;cursor:pointer;top:0%;left:0%'>*</div>"+
					"<div class='changetext' ><span id='actname'></span><span id='actid' style='display:none;'></span></div>"+
					"<div class='deletype'><img src='img/typedele.jpg' style='width:100%;height:100%;'/></div>"+
					"<div  class='changetype' ><img src='img/typechange.jpg' style='width:100%;height:100%;'/></div></td>"+
					"</tr>"
					
			       //"</div>"
			       );
			num++;
			
			//����dialogdiv
			$.ShowDIV('DialogDiv');
			
        } else{
        	alert("�����Ѵ����޻����п����ͣ��޷�����");
        }
	    
	    
	    
		$.addtypeid();
		
		//lyl��ӣ��жϺͿ���div
		$(".changetext").find("#actname").each(function(){
	    	if($(this).html()==""){
	    		isHasNull = 1;
	    	}
	    });
		
		});
});





//ɾ����ť����Ӧ�¼�
$(function(){
    $(".deletype").live('click', function() {
        var $parent = $(this).parent().parent();
        var data = $(this).parent().parent().find(".changetext").children("#actid").html();
        
        if(data==""){$(this).parent().parent().remove();}else{
        	
	        $.get("deleteType.action",{deleteType:data},function(data){
	            if(data=="true"){
	            	$parent.remove();
		            alert("ɾ���ɹ�");   
	            }else{
		            alert("ɾ��ʧ�ܣ�������Ѱ󶨴�����");
	            }
	        }); 
        } 
        $.addtypeid();        
	});	
});

$.extend({'addtypeid':function(){
	    isHasNull = 0;
		$(".changetext").css("width","60%");
		num = $("#typetable").find("tr").size()-1;
}})




