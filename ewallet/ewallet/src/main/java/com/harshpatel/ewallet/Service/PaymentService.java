package com.harshpatel.ewallet.Service;

import com.harshpatel.ewallet.Entity.Transaction;
import com.harshpatel.ewallet.Entity.User;
import com.harshpatel.ewallet.Repository.TransactionRepository;
import com.harshpatel.ewallet.Repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;

@Service
public class PaymentService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TransactionRepository transactionRepository;

    private RazorpayClient razorpayClient;

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    public PaymentService(UserRepository userRepository,
                          TransactionRepository transactionRepository)
                          throws RazorpayException {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.razorpayClient = new RazorpayClient(key, secret);
    }

    public Order createOrder(Double amount)throws RazorpayException{
        JSONObject options = new JSONObject();
        options.put("amount", (int)(amount * 100)); // amount in the smallest currency unit
        options.put("currency", "INR");
        options.put("payment_capture", 1);
        return razorpayClient.Orders.create(options);
    }


    public User addMoney(Long userId, Double amount){
        User user = userRepository.findById(userId).
                orElseThrow(()->new RuntimeException("User not found"));

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setSenderId(null);
        transaction.setReceiverId(userId);
        transaction.setAmount(amount);
        transaction.setType("CREDIT");
        transaction.setTimestamp(new Date());

        transactionRepository.save(transaction);
        return user;
    }
}