import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day04PartTwo
{

    public static void main(String[] args)
    {
        char[][] matrix = parseFile();
        System.out.println("Pattern");
        for (char[] row : matrix)
        {
            System.out.println(Arrays.toString(row));
        }
        String pattern = "SAM";
        int count = countPatternOccurrences(matrix, pattern);
        System.out.println("Number of occurances: " + count);
    }

    public static int countPatternOccurrences(char[][] matrix, String pattern)
    {
        int count = 0;
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                System.out.println("Current coords: " + i + " " + j);

                if ((j <= rows - pattern.length()) && (i <= cols - pattern.length()))
                {
                    if (checkDiagonalDesc(matrix, i, j, pattern))
                    {
                        if (checkDiagonalAsc(matrix, i, j + pattern.length() - 1, pattern))
                        {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    public static boolean checkHorizontal(char[][] matrix, int row, int col, String pattern)
    {
        String forwardSb = new String(matrix[row], col, pattern.length());
        String backwardSb = new StringBuilder(forwardSb).reverse().toString();
        return (forwardSb.equals(pattern) || backwardSb.equals(pattern));
    }

    public static boolean checkVertical(char[][] matrix, int row, int col, String pattern)
    {
        String forwardSb = "";
        for (int i = 0; i < pattern.length(); i++)
        {
            forwardSb += matrix[col + i][row];
        }
        String backwardSb = new StringBuilder(forwardSb).reverse().toString();
        return (forwardSb.equals(pattern) || backwardSb.equals(pattern));
    }


    public static boolean checkDiagonalDesc(char[][] matrix, int row, int col, String pattern)
    {
        String forwardSb = "";
        for (int i = 0; i < pattern.length(); i++)
        {
            forwardSb += matrix[row + i][col + i];
        }
        System.out.println(forwardSb);
        String backwardSb = new StringBuilder(forwardSb).reverse().toString();
        return (forwardSb.equals(pattern) || backwardSb.equals(pattern));
    }

    public static boolean checkDiagonalAsc(char[][] matrix, int row, int col, String pattern)
    {
        String forwardSb = "";
        System.out.println("Current char: " + matrix[row][col]);
        for (int i = 0; i < pattern.length(); i++)
        {
            forwardSb += matrix[row + i][col - i];
        }
        System.out.println(forwardSb);
        String backwardSb = new StringBuilder(forwardSb).reverse().toString();
        return (forwardSb.equals(pattern) || backwardSb.equals(pattern));
    }

    private static char[][] parseFile()
    {
        List<char[]> matrixList = new ArrayList<>();
        File inputFile = new File("C:\\Project\\inputfiles\\input04");
        {
            try
            {
                Scanner scanner = new Scanner(inputFile);
                while (scanner.hasNextLine())
                {
                    String row = scanner.nextLine();
                    matrixList.add(row.toCharArray());
                }
            }
            catch (FileNotFoundException e)
            {
                throw new RuntimeException(e);
            }
            char[][] matrix = new char[matrixList.size()][];
            matrixList.toArray(matrix);

            return matrix;
        }
    }
}


