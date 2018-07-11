function handleNotificationClick(taskId){
	$.ajax({
		type : "GET",
		url : '/get-task-parents',
		data : 'id=' + taskId,
		async : false,
		success : function(response) {
			window.location.href = '/project?id='+response.project_id+'#task='+taskId;
			let searchParams = new URLSearchParams(window.location.search);
			let param = searchParams.get('id');
			if(param == $('#selected_project_id').val()){
				toggleTaskFragment(taskId);
			}
		},
		error : function(e) {
			alert('Error: notification click ' + e);
		}
	});
}

function handleIssueNotificationClick(issueId){
	$.ajax({
		type : "GET",
		url : '/get-issue-parents',
		data : 'id=' + issueId,
		async : false,
		success : function(response) {
			window.location.href = '/project?id='+response.project_id+'#issue='+issueId;
			let searchParams = new URLSearchParams(window.location.search);
			let param = searchParams.get('id');
			if(param == $('#selected_project_id').val()){
				toggleIssueFragment(issueId);
			}
			//location.reload();
		},
		error : function(e) {
			alert('Error: issue click ' + e);
		}
	});
}

function handleNotificationToggle(){
	 $.ajax({
			type : "POST",
			url : '/mark-old-notifications-as-read',
			async: true,
			
			error : function(e) {
				toastr.error("Couldn't update notifications status", "Server Error");
			}
		});	

}

function saveCheckedServiceTemplatesNewOperation(operationId){
	var error = "none";
	$('#service_templates_new_operation_checkbox_list').find(":checkbox:checked").each(function () {
		// alert("operation id:"+operationId);
		var serviceTemplateId = $(this).attr('id');
		var addedServiceId = '';
		
		$.ajax({
			type : "POST",
			url : '/generate-operation-service-from-template',
			data : "operationId=" + operationId + "&templateId=" + serviceTemplateId,
			async: false,
			success : function(response) {
				if (response.status == "FAIL") {
					error = "error";
					toastr.error("Couldn't generate service"+serviceTemplateId, "Error");
				}
	
			},
			error : function(e) {
				error = "error";
				toastr.error("Couldn't generate service"+serviceTemplateId, "Server Error");
			}
		});	
		
		
	});
	
	$('#generate_operation_services_modal').modal('hide');
	
	if(error === "error"){
		swal({
			title : 'Error',
			text : "Generation failed for some or all templates",
			type : 'error',
						
		});
	}else{
		swal({
			title : 'Success',
			text : "Generation done successfully",
			type : 'success',
						
		}).then(
				function(){
					if(window.location.pathname.indexOf('/project') != 0)
						redirectToAddedOperation(operationId);
					else
						toggleOperationFragment(operationId);
					
				}

		);;
	}
	
}



function addProjectToBoq(projectId,boqId){
	if($("#select_boq_content").is(":visible")){
	$.ajax({
		type : "POST",
		url : '/add-project-to-boq',
		data : "projectId=" + projectId + "&boqId=" + boqId,
		success : function(response) {
			if (response.status == "FAIL") {
				toastr.error("Couldn't add project to BOQ", "Error");
			}
		},
		error : function(e) {
			toastr.error("Couldn't add project to BOQ", "Server Error");
		}
	});	
	}
}

function doAddNewBoqAjax(){
	var name = $('#input_new_boq_name').val();
	var startDate = $('#input_new_boq_start_date').val();
	var endDate = $('#input_new_boq_end_date').val();
	var addedBoqId;
	
	$('#new_boq_name_error').hide('fast');
	$('#new_boq_start_date_error').hide('fast');
	$('#new_boq_end_date_error').hide('fast');

	if ($('#service_templates_new_boq_checkbox_list :checkbox:checked').length > 0){
	
	$.ajax({
		type : "POST",
		url : '/add-boq',
		data : "name=" + name + "&startDate=" + startDate + "&endDate=" + endDate,
		async : false,
		success : function(response) {
			// we have the response
			if (response.status == "SUCCESS") {
				toastr.success("BOQ created successfully", "Well done!");
				addedBoqId = response.result;
				if(response.result != 'undefined'){
					doAddServiceTemplatesToNewBoq(addedBoqId);
					
					$("#m_modal_boq").modal('hide');
					$('#input_new_boq_name').val('');
					$('#input_new_boq_start_date').val('');
					$('#input_new_boq_end_date').val('');
					$('#service_templates_new_boq_checkbox_list :checkbox').prop('checked', false);
					if($('#boqs_datatable').length)
						$('#boqs_datatable').mDatatable(
						'reload');
					populateSelectBoq();
				}
			} else {
				if(response.result == 'boq-exists'){
					toastr.error("Couldn't create BOQ, a BOQ with this name already exists", "Change BOQ name");
				}else{
				toastr.error("Couldn't create BOQ", "Error");
				}
				for (i = 0; i < response.result.length; i++) {
						if (response.result[i].code == "boq.name.empty")
							$('#new_boq_name_error').show('slow');
						if (response.result[i].code == "boq.startDate.empty")
							$('#new_boq_start_date_error').show('slow');
						if (response.result[i].code == "boq.endDate.empty")
							$('#new_boq_end_date_error').show('slow');
						if (response.result[i].code == "boq.date.nomatch")
							toastr.warning("Date range you mentioned is invalid", "Check date range");
				}
				
			}
		},
		error : function(e) {
			toastr.error("Couldn't add BOQ", "Server Error");
		}
	});
	}else{
		toastr.warning("Couldn't add BOQ, you have to select at least 1 service", "Select Services");
	}
// $('#service_templates_new_boq_checkbox_list').find(":checkbox:checked").each(function
// () {
// console.log('selected '+$(this).attr('id')+' price
// '+$('#service_price_'+$(this).attr('id')).val());
//		
// });
}

