package com.wuav.client.gui.dto;

import javafx.scene.image.Image;

import java.io.File;
import java.util.List;

public record CreateProjectDTO(int id,
                               String name,
                               String description,
                               List<ImageDTO> images,
                               CustomerDTO customer

) {

}
