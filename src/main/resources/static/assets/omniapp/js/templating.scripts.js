function doAddNewServiceTemplate() {

	var nameError = $('#service_template_name_error');
	var descriptionError = $('#service_template_description_error');
	var priceError = $('#service_template_price_error');
	var categoryError = $('#service_template_category_error');
	var name = $('#input_new_service_template_name').val();
	var description = $('#input_new_service_template_description').val();
//	var price = $('#input_new_service_template_price').val();
	var category = $('#input_new_service_template_category').val();

	nameError.hide('fast');
	descriptionError.hide('fast');
	//priceError.hide('fast');
	categoryError.hide('fast');

	$
			.ajax({
				type : "POST",
				url : '/add-service-template',
				data : "name=" + name + "&description=" + description
						+"&category="+category,
				success : function(response) {
					if (response.status == "SUCCESS") {
						toastr.success("Template added successfully",
								"Well done!");

						$('#input_new_service_template_name').val('');
						$('#input_new_service_template_description').val('');
						//$('#input_new_service_template_price').val('');
						$('#input_new_service_template_category').val(' ');
						
						$('#modal_new_service_template').modal('hide');
						$('#service_templates_datatable').mDatatable('reload');


					} else {

						for (i = 0; i < response.result.length; i++) {

							if (response.result[i].code == "service.template.name.empty")
								nameError.show('slow');
							if (response.result[i].code == "service.template.description.empty")
								descriptionError.show('slow');
//							if (response.result[i].code == "service.template.price.empty")	
//								priceError.show('slow');
							if (response.result[i].code == "service.template.category.empty")	
								categoryError.show('slow');
//							if (price != '' && response.result[i].code == "service.template.price.undefined") {
//								toastr.warning("Price must be logic",
//										"Check price");
//							}

						}

					}
					
					
				},
				error : function(e) {
					toastr.error("Couldn't add template", "Server Error");
				}
			});
}

function handleRemoveServiceClick(id) {
	var serviceId = $("#btn-remove-template-" + id).attr('id').substring(20);

	swal({
		title : 'Are you sure?',
		text : "You won't be able to revert this!",
		type : 'warning',
		showCancelButton : true,
		confirmButtonText : 'Yes, delete it!'
	}).then(
			function(result) {
				if (result.value) {
					$
							.ajax({
								type : "POST",
								url : '/delete-service-template',
								data : "serviceId=" + serviceId,
								async : true,
								success : function(response) {
									if (response.status == "SUCCESS") {
										swal('Deleted!',
												'Template has been deleted.',
												'success');
										$('#service_templates_datatable').mDatatable('reload');

									} else {
										swal('Fail!', 'Template not deleted.',
												'error');
									}

								},
								error : function(e) {
									toastr.error("Couldn't delete template",
											"Server Error");
									result = false;
								}
							});

				}

			}

	);

}

function handleRemoveTaskTemplateClick(id) {
	var taskId = $("#btn-remove-task-template-" + id).attr('id').substring(25);

	swal({
		title : 'Are you sure?',
		text : "You won't be able to revert this!",
		type : 'warning',
		showCancelButton : true,
		confirmButtonText : 'Yes, delete it!'
	}).then(
			function(result) {
				if (result.value) {
					$
							.ajax({
								type : "POST",
								url : '/delete-task-template',
								data : "taskId=" + taskId,
								async : true,
								success : function(response) {
									if (response.status == "SUCCESS") {
										swal('Deleted!',
												'Task Template has been deleted.',
												'success');
										$('#service_template_tasks_datatable').mDatatable('reload');

									} else {
										swal('Fail!', 'Template not deleted.',
												'error');
									}

								},
								error : function(e) {
									toastr.error("Couldn't delete template",
											"Server Error");
									result = false;
								}
							});

				}

			}

	);

}

function saveServiceTemplateTask(name, estimationHR, estimationDays,priority, serviceId) {
	var result = false;

	$.ajax({
		type : "POST",
		url : '/new-service-template-save-task',
		data : "name=" + name + "&estimationHR=" + estimationHR
				+ "&estimationTime=" + estimationDays + "&priority=" + priority + "&serviceId="
				+ serviceId,
		async : false,
		success : function(response) {
			if (response.status == "SUCCESS") {
				$("#add-service-template-tasks").modal('hide');
				result = true;

			} else {
				toastr.warning("Please fill all tasks informations", "Warning");

				result = false;
			}
		},
		error : function(e) {
			toastr.error("Couldn't add task", "Server Error");
			result = false;
		}
	});

	return result;
}

function saveServiceTemplateAddedTasks(serviceId) {
	var result = false;
	$('.add-tasks-repeater').each(
			function(e) {
				var name = $(this).find('.new-task-template-name').val();
				var estimationHR = $(this).find(
						'.new-task-template-estimation-hr').val();
				var estimationDays = $(this).find(
						'.new-task-template-estimation-days').val();
				var priority = $(this).find(
				'.new-task-template-priority').val();
				result = saveServiceTemplateTask(name, estimationHR,
						estimationDays,priority, serviceId);
				

			});
	
	return result;
}

