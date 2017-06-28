package il.ac.technion.cs.sd.sub.test;

import org.junit.Test;

public class CsvRandomFileCreaterTest {
    @Test
    public void createFile() throws Exception {
        CsvRandomFileCreator file = new CsvRandomFileCreator("ourBig.csv");   //choose name for file
        file.createFile();

    }
}
