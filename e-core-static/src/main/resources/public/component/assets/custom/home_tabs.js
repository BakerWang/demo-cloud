
addMenuTab = function(id,url,title,close){
	if(close == undefined){
		close = true;
	}
	var options ={"id":id,"title":title,"url":url,"close": close,iframe:false};
	addTabs(options);
}
var addTabs = function (options) {
   
    id = "tab_" + options.id;
    $(".active").filter(".mtab").removeClass("active");
    //如果TAB不存在，创建一个新的TAB
    if (!$("#" + id)[0]) {
        //固定TAB中IFRAME高度
        mainHeight = $(document.body).height() - 90;
        //创建新TAB的title
        title = '<li role="presentation" id="tab_' + id + '"><a  onclick="$(document).trigger(\'mainTabClick\'); " href="#' + id + '" aria-controls="' + id + '" role="tab" data-toggle="tab">' + options.title;
        //是否允许关闭
        if (options.close) {
            title += ' <i class="glyphicon glyphicon-remove" tabclose="' + id + '"></i>';
        }
        title += '</a></li>';
        //是否指定TAB内容
        if(options.iframe){
        	 content = '<div role="tabpanel" class="tab-pane" id="' + id + '"><iframe src="' + options.url + '" width="100%" height="' + mainHeight +
             '" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe></div>';
        	 $("#homeMainTab").append(content);
        }else{
        	 var $content = $('<div role="tabpanel" class="tab-pane" id="' + id + '">' + '</div>');
        	 $("#homeMainTab").append($content);
        	 $.ajax({
 				url: options.url,
 				beforeSend: function (XMLHttpRequest) {
                    XMLHttpRequest.setRequestHeader("ajax", true);
                }
 			}).
 			error(function(XMLHttpRequest){
 				$.loy.statusErrorShow(XMLHttpRequest.status)
 			})
 			.done(function(result) {
 				$content.empty().html(result);
 			});
        	
        }
        //加入TABS
        $("#homeMainNav").append(title);
       
    }
    //激活TAB
    $("#tab_" + id).addClass('active mtab');
  
            $("#" + id).addClass("active mtab");
    
    $(document).trigger('mainTabClick');
};
var closeTab = function (id) {
    //如果关闭的是当前激活的TAB，激活他的前一个TAB
    if ($("li.active").attr('id') == "tab_" + id) {
        $("#tab_" + id).prev().addClass('active');
        $("#" + id).prev().addClass('active');
    }
    //关闭TAB
    $("#tab_" + id).remove();
    $("#" + id).remove();
};

        
       
DynamicLoad.loadFileList(jsCssFile,function(){
	$.homeGlobal =HomeGlobal;
	$(function () {
        mainHeight = $(document.body).height() - 45;
        $('.main-left,.main-right').height(mainHeight);
        $("[addtabs]").click(function () {
            addTabs({ id: $(this).attr("id"), title: $(this).attr('title'), close: true });
        });
        $("#homeMainNav").on("click", "[tabclose]", function (e) {
            id = $(this).attr("tabclose");
            closeTab(id);
        });
    });

   	 var home = new Home();

   	 home.offsetHeight = function(){
   			return 330;
   	 };
   	 home.getSelfPage = function(){
   		 return "home_tabs.html";
   	 };
   	 home.initSidebar();
   	 home.buildMenu = function(data){
   		    addMenuTab("Index",data.defaultPage,$.i18n.prop("home_page"),false);
   		    var menuData = data.menuData;
			var menuBuffer = [];
			if(menuData){
				for(var i=0;i<menuData.length;i++) {
					var item = menuData[i];
					var name = item.data.name;
					var cls = item.data.cls;
					menuBuffer.push(' <li class="">');
					menuBuffer.push('<a href="" class="dropdown-toggle">');
					menuBuffer.push('<i class="'+cls+'"></i>');
					menuBuffer.push('<span class="menu-text">'+name+'</span>');
					menuBuffer.push('<b class="arrow fa fa-angle-down"></b>');
					menuBuffer.push('</a>');
					menuBuffer.push('<b class="arrow"></b>');
					menuBuffer.push('<ul class="submenu">');
                     var children = menuData[i].children;
                     if(children){
                    	 for(var j=0;j<children.length;j++) {
                        	 var item = children[j];
                        	 var name = item.data.name;
                        	 var cls = item.data.cls;
                        	 var url = item.data.url?item.data.url:'';
                        	 if(url != ''){
                        		 var v =(HomeGlobal && HomeGlobal.VERSION)?HomeGlobal.VERSION:Math.random();
                        		 if(url.indexOf('?')>0){
                        			 url = url+'&version='+v;
                        		 }else{
                        			 url = url+'?version='+v;
                        		 }	
                        	 }
                        	 menuBuffer.push(' <li class="">');
                        	 var menuParams = [];
                        	 var id = item.id?item.id:'';
                        	 menuParams.push("'"+id+"'");
                        	 menuParams.push(",");
                        	 menuParams.push("'"+url+"'");
                        	 menuParams.push(',');
                        	 menuParams.push("'"+name+"'");
                        	 menuBuffer.push('<a   style="cursor: pointer;" href="#" onclick=\"addMenuTab('+menuParams.join('')+')\">');
                        	 menuBuffer.push('<i class="'+cls+'"></i>');
                        	 menuBuffer.push(name);
                        	 menuBuffer.push('</a>');
                        	 
                        	 
                        	 var cld = item.children;
                        	 if(cld && cld.length>0){
                        		 menuBuffer.push('<b class="arrow fa fa-angle-down"></b>');
                        		 menuBuffer.push('<ul class="submenu nav-show" style="display: block;">');
                        		 
                        		 for(var m=0;m<cld.length;m++) {
                        			 var item = cld[m];
		                        	 var name = item.data.name;
		                        	 var cls = item.data.cls;
		                        	 var url = item.data.url?item.data.url:'';
		                        	 var v =HomeGlobal&&HomeGlobal.VERSION?HomeGlobal.VERSION:Math.random();
	                        		 if(url.indexOf('?')>0){
	                        			 url = url+'&version='+v;
	                        		 }else{
	                        			 url = url+'?version='+v;
	                        		 }	
	                        		 
	                        		 
	                        		 var menuParams = [];
		                        	 var id = item.id?item.id:'';
		                        	 menuParams.push("'"+id+"'");
		                        	 menuParams.push(",");
		                        	 menuParams.push("'"+url+"'");
		                        	 menuParams.push(',');
		                        	 menuParams.push("'"+name+"'");
		                        	
		                        	 menuBuffer.push(' <li class="">');
		                        	 menuBuffer.push('<a   style="cursor: pointer;" href="#" onclick=\"addMenuTab('+menuParams.join('')+')\">');
		                        	 menuBuffer.push('<i class="menu-icon fa fa-caret-right"></i>');
		                        	 menuBuffer.push(name);
		                        	 menuBuffer.push('</a>');
		                        	 menuBuffer.push('<b class="arrow"></b>');
		                        	 menuBuffer.push('</li>');
		                        	 
                        		 }
                        		 
                        		 menuBuffer.push('</ul>');
                        	 }else{
                        		 menuBuffer.push('<b class="arrow"></b>');
                        	 }
                        	 
                        	 
                        	 menuBuffer.push('<b class="arrow"></b>');
                        	 menuBuffer.push('</li>');
                         }
                     }   
                menuBuffer.push('</ul>');	
                menuBuffer.push('</li>');	
				}
			}
			
			var menuStr = menuBuffer.join(' ');
			$('#menuDiv').html(menuStr);
       	 };
       	 home.init();
    		
   });