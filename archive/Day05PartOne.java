import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day05PartOne
{

    public static void main(String[] args)
    {

        File inputFile = new File("C:\\Project\\inputfiles\\input05");
        int validNum = 0;
        Map<Integer, List<Integer>> pageNumMap = new HashMap<>();

        try
        {
            Scanner scanner = new Scanner(inputFile);
            while (scanner.hasNextLine())
            {
                String row = scanner.nextLine();
                if (row.contains("|"))
                {
                    String[] parts = row.split("\\|");
                    int num1 = Integer.parseInt(parts[0].trim());
                    int num2 = Integer.parseInt(parts[1].trim());

                    pageNumMap.putIfAbsent(num1, new ArrayList<>());
                    pageNumMap.get(num1).add(num2);
                }

                if (row.contains(","))
                {
                    validNum += getValidMidValue(row, pageNumMap);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        // Test-Out
        for (Map.Entry<Integer, List<Integer>> entry : pageNumMap.entrySet())
        {
            System.out.println("Entry: " + entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("Sum: " + validNum);
    }

    private static int getValidMidValue(String row, Map<Integer, List<Integer>> pageNumMap)
    {
        String[] parts = row.split(",");
        int[] pageArray = new int[parts.length];
        for (int i = 0; i < parts.length; i++)
        {
            pageArray[i] = Integer.parseInt(parts[i]);
        }

        // Check for violations
        for (int i = 1; i < pageArray.length; i++)
        {
            if (pageNumMap.get(pageArray[i]) != null)
            {
                int[] prevPageSubarray = Arrays.copyOfRange(pageArray, 0, i);
                for (int value : prevPageSubarray)
                {
                    if (pageNumMap.get(pageArray[i]).contains(value))
                    {
                        return 0;
                    }
                }
            }
        }
        System.out.println("Found Proper value: " + pageArray[pageArray.length / 2]);
        return pageArray[pageArray.length / 2];
    }
}


