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
		url : 'home/add-project',
		data : "name=" + name + "&client=" + client + "&country=" + country
				+ "&currency=" + currency + "&owner=" + owner,
		success : function(response) {
			// we have the response
			if (response.status == "SUCCESS") {
				toastr.success("Project Added successfully", "Well done!");

				$('#project_name').val('');

				$('#error').hide('slow');
			} else {
				toastr.error("Couldn't add project", "Error");

				for (i = 0; i <= response.result.length; i++) {
					// errorInfo += "<br>" + (i + 1) + ". "
					// + response.result[i].code;
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
			alert('Error: ' + e);
		}
	});
}

function projectDashboardTaskPieChart() {
	var completed;
	var onGoing;
	
	piechart = document.getElementById("piechart");
	$.ajax({
		type : "GET",
		url : '/get-global-task-status',
		success : function(response) {
			// we have the response
			if (response.status == "SUCCESS") {
				completed = response.result[0];
				onGoing = response.result[1];
			} else {
				completed = 0;
				onGoing = 0;

			}
		},
		error : function(e) {
			alert('Error: ' + e);
		}
	});

	if (completed == 0 && onGoing == 0) {
		piechart.html("hello");
	} else {
		google.charts.load('current', {
			'packages' : [ 'corechart' ]
		});
		google.charts.setOnLoadCallback(drawChart);
	}
	
	function drawChart() {

		var data = google.visualization.arrayToDataTable([
				[ 'Task', 'Status' ], [ 'Open', onGoing ],
				[ 'Closed', completed ],

		]);

		var options = {
			width : 300,
			height : 200,
			pieHole : 0.4,
			pieSliceTextStyle: {
	            color: 'black',
	          },
			colors : [ '#f4516c', '#34bfa3' ],
			fontName : 'Poppins',
			legend : {
				position : 'bottom'
			}
		};

		var chart = new google.visualization.PieChart(document
				.getElementById('piechart'));

		chart.draw(data, options);
	}

}

function projectDynamicContent() {

	// projectId = $("#current_project_id").val();

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

$(document).ready(function() {

	projectDashboardTaskPieChart();

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

