$(document).ready(function(){
    var n=0,t=$(".navbar"),i=t.offset();
    $(".expandable-button").click(function(){
        var n=$(this).attr("id");
        info=$(this).siblings();
        $(".expandable-button").fadeOut(400,function(){
            info.fadeIn()
        })
    });
    $(".x").click(function(){
        $(this).parent().fadeOut(400,function(){
            $(".expandable-button").fadeIn()
        })
    });
    $(".project").click(function(){
        $(".project-details").slideToggle();
        $("html,body").animate({scrollTop:$(this).offset().top},800)
    })
});