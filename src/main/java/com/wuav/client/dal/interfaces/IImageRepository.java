package com.wuav.client.dal.interfaces;

import com.wuav.client.be.CustomImage;
import org.apache.ibatis.session.SqlSession;

public interface IImageRepository {

    boolean createImage(SqlSession session,int id, String imageType, String imageUrl) throws Exception;


    boolean addImageToProject(SqlSession session,int projectId, int id, boolean isMainImage) throws Exception;

    CustomImage getImageById(int id) throws Exception;

    boolean updateImage(int id, String imageType, String imageUrl);

    boolean deleteImageById(int id);
}
