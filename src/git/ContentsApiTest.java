package git;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

public class ContentsApiTest {

    @Ignore
    @Test
    public void testGetContentsDir() {
        ContentsApi contentsApi = new ContentsApi();
        Contents contents = contentsApi.getContents("db");
        assertTrue(contents.isDir());
    }

    @Ignore
    @Test
    public void testGetContentsFile() {
        ContentsApi contentsApi = new ContentsApi();
        Contents contents = contentsApi.getContents("db/readme.md");
        assertTrue(contents.isFile());
    }

    @Ignore
    @Test
    public void testGetContentsFileContent() {
        ContentsApi contentsApi = new ContentsApi();
        Contents contents = contentsApi.getContents("db/readme.md");
        assertEquals("このフォルダーに、\nkptのデータが保管されます。\n", contents.getContent());
    }

    @Ignore
    @Test
    public void testCreateFile() {
        ContentsApi contentsApi = new ContentsApi();
        String content = "TESTてすと";
        contentsApi.createFile("db/test.txt", content);

    }

    @Test
    public void testSaveFile() {
        ContentsApi contentsApi = new ContentsApi();
        String content = "TESTてすと" + new Date().toString();
        contentsApi.saveFile("db/test.txt", content);

    }
}
