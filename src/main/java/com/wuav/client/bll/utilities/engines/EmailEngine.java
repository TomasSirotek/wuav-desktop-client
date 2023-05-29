package com.wuav.client.bll.utilities.engines;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Map;


/**
 * The engine for the email sender
 */
public class EmailEngine implements IEmailEngine {

    /**
     * Processes the given template with the given variables
     *
     * @param templateName the name of the template
     * @param variables    the variables to process the template with
     * @return the processed template
     */
    @Override
    public String processTemplate(String templateName, Map<String, Object> variables) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }

        return templateEngine.process(templateName, context);

    }


}
