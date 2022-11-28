package org.fungover.day2;

public record SubPos(int horizontal, int depth) {

    public SubPos forward(int length){
        return new SubPos(horizontal + length, depth);
    }

    public SubPos up(int amount){
        return new SubPos(horizontal, depth - amount);
    }
    public SubPos down(int amount){
        return new SubPos(horizontal, depth + amount);
    }
}
