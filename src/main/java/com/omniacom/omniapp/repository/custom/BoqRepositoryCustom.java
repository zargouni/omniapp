package com.omniacom.omniapp.repository.custom;

import java.util.List;

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.entity.ServiceTemplate;

public interface BoqRepositoryCustom {

	public List<ServiceTemplate> findAllServiceTemplates(BillOfQuantities boq);

	public boolean addAllServiceTemplates(BillOfQuantities boq, List<ServiceTemplate> templates);

	public boolean addOneServiceTemplate(BillOfQuantities boq, ServiceTemplate template,float price);

	public boolean removeOneServiceTemplate(BillOfQuantities boq, ServiceTemplate template);

	public void removeAllServiceTemplates(BillOfQuantities boq);

	public boolean templateExists(BillOfQuantities boq, ServiceTemplate template);

	public boolean boqExists(String name);
	
	public List<BillOfQuantities> findAllAvailableValidBoqs();
	
	public float findServicePriceBoq(BillOfQuantities boq, ServiceTemplate template);
	
	

}
