package com.wuav.client.bll.helpers;

/**
 * Enum with abstract method that forces each individual ENUM to override returning string with its own
 * so if for example MAIN is used it ensures that it will only return views/base.fxml
 */
public enum ViewType {
    LOGIN {
        @Override
        public String getFXMLView() {
            return "views/loginView.fxml";
        }
    },
    MAIN {
        @Override
        public String getFXMLView() {
            return "views/dashboardLayout.fxml";
        }
    },
    PROJECTS {
        @Override
        public String getFXMLView() {
            return "views/projectView.fxml";
        }
    },
    ACTIONS {
        @Override
        public String getFXMLView() {
            return "views/actionModalView.fxml";
        }
    },
    SPECIAL_TICKETS{
        @Override
        public String getFXMLView() {
            return "views/ticketView.fxml";
        }
    },
    DASHBOARD {
        @Override
        public String getFXMLView() {
            return "views/dashboardView.fxml";
        }
    };
    public abstract String getFXMLView();
}
