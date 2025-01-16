import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class Day05PartTwo
{

    public static void swap(final Object array, final int i, final int j)
    {
        final Object atI = Array.get(array, i);
        Array.set(array, i, Array.get(array, j));
        Array.set(array, j, atI);
    }

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
                    String[] parts = row.split(",");
                    int[] pageArray = new int[parts.length];
                    for (int i = 0; i < parts.length; i++)
                    {
                        pageArray[i] = Integer.parseInt(parts[i]);
                    }
                    validNum += getValidMidValue(pageArray, pageNumMap, false);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        // Debug Test-Out
        for (Map.Entry<Integer, List<Integer>> entry : pageNumMap.entrySet())
        {
            System.out.println("Entry: " + entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("Sum: " + validNum);
    }


    private static int getValidMidValue(int[] pageArray, Map<Integer, List<Integer>> pageNumMap, boolean isInvalid)
    {


        // Check for violations
        System.out.println("Currently checked Array: " + Arrays.toString(pageArray));
        for (int i = 1; i < pageArray.length; i++)
        {
            if (pageNumMap.get(pageArray[i]) != null)
            {
                int[] prevPageSubarray = Arrays.copyOfRange(pageArray, 0, i);
                for (int j = 0; j < prevPageSubarray.length; j++)
                {
                    if (pageNumMap.get(pageArray[i]).contains(prevPageSubarray[j]))
                    {
                        swap(pageArray, i, j);
                        System.out.println("Swapped Array: " + Arrays.toString(pageArray));
                        return getValidMidValue(pageArray, pageNumMap, true);
                    }
                }
            }
        }
        System.out.println("Found Proper value: " + pageArray[pageArray.length / 2]);
        if (!isInvalid)
        {
            return 0;
        }
        return pageArray[pageArray.length / 2];
    }

}


