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

            if (checkParadoxMap(tempMap))
            {
                count++;
                System.out.println("Current map: " + position[0] + " and " + position[1]);
                for (List<Character> charList : tempMap)
                {
                    System.out.println(charList);
                }
            }

        }
        return count;
    }

    private static boolean checkParadoxMap(List<List<Character>> map)
    {
        int[] currentPosition = findSymbolPosition(map, '^');
        int direction = 0;
        int[] lastPlusUp = {0, 0};
        int[] lastPlusLeft = {0, 0};
        int[] lastPlusRight = {0, 0};
        int[] lastPlusDown = {0, 0};

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
                        return false;
                    }

                    // check if obstacle
                    if ((map.get(x).get(y) == '#') || (map.get(x).get(y) == 'O'))
                    {
                        //Check for Paradox
                        if (map.get(currentPosition[0]).get(y + 1) == '-' || map.get(currentPosition[0]).get(y + 1) == '+')
                        {
                            //                            System.out.println("Found Paradox upwards!");
                            return true;
                        }
                        if (map.get(currentPosition[0]).get(y + 1) == '#' || map.get(currentPosition[0]).get(y + 1) == 'O')
                        {
                            direction++;
                        }
                        //                        System.out.println("Up Putting in + on " + currentPosition[0] + " and " + y);
                        if ((lastPlusUp[0] == currentPosition[0]) && (lastPlusUp[1] == y))
                        {
                            //                            System.out.println("Found Paradox up to down !");
                            return true;
                        }
                        lastPlusUp[0] = currentPosition[0];
                        lastPlusUp[1] = y;
                        map.get(currentPosition[0]).set(y, '+');
                        direction++;
                        break;
                    } else
                    {
                        if (map.get(currentPosition[0]).get(y) == '-' || map.get(currentPosition[0]).get(y) == '+')
                        {
                            map.get(currentPosition[0]).set(y, '+');
                        } else
                        {
                            map.get(currentPosition[0]).set(y, '|');
                        }
                        currentPosition[0] = x;
                        break;
                    }

                    // Right
                case 1:
                    y++;

                    // If out of map
                    if (y >= map.get(0).size())
                    {
                        return false;
                    }


                    // check if obstacle
                    if ((map.get(x).get(y) == '#') || (map.get(x).get(y) == 'O'))
                    {
                        //Check for Paradox
                        if (map.get(x + 1).get(currentPosition[1]) == '|' || map.get(x + 1).get(currentPosition[1]) == '+')
                        {
                            //                            System.out.println("Found Paradox right!");
                            return true;
                        }
                        if (map.get(x + 1).get(currentPosition[1]) == '#' || map.get(x + 1).get(currentPosition[1]) == 'O')
                        {
                            direction++;
                        }
                        //                        System.out.println("Right Putting in + on " + x + " and " + currentPosition[1]);
                        if ((lastPlusRight[0] == x) && (lastPlusRight[1] == currentPosition[1]))
                        {
                            //                            System.out.println("Found Paradox right to left !");
                            return true;
                        }
                        lastPlusRight[0] = x;
                        lastPlusRight[1] = currentPosition[1];
                        map.get(x).set(currentPosition[1], '+');
                        direction++;
                        break;
                    } else
                    {
                        if (map.get(x).get(currentPosition[1]) == '|' || map.get(x).get(currentPosition[1]) == '+')
                        {
                            map.get(x).set(currentPosition[1], '+');
                        } else
                        {
                            map.get(x).set(currentPosition[1], '-');
                        }
                        currentPosition[1] = y;
                        break;
                    }

                    // Down
                case 2:
                    x++;
                    // If out of map
                    if (x >= map.size())
                    {
                        return false;
                    }

                    // check if obstacle
                    if ((map.get(x).get(y) == '#') || (map.get(x).get(y) == 'O'))
                    {
                        //Check for Paradox
                        if (map.get(currentPosition[0]).get(y - 1) == '-' || map.get(currentPosition[0]).get(y - 1) == '+')
                        {
                            //                            System.out.println("Found Paradox downwards!");
                            return true;
                        }
                        if (map.get(currentPosition[0]).get(y - 1) == '#' || map.get(currentPosition[0]).get(y - 1) == 'O')
                        {
                            direction = -1;
                        }
                        //                        System.out.println("Down Putting in + on " + currentPosition[0] + " and " + y);
                        if ((lastPlusDown[0] == currentPosition[0]) && (lastPlusDown[1] == y))
                        {
                            //                            System.out.println("Found Paradox up to down !");
                            return true;
                        }
                        lastPlusDown[0] = currentPosition[0];
                        lastPlusDown[1] = y;
                        map.get(currentPosition[0]).set(y, '+');
                        direction++;
                        break;
                    } else
                    {
                        if (map.get(currentPosition[0]).get(y) == '-' || map.get(currentPosition[0]).get(y) == '+')
                        {
                            map.get(currentPosition[0]).set(y, '+');
                        } else
                        {
                            map.get(currentPosition[0]).set(y, '|');
                        }
                        currentPosition[0] = x;
                        break;
                    }

                    // Left
                case 3:
                    y--;
                    // If out of map
                    if (y < 0)
                    {
                        return false;
                    }

                    // check if obstacle
                    if ((map.get(x).get(y) == '#') || (map.get(x).get(y) == 'O'))
                    {
                        //Check for Paradox
                        if (map.get(x - 1).get(currentPosition[1]) == '-' || map.get(x - 1).get(currentPosition[1]) == '+')
                        {
                            //                            System.out.println("Found Paradox left!");
                            return true;
                        }
                        if (map.get(x - 1).get(currentPosition[1]) == '#' || map.get(x - 1).get(currentPosition[1]) == 'O')
                        {
                            direction = 1;
                        } else
                        {
                            direction = 0;
                        }
                        //                        System.out.println("Left Putting in + on " + x + " and " + currentPosition[1]);
                        if ((lastPlusLeft[0] == x) && (lastPlusLeft[1] == currentPosition[1]))
                        {
                            //                            System.out.println("Found Paradox left to right !");
                            return true;
                        }
                        lastPlusLeft[0] = x;
                        lastPlusLeft[1] = currentPosition[1];
                        map.get(x).set(currentPosition[1], '+');

                        break;
                    } else
                    {
                        if (map.get(x).get(currentPosition[1]) == '|' || map.get(x).get(currentPosition[1]) == '+')
                        {
                            map.get(x).set(currentPosition[1], '+');
                        } else
                        {
                            map.get(x).set(currentPosition[1], '-');
                        }
                        currentPosition[1] = y;
                        break;
                    }
                default:
                    System.out.println("ERROR!");
                    break;
            }
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