import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day07PartOne
{

    public static void main(String[] args)
    {

        File inputFile = new File("C:\\Project\\inputfiles\\input07");
        long validNum = 0;

        try
        {
            Scanner scanner = new Scanner(inputFile);
            while (scanner.hasNextLine())
            {
                validNum += fetchNumbers(scanner);
                System.out.println("Current score: " + validNum);
            }
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("Sum: " + validNum);
    }

    private static long fetchNumbers(Scanner scanner)
    {
        String row = scanner.nextLine();

        String[] inputRow = row.split("\\:");
        long target = Long.parseLong(inputRow[0].trim());

        System.out.println("Evaluating: " + target);
        String[] parts = inputRow[1].trim().split(" ");
        long[] evaluatingNums = new long[parts.length];

        for (int i = 0; i < evaluatingNums.length; i++)
        {
            evaluatingNums[i] = Long.parseLong(parts[i]);
        }

        return evaluateNumbers(evaluatingNums, target);
    }

    public static long evaluateNumbers(long[] numbers, long target)
    {
        return findCombination(numbers, 0, numbers[0], target);
    }


    private static long findCombination(long[] numbers, int index, long currentResult, long target)
    {
        if (index == numbers.length - 1)
        {
            if (currentResult == target)
            {
                return currentResult;
            } else
            {
                return 0;
            }
        }

        long nextNumber = numbers[index + 1];

        long resultWithAddition = findCombination(numbers, index + 1, currentResult + nextNumber, target);
        if (resultWithAddition != 0)
        {
            return resultWithAddition;
        }

        long resultWithMultiplication = findCombination(numbers, index + 1, currentResult * nextNumber, target);
        return resultWithMultiplication;
    }

}


