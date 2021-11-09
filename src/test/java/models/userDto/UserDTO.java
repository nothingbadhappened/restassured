package models.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
@JsonIgnoreProperties
public class UserDTO {
    @JsonProperty("data")
    private Data data;
    @JsonProperty("support")
    private Support support;
}
