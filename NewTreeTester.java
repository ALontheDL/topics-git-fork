import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jcp.xml.dsig.internal.dom.Utils;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

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
    @DisplayName("Testing Add")
    public void testAdd() {
        Tree tree = new Tree();
        tree.add("entry1");
        tree.add("entry2");

        if (tree.entries.contains("entry1") && tree.entries.contains("entry2")) {
            System.out.println("add method passed.");
        } else {
            System.out.println("add method failed.");
        }
    }

    @Test
    @DisplayName("Testing Remove")
    public void testRemove() {
        Tree tree = new Tree();
        tree.add("entry1");
        tree.add("entry2");
        tree.remove("entry1");

        if (!tree.entries.contains("entry1") && tree.entries.contains("entry2")) {
            System.out.println("remove method passed.");
        } else {
            System.out.println("remove method failed.");
        }
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
        tree.generateBlob(folderPath);

        File blobFile = new File(folderPath + File.separator + "e127f8a1d93221db8f99e6d1a37c05182e326f78");

        if (blobFile.exists()) {
            System.out.println("GenerateBlob method passed.");
        } else {
            System.out.println("GenerateBlob method failed.");
        }

        Utils.deleteDirectory(folderPath);
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
