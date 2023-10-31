import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class FinalGitTester {
    private String testFolderPath = "testObjects";
    private Git git;

    @BeforeEach
    public void setUp() throws Exception {
        git = new Git();
        git.init();
        cleanUpTestObjectsFolder();
    }

    @AfterEach
    public void tearDown() {
        cleanUpTestObjectsFolder();
    }

    private void cleanUpTestObjectsFolder() {
        Path testObjectsPath = Paths.get(testFolderPath);

        if (Files.exists(testObjectsPath)) {
            try {
                Files.walk(testObjectsPath)
                        .sorted((a, b) -> -a.compareTo(b))
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    @DisplayName("Single Commit Test")
    public void testSingleCommit() throws IOException, NoSuchAlgorithmException {
        createTestFile("file1.txt", "Test 1");
        createTestFile("file2.txt", "Test 2");
        git.add("file1.txt");
        git.add("file2.txt");
        git.commit("Initial");
        String currentCommitSHA = git.getHeadCommitSHA();
        String currentTreeSHA = Commit.getCommitTreeSHA(currentCommitSHA);

        assertNotNull(currentCommitSHA);
        assertNotNull(currentTreeSHA);
        assertNull(git.getPrevCommitSHA(currentCommitSHA));
        assertNull(git.getNextCommitSHA(currentCommitSHA));

        Tree currentTree = Tree.getTree(currentTreeSHA, testFolderPath);

        assertTrue(currentTree.entries.contains("blob : " + currentCommitSHA + " : file1.txt"));
        assertTrue(currentTree.entries.contains("blob : " + currentCommitSHA + " : file2.txt"));
    }

    @Test
    @DisplayName("Multiple Commits Test")
    public void testMultipleCommits() throws IOException, NoSuchAlgorithmException {
        createTestFile("file1.txt", "Test 1");
        createTestFile("file2.txt", "Test 2");
        git.add("file1.txt");
        git.add("file2.txt");
        git.commit("First");

        createTestFile("file3.txt", "Test data 3");
        git.add("file3.txt");
        createTestFolder("myFolder");
        createTestFileInFolder("file4.txt", "Test data 4");
        git.add("myFolder");
        git.commit("Second");

        String firstCommitSHA = git.getHeadCommitSHA();
        String firstTreeSHA = Commit.getCommitTreeSHA(firstCommitSHA);

        assertNotNull(firstCommitSHA);
        assertNotNull(firstTreeSHA);
        assertNull(git.getPrevCommitSHA(firstCommitSHA));
        assertNotNull(git.getNextCommitSHA(firstCommitSHA));

        String secondCommitSHA = git.getNextCommitSHA(firstCommitSHA);
        String secondTreeSHA = Commit.getCommitTreeSHA(secondCommitSHA);

        assertNotNull(secondCommitSHA);
        assertNotNull(secondTreeSHA);
        assertEquals(firstCommitSHA, git.getPrevCommitSHA(secondCommitSHA));
        assertNull(git.getNextCommitSHA(secondCommitSHA));
        Tree firstTree = Tree.getTree(firstTreeSHA, testFolderPath);
        assertTrue(firstTree.entries.contains("blob : " + firstCommitSHA + " : file1.txt"));
        assertTrue(firstTree.entries.contains("blob : " + firstCommitSHA + " : file2.txt"));

        Tree secondTree = Tree.getTree(secondTreeSHA, testFolderPath);
        assertTrue(secondTree.entries.contains("blob : " + secondCommitSHA + " : file3.txt"));
        assertTrue(secondTree.entries.contains("tree : " + firstTreeSHA + " : myFolder"));
    }

    @Test
    @DisplayName("Multiple Commits with Unique Data Test")
    public void testMultipleCommitsUniqueData() throws IOException, NoSuchAlgorithmException {
        createTestFile("file1.txt", "Test 1");
        createTestFile("file2.txt", "Test 2");
        git.add("file1.txt");
        git.add("file2.txt");
        git.commit("First");

        createTestFile("file3.txt", "Diff1");
        git.add("file3.txt");
        createTestFolder("myFolder");
        createTestFileInFolder("file4.txt", "Diff2");
        git.add("myFolder");
        git.commit("Second");

        String firstCommitSHA = git.getHeadCommitSHA();
        String firstTreeSHA = Commit.getCommitTreeSHA(firstCommitSHA);

        assertNotNull(firstCommitSHA);
        assertNotNull(firstTreeSHA);
        assertNull(git.getPrevCommitSHA(firstCommitSHA));
        assertNotNull(git.getNextCommitSHA(firstCommitSHA));

        String secondCommitSHA = git.getNextCommitSHA(firstCommitSHA);
        String secondTreeSHA = Commit.getCommitTreeSHA(secondCommitSHA);

        assertNotNull(secondCommitSHA);
        assertNotNull(secondTreeSHA);
        assertEquals(firstCommitSHA, git.getPrevCommitSHA(secondCommitSHA));
        assertNull(git.getNextCommitSHA(secondCommitSHA));

        Tree firstTree = Tree.getTree(firstTreeSHA, testFolderPath);
        assertTrue(firstTree.entries.contains("blob : " + firstCommitSHA + " : file1.txt"));
        assertTrue(firstTree.entries.contains("blob : " + firstCommitSHA + " : file2.txt"));
        Tree secondTree = Tree.getTree(secondTreeSHA, testFolderPath);
        assertTrue(secondTree.entries.contains("blob : " + secondCommitSHA + " : file3.txt"));
        assertTrue(secondTree.entries.contains("tree : " + firstTreeSHA + " : myFolder"));

        String secondCommitPrevSHA = git.getPrevCommitSHA(secondCommitSHA);
        String secondCommitNextSHA = git.getNextCommitSHA(secondCommitSHA);
        String secondCommitTreeSHA = Commit.getCommitTreeSHA(secondCommitSHA);
        Commit secondCommit = Commit.getCommit(secondCommitSHA);
        Commit secondCommitPrev = Commit.getCommit(secondCommitPrevSHA);
        Commit secondCommitNext = Commit.getCommit(secondCommitNextSHA);

        assertEquals(secondCommitPrevSHA, secondCommit.getPrevSHA());
        assertEquals(firstCommitSHA, secondCommit.getPrevSHA());
        assertEquals(secondCommitNextSHA, secondCommit.getNextSHA());
        assertNull(secondCommitNextSHA);
        assertEquals(secondCommitTreeSHA, secondCommit.getTreeSHA());
        Tree secondCommitTree = Tree.getTree(secondTreeSHA, testFolderPath);
        assertTrue(secondCommitTree.entries.contains("blob : " + secondCommitSHA + " : file3.txt"));
        assertTrue(secondCommitTree.entries.contains("tree : " + firstTreeSHA + " : myFolder"));

        assertEquals("Diff1", secondCommitTree.getBlob("file3.txt").getContent());
        assertEquals("Diff2", secondCommitTree.getTree("myFolder", "file3.txt").getBlob("file4.txt").getContent());
    }

    @Test
    @DisplayName("Cleanup After Test")
    public void testCleanUpAfterTest() throws IOException, NoSuchAlgorithmException {
        createTestFile("file1.txt", "Test 1");
        createTestFile("file2.txt", "Test 2");
        git.add("file1.txt");
        git.add("file2.txt");
        git.commit("Test");

        assertTrue(Files.exists(Paths.get("Git")));
        assertTrue(Files.exists(Paths.get("objects")));
        cleanUpTestObjectsFolder();

        assertFalse(Files.exists(Paths.get("Git")));
        assertFalse(Files.exists(Paths.get("objects")));
    }

private void createTestFolder(String folderName) {
        File folder = new File(Paths.get(testFolderPath, folderName).toString());
        folder.mkdirs();
    }

    private void createTestFileInFolder(String filePath, String content) throws IOException {
        String fullPath = Paths.get(testFolderPath, filePath).toString();
        File file = new File(fullPath);
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(fullPath);
        writer.write(content);
        writer.close();
    }


    private void createTestFile(String fileName, String content) throws IOException {
        String filePath = Paths.get(testFolderPath, fileName).toString();
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(filePath);
        writer.write(content);
        writer.close();
    }
}
