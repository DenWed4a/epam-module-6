package com.epam.esm.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
@Builder
@EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class OrderDto extends RepresentationModel<OrderDto> {
    private Integer id;
    private Double total;
    private Integer userId;
    private LocalDateTime dateOfPurchase;
    private CertificateDto certificate;

}
