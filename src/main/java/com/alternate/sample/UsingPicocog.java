package com.alternate.sample;

import com.grydtech.msstack.modelconverter.business.BusinessModel;
import com.grydtech.msstack.modelconverter.engine.ModelReaderJackson;
import com.grydtech.msstack.modelconverter.microservice.ClassSchema;
import com.grydtech.msstack.modelconverter.microservice.MicroServiceModel;
import com.grydtech.msstack.modelconverter.engine.ModelConverterEntityBased;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsingPicocog {
    public static void main(String[] args) throws URISyntaxException, IOException, TemplateException {
        File jsonFile = new File(UsingPicocog.class.getResource("/sample.json").toURI());
        BusinessModel businessModel = new ModelReaderJackson().readBusinessModel(jsonFile);
        List<MicroServiceModel> microServiceModels = new ModelConverterEntityBased().convertToMicroServiceModel(businessModel);
        ClassSchema classSchema = microServiceModels.get(1).getEntityClasses().get(0);

        Map<String, Object> root = new HashMap<>();
        root.put("packageName", "com.alternate.sample");
        root.put("className", classSchema.getName());
        root.put("attributes", classSchema.getAttributes());

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setDirectoryForTemplateLoading(new File(UsingPicocog.class.getResource("/").toURI()));
        Template template = cfg.getTemplate("class-template.ftl");
        Writer fileWriter = new FileWriter(new File("/home/randil/Documents/Projects/Extra/GeneratedProject/" + classSchema.getName() + ".java"));

        template.process(root, fileWriter);
    }
}
