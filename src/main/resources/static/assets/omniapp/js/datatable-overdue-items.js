//== Class definition


var DatatableOverdueItemsRemote = function () {
	// == Private functions
	
	
	// basic demo
	var demo = function () {
		// var userId = $('#profile_user_id').val();
		var datatable = $('#overdue_items_datatable').mDatatable({
			// datasource definition
			data: {
		        type: 'remote',
		        source: {
		          read: {
		            // sample GET method
		            method: 'GET',
		            url: '/get-all-user-overdue-items',
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
				input: $('#generalSearchOverdueItems')
			},

			// columns definition
			columns: [{
				field: "nature",
				title: "Nature",
				sortable: true,
				width: 80,
				template: function(row) {
					if(row.nature == "task")
						return '<span style="width: 100%; height: 20px; padding: 5px 0 0 0;" class="badge badge-info">Task</span>';
					if(row.nature == "issue")
						return '<span style="width: 100%; height: 20px; padding: 5px 0 0 0;" class="badge badge-danger">Issue</span>';
				}
			},{
					field: "name",
					title: "Name",
					sortable: true,
					width: 250,
					template: function(row) {
						if(row.nature == "task")
						return '<a style="font-weight: 500;" href="#" onclick="handleNotificationClick('+ row.id +')">'+row.name + '</a>';
						if(row.nature == "issue")
							return '<a style="font-weight: 500;" href="#" onclick="handleIssueNotificationClick('+ row.id +')">'+row.name + '</a>';
					}
				},{
					field: "creationDate",
					title: "Created",
					type: 'date',
					sortable: true,
					width: 100,
					responsive: {visible: 'lg'},
					template: function(row){
						return '<span style="font-weight:400;font-size:14px;">'+row.creationDate+'</span>'
					}
				}, {
					field: "endDate",
					title: "Due Date",
					type: 'date',
					sortable: true,
					width: 100,
					responsive: {visible: 'lg'},
					template: function(row){
						return '<span style="font-weight:400;font-size:14px;">'+row.endDate+'</span>'
					}
				}, {
					field: "status",
					title: "Status",
					sortable: true,
					width: 100,
					responsive: {visible: 'lg'},
					template: function(row) {
						var status;
						if(row.nature == "task"){
							status = {
						              "ONGOING": {'title': 'Open', 'class': 'm-badge--info'},
						              "COMPLETED": {'title': 'Closed', 'class': ' m-badge--danger'}
						           };	
						}
						if(row.nature == "issue"){
							status = {
						              "OPEN": {'title': 'Open', 'class': 'm-badge--info'},
						              "CLOSED": {'title': 'Closed', 'class': ' m-badge--danger'},
						              "IN_PROGRESS": {'title': 'In progress', 'class': ' m-badge--warning'},
						              "TO_BE_TESTED": {'title': 'To be tested', 'class': ' m-badge--success'},
						           };	
						}
				           return '<span style="margin: 0 auto;" class="m-badge ' + status[row.status].class + ' m-badge--wide">' + status[row.status].title + '</span>';
				         },
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




