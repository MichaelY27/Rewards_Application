package com.example.reward.controller;

import com.example.reward.domain.dto.CreateTransactionDTO;
import com.example.reward.domain.dto.ResponseDTO;
import com.example.reward.domain.dto.UpdateTransactionDTO;
import com.example.reward.service.impl.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.List;

@Validated
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get a transaction information by transaction id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = ResponseDTO.class)))
            }
    )
    public ResponseEntity<ResponseDTO> getTransactionById(@PathVariable Long transactionId) {

        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Success").message(transactionService.getTransactionById(transactionId)).build(), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all transactions by user id")
    @ApiResponse(responseCode = "200", description = "All transactions retrieved successfully")
    public ResponseEntity<ResponseDTO> getAllTransactions(@PathVariable Long userId) {

        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Success").message(transactionService.getTransactionByUserId(userId)).build(), HttpStatus.OK);

    }

    @PostMapping("/single")
    @Operation(summary = "Create a new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction created successfully", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))
            }
    )
    public ResponseEntity<ResponseDTO> createTransaction(@Valid @RequestBody CreateTransactionDTO createTransactionDTO) {
        transactionService.createTransaction(createTransactionDTO);
        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Success").message("Transaction created successfully").build(), HttpStatus.OK);
    }

    @PostMapping("/many")
    @Operation(summary = "Create list of new transactions, return generated transaction ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction created successfully", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))
    }
    )
    public ResponseEntity<ResponseDTO> createTransactionMany(@NotEmpty(message = "transaction list can't be empty") @RequestBody List<@Valid CreateTransactionDTO> createTransactionDTOList) {
        transactionService.createTransactionList(createTransactionDTOList);
        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Success").message("Transaction list created successfully").build(), HttpStatus.OK);
    }


    @PatchMapping("/single")
    @Operation(summary = "Partially update a transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Content", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))
            }
    )
    public ResponseEntity<ResponseDTO> updateTransaction(@Valid @RequestBody UpdateTransactionDTO updateTransactionDTO) {
        transactionService.updateTransaction(updateTransactionDTO);
        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Success").message("Transaction updated successfully").build(), HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}")
    @Operation(summary = "Delete a transaction by transaction id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Content", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))
            }
    )
    public ResponseEntity<ResponseDTO> deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Success").message("Transaction deleted successfully").build(), HttpStatus.OK);
    }



}
