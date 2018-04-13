//== Class definition
var DatatableChildDataLocalDemo = function() {
  // == Private functions

  var subTableInit = function(e) {
    $('<div/>').attr('id', 'child_data_local_' + e.data.RecordID).appendTo(e.detailCell).mDatatable({
      data: {
        type: 'local',
        source: e.data.Orders,
        pageSize: 10,
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
          field: 'OrderID',
          title: 'Order ID',
          sortable: false,
        }, {
          field: 'ShipCountry',
          title: 'Country',
          width: 100,
        }, {
          field: 'ShipAddress',
          title: 'Ship Address',
        }, {
          field: 'ShipName',
          title: 'Ship Name',
        }, {
          field: 'OrderDate',
          title: 'Order Date',
        }, {
          field: 'TotalPayment',
          title: 'Total Payment',
        }, {
          field: 'Status',
          title: 'Status',
          // callback function support for column rendering
          template: function(row) {
            var status = {
              1: {'title': 'Pending', 'class': 'm-badge--brand'},
              2: {'title': 'Delivered', 'class': ' m-badge--metal'},
              3: {'title': 'Canceled', 'class': ' m-badge--primary'},
              4: {'title': 'Success', 'class': ' m-badge--success'},
              5: {'title': 'Info', 'class': ' m-badge--info'},
              6: {'title': 'Danger', 'class': ' m-badge--danger'},
              7: {'title': 'Warning', 'class': ' m-badge--warning'},
            };
            return '<span class="m-badge ' + status[row.Status].class + ' m-badge--wide">' + status[row.Status].title + '</span>';
          },
        }, {
          field: 'Type',
          title: 'Type',
          // callback function support for column rendering
          template: function(row) {
            var status = {
              1: {'title': 'Online', 'state': 'danger'},
              2: {'title': 'Retail', 'state': 'primary'},
              3: {'title': 'Direct', 'state': 'accent'},
            };
            return '<span class="m-badge m-badge--' + status[row.Type].state + ' m-badge--dot"></span>&nbsp;<span class="m--font-bold m--font-' + status[row.Type].state + '">' +
                status[row.Type].title + '</span>';
          },
        },],
    });
  };

  // demo initializer
  var mainTableInit = function() {

    var dataJSONArray = JSON.parse('[{"RecordID":1,"TaskName":"Tommie","TaskPercentage":"Pee","TaskStartDate":"Roodel","TaskEndDate":"tpee0@slashdot.org","TaskDuration":"103-891-3486","TaskStatus":4,"TaskPriority":1,"ss":[{"OrderID":"41250-166","ShipCountry":"FR","ShipAddress":"5 Rutledge Court","ShipName":"Rogahn-Shanahan","OrderDate":"3/7/2017","TotalPayment":"$591994.23","Status":5,"Type":1},{"OrderID":"0078-0595","ShipCountry":"CN","ShipAddress":"953 Schlimgen Park","ShipName":"Hilpert-Sanford","OrderDate":"5/12/2017","TotalPayment":"$79774.93","Status":1,"Type":1},{"OrderID":"47593-443","ShipCountry":"BY","ShipAddress":"46925 Memorial Park","ShipName":"Brakus and Sons","OrderDate":"2/12/2017","TotalPayment":"$1095029.28","Status":1,"Type":1},{"OrderID":"50114-5236","ShipCountry":"NZ","ShipAddress":"1420 Mockingbird Drive","ShipName":"Beer-Harris","OrderDate":"6/6/2017","TotalPayment":"$778690.72","Status":5,"Type":3},{"OrderID":"36987-2826","ShipCountry":"PL","ShipAddress":"3995 Huxley Court","ShipName":"Kling, Miller and Quitzon","OrderDate":"9/1/2017","TotalPayment":"$773995.02","Status":5,"Type":2},{"OrderID":"62750-006","ShipCountry":"ID","ShipAddress":"2064 Dennis Parkway","ShipName":"Lang, Kohler and Considine","OrderDate":"9/21/2017","TotalPayment":"$830550.45","Status":5,"Type":2},{"OrderID":"59779-597","ShipCountry":"IR","ShipAddress":"32 Golf Course Parkway","ShipName":"Jaskolski-Hilll","OrderDate":"4/4/2017","TotalPayment":"$754685.32","Status":3,"Type":3},{"OrderID":"59762-3743","ShipCountry":"HT","ShipAddress":"76 Anthes Hill","ShipName":"Reynolds Group","OrderDate":"1/23/2017","TotalPayment":"$295435.66","Status":2,"Type":1},{"OrderID":"64942-1114","ShipCountry":"ID","ShipAddress":"7511 Mayfield Avenue","ShipName":"Purdy and Sons","OrderDate":"12/1/2016","TotalPayment":"$636911.04","Status":6,"Type":2},{"OrderID":"13537-505","ShipCountry":"KZ","ShipAddress":"36303 Esch Parkway","ShipName":"Reinger, Howe and Kertzmann","OrderDate":"1/31/2016","TotalPayment":"$753691.79","Status":4,"Type":1},{"OrderID":"16781-426","ShipCountry":"SE","ShipAddress":"507 Columbus Lane","ShipName":"Carter, Gibson and Kassulke","OrderDate":"10/26/2017","TotalPayment":"$873190.14","Status":2,"Type":2},{"OrderID":"60512-1008","ShipCountry":"ID","ShipAddress":"8 Jana Lane","ShipName":"Rutherford and Sons","OrderDate":"1/10/2017","TotalPayment":"$242894.68","Status":3,"Type":1},{"OrderID":"0456-0461","ShipCountry":"CN","ShipAddress":"5127 Roxbury Trail","ShipName":"Johnson Inc","OrderDate":"12/10/2017","TotalPayment":"$328850.50","Status":5,"Type":3},{"OrderID":"63304-098","ShipCountry":"GR","ShipAddress":"54627 Randy Lane","ShipName":"Johnston, Veum and Funk","OrderDate":"12/11/2016","TotalPayment":"$278247.03","Status":3,"Type":2},{"OrderID":"64092-317","ShipCountry":"CN","ShipAddress":"292 Rusk Lane","ShipName":"Bode, Zboncak and Reichel","OrderDate":"4/10/2016","TotalPayment":"$798173.38","Status":2,"Type":2},{"OrderID":"36987-1483","ShipCountry":"CU","ShipAddress":"2225 Saint Paul Junction","ShipName":"Dach, Haag and Koss","OrderDate":"2/7/2017","TotalPayment":"$1147799.38","Status":4,"Type":2},{"OrderID":"68084-814","ShipCountry":"ID","ShipAddress":"0 Sheridan Avenue","ShipName":"Little-O\'Hara","OrderDate":"11/24/2016","TotalPayment":"$394051.79","Status":6,"Type":1},{"OrderID":"42023-131","ShipCountry":"BR","ShipAddress":"4238 Roth Drive","ShipName":"Boehm LLC","OrderDate":"4/23/2016","TotalPayment":"$300684.31","Status":6,"Type":3},{"OrderID":"14290-350","ShipCountry":"CN","ShipAddress":"41950 Troy Point","ShipName":"Windler, Larkin and Collier","OrderDate":"4/17/2017","TotalPayment":"$467794.40","Status":4,"Type":1}]}]');
    var datatable = $('.m_datatable').mDatatable({
      // datasource definition
      data: {
        type: 'local',
        source: dataJSONArray,
        pageSize: 10, // display 20 records per page
      },

      // layout definition
      layout: {
        theme: 'default',
        scroll: false,
        height: null,
        footer: false,
      },

      sortable: true,

      filterable: false,

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
          field: 'RecordID',
          title: '',
          sortable: false,
          width: 20,
          textAlign: 'center' // left|right|center,
        }, {
          field: 'TaskName',
          title: 'Task',
        },{
        	field: 'TaskPercentage',
        	title: '% Complete',
        }, {
          field: 'TaskStartDate',
          title: 'Start Date',
        }, {
          field: 'TaskEndDate',
          title: 'End Date',
        }, {
          field: 'TaskDuration',
          title: 'Duration',
        },
        {
          field: 'TaskStatus',
          title: 'Status',
          // callback function support for column rendering
          template: function(row) {
            var status = {
              1: {'title': 'Pending', 'class': 'm-badge--brand'},
              2: {'title': 'Delivered', 'class': ' m-badge--metal'},
              3: {'title': 'Canceled', 'class': ' m-badge--primary'},
              4: {'title': 'Success', 'class': ' m-badge--success'},
              5: {'title': 'Info', 'class': ' m-badge--info'},
              6: {'title': 'Danger', 'class': ' m-badge--danger'},
              7: {'title': 'Warning', 'class': ' m-badge--warning'},
            };
            return '<span class="m-badge ' + status[row.TaskStatus].class + ' m-badge--wide">' + status[row.TaskStatus].title + '</span>';
          },
        }, {
          field: 'TaskPriority',
          title: 'Priority',
          // callback function support for column rendering
          template: function(row) {
            var status = {
              1: {'title': 'None', 'state': 'danger'},
              2: {'title': 'Low', 'state': 'primary'},
              3: {'title': 'Medium', 'state': 'accent'},
              4: {'title': 'High', 'state': 'accent'}
            };
            return '<span class="m-badge m-badge--' + status[row.TaskPriority].state + ' m-badge--dot"></span>&nbsp;<span class="m--font-bold m--font-' + status[row.TaskPriority].state + '">' +
                status[row.TaskPriority].title + '</span>';
          },
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

  };

  return {
    // == Public functions
    init: function() {
      // init dmeo
      mainTableInit();
    },
  };
}();

jQuery(document).ready(function() {
  
});