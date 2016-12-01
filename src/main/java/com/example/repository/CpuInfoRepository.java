package com.example.repository;

import com.example.domain.CpuInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * Created by Xue on 12/01/16.
 */
@Repository
public interface CpuInfoRepository extends JpaRepository<CpuInfo, Timestamp> {

}