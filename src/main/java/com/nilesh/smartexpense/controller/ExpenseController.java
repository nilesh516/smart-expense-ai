package com.nilesh.smartexpense.controller;

import com.nilesh.smartexpense.entity.Expense;
import com.nilesh.smartexpense.model.ExpenseRequest;
import com.nilesh.smartexpense.model.ExpenseResponse;
import com.nilesh.smartexpense.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
@Tag(name = "Expense API", description = "AI-powered expense scanning and management")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Operation(summary = "Scan receipt image", description = "Upload a receipt image — Claude will extract and save expense data automatically")
    @PostMapping("/scan")
    public ResponseEntity<ExpenseResponse> scanReceipt(
            @RequestParam("file") MultipartFile file) throws Exception {

        String base64Image = Base64.getEncoder()
                .encodeToString(file.getBytes());
        String imageType = file.getContentType() != null ?
                file.getContentType().replace("image/", "") : "jpeg";

        ExpenseRequest request = new ExpenseRequest();
        request.setImageBase64(base64Image);
        request.setImageType(imageType);

        return ResponseEntity.ok(expenseService.scanAndSave(request));
    }

    @Operation(summary = "Scan receipt via text", description = "Describe an expense in text — Claude will categorize and save it")
    @PostMapping("/scan-text")
    public ResponseEntity<ExpenseResponse> scanReceiptText(
            @RequestBody ExpenseRequest request) throws Exception {
        return ResponseEntity.ok(expenseService.scanAndSave(request));
    }

    @Operation(summary = "Get all expenses", description = "Retrieve all saved expenses from the database")
    @GetMapping("/all")
    public ResponseEntity<List<Expense>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @Operation(summary = "Get expense by ID", description = "Retrieve a specific expense by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete expense", description = "Delete a specific expense by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Expense deleted successfully");
    }
}