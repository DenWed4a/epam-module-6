package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;


@Builder
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDto extends RepresentationModel<CertificateDto> {
    private final static String NOT_BLANK = "local.name.not.null.not.empty";
    private final static String MAX_LENGTH = "local.max.length";
    private final static String NAME_REGEX = "([\\w\\d\\s]+[,.!:\\-;'?]*)+";
    private final static String POSITIVE_FIELD = "local.positive.field";
    private final static String NOT_NULL = "local.not.null";

    private Integer id;

    @NotBlank(message = NOT_BLANK)
    @Pattern(regexp = NAME_REGEX)
    private String name;

    @NotBlank(message = NOT_BLANK)
    @Size(max = 120, message = MAX_LENGTH)
    private String description;

    @NotNull(message = NOT_NULL)
    @Positive(message = POSITIVE_FIELD)
    private Double price;

    @Positive(message = POSITIVE_FIELD)
    @Max(365)
    private Integer duration;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;



    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    private Set<TagDto> tags;
}
