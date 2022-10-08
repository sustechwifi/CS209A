package practice.lab;

import java.io.IOException;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Practice4 {
    private static String path = "C:\\Users\\25874\\Desktop\\CS209A\\labs\\src\\main\\resources\\cities.txt";

    public static class City {
        private String name;
        private String state;
        private int population;

        @Override
        public String toString() {
            return "City{" +
                    "name='" + name + '\'' +
                    ", state='" + state + '\'' +
                    ", population=" + population +
                    '}';
        }

        public City(String name, String state, int population) {
            this.name = name;
            this.state = state;
            this.population = population;
        }

        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }

        public int getPopulation() {
            return population;
        }

    }

    public static Stream<City> readCities(String filename) throws IOException {
        return Files.lines(Paths.get(filename))
                .map(l -> l.split(", "))
                .map(a -> new City(a[0], a[1], Integer.parseInt(a[2])));
    }

    public static void main(String[] args) throws IOException, URISyntaxException {

        Stream<City> cities = readCities(path);
        // Q1: count how many cities there are for each state
        // TODO: Map<String, Long> cityCountPerState = ...
        System.out.println("of cities per state:");
        Map<String, Long> cityCountPerState = cities.collect(Collectors.groupingBy(City::getState, Collectors.counting()));
        System.out.println(cityCountPerState.toString());
        System.out.println();

        System.out.println("population per state:");
        cities = readCities(path);
        // Q2: count the total population for each state
        // TODO: Map<String, Integer> statePopulation = ...
        Map<String, Integer> statePopulation = cities.collect(
                Collectors.groupingBy(City::getState, Collectors.summingInt(City::getPopulation)));
        System.out.println(statePopulation);
        System.out.println();
        cities = readCities(path);
        System.out.println("get the set of cities with >500,000 population:");
        // Q3: for each state, get the set of cities with >500,000 population
        // TODO: Map<String, Set<City>> largeCitiesByState = ...
        Map<String, Set<City>> largeCitiesByState =
                cities.collect(Collectors.groupingBy(City::getState, Collectors.toSet()));
        largeCitiesByState.forEach((s, c) -> {
            List<City> collect = c.stream().filter(city -> city.getPopulation() > 500000).toList();
            System.out.println(s + ":" + collect);
        });
    }
}
