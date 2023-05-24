package com.wuav.client.dal.repository;

import com.wuav.client.be.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectRepositoryTest {

    private static final int EXISTING_USER_ID = 1;

    private static final int EXISTING_PROJECT_ID = 911577009;


    @Test
    void testGetAllProjects() throws Exception {
        ProjectRepository projectRepository = new ProjectRepository();
        // Act
        List<Project> projects = projectRepository.getAllProjects();

        // Assert
        Assertions.assertNotNull(projects, "Returned projects list is null");
        Assertions.assertFalse(projects.isEmpty(), "Returned projects list is empty");
    }
    @Test
    public void testGetAllProjectsByUserId() throws Exception {
        // Arrange
        ProjectRepository projectRepository = new ProjectRepository();
        // Act
        List<Project> projects = projectRepository.getAllProjectsByUserId(EXISTING_USER_ID);

        // Assert
        assertNotNull(projects, "Returned projects list is null");
        assertFalse(projects.isEmpty(), "Returned projects list is empty");
    }

    @Test
    void testGetProjectById() throws Exception {
        ProjectRepository projectRepository = new ProjectRepository();

        // Act
        Project project = projectRepository.getProjectById(EXISTING_PROJECT_ID);

        // Assert
        Assertions.assertNotNull(project, "Returned project is null");
        Assertions.assertEquals(EXISTING_PROJECT_ID, project.getId(), "Returned project ID does not match");
    }

}
