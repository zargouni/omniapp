function handleRemoveProject(){
	var projectId = $('#selected_project_id').val();
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
								url : '/delete-project',
								data : "id=" + projectId,
								async : true,
								success : function(response) {
									if (response.status == "SUCCESS") {
										swal('Deleted!',
												'Project has been deleted.',
												'success');
										setTimeout(function(){
											window.location.replace("/all-projects");
										},2000);
										//$('#operations_datatable').mDatatable('reload');
										//goToProjectOperations();
										
									} else {
										swal('Fail!', 'Project not deleted.',
												'error');
									}

								},
								error : function(e) {
									toastr.error("Couldn't delete Project",
											"Server Error");
									result = false;
								}
							});

				}

			}

	);
}

function handleRemoveOperation(){
	var operationId = $('#operation_fragment_selected_operation_id').val();
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
								url : '/delete-operation',
								data : "id=" + operationId,
								async : true,
								success : function(response) {
									if (response.status == "SUCCESS") {
										swal('Deleted!',
												'Operation has been deleted.',
												'success');
										$('#operations_datatable').mDatatable('reload');
										goToProjectOperations();
										
									} else {
										swal('Fail!', 'Operation not deleted.',
												'error');
									}

								},
								error : function(e) {
									toastr.error("Couldn't delete Operation",
											"Server Error");
									result = false;
								}
							});

				}

			}

	);
}

function handleRemoveService(){
	var serviceId = $('#service_fragment_selected_service_id').val();
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
								url : '/delete-service',
								data : "id=" + serviceId,
								async : true,
								success : function(response) {
									if (response.status == "SUCCESS") {
										swal('Deleted!',
												'Service has been deleted.',
												'success');
										//$('#operations_datatable').mDatatable('reload');
										//goToProjectOperations();
										
									} else {
										swal('Fail!', 'Service not deleted.',
												'error');
									}

								},
								error : function(e) {
									toastr.error("Couldn't delete Service",
											"Server Error");
									result = false;
								}
							});

				}

			}

	);
}

function populateIssueComments(issueId){
	$('#issue_comments_wrapper').html("");
	$.ajax({
		type : "GET",
		url : '/get-issue-comments',
		data : 'id=' + issueId,
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
					+'<div style="display: block; margin-top: 5%;">'
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
					+'					<span class="col777 pt12 lh25">This issue'
					+'						contains no comments yet.'
					+'						</span>'
					+'				</div>'
					+'			</td>'
					+'		</tr>'
					+'	</tbody>'
					+'</table>'
					+'</div>'
					+'</div>'
					+'</div>';
			
			$('#issue_comments_wrapper').html(html_text);
		},
		error : function(e) {
			alert('Error: issue comments ' + e);
		}
	});
}

function doPostIssueComment(){
	var comment = $('#issue_comment_content').val();
	var issueId = $('#selected_issue_id').val();
	$.ajax({
		type : "POST",
		url : '/do-post-issue-comment',
		data : 'id=' + issueId+"&content="+comment,
		async : false,
		success : function(response) {
			if(response.status == "FAIL")
				toastr.error("Couldn't post comment","Error");
			else if(response.status == "NOCONTENT")
				toastr.error("A comment cannot be empty","Hey !");
			else{
				$('#issue_comment_content').val("");
				populateIssueComments(issueId);
			}
		},
		error : function(e) {
			alert('Error: comment issue ' + e);
		}
	});

}