function doAddServiceTemplatesToNewBoq( boqId ){
	$('#service_templates_new_boq_checkbox_list').find(":checkbox:checked").each(function () {
		var templateId = $(this).attr('id');
		var price = $('#service_price_hidden_'+templateId).val();
		$.ajax({
			type : "POST",
			url : '/add-service-template-to-boq',
			data : "id=" + templateId + "&boqId=" + boqId +"&price="+price,
			success : function(response) {
				// we have the response
				if (response.status == "FAIL") {
					toastr.error("Couldn't add template", "Error");
				} 
			},
			error : function(e) {
				toastr.error("Couldn't add template", "Server Error");
			}
		});
	});
}

function populateCheckboxPopovers(){
	$('#service_templates_new_boq_checkbox_list').find(":checkbox").each(function () {
		var serviceId = $(this).attr('id');
		var serviceName = $('#service_name_'+serviceId).val();
		
		$(this).popover({
			placement: 'top',
			trigger: 'manual',
			skin: 'dark',
			html: true,
			template: '<div class="popover" style="width: 100px !important;" role="tooltip"><div class="arrow"></div><h3 style="color:#fff;background-color:#36a3f7;" class="popover-header"></h3><div class="popover-body"></div></div>',
			content: '<input type="number" placeholde="Price" class="form-control m-input m-input--solid" id="service_price_'+serviceId+'"/>'
			+'<a style="margin: auto;" href="#" id="service_price_save_btn_'+serviceId+'" class="btn btn-info m-btn">'
									+'<span>Save</span>'
								+'</a>',
			
			title: "Price "+serviceId,
		
		});	
		
		$(this).on('change', function(){
			
			var that = $(this);
			$(document).off('focusin.modal');
			// $(this).popover('show');
			$(this).popover().focus();
			if($(this).is(':checked')){
				$(this).popover('show');
				$(':checkbox').not(this).popover('hide');
				$(':checkbox').not(this).parent().addClass('m-checkbox--disabled');
				$('#input_new_boq_name').attr('disabled',true);
				$('#input_new_boq_start_date').attr('disabled',true);
				$('#input_new_boq_end_date').attr('disabled',true);
				
				$(':checkbox').not(this).attr('disabled',true);
				$(this).popover().on('focusout',function() {
					var price = $("#service_price_hidden"+serviceId).val(); 
					if( price == '' ||  price <= 0){
						toastr.warning("hey","price_"+serviceId);
						$(this).popover().focus();
					}
// else{
// $('#service_price_hidden_'+$(this).attr('id')).val($("#service_price_"+serviceId).val());
// $(':checkbox').not(this).parent().removeClass('m-checkbox--disabled');
// $(':checkbox').not(this).attr('disabled',false);
// $(this).popover('hide');
// }
//						
				});
				
				$('#service_price_save_btn_'+serviceId).on('click',function(){
					var price = $("#service_price_"+serviceId).val(); 
					if( price.length == 0 ||  price <= 0){
						toastr.warning("hey","price_"+serviceId);
						// $(this).popover().focus();
					}else{
						$('#service_price_hidden_'+that.attr('id')).val($("#service_price_"+serviceId).val());
						$(':checkbox').not(that).parent().removeClass('m-checkbox--disabled');
						$(':checkbox').not(that).attr('disabled',false);
						$('#input_new_boq_name').attr('disabled',false);
						$('#input_new_boq_start_date').attr('disabled',false);
						$('#input_new_boq_end_date').attr('disabled',false);
						that.popover('hide');
						
						$('#selected_service_templates').append('<span onclick="removeThisServiceFromSelectedServices('+serviceId+')" title="Remove this" id="selected_service_'+serviceId+'" style="font-size:15px;" class="btn btn-secondary btn-sm m-btn m-btn--custom m-btn--label-primary">'
								  +serviceName+' <span style="font-weight:400;font-size:14px;color:#34bfa3;" class="badge badge-light">'+$("#service_price_hidden_"+that.attr("id")).val()+'</span>'
								  +'</span>');
					}
				});
				
			}
			if($(this).is(':unchecked')){
				$(this).popover('hide');
				$('#selected_service_'+serviceId).remove();
				$(':checkbox').not(this).parent().removeClass('m-checkbox--disabled');
				$(':checkbox').not(this).attr('disabled',false);
			}
		});
		

	});
}

function removeThisServiceFromSelectedServices(serviceId){
	$('#service_templates_new_boq_checkbox_list').find(":checkbox[id*="+serviceId+"]").prop('checked',false);
	$('#selected_service_'+serviceId).remove();
}


function doAddProjectAjaxPost() {
	var nameError = $('#project_name_error');
	var clientError = $('#project_client_error');
	var countryError = $('#project_country_error');
	var currencyError = $('#project_currency_error');
	var natureError = $('#project_nature_error');
	var name = $('#project_name').val();
	var client = $('#project_client').val();
	var country = $('#project_country').val();
	var currency = $('#project_currency').val();
	var owner = $('#project_owner').val();
	var nature = $('#select_nature_new_project').val();

	nameError.hide('fast');
	clientError.hide('fast');
	countryError.hide('fast');
	currencyError.hide('fast');
	natureError.hide('fast');

	$.ajax({
		type : "POST",
		url : '/add-project',
		data : "name=" + name + "&client=" + client + "&country=" + country
				+ "&currency=" + currency + "&owner=" + owner+"&nature="+nature,
		success : function(response) {
			if (response.status == "SUCCESS") {
				toastr.success("Project Added successfully", "Well done!");
				var targets = [];
				$.each($("#select_boq_new_project option:selected"), function(){
					addProjectToBoq(response.result,$(this).val());
				});
				
				$('#project_name').val('');
				$('#project_client').val('');
				$('#project_country').val('');
				$('#project_currency').val('');
				$('#select_boq_new_project').val('');
				$('#select_nature_new_project').val('');
				if($("#select_boq_content").is(":visible"))
					$("#m_switch_project_boq").click();

			} else {
				toastr.error("Couldn't add project", "Error");

				for (i = 0; i < response.result.length; i++) {

					if (response.result[i].code == "project.name.empty")
						nameError.show('slow');
					if (response.result[i].code == "project.client.empty")
						clientError.show('slow');
					if (response.result[i].code == "project.country.empty")
						countryError.show('slow');
					if (response.result[i].code == "project.currency.empty")
						currencyError.show('slow');
					if (response.result[i].code == "project.nature.empty")
						natureError.show('slow');
				}

			}
		},
		error : function(e) {
			toastr.error("Couldn't add project", "Server Error");
		}
	});
}

