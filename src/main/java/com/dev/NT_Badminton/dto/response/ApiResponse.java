package com.dev.NT_Badminton.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
