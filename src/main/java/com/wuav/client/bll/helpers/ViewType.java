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
    EXPORT {
        @Override
        public String getFXMLView() {
            return "views/exportView.fxml";
        }
    },
    ACTIONS {
        @Override
        public String getFXMLView() {
            return "views/actionModalView.fxml";
        }
    },
    PROJECT_ACTIONS{
        @Override
        public String getFXMLView() {
            return "views/projectActionView.fxml";
        }
    },
    USER_PROFILE{
        @Override
        public String getFXMLView() {
            return "views/userProfileView.fxml";
        }
    },
    ALL_USERS{
        @Override
        public String getFXMLView() {
            return "views/allUsersView.fxml";
        }
    },
    USER_MODAL{
        @Override
        public String getFXMLView() {
            return "views/userModalView.fxml";
        }
    },
    USER_SETTINGS{
        @Override
        public String getFXMLView() {
            return "views/userSettingsView.fxml";
        }
    },
    PDF_BUILDER{
        @Override
        public String getFXMLView() {
            return "views/pdfBuilderView.fxml";
        }
    },
    DEVICE_CRUD{
        @Override
        public String getFXMLView() {
            return "views/deviceCrudView.fxml";
        }
    },
    DASHBOARD {
        @Override
        public String getFXMLView() {
            return "views/dashboardView.fxml";
        }
    };

    /**
     * Abstract method that forces each individual ENUM to override returning string with its own
     * so if for example MAIN is used it ensures that it will only return views/base.fxml
     * abstract declared and forces other subclasses (in this case, enum constants) to implement it
     * form of polymophism
     * @return String
     */
    public abstract String getFXMLView();
}
