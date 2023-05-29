package com.wuav.client.dal.mappers;

import com.wuav.client.be.CustomImage;
import org.apache.ibatis.annotations.Param;

/**
 * Interface for ImageMapper
 */
public interface IImageMapper {


    /**
     * Creates image
     *
     * @param imageId   the image Id
     * @param imageType the image type
     * @param imageUrl  the image url
     * @return the int if > 0 then success else fail
     */
    int createImage(@Param("imageId") int imageId, @Param("imageType") String imageType, @Param("imageUrl") String imageUrl);


    /**
     * Adds Image to project
     *
     * @param projectId
     * @param imageId
     * @param isMainImage
     * @return
     */
    int addImageToProject(@Param("projectId") int projectId, @Param("imageId") int imageId, @Param("isMainImage") boolean isMainImage);

    /**
     * Gets Image by id that is main image
     *
     * @param id the image id
     * @return the image by id object
     */
    CustomImage getImageByIdThatIsMain(@Param("imageId") int id);

    /**
     * Upadtes Image.
     *
     * @param id        the image id
     * @param imageType the image type
     * @param imageUrl  the image url
     * @return the int if > 0 then success else fail
     */
    int updateImage(@Param("imageId") int id, @Param("imageType") String imageType, @Param("imageURL") String imageUrl);

    /**
     * Deletes Image.
     *
     * @param id the image id
     * @return the int if > 0 then success else fail
     */
    int deleteImage(@Param("imageId") int id);

}
