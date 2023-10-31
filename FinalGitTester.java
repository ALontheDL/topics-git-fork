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

    private void createTestFile(String fileName, String content) throws IOException {
        String filePath = Paths.get(testFolderPath, fileName).toString();
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(filePath);
        writer.write(content);
        writer.close();
    }
}
