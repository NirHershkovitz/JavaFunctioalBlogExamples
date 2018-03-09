package blog.codejunkie;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class ParallelStreamExample {
    private static List<Long> largeList = LongStream.range(1, 10000000).boxed().collect(Collectors.toList());

    private static class Counter {
        private static class InnerCounter {
            AtomicInteger reduce = new AtomicInteger();
            AtomicInteger combine = new AtomicInteger();

            volatile Map<Thread, AtomicInteger> threadsUsage = new HashMap<>();

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("InnerCounter{");
                sb.append("reduce=").append(reduce);
                sb.append(", combine=").append(combine);
                sb.append(", threadsUsage=").append(threadsUsage);
                sb.append('}');
                return sb.toString();//.replace(",",",\n");
            }
        }
        InnerCounter arrayList = new InnerCounter();
        InnerCounter linkedList = new InnerCounter();

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Counter{");
            sb.append("arrayList=").append(arrayList);
            sb.append(", linkedList=").append(linkedList);
            sb.append('}');
            return sb.toString();
        }
    }

    private static Counter counter = new Counter();

    public static void example1() {
        recordTime("Regular sum", () -> LongStream.range(1, 10000000).sum());
        recordTime("Parallel sum", () -> LongStream.range(1, 10000000).parallel().sum());
    }

    public static void example2() {
        largeList = LongStream.range(1, 10000000).boxed().collect(Collectors.toList());
        recordTime("Regular sum", () -> largeList.stream().reduce(0L, Long::sum));
        recordTime("Parallel ArrayList sum",
                () -> new ArrayList<>(largeList).parallelStream().reduce(0L, Long::sum));
        recordTime("Parallel LinkedList sum",
                () -> new LinkedList<>(largeList).parallelStream().reduce(0L, Long::sum));


    }
    public static void main(String [] args) {
        example1();
        example2();

        new LinkedList<>(largeList).parallelStream().reduce(0L,
                (a, b) -> sumAndIncreaseCounter(a, b, counter.linkedList.reduce, counter.linkedList.threadsUsage),
                (a, b) -> sumAndIncreaseCounter(a, b, counter.linkedList.combine, counter.linkedList.threadsUsage));
        new ArrayList<>(largeList).parallelStream().reduce(0L,
                (a, b) -> sumAndIncreaseCounter(a, b, counter.arrayList.reduce, counter.linkedList.threadsUsage),
                (a, b) -> sumAndIncreaseCounter(a, b, counter.arrayList.combine, counter.linkedList.threadsUsage));

        System.out.println(counter);
    }

    private static synchronized Long sumAndIncreaseCounter(Long a, Long b, AtomicInteger counter, Map<Thread, AtomicInteger> threadsUsage) {
        counter.getAndIncrement();
        threadsUsage.putIfAbsent(Thread.currentThread(), new AtomicInteger());
        threadsUsage.get(Thread.currentThread()).getAndIncrement();
        return a + b;
    }

    private static void recordTime(String method, Supplier<Long> supplier) {
        ZonedDateTime now = ZonedDateTime.now();
        supplier.get();
        long seconds = now.until(ZonedDateTime.now(), ChronoUnit.MICROS);
        System.out.println(String.format("%s took %,d micro seconds", method, seconds));
    }
}
