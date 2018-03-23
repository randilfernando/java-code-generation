package com.alternate.sample;

import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.JavaFile;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsingJavaPoet {
    public static void main(String[] args) throws IOException {
        FieldSpec names = FieldSpec.builder(ParameterizedTypeName.get(List.class, String.class), "names", Modifier.PRIVATE)
                .build();
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addStatement("this.names = new $T<$T>", ArrayList.class, String.class)
                .build();
        MethodSpec getNames = MethodSpec.methodBuilder("getNames")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(List.class, String.class))
                .addStatement("return this.names")
                .build();
        TypeSpec sampleClass = TypeSpec.classBuilder("SampleWithJavaPoet")
                .addModifiers(Modifier.PUBLIC)
                .addField(names)
                .addMethod(constructor)
                .addMethod(getNames)
                .build();

        JavaFile javaFile = JavaFile.builder("com.alternate.sample", sampleClass)
                .build();

        File file = new File("/home/randil/Documents/Projects/Extra/GeneratedProject");
        file.createNewFile();
        javaFile.writeTo(file);
    }
}
