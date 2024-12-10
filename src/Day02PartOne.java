import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day02PartOne
{

    public static void main(String[] args)
    {
        System.out.println("Safe reports: " + parseFile());
    }

    private static int parseFile()
    {
        int safeReports = 0;
        File inputFile = new File("C:\\Project\\inputfiles\\input02");
        {
            try
            {
                Scanner scanner = new Scanner(inputFile);
                while (scanner.hasNextLine())
                {
                    String row = scanner.nextLine();
                    String[] strNumbers = row.split(" ");

                    if (Integer.parseInt(strNumbers[0]) > Integer.parseInt(strNumbers[1]))
                    {
                        if (isSafe(strNumbers, true))
                        {
                            safeReports++;
                        }
                    } else if (Integer.parseInt(strNumbers[0]) < Integer.parseInt(strNumbers[1]))
                    {
                        if (isSafe(strNumbers, false))
                        {
                            safeReports++;
                        }
                    }
                }
            }
            catch (FileNotFoundException e)
            {
                throw new RuntimeException(e);
            }
            return safeReports;
        }
    }

    private static boolean isSafe(String[] strNumbers, boolean isDesc)
    {
        for (int i = 0; i < strNumbers.length - 1; i++)
        {
            int distance = Integer.parseInt(strNumbers[i + 1]) - Integer.parseInt(strNumbers[i]);

            if (isDesc)
            {
                if (!((distance <= -1) && (distance >= -3)))
                {
                    return false;
                }
            } else
            {
                if (!((distance <= 3) && (distance >= 1)))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
