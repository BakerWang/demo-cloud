/*
 * Copyright   Loy Fu. 付厚俊
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/


function logout(){
	var $form = $('<form style="display:none"  method="POST" action="logout"></form>');
	$(document.body).append($form);
	$form.submit();
}


function hasPermissionByCode(accessCode){
	var value = HomeGlobal.accessCodes[accessCode];
	if(value == null || value == undefined){
		return true;
	}
	return value;
}

Home = function(){
	this.offsetHeight = function(){
		return 320;
	};
	this.getSelfPage = function(){
		
	};
	this.getIndexDataUrl = function(){
		return "indexData";
	}
	this.hideProfileDropdown = function(){
		FastClick.attach(document.body); 
		var self = this;
		HomeGlobal.gridDefaultHeight = document.documentElement.clientHeight-self.offsetHeight();
		
	   	if($('#myprofileDropdownDiv').is(':visible')){
	   		$('#myprofileDropdown').click();
	   	}
	};
	this.initDefaultPage = function(){
		
	};
	this.buildMenu = function (data){};
	this.setNoticeCount = function(noticeProperties){
		 if(!noticeProperties.noticeCountUrl){
			 return;
		 }
		 $.loy.ajax({
				url:noticeProperties.noticeCountUrl,
				success:function(data){
					if(data.success){
						var result = data.data;
						$('#myNoticeSpan').html(result);
					}	
				}
		 });
	};
	this.bindNoticeDropdown = function(noticeProperties){
		 var self = this;
		 if(!noticeProperties.noticeListUrl){
			 return;
		 }
		 $('#noticeDropdown').bind('show.bs.dropdown', function () {
   		 var $noticeList = $('#noticeList');
   		 $noticeList.html('');
   		 $.loy.ajax({
   				url:noticeProperties.noticeListUrl,
   				success:function(data){
   					if(data.success){
   						var result = data.data;
   						var list = [];
   						var notices = result.content;
   						if(notices){
   						   $('#myNoticeSpan').html(result.totalElements);
   						   var len = notices.length;
   						   if(len>5){
   							   len = 5;
   						   }
   						   for(var i=0;i<len;i++){
   							   var url = "javascript:void(0)"
   							   if(noticeProperties.showDetail){
   								   if(noticeProperties.showDetailUrl){
   									 url = self.getSelfPage()+noticeProperties.showDetailUrl+"?id="+notices[i].id;
   								   }
   							   }
   							   list.push('<li><a href="'+url+'"><i class="btn btn-xs btn-primary fa fa-user"></i>'+notices[i].subject+'</a></li>');
   						   }
   						}
       					$noticeList.html(list.join(''));
   					}	
   				}
   		});
   	 });
	};
	
	this.buildSystemList = function(list){
		if(list && list.length>0){
			 $('#systemsDiv').show();
			 var temp =[];
			 for(var i=0;i<list.length;i++){
				 for(var i=0;i<list.length;i++){
   				 temp.push('<li><a href="'+list[i].url+'"  target="'+list[i].code+'" ><div class="clearfix"><span class="pull-left">'+list[i].name+'</span></div></a></li>');
   			 }
			 }
			 $('#systemListUl').html(temp.join(' '));
		 }
	};
	 
    this.initSidebar = function() {
		var $sidebar = $('.sidebar');
		if($.fn.ace_sidebar) $sidebar.ace_sidebar();
		if($.fn.ace_sidebar_scroll) $sidebar.ace_sidebar_scroll({
			//'scroll_style': 'scroll-dark scroll-thin',
			'scroll_to_active': true, 
			'include_shortcuts': true,
			'include_toggle': false || ace.vars['safari'] || ace.vars['ios_safari'], 
			'smooth_scroll': 150, 
			'outside': false//true && ace.vars['touch'] 
		});
		if($.fn.ace_sidebar_hover)	$sidebar.ace_sidebar_hover({
			'sub_hover_delay': 750,
			'sub_scroll_style': 'no-track scroll-thin scroll-margin scroll-visible'
		});
	};
	
	this.init = function(){
		HomeGlobal.LANG = "zh_CN";	
		
		var LANG = $.cookie("LANG");
		if(LANG){
			HomeGlobal.LANG = LANG;
		}else{
			$.cookie("LANG",HomeGlobal.LANG);
		}
		var self = this;
		self.hideProfileDropdown();
		
		$.loy.ajax({
			showSuccess:false,
			url:self.getIndexDataUrl()+"?lang="+HomeGlobal.LANG,
			success:function(result){
				var data = result.data;
				HomeGlobal.accessCodes = data.accessCodes;
				var simpleUser = data.simpleUser;
				HomeGlobal.USER_ID = data.simpleUser.id;
					
				self.initDefaultPage(data);
				
				bootbox.setDefaults("locale",HomeGlobal.LANG); 
				//初始化语言列表
				var supportLocales = data.supportLocales;
				if(supportLocales){
					var temp = [];
					for(var i=0;i<supportLocales.length;i++){
						var v = supportLocales[i].value;
						var displayName = supportLocales[i].displayName;
						temp.push('<option value="'+v+'" ');
						if(v == HomeGlobal.LANG){
							temp.push(' selected >');
						}else{
							temp.push('>');
						}
						temp.push(displayName+'</option>');
					}
					$('#languageSelect').html(temp.join(' '));
					$('#languageSelect').bind('change', function () {
		        		 var v = $(this).val();
		        		 $.cookie("LANG",v);
		        		 location.reload();
		        	});
				}
				
				if(data.noticeProperties){
					var noticeProperties = data.noticeProperties;
					if(noticeProperties.showCount){
						$('#noticeDropdown').show();
						self.setNoticeCount(noticeProperties);
						if(noticeProperties.showNoticeList){
							self.bindNoticeDropdown(noticeProperties);
						}	
					}else{
						$('#noticeDropdown').hide();
					}
				}
				$('#loginUserName').html(simpleUser.name?simpleUser.name:"");
				$('#mySmallPhoto').attr('alt',simpleUser.name);
				if(data.photo){
					$('#mySmallPhoto').attr('src', "upm/profile/photo?id="+simpleUser.id+"&m="+Math.random());
				}else{
					$('#mySmallPhoto').attr('src', "component/assets/avatars/profile-pic.jpg");
				}
				
				
				$.loy.i18n(['upm/i18n/message'],HomeGlobal.LANG,$(document),{
		        		 callback: function() {// 回调方法
		     		    	HomeGlobal.messageFinish = true;
		     		    	self.buildMenu(data); 
		     		    	$("*[i18n]",$(document)).each(function(){
		     		    		 var $this = $(this);
		     		    		 var key =$this.attr('i18n');
		     		    		 var value = $.i18n.prop(key);
		     		    		 $this.html(value);
		     		    	});
	 						$('.page-content-area').ace_ajax('loadScripts', [null,null], function() {
	                        	 if(HomeGlobal.aceAjaxObject){
  									HomeGlobal.aceAjaxObject.loadUrl(HomeGlobal.hs);
  								}
	 						});
				       }
			    });
	       }
        });
	};
}

