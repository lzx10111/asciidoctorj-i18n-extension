package org.asciidoctor.maven;

import java.util.Map;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.Preprocessor;
import org.asciidoctor.extension.PreprocessorReader;

public class I18nPreprocessor extends Preprocessor {

    @Override
    public void process(Document document, PreprocessorReader reader) {
        Map<String, Object> attributes = document.getAttributes();

        if (!attributes.containsKey(I18n.ATTRIBUTE_DEFAULT_LANG)) {
            attributes.put(I18n.ATTRIBUTE_DEFAULT_LANG, I18n.DEFAULT_LANG);
        }

        if (!attributes.containsKey(I18n.ATTRIBUTE_PREFIX)) {
            attributes.put(I18n.ATTRIBUTE_PREFIX, I18n.DEFAULT_PREFIX);
        }

        if (!attributes.containsKey(I18n.ATTRIBUTE_KEY_NAME)) {
            attributes.put(I18n.ATTRIBUTE_KEY_NAME, I18n.DEFAULT_KEY_NAME);
        }
        
        String keyPrefix = new StringBuilder()
            .append(String.valueOf(attributes.get(I18n.ATTRIBUTE_PREFIX)))
            .append("-")
            .append(String.valueOf(attributes.get(I18n.ATTRIBUTE_KEY_NAME)))
            .append("_")
            .toString();

        attributes.put(I18n.ATTRIBUTE_KEY_PREFIX, keyPrefix);

        String i18n = new StringBuilder()
            .append(".")
            .append(I18n.NAME_POM)
            .append(".")
            .append(keyPrefix)
            .toString();

        attributes.put(I18n.ATTRIBUTE_I18N, i18n);
    }

}
