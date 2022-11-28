package org.fungover.day1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class Day1Test {

    @Test
    void countNumberOfIncreases() {
        String input = """
                199
                200
                208
                210
                200
                207
                240
                269
                260
                263
                """;

        assertThat(Day1.count(input)).isEqualTo(7);
    }

    @Test
    void countNumberOfIncreasesSlidingWindowOf3() {
        String input = """
                199
                200
                208
                210
                200
                207
                240
                269
                260
                263
                """;

        assertThat(Day1.countSlidingWindow(input)).isEqualTo(5);
    }

}