package com.wuav.client.bll.utilities.pdf;


import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;

import java.io.ByteArrayOutputStream;

public interface IPdfGenerator {

    ByteArrayOutputStream generatePdf(AppUser user, Project project, String fileName);
}