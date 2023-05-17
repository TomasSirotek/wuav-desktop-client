package com.wuav.client.dal.interfaces;

import com.wuav.client.be.CustomImage;

public interface IImageRepository {

    CustomImage createImage(int id, String imageType, String imageUrl);


    boolean addImageToProject(int projectId, int id, boolean isMainImage);

    CustomImage getImageById(int id) throws Exception;

    boolean updateImage(int id, String imageType, String imageUrl);

    boolean deleteImageById(int id);
}
