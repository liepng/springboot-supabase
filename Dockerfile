# 构建阶段 - 安装Maven并打包
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app

# 安装Maven
RUN apt-get update && \
    apt-get install -y --no-install-recommends maven && \
    rm -rf /var/lib/apt/lists/*

# 复制项目文件
COPY pom.xml .
COPY src ./src

# 下载依赖+构建（利用Docker缓存层）
RUN mvn dependency:go-offline -B || true
RUN mvn clean package -DskipTests -B

# 运行阶段
FROM openjdk:17-jre-slim AS runtime
WORKDIR /app

# JVM优化参数（Render免费实例内存有限）
ENV JAVA_OPTS="-Xms128m -Xmx384m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+ExitOnOutOfMemoryError"

# 健康检查路径（Render需要）
ENV MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info
ENV MANAGEMENT_ENDPOINT_HEALTH_SHOW-DETAILS=never

COPY --from=builder /app/target/*.jar app.jar

EXPOSE ${PORT:-10000}

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT:-10000}/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=${PORT:-10000}"]
