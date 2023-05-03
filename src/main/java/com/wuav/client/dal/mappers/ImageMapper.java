package com.wuav.client.dal.mappers;

import com.wuav.client.be.CustomImage;
import org.apache.ibatis.annotations.Param;

public interface ImageMapper {


    // retrieving image by id
    CustomImage getImageById(@Param("imageId")int imageId);

    // inserting image to the table
    int createImage(@Param("imageId")int imageId, @Param("imageType") String imageType, @Param("imageUrl") String imageUrl);


    int addImageToProject(@Param("projectId") int projectId,@Param("imageId") int imageId,@Param("isMainImage") boolean isMainImage);
}
