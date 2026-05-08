package com.daka.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daka.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {

    @Select("SELECT * FROM records ORDER BY created_at DESC")
    List<Record> selectAllOrderByCreatedAtDesc();

    @Select("SELECT * FROM records WHERE text ILIKE CONCAT('%', #{text}, '%') LIMIT 100")
    List<Record> selectByTextContaining(@Param("text") String text);

    @Select("SELECT * FROM records WHERE has_audio = true ORDER BY created_at DESC")
    List<Record> selectRecordsWithAudio();

    @Select("SELECT * FROM records WHERE created_at >= #{start} AND created_at <= #{end} ORDER BY created_at DESC")
    List<Record> selectByDateRange(@Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end);
}
