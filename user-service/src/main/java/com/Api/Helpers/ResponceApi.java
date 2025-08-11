package com.Api.Helpers;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Response wrapper for API responses")
public class ResponceApi<T> {
    @Schema(description = "Status of the response", example = "success")
    private String status;
    @Schema(description = "Message from the API", example = "User created successfully")
    private String message;
    @Schema(description = "Actual response payload")
    private  T data;


}
