function populateClientsUI(){
	$('#clients_container_even_row').html('');
	$('#clients_container_odd_row').html('');
	$.ajax({
		type : "GET",
		url : '/get-all-clients-details-json',
		async: false,
		success : function(response) {
			response.length = 0;
			if(response.length == 0){
				$('#clients_container').html('<div style="display: block; margin: auto; vertical-align: middle;">'
											+'	<table width="100%" align="center" border="0" cellpadding="0"'
											+' cellspacing="0">'
											+'<tbody>'
											+'<tr>'
											+' <td align="center"><div style="border-color: #e6e6e6 !important;"'
											+'		class="emptydashboardbox omnia fonticon40">'
											+'		<div class="m-demo-icon__preview">'
											+'			<i style="color: #e6e6e6; font-size: 40px;"'
											+'				class="flaticon-tool"></i>'
											+'		</div>'
											+'	</div>'
											+'</td>'
											+'</tr>'
											+'<tr>'
											+'	<td align="center" height="110px">'
											+'		<div>'
											+'			<span style="color: #a6a6a6;" class="col777 pt12 lh25">Nothing'
											+'				to show here</span>'
											+'		</div>'
											+'	</td>'
											+'	</tr>'
											+'</tbody>'
											+'</table>'
											+'</div>');
			}else
			for (i = 0; i < response.length; i++) {
				var html_text = '<div '
					+' class="m-portlet m-portlet--tabs m-portlet--creative m-portlet--first m-portlet--bordered-semi">'
					+'<div class="m-portlet__head">'
						+'<div class="m-portlet__head-caption">'
							+'<div class="m-portlet__head-title">'
							+	'<span class="m-portlet__head-icon m--hide"> <i'
							+		' class="flaticon-statistics"></i>'
							+	'</span>'
							+'<div class="m-portlet__head-tools">'
							+'<ul class="m-portlet__nav ">'
//							+'	<li class="m-portlet__nav-item"><a href="#"'
//							+'		class="m-portlet__nav-link m-portlet__nav-link--icon"><i'
//							+'			class="la la-close"></i></a></li>'
							+'	<li class="m-portlet__nav-item"><a onclick="toggleUpdateClientModal('+response[i].id+')"'
							+'	class="m-portlet__nav-link m-portlet__nav-link--icon"><i'
							+'		class="la la-refresh"></i></a></li>'
							+'	</ul>'
							+'</div>'

							+	'<h2 class="m-portlet__head-label m-portlet__head-label--info">'
							+'<span>'+ getLogoUploadForm(response[i])+'</span>'

							+		'<span>'+response[i].name+'</span>'
							+	'</h2>'
							+'</div>'
						+'</div>'
						+'<div class="m-portlet__head-tools">'
						+	'<ul'
							+	' class="nav nav-tabs m-tabs m-tabs-line m-tabs-line--right m-tabs-line-danger"'
							+	' role="tablist">'
//							+	'<li class="nav-item m-tabs__item"><a'
//							+		' class="nav-link m-tabs__link active show" data-toggle="tab"'
//							+		' href="#m_portlet_tab_'+response[i].id+'_1" role="tab" aria-selected="true">'
//							+			' Stats </a></li>'
							+	'<li class="nav-item m-tabs__item"><a'
							+		' class="nav-link m-tabs__link  active show" data-toggle="tab"'
							+		' href="#m_portlet_tab_'+response[i].id+'_3" role="tab" aria-selected="true">'
							+		'	Details </a></li>'
							+	'<li class="nav-item m-tabs__item"><a'
							+		' onclick="populateClientSites('+response[i].id+')" class="nav-link m-tabs__link" data-toggle="tab"'
							+		' href="#m_portlet_tab_'+response[i].id+'_2" role="tab" aria-selected="false">'
							+			' Sites ('+response[i].sitesCount+')</a></li>'
						
							+'</ul>'
						+'</div>'
					+'</div>'
					+'<div class="m-portlet__body">'
						+'<div class="tab-content">'
//							+'<div style="height:100%;" class="tab-pane active show" id="m_portlet_tab_'+response[i].id+'_1">'
//							+populateClientStats(response[i].id)
//							+'</div>'
							+'<div style="position:relative;width:100%;height:260px;" class="tab-pane" id="m_portlet_tab_'+response[i].id+'_2">'
							
							+'<div class="sites_map" id="sites_client_'+response[i].id+'">'
							
							
							+'</div>'
							+'<div style="position:relative;top:2%;right:1%;float:right;">'
				            +'<a title="Fullscreen" href="#"  id="btn-enter-full-screen-'+response[i].id+'" class="btn btn-secondary m-btn m-btn--icon m-btn--icon-only"><i class="fa fa-expand"></i></a>'
				            +'<a title="Exit Fullscreen" href="#" style="display:none;" id="btn-exit-full-screen-'+response[i].id+'" class="btn btn-secondary m-btn m-btn--icon m-btn--icon-only" ><i class="fa fa-compress"></i></a>'
				            +'</div>'
				        	+'<div style="position:relative;top:2%;left:1%;float:left;"><a onclick="populateModalNewSite('+response[i].id+')" title="New Site" data-toggle="modal" data-target="#modal_new_site" class="btn btn-danger m-btn m-btn--icon btn-sm m-btn--icon-only">'
							+'<i class="fa fa-map-marker"></i>'
							+'</a></div>'
							+'<div  style="width:17%; position:absolute;top:2%;left:27%;float:left;">'
							+'<select data-style="btn-info" style="position:absolute;width:100%;" id="site_nature_filter_client_'+response[i].id+'" class="form-control m-bootstrap-select m_selectpicker" data-live-search="true" >'
							+'</select>'
							+'</div>'
							+'<div  style="width:25%; position:absolute;top:2%;right:30%;float:right;">'
							+'<select style="position:absolute;width:100%;" id="select_site_client_'+response[i].id+'" class="form-control m-bootstrap-select m_selectpicker" data-live-search="true" >'
							+'</select>'
							+'</div>'
							+'<div style="position:absolute;top:92%;right:1%;float:right;"><a onclick="handleRemoveSite('+response[i].id+')" title="Delete Site" class="btn btn-danger m-btn m-btn--icon btn-sm m-btn--icon-only">'
							+'<i class="fa fa-trash-o"></i>'
							+'</a></div>'
							+'<div  style="position:absolute;top:92%;left:1%;float:left;"><a onclick="populateModalUpdateSite('+response[i].id+')" title="Update Site" class="btn btn-info m-btn m-btn--icon btn-sm m-btn--icon-only">'
							+'<i class="fa fa-check-square-o"></i>'
							+'</a></div>'
							+'</div>'
							+'<div class="tab-pane  active show" id="m_portlet_tab_'+response[i].id+'_3">'
							+populateClientDetails(response[i])
						+'</div>'
					+'</div>'
				+'</div>'
				+'</br>';
				
				
				if(!(i % 2)){
					$('#clients_container_even_row').append(html_text);
					
				}else{
					$('#clients_container_odd_row').append(html_text);
				}
			}

			

		},
		error : function(e) {
			alert("this");
			alert('Error: clients ' + e);
		}
	});
	natureDatatableJson.init();
	populateNatureFilter();

	}

