package com.harshpatel.ewallet.Controller;

import com.harshpatel.ewallet.Entity.User;
import com.harshpatel.ewallet.Service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Double> request){
        try{
            Double amount = request.get("amount");
            Order order = paymentService.createOrder(amount);
            return ResponseEntity.ok(order.toString());
        }catch (RazorpayException e){
            return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
        }
    }

    @PostMapping("/add-money")
    public ResponseEntity<?> addMoney(@RequestBody Map<String, Object> request){
        try{
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Double amount = Double.valueOf(request.get("amount").toString());

            return ResponseEntity.ok(paymentService.addMoney(currentUser.getId(), amount));

        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error adding money: " + e.getMessage());
        }
    }
}
