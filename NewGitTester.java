import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

public class NewGitTester {

    @Test
    @DisplayName("Testing init")
    public void testInit() throws IOException {
        Git git = new Git();
        git.init();
        assertTrue(Files.exists(Paths.get("Git")));
        assertTrue(Files.exists(Paths.get("objects")));
    }


    @Test
    @DisplayName("Testing add")
    public void testAdd() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        String fileName = "testGit.txt";
        git.add(fileName);

        String gitIndexContents = Files.readString(Paths.get("Git"));
        assertTrue(gitIndexContents.contains(fileName));
        assertTrue(Files.exists(Paths.get("./objects/" + new Blob(fileName).getShaName() + ".zip")));
    }

    @Test
    @DisplayName("Testing addEditedFile")
    public void testAddEditedFile() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        String fileName = "testGit.txt";
        git.add(fileName);
        Files.writeString(Paths.get(fileName), "Modified content");
        git.add(fileName);

        String gitIndexContents = Files.readString(Paths.get("Git"));
        assertTrue(gitIndexContents.contains("*edited* " + fileName));
    }

    @Test
    @DisplayName("Testing getSHA1")
    public void testGetSHA1() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        git.add("testGit.txt");

        Blob blob = new Blob("testGit.txt");
        String sha1 = blob.getShaName();
        assertTrue(sha1.matches("^[0-9a-f]{40}$"));
    }

    @Test
    @DisplayName("Testing delete")
    public void testDelete() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        String fileName = "testGit.txt";
        git.add(fileName);

        git.delete(fileName);
        String gitIndexContents = Files.readString(Paths.get("Git"));
        assertTrue(gitIndexContents.contains("*deleted* " + fileName));
        assertFalse(Files.exists(Paths.get("./objects/" + new Blob(fileName).getShaName() + ".zip")));
    }

    @Test
    @DisplayName("Testing checkout")
    public void testCheckout() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        String fileName = "testGit.txt";
        git.add(fileName);
        String initialSHA = git.existsAlready(fileName, new Blob(fileName).getShaName()) ?
                new Blob(fileName).getShaName() : "";

        Files.writeString(Paths.get(fileName), "Modified content");
        git.add(fileName);
        String updatedCommitSHA = "0a5783c9c2598f1f4eb7f5a2301f90c861cfaaf7";

        git.checkout(initialSHA);
        assertEquals(initialSHA, new Blob(fileName).getShaName());
        git.checkout(updatedCommitSHA);
        assertEquals(git.existsAlready(fileName, new Blob(fileName).getShaName()), true);
    }

    @Test
    @DisplayName("Testing existsAlready")
    public void testAlreadyExists() throws IOException, NoSuchAlgorithmException {
        Git git = new Git();
        git.init();
        git.add("testGit.txt");
        boolean exists = git.existsAlready("testGit.txt", "SHA-1-hash");
        assertTrue(exists);
    }
}
