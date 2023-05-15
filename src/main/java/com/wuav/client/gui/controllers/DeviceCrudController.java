package com.wuav.client.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.wuav.client.be.device.Device;
import com.wuav.client.be.device.Projector;
import com.wuav.client.be.device.Speaker;
import com.wuav.client.bll.helpers.EventType;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.controllers.event.RefreshEvent;
import com.wuav.client.gui.models.DeviceModel;
import com.wuav.client.gui.utils.AlertHelper;
import com.wuav.client.gui.utils.enums.DeviceType;
import com.wuav.client.gui.utils.event.CustomEvent;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DeviceCrudController extends RootController implements Initializable {

    @FXML
    private MFXButton deleteToggle;
    @FXML
    private MFXButton toggleCreateEdit;
    @FXML
    private MFXTextField deviceName;
    @FXML
    private ChoiceBox deviceTypeField;
    @FXML
    private VBox deviceFieldVBox;
    @FXML
    private MFXButton toggleChatButton;
    @FXML
    private MFXButton toggleHide;
    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private ListView chatListView;

    @FXML
    private MFXButton handleSend;
    @FXML
    private MFXTextField queryField;

    private String loadingMessage;

    private TextField resolutionField,connectionType,devicePort;

    private final String API_URL = "https://free.churchless.tech/v1/chat/completions";


    private static final double HIDE_DIVIDER_POSITION = 1.0;
    private static final double SHOW_DIVIDER_POSITION = 0.4448;
    private boolean isRightSideVisible = false;
    private boolean isEdit = false;
    final double LOCKED_DIVIDER_POSITION = 0.4448;

    // FOR SPEAKER
    private TextField power,volume;

    private final EventBus eventBus;


    private final DeviceModel deviceModel;

    private Device selectedDeviceForCreateEdit;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Inject
    public DeviceCrudController(EventBus eventBus, DeviceModel deviceModel) {
        this.eventBus = eventBus;
        this.deviceModel = deviceModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventBus.register(this);
        handleExpandActions();
        setupInitialize();
        handleSend.setOnAction(event -> {
            handleAPIChatSend();
        });
    }

    private void setupInitialize() {
        if(!isEdit) {
            fillDeviceTypeChooseField();
            toggleCreateEdit.setOnAction(e -> {
                    toggleCreateEdit.setText("Create");
                    createDevice();
            });
            deviceTypeField.setOnAction(e -> {
                        String selectedDeviceType = (String) deviceTypeField.getValue();

                        VBox deviceCRUDBox = new VBox();
                        deviceCRUDBox.setSpacing(10);
                        deviceCRUDBox.setPadding(new Insets(10));

                        if (selectedDeviceType.equals(DeviceType.PROJECTOR.name())) {
                            setupProjectorFields();
                            selectedDeviceForCreateEdit = new Projector(0,deviceName.getText(),Projector.class.getSimpleName().toUpperCase());
                        } else if (selectedDeviceType.equals(DeviceType.SPEAKER.name())) {
                            setupSpeakerFields();
                            selectedDeviceForCreateEdit = new Speaker(0,deviceName.getText(), Speaker.class.getSimpleName().toUpperCase());
                        }
                        deviceFieldVBox.getChildren().add(deviceCRUDBox);
                    });
        }else {
            deleteToggle.setVisible(true);
            deleteToggle.setOnAction(e -> {
                deleteDevice();
            });

        }
    }

    private void deleteDevice() {
        if (selectedDeviceForCreateEdit != null) {
            Boolean isDeviceDeleted;
            try {
                isDeviceDeleted = asyncDeleteDevice(selectedDeviceForCreateEdit,selectedDeviceForCreateEdit.getClass()).get();

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            if (isDeviceDeleted) {
                finishAndClose();
                AlertHelper.showDefaultAlert("Device deleted successfully", Alert.AlertType.INFORMATION);
            } else {
                AlertHelper.showDefaultAlert("Device deleted failed", Alert.AlertType.ERROR);
            }
        }
    }

    private Future<Boolean> asyncDeleteDevice(Device device,Class<? extends Device> type) {
        return executorService.submit(() -> {
            try {
                return deviceModel.deleteDevice(device.getId(),type);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    private void createDevice() {
        if (selectedDeviceForCreateEdit instanceof Projector) {
            if (validateDeviceInput(selectedDeviceForCreateEdit, Arrays.asList(resolutionField, connectionType, devicePort))) {
                //
                int generatedId = UniqueIdGenerator.generateUniqueId();
                Device device = new Projector(generatedId, deviceName.getText(), Projector.class.getSimpleName().toUpperCase());
                ((Projector) device).setResolution(resolutionField.getText());
                ((Projector) device).setConnectionType(connectionType.getText());
                ((Projector) device).setDevicePort(devicePort.getText());

                deviceCall(device);
                finishAndClose();
            } else {
                AlertHelper.showDefaultAlert("Please fill all the fields", Alert.AlertType.WARNING);
            }
        } else if (selectedDeviceForCreateEdit instanceof Speaker) {
            if (validateDeviceInput(selectedDeviceForCreateEdit, Arrays.asList(power, volume))) {
                int generatedId = UniqueIdGenerator.generateUniqueId();
                Device device = new Speaker(generatedId, deviceName.getText(), Speaker.class.getSimpleName().toUpperCase());
                ((Speaker) device).setPower(power.getText());
                ((Speaker) device).setVolume(volume.getText());

                deviceCall(device);
                finishAndClose();
            } else {
                AlertHelper.showDefaultAlert("Please fill all the fields", Alert.AlertType.WARNING);
            }
        }

    }

    private void finishAndClose() {
        eventBus.post(new RefreshEvent(EventType.REFRESH_DEVICE_LIST));
        Stage stage = (Stage) toggleCreateEdit.getScene().getWindow();
        stage.close();
    }

    private void deviceCall(Device device) {
        Boolean isDeviceCreated;
        try {
            isDeviceCreated = asyncCreateDevice(device).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        if(isDeviceCreated){
            AlertHelper.showDefaultAlert("Device created successfully", Alert.AlertType.INFORMATION);
        } else {
            AlertHelper.showDefaultAlert("Device creation failed", Alert.AlertType.ERROR);
        }
    }

    private void deviceCall2(Device device) {
        Boolean isDeviceCreated;
        try {
            isDeviceCreated = asyncCreateDevice2(device).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        if(isDeviceCreated){
            AlertHelper.showDefaultAlert("Device updated successfully", Alert.AlertType.INFORMATION);
        } else {
            AlertHelper.showDefaultAlert("Device updated failed", Alert.AlertType.ERROR);
        }
    }

    private Future<Boolean> asyncCreateDevice(Device device) {
        return executorService.submit(() -> {
            try {
                return deviceModel.createDevice(device);
            } catch (Exception e) {
                // Handle any exceptions that occurred during device creation
                e.printStackTrace();
                return false;
            }
        });
    }

    private Future<Boolean> asyncCreateDevice2(Device device) {
        return executorService.submit(() -> {
            try {
                return deviceModel.updateDevice(device);
            } catch (Exception e) {
                // Handle any exceptions that occurred during device creation
                e.printStackTrace();
                return false;
            }
        });
    }

    @Subscribe
    public void handleEdit(CustomEvent event) {
        if (event.getEventType() == EventType.SET_CURRENT_DEVICE) {
            deviceTypeField.setDisable(true);
            toggleCreateEdit.setText("Edit");
            deleteToggle.setVisible(true);
            deleteToggle.setOnAction(e -> {
                deleteDevice();
            });

            toggleCreateEdit.setOnAction(e -> {
                editDevice();
            });
            this.selectedDeviceForCreateEdit = (Device) event.getData();

            if (selectedDeviceForCreateEdit instanceof Projector) {
                setupProjectorFields();
                deviceName.setText(selectedDeviceForCreateEdit.getName());
                resolutionField.setText(((Projector) selectedDeviceForCreateEdit).getResolution());
                connectionType.setText(((Projector) selectedDeviceForCreateEdit).getConnectionType());
                devicePort.setText(((Projector) selectedDeviceForCreateEdit).getDevicePort());
            } else if (selectedDeviceForCreateEdit instanceof Speaker) {
                setupSpeakerFields();
                deviceName.setText(selectedDeviceForCreateEdit.getName());
                power.setText(((Speaker) selectedDeviceForCreateEdit).getPower());
                volume.setText(((Speaker) selectedDeviceForCreateEdit).getVolume());
            }
        }
    }

    private void editDevice() {
        if (selectedDeviceForCreateEdit instanceof Projector) {
            if (validateDeviceInput(selectedDeviceForCreateEdit, Arrays.asList(resolutionField, connectionType, devicePort))) {
                //
                Device device = new Projector(selectedDeviceForCreateEdit.getId(), deviceName.getText(), Projector.class.getSimpleName().toUpperCase());
                ((Projector) device).setResolution(resolutionField.getText());
                ((Projector) device).setConnectionType(connectionType.getText());
                ((Projector) device).setDevicePort(devicePort.getText());

                deviceCall2(device);
                finishAndClose();
            } else {
                AlertHelper.showDefaultAlert("Please fill all the fields", Alert.AlertType.WARNING);
            }
        } else if (selectedDeviceForCreateEdit instanceof Speaker) {
            if (validateDeviceInput(selectedDeviceForCreateEdit, Arrays.asList(power, volume))) {
                Device device = new Speaker(selectedDeviceForCreateEdit.getId(), deviceName.getText(), Speaker.class.getSimpleName().toUpperCase());
                ((Speaker) device).setPower(power.getText());
                ((Speaker) device).setVolume(volume.getText());

                deviceCall2(device);
                finishAndClose();
            } else {
                AlertHelper.showDefaultAlert("Please fill all the fields", Alert.AlertType.WARNING);
            }
        }
    }

    private boolean validateDeviceInput(Device device, List<TextField> fields) {
        return fields.stream().noneMatch(field -> field.getText().isEmpty());
    }

    private void fillDeviceTypeChooseField(){
        Arrays.stream(DeviceType.values())
                .map(Enum::toString)
                .forEach(deviceTypeField.getItems()::add);
        deviceTypeField.getSelectionModel().select("Select device");
    }


    // setting different field for the devices
    private void setupSpeakerFields() {
        power = new TextField();
        power.setPromptText("Power");

        volume = new TextField();
        volume.setPromptText("Volume");

        VBox.setMargin(power, new Insets(25, 0, 10, 0));
        VBox.setMargin(volume, new Insets(0, 0, 10, 0));

        deviceFieldVBox.getChildren().clear();
        deviceFieldVBox.getChildren().addAll(power, volume);

    }


    private void setupProjectorFields() {
        resolutionField = new TextField();
        resolutionField.setPromptText("Resolution");

        connectionType = new TextField();
        connectionType.setPromptText("Connection Type");

        devicePort = new TextField();
        devicePort.setPromptText("Device Port");

        VBox.setMargin(resolutionField, new Insets(25, 0, 10, 0));
        VBox.setMargin(connectionType, new Insets(0, 0, 10, 0));
        VBox.setMargin(devicePort, new Insets(0, 0, 10, 0));

        deviceFieldVBox.getChildren().clear();
        deviceFieldVBox.getChildren().addAll(resolutionField, connectionType, devicePort);
    }




    private void handleAPIChatSend() {
        String prompt = queryField.getText();
        if (!prompt.isEmpty()) {
            chatListView.getItems().add("You: " + prompt);
            queryField.clear();
            loadingMessage = "Chat: Loading...";
            chatListView.getItems().add(loadingMessage);
            sendAsyncRequest(prompt);
        }
    }

    private void sendAsyncRequest(String prompt) {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; utf-8");

                String jsonInputString = String.format("{\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}]}", prompt);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String content = in.readLine();

                    // close connections
                    in.close();
                    connection.disconnect();

                    // parse json and get the assistant's message
                    JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();
                    String assistantMessage = jsonObject.getAsJsonArray("choices").get(0).getAsJsonObject()
                            .getAsJsonObject("message").get("content").getAsString();


                    // update the UI on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        // Remove "Loading..." message
                        chatListView.getItems().remove(loadingMessage);
                        // Add the response message
                        chatListView.getItems().add("Assistant: " + assistantMessage);
                    });
                } else {
                    AlertHelper.showDefaultAlert("Something went wrong. Please try again later.", Alert.AlertType.ERROR);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }


        private void handleExpandActions() {
           // Handle the button click event
            mainSplitPane.setDividerPositions(SHOW_DIVIDER_POSITION);
            toggleHide.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (isRightSideVisible) {
                        // Hide the right side
                        mainSplitPane.setDividerPositions(1.0);
                        isRightSideVisible = false;
                    } else {
                        // Show the right side
                        mainSplitPane.setDividerPositions(SHOW_DIVIDER_POSITION);
                        isRightSideVisible = true;
                    }
                }
            });

            toggleChatButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!isRightSideVisible) {
                        // Show the right side
                        mainSplitPane.setDividerPositions(SHOW_DIVIDER_POSITION);
                        isRightSideVisible = true;
                    } else {
                        // Hide the right side
                        mainSplitPane.setDividerPositions(1.0);
                        isRightSideVisible = false;
                    }
                }
            });
        }
}

