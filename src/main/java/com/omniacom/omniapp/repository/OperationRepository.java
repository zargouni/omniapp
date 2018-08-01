package com.omniacom.omniapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.Operation;
import com.omniacom.omniapp.entity.Service;
import com.omniacom.omniapp.repository.custom.OperationRepositoryCustom;

public interface OperationRepository extends CrudRepository<Operation, Long>, OperationRepositoryCustom {


}
