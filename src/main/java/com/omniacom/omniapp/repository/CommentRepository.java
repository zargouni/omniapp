package com.omniacom.omniapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.omniacom.omniapp.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