function getLogoUploadForm(client){
	
	return '<form target="noTarget" id="clientLogoForm_'+client.id+'"'
		+' name="clientLogoForm">'
		+'<div title="Click to update client logo"  class="client-logo">'
		+'<div class="logo">'
		+'	<input id="input_logo_'+client.id+'" class="'+client.id+'" name="file" type="file"'
		+'		accept="image/*" required>'
		+'	<div class="logo__helper">'
		+'		<div '
		+'			style="background:url(\''+ client.logo +'\') no-repeat ;'
		+'						-webkit-background-size: cover;'
		+'								-moz-background-size: cover;'
		+'							-o-background-size: cover;'
		+'										background-size: cover;"'
		+'				class="logo__frame logo__frame--circle">'
		+'						<canvas class="logo__canvas">'
		+'						</canvas>'
		+'				<div class="message is-loading">'
		+'					<i class="fa fa-2x fa-spin fa-spinner"></i>'
		+'				</div>'
		+'	<div class="message is-wrong-file-type">'
		+'				<p>Only images allowed.</p>'
		+'				<p class="message--desktop">Drop your photo here'
		+'					or browse your computer.</p>'
		+'				<p class="message--mobile">Tap here to select your'
		+'					picture.</p>'
		+'			</div>'
		+'			<div class="message is-wrong-image-size">'
		+'				<p>Your photo must be larger than 350px.</p>'
		+'			</div>'
		+'		</div>'
					
		+'		</div>'

		+'	</div>'
		+'</div>'
		+'</form>';
}

