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
				width: 250,
				template: function(row) {
					return '<a href="#" style="font-weight: 500;" onclick="toggleTaskFragment('+ row.id +')">'+row.name + '</a>';
				}
			}, {
				field: "users",
				title: "Assignee",
				sortable: true,
				width: 100,
				template: function(row) {
					if(row.users != "")
						return '<span style="font-weight: 400;">'+row.users + '</span>';
					return '<span style="font-weight: 400;">Unassigned</span>';
				}
			},{
		        field: 'status',
		        title: 'Status',
		        width: 100,
		        template: function(row) {
		           var status = {
		              'COMPLETED': {'title': 'Closed', 'class': 'm-badge--danger'},
		              'ONGOING': {'title': 'Open', 'class': ' m-badge--success'},
		           };
		           return '<span class="m-badge ' + status[row.status].class + ' m-badge--wide">' + status[row.status].title + '</span>';
		         },
		    } ,{
				field: "startDate",
				title: "Start Date",
				type: 'date',
				format: 'DD MMMM YYYY',
				sortable: true,
				width: 100,
				template: function(row){
					if(row.startDate != "01 February 1970")
						return '<span style="font-weight:400;font-size:14px;">'+row.startDate+'</span>'
					return '<span style="font-weight:400;font-size:14px;">Unplanned</span>';
				}
			}, {
				field: "endDate",
				title: "Due Date",
				type: 'date',
				format: 'DD MMMM YYYY',
				sortable: true,
				width: 100,
				responsive: {visible: 'lg'},
				template: function(row){
					if(row.endDate != "01 February 1970")
						return '<span style="font-weight:400;font-size:14px;">'+row.endDate+'</span>'
					return '<span style="font-weight:400;font-size:14px;">Unplanned</span>';
				}
			},{
				field: "completedOn",
				title: "Completed On",
				type: 'date',
				format: 'DD MMMM YYYY',
				sortable: true,
				width: 100,
				template: function(row) {
			           if(row.completedOn != null)
			        	   return '<span style="font-weight:400;font-size:14px;">'+row.completedOn+'</span>';
			           return '-';
			         },
		},
		{
            field: 'completionPercentage',
            title: 'Progress',
            // sortable: 'asc', // default sort
            filterable: false, // disable or enable filtering
            width: 200,
            // basic templating support for column rendering,
            template: function(row) {
          // callback function support for column rendering
          	  return '<div style="width:200px !important;" class="progress m-progress--sm">'
          	  +'<div class="progress-bar m--bg-success" role="progressbar" style="width: '+row.completionPercentage+'%;" aria-valuemin="0" aria-valuemax="100"></div>'
          	  +'</div>'
          	  +'<span style="font-weight:600;font-size:12px;float:right;" class="m-widget4__number m--font-info">'+row.completionPercentage+'%</span>'
          	  ;
            },
          }
		//{
//				field: "priority",
//				title: "Priority",
//				sortable: true,
//				width: 100,
//				responsive: {visible: 'lg'},
//				template: function(row) {
//			           var status = {
//			        	  'none': {'title': 'None', 'class': 'm-badge--metal'},
//			              'low': {'title': 'Low', 'class': 'm-badge--metal'},
//			              'medium': {'title': 'Medium', 'class': ' m-badge--brand'},
//			              'high': {'title': 'High', 'class': ' m-badge--danger'},
//			           };
//			           return '<span class="m-badge ' + status[row.priority].class + ' m-badge--wide">' + status[row.priority].title + '</span>';
//			         },
//			}
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




