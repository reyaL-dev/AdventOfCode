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
        File inputFile = new File("C:\\Project\\inputfiles\\input12TEST");
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
                    Set<Coords> trackedPos = new HashSet<>();
                    if (plantType == 'R')
                    {
                        int regionPrice = getRegionPrice(origMap, currentPos, trackedPos, plantType); // trackedPos.size();
                        System.out.println("A region of " + plantType + " and number: " + trackedPos.size() + " plants with price:" + regionPrice);
                        visitedPos.addAll(trackedPos);
                        totalPrice += regionPrice;
                    }
                }
            }
        }
        return totalPrice;
    }

    private static int getRegionPrice(List<List<Character>> origMap, Coords currentPos, Set<Coords> visitedPos, char cropId)
    {
        visitedPos.add(currentPos);

        int ownShape = getShape(origMap, currentPos, cropId);
        if (ownShape == 15)
        {
            return 4;
        }
        int totalPrice = 0, numCornor;
        HashMap<Integer, Integer> neighborShapes = new HashMap<>();
        neighborShapes.put(TOP, getShape(origMap, new Coords(currentPos.x, currentPos.y - 1), cropId));
        neighborShapes.put(LEFT, getShape(origMap, new Coords(currentPos.x - 1, currentPos.y), cropId));
        neighborShapes.put(RIGHT, getShape(origMap, new Coords(currentPos.x + 1, currentPos.y), cropId));
        neighborShapes.put(BOTTOM, getShape(origMap, new Coords(currentPos.x, currentPos.y + 1), cropId));
//        System.out.println("Neighbors on " + currentPos + " : " + neighborShapes);
        switch (ownShape)
        {
            case 0, 6, 9:
                numCornor = 0;
                break;
                // Single pieces
            case 7, 11, 13, 14:
                numCornor = 2;
                break;
            case 10:
                numCornor = 2;
                if (((neighborShapes.get(RIGHT) & TOP) != TOP) && ((neighborShapes.get(TOP) & RIGHT) != RIGHT))
                {
                    numCornor = 1;
                }
                break;
            case 12:
                numCornor = 2;
                if (((neighborShapes.get(LEFT) & TOP) != TOP) && ((neighborShapes.get(TOP) & LEFT) != LEFT))
                {
                    numCornor = 1;
                }
                break;
            case 3:
                numCornor = 2;
                if (((neighborShapes.get(RIGHT) & BOTTOM) != BOTTOM) && ((neighborShapes.get(BOTTOM) & RIGHT) != RIGHT))
                {
                    numCornor = 1;
                }
                break;
            case 5:
                numCornor = 2;
                if (((neighborShapes.get(LEFT) & BOTTOM) != BOTTOM) && ((neighborShapes.get(TOP) & RIGHT) != RIGHT))
                {
                    numCornor = 1;
                }
                break;
            case 1:
                numCornor = 2;
                if ((neighborShapes.get(LEFT) & BOTTOM) != BOTTOM)
                {
                    numCornor--;
                }
                if ((neighborShapes.get(RIGHT) & BOTTOM) != BOTTOM)
                {
                    numCornor--;
                }
                break;
            case 2:
                numCornor = 2;
                if ((neighborShapes.get(TOP) & RIGHT) != RIGHT)
                {
                    numCornor--;
                }
                if ((neighborShapes.get(BOTTOM) & RIGHT) != RIGHT)
                {
                    numCornor--;
                }
                break;
            case 4:
                numCornor = 2;
                if ((neighborShapes.get(TOP) & LEFT) != LEFT)
                {
                    numCornor--;
                }
                if ((neighborShapes.get(BOTTOM) & LEFT) != LEFT)
                {
                    numCornor--;
                }
                break;
            case 8:
                numCornor = 2;
                if ((neighborShapes.get(LEFT) & TOP) != TOP)
                {
                    numCornor--;
                }
                if ((neighborShapes.get(RIGHT) & TOP) != TOP)
                {
                    numCornor--;
                }
                break;
            default:
                System.out.println("Invalid state");
                numCornor = 0;
                break;
        }

        Coords nextPos;

        nextPos = new Coords(currentPos.x + 1, currentPos.y);
        if (isInBounds(origMap, nextPos))
        {
            if (!visitedPos.contains(nextPos))
            {
                if (getCharacter(origMap, nextPos) == cropId)
                {
                    totalPrice += getRegionPrice(origMap, nextPos, visitedPos, cropId);
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
                    totalPrice += getRegionPrice(origMap, nextPos, visitedPos, cropId);
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
                    totalPrice += getRegionPrice(origMap, nextPos, visitedPos, cropId);
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
                    totalPrice += getRegionPrice(origMap, nextPos, visitedPos, cropId);
                }
            }
        }
        System.out.println("Cornors on : " + currentPos + " : " + numCornor + " and shape: " + ownShape);
        totalPrice += numCornor;
        return totalPrice;
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
