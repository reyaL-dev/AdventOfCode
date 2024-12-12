import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day12PartOne
{

    public static void main(String[] args) throws IOException
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input12");
        List<List<Character>> origMap = loadMapFromFile(inputFile);
        System.out.println("Total Price of regions: " + getTotalPrice(origMap));
    }

    private static int getTotalPrice(List<List<Character>> origMap)
    {
        Set<Coords> visitedPos = new HashSet<>();
        int totalPrice = 0;
        for (int i = 0; i < origMap.size(); i++)
        {
            List<Character> innerList = origMap.get(i);
            for (int j = 0; j < innerList.size(); j++)
            {
                Coords currentPos = new Coords(i, j);
                if (!visitedPos.contains(currentPos))
                {
                    char plantType = origMap.get(currentPos.x).get(currentPos.y);
                    Set<Coords> trackedPos = new HashSet<>();
                    int regionPrice = getRegionPrice(origMap, currentPos, trackedPos, plantType) * trackedPos.size();
                    System.out.println("A region of " + plantType + " plants with price:" + regionPrice);
                    visitedPos.addAll(trackedPos);
                    totalPrice+=regionPrice;
                }
            }
        }
        return totalPrice;
    }

    private static int getRegionPrice(List<List<Character>> origMap, Coords currentPos, Set<Coords> visitedPos, char cropId)
    {
        visitedPos.add(currentPos);

        int totalPrice = 0, AreaPrice = 4;
        Coords nextPos;

        nextPos = new Coords(currentPos.x + 1, currentPos.y);
        if (isInBounds(origMap, nextPos))
        {
            if (!visitedPos.contains(nextPos))
            {
                if (origMap.get(nextPos.x).get(nextPos.y) == cropId)
                {
                    AreaPrice--;
                    totalPrice += getRegionPrice(origMap, nextPos, visitedPos, cropId);
                }
            } else
            {
                AreaPrice--;
            }
        }
        nextPos = new Coords(currentPos.x, currentPos.y + 1);
        if (isInBounds(origMap, nextPos)) if (isInBounds(origMap, nextPos))
        {
            if (!visitedPos.contains(nextPos))
            {
                if (origMap.get(nextPos.x).get(nextPos.y) == cropId)
                {
                    AreaPrice--;
                    totalPrice += getRegionPrice(origMap, nextPos, visitedPos, cropId);
                }
            } else
            {
                AreaPrice--;
            }
        }
        nextPos = new Coords(currentPos.x - 1, currentPos.y);
        if (isInBounds(origMap, nextPos))
        {
            if (!visitedPos.contains(nextPos))
            {
                if (origMap.get(nextPos.x).get(nextPos.y) == cropId)
                {
                    AreaPrice--;
                    totalPrice += getRegionPrice(origMap, nextPos, visitedPos, cropId);
                }
            } else
            {
                AreaPrice--;
            }
        }
        nextPos = new Coords(currentPos.x, currentPos.y - 1);
        if (isInBounds(origMap, nextPos))
        {
            if (!visitedPos.contains(nextPos))
            {
                if (origMap.get(nextPos.x).get(nextPos.y) == cropId)
                {
                    AreaPrice--;
                    totalPrice += getRegionPrice(origMap, nextPos, visitedPos, cropId);
                }
            } else
            {
                AreaPrice--;
            }
        }
        totalPrice += AreaPrice;
        return totalPrice;
    }

    private static List<List<Character>> loadMapFromFile(File inputFile) throws IOException
    {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                lines.add(line);
            }
        }

        List<List<Character>> map = new ArrayList<>();

        for (String str : lines)
        {
            List<Character> charList = new ArrayList<>();

            for (char c : str.toCharArray())
            {
                charList.add(c);
            }
            map.add(charList);
        }
        return map;
    }

    private static Boolean isInBounds(List<List<Character>> origMap, Coords currentPos)
    {
        return ((currentPos.x >= 0) && (currentPos.y >= 0) && (currentPos.x < origMap.size()) && (currentPos.y < origMap.get(0).size()));
    }


    record Coords(int x, int y)
    {

    }

}
