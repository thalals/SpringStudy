FROM openjdk:17

  # 애플리케이션 코드를 컨테이너 내부에 복사할 디렉토리 설정
RUN mkdir /root/application
WORKDIR /root/application

  # 호스트 시스템에서 애플리케이션의 jar 파일을 컨테이너의 /app 디렉토리로 복사
COPY build/libs/shedlock-0.0.1-SNAPSHOT.jar /root/application/app.jar
COPY pinpoint-agent-3.0.0.tar.gz /root/application/pinpoint-agent-3.0.0.tar.gz

RUN tar -xvf /root/application/pinpoint-agent-3.0.0.tar.gz
  
  # Update the Pinpoint configuration
RUN sed -i 's/profiler.transport.grpc.collector.ip=127.0.0.1/profiler.transport.grpc.collector.ip=172.24.0.10/g' /root/application/pinpoint-agent-3.0.0/pinpoint-root.config
RUN sed -i 's/profiler.collector.ip=127.0.0.1/profiler.collector.ip=172.24.0.10/g' /root/application/pinpoint-agent-3.0.0/pinpoint-root.config
  
  # 노출시킬 포트 설정
EXPOSE 9000

RUN chmod a+x "/root/application"

  # Run the application
ENTRYPOINT ["java","-jar", \
"-javaagent:/root/application/pinpoint-agent-3.0.0/pinpoint-bootstrap-3.0.0.jar", \
"-Dpinpoint.applicationName={앱 이름}", \
"-Dpinpoint.config=/root/application/pinpoint-agent-3.0.0/pinpoint-root.config", \
"/root/application/app.jar"]