package com.harshpatel.ewallet.Controller;

import com.harshpatel.ewallet.Entity.PaymentOrder;
import com.harshpatel.ewallet.Repository.PaymentOrderRepository;
import com.harshpatel.ewallet.Service.PaymentService;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentWebhookController {

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

//    @PostMapping("/webhook")
//    public ResponseEntity<?> verifyPayment(@RequestBody String payload,
//                                           @RequestHeader ("X-Razorpay-Signature") String signature){
//        try{
////            boolean isValid = paymentService.verifySignature(payload, signature, webhookSecret);
//            boolean isValid = Utils.verifyWebhookSignature(payload, signature, webhookSecret);
//
//            if(isValid){
//                JSONObject json = new JSONObject(payload);
//                String orderId = json
//                        .getJSONObject("payload")
//                        .getJSONObject("payment")
//                        .getJSONObject("entity")
//                        .getString("order_id");
//
//                Double amount = json
//                        .getJSONObject("payload")
//                        .getJSONObject("payment")
//                        .getJSONObject("entity")
//                        .getDouble("amount") / 100.0;
//
//                PaymentOrder paymentOrder = paymentOrderRepository.findByRazorpayOrderId(orderId)
//                        .orElseThrow(()-> new RuntimeException("Order not found"));
//                Long userId = paymentOrder.getUserId();
//
//                paymentService.addMoney(userId, amount);
//
//                paymentOrder.setStatus("PAID");
//                paymentOrderRepository.save(paymentOrder);
//
//                return ResponseEntity.ok("Payment verified and wallet updated successfully");
//            }else {
//                return ResponseEntity.status(400).body("Invalid signature. Payment verification failed.");
//            }
//        }catch (Exception e){
//            return ResponseEntity.status(500).body("Error verifying payment: " + e.getMessage());
//        }
//    }
@PostMapping("/webhook")
public ResponseEntity<?> verifyPayment(
        @RequestBody String payload,
        @RequestHeader("X-Razorpay-Signature") String signature) {

    try {
        // 1️⃣ Verify webhook signature
        boolean isValid = Utils.verifyWebhookSignature(payload, signature, webhookSecret);

        if (!isValid) {
            return ResponseEntity.status(401).body("Invalid signature. Payment verification failed.");
        }

        // 2️⃣ Parse JSON
        JSONObject json = new JSONObject(payload);
        JSONObject paymentEntity = json
                .getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity");

        String orderId = paymentEntity.getString("order_id");
        Double amount = paymentEntity.getDouble("amount") / 100.0;

        // 3️⃣ Fetch payment order from DB
        PaymentOrder paymentOrder = paymentOrderRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Long userId = paymentOrder.getUserId();

        // 4️⃣ Add money to wallet
        paymentService.addMoney(userId, amount);

        // 5️⃣ Update payment order status
        paymentOrder.setStatus("PAID");
        paymentOrderRepository.save(paymentOrder);

        return ResponseEntity.ok("Payment verified and wallet updated successfully");

    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error verifying payment: " + e.getMessage());
    }
}
}
