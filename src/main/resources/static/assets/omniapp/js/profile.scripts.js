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

