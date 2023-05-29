package com.wuav.client.gui.utils.api;

import com.wuav.client.gui.dto.ImageDTO;

import java.util.List;

/**
 * The interface Image operation service.
 *
 * @param <T> the type parameter
 */
public interface IImageOperationService<T> {
    /**
     * The interface Image fetch callback.
     */
    void startImageFetch(ImageOperationFacade.ImageFetchCallback callback);

    /**
     * Gets stored fetched images.
     *
     * @return the stored fetched images
     */
    List<ImageDTO> getStoredFetchedImages();

    /**
     * Stop image fetch.
     */
    void stopImageFetch();

    /**
     * Remove images from server.
     */
    void removeImagesFromServer();

    /**
     * Fetch images from server.
     */
    List<T> fetchImagesFromServer(int id);
}
