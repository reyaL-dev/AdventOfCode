import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day19PartTwo implements AoCMain.AoCHandler
{

    private static final Node headNode = new Node(null);
    private static final HashMap<String, Long> cache = new HashMap<>();

    public Object read(String path) throws IOException
    {
        List<String> availablePatterns = new ArrayList<>();
        List<String> desiredDesigns = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            String firstLine = reader.readLine();
            if (firstLine != null)
            {
                String[] values = firstLine.split(",\\s*");
                availablePatterns.addAll(Arrays.asList(values));
            }

            String line;
            while ((line = reader.readLine()) != null)
            {
                if (!line.isBlank())
                {
                    desiredDesigns.add(line);
                }
            }
        }

        return new ParsedFile(availablePatterns, desiredDesigns);
    }

    @Override
    public String getSolutionString(String path) throws Exception
    {
        long validDesigns = 0;
        ParsedFile input = (ParsedFile) read(path);

        // Prefix Trie https://en.wikipedia.org/wiki/Trie
        for (String pattern : input.availablePatterns)
        {
            headNode.addPossible(pattern);
        }

        for (String design : input.desiredDesigns)
        {
            validDesigns += headNode.canMatch(design);
        }

        System.out.println("Total valid designs: " + validDesigns);
        return input.toString();
    }


    static class ParsedFile
    {

        private final List<String> availablePatterns;
        private final List<String> desiredDesigns;

        public ParsedFile(List<String> commaSeparatedValues, List<String> lines)
        {
            this.availablePatterns = commaSeparatedValues;
            this.desiredDesigns = lines;
        }

        @Override
        public String toString()
        {
            return "Comma-separated values: " + availablePatterns + "\nLines: " + desiredDesigns;
        }
    }

    static class Node
    {

        private final Character c;
        private boolean possible;
        private final Node[] out = new Node[5]; // only 5 colors

        public Node(Character c)
        {
            this.c = c;
        }

        public void addPossible(String pattern)
        {
            if (pattern.isEmpty())
            {
                possible = true;
            } else
            {
                char firstChar = pattern.charAt(0);
                int index = convert(firstChar);
                if (out[index] == null)
                {
                    out[index] = new Node(firstChar);
                }
                out[index].addPossible(pattern.substring(1));
            }
        }

        private int convert(char c)
        {
            return switch (c)
            {
                case 'w' -> 0;
                case 'u' -> 1;
                case 'b' -> 2;
                case 'r' -> 3;
                case 'g' -> 4;
                default -> throw new IllegalArgumentException();
            };
        }

        public long canMatch(String pattern)
        {
            if (c == null)
            {
                if (cache.containsKey(pattern))
                {
                    return cache.get(pattern);
                }
            }
            long result = 0;
            if (pattern.isEmpty())
            {
                result = possible ? 1 : 0;
            } else
            {
                if (possible)
                {
                    result += headNode.canMatch(pattern);
                }
                int index = convert(pattern.charAt(0));
                if (out[index] != null)
                {
                    result += out[index].canMatch(pattern.substring(1));
                }
            }
            if (c == null)
            {
                cache.put(pattern, result);
            }
            return result;
        }
    }
}