function redirectToAddedOperation(operationId){
	$.ajax({
		type : "GET",
		url : '/get-operation-details',
		data : 'id=' + operationId,
		async : false,
		success : function(response) {
			window.location.href = '/project?id='+response.project.id+'#operation='+operationId;
		},
		error : function(e) {
			alert('Error: Redirecting to created operation ' + e);
		}
	});
}

function doAddOperationAjaxPost() {

	var nameError = $('#operation_name_error');
	var projectError = $('#operation_project_error');
	var startDateError = $('#operation_startDate_error');
	var endDateError = $('#operation_endDate_error');
	var name = $('#operation_name').val();
	var project = $('#select_project_new_operation').val();
	var startDate = $('#m_datepicker_4_3').val();
	var endDate = $('#m_datepicker_4_4').val();
	var site = $('#select_site_new_operation').val();
	var responsible = $('#operation_responsible').val();

	nameError.hide('fast');
	projectError.hide('fast');
	startDateError.hide('fast');
	endDateError.hide('fast');

	
	$.ajax({
		type : "POST",
		url : '/add-operation',
		data : "name=" + name + "&site="+site+"&project=" + project + "&startDate="
				+ startDate + "&endDate=" + endDate+"&responsible="+responsible,
		success : function(response) {
			if (response.status == "SUCCESS") {
				var addedOperationId = response.result[0];
				var parentProjectId = project;
				$('#operation_name').val('');
				$('#select_project_new_operation').val('');
				$('#m_datepicker_4_3').val('');
				$('#m_datepicker_4_4').val('');
				

				if(response.result[1] != 0){
				swal({
					title : 'Operation added',
					text : "Do you want to generate its services ?         " +
							"You can generate services and tasks from predefined templates within a second ",
					showCancelButton : true,
					type: 'success',
					confirmButtonText : 'Yes, do it!',
					background: '#f1f1f1'
				}).then(
						function(result) {
							if (result.value) {
								generateNewOperationServicesFromTemplates(parentProjectId,addedOperationId);

								$('#generate_operation_services_modal').modal('show');

							}else{
								if(window.location.pathname.indexOf('/project') != 0)
									redirectToAddedOperation(addedOperationId);
								else
									toggleOperationFragment(addedOperationId);
								
							}

						}

				);
				
				}else{
					toastr.success("Operation Added successfully", "Well done!");
					  setTimeout(function (){
						  if(window.location.pathname.indexOf('/project') != 0)
								redirectToAddedOperation(addedOperationId);
							else
								toggleOperationFragment(addedOperationId);
				   },2000);
					
				}
				$('#m_quick_sidebar_add_close').click();
				if($('#operations_datatable').length){
					$('#operations_datatable').mDatatable('reload');
				}


			} else {
				if(response.result == "INVALIDUSER"){
					toastr.error('Invalid responsible');
					
				}else{
					toastr.error("", "Please fill all required fields");
					for (i = 0; i < response.result.length; i++) {
						if (response.result[i].code == "operation.name.empty")
							nameError.show('slow');
						if (response.result[i].code == "operation.project.empty")
							projectError.show('slow');
						if (response.result[i].code == "operation.startDate.empty")
							startDateError.show('slow');
						if (response.result[i].code == "operation.endDate.empty")
							endDateError.show('slow');
						if (response.result[i].code == "operation.date.nomatch" || response.result[i].code == "operation.date.nomatch.operation")
							toastr.warning("Dates don't match", "Warning");
						if (response.result[i].code == "operation.site.empty")
							toastr.warning('Select a site for the operation');
					}
				}

			}
		},
		error : function(e) {
			toastr.error("Couldn't add operation", "Server Error");
		}
	});
}

function doAddTaskAjaxPost() {
	var nameError = $('#task_name_error');
	var serviceError = $('#task_service_error');
	var taskDateError = $('#task_date_error');
	var taskStartDateError = $('#task_startDate_error');
	var taskEndDateError = $('#task_endDate_error');
	var name = $('#task_name').val();
	var service = $('#select_service_new_task').val();
	var startDate = $('#m_datepicker_4_1').val();
	var endDate = $('#m_datepicker_4_2').val();
	var priority = $('#task_priority').val();

	nameError.hide('fast');
	serviceError.hide('fast');
	taskDateError.hide('fast');
	taskStartDateError.hide('fast');
	taskEndDateError.hide('fast');

	$.ajax({
		type : "POST",
		url : '/add-task',
		data : "name=" + name + "&service=" + service + "&startDate="
				+ startDate + "&endDate=" + endDate + "&priority=" + priority,
		success : function(response) {
			if (response.status == "SUCCESS") {
				toastr.success("Task added successfully", "Well done!");
				doAddTaskOwners(response.result);
				$('#task_name').val('');
				$('#m_datepicker_4_1').val('');
				$('#m_datepicker_4_2').val('');
				$('#task_priority').val('');
				$('#select_project_new_task').val(' ');
				$('#select_project_new_task').selectpicker('refresh');
				$('#select_service_div').hide();
				$('#task_priority').val('none');
				$('#m_quick_sidebar_add_close').click();
				if($('#service_tasks_datatable').length){
					$('#service_tasks_datatable').mDatatable('reload');
				}

			} else {

				for (i = 0; i < response.result.length; i++) {

					if (response.result[i].code == "task.name.empty")
						nameError.show('slow');
					if (response.result[i].code == "task.service.empty")
						toastr.warning("you must select a service",
								"Service required");
					if (response.result[i].code == "task.date.nomatch")
						toastr.warning("please select a valid date range",
								"Dates don't match");
					if (response.result[i].code == "task.startDate.empty")
						taskStartDateError.show('slow');
					if (response.result[i].code == "task.endDate.empty")
						taskEndDateError.show('slow');
				}

			}
		},
		error : function(e) {
			toastr.error("Couldn't add task", "Server Error");
		}
	});
}