function HandleMapControls(map,clientId){
	// var googleMapWidth = $("#m_portlet_tab_1_2").css('width');
	// var googleMapHeight = $("#m_portlet_tab_1_2").css('height');

	$('#btn-enter-full-screen-'+clientId).click(function() {
		
// if(!(clientId % 2))
// $('#clients_container_odd_row').hide();
// else
// $('#clients_container_even_row').hide();
//		
		$('#general-header').hide();
		
	    $("#m_portlet_tab_"+clientId+"_2").css({
	        position: 'fixed',
	        top: 0,
	        zIndex: 2,
	        left: 0,
	        width: '100%',
	        height: '100%',
	        backgroundColor: 'white'
	    });

	    $("#sites_client_"+clientId).css({
	        height: '100%'
	    });

	    google.maps.event.trigger(map, 'resize');
	    // map.setCenter(newyork);

	    // Gui
	    $('#btn-enter-full-screen-'+clientId).toggle();
	    $('#btn-exit-full-screen-'+clientId).toggle();

	    return false;
	});

	$('#btn-exit-full-screen-'+clientId).click(function() {
		
		$('#general-header').show();
		
	
		
		$("#m_portlet_tab_"+clientId+"_2").css({
	        position: 'relative',
	        top: 0,
	        zIndex: 1,
	        width: '100%',
	        height: '260px',
	        backgroundColor: 'transparent'
	    });

	    google.maps.event.trigger(map, 'resize');
	    // map.setCenter(newyork);

	    // Gui
	    $('#btn-enter-full-screen-'+clientId).toggle();
	    $('#btn-exit-full-screen-'+clientId).toggle();
	    return false;
	});
	
}
	

function populateModalUpdateSite(clientId){
	$('#update_site_nature').html('');
	var siteId = $('#select_site_client_'+clientId).val();
	var arr = [];
	$('#button_update_site').attr("onClick",'handleUpdateSite('+siteId+')');
	$.ajax({
		type : "GET",
		url : '/get-site-details',
		data: 'siteId='+siteId,
		async: false,
		success : function(response) {
			if(response.status == "SUCCESS"){
				$('#update_site_name').val(response.result.name);
				$('#update_site_latitude').val(response.result.latitude);
				$('#update_site_longitude').val(response.result.longitude);
				for (i = 0; i < response.result.natures.length; i++){
					arr.push({
						 id: response.result.natures[i].name,
			             text: response.result.natures[i].name,
			             selected: response.result.natures[i].selected
				   	});
				
				}
				$('#update_site_nature').select2({
						data: arr,
				});
				$('#modal_update_site').modal('show');
			}else{
				toastr("cant get details");
			}
		},
		error : function(e) {
			alert('Error: site details ' + e);
		}
	});
	
}

