package org.fungover.day2;

public record SubPosAim(int horizontal, int depth, int aim) {

    public SubPosAim forward(int amount){
        return new SubPosAim(horizontal + amount, depth + aim * amount,aim);
    }

    public SubPosAim up(int amount){
        return new SubPosAim(horizontal, depth, aim - amount);
    }
    public SubPosAim down(int amount){
        return new SubPosAim(horizontal, depth, aim + amount);
    }
}
