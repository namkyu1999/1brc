package io.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static final String FILE_NAME = "measurements-1m.txt";

    public static void main(String[] args) throws IOException {
        HashMap<String, List<String>> cities = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = br.readLine();

            while (line != null) {
                String[] split = line.split(";");
                if(cities.containsKey(split[0])){
                    cities.get(split[0]).add(split[1]);
                }else{
                    ArrayList<String> newList = new ArrayList<>();
                    newList.add(split[1]);
                    cities.put(split[0], newList);
                }
                line = br.readLine();
            }

            List<String> results = cities.keySet().stream().sorted().map(key -> {
                List<String>temperatures = cities.get(key);
                Collections.sort(temperatures);
                String min = String.format("%.1f", Double.parseDouble(temperatures.getFirst()));
                String max = String.format("%.1f", Double.parseDouble(temperatures.getLast()));
                String mean = String.format("%.1f", temperatures.stream().mapToDouble(Double::parseDouble).average().getAsDouble());
                return key+"="+min+"/"+mean+"/"+max;
            }).toList();

            System.out.println("{" + String.join(", ", results) + "}");

        } catch (Exception e) {
            System.err.println(e);
            throw e;
        }
    }
}