

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;

public class NewTreeTester {
    public ExpectedException exceptionRule = ExpectedException.none();
    
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

    @Test
    @DisplayName("Testing Initialize")
    public void testAdd() throws Exception{
        Utils.deleteDirectory("objects");

        Tree tree = new Tree();
        String path = "objects/bc323153dcce17da2a8cd62cb240abdc49f3fe7b";
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.generateBlob();
        assertTrue(Utils.exists(path));

        try{
            tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    @DisplayName("Testing Remove Blob")
    public void testRemove() throws Exception{
        Utils.deleteDirectory("objects");
        
        Tree tree = new Tree();
        String path1 = "objects/bc323153dcce17da2a8cd62cb240abdc49f3fe7b";
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.generateBlob();
        assertTrue(Utils.exists(path1));

        assertTrue(tree.remove("file1.txt"));

        //tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        //assertTrue(Utils.exists(path1));

        String path2 = "objects/14a7cd0f425d7a0ff49391ae32abfc853b44fa1f";
        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b : file2.txt");
        tree.generateBlob();
        assertTrue(Utils.exists(path2));

        assertTrue(tree.remove("file2.txt"));

        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b : file2.txt");
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");

        //tree.remove("file1.txt");
        //assertFalse(Utils.exists(path1));
        //assertTrue(Utils.exists(path2));

        //tree.remove("file2.txt");
        //assertFalse(Utils.exists(path2));
    }

    @Test
    @DisplayName("Testing Add Blob")
    public void testWriteToObjects() throws Exception{
        Utils.deleteDirectory("objects");

        Tree tree = new Tree();
        String path = "objects/bc323153dcce17da2a8cd62cb240abdc49f3fe7b";
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.generateBlob();
        assertTrue(Utils.exists(path));

        tree.remove("file1.txt");
        tree.generateBlob();
        assertTrue(Utils.exists("objects/da39a3ee5e6b4b0d3255bfef95601890afd80709"));
    }
}
