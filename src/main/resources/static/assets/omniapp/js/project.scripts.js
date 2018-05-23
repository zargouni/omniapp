function populateProjectDetails() {
	var projectId = $('#selected_project_id').val();
	$.ajax({
		type : "GET",
		url : '/get-project-details',
		data : 'id=' + projectId,
		async : false,
		success : function(response) {
			$('#unassigned_tasks').html(response.unassignedTasksCount);
			$('#overdue_tasks').html(response.overdueTasksCount);
			$('#tasks_count').html(response.tasksCount)
		},
		error : function(e) {
			alert('Error: project details ' + e);
		}
	});
}

function filterMarkersByOperationStatus(map,status){
    for (i = 0; i < map.markers.length; i++) {
        if (map.markers[i].status.match(status) || status.length === 0) {
        	map.markers[i].setVisible(true);
        }
        else {
        	map.markers[i].setVisible(false);
        }
    }
}

function handleDashboardMapControls(map){
	//var googleMapWidth = $("#m_portlet_tab_1_2").css('width');
	//var googleMapHeight = $("#m_portlet_tab_1_2").css('height');

	$('#btn-enter-full-screen-dashboard-map').click(function() {
		
//		if(!(clientId % 2))
//			$('#clients_container_odd_row').hide();
//		else
//			$('#clients_container_even_row').hide();
//		
		$('#general-header').hide();
		
	    $('#dashboard_map').parent().css({
	        position: 'fixed',
	        top: 0,
	        zIndex: 2,
	        left: 0,
	        width: '100%',
	        height: '100%',
	        backgroundColor: 'white'
	    });

	    $('#dashboard_map').css({
	        height: '100%'
	    });

	    google.maps.event.trigger(map, 'resize');
	    //map.setCenter(newyork);

	    // Gui
	    $('#btn-enter-full-screen-dashboard-map').toggle();
	    $('#btn-exit-full-screen-dashboard-map').toggle();

	    return false;
	});

	$('#btn-exit-full-screen-dashboard-map').click(function() {
		
		$('#general-header').show();
		
	
		
		 $('#dashboard_map').parent().css({
	        position: 'relative',
	        top: 0,
	        zIndex: 1,
	        width: '100%',
	        height: '500px',
	        backgroundColor: 'transparent'
	    });

	    google.maps.event.trigger(map, 'resize');
	    //map.setCenter(newyork);

	    // Gui
	    $('#btn-enter-full-screen-dashboard-map').toggle();
	    $('#btn-exit-full-screen-dashboard-map').toggle();
	    return false;
	});
}

function populateDashboardMap() {
	var projectId = $('#selected_project_id').val();
	var map_div = "#dashboard_map";
	
	$('#operation_status_filter').on('change',function(){
		filterMarkersByOperationStatus(map,$(this).val())
	});

	var map = new GMaps({
		div : map_div,
		lat : 34.7615155,
		lng : 10.6630578,
		disableDefaultUI: true,
		
	});
	
	handleDashboardMapControls(map);

	$
			.ajax({
				type : "GET",
				url : '/get-project-operations-status',
				data : 'id=' + projectId,
				async : false,
				success : function(response) {
					var icons = {
						open : {
							icon : 'https://mt.google.com/vt/icon?psize=30&font=fonts/arialuni_t.ttf&color=ff304C13&name=icons/spotlight/spotlight-waypoint-a.png&ax=43&ay=48&text=•'
						},
						closed : {
							icon : 'https://mt.googleapis.com/vt/icon/name=icons/spotlight/spotlight-poi.png&scale=1'
						}
					};
					for (i = 0; i < response.length; i++) {
						map.addMarker({
							lat : response[i].latitude,
							lng : response[i].longitude,
							title : response[i].operationName + ' in '
									+ response[i].siteName + ' status:'
									+ response[i].status,
							id : response[i].operationId,
							status : response[i].status,
							icon : icons[response[i].status].icon,

						 click: function(e,id) {
							 //console.log("id: "+$(this).attr('id'));
							 
							 toggleOperationFragment($(this).attr('id'));
							if($('#general-header').is(':hidden'))
								$('#general-header').show();
							 
														
						 }
						});

					}
					map.setZoom(6);
					map.refresh();
				},
				error : function(e) {
					alert('Error: dashboard map ' + e);
				}
			});
}

