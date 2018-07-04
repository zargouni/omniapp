//== Class definition


var DatatableUserTasksJsonRemote = function () {
	// == Private functions
	
	
	// basic demo
	var demo = function () {
		var userId =  $('#profile_user_id').val();
		var datatable = $('#user_tasks_datatable').mDatatable({
			// datasource definition
			data: {
		        type: 'remote',
		        source: {
		          read: {
		            // sample GET method
		            method: 'GET',
		            url: '/json-user-tasks?id='+userId,
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
				input: $('#generalSearchMyTasks')
			},

			// columns definition
			columns: [

			{
				field: "name",
				title: "Task Name",
				sortable: true,
				width: 300,
				template: function(row) {
					return '<a href="#" style="font-weight: 500;" onclick="handleOnTaskClick('+ row.id +')">'+row.name + '</a>';
				}
			}, 

			{
		        field: 'status',
		        title: 'Status',
		        width: 80,
		        template: function(row) {
		           var status = {
		              'COMPLETED': {'title': 'Closed', 'class': 'm-badge--danger'},
		              'ONGOING': {'title': 'Open', 'class': ' m-badge--success'},
		           };
		           return '<span class="m-badge ' + status[row.status].class + ' m-badge--wide">' + status[row.status].title + '</span>';
		         },
		         responsive: {visible: 'lg'}
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
				},
				responsive: {visible: 'lg'}
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
				},
				responsive: {visible: 'lg'}
			},

		{
            field: 'completionPercentage',
            title: 'Progress',
            // sortable: 'asc', // default sort
            filterable: false, // disable or enable filtering
            width: 100,
            // basic templating support for column rendering,
            template: function(row) {
          // callback function support for column rendering
          	  return '<div style="height:11px;width:100px !important;" class="progress m-progress--sm">'
          	  +'<div class="progress-bar m--bg-success" role="progressbar" style="width: '+row.completionPercentage+'%;" aria-valuemin="0" aria-valuemax="100">'
          	  
          	  +'<span style="font-weight:500;font-size:10px;float:right;" class="m-widget4__number m--font-secondary">'+row.completionPercentage+'%</span>'

          	  
          	  +'</div>'

          	  +'</div>'
          	  ;
            },
            responsive: {visible: 'lg'}
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
		init: function () {
			demo();
		}
	
		
	};
}();




