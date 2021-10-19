package io.bigin.biginwebhookprocessor.dto.customers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomersDataRequestParameter {
    Long shop_id;
    String shop_domain;
    long[] orders_requested;
    Customer customer;
    DataRequest data_request;
}