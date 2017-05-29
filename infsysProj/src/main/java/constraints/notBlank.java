package constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraints.NotBlank;

public class notBlank implements ConstraintValidator<NotBlank, String> {

	public void initialize(NotBlank annotation) {
	}

	/**
	 * Checks that the trimmed string is not empty.
	 *
	 * @param charSequence The character sequence to validate.
	 * @param constraintValidatorContext context in which the constraint is evaluated.
	 *
	 * @return Returns <code>true</code> if the string is <code>null</code> or the length of <code>charSequence</code> between the specified
	 *         <code>min</code> and <code>max</code> values (inclusive), <code>false</code> otherwise.
	 */
	public boolean isValid(String charSequence, ConstraintValidatorContext constraintValidatorContext) {
		if ( charSequence == null ) {
			return true;
		}

		return charSequence.toString().trim().length() > 0;
	}

}
