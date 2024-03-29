package fr.croixrouge.exposition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.croixrouge.config.InDBMockRepositoryConfig;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.exposition.dto.core.LoginRequest;
import fr.croixrouge.exposition.dto.product.CreateFoodProductDTO;
import fr.croixrouge.exposition.dto.product.FoodProductResponse;
import fr.croixrouge.exposition.dto.product.FoodStorageProductResponse;
import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.quantifier.WeightUnit;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import({InDBMockRepositoryConfig.class})
class FoodProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    private static String createdFoodProductId;

    private ZonedDateTime timestampToLocalDateTime(Timestamp timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneId.of("Europe/Paris"));
    }

    @BeforeEach
    public void setUp() throws Exception {
        LoginRequest loginRequest = new LoginRequest("defaultUser", "defaultPassword");

        String result = mockMvc.perform(post("/login/volunteer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(result).get("jwtToken").asText();
    }

    @Test
    @Order(1)
    @DisplayName("Test that the endpoint returns a food product when given a correct id")
    public void productIdSuccessTest() throws Exception {
        final LocalDate date = LocalDate.now();

        mockMvc.perform(get("/product/food/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Pommes"))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(WeightUnit.KILOGRAM.getName()))
                .andExpect(jsonPath("$.quantity.value").value(1))
                .andExpect(jsonPath("$.foodConservation").value("température ambiante"))
                .andExpect(jsonPath("$.expirationDate").value(ZonedDateTime.of(LocalDateTime.of(date.plusMonths(2).getYear(), date.plusMonths(2).getMonthValue(), date.plusMonths(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")).toString()))
                .andExpect(jsonPath("$.optimalConsumptionDate").value(ZonedDateTime.of(LocalDateTime.of(date.plusMonths(1).getYear(), date.plusMonths(1).getMonthValue(), date.plusMonths(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")).toString()));
    }

    @Test
    @Order(2)
    @DisplayName("Test that the endpoint returns a 404 when given a incorrect food product id")
    public void productIdFailedTest() throws Exception {
        mockMvc.perform(get("/product/food/-1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    @DisplayName("Test that the get endpoint returns a list of food product")
    public void testGetAllFoodSuccessTest() throws Exception {
        final LocalDate date = LocalDate.now();

        FoodProductResponse foodProductResponse1 = new FoodProductResponse(
                1L,
                8L,
                "Pommes",
                new QuantifierDTO(WeightUnit.KILOGRAM.getName(), 1),
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(2).getYear(), date.plusMonths(2).getMonthValue(), date.plusMonths(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(1).getYear(), date.plusMonths(1).getMonthValue(), date.plusMonths(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                1L
        );
        FoodProductResponse foodProductResponse2 = new FoodProductResponse(
                2L,
                9L,
                "Pates",
                new QuantifierDTO(WeightUnit.KILOGRAM.getName(), 1),
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(5).getYear(), date.plusDays(5).getMonthValue(), date.plusDays(5).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                1L
        );

        mockMvc.perform(get("/product/food")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(foodProductResponse1.getName()))
                .andExpect(jsonPath("$[0].quantity.measurementUnit").value(foodProductResponse1.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[0].quantity.value").value(foodProductResponse1.getQuantity().getValue()))
                .andExpect(jsonPath("$[0].foodConservation").value(foodProductResponse1.getFoodConservation()))
                .andExpect(jsonPath("$[0].expirationDate").value(foodProductResponse1.getExpirationDate()))
                .andExpect(jsonPath("$[0].optimalConsumptionDate").value(foodProductResponse1.getOptimalConsumptionDate()))
                .andExpect(jsonPath("$[0].price").value(foodProductResponse1.getPrice()))
                .andExpect(jsonPath("$[1].name").value(foodProductResponse2.getName()))
                .andExpect(jsonPath("$[1].quantity.measurementUnit").value(foodProductResponse2.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$[1].quantity.value").value(foodProductResponse2.getQuantity().getValue()))
                .andExpect(jsonPath("$[1].foodConservation").value(foodProductResponse2.getFoodConservation()))
                .andExpect(jsonPath("$[1].expirationDate").value(foodProductResponse2.getExpirationDate()))
                .andExpect(jsonPath("$[1].optimalConsumptionDate").value(foodProductResponse2.getOptimalConsumptionDate()))
                .andExpect(jsonPath("$[1].price").value(foodProductResponse2.getPrice()));
    }

    @Test
    @Order(4)
    @DisplayName("Test that the post endpoint, used to create a new food product, returns OK when given a correct food product")
    public void productAddSuccessTest() throws Exception {
        final LocalDate date = LocalDate.now();
        CreateFoodProductDTO createProductDTO = new CreateFoodProductDTO("new Product",
                new QuantifierDTO(WeightUnit.KILOGRAM.getName(), 1),
                null, FoodConservation.ROOM_TEMPERATURE.getLabel(),
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(date.plusMonths(3).getYear(), date.plusMonths(3).getMonthValue(), date.plusMonths(3).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(date.plusMonths(2).getYear(), date.plusMonths(2).getMonthValue(), date.plusMonths(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                1,
                "1",
                10);

        var res = mockMvc.perform(post("/product/food")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        createdFoodProductId = objectMapper.readTree(res).get("value").asText();

        mockMvc.perform(get("/product/food/" + createdFoodProductId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(createProductDTO.getName()))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(createProductDTO.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$.quantity.value").value(createProductDTO.getQuantity().getValue()))
                .andExpect(jsonPath("$.foodConservation").value(createProductDTO.getFoodConservation()))
                .andExpect(jsonPath("$.expirationDate").value(timestampToLocalDateTime(Timestamp.valueOf(createProductDTO.getExpirationDate().toLocalDateTime())).toString()))
                .andExpect(jsonPath("$.optimalConsumptionDate").value(timestampToLocalDateTime(Timestamp.valueOf(createProductDTO.getOptimalConsumptionDate().toLocalDateTime())).toString()));
    }

    @Test
    @Order(5)
    @DisplayName("Test that the put endpoint, used to update a food product, returns OK when given a correct food product")
    public void testUpdateFoodByIdSuccessTest() throws Exception {
        final LocalDate date = LocalDate.now();

        FoodProductResponse foodProductResponse = new FoodProductResponse(
                1L,
                8L,
                "Pommes",
                new QuantifierDTO(WeightUnit.KILOGRAM.getName(), 1),
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(2).getYear(), date.plusMonths(2).getMonthValue(), date.plusMonths(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.plusMonths(1).getYear(), date.plusMonths(1).getMonthValue(), date.plusMonths(1).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                1L
        );
        mockMvc.perform(get("/product/food/" + foodProductResponse.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(foodProductResponse.getName()))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(foodProductResponse.getQuantity().getMeasurementUnit()))
                .andExpect(jsonPath("$.quantity.value").value(foodProductResponse.getQuantity().getValue()))
                .andExpect(jsonPath("$.foodConservation").value(foodProductResponse.getFoodConservation()))
                .andExpect(jsonPath("$.expirationDate").value(foodProductResponse.getExpirationDate()))
                .andExpect(jsonPath("$.optimalConsumptionDate").value(foodProductResponse.getOptimalConsumptionDate()))
                .andExpect(jsonPath("$.price").value(foodProductResponse.getPrice()));

        CreateFoodProductDTO createProductDTO = new CreateFoodProductDTO(
                "Pommes vertes",
                new QuantifierDTO(WeightUnit.GRAM.getName(), 950),
                null, FoodConservation.ROOM_TEMPERATURE.getLabel(),
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(date.plusMonths(3).getYear(), date.plusMonths(3).getMonthValue(), date.plusMonths(3).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.of(date.plusMonths(2).getYear(), date.plusMonths(2).getMonthValue(), date.plusMonths(2).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")).toLocalDateTime()),
                43,
                "1",
                10);

        mockMvc.perform(post("/product/food/" + foodProductResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(createProductDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/product/food/" + foodProductResponse.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(foodProductResponse.getId()))
                .andExpect(jsonPath("$.name").value("Pommes vertes"))
                .andExpect(jsonPath("$.quantity.measurementUnit").value(WeightUnit.GRAM.getName()))
                .andExpect(jsonPath("$.quantity.value").value(950))
                .andExpect(jsonPath("$.foodConservation").value(foodProductResponse.getFoodConservation()))
                .andExpect(jsonPath("$.expirationDate").value(timestampToLocalDateTime(createProductDTO.getExpirationDate()).toString()))
                .andExpect(jsonPath("$.optimalConsumptionDate").value(timestampToLocalDateTime(createProductDTO.getOptimalConsumptionDate()).toString()));
    }

    @Test
    @Order(6)
    @DisplayName("Test that the delete endpoint returns OK when given a correct food product id")
    public void productDeleteSuccessTest() throws Exception {
        mockMvc.perform(get("/product/food/" + createdFoodProductId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/product/food/" + createdFoodProductId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/product/food/" + createdFoodProductId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    @DisplayName("Test that the get expired endpoint returns a list of soon expired food product")
    public void testGetAllExpiredFoodSuccessTest() throws Exception {
        final LocalDate date = LocalDate.now();

        FoodStorageProductResponse foodStorageProductResponse = new FoodStorageProductResponse(
                2L,
                9L,
                1L,
                1L,
                "Pates",
                10,
                "1.0",
                WeightUnit.KILOGRAM.getName(),
                FoodConservation.ROOM_TEMPERATURE,
                ZonedDateTime.of(LocalDateTime.of(date.plusDays(5).getYear(), date.plusDays(5).getMonthValue(), date.plusDays(5).getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                ZonedDateTime.of(LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0), ZoneId.of("Europe/Paris")),
                1L,
                null
        );

        mockMvc.perform(get("/product/food/expired")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value(foodStorageProductResponse.getProductName()))
                .andExpect(jsonPath("$[0].quantity").value(foodStorageProductResponse.getQuantity()))
                .andExpect(jsonPath("$[0].quantifierQuantity").value(foodStorageProductResponse.getQuantifierQuantity()))
                .andExpect(jsonPath("$[0].quantifierName").value(foodStorageProductResponse.getQuantifierName()))
                .andExpect(jsonPath("$[0].foodConservation").value(foodStorageProductResponse.getFoodConservation()))
                .andExpect(jsonPath("$[0].expirationDate").value(foodStorageProductResponse.getExpirationDate()))
                .andExpect(jsonPath("$[0].optimalConsumptionDate").value(foodStorageProductResponse.getOptimalConsumptionDate()))
                .andExpect(jsonPath("$[0].price").value(foodStorageProductResponse.getPrice()));
    }

}
