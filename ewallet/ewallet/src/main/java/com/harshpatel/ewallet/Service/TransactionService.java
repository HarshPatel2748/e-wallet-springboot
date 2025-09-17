package com.harshpatel.ewallet.Service;

import com.harshpatel.ewallet.Entity.Transaction;
import com.harshpatel.ewallet.Entity.User;
import com.harshpatel.ewallet.Repository.TransactionRepository;
import com.harshpatel.ewallet.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public Transaction sendMoney(Long senderId, Long receiverId, Double amount){

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if(sender.getBalance() < amount){
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct amount from sender
        sender.setBalance(sender.getBalance() - amount);
        userRepository.save(sender);

        // Add amount to receiver
        receiver.setBalance(receiver.getBalance() + amount);
        userRepository.save(receiver);

        // Create transaction record

        Transaction debitTransaction = new Transaction();
        debitTransaction.setSenderId(senderId);
        debitTransaction.setReceiverId(receiverId);
        debitTransaction.setAmount(amount);
        debitTransaction.setType("DEBIT");
        debitTransaction.setTimestamp(java.time.LocalDateTime.now());
        transactionRepository.save(debitTransaction);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setSenderId(senderId);
        creditTransaction.setReceiverId(receiverId);
        creditTransaction.setAmount(amount);
        creditTransaction.setType("CREDIT");
        creditTransaction.setTimestamp(java.time.LocalDateTime.now());
        transactionRepository.save(creditTransaction);

        return debitTransaction;
    }

    public List<Transaction> getTransactionHistory(Long userId){
        return transactionRepository.findBySenderIdOrReceiverId(userId, userId);
    }
}
