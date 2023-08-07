package kpop.kpopGeneration.security.exception;

public class NotExistedRoleException extends RuntimeException {
    public NotExistedRoleException(String message) {
        super(message);
    }
    public NotExistedRoleException() {
    }

}
