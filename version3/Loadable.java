import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public interface Loadable {

    boolean processLine(String line, ImageStore imageStore);

    static void tryload(Loadable loadable, String filename, ImageStore imageStore) {
        try
        {
            System.out.println("LOADING " + loadable.getClass() + " USING " + filename);
            Scanner in = new Scanner(new File(filename));
            load(loadable, in, imageStore);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("LOAD " + loadable.getClass() + " FAILED USING " + filename);
            System.err.println(e.getMessage());
        }
    }

    static void load(Loadable loadable, Scanner in, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine())
        {
            try
            {
                if (!loadable.processLine(in.nextLine(), imageStore))
                {
                    System.err.println(String.format("mmA invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e)
            {
                System.err.println(String.format("mmB invalid entry on line %d",
                        lineNumber));
            }
            catch (IllegalArgumentException e)
            {
                System.err.println(String.format("mmC issue on line %d: %s",
                        lineNumber, e.getMessage()));
            }
            catch (LineProcessingError e)
            {
                System.err.println(String.format("%s %d: ",
                        e.getMessage(), lineNumber));
                System.err.println(e.getLine());
            }
            lineNumber++;
        }
    }

}
