package org.asciidoctor.maven;

import java.util.Map;

import org.asciidoctor.ast.ContentNode;

import com.fasterxml.jackson.databind.JsonNode;

public final class FileReader {

    private FileReader() {
    }

    public static final String getValue(ContentNode contentNode, String target, Map<String, Object> attributes) {
        String defaultLang = (String) contentNode.getDocument().getAttribute(I18n.ATTRIBUTE_DEFAULT_LANG);
        JsonNode jsonNode = FileLoader.getTranslations(contentNode).get(defaultLang);
        String key = I18nUtils.replaceForJsonNode(target, attributes);
        String text = null;

        if (!jsonNode.at(key).isMissingNode()) {
            text = jsonNode.at(key).asText();
        }

        return text;
    }
}
