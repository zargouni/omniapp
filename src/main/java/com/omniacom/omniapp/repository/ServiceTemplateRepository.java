package com.omniacom.omniapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.ServiceTemplate;
import com.omniacom.omniapp.repository.custom.ServiceTemplateRepositoryCustom;

public interface ServiceTemplateRepository extends CrudRepository<ServiceTemplate, Long>,ServiceTemplateRepositoryCustom{

}
