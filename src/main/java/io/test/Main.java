package io.test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static final String FILE_NAME = "measurements-1b.txt";

    public static void main(String[] args) throws IOException {
        try (Stream<String> lines = java.nio.file.Files.lines(Paths.get(FILE_NAME))) {
            Map<String, Double[]> map = lines
                    .parallel()
                    .map(line -> line.split(";"))
                    .reduce(
                            new ConcurrentHashMap<>(),
                            (hashMap, e) -> {
                                Double value = Double.parseDouble(e[1]);
                                if(!hashMap.containsKey(e[0])) {
                                    Double[] newList = new Double[4];
                                    newList[0] = Double.MAX_VALUE;
                                    newList[1] = 0.0;
                                    newList[2] = Double.MIN_VALUE;
                                    newList[3] = 0.0;
                                    hashMap.put(e[0], newList);
                                }
                                Double[] arr = hashMap.get(e[0]);
                                if (arr[0] > value) arr[0] = value;
                                else if (arr[2] < value) arr[2] = value;
                                arr[1] += value;
                                arr[3] += 1;
                                return hashMap;
                            },
                            (m, m2) -> {
                                m.putAll(m2);
                                return m;
                            }
                    );

            System.out.println(
                    map.keySet().stream().sorted().map(key -> {
                        Double[] temperatures = map.get(key);
                        String min = String.format("%.1f", temperatures[0]);
                        String max = String.format("%.1f", temperatures[2]);
                        String mean = String.format("%.1f", temperatures[1] / temperatures[3]);
                        return key+"="+min+"/"+mean+"/"+max;
                    }).collect(Collectors.joining(", ", "{", "}"))
            );
        }
    }
}