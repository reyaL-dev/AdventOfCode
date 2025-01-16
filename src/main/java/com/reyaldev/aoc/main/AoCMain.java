package com.reyaldev.aoc.main;

import com.reyaldev.aoc.downloader.Downloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AoCMain
{

    static String inputfileFolder = "inputfiles";

    public static void main(String[] args) throws Exception
    {
        //downloadInputs();
        List<Supplier<Day>> days = List.of(
                Day01::new);
        List<Duration> durations = new ArrayList<>();
        Instant bigStart = Instant.now();
        days.forEach(daySupplier -> {
            Instant start = Instant.now();
            Day d = daySupplier.get();
            System.out.println(d.part1());
            Instant part1 = Instant.now();
            System.out.println(d.part2());
            Instant end = Instant.now();
            durations.add(Duration.between(start, end));
            System.out.println("  part 1 time: " + Duration.between(start, part1).toMillis());
            System.out.println("  part 2 time: " + Duration.between(part1, end).toMillis());
            System.out.println("full day time: " + Duration.between(start, end).toMillis());
        });
        Instant bigEnd = Instant.now();
        System.out.println("Full execution time: " + Duration.between(bigStart, bigEnd).toMillis());
        System.out.println("   Sum of all times: " + durations.stream().reduce(Duration.ZERO, Duration::plus).toMillis());

    }

    public static String readInput(int dayNumber)
    {
        String fileName = String.format(inputfileFolder + "/input-%02d.txt", dayNumber);
        try
        {
            File file = new File(fileName).getAbsoluteFile().getCanonicalFile();
            return Files.readString(file.toPath());
        }
        catch (IOException ioe)
        {
            throw new IllegalArgumentException("Can not read input for day: " + dayNumber, ioe);
        }
    }

    private static void downloadInputs()
    {
        String sessionId = System.getenv("AOC_SESSION_ID");
        if (sessionId == null || sessionId.isBlank())
        {
            System.err.println("Session ID in environment variable AOC_SESSION_ID is not set correctly!");
            System.exit(1);
        }
        new Downloader(sessionId, 2024, inputfileFolder).downloadInputs();
    }
}