function toggleUpdateClientModal(clientId){
	$('#button_update_client').attr('onClick','updateClientDetails('+clientId+')');
	populateModalUpdateClientDetails(clientId);
	$('#modal_update_client').modal('show');
}

function populateModalUpdateClientDetails(clientId){
	$.ajax({
		type : "GET",
		url : '/get-client-details',
		data : "id="+clientId,
	    success : function(response) {
			// we have the response
			$('#update_client_name').val(response.name);
			$('#update_client_email').val(response.email);
			$('#update_client_country').val(response.country);
			$('#update_client_address').val(response.address);
			$('#update_client_phone').val(response.phone);
			
		},
		error : function(e) {
			toastr('Error: can\'t get client details ', e);
		}
	});	
}

function updateClientDetails(clientId){
	var name = $('#update_client_name').val();
	var email = $('#update_client_email').val();
	var country = $('#update_client_country').val();
	var address = $('#update_client_address').val();
	var phone = $('#update_client_phone').val();
	
	var nameError = $('#update_client_name_error');
	var emailError = $('#update_client_email_error');
	var countryError = $('#update_client_country_error');
	var addressError = $('#update_client_address_error');
	var phoneError = $('#update_client_phone_error');
	
	nameError.hide('fast');
	emailError.hide('fast');
	countryError.hide('fast');
	addressError.hide('fast');
	phoneError.hide('fast');
// if ($('#update_site_nature').find(":selected").length != 0){
	$.ajax({
		type : "POST",
		url : '/update-client',
		data : "id="+clientId+"&name="+name+"&email="+email+"&country="+country+"&address="+address+"&phone="+phone,
	    success : function(response) {
			// we have the response
			if (response.status == "SUCCESS") {
				toastr.success("Client updated successfully", "Well done!");
				if(response.result != 'undefined'){
					// doUpdateSiteNatures(siteId);
					$("#modal_update_client").modal('hide');
					$('#update_client_name').val('');
					$('#update_client_email').val('');
					$('#update_client_address').val('');
					$('#update_client_phone').val('');

					// $('#service_templates_boq_checkbox_list
					// :checkbox').prop('checked', false);
// setTimeout(
// function()
// {
// location.reload()
// }, 1000);
					
					populateClientsUI();
				}
			} else {
				if(response.result == 'client-exists'){
					toastr.error("Couldn't update client, a client with this name already exists", "Change Client name");
				}else{
					toastr.error("Couldn't update client", "Error");
				}	
				
				for (i = 0; i < response.result.length; i++) {
						if (response.result[i].code == "client.name.empty")
							nameError.show('slow');
						if (response.result[i].code == "client.email.empty")
							emailError.show('slow');
						if (response.result[i].code == "client.country.empty")
							countryError.show('slow');
						if (response.result[i].code == "client.address.empty")
							addressError.show('slow');
						if (response.result[i].code == "client.phone.empty")
							phoneError.show('slow');
						

				}
				
			}
		},
		error : function(e) {
			toastr('Error: can\'t update client details ', e);
		}
	});	
// }else{
// toastr.warning("Couldn't update Site, you have to select at least 1 nature",
// "Select Nature(s)");
// }
	// console.log("hi"+id);
}

