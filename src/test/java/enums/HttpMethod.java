package enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    public String value;
}
