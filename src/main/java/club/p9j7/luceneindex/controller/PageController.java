package club.p9j7.luceneindex.controller;

import club.p9j7.luceneindex.entity.Record;
import club.p9j7.luceneindex.entity.Result;
import club.p9j7.luceneindex.service.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Controller
public class PageController {
    @Resource
    private PageService pageService;
    /**
     *
     * @return  主页视图
     */
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    /**
     * 初始化索引数据
     * @return  成功或者失败的消息
     */
    @ResponseBody
    @RequestMapping("/indexFiles")
    public Result indexFiles() {
        return pageService.indexFiles();
    }
    /**
     * 响应搜索请求
     * @return  list集合的数据
     */
    @ResponseBody
    @RequestMapping(value = "/searchFiles", method = RequestMethod.POST)
    public List<Record> searchFiles(@RequestParam String keyword) throws IOException {
        return pageService.searchFiles(keyword);
    }
}
