
window.DynamicLoad={

    loadFileList:function(_files,callback,version){
        
        var fileArray=[];
        if(typeof _files==="object"){
            fileArray=_files;
        }else{
            if(typeof _files==="string"){
                fileArray=_files.split(",");
            }
        }
        if(fileArray!=null && fileArray.length>0){
           loadFile(fileArray[0],0);
        }else{
            callback();
        }
        function complete(index){
        	if(index != fileArray.length-1){
        	   loadFile(fileArray[index+1],index+1);
        	}else{
        		callback();
        	}
        }
        function loadFile(url,index) {  
            var thisType=getFileType(url);
            if(version){
            	url =url+"?"+version;
            }else{
            	url =url+"?"+Math.random();
            }
            var fileObj=null;
            if(thisType==".js"){
                fileObj=document.createElement('script');
                fileObj.src = url;
            }else if(thisType==".css"){
                fileObj=document.createElement('link');
                fileObj.href = url;
                fileObj.type = "text/css";
                fileObj.rel="stylesheet";
            }else if(thisType==".less"){
                fileObj=document.createElement('link');
                fileObj.href = url;
                fileObj.type = "text/css";
                fileObj.rel="stylesheet/less";
            }
            fileObj.onload = fileObj.onreadystatechange = function() {
                if (!this.readyState || 'loaded' === this.readyState || 'complete' === this.readyState) {
                    complete(index);
                }
            };
            document.getElementsByTagName('head')[0].appendChild(fileObj);
        }
        function getFileType(url){
            if(url!=null && url.length>0){
                return url.substr(url.lastIndexOf(".")).toLowerCase();
            }
            return "";
        }
    }
}