package com.wuav.client.bll.utilities.engines;

import java.util.Map;

/**
 * The interface for the email engine
 */
public interface IEmailEngine {
    /**
     * Processes the given template with the given variables
     *
     * @param templateName the name of the template
     * @param variables    the variables to process the template with
     * @return the processed template
     */
    String processTemplate(String templateName, Map<String, Object> variables);
}