function doUpdateIssue(issueId) {
	var name = $('#issue_name_input').val();
	var status = $('#issue_status_select').val();
	var severity = $('#issue_severity_select').val();
	var description = $('#issue_description').val();
	var endDate = $('#issue_end_date_select').val();

	var nameError = $('#issue_fragment_name_error');
	var descriptionError = $('#issue_fragment_description_error');
	var EndDateError = $('#issue_fragment_endDate_error');

	nameError.hide('fast');
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
							+ endDate + "&description=" + description,
					success : function(response) {
						// we have the response
						if (response.status == "SUCCESS") {
							toastr.success("Issue updated successfully",
									"Well done!");
								doUpdateIssueOwners(issueId);
								populateIssueFragmentDetails(issueId);
								$('#issues_datatable').mDatatable(
										'reload');
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

function deleteIssue(){
	var id = $('#selected_issue_id').val();
	
	swal({
		title : 'Are you sure?',
		text : "You won't be able to revert this!",
		type : 'warning',
		showCancelButton : true,
		confirmButtonText : 'Yes, delete it!'
	}).then(
			function(result) {
				if (result.value) {
					$.ajax({
						type : "POST",
						url : '/delete-issue',
						data : "id=" + id,
						success : function(response) {
							// we have the response
							if (response.status == "SUCCESS") {
								swal('Deleted!',
										'Issue has been deleted.',
										'success');
								goToProjectIssues();
								$('#issues_datatable').mDatatable(
								'reload');
//								if (response.result != 'undefined') {
//									$('#service_tasks_datatable').mDatatable(
//											'reload');
//								}
							} else {
								swal('Fail!', 'Couldn\'t delete issue.',
								'error');
							}
						},
						error : function(e) {
							toastr('Error: can\'t delete issue ', e);
						}
					});
				}
			}
	);
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
	
	
// $('#task_start_date_select').datepicker({
// format : 'dd/mm/yyyy'
// });
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
			

			// $('#task_start_date_select').val(response.startDate);
			$('#issue_end_date_select').val(response.endDate);
// $('#task_estimation_hr_input').val(response.estimationHR);
// $('#task_estimation_days_input').val(response.estimationTime);
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
// $('#issue_operation').val(response.operation_id);
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
	if($('#session_user_id').val() == $('#project_manager_id').val()){
		$('#issue_name_container').show();
		$('#issue_fragment_details').find('select,input,textarea').each(function() {
			$(this).attr("disabled", false);
			if ($(this).hasClass('m-bootstrap-select'))
				$(this).selectpicker('refresh');
			if ($(this).hasClass('issue_date'))
				$(this).datepicker('refresh');
		});
	}else{
		$('#issue_status_select').attr("disabled",false);
		$('#issue_status_select').selectpicker('refresh');
		}
	
	

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
	populateIssueComments(issueId);
	
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
					
					$('#input_new_issue_name').val("");
					$('#input_new_issue_description').val("");
					$('#input_new_issue_due_date').val("");
					$('#input_new_issue_severity').val("");
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
			alert('Error: operation snags ' + e);
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
	+'<a href="/profile?id='+snag.user_id+'" >'
	+			'<img style="box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);width: 44px; height:44px;" class="m-widget3__img"'
	+			'	src="' + snag.user_pic + '" alt="">'
	+' </a>'
	+		'</div>'
	+		'<div class="m-widget3__info">'
	+'<a href="/profile?id='+snag.user_id+'" style=" text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);font-weight: 600;color: white;" >'
	+			'<span style="color: white;"  class="m-widget3__username">' + snag.user + '</span>'
	+' </a>'

	+'<br>'
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
	 $('.loader-wrapper').show();
	 
	 setTimeout(function(){
			
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
						// console.log("taille des valeurs: "+val.length);
							  for (var j = 0; j < val.length; j++) {
								  var content = "";
								  //console.log("objet: "+val[j].name+" Type: "+val[j].type+" activityType: "+val[j].activityType)
								  content = getDetailedActivityUI(val[j],val[j].type,val[j].activityType);
								  contentWrapper += content;
								  if(val[j].type == "operation" && val[j].activityType == "creation"){
									  operations.push(val[j]);
								  }
							  }
							  $("#content_"+date).append(contentWrapper);
							  
							  for (var j = 0; j < val.length; j++) {
								  if(val[j].type == "operation" && val[j].activityType == "update"){
									  var object = val[j];
									  $('#popover-dismiss-'+val[j].op_id+''+val[j].up_id).popover({
										 	html: true, 
										 	animation: true,
										 	trigger: 'hover',
										 	placement: "auto",
										 	content: getUpdateChangesUI(object)
										 	
										});
								  }
								  
								  if(val[j].type == "service" && val[j].activityType == "update"){
									  var object = val[j];
									  $('#popover-dismiss-'+val[j].service_id+''+val[j].up_id).popover({
										 	html: true, 
										 	animation: true,
										 	trigger: 'hover',
										 	placement: "auto",
										 	content: getUpdateChangesUI(object)
										 	
										});
								  }
							  }
							  
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
		 
	        $('.loader-wrapper').hide();
	       
	    },500);
	 
}


function getUpdateChangesUI(object){
	var html_content = "<ul>";
	//console.log("hi forom here!! ")
	for(cmpt = 0; cmpt< object.changes.length ; cmpt++){
		html_content += "<li style='margin: 10px 0;padding: 0;font-weight: 450;color: #5867dd;'>Changed <span style='color: #36a3f7;'>"+object.changes[cmpt].field+"</span> from "
		+"<span style='color: #f4516c;'>"+object.changes[cmpt].old_value+"</span> to <span style='color: #34bfa3;'>"+object.changes[cmpt].new_value+"</div></li>";
	}
	return html_content+'</ul>';
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
				+'<span style="font-size: 24px; color: #c4c5d6;" >‍🚀</span>'
				+'<span style="background: transparent;" class="m-list-badge__item">Service <span style="color:#34bfa3;">'+object.name+'</span> is now closed</span> '
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
					+'<span style="font-size: 24px; color: #c4c5d6;" >‍🔧</span>'
					+'<span style="background:transparent;" class="m-list-badge__item"><a href="/profile?id='+object.user_id+'">'+object.createdBy+'</a> created service <span style="color:#34bfa3;">'+object.name+'</span>'
					+parentSpan+'</span> '
							+'	</div>'
				+'</div>';
		}
		
		if(activityType=="deletion"){
			var parentSpan = "";
			if(getParentSpan(object.parent) != null){
				parentSpan = getParentSpan(object.parent);
			}
			content = '<div class="m-list-badge m--margin-top-15">'
				+'<div class="m-list-badge__label m--font-metal">'+object.deletionTime+'</div>'
					+'<div class="m-list-badge__items">'
					+'<span style="font-size: 24px; color: #c4c5d6;" >‍❌</span>'
					+'<span style="background:transparent;" class="m-list-badge__item"><a href="/profile?id='+object.user_id+'">'+object.deletedBy+'</a> deleted service <span style="color:#34bfa3;">'+object.name+'</span>'
					+parentSpan+'</span> '
							+'	</div>'
				+'</div>';
		}
		
		if(activityType=="update"){
			var detailsPopoverId = 'popover-dismiss-'+object.service_id+''+object.up_id;
			var popoverButton = '<span  style="margin-left: 8px;padding: 0 10px;" id='+detailsPopoverId+' class="m-badge m-badge--success m-badge--rounded">'
					+'Details</span>';
			
			content = '<div class="m-list-badge m--margin-top-15">'
				+'<div class="m-list-badge__label m--font-metal">'+object.creationTime+'</div>'
					+'<div class="m-list-badge__items">'
					+'<span style="font-size: 24px; color: #c4c5d6;" >✔</span>'
					+'<span style="background:transparent;" class="m-list-badge__item"><a href="/profile?id='+object.user_id+'">'+object.updatedBy+'</a> updated service <span style="color:#34bfa3;">'+object.name+'</span> '
					+popoverButton+'</span>'
							+'	</div>'
				+'</div>';
		}
	}
	
	if(objectType == "operation"){
		
		if(activityType=="deletion"){
			content = '<div class="m-list-badge m--margin-top-15">'
				+'<div class="m-list-badge__label m--font-metal">'+object.deletionTime+'</div>'
					+'<div class="m-list-badge__items">'
					+'<span style="font-size: 24px; color: #c4c5d6;" >‍❌</span>'
					+'<span style="background:transparent;" class="m-list-badge__item"><a href="/profile?id='+object.user_id+'">'+object.deletedBy+'</a> deleted operation <span style="color:#34bfa3;">'+object.name+'</span>'
					+'</span> '
							+'	</div>'
				+'</div>';
		}else if(activityType=="update"){
			//var html_content = "";
			var detailsPopoverId = 'popover-dismiss-'+object.op_id+''+object.up_id;
			var popoverButton = '<span  style="margin-left: 8px;padding: 0 10px;" id='+detailsPopoverId+' class="m-badge m-badge--success m-badge--rounded">'
					+'Details</span>';
			
			content = '<div class="m-list-badge m--margin-top-15">'
				+'<div class="m-list-badge__label m--font-metal">'+object.creationTime+'</div>'
					+'<div class="m-list-badge__items">'
					+'<span style="font-size: 24px; color: #c4c5d6;" >✔</span>'
					+'<span style="background:transparent;" class="m-list-badge__item"><a href="/profile?id='+object.user_id+'">'+object.updatedBy+'</a> updated operation <span style="color:#f4516c;">'+object.name+'</span> '
					+popoverButton+'</span>'
							+'	</div>'
				+'</div>';
			//content += update;
		}else{
		
			content = '<div style="width:100%;" class="m-list-badge m--margin-top-15">'
				
				+'<table style="width:100%;border-collapse: separate;border-spacing: 0 5px;" >'
				+'<tr>'
				+'<td>'
				
				+'<div class="m-list-badge__label m--font-metal">'+object.creationTime+'</div>'
				
				+'</td>'
				+'<td>'
				
				+'<div style="position:relative;width:100%;" class="m-list-badge__items">'
				+'<span style="font-size: 24px; color: #c4c5d6;" >🚩</span>'
				+'<span style="background:transparent;" class="m-list-badge__item"><a href="/profile?id='+object.user_id+'">'+object.createdBy+'</a> created operation <span style="color:#f4516c;">'+object.name+'</span>'
				+' in site <span style="color:#36a3f7;">'+object.site.name+'</span>'
				+'</span> '
				+'	</div>'
				
				+'</td>'
				+'</tr>'
				+'<tr>'
				+'<td>'
				+'</td>'
				+'<td>'
				
				+'<div class="boxshadow" style="position:relative;float:right;width:100%;height:120px; border:5px #000; " >'
				
				
				+'<div class="sites_map boxshadow" style="width:100% !important;height:120px  !important;" id="'+object.id+'"></div>'
				
				// +'</div>'
				
				+'</div>'
				
				+'</td>'
				+'</tr>'
				+'</table>'
				
				
				+'</div>';
			
		}
		
				
		
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
        zoomControl: false,
        
    });
	
	map.addMarker({
        lat: object.site.latitude,
        lng: object.site.longitude,
        title: object.site.name,
        id: object.site.id,
        icon : "https://png.icons8.com/cotton/30/000000/place-marker.png",
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
			alert('Error: Calendar events ' + e);
		}
	});
	
	for(var j = 0 ; j< events.length; j++){
		$('#project_calendar').fullCalendar('renderEvent', events[j],true);
	}
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
			$('#unplanified_tasks').html(response.unplanifiedTasksCount);
			$('#overdue_tasks').html(response.overdueTasksCount);
			$('#tasks_count').html(response.tasksCount);
			$('#issues_count').html(response.issuesCount);	
			$('#unassigned_issues').html(response.unassignedIssuesCount);
			$('#overdue_issues').html(response.overdueIssuesCount);
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
	$('#btn-enter-full-screen-dashboard-map').click(function() {
				
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
	var in_progress = 0;
	$('#operation_status_filter').on('change',function(){
		filterMarkersByOperationStatus(map,$(this).val())
	});

	var map = new GMaps({
		div : map_div,
		lat : 34.7615155,
		lng : 10.6630578,
		disableDefaultUI: false,
		
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
							icon : '/assets/omniapp/img/green_marker.png'
								},
						closed : {
							icon : '/assets/omniapp/img/red_marker.png'
						},
						in_progress : {
							icon : '/assets/omniapp/img/yellow_marker.png'
						}
					};
					for (i = 0; i < response.length; i++) {
						
						if(response[i].status == "open")
							open++;
						else if(response[i].status == "closed")
							closed++;
						else if (response[i].status == "in_progress")
							in_progress++;
						
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
					$('#in_progress_operations').html(in_progress);
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
		draggable: false,
		disableDoubleClickZoom: true,
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
			if(response.length != 0)
				for(var i=0;i<response.length;i++){
					html_text += getCommentUI(response[i]);
				}
				else
					html_text = '<div class="m-widget24__item">'
						+'<div>'
						+'<div style="display: block; margin-top: 5%;">'
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
						+'					<span class="col777 pt12 lh25">This task'
						+'						contains no comments yet.'
						+'						</span>'
						+'				</div>'
						+'			</td>'
						+'		</tr>'
						+'	</tbody>'
						+'</table>'
						+'</div>'
						+'</div>'
						+'</div>';
			
			$('#task_comments_wrapper').html(html_text);
		},
		error : function(e) {
			alert('Error: operation site ' + e);
		}
	});
}

