package com.example.reward;

import com.example.reward.domain.dto.CreateTransactionDTO;
import com.example.reward.domain.dto.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = RewardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test createTransaction method
     * The response should be a success message
     */
    @Test
    void testCreateTransaction() {
        CreateTransactionDTO createTransactionDTO = new CreateTransactionDTO(1L, BigDecimal.valueOf(100), LocalDateTime.now());
        ResponseEntity<ResponseDTO> responseEntity = this.restTemplate
            .postForEntity("http://localhost:" + port + "/transaction/single", createTransactionDTO, ResponseDTO.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Success", responseEntity.getBody().getMessageType());
    }

    /**
     * Test getTransactionById method
     * User with id 1 should exist
     * The response should be a success message
     */
    @Test
    @Sql({"/testdata.sql"})
    void testGetTransactionByIdSuccess() {
        ResponseEntity<ResponseDTO> responseEntity = this.restTemplate
            .getForEntity("http://localhost:" + port + "/transaction/1", ResponseDTO.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Success", responseEntity.getBody().getMessageType());
    }

    /**
     * Test getTransactionById method
     * User with id 3 should not exist
     * The response should be Error message
     * The status code should be 404
     */
    @Test
    @Sql({"/testdata.sql"})
    void testGetTransactionByIdFail() {
        ResponseEntity<ResponseDTO> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/transaction/3", ResponseDTO.class);
        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals("Error", responseEntity.getBody().getMessageType());
    }

    /**
     * Test getAllTransactions method
     * User with id 1 should exist
     * The response should be a success message
     */
    @Test
    @Sql({"/testdata.sql"})
    void testDeleteTransactionSuccess() {
        ResponseEntity<ResponseDTO> responseEntity = this.restTemplate
                .exchange("http://localhost:" + port + "/transaction/1", HttpMethod.DELETE, null, ResponseDTO.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Success", responseEntity.getBody().getMessageType());
    }

    /**
     * Test getAllTransactions method
     * User with id 3 should not exist
     * The response should be Error message
     * The status code should be 422
     */
    @Test
    @Sql({"/testdata.sql"})
    void testDeleteTransactionFail() {
        ResponseEntity<ResponseDTO> responseEntity = this.restTemplate
                .exchange("http://localhost:" + port + "/transaction/3", HttpMethod.DELETE, null, ResponseDTO.class);
        assertEquals(422, responseEntity.getStatusCodeValue());
        assertEquals("Error", responseEntity.getBody().getMessageType());
    }


}