function doUpdateServiceTemplate(id){
	var name= $('#input_service_template_name').val();
	var description= $('#input_service_template_description').val();
	//var price= $('#input_service_template_price').val();
	var category= $('#input_service_template_category').val();
	
	var nameError = $('#input_service_template_name_error');
	var descriptionError = $('#input_service_template_description_error');
	//var priceError = $('#input_service_template_price_error');
	var categoryError = $('#input_service_template_category_error');
	
	nameError.hide('fast');
	descriptionError.hide('fast');
	//priceError.hide('fast');
	categoryError.hide('fast');

	$.ajax({
		type : "POST",
		url : '/update-service-template-details',
		data : "id="+id+"&name="+name+"&description="+description+"&category="+category,
	    success : function(response) {
	    	if(response.status == "SUCCESS"){
	    		toastr.success("Service updated successfully", "Well done!");
	    		$('#edit-service-template-details').modal('hide');
	    		$('#input_service_template_category').val(' ');
	    		 $('#input_service_template_name').val('');
	    		 $('#input_service_template_description').val('');
	    		// $('#input_service_template_price').val('');
					$('#service_templates_datatable').mDatatable('reload');


	    		 
	    		
	    	}else{
	    		if(response.result == 'template-exists'){
					toastr.error("Couldn't update Service, a service with this name already exists", "Change Service name");
				}else{
				toastr.error("Couldn't update Service", "Error");
				}
				for (i = 0; i < response.result.length; i++) {
						if (response.result[i].code == "service.template.name.empty")
							nameError.show('slow');
						if (response.result[i].code == "service.template.description.empty")
							descriptionError.show('slow');
//						if (response.result[i].code == "service.template.price.empty")
//							priceError.show('slow');
						if (response.result[i].code == "service.template.category.empty")
							categoryError.show('slow');
//						if (response.result[i].code == "service.template.price.undefined")
//							toastr.warning("Price must be logic",
//							"Check price");
						}
	    	}
	    },
		error : function(e) {
			toastr('Error: can\'t update Service details ', e);
		}
	});	
}

function toggleModalEditDetails(id) {
//TODO
	$('#button_update_service_template').attr('onClick','doUpdateServiceTemplate('+id+')');
	$('#input_service_template_category').select2({
		width: "100%"
	});
	
	populateServiceTemplateEditDetailsModal(id);
	$("#edit-service-template-details").modal();
}

function toggleModalAddTasks(id) {
	$('#m_repeater_2').find('[data-repeater-delete]').click();
	$('#m_repeater_2').find('[data-repeater-create]').click();
	populateTaskTemplatePriorities();
	
	$('#submit_added_tasks_button').attr("onclick",
			"saveServiceTemplateAddedTasks(" + id + ")")
	$("#add-service-template-tasks").modal();
}

function populateTaskTemplatePriorities(){

	$('select[class*=new-task-template-priority]').select2({
		placeholder: {
		    id: 'none', // the value of the option
		    text: 'Priority'
		  },
        minimumResultsForSearch: Infinity,
        width: 'resolve',
        templateResult: function formatState (state) {
	        if (!state.id) { return state.text; }
	        if(state.element.value === 'low')
	        	var $state = $('<span style="color:#36A3F7;">' + state.text + '</span>');
	        if(state.element.value === 'medium')
	        	var $state = $('<span style="color:#34BFA3;">' + state.text + '</span>');
	        if(state.element.value === 'high')
	        	var $state = $('<span style="color:#F4516C;">' + state.text + '</span>');
	        return $state;
	      },
	     templateSelection: function format (state) {
			        if (state.id == "none") { return '<span style="display:table;margin:-10px auto;font-size: 14px;">'+state.text+'</span>'; }
			        if(state.text === 'Low')
			        	var $state = $('<span style="display:table;margin:-10px auto;font-size: 14px;color:#36A3F7;">' + state.text + '</span>');
			        if(state.text === 'Medium')
			        	var $state = $('<span style="display:table;margin:-10px auto;font-size: 14px;color:#34BFA3;">' + state.text + '</span>');
			        if(state.text === 'High')
			        	var $state = $('<span style="display:table;margin:-10px auto;font-size: 14px;color:#F4516C;">' + state.text + '</span>');
			        return $state;
			      },
		      escapeMarkup: function(m) { return m; }
		  

        });
	
	$('.select2-selection').css('border','0px')
	$('.select2-container').children().css('border','0px')
	$('.select2-selection__arrow').hide();
}



function toggleModalEditTasks(id) {
//TODO
	$('#service_template_tasks_datatable').mDatatable('destroy');
	ServiceTemplateTasksDatatableJson.init(id);
	$("#edit-service-template-tasks").modal();
}

function populateServiceTemplateEditDetailsModal(id){
	

	$.ajax({
		type : "GET",
		url : '/get-service-template-details',
		data : "id=" + id,
		async : false,
		success : function(response) {
			$('#input_service_template_name').val(response.name);
			$('#input_service_template_description').val(response.description);
			$('#input_service_template_category').val(response.category);
			$('#input_service_template_price').val(response.price);
		},
		error : function(e) {
			toastr.error("Couldn't retrieve template details", "Server Error");
			result = false;
		}
	});
}

$(document).ready(function() {
	$('#input_new_service_template_category').select2({
		width: "100%"
	});
	
});