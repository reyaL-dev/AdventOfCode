public class AoCMain
{

    public static void main(String[] args) throws Exception
    {
        /*Day16PartOne DayHandler = new Day16PartOne("C:\\Project\\inputfiles\\input16");*/
        Day16PartTwo DayHandler = new Day16PartTwo("C:\\Project\\inputfiles\\input16TEST");
        System.out.println(DayHandler.getSolutionString(""));
    }

    // INTERFACES
    interface AoCHandler
    {
        Object read(String path) throws Exception;
        String getSolutionString(String param) throws Exception;
    }
}
