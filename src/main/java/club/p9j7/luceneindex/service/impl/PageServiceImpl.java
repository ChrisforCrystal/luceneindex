package club.p9j7.luceneindex.service.impl;

import club.p9j7.luceneindex.entity.Record;
import club.p9j7.luceneindex.entity.Result;
import club.p9j7.luceneindex.service.PageService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PageServiceImpl implements PageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //源文件存放位置为用户主目录下的 lucenedir 文件夹
    private final String docsPath = System.getProperty("user.home") + "/lucenedir";
    //索引文件存放位置用户主目录下的 luceneidx 文件夹
    private final String indexPath = System.getProperty("user.home") + "/luceneidx";

    @Override
    public Result indexFiles() {
        final Path docDir = Paths.get(docsPath);
        //如果路径错误退出程序并且打印日志
        if (!Files.isReadable(docDir)) {
            logger.error(docDir.toString() + "路径错误!");
            System.exit(1);
        }
        //记录程序处理的开始时间
        Date start = new Date();
        try {
            logger.info("程序开始索引文件夹:" + docsPath);
            //自动根据平台选择文件系统的实现类
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            //使用标准的分词器,使用空格分词
            Analyzer analyzer = new StandardAnalyzer();
            //配置索引创建类
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            IndexWriter indexWriter = new IndexWriter(dir, indexWriterConfig);
            indexDocs(indexWriter, docDir);
            //索引完成,关闭索引创建类,并写入索引时间
            indexWriter.close();
            Long indextime = new Date().getTime() - start.getTime();
            logger.info("本次索引创建所用时间:" + indextime + "毫秒");
        } catch (IOException e) {
            logger.error("错误" + e.getClass() +
                    "\n详细信息" + e.getMessage());
            //出现错误,返回失败的 result 信息
            return new Result("索引建立失败!请联系软件维护人员!");
        }
        //如果索引建立完成,则返回成功的 result 信息
        return new Result("索引建立成功！当前搜索可用！");
    }

    //文件夹遍历类
    private void indexDocs(final IndexWriter writer, Path path) throws IOException {
        if (Files.isDirectory(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        indexDoc(writer, file);
                    } catch (IOException ignore) {
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            indexDoc(writer, path);
        }
    }

    //索引的具体建造方法
    private void indexDoc(IndexWriter writer, Path file) throws IOException {
        try (BufferedReader bufferedReader = Files.newBufferedReader(file)) {
            logger.info("开始为 {} 建立索引",file);
            //每行的内容
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                //建立一条索引记录
                Document document = new Document();
                //将路径设置为索引记录中的 path 字段
                Field pathField = new StringField("path", file.toString(), Field.Store.YES);
                //将一行封装为一条记录
                Field recordField = new TextField("content", content, Field.Store.YES);
                //提交索引相关字段
                document.add(recordField);
                document.add(pathField);
                //正式创建一条索引
                writer.addDocument(document);
            }

            if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                logger.info("索引创建完毕 文件名: {} 覆写模式",file);
            } else {
                logger.info("索引创建完毕 追加模式");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    //搜索执行类
    @Override
    public List<Record> searchFiles(String keyword) {
        //定义索引读取器
        logger.info("检索值: " + keyword);
        IndexReader indexReader = null;
        try {
            indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        } catch (IOException e) {
            logger.error("索引文件路径发生错误!");
            e.printStackTrace();
        }
        //定义索引搜索器
        IndexSearcher searcher = new IndexSearcher(indexReader);
        //转为小写否则出错
        keyword = keyword.toLowerCase();
        Term aTerm = new Term("content", keyword);
        Query query = new TermQuery(aTerm);
        TopDocs topDocs;
        ScoreDoc[] scoreDocs = null;
        try {
            //定义缓存1000条记录
            topDocs = searcher.search(query, 1000);
            logger.info("符合条件的记录总数为：" + topDocs.totalHits);
            //缓存的1000条记录
            scoreDocs = topDocs.scoreDocs;
        } catch (IOException e1) {
            logger.error("字段搜索过程中出错!");
            e1.printStackTrace();
        }
        //将查询到的前1000条记录加入 list<Recoed>
        List<Record> recordList = new ArrayList<>(scoreDocs.length);
        for(int i=0;i<scoreDocs.length;i++) {
            //根据id查询防止返回重复数据
            Document doc = null;
            try {
                doc = searcher.doc(scoreDocs[i].doc);
            } catch (IOException e) {
                logger.error("字段遍历过程中出错!");
                e.printStackTrace();
            }
            Record record = new Record(i,doc.get("path"),doc.get("content"));
            recordList.add(record);
        }
        logger.info("recordList创建完成！实际大小为：" + recordList.size());
        return recordList;
//        ObjectMapper objectMapper = new ObjectMapper();
        //将生成的json文件保存到log文件夹下
        //todo 如何访问呢？
//        File output = new File("/var/tmp/data.json");
//        File output = new File("data.json");
//        try {
            //必须输出为标准json格式，否则bootstrap-table无法识别
//            objectMapper.writerWithDefaultPrettyPrinter().writeValue(output, recordList);
//            logger.info("json文件生成在 {} 目录下",output);
//        } catch (IOException e) {
//            logger.error("字段输出过程中出错!");
//            Result failureResult = new Result("程序输出错误！请联系软件维护人员！");
//            return failureResult;
//            e.printStackTrace();
//        }

//        if (recordList.size() == 0) {
//            Result successResult = new Result("未查询到符合的结果!");
//            return successResult;
//        }
//        Result successResult = new Result("查询成功！结果集合如下表");
//        return successResult;
    }
}


