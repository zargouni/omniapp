//== Class definition


var DatatableJsonRemoteDemo = function () {
	//== Private functions

	
	// basic demo
	var demo = function () {

		var datatable = $('#service_templates_datatable').mDatatable({
			// datasource definition
			data: {
		        type: 'remote',
		        source: {
		          read: {
		            // sample GET method
		            method: 'GET',
		            url: '/json-service-templates',
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
				width: 200,
				
			}, {
				field: "category",
				title: "Category",
				sortable: true,
				width: 100,
				
			}, {
				field: "description",
				title: "Description",
				width: 400
			}
			, {
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
						    	<a href="#"  onclick="toggleModalAddTasks('+row.id+')" class="dropdown-item"><i class="la la-leaf"></i> Add Tasks</a>\
						    	<a href="#" onclick="toggleModalEditTasks('+row.id+')" class="dropdown-item"><i class="la la-print"></i> Edit Task Lisk</a>\
						  	</div>\
						</div>\
												\<a onclick="handleRemoveServiceClick('+row.id+')" id="btn-remove-template-'+row.id+'" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="Delete template">\
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

jQuery(document).ready(function () {
	DatatableJsonRemoteDemo.init();
});