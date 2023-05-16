package com.wuav.client.gui.utils.api;

import com.wuav.client.gui.dto.ImageDTO;

import java.util.List;

public interface IImageOperationService<T> {
        void startImageFetch(ImageOperationFacade.ImageFetchCallback callback);

        List<ImageDTO> getStoredFetchedImages();

        void stopImageFetch();
        void removeImagesFromServer();
        List<T> fetchImagesFromServer(int id);
}