function doAddTaskOwners(taskId){
	$('#select_owners_new_task').find(":selected").each(function(){
		var user = $(this).attr('value');
		// alert('selected values: '+$(this).attr('value'));
		// alert('task: '+taskId);
		$.ajax({
			type : "POST",
			url : '/add-task-owner',
			data : "id=" + taskId + "&userId=" + user,
			success : function(response) {
				// we have the response
				if (response.status == "FAIL") {
					toastr.error("Couldn't add owner", "Error");
				} 
			},
			error : function(e) {
				console.log("error: "+e);
				toastr.error("Couldn't add owner", "Server Error");
			}
		});
	});
		
	
}

function populateNewTaskOwnerSelect(){
	
	$.ajax({
		type : "GET",
		url : '/get-all-users-json-details',
		async: false,
		success : function(response) {
			var html_text = '';
			for (i = 0; i < response.length; i++) {
				html_text += '<option value="'+response[i].id+'">'+response[i].firstName+' '+response[i].lastName+'</option>';
				
			}
			$('#select_owners_new_task').html(html_text);
			$('#select_owners_new_task').selectpicker('refresh');
		},
		error : function(e) {
			alert('Error: can"t get users ' + e);
		}
	});
	
}

function doAddServiceAjaxPost() {
	var nameError = $('#service_name_error');
	var projectError = $('#service_project_error');
	var operationError = $('#service_operation_error');
	var priceError = $('#service_price_error');
	var categoryError = $('#service_category_error');
	
	var name = $('#input_new_service_name').val();
	var project = $('#select_project_new_service').val();
	var operation = $('#select_operation_new_service').val();
	var price = $('#input_new_service_price').val();
	var category = $('#input_new_service_category').val();
	var flag = $('#input_service_flag').val();

	nameError.hide('fast');
	projectError.hide('fast');
	operationError.hide('fast');
	priceError.hide('fast');
	categoryError.hide('fast');

	$.ajax({
		type : "POST",
		url : '/add-service',
		data : "name=" + name + "&project=" + project + "&operation="
				+ operation + "&priceHT=" + price + "&category=" + category+"&flag=" + flag,
		success : function(response) {
			if (response.status == "SUCCESS") {
				toastr.success("Service added successfully", "Well done!");

				$('#input_new_service_name').val('');
				
				$('#select_project_new_service').val(' ');
				$('#select_project_new_service').selectpicker('refresh');
				 $('#select_operation_new_service').val(' ');
				 $('#select_operation_new_service').selectpicker('refresh');
				 $('#input_new_service_price').val('');
				 $('#input_new_service_category').val(' ');
				 $('#input_new_service_category').selectpicker('refresh');
				 $('#input_service_flag').val("0");
				
				$('#m_quick_sidebar_add_close').click();
				if($('#operation-fragment').is(':visible')){
					var operationId = $('#operation_fragment_selected_operation_id').val();
					 populateServicesTabOperationFragment(operationId);
				}
				if($('#operations_datatable').length){
					// var operationId =
					// $('#operation_fragment_selected_operation_id').val();
					$('#operations_datatable').mDatatable('reload');
				}
				
				

			} else {

				for (i = 0; i < response.result.length; i++) {

					if (response.result[i].code == "service.name.empty")
						nameError.show('slow');
					if (response.result[i].code == "service.project.empty")
						projectError.show('slow');
					if (response.result[i].code == "service.operation.empty")
						operationError.show('slow');
					if (response.result[i].code == "service.price.empty")
						priceError.show('slow');
					if (response.result[i].code == "service.category.empty")
						categoryError.show('slow');
					if (response.result[i].code == "service.price.undefined")
						toastr.warning("please enter a valid price",
						"Check Price");
				}

			}
		},
		error : function(e) {
			toastr.error("Couldn't add Service", "Server Error");
		}
	});
}


function populateServicesListNewTaskForm() {
	var selectedProjectId = $("#select_project_new_task").val();
	if(selectedProjectId != " ")
	$.ajax({
		type : "GET",
		url : '/set-selected-project-services',
		data : 'projectId=' + selectedProjectId,
		async: false,
		success : function(response) {

			var html_text = "";
			for (i = 0; i < response.length; i++) {
				html_text += "<option value='" + response[i].id + "'>"
						+ response[i].name + "</option>"

			}
			$("#select_service_new_task").html(html_text);
			$('#select_service_new_task').selectpicker('refresh');
		},
		error : function(e) {
			alert('Error: services ' + e);
		}
	});

}

function updateSelectServices() {

	populateServicesListNewTaskForm();
	$("#select_service_div").show();

}

