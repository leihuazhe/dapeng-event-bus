<h1 align="center">dapeng-soa示例项目</h1> 

> demo所有模块已升级至2.0.0

## 环境

- [JDK1.8+](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [scala SDK 2.12.2+](https://www.scala-lang.org/download/)
- [sbt](https://www.scala-sbt.org/download.html) 
- [maven](https://maven.apache.org/download.cgi)
    - sbt 镜像加速
    - 在你的用户文件夹下的 .sbt 文件夹里面新建 `repositories` 文件
    加入内容如下（注意最后不要有空格，`maven-local` 据你的修改）：
    
```
[repositories]
    local
    maven-local: file:/Users/struy/.m2/repository
    # not need config dns
    repox-maven: http://gateway.today36524.com:9001/repository/maven-public/
    # need config dns 10.10.10.6
    #repox-maven: http://nexus.today36524.com/repository/maven-public/
    #repox-maven: http://maven.aliyun.com/nexus/content/groups/public
    repox-ivy: http://hzways.com:8078/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]
```

- `service-commons` 使用说明
    - 必须的依赖，作为项目前置/后置检查的断言 `Assert`
    - 继承 `Action` 规范代码结构
    - 继承 `Query` 规范代码结构
    - 一些可能会用到的隐式转换 `implicit`
        - `CommonEx => in/notIn` 可用于权限判断或者状态判断
        - `OrderedExt => between` 是否在区间内
        - `SqlBooleanImplicit => optional` 根据 `Boolean` 值判断是否需要返回某段sql，不满足返回 `sql""`
        
## 运行
- `clone` [http://pms.today36524.com.cn:8083/central-services/demo](http://pms.today36524.com.cn:8083/central-services/demo)
- 在项目目录执行 `sbt compile`(针对windows，建议在idea中添加sbt task执行)
- 本地启动 `zookeeper` /或 `docker`(建议) 运行一个 `zookeeper` 容器
- 初始化`demo.sql` 数据库脚本(数据库名默认为demo_db)
- `sbt runContainer` 启动项目
- 建议IDEA 配置运行，请选择`sbt Task`,填入`runContainer` 为Tasks,再在`VM parameter`后追加` -Dfile.encoding=utf8`
- 本地文档站点测试 [http://localhost:8192/api/index.htm](http://localhost:8192/api/index.htm)

## 基础服务编写分层示例图
```
|--scala
    |--com 
        |--today   
        |   |-- commons                                       通用
        |   |   |-- QueryUtil.scala
        |   |   |-- UserUtil.scala
        |   |-- service
        |   |   |-- demo                          
        |   |   |   |-- action                                     动作
        |   |   |   |   |-- sql
        |   |   |   |   |   |-- UserActionSql.scala         具体数据操作/更新,插入
        |   |   |   |   |-- UserXxxxAction.scala           具体的动作/业务逻辑
        |   |   |   |   |-- ....
        |   |   |   |-- query                                      查询
        |   |   |   |   |-- sql
        |   |   |   |   |   |-- UserQuerySql.scala          独立的读取操作/查询
        |   |   |   |   |-- FindUserXxxxQuery.scala     具体查询动作
        |   |   |   |   |-- ....
        |   |   |   |-- UserServiceImpl.scala              服务 
        |   |   |   |-- MemberDataSource.scala        数据源 
-------------------------------------------------------
```
## 基础约束
 
  * 服务主流程尽量简单、清晰，没有复杂的分支流程
  * 一个方法体不能超过90行，每行长度不超过120列
  * 必须规范化代码后才能提交，包括format以及优化import
  * 避免使用 var 及 mutable collections(如果一定要用可变量,那么必须申请review)
  * 新的EmptyChecking，统一对逻辑Empty的检查。
  * 服务代码中避免 null 的使用，对基本类型，使用Option[T] 替代，对集合类型，使用 List.Empty 或者类似集合替代
  * Option[T] 可以直接传递给 scala-sql 包。
  * Enum 类型可以直接传递给 scala-sql 包。
  * 新的明确语义的 Bean Copy 函数，完成对象间的转换。明确，无歧义。
  * 链式调用分行处理。
## 更多规范参照wiki