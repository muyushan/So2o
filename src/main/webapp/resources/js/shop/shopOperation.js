/**
 *
 */
$(function(){
    var shopId=getQueryString("shopId");
    var isEdit=false;
    if(shopId!=null){
        isEdit=true;
    }
    var initurl="/So2o/shopadmin/getshopinitinfo";
    var registeShopUrl="/So2o/shopadmin/registershop";
    var shopInfoUrl="/So2o/shopadmin/getshopbyid?shopId="+shopId;
    var editShopUrl="/So2o/shopadmin/modifyshop";
    if(isEdit){
        getShopInfo(shopId);
    }else{
        getShopInitInfo();
    }

    function getShopInfo(shopId){
        $.getJSON(shopInfoUrl,function(data){
            if(data.success){
                var shop=data.shop;
                $("#shop-id").val(shop.shopId)
                $("#shop-name").val(shop.shopName);
                $("#shop-addr").val(shop.shopAddr);
                $("#shop-phone").val(shop.phone);
                $("#shop-desc").val(shop.shopDesc);
                var categoryHtml="";
                var tempAreaHtml="";
                data.shopCategoryList.map(function (item, index) {
                    if(item.shopCategoryId==shop.shopCategory.shopCategoryId){
                        categoryHtml+='<option data-id="'+item.shopCategoryId+'" selected>'+item.shopCategoryName+'</option>';
                    }else{
                        categoryHtml+='<option data-id="'+item.shopCategoryId+'">'+item.shopCategoryName+'</option>';
                    }
                });

                data.areaList.map(function (item, index) {

                    if(item.areaId==shop.area.areaId){
                        tempAreaHtml+='<option data-id="'+item.areaId+'" selected>'+item.areaName+'</option>';
                    }else{
                        tempAreaHtml+='<option data-id="'+item.areaId+'">'+item.areaName+'</option>';
                    }
                });

                $("#shop-category").html(categoryHtml);
                $("#shop-category").attr("disabled","disabled");
                $("#area").html(tempAreaHtml);

            }
        });
    }
    function getShopInitInfo(){
        $.getJSON(initurl,function(data){
            if(data.success){
                var categoryHtml="";
                var tempAreaHtml="";
                data.shopCategoryList.map(function (item, index) {
                    categoryHtml+='<option data-id="'+item.shopCategoryId+'">'+item.shopCategoryName+'</option>';
                });

                data.areaList.map(function (item, index) {
                    tempAreaHtml+='<option data-id="'+item.areaId+'">'+item.areaName+'</option>';
                });
                $("#shop-category").html(categoryHtml);
                $("#area").html(tempAreaHtml);

            }
        });
    }

    $("#submit").click(function(){
        var shop={};
        if(isEdit){
            shop.shopId=$("#shop-id").val();
        }
        shop.shopName=$("#shop-name").val();
        shop.shopAddr=$("#shop-addr").val();
        shop.phone=$("#shop-phone").val();
        shop.shopDesc=$("#shop-desc").val();
        shop.shopCategory={
            shopCategoryId:$("#shop-category").find('option').not(function(){
                return !this.selected;
            }).data('id')
        };
        shop.area={
            areaId:$("#area").find('option').not(function(){
                return !this.selected;
            }).data('id')
        };
        var shopImg=$("#shop-img")[0].files[0];
        var formData=new FormData();
        if(shopImg!=null){
            formData.append('shopImg',shopImg);
        }
        if($("#verifyCode").val()!=null&&$("#verifyCode").val()!=""){
            formData.append('verifyCode',$("#verifyCode").val());
        }else{
            $.toast('请输入验证码');
            return;
        }
        formData.append('shopStr',JSON.stringify(shop));
        $.ajax({
            url:isEdit?editShopUrl:registeShopUrl,
            data:formData,
            type:"POST",
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    $.toast('店铺创建成功');
                }else{
                    $.toast('店铺创建失败！'+data.errMsg);

                }
                $("#verifyCodeImg").click();
            }
        })
    });

    $("#verifyCodeImg").click(function(){
        $("#verifyCodeImg").attr("src",$("#verifyCodeImg").attr("src")+"?"+Math.random());

    });
})