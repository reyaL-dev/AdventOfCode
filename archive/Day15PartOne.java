import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day15PartOne
{

    public static final byte TOP = 0, LEFT = 1, RIGHT = 2, BOTTOM = 3;

    public static void main(String[] args) throws IOException
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input15");

        List<List<Character>> map = new ArrayList<>();
        List<Byte> directions = new ArrayList<>();
        Coords lanternfishPos = readFile(inputFile, map, directions);

        for (byte direction : directions)
        {
            lanternfishPos = moveLanternfish(map, lanternfishPos, direction);
        }
        System.out.println(lanternfishPos);
        printOutput(map, directions);
    }

    private static Coords moveLanternfish(List<List<Character>> map, Coords lanternfishPos, byte direction) throws IllegalArgumentException
    {
        int distanceMovedObjects = findValidDistance(map, lanternfishPos, direction);
        if (distanceMovedObjects > 0)
        {
            switch (direction)
            {
                case TOP:
                {
                    for (int i = distanceMovedObjects; i > 0; i--)
                    {
                        if (i == 1)
                        {
                            setCharFromPos(map, lanternfishPos, '.');
                            setCharFromPos(map, new Coords(lanternfishPos.x, lanternfishPos.y - 1), '@');
                            return new Coords(lanternfishPos.x, lanternfishPos.y - 1);
                        }
                        setCharFromPos(map, new Coords(lanternfishPos.x, lanternfishPos.y - i), 'O');
                    }
                }
                break;
                case BOTTOM:
                {
                    for (int i = distanceMovedObjects; i > 0; i--)
                    {
                        if (i == 1)
                        {
                            setCharFromPos(map, lanternfishPos, '.');
                            setCharFromPos(map, new Coords(lanternfishPos.x, lanternfishPos.y + 1), '@');
                            return new Coords(lanternfishPos.x, lanternfishPos.y + 1);
                        }
                        setCharFromPos(map, new Coords(lanternfishPos.x, lanternfishPos.y + i), 'O');
                    }
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
                        setCharFromPos(map, new Coords(lanternfishPos.x - i, lanternfishPos.y), 'O');
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
                        setCharFromPos(map, new Coords(lanternfishPos.x + i, lanternfishPos.y), 'O');
                    }
                }
                break;
                default:
                    throw new IllegalArgumentException("Invalid direction!");
            }
        }
        return lanternfishPos;
    }

    private static int findValidDistance(List<List<Character>> map, Coords lanternfishPos, byte direction)
    {
        int distance = 1;
        while (true)
        {
            switch (direction)
            {
                case TOP:
                    if (getCharFromPos(map, new Coords(lanternfishPos.x, lanternfishPos.y - distance)) == '#')
                    {
                        return 0;
                    }
                    if (getCharFromPos(map, new Coords(lanternfishPos.x, lanternfishPos.y - distance)) == '.')
                    {
                        return distance;
                    }
                    break;
                case BOTTOM:
                    if (getCharFromPos(map, new Coords(lanternfishPos.x, lanternfishPos.y + distance)) == '#')
                    {
                        return 0;
                    }
                    if (getCharFromPos(map, new Coords(lanternfishPos.x, lanternfishPos.y + distance)) == '.')
                    {
                        return distance;
                    }
                    break;
                case LEFT:
                    if (getCharFromPos(map, new Coords(lanternfishPos.x - distance, lanternfishPos.y)) == '#')
                    {
                        return 0;
                    }
                    if (getCharFromPos(map, new Coords(lanternfishPos.x - distance, lanternfishPos.y)) == '.')
                    {
                        return distance;
                    }
                    break;
                case RIGHT:
                    if (getCharFromPos(map, new Coords(lanternfishPos.x + distance, lanternfishPos.y)) == '#')
                    {
                        return 0;
                    }
                    if (getCharFromPos(map, new Coords(lanternfishPos.x + distance, lanternfishPos.y)) == '.')
                    {
                        return distance;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid direction!");
            }
            distance++;
        }
    }

    private static char getCharFromPos(List<List<Character>> map, Coords coords)
    {
        return map.get(coords.y).get(coords.x);
    }

    private static char setCharFromPos(List<List<Character>> map, Coords coords, char c)
    {
        return map.get(coords.y).set(coords.x, c);
    }


    private static void printOutput(List<List<Character>> map, List<Byte> directions)
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

//        System.out.println("Directions:");
//        for (Byte direction : directions)
//        {
//            System.out.print(direction + " ");
//        }

        int GPSSum = 0;

        for (int y = 1; y < map.size() - 1; y++)
        {
            for (int x = 1; x < map.get(0).size() - 1; x++)
            {
                if (getCharFromPos(map, new Coords(x,y)) == 'O')
                {
                    GPSSum += (y * 100) + x;
                }
            }
        }

        System.out.println("GPS Sum: " + GPSSum);
    }

    public static Coords readFile(File inputFile, List<List<Character>> map, List<Byte> directions) throws IOException
    {
        Coords lanternfishPos = null;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile)))
        {
            String line;
            boolean readingMap = true;
            int x = 0;

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
                    for (int y = 0; y < line.length(); y++)
                    {
                        row.add(line.charAt(y));
                        if (line.charAt(y) == '@')
                        {
                            lanternfishPos = new Coords(x, y);
                        }
                    }
                    x++;
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
