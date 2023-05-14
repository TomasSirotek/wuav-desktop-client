package com.wuav.client.gui.utils.api;

import java.util.List;

public interface IImageOperationService<T> {
        void startImageFetch(ImageOperationFacade.ImageFetchCallback callback);
        void stopImageFetch();
        void removeImagesFromServer();
        List<T> fetchImagesFromServer(int id);
}
