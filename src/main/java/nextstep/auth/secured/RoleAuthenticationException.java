package nextstep.auth.secured;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RoleAuthenticationException extends RuntimeException {
}
