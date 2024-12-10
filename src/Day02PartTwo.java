import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day02PartTwo
{

    public static void main(String[] args)
    {
        System.out.println("Safe reports: " + parseFile());
    }

    private static int parseFile()
    {
        int safeReports = 0;
        File inputFile = new File("C:\\Project\\inputfiles\\input02");
        //File inputFile = new File("C:\\Project\inputfiles\\input02_test");

        {
            try
            {
                Scanner scanner = new Scanner(inputFile);
                while (scanner.hasNextLine())
                {
                    String row = scanner.nextLine();
                    String[] strNumbers = row.split(" ");

                    if (isSafe(strNumbers, 1))
                    {
                        safeReports++;
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

    private static boolean isSafe(String[] strNumbers, int badLevelTolerance)
    {

        boolean isDesc = (Integer.parseInt(strNumbers[0]) > Integer.parseInt(strNumbers[1]));

        for (int i = 0; i < strNumbers.length - 1; i++)
        {
            int rawDistance = Integer.parseInt(strNumbers[i + 1]) - Integer.parseInt(strNumbers[i]);
            int absDistance = Math.abs(rawDistance);

            if (!((isDesc ^ (rawDistance > 0)) && ((absDistance <= 3) && (absDistance >= 1))))
            {

                if (badLevelTolerance == 0)
                {
                    return false;
                }

                if (i == 0)
                {
                    return (isSafe(removeEntry(strNumbers, 0), badLevelTolerance - 1) || isSafe(removeEntry(strNumbers, i + 1), badLevelTolerance - 1));
                }
                return (isSafe(removeEntry(strNumbers, i), badLevelTolerance - 1) || isSafe(removeEntry(strNumbers, i + 1), badLevelTolerance - 1) || isSafe(removeEntry(strNumbers, i - 1), badLevelTolerance - 1));
            }
        }
        return true;
    }

    public static String[] removeEntry(String[] strNumbers, int removeIndex)
    {
        String[] strNumbersSub = new String[strNumbers.length - 1];
        System.arraycopy(strNumbers, 0, strNumbersSub, 0, removeIndex);
        System.arraycopy(strNumbers, removeIndex + 1, strNumbersSub, removeIndex, strNumbers.length - removeIndex - 1);
        return strNumbersSub;
    }

}

