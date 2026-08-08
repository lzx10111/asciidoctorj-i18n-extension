package org.asciidoctor.maven;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.ast.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class FileLoader {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private FileLoader() {
    }

    public static String getScriptNoInjectedJson() {
        return readJavaScriptFile("/scripts/i18nNoInjectedJson.js");
    }

    public static String getScriptInjectedJson() {
        return readJavaScriptFile("/scripts/i18nInjectedJson.js");
    }

    public static String getScriptNoInjectedJsonGenerateJson() {
        return readJavaScriptFile("/scripts/i18nNoInjectedJsonGenerateJson.js");
    }

    public static String getScriptInjectedJsonGenerateJson() {
        return readJavaScriptFile("/scripts/i18nInjectedJsonGenerateJson.js");
    }

    public static Map<String, JsonNode> getTranslations(Document document) {
        String docdir = (String) document.getAttribute(I18n.ATTRIBUTE_DOCDIR);
        
        return getTranslations(docdir);
    }

    public static Map<String, JsonNode> getTranslations(ContentNode contentNode) {
        String docdir = (String) contentNode.getDocument().getAttribute(I18n.ATTRIBUTE_DOCDIR);
        
        return getTranslations(docdir);
    }

    public static Map<String, JsonNode> getTranslations(String pathName) {
        Path path = Paths.get(pathName, "locales");
        File file = path.toFile();
        File[] array = file.listFiles();
        Map<String, JsonNode> fMap = new HashMap<>();

        for (File f : array) {
            if (f.isDirectory() && isLocaleValid(f.getName())) {
                File[] files = f.listFiles();

                if (files != null && files.length > 0) {
                    if (files[0].isFile() && files[0].getName().endsWith("json")) {
                        fMap.put(f.getName(), readJsonFile(files[0]));
                    }
                }
            }
        }

        return fMap;
    }

    public static List<String> getKeys(Document document) {
        String docdir = (String) document.getAttribute(I18n.ATTRIBUTE_DOCDIR);

        return getKeys(docdir);
    }

    public static List<String> getKeys(ContentNode contentNode) {
        String docdir = (String) contentNode.getDocument().getAttribute(I18n.ATTRIBUTE_DOCDIR);

        return getKeys(docdir);
    }

    public static List<String> getKeys(String pathName) {
        Path path = Paths.get(pathName, "locales");
        File file = path.toFile();
        File[] array = file.listFiles();
        List<String> list = new ArrayList<>();

        for (File f : array) {
            if (f.isDirectory() && isLocaleValid(f.getName())) {
                File[] files = f.listFiles();

                if (files != null && files.length > 0) {
                    if (files[0].isFile() && files[0].getName().endsWith("json")) {
                        list.add(f.getName());
                    }
                }
            }
        }

        return list;
    }

    public static Map<String, String> getFileNames(Document document) {
        String docdir = (String) document.getAttribute(I18n.ATTRIBUTE_DOCDIR);

        return getFileNames(docdir);
    }

    public static Map<String, String> getFileNames(ContentNode contentNode) {
        String docdir = (String) contentNode.getDocument().getAttribute(I18n.ATTRIBUTE_DOCDIR);

        return getFileNames(docdir);
    }

    public static Map<String, String> getFileNames(String pathName) {
        Path path = Paths.get(pathName, "locales");
        File file = path.toFile();
        File[] array = file.listFiles();
        Map<String, String> fMap = new HashMap<>();

        for (File f : array) {
            if (f.isDirectory() && isLocaleValid(f.getName())) {
                File[] files = f.listFiles();

                if (files != null && files.length > 0) {
                    if (files[0].isFile() && files[0].getName().endsWith("json")) {
                        fMap.put(f.getName(), files[0].getName());
                    }
                }
            }
        }

        return fMap;
    }

    public static boolean isLocaleValid(String lang) {
        Locale locale = new Locale(lang);

        return Arrays.asList(Locale.getAvailableLocales()).contains(locale);
    }

    public static JsonNode readJsonFile(File file) {
        JsonNode jsonNode = null;

        try {
            jsonNode = objectMapper.readTree(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonNode;
    }

    public static String readJavaScriptFile(String path) {
        InputStream inputStream = FileLoader.class.getResourceAsStream(path);
        String file = null;

        if (inputStream == null) {
            return file;
        }

        try {
            file = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
