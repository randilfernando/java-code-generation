package com.alternate.sample;

import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsingJavaPoet {
    public static void main(String[] args) throws IOException {
        FieldSpec namesField = FieldSpec.builder(ParameterizedTypeName.get(List.class, String.class), "names", Modifier.PRIVATE)
                .build();

        MethodSpec sampleClassConstructor = MethodSpec.constructorBuilder()
                .addStatement("this.names = new $T<$T>", ArrayList.class, String.class)
                .build();

        MethodSpec getNamesMethod = MethodSpec.methodBuilder("getNames")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(List.class, String.class))
                .addStatement("return this.names")
                .build();

        TypeSpec sampleClass = TypeSpec.classBuilder("SampleWithJavaPoet")
                .addModifiers(Modifier.PUBLIC)
                .addField(namesField)
                .addMethod(sampleClassConstructor)
                .addMethod(getNamesMethod)
                .build();

        JavaFile sampleClassFile = JavaFile.builder("com.alternate.sample.package1", sampleClass)
                .build();

        ClassName listClass = ClassName.get("java.util", "List");
        ClassName className = ClassName.get("com.alternate.sample.package1", "SampleWithJavaPoet");

        FieldSpec sampleClassField = FieldSpec.builder(ParameterizedTypeName.get(listClass, className), "sampleClass", Modifier.PRIVATE)
                .build();

        TypeSpec anotherClass = TypeSpec.classBuilder("AnotherClassWithJavaPoet")
                .addModifiers(Modifier.PUBLIC)
                .addField(sampleClassField)
                .build();

        JavaFile anotherClassFile = JavaFile.builder("com.alternate.sample.package2", anotherClass)
                .build();

        File file = new File("/home/randil/Documents/Projects/Extra/generated/using-java-poet");
        sampleClassFile.writeTo(file);
        anotherClassFile.writeTo(file);
    }
}
