function toggleServiceFragment(serviceId) {
	$('#service_fragment_selected_service_id').attr('value', serviceId);
	$("#operation-fragment").hide();
	if ($('#service_tasks_datatable').hasClass('m-datatable--loaded')){
		$('#service_tasks_datatable').mDatatable('destroy');
	}

		DatatableServiceTasksJsonRemote.init();
	populateServiceFragment(serviceId);
	$('#service-fragment').show();
}

function populateServiceFragment(serviceId) {
	
	$.ajax({
		type : "GET",
		url : '/get-service-details',
		data: "id="+serviceId,
	    success : function(response) {
	    	$('#service_fragment_service_name').html(response.name);
	    	$('#service_fragment_price').html(response.price+ " " + response.currency);
	    	$('#service_fragment_category').html("("+response.category+")");
	    	$('#service_fragment_creation_date').html(response.formattedCreationDate);
	    	if(response.parent != "none")
	    		$('#service_fragment_parent').html('in <a style="color: #36a3f7;cursor: pointer;" onclick="toggleOperationFragment('+response.parent.id+')">'
	    			+response.parent.name+'</a>');
	    	$('#service_fragment_all_tasks_count').html(response.taskCount);
	    	$('#service_fragment_closed_tasks_percentage').html(response.percentage);
	    	$('#service_fragment_open_tasks_percentage').html(response.openTasksPercentage);

	    	
			
		},
		error : function(e) {
			alert('Error: service details ' + e);
		}
	});
}