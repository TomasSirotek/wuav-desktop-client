package com.wuav.client.dal.mappers;

import com.wuav.client.be.device.Projector;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface IProjectorMapper {
    List<Projector> getAllProjectors();
    Projector getProjectorById(@Param("projectorId") int projectorId);
    int createProjector(Projector projector);
    //int updateProjectorById(@Param("projectorId") int id,@Param("resolution") String resolution,@Param("connectionType") String connectionType,@Param("devicePort") String devicePort);
    int updateProjectorById(Projector projector);

    int deleteProjectorById(@Param("projectorId") int projectorId);
}
