import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day08PartOne
{

    public static void main(String[] args) throws IOException
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input08");

        List<List<Character>> origMap = loadMapFromFile(inputFile);

        for (List<Character> charList : origMap)
        {
            System.out.println(charList);
        }

        Map<Character, List<int[]>> coordinatesMap = new HashMap<>();

        for (int row = 0; row < origMap.size(); row++)
        {
            List<Character> currentRow = origMap.get(row);
            for (int col = 0; col < currentRow.size(); col++)
            {
                char currentChar = currentRow.get(col);
                if (currentChar != '.')
                {
                    coordinatesMap.computeIfAbsent(currentChar, k -> new ArrayList<>()).add(new int[]{row, col});
                }
            }
        }

        ArrayList<Map.Entry<Character, List<int[]>>> resultList = new ArrayList<>(coordinatesMap.entrySet());
        for (Map.Entry<Character, List<int[]>> entry : resultList)
        {
            char character = entry.getKey();
            System.out.println("Character: " + character);

            for (int i = 0; i < entry.getValue().size(); i++)
            {
                for (int j = i + 1; j < entry.getValue().size(); j++)
                {
                    int[] coord1 = entry.getValue().get(i);
                    int[] coord2 = entry.getValue().get(j);
                    putAntinode(origMap, coord1, coord2);
                }
            }

        }

        // Output end map:
        for (List<Character> charList : origMap)
        {
            System.out.println(charList);
        }

        System.out.println("Anzahl der Antinodes: " + countHashes(origMap));
    }

    private static int countHashes(List<List<Character>> origMap)
    {
        int count = 0;
        for (List<Character> row : origMap)
        {
            for (char c : row)
            {
                if (c == '#')
                {
                    count++;
                }
            }
        }

        return count;
    }

    private static void putAntinode(List<List<Character>> origMap, int[] coord1, int[] coord2)
    {
        int deltaX = coord2[0] - coord1[0];
        int deltaY = coord2[1] - coord1[1];

        int targetX, targetY;

        // Put Antinode on delta relative to coord1
        targetX = coord1[0] - deltaX;
        targetY = coord1[1] - deltaY;

        // Skip if out of index
        if ((targetX >= 0) && (targetY >= 0) && (targetX < origMap.size()) && (targetY < origMap.get(0).size()))
        {

            origMap.get(targetX).set(targetY, '#');

        }

        // Put Antinode on delta relative to coord2
        targetX = coord2[0] + deltaX;
        targetY = coord2[1] + deltaY;

        // Skip if out of index
        if ((targetX >= 0) && (targetY >= 0) && (targetX < origMap.size()) && (targetY < origMap.get(0).size()))
        {

            origMap.get(targetX).set(targetY, '#');

        }
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

    private static int countDistinctPositions(char[][] map)
    {
        int count = 1;
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (map[i][j] == 'X')
                {
                    count++;
                }
            }
        }

        return count;
    }

}