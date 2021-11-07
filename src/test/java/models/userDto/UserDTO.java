package models.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Data data;
    private Support support;
}
