package com.daka.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "打卡记录响应体")
public class RecordResponse {

    @Schema(description = "记录ID", example = "uuid-string")
    private String id;

    @Schema(description = "用户信息JSON对象")
    private Map<String, Object> userInfo;

    @Schema(description = "打卡文本内容")
    private String text;

    @Schema(description = "图片URL列表")
    private List<String> images;

    @Schema(description = "视频URL列表")
    private List<String> videos;

    @Schema(description = "是否包含音频")
    private Boolean hasAudio;

    @Schema(description = "音频数据(Base64)")
    private String audioData;

    @Schema(description = "音频MIME类型")
    private String audioMime;

    @Schema(description = "音频时长")
    private String audioDuration;

    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

    public static RecordResponse fromEntity(com.daka.entity.Record record) {
        return RecordResponse.builder()
                .id(record.getId())
                .userInfo(record.getUserInfo())
                .text(record.getText())
                .images(record.getImages())
                .videos(record.getVideos())
                .hasAudio(record.getHasAudio())
                .audioData(record.getAudioData())
                .audioMime(record.getAudioMime())
                .audioDuration(record.getAudioDuration())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
