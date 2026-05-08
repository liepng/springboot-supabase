package com.daka.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daka.dto.RecordRequest;
import com.daka.dto.RecordResponse;
import com.daka.entity.Record;
import com.daka.mapper.RecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordMapper recordMapper;

    public List<RecordResponse> getAllRecords() {
        return recordMapper.selectAllOrderByCreatedAtDesc().stream()
                .map(RecordResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public IPage<RecordResponse> getRecordsPaginated(int page, int size) {
        Page<Record> pageResult = new Page<>(page + 1, size);
        LambdaQueryWrapper<Record> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Record::getCreatedAt);

        IPage<Record> result = recordMapper.selectPage(pageResult, wrapper);
        return result.convert(RecordResponse::fromEntity);
    }

    public RecordResponse getRecordById(String id) {
        Record record = recordMapper.selectById(id);
        if (record == null) {
            throw new RuntimeException("记录不存在: " + id);
        }
        return RecordResponse.fromEntity(record);
    }

    @Transactional
    public RecordResponse createRecord(RecordRequest request) {
        if (request.getId() == null || request.getId().isEmpty()) {
            request.setId(UUID.randomUUID().toString());
        }

        Record record = Record.builder()
                .id(request.getId())
                .userInfo(request.getUserInfo() != null ? request.getUserInfo() : Map.of())
                .text(request.getText() != null ? request.getText() : "")
                .images(request.getImages() != null ? request.getImages() : List.of())
                .videos(request.getVideos() != null ? request.getVideos() : List.of())
                .hasAudio(request.getHasAudio() != null ? request.getHasAudio() : false)
                .audioData(request.getAudioData())
                .audioMime(request.getAudioMime() != null ? request.getAudioMime() : "")
                .audioDuration(request.getAudioDuration() != null ? request.getAudioDuration() : "")
                .createdAt(OffsetDateTime.now())
                .build();

        recordMapper.insert(record);
        return RecordResponse.fromEntity(record);
    }

    @Transactional
    public RecordResponse updateRecord(String id, RecordRequest request) {
        Record existing = recordMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("记录不存在: " + id);
        }

        if (request.getUserInfo() != null) existing.setUserInfo(request.getUserInfo());
        if (request.getText() != null) existing.setText(request.getText());
        if (request.getImages() != null) existing.setImages(request.getImages());
        if (request.getVideos() != null) existing.setVideos(request.getVideos());
        if (request.getHasAudio() != null) existing.setHasAudio(request.getHasAudio());
        if (request.getAudioData() != null) existing.setAudioData(request.getAudioData());
        if (request.getAudioMime() != null) existing.setAudioMime(request.getAudioMime());
        if (request.getAudioDuration() != null) existing.setAudioDuration(request.getAudioDuration());

        recordMapper.updateById(existing);
        return RecordResponse.fromEntity(existing);
    }

    @Transactional
    public void deleteRecord(String id) {
        Record existing = recordMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("记录不存在: " + id);
        }
        recordMapper.deleteById(id);
    }

    public List<RecordResponse> searchByText(String text) {
        return recordMapper.selectByTextContaining(text).stream()
                .map(RecordResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RecordResponse> getRecordsWithAudio() {
        return recordMapper.selectRecordsWithAudio().stream()
                .map(RecordResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RecordResponse> getRecordsByDateRange(OffsetDateTime start, OffsetDateTime end) {
        return recordMapper.selectByDateRange(start, end).stream()
                .map(RecordResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
