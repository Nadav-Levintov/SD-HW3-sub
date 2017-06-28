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
 * Created by benny on 07/06/2017.
 */
final class JsonRandomFileCreater {

    private String fileName;
    JsonRandomFileCreater(String fileName)
    {
        this.fileName = fileName;
    }

    public void createFile() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("[");

        StringBuilder lineBuild = new StringBuilder();
        String line;
        RandomString randName = new RandomString(4);
        RandomString randNameUser = new RandomString(2); //up to 100 users
        Integer amount;
        /*journal */
        for(int i=0; i<1000000; i++)
        {
            lineBuild.delete(0,lineBuild.length());
            lineBuild.append("  {").append("\"type\": ").append("\"journal\"");
            lineBuild.append(", \"journal-id\": \"").append(randName.nextString());
            amount = ThreadLocalRandom.current().nextInt(1, 1000);
            lineBuild.append("\", \"price\": ").append(amount).append("},");
            lines.add(lineBuild.toString());

        }
        /*subscription */
        for(int i=0; i<1000000; i++)
        {
            lineBuild.delete(0,lineBuild.length());
            lineBuild.append("  {").append("\"type\": ").append("\"subscription\"");
            lineBuild.append(", \"user-id\": \"").append(randNameUser.nextString());
            lineBuild.append("\", \"journal-id\": \"").append(randName.nextString()).append("\"},");
            lines.add(lineBuild.toString());
        }
        /*cancel*/
        for(int i=0; i<1000000; i++)
        {
            lineBuild.delete(0,lineBuild.length());
            lineBuild.append("  {").append("\"type\": ").append("\"cancel\"");
            lineBuild.append(", \"user-id\": \"").append(randNameUser.nextString());
            lineBuild.append("\", \"journal-id\": \"").append(randName.nextString()).append("\"},");
            lines.add(lineBuild.toString());
        }

        lineBuild.delete(0,lineBuild.length());
        lineBuild.append("  {").append("\"type\": ").append("\"cancel\"");
        lineBuild.append(", \"user-id\": \"").append(randName.nextString());
        lineBuild.append("\", \"journal-id\": \"").append(randName.nextString()).append("\"}");
        lines.add(lineBuild.toString());

        lines.add("]");
        Path file = Paths.get(fileName);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }

}