function toggleOperationFragment(operationId) {
	$('#operation_fragment_selected_operation_id').attr('value', operationId);
	var map_div = "#operation_map";

	var coordinates = populateOperationSiteMap(operationId);
	// console.log("lat: "+coordinates[0]);
	// console.log("long: "+coordinates[1]);
	if($("#operations-fragment").is(':visible'))
		$("#operations-fragment").hide();
	if($("#dashboard-fragment").is(':visible'))
		$("#dashboard-fragment").hide();
	populateServicesTabOperationFragment(operationId);
	populateOperationDetails(operationId);
	populateOperationComments(operationId);
	$("#operation-fragment").show();

	$('#operation_map').hide('fast');

	var map = new GMaps({
		div : map_div,
		lat : 20.203036,
		lng : -24.834370,
		scrollwheel : false,
		disableDefaultUI : true,
	});
	map.addMarker({
		lat : coordinates[0],
		lng : coordinates[1],
		title : coordinates[2],

	// click: function(e,id,title) {
	//       	  
	// if (console.log) console.log(e);
	// for (i in map.markers){
	// map.markers[i].setIcon();
	//	
	// }
	// $(this).attr('icon','https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/48/Map-Marker-Marker-Outside-Chartreuse.png');
	// $('#select_site_client_'+clientId).val($(this).attr('id'));
	// $('#select_site_client_'+clientId).selectpicker('refresh');
	//				
	// }
	});

	map.setZoom(4);
	map.refresh();
	$('#operation_map').show('fast');
}

function populateOperationComments(operationId){
	$('#operation_comments_wrapper').html("");
	$.ajax({
		type : "GET",
		url : '/get-operation-comments',
		data : 'id=' + operationId,
		async : false,
		success : function(response) {
			var html_text = "";
			for(var i=0;i<response.length;i++){
				html_text += getCommentUI(response[i]);
			}
			
			$('#operation_comments_wrapper').html(html_text);
		},
		error : function(e) {
			alert('Error: operation site ' + e);
		}
	});
}

function getCommentUI(comment){
	return '<div class="m-widget3">'
	+'<div class="m-widget3__item">'
	+	'<div class="m-widget3__header">'
	+		'<div class="m-widget3__user-img">'
	+			'<img class="m-widget3__img"'
	+			'	src="assets/app/media/img/users/user-icon.png" alt="">'
	+		'</div>'
	+		'<div class="m-widget3__info">'
	+			'<span class="m-widget3__username">' + comment.user + '</span><br>'
	+			'<span class="m-widget3__time">' + comment.date + '</span>'
	+		'</div>'
	
	+	'</div>'
	+	'<div class="m-widget3__body">'
	+		'<p style="color: white;" class="m-widget3__text">'
	+ 			comment.content
	+		'</p>'
	+	'</div>'
	+'</div>'
+'</div>';
}

function doPostComment(){
	var comment = $('#comment_content').val();
	var operationId = $('#operation_fragment_selected_operation_id').val();
	$.ajax({
		type : "POST",
		url : '/do-post-comment',
		data : 'id=' + operationId+"&content="+comment,
		async : false,
		success : function(response) {
			if(response.status == "FAIL")
				toastr.error("Couldn't post comment","Error");
			else if(response.status == "NOCONTENT")
				toastr.error("A comment cannot be empty","Hey !");
			else{
				$('#comment_content').val("");
				populateOperationComments(operationId);
			}
		},
		error : function(e) {
			alert('Error: comment operation ' + e);
		}
	});

}

