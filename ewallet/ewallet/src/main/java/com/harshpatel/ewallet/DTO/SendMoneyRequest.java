package com.harshpatel.ewallet.DTO;

import lombok.Data;

@Data
public class SendMoneyRequest {

    private Long senderId;
    private Long receiverId;
    private Double amount;
}
