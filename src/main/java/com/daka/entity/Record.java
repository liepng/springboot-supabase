package com.daka.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("records")
public class Record {

    @TableId(type = IdType.INPUT)
    private String id;

    @TableField("user_info")
    private Map<String, Object> userInfo;

    @TableField("text")
    private String text;

    @TableField(typeHandler = com.daka.config.ListTypeHandler.class)
    private List<String> images;

    @TableField(typeHandler = com.daka.config.ListTypeHandler.class)
    private List<String> videos;

    @TableField("has_audio")
    private Boolean hasAudio;

    @TableField("audio_data")
    private String audioData;

    @TableField("audio_mime")
    private String audioMime;

    @TableField("audio_duration")
    private String audioDuration;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;
}
