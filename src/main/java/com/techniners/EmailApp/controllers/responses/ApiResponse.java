package com.techniners.EmailApp.controllers.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApiResponse {
  private Object payload;
  private String message;
  private boolean isSuccessful;
  private int statusCode;

}
