//== Class definition

var DatatableChildRemoteDataDemo = function() {
  //== Private functions

  // demo initializer
  var demo = function() {

    var datatable = $('.m_datatable').mDatatable({
      // datasource definition
      data: {
        type: 'remote',
        source: {
          read: {
            url: '/get_services_all_tasks_for_current_project?id=2',
          },
        },
        pageSize: 10, // display 20 records per page
        serverPaging: true,
        serverFiltering: false,
        serverSorting: true,
      },

      // layout definition
      layout: {
        theme: 'default',
        scroll: false,
        height: null,
        footer: false,
      },

      // column sorting
      sortable: true,

      pagination: true,

      detail: {
        title: 'Load sub table',
        content: subTableInit,
      },

      search: {
        input: $('#generalSearch'),
      },

      // columns definition
      columns: [
        {
          field: 'id',
          title: '',
          sortable: false,
          width: 20,
          textAlign: 'center',
        }, {
          field: 'checkbox',
          title: '',
          template: '{{id}}',
          sortable: false,
          width: 20,
          textAlign: 'center',
          selector: {class: 'm-checkbox--solid m-checkbox--brand'},
        }, {
          field: 'name',
          title: 'Service',
          sortable: 'asc',
          // responsive: {hidden: 'md'}
        }, {
          field: 'Actions',
          width: 110,
          title: 'Actions',
          sortable: false,
          overflow: 'visible',
          template: function (row, index, datatable) {
            var dropup = (datatable.getPageSize() - index) <= 4 ? 'dropup' : '';
            return '\
						<div class="dropdown ' + dropup + '">\
							<a href="#" class="btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" data-toggle="dropdown">\
                                <i class="la la-ellipsis-h"></i>\
                            </a>\
						  	<div class="dropdown-menu dropdown-menu-right">\
						    	<a class="dropdown-item" href="#"><i class="la la-edit"></i> Edit Details</a>\
						    	<a class="dropdown-item" href="#"><i class="la la-leaf"></i> Update Status</a>\
						    	<a class="dropdown-item" href="#"><i class="la la-print"></i> Generate Report</a>\
						  	</div>\
						</div>\
						<a href="#" class="m-portlet__nav-link btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" title="Edit details">\
							<i class="la la-edit"></i>\
						</a>\
						<a href="#" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="Delete">\
							<i class="la la-trash"></i>\
						</a>\
					';
          },
        }],
    });

    function subTableInit(e) {
      $('<div/>').attr('id', 'child_data_ajax_' + e.data.id).appendTo(e.detailCell).mDatatable({
        data: {
          type: 'remote',
          source: {
            read: {
              url: '/get_tasks_by_service?serviceId='+e.data.id,
              headers: {'x-my-custom-header': 'some value', 'x-test-header': 'the value'},
              params: {
                // custom query params
                query: {
                  generalSearch: '',
                  CustomerID: e.data.RecordID,
                },
              },
            },
          },
          pageSize: 10,
          serverPaging: true,
          serverFiltering: false,
          serverSorting: true,
        },

        // layout definition
        layout: {
          theme: 'default',
          scroll: true,
          height: 300,
          footer: false,

          // enable/disable datatable spinner.
          spinner: {
            type: 1,
            theme: 'default',
          },
        },

        sortable: true,

        // columns definition
        columns: [
          {
            field: 'RecordID',
            title: '#',
            sortable: false,
            width: 20,
            responsive: {hide: 'xl'},
          }, {
            field: 'TaskCode',
            title: 'Code',
            template: function(row) {
              return '<span>' + row.TaskId + ' - ' + row.TaskName + '</span>';
            },
          }, {
            field: 'TaskName',
            title: 'Task Name',
            width: 100,
          }, {
            field: 'TaskStartDate',
            title: 'Start Date',
          }, {
            field: 'TaskEndDate',
            title: 'End Date',
          }, {
            field: 'TaskDuration',
            title: 'Duration',
            type: 'number',
          }, {
            field: 'TaskStatus',
            title: 'Status',
            // callback function support for column rendering
            template: function(row) {
              var status = {
                'ONGOING': {'title': 'Pending', 'class': 'm-badge--brand'},
                'COMPLETED': {'title': 'Success', 'class': ' m-badge--success'},
                 };
              return '<span class="m-badge ' + status[row.TaskStatus].class + ' m-badge--wide">' + status[row.TaskStatus].title + '</span>';
            },
          }, {
            field: 'TaskPriority',
            title: 'Priority',
            // callback function support for column rendering
            template: function(row) {
              var status = {
                'none': {'title': 'Online', 'state': 'danger'},
                'low': {'title': 'Retail', 'state': 'primary'},
                'medium': {'title': 'Direct', 'state': 'accent'},
                'high': {'title': 'Direct', 'state': 'accent'},
              };
              return '<span class="m-badge m-badge--' + status[row.TaskPriority].state + ' m-badge--dot"></span>&nbsp;<span class="m--font-bold m--font-' + status[row.TaskPriority].state + '">' +
                  status[row.TaskPriority].title + '</span>';
            },
          },],
      });
    }
  };

  return {
    //== Public functions
    init: function() {
      // init dmeo
      demo();
    },
  };
}();

