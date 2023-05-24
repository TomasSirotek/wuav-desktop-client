package com.wuav.client.dal.repository;

import com.wuav.client.be.Project;
import com.wuav.client.be.device.Device;
import com.wuav.client.dal.mappers.IProjectMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void testUpdateProjectDescription() throws Exception {
        ProjectRepository projectRepository = new ProjectRepository();
        String description = "Updated description";

        // Update the project
        projectRepository.updateProject(EXISTING_PROJECT_ID, description);

        // Retrieve the project again
        Project updatedProject = projectRepository.getProjectById(EXISTING_PROJECT_ID);

        // Assert
        assertNotNull(updatedProject, "Returned project is null");
        assertEquals(EXISTING_PROJECT_ID, updatedProject.getId(), "Returned project ID does not match");
        assertEquals(description, updatedProject.getDescription(), "Returned project description does not match");
    }

    @Test
    void testAddProjectToUser() throws Exception {
        ProjectRepository projectRepository = new ProjectRepository();
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

        // CONTINUE LATER
        // Add the project to the user
        projectRepository.addProjectToUser(session, EXISTING_USER_ID, EXISTING_PROJECT_ID);

        // Retrieve the user's projects again
        List<Project> userProjects = projectRepository.getAllProjectsByUserId(EXISTING_USER_ID);

        // Assert
        assertNotNull(userProjects, "Returned projects are null");
        assertTrue(userProjects.stream().anyMatch(p -> p.getId() == EXISTING_PROJECT_ID), "User does not have the added project");
    }

    @Test
    void testUpdateProjectName() throws Exception {
        ProjectRepository projectRepository = new ProjectRepository();
        String name = "Updated name";

        // Update the project
        projectRepository.updateNotes(EXISTING_PROJECT_ID, name);

        // Retrieve the project again
        Project updatedProject = projectRepository.getProjectById(EXISTING_PROJECT_ID);

        // Assert
        assertNotNull(updatedProject, "Returned project is null");
        assertEquals(EXISTING_PROJECT_ID, updatedProject.getId(), "Returned project ID does not match");
        assertEquals(name, updatedProject.getDescription(), "Returned name does not match");
    }

    @Test
    void testDeleteProjectById() throws Exception {
        ProjectRepository projectRepository = new ProjectRepository();
        SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

        // Delete the project
        boolean isDeleted = projectRepository.deleteProjectById(session,EXISTING_PROJECT_ID);

        // Assert
        assertTrue(isDeleted, "Project was not deleted");

        // Try to retrieve the project again
        Project deletedProject = projectRepository.getProjectById(EXISTING_PROJECT_ID);

        // Assert
        assertNull(deletedProject, "Deleted project was found");
    }





}
