import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Day06PartOne
{

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

    public static int[] findSymbolPosition(char[][] map, char symbol)
    {
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (map[i][j] == symbol)
                {
                    System.out.println("Position: " + i + " and " + j);
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public static char[][] loadMapFromFile(File inputFile) throws IOException
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

        // Convert to 2D-Array
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] map = new char[rows][cols];

        for (int i = 0; i < rows; i++)
        {
            map[i] = lines.get(i).toCharArray();
        }

        return map;
    }


    public static void main(String[] args)
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input06");
        try
        {
            char[][] map = loadMapFromFile(inputFile);
            System.out.println("Distinct positions : " + findDistinctPositions(map));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static int findDistinctPositions(char[][] map)
    {
        int[] currentPosition = findSymbolPosition(map, '^');

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
                        return countDistinctPositions(map);
                    }

                    // check if obstacle
                    if (map[x][y] == '#')
                    {
                        direction++;
                        break;
                    } else
                    {
                        map[currentPosition[0]][y] = 'X';
                        currentPosition[0] = x;
                        break;
                    }

                    // Right
                case 1:
                    y++;
                    if (y >= map[0].length)
                    {
                        return countDistinctPositions(map);
                    }

                    // check if obstacle
                    if (map[x][y] == '#')
                    {
                        direction++;
                        break;
                    } else
                    {
                        map[x][currentPosition[1]] = 'X';
                        currentPosition[1] = y;
                        break;
                    }

                    // Down
                case 2:
                    x++;
                    // If out of map
                    if (x >= map.length)
                    {
                        return countDistinctPositions(map);
                    }

                    // check if obstacle
                    if (map[x][y] == '#')
                    {
                        direction++;
                        break;
                    } else
                    {
                        map[currentPosition[0]][y] = 'X';
                        currentPosition[0] = x;
                        break;
                    }

                    // Left
                case 3:
                    y--;
                    // If out of map
                    if (y < 0)
                    {
                        return countDistinctPositions(map);
                    }

                    // check if obstacle
                    if (map[x][y] == '#')
                    {
                        direction = 0;
                        break;
                    } else
                    {
                        map[x][currentPosition[1]] = 'X';
                        currentPosition[1] = y;
                        break;
                    }

                default:
                    System.out.println("ERROR!");
                    break;
            }
        }
    }
}


