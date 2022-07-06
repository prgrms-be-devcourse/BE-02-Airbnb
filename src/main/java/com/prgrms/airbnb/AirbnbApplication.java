package com.prgrms.airbnb;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AirbnbApplication {

  public static void main(String[] args) {
    SpringApplication.run(AirbnbApplication.class, args);
  }

  @PostConstruct
  public void started() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }
}
