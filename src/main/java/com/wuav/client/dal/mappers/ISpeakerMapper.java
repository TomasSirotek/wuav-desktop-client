package com.wuav.client.dal.mappers;

import com.wuav.client.be.device.Speaker;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ISpeakerMapper {
    List<Speaker> getAllSpeakers();
    Speaker getSpeakerById(@Param("speakerId") int speakerId);
    int createSpeaker(Speaker speaker);
    int updateSpeakerById(Speaker speaker);
    boolean deleteSpeakerById(@Param("speakerId") int speakerId);

    int updateSpeaker(Speaker device);
}
