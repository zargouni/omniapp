//== Class definition


var DatatableUsersRemote = function () {
	// == Private functions
	
	
	// basic demo
	var demo = function () {
		var datatable = $('#users_datatable').mDatatable({
			// datasource definition
			data: {
		        type: 'remote',
		        source: {
		          read: {
		            // sample GET method
		            method: 'GET',
		            url: '/get-all-app-users',
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
				input: $('#generalSearchUsers')
			},

			// columns definition
			columns: [{
				field: "picture",
				title: "",
				sortable: false,
				width: 70,
				template: function(row) {
					return'<img style="height: 41px; width: 41px;" src="'+row.picture+'" class="m--img-rounded m--marginless m--img-centered">';
				}
			},{
					field: "name",
					title: "Name",
					sortable: true,
					width: 250,
					template: function(row) {
						return '<a style="font-weight: 500;" href="/profile?id='+row.id+'">'+row.name + '</a>';
					}
				},{
					field: "username",
					title: "Username",
					sortable: false,
					width: 100,
					responsive: {visible: 'lg'},
					template: function(row){
						return '<span style="font-weight:400;font-size:14px;">'+row.username+'</span>'
					}
				}, {
			        field: 'email',
			        title: 'Email',
			        width: 300,
			        template: function(row) {
			        	return '<span style="font-weight:400;font-size:14px;">'+row.email+'</span>'
					},
			    } ,{
					field: "registerDate",
					title: "Register Date",
					sortable: true,
					width: 250,
					template: function(row) {
						return row.registerDate ;
					}
				}, {
					field: "role",
					title: "Role",
					sortable: true,
					width: 100,
					responsive: {visible: 'lg'},
					template: function(row){
						return '<span style="font-weight:400;font-size:14px;">'+row.role+'</span>'
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




