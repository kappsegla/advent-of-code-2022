package org.fungover.day9;

public record Point(int x, int y, int height) {
    @Override
    public String toString() {
        return "" + height;
    }
}
