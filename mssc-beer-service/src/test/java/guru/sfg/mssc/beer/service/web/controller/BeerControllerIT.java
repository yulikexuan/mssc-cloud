//: guru.sfg.mssc.beer.service.web.controller.BeerControllerIT.java


package guru.sfg.mssc.beer.service.web.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import guru.sfg.mssc.beer.service.domain.services.BeerService;
import guru.sfg.mssc.beer.service.web.model.BeerDto;
import guru.sfg.mssc.beer.service.web.model.BeerStyleEnum;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.UnaryOperator;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BeerController.class)
@DisplayName("Beer's Controller Test - ")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BeerControllerIT {

    static final String BEER_UPC = "0631234200036";

    static final String REQUEST_MAPPING = "/api/v1/beer";

    private String uuid;
    private String name;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BeerService beerService;

    private UnaryOperator<String> uriFunc = uuid ->
            String.format("%s/%s", REQUEST_MAPPING, uuid);

    private BeerDto dto;

    @BeforeEach
    void setUp() {
        this.uuid = UUID.randomUUID().toString();
        this.name = RandomStringUtils.randomAlphabetic(10);
        this.dto = BeerDto.builder()
                .beerName(this.name)
                .beerStyle(BeerStyleEnum.STOUT)
                .upc(BEER_UPC)
                .price(BigDecimal.valueOf(12.00))
                .build();
    }

    @Test
    void test_Given_An_Id_When_Get_Beer_By_Id_Then_Get_200() throws Exception {

        // Given // When & Then
        this.mockMvc.perform(get(uriFunc.apply(this.uuid))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test_Given_An_Beer_Obj_When_Save_Then_Get_Beer_Created()
            throws Exception {

        // Given
        String beerDtoJson = this.objectMapper.writeValueAsString(this.dto);

        // When & Then
        this.mockMvc.perform(
                post(REQUEST_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isCreated());
    }

    @Test
    void test_Given_An_Beer_Obj_With_UUID_When_Update_Then_Get_Beer_Updated()
            throws Exception {

        // Given
        String beerDtoJson = this.objectMapper.writeValueAsString(this.dto);

        // When & Then
        this.mockMvc
                .perform(put(uriFunc.apply(this.uuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_Given_An_Invalid_Beer_When_Update_Then_Get_List_Of_Error_Messages()
            throws Exception {

        // Given
        BeerDto beerDto = BeerDto.builder()
                .id(UUID.randomUUID())
                .build();
        String beerDtoJson = this.objectMapper.writeValueAsString(beerDto);

        // When
        this.mockMvc.perform(put(uriFunc.apply(this.uuid))
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        containsString("id must be null")))
                .andExpect(content().string(
                        containsString("beerName must not be blank")))
                .andExpect(content().string(
                        containsString("beerStyle must not be null")))
                .andExpect(content().string(
                        containsString("upc must not be blank")));
    }

}///:~