package com.epam.esm.validation;

import com.epam.esm.dto.CertificateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class UpdatedCertificateValidator implements Validator interface.
 * This class validate fields CertificateDto for updating
 * @author Dzianis Savastsiuk
 */
@Component
public class UpdatedCertificateDtoValidator implements Validator {



    private final static String NAME_DESCRIPTION_REGEX = "([\\w\\d\\s]+[,.!:\\-;'?]*)+";
    private final static String NAME_NOT_EMPTY = "local.name.not.null.not.empty";
    private final static String ERROR_CODE_400 = "400";
    private final static String DESCRIPTION_MAX_LENGTH = "local.description.length";
    private final static String PRICE_POSITIVE = "local.price.positive";
    private final static String DURATION_RANGE = "local.duration.range";


    @Override
    public boolean supports(Class<?> clazz) {
        return CertificateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CertificateDto certificateDto = (CertificateDto) target;

        if(certificateDto.getName() != null){
            Pattern pattern = Pattern.compile(NAME_DESCRIPTION_REGEX);
            Matcher matcher = pattern.matcher(certificateDto.getName());

            if (!matcher.matches()){
                errors.reject(ERROR_CODE_400, NAME_NOT_EMPTY);
            }
        }

        if(certificateDto.getDescription() != null){
            Pattern pattern = Pattern.compile(NAME_DESCRIPTION_REGEX);
            Matcher matcher = pattern.matcher(certificateDto.getDescription());

            if (!matcher.matches()||certificateDto.getDescription().length() > 120){
                errors.reject(ERROR_CODE_400, DESCRIPTION_MAX_LENGTH);
            }
        }

        if(certificateDto.getPrice() != null){
            if(certificateDto.getPrice() < 0){
                errors.reject(ERROR_CODE_400, PRICE_POSITIVE);
            }
        }

        if(certificateDto.getDuration() != null){
            if(certificateDto.getDuration() < 0 || certificateDto.getDuration() > 365){
                errors.reject(ERROR_CODE_400, DURATION_RANGE);
            }
        }

    }
}
