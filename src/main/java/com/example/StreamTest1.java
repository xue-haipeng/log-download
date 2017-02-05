package com.example;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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

    public static <K, V extends Comparable<? super V>> Map<K, V> compareByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> mapInStream = map.entrySet().stream();
        mapInStream.sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
        return result;
    }
    public static <K extends Comparable<? super K>, V> Map<K, V> compareByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> mapInStream = map.entrySet().stream();
        mapInStream.sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
        return result;
    }

    public static void main (String[] args) {
        try (Stream<String> lines = Files.lines(Paths.get("D:/XOAPS_Srv001_2017_01_11_12_00.log"), Charset.defaultCharset())) {
//            lines.flatMap(line -> Arrays.stream(line.split("^####"))).filter(l -> l.contains("<Error>")).forEach(System.out::println);
//            lines.flatMap(line -> Arrays.stream(line.split("^####"))).filter(l -> l.contains("<Error>")).forEach(System.out::println);
/*            Map<String, List<String>> map = lines.flatMap(line -> Arrays.stream(line.split("^####"))).filter(l -> l.contains("<Error>"))
                    .collect(Collectors.groupingBy(s -> checkBusinessLine(s)));
            map.forEach((k, v) -> {
                System.out.println(k + " : " +v.size());
            });*/
//            System.out.println(total);
//            System.out.println(" ============ " + LocalTime.now() + " ============ ");
            long start = System.nanoTime();
            Map<String, Long> wordCount = lines.flatMap(line -> Arrays.stream(line.split(""))) .parallel()
                    .filter(s -> !s.equals("\\s*|\\t|\\r|\\n"))
                    .map(s -> s.replaceAll("[^a-zA-Z]", "@")).map(s -> s.toUpperCase())
                    .map(s -> new AbstractMap.SimpleEntry<String, Integer>(s, 1))
                    .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey, Collectors.counting()));
            wordCount.forEach((k, v) -> System.out.println(k + " : " + v));
            long end = System.nanoTime();
            System.out.println(" ============ " + (end - start) + " ============ ");
/*            Map<String, Long> sortedMap = wordCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));*/

            Map<String, Long> sortedMap = new LinkedHashMap<>();
            wordCount.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(5)
                    .forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));

            sortedMap.forEach((k, v) -> System.out.println(k + " : " + v));
//            System.out.println(Runtime.getRuntime().availableProcessors());

/*
            List<String> list = lines.flatMap(line -> Arrays.stream(line.split(""))).collect(Collectors.toList());
            System.out.println(LocalTime.now() + " ============ " + System.currentTimeMillis() + " ====== " + System.nanoTime());
            System.out.println(list.size());
            System.out.println(LocalTime.now() + " ============ " + System.currentTimeMillis() + " ====== " + System.nanoTime());
*/

        } catch (IOException e) {
            e.printStackTrace();
        }
/*        List<Integer> integerList = Arrays.asList( 7771, 7772, 7773, 7774, 7775, 7776, 7777, 7778, 7779, 77710, 77721, 77722, 77723, 77724, 77725, 77726, 77727, 77728, 77729, 77720,
                9991, 9992, 9993, 9994, 9995, 9996, 9997, 9998, 9999, 99910, 99921, 99922, 99923, 99924, 99925, 99926, 99927, 99928, 99929, 99920);
        integerList.forEach(System.out :: println);
        long c = integerList.stream().count();
        System.out.println(" " + System.currentTimeMillis());
        Optional<Integer> s = integerList.stream().reduce(Integer :: sum);
        System.out.println(" ===== " + System.currentTimeMillis());
        System.out.println("--------------");
        System.out.println("Stream Count: " + c + ", Size: " + integerList.size());
        s.ifPresent(str -> System.out.println("Optional: " + str));
        IntStream.range(0, 100).forEach(System.out :: print);
        Optional<Integer> optional = Stream.iterate(1, i -> i +1).limit(100).reduce(Integer :: sum);
        System.out.println("----------------");
        optional.ifPresent(System.out :: println);*/
    }
}