function populateSelectOwnedProjects() {
	$.ajax({
		type : "GET",
		url : '/set-select-owned-projects',
		async: false,
		success : function(response) {

			var html_select_options = "";

			var html_text = "<option value=' ' selected>Nothing Selected</option>";
			if (response.length == 1) {
				html_text += "<option value='" + response[0].id + "'>"
						+ response[0].name + "</option>";
				$("#select_project_new_task").html(html_text);
				$("#select_project_new_service").html(html_text);
				$("#select_project_new_operation").html(html_text);
				updateSelectServices();
			} else {
				for (i = 0; i < response.length; i++) {

					html_text += "<option value='" + response[i].id + "'>" + response[i].name
							+ "</option>";
				}
				$("#select_project_new_task").html(html_text);
				$("#select_project_new_operation").html(html_text);
				$("#select_project_edit_operation").html(html_text);
				$("#select_project_new_service").html(html_text);

			}
			$("#select_project_new_operation").selectpicker('refresh');
			$("#select_project_edit_operation").selectpicker('refresh');

			$("#select_project_new_task").selectpicker('refresh');
			$("#select_project_new_service").selectpicker('refresh');
			
			$("#select_project_new_operation").on('change', function() {
				var projectId = $("#select_project_new_operation").val();
				
				if(projectId == ' '){ 
					$('#sites_map_container').attr("style","display:none;width:100%; height:50px;position: relative;");
					$('#sites_map_canvas_sidebar').attr("style","position: absolute; display:none;top: 20%; right: 0; bottom: 0; left: 0;");
				}else{
					initializeSitesGmap(projectId);
				}
			});
			
			$("#select_project_new_service").on('change', function() {
				var projectId = $("#select_project_new_service").val();
				
				if(projectId != ' '){ 
					populateSelectOperationNewService(projectId);
				}
			});

		},
		error : function(e) {
			$("#select_project_new_task").html(
					"<option value=''>Nothing selected</option>");
			$("#select_project_new_operation").html(
					"<option value=''>Nothing selected</option>");

		}
	});

}

function populateSelectOperationNewService(projectId){
	$.ajax({
		type : "GET",
		url : '/get-project-operations',
		async: false,
		data: "projectId="+projectId,
		success : function(response) {
			
			var html_text = "<option value=' ' selected>Nothing Selected</option>";
			if (response.length == 1) {
				html_text += "<option value='" + response[0].id + "'>"
						+ response[0].name + "</option>";
				$("#select_operation_new_service").html(html_text);
				} else {
				for (i = 0; i < response.length; i++) {

					html_text += "<option value='" + response[i].id + "'>" + response[i].name
							+ "</option>";
				}
				$("#select_operation_new_service").html(html_text);


			}
			$("#select_operation_new_service").selectpicker('refresh');

			
			
		},
	error : function(e) {
		$("#select_operation_new_service").html(
				"<option value=''>Nothing selected</option>");
		
	}
});
}


function projectDashboardTaskPieChart() {
	function drawChart() {
		
		var data = google.visualization.arrayToDataTable([
				[ 'Task', 'Status' ], [ 'Open', onGoing ],
				[ 'Closed', completed ],

		]);

		var options = {
			width : 239,
			height : 200,
			pieHole : 0.4,
			pieSliceTextStyle : {
				color : 'black',
			},
			colors : [ '#f4516c', '#34bfa3' ],
			fontName : 'Poppins',
			legend : {
				position : 'bottom'
			}
		};
		if (completed == 0 && onGoing == 0) {
			$("#piechart_error").show();

		} else {
			$("#piechart_error").hide();
			var chart = new google.visualization.PieChart(document
					.getElementById('piechart'));

			chart.draw(data, options);
		}
	}
	google.charts.load('current', {
		'packages' : [ 'corechart' ]
	});
	google.charts.setOnLoadCallback(drawChart);
}

function wizardSaveClient() {
	name = $('#modal_client_name').val();
	email = $('#modal_client_email').val();
	phone = $('#modal_client_phone').val();
	country = $('#modal_client_country').val();
	address = $('#modal_client_address').val();

	nameError = $('#modal_client_name_error');
	var result = false;

	$.ajax({
		type : "POST",
		url : '/wizard-save-client',
		data : "name=" + name+"&email="+email+"&phone="+phone+"&country="+country+"&address="+address,
		async : false,
		success : function(response) {
			if (response.status == "SUCCESS") {
				result = true;

			}else if(response.status == "FAIL" && response.result == "existing.client"){
				toastr.error("This client exists already", "Error");
			} else {
				toastr.warning("Please fill client informations", "Warning");

				for (i = 0; i < response.result.length; i++) {

					if (response.result[i].code == "client.name.empty")
						nameError.show('slow');

				}
				result = false;
			}
		},
		error : function(e) {
			toastr.error("Couldn't add client", "Server Error");
			result = false;
		}
	});

	return result;

}

