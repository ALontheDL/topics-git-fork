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
        assert Files.exists(Paths.get("Git"));
        assert Files.exists(Paths.get("objects"));

        System.out.println("init method tested successfully.");
    }

    @Test
    @DisplayName("Testing index File Creation")
    public static void testIndexFileCreation() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        git.add("testGit.txt");

        assert Files.exists(Paths.get("Git"));
        System.out.println("Index file creation tested successfully.");
    }

    @Test
    @DisplayName("Testing 'object' Folder Creation")
    public static void testObjectsFolderCreation() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        git.add("testGit.txt");

        assert Files.exists(Paths.get("objects"));
        System.out.println("'Objects' folder creation tested successfully.");
    }

    @Test
    @DisplayName("Testing add")
    public static void testAdd() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        git.add("testGit.txt");

        assert Files.readAllLines(Paths.get("Git")).stream().anyMatch(line -> line.startsWith("testGit.txt : "));

        System.out.println("Add method tested successfully.");
    }

    @Test
    @DisplayName("Testing getSHA1")
    public static void testGetSHA1() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        git.add("testGit.txt");

        Blob blob = new Blob("testGit.txt");
        String sha1 = blob.getShaName();

        assert sha1.matches("^[0-9a-f]{40}$");

        System.out.println("GetSHA1 method tested successfully.");
    }

    @Test
    @DisplayName("Testing delete")
    public static void testDelete() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        git.add("testGit.txt");
        git.delete("testGit.txt");

        assert Files.readAllLines(Paths.get("Git")).stream().noneMatch(line -> line.startsWith("testGit.txt : "));

        System.out.println("Delete method tested successfully.");
    }

    @Test
    @DisplayName("Testing alreadyExists")
    public static void testAlreadyExists() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        git.add("testGit.txt");

        boolean exists = git.existsAlready("testGit.txt", "SHA-1-hash");

        assert exists;
        System.out.println("AlreadyExists method tested successfully.");
    }
}

