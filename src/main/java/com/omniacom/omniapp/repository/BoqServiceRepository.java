package com.omniacom.omniapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.BoqService;
import com.omniacom.omniapp.entity.BoqServiceId;

public interface BoqServiceRepository extends CrudRepository<BoqService, BoqServiceId> {

}
