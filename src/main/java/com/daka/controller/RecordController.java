package com.daka.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.daka.dto.RecordRequest;
import com.daka.dto.RecordResponse;
import com.daka.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "记录管理", description = "打卡记录的增删改查接口")
public class RecordController {

    private final RecordService recordService;

    @GetMapping
    @Operation(summary = "获取所有记录", description = "获取所有打卡记录，按创建时间倒序排列")
    public ResponseEntity<List<RecordResponse>> getAllRecords() {
        return ResponseEntity.ok(recordService.getAllRecords());
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询记录", description = "分页获取打卡记录，支持指定页码和每页大小")
    public ResponseEntity<Map<String, Object>> getRecordsPaginated(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        IPage<RecordResponse> result = recordService.getRecordsPaginated(page, size);
        return ResponseEntity.ok(Map.of(
                "content", result.getRecords(),
                "totalElements", result.getTotal(),
                "totalPages", result.getPages(),
                "currentPage", result.getCurrent(),
                "size", result.getSize()
        ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取记录", description = "通过记录ID获取单条打卡记录详情")
    public ResponseEntity<RecordResponse> getRecordById(
            @Parameter(description = "记录ID", required = true) @PathVariable String id) {
        return ResponseEntity.ok(recordService.getRecordById(id));
    }

    @PostMapping
    @Operation(summary = "创建记录", description = "新建一条打卡记录，ID可选，不传则自动生成UUID")
    public ResponseEntity<RecordResponse> createRecord(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "记录请求体", required = true)
            @RequestBody RecordRequest request) {
        return ResponseEntity.ok(recordService.createRecord(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新记录", description = "根据ID更新已有打卡记录，只更新传入的非空字段")
    public ResponseEntity<RecordResponse> updateRecord(
            @Parameter(description = "记录ID", required = true) @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "记录请求体", required = true)
            @RequestBody RecordRequest request) {
        return ResponseEntity.ok(recordService.updateRecord(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除记录", description = "根据ID删除一条打卡记录")
    public ResponseEntity<Void> deleteRecord(
            @Parameter(description = "记录ID", required = true) @PathVariable String id) {
        recordService.deleteRecord(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(summary = "文本搜索记录", description = "通过文本内容模糊搜索打卡记录")
    public ResponseEntity<List<RecordResponse>> searchByText(
            @Parameter(description = "搜索关键词", required = true) @RequestParam String text) {
        return ResponseEntity.ok(recordService.searchByText(text));
    }

    @GetMapping("/with-audio")
    @Operation(summary = "获取带音频的记录", description = "获取所有包含音频数据的打卡记录")
    public ResponseEntity<List<RecordResponse>> getRecordsWithAudio() {
        return ResponseEntity.ok(recordService.getRecordsWithAudio());
    }

    @GetMapping("/date-range")
    @Operation(summary = "按时间范围查询", description = "根据起止时间范围查询打卡记录，时间格式为ISO-8601（如：2024-01-01T00:00:00+08:00）")
    public ResponseEntity<List<RecordResponse>> getByDateRange(
            @Parameter(description = "开始时间", required = true) @RequestParam String start,
            @Parameter(description = "结束时间", required = true) @RequestParam String end) {
        OffsetDateTime startTime = OffsetDateTime.parse(start);
        OffsetDateTime endTime = OffsetDateTime.parse(end);
        return ResponseEntity.ok(recordService.getRecordsByDateRange(startTime, endTime));
    }
}
