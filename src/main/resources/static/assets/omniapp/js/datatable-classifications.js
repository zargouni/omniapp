//== Class definition

var DatatableClassificationsJsonRemote = function() {
	// == Private functions

	// basic demo
	var demo = function() {
		var datatable = $('#classifications_datatable').mDatatable({
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
				width : 800

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
				for(i = 0; i < response.result.length; i++){
					if(response.result[i].code == "classification.name.empty")
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

$(document).ready(function() {
	DatatableClassificationsJsonRemote.init();
});

function toggleModalEditOperationDetails(id) {
	// $('#m_quick_sidebar_add_toggle').click();
	// $('#m_quick_sidebar_add_tabs').children().hide();
	//	
	// $('#quick_sidebar_add_tabs_content').children().hide();
	// $('#quick_sidebar_edit_operation_nav').show();
	// $('#m_quick_sidebar_add_tabs_edit_operation').show();

	// populateOperationDetails(id);
	// populateCheckboxPopoversEditBoq();
	// $('#button_update_boq').attr('onClick','handleUpdateBoq('+id+')');
	// $('#edit-boq-details').modal();
}
