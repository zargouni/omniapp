//== Class definition


var DatatableIssuesJsonRemote = function () {
	// == Private functions

	
	// basic demo
	var demo = function () {
		var projectId = $('#selected_project_id').val();
		var datatable = $('#issues_datatable').mDatatable({
			// datasource definition
			data: {
		        type: 'remote',
		        source: {
		          read: {
		            // sample GET method
		            method: 'GET',
		            url: '/json-issues?id='+projectId,
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
				input: $('#generalSearchIssues')
			},

			// columns definition
			columns: [
//				{
//				field: "id",
//				title: "#",
//				width: 20,
//				sortable: false,
//				selector: false,
//				textAlign: 'center',
//			}, 
			{
				field: "name",
				title: "Issue",
				sortable: true,
				width: 200,
				template: function(row) {
					return '<a style="font-weight: 500;" href="#" onclick="toggleIssueFragment('+ row.id +')">'+row.name + '</a>';
				}
			},{
				field: "operationName",
				title: "Operation",
				sortable: true,
				width: 100,
				template: function(row) {
					return '<a style="font-weight: 500;" href="#" onclick="toggleOperationFragment('+ row.operation_id +')">'+row.operationName + '</a>';
				}
			},{
				field: "site",
				title: "Site",
				sortable: true,
				width: 100,
				template: function(row) {
					return '<span style="font-weight: 500;">'+row.site + '</span>';
				}
			},{
				field: "creationDate",
				title: "Created",
				type: 'date',
				format: 'DD MMMM YYYY',
				sortable: true,
				width: 100,
				responsive: {visible: 'lg'},
				template: function(row){
					return '<span style="font-weight:400;font-size:14px;">'+row.creationDate+'</span>'
				}
			},{
				field: "owners",
				title: "Assignee",
				sortable: true,
				width: 100,
				responsive: {visible: 'lg'},
				template: function(row){
					return '<span style="font-weight:400;font-size:14px;">'+row.owners+'</span>'
				}
			}, {
		        field: 'status',
		        title: 'Status',
		        width: 70,
		        template: function(row) {
		           var status = {
		              "OPEN": {'title': 'Open', 'class': 'm-badge--info'},
		              "IN_PROGRESS": {'title': 'In Progress', 'class': ' m-badge--warning'},
		              "TO_BE_TESTED": {'title': 'To Be Tested', 'class': ' m-badge--success'},
		              "CLOSED": {'title': 'Closed', 'class': ' m-badge--danger'},
		           };
		           return '<span style="margin: 0 auto;" class="m-badge ' + status[row.status].class + ' m-badge--wide">' + status[row.status].title + '</span>';
		         },
		    } , {
				field: "endDate",
				title: "Due Date",
				type: 'date',
				format: 'DD MMMM YYYY',
				sortable: true,
				width: 100,
				responsive: {visible: 'lg'},
				template: function(row){
					return '<span style="font-weight:400;font-size:14px;">'+row.endDate+'</span>'
				}
			}, {
				field: "severity",
				title: "Severity",
				sortable: true,
				width: 100,
				responsive: {visible: 'lg'},
				template: function(row) {
			           var status = {
			              "minor": {'title': 'Minor', 'class': 'm-badge--info'},
			              "major": {'title': 'Major', 'class': ' m-badge--warning'},
			              "critical": {'title': 'Critical', 'class': ' m-badge--danger'},
			           };
			           return '<span style="margin: 0 auto;" class="m-badge ' + status[row.severity].class + ' m-badge--wide">' + status[row.severity].title + '</span>';
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

function toggleModalEditOperationDetails(id){
	$('#m_quick_sidebar_add_toggle').click();
	$('#m_quick_sidebar_add_tabs').children().hide();
	
	$('#quick_sidebar_add_tabs_content').children().hide();
	$('#quick_sidebar_edit_operation_nav').show();
	$('#m_quick_sidebar_add_tabs_edit_operation').show();
	
//	populateOperationDetails(id);
//	populateCheckboxPopoversEditBoq();
//	$('#button_update_boq').attr('onClick','handleUpdateBoq('+id+')');
//	$('#edit-boq-details').modal();
}


