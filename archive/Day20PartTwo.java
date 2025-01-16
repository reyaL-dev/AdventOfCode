import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class Day20PartTwo implements AoCMain.AoCHandler
{

    private final Mapfile fileInput;

    public Day20PartTwo(String path)
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

    private Map<Integer, List<Coords>> findValidShortcutsInRadius(Coords pos, List<Coords> remainingPath, int radius, int minDistance)
    {
        Map<Integer, List<Coords>> validCoords = new HashMap<>();
        Set<Coords> radialCoords = new HashSet<>();

        // Use taxicab radius
        for (int i = 0; i <= radius; i++)
        {
            for (int j = 0; j <= radius; j++)
            {
                if ((i + j) <= radius && (i + j > 1))
                {
                    radialCoords.add(new Coords(i, j));
                    radialCoords.add(new Coords(-i, j));
                    radialCoords.add(new Coords(i, -j));
                    radialCoords.add(new Coords(-i, -j));
                }
            }
        }

        for (Coords radialCoord : radialCoords)
        {
            Coords checkCoord = new Coords(radialCoord.x() + pos.x(), radialCoord.y() + pos.y());
            if (!isValidCoord(checkCoord))
                continue;

            // Skip if minimum distance can't be reached or not a valid Coord
            int shortcutLength = remainingPath.indexOf(checkCoord) + 1;
            if (shortcutLength == 0)
                continue;

            int cheatDistance = Math.abs(radialCoord.x) + Math.abs(radialCoord.y);
            if (minDistance <= shortcutLength - cheatDistance)
            {
                validCoords.computeIfAbsent(shortcutLength - cheatDistance, k -> new ArrayList<>()).add(checkCoord);
            }
        }
        return validCoords;
    }

    private List<Coords> findShortestPath(char[][] map, Coords start, Coords end)
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
            for (int i = 0; i <= 3; i++)
            {
                Coords neighbor = switch (i)
                {
                    case 0 -> new Coords(current.coords.x() + 1, current.coords.y());
                    case 1 -> new Coords(current.coords.x() - 1, current.coords.y());
                    case 2 -> new Coords(current.coords.x(), current.coords.y() + 1);
                    case 3 -> new Coords(current.coords.x(), current.coords.y() - 1);
                    default ->
                    {
                        yield null;
                    }
                };

                // Skip invalid or blocked coordinates
                if (!isValidCoord(neighbor) || getCharAt(map, neighbor) == '#' || closedSet.contains(neighbor))
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

        List<Coords> foundPath = findShortestPath(fileInput.map, fileInput.startCoord, this.fileInput.endCoord);

        // Return the path
        if (!foundPath.isEmpty())
        {
            TreeMap<Integer, List<Coords>> shortCuts = findShortCuts(foundPath);
            int numValidCheats = 0;
            for (Map.Entry<Integer, List<Coords>> entry : shortCuts.entrySet())
            {
                System.out.println("There are " + entry.getValue().size() + " cheats that save " + entry.getKey() + " picoseconds.");
                numValidCheats += entry.getValue().size();
            }
            return "Number of valid cheats: " + numValidCheats;
        }

        return "No Path Found!";
    }

    private TreeMap<Integer, List<Coords>> findShortCuts(List<Coords> foundPath)
    {
        TreeMap<Integer, List<Coords>> shortcutMap = new TreeMap<>();

        for (int i = 0; i < foundPath.size(); i++)
        {
            Map<Integer, List<Coords>> shortCutPositions = findValidShortcutsInRadius(foundPath.get(i), foundPath.subList(i + 1, foundPath.size()), 20, 100);

            for (Map.Entry<Integer, List<Coords>> entry : shortCutPositions.entrySet())
            {
                shortcutMap.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
            }
        }
        return shortcutMap;
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

