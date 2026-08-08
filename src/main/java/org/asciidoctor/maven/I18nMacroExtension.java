package org.asciidoctor.maven;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.asciidoctor.jruby.extension.spi.ExtensionRegistry;

public class I18nMacroExtension implements ExtensionRegistry {

    public void register(Asciidoctor asciidoctor) {
        JavaExtensionRegistry javaExtensionRegistry = asciidoctor.javaExtensionRegistry();
        javaExtensionRegistry.preprocessor(I18nPreprocessor.class);
        javaExtensionRegistry.inlineMacro(I18nInlineMacro.class);
        javaExtensionRegistry.docinfoProcessor(I18nDocinfoProcessor.class);
    }

}