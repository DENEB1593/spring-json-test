package io.study.deneb.model;

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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class AppearanceTest {

  @Autowired
  ApplicationContext context;

  @Autowired
  JacksonTester<Appearance> jacksonTester;

  @Autowired
  JacksonTester<List<Appearance>> jacksonListTester;


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

  @Test
  void serializationListJsonTestWithJsonPath() throws IOException {
    var appearance = new Appearance(
      "cd4b2704-bd92-405e-a526-39b740e90010",
      "json data 1",
      "this is my description",
      LocalDate.of(2023, 1, 1),
      LocalDate.of(2023, 1, 10),
      Type.CONFERENCE,
      List.of("apple", "banana"),
      "www.google.com"
    );

    var appearance2 = new Appearance(
      "cd4b2704-bd92-405e-a526-39b740e90010",
      "json data 2",
      "hello everyone",
      LocalDate.of(2023, 1, 11),
      LocalDate.of(2023, 1, 20),
      Type.CONFERENCE,
      List.of("strawberry", "kiwi"),
      "www.naver.com"
    );

    List<Appearance> appearances = List.of(appearance, appearance2);

    JsonContent<List<Appearance>> json = jacksonListTester.write(appearances);

    assertThat(json).extractingJsonPathValue("$.length()").isEqualTo(appearances.size());

  }

}