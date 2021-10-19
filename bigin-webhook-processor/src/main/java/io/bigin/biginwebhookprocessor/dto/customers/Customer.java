package io.bigin.biginwebhookprocessor.dto;


import lombok.Builder;

@Builder
public class Customer {
    Long id;
    String email;
    String phone;
}
