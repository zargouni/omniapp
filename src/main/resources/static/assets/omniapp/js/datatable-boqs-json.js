//== Class definition


var DatatableBoqsJsonRemote = function () {
	// == Private functions

	
	// basic demo
	var demo = function () {

		var datatable = $('#boqs_datatable').mDatatable({
			// datasource definition
			data: {
		        type: 'remote',
		        source: {
		          read: {
		            // sample GET method
		            method: 'GET',
		            url: '/json-boqs',
		            map: function(raw) {
		              // sample data mapping
		              var dataSet = raw;
		              if (typeof raw.data !== 'undefined') {
		                dataSet = raw.data;
		              }
		              return dataSet;
		            },
		          },
		        },
		        pageSize: 10,
		        serverPaging: false,
		        serverFiltering: false,
		        serverSorting: false,
		      },

		      // layout definition
		      layout: {
		        scroll: false,
		        footer: false
		      },

		      // column sorting
		      sortable: true,

		      pagination: true,

		      toolbar: {
		        // toolbar items
		        items: {
		          // pagination
		          pagination: {
		            // page size select
		            pageSizeSelect: [10, 20, 30, 50, 100],
		          },
		        },
		      },

			search: {
				input: $('#generalSearch')
			},

			// columns definition
			columns: [{
				field: "id",
				title: "#",
				width: 50,
				sortable: false,
				selector: false,
				textAlign: 'center'
			}, {
				field: "name",
				title: "Name",
				sortable: true,
				width: 400,
				
			}, {
		        field: 'valid',
		        title: 'Status',
		          // callback function support for column rendering
		        template: function(row) {
		           var status = {
		              true: {'title': 'Valid', 'class': 'm-badge--success'},
		              false: {'title': 'Invalid', 'class': ' m-badge--metal'},
		           };
		           return '<span class="m-badge ' + status[row.valid].class + ' m-badge--wide">' + status[row.valid].title + '</span>';
		         },
		    }, {
				field: "startDate",
				title: "Start Date",
				type: 'date',
				format: 'DD MMMM YYYY',
				sortable: true,
				width: 150
			}, {
				field: "endDate",
				title: "Due Date",
				type: 'date',
				format: 'DD MMMM YYYY',
				sortable: true,
				width: 150,
				responsive: {visible: 'lg'}
			},{
				field: "serviceCount",
				title: "Services",
				type: 'number',
				sortable: true,
				width: 100,
				responsive: {visible: 'lg'}
			},
			{
				field: "Actions",
				width: 60,
				title: "Actions",
				sortable: false,
				overflow: 'visible',
				template: function (row, index, datatable) {
					var dropup = (datatable.getPageSize() - index) <= 4 ? 'dropup' : '';
					
					return '\
						<div class="dropdown ' + dropup + '">\
							<a href="#" class="btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" data-toggle="dropdown">\
                                <i class="la la-ellipsis-h"></i>\
                            </a>\
						  	<div class="dropdown-menu dropdown-menu-right">\
						    	<a href="#" onclick="toggleModalEditDetails('+row.id+')" class="dropdown-item"><i class="la la-edit"></i> Edit Details</a>\
						  	</div>\
						</div>\
												\<a onclick="handleRemoveBoqClick('+row.id+')" id="btn-remove-boq-'+row.id+'" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="Delete BOQ">\
							<i class="la la-trash-o"></i>\
						</a>\
					';
				}
			}]
		});

		var query = datatable.getDataSourceQuery();
		
		$('#m_form_status').on('change', function () {
			datatable.search($(this).val(), 'Status');
		}).val(typeof query.Status !== 'undefined' ? query.Status : '');

		$('#m_form_type').on('change', function () {
			datatable.search($(this).val(), 'Type');
		}).val(typeof query.Type !== 'undefined' ? query.Type : '');

		$('#m_form_status, #m_form_type').selectpicker();

	};

	return {
		// public functions
		init: function () {
			demo();
		}
	
		
	};
}();



