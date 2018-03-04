package com.thales.models;

import javafx.geometry.Pos;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static int Distance(Position p1, Position p2) {
        return Math.abs(p2.getX() - p1.getX()) + Math.abs(p2.getY() - p1.getY());
    }

    @Override
    public boolean equals(Object obj) {
        Position p = (Position)obj;
        return x == p.x && y == p.y;
    }
}
