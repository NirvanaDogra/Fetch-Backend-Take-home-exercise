package com.nirvana.fetch_assessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  {"shortDescription": "Pepsi - 12-oz", "price": "1.25"},
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(hidden = true)
    private String id;

    @NotNull
    @NotEmpty
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
            description = "The Short Product Description for the item.",
            type = "^[\\\\w\\\\s\\\\-]+$",
            pattern = "^\\d+\\.\\d{2}$",
            example = "Mountain Dew 12PK")
    String shortDescription;

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
            description = "The total price payed for this item.",
            type = "string",
            pattern = "^\\\\d+\\\\.\\\\d{2}$",
            example = "6.49")
    Double price;
}
