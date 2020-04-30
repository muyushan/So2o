$(function(){
    debugger;
    var shopId=getQueryString("shopId");
    var shopInfoUrl="/So2o/shopadmin/getshopmangementnnfo?shopId="+shopId;
    $.getJSON(shopInfoUrl,function (data) {
        if(data.redirect){
            window.location.href=data.url;
        }else{
            if(data.shopId!=undefined&&data.shopId!=null){
                shopId=data.shopId;
            }
            $("#shopInfo").attr("href","/So2o/shopadmin/shopOperation?shopId="+shopId);
        }
    })
});