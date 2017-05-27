package constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class pageValidator implements ConstraintValidator<ValidPages, String> {

    public void initialize(ValidPages constraintAnnotation) {
    }

    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if(object == null){
        	return true;
        } else if(object.matches("[0-9]+")){
        	return true;
        }
        object.replaceFirst("-", "");
        if (object.matches("[0-9]+")){
        	return true;
        } else {
        	return false;
        }
    }

}