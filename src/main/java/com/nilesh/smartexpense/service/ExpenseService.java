package com.nilesh.smartexpense.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nilesh.smartexpense.entity.Expense;
import com.nilesh.smartexpense.model.ExpenseRequest;
import com.nilesh.smartexpense.model.ExpenseResponse;
import com.nilesh.smartexpense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ExpenseService {

    @Value("${anthropic.api.key}")
    private String apiKey;

    @Autowired
    private ExpenseRepository expenseRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CLAUDE_API_URL = "https://api.anthropic.com/v1/messages";

    // Scan receipt and save to DB
    public ExpenseResponse scanAndSave(ExpenseRequest request) throws Exception {

        ExpenseResponse response = scanReceipt(request);

        // Save to database
        Expense expense = new Expense();
        expense.setVendor(response.getVendor());
        expense.setAmount(response.getAmount());
        expense.setDate(response.getDate());
        expense.setCategory(response.getCategory());
        expense.setPaymentMethod(response.getPaymentMethod());
        expense.setTaxAmount(response.getTaxAmount());
        expense.setCurrency(response.getCurrency());
        expense.setNotes(response.getNotes());

        expenseRepository.save(expense);

        return response;
    }

    // Get all expenses from DB
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // Get expense by ID
    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    // Delete expense
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    // Core Claude API call
    public ExpenseResponse scanReceipt(ExpenseRequest request) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        List<Map<String, Object>> content = new ArrayList<>();

        if (request.getImageBase64() != null && !request.getImageBase64().isEmpty()) {
            Map<String, Object> imageSource = new HashMap<>();
            imageSource.put("type", "base64");
            imageSource.put("media_type", "image/" + request.getImageType());
            imageSource.put("data", request.getImageBase64());

            Map<String, Object> imageBlock = new HashMap<>();
            imageBlock.put("type", "image");
            imageBlock.put("source", imageSource);
            content.add(imageBlock);
        }

        String prompt = """
                You are an intelligent expense receipt analyzer.
                Analyze the receipt and respond ONLY in this JSON format with no extra text:
                {
                  "vendor": "name of the shop or vendor",
                  "amount": total amount as number,
                  "date": "date in YYYY-MM-DD format",
                  "category": "Food & Dining or Travel or Shopping or Utilities or Healthcare or Entertainment or Other",
                  "paymentMethod": "Cash or UPI or Credit Card or Debit Card or Unknown",
                  "taxAmount": tax amount as number or 0,
                  "currency": "INR or USD or EUR etc",
                  "notes": "any important notes from the receipt"
                }
                """ +
                (request.getDescription() != null ?
                        "Additional info: " + request.getDescription() : "");

        Map<String, Object> textBlock = new HashMap<>();
        textBlock.put("type", "text");
        textBlock.put("text", prompt);
        content.add(textBlock);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", content);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "claude-opus-4-5");
        requestBody.put("max_tokens", 1000);
        requestBody.put("messages", List.of(message));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                CLAUDE_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        JsonNode root = objectMapper.readTree(response.getBody());
        String rawJson = root.path("content").get(0).path("text").asText();

        rawJson = rawJson.trim();
        if (rawJson.contains("{")) {
            rawJson = rawJson.substring(rawJson.indexOf("{"),
                    rawJson.lastIndexOf("}") + 1);
        }

        return objectMapper.readValue(rawJson, ExpenseResponse.class);
    }
}