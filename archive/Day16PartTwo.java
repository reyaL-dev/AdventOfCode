import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class Day16PartTwo implements AoCMain.AoCHandler
{

    private final Mapfile fileInput;

    public Day16PartTwo(String path)
    {
        this.fileInput = read(path);
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

    private static char getCharAt(char[][] map, Coords neighbor)
    {
        return map[neighbor.y][neighbor.x];
    }

    private boolean isValidCoord(Coords coord)
    {
        return coord.x >= 0 && coord.x < this.fileInput.map[0].length && coord.y >= 0 && coord.y < this.fileInput.map.length;
    }

    private Route findShortestPath(char[][] map, Coords start, Coords end)
    {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getfCost));
        Set<Coords> closedSet = new HashSet<>();
        Map<Coords, Coords> cameFrom = new HashMap<>();

        Node startNode = new Node(start, 0, heuristic(start, end), 0);
        openSet.add(startNode);

        while (!openSet.isEmpty())
        {
            Node current = openSet.poll();

            if (current.coords.equals(end))
            {
                return new Route(reconstructPath(cameFrom, end), current.fCost);
            }

            closedSet.add(current.coords);

            // Check neighbors
            for (int i = 0; i <= 3; i++)
            {
                Coords neighbor = switch (i)
                {
                    case 0 -> new Coords(current.coords.x() + 1, current.coords.y()); // East
                    case 1 -> new Coords(current.coords.x(), current.coords.y() + 1); // South
                    case 2 -> new Coords(current.coords.x() - 1, current.coords.y()); // West
                    case 3 -> new Coords(current.coords.x(), current.coords.y() - 1); // North
                    default ->
                    {
                        yield null;
                    }
                };
                int distance = Math.abs(current.direction - i);
                int numTurns = Math.min(distance, 4 - distance);

                // Skip invalid or blocked coordinates or backwards
                if (!isValidCoord(neighbor) || getCharAt(map, neighbor) == '#' || closedSet.contains(neighbor) || numTurns == 2)
                {
                    continue;
                }


                // get G-Cost (cost from start to neighbor)
                int tentativeG = current.gCost + numTurns * 1000 + 1;

                // Check if the neighbor is in the open set
                Optional<Node> neighborInOpenSet = openSet.stream().filter(n -> n.coords.equals(neighbor)).findFirst();
                if (neighborInOpenSet.isEmpty() || tentativeG < neighborInOpenSet.get().gCost)
                {
                    cameFrom.put(neighbor, current.coords);
                    Node neighborNode = new Node(neighbor, tentativeG, tentativeG + heuristic(neighbor, end), i);
                    openSet.add(neighborNode);
                }
            }
        }
        // If no path was found
        return new Route(Collections.emptyList(), 0);
    }

    @Override
    public Mapfile read(String path)
    {
        try
        {
            List<String> lines = Files.readAllLines(Path.of(path));
            char[][] map = new char[lines.size()][];
            Coords startCoord = null, endCoord = null;
            for (int i = 0; i < lines.size(); i++)
            {
                if (lines.get(i).indexOf('S') != -1)
                {
                    startCoord = new Coords(lines.get(i).indexOf('S'), i);
                }
                if (lines.get(i).indexOf('E') != -1)
                {
                    endCoord = new Coords(lines.get(i).indexOf('E'), i);
                }
                map[i] = lines.get(i).toCharArray();
            }
            return new Mapfile(map, startCoord, endCoord);
        }
        catch (IOException e)
        {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String getSolutionString(String parameter) throws Exception
    {
        // Print the inputmap to verify
        System.out.println(fileInput);

        Route foundPath = findShortestPath(fileInput.map, fileInput.startCoord, new Coords(3, 9));
        //Route foundPath = findShortestPath(fileInput.map, fileInput.startCoord, this.fileInput.endCoord);

        // Return the path
        if (!foundPath.pathCords().isEmpty())
        {
            System.out.println("Shortest Path with " + (foundPath.pathCords().size() - 1) + " steps and cost " + foundPath.cost() + " :");
            System.out.println(foundPath.toString());
        }

        return "No Path Found!";
    }

    static class Node
    {

        Coords coords;

        int gCost; // Cost from start
        int fCost; // Total cost (g + heuristic)
        int direction; // 0 = East, 1 = South, 2 = West, 3 = North

        Node(Coords coords, int gCost, int totalCost, int direction)
        {
            this.coords = coords;
            this.gCost = gCost;
            this.fCost = totalCost;
            this.direction = direction;
        }

        int getfCost()
        {
            return fCost;
        }
    }

    record Coords(int x, int y)
    {

    }

    record Route(List<Coords> pathCords, int cost)
    {

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            for (Coords coords : pathCords)
            {
                sb.append(coords).append("\n");
            }
            return "Route{" +
                    "pathCords=" + sb.toString() +
                    ", cost=" + cost +
                    '}';
        }
    }

    class Mapfile
    {

        final char[][] map;
        final Coords startCoord;
        final Coords endCoord;

        public Mapfile(char[][] map, Coords startCoord, Coords endCoord)
        {
            this.map = map;
            this.startCoord = startCoord;
            this.endCoord = endCoord;
        }

        @Override
        public String toString()
        {
            String output = "";
            for (char[] row : map)
            {
                output += String.valueOf(row) + "\n";
            }
            return output + "\nStartCoord=" + startCoord + "\nEndCoord=" + endCoord;
        }
    }
}

