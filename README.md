# Java Code Generation
Analyzed main two ways to generate java code programmatically.

## Java Code Based
Generate java code via execution of some logic.  
[Java Poet](https://github.com/square/javapoet) library used to generate code.
```xml
<dependency>
    <groupId>com.squareup</groupId>
    <artifactId>javapoet</artifactId>
    <version>1.10.0</version>
</dependency>
```

## Template Based
Generate java code by combining template and data model  
[Apache Freemaker](https://freemarker.apache.org/) library used to generate code.
```xml
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.27-incubating</version>
</dependency>
```

## Issues when generating code
Multiple case formats use in java programming (eg: Camel case, Headless camel case)  
Java class names use camel case while method and attributes use headless camel case.  
[Google guava](https://github.com/google/guava) provide CaseFormat class which can be used to 
convert strings between case formats.
```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>24.1-jre</version>
</dependency>
```

