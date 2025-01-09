public class AoCMain
{

    public static void main(String[] args) throws Exception
    {
        Day18PartTwo DayHandler = new Day18PartTwo();
        System.out.println(DayHandler.getSolutionString("C:\\Project\\inputfiles\\input18"));
    }

    // INTERFACES
    interface AoCHandler
    {
        <T> Object read(String path) throws Exception;
        String getSolutionString(String path) throws Exception;
    }
}
