import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class NewBlobTester {
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        /*
         * Utils.writeStringToFile("junit_example_file_data.txt", "test file contents");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        /*
         * Utils.deleteFile("junit_example_file_data.txt");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }
    
    public void main(String[] args) {
        try {
            testConstructor();
            testDoSha();
            testReadText();
            testMakeBite();
            testMakeBlob();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Testing Constructor")
    public void testConstructor() throws IOException {
        Blob blob = new Blob("test.txt");
        System.out.println("File Name: " + blob.getToTextFile());
        System.out.println("SHA-1 Hash: " + blob.getShaName());
        System.out.println("File Contents: " + blob.getFileContents());

        // Ensure the SHA-1 hash is correct for the given file
        assert blob.getShaName().equals("a94a8fe5ccb19ba61c4c0873d391e987982fbbd3a");

        System.out.println("Constructor and related methods tested successfully.");
    }

    @Test
    @DisplayName("Testing doSha")
    public void testDoSha() {
        String testInput = "Test SHA-1 Hashing";
        String expectedHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        String actualHash = Blob.doSha(testInput);
        assert actualHash.equals(expectedHash);

        System.out.println("doSha method tested successfully.");
    }

    @Test
    @DisplayName("Testing readText")
    public void testReadText() throws IOException {
        String testFileName = "test.txt";
        String expectedContent = "This is a test file for reading.";
        String actualContent = new Blob(testFileName).readText(testFileName);
        assert actualContent.equals(expectedContent);

        System.out.println("readText method tested successfully.");
    }

    @Test
    @DisplayName("Testing makeBite")
    public void testMakeBite() throws IOException {
        String testFileName = "test.txt";
        byte[] expectedBytes = "This is a test file for reading.".getBytes();
        byte[] actualBytes = new Blob(testFileName).makeBite(testFileName);
        assert new String(actualBytes).equals(new String(expectedBytes));

        System.out.println("makeBite method tested successfully.");
    }

    @Test
    @DisplayName("Testing makeBlob")
    public void testMakeBlob() throws IOException {
        String testFileName = "test.txt";
        Blob blob = new Blob(testFileName);
        blob.makeBlob();

        assert Files.exists(Paths.get("Objects", blob.getShaName()));

        System.out.println("makeBlob method tested successfully.");
    }
}
