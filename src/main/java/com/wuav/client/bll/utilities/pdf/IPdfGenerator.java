package com.wuav.client.bll.utilities.pdf;


import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;

public interface IPdfGenerator {

    String generatePdf(AppUser user, Project project,String fileName);
}