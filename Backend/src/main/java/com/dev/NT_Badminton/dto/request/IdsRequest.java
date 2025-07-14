package com.dev.NT_Badminton.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class IdsRequest {
    @NotNull
    @Size(min = 1)
    private List<Integer> ids;
}
