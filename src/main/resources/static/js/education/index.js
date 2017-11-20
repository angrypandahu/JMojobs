$(function () {

    var $educationId = $('#educationId');
    var $addEducationId = $('#addEducationId');


    $addEducationId.click(function () {
        var resumeId = $('#educationResumeId').val();
        $.get(
            '/education/create', {'resume.id': resumeId}, function (response, status, xhr) {
                $educationId.html(response);
                $educationId.find('.form_date').datetimepicker({
                    weekStart: 1,
                    todayBtn: 1,
                    autoclose: 1,
                    todayHighlight: 1,
                    startView: 3,
                    minView: 3,
                    forceParse: 0
                });
                $educationId.removeClass('hidden');
            }
        )
    });
    $educationId.addClass('hidden');

});

function editEducation(id) {
    $.get(
        '/education/edit', {id: id}, function (response, status, xhr) {
            $('#education-div-' + id).addClass('hidden');
            $('#education-edit-' + id).html(response).removeClass("hidden")
        }
    )

}
function deleteEducation(id) {
    $('#myModal').find('[name=id]').val(id)
}


function cancelEducation(id) {
    $('#education-div-' + id).removeClass('hidden');
    $('#education-edit-' + id).addClass('hidden');

}
function cancelCreateEducation() {
    $('#educationId').addClass('hidden');

}

function checkIsPresent(obj) {
    $(obj).next().val(obj.checked)
}