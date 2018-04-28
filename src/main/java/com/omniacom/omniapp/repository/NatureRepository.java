package com.omniacom.omniapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.Nature;
import com.omniacom.omniapp.repository.custom.NatureRepositoryCustom;

public interface NatureRepository extends CrudRepository<Nature, Long>, NatureRepositoryCustom {

}
