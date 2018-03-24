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

import com.alternate.sample.helpers.ProjectWriter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class UsingFreemaker {
    public static void main(String[] args) throws URISyntaxException, IOException, TemplateException {
        String pathToStore = "/home/randil/Documents/Projects/Extra/generated/using-freemaker";
        String groupId = "com.grydtech";
        String version = "0.1.0";

        File jsonFile = new File(UsingFreemaker.class.getResource("/sample.json").toURI());
        BusinessModel businessModel = new ModelReaderJackson().readBusinessModel(jsonFile);
        List<MicroServiceModel> microServiceModels = new ModelConverterEntityBased().convertToMicroServiceModel(businessModel);

        for (MicroServiceModel microServiceModel: microServiceModels) {
            String artifactId = microServiceModel.getServiceName();

            String projectPath = pathToStore +
                    File.separator + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, artifactId);
            String basePackage = groupId + "." + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, artifactId).toLowerCase();

            ProjectWriter projectWriter = new ProjectWriter(projectPath);

            projectWriter.writeProjectPom(groupId, artifactId, version);

            for (EntityClassSchema entityClass: microServiceModel.getEntityClasses()) {
                projectWriter.writeEntityClass(basePackage, entityClass);
            }

            for (EventClassSchema eventClass: microServiceModel.getEventClasses()) {
                projectWriter.writeEventClass(basePackage, eventClass);
            }

            for (HandlerClassSchema handlerClass: microServiceModel.getHandlers()) {
                projectWriter.writeHandlerClass(basePackage, handlerClass);
            }
        }
    }
}
