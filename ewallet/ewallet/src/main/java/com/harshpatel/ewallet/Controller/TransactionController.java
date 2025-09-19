package com.harshpatel.ewallet.Controller;

import com.harshpatel.ewallet.DTO.SendMoneyRequest;
import com.harshpatel.ewallet.Entity.Transaction;
import com.harshpatel.ewallet.Entity.User;
import com.harshpatel.ewallet.Repository.UserRepository;
import com.harshpatel.ewallet.Security.JwtUtil;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMoney(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SendMoneyRequest request){
        try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(401).body("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);

            User sender = userRepository.findByEmail(email).
                    orElseThrow(()->new RuntimeException("Sender not found"));

            Transaction transaction = transactionService.sendMoney(
                    sender.getId(), request.getReceiverId(), request.getAmount());
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
