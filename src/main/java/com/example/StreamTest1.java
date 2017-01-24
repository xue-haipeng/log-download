package com.example;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Xue on 01/23/17.
 */
public class StreamTest1 {

    public static String checkBusinessLine(String line) {
        if (line.contains("YQT")) {
            return "YQT";
        } else if (line.contains("XS")) {
            return "XS";
        } else if (line.contains("TRQ")) {
            return "TRQ";
        } else if (line.contains("LYHG")) {
            return "LYHG";
        } else if (line.contains("JCO")){
            return "JCO";
        } else {
            return "Other";
        }
    }

    public static void main (String[] args) {
        try (Stream<String> lines = Files.lines(Paths.get("D:/XOAPS_Srv001_2017_01_11_12_00.log"), Charset.defaultCharset())) {
//            lines.flatMap(line -> Arrays.stream(line.split("^####"))).filter(l -> l.contains("<Error>")).forEach(System.out::println);
            Map<String, List<String>> map = lines.flatMap(line -> Arrays.stream(line.split("^####"))).filter(l -> l.contains("<Error>"))
                    .collect(Collectors.groupingBy(s -> checkBusinessLine(s)));
            map.forEach((k, v) -> {
                System.out.println(k + " : " +v.size());
            });
//            System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
