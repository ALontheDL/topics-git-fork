import java.io.File;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class NewCommitTester {
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
        @DisplayName("Testing createTree")
        public void testCreateTree() {
            Commit commit = new Commit("treeSHA", "author", "summary");
            String treeSHA = commit.createTree("objects", "treeContent");
    
            if (treeSHA != null) {
                System.out.println("createTree passed");
            } else {
                System.out.println("createTree failed");
            }
        }
    
        @Test
        @DisplayName("Testing generateSHA")
        public void testGenerateSHA() {
            Commit commit = new Commit("treeSHA", "author", "summary");
            String SHA = commit.generateSHA();
    
            if (SHA != null) {
                System.out.println("generateSHA passed");
            } else {
                System.out.println("generateSHA failed");
            }
        }
    
        @Test
        @DisplayName("Testing getDate")
        public void testGetDate() {
            Commit commit = new Commit("treeSHA", "author", "summary");
            String date = commit.getDate();
    
            if (date != null && !date.isEmpty()) {
                System.out.println("getDate passed");
            } else {
                System.out.println("getDate failed");
            }
        }
    
        @Test
        @DisplayName("Testing writeToFile")
        public void testWriteToFile() {
            Commit commit = new Commit("treeSHA", "author", "summary");
            String folderPath = "testObjects";
            commit.writeToFile(folderPath);
    
            File commitFile = new File(folderPath + File.separator + "expectedSHA");
            if (commitFile.exists()) {
                System.out.println("writeToFile passed.");
            } else {
                System.out.println("writeToFile failed.");
            }
    
            deleteFolder(folderPath);
        }
    
        private static void deleteFolder(String folderPath) {
            
        }
    }
