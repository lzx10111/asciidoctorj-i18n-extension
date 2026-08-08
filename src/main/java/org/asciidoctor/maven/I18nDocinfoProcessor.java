package org.asciidoctor.maven;

import java.util.Map;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.DocinfoProcessor;
import org.asciidoctor.extension.Location;
import org.asciidoctor.extension.LocationType;

@Location(LocationType.HEADER)
public class I18nDocinfoProcessor extends DocinfoProcessor {

    @Override
    public String process(Document document) {
        Map<String, Object> attributes = document.getAttributes();
        boolean injectedJson = attributes.containsKey(I18n.ATTRIBUTE_INJECTED_JSON);
        boolean generateJson = attributes.containsKey(I18n.ATTRIBUTE_GENERATE_JSON);

        return getStringHTML(document, injectedJson, generateJson);
    }

    public String getStringHTML(Document document, boolean injectedJson, boolean generateJson) {
        String script = null;
        String allLangJson = "";

        if (injectedJson && !generateJson) {
            script = FileWriter.getScriptInjectedJsonHTML(document);
            allLangJson = FileWriter.getAllLangJson(document);
        }
        else if (!injectedJson && !generateJson) {
            script = FileWriter.getScriptNoInjectedJsonHTML(document);
        }
        else if (injectedJson && generateJson) {
            script = FileWriter.getScriptInjectedJsonGenerateJsonHTML(document);
            allLangJson = FileWriter.getAllLangJson(document);
        }
        else if (!injectedJson && generateJson) {
            script = FileWriter.getScriptNoInjectedJsonGenerateJsonHTML(document);
        }
        
        String sb = new StringBuilder()
            .append(script)
            .append(allLangJson)
            .toString();

        return sb;
    }

}
