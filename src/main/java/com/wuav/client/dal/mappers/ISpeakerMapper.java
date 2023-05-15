package com.wuav.client.dal.mappers;

import com.wuav.client.be.device.Speaker;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ISpeakerMapper {
    List<Speaker> getAllSpeakers();
    Speaker getSpeakerById(@Param("speakerId") int speakerId);
    int createSpeaker(Speaker speaker);
    boolean updateSpeakerById(@Param("speakerId") int id,@Param("name") String name ,@Param("power") String power,@Param("volume") String volume);
    boolean deleteSpeakerById(@Param("speakerId") int speakerId);
}
