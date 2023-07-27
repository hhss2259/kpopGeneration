package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.dialect.unique.DefaultUniqueDelegate;

@Data
@AllArgsConstructor
@Builder
public class DefaultResponse<T> {
    T data;
    String message;
    int statusCode;

    public DefaultResponse(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }

    public static <T>DefaultResponse<T> res(final int statusCode, final String message){
        return res(statusCode, message, null);
    }

    public static <T> DefaultResponse<T> res(final int statusCode, final String message, final T t){
        return DefaultResponse.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .message(message)
                .build();
    }

}
