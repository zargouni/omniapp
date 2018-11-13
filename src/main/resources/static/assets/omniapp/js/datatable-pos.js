//== Class definition

var DatatablePosJsonRemote = function() {
	// == Private functions

	// basic demo
	var demo = function() {
		var projectId = $('#selected_project_id').val();
		var datatable = $('#pos_datatable')
				.mDatatable(
						{
							// datasource definition
							data : {
								type : 'remote',
								source : {
									read : {
										// sample GET method
										method : 'GET',
										url : '/json-pos?id=' + projectId,
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
								pageSize : 10,
								serverPaging : false,
								serverFiltering : false,
								serverSorting : false,
							},

							// layout definition
							layout : {
								scroll : false,
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
										pageSizeSelect : [ 10, 20, 30, 40, 50 ],
									},
								},
							},

							search : {
								input : $('#generalSearchPos')
							},

							// columns definition
							columns : [
									{
										field : "number",
										title : "#",
										width : 200,
										sortable : true,
										selector : false,
										textAlign : 'center',
										template : function(row) {
											return '<span style="font-weight: 500;">'
													+ row.number + '</span>';
										}
									},
									{
										field : "actual_money",
										title : "Progress",
										width : 200,
										sortable : true,
										selector : false,
										textAlign : 'center',
										template : function(row) {
											if(row.number != "NOPO")
											 return '<div style="width:200px !important;" class="progress m-progress--sm">'
								          	  +'<div class="progress-bar m--bg-success" role="progressbar" style="width: '+row.money_percentage+'%;" aria-valuemin="0" aria-valuemax="100"></div>'
								          	  +'</div>'
								          	  +'<span style="font-weight:600;font-size:12px;float:right;" class="m-widget4__number m--font-info">'+row.actual_money+' '+row.currency+'</span>'
								          	+'<span style="font-weight:600;font-size:12px;float:left;" class="m-widget4__number m--font-info">'+row.money_percentage+'%</span>'
								          	  ;
											return "-"; 
										}
									},
									{
										field : "services",
										title : "Total Services",
										sortable : true,
										width : 80,
										textAlign : 'center',
										template : function(row) {
											return '<span style="font-weight: 400;">'
													+ row.services + '</span>';
										}
									},
									{
										field : "price",
										title : "Total Price",
										sortable : true,
										width : 80,
										textAlign : 'center',
										template : function(row) {
											return '<span style="font-weight:600;font-size:14px;color: #f4516c;">'
													+ row.price
													+ ' '
													+ row.currency + '</span>';
										}
									},
									{
										field : "Actions",
										width : 100,
										title : "Actions",
										sortable : false,
										overflow : 'visible',
										textAlign : 'center',
										template : function(row, index,
												datatable) {
											var dropup = (datatable
													.getPageSize() - index) <= 4 ? 'dropup'
													: '';
											if ($('#session_user_id').val() == $(
													'#project_manager_id')
													.val()) {
												if (row.number == "NOPO") {
													return '\
													<div class="dropdown '
															+ dropup
															+ '">\
							<a href="#" class="btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" data-toggle="dropdown">\
                                <i class="la la-ellipsis-h"></i>\
                            </a>\
						  	<div class="dropdown-menu dropdown-menu-right">\
						    	<a href="#" onclick="toggleModalEditDetailsPO(\''
															+ row.number
															+ '\')" class="dropdown-item"><i class="la la-edit"></i> View Details</a>\
						  	</div>\
						</div>';
												}
												return '\
												<div class="dropdown '
														+ dropup
														+ '">\
													<a href="#" class="btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" data-toggle="dropdown">\
						                                <i class="la la-ellipsis-h"></i>\
						                            </a>\
												  	<div class="dropdown-menu dropdown-menu-right">\
												    	<a href="#" onclick="toggleModalEditDetailsPO(\''
														+ row.number
														+ '\')" class="dropdown-item"><i class="la la-edit"></i> Edit Details</a>\
												  	</div>\
												</div>\
																		\<a onclick="handleRemovePOClick(\''
														+ row.number
														+ '\')" id="btn-remove-boq-'
														+ row.number
														+ '" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="Delete PO">\
													<i class="la la-trash-o"></i>\
												</a>\
											';
											}

											return '\
											<div class="dropdown '
													+ dropup
													+ '">\
												<a href="#" class="btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" data-toggle="dropdown">\
					                                <i class="la la-ellipsis-h"></i>\
					                            </a>\
											  	<div class="dropdown-menu dropdown-menu-right">\
											    	<a href="#" onclick="toggleModalEditDetailsPO(\''
													+ row.number
													+ '\')" class="dropdown-item"><i class="la la-edit"></i> View Details</a>\
											  	</div>\
											</div>';

										}
									}

							]
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

function toggleModalAddPO() {
	populateCheckboxesEditPo();
	$('#add_po_modal').modal('show');
}

function doAddPo() {
	var poNumber = $('#input_new_po_number').val();
	var projectId = $('#selected_project_id').val();

	$
			.ajax({
				type : "POST",
				url : '/check-po-existance',
				data : "id=" + projectId + "&number=" + poNumber,
				async : false,
				success : function(response) {
					// we have the response
					if (response.status == "EXISTS") {
						toastr
								.error(
										"Couldn't add PO, a PO with the same number already exists in this project",
										"Error");
					} else if (response.status == "SUCCESS") {
						if ($('#nopo_services_checkbox_list_add').find(
								":checkbox:checked").length == 0) {
							toastr
									.warning("You have to select at least one service");
						} else {
							handleAddPoServices(poNumber);
							toastr
									.success("PO Added successfully",
											"Well done");
							$('#add_po_modal').modal('hide');
						}

					}
				},
				error : function(e) {
					toastr.error("Couldn't add PO", "Server Error");
				}
			});
}

function handleAddPoServices(number) {
	$('#nopo_services_checkbox_list_add').find(":checkbox:checked").each(
			function() {
				var serviceId = $(this).attr('id');
				$
						.ajax({
							type : "POST",
							url : '/update-service-po',
							data : "id=" + serviceId + "&number=" + number,
							async : false,
							success : function(response) {
								// we have the response
								if (response.status == "FAIL") {
									toastr.error("Couldn't update service PO",
											"Error");
								}
							},
							error : function(e) {
								toastr.error("Couldn't update service PO",
										"Server Error");
							}
						});
			});
	$('#po_datatable').mDatatable('reload');
	$('#pos_datatable').mDatatable('reload');

	// $('#po_services_cancel_button').click();
}

function toggleModalEditDetailsPO(number) {

	$('#po_services_update_button').attr('onClick',
			'handleUpdatePoServices(\'' + number + '\')');
	$('#po_title').html("PO n°: " + number);
	$('#po_datatable').mDatatable('destroy');
	DatatablePoServicesJsonRemote.init(number);
	if(number == "NOPO"){
		$('#toggle_add_po_services').hide();
	}else{
		$('#toggle_add_po_services').show();
	}

	$('#toggle_add_po_services').on("click", function() {
		populateCheckboxesEditPo();
		$('#po_services_datatable_section').hide();
		$('#add_po_services_section').show();
	});

	$('#po_services_cancel_button').on("click", function() {
		$('#add_po_services_section').hide();
		$('#po_services_datatable_section').show();
	});

	$('#edit_po_modal').modal('show');
}

function handleUpdatePoServices(number) {
	$('#nopo_services_checkbox_list').find(":checkbox:checked").each(
			function() {
				var serviceId = $(this).attr('id');
				$
						.ajax({
							type : "POST",
							url : '/update-service-po',
							data : "id=" + serviceId + "&number=" + number,
							async : false,
							success : function(response) {
								// we have the response
								if (response.status == "FAIL") {
									toastr.error("Couldn't update service PO",
											"Error");
								}
							},
							error : function(e) {
								toastr.error("Couldn't update service PO",
										"Server Error");
							}
						});
			});
	$('#po_datatable').mDatatable('reload');
	$('#pos_datatable').mDatatable('reload');

	$('#po_services_cancel_button').click();
}

function populateCheckboxesEditPo() {
	var projectId = $('#selected_project_id').val();
	var html_text = "";
	$
			.ajax({
				type : "GET",
				url : '/json-po-services',
				data : 'number=NOPO&id=' + projectId,
				async : false,
				success : function(response) {
					if (response.length == 0) {
						html_text = "<center><h3>No services available</h3></center>";
						$('#add_po_services_section').find('.m-form__help')
								.hide();
						$('#add_po_nopo_services_section')
								.find('.m-form__help').hide();
					} else {
						for (var i = 0; i < response.length; i++) {
							html_text += '<label '
									+ '	class="m-checkbox m-checkbox--success"> <input'
									+ '	id="' + response[i].id
									+ '" type="checkbox"' + '	>'
									+ response[i].name
									+ ' (in <a href="#" style="color: red;">'
									+ response[i].operation_name
									+ '</a>) <span></span> <input'
									+ '	type="hidden" id="nopo_service_'
									+ response[i].name + '"' + '	value="'
									+ response[i].id + '"> ' + '</label>';
						}
					}
					if ($('#nopo_services_checkbox_list_add').length)
						$('#nopo_services_checkbox_list_add').html(html_text);
					if ($('#nopo_services_checkbox_list').length)
						$('#nopo_services_checkbox_list').html(html_text);
				},
				error : function(e) {
					alert('Error: NOPO services checkboxes  ' + e);
				}
			});

}

function handleRemovePOClick(number) {
	var projectId = $('#selected_project_id').val();

	swal({
		title : 'Are you sure?',
		text : "You won't be able to revert this!",
		type : 'warning',
		showCancelButton : true,
		confirmButtonText : 'Yes, remove it!'
	})
			.then(
					function(result) {
						if (result.value) {
							$
									.ajax({
										type : "POST",
										url : '/delete-po',
										data : "id=" + projectId + "&number="
												+ number,
										async : false,
										success : function(response) {
											if (response.status == "SUCCESS") {
												swal('Removed!',
														'PO has been removed.',
														'success');
												// $('#po_datatable').mDatatable('reload');
												$('#pos_datatable').mDatatable(
														'reload');

											} else {
												swal(
														'Fail!',
														'PO not removed, some or all services couldn\'t be updated',
														'error');
											}

										},
										error : function(e) {
											toastr.error("Couldn't remove PO",
													"Server Error");
											result = false;
										}
									});

						}

					}

			);

}
