import static org.junit.Assert.fail;
import java.io.File;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class TreeCoreTester{
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {

    }

    @Test
    @DisplayName("Testing addDirectory in a single folder with 3 files")
    public void testAddDirectoryWithFiles(){
        Tree tree = new Tree();

        String path = "test_directory_1";
        File direc = new File(path);
        direc.mkdir();

        try{
            File file1 = new File(path, "file1.txt");
            File file2 = new File(path, "file2.txt");
            File file3 = new File(path, "file3.txt");
            
            file1.createNewFile();
            file2.createNewFile();
            file3.createNewFile();

            String SHA1 = tree.addDirectory(path);
            deleteDirectory(direc);

            if (SHA1 == null){
                fail("Returned null");
            }
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Testing addDirectory into a folder with files and subfolders")
    public void testAddDirectoryWithSubfolders(){
        Tree tree = new Tree();

        String path = "test_directory_2";
        File direc = new File(path);
        direc.mkdir();

        try{
            File file1 = new File(path, "file1.txt");
            File file2 = new File(path, "file2.txt");
            File file3 = new File(path, "file3.txt");

            file1.createNewFile();
            file2.createNewFile();
            file3.createNewFile();

            String subfolder1Path = path + File.separator + "subfolder1";
            String subfolder2Path = path + File.separator + "subfolder2";
            File subfolder1 = new File(subfolder1Path);
            File subfolder2 = new File(subfolder2Path);
            subfolder1.mkdir();
            subfolder2.mkdir();

            String SHA1 = tree.addDirectory(path);
            deleteDirectory(direc);

            if (SHA1 == null){
                fail("Returned null");
            }
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    private void deleteDirectory(File direc){
        if (direc.exists()){
            File[] files = direc.listFiles();
            if (files != null){
                for (File file : files){
                    if (file.isDirectory()){
                        deleteDirectory(file);
                    } else{
                        file.delete();
                    }
                }
            }
            direc.delete();
        }
    }
}