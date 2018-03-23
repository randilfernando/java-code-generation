package com.alternate.sample.helpers;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import com.google.common.base.CaseFormat;

import java.util.List;

public class ToHeadlessCamelCase implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 1) {
            throw new TemplateModelException("Wrong arguments");
        }
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, String.valueOf(list.get(0)));
    }
}
