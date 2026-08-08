package org.asciidoctor.maven;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.asciidoctor.ast.Document;

import com.fasterxml.jackson.databind.JsonNode;

public final class FileWriter {

    private FileWriter() {
    }

    public static String replaceImports(Document document, String script) {
        Map<String, String> nullFileNames = new HashMap<>();
        nullFileNames.put("null", "translation.json");

        String nullImports = getImports(nullFileNames);
        String newImports = getImports(document);

        return replace(nullImports, newImports, script);
    }

    public static String replaceImports(Map<String, String> fileNames, String script) {
        Map<String, String> nullFileNames = new HashMap<>();
        nullFileNames.put("null", "translation.json");

        String nullImports = getImports(nullFileNames);
        String newImports = getImports(fileNames);

        return replace(nullImports, newImports, script);
    }

    public static String replaceAttributeValues(Document document, String script) {
        Map<String, Object> nullAttributes = new HashMap<>();
        nullAttributes.put(I18n.ATTRIBUTE_DEFAULT_LANG, "null");
        nullAttributes.put(I18n.ATTRIBUTE_PREFIX, "null");
        nullAttributes.put(I18n.ATTRIBUTE_KEY_PREFIX, "null");

        String nullAttributeValues = getAttributeValues(nullAttributes);
        String newAttributeValues = getAttributeValues(document);

        return replace(nullAttributeValues, newAttributeValues, script);
    }

    public static String replaceAttributeValues(Map<String, Object> attributes, String script) {
        Map<String, Object> nullAttributes = new HashMap<>();
        nullAttributes.put(I18n.ATTRIBUTE_DEFAULT_LANG, "null");
        nullAttributes.put(I18n.ATTRIBUTE_PREFIX, "null");
        nullAttributes.put(I18n.ATTRIBUTE_KEY_PREFIX, "null");

        String nullAttributeValues = getAttributeValues(nullAttributes);
        String newAttributeValues = getAttributeValues(attributes);

        return replace(nullAttributeValues, newAttributeValues, script);
    }

    public static String replaceFunctionLoad(Document document, String script) {
        List<String> nullKeys = Arrays.asList("null");
        String nullFunctionLoad = getFunctionLoad(nullKeys, "null");
        String newFunctionLoad = getFunctionLoad(document);
 
        return replace(nullFunctionLoad, newFunctionLoad, script);
    }

    public static String replaceFunctionLoad(List<String> keys, String lang, String script) {
        List<String> nullKeys = Arrays.asList("null");
        String nullFunctionLoad = getFunctionLoad(nullKeys, "null");
        String newFunctionLoad = getFunctionLoad(keys, lang);
 
        return replace(nullFunctionLoad, newFunctionLoad, script);
    }

    public static String replaceNavbarInnerHTML(Document document, String script) {
        String nullSwitcherInnerHTML = MessageFormat.format(I18n.HTML_NAVBAR_INNERHTML, "null");
        String newSwitcherInnerHTML = getNavbarInnerHTML(document);

        return replace(nullSwitcherInnerHTML, newSwitcherInnerHTML, script);
    }

    public static String replaceNavbarInnerHTML(List<String> list, String lang, String prefix, String script) {
        String nullSwitcherInnerHTML = MessageFormat.format(I18n.HTML_NAVBAR_INNERHTML, "null");
        String newSwitcherInnerHTML = getNavbarInnerHTML(list, lang, prefix);

        return replace(nullSwitcherInnerHTML, newSwitcherInnerHTML, script);
    }

    public static String replace(String oldString, String newString, String script) {
        int start = script.indexOf(oldString);
        int end = start + oldString.length();

        StringBuilder sb = new StringBuilder(script);
        sb.replace(start, end, newString);

        return sb.toString();
    }

    public static String getScriptNoInjectedJsonHTML(Document document) {
        String script = getScriptNoInjectedJson(document);
        String htmlString = MessageFormat.format(
            I18n.HTML_SCRIPT,
            script);

        return htmlString;
    }

    public static String getScriptInjectedJsonHTML(Document document) {
        String script = getScriptInjectedJson(document);
        String htmlString = MessageFormat.format(
            I18n.HTML_SCRIPT,
            script);

        return htmlString;
    }

    public static String getScriptNoInjectedJsonGenerateJsonHTML(Document document) {
        String script = getScriptNoInjectedJsonGenerateJson(document);
        String htmlString = MessageFormat.format(
            I18n.HTML_SCRIPT,
            script);

        return htmlString;
    }

    public static String getScriptInjectedJsonGenerateJsonHTML(Document document) {
        String script = getScriptInjectedJsonGenerateJson(document);
        String htmlString = MessageFormat.format(
            I18n.HTML_SCRIPT,
            script);

        return htmlString;
    }

    public static String getScriptNoInjectedJson(Document document) {
        String script1 = FileLoader.getScriptNoInjectedJson();
        String script2 = replaceImports(document, script1);
        String script3 = replaceAttributeValues(document, script2);
        String script4 = replaceFunctionLoad(document, script3);
        String script5 = replaceNavbarInnerHTML(document, script4);
         
        script5 = script5.replaceAll("\r\n", "\n");

        return script5;
    }

    public static String getScriptInjectedJson(Document document) {
        String script1 = FileLoader.getScriptInjectedJson();
        String script2 = replaceAttributeValues(document, script1);
        String script3 = replaceNavbarInnerHTML(document, script2);
         
        script3 = script3.replaceAll("\r\n", "\n");

        return script3;
    }

