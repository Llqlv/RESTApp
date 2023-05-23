package com.llqlv.restapp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PersonErrorResponse {
    private String message;
    private long timestamp;
}
