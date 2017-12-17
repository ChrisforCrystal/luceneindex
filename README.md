# luceneindex全文检索工具

- 基于[lucene7.1.0](https://lucene.apache.org/ "lucene7.1.0")+[springboot1.5.9](https://projects.spring.io/spring-boot/ "springboot1.5.9")
- [maven](https://maven.apache.org/ "maven")管理项目依赖，[jackson json](https://github.com/FasterXML/jackson "jackson json")作为前后端交互工具
- 使用[bootstrap](http://www.bootcss.com/ "bootstrap")和[bootstrap table](http://bootstrap-table.wenzhixin.net.cn/zh-cn/ "bootstrap table")插件构建前端
- 实现了基本的按行建立索引和基础的`TermQuery`
- 代码有详细的注释，可以作为新手入门**springboot**和**lucene**的实例

# 运行界面
![image](https://github.com/p9j7/luceneindex/raw/master/images/luceneindex1.png)
![image](https://github.com/p9j7/luceneindex/raw/master/images/luceneindex2.png)

# How to start
1.下载项目到本地`git clone https://github.com/P9J7/luceneindex`  
2.使用maven构建命令`mvn clean package -Dmaven.test.skip=true`  
3.进入target文件夹`cd target`  
4.使用java命令运行`java -jar luceneindex.jar`  
5.访问[http://localhost:8080/](http://localhost:8080/ "http://localhost:8080/")即可看到项目运行


------------

本项目同时有[war](https://github.com/P9J7/luceneindex/tree/war "war")分支，可以部署在外部tomcat中，代码稍有不同
