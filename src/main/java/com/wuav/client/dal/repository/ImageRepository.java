package com.wuav.client.dal.repository;

import com.wuav.client.be.CustomImage;
import com.wuav.client.dal.interfaces.IImageRepository;
import com.wuav.client.dal.mappers.IImageMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ImageRepository class.
 **/
public class ImageRepository implements IImageRepository {

    private Logger logger = LoggerFactory.getLogger(ImageRepository.class);

    /**
     * Get the image by id.
     *
     * @param id image id
     * @return CustomImage
     * @throws Exception Exception
     */
    @Override
    public CustomImage getImageById(int id) throws Exception {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IImageMapper mapper = session.getMapper(IImageMapper.class);
            return mapper.getImageByIdThatIsMain(id);
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    /**
     * Update the image.
     *
     * @param id        image id
     * @param imageType image type
     * @param imageUrl  image url
     * @return boolean true if updated
     */
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

    /**
     * Create a new image.
     *
     * @param session   sql session
     * @param imageId   image id
     * @param imageType image type
     * @param imageUrl  image url
     * @return boolean true if created
     * @throws Exception Exception
     */
    @Override
    public boolean createImage(SqlSession session, int imageId, String imageType, String imageUrl) throws Exception {
        try {
            IImageMapper mapper = session.getMapper(IImageMapper.class);
            return mapper.createImage(imageId, imageType, imageUrl) > 0;
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }


    /**
     * Add image to project.
     *
     * @param session     sql session
     * @param projectId   project id
     * @param imageId     image id
     * @param isMainImage is main image
     * @return boolean true if added
     * @throws Exception Exception
     */
    @Override
    public boolean addImageToProject(SqlSession session, int projectId, int imageId, boolean isMainImage) throws Exception {
        try {
            IImageMapper mapper = session.getMapper(IImageMapper.class);
            return mapper.addImageToProject(projectId, imageId, isMainImage) > 0; // needs more validation
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    /**
     * Delete image by id.
     *
     * @param session sql session
     * @param id      image id
     * @return boolean true if deleted
     * @throws Exception Exception
     */
    @Override
    public boolean deleteImageById(SqlSession session, int id) throws Exception {
        try {
            IImageMapper mapper = session.getMapper(IImageMapper.class);
            int affectedRows = mapper.deleteImage(id);

            return affectedRows > 0;
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }


}
