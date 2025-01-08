public class AoCMain
{

    public static void main(String[] args) throws Exception
    {
        Day17PartTwo d17handler = new Day17PartTwo();
        System.out.println(d17handler.getSolutionString("C:\\Project\\inputfiles\\input17"));
    }

    // INTERFACES
    interface AoCHandler
    {
        <T> Object read(String path) throws Exception;
        String getSolutionString(String path) throws Exception;
    }
}
