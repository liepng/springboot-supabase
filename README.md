# Daka Pro - Spring Boot + Supabase

## 项目结构

```
daka-pro/
├── pom.xml                              # Maven配置
├── src/main/java/com/daka/
│   ├── DakaProApplication.java          # 启动类
│   ├── entity/Record.java               # 实体类 (对应records表)
│   ├── repository/RecordRepository.java # 数据访问层
│   ├── service/RecordService.java       # 业务逻辑层
│   ├── controller/RecordController.java # REST API控制器
│   ├── dto/                             # 数据传输对象
│   │   ├── RecordRequest.java           # 请求DTO
│   │   └── RecordResponse.java          # 响应DTO
│   ├── config/AppConfig.java            # 配置类 (CORS、Jackson等)
│   └── exception/
│       └── GlobalExceptionHandler.java  # 全局异常处理
├── src/main/resources/
│   └── application.properties           # 应用配置 (含Supabase连接)
└── src/test/java/                       # 测试目录
```

## 配置Supabase连接

1. 登录 [Supabase Dashboard](https://supabase.com/dashboard)
2. 进入项目 → Settings → Database
3. 找到 **Connection string** → **JDBC** 格式
4. 复制 `application.properties` 中的连接信息：

```properties
# 替换为你的Supabase实际连接信息
spring.datasource.url=jdbc:postgresql://db.xxxx.supabase.co:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=你的真实密码
```

5. 在 Supabase SQL Editor 中执行建表语句创建 `records` 表

## 启动项目

```bash
# 使用Maven启动
mvn spring-boot:run

# 或打包后运行
mvn clean package -DskipTests
java -jar target/daka-pro-1.0.0.jar
```

## API接口列表

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/records` | 获取所有记录（按时间倒序）|
| GET | `/api/records/page` | 分页查询 (?page=0&size=10) |
| GET | `/api/records/{id}` | 根据ID获取记录 |
| POST | `/api/records` | 创建新记录 |
| PUT | `/api/records/{id}` | 更新记录 |
| DELETE | `/api/records/{id}` | 删除记录 |
| GET | `/api/records/search?text=xxx` | 按文本搜索 |
| GET | `/api/records/with-audio` | 获取带音频的记录 |
| GET | `/api/records/date-range?start=&end=` | 按时间范围查询 |

## 示例请求

### 创建记录

POST /api/records
```json
{
  "userInfo": {"name": "张三"},
  "text": "今日打卡完成",
  "images": ["https://example.com/img.jpg"],
  "videos": [],
  "hasAudio": false,
  "audioMime": "",
  "audioDuration": ""
}
```
