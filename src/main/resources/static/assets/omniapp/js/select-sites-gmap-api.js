function initializeSitesGmap(projectId) {
	//var selectedProjectId = $("#select_project_new_operation").val();
	
	
	var map = new GMaps({
        div: '.sites_map_canvas',
        lat: 34.7615155,
        lng: 10.6630578,
        
    });
	
	$.ajax({
		type : "GET",
		url : '/set-selected-project-client-sites',
		async: false,
		data : 'projectId=' + projectId,
		success : function(response) {

			//var html_text = "";
			for (i = 0; i < response.length; i++) {
				//map.setCenter(0,0);
				  map.addMarker({
				        lat: response[i].latitude,
				        lng: response[i].longitude,
				        title: response[i].name,
				        id: response[i].id,
				        infoWindow: {
				            content: '<p>'+response[i].name+'</p>'
				          },
				       
				        click: function(e,id,title) {
				            if (console.log) console.log(e);
				            
				            $("#selected_site_new_operation").html($(this).attr('title'));
				            $('#selected_site_hidden_input').attr('value',$(this).attr('id'));
				            //alert('You clicked in this marker '+$(this).attr('id'));
				        }
				    });
			
		        }
			map.setZoom(5);
//					$("#sites_map_container").show();
//					$(".sites_map_canvas").show();
//					 
				
				
			
		},
		error : function(e) {
			alert('Error: services ' + e);
		}
	});
	
        
        
      }