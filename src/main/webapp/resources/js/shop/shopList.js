$(function(){
    function getList() {
        $.ajax({
            url:'/So2o/shopadmin/getShopList',
            type:"get",
            dataType:"json",
            success:function(data){
                handleUserInfo(data.user);
                handleShopList(data.shopList);
        }});
    }

    function handleUserInfo(personInfo){
        $("#user_name").text(personInfo.name);
    }
    function handleShopList(shopList){
        var html="";
        shopList.map(function (value, index) {
            html+=' <div class="row row_shop">\n' +
                '            <div class="col-40">'+value.shopName+'</div>\n' +
                '            <div class="col-40">'+getShopStats(value.enableStatus)+'</div>\n' +
                '            <div class="col-20">'+goShop(value.enableStatus,value.shopId)+"</div></div>";

        });

        $(".shopWarp").html(html);


    }

    function getShopStats(status){
        if(status==1){
            return "审核通过";
        }else if(status==0){
            return "审核中";
        }else{
            return "店铺非法";
        }
    }

    function goShop(status,shopId){
        if(status==1){
            return '<a href="/So2o/shopadmin/shopmanagement?shopId='+shopId+'">进入</a>';
        }else{
            return "";
        }
    }
    getList();
})