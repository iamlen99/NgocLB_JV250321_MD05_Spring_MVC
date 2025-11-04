# ========== STAGE 1: Build ứng dụng ==========
FROM gradle:8.7-jdk17 AS build
WORKDIR /home/gradle/project

# Copy toàn bộ mã nguồn vào container
COPY --chown=gradle:gradle . .

# Build project mà không dùng daemon (nhanh, nhẹ)
RUN gradle clean build -x test --no-daemon


# ========== STAGE 2: Chạy ứng dụng ==========
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy file jar từ stage build
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Mở cổng 8080 để Render truy cập
EXPOSE 8080

# Lệnh khởi động app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
