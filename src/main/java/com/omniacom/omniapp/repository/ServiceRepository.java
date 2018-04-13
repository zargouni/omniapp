package com.omniacom.omniapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.repository.custom.ServiceRepositoryCustom;

public interface ServiceRepository extends CrudRepository<Service, Long>, ServiceRepositoryCustom {

}
