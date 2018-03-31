function doAddProjectAjaxPost() {
	// get the form values
	var nameError = $('#project_name_error');
	var clientError = $('#project_client_error');
	var countryError = $('#project_country_error');
	var currencyError = $('#project_currency_error');
	var name = $('#project_name').val();
	var client = $('#project_client').val();
	var country = $('#project_country').val();
	var currency = $('#project_currency').val();
	var owner = $('#project_owner').val();

	nameError.hide('fast');
	clientError.hide('fast');
	countryError.hide('fast');
	currencyError.hide('fast');

	$.ajax({
		type : "POST",
		url : '/add-project',
		data : "name=" + name + "&client=" + client + "&country=" + country
				+ "&currency=" + currency + "&owner=" + owner,
		success : function(response) {
			// we have the response
			if (response.status == "SUCCESS") {
				toastr.success("Project Added successfully", "Well done!");

				$('#project_name').val('');
				$('#project_client').val('');
				$('#project_country').val('');
				$('#project_currency').val('');

				$('#error').hide('slow');
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
				}

			}
		},
		error : function(e) {
			toastr.error("Couldn't add project", "Server Error");
		}
	});
}

function doAddOperationAjaxPost() {
	// get the form values
	var nameError = $('#operation_name_error');
	var projectError = $('#operation_project_error');
	var startDateError = $('#operation_startDate_error');
	var endDateError = $('#operation_endDate_error');
	var name = $('#operation_name').val();
	var project = $('#select_project_new_operation').val();
	var startDate = $('#m_datepicker_4_3').val();
	var endDate = $('#m_datepicker_4_4').val();
//	var currency = $('#project_currency').val();
//	var owner = $('#project_owner').val();

	nameError.hide('fast');
	projectError.hide('fast');
	startDateError.hide('fast');
	endDateError.hide('fast');

	$.ajax({
		type : "POST",
		url : '/add-operation',
		data : "name=" + name + "&project=" + project + "&startDate=" + startDate
				+ "&endDate=" + endDate,
		success : function(response) {
			// we have the response
			
			if (response.status == "SUCCESS") {
				toastr.success("Operation Added successfully", "Well done!");

				$('#operation_name').val('');
				$('#select_project_new_operation').val('');
				$('#m_datepicker_4_3').val('');
				$('#m_datepicker_4_4').val('');

				//$('#error').hide('slow');
			} else {
				//toastr.error("Couldn't add operation", "Error");

				for (i = 0; i < response.result.length; i++) {
					console.log(response.result[i].code);
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
				}

			}
		},
		error : function(e) {
			toastr.error("Couldn't add operation", "Server Error");
		}
	});
}

function doAddTaskAjaxPost() {
	// get the form values
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
			// we have the response
			if (response.status == "SUCCESS") {
				toastr.success("Task added successfully", "Well done!");

				$('#task_name').val('');
				$('#m_datepicker_4_1').val('');
				$('#m_datepicker_4_2').val('');
				$('#task_priority').val('');

				// $('#error').hide('slow');
			} else {
				// toastr.error("Couldn't add task", "Error");

				for (i = 0; i < response.result.length; i++) {

					if (response.result[i].code == "task.name.empty")
						nameError.show('slow');
					if (response.result[i].code == "task.service.empty")
						// serviceError.show('slow');
						toastr.warning("you must select a service",
								"Service required");
					if (response.result[i].code == "task.date.nomatch")
						// taskDateError.show('slow');
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

	$("#m_dynamic_content_project").load("/dashboard");

	$("#project_dashboard_toggle").on("click", function() {
		$("#m_dynamic_content_project").load("/dashboard");
	});

	$("#project_feed_toggle").on("click", function() {
		$("#m_dynamic_content_project").load("/feed");
	});

	$("#project_tasks_toggle").on("click", function() {
		$("#m_dynamic_content_project").load("/tasks");
	});

	$("#project_operations_toggle").on("click", function() {
		$("#m_dynamic_content_project").load("/operations");
	});

	$("#project_issues_toggle").on("click", function() {
		$("#m_dynamic_content_project").load("/issues");
	});

	$("#project_calendar_toggle").on("click", function() {
		$("#m_dynamic_content_project").load("/calendar");
	});

}

function populateServicesListNewTaskForm() {
	var selectedProjectId = $("#select_project_new_task").val();
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
	// alert($(this).val());
	populateServicesListNewTaskForm();
	$("#select_service_div").show();

}

function populateSelectOwnedProjects() {
	$.ajax({
		type : "GET",
		url : '/set-select-owned-projects',
		success : function(response) {

			var html_select_options = "";

			var html_text = "";
			var selectedOption = "";
			if (response.length == 1) {
				html_text += "<option value='" + response[0].id + "'>"
						+ response[0].name + "</option>";
				$("#select_project_new_task").html(html_text);
				$("#select_project_new_operation").html(html_text);
				updateSelectServices();
			} else {
				for (i = 0; i < response.length; i++) {
					if (i == 0)
						selectedOption = "selected";
					else
						selectedOption = "";
					html_text += "<option value='" + response[i].id + "' "
							+ selectedOption + " >" + response[i].name
							+ "</option>";
				}
				$("#select_project_new_task").html(html_text);
				$("#select_project_new_operation").html(html_text);

			}
			$("#select_project_new_operation").selectpicker('refresh');

			$("#select_project_new_task").selectpicker('refresh');

		},
		error : function(e) {
			$("#select_project_new_task").html(
					"<option value=''>Nothing selected</option>");
			$("#select_project_new_operation").html(
					"<option value=''>Nothing selected</option>");

		}
	});

}

function populateSidebarAdd() {
	populateSelectOwnedProjects();
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

// == Class definition

