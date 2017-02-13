package com.example.repository;

import com.example.domain.WlsMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Xue on 12/01/16.
 */
@Repository
public interface WlsMonitorRepository extends JpaRepository<WlsMonitor, Long> {

    public List<WlsMonitor> findByRecordTimeBetween(Date start, Date end);
}