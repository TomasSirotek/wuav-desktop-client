package com.wuav.client.dal.mappers;

import com.wuav.client.be.device.Projector;
import org.apache.ibatis.annotations.Param;


/**
 * interface IProjectorMapper
 **/
public interface IProjectorMapper {

    /**
     * Creates projector
     *
     * @param projector projector
     * @return int > 0 if success else 0 (fail)
     */
    int createProjector(Projector projector);

    /**
     * Update projector by Id
     *
     * @param projector
     * @return
     */
    int updateProjectorById(Projector projector);

    /**
     * Delete projector by projectorId
     *
     * @param projectorId projectorId
     * @return int > 0 if success else 0 (fail)
     */
    int deleteProjectorById(@Param("projectorId") int projectorId);
}