    public static String getScriptNoInjectedJsonGenerateJson(Document document) {
        String script1 = FileLoader.getScriptNoInjectedJsonGenerateJson();
        String script2 = replaceImports(document, script1);
        String script3 = replaceAttributeValues(document, script2);
        String script4 = replaceFunctionLoad(document, script3);
        String script5 = replaceNavbarInnerHTML(document, script4);
         
        script5 = script5.replaceAll("\r\n", "\n");

        return script5;
    }

    public static String getScriptInjectedJsonGenerateJson(Document document) {
        String script1 = FileLoader.getScriptInjectedJsonGenerateJson();
        String script2 = replaceAttributeValues(document, script1);
        String script3 = replaceNavbarInnerHTML(document, script2);
         
        script3 = script3.replaceAll("\r\n", "\n");

        return script3;
    }

    public static String getImports(Document document) {
        Map<String, String> fileNames = FileLoader.getFileNames(document);

        return getImports(fileNames);
    }

    public static String getImports(Map<String, String> map) {
        String s = "";
        
        for (Map.Entry<String, String> e : map.entrySet()) {
            s = s + MessageFormat.format(I18n.SCRIPT_IMPORT, e.getKey(), e.getValue());
        }

        return s;
    }

    public static String getAttributeValues(Document document) {
        Map<String, Object> attributes = document.getAttributes();

        return getAttributeValues(attributes);
    }

    public static String getAttributeValues(Map<String, Object> attributes) {
        String s = "";
        
        s = MessageFormat.format(
                I18n.SCRIPT_ATTRIBUTE_VALUES,
                String.valueOf(attributes.get(I18n.ATTRIBUTE_DEFAULT_LANG)),
                String.valueOf(attributes.get(I18n.ATTRIBUTE_PREFIX)),
                String.valueOf(attributes.get(I18n.ATTRIBUTE_KEY_PREFIX)));

        return s;
    }

    public static String getFunctionLoad(Document document) {
        String defaultLang = (String) document.getAttribute(I18n.ATTRIBUTE_DEFAULT_LANG);
        List<String> keys = FileLoader.getKeys(document);

        return getFunctionLoad(keys, defaultLang);
    }
    
    
    public static String getFunctionLoad(List<String> keys, String lang) {
        String casesString = getSwitchCases(keys);
        String defaulString = getSwitchDefault(lang);
        String s = MessageFormat.format(
            I18n.SCRIPT_FUNCTION_LOAD, 
            casesString, 
            defaulString);

        return s;
    }

    public static String getSwitchCases(List<String> list) {
        String s = "";

        for (String k : list) {
            s = s + MessageFormat.format(
                        I18n.SCRIPT_SWITCH_CASE, 
                        k);
        }

        return s;
    }

    public static String getSwitchDefault(String lang) {
        String s = MessageFormat.format(
                    I18n.SCRIPT_SWITCH_DEFAULT, 
                    lang);

        return s;
    }

    public static String getAllLangJson(Document document) {
        Map<String, JsonNode> map = FileLoader.getTranslations(document);

        return getAllLangJson(map);
    }

    public static String getAllLangJson(Map<String, JsonNode> map) {
        String s = "";
        
        for (Map.Entry<String, JsonNode> e : map.entrySet()) {
            s = s + getLangJson(e.getKey(), e.getValue()) + "\n";
        }

        return s;
    }

    public static String getLangJson(String lang, JsonNode jsonNode) {
        String s = MessageFormat.format(
                    I18n.HTML_INJECTED_JSON, 
                    lang,
                    jsonNode.toPrettyString());

        s = s.replaceAll("\r\n", "\n");

        return s;
    }

    public static String getNavbarInnerHTML(Document document) {
        String i18nPrefix = (String) document.getAttribute(I18n.ATTRIBUTE_PREFIX);
        String keyPrefix = (String) document.getAttribute(I18n.ATTRIBUTE_KEY_PREFIX);
        String defaultLang = (String) document.getAttribute(I18n.ATTRIBUTE_DEFAULT_LANG);
        List<String> keys = FileLoader.getKeys(document);

        String sb = new StringBuilder()
            .append(i18nPrefix)
            .append(" ")
            .append(keyPrefix)
            .toString();

        return getNavbarInnerHTML(keys, defaultLang, sb);
    }

    public static String getNavbarInnerHTML(List<String> list, String lang, String prefix) {
        String options = getSwitcherSelect(list, lang, prefix);
        String s = MessageFormat.format(
            I18n.HTML_NAVBAR_INNERHTML,
            options);

        return s;
    }

    public static String getSwitcherSelect(List<String> list, String lang, String prefix) {
        String options = getSwitcherOptions(list, lang);
        String s = MessageFormat.format(
            I18n.HTML_SWITCHER_SELECT,
            prefix,
            options);

        return s;
    }

    public static String getSwitcherOptions(List<String> list, String lang) {
        Locale defaultLang = new Locale(lang);
        String s = "";

        for (String k : list) {
            Locale locale = new Locale(k);
            s = s + MessageFormat.format(
                        I18n.HTML_SWITCHER_OPTION, 
                        k,
                        locale.getDisplayLanguage(defaultLang));
        }

        return s;
    }
}
