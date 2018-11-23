//== Class definition


var DatatableOperationsJsonRemote = function () {
	// == Private functions
	
	// basic demo
	var demo = function () {
		var projectId = $('#selected_project_id').val();
		var datatable = $('#operations_datatable').mDatatable({
			// datasource definition
			data: {
		        type: 'remote',
		        source: {
		          read: {
		            // sample GET method
		            method: 'GET',
		            url: '/json-operations?id='+projectId,
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
		        pageSize: 5,
		        serverPaging: false,
		        serverFiltering: false,
		        serverSorting: false,
		      },

		      // layout definition
		      layout: {
		        scroll: true,
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
		            pageSizeSelect: [5, 10, 20, 40, 50],
		          },
		        },
		      },

			search: {
				input: $('#generalSearchOperations')
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
				title: "Name",
				sortable: true,
				width: 250,
				template: function(row) {
					return '<a style="font-weight: 500;" href="#" onclick="toggleOperationFragment('+ row.id +')">'+row.name + '</a>';
				}
			},{
	            field: 'percentage',
	            title: 'Progress',
	            // sortable: 'asc', // default sort
	            filterable: false, // disable or enable filtering
	            width: 200,
	            // basic templating support for column rendering,
	            template: function(row) {
	          // callback function support for column rendering
	          	  return '<div style="width:200px !important;" class="progress m-progress--sm">'
	          	  +'<div class="progress-bar m--bg-success" role="progressbar" style="width: '+row.percentage+'%;" aria-valuemin="0" aria-valuemax="100"></div>'
	          	  +'</div>'
	          	  +'<span style="font-weight:600;font-size:12px;float:right;" class="m-widget4__number m--font-info">'+row.percentage+'%</span>'
	          	  ;
	            },
	          }, {
		        field: 'status',
		        title: 'Status',
		        width: 100,
		        template: function(row) {
		           var status = {
		              "Open": {'title': 'Open', 'class': 'm-badge--info'},
		              "Overdue": {'title': 'Overdue', 'class': ' m-badge--danger'},
		              "Closed": {'title': 'Closed', 'class': ' m-badge--success'},
		              "In Progress": {'title': 'In Progress', 'class': ' m-badge--warning'}
		           };
		           return '<span style="margin: 0 auto;" class="m-badge ' + status[row.status].class + ' m-badge--wide">' + status[row.status].title + '</span>';
		         },
		    } ,{
				field: "startDate",
				title: "Start Date",
				type: 'date',
				format: 'DD MMMM YYYY',
				sortable: true,
				width: 100,
				responsive: {visible: 'lg'},
				template: function(row){
					return '<span style="font-weight:400;font-size:14px;">'+row.startDate+'</span>'
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
					return '<span style="font-weight:400;font-size:14px;">'+row.endDate+'</span>'
				}
			}, {
				field: "price",
				title: "Total Price",
				type: 'number',
				sortable: true,
				width: 100,
				responsive: {visible: 'lg'},
				template: function(row){
					return '<span style="font-weight:600;font-size:14px;color: #f4516c;">'+row.price+' '+row.currency+'</span>';
				}
			},{
				field: "serviceCount",
				title: "Services",
				type: 'number',
				sortable: true,
				width: 100,
				responsive: {visible: 'lg'},
				template: function(row){
					return '<span style="font-weight:600;font-size:14px;color: #36a3f7;">'+row.serviceCount+'</span>'
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


