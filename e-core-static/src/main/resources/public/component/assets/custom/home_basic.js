if(Config){
	if(Config.VERSION){
		HomeGlobal.VERSION = Config.VERSION;
	}
}


HomeGlobal = {"homeBaseDataFinish":false,
			 "i18n":false,
			 "LANG" :"zh_CN",
			 "messageFinish":false,
			 "USER_ID":null,
			 "accessCodes":null,
			 "BIG_SCREEN":1024,
			 hs:null,
			 callbackAceAjax : function(hash){
        		 this.hs = hash;
        	 }
};
function isIE() { 
    if (!!window.ActiveXObject || "ActiveXObject" in window)  
        return true;  
    else  
        return false;  
}  

var getCookie = function (cname) {
	 var name = cname + "=";
	 var ca = document.cookie.split(';');
	 for(var i=0; i<ca.length; i++) {
	  var c = ca[i];
	  while (c.charAt(0)==' ') c = c.substring(1);
	  if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
	 }
	 return "";
};

var jsCssFile = [];
for(var i=0;i<Config.jsCssFile.length;i++){
	if(Config.jsCssFile[i].lastIndexOf("zh_CN.js")!= -1){	
		var LANG = getCookie("LANG");
		if(!LANG){
			LANG = "zh_CN";
		}
		LANG = LANG.replace('_',"-");
		var jsFile = Config.jsCssFile[i].replace("zh_CN.js",LANG+".js");
		jsCssFile.push(jsFile);
	}else{
		if(Config.jsCssFile[i].lastIndexOf("-ie.js")!= -1){//区分IE
			if(!isIE()){
				var jsFile = Config.jsCssFile[i].replace("-ie.js",".js");
				jsCssFile.push(jsFile);
				continue;
			}
		}
		jsCssFile.push(Config.jsCssFile[i]);
	}
}