function toggleOperationFragment(operationId){
	$('#operation_fragment_selected_operation_id').attr('value',operationId);
	var map_div = "#operation_map";	

	var coordinates = populateOperationSiteMap(operationId);
//	console.log("lat: "+coordinates[0]);
//	console.log("long: "+coordinates[1]);
	
	$("#operations-fragment").hide();
	populateServicesTabOperationFragment(operationId);
	populateOperationDetails(operationId);
	$("#operation-fragment").show();
	
	
	$('#operation_map').hide('fast');
	
	var map = new GMaps({
        div: map_div,
        lat: 20.203036, 
        lng: -24.834370,
        scrollwheel: false,
        disableDefaultUI: true,
    });
	map.addMarker({
        lat: coordinates[0],
        lng: coordinates[1],
        title: coordinates[2],
        
//        click: function(e,id,title) {
//       	  
//	            if (console.log) console.log(e);
//	            for (i in map.markers){
//		        	map.markers[i].setIcon();
//	
//	            }
//	            $(this).attr('icon','https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/48/Map-Marker-Marker-Outside-Chartreuse.png');
//	            $('#select_site_client_'+clientId).val($(this).attr('id'));
//	            $('#select_site_client_'+clientId).selectpicker('refresh');
//				
//        }
    });
	
	map.setZoom(4);
	map.refresh();
	$('#operation_map').show('fast');
}

function populateOperationSiteMap(operationId){

	var coordinates = [];
	$.ajax({
		type : "GET",
		url : '/get-operation-details',
		data : 'id=' + operationId,
		async: false,
		success : function(response) {
			coordinates.push(response.site.latitude);
			coordinates.push(response.site.longitude);
			coordinates.push(response.site.name);
		},
		error : function(e) {
			alert('Error: operation site ' + e);
		}
	});
	return coordinates;
	
}

function populateOperationDetails(operationId){
	
	$.ajax({
		type : "GET",
		url : '/get-operation-details',
		data : 'id=' + operationId,
		success : function(response) {
			$("#operation_fragment_header_name").html(response.name);
			$('#operation_project_nav_link').attr('href','/project?id='+response.project.id);
			$('#operation_project_nav_text').html(response.project.name)
		},
		error : function(e) {
			alert('Error: services ' + e);
		}
	});

}

function toggleAddNewServiceSidebar(operationId){
	var selectedProject = $('#selected_project_id').val();
	$('#m_quick_sidebar_add_toggle').click();
	$('#quick_sidebar_new_service_nav').click();
	$('#select_project_new_service').val(selectedProject).change();
	$('#select_operation_new_service').val(operationId);

	$('#select_project_new_service').selectpicker('refresh');
	$('#select_operation_new_service').selectpicker('refresh');
}

function toggleAddNewOperationSidebar(){
	$('#m_quick_sidebar_add_toggle').click();
	$('#quick_sidebar_new_operation_nav').click();
//	$('#select_project_new_operation').selectpicker('refresh');
	var projectId = $('#selected_project_id').val();
	$('#select_project_new_operation').val(projectId).change();
	
	$('#select_project_new_operation').selectpicker('refresh');
//	$('#sites_map_container').attr("style","width:100%; height:250px;position: relative;");
//	$('.sites_map_canvas').attr("style","position: absolute; top: 20%; right: 0; bottom: 0; left: 0;");
//	
}

