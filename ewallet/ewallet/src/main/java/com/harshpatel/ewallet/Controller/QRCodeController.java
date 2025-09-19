package com.harshpatel.ewallet.Controller;

import com.google.zxing.WriterException;
import com.harshpatel.ewallet.Service.PaymentService;
import com.harshpatel.ewallet.Service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/qr")
public class QRCodeController{

    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateQRCode(@RequestBody Map<String, Object> request){
        try{
            Long userId = Long.valueOf(request.get("userId").toString());
            Double amount = Double.valueOf(request.get("amount").toString());

            String qrText = "userId:" + userId + ",amount:" + amount;

            String qrCodeBase64 = qrCodeService.generateQRCode(qrText, 250, 250);

            return ResponseEntity.ok(Map.of(
                    "qrCode", qrCodeBase64,
                    "message", "QR code generated successfully"
            ));

        }catch (WriterException | IOException e){
            return ResponseEntity.status(500).body("Error generating QR code: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Invalid request: " + e.getMessage());
        }
    }

    @PostMapping("/generate-order-qr")
    public ResponseEntity<?> generateOrderQRCode(@RequestBody Map<String, Object> request){
        try{
            Long userId = Long.valueOf(request.get("userId").toString());
            Double amount = Double.valueOf(request.get("amount").toString());

            String qrText = paymentService.createOrderAndGetQRCodeText(userId, amount);

            String qrCodeBase64 = qrCodeService.generateQRCode(qrText, 250, 250);

            return ResponseEntity.ok(Map.of(
                    "qrCode", qrCodeBase64,
                    "message", "Order QR code with Razorpay generated successfully"
            ));
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error generating order QR code: " + e.getMessage());
        }
    }


    @PostMapping("/generate-paymentlink-qr")
    public ResponseEntity<?> generatePaymentLinkQRCode(@RequestBody Map<String, Object> request){
        try{
            Long userId = Long.valueOf(request.get("userId").toString());
            Double amount = Double.valueOf(request.get("amount").toString());

            // Get the Razorpay payment link URL
            String paymentUrl = paymentService.createPaymentLink(userId, amount);

            // Generate QR code of the payment URL
            String qrCodeBase64 = qrCodeService.generateQRCode(paymentUrl, 250, 250);

            return ResponseEntity.ok(Map.of(
                    "paymentUrl", paymentUrl,
                    "qrCode", qrCodeBase64,
                    "message", "Payment link QR code generated successfully"
            ));
        }catch(Exception e){
            return ResponseEntity.status(500).body("Error generating payment link QR code: " + e.getMessage());
        }
    }
}
