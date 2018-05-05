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
			text : "generation failed",
			type : 'error',
						
		});
	}else{
		swal({
			title : 'Success',
			text : "Generation done successfully",
			type : 'success',
						
		});
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
}

function doAddServiceTemplatesToNewBoq( boqId ){
	$('#service_templates_new_boq_checkbox_list').find(":checkbox:checked").each(function () {
		var templateId = $(this).attr('id');
		$.ajax({
			type : "POST",
			url : '/add-service-template-to-boq',
			data : "id=" + templateId + "&boqId=" + boqId,
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

	nameError.hide('fast');
	projectError.hide('fast');
	startDateError.hide('fast');
	endDateError.hide('fast');

	
	$.ajax({
		type : "POST",
		url : '/add-operation',
		data : "name=" + name + "&site="+site+"&project=" + project + "&startDate="
				+ startDate + "&endDate=" + endDate,
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

							}

						}

				);
				
				}else{
					toastr.success("Operation Added successfully", "Well done!");
				}
				$('#m_quick_sidebar_add_close').click();


			} else {
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
					if (response.result[i].code == "operation.date.nomatch")
						toastr.warning("Dates don't match", "Warning");
					if (response.result[i].code == "operation.site.empty")
						toastr.warning('Select a site for the operation');
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

				$('#task_name').val('');
				$('#m_datepicker_4_1').val('');
				$('#m_datepicker_4_2').val('');
				$('#task_priority').val('');

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

function projectDynamicContent() {
	projectDashboardTaskPieChart();
	$("#dashboard-fragment").show();

	$("#project_dashboard_toggle").on("click", function(event) {
		$("#m_dynamic_content_project").children().hide();
		projectDashboardTaskPieChart();
		$("#dashboard-fragment").show();
	});

	$("#project_feed_toggle").on("click", function() {
		$("#m_dynamic_content_project").children().hide();
		$("#feed-fragment").show();

		});

	$("#project_tasks_toggle").on("click", function() {
		$("#m_dynamic_content_project").children().hide();
		
		$("#tasks-fragment").show();
		
		});

	$("#project_operations_toggle").on("click", function() {
		$("#m_dynamic_content_project").children().hide();
		DatatableOperationsJsonRemote.init();
		$("#operations-fragment").show();
	});

	$("#project_issues_toggle").on("click", function() {
		$("#m_dynamic_content_project").children().hide();
		$("#issues-fragment").show();	
	});

	$("#project_calendar_toggle").on("click", function() {
		$("#m_dynamic_content_project").children().hide();
		$("#calendar-fragment").show();
	});

}

function populateServicesListNewTaskForm() {
	var selectedProjectId = $("#select_project_new_task").val();
	if(selectedProjectId != " ")
	$.ajax({
		type : "GET",
		url : '/set-selected-project-services',
		data : 'projectId=' + selectedProjectId,
		success : function(response) {

			var html_text = "";
			for (i = 0; i < response.length; i++) {
				html_text += "<option value='" + response[i].id + "'>"
						+ response[i].name + "</option>"

			}
			$("#select_service_new_task").html(html_text);
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
		success : function(response) {

			var html_select_options = "";

			var html_text = "<option value=' ' selected>Nothing Selected</option>";
			if (response.length == 1) {
				html_text += "<option value='" + response[0].id + "'>"
						+ response[0].name + "</option>";
				$("#select_project_new_task").html(html_text);
				$("#select_project_new_operation").html(html_text);
				updateSelectServices();
			} else {
				for (i = 0; i < response.length; i++) {

					html_text += "<option value='" + response[i].id + "'>" + response[i].name
							+ "</option>";
				}
				$("#select_project_new_task").html(html_text);
				$("#select_project_new_operation").html(html_text);

			}
			$("#select_project_new_operation").selectpicker('refresh');

			$("#select_project_new_task").selectpicker('refresh');
			
			$("#select_project_new_operation").change(function() {
				var projectId = $("#select_project_new_operation").val();
				
				if(projectId == ' '){ 
					$('#sites_map_container').attr("style","display:none;width:100%; height:50px;position: relative;");
					$('.sites_map_canvas').attr("style","position: absolute; display:none;top: 20%; right: 0; bottom: 0; left: 0;");
				}else{
					$('#sites_map_container').attr("style","width:100%; height:250px;position: relative;");
					$('.sites_map_canvas').attr("style","position: absolute; top: 20%; right: 0; bottom: 0; left: 0;");
					initializeSitesGmap(projectId);

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
									+ 'class="m-checkbox m-checkbox--success"> <input '
									+'id="'+response[i].id+'" type="checkbox">'
									+response[i].name+' <span></span>'
									+'</label>';
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
					
					
					
//					if(i==0){
//						navLinks += '<li id="'+response[i].id+'" style="display:none;" class="nav-item">'
//							+'<a href="#'+response[i].name+'" class="nav-link" data-toggle="tab" role="tab" aria-controls="'+response[i].name+'">'+response[i].name+'</a>'
//							+'</li>';
//		
//					}else{
//						navLinks += '<li id="'+response[i].id+'" style="display:none;" class="nav-item">'
//							+'<a href="#'+response[i].name+'" class="nav-link" data-toggle="tab" role="tab" aria-controls="'+response[i].name+'">'+response[i].name+'</a>'
//							+'</li>';
//					}
//						navContent +='<div name="nav_content_'+response[i].id+'" class="tab-pane fade" id="'+response[i].name+'" role="tabpanel">'
//						+'<div class="m-widget11">'
//						+'<div class="table-responsive">'
//							+'<!--begin::Table-->		'						 
//							+'<table class="table">'
//								+'<!--begin::Thead-->'
//								+'<thead>'
//								+'	<tr>'
//									+'	<td style="width:5%;" class="m-widget11__label">#</td>'
//										+'<td style="width:50%;" class="m-widget11__app">Name</td>'
//										+'<td style="width:15%;" class="m-widget11__sales"><span style="display:table;margin:0 auto;">HR</span></td>'
//										+'<td style="width:15%;" class="m-widget11__price"><span style="display:table;margin:0 auto;">Days</span></td>'
//										+'<td style="width:15%;" class="m-widget11__total"><span style="display:table;margin:0 auto;">Priority</span></td>'
//									+'</tr>'
//								+'</thead>'
//								+'<!--end::Thead-->'
//								+'<!--begin::Tbody-->'
//								+'<tbody name="tbody_'+response[i].id+'">'
//								+'</tbody>'
//								+'<!--end::Tbody-->	'									     
//							+'</table>'
//							+'<!--end::Table-->'
//						+'</div>'
//
//					+'</div>'
//								+'</div>';
		
					
					
				}
				
				$("div[id*=category_]").each(function (){
					//console.log($(this).attr('id'));
					if($(this).children().length > 0)
						$(this).parent().show();
				});
				//$('#service_templates_new_operation_checkbox_list').html(html_text);
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
	
	populateSelectOwnedProjects();
	$('#sites_map_container').attr("style","display:none;width:100%; height:50px;position: relative;");
	$('.sites_map_canvas').attr("style","position: absolute; display:none;top: 20%; right: 0; bottom: 0; left: 0;");
	

	
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
	




$(document).ready(function() {
	
	
	
	
	$("#select_service_div").hide();

	$("#select_project_new_task").change(function() {
		updateSelectServices();
	});

	
	
	  	
	projectDynamicContent();
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




