// JS code for NewAdvance search new design logic
$(function(){
        $(document).on('click', '.frmcheckwrap', function(){
                $(this).toggleClass('active');
        });

        $('#opnadvance').click(function(){
                $(this).remove();
                $('.advancewrap').show();
                $('.srch_wrap').css('padding-bottom','10px');
        });
	jQuery('.rgttablnk').click(function(){
                var ele = jQuery(this);
                if(ele.data('content') == 'topcomp')
                {
                        jQuery('.rgttablnk').removeClass('active');
                        ele.addClass('active');
                        jQuery('#topcons').hide();
                        jQuery('#'+ele.data('content')).show();
                }
                else if (ele.data('content') == 'topcons')
                {
                        jQuery('.rgttablnk').removeClass('active');
                        ele.addClass('active');
                        jQuery('#topcomp').hide();
                        jQuery('#'+ele.data('content')).show();
                }
        });
	jQuery('.frmcheckwrap').click(function(){
		if(!jQuery(this).hasClass('active')){
			$("#ans").val(1);
		} else {
			$("#ans").val(0);
		}
	});
	$(function(){         
                $("select[name='ind']").change(function(){                
                        loadFA(this, document.myform.jbc, 0, '', 'Function');                
                        $("select[name='jbc']").selectpicker('refresh', true);
                
                        loadRoles(document.myform.jbc,document.myform.jbr,0,'0','Role');                
                        $("select[name='jbr']").selectpicker('refresh', true);
                        $("select[name='ind']").selectpicker('refresh', true);
                });
        
                $("select[name='jbc']").change(function() {
                        loadRoles(document.myform.jbc,document.myform.jbr,0,'0','Role');                
                        $("select[name='jbr']").selectpicker('refresh', true);
                        $("select[name='jbc']").selectpicker('refresh', true);
                });     
        
                $("select[name='jbr']").change(function() {
                        $("select[name='jbr']").selectpicker('refresh', true);
                });
	});
	$('.viewall').click(function(){
		$('.field_hide').show();
		$('.viewall').hide();
		$('.hideall').show();
		$('.ind_mob').addClass('mob_show');
		$('.func_mob').addClass('mob_show');
       	});
	$('.hideall').click(function(){
                $('.field_hide').hide();
                $('.viewall').show();
                $('.hideall').hide();
                $('.ind_mob').removeClass('mob_show');
                $('.func_mob').removeClass('mob_show');
       	});
	$('#mn-loader').show();
	setTimeout(function(){
		$('#mn-loader').hide();
	}, 3000);
	$('.close_key').click(function(){
		$('.choose').hide();
       	});
	$('#fts_id,#lmy').click(function(){
                jQuery('#workhist-ui-id-1,#workhist-ui-id-2').addClass('autocomplete_maindiv  autocomplete_subdiv');
        });
});

function getSalaryVal(counter,selMin,selMax) {
        var salVal;
        var salMaxVal = 0;
        var salFinalval = 0;
	var salValue;
	var salMaxValue;
        if(selMin){ selMin = selMin+'00000'; jQuery('input[name="mns"]').filter('[value='+selMin+']').attr('checked', true); }
        if(selMax) { selMax = selMax+'000'; jQuery('input[name="mxs"]').filter('[value='+selMax+']').attr('checked', true); }
        if(jQuery('input[name="mns"]').is(':checked')) {
                salVal = salVal*1;
                if(selMin){ salVal = selMin; }
                else {
                salValue = jQuery('input[name="mns"]:checked').val(); }
                salVal = salValue;
        }
        if(jQuery('input[name="mxs"]').is(':checked')) {
                if(selMax) { salMaxVal = selMax; }
                else {
                        salMaxValue = jQuery('input[name="mxs"]:checked').val();
                }
                salMaxVal = salMaxValue*1;
                if(salVal == undefined) {
                        salVal=0;
                }
                salVal = salVal+" - "+salMaxVal;
        }

        jQuery("#salary_"+counter).val(salVal);

        if(salVal != null)
                jQuery('#salary_'+counter+'_value').html(salVal);
}

function resetSalaryVal(counter) {
	if(jQuery("input[type='radio'].from_min").is(':checked')) {
		jQuery('input[name=mns]').attr('checked', false);
	}
	if(jQuery("input[type='radio'].from_max").is(':checked')) {
		jQuery('input[name=mxs]').attr('checked', false);
	}
	jQuery('#salary_'+counter+'_value').html('Salary(in Lacs)');
	jQuery("#salary_"+counter).val('');
}

/*search tips*/
var item = 0;
var slidetimeer ='';

function rotateslidemain(j){
	var mrgn = j * 240;
	$('.tipslidenavitem').removeClass('active');
	$('.tipslidenavitem').eq(j).addClass('active');
        if(Mons.LANG == 'ar'){
	$('.tipslideinnr').animate({'margin-right': '-'+mrgn+'px'}, 500);
	}else{
	 $('.tipslideinnr').animate({'margin-left': '-'+mrgn+'px'}, 500);
	}

}
	
function rotateslide(){
	console.log(item);
	slidetimeer = setInterval(function(){
		item++;
		if(item > 2){
			item = 0;
		}
		rotateslidemain(item);
	}, 4500);
}
	
rotateslide();

$(function(){
	$(document).on('click', '.tipslidenavitem', function(){
		if(!$(this).hasClass('active')){
			item = $(this).index();
			clearInterval(slidetimeer);
			rotateslidemain($(this).index());
			rotateslide();
		}
	});
});
/*search tips*/
$( document ).ready(function() {
        jQuery('#workhist-ui-id-1,#workhist-ui-id-2').addClass('autocomplete_maindiv  autocomplete_subdiv');
	if(typeof roleDataTargetVal !== 'undefined') {
		$('#roleDataTarget').click();
	}
	if(typeof freshnessDataTargetVal !== 'undefined') {
                $('#freshnessDataTarget').click();
        }
	if(typeof locationDataTargetVal !== 'undefined') {
                $('#locationDataTarget').click();
        }
	if(typeof companyTypeDataTargetVal !== 'undefined') {
                $('#companyTypeDataTarget').click();
        }
	if(typeof experienceDataTargetVal !== 'undefined') {
                $('#experienceDataTarget').click();
        }
	if(typeof salaryDataTargetVal !== 'undefined') {
                $('#salaryDataTarget').click();
        }
	if(typeof industrydataTargetVal !== 'undefined') {
                $('#industrydataTarget').click();
        }
	if(typeof functionDataTargetVal !== 'undefined') {
                $('#functionDataTarget').click();
        }
});