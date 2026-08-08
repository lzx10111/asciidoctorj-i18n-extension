package org.asciidoctor.maven;

import java.util.Map;

public final class I18nUtils {
    public static String replaceForRole(String target, Map<String, Object> attributes) {
        String s = String.valueOf(attributes.get("key"));

        return target + replace(s, "_");
    }

    public static String replaceForJsonNode(String target, Map<String, Object> attributes) {
        String s = String.valueOf(attributes.get("key"));

        return "/" + target + replace(s, "/");
    }

    public static String replace(String key, String delimeter) {
        String result = key;

        if (result.contains(".")) {
            result = result.replace(".", delimeter);
        }
        
        return delimeter + result;
    }
}
