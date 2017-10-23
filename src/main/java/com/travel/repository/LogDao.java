package com.travel.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.travel.entity.Log;


public interface LogDao extends PagingAndSortingRepository<Log, Long>, JpaSpecificationExecutor<Log>{
	
}