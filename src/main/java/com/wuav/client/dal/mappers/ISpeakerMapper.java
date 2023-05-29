package com.wuav.client.dal.mappers;

import com.wuav.client.be.device.Speaker;
import org.apache.ibatis.annotations.Param;

/**
 * interface ISpeakerMapper
 **/
public interface ISpeakerMapper {

    /**
     * Creates speaker
     *
     * @param speaker speaker
     * @return int > 0 if success else 0 (fail)
     */
    int createSpeaker(Speaker speaker);

    /**
     * Update speaker by id
     *
     * @param speaker speaker class
     * @return
     */
    int updateSpeakerById(Speaker speaker);

    /**
     * Delete speaker by speakerId
     *
     * @param speakerId speakerId
     * @return int > 0 if success else 0 (fail)
     */
    int deleteSpeakerById(@Param("speakerId") int speakerId);
}
