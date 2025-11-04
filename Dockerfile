# 1. JDK 17을 기반으로 하는 이미지를 선택
FROM eclipse-temurin:17-jdk-jammy AS build

# 2. git 설치
RUN apt-get update && apt-get install -y git

# 3. 프로젝트 소스를 복사
WORKDIR /app
COPY . .

# 4. Gradle을 사용하여 빌드
RUN ./gradlew build --no-daemon

# 5. Spring Boot 실행을 위한 기본 이미지
FROM eclipse-temurin:17-jre-jammy

# 6. 애플리케이션 JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 7. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
