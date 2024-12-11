import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day10PartOne
{

    public static void main(String[] args) throws IOException
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input10TEST");
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
                    trailheadScore += getTrailheadScore(origMap, i, j);
                }
            }
        }
        System.out.println("trailheadScore: " + trailheadScore);
    }

    private static int getTrailheadScore(List<List<Integer>> origMap, int i, int j)
    {
        int numPathes = findValidPath(origMap, i, j, 0);
        System.out.println("Score for this path: " + numPathes);
        return numPathes;
    }

    private static int findValidPath(List<List<Integer>> origMap, int i, int j, int currentLevel)
    {
        System.out.println("Checking out: i=" + i + " j=" + j + " level:" + currentLevel);
        if ((i < 0) || (j < 0) || (i >= origMap.size()) || (j >= origMap.get(0).size()))
        {
            return 0;
        }
        System.out.println("Current value: " + origMap.get(i).get(j));
        if (origMap.get(i).get(j) != (currentLevel))
        {
            return 0;
        }
        if (currentLevel == 9)
        {
            System.out.println("Found path!");
            return 1;
        }
        int validPathes = 0;
        validPathes += findValidPath(origMap, i + 1, j, currentLevel + 1);
        validPathes += findValidPath(origMap, i, j + 1, currentLevel + 1);
        validPathes += findValidPath(origMap, i - 1, j, currentLevel + 1);
        validPathes += findValidPath(origMap, i, j - 1, currentLevel + 1);
        return validPathes;
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
}
