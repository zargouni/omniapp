//== Class definition


var ServiceTemplateTasksDatatableJson = function () {
	//== Private functions

	
	// basic demo
	var demo = function (serviceTemplateId) {
		var datatable = $('#service_template_tasks_datatable').mDatatable({
			// datasource definition
			data: {
		        type: 'remote',
		        source: {
		          read: {
		            // sample GET method
		            method: 'GET',
		            url: '/json-service-template-tasks?id='+serviceTemplateId,
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
				field: "estimationJH",
				title: "Estimation (J/H)",
				sortable: true,
				width: 100,
				
			}, {
				field: "estimationTime",
				title: "Estimation in days",
				width: 100
			}
			, {
				field: "Actions",
				width: 60,
				title: "Actions",
				sortable: false,
				overflow: 'visible',
				template: function (row, index, datatable) {
					var dropup = (datatable.getPageSize() - index) <= 4 ? 'dropup' : '';
					
					return '\<a onclick="handleRemoveTaskTemplateClick('+row.id+')" id="btn-remove-task-template-'+row.id+'" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="Delete task">\
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
		init: function (serviceTemplateId) {
			demo(serviceTemplateId);
		}
	};
}();

//jQuery(document).ready(function () {
//	ServiceTemplateTasksDatatableJson.init();
//});