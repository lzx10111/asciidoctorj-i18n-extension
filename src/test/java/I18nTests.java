import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.asciidoctor.maven.FileLoader;
import org.asciidoctor.maven.FileWriter;
import org.asciidoctor.maven.I18n;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class I18nTests {
    private static final Logger log = Logger.getLogger(I18nTests.class.getName());
    private static final String pathName = Paths.get(System.getProperty("user.dir"), "src", "main", "resources").toString();

    @Test
    void junitTest() throws Exception {

        assertEquals("test", "test");
    }

    @Test
    void getTranslationsTest() throws Exception {
        log.info(pathName);

        Map<String, JsonNode> fMap = FileLoader.getTranslations(pathName);
        log.info(fMap.get("en").toString());

        assertEquals(fMap.get("en").at("/courses/1/durationHours").asInt(), 80);
    }

    @Test
    void getKeysTest() throws Exception {

        assertEquals(FileLoader.getKeys(pathName).size(), 3);
    }

    @Test
    void getFileNamesTest() throws Exception {
        
        assertEquals(FileLoader.getFileNames(pathName).get("es"), "test-file-json.json");
    }

    @Test
    void replaceImportsTest() throws Exception {
        Map<String, String> fileNames = FileLoader.getFileNames(pathName);
        String script = FileLoader.getScriptNoInjectedJson();
        String imports = FileWriter.getImports(fileNames);
        String newScript = FileWriter.replaceImports(fileNames, script);

        log.info(newScript);
        assertEquals(newScript.contains(imports), true);
    }

    @Test
    void replaceAttributeValuesTest() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(I18n.ATTRIBUTE_DEFAULT_LANG, "en");
        attributes.put(I18n.ATTRIBUTE_PREFIX, "i18n");
        attributes.put(I18n.ATTRIBUTE_KEY_PREFIX, "key");

        String script = FileLoader.getScriptNoInjectedJson();
        String attributeValues = FileWriter.getAttributeValues(attributes);
        String newScript = FileWriter.replaceAttributeValues(attributes, script);

        log.info(newScript);
        assertEquals(newScript.contains(attributeValues), true);
    }

    @Test
    void replaceNavbarInnerHTMLTest() throws Exception {
        List<String> keys = FileLoader.getKeys(pathName);
        String lang = "en";
        String prefix = "i18n i18n-key_";
        String script = FileLoader.getScriptNoInjectedJson();
        String navbar = FileWriter.getNavbarInnerHTML(keys, lang, prefix);
        String newScript = FileWriter.replaceNavbarInnerHTML(keys, lang, prefix, script);

        log.info(newScript);
        assertEquals(newScript.contains(navbar), true);
    }

    @Test
    void replaceFunctionLoadTest() throws Exception {
        List<String> keys = FileLoader.getKeys(pathName);
        String lang = "en";
        String script = FileLoader.getScriptNoInjectedJson();
        String functionLoad = FileWriter.getFunctionLoad(keys, "en");
        String newScript = FileWriter.replaceFunctionLoad(keys, lang, script);

        log.info(newScript);
        assertEquals(newScript.contains(functionLoad), true);
    }

    @Test
    void replaceSwitcherInnerHTMLTest() throws Exception {
        List<String> keys = FileLoader.getKeys(pathName);
        String lang = "en";
        String prefix = "i18n i18n-key_";
        String script = FileLoader.getScriptNoInjectedJson();
        String switcherInnerHTML = FileWriter.getNavbarInnerHTML(keys, lang, prefix);
        String newScript = FileWriter.replaceNavbarInnerHTML(keys, lang, prefix, script);

        log.info(newScript);
        assertEquals(newScript.contains(switcherInnerHTML), true);
    }

    @Test
    void getImportsTest() throws Exception {
        Map<String, String> fileNames = FileLoader.getFileNames(pathName);
        String imports = FileWriter.getImports(fileNames);
        String es = MessageFormat
            .format(I18n.SCRIPT_IMPORT, "es", "test-file-json.json")
            .toString();

        log.info(imports);
        assertEquals(imports.contains(es), true);
    }

    @Test 
    void getAttributeValuesTest() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(I18n.ATTRIBUTE_DEFAULT_LANG, "en");
        attributes.put(I18n.ATTRIBUTE_PREFIX, "i18n");
        attributes.put(I18n.ATTRIBUTE_KEY_PREFIX, "key");

        String attributeValues = FileWriter.getAttributeValues(attributes);

        log.info(attributeValues);
        assertEquals(attributeValues.isEmpty(), false);
    }

    @Test
    void getFunctionLoadTest() throws Exception {
        String functionLoad = FileWriter.getFunctionLoad(FileLoader.getKeys(pathName), "es");

        log.info(functionLoad);
        assertEquals(functionLoad.isEmpty(), false);
    }

    @Test
    void getSwitchCasesTest() throws Exception {
        String casesString = FileWriter.getSwitchCases(FileLoader.getKeys(pathName));
        String de = MessageFormat
            .format(I18n.SCRIPT_SWITCH_CASE, "de")
            .toString();

        log.info(casesString);
        assertEquals(casesString.contains(de), true);
    }

    @Test
    void getSwitchDefaultTest() throws Exception {
        String defaultString = FileWriter.getSwitchDefault("es");
        String es = MessageFormat
            .format(I18n.SCRIPT_SWITCH_DEFAULT, "es")
            .toString();

        log.info(defaultString);
        assertEquals(defaultString.contains(es), true);
    }

    @Test
    void getAllLangJsonTest() throws Exception {
        String all = FileWriter.getAllLangJson(FileLoader.getTranslations(pathName));

        log.info(all);
        assertEquals(all.isEmpty(), false);
    }

    @Test
    void getLangJsonTest() throws Exception {
        String lang = "de";
        String langJson = FileWriter.getLangJson(lang, FileLoader.getTranslations(pathName).get(lang));

        log.info(langJson);
        assertEquals(langJson.isEmpty(), false);
    }

    @Test
    void getNavbarInnerHTML() throws Exception {
        String innerHTML = FileWriter.getNavbarInnerHTML(FileLoader.getKeys(pathName), "en", "i18n i18n-key_");

        log.info(innerHTML);
        assertEquals(innerHTML.isEmpty(), false);
    }

    @Test
    void getSwitcherSelectTest() throws Exception {
        String select = FileWriter.getSwitcherSelect(FileLoader.getKeys(pathName), "en", "i18n i18n-key_");

        log.info(select);
        assertEquals(select.isEmpty(), false);
    }

    @Test
    void getSwitcherOptionsTest() throws Exception {
        String options = FileWriter.getSwitcherOptions(FileLoader.getKeys(pathName), "en");
        String de = MessageFormat
            .format(I18n.HTML_SWITCHER_OPTION, "de", "German");

        log.info(options);
        assertEquals(options.contains(de), true);
    }

    @Test
    void isLocaleValidTest() throws Exception {
        assertEquals(FileLoader.isLocaleValid("de"), true);
    }

    @Test 
    void readJsonFileTest() throws Exception {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "locales", "en", "test-file-json.json");
        File file = path.toFile();

        assertEquals(FileLoader.readJsonFile(file).at("/courses/1/durationHours").asInt(), 80);
    }

    @Test 
    void readScriptFileTest() throws Exception {
        String file = FileLoader.getScriptNoInjectedJson();

        log.info(file);
        assertEquals(file.contains("const i18nDefaultLang = \"null\";"), true);
    }
}
