package com.omniacom.omniapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.omniacom.omniapp.entity.BoqService;
import com.omniacom.omniapp.entity.BoqServiceId;

public interface BoqServiceRepository extends CrudRepository<BoqService, BoqServiceId> {
	
	@Query("select bs from BoqService bs where bs.boq.id=:boqId and bs.template.id=:templateId")
	BoqService findByBoqAndTemplate(@Param("boqId") Long boqId, @Param("templateId") Long templateId);

}
