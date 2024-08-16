package com.nirvana.fetch_assessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * {
 * "retailer": "Walgreens",
 * "purchaseDate": "2022-01-02",
 * "purchaseTime": "08:13",
 * "total": "2.65",
 * "items": [
 * {"shortDescription": "Pepsi - 12-oz", "price": "1.25"},
 * {"shortDescription": "Dasani", "price": "1.40"}
 * ]
 * }
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @NotNull
    @NotEmpty
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
            description = "The name of the retailer or store the receipt is from.",
            type = "String",
            pattern = "^[\\w\\s\\-&]+$",
            example = "M&M Corner Market")
    private String retailer;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @PastOrPresent
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
            description = "The date of the purchase printed on the receipt.",
            type = "String",
            format = "yyyy-MM-dd",
            example = "2022-01-01")
    private Date purchaseDate;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
            description = "The time of the purchase printed on the receipt. 24-hour time expected.",
            type = "String",
            format = "HH:mm",
            example = "13:01")
    private LocalTime purchaseTime;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "purchase_id")
    @NotEmpty
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1)
    private List<Item> items;

    @NotNull
    @Min(0)
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
            description = "The total amount paid on the receipt.",
            type = "string",
            pattern = "^\\d+\\.\\d{2}$",
            example = "6.49")
    private Double total;
}
