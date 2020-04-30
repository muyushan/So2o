$(function(){
    function getList() {
        $.ajax({
            url:'/So2o/shopadmin/getproductcategorylist',
            type:"get",
            dataType:"json",
            success:function(result){
                handleProductCategoryList(result.data);
        }});
    }
    function handleProductCategoryList(categoryList){
        var html="";
        categoryList.map(function (value, index) {
            html+='<div class="row row_category now">\n' +
                '            <div class="col-33">'+value.productCategoryName+'</div>\n' +
                '            <div class="col-33">'+value.priority+'</div>\n' + '<div class="col-33"><a href="#" class="button button-round" data-id="'+value.productCategoryId+'">删除</a></div></div>"';
        });

        $(".categoryWarp").html(html);
    }
    $("#createNew").click(function(){
       var appendHtml="";
        appendHtml+='<div class="row row_category temp">\n' +
            '            <div class="item-input col-33"><input type="text" class="categoryName" placeholder="请输入分类名称"/> </div>\n' +
            '            <div class="item-input col-33"><input type="text" class="categoryPriority" placeholder="请输入分类优先级"/></div>\n' + '<div class="item-input col-33"><a href="#" class="button button-round">删除</a></div></div>'+"";
        $(".categoryWarp").append(appendHtml);
    });
    $("#saveNew").click(function(){
        var temps=$(".temp");
        var tempObjArray=[];
        temps.map(function (index,item) {
            var tempObj={};
            tempObj["productCategoryName"]=$(item).find(".categoryName").val();
            tempObj["priority"]=$(item).find(".categoryPriority").val();
            if( tempObj["productCategoryName"]&& tempObj["priority"]){
                tempObjArray.push(tempObj);
            }
        });
        if(tempObjArray.length>0){
            $.ajax({
                url:"/So2o/shopadmin/addproductcategory",
                type:"POST",
                contentType:"application/json",
                dataType:"json",
                data:JSON.stringify(tempObjArray),
                success:function(data){
                    if(data.success){
                        $.toast("添加成功");
                        getList();
                    }else{
                        $.toast(data.errMsg);
                    }
                }


            });
        }else{
            $.toast('请填写类别信息');
        }
    });
    $(".categoryWarp").on('click', ".row_category.temp .button", function(){
        $(this).parent().parent().remove();
    });
    $(".categoryWarp").on('click', ".row_category.now .button", function(e){
        var target=e.currentTarget;
        $.confirm("确定删除么？",function(){
            $.ajax({
                url:"/So2o/shopadmin/deletecategory",
                data:{categoryId:target.dataset.id},
                method:"POST",
                success:function(data){
                    if(data.success){
                        $.toast("删除完成");
                        getList();
                    }else{
                        $.toast("删除失败");
                    }
                }

            })
        })

    });


    getList();
})