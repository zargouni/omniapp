package com.omniacom.omniapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.omniacom.omniapp.entity.Project;
import com.omniacom.omniapp.repository.custom.ProjectRepositoryCustom;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>, ProjectRepositoryCustom {

	

}
