import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day18PartOne implements AoCMain.AoCHandler
{

    private final int mapSize = 71;

    private static boolean isValidCoord(Coords coord, int mapSize)
    {
        return coord.x >= 0 && coord.x < mapSize && coord.y >= 0 && coord.y < mapSize;
    }

    private static int heuristic(Coords a, Coords b)
    {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

    private static List<Coords> reconstructPath(Map<Coords, Coords> cameFrom, Coords current)
    {
        List<Coords> path = new ArrayList<>();
        while (current != null)
        {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    public List<Coords> findShortestPath(char[][] map, Coords start, Coords end)
    {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getfCost));
        Set<Coords> closedSet = new HashSet<>();
        Map<Coords, Coords> cameFrom = new HashMap<>();

        Node startNode = new Node(start, 0, heuristic(start, end));
        openSet.add(startNode);

        while (!openSet.isEmpty())
        {
            Node current = openSet.poll();

            if (current.coords.equals(end))
            {
                return reconstructPath(cameFrom, current.coords);
            }

            closedSet.add(current.coords);

            // Check neighbors
            for (int i = 0; i < 4; i++)
            {
                Coords neighbor = switch (i)
                {
                    case 0 ->
                    {
                        yield new Coords(current.coords.x() + 1, current.coords.y());
                    }
                    case 1 ->
                    {
                        yield new Coords(current.coords.x() - 1, current.coords.y());
                    }
                    case 2 ->
                    {
                        yield new Coords(current.coords.x(), current.coords.y() + 1);
                    }
                    case 3 ->
                    {
                        yield new Coords(current.coords.x(), current.coords.y() - 1);
                    }
                    default ->
                    {
                        yield null;
                    }
                };

                // Skip invalid or blocked coordinates
                if (!isValidCoord(neighbor, mapSize) || map[neighbor.x][neighbor.y] == '#' || closedSet.contains(neighbor))
                {
                    continue;
                }

                // get G-Cost (cost from start to neighbor)
                int tentativeG = current.gCost + 1;

                // Check if the neighbor is in the open set
                Optional<Node> neighborInOpenSet = openSet.stream().filter(n -> n.coords.equals(neighbor)).findFirst();
                if (neighborInOpenSet.isEmpty() || tentativeG < neighborInOpenSet.get().gCost)
                {
                    cameFrom.put(neighbor, current.coords);
                    Node neighborNode = new Node(neighbor, tentativeG, tentativeG + heuristic(neighbor, end));
                    openSet.add(neighborNode);
                }
            }
        }

        // If no path was found
        return Collections.emptyList();
    }

    @Override
    public char[][] read(String path)
    {
        List<Coords> coordinates = new ArrayList<>();

        try
        {
            List<String> lines = Files.readAllLines(Path.of(path));

            for (String line : lines.subList(0,1023))
            {
                String[] parts = line.split(",");
                coordinates.add(new Coords(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim())));
            }
        }
        catch (IOException e)
        {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }

        char[][] map = new char[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++)
        {
            for (int j = 0; j < mapSize; j++)
            {
                map[i][j] = '.';
            }
        }

        for (Coords coord : coordinates)
        {
            map[coord.x()][coord.y()] = '#';
        }

        return map;
    }

    @Override
    public String getSolutionString(String path) throws Exception
    {
        char[][] map = this.read(path);
        if (map == null) return "Error reading file.";

        //Print map for visualisation
        StringBuilder mapOutput = new StringBuilder();
        for (int y = 0; y < mapSize; y++)
        {
            for (int x = 0; x < mapSize; x++)
            {
                mapOutput.append(map[x][y]);
            }
            mapOutput.append("\n");
        }

        System.out.println(mapOutput);

        List<Coords> foundPath = findShortestPath(map, new Coords(0, 0), new Coords(70, 70));

        // Return the path
        if (!foundPath.isEmpty())
        {
            System.out.println("Shortest Path with " + (foundPath.size() - 1) + " steps:");
            for (Coords coord : foundPath)
            {
                map[coord.x()][coord.y()] = 'O';
            }

            //Print map for visualisation
            StringBuilder mapPathOutput = new StringBuilder();
            for (int y = 0; y < mapSize; y++)
            {
                for (int x = 0; x < mapSize; x++)
                {
                    mapPathOutput.append(map[x][y]);
                }
                mapPathOutput.append("\n");
            }
            return mapPathOutput.toString();
        }

        return "No Path Found!";
    }

    static class Node
    {

        Coords coords;
        int gCost; // Cost from start
        int fCost; // Total cost (g + heuristic)

        Node(Coords coords, int gCost, int totalCost)
        {
            this.coords = coords;
            this.gCost = gCost;
            this.fCost = totalCost;
        }

        int getfCost()
        {
            return fCost;
        }
    }

    record Coords(int x, int y)
    {

    }
}

