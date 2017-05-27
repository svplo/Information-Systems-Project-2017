package constraints;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = authorValidator.class)
@Documented
public @interface CheckAuthorNotEmpty {

    String message() default "{constraints.checkAuthorNotEmpty}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
