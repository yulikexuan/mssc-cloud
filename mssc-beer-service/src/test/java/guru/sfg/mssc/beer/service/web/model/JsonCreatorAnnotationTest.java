//: guru.sfg.mssc.beer.service.web.model.JsonCreatorAnnotationTest.java


package guru.sfg.mssc.beer.service.web.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Jackson JsonCreator Annotation Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JsonCreatorAnnotationTest {

    static class BeanWithCreator {

        private final UUID id;
        private final String name;

        @JsonCreator
        public BeanWithCreator(
                @JsonProperty("id") UUID id,
                @JsonProperty("beanName") String name) {
            this.id = id;
            this.name = name;
        }

        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    void test_Given_Json_String_When_Deserializing_With_JsonCreator_Then_Correct_Property_Name() throws IOException {

        // Given
        String jsonUUID = UUID.randomUUID().toString();
        String name = RandomStringUtils.randomAlphanumeric(30);
        String json = String.format("{\"id\":\"%s\",\"beanName\":\"%s\"}", jsonUUID, name);

        // When
        BeanWithCreator bwc = new ObjectMapper()
                .readerFor(BeanWithCreator.class)
                .readValue(json);

        // Then
        assertThat(bwc.id.toString()).isEqualTo(jsonUUID);
    }
}///:~