import com.github.dapeng.plugins.DbGeneratePlugin

name := "easy-demo"

resolvers += Resolver.mavenLocal

lazy val commonSettings = Seq(
  organization := "com.today",
  version := "2.1.1",
  scalaVersion := "2.12.4"
)

javacOptions ++= Seq("-encoding", "UTF-8")

lazy val api = (project in file("demo-api"))
  .settings(
    commonSettings,
    name := "demo-api",
    libraryDependencies ++= Seq(
      "com.github.dapeng-soa" % "dapeng-client-netty" % "2.1.1-SNAPSHOT"
    )
  ).enablePlugins(ThriftGeneratorPlugin)


lazy val service = (project in file("demo-service"))
  .dependsOn(api)
  .settings(
    commonSettings,
    name := "demo_service",
    libraryDependencies ++= Seq(
      "com.github.dapeng-soa" % "dapeng-spring" % "2.1.1-SNAPSHOT",
      "com.github.wangzaixiang" %% "scala-sql" % "2.0.6",
      "org.slf4j" % "slf4j-api" % "1.7.13",
      "org.slf4j" % "jcl-over-slf4j" % "1.7.25", // transfer common logging to slf4j ?
      "ch.qos.logback" % "logback-classic" % "1.1.3",
      "ch.qos.logback" % "logback-core" % "1.1.3",
      "org.codehaus.janino" % "janino" % "2.7.8", //logback (use if condition in logBack config file need this dependency)
      "mysql" % "mysql-connector-java" % "5.1.36",
      "com.alibaba" % "druid" % "1.0.17",
      "org.springframework" % "spring-context" % "4.3.5.RELEASE",
      "org.springframework" % "spring-tx" % "4.3.5.RELEASE",
      "org.springframework" % "spring-jdbc" % "4.3.5.RELEASE",
      "com.github.dapeng-soa" % "dapeng-utils" % "2.1.1-SNAPSHOT" excludeAll ExclusionRule().withOrganization("com.github.dapeng-soa").withName("dapeng-core"),
//      "com.today" %% "event-bus" % "2.1.1",
      "com.today" %% "event-bus" % "3.0.0",
      "com.today" %% "idgen-api" % "2.0.5",
      "com.today" %% "service-commons" % "1.5-SNAPSHOT",
      "junit" % "junit" % "4.12" % Test
    )).enablePlugins(ImageGeneratorPlugin)
  .enablePlugins(DbGeneratePlugin)
  .enablePlugins(RunContainerPlugin)
