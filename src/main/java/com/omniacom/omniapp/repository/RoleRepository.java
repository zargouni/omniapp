package com.omniacom.omniapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.omniacom.omniapp.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	@Query("select role from Role role where role.name = :name")
	Role findByName(@Param("name") String name);
}