function populateBoqDetails(id){
	$('#service_templates_checkbox_list').find(":checkbox").prop('checked',false);
	$.ajax({
		type : "GET",
		url : '/get-boq-details',
		data : "id="+id,
	    success : function(response) {

			$("#input_boq_name").val(response.name);
			$("#input_boq_start_date").val(response.startDate);
			$("#input_boq_end_date").val(response.endDate);
			for (i = 0; i < response.services.length; i++) {
				$('#service_templates_checkbox_list')
				.find(":checkbox[id='"+response.services[i].id+"']")
				.prop("checked", true);
					
			}			
		},
		error : function(e) {
			toastr('Error: can\'t get BOQ details ', e);
		}
	});	
}

function handleRemoveBoqClick(id){
	var boqId = $("#btn-remove-boq-" + id).attr('id').substring(15);

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
								url : '/delete-boq',
								data : "boqId=" + boqId,
								async : true,
								success : function(response) {
									if (response.status == "SUCCESS") {
										swal('Deleted!',
												'BOQ has been deleted.',
												'success');
										$('#boqs_datatable').mDatatable('reload');

									} else {
										swal('Fail!', 'BOQ not deleted.',
												'error');
									}

								},
								error : function(e) {
									toastr.error("Couldn't delete BOQ",
											"Server Error");
									result = false;
								}
							});

				}

			}

	);

}

function handleUpdateBoq(id){
	// TODO
	var name = $('#input_boq_name').val();
	var startDate = $('#input_boq_start_date').val();
	var endDate = $('#input_boq_end_date').val();
	if ($('#service_templates_checkbox_list :checkbox:checked').length > 0){
	$.ajax({
		type : "POST",
		url : '/update-boq',
		data : "id="+id+"&name="+name+"&startDate="+startDate+"&endDate="+endDate,
	    success : function(response) {
			// we have the response
			if (response.status == "SUCCESS") {
				toastr.success("BOQ updated successfully", "Well done!");
				if(response.result != 'undefined'){
					doUpdateServiceTemplatesToBoq(id);
					$("#edit-boq-details").modal('hide');
					$('#input_boq_name').val('');
					$('#input_boq_start_date').val('');
					$('#input_boq_end_date').val('');
					$('#service_templates_boq_checkbox_list :checkbox').prop('checked', false);
					$('#boqs_datatable').mDatatable('reload');
				}
			} else {
				if(response.result == 'boq-exists'){
					toastr.error("Couldn't create BOQ, a BOQ with this name already exists", "Change BOQ name");
				}else{
				toastr.error("Couldn't create BOQ", "Error");
				}
				for (i = 0; i < response.result.length; i++) {
						if (response.result[i].code == "boq.name.empty")
							$('#new_boq_name_error').show('slow');
						if (response.result[i].code == "boq.startDate.empty")
							$('#new_boq_start_date_error').show('slow');
						if (response.result[i].code == "boq.endDate.empty")
							$('#new_boq_end_date_error').show('slow');
						if (response.result[i].code == "boq.date.nomatch")
							toastr.warning("Date range you mentioned is invalid", "Check date range");
				}
				
			}
		},
		error : function(e) {
			toastr('Error: can\'t get BOQ details ', e);
		}
	});	
	}else{
		toastr.warning("Couldn't add BOQ, you have to select at least 1 service", "Select Services");
	}
	//console.log("hi"+id);
}

function toggleModalEditDetails(id){
	populateBoqDetails(id);
	$('#button_update_boq').attr('onClick','handleUpdateBoq('+id+')');
	$('#edit-boq-details').modal();
}




function doUpdateServiceTemplatesToBoq( boqId ){
	$('#service_templates_checkbox_list').find(":checkbox:checked").each(function () {
		var templateId = $(this).attr('id');
		$.ajax({
			type : "POST",
			url : '/update-boq-service-templates',
			data : "id=" + templateId + "&boqId=" + boqId,
			async: false,
			success : function(response) {
				// we have the response
				if (response.status == "FAIL") {
					toastr.error("Couldn't add template", "Error");
				} 
			},
			error : function(e) {
				toastr.error("Couldn't add template", "Server Error");
			}
		});
	});
}


jQuery(document).ready(function () {
	//$.fn.dataTable.moment( 'dd MM YYYY' );
	DatatableBoqsJsonRemote.init();
	
});