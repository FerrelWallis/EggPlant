/**
 * Created by GavinGu on 2016/4/27.
 */

$(function(){

    $(".yScrollListInList .yScrollListbtnl").click(function(){

        var items_w = $(this).parent(".yScrollListInList1").find(' .list-items').width();

        var box_w = $(this).parent(".yScrollListInList1").width();

        var item_w = $(this).parent(".yScrollListInList1").find(' .list-item').width();

        var item_mr_w = parseInt($(this).parent(".yScrollListInList1").find(' .list-item').css('marginRight'))

        var item_ml_w = parseInt($(this).parent(".yScrollListInList1").find(' .list-item').css('marginLeft'))

        var border_w = parseInt($(this).parent(".yScrollListInList1").find(' .list-item').css('borderRightWidth'))

        $(this).parent(".yScrollListInList1").find(' .list-items').css({width: $(this).parent(".yScrollListInList1").find(' .list-item').length*(item_w+item_ml_w+item_mr_w+border_w*2)+"px"});

        var numwidth=item_w+item_mr_w+item_ml_w;

        var obj=$(this).parent(".yScrollListInList").find(".list-items");

        if(items_w>box_w){

            if (!(obj.is(":animated"))) {

                var lefts=parseInt(obj.css("left").slice(0,-2));

                if(lefts<0){

                    obj.animate({left:lefts+numwidth},200);

                }
            }
        }

    });
    $(".yScrollListInList .yScrollListbtnr").click(function(){

        var items_w = $(this).parent(".yScrollListInList1").find(' .list-items').width();

        var box_w = $(this).parent(".yScrollListInList1").width();

        var item_w = $(this).parent(".yScrollListInList1").find(' .list-item').width();

        var item_mr_w = parseInt($(this).parent(".yScrollListInList1").find(' .list-item').css('marginRight'))

        var item_ml_w = parseInt($(this).parent(".yScrollListInList1").find(' .list-item').css('marginLeft'))

        var border_w = parseInt($(this).parent(".yScrollListInList1").find(' .list-item').css('borderRightWidth'))

        $(this).parent(".yScrollListInList1").find(' .list-items').css({width: $(this).parent(".yScrollListInList1").find(' .list-item').length*(item_w+item_ml_w+item_mr_w+border_w*2)+"px"});

        var numwidth=item_w+item_mr_w+item_ml_w;

        var obj=$(this).parent(".yScrollListInList").find(".list-items");

        var objcds=-(30+(Math.ceil(obj.find(".list-item").length)-2)*numwidth);

        if(items_w>box_w){

            if (!(obj.is(":animated"))) {

                var lefts=parseInt(obj.css("left").slice(0,-2));

                if(lefts > objcds){

                    obj.animate({left:lefts-numwidth},200);

                }

            }
        }

    })
});
$(function () {

    $('.items_bar .list-item').click(function () {

        $(this).addClass('active').siblings().removeClass('active');

        $(this).find('.sec-list-items').slideToggle();

        $(this).siblings().find('.sec-list-items').slideUp()

    });

    $('.items_bar .sec-list-item').click(function () {
        $(this).addClass('active').siblings().removeClass('active');
        event.stopPropagation();
        window.event.cancelBubble = true;
    })
});
$(function () {
   var item_w = $('.items_f').width();

    var item_num = $('.items_f').find('.list-item').length;

    var item_w_d = item_w/item_num-1;//@authot zhanlingzhi -1 否则ff下会掉下来

    $('.items_f').find('.list-item').width(item_w_d);

    $('.items_f').find('.list-item').hover(
        function () {
            // $(this).addClass('active').siblings().removeClass('active');
            $(this).css('background','#076543');
            $(this).find('a:first').css('color','#fff')
            $(this).find('.sec-list-items').stop().slideDown()
        },
        function () {
            // $(this).removeClass('active');
            $(this).css('background','#fff');
            $(this).find('a:first').css('color','#076543')
            $(this).find('.sec-list-items').stop().slideUp()
        }
    );
    $('.query-box').hover(
        function () {
            $('#slide-box').stop().slideDown();
        },
        function () {
            $('#slide-box').stop().slideUp();
        }
    );
});
function fl_hold(e){
    $(e).stop().slideDown()
}
function  fl_hide(e){
    $(e).stop().slideUp()
}

$(function () {
   var square_w = $('.square-items li').width();

    $('.square-items li').height(square_w+25);

});


$(function () {
    var pag_w = $('.Pagination div').width()/2;
    $('.Pagination div').css('marginLeft',-pag_w);
})

    
$(function () {
     function $(id){
        return typeof id==='string'? document.getElementById(id) : id;
    }
obj_qikanlunwen = $("container-tit2");
if(obj_qikanlunwen ){
        window.onload=function(){
        // 标签的索引
        var index=0;
        var timer=null;

        var lis=$('container-tit2').getElementsByTagName('li'),
            divs=$('container-con2').getElementsByTagName('div');

        if(lis.length!=divs.length) return;

        // 遍历所有的页签
        for(var i=0;i<lis.length;i++){
            lis[i].id=i;
            lis[i].onmouseover=function(){
                // 用that这个变量来引用当前滑过的li
                var that=this;
                // 如果存在准备执行的定时器，立刻清除，只有当前停留时间大于100ms时才开始执行
                if(timer){
                    clearTimeout(timer);
                    timer=null;
                }
                // 延迟0.1秒执行
                timer=window.setTimeout(function(){
                    for(var j=0;j<lis.length;j++){
                        lis[j].className='list-item list-tap-item ';
                        divs[j].style.display='none';
                    }
                    lis[that.id].className='list-item list-tap-item active';
                    divs[that.id].style.display='block';
                },100);
            }
        }
    }
    }
});



