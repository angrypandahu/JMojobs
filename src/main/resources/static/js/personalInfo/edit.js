$(function () {
    initUpload();
    initUpdateInfo();
});

function initUpload() {
    $('#changePhoto').click(function () {
        $(this).next().click();
    });
    $('form[name=uploadPhoto]').ajaxForm({
        success: function (data) {
            if (data) {
                $('#uploadPhotoId').html(data);
                initUpload();
            }
        }
    });
    $('#exampleInputFile').change(function () {
        $('form[name=uploadPhoto]').submit();
    });
}

function initUpdateInfo() {
    $('form[name=updateInfo]').ajaxForm({
        success: function (data) {
            if (data) {
                $('#updateInfoId').html(data);
                initUpdateInfo();
            }
        }
    });
}