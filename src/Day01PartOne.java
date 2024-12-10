import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day01PartOne
{

    public static void main(String[] args)
    {
        List<Integer> firstList = new ArrayList<>();
        List<Integer> secondList = new ArrayList<>();

        File inputFile = new File("C:\\Project\\inputfiles\\input01");
        {
            try
            {
                Scanner scanner = new Scanner(inputFile);
                while (scanner.hasNextLine())
                {
                    String row = scanner.nextLine();
                    String[] strNumbers = row.split("   ");

                    firstList.add(Integer.parseInt(strNumbers[0]));
                    secondList.add(Integer.parseInt(strNumbers[1]));
                }
            }
            catch (FileNotFoundException e)
            {
                throw new RuntimeException(e);
            }

            // Sort both lists
            Collections.sort(firstList);
            Collections.sort(secondList);

            // Calculate distances
            int totalDistance = 0;
            for (int i = 0; i < firstList.size(); i++)
            {
                totalDistance += Math.abs(firstList.get(i) - secondList.get(i));
            }

            System.out.println("Total Distance: " + totalDistance);
        }
    }
}
