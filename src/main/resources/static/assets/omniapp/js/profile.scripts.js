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

function populateModalUpdateInfos(){
	var userId = $('#profile_user_id').val();
	ValidateUpdateInfosForm.init();
	$.ajax({
		type : "GET",
		url : '/get-user-infos',
		data : 'id=' + userId,
		async : false,
		success : function(response) {
			$('#user_first_name').val(response.firstName);
			$('#user_last_name').val(response.lastName);
			$('#user_email').val(response.email);
		},
		error : function(e) {
			alert('Error: User infos ' + e);
		}
	});
}

function UpdateUserInfos(){
	var userId = $('#profile_user_id').val();
	var firstName = $('#user_first_name').val();
	var lastName = $('#user_last_name').val();
	var email = $('#user_email').val();
	$.ajax({
		type : "POST",
		url : '/update-user-infos',
		data : 'id=' + userId+"&firstName="+firstName+"&lastName="+lastName+"&email="+email,
		async : false,
		success : function(response) {
			if(response.status == "SUCCESS"){
				toastr.success("Informations updated successfully");
				location.reload();
			}else{
				toastr.error("Couldn't update user informations");
			}
		},
		error : function(e) {
			alert('Error: cant update user infos ' + e);
		}
	});
}

function UpdateUserPassword(){
	var userId = $('#profile_user_id').val();
	var currentPassword = $('#current_password').val();
	var newPassword = $('#new_password').val();
	$.ajax({
		type : "POST",
		url : '/update-user-password',
		data : 'id=' + userId+"&current="+currentPassword+"&new="+newPassword,
		async : false,
		success : function(response) {
			if(response.status == "SUCCESS"){
				toastr.success("Password updated successfully");
				$('#modal_update_password').modal('hide');
				$('#modal_update_user_infos').modal('hide');
			}else if(response.status == "WRONG-PASS"){
				toastr.error("Incorrect password");
			}else{
				toastr.error("Couldn't update user password");
			}
		},
		error : function(e) {
			alert('Error: cant update user password ' + e);
		}
	});
}


var ValidateUpdateInfosForm = function () {
    
    var formInfos = function () {
        $( "#update_user_infos_form" ).validate({
            rules: {
                email: {
                    required: true,
                    email: true,
                    minlength: 8 
                },
                user_first_name: {
                    required: true, 
                },
                user_last_name: {
                    required: true,
                }
                },
            
            //display error alert on form submit  
            invalidHandler: function(event, validator) {     
               toastr.error("Oh snap! Change a few things up and try submitting again.");
            },

            submitHandler: function (form) {
            	UpdateUserInfos();
            }
        });       
    }
    
    return {
        // public functions
        init: function() {
            formInfos(); 
            
        }
    };
    
}();

var ValidateUpdatePasswordForm = function () {
    
    var formPassword = function () {
        $( "#update_password_form" ).validate({
            rules: {
                current_password: {
                    required: true,
                   },
                new_password: {
                    required: true,
                    minlength: 6
                },
                confirm_password: {
                    required: true,
                    minlength: 6,
                    equalTo: "#new_password"
                }
                },
            
            //display error alert on form submit  
            invalidHandler: function(event, validator) {     
               toastr.error("Oh snap! Change a few things up and try submitting again.");
            },

            submitHandler: function (form) {
            	UpdateUserPassword();
            }
        });       
    }
    
    return {
        // public functions
        init: function() {
        	formPassword(); 
            
        }
    };
    
}();





