import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public Utils() {
        
    }
    public static void writeToFile(String str, String fileName) throws Exception {
        Path pathObject = Paths.get(fileName);
        if (!exists(fileName)){
            if (pathObject.getParent() != null){
                Files.createDirectories(pathObject.getParent());
            }
            Files.createFile(Paths.get(fileName));
        }
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.print(str);
        writer.close();
    }

    public static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, "UTF-8");
    }

    public static boolean exists (String path){
        return (Files.exists(Paths.get(path)));
    }
    public static void deleteDirectory(String string) {
        File file = new File(string);
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDirectory(f.toString());
            }
        }
        file.delete();
    }

    public static void deleteFile(String string) throws Exception{
        Files.deleteIfExists(Paths.get(string));
    }
}