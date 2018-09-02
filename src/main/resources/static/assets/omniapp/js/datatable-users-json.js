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
					width: 200,
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
			        width: 200,
			        template: function(row) {
			        	return '<span style="font-weight:400;font-size:14px;">'+row.email+'</span>'
					},
			    } ,{
					field: "registerDate",
					title: "Register Date",
					sortable: true,
					width: 100,
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
				}, {
					field: "status",
					title: "Account status",
					sortable: true,
					width: 100,
					responsive: {visible: 'lg'},
					template: function(row){
						switch (row.status){
						case true:
							return '<span style="font-weight:400;font-size:14px;">Enabled</span>';
							break;	
						}
						return '<span style="font-weight:400;font-size:14px;">Disabled</span>';
					}
				},
				{
					field: "Actions",
					width: 80,
					title: "Actions",
					sortable: false,
					overflow: 'visible',
					template: function (row, index, datatable) {
						var dropup = (datatable.getPageSize() - index) <= 4 ? 'dropup' : '';
						var activateAction = "";
						if(!row.status){
							activateAction += '<a onclick="activateUserAccount('+row.id+')"  ' 
							+' class="m-portlet__nav-link btn m-btn m-btn--hover-success m-btn--icon m-btn--icon-only m-btn--pill" title="Activate Account"> '
							+'	<i class="la la-check"></i>'
							+' </a>';
						}
						
						return '\
							<div class="dropdown ' + dropup + '">\
								<a href="#" class="btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" data-toggle="dropdown">\
	                                <i class="la la-ellipsis-h"></i>\
	                            </a>\
							  	<div class="dropdown-menu dropdown-menu-right">\
							    	<a href="#" onclick="toggleModalEditUserDetails('+row.id+',\''+row.role+'\')" class="dropdown-item"><i class="la la-edit"></i> Edit Details</a>\
							  	</div>\
							</div>'
						+	activateAction 						
						;
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

function populateUserRoles(){
	$.ajax({
		type : "GET",
		url : '/json-roles',
		success : function(response) {
			var arr = [];
			for (i = 0; i < response.length; i++) {
						arr.push({
							 id: response[i].name,
				             text: response[i].name
					   	});

			}
			
			$("#user-role-select").select2({
				data: arr,
		         
	            width: '100%'
			});
			
		},
		error : function(e) {
			alert('Error: user roles ' + e);
		}
	});
}

function activateUserAccount(userId){
	$.ajax({
		type : "POST",
		url : '/activate-user-account',
		data: "userId="+userId,
		success : function(response) {
			if( response.status=="SUCCESS" ){
				$('#users_datatable').mDatatable('reload');
			}
		},
		error : function(e) {
			alert('Error: user account activation ' + e);
		}
	});
}

function toggleModalEditUserDetails(userId,role){
	populateUserRoles();
	$('#user-role-select').val(role); // Select the option with a value of '1'
	$('#user-role-select').trigger('change'); // Notify any JS components that the value changed
	$('#modal_update_user_details').modal('show');
	$('#button_update_user_details').attr('onClick','doUpdateUserRole('+userId+')');
	
}

function doUpdateUserRole(userId){
	var role = $('#user-role-select').val();
	$.ajax({
		type: "POST",
		url : '/update-user-role',
		data: "userId="+userId+"&role="+role,
		success : function(response) {
			if( response.status=="SUCCESS" ){
				$('#users_datatable').mDatatable('reload');
				$('#modal_update_user_details').modal('hide');
				toastr.success("User role updated successfully");
			}else{
				toastr.error("Failed to update user role");
			}
		},
		error : function(e) {
			alert('Error: user role update ' + e);
		}
	});
}




