package il.ac.technion.cs.sd.sub.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by benny on 25/06/2017.
 */
public class CsvRandomFileCreator {

    private String fileName;
    CsvRandomFileCreator(String fileName)
    {
        this.fileName = fileName;
    }

    public void createFile() throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder lineBuild = new StringBuilder();
        String line;
        RandomString randName = new RandomString(4);
        Integer amount;
        /*journal */
        for(int i=0; i<1000000; i++)
        {
            lineBuild.delete(0,lineBuild.length());
            lineBuild.append("journal,");
            lineBuild.append(randName.nextString()).append(",");
            amount = ThreadLocalRandom.current().nextInt(1, 1000);
            lineBuild.append(amount);
            lines.add(lineBuild.toString());

        }
        /*subscription */
        for(int i=0; i<1000000; i++)
        {
            lineBuild.delete(0,lineBuild.length());
            lineBuild.append("subscription,");
            lineBuild.append(randName.nextString()).append(",");
            lineBuild.append(randName.nextString());
            lines.add(lineBuild.toString());

        }
        /*cancel*/
        for(int i=0; i<1000000; i++)
        {
            lineBuild.delete(0,lineBuild.length());
            lineBuild.append("cancel,");
            lineBuild.append(randName.nextString()).append(",");
            lineBuild.append(randName.nextString());
            lines.add(lineBuild.toString());
        }
        Path file = Paths.get(fileName);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }

}
