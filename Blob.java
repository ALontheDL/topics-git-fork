import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob {
    private Path toTextFile;
    private String fileContents;
    private String shaName;

    public Blob(String nameOfFile) throws IOException {
        toTextFile = Paths.get(nameOfFile);
        fileContents = readText(nameOfFile);
        shaName = doSha(fileContents);
    }

    public static String doSha(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);

            while (hashText.length() < 40) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String readText(String fileName) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        }
        return output.toString();
    }

    public String getShaName() {
        return shaName;
    }

    public byte[] makeBite() {
        return fileContents.getBytes();
    }

    public void makeBlob() throws IOException {
        byte[] insideFile = makeBite();
        String folderPath = "Objects";
        Path toObjectsFolder = Paths.get(folderPath, shaName);
        Files.write(toObjectsFolder, insideFile);
    }

    public Path getToTextFile() {
        return toTextFile;
    }

    public String getFileContents() {
        return fileContents;
    }
}
