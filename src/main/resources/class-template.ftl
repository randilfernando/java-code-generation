package ${packageName};

public class ${toCamel(className)} {
<#list attributes as attribute>
    private ${toCamel(attribute.type)} ${toHeadlessCamel(attribute.name)};
</#list>
}