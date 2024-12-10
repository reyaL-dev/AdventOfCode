import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day09PartOne
{

    public static void main(String[] args)
    {
        List<Block> diskSpaceList = parseFile();
        System.out.println(diskSpaceList);
    }

    private static List<Block> parseFile()
    {
        File inputFile = new File("C:\\project\\inputfiles\\input09");
        List<Block> blockList = new ArrayList<>();
        {
            try (BufferedReader br = new BufferedReader(new FileReader(inputFile)))
            {
                String input;

                while ((input = br.readLine()) != null)
                {
                    for (int i = 0; i < input.length(); i += 2)
                    {
                        if (i + 1 >= input.length())
                        {
                            blockList.add(new Block(i / 2, Integer.parseInt(Character.toString(input.charAt(i))), 0));
                        } else
                        {
                            blockList.add(new Block(i / 2, Integer.parseInt(Character.toString(input.charAt(i))), Integer.parseInt(Character.toString(input.charAt(i + 1)))));
                        }
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return blockList;
    }


    static class Block
    {

        int id, len, space;

        public Block(int id, int len, int space)
        {
            this.id = id;
            this.len = len;
            this.space = space;
        }

        public String toString()
        {
            //            String s = id + ":" + len +"x" + space;
            String s = "";
            s += String.join("", Collections.nCopies(len, "[" + id + "]"));
            s += String.join("", Collections.nCopies(space, "."));
            return s;
        }
    }
}