function populateServicesTabOperationFragment(operationId){
	$('#project_subheader').hide();
	$('#operation_services_widget').html('');
	$.ajax({
		type : "GET",
		url : '/get-operation-services',
		data : 'id=' + operationId,
		async: false,
		success : function(response) {
			$('.operation_add_new_service').attr('onClick','toggleAddNewServiceSidebar('+operationId+')')
			if(response.length == 0){
				$("#operation_services_widget").hide();
				$('#operation_empty_services').show();
			}else{
				var html_text = "";
				for (i = 0; i < response.length; i++) {
					html_text += '<div class="m-widget4__item">'
							+	'<div class="m-widget4__ext">'
							+		'<span class="m-widget4__icon m--font-brand"> <i'
							+			' class="flaticon-more"></i>'
							+		'</span>'
							+	'</div>'
							+	'<div class="m-widget4__info">'
							+		'<a onclick="toggleServiceFragment('+response[i].id+')">'
							+			'<span class="m-widget4__text">'+response[i].name+' <span class="m-widget4__number m--font-info"> ('+response[i].category+') </span>'
							+				'</span>'
							+			'<span style="font-size:11px;">created on: <span style="color: #36a3f7;">'+response[i].creationDate+'</span></span>'
							+		'</a>'
							+	'</div>'
							+'<div class="m-widget4__ext">'
							+'<div style="width:200px !important;" class="progress m-progress--sm">'
							+'<div class="progress-bar m--bg-success" role="progressbar" style="width: '+ response[i].percentage+';" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"></div>'
							+'</div>'
							+'<span style="font-size:11px;" class="m-widget4__number m--font-info">'+response[i].taskCount+' Tasks</span>'
							+'<span style="font-size:11px;float:right;" class="m-widget4__number m--font-info">'+ response[i].percentage+'</span>'
							+'</div>'
//							+	'<div class="m-widget4__ext">'
//							+		'<span title="'+response[i].taskCount+' Tasks" class="m-widget4__number m--font-info"> '+response[i].taskCount+'</span>'
//							+	'</div>'
							+ '</div>';
					
					}
				$('#operation_empty_services').hide('fast');
				$("#operation_services_widget").html(html_text);
				$("#operation_services_widget").show();
			}
		},
		error : function(e) {
			alert('Error: services ' + e);
		}
	});

}



function toggleAddNewTaskSidebar(){
	$('#m_quick_sidebar_add_toggle').click();
	populateSidebarAdd();
	$('#quick_sidebar_new_task_nav').click();
	var projectId = $('#selected_project_id').val();
	var serviceId = $('#service_fragment_selected_service_id').val();
	//console.log('selected service id= '+serviceId);
	$('#select_project_new_task').val(projectId).change();
	$('#select_service_new_task').val(serviceId);
	//$('#select_service_div').show();
	$('#select_service_new_task').selectpicker('refresh');
	//$('#select_project_new_task').selectpicker('refresh');
	
	
}
function populateTaskFragmentDetails(taskId){
	populateTaskParents(taskId);
	$('#task_start_date_select').datepicker({
	    format: 'dd/mm/yyyy'
	});
	$('#task_end_date_select').datepicker({
	    format: 'dd/mm/yyyy'
	});
	
	$.ajax({
		type : "GET",
		url : '/get-task-details',
		data : 'id=' + taskId,
		async: false,
		success : function(response) {
			$('#portlet_task_name').html(response.name);
			$('#task_name_input').val(response.name);
			$('#task_status_select').val(response.status);
			
			$('#task_priority_select').val(response.priority);
		
			$('#task_start_date_select').val(response.startDate);
			$('#task_end_date_select').val(response.endDate);
			$('#task_estimation_hr_input').val(response.estimationHR);
			$('#task_estimation_days_input').val(response.estimationTime);
			toggleReadOnlyModeTask();
			
			
		},
		error : function(e) {
			alert('Error: Task ' + e);
		}
	});
}

function populateTaskParents(taskId){
	//var serviceId = $('#service_fragment_selected_service_id').val();
//	var operationId = $('#operation_fragment_selected_operation_id').val();
//	var projectId = $('#selected_project_id').val();
	$.ajax({
		type : "GET",
		url : '/get-task-parents',
		data : 'id=' + taskId,
		async: false,
		success : function(response) {
			$('#task_fragment_service').html(response.service);
			$('#task_fragment_operation').html(response.operation);
			$('#task_fragment_project').html(response.project);
		},
		error : function(e) {
			alert('Error: Task Parents ' + e);
		}
	});
	
}
function toggleTaskFragment(taskId){
	if($('#service-fragment').is(':visible'))
		$("#service-fragment").hide();
	if($('#tasks-fragment').is(':visible'))
		$('#tasks-fragment').hide();
	populateTaskFragmentDetails(taskId);
	$('#task_owner_select').select2({
		placeholder: "Task Owner(s)",
		width: '100%',
	});
	$('#task-fragment').show();
	$('#task_fragment_update_cancel').attr('onClick','populateTaskFragmentDetails('+taskId+')');
	$('#task_fragment_do_update').attr('onClick','doUpdateTask('+taskId+')');
	
}

