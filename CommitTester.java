import java.io.File;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class CommitTester {
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
}
