package constraints;

import java.util.Calendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class yearValidator implements ConstraintValidator<CheckYear, Integer> {
//TODO modify message (currently states may not be empty)
	private int year;
	
    public void initialize(CheckYear constraintAnnotation) {
        this.year = Calendar.getInstance().get(Calendar.YEAR) + 1;
    }

    public boolean isValid(Integer object, ConstraintValidatorContext constraintContext) {
    	return object <= year;
    }

}