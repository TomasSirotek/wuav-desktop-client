package com.wuav.client.gui.dto;

import com.wuav.client.be.device.Device;
import java.util.List;

/**
 * The record CreateProjectDTO.
 */
public record CreateProjectDTO(int id,
                               String name,
                               String description,
                               List<ImageDTO> images,
                               CustomerDTO customer,
                               List<Device> selectedDevices

) {

}
