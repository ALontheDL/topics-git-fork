import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class NewBlobTester {
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
    @DisplayName("Testing file writing")
    public void testBlob(){
        String testSHA = Blob.doSha("ajdfljsofjowisdoujxlx");
        assertEquals(testSHA, "a6f717f06c3a2d3543d2f95c1d36baa73af88ac5");
    }

    @Test
    @DisplayName("Testing file writing")
    public void testWriteToFile() throws Exception{
        Utils.writeToFile("djaofowudjfudisn\njfklhxoiufwjekh2", "Test File");
        Blob blob = new Blob("Test File");
        blob.makeBlob();
        assertTrue(Utils.exists("objects/24db0e9e4351893d753e2e5123d1a19cfcca2b80"));
    }
}
