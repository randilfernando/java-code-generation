package com.alternate.sample;

import com.grydtech.msstack.modelconverter.business.BusinessModel;
import com.grydtech.msstack.modelconverter.engine.ModelReaderJackson;
import com.grydtech.msstack.modelconverter.microservice.ClassSchema;
import com.grydtech.msstack.modelconverter.microservice.MicroServiceModel;
import com.grydtech.msstack.modelconverter.engine.ModelConverterEntityBased;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import com.alternate.sample.helpers.*;

import com.google.common.base.CaseFormat;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsingFreemaker {
    public static void main(String[] args) throws URISyntaxException, IOException, TemplateException {
        File jsonFile = new File(UsingFreemaker.class.getResource("/sample.json").toURI());
        BusinessModel businessModel = new ModelReaderJackson().readBusinessModel(jsonFile);
        List<MicroServiceModel> microServiceModels = new ModelConverterEntityBased().convertToMicroServiceModel(businessModel);
        ClassSchema classSchema = microServiceModels.get(1).getEntityClasses().get(0);

        Map<String, Object> root = new HashMap<>();
        root.put("packageName", "com.alternate.sample");
        root.put("className", classSchema.getName());
        root.put("attributes", classSchema.getAttributes());
        root.put("toCamel", new ToCamelCase());
        root.put("toHeadlessCamel", new ToHeadlessCamelCase());

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setDirectoryForTemplateLoading(new File(UsingFreemaker.class.getResource("/").toURI()));
        Template template = cfg.getTemplate("class-template.ftl");
        String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, classSchema.getName());
        Writer fileWriter = new FileWriter(new File("/home/randil/Documents/Projects/Extra/GeneratedProject/" + className + ".java"));

        template.process(root, fileWriter);
    }
}
