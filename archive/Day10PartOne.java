import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day10PartOne
{

    public static void main(String[] args) throws IOException
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input10");
        int trailheadScore = 0;

        List<List<Integer>> origMap = loadMapFromFile(inputFile);

        // Iterate over all trailheads
        for (int i = 0; i < origMap.size(); i++)
        {
            List<Integer> innerList = origMap.get(i);
            for (int j = 0; j < innerList.size(); j++)
            {
                if (innerList.get(j) == 0)
                {
                    System.out.println("Checking list: " + i + " and " + j);
                    trailheadScore += getTrailheadScore(origMap, new Coords(i, j));
                }
            }
        }


        //        trailheadScore += getTrailheadScore(origMap, new Coords(0, 2));
        System.out.println("trailheadScore: " + trailheadScore);
    }

    private static int getTrailheadScore(List<List<Integer>> origMap, Coords pos)
    {
        int numPathes = findValidPath(origMap, pos, 0, new HashSet<>());
        System.out.println("Score for this path: " + numPathes);
        return numPathes;
    }

    private static int findValidPath(List<List<Integer>> origMap, Coords currentPos, int currentLevel, Set<Coords> visitedPos)
    {
        System.out.println("Checking out: x=" + currentPos.x + " y=" + currentPos.y + " level:" + currentLevel);
        visitedPos.add(currentPos);
        if (currentLevel == 9)
        {
            System.out.println("Found path!");
            return 1;
        }
        int validPathes = 0;

        Coords nextPos;

        nextPos = new Coords(currentPos.x + 1, currentPos.y);
        if (isInBounds(origMap, nextPos))
        {
            if ((!visitedPos.contains(nextPos)) && (origMap.get(nextPos.x).get(nextPos.y) == currentLevel + 1))
            {
                validPathes += findValidPath(origMap, nextPos, currentLevel + 1, visitedPos);
            }
        }
        nextPos = new Coords(currentPos.x, currentPos.y + 1);
        if (isInBounds(origMap, nextPos))
        {
            if ((!visitedPos.contains(nextPos)) && (origMap.get(nextPos.x).get(nextPos.y) == currentLevel + 1))
            {
                validPathes += findValidPath(origMap, nextPos, currentLevel + 1, visitedPos);
            }
        }
        nextPos = new Coords(currentPos.x - 1, currentPos.y);
        if (isInBounds(origMap, nextPos))
        {
            if ((!visitedPos.contains(nextPos)) && (origMap.get(nextPos.x).get(nextPos.y) == currentLevel + 1))
            {
                validPathes += findValidPath(origMap, nextPos, currentLevel + 1, visitedPos);
            }
        }
        nextPos = new Coords(currentPos.x, currentPos.y - 1);
        if (isInBounds(origMap, nextPos))
        {
            if ((!visitedPos.contains(nextPos)) && (origMap.get(nextPos.x).get(nextPos.y) == currentLevel + 1))
            {
                validPathes += findValidPath(origMap, nextPos, currentLevel + 1, visitedPos);
            }
        }
        return validPathes;
    }

    private static Boolean isInBounds(List<List<Integer>> origMap, Coords currentPos)
    {
        return ((currentPos.x >= 0) && (currentPos.y >= 0) && (currentPos.x < origMap.size()) && (currentPos.y < origMap.get(0).size()));
    }

    private static List<List<Integer>> loadMapFromFile(File inputFile) throws IOException
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

        List<List<Integer>> map = new ArrayList<>();

        for (String str : lines)
        {
            List<Integer> intList = new ArrayList<>();

            for (char c : str.toCharArray())
            {
                intList.add(Character.getNumericValue(c));
            }
            System.out.println();
            map.add(intList);
        }
        return map;
    }

    record Coords(int x, int y)
    {

    }
}
