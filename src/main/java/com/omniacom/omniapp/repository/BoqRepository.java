package com.omniacom.omniapp.repository;


import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.BillOfQuantities;
import com.omniacom.omniapp.repository.custom.BoqRepositoryCustom;

public interface BoqRepository extends CrudRepository<BillOfQuantities, Long>, BoqRepositoryCustom{

	

}
