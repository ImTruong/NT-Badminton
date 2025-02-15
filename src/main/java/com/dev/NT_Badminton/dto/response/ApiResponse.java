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
    private long totalRecords;
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public ApiResponse(boolean success,T data,long totalRecords) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
