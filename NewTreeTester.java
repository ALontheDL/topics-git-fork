import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class NewTreeTester {
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @Test
    @DisplayName("Testing Add")
    public void testAdd() {
        Tree tree = new Tree();
        tree.add("entry1");
        tree.add("entry2");
        assertTrue(tree.entries.contains("entry1"));
        assertTrue(tree.entries.contains("entry2"));
    }

    @Test
    @DisplayName("Testing Remove")
    public void testRemove() {
        Tree tree = new Tree();
        tree.add("entry1");
        tree.add("entry2");
        tree.remove("entry1");
        assertFalse(tree.entries.contains("entry1"));
        assertTrue(tree.entries.contains("entry2"));
    }

    @Test
    @DisplayName("Testing calculateSHA1")
    public void testCalculateSHA1() {
        Tree tree = new Tree();
        String sha1 = tree.calculateSHA1("input");

        if (sha1.equals("expected_sha1_value")) {
            System.out.println("calculateSHA1 method passed.");
        } else {
            System.out.println("calculateSHA1 method failed.");
        }
    }

    @Test
    @DisplayName("Testing generateBlob")
    public void testGenerateBlob() throws Exception {
        Tree tree = new Tree();
        tree.add("entry1");
        tree.add("entry2");

        String folderPath = "testObjects";
        File objectsFolder = new File(folderPath);
        if (!objectsFolder.exists()) {
            objectsFolder.mkdir();
        }
        tree.generateBlob();

        File blobFile = new File(folderPath + File.separator + "expected_sha1_value");
        if (blobFile.exists()) {
            System.out.println("generateBlob method passed.");
        } else {
            System.out.println("generateBlob method failed.");
        }

        deleteFile(folderPath);
    }

    @Test
    @DisplayName("Testing adding the file to index")
    public void testAddFileToIndex() {
        Tree tree = new Tree();
        String filename = "testFile.txt";
        tree.add(filename);

        if (tree.entries.contains(filename)) {
            System.out.println("add method passed.");
        } else {
            System.out.println("add method failed.");
        }
    }

    @Test
    @DisplayName("Testing removing the file from")
    public void testRemoveFileFromIndex() {
        Tree tree = new Tree();
        String filename = "testFile.txt";
        tree.add(filename);
        tree.remove(filename);

        if (!tree.entries.contains(filename)) {
            System.out.println("remove method passed.");
        } else {
            System.out.println("remove method failed.");
        }
    }

    public void deleteFile(String string) throws Exception{
        Files.deleteIfExists(Paths.get(string));
    }
}
