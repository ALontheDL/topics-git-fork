import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class NewBlobTester {

    @Test
    public void testConstructor() throws IOException {
        Blob blob = new Blob("test.txt");
        assertEquals("test.txt", blob.getToTextFile().toString());
        assertEquals("4b6fcb2d521ef0fd442a5301e7932d16cc9f375a", blob.getShaName());
        assertEquals("This is a test file for reading.\n", blob.getFileContents());
    }

    @Test
    public void testDoSha() {
        String input = "Test SHA-1 Hashing";
        String expectedHash = "54d12c68fcc94598a043fde6874292288b244dea";
        String actualHash = Blob.doSha(input);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testReadText() throws IOException {
        String testFileName = "test.txt";
        String expectedContent = "This is a test file for reading.\n";
        String actualContent = new Blob(testFileName).readText(testFileName);
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testMakeBite() throws IOException {
        Blob blob = new Blob("test.txt");
        byte[] expectedBytes = "This is a test file for reading.\n".getBytes();
        byte[] actualBytes = blob.makeBite();
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testMakeBlob() throws IOException {
        Blob blob = new Blob("test.txt");
        blob.makeBlob();

        String shaName = blob.getShaName();
        Path blobFilePath = Paths.get("Objects", shaName);
        assertTrue(Files.exists(blobFilePath.toAbsolutePath().normalize()));

        // Cleanup: delete the created blob file
        File blobFile = blobFilePath.toFile();
        if (blobFile.exists()) {
            blobFile.delete();
        }
    }
}
