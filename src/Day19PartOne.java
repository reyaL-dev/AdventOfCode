import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day19PartOne implements AoCMain.AoCHandler
{


    // Unfit for larger Strings
    private static void findCombinationsHelper(List<String> availablePatterns, String target, int index,
                                               List<String> currentCombination, List<List<String>> result)
    {
        if (index == target.length())
        {
            result.add(new ArrayList<>(currentCombination));
            return;
        }

        for (String s : availablePatterns)
        {
            if (target.startsWith(s, index))
            {
                currentCombination.add(s);
                findCombinationsHelper(availablePatterns, target, index + s.length(), currentCombination, result);
                currentCombination.remove(currentCombination.size() - 1);
            }
        }
    }

    public static List<String> findFirstCombination(List<String> availablePatterns, String design)
    {
        List<String> result = new ArrayList<>();
        if (findFirstCombinationHelper(availablePatterns, design, 0, result))
        {
            return result;
        }
        return null;
    }

    private static boolean findFirstCombinationHelper(List<String> availablePatterns, String design, int index, List<String> result)
    {
        if (index == design.length())
        {
            return true;
        }

        for (String s : availablePatterns)
        {
            if (design.startsWith(s, index))
            {
                // If the string `s` can fit at the current index, use it
                result.add(s);
                if (findFirstCombinationHelper(availablePatterns, design, index + s.length(), result))
                {
                    return true; // Stop further searching once a valid combination is found
                }
                // Backtrack
                result.remove(result.size() - 1);
            }
        }
        return false; // No valid combination found at this branch
    }

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
        int validDesigns = 0;
        ParsedFile input = (ParsedFile) read(path);
        System.out.println(input.toString());
        for (String design : input.desiredDesigns)
        {
            /*List<List<String>> combinations = findCombinations(input.availablePatterns, design);

            if (!combinations.isEmpty())
            {
                System.out.println("Found Patterns for design: " + design);
                for (List<String> combination : combinations)
                {
                    System.out.println(combination);
                }
                validDesigns++;
            }*/

            List<String> combination = findFirstCombination(input.availablePatterns, design);

            if (!(combination == null || combination.isEmpty()))
            {
                System.out.println("Found Patterns for design: " + design);
                System.out.println(combination);
                validDesigns++;
            }

        }
        System.out.println("Total valid designs: " + validDesigns);
        return input.toString();
    }

    private List<List<String>> findCombinations(List<String> availablePatterns, String design)
    {
        List<List<String>> result = new ArrayList<>();
        findCombinationsHelper(availablePatterns, design, 0, new ArrayList<>(), result);
        return result;
    }

    class ParsedFile
    {

        private final List<String> availablePatterns;
        private final List<String> desiredDesigns;

        public ParsedFile(List<String> commaSeparatedValues, List<String> lines)
        {
            this.availablePatterns = commaSeparatedValues;
            this.desiredDesigns = lines;
        }

        public List<String> getAvailablePatterns()
        {
            return availablePatterns;
        }

        public List<String> getDesiredDesigns()
        {
            return desiredDesigns;
        }

        @Override
        public String toString()
        {
            return "Comma-separated values: " + availablePatterns + "\nLines: " + desiredDesigns;
        }
    }
}

