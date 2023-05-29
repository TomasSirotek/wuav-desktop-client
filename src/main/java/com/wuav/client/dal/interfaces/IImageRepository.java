package com.wuav.client.dal.interfaces;

import com.wuav.client.be.CustomImage;
import org.apache.ibatis.session.SqlSession;


/**
 * Interface for ImageRepository.
 */
public interface IImageRepository {

    /**
     * Creates images
     *
     * @param session   sql session
     * @param id        image id
     * @param imageType image type
     * @param imageUrl  image url
     * @return true if image is created
     * @throws Exception if error occurs
     */
    boolean createImage(SqlSession session, int id, String imageType, String imageUrl) throws Exception;

    /**
     * Adds image to project
     *
     * @param session     sql session
     * @param projectId   project id
     * @param id          image id
     * @param isMainImage is main image
     * @return true if image is added to project
     * @throws Exception if error occurs
     */
    boolean addImageToProject(SqlSession session, int projectId, int id, boolean isMainImage) throws Exception;

    /**
     * Gets image by id
     *
     * @param id image id
     * @return image
     * @throws Exception if error occurs
     */
    CustomImage getImageById(int id) throws Exception;

    /**
     * Updates image
     *
     * @param id        image id
     * @param imageType image type
     * @param imageUrl  image url
     * @return true if image is updated
     */
    boolean updateImage(int id, String imageType, String imageUrl);

    /**
     * Deletes image by id
     *
     * @param session sql session
     * @param id      image id
     * @return true if image is deleted
     * @throws Exception if error occurs
     */
    boolean deleteImageById(SqlSession session, int id) throws Exception;
}
