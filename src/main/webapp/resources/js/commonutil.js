/**
 * 
 */
Date.prototype.Format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return decodeURIComponent(r[2]);
	}
	return '';
}
function changeVerifyCode(img) {
	img.src = "../Kaptcha?" + Math.floor(Math.random() * 100);
}
//上传图片后给 预览图片信息
function showImg(obj) {
	var files=$(obj)[0].files
	if(files!=null){
		$(obj).siblings('br').remove();
		$(obj).siblings('img').remove();
		$(obj).parent().append('<br/>');
		for (var x = 0; x < files.length; x++) {
			var file=files[x]; //获取文件信息
			if(file)
			{
				var reader=new FileReader();  //调用FileReader
				reader.readAsDataURL(file); //将文件读取为 DataURL(base64)
				reader.onload=function(evt){   //读取操作完成时触发。
					var img = $('<img></img>').attr({
						src:evt.target.result,
						width:80,
						height:80,
						class:"previewImg"
					});
					img.appendTo($(obj).parent());
				};
			}
			else{
				$.toast('上传失败');
			}

		}
	}

}