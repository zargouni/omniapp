//== Class definition

var DatatableClassificationsJsonRemote = function() {
	// == Private functions

	// basic demo
	var demo = function() {
		var datatable = $('#classifications_datatable')
				.mDatatable(
						{
							// datasource definition
							data : {
								type : 'remote',
								source : {
									read : {
										// sample GET method
										method : 'GET',
										url : '/json-classifications',
										map : function(raw) {
											// sample data mapping
											var dataSet = raw;
											if (typeof raw.data !== 'undefined') {
												dataSet = raw.data;
											}
											return dataSet;
										},
									},
								},
								pageSize : 5,
								serverPaging : false,
								serverFiltering : false,
								serverSorting : false,
							},

							// layout definition
							layout : {
								scroll : true,
								footer : false
							},

							// column sorting
							sortable : true,

							pagination : true,

							toolbar : {
								// toolbar items
								items : {
									// pagination
									pagination : {
										// page size select
										pageSizeSelect : [ 5, 10, 20, 40, 50 ],
									},
								},
							},

							search : {
								input : $('#generalSearchClassifications')
							},

							// columns definition
							columns : [

									{
										field : "name",
										title : "Name",
										sortable : true,
										width : 800,
										template: function(row){
											return '<span class="m-badge m-badge--info m-badge--wide" style="font-weight: 500;">'+row.name+'</span>';
										}

									},
									{
										field : "Actions",
										width : 60,
										title : "Actions",
										sortable : false,
										overflow : 'visible',
										template : function(row, index,
												datatable) {
											var dropup = (datatable
													.getPageSize() - index) <= 4 ? 'dropup'
													: '';

											return '\
						<div class="dropdown '
													+ dropup
													+ '">\
							<a href="#" class="btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" data-toggle="dropdown">\
                                <i class="la la-ellipsis-h"></i>\
                            </a>\
						  	<div class="dropdown-menu dropdown-menu-right">\
						    	<a href="#" onclick="toggleModalEditClassification('
													+ row.id
													+ ')" class="dropdown-item"><i class="la la-edit"></i> Edit Details</a>\
						  	</div>\
						</div>\
												\<a onclick="handleRemoveClassificationClick('
													+ row.id
													+ ')" id="btn-remove-classification-'
													+ row.id
													+ '" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="Delete Classification">\
							<i class="la la-trash-o"></i>\
						</a>\
					';
										}
									} ]
						});

		var query = datatable.getDataSourceQuery();

		$('#m_form_status').on('change', function() {
			datatable.search($(this).val(), 'Status');
		}).val(typeof query.Status !== 'undefined' ? query.Status : '');

		$('#m_form_type').on('change', function() {
			datatable.search($(this).val(), 'Type');
		}).val(typeof query.Type !== 'undefined' ? query.Type : '');

		$('#m_form_status, #m_form_type').selectpicker();

	};

	return {
		// public functions
		init : function() {
			demo();
		}

	};
}();

function doAddClassification() {
	var name = $('#input_classification_name').val();
	var nameError = $("#classification_name_error");

	nameError.hide("fast");

	$.ajax({
		type : "POST",
		url : '/add-classification',
		data : "name=" + name,
		success : function(response) {
			// we have the response
			if (response.status == "SUCCESS") {
				toastr.success("Classification added successfully");
				$('#classifications_datatable').mDatatable("reload");
				$("#m_modal_classification").modal("hide");

			} else {
				toastr.error("Couldn't add classification");
				for (i = 0; i < response.result.length; i++) {
					if (response.result[i].code == "classification.name.empty")
						nameError.show("slow");
				}
			}
		},
		error : function(e) {
			console.log("error: " + e);
			toastr.error("Couldn't add classification", "Server Error");
		}
	});

}

function handleRemoveClassificationClick(classId) {
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
						url : '/delete-classification',
						data : "classId=" + classId,
						async : true,
						success : function(response) {
							if (response.status == "SUCCESS") {
								swal('Deleted!',
										'Classification has been deleted.',
										'success');
								$('#classifications_datatable').mDatatable(
										'reload');

							} else {
								swal('Fail!', 'Classification not deleted.',
										'error');
							}

						},
						error : function(e) {
							toastr.error("Couldn't delete Classification",
									"Server Error");
							result = false;
						}
					});
				}
			}
	);
}


function doUpdateClassification(classId) {
	var name = $("#edit_classification_name").val();
	var nameError = $("#edit_classification_name_error");

	$.ajax({
		type : "POST",
		url : '/update-classification',
		data : "classId=" + classId + "&name=" + name,
		async : true,
		success : function(response) {
			if (response.status == "SUCCESS") {
				toastr.success("Classification updated successfully");
				$('#classifications_datatable').mDatatable('reload');
				$("#edit-classification-details").modal('hide');
			} else {
				toastr.error("Couldn't update classification");

				if (response.result[i].code == "classification.name.empty")
					nameError.show("slow");

			}

		},
		error : function(e) {
			toastr.error("Couldn't update Classification", "Server Error");
		}
	});
}

function toggleModalEditClassification(classId) {

	$.ajax({
		type : "GET",
		url : '/json-classification',
		data : "classId=" + classId,
		async : true,
		success : function(response) {
			if (response != null) {
				$("#edit_classification_name").val(response.name);
				$("#button_update_classification").attr("onclick",
						"doUpdateClassification(" + classId + ")");
			}

		},
		error : function(e) {
			toastr.error("Couldn't get Classification", "Server Error");
		}
	});

	$("#edit-classification-details").modal('show');
}


$(document).ready(function() {
	DatatableClassificationsJsonRemote.init();
});
