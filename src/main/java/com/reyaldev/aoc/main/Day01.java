package com.reyaldev.aoc.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.reyaldev.aoc.main.AoCMain.readInput;

public class Day01 extends AbstractDay
{

    private List<Integer> firstList = new ArrayList<>();
    private List<Integer> secondList = new ArrayList<>();

    public Day01()
    {
        this(readInput(1));
    }

    public Day01(String input)
    {
        super(input, 1);
        List<String> lines = new ArrayList<>();
        for (var line : input.split("\n"))
        {
            lines.add(line.trim());
        }

        for (int i = 0; i < lines.size(); i++)
        {
            String line = lines.get(i);
            String[] strNumbers = line.split("   ");

            firstList.add(Integer.parseInt(strNumbers[0]));
            secondList.add(Integer.parseInt(strNumbers[1]));
        }
        Collections.sort(firstList);
        Collections.sort(secondList);
    }

    public static void main(String[] args)
    {
        AbstractDay day01 = new Day01();
        System.out.println(day01.part1());
        System.out.println(day01.part2());
    }

    private static int countOccurrences(List<Integer> list, int target)
    {
        int count = 0;

        for (int num : list)
        {
            if (num == target)
            {
                count++;
            }
        }
        return count;
    }

    @Override
    public String part1()
    {
        long totalDistance = 0L;
        for (int i = 0; i < firstList.size(); i++)
        {
            totalDistance += Math.abs(firstList.get(i) - secondList.get(i));
        }
        return Long.toString(totalDistance);
    }

    @Override
    public String part2()
    {
        long similarityScore = 0L;
        for (int i = 0; i < firstList.size(); i++)
        {
            similarityScore += firstList.get(i) * countOccurrences(secondList, firstList.get(i));
        }
        return Long.toString(similarityScore);
    }
}
