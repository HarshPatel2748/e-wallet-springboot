package com.harshpatel.ewallet.DTO;

import lombok.Data;

@Data
public class AddMoneyRequest {

    private Long userId;
    private Double amount;
}
