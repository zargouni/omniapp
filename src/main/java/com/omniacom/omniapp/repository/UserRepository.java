package com.omniacom.omniapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.custom.UserRepositoryCustom;

public interface UserRepository  extends CrudRepository<User, Long>, UserRepositoryCustom {

	
}