function wizardSaveSite(name,long,lat){
		var result = false;

	$.ajax({
		type : "POST",
		url : '/wizard-save-site',
		data : "name=" + name +"&longitude="+ long +"&latitude="+ lat,
		async : false,
		success : function(response) {
			if (response.status == "SUCCESS") {
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

function wizardSaveManualAddedSites() {
	var result = false;
	jQuery('.wizard-form-site').each(function(e)
			{
		var name = $(this).find('.wizard-form-site-name').val();
		var long = $(this).find('.wizard-form-site-longitude').val();
		var lat = $(this).find('.wizard-form-site-latitude').val();
		result = wizardSaveSite(name,long,lat);
			});

	return result;
}

function ClientWizardPublish(){
	$("#m_modal_4").modal('hide');

	$.ajax({
		type : "POST",
		url : '/client-wizard-publish',
		async : false,
		success : function(response) {
			if(response == true)
				toastr.success("New Client added successfully", "Well done!");
				

			
		},
		error : function(e) {
			toastr.error("Couldn't add client", "Server Error");
			result = false;
		}
	});
	
}

function populateGenerateOperationServicesCheckboxList(projectId,operationId){
	var html_text = '';
	var navLinks = '';
	var navContent = '';
	
	$.ajax({
		type : "GET",
		url : '/get-service-templates-from-project-boq',
		data : "projectId=" + projectId,
		success : function(response) {

				for (i = 0; i < response.length; i++)
				{
					
					html_text = '<label '
									+ 'class="btn btn-secondary btn-sm m-btn m-btn--custom m-btn--label-primary">'
									+response[i].name +'<span style="font-weight:400;font-size:14px;color:#f4516c;" class="badge badge-light">'+response[i].price+'</span>'
									+'<input '
									+'id="'+response[i].id+'" type="checkbox" class="badgebox">'
									+'<span class="badge"><i style="color:#34bfa3;font-size:18px;font-weight:500;margin:auto;" class="la la-dot-circle-o"></i></span>'
									
									+'</label>';
									// +'<span class="badge
									// badge-success">'+response[i].price+'</span>';
					switch(response[i].category) {
				    	case "BTS":
				    		$('#category_bts').append(html_text);
				    		break;
				    	case "CENTER_ROOM":
				    		$('#category_center_room').append(html_text);
				    		break;
				    	case "OTT":
				    		$('#category_ott').append(html_text);
				    		break;
				    	case "LOGISTIC":
				    		$('#category_logistic').append(html_text);
				    		break;
				    	case "OPTIONAL":
				    		$('#category_optional').append(html_text);
				    		break;
				    	default:
				    		break;
						}
					
					
					
// if(i==0){
// navLinks += '<li id="'+response[i].id+'" style="display:none;"
// class="nav-item">'
// +'<a href="#'+response[i].name+'" class="nav-link" data-toggle="tab"
// role="tab" aria-controls="'+response[i].name+'">'+response[i].name+'</a>'
// +'</li>';
//		
// }else{
// navLinks += '<li id="'+response[i].id+'" style="display:none;"
// class="nav-item">'
// +'<a href="#'+response[i].name+'" class="nav-link" data-toggle="tab"
// role="tab" aria-controls="'+response[i].name+'">'+response[i].name+'</a>'
// +'</li>';
// }
// navContent +='<div name="nav_content_'+response[i].id+'" class="tab-pane
// fade" id="'+response[i].name+'" role="tabpanel">'
// +'<div class="m-widget11">'
// +'<div class="table-responsive">'
// +'<!--begin::Table--> '
// +'<table class="table">'
// +'<!--begin::Thead-->'
// +'<thead>'
// +' <tr>'
// +' <td style="width:5%;" class="m-widget11__label">#</td>'
// +'<td style="width:50%;" class="m-widget11__app">Name</td>'
// +'<td style="width:15%;" class="m-widget11__sales"><span
// style="display:table;margin:0 auto;">HR</span></td>'
// +'<td style="width:15%;" class="m-widget11__price"><span
// style="display:table;margin:0 auto;">Days</span></td>'
// +'<td style="width:15%;" class="m-widget11__total"><span
// style="display:table;margin:0 auto;">Priority</span></td>'
// +'</tr>'
// +'</thead>'
// +'<!--end::Thead-->'
// +'<!--begin::Tbody-->'
// +'<tbody name="tbody_'+response[i].id+'">'
// +'</tbody>'
// +'<!--end::Tbody--> '
// +'</table>'
// +'<!--end::Table-->'
// +'</div>'
//
// +'</div>'
// +'</div>';
		
					
					
				}
				
				$("div[id*=category_]").each(function (){
					// console.log($(this).attr('id'));
					if($(this).children().length > 0)
						$(this).parent().show();
				});
				// $('#service_templates_new_operation_checkbox_list').html(html_text);
				// $('#vertical-nav-links').html(navLinks);
				// $('#vertical-nav-content').html(navContent);

			
		},
		error : function(e) {
			toastr.error("Couldn't get templates", "Server Error");
			result = false;
		}
	});
	
}

function generateNewOperationServicesFromTemplates(projectId,operationId){
	$('button[name=save_changes_generate_services_modal]').attr("onClick",'saveCheckedServiceTemplatesNewOperation('+operationId+')');
	populateGenerateOperationServicesCheckboxList(projectId,operationId);
	

}



function populateConfirmationSiteDetails(){
	$.ajax({
		type : "GET",
		url : '/get-added-sites-wizard',
	    success : function(response) {

			var html_text = "";
			for (i = 0; i < response.length; i++) {
				
				html_text += "<div class='form-group m-form__group m-form__group--sm row'>"
				+"<label class='col-xl-3 col-lg-3 col-form-label'>Site name:</label>"
				+"<div class='col-xl-9 col-lg-9'>"
				+"	<span class='m-form__control-static'>"+ response[i].name +"</span>"
				+"</div>"
			+"</div>"
			+"<div class='form-group m-form__group m-form__group--sm row'>"
			+"	<label class='col-xl-3 col-lg-3 col-form-label'>Longitude:</label>"
			+"	<div class='col-xl-9 col-lg-9'>"
			+"		<span class='m-form__control-static'>"+ response[i].longitude +"</span>"
			+"	</div>"
			+"</div>"
			+"<div"
			+"	class='form-group m-form__group m-form__group--sm row'>"
			+"	<label class='col-xl-3 col-lg-3 col-form-label'>Latitude:</label>"
			+"	<div class='col-xl-9 col-lg-9'>"
			+"		<span class='m-form__control-static'>"+ response[i].latitude +"</span>"
			+"	</div>"
			+"</div>";
				

			}
			$("#confirm_site_details").append(html_text);
		},
		error : function(e) {
			alert('Error: confirmation step wizard ' + e);
		}
	});

}

function populateConfirmationClientDetails(){
	$.ajax({
		type : "GET",
		url : '/get-added-client-wizard',
	    success : function(response) {

			$("#confirm_client_name").html(response.name);
			$("#confirm_client_email").html(response.email);
			$("#confirm_client_phone").html(response.phone);
			$("#confirm_client_country").html(response.country);
			$("#confirm_client_address").html(response.address);
			
		},
		error : function(e) {
			alert('Error: confirmation step wizard ' + e);
		}
	});
}

function populateConfirmationStepWizard(){
	populateConfirmationClientDetails();
	populateConfirmationSiteDetails();
}


function populateSelectBoq() {
	$.ajax({
		type : "GET",
		url : '/set-select-boq',
		async: false,
		success : function(response) {

			var html_select_options = "";

			var html_text = "<option value=''>Nothing Selected</option>";
			var selectedOption = "";
			if (response.length == 1) {
				html_text += "<option value='" + response[0].id + "'>"
						+ response[0].name + "</option>";
				$("#select_boq_new_project").html(html_text);
			} else {
				for (i = 0; i < response.length; i++) {
					html_text += "<option value='" + response[i].id + "'>" + response[i].name
							+ "</option>";
				}
				$("#select_boq_new_project").html(html_text);
				

			}
			$("#select_boq_new_project").selectpicker('refresh');

		},
		error : function(e) {
			alert("eroor");
			$("#select_boq_new_project").html(
					"<option value=''>Nothing selected</option>");
			

		}
	});

}

function populateSelectNatureNewProject(){
	$.ajax({
		type : "GET",
		url : '/json-natures',
	
		success : function(response) {
			var arr = [];
		
			for (i = 0; i < response.length; i++) {

						arr.push({
							 id: response[i].name,
				             text: response[i].name
					   	});

			}
			$("#select_nature_new_project").select2({
			            data: arr,
			            width: '100%',
			        });
				
			
		},
		error : function(e) {
			alert('Error: natures ' + e);
		}
	});
}



function populateSidebarAdd() {
	$('#operation_name_error').hide('fast');
	$('#operation_project_error').hide('fast');
	$('#operation_startDate_error').hide('fast');
	$('#operation_endDate_error').hide('fast');
	
	populateNewTaskOwnerSelect();
	
	populateSelectOwnedProjects();
	
	populateOperationResponsibleTypeAhead();
	$('#sites_map_container').attr("style","display:none;max-width:100%; max-height:50px;position: relative;");
	$('#sites_map_canvas_sidebar').attr("style","position: absolute; display:none;top: 20%; right: 0; bottom: 0; left: 0;");
	$('#select_service_div').hide();
	$('#select_project_new_service').val(' ').change();
	$('#select_operation_new_service').html('');
	$('#select_operation_new_service').selectpicker('refresh');

	
}

function populateOperationResponsibleTypeAhead() {
	
		
    var users = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.whitespace,
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        prefetch: './get-all-users-json'
    });

    $('#operation_responsible').typeahead(null, {
        name: 'users',
        source: users
    });
}

function gmapPopoverInit(){
		
	$(".data-repeater").on('mouseover','.toggle_gmap', function (event){
		$(this).popover({
			 content: function(){
					 	var long = $(this).parent().find('input.wizard-form-site-longitude').val();
					 	var lat = $(this).parent().find('input.wizard-form-site-latitude').val();
					 	var content = "<img src='https://maps.googleapis.com/maps/api/staticmap?zoom=10&size=350x300&maptype=roadmap"
					 		+"&markers=color:red%7Clabel:S%7C"+lat+","+long+"&key=AIzaSyDArbmGWjBSiyGLbUAGUMmR8vwNahwxdeg'></img>"
					 	return content;
				 	},
					html: true,
					placement: 'right',
					trigger: 'hover'
			  });
			
		});
		
	}
	


function populateModalOverdueItems(){
	DatatableOverdueItemsRemote.init();
}

var userOverviewChart = function() {

	var demo7 = function() {
		var chartData = generateChartData();
		var chart = AmCharts.makeChart("user_overview", {
			"type" : "serial",
			"theme" : "patterns",
			"marginRight" : 0,
			"marginLeft" : 0,
			"autoMarginOffset" : 12,
			"marginTop" : 0,
			"dataProvider" : chartData,
			"valueAxes" : [ {
				"axisAlpha" : 0.2,
				"dashLength" : 0,
				"position" : "left"
			} ],
			"mouseWheelZoomEnabled" : false,
			"graphs" : [ {
				"id" : "g1",
				"balloonText" : "[[value]] items closed",
				"bullet" : "round",
				 "showBalloon": true,
				"bulletBorderAlpha" : 0,
				"bulletColor" : "#fff",
				"hideBulletsCount" : 50,
				"title" : "red line",
				"valueField" : "tasks",
				"useLineColorForBulletBorder" : true,
				"balloon" : {
					"drop" : false,
					"color": "#fff",
					"fillColor": "#000",
					"fillAlpha": 0.8,
					"borderThickness": 0
				}
			} ],
			"chartScrollbar" : {
				"autoGridCount" : true,
				"graph" : "g1",
				"scrollbarHeight" : 20,
				"resizeEnabled": false,
			},
			"chartCursor" : {
				"limitToGraph" : "g1",
				
			},
			"categoryField" : "date",
			"categoryAxis" : {
				"parseDates" : false,
				"axisColor" : "#fff",
				"dashLength" : 1,
				"minorGridEnabled" :true
			},
			"export" : {
				"enabled" : false
			}
		});


		// generate some random data, quite different range
		function generateChartData() {

			var chartData = [];
			
		
			$.ajax({
				type : "GET",
				url : '/json-user-overview-feed',
				async : false,
				success : function(response) {

					$.each(response, function(date, val) {

							var newDate = date;

							var tasksCount = val;
						
						chartData.push({
							date : newDate,
							tasks : tasksCount
						});
					});

				},
				error : function(e) {
					alert('Error: user overview ' + e);
				}
			});

			return chartData;
		}
	}

	return {
		// public functions
		init : function() {

			demo7();

		}
	};
}();
function populateUserCalendar(){
	
    var todayDate = moment().startOf('day');
    var YM = todayDate.format('YYYY-MM');
    var YESTERDAY = todayDate.clone().subtract(1, 'day').format('YYYY-MM-DD');
    var TODAY = todayDate.format('YYYY-MM-DD');
    var TOMORROW = todayDate.clone().add(1, 'day').format('YYYY-MM-DD');
   // $('.loader-wrapper').show();
	
    // var eventss = getCalendarEvents();
    
    
    $('#user_calendar').fullCalendar( 'removeEvents');
    
    
 
    
   // setTimeout(function(){
		// $(".m-timeline-1__items").html("");
    	
    	$('#user_calendar').fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'listWeek,month,agendaWeek,agendaDay'
               
            },
            defaultView: 'listWeek',
            editable: true,
            eventLimit: true, // allow "more" link when too many
								// events
            navLinks: true,
            editable: false,
            
            businessHours: {
            	  // days of week. an array of zero-based day of
					// week integers (0=Sunday)
            	  dow: [ 1, 2, 3, 4, 5 ], // Monday - Thursday

            	  start: '8:00', // a start time (10am in this
									// example)
            	  end: '18:00', // an end time (6pm in this example)
            	},
          // events: eventss,

            eventRender: function(event, element) {
                if (element.hasClass('fc-day-grid-event')) {
                	element.data('content', event.description);
                    element.data('placement', 'top');
                    mApp.initPopover(element); 
                    element.css("background",event.color);
                  // element.find('.fc-time').html('<i class="fa
					// fa-'+event.icon+'">' + event.description +
					// '</i>');
                } else if (element.hasClass('fc-time-grid-event')) {
                    element.find('.fc-title').append('<div class="fc-description">' + event.description + '</div>'); 
                } else if (element.find('.fc-list-item-title').lenght !== 0) {
                    element.find('.fc-list-item-title').append('<div class="fc-description">' + event.description + '</div>'); 
                }
            },
            
            eventClick: function(calEvent, jsEvent, view) {

            	if(calEvent.type == "task"){
                // alert('Operation: ' + calEvent.title);
            		handleNotificationClick(calEvent.id);
            	}
            	else{
            		handleIssueNotificationClick(calEvent.id);
            	}
            		// alert('Service: ' + calEvent.title);
                // change the border color just for fun
                // $(this).css('border-color', 'red');

              }
        });
    	
		getUserCalendarEvents();
       // $('.loader-wrapper').hide();
       
    // },500);

}

