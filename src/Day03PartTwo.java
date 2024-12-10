import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03PartTwo
{

    public static void main(String[] args)
    {

        int mulSum = 0;

        File inputFile = new File("C:\\Project\\inputfiles\\input03");
        final Pattern pattern = Pattern.compile("mul\\(([0-9]+,[0-9]+)\\)", Pattern.MULTILINE);
        {
            try
            {

                Scanner scanner = new Scanner(inputFile);
                while (scanner.hasNextLine())
                {
                    String row = scanner.nextLine();

                    Matcher matcher = pattern.matcher(row);

                    while (matcher.find())
                    {
                        String[] strNumbers = matcher.group(1).split(",");

                        mulSum += Integer.parseInt(strNumbers[0]) * Integer.parseInt(strNumbers[1]);
                    }
                }
            }
            catch (FileNotFoundException e)
            {
                throw new RuntimeException(e);
            }

            System.out.println("mulSum: " + mulSum);
        }
    }
}
