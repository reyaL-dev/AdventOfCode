import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day11PartTwo
{

    public static void main(String[] args)
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input11");
        List<Long> stoneLine = getInputFile(inputFile);
        System.out.println(blinkAction(stoneLine, 75));
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

    static List<Long> handleStone(Long stone)
    {
        int numCount = String.valueOf(stone).length();
        if (stone == 0L)
        {
            return List.of(1L);
        } else if (numCount % 2 == 0)
        {
            return List.of(Long.parseLong(Long.toString(stone).substring(0, numCount / 2)), Long.parseLong(Long.toString(stone).substring(numCount / 2, numCount)));
        } else
        {
            return List.of(stone * 2024);
        }
    }

    static long blinkAction(List<Long> stoneLine, int blinks)
    {
        Map<Long, Long> cachedActions = new HashMap<>();
        List<Long> nextStoneLine = new ArrayList<>(stoneLine);
        for (Long stone : nextStoneLine)
        {
            cachedActions.put(stone, cachedActions.getOrDefault(stone, 0L) + 1);
        }

        for (int i = 0; i < blinks; i++)
        {
            Map<Long, Long> nextCachedActions = new HashMap<>();
            for (Map.Entry<Long, Long> entry : cachedActions.entrySet())
            {
                nextStoneLine = handleStone(entry.getKey());
                for (Long stone : nextStoneLine)
                {
                    nextCachedActions.put(stone, nextCachedActions.getOrDefault(stone, 0L) + entry.getValue());
                }
            }
            cachedActions = nextCachedActions;
        }
        System.out.println(cachedActions);
        return cachedActions.values().stream().mapToLong(l -> l).sum();
    }

}
