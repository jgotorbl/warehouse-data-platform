package com.jaime.gotor.warehouse.software.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DataValidatorService
 * <br>
 * <code>com.jaime.gotor.warehouse.software.validation.DataValidatorService</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 20 February 2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataValidator<T> {

    /**
     * Validates an object
     * @param object object being validated
     * @return a set of constraint violations, if there are any, or an empty set otherwise
     */
    public Set<ConstraintViolation<T>> validate(T object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(object);
    }

    /**
     * Builds the error message of constraint violations
     * @param violationSet set containing all the constraint violations
     * @return a message sent as error message in the response
     */
    public String buildExceptionMessage(Set<ConstraintViolation<T>> violationSet) {
        return violationSet.stream()
                .map(v -> v.getPropertyPath().toString().concat(" : ").concat(v.getMessageTemplate()))
                .collect(Collectors.joining(", "));
    }

}
