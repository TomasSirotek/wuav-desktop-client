package com.wuav.client.gui.dto;

import javafx.scene.image.Image;

import java.io.File;
import java.util.List;

public record CreateProjectDTO(int id,
                               String name,
                               String description,
                               File mainImage,
                               CustomerDTO customer,
                               List<Image> imagesFromApp
) {
    @Override
    public String toString() {
        return "CreateProjectDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mainImage=" + mainImage +
                ", customer=" + customer +
                ", imagesFromApp=" + imagesFromApp +
                '}';
    }
}