function populateOperationSiteMap(operationId) {

	var coordinates = [];
	$.ajax({
		type : "GET",
		url : '/get-operation-details',
		data : 'id=' + operationId,
		async : false,
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

function populateOperationDetails(operationId) {

	$.ajax({
		type : "GET",
		url : '/get-operation-details',
		data : 'id=' + operationId,
		success : function(response) {
			$("#operation_fragment_header_name").html(response.name + ' <span style="color:#F4516C;"><small>in '
							+ response.site.name + '</small></span>');
			$('#operation_project_nav_link').attr('href',
					'/project?id=' + response.project.id);
			$('#operation_project_nav_text').html(response.project.name)
			$('#operation_price')
					.html(response.price + " " + response.currency);
		},
		error : function(e) {
			alert('Error: services ' + e);
		}
	});

}

function toggleAddNewServiceSidebar(operationId) {
	var selectedProject = $('#selected_project_id').val();
	$('#m_quick_sidebar_add_toggle').click();
	$('#quick_sidebar_new_service_nav').click();
	$('#select_project_new_service').val(selectedProject).change();
	$('#select_operation_new_service').val(operationId);

	$('#select_project_new_service').selectpicker('refresh');
	$('#select_operation_new_service').selectpicker('refresh');
}

function toggleAddNewOperationSidebar() {
	$('#m_quick_sidebar_add_toggle').click();
	$('#quick_sidebar_new_operation_nav').click();
	// $('#select_project_new_operation').selectpicker('refresh');
	var projectId = $('#selected_project_id').val();
	$('#select_project_new_operation').val(projectId).change();

	$('#select_project_new_operation').selectpicker('refresh');
	// $('#sites_map_container').attr("style","width:100%;
	// height:250px;position: relative;");
	// $('.sites_map_canvas').attr("style","position: absolute; top: 20%; right:
	// 0; bottom: 0; left: 0;");
	//	
}

function populateServicesTabOperationFragment(operationId) {
	$('#project_subheader').hide();
	$('#operation_services_widget').html('');
	$
			.ajax({
				type : "GET",
				url : '/get-operation-services',
				data : 'id=' + operationId,
				async : false,
				success : function(response) {
					$('.operation_add_new_service').attr('onClick',
							'toggleAddNewServiceSidebar(' + operationId + ')')
					if (response.length == 0) {
						$("#operation_services_widget").hide();
						$('#operation_empty_services').show();
					} else {
						var html_text = "";
						for (i = 0; i < response.length; i++) {
							html_text += '<div class="m-widget4__item">'
									+ '<div class="m-widget4__ext">'
									+ '<span class="m-widget4__icon m--font-brand"> <i'
									+ ' class="flaticon-more"></i>'
									+ '</span>'
									+ '</div>'
									+ '<div class="m-widget4__info">'
									+ '<a href="#" onclick="toggleServiceFragment('
									+ response[i].id
									+ ')">'
									+ '<span class="m-widget4__text">'
									+ response[i].name
									+ ' <span class="m-widget4__number m--font-info"> ('
									+ response[i].category
									+ ') </span>'
									+ '</span>'
									+ '<span style="font-size:11px;">created on: <span style="color: #36a3f7;">'
									+ response[i].creationDate
									+ '</span></span>'
									+ '</a>'
									+ '</div>'
									+ '<div class="m-widget4__ext">'
									+ '<div style="width:200px !important;" class="progress m-progress--sm">'
									+ '<div class="progress-bar m--bg-success" role="progressbar" style="width: '
									+ response[i].percentage
									+ ';" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"></div>'
									+ '</div>'
									+ '<span style="font-size:11px;" class="m-widget4__number m--font-info">'
									+ response[i].taskCount
									+ ' Tasks</span>'
									+ '<span style="font-size:11px;float:right;" class="m-widget4__number m--font-info">'
									+ response[i].percentage
									+ '</span>'
									+ '</div>'
									// + '<div class="m-widget4__ext">'
									// + '<span title="'+response[i].taskCount+'
									// Tasks" class="m-widget4__number
									// m--font-info">
									// '+response[i].taskCount+'</span>'
									// + '</div>'
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

function toggleAddNewTaskSidebar() {
	$('#m_quick_sidebar_add_toggle').click();
	populateSidebarAdd();
	$('#quick_sidebar_new_task_nav').click();
	var projectId = $('#selected_project_id').val();
	var serviceId = $('#service_fragment_selected_service_id').val();
	// console.log('selected service id= '+serviceId);
	$('#select_project_new_task').val(projectId).change();
	$('#select_service_new_task').val(serviceId);
	// $('#select_service_div').show();
	$('#select_service_new_task').selectpicker('refresh');
	// $('#select_project_new_task').selectpicker('refresh');

}

function populateTaskOwnerSelect(taskId) {

	$.ajax({
		type : "GET",
		url : '/get-all-users-in-task-details-json',
		async : false,
		data : "id=" + taskId,
		success : function(response) {
			var html_text = '';
			for (i = 0; i < response.length; i++) {
				if (response[i].selected == true)
					html_text += '<option selected value="' + response[i].id
							+ '">' + response[i].firstName + ' '
							+ response[i].lastName + '</option>';
				else
					html_text += '<option value="' + response[i].id + '">'
							+ response[i].firstName + ' '
							+ response[i].lastName + '</option>';

			}
			$('#task_owner_select').html(html_text);
			// $('#task_owner_select').selectpicker('refresh');
		},
		error : function(e) {
			alert('Error: can"t get users ' + e);
		}
	});

}
function populateTaskFragmentDetails(taskId) {
	populateTaskParents(taskId);
	populateTaskOwnerSelect(taskId);
	$('#task_start_date_select').datepicker({
		format : 'dd/mm/yyyy'
	});
	$('#task_end_date_select').datepicker({
		format : 'dd/mm/yyyy'
	});
	

	$.ajax({
		type : "GET",
		url : '/get-task-details',
		data : 'id=' + taskId,
		async : false,
		success : function(response) {
			var html_text = "";
			$('#portlet_task_name').html(response.name);
			$('#task_name_input').val(response.name);
			$('#task_status_select').val(response.status);

			$('#task_priority_select').val(response.priority);

			$('#task_start_date_select').val(response.startDate);
			$('#task_end_date_select').val(response.endDate);
			$('#task_estimation_hr_input').val(response.estimationHR);
			$('#task_estimation_days_input').val(response.estimationTime);
			toggleReadOnlyModeTask();
			for(i=0 ; i < response.files.length ; i++){
				html_text += '<div><div class="row">'
				+'<div class="col-md-8"><i class="fa fa-file-text"></i><a target="self" href="/download-attachment?id='+response.files[i].id+'"> '
				+response.files[i].name+'</a>'
				+'<span><p><small>size: '+parseInt(response.files[i].size/1024) +' KB</small>'
				+'<small> Added on: '+response.files[i].creationDate+'</small></p></span>'
				+'</div>'
				+'<div class="col-md-4"><a href="#" onclick="handleDeleteAttachment('+response.files[i].id+')" class="btn btn-danger m-btn m-btn--icon m-btn--icon-only m-btn--pill">'
				+'<i class="la la-close"></i>'
				+'</a>'
				+'</div>'
				+'</div>'
				
				+'</div>';
			}
			$('#attachments_div').html(html_text);

		},
		error : function(e) {
			alert('Error: Task ' + e);
		}
	});
}

function handleDeleteAttachment(fileId){
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
								url : '/delete-attachment',
								data : "id=" + fileId,
								async : true,
								success : function(response) {
									if (response.status == "SUCCESS") {
										swal('Deleted!',
												'Attachment has been deleted.',
												'success');
										populateTaskFragmentDetails($('#selected_task_id').val());
										
									} else {
										swal('Fail!', 'Attachment not deleted.',
												'error');
									}

								},
								error : function(e) {
									toastr.error("Couldn't delete Attachment",
											"Server Error");
									result = false;
								}
							});

				}

			}

	);
	
}

function populateTaskParents(taskId) {
	// var serviceId = $('#service_fragment_selected_service_id').val();
	// var operationId = $('#operation_fragment_selected_operation_id').val();
	// var projectId = $('#selected_project_id').val();
	$.ajax({
		type : "GET",
		url : '/get-task-parents',
		data : 'id=' + taskId,
		async : false,
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
function toggleTaskFragment(taskId) {
	if ($('#service-fragment').is(':visible'))
		$("#service-fragment").hide();
	if ($('#tasks-fragment').is(':visible'))
		$('#tasks-fragment').hide();
	populateTaskFragmentDetails(taskId);
	
	$('#selected_task_id').val(taskId);

	$('#task-fragment').show();
	$('#task_fragment_update_cancel').attr('onClick',
			'populateTaskFragmentDetails(' + taskId + ')');
	$('#task_fragment_do_update').attr('onClick',
			'doUpdateTask(' + taskId + ')');

}

function toggleUpdateModeTask() {
	$('#task_fragment_update_footer').show();
	$('#task_name_container').show();
	$('#task_fragment_details').find('select,input').each(function() {
		$(this).attr("disabled", false);
		if ($(this).hasClass('m-bootstrap-select'))
			$(this).selectpicker('refresh');
		if ($(this).hasClass('task_date'))
			$(this).datepicker('refresh');
	});

}

function toggleReadOnlyModeTask() {
	$('#task_fragment_update_footer').hide();
	$('#task_name_container').hide();
	$('#task_fragment_details').find('select,input').each(function() {
		$(this).attr("disabled", true);
		if ($(this).hasClass('m-bootstrap-select'))
			$(this).selectpicker('refresh');
		if ($(this).hasClass('task_date'))
			$(this).datepicker('refresh');
	});
}

function doUpdateTask(id) {
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

	if ($('#task_owner_select').find(":selected").length != 0) {
		$
				.ajax({
					type : "POST",
					url : '/update-task',
					data : "id=" + id + "&name=" + name + "&status=" + status
							+ "&priority=" + priority + "&startDate="
							+ startDate + "&endDate=" + endDate
							+ "&estimationRH=" + estimationHR
							+ "&estimationTime=" + estimationTime + "&service="
							+ service,
					success : function(response) {
						// we have the response
						if (response.status == "SUCCESS") {
							toastr.success("Task updated successfully",
									"Well done!");
							if (response.result != 'undefined') {
								doUpdateTaskOwners(id);
								$("#edit-boq-details").modal('hide');
								populateTaskFragmentDetails(id);
								$('#service_tasks_datatable').mDatatable(
										'reload');
							}
						} else {
							if (response.result == 'task-exists') {
								toastr
										.error(
												"Couldn't update Task, a task with this name already exists",
												"Change Task name");
							} else {
								toastr.error("Couldn't update Task", "Error");
							}

							// todo
							for (i = 0; i < response.result.length; i++) {
								if (response.result[i].code == "task.name.empty")
									nameError.show('slow');
								if (response.result[i].code == "task.date.nomatch")
									toastr.warning(
											"please select a valid date range",
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
	} else {
		toastr.warning(
				"Couldn't update Task, you have to select at least 1 owner",
				"Select Owner(s)");
	}
}

function doUpdateTaskOwners(taskId) {
	$('#task_owner_select').find(":selected").each(function() {
		var user = $(this).attr('value');
		// alert('selected values: '+$(this).attr('value'));
		// alert('task: '+taskId);
		$.ajax({
			type : "POST",
			url : '/update-task-owners',
			data : "id=" + taskId + "&userId=" + user,
			success : function(response) {
				// we have the response
				if (response.status == "FAIL") {
					toastr.error("Couldn't add owner", "Error");
				}
			},
			error : function(e) {
				console.log("error: " + e);
				toastr.error("Couldn't add owner", "Server Error");
			}
		});
	});

}

function populateTasksFragmentWidget() {
	var projectId = $('#selected_project_id').val();
	$.ajax({
		type : "GET",
		url : '/get-project-tasks-stats',
		data : 'id=' + projectId,
		async : false,
		success : function(response) {
			$('#tasks_fragment_all_tasks_count').html(response.totalTasks);
			$('#tasks_fragment_open_tasks_percentage').html(response.ongoing);
			$('#tasks_fragment_closed_tasks_percentage').html(
					response.completed);

		},
		error : function(e) {
			alert('Error: Project stats widget ' + e);
		}
	});
}

function setSelectedService(serviceId) {
	$('#service_fragment_selected_service_id').attr('value', serviceId);
}

function projectDynamicContent() {
	projectDashboardTaskPieChart();
	populateDashboardMap();
	$("#dashboard-fragment").show();
	$('#project_subheader').show();

	$("#project_dashboard_toggle").on("click", function(event) {
		$('#project_subheader').show();
		populateDashboardMap();
		$("#m_dynamic_content_project").children().hide();
		projectDashboardTaskPieChart();
		$("#dashboard-fragment").show();
	});

	$("#project_feed_toggle").on("click", function() {
		$('#project_subheader').show();
		$("#m_dynamic_content_project").children().hide();
		$("#feed-fragment").show();

	});

	$("#project_tasks_toggle").on("click", function() {
		$('#project_subheader').show();
		$("#m_dynamic_content_project").children().hide();
		populateTasksFragmentWidget();
		$("#tasks-fragment").show();

	});

	$("#project_operations_toggle").on("click", function() {
		$('#project_subheader').show();
		$("#m_dynamic_content_project").children().hide();
		DatatableOperationsJsonRemote.init();
		$("#operations-fragment").show();
	});

	$("#project_issues_toggle").on("click", function() {
		$('#project_subheader').show();
		$("#m_dynamic_content_project").children().hide();
		$("#issues-fragment").show();
	});

	$("#project_calendar_toggle").on("click", function() {
		$('#project_subheader').show();
		$("#m_dynamic_content_project").children().hide();
		$("#calendar-fragment").show();
	});

}

$(document).ready(function() {
	
	
	populateProjectDetails();
	projectDynamicContent();
	
	// bootstrap dropzone
	// Get the template HTML and remove it from the doumenthe template HTML and
	// remove it from the doument
	var previewNode = document.querySelector("#template");
	previewNode.id = "";
	var previewTemplate = previewNode.parentNode.innerHTML;
	previewNode.parentNode.removeChild(previewNode);

	var myDropzone = new Dropzone("#task-fragment", { // Make the whole body a
														// dropzone
	  url: "/upload", // Set the url
	  thumbnailWidth: 80,
	  thumbnailHeight: 80,
	  parallelUploads: 20,
	  previewTemplate: previewTemplate,
	  autoQueue: false, // Make sure the files aren't queued until manually
						// added
	  previewsContainer: "#previews", // Define the container to display the
										// previews
	  clickable: ".fileinput-button", // Define the element that should be
										// used as click trigger to select
										// files.
	  acceptedFiles: "image/*,application/pdf, .doc, .docx, .xls",
	});

	myDropzone.on("addedfile", function(file) {
		if($('#previews').is(':hidden'))
		$('#previews').show();
	  // Hookup the start button
	  file.previewElement.querySelector(".start").onclick = function() { myDropzone.enqueueFile(file); };
	});

	// Update the total progress bar
	myDropzone.on("totaluploadprogress", function(progress) {
	  document.querySelector("#total-progress .progress-bar").style.width = progress + "%";
	});

	myDropzone.on("sending", function(file, xhr, formData) {
	  // Show the total progress bar when upload starts
		 formData.append('id',$('#selected_task_id').val());
	  document.querySelector("#total-progress").style.opacity = "1";
	  // And disable the start button
	  file.previewElement.querySelector(".start").setAttribute("disabled", "disabled");
	});

	// Hide the total progress bar when nothing's uploading anymore
	myDropzone.on("queuecomplete", function(progress) {
	  document.querySelector("#total-progress").style.opacity = "0";
	  toastr.success("Attachment uploaded successfully","Well Done")
	  $('#previews').hide('slow');
	  populateTaskFragmentDetails($('#selected_task_id').val());
	});

	// Setup the buttons for all transfers
	// The "add files" button doesn't need to be setup because the config
	// `clickable` has already been specified.
	document.querySelector("#actions .start").onclick = function() {
	  myDropzone.enqueueFiles(myDropzone.getFilesWithStatus(Dropzone.ADDED));
	};
	document.querySelector("#actions .cancel").onclick = function() {
	  myDropzone.removeAllFiles(true);
	};
	// end bootstrap dropzone
	
	
});
