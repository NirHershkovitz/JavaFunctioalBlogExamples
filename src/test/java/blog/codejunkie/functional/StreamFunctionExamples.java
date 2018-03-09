package blog.codejunkie.functional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StreamFunctionExamples {
    private int numberOfInvocation;

    private Collection<Supplier<Boolean>> suppliers = Arrays.asList(
            () -> {numberOfInvocation++; return true;},
            () -> {numberOfInvocation++; return true;},
            () -> {numberOfInvocation++; return false;},
            () -> {numberOfInvocation++; return true;});

    @BeforeEach
    public void setup() {
        numberOfInvocation = 0;
    }

    @Test
    public void usingReduceInvokesAllSuppliers() {
        Optional<Boolean> result = suppliers.stream().
                map(s -> s.get()).
                reduce((a, b) -> a && b);

        assertTrue(result.isPresent());
        assertEquals(false, result.get());
        assertEquals(4, numberOfInvocation);
    }

    @Test
    public void usingAllMatchStopsOnTheFirstFalseInvocation() {
        boolean result = suppliers.stream().
                allMatch(s -> s.get());

        assertEquals(false, result);
        assertEquals(3, numberOfInvocation);
    }

    @Test
    public void usingFindFirstStopsOnTheFirstMath() {
        Optional<Supplier<Boolean>> firstFalseSupplier = suppliers.stream().
                filter(i -> !i.get()).
                findFirst();

        assertEquals(3, numberOfInvocation);
        assertTrue(firstFalseSupplier.isPresent());
        assertFalse(firstFalseSupplier.get().get());
    }

    @Test
    public void usingReduceAndMapToCountWhichValueAppearsTheMost() {
        Optional<Boolean> moreTrue = suppliers.stream().
                map(s -> s.get() ? 1 : -1).
                reduce((a, b) -> a + b).
                map(i -> i > 0);

        assertTrue(moreTrue.isPresent());
        assertEquals(true, moreTrue.get());
    }

}
