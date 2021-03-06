package com.example.controller;

import com.example.domain.ESQueryResponse;
import com.example.service.SftpInvokeService;
import com.example.service.WlsJmxMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Xue on 11/29/16.
 */
@Controller
public class HomeController {

    private final String URL = "http://11.11.44.201:9200/logstash-date/_search?q=level:'Error'&q=message:'plate'";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @RequestMapping("/")
    public String homePage() {
        return "home";
    }

    @RequestMapping("/zzkf/zhap5")
    public String zhap5Page() { return "zhap5"; }

    @Autowired
    WlsJmxMonitorService service;

    @RequestMapping("/zzkf/zhap5_server_data")
    @ResponseBody
    public List<Map<String, String>> pollWlsJmxState() {
        return service.pollingWlsVieJmx();
    }

    @RequestMapping("/zzkf/zhap5_domain_data")
    @ResponseBody
    public List<Map<String, String>> pollDomainJmxState() {
        try {
            return service.pollingDomainVieJmx();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @RequestMapping("/jcpt/xoaps")
    public String xoapsPage() { return "xoaps"; }

    @RequestMapping("/jcpt/xmapt")
    public String xmaptPage() { return "xmapt"; }

    @Autowired
    SftpInvokeService invokeService;

    @RequestMapping(value = "/download/{ip}/{logfile}/{type}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("ip") String ip, @PathVariable("logfile") String logfile, @PathVariable("type") String type)
            throws IOException {
        invokeService.logFileDownload(ip, type, logfile.substring(logfile.length()-1));
        String filePath;
        if ("log".equals(type)) {
            filePath = "/oracle/logs/" + logfile + ".log";
        } else if ("out".equals(type)) {
            filePath = "/oracle/logs/" + logfile + ".out";
        } else {
            filePath = "/oracle/logs/gc_" + logfile + ".log";
        }

        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }

    @RequestMapping(value = "/download/xoaps/{ip}/{logfile}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadXoapsLog(@PathVariable("ip") String ip, @PathVariable("logfile") String logfile)
            throws IOException {
//        invokeService.logFileDownload(ip, type, logfile.substring(logfile.length()-1));
        invokeService.logFileDownloadXoaps(ip, logfile.substring(logfile.length()-1));
        String filePath = "/oracle/logs/" + logfile + "_yyyy_MM_dd_hh_mm.log";

        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/jcpt/xoaps/error_distr")
    @ResponseBody
    public Map<String, Long> xoapsErrorDistr() {
        List<String> domains = Arrays.asList("YQT", "XS", "LYHG", "TRQ");
        Map<String, Long> count = new HashMap<>();
        domains.forEach(domain -> {
            ESQueryResponse response = restTemplate.getForObject(URL.replace("date", LocalDate.now().format(formatter)).replace("plate", domain), ESQueryResponse.class);
            count.put(domain, response.getHits().getTotal());
        });
        return count;
    }

}
