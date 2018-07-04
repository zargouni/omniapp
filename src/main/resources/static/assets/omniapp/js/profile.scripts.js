function handleOnTaskClick(taskId){
	$.ajax({
		type : "GET",
		url : '/get-task-parents',
		data : 'id=' + taskId,
		async : false,
		success : function(response) {
			window.location.href = '/project?id='+response.project_id+'#task='+taskId;
	    	 $('.loader-wrapper').show();

		},
		error : function(e) {
			alert('Error: Task click ' + e);
		}
	});
}

function handleOnIssueClick(issueId){
	$.ajax({
		type : "GET",
		url : '/get-issue-parents',
		data : 'id=' + issueId,
		async : false,
		success : function(response) {
			window.location.href = '/project?id='+response.project_id+'#issue='+issueId;
		},
		error : function(e) {
			alert('Error: Issue click ' + e);
		}
	});
}

function populateProfileStats(){
	var profileId = $('#profile_user_id').val();
	$.ajax({
		type : "GET",
		url : '/json-user-stats',
		data : 'id=' + profileId,
		async : false,
		success : function(response) {
			$('#profile_all_projects_count').html(response.projects);
			$('#profile_all_tasks_count').html(response.tasks);
			$('#profile_all_issues_count').html(response.issues);
		},
		error : function(e) {
			alert('Error: profile stats ' + e);
		}
	});
}

