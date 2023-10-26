import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NewTreeTester {

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

    @Test
    @DisplayName("Testing add")
    public void testAdd() {
        // Test the add method by adding entries to the tree and then verifying that they are present
        Tree tree = new Tree();
        tree.add("entry1");
        tree.add("entry2");
        assertTrue(tree.entries.contains("entry1"));
        assertTrue(tree.entries.contains("entry2"));
    }

    @Test
    @DisplayName("Testing remove")
    public void testRemove() {
        // Test the remove method by adding entries, removing one, and verifying that it is not present
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
        String sha1 = tree.calculateSHA1("test_input");
        assertEquals("expected_sha1_value", sha1);
    }

    @Test
    @DisplayName("Testing generateBlob")
    public void testGenerateBlob() throws Exception {
        Tree tree = new Tree();
        tree.add("entry1");
        tree.add("entry2");

        String folderPath = "test_objects";
        File objectsFolder = new File(folderPath);
        if (!objectsFolder.exists()) {
            objectsFolder.mkdir();
        }
        tree.generateBlob();
        File blobFile = new File(folderPath + File.separator + "expected_sha1_value");
        assertTrue(blobFile.exists());

        deleteFile(folderPath);
    }

    @Test
    @DisplayName("Testing delete")
    public void testDelete() {
        Tree tree = new Tree();
        tree.add("entry1");
        tree.add("entry2");
        tree.delete("entry1");
        assertFalse(tree.entries.contains("entry1"));
        assertTrue(tree.entries.contains("entry2"));
    }

    @Test
    @DisplayName("Testing addFile")
    public void testAddFile() throws IOException {
        Tree tree = new Tree();
        String filename = "test_file.txt";
        File testFile = new File(filename);
        testFile.createNewFile();

        tree.add(filename);
        assertTrue(tree.entries.contains(filename));
        testFile.delete();
    }

    @Test
    @DisplayName("Testing removeFile")
    public void testRemoveFile() throws IOException {
        Tree tree = new Tree();
        String filename = "test_file.txt";
        tree.add(filename);
        tree.remove(filename);

        assertFalse(tree.entries.contains(filename));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NewTreeTester");
    }

    public static void deleteFile(String string) throws Exception{
        Files.deleteIfExists(Paths.get(string));
    }
}
