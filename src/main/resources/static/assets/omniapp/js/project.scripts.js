function doUpdateIssue(issueId) {
	var name = $('#issue_name_input').val();
	var status = $('#issue_status_select').val();
	var severity = $('#issue_severity_select').val();
	var description = $('#issue_description').val();
	var endDate = $('#issue_end_date_select').val();
//	var operation = $('#issue_operation')
//	var estimationHR = $('#task_estimation_hr_input').val();
//	var estimationTime = $('#task_estimation_days_input').val();
//	var service = $('#service_fragment_selected_service_id').val();

	var nameError = $('#issue_fragment_name_error');
	var descriptionError = $('#issue_fragment_description_error');
	var EndDateError = $('#issue_fragment_endDate_error');

	nameError.hide('fast');
//	taskStartDateError.hide('fast');
	EndDateError.hide('fast');
	descriptionError.hide('fast');

	if ($('#issue_owner_select').find(":selected").length != 0) {
		$
				.ajax({
					type : "POST",
					url : '/update-issue',
					async: false,
					data : "id=" + issueId + "&name=" + name + "&status=" + status
							+ "&severity=" + severity + "&endDate="
							+ endDate + "&description=" + description
//							+ "&operationId="
//							+ operation
							,
					success : function(response) {
						// we have the response
						if (response.status == "SUCCESS") {
							toastr.success("Issue updated successfully",
									"Well done!");
							//if (response.result != 'undefined') {
								doUpdateIssueOwners(issueId);
								populateIssueFragmentDetails(issueId);
								$('#issues_datatable').mDatatable(
										'reload');
							//}
						} else {
							
								toastr.error("Couldn't update Issue", "Error");
							

							// todo
							for (i = 0; i < response.result.length; i++) {
								console.log(response.result[i].code);
								if (response.result[i].code == "issue.name.empty")
									nameError.show('slow');
								if (response.result[i].code == "issue.description.empty")
									descriptionError.show('slow');
								if (response.result[i].code == "task.endDate.empty")
									endDateError.show('slow');
								if (response.result[i].code == "issue.operation.empty")
									toastr.warning("no op");
							}

						}
					},
					error : function(e) {
						toastr('Error: can\'t update issue ', e);
					}
				});
	} else {
		toastr.warning(
				"Couldn't update Issue, you have to select at least 1 owner",
				"Select Owner(s)");
	}
}

