function toggleOperationFragment(operationId){
	var map_div = "#operation_map";	

	var coordinates = populateOperationSiteMap(operationId);
//	console.log("lat: "+coordinates[0]);
//	console.log("long: "+coordinates[1]);
	
	$("#operations-fragment").hide();
	populateServicesTabOperationFragment(operationId);
	populateOperationDetails(operationId);
	$("#operation-fragment").show();
	
	
	$('#operation_map').hide('fast');
	
	var map = new GMaps({
        div: map_div,
        lat: 20.203036, 
        lng: -24.834370,
        scrollwheel: false,
    });
	map.addMarker({
        lat: coordinates[0],
        lng: coordinates[1],
        title: coordinates[2],
        
//        click: function(e,id,title) {
//       	  
//	            if (console.log) console.log(e);
//	            for (i in map.markers){
//		        	map.markers[i].setIcon();
//	
//	            }
//	            $(this).attr('icon','https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/48/Map-Marker-Marker-Outside-Chartreuse.png');
//	            $('#select_site_client_'+clientId).val($(this).attr('id'));
//	            $('#select_site_client_'+clientId).selectpicker('refresh');
//				
//        }
    });
	
	map.setZoom(4);
	map.refresh();
	$('#operation_map').show('fast');
}

function populateOperationSiteMap(operationId){

	var coordinates = [];
	$.ajax({
		type : "GET",
		url : '/get-operation-details',
		data : 'id=' + operationId,
		async: false,
		success : function(response) {
			coordinates.push(response.site.latitude);
			coordinates.push(response.site.longitude);
			coordinates.push(response.site.name);
		},
		error : function(e) {
			alert('Error: operation site ' + e);
		}
	});
	return coordinates;
	
}

function populateOperationDetails(operationId){
	
	$.ajax({
		type : "GET",
		url : '/get-operation-details',
		data : 'id=' + operationId,
		success : function(response) {
			$("#operation_fragment_header_name").html(response.name);
			$('#operation_project_nav_link').attr('href','/project?id='+response.project.id);
			$('#operation_project_nav_text').html(response.project.name)
		},
		error : function(e) {
			alert('Error: services ' + e);
		}
	});

}

function toggleAddNewServiceSidebar(operationId){
	$('#m_quick_sidebar_add_toggle').click();
	$('#quick_sidebar_new_service_nav').click();
}

function populateServicesTabOperationFragment(operationId){
	$.ajax({
		type : "GET",
		url : '/get-operation-services',
		data : 'id=' + operationId,
		success : function(response) {
			if(response.length == 0){
				$("#operation_services_widget").hide();
				$('#operation_add_new_service').attr('onClick','toggleAddNewServiceSidebar('+operationId+')')
				$('#operation_empty_services').show();
			}else{
				var html_text = "";
				for (i = 0; i < response.length; i++) {
					html_text += '<div class="m-widget4__item">'
							+	'<div class="m-widget4__ext">'
							+		'<span class="m-widget4__icon m--font-brand"> <i'
							+			' class="flaticon-more"></i>'
							+		'</span>'
							+	'</div>'
							+	'<div class="m-widget4__info">'
							+		'<span class="m-widget4__text">'+response[i].name+' <span class="m-widget4__number m--font-info"> ('+response[i].category+') </span>'
							+			'</span>'
							+			'<span style="font-size:11px;">created on: <span style="color: #36a3f7;">'+response[i].creationDate+'</span></span>'

							+	'</div>'
							+	'<div class="m-widget4__ext">'
							+		'<span title="'+response[i].taskCount+' Tasks" class="m-widget4__number m--font-info"> '+response[i].taskCount+'</span>'
							+	'</div>'
							+ '</div>';
					
					}
				$('#operation_empty_services').hide('fast');
				$("#operation_services_widget").html(html_text);
				$("#operation_services_widget").show();
			}
		},
		error : function(e) {
			alert('Error: services ' + e);
		}
	});

}