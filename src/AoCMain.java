public class AoCMain
{

    public static void main(String[] args) throws Exception
    {
        /*Day20PartOne DayHandler = new Day20PartOne("C:\\Project\\inputfiles\\input20");*/
        Day20PartTwo DayHandler = new Day20PartTwo("C:\\Project\\inputfiles\\input20");
        System.out.println(DayHandler.getSolutionString(""));
    }

    // INTERFACES
    interface AoCHandler
    {
        Object read(String path) throws Exception;
        String getSolutionString(String param) throws Exception;
    }
}
