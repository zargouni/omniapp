package com.omniacom.omniapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.repository.custom.ClientRepositoryCustom;

public interface ClientRepository extends CrudRepository<Client, Long>, ClientRepositoryCustom {

	

}