function toggleUpdateModeTask(){
	$('#task_fragment_update_footer').show();
	$('#task_name_container').show();
	$('#task_fragment_details').find('select,input').each(function(){
		$(this).attr("disabled",false);
		if($(this).hasClass('m-bootstrap-select'))
			$(this).selectpicker('refresh');
		if($(this).hasClass('task_date'))
			$(this).datepicker('refresh');
	});
	
}

function toggleReadOnlyModeTask(){
	$('#task_fragment_update_footer').hide();
	$('#task_name_container').hide();
	$('#task_fragment_details').find('select,input').each(function(){
		$(this).attr("disabled",true);
		if($(this).hasClass('m-bootstrap-select'))
			$(this).selectpicker('refresh');
		if($(this).hasClass('task_date'))
			$(this).datepicker('refresh');
	});
}

function doUpdateTask(id){
	var name = $('#task_name_input').val();
	var status = $('#task_status_select').val();
	var priority = $('#task_priority_select').val();
	var startDate = $('#task_start_date_select').val();
	var endDate = $('#task_end_date_select').val();
	var estimationHR = $('#task_estimation_hr_input').val();
	var estimationTime = $('#task_estimation_days_input').val();
	var service = $('#service_fragment_selected_service_id').val();
	
	var nameError = $('#task_fragment_name_error');
	var taskStartDateError = $('#task_fragment_startDate_error');
	var taskEndDateError = $('#task_fragment_endDate_error');
	
	nameError.hide('fast');
	taskStartDateError.hide('fast');
	taskEndDateError.hide('fast');

	
	$.ajax({
		type : "POST",
		url : '/update-task',
		data : "id="+id+"&name="+name+"&status="+status+"&priority="+priority+"&startDate="+startDate+"&endDate="+endDate
		+"&estimationRH="+estimationHR+"&estimationTime="+estimationTime+"&service="+service,
	    success : function(response) {
			// we have the response
			if (response.status == "SUCCESS") {
				toastr.success("Task updated successfully", "Well done!");
				if(response.result != 'undefined'){
					//doUpdateServiceTemplatesToBoq(id);
					$("#edit-boq-details").modal('hide');
					populateTaskFragmentDetails(id);
					$('#service_tasks_datatable').mDatatable('reload');
				}
			} else {
				if(response.result == 'task-exists'){
					toastr.error("Couldn't update Task, a task with this name already exists", "Change Task name");
				}else{
				toastr.error("Couldn't update Task", "Error");
				}
				
				//todo
				for (i = 0; i < response.result.length; i++) {
					if (response.result[i].code == "task.name.empty")
						nameError.show('slow');
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
			toastr('Error: can\'t update Task ', e);
		}
	});	
}

function populateTasksFragmentWidget(){
	var projectId = $('#selected_project_id').val();
	$.ajax({
		type : "GET",
		url : '/get-project-tasks-stats',
		data : 'id=' + projectId,
		async: false,
		success : function(response) {
			$('#tasks_fragment_all_tasks_count').html(response.totalTasks);
			$('#tasks_fragment_open_tasks_percentage').html(response.ongoing);
			$('#tasks_fragment_closed_tasks_percentage').html(response.completed);

		},
		error : function(e) {
			alert('Error: Project stats widget ' + e);
		}
	});
}

function setSelectedService(serviceId){
	$('#service_fragment_selected_service_id').attr('value',serviceId);
}
