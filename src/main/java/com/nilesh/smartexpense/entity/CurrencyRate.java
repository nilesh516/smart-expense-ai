package com.nilesh.smartexpense.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity caching exchange rates between currency pairs.
 * Rates are fetched from an external API and cached here
 * to avoid hitting rate limits on every expense conversion.
 *
 * Rates should be refreshed every 24 hours via a scheduled job.
 * When converting, always fetch the rate with the most recent fetchedAt.
 *
 * Example: fromCurrency=USD, toCurrency=INR, rate=83.45
 * means 1 USD = 83.45 INR
 */
@Entity
@Table(name = "currency_rates")
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ISO 4217 source currency e.g. USD
    @Column(name = "from_currency", nullable = false, length = 3)
    private String fromCurrency;

    // ISO 4217 target currency e.g. INR
    @Column(name = "to_currency", nullable = false, length = 3)
    private String toCurrency;

    // Exchange rate: 1 unit of fromCurrency = rate units of toCurrency
    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal rate;

    // Timestamp when this rate was fetched from the external API
    @Column(name = "fetched_at", nullable = false)
    private LocalDateTime fetchedAt;

    @PrePersist
    public void prePersist() {
        this.fetchedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFromCurrency() { return fromCurrency; }
    public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }

    public String getToCurrency() { return toCurrency; }
    public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }

    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }

    public LocalDateTime getFetchedAt() { return fetchedAt; }
    public void setFetchedAt(LocalDateTime fetchedAt) { this.fetchedAt = fetchedAt; }
}
