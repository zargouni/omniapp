function populateClientsUI(){
	$.ajax({
		type : "GET",
		url : '/get-all-clients-details-json',
		async: false,
		success : function(response) {

			for (i = 0; i < response.length; i++) {
				var html_text = '<div'
					+' class="m-portlet m-portlet--tabs m-portlet--creative m-portlet--first m-portlet--bordered-semi">'
					+'<div class="m-portlet__head">'
						+'<div class="m-portlet__head-caption">'
							+'<div class="m-portlet__head-title">'
							+	'<span class="m-portlet__head-icon m--hide"> <i'
							+		' class="flaticon-statistics"></i>'
							+	'</span>'
							+'<div class="m-portlet__head-tools">'
							+'<ul class="m-portlet__nav ">'
							+'	<li class="m-portlet__nav-item"><a href="#"'
							+'		class="m-portlet__nav-link m-portlet__nav-link--icon"><i'
							+'			class="la la-close"></i></a></li>'
							+'	<li class="m-portlet__nav-item"><a href="#"'
							+'	class="m-portlet__nav-link m-portlet__nav-link--icon"><i'
							+'		class="la la-refresh"></i></a></li>'
							+'	</ul>'
							+'</div>'

							+	'<h2 class="m-portlet__head-label m-portlet__head-label--danger">'
							+		'<span>'+response[i].name+'</span>'
							+	'</h2>'
							+'</div>'
						+'</div>'
						+'<div class="m-portlet__head-tools">'
						+	'<ul'
							+	' class="nav nav-tabs m-tabs m-tabs-line m-tabs-line--right m-tabs-line-danger"'
							+	' role="tablist">'
							+	'<li class="nav-item m-tabs__item"><a'
							+		' class="nav-link m-tabs__link active show" data-toggle="tab"'
							+		' href="#m_portlet_tab_'+response[i].id+'_1" role="tab" aria-selected="true">'
							+			' Stats </a></li>'
							+	'<li class="nav-item m-tabs__item"><a'
							+		' onclick="populateClientSites('+response[i].id+')" class="nav-link m-tabs__link" data-toggle="tab"'
							+		' href="#m_portlet_tab_'+response[i].id+'_2" role="tab" aria-selected="false">'
							+			' Sites ('+response[i].sitesCount+')</a></li>'
							+	'<li class="nav-item m-tabs__item"><a'
							+		' class="nav-link m-tabs__link" data-toggle="tab"'
							+		' href="#m_portlet_tab_'+response[i].id+'_3" role="tab" aria-selected="false">'
							+		'	Details </a></li>'
							+'</ul>'
						+'</div>'
					+'</div>'
					+'<div class="m-portlet__body">'
						+'<div class="tab-content">'
							+'<div class="tab-pane active show" id="m_portlet_tab_'+response[i].id+'_1">'
							+populateClientStats(response[i].id)
							+'</div>'
							+'<div style="position:relative;width:100%;height:260px;" class="tab-pane" id="m_portlet_tab_'+response[i].id+'_2">'
							+'<div class="sites_map" id="sites_client_'+response[i].id+'"></div>'
							+'<div style="position:absolute;top:18%;right:2%;float:right;"><a onclick="populateModalNewSite('+response[i].id+')" title="New Site" data-toggle="modal" data-target="#modal_new_site" class="btn btn-danger m-btn m-btn--icon btn-sm m-btn--icon-only">'
							+'<i class="fa fa-map-marker"></i>'
							+'</a></div>'
							+'<div style="position:relative;top:2%;right:10%;float:right;">'
							+'<select id="select_site_client_'+response[i].id+'" class="form-control m-bootstrap-select m_selectpicker" data-live-search="true" >'
							+'</select>'
							+'</div>'
							+'<div style="position:relative;top:3%;right:12%;float:right;"><a onclick="handleRemoveSite('+response[i].id+')" title="Delete Site" class="btn btn-danger m-btn m-btn--icon btn-sm m-btn--icon-only">'
							+'<i class="fa fa-trash-o"></i>'
							+'</a></div>'
							+'<div style="position:relative;top:3%;right:13%;float:right;"><a title="Update Site" href="#" class="btn btn-info m-btn m-btn--icon btn-sm m-btn--icon-only">'
							+'<i class="fa fa-check-square-o"></i>'
							+'</a></div>'
							+'</div>'
							+'<div class="tab-pane" id="m_portlet_tab_'+response[i].id+'_3">'
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
			alert('Error: clients ' + e);
		}
	});
	

	}
	
	 

function populateClientStats(clientId){
	var html_text;
	$.ajax({
		type : "GET",
		url : '/get-client-stats',
		data: 'clientId='+clientId,
		async: false,
		success : function(response) {
			
			
			html_text = '<div class="m-widget4">'
+'<div class="m-widget4__item">'
+	'<div class="m-widget4__ext">'
+		'<span class="m-widget4__icon m--font-brand"> <i'
+			' class="flaticon-interface-3"></i>'
+		'</span>'
+	'</div>'
+	'<div class="m-widget4__info">'
+		'<span class="m-widget4__text"> Total Projects </span>'
+	'</div>'
+	'<div class="m-widget4__ext">'
+		'<span class="m-widget4__number m--font-info"> '+response.projects+' </span>'
+	'</div>'
+'</div>'
+'<div class="m-widget4__item">'
+	'<div class="m-widget4__ext">'
+		'<span class="m-widget4__icon m--font-brand"> <i'
+			' class="flaticon-folder-4"></i>'
+		'</span>'
+	'</div>'
+	'<div class="m-widget4__info">'
+		'<span class="m-widget4__text"> Total Transactions </span>'
+	'</div>'
+	'<div class="m-widget4__ext">'
+		'<span class="m-widget4__number m--font-info"> '+response.money+' </span>'
+	'</div>'
+'</div>'
+'</div>';
			  

		
},
error : function(e) {
	alert('Error: clients ' + e);
}
});
return html_text;		
}

function populateClientSites(clientId){
	var map_div = '#sites_client_'+clientId;
	
	
	var map = new GMaps({
        div: map_div,
        lat: 34.7615155,
        lng: 10.6630578,
        
      
        
    });
	
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
		setTimeout(function(){
			location.reload();
		}, 2000);
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
					
					setTimeout(
							  function() 
							  {
								  location.reload();
								  
							  }, 500);
					
					
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
										setTimeout(
												  function() 
												  {
													  location.reload()
												  }, 1000);
										
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
										setTimeout(
												  function() 
												  {
													  location.reload()
												  }, 500);
										$('#init_nature_datatable').click();
										
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
}

function initFormRepeaterNewSite(){
	populateNatureListNewSite();
}

$(document).ready(function() {
	$('#init_nature_datatable').on('click',function(){
		natureDatatableJson.init();
	});
	
});
