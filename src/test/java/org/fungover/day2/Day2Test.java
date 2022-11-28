package org.fungover.day2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class Day2Test {

    @Test
    void calculateSubmarinePosition() {
        String input = """
                forward 5
                down 5
                forward 8
                up 3
                down 8
                forward 2
                """;

        SubPos subPos = Day2.calculatePosition(input);

        assertThat(subPos)
                .hasFieldOrPropertyWithValue("horizontal", 15)
                .hasFieldOrPropertyWithValue("depth", 10);
    }

    @Test
    void calculateSubmarinePositionWithAim() {
        String input = """
                forward 5
                down 5
                forward 8
                up 3
                down 8
                forward 2
                """;

        SubPosAim subPos = Day2.calculatePositionWithAim(input);

        assertThat(subPos)
                .hasFieldOrPropertyWithValue("horizontal", 15)
                .hasFieldOrPropertyWithValue("depth", 60);
    }

}