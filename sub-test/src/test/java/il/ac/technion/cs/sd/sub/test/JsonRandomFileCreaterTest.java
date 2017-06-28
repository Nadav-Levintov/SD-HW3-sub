package il.ac.technion.cs.sd.sub.test;

import org.junit.Test;

/**
 * Created by benny on 09/06/2017.
        */
public class JsonRandomFileCreaterTest {
    @Test
    public void createFile() throws Exception {
        JsonRandomFileCreater file = new JsonRandomFileCreater("ourBig.json");   //choose name for file
        file.createFile();

    }

}