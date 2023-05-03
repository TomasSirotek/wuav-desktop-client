package com.wuav.client.dal.repository;

import com.wuav.client.be.CustomImage;
import com.wuav.client.dal.interfaces.IImageRepository;
import com.wuav.client.dal.mappers.ImageMapper;
import com.wuav.client.dal.mappers.ProjectMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageRepository implements IImageRepository {

    static Logger logger = LoggerFactory.getLogger(ImageRepository.class);

    @Override
    public CustomImage createImage(int imageId, String imageType, String imageUrl) {
        CustomImage customImage = null;

        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            ImageMapper mapper = session.getMapper(ImageMapper.class);
            mapper.createImage(imageId, imageType, imageUrl); // needs more validation

            // after inserting the image to the table, retrieve it by id
            customImage = mapper.getImageById(imageId);
            session.commit();
            // return custom image by id
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return customImage;
    }


    @Override
    public boolean addImageToProject(int projectId, int imageId, boolean isMainImage) {
        boolean isAdded = false;

        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            ImageMapper mapper = session.getMapper(ImageMapper.class);
            int affectedRows = mapper.addImageToProject(projectId,imageId,isMainImage); // needs more validation
            session.commit();
            if (affectedRows > 0) {
                isAdded = true;
            }
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return isAdded;
    }
}
