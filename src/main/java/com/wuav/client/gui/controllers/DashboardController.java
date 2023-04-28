package com.wuav.client.gui.controllers;

import com.wuav.client.be.Event;
import com.wuav.client.be.SpecialTicketType;
import com.wuav.client.be.Ticket;
import com.wuav.client.be.TicketType;
import com.wuav.client.be.user.AppRole;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.utilities.AlertHelper;
import com.wuav.client.bll.utilities.email.IEmailSender;
import com.wuav.client.bll.utilities.pdf.IPdfGenerator;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.models.CurrentUser;
import com.wuav.client.gui.models.event.IEventModel;
import com.wuav.client.gui.models.user.IUserModel;
import com.google.inject.Inject;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DashboardController extends RootController implements Initializable {





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @Inject
    public DashboardController() {

    }



}
