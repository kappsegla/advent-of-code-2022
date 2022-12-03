package org.fungover.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StreamUtils {
    public static <T> Collection<List<T>> createRows(List<T> inputList, int columnsPerRow) {
        AtomicInteger counter = new AtomicInteger();

        return inputList.stream().collect(Collectors.groupingBy(gr -> counter.getAndIncrement() / columnsPerRow)).values();
    }
}
