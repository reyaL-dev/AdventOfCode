public class AoCMain
{

    public static void main(String[] args) throws Exception
    {
        Day19PartOne DayHandler = new Day19PartOne();
        System.out.println(DayHandler.getSolutionString("C:\\Project\\inputfiles\\input19"));
    }

    // INTERFACES
    interface AoCHandler
    {
        <T> Object read(String path) throws Exception;
        String getSolutionString(String path) throws Exception;
    }
}
