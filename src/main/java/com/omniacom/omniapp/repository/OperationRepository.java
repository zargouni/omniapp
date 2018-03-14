package com.omniacom.omniapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.repository.custom.OperationRepositoryCustom;

public interface OperationRepository extends CrudRepository<Operation, Long>, OperationRepositoryCustom {

}
