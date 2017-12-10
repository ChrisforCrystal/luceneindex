package club.p9j7.luceneindex;

import club.p9j7.luceneindex.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuceneindexApplication.class)
public class QueryTest {
    @Autowired
    PageService pageService;
    @Test
    public void testPageService() throws IOException {
        pageService.searchFiles("li");
        System.out.println(System.getProperty("user.home"));
//        pageService.indexFiles();

//        try {
//            Document document = pageService.searchFiles("chen");
//            System.out.println(document.get("path"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
