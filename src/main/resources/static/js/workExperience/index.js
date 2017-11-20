$(function () {

    var $workExperienceId = $('#workExperienceId');
    var $addWorkExperienceId = $('#addWorkExperienceId');


    $addWorkExperienceId.click(function () {
        var resumeId = $('#workExperienceResumeId').val();
        $.get(
            '/workExperience/create', {'resume.id': resumeId}, function (response, status, xhr) {
                $workExperienceId.html(response);
                $workExperienceId.find('.form_date').datetimepicker({
                    weekStart: 1,
                    todayBtn: 1,
                    autoclose: 1,
                    todayHighlight: 1,
                    startView: 3,
                    minView: 3,
                    forceParse: 0
                });
                $workExperienceId.removeClass('hidden');
            }
        )
    });
    $workExperienceId.addClass('hidden');

});

function editWorkExperience(id) {
    $.get(
        '/workExperience/edit', {id: id}, function (response, status, xhr) {
            $('#workExperience-div-' + id).addClass('hidden');
            $('#workExperience-edit-' + id).html(response).removeClass("hidden")
        }
    )

}
function deleteWorkExperience(id) {
    $('#myModal').find('[name=id]').val(id)
}


function cancelWorkExperience(id) {
    $('#workExperience-div-' + id).removeClass('hidden');
    $('#workExperience-edit-' + id).addClass('hidden');

}
function cancelCreateWorkExperience() {
    $('#workExperienceId').addClass('hidden');

}

function checkIsPresent(obj) {
    $(obj).next().val(obj.checked)
}