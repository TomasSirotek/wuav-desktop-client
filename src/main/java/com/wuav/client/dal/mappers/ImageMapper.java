package com.wuav.client.dal.mappers;

import com.wuav.client.be.CustomImage;
import org.apache.ibatis.annotations.Param;

public interface ImageMapper {

    CustomImage getImageById(@Param("imageId")int imageId);

    int createImage(@Param("imageId")int imageId, @Param("imageType") String imageType, @Param("imageUrl") String imageUrl);


    int addImageToProject(@Param("projectId") int projectId,@Param("imageId") int imageId,@Param("isMainImage") boolean isMainImage);

    CustomImage getImageByIdThatIsMain(@Param("imageId") int id);

    int updateImage(@Param("imageId") int id,@Param("imageType") String imageType,@Param("imageURL") String imageUrl);

    int deleteImage(@Param("imageId") int id);

}
