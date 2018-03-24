package com.alternate.sample.helpers;

import com.google.common.base.CaseFormat;
import com.grydtech.msstack.modelconverter.microservice.EntityClassSchema;
import com.grydtech.msstack.modelconverter.microservice.EventClassSchema;
import com.grydtech.msstack.modelconverter.microservice.HandlerClassSchema;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaClassWriter {
    private final String outputPath;
    private final Configuration cfg;

    public JavaClassWriter(String outputPath) {
        this.outputPath = outputPath;
        this.cfg = new Configuration(Configuration.VERSION_2_3_27);
        this.initConfigurations();
    }

    private void initConfigurations() {
        try {
            cfg.setDirectoryForTemplateLoading(new File(JavaClassWriter.class.getResource("/").toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void writeEntityClass(String basePackageName, EntityClassSchema entityClass) throws IOException, TemplateException {
        String packageName = basePackageName + ".entities";

        List<String> importPackages = new ArrayList<>();

        importPackages.add("import java.util.List");
        importPackages.add(basePackageName + ".events.*");

        Map<String, Object> root = new HashMap<>();
        root.put("packageName", packageName);
        root.put("importPackages", importPackages);
        root.put("className", entityClass.getName());
        root.put("attributes", entityClass.getAttributes());
        root.put("events", entityClass.getEvents());
        root.put("toCamel", new ToCamelCase());
        root.put("toHeadlessCamel", new ToHeadlessCamelCase());

        Template template = cfg.getTemplate("entity-class-template.ftl");
        String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, entityClass.getName());
        writeClass(packageName, className, template, root);
    }

    public void writeEventClass(String basePackageName, EventClassSchema eventClass) throws IOException, TemplateException {
        String packageName = basePackageName + ".events";

        List<String> importPackages = new ArrayList<>();

        Map<String, Object> root = new HashMap<>();
        root.put("packageName", packageName);
        root.put("importPackages", importPackages);
        root.put("className", eventClass.getName());
        root.put("attributes", eventClass.getAttributes());
        root.put("toCamel", new ToCamelCase());
        root.put("toHeadlessCamel", new ToHeadlessCamelCase());

        Template template = cfg.getTemplate("event-class-template.ftl");
        String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, eventClass.getName());
        writeClass(packageName, className, template, root);
    }

    public void writeHandlerClass(String basePackageName, HandlerClassSchema handlerClass) throws IOException, TemplateException {
        String packageName = basePackageName + ".handlers";

        List<String> importPackages = new ArrayList<>();

        importPackages.add(basePackageName + ".events.*");

        Map<String, Object> root = new HashMap<>();
        root.put("packageName", packageName);
        root.put("importPackages", importPackages);
        root.put("className", handlerClass.getName());
        root.put("events", handlerClass.getEvents());
        root.put("toCamel", new ToCamelCase());
        root.put("toHeadlessCamel", new ToHeadlessCamelCase());

        Template template = cfg.getTemplate("handler-class-template.ftl");
        String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, handlerClass.getName());
        writeClass(packageName, className, template, root);
    }

    private void writeClass(String packageName, String className, Template template, Map<String, Object> root) throws IOException, TemplateException {
        String packagePath = outputPath + File.separator + packageName.replace(".", File.separator);

        File filePath = new File(packagePath);
        filePath.mkdirs();

        Writer fileWriter = new FileWriter(new File(packagePath + File.separator + className + ".java"));
        template.process(root, fileWriter);
    }
}
