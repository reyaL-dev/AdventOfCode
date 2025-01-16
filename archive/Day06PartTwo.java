import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day06PartTwo
{

    public static void main(String[] args) throws IOException
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input06");

        List<List<Character>> origMap = loadMapFromFile(inputFile);
        origMap = makeUnmodifiable(origMap);

        List<int[]> positions = findDistinctPositions(origMap);
        System.out.println("Possible paradoxes : " + countParadoxPositions(origMap, positions));
    }

    // Helper function to preserve original map
    private static List<List<Character>> makeUnmodifiable(List<List<Character>> origMap)
    {
        List<List<Character>> unmodifiableMap = new ArrayList<>();

        for (List<Character> row : origMap)
        {
            unmodifiableMap.add(Collections.unmodifiableList(new ArrayList<>(row)));
        }

        return Collections.unmodifiableList(unmodifiableMap);
    }

    private static int countParadoxPositions(List<List<Character>> origMap, List<int[]> positions)
    {
        int count = 0;

        for (int[] position : positions)
        {
            List<List<Character>> tempMap = createModifiableCopy(origMap);

            tempMap.get(position[0]).set(position[1], 'O');
            boolean isParadoxMap = checkParadoxMap(tempMap);

//            System.out.println("Current map: " + position[0] + " and " + position[1]);
//            for (List<Character> charList : tempMap)
//            {
//                for (Character c : charList)
//                {
//                    int numericValue = (int) c;
//                    System.out.printf("%02d" + ",", numericValue);
//                }
//                System.out.println();
//            }

            if (isParadoxMap)
            {
                count++;
            }

        }
        return count;
    }

    private static boolean checkParadoxMap(List<List<Character>> map)
    {
        int[] lastPosition = findSymbolPosition(map, '^');
        assert lastPosition != null;
        map.get(lastPosition[0]).set(lastPosition[1], '.');
        int direction = 0;

        while (true)
        {
            int currentX, currentY;
            char currentSymbol, lastSymbol;
            switch (direction)
            {
                // Up --> 0b0001
                case 0:
                    currentX = lastPosition[0] - 1;
                    currentY = lastPosition[1];
                    if (currentX < 0)
                    {
                        return false;
                    }
                    lastSymbol = map.get(lastPosition[0]).get(lastPosition[1]);
                    currentSymbol = map.get(currentX).get(currentY);
                    addTrailLastPosition(map, lastSymbol, lastPosition[0], lastPosition[1], (char) 0b0001);
                    // check if obstacle
                    if ((currentSymbol == '#') || (currentSymbol == 'O'))
                    {
                        direction++;
                        break;
                    } else
                    {
                        //Check for Paradox
                        if (((currentSymbol & 0b0001) == 0b0001) && (currentSymbol != '.'))
                        {
                            return true;
                        }
                        lastPosition[0] = currentX;
                        break;
                    }
                    // Right --> 0b0010
                case 1:
                    currentX = lastPosition[0];
                    currentY = lastPosition[1] + 1;
                    if (currentY >= map.get(0).size())
                    {
                        return false;
                    }
                    lastSymbol = map.get(lastPosition[0]).get(lastPosition[1]);
                    currentSymbol = map.get(currentX).get(currentY);
                    addTrailLastPosition(map, lastSymbol, lastPosition[0], lastPosition[1], (char) 0b0010);
                    // check if obstacle
                    if ((currentSymbol == '#') || (currentSymbol == 'O'))
                    {
                        direction++;
                        break;
                    } else
                    {
                        //Check for Paradox
                        if (((currentSymbol & 0b0010) == 0b0010) && (currentSymbol != '.'))
                        {
                            return true;
                        }
                        lastPosition[1] = currentY;
                        break;
                    }

                    // Down
                case 2:
                    currentX = lastPosition[0] + 1;
                    currentY = lastPosition[1];
                    // If out of map
                    if (currentX >= map.size())
                    {
                        return false;
                    }
                    lastSymbol = map.get(lastPosition[0]).get(lastPosition[1]);
                    currentSymbol = map.get(currentX).get(currentY);
                    addTrailLastPosition(map, lastSymbol, lastPosition[0], lastPosition[1], (char) 0b0100);
                    // check if obstacle
                    if ((currentSymbol == '#') || (currentSymbol == 'O'))
                    {
                        direction++;
                        break;
                    } else
                    {
                        //Check for Paradox
                        if (((currentSymbol & 0b0100) == 0b0100) && (currentSymbol != '.'))
                        {
                            return true;
                        }
                        lastPosition[0] = currentX;
                        break;
                    }

                    // Left
                case 3:
                    currentX = lastPosition[0];
                    currentY = lastPosition[1] - 1;
                    // If out of map
                    if (currentY < 0)
                    {
                        return false;
                    }
                    lastSymbol = map.get(lastPosition[0]).get(lastPosition[1]);
                    currentSymbol = map.get(currentX).get(currentY);
                    addTrailLastPosition(map, lastSymbol, lastPosition[0], lastPosition[1], (char) 0b1000);
                    // check if obstacle
                    if ((currentSymbol == '#') || (currentSymbol == 'O'))
                    {
                        direction = 0;
                        break;
                    } else
                    {
                        //Check for Paradox
                        if (((currentSymbol & 0b1000) == 0b1000) && (currentSymbol != '.'))
                        {
                            return true;
                        }
                        lastPosition[1] = currentY;
                        break;
                    }
                default:
                    System.out.println("ERROR!");
                    break;
            }
        }
    }

    private static void addTrailLastPosition(List<List<Character>> map, char lastSymbol, int currentX, int currentY, char pos)
    {
        if (lastSymbol == '.')
        {
            map.get(currentX).set(currentY, pos);
        } else
        {
            map.get(currentX).set(currentY, (char) (lastSymbol | pos));
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

    private static List<int[]> getListPositions(List<List<Character>> map, int startX, int startY)
    {
        List<int[]> positions = new ArrayList<>();
        map.get(startX).set(startY, '^');
        System.out.println("Used mapper: ");
        for (List<Character> charList : map)
        {
            System.out.println(charList);
        }

        for (int i = 0; i < map.size(); i++)
        {
            for (int j = 0; j < map.get(i).size(); j++)
            {
                if (map.get(i).get(j) == 'X')
                {
                    positions.add(new int[]{i, j});
                }
            }
        }
        return positions;
    }


    private static List<int[]> findDistinctPositions(List<List<Character>> origMap)
    {
        List<List<Character>> map = createModifiableCopy(origMap);

        int[] currentPosition = findSymbolPosition(map, '^');
        assert currentPosition != null;
        int startX = currentPosition[0];
        int startY = currentPosition[1];
        int direction = 0;

        while (true)
        {
            int x = currentPosition[0];
            int y = currentPosition[1];
            switch (direction)
            {
                // Up
                case 0:
                    x--;
                    if (x < 0)
                    {
                        return getListPositions(map, startX, startY);
                    }

                    // check if obstacle
                    if (map.get(x).get(y) == '#')
                    {
                        direction++;
                        break;
                    } else
                    {
                        map.get(currentPosition[0]).set(y, 'X');
                        currentPosition[0] = x;
                        break;
                    }

                    // Right
                case 1:
                    y++;
                    // If out of map
                    if (y >= map.get(0).size())
                    {
                        return getListPositions(map, startX, startY);
                    }

                    // check if obstacle
                    if (map.get(x).get(y) == '#')
                    {
                        direction++;
                        break;
                    } else
                    {
                        map.get(x).set(currentPosition[1], 'X');
                        currentPosition[1] = y;
                        break;
                    }

                    // Down
                case 2:
                    x++;
                    // If out of map
                    if (x >= map.size())
                    {
                        return getListPositions(map, startX, startY);
                    }

                    // check if obstacle
                    if (map.get(x).get(y) == '#')
                    {
                        direction++;
                        break;
                    } else
                    {
                        map.get(currentPosition[0]).set(y, 'X');
                        currentPosition[0] = x;
                        break;
                    }

                    // Left
                case 3:
                    y--;
                    // If out of map
                    if (y < 0)
                    {
                        return getListPositions(map, startX, startY);
                    }

                    // check if obstacle
                    if (map.get(x).get(y) == '#')
                    {
                        direction = 0;
                        break;
                    } else
                    {
                        map.get(x).set(currentPosition[1], 'X');
                        currentPosition[1] = y;
                        break;
                    }

                default:
                    System.out.println("ERROR!");
                    break;
            }
        }
    }

    private static List<List<Character>> createModifiableCopy(List<List<Character>> origMap)
    {
        List<List<Character>> modifiableCopy = new ArrayList<>();

        for (List<Character> row : origMap)
        {
            modifiableCopy.add(new ArrayList<>(row));
        }

        return modifiableCopy;
    }

    public static int[] findSymbolPosition(List<List<Character>> map, char symbol)
    {
        for (int i = 0; i < map.size(); i++)
        {
            List<Character> innerList = map.get(i);
            for (int j = 0; j < innerList.size(); j++)
            {
                if (innerList.get(j) == symbol)
                {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

}