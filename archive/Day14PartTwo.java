import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14PartTwo
{

    public static void main(String[] args) throws IOException
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input14"); // Replace with your file path
        List<RobotInputs> robotInputs = readRobotInputs(inputFile);
//        int[] tileset = {11, 7};
        int[] tileset = {101, 103};
        int issuedSteps = 0;
        boolean patternFound = false;
        List<int[]> robotPositions = new ArrayList<>();

        for (RobotInputs input : robotInputs)
        {
            robotPositions.add(input.pos);
        }
        while (!patternFound)
        {
            issuedSteps++;
            for (int i = 0; i < robotInputs.size(); i++)
            {
                robotPositions.set(i, getNextPosition(robotPositions.get(i), robotInputs.get(i).vel, tileset));
            }

            if (hasAllNeighbors(robotPositions))
            {
                System.out.println("Found pattern after steps: " + issuedSteps);
                plotMap(robotPositions, tileset);
                patternFound = true;
            }

//            if (issuedSteps == 100)
//            {
//                plotMap(robotPositions, tileset);
//                patternFound = true;
//            }
        }
    }

    public static void plotMap(List<int[]> coordinates, int[] tileset)
    {
        int[][] grid = new int[tileset[1]][tileset[0]];

        for (int[] point : coordinates)
        {
            grid[point[1]][point[0]]++;
        }

        System.out.println("Plotted Output:");
        for (int x = 0; x < tileset[1]; x++)
        {
            for (int y = 0; y < tileset[0]; y++)
            {
                if (grid[x][y] == 0)
                {
                    System.out.print(".");
                } else
                {
                    System.out.print(grid[x][y]);
                }
            }
            System.out.println();
        }
    }


    private static int[] getQuadrantPositions(List<int[]> robotPositions, int[] tileset)
    {
        int[] quadrantCount = {0, 0, 0, 0};
        for (int[] pos : robotPositions)
        {
            int x = pos[0];
            int y = pos[1];
            if (x >= 0 && x < (tileset[0] / 2) && y >= 0 && y < (tileset[1] / 2))
            {
                quadrantCount[0]++;
            } else if (x >= 0 && x < (tileset[0] / 2) && y > (tileset[1] / 2) && y <= tileset[1])
            {
                quadrantCount[2]++;
            } else if (x > (tileset[0] / 2) && x < tileset[0] && y >= 0 && y < (tileset[1] / 2))
            {
                quadrantCount[1]++;
            } else if (x > (tileset[0] / 2) && x < tileset[0] && y > (tileset[1] / 2) && y <= tileset[1])
            {
                quadrantCount[3]++;
            }
        }
        return quadrantCount;
    }

    private static int[] getRobotPosition(RobotInputs input, int[] tileset, int steps)
    {
        int[] currentPos = input.pos;
        for (int i = 0; i < steps; i++)
        {
            currentPos = getNextPosition(currentPos, input.vel, tileset);
        }

        return currentPos;
    }

    private static int[] getNextPosition(int[] currentPos, int[] vel, int[] tileset)
    {
        int[] nextPos = {currentPos[0] + (vel[0] % tileset[0]), currentPos[1] + (vel[1] % tileset[1])};
        //        System.out.println("Calculated next Pos: " + Arrays.toString(nextPos));
        if (nextPos[0] < 0)
        {
            nextPos[0] += tileset[0];
        }
        if (nextPos[1] < 0)
        {
            nextPos[1] += tileset[1];
        }
        if (nextPos[0] >= tileset[0])
        {
            nextPos[0] -= tileset[0];
        }
        if (nextPos[1] >= tileset[1])
        {
            nextPos[1] -= tileset[1];
        }
        return nextPos;
    }

    public static List<RobotInputs> readRobotInputs(File inputFile) throws IOException
    {
        List<RobotInputs> robotInputsList = new ArrayList<>();

        Pattern pattern = Pattern.compile("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find())
                {
                    int[] pos = {Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))};
                    int[] vel = {Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))};

                    robotInputsList.add(new RobotInputs(pos, vel));
                }
            }
        }

        return robotInputsList;
    }

    public static boolean hasAllNeighbors(List<int[]> coordinates) {
        Set<String> coordinateSet = new HashSet<>();
        for (int[] point : coordinates) {
            coordinateSet.add(point[0] + "," + point[1]);
        }

        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] point : coordinates) {
            int x = point[0];
            int y = point[1];
            boolean hasAllNeighbors = true;

            for (int[] direction : directions) {
                int neighborX = x + direction[0];
                int neighborY = y + direction[1];
                if (!coordinateSet.contains(neighborX + "," + neighborY)) {
                    hasAllNeighbors = false;
                    break;
                }
            }

            if (hasAllNeighbors) {
                return true;
            }
        }

        return false;
    }

    record RobotInputs(int[] pos, int[] vel)
    {

    }
}