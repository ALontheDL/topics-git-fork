import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NewCommitTester {
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @Test
    @DisplayName("Testing createTree")
    public void testCreateTree() {
        Commit commit = new Commit("treeSHA", "author", "summary", null, null);
        String treeSHA = commit.createTree("objects", "treeContent");

        assertNotNull(treeSHA);
        System.out.println("createTree passed");
    }

    @Test
    @DisplayName("Testing generateSHA")
    public void testGenerateSHA() {
        Commit commit = new Commit("treeSHA", "author", "summary", null, null);
        String SHA = commit.generateSHA();
        assertNotNull(SHA);
        System.out.println("generateSHA passed");
    }

    @Test
    @DisplayName("Testing getDate")
    public void testGetDate() {
        Commit commit = new Commit("treeSHA", "author", "summary", null, null);
        String date = commit.getDate();
        assertNotNull(date);
        System.out.println("getDate passed");
    }

    @Test
    @DisplayName("Testing writeToFile")
    public void testWriteToFile() throws Exception {
        Commit commit = new Commit("treeSHA", "author", "summary", null, null);
        String folderPath = "testObjects";
        commit.writeToFile(folderPath);
    
        String sha1 = commit.generateSHA();
        File commitFile = new File(folderPath + File.separator + sha1);
        if (commitFile.exists()) {
            System.out.println("writeToFile passed.");
        } else {
            System.out.println("writeToFile failed: Commit file not found.");
        }
        commitFile.delete();
    }
}
