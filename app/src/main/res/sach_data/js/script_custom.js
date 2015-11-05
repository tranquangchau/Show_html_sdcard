$.mobile.loading("hide");
// (or presumably as submitted by @Pnct)
$.mobile.loading().hide();
var bien1 = 1, bien2 = 2;
$(function () {
    $(document).on('vmousedown vmouseup', function (event) {
        //holdCords.holdX = event.pageX;
        holdCords.holdX = event.clientX;
        //holdCords.holdY = event.pageY;
        holdCords.holdY = event.clientY;

//        if (event.pageX != undefined && event.pageY != undefined) {
//            holdCords.holdX = event.pageX;
//            holdCords.holdY = event.pageY;
//        }
//        else {
////            holdCords.holdX = event.clientX + document.body.scrollLeft +document.documentElement.scrollLeft;
////            holdCords.holdY = event.clientY + document.body.scrollTop +document.documentElement.scrollTop;
//            holdCords.holdX = 0;
//            holdCords.holdY = 0;
//        }
        $('body').click(function () {
            // do something here
            var $menu = $('#menu');
            $menu.hide();
        });

    });

    //$(document).on('taphold', function(e){
    //alert('X: ' + holdCords.holdX + ' Y: ' + holdCords.holdY ); 
    //});

    var holdCords = {
        holdX: 0,
        holdY: 0
    }

    var $menu = $('#menu');
    var $wMenu = $menu.outerWidth();
    var $hMenu = $menu.outerHeight();
    var $menuSub = $('#menu ul');

    var $leftSub = parseFloat($menuSub.css('left'), 10);
    var $wSub = $menuSub.outerWidth();
    $(".khoang").bind('taphold', function (e) {

        //var xm = $('#contaner').attr('class');
        //alert(e.class);
        //var myClass = $(this).attr("class");
        // alert(myClass);

        var list_bai = $(this).attr("itemid");
        //alert( list_bai );
        var string = list_bai;
        var array = string.split(",");
        //alert(array[0]);
        bien1 = array[0], bien2 = array[1];

        var $leftM = holdCords.holdX, $topM = holdCords.holdY;
        //console.log('left: '+$leftM + ' top: '+$topM);
        //console.log(holdCords.holdX);
        var $rightM = $(this).width() - $leftM;
        var $bottomM = $(this).height() - $topM;
        if ($rightM < $wMenu) {
            $leftM -= $wMenu;
        }
        if ($bottomM < $hMenu) {
            $topM -= $hMenu;
        }
        $menu.css({left: $leftM, top: $topM, display: 'block'});

        //video2
        var $rsubMenu = $(this).width() - $leftM - $wMenu;
        if ($rsubMenu < $wSub) {
            $menuSub.css('left', -$leftSub);
        } else {
            $menuSub.css('left', $leftSub);
        }
        e.preventDefault();
    }).click(function () {
        $menu.hide();
    });
});

function send_data() {
    //alert("Send data");
    //alert(bien1 + " to " + bien2);
    Android.play_part(parseInt(bien1), parseInt(bien2));
    //Android.play_part(3,5);
}

function test1() {
    //alert("Function test1");
    var $menu = $('#menu');
    $menu.hide();

    var a = document.getElementsByTagName('p');
    for (i = 0; i < a.length; i++) {
        a[i].classList.remove('active');
    }

    a[bien1 - 1].classList.add('active');
    //elem.classList.add('active');

    send_data();
}


function test2() {
    //alert("Function test2");
    var $menu = $('#menu');
    $menu.hide();
}


//script for android
function showAndroidToast(toast, elem) {
    var a = document.getElementsByTagName('p');
    for (i = 0; i < a.length; i++) {
        a[i].classList.remove('active');
    }
    elem.classList.add('active');
    //alert(toast);	
    Android.showToast(toast);
//alert(data_link_mp3[toast]);		

}
function showAndroidToast1(toast) {
    //alert("1234");
    var x = document.getElementsByTagName('p');
    for (i = 0; i < x.length; i++) {
        x[i].classList.remove('active');
    }
    //elem.classList.add('active');
    x[3].classList.add('active');

    //x[2].classList.remove("active");
    Android.showToast(3);
}
function showAndroidToast0(toast) {
    //alert("1234");
    var x = document.getElementsByTagName('p');
    for (i = 0; i < x.length; i++) {
        x[i].classList.remove('active');
    }
    //elem.classList.add('active');
    x[toast].classList.add('active');
    jump(toast);
    //x[2].classList.remove("active");
    //Android.showToast(3);
}

function showAndroidToast11(toast) {
    //alert("1234");
    var x = document.getElementsByTagName('p');
    for (i = 0; i < x.length; i++) {
        x[i].classList.remove('active');
    }
    //elem.classList.add('active');
    x[toast].classList.add('active');
    //jump(toast);
    //x[2].classList.remove("active");
    //Android.showToast(3);
}

function goBack() {
    window.history.back();
}

function stopPlay() {
    Android.stopPlay();
}



function get_bytag() {
    var x = document.getElementsByTagName('p');
    //document.getElementById("demo").innerHTML = x[2].innerHTML;
    //x[2].style.backgroundColor =    'green';
    //alert("1234");
    x[2].classList.add('active');
    //x[2].classList.remove("active");
}
function get_bytag3() {
    var x = document.getElementsByTagName('p');
    //document.getElementById("demo").innerHTML = x[2].innerHTML;
    //x[2].style.backgroundColor =    'green';

    x[3].onclick = showAndroidToast1('http://developer.j-tec.com.vn/projects/android/nhac/iphone_6_plus.mp3');
}

function jump(h) {

    var x1 = document.getElementsByTagName('p');
    $('html, body').animate({
        scrollTop: $(x1[h]).offset().top-100
    }, 200);
}

function jump1(h) {
    var x = document.getElementsByTagName('p');
    var top = x[h].offsetTop;
    //var top = document.getElementById(h).offsetTop;
    window.scrollTo(0, top);
}

