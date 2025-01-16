package com.reyaldev.aoc.main;

import static com.reyaldev.aoc.main.AoCMain.readInput;

abstract class AbstractDay implements Day
{
    protected final String INPUT;
    protected final int dayNumber;

    AbstractDay(int dayNumber)
    {
        this(readInput(dayNumber), dayNumber);
    }

    AbstractDay(String input, int dayNumber)
    {
        this.INPUT = input;
        this.dayNumber = dayNumber;
    }
}
