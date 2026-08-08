package org.asciidoctor.maven;

public final class I18n {
    public static final String NAME_POM = "i18n";
    public static final String DEFAULT_PREFIX = "i18n";
    public static final String DEFAULT_KEY_NAME = "key";
    public static final String DEFAULT_LANG = "en";
    public static final String ATTRIBUTE_DOCDIR = "docdir";
    public static final String ATTRIBUTE_I18N = "i18n";
    public static final String ATTRIBUTE_DEFAULT_LANG = NAME_POM + "-" + "defaultlang";
    public static final String ATTRIBUTE_PREFIX = NAME_POM + "-" + "prefix";
    public static final String ATTRIBUTE_KEY_NAME = NAME_POM + "-" + "key-name";
    public static final String ATTRIBUTE_KEY_PREFIX = NAME_POM + "-" + "key-prefix";
    public static final String ATTRIBUTE_INJECTED_JSON = NAME_POM + "-" + "injectedjson";
    public static final String ATTRIBUTE_GENERATE_JSON = NAME_POM + "-" + "generatejson";
    public static final String HTML_INJECTED_JSON = CodeLine.builder()
                .tab(0).code("<script type=\"application/json\" id=\"i18n-lang-json-{0}\">").newLine()
                .tab(0).code("{1}").newLine()
                .tab(0).code("</script>").newLine(0)
                .build().getCode();

    public static final String HTML_SCRIPT = CodeLine.builder()
                .tab(0).code("<script type=\"module\">").newLine()
                .tab(0).code("{0}").newLine()
                .tab(0).code("</script>").newLine(0)
                .build().getCode();

    public static final String HTML_NAVBAR_INNERHTML = CodeLine.builder()
                .tab(0).code("navbar.innerHTML =").newLine()
                .tab(2).code("`{0}`;").newLine(0)
                .build().getCode();

    public static final String HTML_SWITCHER_SELECT = CodeLine.builder()
                .tab(0).code("<select class=\"{0}locales\" id=\"lang-switcher\">").newLine()
                .tab(0).code("{1}").newLine(0)
                .tab(2).code("</select>").newLine()
                .build().getCode();

    public static final String HTML_SWITCHER_OPTION = CodeLine.builder()
                .tab(3).code("<option value=\"{0}\">{1}</option>").newLine()
                .build().getCode();

    public static final String SCRIPT_IMPORT = "import {0}Translations from \"./locales/{0}/{1}\" with '{' type: \"json\" '}';\r\n";
    public static final String SCRIPT_ATTRIBUTE_VALUES = CodeLine.builder()
                .tab(0).code("const i18nDefaultLang = \"{0}\";").newLine()
                .tab(0).code("const i18nPrefix = \"{1}\";").newLine()
                .tab(0).code("const i18nKeyPrefix = \"{2}\";").newLine()
                .build().getCode();

    public static final String SCRIPT_FUNCTION_LOAD = CodeLine.builder()
                .tab(0).code("function loadTranslations(lang) '{'").newLine()
                .tab(1).code("switch (lang) '{'").newLine()
                .tab(0).code("{0}").newLine()
                .tab(0).code("{1}").newLine(0)
                .tab(1).code("'}'").newLine()
                .tab(0).code("'}'").newLine(0)
                .build().getCode();
                
    public static final String SCRIPT_SWITCH_CASE = CodeLine.builder()
                .tab(2).code("case \"{0}\":").newLine()
                .tab(3).code("return {0}Translations;").newLine()
                .build().getCode();
                
    public static final String SCRIPT_SWITCH_DEFAULT = CodeLine.builder()
                .tab(2).code("default:").newLine()
                .tab(3).code("return {0}Translations;").newLine()
                .build().getCode();
    
    private I18n() {
    }
}
