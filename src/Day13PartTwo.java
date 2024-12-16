import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13PartTwo
{

    public static void main(String[] args) throws IOException
    {
        File inputFile = new File("C:\\Project\\inputfiles\\input13");
        List<Target> games = loadGamesFromFile(inputFile);
        System.out.println("Total need tokens: " + getTotalTokens(games));
    }

    private static BigInteger getTotalTokens(List<Target> games)
    {
        BigInteger totalTokens = BigInteger.ZERO;
        for (Target game : games)
        {
            BigInteger[] result = FindNeededPresses(game.button1, game.button2, game.target);

            if (result != null)
            {
                totalTokens = totalTokens.add( result[0].multiply(BigInteger.valueOf(3)).add(result[1]));
            }
        }
        return totalTokens;
    }

    private static List<Target> loadGamesFromFile(File inputFile) throws IOException
    {
        List<Target> targets = new ArrayList<>();

        Pattern pattern = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)\\s+" + "Button B: X\\+(\\d+), Y\\+(\\d+)\\s+" + "Prize: X=(\\d+), Y=(\\d+)");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)))
        {
            String line;
            StringBuilder inputBlock = new StringBuilder();

            while ((line = reader.readLine()) != null)
            {
                if (line.trim().isEmpty())
                {
                    processBlock(inputBlock.toString(), pattern, targets, new BigInteger("10000000000000"));
                    inputBlock.setLength(0);
                } else
                {
                    inputBlock.append(line).append(" ");
                }
            }

            if (!inputBlock.isEmpty())
            {
                processBlock(inputBlock.toString(), pattern, targets, new BigInteger("10000000000000"));
            }
        }

        return targets;
    }

    private static void processBlock(String block, Pattern pattern, List<Target> targets)
    {
        Matcher matcher = pattern.matcher(block);
        if (matcher.find())
        {
            BigInteger[] button1 = {new BigInteger(matcher.group(1)), new BigInteger(matcher.group(2))};
            BigInteger[] button2 = {new BigInteger(matcher.group(3)), new BigInteger(matcher.group(4))};
            BigInteger[] prize = {new BigInteger(matcher.group(5)), new BigInteger(matcher.group(6))};

            targets.add(new Target(button1, button2, prize));
        }
    }

    private static void processBlock(String block, Pattern pattern, List<Target> targets, BigInteger offset)
    {
        Matcher matcher = pattern.matcher(block);
        if (matcher.find())
        {
            BigInteger[] button1 = {new BigInteger(matcher.group(1)), new BigInteger(matcher.group(2))};
            BigInteger[] button2 = {new BigInteger(matcher.group(3)), new BigInteger(matcher.group(4))};
            BigInteger[] prize = {new BigInteger(matcher.group(5)).add(offset), new BigInteger(matcher.group(6)).add(offset)};

            targets.add(new Target(button1, button2, prize));
        }
    }

    public static BigInteger[] FindNeededPresses(BigInteger[] V1, BigInteger[] V2, BigInteger[] V3)
    {
        BigInteger button1X = V1[0], button1Y = V1[1];
        BigInteger button2X = V2[0], button2Y = V2[1];
        BigInteger targetX = V3[0], V3_y = V3[1];

        BigInteger determinant = button1X.multiply(button2Y).subtract(button1Y.multiply(button2X));

        if (determinant.equals(BigInteger.ZERO))
        {
            return null;
        }

        BigInteger nNumerator = targetX.multiply(button2Y).subtract(V3_y.multiply(button2X));
        BigInteger mNumerator = button1X.multiply(V3_y).subtract(button1Y.multiply(targetX));

        if (!nNumerator.remainder(determinant).equals(BigInteger.ZERO) || !mNumerator.remainder(determinant).equals(BigInteger.ZERO))
        {
            return null;
        }

        BigInteger n = nNumerator.divide(determinant);
        BigInteger m = mNumerator.divide(determinant);

        return new BigInteger[]{n, m};
    }

    record Target(BigInteger[] button1, BigInteger[] button2, BigInteger[] target)
    {

    }
}