function getUserCalendarEvents(){
// var projectId = $('#selected_project_id').val();
var events = [];
$.ajax({
type : "GET",
url : '/get-user-calendar-events',
async : false,
success : function(response) {

for(var i = 0 ; i < response.length ; i++){
	var event;
	if(response[i].type == "task")
	var event={
			type: "task",
			id:	response[i].id ,
			title: response[i].name ,
			start:  response[i].startDate,
			end: response[i].endDate,
			color: "#34bfa3",
			className: "m-fc-event--light m-fc-event--solid-light",
			description: 'Task "'+response[i].name+'" due date.',
				 };
	else{
		var issueSeverity;
		switch(response[i].severity) {
	    case "minor":
	        issueSeverity = "#36a3f7";
	        break;
	    case "major":
	    	issueSeverity = "#ffb822";
	    	break;
	    case "critical":
	    	issueSeverity = "#f4516c";
	    	break;
	}
		var event={
				type: "issue",
				id:	response[i].id ,
				title: response[i].name ,
				start:  response[i].startDate,
				end: response[i].endDate,
				color: issueSeverity,
				description: 'Issue "'+response[i].name+'" due date.',
				className: "m-fc-event--light m-fc-event--solid-light"
			};
	}
		
	events.push(event);
}

},
error : function(e) {
alert('Error: refresh project feed ' + e);
}
});

for(var j = 0 ; j< events.length; j++){
$('#user_calendar').fullCalendar('renderEvent', events[j],true);
}
$("#user_calendar").fullCalendar('rerenderEvents');

return events;


}

