package com.wuav.client.dal.mappers;

import com.wuav.client.be.device.Projector;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface IProjectorMapper {
    List<Projector> getAllProjectors();
    Projector getProjectorById(@Param("projectorId") int projectorId);
    int createProjector(Projector projector);
    boolean updateProjectorById(@Param("projectorId") int id,@Param("name") String name,@Param("resolution") String resolution,@Param("connectionType") String connectionType,@Param("devicePort") String devicePort);
    boolean deleteProjectorById(@Param("projectorId") int projectorId);
}
