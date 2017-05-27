package constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class authorValidator implements ConstraintValidator<CheckAuthorNotEmpty, String> {

    public void initialize(CheckAuthorNotEmpty constraintAnnotation) {
    }

    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        return object != "";
    }

}
