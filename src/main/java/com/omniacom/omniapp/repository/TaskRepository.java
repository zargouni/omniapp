package com.omniacom.omniapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

}
