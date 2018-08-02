package com.omniacom.omniapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.UploadedFile;

public interface IssueRepository extends CrudRepository<Issue, Long> {

	@Query("SELECT f FROM UploadedFile f WHERE f.issue.id = :issueId")
	List<UploadedFile> findAllFiles(@Param("issueId") Long issueId);

}
