-- ============================================================
-- Daka Pro 性能优化 - 数据库索引
-- 在 Supabase Dashboard → SQL Editor 中执行
-- ============================================================

-- 1. created_at 排序索引（最常用，所有列表查询都用到）
CREATE INDEX IF NOT EXISTS idx_records_created_at
    ON public.records (created_at DESC);

-- 2. has_audio 过滤索引（只索引 true 值，节省空间）
CREATE INDEX IF NOT EXISTS idx_records_has_audio
    ON public.records (has_audio)
    WHERE has_audio = true;

-- 3. text 字段全文搜索索引（使用 pg_trgm 支持高效模糊搜索）
-- 先启用扩展（Supabase 默认已安装）
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- 创建 GIN 索引加速 ILIKE 查询
CREATE INDEX IF NOT EXISTS idx_records_text_trgm
    ON public.records USING gin (text gin_trgm_ops);

-- 4. 复合索引：按时间范围 + 音频筛选
CREATE INDEX IF NOT EXISTS idx_records_created_at_has_audio
    ON public.records (created_at DESC, has_audio);

-- 5. 验证索引创建成功
SELECT indexname, tablename, indexdef
FROM pg_indexes
WHERE tablename = 'records'
ORDER BY indexname;
