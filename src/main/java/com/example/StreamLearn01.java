package com.example;

/**
 * Created by Xue on 02/01/17.
 */
public class StreamLearn01 {
/*
    public static void main(String[] args) {

        List<Integer> number1 = Arrays.asList(1, 2, 3);
        List<Integer> number2 = Arrays.asList(4, 5);

        List<int[]> pairs = number1.stream().flatMap(i ->
                                number2.stream().map(j -> new int[] {i, j}))
                                    .collect(Collectors.toList());

        pairs.forEach(l -> System.out.println(l[0] + " : " + l[1]));

        *//**
         *
         *//*
        List<List<Integer>> outer = new ArrayList<>();
        List<Integer> inner1 = new ArrayList<>();
        inner1.add(1);
        List<Integer> inner2 = new ArrayList<>();
        inner1.add(2);
        List<Integer> inner3 = new ArrayList<>();
        inner1.add(3);
        List<Integer> inner4 = new ArrayList<>();
        inner1.add(4);
        List<Integer> inner5 = new ArrayList<>();
        inner1.add(5);
        outer.add(inner1);
        outer.add(inner2);
        outer.add(inner3);
        outer.add(inner4);
        outer.add(inner5);
        outer.stream().flatMap(inner -> inner.stream().map(i -> i)).forEach(System.out::println);

        List<Integer> l1 = new ArrayList<>();
        l1.add(1);
        l1.add(2);
        l1.add(3);
        l1.add(4);
        List<Integer> l2 = new ArrayList<>();
        l2.add(5);
        l2.add(6);
        l2.add(7);
        l2.add(8);

//        Stream.of(l1, l2).flatMap(n -> n.stream()).filter(n -> n%2 == 0).forEach(System.out::println);
        List<Integer> list = Stream.of(l1, l2).flatMap(n -> n.stream()).filter(n -> n%2 == 0).collect(Collectors.toList());
        Stream.of(l1, l2).flatMap(n -> n.stream()).filter(n -> n%2 == 0).map(i -> i + " : ").forEach(System.out::print);
    }*/
}
