package com.bitcodex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitcodex.entity.FileDetails;

public interface FileRepository extends JpaRepository<FileDetails,Integer> {

}