function deleteComment(commentId){
	$.ajax({
		type : "POST",
		url : '/delete-comment',
		data : 'id=' + commentId,
		async : false,
		success : function(response) {
			if(response.status == "SUCCESS"){
				toastr.info("Comment deleted successfully");
				if($('#issue-fragment').is(':visible')){
					populateIssueComments($('#selected_issue_id').val()); 
				}else if($('#task-fragment').is(':visible')){
					populateTaskComments($('#selected_task_id').val());	
				}else if($('#operation-fragment').is(':visible')){
					populateOperationComments($('#operation_fragment_selected_operation_id').val());	
				}
			}
		},
		error : function(e) {
			alert('Error: delete comment ' + e);
		}
	});
}

function getCommentUI(comment){
	var deleteComment = "";
	if($('#session_user_id').val() == comment.user_id){
		deleteComment = '<a style="opacity: 0.9;float: right;" title="Delete Comment" onclick="deleteComment('+comment.id+')" class="btn btn-danger m-btn m-btn--icon btn-sm m-btn--icon-only  m-btn--pill m-btn--air">'
						+			'<i class="la la-close"></i>'
						+		'</a>';
	

	}
	return '<div class="m-widget3">'
	+'<div class="m-widget3__item">'
	+	'<div class="m-widget3__header">'
	+		'<div class="m-widget3__user-img">'
	+			'<img style="box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);width: 44px; height:44px;" class="m-widget3__img"'
	+			'	src="'+ comment.user_pic+'" alt="">'
	+		'</div>'
	+		'<div class="m-widget3__info">'
	+		deleteComment
	+			'<span class="m-widget3__username"><a style=" text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);font-weight: 600;color: white;" href="/profile?id='+comment.user_id+'">' + comment.user + '</a></span><br>'
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
+'<hr style="opacity: 0.4;background: #fff;" />';
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
									// + '<a href="#"
									// onclick="toggleServiceFragment('
									// + response[i].id
									// + ')">'
									+ '<span class="m-widget4__text">'
									+ response[i].name
									+ ' <span class="m-widget4__number m--font-info"> ('
									+ response[i].category
									+ ') </span>'
									+ '</span>'
									+ '<span style="font-size:11px;">created on: <span style="color: #36a3f7;">'
									+ response[i].formattedCreationDate
									+ '</span></span>'
									// + '</a>'
									+ '</div>'
									+ '<div class="m-widget4__ext">'
									+ '<div style="width:200px !important;" class="progress m-progress--sm">'
									+ '<div class="progress-bar m--bg-success" role="progressbar" style="width: '
									+ response[i].percentage
									+ '%;" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"></div>'
									+ '</div>'
									+ '<span style="font-size:11px;" class="m-widget4__number m--font-info">'
									+ response[i].taskCount
									+ ' Tasks</span>'
									+ '<span style="font-size:11px;float:right;" class="m-widget4__number m--font-info">'
									+ response[i].percentage
									+ '%</span>'
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

			if(response.startDate != "01/02/1970")
				$('#task_start_date_select').val(response.startDate);
			else
				$('#task_start_date_select').val("");
			if(response.endDate != "01/02/1970")
				$('#task_end_date_select').val(response.endDate);
			else
				$('#task_end_date_select').val("");
			
			$('#task_estimation_hr_input').val(response.estimationHR);
			$('#task_estimation_days_input').val(response.estimationTime);
			$('#task_completion_percentage').val(response.completionPercentage);
			
			
			toggleReadOnlyModeTask();
			if(response.files.length == 0)
				html_text += '<div><div class="row"><div style="margin: auto auto;" class="col-md-8">No attachments</div></div></div>';
			else
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
	
	$('#task_status_select').selectpicker('refresh');
	$('#task_priority_select').selectpicker('refresh');
	
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
	
	$.ajax({
		type : "GET",
		url : '/get-task-parents',
		data : 'id=' + taskId,
		async : false,
		success : function(response) {
			$('#task_fragment_service').html('<a style="color: white;" href="#" onclick="toggleServiceFragment('+response.service_id+')">'+response.service+'</a><input type="hidden" value="'+response.service_id+'" id="task_parent_service_id" />');
			if(response.operation != 'none')
				$('#task_fragment_operation').html('<a style="color: white;" href="#" onclick="toggleOperationFragment('+response.operation_id+')">'+response.operation+'</a>');
			else
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
	if($('#session_user_id').val() == $('#project_manager_id').val()){
		$('#task_name_container').show();
		$('#task_fragment_details').find('select,input').each(function() {
			$(this).attr("disabled", false);
			if ($(this).hasClass('m-bootstrap-select'))
				$(this).selectpicker('refresh');
			if ($(this).hasClass('task_date'))
				$(this).datepicker('refresh');
		});	
	}else{
		$('#task_status_select').attr("disabled",false);
		$('#task_completion_percentage').attr("disabled",false);
		$('#task_status_select').selectpicker('refresh');
		$('#task_completion_percentage').selectpicker('refresh');
	}
	

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

function deleteTask(){
	var id = $('#selected_task_id').val();
	
	swal({
		title : 'Are you sure?',
		text : "You won't be able to revert this!",
		type : 'warning',
		showCancelButton : true,
		confirmButtonText : 'Yes, delete it!'
	}).then(
			function(result) {
				if (result.value) {
					$.ajax({
						type : "POST",
						url : '/delete-task',
						data : "id=" + id,
						success : function(response) {
							// we have the response
							if (response.status == "SUCCESS") {
								swal('Deleted!',
										'Task has been deleted.',
										'success');
								window.location.href = '#tasks';
								//goToProjectTasks();
							} else {
								swal('Fail!', 'Couldn\'t delete task.',
								'error');
							}
						},
						error : function(e) {
							toastr('Error: can\'t delete Task ', e);
						}
					});
				}
			}
	);
}

function doUpdateTask(id) {
	var name = $('#task_name_input').val();
	var status = $('#task_status_select').val();
	var priority = $('#task_priority_select').val();
	var startDate = $('#task_start_date_select').val();
	var endDate = $('#task_end_date_select').val();
	var estimationHR = $('#task_estimation_hr_input').val();
	var estimationTime = $('#task_estimation_days_input').val();
	var service = $('#task_parent_service_id').val();
	var completionPercentage = $('#task_completion_percentage').val();

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
							+ service+"&completionPercentage="+completionPercentage,
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

function populateIssuesFragmentWidget() {
	var projectId = $('#selected_project_id').val();
	$.ajax({
		type : "GET",
		url : '/get-project-issues-stats',
		data : 'id=' + projectId,
		async : false,
		success : function(response) {
			$('#issues_fragment_open_issues_percentage').html(response.open);
			$('#issues_fragment_in_progress_issues_percentage').html(response.in_progress);
			$('#issues_fragment_to_be_tested_issues_percentage').html(response.to_be_tested);
			$('#issues_fragment_closed_issues_percentage').html(response.closed);
		},
		error : function(e) {
			alert('Error: Project issues stats widget ' + e);
		}
	});
}

function setSelectedService(serviceId) {
	$('#service_fragment_selected_service_id').attr('value', serviceId);
}

function projectDynamicContent() {
	goToDashboard();

	$("#project_dashboard_toggle").on("click", function(event) {
		goToDashboard();
	});

	$("#project_feed_toggle").on("click", function() {
		goToProjectFeed();
	});

	$("#project_tasks_toggle").on("click", function() {
		goToProjectTasks();
	});

	$("#project_operations_toggle").on("click", function() {
		goToProjectOperations();
	});
	
	$("#project_pos_toggle").on("click", function() {
		goToProjectPos();
	});

	$("#project_issues_toggle").on("click", function() {
		goToProjectIssues();
	});

	$("#project_calendar_toggle").on("click", function() {
		goToProjectCalendar();
	});

}

function goToDashboard(){
	$('.loader-wrapper').show();
	$('#project_subheader').show();
	 populateGanttChart();
	populateDashboardMap();
	$("#m_dynamic_content_project").children().hide();
	// projectDashboardTaskPieChart();
	$("#dashboard-fragment").show();
	setTimeout(function(){
		$('.loader-wrapper').hide()
	},500);
}

function goToProjectFeed(){
	populateProjectFeed();
	$('#project_subheader').show();
	$("#m_dynamic_content_project").children().hide();
	$("#feed-fragment").show();
}

function goToProjectTasks(){
	$('#project_subheader').show();
	$("#m_dynamic_content_project").children().hide();
	populateTasksFragmentWidget();
	$("#tasks-fragment").show();
}

function goToProjectIssues(){
	$('#project_subheader').show();
	$("#m_dynamic_content_project").children().hide();
	populateIssuesFragmentWidget();
	DatatableIssuesJsonRemote.init();
	$("#issues-fragment").show();
}

function goToProjectPos(){
	$('#project_subheader').show();
	$("#m_dynamic_content_project").children().hide();
	//populateIssuesFragmentWidget();
	DatatablePosJsonRemote.init();
	$("#pos-fragment").show();
}

function goToProjectOperations(){
	$('#project_subheader').show();
	$("#m_dynamic_content_project").children().hide();
	DatatableOperationsJsonRemote.init();
	$("#operations-fragment").show();
}

function goToProjectCalendar(){
	$('#project_subheader').show();
	$("#m_dynamic_content_project").children().hide();
	populateProjectCalendar();
	$("#calendar-fragment").show();
}

function taskDropZone(){
	// bootstrap dropzone
	// Get the template HTML and remove it from the doumenthe template HTML and
	// remove it from the doument
	var previewNode = document.querySelector("#template");
	previewNode.id = "";
	var previewTemplate = previewNode.parentNode.innerHTML;
	previewNode.parentNode.removeChild(previewNode);

	var myDropzone = new Dropzone(".attachments_block", { // Make the whole body a
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
	  previewsContainer: "#issue_previews", // Define the container to display
											// the
										// previews
	  clickable: ".issue_fileinput-button", // Define the element that should be
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

function populateGanttChart(){
	 var projectId = $('#selected_project_id').val();
	var g = new JSGantt.GanttChart(document.getElementById('GanttChartDIV'), 'day');

	$.ajax({
		type : "GET",
		url : '/get-project-gantt',
		data : 'id=' + projectId,
		async : false,
		success : function(response) {
			if(response.length > 0){
				for(var i = 0 ;i < response.length; i++){
					var operation = response[i];
						if(operation.tasks.length > 0){
							var opId = i+1;
							g.AddTaskItem(
									new JSGantt.TaskItem(opId, operation.name, operation.startDate, operation.endDate,'ggroupblack','', 0,  '', operation.percentage,  1,0,1,'','','this is an Operation',g));
							for(var j = 0; j < operation.tasks.length; j++){
								var tId = ""+opId+(j+1);
								var task = operation.tasks[j];
								if(task.startDate != "1970-02-01 00:00" && task.endDate != "1970-02-01 00:00" ){
									if(task.status == "1")
										g.AddTaskItem(
												new JSGantt.TaskItem(tId, task.name, task.startDate, task.endDate,'gtaskred','', 0,  task.users, task.completionPercentage,  0, opId ,0,'','','this is a Task',g));
									else
										g.AddTaskItem(
												new JSGantt.TaskItem(tId, task.name, task.startDate, task.endDate,'gtaskgreen','', 0,  task.users, task.completionPercentage,  0, opId ,1,'','','this is a Task',g));
								
								}
							}	
						}				
				}
				g.Draw();
			}else{
				$('#gantt_empty').show();
			}
			
		},
		error : function(e) {
			alert('Error: Gantt ' + e);
		}
	});
	

}

function populateSelectOwnedProjectsEditOperationSidebar() {
	var projectId = $('#selected_project_id').val();
	$.ajax({
		type : "GET",
		url : '/set-select-owned-projects',
		async: false,
		success : function(response) {

			var html_select_options = "";

			var html_text = "<option value=' '>Nothing Selected</option>";
			if (response.length == 1) {
				if(response[0].id == projectId )
					html_text += "<option value='" + response[0].id + "' selected>"
					+ response[0].name + "</option>";
				else
				html_text += "<option value='" + response[0].id + "'>"
						+ response[0].name + "</option>";
				$("#select_project_edit_operation").html(html_text);
			} else {
				for (i = 0; i < response.length; i++) {
					if(response[i].id == projectId )
						html_text += "<option value='" + response[i].id + "' selected>"
						+ response[i].name + "</option>";
					else
					html_text += "<option value='" + response[i].id + "'>"
							+ response[i].name + "</option>";
				}
				$("#select_project_edit_operation").html(html_text);

			}
			$("#select_project_edit_operation").selectpicker('refresh');

			$("#select_project_edit_operation").on('change', function() {
				var projectId = $("#select_project_edit_operation").val();
				
				if(projectId == ' '){ 
					$('#sites_map_container_edit_operation').attr("style","display:none;width:100%; height:50px;position: relative;");
					$('#sites_map_canvas_sidebar_edit_operation').attr("style","position: absolute; display:none;top: 20%; right: 0; bottom: 0; left: 0;");
				}else{
					initializeSitesGmapEditOperation(projectId);
				}
			});


		},
		error : function(e) {
			$("#select_project_edit_operation").html(
					"<option value=''>Nothing selected</option>");

		}
	});

}

function editOperationSelectProject(){
	var projectId = $('#selected_project_id').val();
	
	$('#select_project_edit_operation').val(projectId).change();

	$('#select_project_edit_operation').selectpicker('refresh');
}

function populateEditOperationResponsibleTypeAhead() {
    var users = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.whitespace,
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        prefetch: './get-all-users-json'
    });

    $('#edit_operation_responsible').typeahead(null, {
        name: 'users',
        source: users
    });
}

function populateEditOperationSidebar(){
	populateSelectOwnedProjectsEditOperationSidebar();
	editOperationSelectProject();
	populateEditOperationResponsibleTypeAhead();
	var operationId = $('#operation_fragment_selected_operation_id').val();
	$.ajax({
		type : "GET",
		url : '/get-operation-details',
		data : 'id=' + operationId,
		async : false,
		success : function(response) {
			$("#input_edit_operation_name").val(response.name);
			$("#select_site_edit_operation").val(response.site.id).change();
			$('#select_site_edit_operation').selectpicker('refresh');
			$('#input_edit_operation_start_date').val(response.startDate_for_edit);
			$('#input_edit_operation_end_date').val(response.endDate_for_edit);
			$("#edit_operation_responsible").val(response.responsible_username);
		},
		error : function(e) {
			alert('Error: Gantt ' + e);
		}
	});
}

function doUpdateOperationAjaxPost() {

	var operationId = $('#operation_fragment_selected_operation_id').val();
	
	var nameError = $('#edit_operation_name_error');
	//var projectError = $('#operation_project_error');
	var startDateError = $('#edit_operation_startDate_error');
	var endDateError = $('#edit_operation_endDate_error');
	var name = $('#input_edit_operation_name').val();
	var project = $('#select_project_edit_operation').val();
	var startDate = $('#input_edit_operation_start_date').val();
	var endDate = $('#input_edit_operation_end_date').val();
	var site = $('#select_site_edit_operation').val();
	var responsible = $('#edit_operation_responsible').val();

//	nameError.hide('fast');
//	projectError.hide('fast');
//	startDateError.hide('fast');
//	endDateError.hide('fast');

	
	$.ajax({
		type : "POST",
		url : '/update-operation',
		data : "id="+operationId +"&name=" + name + "&site="+site+"&startDate="
				+ startDate + "&endDate=" + endDate+"&responsible="+responsible+"&project="+project,
		success : function(response) {
			if (response.status == "SUCCESS") {

				toastr.success("","Operation updated successfully ");
				$('#m_operation_edit_sidebar_close').click();
				toggleOperationFragment(operationId);
				$('#operations_datatable').mDatatable('reload');


			} else {
				if(response.result == "INVALIDUSER"){
					toastr.error('Invalid responsible');
					
				}else{
					toastr.error("", "Please fill all required fields");
					for (i = 0; i < response.result.length; i++) {
						if (response.result[i].code == "operation.name.empty")
							nameError.show('slow');
//						if (response.result[i].code == "operation.project.empty")
//							projectError.show('slow');
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
			toastr.error("Couldn't update operation", "Server Error");
		}
	});
}

function populateSelectOwnedProjectsEditServiceSidebar() {
	var projectId = $('#selected_project_id').val();
	$.ajax({
		type : "GET",
		url : '/set-select-owned-projects',
		async: false,
		success : function(response) {

			var html_select_options = "";

			var html_text = "<option value=' '>Nothing Selected</option>";
			if (response.length == 1) {
				if(response[0].id == projectId )
					html_text += "<option value='" + response[0].id + "' selected>"
					+ response[0].name + "</option>";
				else
				html_text += "<option value='" + response[0].id + "'>"
						+ response[0].name + "</option>";
				$("#select_project_edit_service").html(html_text);
			} else {
				for (i = 0; i < response.length; i++) {
					if(response[i].id == projectId )
						html_text += "<option value='" + response[i].id + "' selected>"
						+ response[i].name + "</option>";
					else
					html_text += "<option value='" + response[i].id + "'>"
							+ response[i].name + "</option>";
				}
				$("#select_project_edit_service").html(html_text);

			}
			$("#select_project_edit_service").selectpicker('refresh');

//			$("#select_project_edit_operation").on('change', function() {
//				var projectId = $("#select_project_edit_operation").val();
//				
//				if(projectId == ' '){ 
//					$('#sites_map_container_edit_operation').attr("style","display:none;width:100%; height:50px;position: relative;");
//					$('#sites_map_canvas_sidebar_edit_operation').attr("style","position: absolute; display:none;top: 20%; right: 0; bottom: 0; left: 0;");
//				}else{
//					initializeSitesGmapEditOperation(projectId);
//				}
//			});


		},
		error : function(e) {
			alert('populate select project edit service Error!');
			$("#select_project_edit_service").html(
					"<option value=''>Nothing selected</option>");

		}
	});

}

function populateSelectOperationEditService(projectId){
	var selectedOperationId = $('#operation_fragment_selected_operation_id').val();

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
				$("#select_operation_edit_service").html(html_text);
				} else {
				for (i = 0; i < response.length; i++) {
					if(selectedOperationId == response[i].id){
						html_text += "<option value='" + response[i].id + "' selected>" + response[i].name
						+ "</option>";
					}else{
						html_text += "<option value='" + response[i].id + "'>" + response[i].name
						+ "</option>";	
					}
					
				}
				$("#select_operation_edit_service").html(html_text);


			}
			$("#select_operation_edit_service").selectpicker('refresh');

			
			
		},
	error : function(e) {
		alert('Select operation edit service Error !');
		$("#select_operation_edit_service").html(
				"<option value=''>Nothing selected</option>");
		
	}
});
}

function populateEditServiceSidebar(){
	populateSelectOwnedProjectsEditServiceSidebar();
	
	var projectId = $("#select_project_edit_service").val();
	populateSelectOperationEditService(projectId);

	var serviceId = $('#service_fragment_selected_service_id').val();
	$.ajax({
		type : "GET",
		url : '/get-service-details',
		data : 'id=' + serviceId,
		async : false,
		success : function(response) {
			$("#input_edit_service_name").val(response.name);
			
			$('#input_edit_service_price').val(response.price);
			$('#input_edit_service_category').val(response.category);
			$('#input_edit_service_category').selectpicker('refresh');
			$("#input_edit_service_flag").val(response.flag);
			$("#input_edit_service_flag").selectpicker('refresh');
		},
		error : function(e) {
			alert('Error: Service Details population ' + e);
		}
	});
}

function doUpdateServiceAjaxPost() {

	var serviceId = $('#service_fragment_selected_service_id').val();
	
	var nameError = $('#edit_service_name_error');
	var projectError = $('#edit_service_operation_error');
	var operationError = $('#edit_service_operation_error');
	var priceError = $('#edit_service_price_error');
	var categoryError = $('#edit_service_category_error');
	
	var name = $('#input_edit_service_name').val();
	var project = $('#select_project_edit_service').val();
	var operation = $('#select_operation_edit_service').val();
	var price = $('#input_edit_service_price').val();
	var category = $('#input_edit_service_category').val();
	var flag = $('#input_edit_service_flag').val();
	

	nameError.hide('fast');
	projectError.hide('fast');
	operationError.hide('fast');
	priceError.hide('fast');
	categoryError.hide('fast');

	$.ajax({
		type : "POST",
		url : '/update-service',
		data : "id="+serviceId +"&name=" + name + "&priceHT="+price+"&category="
				+ category + "&flag=" + flag+"&operation="+operation+"&project="+project,
		success : function(response) {
			if (response.status == "SUCCESS") {

				toastr.success("","Service updated successfully ");
				$('#m_service_edit_sidebar_close').click();
				toggleServiceFragment(serviceId);
				$('#services_datatable').mDatatable('reload');

			} else {

					toastr.error("", "Please fill all required fields");
					for (i = 0; i < response.result.length; i++) {
						console.log(response.result[i].code);
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
			toastr.error("Couldn't update service", "Server Error");
		}
	});
}

$(document).ready(function() {
	
	populateProjectDetails();
	projectDynamicContent();
	taskDropZone();
	issueDropZone();

   
	
	
	
	
});