function doUpdateIssueOwners(issueId) {
	$('#issue_owner_select').find(":selected").each(function() {
		var user = $(this).attr('value');
		
		$.ajax({
			type : "POST",
			url : '/update-issue-owners',
			async: false,
			data : "id=" + issueId + "&userId=" + user,
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


function populateIssueFragmentDetails(issueId) {
	
	
//	$('#task_start_date_select').datepicker({
//		format : 'dd/mm/yyyy'
//	});
	$('#issue_end_date_select').datepicker({
		format : 'dd/mm/yyyy'
	});
	

	$.ajax({
		type : "GET",
		url : '/get-issue-details',
		data : 'id=' + issueId,
		async : false,
		success : function(response) {
			var html_text = "";
			$('#portlet_issue_name').html(response.name);
			$('#issue_name_input').val(response.name);
			$('#issue_status_select').val(response.status);

			$('#issue_severity_select').val(response.severity);
			$('#issue_description').val(response.description);
			

			//$('#task_start_date_select').val(response.startDate);
			$('#issue_end_date_select').val(response.endDate);
//			$('#task_estimation_hr_input').val(response.estimationHR);
//			$('#task_estimation_days_input').val(response.estimationTime);
			toggleReadOnlyModeIssue();
			for(i=0 ; i < response.files.length ; i++){
				html_text += '<div><div class="row">'
				+'<div class="col-md-8"><i class="fa fa-file-text"></i><a target="self" href="/attachment?id='+response.files[i].id+'"> '
				+response.files[i].name+'</a>'
				+'<span><p><small>size: '+parseInt(response.files[i].size/1024) +' KB</small>'
				+'<small> Added on: '+response.files[i].creationDate+'</small></p></span>'
				+'</div>'
				+'<div style="display:none;" class="delete_attachment" class="col-md-4"><a href="#" onclick="handleDeleteAttachment('+response.files[i].id+')" class="btn btn-danger m-btn m-btn--icon m-btn--icon-only m-btn--pill">'
				+'<i class="la la-close"></i>'
				+'</a>'
				+'</div>'
				+'</div>'
				
				+'</div>';
			}
			$('#issue_attachments_div').html(html_text);

		},
		error : function(e) {
			alert('Error: Task ' + e);
		}
	});
	
	populateIssueParents(issueId);
	populateIssueOwnerSelect(issueId);
	
}

function populateIssueParents(issueId) {
	// var serviceId = $('#service_fragment_selected_service_id').val();
	// var operationId = $('#operation_fragment_selected_operation_id').val();
	// var projectId = $('#selected_project_id').val();
	$.ajax({
		type : "GET",
		url : '/get-issue-parents',
		data : 'id=' + issueId,
		async : false,
		success : function(response) {
			$('#issue_fragment_operation').html(response.operation);
			$('#issue_fragment_project').html(response.project);
//			$('#issue_operation').val(response.operation_id);
		},
		error : function(e) {
			alert('Error: Issue Parents ' + e);
		}
	});

}

function populateIssueOwnerSelect(issueId) {
	$('.attachments_block').hide();
	// console.log("session user id: "+$('#session_user_id').val());
	$.ajax({
		type : "GET",
		url : '/get-all-users-in-issue-details-json',
		async : false,
		data : "id=" + issueId,
		success : function(response) {
			var html_text = '';
			for (i = 0; i < response.length; i++) {
				// console.log("user "+response[i].id);
				// console.log("task "+taskId);
				if (response[i].selected == true)
					html_text += '<option selected value="' + response[i].id
							+ '">' + response[i].firstName + ' '
							+ response[i].lastName + '</option>';
				else
					html_text += '<option value="' + response[i].id + '">'
							+ response[i].firstName + ' '
							+ response[i].lastName + '</option>';
				
				if(response[i].id == $('#session_user_id').val() && response[i].selected == true
						|| $('#session_user_id').val() == $('#project_manager_id').val()){
					// show attachments actions block if user appears in owners
					// list or user is PM
					$('.attachments_block').show();
					$('.delete_attachment').show();
					
					// show actions block if user appears in owners list or user
					// is PM
					$('#issue_actions').show();
				}

			}
			$('#issue_owner_select').html(html_text);
			$('#issue_owner_select').selectpicker('refresh');
		},
		error : function(e) {
			alert('Error: can"t get users in issue ' + e);
		}
	});

}

function toggleUpdateModeIssue() {
	$('#issue_fragment_update_footer').show();
	$('#issue_name_container').show();
	$('#issue_fragment_details').find('select,input,textarea').each(function() {
		$(this).attr("disabled", false);
		if ($(this).hasClass('m-bootstrap-select'))
			$(this).selectpicker('refresh');
		if ($(this).hasClass('issue_date'))
			$(this).datepicker('refresh');
	});

}

function toggleReadOnlyModeIssue() {
	$('#issue_fragment_update_footer').hide();
	$('#issue_name_container').hide();
	$('#issue_fragment_details').find('select,input,textarea').each(function() {
		$(this).attr("disabled", true);
		if ($(this).hasClass('m-bootstrap-select'))
			$(this).selectpicker('refresh');
		if ($(this).hasClass('issue_date'))
			$(this).datepicker('refresh');
	});
}

function toggleIssueFragment(issueId){
	
	$("#m_dynamic_content_project").children().hide();
	$('#issue-fragment').show();
	populateIssueFragmentDetails(issueId);
//	populateTaskComments(taskId);
	
	$('#selected_issue_id').val(issueId);

	
	$('#issue_fragment_update_cancel').attr('onClick',
			'populateIssueFragmentDetails(' + issueId + ')');
	$('#issue_fragment_do_update').attr('onClick',
			'doUpdateIssue(' + issueId + ')');
}

function doAddNewIssueAjax(){
	var name = $('#input_new_issue_name').val();
	var description = $('#input_new_issue_description').val();
	var dueDate = $('#input_new_issue_due_date').val();
	var severity = $('#input_new_issue_severity').val();
	var operationId = $('#input_new_issue_operation').val();
	var projectId = $('#selected_project_id').val();
	
	var nameError = $('#new_issue_name_error'); 
	var descriptionError = $('#new_issue_description_error');
	var dateError = $('#new_issue_date_error'); 
	var operationError = $('#new_issue_operation_error'); 
	
	nameError.hide('fast');
	descriptionError.hide('fast');
	dateError.hide('fast');
	operationError.hide('fast');


	if($('#input_new_issue_operation').val() == 'none'){
		toastr.warning("You have to select the concerned operation", "Select Operation");
		
	
	}else{
		$.ajax({
			type : "POST",
			url : '/add-issue',
			data : 'name=' + name+'&description='+description+'&endDate='+dueDate+'&severity='+severity+'&operation='+operationId
			+'&id='+projectId,
			async : false,
			success : function(response) {
				if (response.status == "SUCCESS") {
					if($('#issues_datatable').length){
						$('#issues_datatable').mDatatable('reload');
					}
					$('#m_modal_issue').modal('hide');
					doAddIssueOwners(response.result);
					toastr.success("Issue submitted successfully", "Well done!");
				}
					
				else{
					toastr.warning("Please check that all required fields are filled", "Error!");
					for (i = 0; i < response.result.length; i++) {
						console.log(response.result[i].code);
						if (response.result[i].code == "issue.name.empty")
							nameError.show('slow');
						if (response.result[i].code == "issue.description.empty")
							descriptionError.show('slow');
						if (response.result[i].code == "issue.endDate.empty")
							dateError.show('slow');

						
					}
				}
					
			},
			error : function(e) {
				alert('Error: couldnt add issue ' + e);
			}
		});
	}
	
	
}

function doAddIssueOwners(issueId){
	$('#input_new_issue_users').find(":selected").each(function(){
		var user = $(this).attr('value');
		// alert('selected values: '+$(this).attr('value'));
		// alert('task: '+taskId);
		$.ajax({
			type : "POST",
			url : '/add-issue-owner',
			data : "id=" + issueId + "&userId=" + user,
			success : function(response) {
				// we have the response
				if (response.status == "FAIL") {
					toastr.error("Couldn't add owner to issue", "Error");
				} 
			},
			error : function(e) {
				console.log("error: "+e);
				toastr.error("Couldn't add owner", "Server Error");
			}
		});
	});
		
	
}

function populateNewIssueModal(){
	var projectId = $('#selected_project_id').val();
	populateSelectOperations(projectId);
	populateSelectUsers();
}

function populateSelectOperations(projectId){
	$.ajax({
		type : "GET",
		url : '/json-operations',
		data : 'id=' + projectId,
		async : false,
		success : function(response) {
			var html_text = "<option value='none'>None</option>";
			for (i = 0; i < response.length; i++) {
				html_text += '<option value="'+response[i].id+'">'+response[i].name+'</option>';
				
			}
			
			
			$('#input_new_issue_operation').html(html_text);
			$('#input_new_issue_operation').selectpicker('refresh');
		},
		error : function(e) {
			alert('Error: project operations ' + e);
		}
	});
}

function populateSelectUsers(){
	$.ajax({
		type : "GET",
		url : '/get-all-users-json-details',
		async: false,
		success : function(response) {
			var html_text = "<option value=' '>None</option>";
			for (i = 0; i < response.length; i++) {
				html_text += '<option value="'+response[i].id+'">'+response[i].firstName+' '+response[i].lastName+'</option>';
				
			}
			$('#input_new_issue_users').html(html_text);
			$('#input_new_issue_users').selectpicker('refresh');
		},
		error : function(e) {
			alert('Error: can"t get users ' + e);
		}
	});
}

function populateOperationSnags(operationId){
	$('#operation_snags_wrapper').html("");
	$.ajax({
		type : "GET",
		url : '/get-operation-snags',
		data : 'id=' + operationId,
		async : false,
		success : function(response) {
			var html_text = "";
			$('#snags_count').html(response.length);
			if(response.length != 0)
			for(var i=0;i<response.length;i++){
				html_text += getSnagUI(response[i]);
			}
			else
				html_text = '<div class="m-widget24__item">'
				+'<div>'
				+'<div style="display: block; vertical-align: middle;">'
				+'<table width="100%" align="center" border="0"'
				+'	cellpadding="0" cellspacing="0">'
				+'	<tbody>'
				+'		<tr>'
				+'			<td align="center"><div'
				+'					 class="emptydashboardbox omnia fonticon40">'
				+'						<div class="m-demo-icon__preview">'
				+'							<i style="font-size: 40px;" class="flaticon-user-ok"></i>'
				+'					</div>'
				+'				</div></td>'
				+'		</tr>'
				+'		<tr>'
				+'			<td align="center" height="110px">'
				+'				<div>'
				+'					<span class="col777 pt12 lh25">This operation'
				+'						contains no snags. Look up, You can post a snag from there'
				+'						</span>'
				+'				</div>'
				+'			</td>'
				+'		</tr>'
				+'	</tbody>'
				+'</table>'
				+'</div>'
				+'</div>'
				+'</div>';
			
			$('#operation_snags_wrapper').html(html_text);
		},
		error : function(e) {
			alert('Error: operation comments ' + e);
		}
	});
}

function getSnagUI(snag){
	var bgColor;
	switch(snag.severity) {
    case 'low':
        bgColor = "#34bfa3";
        break;
    case 'medium':
    	bgColor = "#ffb822";
        break;
    default:
    	bgColor = "#f4516c";
}
	return '<div style="border-radius: 5px;background:'+bgColor+';padding: 10px 20px;" class="m-widget3">'
	+'<div class="m-widget3__item">'
	+	'<div class="m-widget3__header">'
	+		'<div class="m-widget3__user-img">'
	+			'<img class="m-widget3__img"'
	+			'	src="assets/app/media/img/users/user-icon.png" alt="">'
	+		'</div>'
	+		'<div class="m-widget3__info">'
	+			'<span style="color: white;"  class="m-widget3__username">' + snag.user + '</span><br>'
	+			'<span style="color: white;" class="m-widget3__time">' + snag.date + '</span>'
	+		'</div>'
	
	+	'</div>'
	+	'<div class="m-widget3__body">'
	+		'<p  style="color: white;" class="m-widget3__text">'
	+' <strong>'+snag.title+': </strong> '
	+ 			snag.content
	+		'</p>'
	+	'</div>'
	+'</div>'
+'</div>'
+'<hr style="background: #fff;" />';
}

function doPostSnag(){
	var content = $('#snag_content').val();
	var title = $('#snag_title').val();
	var severity = $('#snag_severity').val();
	var operationId = $('#operation_fragment_selected_operation_id').val();
	
	
	var titleError = $('#snag_title_error');
	var contentError = $('#snag_content_error');
	
	
	titleError.hide('fast');
	contentError.hide('fast');

	
	$.ajax({
		type : "POST",
		url : '/do-post-snag',
		data : 'id=' + operationId+"&title="+title+"&content="+content+"&severity="+severity,
		async : false,
		success : function(response) {
			if(response.status == "FAIL"){
				toastr.error("Couldn't submit snag","Error !");
				for (i = 0; i < response.result.length; i++) {
					if (response.result[i].code == "snag.title.empty")
						titleError.show('slow');
					if (response.result[i].code == "snag.content.empty")
						contentError.show('slow');	
				}
			}else{
				$('#snag-modal').modal("hide");
				toastr.success("Snag submitted successfully","Well done !");
				$('#snag_content').val("");
				$('#snag_title').val("");
				populateOperationSnags(operationId);
			}
		},
		error : function(e) {
			alert('Error: snag operation ' + e);
		}
	});

}

function populateProjectFeed(){
	var projectId = $('#selected_project_id').val();
	var feedWrapper = $(".m-timeline-1__items");
	feedWrapper.html("");
	
	$.ajax({
		type : "GET",
		url : '/get-project-feed',
		data : 'id=' + projectId,
		async : false,
		success : function(response) {
			var i = 0;
			 var operations = [];
			$.each(response, function(date, val) {
				
				var dateWrapper = "";
				var alignContent = "";
				var contentWrapper = "";
				var firstItem = "";
				var formattedDate = moment(date).format("DD MMMM YYYY");
				if(i == 0){
					firstItem = "m-timeline-1__item--first";
				}
				if(i % 2 == 0){
					alignContent = "right";
				}else{
					alignContent = "left";
				}
	

				
				
				dateWrapper = '<div class="m-timeline-1__item m-timeline-1__item--'+alignContent+' '+firstItem+'">'
					+'<div style="background:#36a3f7; " class="m-timeline-1__item-circle">'
					+'	<i class="fa fa-circle m--font-light"></i>'
					+'</div>'
					+'<div class="m-timeline-1__item-arrow"></div>'
					+'<span style="z-index:1;margin-top:2%;color:#fff;" class="m-timeline-1__item-time m-badge m-badge--info m-badge--wide">'+formattedDate+'</span>'
					+'<div id="content_'+date+'" class="m-timeline-1__item-content">'
					
					+'</div>'
					+'</div>';
				feedWrapper.append(dateWrapper);
				 
				
					  
					  for (var j = 0; j < val.length; j++) {
						  var content = "";
						  content = getDetailedActivityUI(val[j],val[j].type,val[j].activityType)
						 
						  
						  
						  contentWrapper += content;
						  if(val[j].type == "operation"){
							  operations.push(val[j]);
						  }
						  
						  
					  }
					  $("#content_"+date).append(contentWrapper);
					  
					 
					  
					  i++;
					  
					  
					  
					  
				});
			
			 for (var cpt = 0; cpt < operations.length; cpt++){
				  var object = operations[cpt];
				 
					  while(getFeedMapUI(object) == false){
						  getFeedMapUI(object);
					  }
				 
			  }
		},
		error : function(e) {
			alert('Error: project feed ' + e);
		}
	});
}

function getParentSpan(parent){
	if(parent != "none")
		return ' in <span style="color:#f4516c;">'+parent.name+'</span>';
	return null;
}
function getDetailedActivityUI(object,objectType,activityType){
	var content = ""; 
	if(objectType == "service"){
		if(activityType=="closed")
		
			content = '<div class="m-list-badge m--margin-top-15">'
			+'<div class="m-list-badge__label m--font-metal">'+object.closedDate+'</div>'
				+'<div class="m-list-badge__items">'
				+'<span style="background:#ddffcc;" class="m-list-badge__item">service <span style="color:#34bfa3;">'+object.name+'</span> is closed</span> '
						+'	</div>'
			+'</div>';
		
		
		if(activityType=="creation"){
			var parentSpan = "";
			if(getParentSpan(object.parent) != null){
				parentSpan = getParentSpan(object.parent);
			}
			
			
			content = '<div class="m-list-badge m--margin-top-15">'
				+'<div class="m-list-badge__label m--font-metal">'+object.creationTime+'</div>'
					+'<div class="m-list-badge__items">'
					+'<span style="background:#e6f2ff;" class="m-list-badge__item">PM created service <span style="color:#34bfa3;">'+object.name+'</span>'
					+parentSpan+'</span> '
							+'	</div>'
				+'</div>';
		}
	}
	
	if(objectType == "operation"){
		
		content = '<div style="width:100%;" class="m-list-badge m--margin-top-15">'
			
			+'<table style="width:100%;border-collapse: separate;border-spacing: 0 5px;" >'
			+'<tr>'
			+'<td>'
			
			+'<div class="m-list-badge__label m--font-metal">'+object.creationTime+'</div>'
			
			+'</td>'
			+'<td>'
			
			+'<div style="position:relative;width:100%;" class="m-list-badge__items">'
			+'<span style="background:#f2e6ff;" class="m-list-badge__item">PM created operation <span style="color:#f4516c;">'+object.name+'</span>'
			+' in site <span style="color:#36a3f7;">'+object.site.name+'</span>'
			+'</span> '
			+'	</div>'
			
			+'</td>'
			+'</tr>'
			+'<tr>'
			+'<td>'
			+'</td>'
			+'<td>'
			
+'<div style="position:relative;float:right;width:100%;height:120px;" >'
			
			
			+'<div class="sites_map boxshadow" style="width:100% !important;height:120px  !important;border:2px #000; " id="'+object.id+'"></div>'
			
			// +'</div>'
			
			+'</div>'
			
			+'</td>'
			+'</tr>'
			+'</table>'
			
			
			
			
			
			
			
		
			
			+'</div>';
		
		
		
	}
	return content;
}

function getFeedMapUI(object){
	if($('#'+object.id).length){
		
	var map_div = "#"+object.id;
	$('#'+object.id).hide();
	var map = new GMaps({
        div: map_div,
        lat: 34.7615155,
        lng: 10.6630578,
        scrollwheel : false,
        disableDefaultUI: true,
        fullscreenControl: true,
        zoomControl: true,
        
    });
	
	map.addMarker({
        lat: object.site.latitude,
        lng: object.site.longitude,
        title: object.site.name,
        id: object.site.id,
        icon : "https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/48/Map-Marker-Flag--Right-Pink.png",
        natures: object.site.natures,
       
        click: function(e,id,title) {
       	  

				
        }
    });
	
// map.drawOverlay({
// lat: object.site.latitude,
// lng: object.site.longitude,
// content: ' <div class="overlay">'+object.site.name+'</div>',
// });
	map.setZoom(4);
	map.refresh();
	
	
	$('#'+object.id).show();
	return true;
	}
	
	return false;
}

function refreshProjectFeed(){
	var projectId = $('#selected_project_id').val();
	$.ajax({
		type : "GET",
		url : '/refresh-project-feed',
		data : 'id=' + projectId,
		async : false,
		success : function(response) {
			if(response.status == "REFRESH"){
				$('.loader-wrapper').show();
				setTimeout(function(){
					$(".m-timeline-1__items").html("");
					populateProjectFeed();
			        $('.loader-wrapper').hide();
			       
			    },2100);
				
				
				
				
					
			}else{
				toastr.info("Nothing to add, Feed is up to date");
			}
		},
		error : function(e) {
			alert('Error: refresh project feed ' + e);
		}
	});
	
}

function populateProjectCalendar(){
	
	            var todayDate = moment().startOf('day');
	            var YM = todayDate.format('YYYY-MM');
	            var YESTERDAY = todayDate.clone().subtract(1, 'day').format('YYYY-MM-DD');
	            var TODAY = todayDate.format('YYYY-MM-DD');
	            var TOMORROW = todayDate.clone().add(1, 'day').format('YYYY-MM-DD');
	            $('.loader-wrapper').show();
				
	            // var eventss = getCalendarEvents();
	            
	            
	            $('#project_calendar').fullCalendar( 'removeEvents');
	            
	            
	          // $('#project_calendar').fullCalendar( 'addEventSource',
				// getCalendarEvents() );
	            
	            setTimeout(function(){
					// $(".m-timeline-1__items").html("");
	            	
	            	$('#project_calendar').fullCalendar({
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

		                	if(calEvent.type == "operation"){
		                    // alert('Operation: ' + calEvent.title);
		                	$("#calendar-fragment").hide();
		                	toggleOperationFragment(calEvent.id);
		                	}
		                	else{

			                	$("#calendar-fragment").hide();
			                	toggleServiceFragment(calEvent.id);
		                	}
		                		// alert('Service: ' + calEvent.title);
		                    // change the border color just for fun
		                    // $(this).css('border-color', 'red');

		                  }
		            });
	            	
					getCalendarEvents();
			        $('.loader-wrapper').hide();
			       
			    },500);
	
}

function getCalendarEvents(){
	var projectId = $('#selected_project_id').val();
	var events = [];
	$.ajax({
		type : "GET",
		url : '/get-project-calendar-events',
		data : 'id=' + projectId,
		async : false,
		success : function(response) {
// if(response.status == "REFRESH"){
// $('#loader-wrapper').show();
// setTimeout(function(){
// $(".m-timeline-1__items").html("");
// populateProjectFeed();
// $('#loader-wrapper').hide();
//			       
// },2100);
//				
//				
//				
//				
//					
// }else{
// toastr.info("Nothing to add, Feed is up to date");
// }
			for(var i = 0 ; i < response.length ; i++){
				var event;
				if(response[i].type == "operation")
				var event={
						type: "operation",
						id:	response[i].id ,
						title: response[i].name ,
						start:  response[i].startDate,
						end: response[i].endDate,
						color: "#bd80ff",
						className: "m-fc-event--light m-fc-event--solid-light",
						description: 'Operation "'+response[i].name+'" due date.',
							 };
				else
					var event={
						type: "service",
						id:	response[i].id ,
						title: response[i].name ,
						start:  response[i].startDate,
						end: response[i].endDate,
						color: "#80bdff",
						description: 'Service "'+response[i].name+'" due date.',
						className: "m-fc-event--light m-fc-event--solid-light"
							 };
				events.push(event);
			}
			
		},
		error : function(e) {
			alert('Error: refresh project feed ' + e);
		}
	});
	
	for(var j = 0 ; j< events.length; j++){
		// console.log("events:"+i +" "+ events[j].title+" startDate:
		// "+moment(events[j].startDate)+" endDate:
		// "+moment(events[j].endDate));
		$('#project_calendar').fullCalendar('renderEvent', events[j],true);
	}
	// console.log("events:"+events.length);
	$("#project_calendar").fullCalendar('rerenderEvents');
    
	return events;
	
	
}

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
			$('#project_creation_date').html(response.creationDate);
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
	// var googleMapWidth = $("#m_portlet_tab_1_2").css('width');
	// var googleMapHeight = $("#m_portlet_tab_1_2").css('height');

	$('#btn-enter-full-screen-dashboard-map').click(function() {
		
// if(!(clientId % 2))
// $('#clients_container_odd_row').hide();
// else
// $('#clients_container_even_row').hide();
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
	    // map.setCenter(newyork);

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
	    // map.setCenter(newyork);

	    // Gui
	    $('#btn-enter-full-screen-dashboard-map').toggle();
	    $('#btn-exit-full-screen-dashboard-map').toggle();
	    return false;
	});
}

function populateDashboardMap() {
	var projectId = $('#selected_project_id').val();
	var map_div = "#dashboard_map";
	var open = 0;
	var closed = 0;
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
						
						if(response[i].status == "open")
							open++;
						else if(response[i].status == "closed")
							closed++;
						
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
							 // console.log("id: "+$(this).attr('id'));
							 
							 toggleOperationFragment($(this).attr('id'));
							if($('#general-header').is(':hidden'))
								$('#general-header').show();
							 
														
						 }
						});

					}
					$('#open_operations').html(open);
					$('#closed_operations').html(closed);
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
	$("#m_dynamic_content_project").children().hide();
	populateServicesTabOperationFragment(operationId);
	populateOperationDetails(operationId);
	populateOperationComments(operationId);
	populateOperationSnags(operationId);
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
			if(response.length != 0)
			for(var i=0;i<response.length;i++){
				html_text += getCommentUI(response[i]);
			}
			else
				html_text = '<div class="m-widget24__item">'
				+'<div>'
				+'<div style="display: block; vertical-align: middle;">'
				+'<table width="100%" align="center" border="0"'
				+'	cellpadding="0" cellspacing="0">'
				+'	<tbody>'
				+'		<tr>'
				+'			<td align="center"><div'
				+'					style="border-color:#fff !important;" class="emptydashboardbox omnia fonticon40">'
				+'						<div class="m-demo-icon__preview">'
				+'							<i style="color:#fff;font-size: 40px;" class="flaticon-chat-1"></i>'
				+'					</div>'
				+'				</div></td>'
				+'		</tr>'
				+'		<tr>'
				+'			<td align="center" height="110px">'
				+'				<div>'
				+'					<span class="col777 pt12 lh25">This operation'
				+'						contains no comments yet. You can post a comment down below'
				+'						</span>'
				+'				</div>'
				+'			</td>'
				+'		</tr>'
				+'	</tbody>'
				+'</table>'
				+'</div>'
				+'</div>'
				+'</div>';
			
			$('#operation_comments_wrapper').html(html_text);
		},
		error : function(e) {
			alert('Error: operation comments ' + e);
		}
	});
}

