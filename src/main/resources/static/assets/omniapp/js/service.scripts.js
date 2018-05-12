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

}