//== Class definition

var projectsDatatableRemoteAjax = function() {
  //== Private functions

  // basic demo
  var demo = function() {

    var datatable = $('#projects_datatable').mDatatable({
      // datasource definition
      data: {
        type: 'remote',
        source: {
          read: {
            // sample GET method
            method: 'GET',
            url: '/get-all-projects',
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
            pageSizeSelect: [10, 20, 30, 50, 100],
          },
        },
      },

      search: {
        input: $('#projectSearch'),
      },

      // columns definition
      columns: [
        {
          field: 'id',
          title: '#',
          sortable: false, // disable sort for this column
          width: 40,
          selector: false,
          textAlign: 'center',
        }, {
          field: 'name',
          title: 'Project Name',
          // sortable: 'asc', // default sort
          filterable: false, // disable or enable filtering
          width: 200,
          // basic templating support for column rendering,
          template: function(row) {
        // callback function support for column rendering
        	  return '<a href="/project?id='+row.id+'">'+row.name + '</a>';
          },
        }, {
          field: 'client',
          title: 'Client',
          width: 100,
//          template: function(row) {
//            // callback function support for column rendering
//            return row.ShipCountry + ' - ' + row.ShipCity;
//          },
        }, {
          field: 'owner',
          title: 'Owner',
          width: 200,
        }, {
          field: 'country',
          title: 'Country',
          width: 100,
        }, {
          field: 'currency',
          title: 'Currency',
          type: 'number',
          width: 100,
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
          	  +'<div class="progress-bar m--bg-success" role="progressbar" style="width: '+row.percentage+';" aria-valuemin="0" aria-valuemax="100"></div>'
          	  +'</div>'
          	  +'<span style="font-weight:600;font-size:12px;float:right;" class="m-widget4__number m--font-info">'+row.percentage+'</span>'
          	  ;
            },
          },],
    });

    $('#m_form_client').on('change', function() {
      datatable.search($(this).val().toLowerCase(), 'client');
    });

   
    $('#m_form_client').selectpicker();

  };

  return {
    // public functions
    init: function() {
      demo();
    },
  };
}();


