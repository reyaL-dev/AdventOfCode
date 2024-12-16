import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day12PartTwo
{

    public static final int TOP = 1, LEFT = 2, RIGHT = 4, BOTTOM = 8;

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
        for (int y = 0; y < origMap.size(); y++)
        {
            List<Character> innerList = origMap.get(y);
            for (int x = 0; x < innerList.size(); x++)
            {
                Coords currentPos = new Coords(x, y);
                if (!visitedPos.contains(currentPos))
                {
                    char plantType = getCharacter(origMap, currentPos);
                    List<Coords> trackedPos = new ArrayList<>();
                    getDistinctRegion(origMap, currentPos, trackedPos, plantType);
                    int distinctWalls = getDistinctWalls(origMap, trackedPos, plantType);
                    totalPrice+=trackedPos.size() * distinctWalls;
                    visitedPos.addAll(trackedPos);
                }
            }
        }
        return totalPrice;
    }

    private static int getDistinctWalls(List<List<Character>> origMap, List<Coords> trackedPos, char cropId)
    {
        int wallCount = 0;
        Coords lastCheck;
        lastCheck = new Coords(-1, -1);
        boolean lastWall = false;
        trackedPos.sort(Comparator.comparingInt(Coords::x).thenComparingInt(Coords::y));
        // Left Walls
        for (Coords Coord : trackedPos)
        {
            boolean isWalled = (getShape(origMap, Coord, cropId) & LEFT) == LEFT;
            if (isWalled)
            {
                if (!((Objects.equals(lastCheck, new Coords(Coord.x, Coord.y - 1))) && lastWall))
                {
                    wallCount++;
                }
            }
            lastCheck = Coord;
            lastWall = isWalled;
        }
        // Right Walls
        for (Coords Coord : trackedPos)
        {
            boolean isWalled = (getShape(origMap, Coord, cropId) & RIGHT) == RIGHT;
            if (isWalled)
            {
                if (!((Objects.equals(lastCheck, new Coords(Coord.x, Coord.y - 1))) && lastWall))
                {
                    wallCount++;
                }
            }
            lastCheck = Coord;
            lastWall = isWalled;
        }
        trackedPos.sort(Comparator.comparingInt(Coords::y).thenComparingInt(Coords::x));
        // Left Walls
        for (Coords Coord : trackedPos)
        {
            boolean isWalled = (getShape(origMap, Coord, cropId) & BOTTOM) == BOTTOM;
            if (isWalled)
            {
                if (!((Objects.equals(lastCheck, new Coords(Coord.x - 1, Coord.y))) && lastWall))
                {
                    wallCount++;
                }
            }
            lastCheck = Coord;
            lastWall = isWalled;
        }
        // Top Walls
        for (Coords Coord : trackedPos)
        {
            boolean isWalled = (getShape(origMap, Coord, cropId) & TOP) == TOP;
            if (isWalled)
            {
                if (!((Objects.equals(lastCheck, new Coords(Coord.x - 1, Coord.y))) && lastWall))
                {
                    wallCount++;
                }
            }
            lastCheck = Coord;
            lastWall = isWalled;
        }
        return wallCount;
    }

    private static void getDistinctRegion(List<List<Character>> origMap, Coords currentPos, List<Coords> visitedPos, char cropId)
    {
        visitedPos.add(currentPos);
        Coords nextPos;

        nextPos = new Coords(currentPos.x + 1, currentPos.y);
        if (isInBounds(origMap, nextPos))
        {
            if (!visitedPos.contains(nextPos))
            {
                if (getCharacter(origMap, nextPos) == cropId)
                {
                    getDistinctRegion(origMap, nextPos, visitedPos, cropId);
                }
            }
        }
        nextPos = new Coords(currentPos.x, currentPos.y + 1);
        if (isInBounds(origMap, nextPos))
        {
            if (!visitedPos.contains(nextPos))
            {
                if (getCharacter(origMap, nextPos) == cropId)
                {
                    getDistinctRegion(origMap, nextPos, visitedPos, cropId);
                }
            }
        }
        nextPos = new Coords(currentPos.x - 1, currentPos.y);
        if (isInBounds(origMap, nextPos))
        {
            if (!visitedPos.contains(nextPos))
            {
                if (getCharacter(origMap, nextPos) == cropId)
                {
                    getDistinctRegion(origMap, nextPos, visitedPos, cropId);
                }
            }
        }
        nextPos = new Coords(currentPos.x, currentPos.y - 1);
        if (isInBounds(origMap, nextPos))
        {
            if (!visitedPos.contains(nextPos))
            {
                if (getCharacter(origMap, nextPos) == cropId)
                {
                    getDistinctRegion(origMap, nextPos, visitedPos, cropId);
                }
            }
        }
    }

    private static Character getCharacter(List<List<Character>> origMap, Coords nextPos)
    {
        return origMap.get(nextPos.y).get(nextPos.x);
    }

    private static Integer getShape(List<List<Character>> origMap, Coords currentPos, char cropId)
    {
        if (!isInBounds(origMap, currentPos))
        {
            return 16;
        }

        if (getCharacter(origMap, currentPos) != cropId)
        {
            return 16;
        }
        int wallPositions = 15;
        Coords nextPos;
        nextPos = new Coords(currentPos.x + 1, currentPos.y);
        if (isInBounds(origMap, nextPos))
        {
            if (getCharacter(origMap, nextPos) == cropId)
            {
                wallPositions -= RIGHT;
            }
        }
        nextPos = new Coords(currentPos.x, currentPos.y + 1);
        if (isInBounds(origMap, nextPos))
        {
            if (getCharacter(origMap, nextPos) == cropId)
            {
                wallPositions -= BOTTOM;
            }
        }
        nextPos = new Coords(currentPos.x - 1, currentPos.y);
        if (isInBounds(origMap, nextPos))
        {
            if (getCharacter(origMap, nextPos) == cropId)
            {
                wallPositions -= LEFT;
            }
        }
        nextPos = new Coords(currentPos.x, currentPos.y - 1);
        if (isInBounds(origMap, nextPos))
        {
            if (getCharacter(origMap, nextPos) == cropId)
            {
                wallPositions -= TOP;
            }
        }
        return wallPositions;
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
        return ((currentPos.x >= 0) && (currentPos.y >= 0) && (currentPos.y < origMap.size()) && (currentPos.x < origMap.get(0).size()));
    }


    record Coords(int x, int y)
    {

    }

}
