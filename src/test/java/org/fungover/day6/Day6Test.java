package org.fungover.day6;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class Day6Test {


    @Test
    void exampleInputShouldGive7() {
        var input = "mjqjpqmgbljsphdztnvjfqwrcgsmlb";

        var result = Day6.packetMarker(input);

        assertThat(result).isEqualTo(7);
    }

    @Test
    void examplInputShouldGive5() {
        var input = "bvwbjplbgvbhsrlpgdmjqwftvncz";

        var result = Day6.packetMarker(input);

        assertThat(result).isEqualTo(5);
    }

    @ParameterizedTest
    @CsvSource({"nppdvjthqldpwncqszvftbrmjlhg,6",
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg,10",
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw,11"})
    void exampleInputShouldGiveExpectedResult(String input, int expected) {
        var result = Day6.packetMarker(input);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void numberOfProcessedCharactersForMessageMarkerShouldReturn19() {
        var input = "mjqjpqmgbljsphdztnvjfqwrcgsmlb";

        var result = Day6.messageMarker(input);

        assertThat(result).isEqualTo(19);
    }

    @Test
    void numberOfProcessedCharactersForMessageMarkerShouldReturn23() {
        var input = "bvwbjplbgvbhsrlpgdmjqwftvncz";

        var result = Day6.messageMarker(input);

        assertThat(result).isEqualTo(23);
    }

    @Test
    void isUniqueGivenAbcShouldReturnTrue() {
        assertThat(Day6.isUnique("abc")).isTrue();
    }

    @Test
    void isUnigueGivenaabshouldReturnFalse() {
        assertThat(Day6.isUnique("aab")).isFalse();
    }
    
    


}