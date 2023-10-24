import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class NewGitTester {
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
        Utils.deleteDirectory("objects");
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // Test Git class methods individually
        testInit();
        testIndexFileCreation();
        testObjectsFolderCreation();
        testAdd();
        testGetSHA1();
    }

    @Test
    @DisplayName("Testing init")
    public static void testInit() throws IOException {
        Git git = new Git();
        git.init();

        // Verify that the "Git" file and "objects" folder were created
        assert Files.exists(Paths.get("Git"));
        assert Files.exists(Paths.get("objects"));

        System.out.println("init method tested successfully.");
    }

    @Test
    @DisplayName("Testing index File Creation")
    public static void testIndexFileCreation() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();

        // Create an example index file
        git.add("testGit.txt");

        // Verify that the index file was created
        assert Files.exists(Paths.get("Git"));

        System.out.println("Index file creation tested successfully.");
    }

    @Test
    @DisplayName("Testing 'object' Folder Creation")
    public static void testObjectsFolderCreation() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();

        // Create an example object (blob)
        git.add("testGit.txt");

        // Verify that the "objects" folder was created
        assert Files.exists(Paths.get("objects"));

        System.out.println("'Objects' folder creation tested successfully.");
    }

    @Test
    @DisplayName("Testing add")
    public static void testAdd() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();

        // Create and add a blob
        git.add("testGit.txt");

        // Verify that the blob was added to the "Git" file
        assert Files.readAllLines(Paths.get("Git")).stream().anyMatch(line -> line.startsWith("testGit.txt : "));

        System.out.println("Add method tested successfully.");
    }

    @Test
    @DisplayName("Testing getSHA1")
    public static void testGetSHA1() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();

        // Create and add a blob
        git.add("testGit.txt");

        // Get the SHA-1 hash of the added blob
        Blob blob = new Blob("testGit.txt");
        String sha1 = blob.getShaName();

        // Verify that the retrieved SHA-1 hash matches the expected format
        assert sha1.matches("^[0-9a-f]{40}$");

        System.out.println("GetSHA1 method tested successfully.");
    }

    @Test
    @DisplayName("Testing delete")
    public static void testDelete() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();

        // Create and add a blob
        git.add("testGit.txt");

        // Delete the added blob
        git.delete("testGit.txt");

        // Verify that the blob was removed from the "Git" file
        assert Files.readAllLines(Paths.get("Git")).stream().noneMatch(line -> line.startsWith("testGit.txt : "));

        System.out.println("Delete method tested successfully.");
    }

    @Test
    @DisplayName("Testing alreadyExists")
    public static void testAlreadyExists() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();

        // Create and add a blob
        git.add("testGit.txt");

        // Check if the blob already exists
        boolean exists = git.existsAlready("testGit.txt", "SHA-1-hash"); // Replace with the actual SHA-1 hash

        // Verify that the method correctly detects the existing blob
        assert exists;

        System.out.println("AlreadyExists method tested successfully.");
    }
}

