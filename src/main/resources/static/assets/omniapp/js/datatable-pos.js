//== Class definition


var DatatablePosJsonRemote = function () {
	// == Private functions

	
	// basic demo
	var demo = function () {
		var projectId = $('#selected_project_id').val();
		var datatable = $('#pos_datatable').mDatatable({
			// datasource definition
			data: {
		        type: 'remote',
		        source: {
		          read: {
		            // sample GET method
		            method: 'GET',
		            url: '/json-pos?id='+projectId,
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
				input: $('#generalSearchPos')
			},

			// columns definition
			columns: [
				{
				field: "number",
				title: "#",
				width: 200,
				sortable: true,
				selector: false,
				textAlign: 'center',
			}, 
			{
				field: "services",
				title: "Total Services",
				sortable: true,
				width: 100,
//				template: function(row) {
//					return '<a style="font-weight: 500;" href="#" onclick="toggleIssueFragment('+ row.id +')">'+row.name + '</a>';
//				}
			}
			, 
			{
				field: "price",
				title: "Total Price",
				sortable: true,
				width: 100,
//				template: function(row) {
//					return '<a style="font-weight: 500;" href="#" onclick="toggleIssueFragment('+ row.id +')">'+row.name + '</a>';
//				}
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




