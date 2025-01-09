public class AoCMain
{

    public static void main(String[] args) throws Exception
    {
        Day19PartTwo DayHandler = new Day19PartTwo();
        System.out.println(DayHandler.getSolutionString("C:\\Project\\inputfiles\\input19"));
    }

    // INTERFACES
    interface AoCHandler
    {
        Object read(String path) throws Exception;
        String getSolutionString(String path) throws Exception;
    }
}
