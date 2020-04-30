var initUrl="/So2o/shopadmin/getproductcategorylist";
var queryProduct="/So2o/shopadmin/queryproductbyid";
var queryProductList="/So2o/shopadmin/queryproductlist";
var addProductUrl="/So2o/shopadmin/addproduct";
var modifyProductUrl="/So2o/shopadmin/modifyproduct";
$(function(){
    var productId=getQueryString("productId");
    var isEdit=false;
    if(productId!=null){
        isEdit=true;
        getProductInfo(productId);
    }
    function initProductCategoryList(){
       $.getJSON(initUrl,function (data) {
           if(data.success){
               var categoryList=data.data;
               var html="";
               categoryList.map(function (item) {
                   html+='<option data-id="'+item.productCategoryId+'">'+item.productCategoryName+'</option>';
               });
               $("#category").html(html);

           }
       })
    };

    initProductCategoryList();
    function saveProduct(){
    var formData=new FormData();
    var product={};
    if(isEdit){
        product.productId=$("#productId").val();
    }
    product.productName=$("#productName").val();
    product.productCategory={
        productCategoryId:$("#category").find('option').not(function(){
            return !this.selected;
        }).data('id')
    };
    product.priority=$("#priority").val();
    product.normalPrice=$("#normalPrice").val();
    product.promotionPrice=$("#promotionPrice").val();
    product.productDesc=$("#productDesc").val();
    var smallImg=$("#smallImg")[0].files[0];
    var detailImg=$("#detailImg")[0].files;
    if(product.productName==""){
        $.toast('请输入商品名称');
        return;
    }

    if(product.priority==""){
        $.toast('请输入商品优先级');
        return;
    }
    if(smallImg==null){
        if(!isEdit){
            $.toast('缩略图必须上传');
            return;
        }
    }else{
        formData.append("productImg",smallImg);
    }
    if(detailImg!=null){
        for (var x = 0; x < detailImg.length; x++) {
            formData.append("imgList", detailImg[x]);
        }
    }
    formData.append("productStr",JSON.stringify(product));
        $.ajax({
            url:isEdit?modifyProductUrl:addProductUrl,
            data:formData,
            type:"POST",
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    $.toast('商品修改成功');
                }else{
                    $.toast('商品修改失败！'+data.errMsg);

                }
            }
        })
}
    function getProductInfo(productId){
        $.getJSON(queryProduct,{productId:productId},function(data){
            if(data.success){
                var product=data.data;
                $("#productId").val(product.productId);
                $("#productName").val(product.productName);
                $("#priority").val(product.priority);
                $("#normalPrice").val(product.normalPrice);
                $("#promotionPrice").val(product.promotionPrice);
                $("#productDesc").val(product.productDesc);
                $("#category option" ).each(function() {
                    if($(this).data('id')==product.productCategory.productCategoryId){
                        $(this).attr("selected",true)
                    };
                });
            }
        });
    }



    $(".button-success").click(function(){
    saveProduct();
    });
});