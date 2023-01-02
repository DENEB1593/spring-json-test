package io.study.deneb.model;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@JsonTest
class AppearanceTest {

  @Autowired
  ApplicationContext context;

  @Autowired
  JacksonTester<Appearance> jacksonTester;


  @BeforeEach
  void setUp() {
    Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
  }

  @Test
  void contextLoads() {
    assertThat(context).isNotNull();
    assertThat(jacksonTester).isNotNull();
  }

  @Test
  void serializationJsonTestWithJsonPath() throws IOException {
    var id = UUID.randomUUID().toString();
    var appearance = new Appearance(
      id,
      "sometitle",
      "this is my description",
      LocalDate.of(2023, 01, 1),
      LocalDate.of(2023, 01, 10),
      Type.CONFERENCE,
      List.of("apple", "banana"),
      "www.google.com"
    );

    JsonContent<Appearance> json = jacksonTester.write(appearance);

    assertThat(json).extractingJsonPathValue("$.id").isEqualTo(id);
    assertThat(json).extractingJsonPathValue("$.title").isEqualTo(appearance.getTitle());
    assertThat(json).extractingJsonPathValue("$.description").isEqualTo(appearance.getDescription());
    assertThat(json).extractingJsonPathValue("$.startDate").isEqualTo(appearance.getStartDate().toString());
    assertThat(json).extractingJsonPathValue("$.endDate").isEqualTo(appearance.getEndDate().toString());
    assertThat(json).extractingJsonPathValue("$.type").isEqualTo(Type.CONFERENCE.toString());
    assertThat(json).extractingJsonPathValue("$.tags").isEqualTo(appearance.getTags());
    assertThat(json).extractingJsonPathValue("$.url").isEqualTo(appearance.getUrl());
  }

  @Test
  void serializationJsonTestWithJsonFile() throws IOException {

    var appearance = new Appearance(
      "cd4b2704-bd92-405e-a526-39b740e90008",
      "sometitle",
      "this is my description",
      LocalDate.of(2023, 1, 1),
      LocalDate.of(2023, 1, 10),
      Type.CONFERENCE,
      List.of("apple", "banana"),
      "www.google.com"
    );

    JsonContent<Appearance> json = jacksonTester.write(appearance);

    assertThat(json).isEqualToJson(new ClassPathResource("appearance.json"));

  }

}