$(document).ready(function() {
	
	
	gmapPopoverInit();
	
	$("#select_service_div").hide();

	$("#select_project_new_task").change(function() {
		updateSelectServices();
	});
	
	 
	 if (window.location.hash.indexOf('#my-tasks') == 0){
			$('#my-tasks-nav-link').click();			
	 }
	 
	 if (window.location.hash.indexOf('#my-issues') == 0){
			$('#my-issues-nav-link').click();
	 }
	 

	 if (window.location.hash.indexOf('#task=') == 0){
			externalTaskLoad();
			
	 }
	 
	 if (window.location.hash.indexOf('#feed') == 0){
		 	goToProjectFeed();
			
	 }
	 
	 if (window.location.hash.indexOf('#tasks') == 0){
			goToProjectTasks();
			
	 }
	 
	 if (window.location.hash.indexOf('#issues') == 0){
			goToProjectIssues();
			
	 }
	 
	 if (window.location.hash.indexOf('#operations') == 0){
			goToProjectOperations();
			
	 }
	 
	 if (window.location.hash.indexOf('#calendar') == 0){
			goToProjectCalendar();
			
	 }
	 
	 
	 if (window.location.hash.indexOf('#issue=') == 0){
			externalIssueLoad();
			
	 }
	 
	 if (window.location.hash.indexOf('#operation=') == 0){
			externalOperationLoad();
			
	 }
	
	    
	
	
	  	
	
	toastr.options = {
		"closeButton" : true,
		"debug" : false,
		"newestOnTop" : false,
		"progressBar" : false,
		"positionClass" : "toast-bottom-left",
		"preventDuplicates" : false,
		"onclick" : null,
		"showDuration" : "300",
		"hideDuration" : "1000",
		"timeOut" : "5000",
		"extendedTimeOut" : "1000",
		"showEasing" : "swing",
		"hideEasing" : "linear",
		"showMethod" : "fadeIn",
		"hideMethod" : "fadeOut"
	};
	


	
	
	
    });






