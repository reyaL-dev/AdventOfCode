import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day11PartOne
{

    public static void main(String[] args)
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input11");
        ArrayList<Long> stoneLine = getInputFile(inputFile);

        long counter = 0;
        while (counter != 25)
        {
            counter++;
            blinkAction(stoneLine);
        }
        System.out.println(stoneLine.size());
    }

    private static ArrayList<Long> getInputFile(File inputFile)
    {
        ArrayList<Long> stoneList = new ArrayList<Long>();
        try
        {
            Scanner scanner = new Scanner(inputFile);
            while (scanner.hasNextLine())
            {
                String row = scanner.nextLine();
                String[] strNumbers = row.split(" ");

                for (String number : strNumbers)
                {
                    stoneList.add(Long.parseLong(number));
                }
            }
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        System.out.println(stoneList);
        return stoneList;
    }

    private static void blinkAction(ArrayList<Long> stoneLine)
    {
        for (int i = stoneLine.size() - 1; i >= 0; i--)
        {
            long currentVal = stoneLine.get(i);
            int numCount = String.valueOf(stoneLine.get(i)).length();
            if (currentVal == 0)
            {
                stoneLine.set(i, 1L);
            } else if (numCount % 2 == 0)
            {
                stoneLine.add(i, Long.parseLong(Long.toString(currentVal).substring(0, numCount / 2)));
                stoneLine.set(i + 1, Long.parseLong(Long.toString(currentVal).substring(numCount / 2, numCount)));
            } else
            {
                stoneLine.set(i, currentVal * 2024);
            }

        }
    }
}