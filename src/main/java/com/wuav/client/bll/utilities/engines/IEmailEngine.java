package com.wuav.client.bll.utilities.engines;

import java.util.Map;

public interface IEmailEngine {
    String processTemplate(String templateName, Map<String, Object> variables);
}
