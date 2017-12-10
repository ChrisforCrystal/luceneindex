package club.p9j7.luceneindex.service;


import club.p9j7.luceneindex.entity.Record;
import club.p9j7.luceneindex.entity.Result;

import java.io.IOException;
import java.util.List;

/**
 * 系统服务接口
 */
public interface PageService {
    /**
     * 文件索引方法
     * @return  建立成功与否的消息
     */
    Result indexFiles();

    /**
     * 索引查询方法
     * @param keyword
     * @return  查询到的记录集合
     * @throws IOException
     */
    List<Record> searchFiles(String keyword) throws IOException;
}
