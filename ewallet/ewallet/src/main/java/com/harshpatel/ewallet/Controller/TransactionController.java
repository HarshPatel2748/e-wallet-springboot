package com.harshpatel.ewallet.Controller;

import com.harshpatel.ewallet.DTO.SendMoneyRequest;
import com.harshpatel.ewallet.Entity.Transaction;
import com.harshpatel.ewallet.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/send")
    public ResponseEntity<Transaction> sendMoney(@RequestBody SendMoneyRequest request){
        try{
            Transaction transaction = transactionService.sendMoney
                    (request.getSenderId(), request.getReceiverId(), request.getAmount());
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long userId){
        List<Transaction> transactions = transactionService.getTransactionHistory(userId);
        return ResponseEntity.ok(transactions);
    }
}