function handleUpdateSite(siteId){
	var name = $('#update_site_name').val();
	var lat = $('#update_site_latitude').val();
	var long = $('#update_site_longitude').val();
	if ($('#update_site_nature').find(":selected").length != 0){
	$.ajax({
		type : "POST",
		url : '/update-site',
		data : "id="+siteId+"&name="+name+"&latitude="+lat+"&longitude="+long,
	    success : function(response) {
			// we have the response
			if (response.status == "SUCCESS") {
				toastr.success("Site updated successfully", "Well done!");
				if(response.result != 'undefined'){
					doUpdateSiteNatures(siteId);
					$("#modal_update_site").modal('hide');
					$('#update_site_name').val('');
					$('#update_site_latitude').val('');
					$('#update_site_longitude').val('');
					// $('#service_templates_boq_checkbox_list
					// :checkbox').prop('checked', false);
					populateClientsUI();
				}
			} else {
				
				toastr.error("Couldn't update Site", "Error");
				
				for (i = 0; i < response.result.length; i++) {
						if (response.result[i].code == "site.name.empty")
							$('#update_site_name_error').show('slow');
						if (response.result[i].code == "site.latitude.empty")
							$('#update_site_latitude_error').show('slow');
						if (response.result[i].code == "site.longitude.empty")
							$('#update_site_longitude_error').show('slow');
						

				}
				
			}
		},
		error : function(e) {
			toastr('Error: can\'t get Site details ', e);
		}
	});	
	}else{
		toastr.warning("Couldn't update Site, you have to select at least 1 nature", "Select Nature(s)");
	}
	// console.log("hi"+id);
}

function doUpdateSiteNatures(siteId){
	$('#update_site_nature').find(":selected").each(function(){
		var nature = $(this).attr('value');
		// alert('selected values: '+$(this).attr('value'));
		$.ajax({
			type : "POST",
			url : '/update-site-natures',
			data : "nature=" + nature + "&siteId=" + siteId,
			success : function(response) {
				// we have the response
				if (response.status == "FAIL") {
					toastr.error("Couldn't add nature", "Error");
				} 
			},
			error : function(e) {
				toastr.error("Couldn't add nature", "Server Error");
			}
		});
	});
		
	
}

//function populateClientStats(clientId){
//	var html_text;
//	$.ajax({
//		type : "GET",
//		url : '/get-client-stats',
//		data: 'clientId='+clientId,
//		async: false,
//		success : function(response) {
//			
//			
//			html_text = '<div class="m-widget4">'
//+'<div class="m-widget4__item">'
//+	'<div class="m-widget4__ext">'
//+		'<span class="m-widget4__icon m--font-brand"> <i'
//+			' class="flaticon-interface-3"></i>'
//+		'</span>'
//+	'</div>'
//+	'<div class="m-widget4__info">'
//+		'<span class="m-widget4__text"> Total Projects </span>'
//+	'</div>'
//+	'<div class="m-widget4__ext">'
//+		'<span class="m-widget4__number m--font-info"> '+response.projects+' </span>'
//+	'</div>'
//+'</div>'
//+'<div class="m-widget4__item">'
//+	'<div class="m-widget4__ext">'
//+		'<span class="m-widget4__icon m--font-brand"> <i'
//+			' class="flaticon-folder-4"></i>'
//+		'</span>'
//+	'</div>'
//+	'<div class="m-widget4__info">'
//+		'<span class="m-widget4__text"> Total Transactions </span>'
//+	'</div>'
//+	'<div class="m-widget4__ext">'
//+		'<span class="m-widget4__number m--font-info"> '+response.money+' </span>'
//+	'</div>'
//+'</div>'
//+'</div>';
//			  
//
//		
//},
//error : function(e) {
//	alert("this+1");
//	alert('Error: clients ' + e);
//}
//});
//return html_text;		
//}

