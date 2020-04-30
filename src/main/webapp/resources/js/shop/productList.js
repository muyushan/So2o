$(function(){
    function getList() {
        $.ajax({
            url:'/So2o/shopadmin/queryproductlist',
            type:"get",
            dataType:"json",
            success:function(data){
                // handleUserInfo(data.user);
                handleProductList(data.productList);
        }});
    }
    function changeEnableStatus(productId){
        $.getJSON("/So2o/shopadmin/on_off",{productId:productId},function(data){
            if(data.success){
                getList();
            }
        });
    }

    function handleProductList(productList){
        var html="";
        productList.map(function (value, index) {

            var t=value.enableStatus==0?'上架':'下架';
            var className=value.enableStatus==0?'button button-round on_off':'button button-round button-warning on_off';
        html+='<li> ' +
            '<div class="item-content"> ' +
            '<div class="item-media"><img src="images'+value.imgAddr+'" width="44"></div> ' +
            '<div class="item-inner"> ' +
            '<div class="item-title-row"> ' +
            '<div class="item-title" style="cursor: pointer"  data-id="'+value.productId+'">'+value.productName+'</div> ' +
            '<div style="float：right;">' +
            '<a data-id="'+value.productId+'" class="'+className+'">'+t+'</a> <a style="display: inline;" data-id="'+value.productId+'" class="button button-round">预览</a></div>'+
            '</div> '+
            '<div class="item-subtitle">'+value.productDesc+'</div> ' +
            '</div> '+
            '</div> '+
            '</li>';
        });

        $(".media-list").children("ul").html(html);
        $(".media-list").find(".item-title").on('click',function(e){
            var target=e.currentTarget;
            window.location.href="/So2o/shopadmin/productoperation?productId="+target.dataset.id;
        });

        $(".media-list").find(".on_off").on('click',function(e){
            var target=e.currentTarget;
            $.confirm("确定要修改上架状态？",function(){
                changeEnableStatus(target.dataset.id);
            });
        });
    }



    getList();
})