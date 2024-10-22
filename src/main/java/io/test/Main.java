package io.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static final String FILE_NAME = "measurements-1b.txt";

    public static void main(String[] args) throws IOException {
        HashMap<String, Double[]> cities = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = br.readLine();

            while (line != null) {
                String[] split = line.split(";");
                Double value = Double.parseDouble(split[1]);

                if(!cities.containsKey(split[0])){
                    Double[] newList = new Double[4];
                    newList[0] = Double.MAX_VALUE;
                    newList[1] = 0.0;
                    newList[2] = Double.MIN_VALUE;
                    newList[3] = 0.0;
                    cities.put(split[0], newList);
                }
                Double[] arr = cities.get(split[0]);

                if (arr[0] > value) arr[0] = value;
                else if (arr[2] < value) arr[2] = value;
                arr[1] += value;
                arr[3] += 1;

                line = br.readLine();
            }

            List<String> results = cities.keySet().stream().sorted().map(key -> {
                Double[] temperatures = cities.get(key);
                String min = String.format("%.1f", temperatures[0]);
                String max = String.format("%.1f", temperatures[2]);
                String mean = String.format("%.1f", temperatures[1] / temperatures[3]);
                return key+"="+min+"/"+mean+"/"+max;
            }).toList();

            System.out.println("{" + String.join(", ", results) + "}");

        } catch (Exception e) {
            System.err.println(e);
            throw e;
        }
    }
}