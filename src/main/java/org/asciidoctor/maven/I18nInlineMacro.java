package org.asciidoctor.maven;

import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.extension.InlineMacroProcessor;
import org.asciidoctor.extension.Name;

@Name("i18n")
public class I18nInlineMacro extends InlineMacroProcessor {

    @Override
    public Object process(ContentNode arg0, String arg1, Map<String, Object> arg2) {
        String prefix = (String) arg0.getDocument().getAttribute(I18n.ATTRIBUTE_PREFIX);
        String keyPrefix = (String) arg0.getDocument().getAttribute(I18n.ATTRIBUTE_KEY_PREFIX);
        Map<String, Object> attributes = new HashMap<>();
        String text = FileReader.getValue(arg0, arg1, arg2);
        String role = new StringBuilder()
            .append(prefix)
            .append(" ")
            .append(keyPrefix)
            .append(I18nUtils.replaceForRole(arg1, arg2))
            .toString();

        attributes.put("role", role);
        
        return createPhraseNode(arg0, "quoted", text, attributes);
    }
}