function populateClientSites(clientId){
	var map_div = '#sites_client_'+clientId;
	
	
	var map = new GMaps({
        div: map_div,
        lat: 34.7615155,
        lng: 10.6630578,
        disableDefaultUI: true,
        
    });

	
	HandleMapControls(map,clientId);
		
	var selectSiteOptions = '';
	$.ajax({
		type : "GET",
		url : '/get-client-sites',
		data : 'clientId=' + clientId,
		async: false,
		success : function(response) {

			for (i = 0; i < response.length; i++) {
				  map.addMarker({
				        lat: response[i].latitude,
				        lng: response[i].longitude,
				        title: response[i].name,
				        id: response[i].id,
				        natures: response[i].natures,
				       
				        click: function(e,id,title) {
				       	  
					            if (console.log) console.log(e);
					            for (i in map.markers){
						        	map.markers[i].setIcon();
					
					            }
					            $(this).attr('icon','https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/48/Map-Marker-Marker-Outside-Chartreuse.png');
					            $('#select_site_client_'+clientId).val($(this).attr('id'));
					            $('#select_site_client_'+clientId).selectpicker('refresh');
								
				        }
				    });
				  
				  selectSiteOptions += '<option data-subtext="'+response[i].natures+'" value="'+response[i].id+'">'+response[i].name+'</option>';
				  
		        }
			map.setZoom(5);

					$('#select_site_client_'+clientId).selectpicker({
						width: '100%',
						
						
					});
			$('#select_site_client_'+clientId).html(selectSiteOptions);
			$('#select_site_client_'+clientId).selectpicker('refresh');
			$('#select_site_client_'+clientId).on('change',function(){
				var selectedSite = $('#select_site_client_'+clientId).val();
				for (i in map.markers){
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
	
	$('#site_nature_filter_client_'+clientId).on('change',function(){
		filterMarkersByNature(map,$(this).val())
	});
	
	
}

function filterMarkersByNature(map,nature){
	    for (i = 0; i < map.markers.length; i++) {
	        if (map.markers[i].natures.match(nature) || nature.length === 0) {
	        	map.markers[i].setVisible(true);
	        }
	        else {
	        	map.markers[i].setVisible(false);
	        }
	    }
}

function populateClientDetails(client){
	var html_text = '<div class="m-list-search">'
   + '<div class="m-list-search__results">'
   + '    <span class="m-list-search__result-item">'
    + '       <span class="m-list-search__result-item-icon"><i class="fa fa-inbox m--font-success"></i></span>'
    +  '      <span class="m-list-search__result-item-text">Email: <strong>'+client.email+'</strong></span>'
    +  '  </span>'
    +  '  <span class="m-list-search__result-item">'
    +   '     <span class="m-list-search__result-item-icon"><i class="fa fa-address-book m--font-primary"></i></span>'
     +  '     <span class="m-list-search__result-item-text">Address: <strong>'+client.address+'</strong></span>'
     +  ' </span>'
     +  ' <span class="m-list-search__result-item">'
    +    '    <span class="m-list-search__result-item-icon"><i class="fa fa-plane m--font-warning"></i></span>'
    +    '    <span class="m-list-search__result-item-text">Country: <strong>'+client.country+'</strong></span>'
    +   ' </span>'
    +   ' <span class="m-list-search__result-item">'
    +    '    <span class="m-list-search__result-item-icon"><i class="fa fa-phone m--font-danger"></i></span>'
    +    '    <span class="m-list-search__result-item-text">Phone: <strong>'+client.phone+'</strong></span>'
    +    '</span>'
    +'</div>'
+'</div>';
	return html_text;
}

function SaveNewSite(name,long,lat,clientId,natureSelect){
	var result = false;
$.ajax({
	type : "POST",
	url : '/save-new-site',
	data : "name=" + name +"&longitude="+ long +"&latitude="+ lat+"&clientId="+clientId,
	async : false,
	success : function(response) {
		if (response.status == "SUCCESS") {
			natureSelect.find(":selected").each(function(){
				addNatureToSite(response.result,$(this).val());
			});
			result = true;
		} else if(response.status == "FAIL"){
			toastr.warning("Please fill all site informations", "Warning");
			result = false;
		}else{
			toastr.error("No such client", "Error");
			result = false;
		}
	},
	error : function(e) {
		toastr.error("Couldn't add sites", "Server Error");
		result = false;
	}
});

return result;
}

function addNatureToSite(siteId,nature){
	$.ajax({
		type : "POST",
		url : '/add-nature-to-site',
		data : "siteId=" + siteId + "&nature=" + nature,
		success : function(response) {
			if (response.status == "FAIL") {
				toastr.error("Couldn't add nature to Site", "Error");
			}
		},
		error : function(e) {
			toastr.error("Couldn't add nature to Site", "Server Error");
		}
	});	
}

function newSiteSaveManualAddedSites(clientId) {
	var result = false;
	jQuery('.new-site-form').each(function(e)
			{
		var name = $(this).find('.new-site-form-name').val();
		var long = $(this).find('.new-site-form-longitude').val();
		var lat = $(this).find('.new-site-form-latitude').val();
		var natureSelect = $(this).find('.new-site-form-nature');
		result = SaveNewSite(name,long,lat,clientId,natureSelect);
			});
	if(result == true){
		toastr.success('All sites added successfully','Well done!')
		$("#modal_new_site").modal('hide');
		populateClientsUI();
	}
	else{
		toastr.error('Couldn\'t add some sites','Oops!')
		
	}
}

function doAddNewNature(){
	var name = $('#new_nature_name').val();
	var description = $('#new_nature_description').val();
	
	
	$('#new_nature_name_error').hide('fast');
	$('#new_nature_description_error').hide('fast');

	$.ajax({
		type : "POST",
		url : '/add-new-nature',
		data : "name=" + name + "&description=" + description ,
		async : false,
		success : function(response) {
			if (response.status == "SUCCESS") {
				toastr.success("Nature created Successfully", "Well done!");
				if(response.result != 'undefined'){
					$('#new_nature_name').val('');
					$('#new_nature_description').val('');
					$("#new-nature-modal").modal('hide');
					$('#natures_datatable').mDatatable('reload');
					
// setTimeout(
// function()
// {
// location.reload();
//								  
// }, 500);
					
					
				}
			} else {
				if(response.result == 'nature-exists'){
					toastr.error("Couldn't create nature, a nature with this name already exists", "Change nature name");
				}else{
				toastr.error("Couldn't create nature", "Error");
				}
				for (i = 0; i < response.result.length; i++) {
						if (response.result[i].code == "nature.name.empty")
							$('#new_nature_name_error').show('slow');
						if (response.result[i].code == "nature.description.empty")
							$('#new_nature_description_error').show('slow');
						
				}
				
			}
		},
		error : function(e) {
			toastr.error("Couldn't add nature", "Server Error");
		}
	});
	
}


function handleRemoveSite(clientId){
	var siteId = $('#select_site_client_'+clientId).val();
	swal({
		title : 'Are you sure?',
		text : "You won't be able to revert this!",
		type : 'warning',
		showCancelButton : true,
		confirmButtonText : 'Yes, delete it!'
	}).then(
			function(result) {
				if (result.value) {
					$
							.ajax({
								type : "POST",
								url : '/delete-site',
								data : "siteId=" + siteId,
								async : true,
								success : function(response) {
									if (response.status == "SUCCESS") {
										swal('Deleted!',
												'Site has been deleted.',
												'success');
										populateClientsUI();
										
									} else {
										swal('Fail!', 'Site not deleted.',
												'error');
									}

								},
								error : function(e) {
									toastr.error("Couldn't delete Site",
											"Server Error");
									result = false;
								}
							});

				}

			}

	);
}


function handleRemoveNatureClick(natureId) {
	swal({
		title : 'Are you sure?',
		text : "You won't be able to revert this!",
		type : 'warning',
		showCancelButton : true,
		confirmButtonText : 'Yes, delete it!'
	}).then(
			function(result) {
				if (result.value) {
					$
							.ajax({
								type : "POST",
								url : '/delete-nature',
								data : "natureId=" + natureId,
								async : true,
								success : function(response) {
									if (response.status == "SUCCESS") {
										swal('Deleted!',
												'Nature has been deleted.',
												'success');
										$('#natures_datatable').mDatatable('reload');
										populateClientsUI();
										// $('#init_nature_datatable').click();
										
									} else {
										swal('Fail!', 'Nature not deleted.',
												'error');
									}

								},
								error : function(e) {
									toastr.error("Couldn't delete Nature",
											"Server Error");
									result = false;
								}
							});

				}

			}

	);

}

function populateNatureFilter(){
	$.ajax({
		type : "GET",
		url : '/json-natures',
		success : function(response) {
			var html_text = '<option value="">All Sites</option>';
			for (i = 0; i < response.length; i++) {
				html_text += '<option value="'+response[i].name+'">'+response[i].name+'</option>';
			}
			
			
			$('select[id*=site_nature_filter_client_]').each(function(){
				$(this).html(html_text);
				$(this).selectpicker('refresh');
			});
		},
		error : function(e) {
			alert('Error: natures ' + e);
		}
	});
}

function populateNatureListNewSite() {
	$.ajax({
		type : "GET",
		url : '/json-natures',
		success : function(response) {
			var arr = [];
			for (i = 0; i < response.length; i++) {
						arr.push({
							 id: response[i].name,
				             text: response[i].name
					   	});

			}
			
			
			$(".new-site-form-nature").each(function(){
				if($(this).val() == "0" || $(this).val() == null || $(this).val() == '' ){
					$(this).html('');
					$(this).select2({
			            placeholder: "Nature(s)",
			            data: arr,
			         
			            width: '100%'
			        });
				}
			});
		},
		error : function(e) {
			alert('Error: natures ' + e);
		}
	});

}

function populateModalNewSite(clientId){
	$('#button_save_sites').attr('onClick', 'newSiteSaveManualAddedSites('+clientId+')');
	populateNatureListNewSite();
	
	 
	 var myDropzone = new Dropzone("#xls-dropzone-site", { // Make the whole
															// body a
			// dropzone
		 paramName: "excelfile",
		 url: "/process-excel-sites", // Set the url
		 autoQueue: true, // Make sure the files aren't queued until manuall
		 previewsContainer: false,
		 acceptedFiles: ".xlsx, .xls",
		 success: function (file, response) {
		      
		      if(response.status == "SUCCESS"){
		    	  toastr.success("Added "+response.result+" sites","Well Done");
		    	  $('#modal_new_site').modal('hide');
		    	  populateClientsUI();
		      }
		      else
		    	  toastr.error("You are trying to add already existing sites","Existing Sites")
		 }
	 });
	 
	 myDropzone.on("sending", function(file, xhr, formData) {
		  // Show the total progress bar when upload starts
			 formData.append('id',clientId);
		 // document.querySelector("#total-progress").style.opacity = "1";
		  // And disable the start button
		  // file.previewElement.querySelector(".start").setAttribute("disabled",
			// "disabled");
		});
	}

function initFormRepeaterNewSite(){
	populateNatureListNewSite();
}

function uploadClientLogo(clientId) {
    var input = document.querySelector('#input_logo_'+clientId);
	var file = input.files[0];
	var formData = new FormData();
    formData.append("id", clientId);
    formData.append("file", file);
    $.ajax({
		type : "POST",
		url : '/upload-client-logo',
		data: formData,
		processData: false,
		contentType: false,
		success : function(response) {
			if(response.status="SUCCESS")
				toastr.success("Logo updated successfully");
			else
				toastr.error("Couldn't update logo");
		},
		error : function(e) {
			alert('Error: Client logo upload ' + e);
		}
	});
    
}





$('.loader-wrapper').show();

$(document).ready(function() {

	
	populateClientsUI();
	$('.loader-wrapper').hide();
	
	$('form[id*=clientLogoForm_]').each(function(){
		 var p = new ClientLogo("#"+$(this).attr('id'), null,
      {
        imageHelper: true,
          onRemove: function (type) {
              $('.preview').hide().attr('src','');
          },
          onError: function (type) {
              console.log('Error type: ' + type);
          }
      });
		 
		
	});
	

	
	
});