function populateTaskComments(taskId){
	$('#task_comments_wrapper').html("");
	$.ajax({
		type : "GET",
		url : '/get-task-comments',
		data : 'id=' + taskId,
		async : false,
		success : function(response) {
			var html_text = "";
			for(var i=0;i<response.length;i++){
				html_text += getCommentUI(response[i]);
			}
			
			$('#task_comments_wrapper').html(html_text);
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
+'</div>'
+'<hr style="background: #fff;" />';
}

function doPostTaskComment(){
	var comment = $('#task_comment_content').val();
	var taskId = $('#selected_task_id').val();
	$.ajax({
		type : "POST",
		url : '/do-post-task-comment',
		data : 'id=' + taskId+"&content="+comment,
		async : false,
		success : function(response) {
			if(response.status == "FAIL")
				toastr.error("Couldn't post comment","Error");
			else if(response.status == "NOCONTENT")
				toastr.error("A comment cannot be empty","Hey !");
			else{
				$('#task_comment_content').val("");
				populateTaskComments(taskId);
			}
		},
		error : function(e) {
			alert('Error: comment task ' + e);
		}
	});

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
	var projectId = $('#selected_project_id').val();
	
	$('#select_project_new_operation').val(projectId).change();

	$('#select_project_new_operation').selectpicker('refresh');

	
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
							html_text += '<div style="cursor:pointer;" class="m-widget4__item" onclick="toggleServiceFragment('
								+ response[i].id
								+ ')">'
									+ '<div class="m-widget4__ext">'
									+ '<span class="m-widget4__icon m--font-brand"> <i'
									+ ' class="flaticon-more"></i>'
									+ '</span>'
									+ '</div>'
									+ '<div class="m-widget4__info">'
									//+ '<a href="#" onclick="toggleServiceFragment('
									//+ response[i].id
									//+ ')">'
									+ '<span class="m-widget4__text">'
									+ response[i].name
									+ ' <span class="m-widget4__number m--font-info"> ('
									+ response[i].category
									+ ') </span>'
									+ '</span>'
									+ '<span style="font-size:11px;">created on: <span style="color: #36a3f7;">'
									+ response[i].formattedCreationDate
									+ '</span></span>'
									//+ '</a>'
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
	$('.attachments_block').hide();
	// console.log("session user id: "+$('#session_user_id').val());
	$.ajax({
		type : "GET",
		url : '/get-all-users-in-task-details-json',
		async : false,
		data : "id=" + taskId,
		success : function(response) {
			var html_text = '';
			for (i = 0; i < response.length; i++) {
				// console.log("user "+response[i].id);
				// console.log("task "+taskId);
				if (response[i].selected == true)
					html_text += '<option selected value="' + response[i].id
							+ '">' + response[i].firstName + ' '
							+ response[i].lastName + '</option>';
				else
					html_text += '<option value="' + response[i].id + '">'
							+ response[i].firstName + ' '
							+ response[i].lastName + '</option>';
				
				if(response[i].id == $('#session_user_id').val() && response[i].selected == true
						|| $('#session_user_id').val() == $('#project_manager_id').val()){
					// show attachments actions block if user appears in owners
					// list or user is PM
					$('.attachments_block').show();
					$('.delete_attachment').show();
					
					// show actions block if user appears in owners list or user
					// is PM
					$('#task_actions').show();
				}

			}
			$('#task_owner_select').html(html_text);
			$('#task_owner_select').selectpicker('refresh');
		},
		error : function(e) {
			alert('Error: can"t get users ' + e);
		}
	});

}
function populateTaskFragmentDetails(taskId) {
	
	
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
				+'<div class="col-md-8"><i class="fa fa-file-text"></i><a target="self" href="/attachment?id='+response.files[i].id+'"> '
				+response.files[i].name+'</a>'
				+'<span><p><small>size: '+parseInt(response.files[i].size/1024) +' KB</small>'
				+'<small> Added on: '+response.files[i].creationDate+'</small></p></span>'
				+'</div>'
				+'<div style="display:none;" class="delete_attachment" class="col-md-4"><a href="#" onclick="handleDeleteAttachment('+response.files[i].id+')" class="btn btn-danger m-btn m-btn--icon m-btn--icon-only m-btn--pill">'
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
	
	populateTaskParents(taskId);
	populateTaskOwnerSelect(taskId);
	
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
										if($('#issue-fragment').is(':visible')){
											populateIssueFragmentDetails($('#selected_issue_id').val());
											
										}else if($('#task-fragment').is(':visible')){
											populateTaskFragmentDetails($('#selected_task_id').val());
												
										}
										
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

function externalTaskLoad() {
	$("#m_dynamic_content_project").children().hide();
	var WindowLocation = window.location.hash;
    
    	var taskId = WindowLocation.substr(WindowLocation.lastIndexOf("=")+1);
    	toggleTaskFragment(taskId);
    
}

function externalIssueLoad() {
	$("#m_dynamic_content_project").children().hide();
	var WindowLocation = window.location.hash;
    
    	var issueId = WindowLocation.substr(WindowLocation.lastIndexOf("=")+1);
    	toggleIssueFragment(issueId);
    
}

function externalOperationLoad() {
	$("#m_dynamic_content_project").children().hide();
	var WindowLocation = window.location.hash;
    
    	var operationId = WindowLocation.substr(WindowLocation.lastIndexOf("=")+1);
    	toggleOperationFragment(operationId);
    
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
	populateTaskComments(taskId);
	
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
	// projectDashboardTaskPieChart();
	populateDashboardMap();
	$("#dashboard-fragment").show();
	$('#project_subheader').show();

	$("#project_dashboard_toggle").on("click", function(event) {
		$('#project_subheader').show();
		populateDashboardMap();
		$("#m_dynamic_content_project").children().hide();
		// projectDashboardTaskPieChart();
		$("#dashboard-fragment").show();
	});

	$("#project_feed_toggle").on("click", function() {
		populateProjectFeed();
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
		DatatableIssuesJsonRemote.init();
		$("#issues-fragment").show();
	});

	$("#project_calendar_toggle").on("click", function() {
		$('#project_subheader').show();
		$("#m_dynamic_content_project").children().hide();
		
		populateProjectCalendar();
		
		$("#calendar-fragment").show();
	});

}

function taskDropZone(){
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
}

function issueDropZone(){
	// bootstrap dropzone
	// Get the template HTML and remove it from the doumenthe template HTML and
	// remove it from the doument
	var previewNode = document.querySelector("#issue_template");
	previewNode.id = "";
	var previewTemplate = previewNode.parentNode.innerHTML;
	previewNode.parentNode.removeChild(previewNode);

	var myDropzone = new Dropzone("#issue-fragment", { // Make the whole body a
														// dropzone
	  url: "/upload-issue-attachment", // Set the url
	  thumbnailWidth: 80,
	  thumbnailHeight: 80,
	  parallelUploads: 20,
	  previewTemplate: previewTemplate,
	  autoQueue: false, // Make sure the files aren't queued until manually
						// added
	  previewsContainer: "#issue_previews", // Define the container to display the
										// previews
	  clickable: ".fileinput-button", // Define the element that should be
										// used as click trigger to select
										// files.
	  acceptedFiles: "image/*,application/pdf, .doc, .docx, .xls",
	});

	myDropzone.on("addedfile", function(file) {
		if($('#issue_previews').is(':hidden'))
		$('#issue_previews').show();
	  // Hookup the start button
	  file.previewElement.querySelector(".start").onclick = function() { myDropzone.enqueueFile(file); };
	});

	// Update the total progress bar
	myDropzone.on("totaluploadprogress", function(progress) {
	  document.querySelector("#issue_total-progress .progress-bar").style.width = progress + "%";
	});

	myDropzone.on("sending", function(file, xhr, formData) {
	  // Show the total progress bar when upload starts
		 formData.append('id',$('#selected_issue_id').val());
	  document.querySelector("#issue_total-progress").style.opacity = "1";
	  // And disable the start button
	  file.previewElement.querySelector(".start").setAttribute("disabled", "disabled");
	});

	// Hide the total progress bar when nothing's uploading anymore
	myDropzone.on("queuecomplete", function(progress) {
	  document.querySelector("#issue_total-progress").style.opacity = "0";
	  toastr.success("Attachment uploaded successfully","Well Done")
	  $('#issue_previews').hide('slow');
	  populateIssueFragmentDetails($('#selected_issue_id').val());
	});

	// Setup the buttons for all transfers
	// The "add files" button doesn't need to be setup because the config
	// `clickable` has already been specified.
	document.querySelector("#issue_actions .start").onclick = function() {
	  myDropzone.enqueueFiles(myDropzone.getFilesWithStatus(Dropzone.ADDED));
	};
	document.querySelector("#issue_actions .cancel").onclick = function() {
	  myDropzone.removeAllFiles(true);
	};
	// end bootstrap dropzone
}

$(document).ready(function() {
	
	
	populateProjectDetails();
	projectDynamicContent();
	taskDropZone();
	issueDropZone();

   
	
	
	
	
});
