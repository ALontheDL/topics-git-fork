import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class FinalGitTester {
    private Git git;

    @Before
    public void setUp() {
        git = new Git();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testSingleCommitWithFiles() throws Exception {
        Commit commit = git.createSingleCommitWithFiles();

        assertNotNull(commit.getTreeSHA());
        assertNull(commit.getPrevSHA());
        assertNull(commit.getNextSHA());
    }

    @Test
    public void testTwoCommitsWithFilesAndFolder() throws Exception {
        Commit commit1 = git.createSingleCommitWithFiles();

        git.createFolderWithFiles();
        Commit commit2 = git.createCommitWithFolder();

        assertNotNull(commit1.getTreeSHA());
        assertNull(commit1.getPrevSHA());
        assertNotNull(commit1.getNextSHA());

        assertNotNull(commit2.getTreeSHA());
        assertNotNull(commit2.getPrevSHA());
        assertNull(commit2.getNextSHA());

        Tree tree1 = git.getCommitTree(commit1.getTreeSHA());
        Tree tree2 = git.getCommitTree(commit2.getTreeSHA());

        assertTrue(tree1.getEntries().size() >= 2);
        assertTrue(tree2.getEntries().size() >= 2);
    }

    @Test
    public void testFourCommitsWithVariousFilesAndFolders() throws Exception {
        Commit commit1 = git.createSingleCommitWithFiles();
        git.createFolderWithFiles();

        Commit commit2 = git.createCommitWithFolder();
        Commit commit3 = git.createSingleCommitWithFiles();

        git.createFolderWithFiles();
        Commit commit4 = git.createCommitWithFolder();

        assertNotNull(commit1.getTreeSHA());
        assertNull(commit1.getPrevSHA());
        assertNotNull(commit1.getNextSHA());

        assertNotNull(commit2.getTreeSHA());
        assertNotNull(commit2.getPrevSHA());
        assertNotNull(commit2.getNextSHA());

        assertNotNull(commit3.getTreeSHA());
        assertNotNull(commit3.getPrevSHA());
        assertNotNull(commit3.getNextSHA());

        assertNotNull(commit4.getTreeSHA());
        assertNotNull(commit4.getPrevSHA());
        assertNull(commit4.getNextSHA());
    }
}
