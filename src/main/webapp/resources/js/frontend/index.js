$(function() {
    var url="/So2o/frontend/listmainpageinfo";

    $.getJSON(url,function(data){
        if(data.success){
            if(data.shopCategoryList){
                var htmlStr="";
                data.shopCategoryList.map(function(item,index){
                        if((index+1)%2!=0){
                            htmlStr+="<div class=\"row\">\n";
                        }
                        htmlStr+=
                        "                <div class=\"col-50\">\n" +
                        "                    <div data-id='"+item.shopCategoryId+"' class=\"list-block media-list inset\">\n" +
                        "                        <ul>\n" +
                        "                            <li>\n" +
                        "                                <a href=\"#\" class=\"item-content\">\n" +
                        "                                    <div class=\"item-inner\">\n" +
                        "                                        <div class=\"item-title-row\">\n" +
                        "                                            <div class=\"item-title\">"+item.shopCategoryName+"</div>\n" +
                        "                                        </div>\n" +"<div class=\"item-subtitle\">"+item.shopCategoryDesc+"</div>"+
                        "                                    </div>\n" +
                        "                                    <div class=\"item-media\" style=\"padding-right: 1rem\"><img src=\"shopcategory"+item.shopCategoryImg+"\"style='width: 2.2rem;'></div>\n" +
                        "                                </a>\n" +
                        "                            </li>\n" +
                        "                        </ul>\n" +
                        "                    </div>\n" +
                        "                </div>\n";
                    if ((index+1)%2==0||index==data.shopCategoryList.length){
                        htmlStr+="</div>"
                    }
                });
                $(".categorywarraper").html(htmlStr);
                $(".list-block").on('click',function(e){
                    window.location.href="/So2o/frontend/shoplistsearch?parentId="+e.currentTarget.dataset.id;
                })
            }

            if(data.headLineList){
                var htmlStr="";
                data.headLineList.map(function(item,index){
                htmlStr+="<div class=\"swiper-slide\"><img src=\"headtitle"+item.lineImg+"\" alt=\"\"></div>";
                });
                $(".swiper-wrapper").html(htmlStr);

                var mySwiper = new Swiper ('.swiper-container', {
                    // Optional parameters
                    loop: true,
                    // If we need pagination
                    pagination: {
                        el: '.swiper-pagination',
                        type: 'progressbar',
                    },

                    // And if we need scrollbar
                    scrollbar: {
                        el: '.swiper-scrollbar',
                    },
                    autoplay: {//自动切换
                        delay: 3000,
                        stopOnLastSlide: false,
                        disableOnInteraction: false,
                    },
                });
            }
        }
    });



});