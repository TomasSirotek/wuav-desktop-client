package com.wuav.client.dal.repository;

import com.wuav.client.be.CustomImage;
import com.wuav.client.dal.interfaces.IImageRepository;
import com.wuav.client.dal.mappers.IImageMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageRepository implements IImageRepository {

    static Logger logger = LoggerFactory.getLogger(ImageRepository.class);


    @Override
    public CustomImage getImageById(int id) throws Exception {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IImageMapper mapper = session.getMapper(IImageMapper.class);
            return mapper.getImageByIdThatIsMain(id);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    @Override
    public boolean updateImage(int id, String imageType, String imageUrl) {
        int affectedRows = 0;

        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IImageMapper mapper = session.getMapper(IImageMapper.class);
            affectedRows = mapper.updateImage(
                    id,
                    imageType,
                    imageUrl
            );
            session.commit();

            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }

        return false;
    }

    @Override
    public boolean createImage(SqlSession session,int imageId, String imageType, String imageUrl) throws Exception {
        try {
            IImageMapper mapper = session.getMapper(IImageMapper.class);
            return mapper.createImage(imageId, imageType, imageUrl) > 0; // needs more validation
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }


    @Override
    public boolean addImageToProject(SqlSession session, int projectId, int imageId, boolean isMainImage) throws Exception {
        try {
            IImageMapper mapper = session.getMapper(IImageMapper.class);
            return mapper.addImageToProject(projectId,imageId,isMainImage) > 0; // needs more validation
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    @Override
    public boolean deleteImageById(int id) {
        int affectedRows = 0;

        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IImageMapper mapper = session.getMapper(IImageMapper.class);
            affectedRows = mapper.deleteImage(
                    id
            );
            session.commit();

            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }

        return false;
    }



}
