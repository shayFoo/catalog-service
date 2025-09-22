FROM amazoncorretto:25-al2023 AS builder
WORKDIR /workspace

COPY . .

RUN yum install -y findutils

RUN ./gradlew bootJar
ARG JAR_FILE=build/libs/*.jar
# https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM amazoncorretto:25-al2023 AS runtime

RUN yum install -y shadow-utils

# タイムゾーンをAsia/Tokyoに設定
RUN ln -sf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime
RUN echo "Asia/Tokyo" > /etc/timezone
ENV LANG=ja_JP.UTF-8
ENV LANGUAGE=ja_JP:ja
ENV LC_ALL=ja_JP.UTF-8

# appuserの作成
RUN groupadd -g 1000 appuser && useradd -m -u 1000 -g 1000 appuser

# 作成したappuserに切り替え
USER appuser
WORKDIR /home/appuser

# Copy the layers from the builder stage
COPY --chown=1000:1000 --from=builder /workspace/extracted/dependencies/ ./
COPY --chown=1000:1000 --from=builder /workspace/extracted/spring-boot-loader/ ./
COPY --chown=1000:1000 --from=builder /workspace/extracted/snapshot-dependencies/ ./
COPY --chown=1000:1000 --from=builder /workspace/extracted/application/ ./

ENTRYPOINT ["java",  "-jar", "application.jar"]