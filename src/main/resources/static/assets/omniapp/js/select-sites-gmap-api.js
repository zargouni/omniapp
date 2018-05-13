function initializeSitesGmap(projectId) {
		var map = new GMaps({
        div: '#sites_map_canvas_sidebar',
        lat: 34.7615155,
        lng: 10.6630578,
        
        
    });
	if($('#sites_map_container').is(':hidden')){
		$('#sites_map_canvas_sidebar').attr("style","position: absolute; top: 20%; right: 0; bottom: 0; left: 0;");
		$('#sites_map_container').attr("style","width:100%; height:250px;position: relative;");
	}
	
	$.ajax({
		type : "GET",
		url : '/set-selected-project-client-sites-by-nature',
		async: false,
		data : 'projectId=' + projectId,
		success : function(response) {

			
			var selectSiteOptions = "";
			for (i = 0; i < response.length; i++) {
				//map.setCenter(0,0);
				  map.addMarker({
				        lat: response[i].latitude,
				        lng: response[i].longitude,
				        title: response[i].name,
				        id: response[i].id,
				     			       
				        click: function(e,id,title) {
				            if (console.log) console.log(e);
				            
				            for (i in map.markers){
								// map.removeMarkers();
					        	map.markers[i].setIcon();
				
				            }
				            $(this).attr('icon','https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/48/Map-Marker-Marker-Outside-Chartreuse.png');
				            $('#select_site_new_operation').val($(this).attr('id'));
				            // find('option[value='+$(this).attr('id')+']').attr('selected',true);
				            $('#select_site_new_operation').selectpicker('refresh');
				        }
				    });
				  selectSiteOptions += '<option data-subtext="'+response[i].natures+'" value="'+response[i].id+'">'+response[i].name+'</option>';
		        }
			map.setZoom(5);
//					$("#sites_map_container").show();
//					$(".sites_map_canvas").show();
//					 
			$('#select_site_new_operation').selectpicker({
				width: '100%',
				
			});
			$('#select_site_new_operation').html(selectSiteOptions);
			$('#select_site_new_operation').selectpicker('refresh');
			$('#select_site_new_operation').on('change',function(){
				var selectedSite = $('#select_site_new_operation').val();
				for (i in map.markers){
					// map.removeMarkers();
		        	map.markers[i].setIcon();

			        if(map.markers[i].id == selectedSite){
			        	map.markers[i].setIcon('https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/48/Map-Marker-Marker-Outside-Chartreuse.png');
			        }
			    }
			});
			
			
			
		},
		error : function(e) {
			alert('Error: services ' + e);
		}
	});
	
        
        
      }