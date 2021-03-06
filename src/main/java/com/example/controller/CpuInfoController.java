package com.example.controller;

import com.example.domain.CpuInfo;
import com.example.repository.CpuInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Xue on 12/01/16.
 */
@RestController
public class CpuInfoController {
    @Autowired
    private CpuInfoRepository cpuInfoRepository;

    @RequestMapping("/hr_cpuInfo")
    public List<CpuInfo> cpuInfos() {
        List<CpuInfo> cpuInfos = cpuInfoRepository.findAll();
        /*cpuInfos.stream().map(cpuInfo -> {
            cpuInfo.setCpuusage(cpuInfo.getCpuusage() * 100);
            return cpuInfo;
        }).collect(Collectors.toList());*/
        return cpuInfos;
    }
}
