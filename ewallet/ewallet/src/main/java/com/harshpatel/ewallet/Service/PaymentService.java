package com.harshpatel.ewallet.Service;

import com.harshpatel.ewallet.Entity.Transaction;
import com.harshpatel.ewallet.Entity.User;
import com.harshpatel.ewallet.Repository.TransactionRepository;
import com.harshpatel.ewallet.Repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.annotation.PostConstruct;
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

    @Value("${razorpay.key-id}")
    private String key;

    @Value("${razorpay.key-secret}")
    private String secret;

    public PaymentService(UserRepository userRepository,
                          TransactionRepository transactionRepository){
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostConstruct
    public void initRazorpayClient() throws RazorpayException{
        this.razorpayClient = new RazorpayClient(key, secret);
    }

    public Order createOrder(Double amount)throws RazorpayException{
        JSONObject options = new JSONObject();
        options.put("amount", (int)(amount * 100)); // amount in the smallest currency unit
        options.put("currency", "INR");
        options.put("payment_capture", 1);
        return razorpayClient.orders.create(options);
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

    public String createOrderAndGetQRCodeText(Long userId, Double amount)throws RazorpayException{
        JSONObject options = new JSONObject();
        options.put("amount", (int)(amount * 100)); // amount in the smallest currency unit
        options.put("currency", "INR");
        options.put("payment_capture", 1);

        Order order = razorpayClient.orders.create(options);

        return "orderId:" + order.get("id") + ",userId:" + userId + ",amount:" + amount;
    }

    public String createPaymentLink(Long userId, Double amount) throws RazorpayException {
        // Get user details
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", (int)(amount * 100)); // amount in paise
        paymentLinkRequest.put("currency", "INR");

        // Customer details
        JSONObject customer = new JSONObject();
        customer.put("name", user.getName());
        customer.put("email", user.getEmail());
        paymentLinkRequest.put("customer", customer);

        // Notify via SMS & Email
        JSONObject notify = new JSONObject();
        notify.put("sms", true);
        notify.put("email", true);
        paymentLinkRequest.put("notify", notify);

        // Optional: Add a description
        paymentLinkRequest.put("description", "Adding money to wallet");

        // Create Payment Link
        com.razorpay.PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

        // Return short URL for redirect / QR
        return paymentLink.get("short_url").toString();
    }

}