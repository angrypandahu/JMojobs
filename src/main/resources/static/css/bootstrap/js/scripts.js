// Empty JS for your own code to be here
function toLogin() {
    window.location.href = '/login'
}
function toLogout() {
    window.location.href = '/logout'
}
function toRegister() {
    window.location.href = '/register/register'
}

$(function () {
    $('#resumeDetail').find('a').each(function () {
        var href = $(this).attr('href');
        href = href.substring(href.lastIndexOf("/"));
        var currentHref = window.location.href;
        if (currentHref.indexOf(href) > -1) {
            $(this).parent().addClass('active');
            $('#oneResumeDetail').html($(this).html())
        }

    });
    $('#mainNavbar').find('a').each(function () {
        var href = $(this).attr('href');
        var currentHref = window.location.href;
        var endWith = currentHref.substring(currentHref.lastIndexOf("/"));
        if (href == endWith) {
            $(this).parent().addClass('active');
        }
    });
});