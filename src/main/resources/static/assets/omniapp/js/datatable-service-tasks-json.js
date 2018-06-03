//== Class definition


var DatatableServiceTasksJsonRemote = function () {
	// == Private functions

	
	// basic demo
	var demo = function () {
		var serviceId = $('#service_fragment_selected_service_id').val();
		var datatable = $('#service_tasks_datatable').mDatatable({
			// datasource definition
			data: {
		        type: 'remote',
		        source: {
		          read: {
		            // sample GET method
		            method: 'GET',
		            url: '/json-service-tasks?id='+serviceId,
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
				input: $('#generalSearchServiceTasks')
			},

			// columns definition
			columns: [{
				field: "id",
				title: "#",
				width: 20,
				sortable: false,
				selector: false,
				textAlign: 'center',
			}, {
				field: "name",
				title: "Task",
				sortable: true,
				width: 200,
				template: function(row) {
					return '<a onclick="toggleTaskFragment('+ row.id +')">'+row.name + '</a>';
				}
			}, {
		        field: 'status',
		        title: 'Status',
		        width: 100,
		        template: function(row) {
		           var status = {
		              'COMPLETED': {'title': 'Closed', 'class': 'm-badge--success'},
		              'ONGOING': {'title': 'Open', 'class': ' m-badge--danger'},
		           };
		           return '<span class="m-badge ' + status[row.status].class + ' m-badge--wide">' + status[row.status].title + '</span>';
		         },
		    } ,{
				field: "startDate",
				title: "Start Date",
				type: 'date',
				format: 'DD MMMM YYYY',
				sortable: true,
				width: 100
			}, {
				field: "endDate",
				title: "Due Date",
				type: 'date',
				format: 'DD MMMM YYYY',
				sortable: true,
				width: 100,
				responsive: {visible: 'lg'}
			},{
				field: "completedOn",
				title: "Completed On",
				type: 'date',
				format: 'DD MMMM YYYY',
				sortable: true,
				width: 100,
				template: function(row) {
			           if(row.completedOn != null)
			        	   return row.completedOn;
			           return 'None';
			         },
			},{
				field: "priority",
				title: "Priority",
				sortable: true,
				width: 100,
				responsive: {visible: 'lg'},
				template: function(row) {
			           var status = {
			        	  'none': {'title': 'None', 'class': 'm-badge--metal'},
			              'low': {'title': 'Low', 'class': 'm-badge--metal'},
			              'medium': {'title': 'Medium', 'class': ' m-badge--brand'},
			              'high': {'title': 'High', 'class': ' m-badge--danger'},
			           };
			           return '<span class="m-badge ' + status[row.priority].class + ' m-badge--wide">' + status[row.priority].title + '</span>';
			         },
			}
//			, {
//				field: "Actions",
//				width: 60,
//				title: "Actions",
//				sortable: false,
//				overflow: 'visible',
//				template: function (row, index, datatable) {
//					var dropup = (datatable.getPageSize() - index) <= 4 ? 'dropup' : '';
//					
//					return '\
//						<div class="dropdown ' + dropup + '">\
//							<a href="#" class="btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" data-toggle="dropdown">\
//                                <i class="la la-ellipsis-h"></i>\
//                            </a>\
//						  	<div class="dropdown-menu dropdown-menu-right">\
//						    	<a href="#" onclick="toggleModalEditDetails('+row.id+')" class="dropdown-item"><i class="la la-edit"></i> Edit Details</a>\
//						  	</div>\
//						</div>\
//												\<a onclick="handleRemoveBoqClick('+row.id+')" id="btn-remove-boq-'+row.id+'" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="Delete BOQ">\
//							<i class="la la-trash-o"></i>\
//						</a>\
//					';
//				}
//			}
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
		init: function () {
			demo();
		}
	
		
	};
}();




