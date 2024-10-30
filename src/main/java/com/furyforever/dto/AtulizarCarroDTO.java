package com.furyforever.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AtulizarCarroDTO {
    @NotBlank
    @Size(max = 17)
    private String chassi;

    @NotBlank
    @Size(max = 7)
    private String placa;

    @NotBlank
    @Size(max = 20)
    private String cor;

    @NotBlank
    @Size(max = 40)
    private String motor;

    @NotBlank
    @Size(max = 30)
    private String marca;

    @NotBlank
    @Size(max = 30)
    private String modelo;
}
