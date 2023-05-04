package com.wuav.client.dal.repository;

import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Project;
import com.wuav.client.dal.interfaces.IProjectRepository;
import com.wuav.client.dal.mappers.ProjectMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProjectRepository implements IProjectRepository {
    static Logger logger = LoggerFactory.getLogger(ProjectRepository.class);


//    @Override
//    public Project createProjectByName(int userId, int id, String name,String status) {
//        Project project = null;
//
//        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
//            ProjectMapper mapper = session.getMapper(ProjectMapper.class);
//            mapper.createProjectByName(id, name,status);
//            project = mapper.getProjectById(id);
//            mapper.addUserToProject(userId, id); // Insert the userId and projectId into the user_project table
//            session.commit();
//            return project;
//        } catch (Exception ex) {
//            logger.error("An error occurred mapping tables", ex);
//        }
//
//        return project;
//    }

    // here bring the logger and listener to the GUI
    @Override
    public List<Project> getAllProjectsByUserId(int userId) {
        List<Project> fetchedProjects = new ArrayList<>();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            ProjectMapper mapper = session.getMapper(ProjectMapper.class);
            fetchedProjects = mapper.getAllProjectsByUserId(userId);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return fetchedProjects;
    }




    @Override
    public Project updateProject(int projectId, String description) {
        Project updatedProject = null;

        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            ProjectMapper mapper = session.getMapper(ProjectMapper.class);
            mapper.updateProjectForUserById(projectId, description);
            updatedProject = mapper.getProjectById(projectId);
            session.commit();
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return updatedProject;
    }






//    // could be here seperate method add image to project with userId,projectId, blobURL and the boolean value
//    @Override
//    Project addImageToProject(int userId, int projectId, String blobUrl, boolean isMainImage){
//
//    }


//    @Override
//    public List<Event> getAllEvents() {
//        List<Event> fetchedEvents = new ArrayList<>();
//        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
//            EventMapper mapper = session.getMapper(EventMapper.class);
//            fetchedEvents = mapper.getAllEvents();
//        } catch (Exception ex) {
//            logger.error("An error occurred mapping tables", ex);
//        }
//        return fetchedEvents;
//    }
//
//
//    @Override
//    public List<SpecialTicketType> getAllSpecialTickets() {
//        List<SpecialTicketType> fetchedEvents = new ArrayList<>();
//        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
//            EventMapper mapper = session.getMapper(EventMapper.class);
//            fetchedEvents = mapper.getAllSpecialTickets();
//        } catch (Exception ex) {
//            logger.error("An error occurred mapping tables", ex);
//        }
//        return fetchedEvents;
//    }
//
//    @Override
//    public int createEvent(int id, String title, String loc, Date startDate2, Date endDate2, String startTime2, String endTime2, String notes2) {
//        int returnedId = 0;
//        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
//            EventMapper mapper = session.getMapper(EventMapper.class);
//            int affectedRows = mapper.createEvent(
//                    id,
//                    title,
//                    loc,
//                    startDate2,
//                    endDate2,
//                    startTime2,
//                    endTime2,
//                    notes2
//            );
//            session.commit();
//            returnedId = affectedRows > 0 ? id : 0;
//        } catch (Exception ex) {
//            logger.error("An error occurred mapping tables", ex);
//        }
//        return returnedId;
//    }
//
//    @Override
//    public int addTypeToEvent(int id, TicketType ticketType) {
//        int finalAffectedRows = 0;
//        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
//            EventMapper mapper = session.getMapper(EventMapper.class);
//            int affectedRows = mapper.addTypeToTicket(id,ticketType.getId());
//            session.commit();
//            return affectedRows;
//        } catch (Exception ex) {
//            logger.error("An error occurred mapping tables", ex);
//        }
//        return finalAffectedRows;
//    }
//
//    @Override
//    public int createTicketType(TicketType ticketType) {
//        int returnedId = 0;
//        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
//            EventMapper mapper = session.getMapper(EventMapper.class);
//            int affectedRows = mapper.createTypeForTicket(ticketType);
//            session.commit();
//            returnedId = affectedRows > 0 ? ticketType.getId() : 0;
//        } catch (Exception ex) {
//            logger.error("An error occurred mapping tables", ex);
//        }
//        return returnedId;
//    }
//
//    @Override
//    public int addSpecialTicketToEvent(SpecialTicketType specialTicketType, int eventId) {
//        int finalAffectedRows = 0;
//        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
//            EventMapper mapper = session.getMapper(EventMapper.class);
//            int affectedRows = mapper.addSpecialTicketToEvent(eventId,specialTicketType.getId());
//            session.commit();
//            return affectedRows;
//        } catch (Exception ex) {
//            logger.error("An error occurred mapping tables", ex);
//        }
//        return finalAffectedRows;
//    }
//
//    @Override
//    public int createSpecialTicket(SpecialTicketType specialTicketType) {
//        int returnedId = 0;
//        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
//            EventMapper mapper = session.getMapper(EventMapper.class);
//            int affectedRows = mapper.createSpecialTicketType(specialTicketType);
//            session.commit();
//            returnedId = affectedRows > 0 ? specialTicketType.getId() : 0;
//        } catch (Exception ex) {
//            logger.error("An error occurred mapping tables", ex);
//        }
//        return returnedId;
//    }

}
