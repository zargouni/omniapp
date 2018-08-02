package com.omniacom.omniapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.custom.TaskRepositoryCustom;

public interface TaskRepository extends CrudRepository<Task, Long>, TaskRepositoryCustom {



}
