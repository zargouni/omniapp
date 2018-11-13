//== Class definition


var DatatablePoServicesJsonRemote = function () {
	// == Private functions

	
	// basic demo
	var demo = function (poNumber) {
		var projectId = $('#selected_project_id').val();
		var datatable = $('#po_datatable').mDatatable({
			// datasource definition
			data: {
		        type: 'remote',
		        source: {
		          read: {
		            // sample GET method
		            method: 'GET',
		            url: '/json-po-services?number='+poNumber+'&id='+projectId,
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
		            pageSizeSelect: [10, 20, 30, 40, 50],
		          },
		        },
		      },

			search: {
				input: $('#generalSearchPoServices')
			},

			// columns definition
			columns: [
//				{
//				field: "id",
//				title: "#",
//				width: 40,
//				sortable: true,
//				selector: false,
//				textAlign: 'center',
//			}, 
			{
				field: "name",
				title: "Service",
				sortable: true,
				width: 100,
				textAlign: 'center',
//				template: function(row) {
//					return '<a style="font-weight: 500;" href="#" onclick="toggleIssueFragment('+ row.id +')">'+row.name + '</a>';
//				}
			}
			, 
			{
				field: "site_name",
				title: "Site",
				sortable: true,
				width: 80,
				textAlign: 'center',
//				template: function(row) {
//					return '<a style="font-weight: 500;" href="#" onclick="toggleIssueFragment('+ row.id +')">'+row.name + '</a>';
//				}
			}, 
			{
				field: "operation_name",
				title: "Operation",
				sortable: true,
				width: 100,
				textAlign: 'center',
//				template: function(row) {
//					return '<a style="font-weight: 500;" href="#" onclick="toggleIssueFragment('+ row.id +')">'+row.name + '</a>';
//				}
			}, 
			{
				field: "description",
				title: "Description",
				sortable: false,
				width: 150,
				textAlign: 'center',
//				template: function(row) {
//					return '<a style="font-weight: 500;" href="#" onclick="toggleIssueFragment('+ row.id +')">'+row.name + '</a>';
//				}
			},
			{
				field: "price",
				title: "Price",
				sortable: true,
				width: 80,
				textAlign: 'center',
				template: function(row) {
					return '<span style="font-weight:600;font-size:14px;color: #f4516c;">'
					+ row.price +' ' + row.currency + '</span>';
				}
			},
			{
				field: "Actions",
				width: 50,
				title: "Actions",
				sortable: false,
				overflow: 'visible',
				textAlign: 'center',
				template: function (row, index, datatable) {
					var dropup = (datatable.getPageSize() - index) <= 4 ? 'dropup' : '';
					if($('#session_user_id').val() == $('#project_manager_id').val() ){
						if(row.po_number == "NOPO"){
							return "-";
						}
						return '\<a onclick="handleRemovePOServiceClick('+row.id+')"  class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="Delete PO">\
						<i class="la la-trash-o"></i>\
					</a>\
				';
					}
					return '-';
					
				}
			}

			]
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
		init: function (poNumber) {
			demo(poNumber);
		}
	
		
	};
}();


function handleRemovePOServiceClick(serviceId){
	swal({
		title : 'Are you sure?',
		//text : "You won't be able to revert this!",
		type : 'warning',
		showCancelButton : true,
		confirmButtonText : 'Yes, remove it!'
	}).then(
			function(result) {
				if (result.value) {
					$
							.ajax({
								type : "POST",
								url : '/remove-service-from-po',
								data : "id=" + serviceId,
								async : true,
								success : function(response) {
									if (response.status == "SUCCESS") {
										swal('Removed!',
												'Service has been removed.',
												'success');
										$('#po_datatable').mDatatable('reload');
										$('#pos_datatable').mDatatable('reload');

									} else {
										swal('Fail!', 'Service not removed.',
												'error');
									}

								},
								error : function(e) {
									toastr.error("Couldn't remove Service",
											"Server Error");
									result = false;
								}
							});

				}

			}

	);
}

