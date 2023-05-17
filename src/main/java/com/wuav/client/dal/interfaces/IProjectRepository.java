package com.wuav.client.dal.interfaces;

import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

public interface IProjectRepository {

    List<Project> getAllProjectsByUserId(int userId) throws Exception;

    List<Project> getAllProjects() throws Exception;

    Project getProjectById(int projectId) throws Exception;

    Project updateProject(int projectId, String description) throws Exception;

    boolean createProject(CreateProjectDTO projectDTO) throws Exception;

    int addProjectToUser(int userId, int projectId) throws Exception;

    boolean updateNotes(int projectId, String content) throws Exception;

    boolean deleteProjectById(int id) throws Exception;

    int addDeviceToProject(int projectId, int deviceId) throws Exception;
}
