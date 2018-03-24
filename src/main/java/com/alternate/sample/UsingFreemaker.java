package com.alternate.sample;

import com.google.common.base.CaseFormat;
import com.grydtech.msstack.modelconverter.business.BusinessModel;
import com.grydtech.msstack.modelconverter.engine.ModelConverterEntityBased;
import com.grydtech.msstack.modelconverter.engine.ModelReaderJackson;
import com.grydtech.msstack.modelconverter.microservice.MicroServiceModel;
import com.grydtech.msstack.modelconverter.microservice.EntityClassSchema;
import com.grydtech.msstack.modelconverter.microservice.EventClassSchema;
import com.grydtech.msstack.modelconverter.microservice.HandlerClassSchema;

import freemarker.template.TemplateException;

import com.alternate.sample.helpers.JavaClassWriter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class UsingFreemaker {
    public static void main(String[] args) throws URISyntaxException, IOException, TemplateException {
        String pathToStore = "~/Documents/Projects/Extra/generated-using-freemaker";
        String basePackage = "com.grydtech.sample";

        File jsonFile = new File(UsingFreemaker.class.getResource("/sample.json").toURI());
        BusinessModel businessModel = new ModelReaderJackson().readBusinessModel(jsonFile);
        List<MicroServiceModel> microServiceModels = new ModelConverterEntityBased().convertToMicroServiceModel(businessModel);

        for (MicroServiceModel microServiceModel: microServiceModels) {
            String outputPath = pathToStore +
                    File.separator + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, microServiceModel.getServiceName()) +
                    File.separator + "src";
            JavaClassWriter javaClassWriter = new JavaClassWriter(outputPath);
            for (EntityClassSchema entityClass: microServiceModel.getEntityClasses()) {
                javaClassWriter.writeEntityClass(basePackage, entityClass);
            }
            for (EventClassSchema eventClass: microServiceModel.getEventClasses()) {
                javaClassWriter.writeEventClass(basePackage, eventClass);
            }
            for (HandlerClassSchema handlerClass: microServiceModel.getHandlers()) {
                javaClassWriter.writeHandlerClass(basePackage, handlerClass);
            }
        }
    }
}
