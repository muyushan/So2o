$(function(){
    var loading=false;
    var maxItem=999;
    var pageSize=10;
    var searchDivUrl='/So2o/frontend/listshopspageinfo';
    var listUrl='/So2o/frontend/listshops';

    var pageNum=1;
    var parentId=getQueryString("parentId");
    var areaId="";
    var shopCategoryId="";
    var shopName="";
    function getSearchDivData(){
        var url=searchDivUrl+"?parentId="+parentId;
        $.getJSON(url,function(data){
            if(data.success){
                var shopCategoryList=data.shopCategoryList;
                var html="";
                html+="<div class=\"row\" style=\"margin-top: 3px;\">";
                shopCategoryList.map(function (value, index) {
                    html+="<div class=\"col-33\"><a href=\"#\" class=\"button\" data-categoryId='"+value.shopCategoryId+"'>"+value.shopCategoryName+"</a></div>\n";
                    if((index+1)%3==0){
                        html+="</div>";
                        if(index<shopCategoryList.length){
                            html+="<div class=\"row\" style=\"margin-top: 3px;\">";
                        }
                    }

                });
                $("#shoplist-search-div").html(html);

                html="";
                var areaList=data.areaList;
                html += '<option value="">全部街道</option>';
                areaList.map(function(item,index){
                    html += '<option value="'
                        + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $("#area-search").html(html);

            }
        });
    }
    getSearchDivData();
    function addItems(pageSize,pageIndex){
        loading=true;
        $.getJSON(listUrl,{pageIndex:pageIndex,pageSize:pageSize,parentId:parentId,areaId:areaId,shopCategoryId:shopCategoryId,shopName:shopName},function(data){
            if(data.success){

                var shopList=data.shopList;
                maxItem=data.count;
                var html="";
                shopList.map(function (value, index) {
                    html+="<div class=\"card\" data-shopId='"+value.shopId+"'>\n" +
                        "                        <div class=\"card-header\">"+value.shopName+"</div>\n" +
                    "                        <div class=\"card-content\">\n" +
                    "                            <div class=\"list-block media-list\">\n" +
                    "                                <ul>\n" +
                    "                                    <li class=\"item-content\">\n" +
                    "                                        <div class=\"item-media\">\n" +
                    "                                            <img src=\"images"+value.shopImg+"?shopId="+value.shopId+"\" width=\"44\">\n" +
                    "                                        </div>\n" +
                    "                                        <div class=\"item-inner\">\n" +
                    "                                            <div class=\"item-subtitle\">"+value.shopDesc+"</div>\n" +
                    "                                        </div>\n" +
                    "                                    </li>\n" +
                    "                                </ul>\n" +
                    "                            </div>\n" +
                    "                        </div>\n" +
                    "                        <div class=\"card-footer\">\n" +
                    "                            <span>"+new Date(value.lastEditTime).Format('yyyy-MM-dd')+"</span>"+
                    "                            <span>5 评论</span>\n" +
                    "                        </div>\n" +
                    "                    </div>";
                });

                $(".shop-list").append(html);
                var total=$(".card").length;
                if(total>=maxItem){
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    $(".infinite-scroll-preloader").hide();

                }else{
                    $(".infinite-scroll-preloader").show();
                }
                pageNum+=1;
                loading=false;
                $.refreshScroller();


            }
        });
    }
    addItems(pageSize, pageNum);
    $(document).on('infinite', '.infinite-scroll-bottom', function() {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });

    $(".shop-list").on('click','.card',function(e){
        alert(e.currentTarget.dataset.shopid);
    });
    $("#shoplist-search-div").on('click','.button',function(e){
        if(parentId){
            shopCategoryId=e.currentTarget.dataset.categoryid;
            if($(e.target).hasClass("button-fill")){
                $(e.target).removeClass("button-fill")
                shopCategoryId='';

            }else{
                $("#shoplist-search-div").find(".button").removeClass('button-fill');
                $(e.target).addClass('button-fill');
            }
            $('.list-div').empty();
            pageNum=1;
            addItems(pageSize,pageNum);
        }else{
            parentId = e.target.dataset.categoryid;
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                parentId = '';
            } else {
                $("#shoplist-search-div").find(".button").removeClass('button-fill');
                $(e.target).addClass('button-fill');
            }
            $('.list-div').empty();
            pageNum = 1;
            addItems(pageSize, pageNum);
            parentId = '';
        }
    });

    $('#search').on('input', function(e) {
        shopName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    $('#area-search').on('change', function() {
        areaId = $('#area-search').val();
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    $.init();

});
