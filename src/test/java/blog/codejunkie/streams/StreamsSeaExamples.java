package blog.codejunkie.streams;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
class StreamsSeaExamples {
    private List<Sea> seas = Arrays.asList(
            new Sea("Baltic Sea", "Europe, Africa, and Asia",1641650 ),
            new Sea("Caribbean Sea", "Americas", 2754000),
            new Sea("Mediterranean Sea", "Europe, Africa, and Asia", 2500000)
    );

    @Test
    void shouldFindLargestSeaArea() {
        Optional<Integer> largestArea = seas.stream().
                map(Sea::getArea).
                max(Comparator.naturalOrder());

        assertTrue(largestArea.isPresent());
        assertEquals(2754000, largestArea.get().intValue());
    }

    @Test
    void shouldSortSeaAreas() {
        List<Integer> sortedAreas = seas.stream().
                map(Sea::getArea).
                sorted().
                collect(Collectors.toList());

        assertEquals(Arrays.asList(1641650,2500000, 2754000), sortedAreas);
    }

    @Test
    void shouldFilterSeasAreasSmallerThan2MSqKM() {
        List<Integer> sortedAreas = seas.stream().
                map(Sea::getArea).
                filter(area -> area > 2000000).
                collect(Collectors.toList());

        assertEquals(Arrays.asList(2754000, 2500000), sortedAreas);
    }

    @Test
    void shouldFilterSeasAreasSmallerThan2MSqKMShowingOperationOrder() {
        Predicate<Integer> predicate = i -> {
            System.out.println("filter: " + i);
            return i > 2000000;
        };

        Function<Sea, Integer> map = s -> {
            System.out.println("map: " + s);
            return s.getArea();
        };
        seas.stream().
                map(map).
                filter(predicate).
                forEach(i -> System.out.println("forEach: " + i));
     }

    @Test
    void filterSeasSmallerThan2MSqKMAndSort() {
        List<Sea> result  = seas.stream().
                filter(sea -> sea.getArea() > 2000000).
                sorted(Comparator.comparing(Sea::getArea)).
                collect(Collectors.toList());
        assertThat(result, contains(seas.get(2), seas.get(1)));
    }

    @Test
    void filterSeasSmallerThan2MSqKMAndSortOrderMattersExample() {
        Predicate<Sea> seaPredicate = sea -> {
            System.out.println("filter: " + sea);
            return sea.getArea() > 2000000;
        };
        Comparator<Sea> seaComparator = (s1, s2) -> {
            System.out.println("sort: " + s1 + " ? " + s2);
            return s1.getArea() - s2.getArea();
        };

        seas.stream().
                filter(seaPredicate).
                sorted(seaComparator).
                collect(Collectors.toList());

        System.out.println("-------------------------");

        seas.stream().
                sorted(seaComparator).
                filter(seaPredicate).
                collect(Collectors.toList());
    }

    @Test
    void shouldCalculateAverageSeaAreas() {
        OptionalDouble averageArea = seas.stream().mapToInt(Sea::getArea).average();

        assertTrue(averageArea.isPresent());
        assertEquals(2298550.0, averageArea.getAsDouble());
    }

    @Test
    void shouldFindLargestSeaUsingReduce() {
        Optional<Sea> largestSea =
                seas.stream().reduce((s1, s2) -> s1.getArea() > s2.getArea() ? s1 : s2);
        assertSeaName(largestSea, "Caribbean Sea");
    }

    @Test
    void shouldFindLargestSeaAreaUsingMax() {
       Optional<Sea> largestSea = seas.stream().
               max(Comparator.comparing(Sea::getArea));
       assertSeaName(largestSea, "Caribbean Sea");
    }

    @Test
    void shouldFindLargestSeaAreaUsingMaxExplicitComparator() {
        Optional<Sea> largestSea = seas.stream().
                max((s1, s2) -> s1.getArea() > s2.getArea() ? 1 : -1);
        assertSeaName(largestSea, "Caribbean Sea");
    }

    @Test
    public void shouldGroupByRegion() {
        Map<String, List<Sea>> result = seas.stream().collect(Collectors.groupingBy(Sea::getRegion));
        assertEquals(2, result.size());
        assertThat(result.get("Europe, Africa, and Asia"), contains(seas.get(0), seas.get(2)));
        assertThat(result.get("Americas"), contains(seas.get(1)));
    }


    @Test void mapToFunctionExample() {
        Map<String, Function<Sea, String>> printFunctionByRegion =
                Map.of("Americas", s -> String.format("Size is %.2f million square miles", s.getArea()  * 0.386),
                       "Europe, Africa, and Asia", s -> String.format("Size is %s million square kilometers", s.getArea()));
        seas.stream().
                map(s -> printFunctionByRegion.get(s.getRegion()).apply(s)).
                forEach(System.out::println);
    }
    private void assertSeaName(Optional<Sea> largestSea, String sea) {
        assertTrue(largestSea.isPresent());
        assertEquals(sea, largestSea.get().getName());
    }

}