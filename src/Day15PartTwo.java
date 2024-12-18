import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day15PartTwo
{

    public static final byte TOP = 0, LEFT = 1, RIGHT = 2, BOTTOM = 3;

    public static void main(String[] args) throws IOException
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input15TEST3");
//        File inputFile = new File("C:\\Project\\inputfiles\\input15");

        List<List<Character>> map = new ArrayList<>();
        List<Byte> directions = new ArrayList<>();
        Coords lanternfishPos = readFile(inputFile, map, directions);
        System.out.println("Fish at: " + lanternfishPos);

        for (byte direction : directions)
        {
            lanternfishPos = moveLanternfish(map, lanternfishPos, direction);
            printMap(map);
        }

        printMap(map);
        int GPSSum = 0;

        for (int y = 1; y < map.size() - 1; y++)
        {
            for (int x = 2; x < map.get(0).size() - 1; x++)
            {
                if (getCharFromPos(map, new Coords(x, y)) == '[')
                {
                    GPSSum += (y * 100) + x;
                }
            }
        }
        System.out.println("GPS Sum: " + GPSSum);

    }

    private static Coords moveLanternfish(List<List<Character>> map, Coords lanternfishPos, byte direction) throws IllegalArgumentException
    {
        List<Coords> movingObjects = new ArrayList<>();
        movingObjects.add(lanternfishPos);
        int distanceMovedObjects = findValidDistance(map, movingObjects, direction);
        System.out.println("Distance: " + distanceMovedObjects);
        Collections.reverse(movingObjects);
        if (distanceMovedObjects > 0)
        {
            switch (direction)
            {
                case TOP:
                {
                    movingObjects.remove(lanternfishPos);
                    for (Coords moveObject : movingObjects)
                    {
                        setCharFromPos(map, new Coords(moveObject.x, moveObject.y - 1), '[');
                        setCharFromPos(map, new Coords(moveObject.x + 1, moveObject.y - 1), ']');
                        setCharFromPos(map, moveObject, '.');
                        setCharFromPos(map, new Coords(moveObject.x + 1, moveObject.y), '.');
                    }
                    setCharFromPos(map, lanternfishPos, '.');
                    lanternfishPos = new Coords(lanternfishPos.x, lanternfishPos.y - 1);
                    setCharFromPos(map, lanternfishPos, '@');
                }
                break;
                case BOTTOM:
                {
                    movingObjects.remove(lanternfishPos);
                    for (Coords moveObject : movingObjects)
                    {
                        setCharFromPos(map, new Coords(moveObject.x, moveObject.y + 1), '[');
                        setCharFromPos(map, new Coords(moveObject.x + 1, moveObject.y + 1), ']');
                        setCharFromPos(map, moveObject, '.');
                        setCharFromPos(map, new Coords(moveObject.x + 1, moveObject.y), '.');
                    }
                    setCharFromPos(map, lanternfishPos, '.');
                    lanternfishPos = new Coords(lanternfishPos.x, lanternfishPos.y + 1);
                    setCharFromPos(map, lanternfishPos, '@');
                }
                break;
                case LEFT:
                {
                    for (int i = distanceMovedObjects; i > 0; i--)
                    {
                        if (i == 1)
                        {
                            setCharFromPos(map, lanternfishPos, '.');
                            setCharFromPos(map, new Coords(lanternfishPos.x - 1, lanternfishPos.y), '@');
                            return new Coords(lanternfishPos.x - 1, lanternfishPos.y);
                        }
                        if (i % 2 == 1)
                        {
                            setCharFromPos(map, new Coords(lanternfishPos.x - i, lanternfishPos.y), '[');
                        } else
                        {
                            setCharFromPos(map, new Coords(lanternfishPos.x - i, lanternfishPos.y), ']');
                        }

                    }
                }
                break;
                case RIGHT:
                {
                    for (int i = distanceMovedObjects; i > 0; i--)
                    {
                        if (i == 1)
                        {
                            setCharFromPos(map, lanternfishPos, '.');
                            setCharFromPos(map, new Coords(lanternfishPos.x + 1, lanternfishPos.y), '@');
                            return new Coords(lanternfishPos.x + 1, lanternfishPos.y);
                        }
                        if (i % 2 == 0)
                        {
                            setCharFromPos(map, new Coords(lanternfishPos.x + i, lanternfishPos.y), '[');
                        } else
                        {
                            setCharFromPos(map, new Coords(lanternfishPos.x + i, lanternfishPos.y), ']');
                        }
                    }
                }
                break;
                default:
                    throw new IllegalArgumentException("Invalid direction!");
            }
        }
        return lanternfishPos;
    }

    private static int findValidDistance(List<List<Character>> map, List<Coords> movingObjects, byte direction)
    {
        int distance = 1;
        List<Coords> checkNodes = new ArrayList<>(movingObjects);
        while (true)
        {
            List<Coords> nextNodes = new ArrayList<>();
            for (Coords objectPos : checkNodes)
            {
                switch (direction)
                {
                    case TOP:
                    {
                        int nextMovement = objectPos.y - 1;
                        Integer collision = getCollision(map, movingObjects, objectPos, nextMovement, nextNodes);
                        if (collision != null) return collision;
                        if (nextNodes.isEmpty())
                        {
                            return distance;
                        }
                        checkNodes = nextNodes;
                        break;
                    }
                    case BOTTOM:
                    {
                        int nextMovement = objectPos.y + 1;
                        Integer collision = getCollision(map, movingObjects, objectPos, nextMovement, nextNodes);
                        if (collision != null) return collision;
                        if (nextNodes.isEmpty())
                        {
                            return distance;
                        }
                        checkNodes = nextNodes;
                        break;
                    }
                    case LEFT:
                        if (getCharFromPos(map, new Coords(objectPos.x - distance, objectPos.y)) == '#')
                        {
                            return 0;
                        }
                        if (getCharFromPos(map, new Coords(objectPos.x - distance, objectPos.y)) == '.')
                        {
                            return distance;
                        }
                        break;
                    case RIGHT:
                        if (getCharFromPos(map, new Coords(objectPos.x + distance, objectPos.y)) == '#')
                        {
                            return 0;
                        }
                        if (getCharFromPos(map, new Coords(objectPos.x + distance, objectPos.y)) == '.')
                        {
                            return distance;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid direction!");
                }
            }
            distance++;
        }
    }

    private static Integer getCollision(List<List<Character>> map, List<Coords> movingObjects, Coords objectPos, int nextMovement, List<Coords> consideredNodes)
    {
        char currentSymbol = getCharFromPos(map, objectPos);
        Coords nextPos = new Coords(objectPos.x, nextMovement);
        char nextSymbol = getCharFromPos(map, nextPos);
        if (currentSymbol == '@')
        {
            if (nextSymbol == '#')
            {
                return 0;
            } else if (nextSymbol == '.')
            {
                return 1;
            } else if (nextSymbol == '[')
            {
                consideredNodes.remove(objectPos);
                consideredNodes.add(nextPos);
                movingObjects.add(nextPos);
            } else if (nextSymbol == ']')
            {
                consideredNodes.remove(objectPos);
                Coords boxPos = new Coords(nextPos.x - 1, nextPos.y);
                consideredNodes.add(boxPos);
                movingObjects.add(boxPos);
            }
        } else
        {
            Coords nextRightWallPos = new Coords(objectPos.x + 1, nextMovement);
            char nextRightWallSymbol = getCharFromPos(map, nextRightWallPos);
            if ((nextSymbol == '#') || (nextRightWallSymbol == '#'))
            {
                return 0;
            }
            if (nextSymbol == '[')
            {
                consideredNodes.add(nextPos);
                movingObjects.add(nextPos);
            }
            if (nextRightWallSymbol == '[')
            {
                consideredNodes.add(nextRightWallPos);
                movingObjects.add(nextRightWallPos);
            }
            if (nextSymbol == ']')
            {
                Coords boxCoords = new Coords(nextPos.x - 1, nextPos.y);
                consideredNodes.add(boxCoords);
                movingObjects.add(boxCoords);
            }
            consideredNodes.remove(objectPos);
        }
        return null;
    }

    private static char getCharFromPos(List<List<Character>> map, Coords coords)
    {
        return map.get(coords.y).get(coords.x);
    }

    private static void setCharFromPos(List<List<Character>> map, Coords coords, char c)
    {
        map.get(coords.y).set(coords.x, c);
    }


    private static void printMap(List<List<Character>> map)
    {
        System.out.println("Map:");
        for (List<Character> row : map)
        {
            for (Character c : row)
            {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public static Coords readFile(File inputFile, List<List<Character>> map, List<Byte> directions) throws IOException
    {
        Coords lanternfishPos = null;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile)))
        {
            String line;
            boolean readingMap = true;
            int y = 0;

            while ((line = br.readLine()) != null)
            {
                if (line.isEmpty())
                {
                    readingMap = false;
                    continue;
                }

                if (readingMap)
                {
                    List<Character> row = new ArrayList<>();
                    for (int x = 0; x < line.length(); x++)
                    {
                        if (line.charAt(x) == '#')
                        {
                            row.add('#');
                            row.add('#');
                        }
                        if (line.charAt(x) == 'O')
                        {
                            row.add('[');
                            row.add(']');
                        }
                        if (line.charAt(x) == '@')
                        {
                            row.add('@');
                            row.add('.');
                            lanternfishPos = new Coords((x * 2), y);
                        }
                        if (line.charAt(x) == '.')
                        {
                            row.add('.');
                            row.add('.');
                        }
                    }
                    y++;
                    map.add(row);
                } else
                {
                    for (char c : line.toCharArray())
                    {
                        directions.add(charToByte(c));
                    }
                }
            }
        }
        return lanternfishPos;
    }

    private static byte charToByte(char c)
    {
        return switch (c)
        {
            case '<' -> LEFT;
            case 'v' -> BOTTOM;
            case '>' -> RIGHT;
            case '^' -> TOP;
            default -> throw new IllegalArgumentException("Invalid input: " + c);
        };
    }

    record Coords(int x, int y)
    {

    }
}
