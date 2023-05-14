package com.wuav.client.dal.repository;

import com.wuav.client.be.Project;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectRepositoryTest {

    private static final int EXISTING_USER_ID = 1;

    @Test
    public void testGetAllProjectsByUserId() {
        // Arrange
        ProjectRepository projectRepository = new ProjectRepository();
        // Act
        List<Project> projects = projectRepository.getAllProjectsByUserId(EXISTING_USER_ID);

        // Assert
        assertNotNull(projects, "Returned projects list is null");
        assertFalse(projects.isEmpty(), "Returned projects list is empty");
    }

}
