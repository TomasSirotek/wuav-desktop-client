package com.wuav.client.dal.mappers;

import com.wuav.client.be.device.Device;

import java.util.List;

public interface IDeviceMapper {
    List<Device> getDevicesByProjectId(int projectId);
}
