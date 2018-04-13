function doAddNewServiceTemplate() {

	// get the form values
	var nameError = $('#service_template_name_error');
	var descriptionError = $('#service_template_description_error');
	var priceError = $('#service_template_price_error');
	var name = $('#input_new_service_template_name').val();
	var description = $('#input_new_service_template_description').val();
	var price = $('#input_new_service_template_price').val();

	nameError.hide('fast');
	descriptionError.hide('fast');
	priceError.hide('fast');

	$
			.ajax({
				type : "POST",
				url : '/add-service-template',
				data : "name=" + name + "&description=" + description
						+ "&price=" + price,
				success : function(response) {
					// we have the response
					if (response.status == "SUCCESS") {
						toastr.success("Template added successfully",
								"Well done!");

						$('#input_new_service_template_name').val('');
						$('#input_new_service_template_description').val('');
						$('#input_new_service_template_price').val('');

						// $('#error').hide('slow');
					} else {
						// toastr.error("Couldn't add task", "Error");

						for (i = 0; i < response.result.length; i++) {

							if (response.result[i].code == "service.template.name.empty")
								nameError.show('slow');
							if (response.result[i].code == "service.template.description.empty")
								// serviceError.show('slow');
								descriptionError.show('slow');
							if (response.result[i].code == "service.template.price.empty")
								// taskDateError.show('slow');
								priceError.show('slow');
							if (response.result[i].code == "service.template.price.undefined") {
								priceError.show('slow');
								toastr.warning("Price must be logic",
										"Check price");
							}

						}

					}
					
					$('#modal_new_service_template').modal('hide');
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
									// we have the response
									if (response.status == "SUCCESS") {
										// toastr.success("sites saved
										// successfully", "Well done!");
										swal('Deleted!',
												'Template has been deleted.',
												'success');

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

function saveServiceTemplateTask(name, estimationHR, estimationDays, serviceId) {
	var result = false;

	$.ajax({
		type : "POST",
		url : '/new-service-template-save-task',
		data : "name=" + name + "&estimationHR=" + estimationHR
				+ "&estimationTime=" + estimationDays + "&serviceId="
				+ serviceId,
		async : false,
		success : function(response) {
			// we have the response
			if (response.status == "SUCCESS") {
				// toastr.success("sites saved successfully", "Well done!");
				result = true;

			} else {
				toastr.warning("Please fill all site informations", "Warning");

				result = false;
			}
		},
		error : function(e) {
			toastr.error("Couldn't add sites", "Server Error");
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
				result = saveServiceTemplateTask(name, estimationHR,
						estimationDays, serviceId);
				

			});
	$("#add-service-template-tasks").modal('hide');
	return result;
}

function toggleModalEditDetails(id) {

	$("#edit-service-template-details").modal();
}

function toggleModalAddTasks(id) {
	$('#m_repeater_2').find('[data-repeater-delete]').click();
	$('#m_repeater_2').find('[data-repeater-create]').click();
	
//	$('#m_repeater_2').find('.add-tasks-repeater').attr("name", "repeater_" + id);
	$('#submit_added_tasks_button').attr("onclick",
			"saveServiceTemplateAddedTasks(" + id + ")")
	// $("#edit-service-template-modal-title").html("Edit details "+id);
	$("#add-service-template-tasks").modal();
}



function toggleModalEditTasks(id) {

	// $("#edit-service-template-modal-title").html("Edit details "+id);
	$("#edit-service-template-tasks").modal();
}