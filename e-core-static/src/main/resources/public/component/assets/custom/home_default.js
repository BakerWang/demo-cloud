
DynamicLoad.loadFileList(jsCssFile,function(){
	$.homeGlobal =HomeGlobal;
	 var home = new Home();
	 home.initDefaultPage = function(data){
		 $('#homeDefaultPage').attr("href","home.html#"+data.defaultPage);
		 document.getElementById('homeDefaultPage').click();
	 };
	 home.getSelfPage = function(){
		 return "home.html";
	 };
	 home.getIndexDataUrl = function(){
		return "upm/indexData";
 	 }
	 home.buildMenu = function(data){
		    var menuData = data.menuData;
			var menuBuffer = [];
			if(menuData){
				for(var i=0;i<menuData.length;i++) {
					var item = menuData[i];
					var name = item.data.name;
					
					var cls = item.data.cls;
					menuBuffer.push(' <li class="">');
					menuBuffer.push('<a href="javascript:void(0)" class="dropdown-toggle">');
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
                        	 var v =$.homeGlobal&&$.homeGlobal.VERSION?$.homeGlobal.VERSION:Math.random();
                    		 if(url.indexOf('?')>0){
                    			 url = url+'&version='+v;
                    		 }else{
                    			 url = url+'?version='+v;
                    		 }	
                        	 menuBuffer.push(' <li class="">');
                        	 menuBuffer.push('<a  data-url="'+url+'" href="home.html#'+url+'">');
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
		                        	 var v =$.homeGlobal&&$.homeGlobal.VERSION?$.homeGlobal.VERSION:Math.random();
	                        		 if(url.indexOf('?')>0){
	                        			 url = url+'&version='+v;
	                        		 }else{
	                        			 url = url+'?version='+v;
	                        		 }	
		                        	 menuBuffer.push(' <li class="">');
		                        	 menuBuffer.push('<a  data-url="'+url+'" href="home.html#'+url+'">');
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
                        	 
                        	 
                        	
                        	 menuBuffer.push('</li>');
                         }
                     } 
                menuBuffer.push('</ul>');	
                menuBuffer.push('</li>');	
				}
			}
			
			var menuStr = menuBuffer.join(' ');
			$('#menuDiv').html(menuStr);
	 }
	 home.init();
});