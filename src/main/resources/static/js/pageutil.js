function myPage(currentPage, totalPages, url) {
    var element = $('#bp-element');
    options = {
        bootstrapMajorVersion: 3, //对应的bootstrap版本
        currentPage: currentPage, //当前页数，这里是用的EL表达式，获取从后台传过来的值
        numberOfPages: 5, //每页页数
        totalPages: totalPages, //总页数，这里是用的EL表达式，获取从后台传过来的值
        shouldShowPage: true,//是否显示该按钮
        itemTexts: function (type, page, current) {//设置显示的样式，默认是箭头
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "上一页";
                case "next":
                    return "下一页";
                case "last":
                    return "末页";
                case "page":
                    return page;
            }
        },
        //点击事件
        onPageClicked: function (event, originalEvent, type, page) {
            // location.href = url + (url.indexOf("?") > -1) ? "&" : "?" + "page=" + page;
            location.href = "/self?event=toUserOperaLog&page=" + 1;
        }
    };
    element.bootstrapPaginator(options);
}
