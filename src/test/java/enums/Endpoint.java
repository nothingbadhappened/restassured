package enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Endpoint {
    BASE_URL("https://reqres.in/api"),
    LOGIN("/login"),
    USERS("/users");

    public String value;
}
