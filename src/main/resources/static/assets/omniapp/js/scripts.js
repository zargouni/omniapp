function doAddProjectAjaxPost() {
	// get the form values
	var name = $('#project_name').val();
	var client = $('#project_client').val();
	var country = $('#project_country').val();
	var currency = $('#project_currency').val();
	var owner = $('#project_owner').val();

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
				errorInfo = "";
				for (i = 0; i < response.result.length; i++) {
					errorInfo += "<br>" + (i + 1) + ". "
							+ response.result[i].code;
				}
				$('#error').html(
						"Please correct following errors: " + errorInfo);
				$('#error').show('slow');
			}
		},
		error : function(e) {
			alert('Error: ' + e);
		}
	});
}

$(document).ready(function() {
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
