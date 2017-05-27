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
@Constraint(validatedBy = pageValidator.class)
@Documented
public @interface ValidPages {

    String message() default "{constraints.pageValidator}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}