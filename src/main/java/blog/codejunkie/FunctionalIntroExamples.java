package blog.codejunkie;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Functional intro examples
 *
 * @author Nir Hershkovitz
 */
public class FunctionalIntroExamples {

    private static void functionalVsLoopExample() {
        List<String> l = Arrays.asList("one", "two", "tree");

        System.out.println("Print using a plain old for loop");
        for (String s : l) {
            System.out.println(s);
        }

        System.out.println("Print using a function");
        l.forEach(s -> System.out.println(s));
    }

    private static void usingMethodRef() {
        List<String> l = Arrays.asList("one", "two", "tree");
        l.forEach(System.out::println);
    }


    private static void functionExplainedExample() {
        List<String> l = Arrays.asList("one", "two", "tree");

        System.out.println("forEach is receiving a function, which is defined by the Consumer functional interface");
        Consumer<String> consumer = s -> System.out.println(s);
        l.forEach(consumer);

        System.out.println("Here is the verbose version, using the Consumer interface");
        Consumer<String> verboseConsumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        l.forEach(verboseConsumer);

    }

    public static void main(String [] args) {
        functionalVsLoopExample();
        usingMethodRef();
        functionExplainedExample();
    }
}
