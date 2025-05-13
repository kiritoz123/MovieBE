package com.hcmute.myanime.controller;

import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.config.EmailTemplate;
import com.hcmute.myanime.model.OrderPremiumEntity;
import com.hcmute.myanime.model.PaypalOrderEntity;
import com.hcmute.myanime.model.UsersEntity;
import com.hcmute.myanime.repository.OrderPremiumRepository;
import com.hcmute.myanime.repository.PaypalOrderRepository;
import com.hcmute.myanime.service.EmailSenderService;
import com.hcmute.myanime.service.PaypalOrderService;
import com.hcmute.myanime.service.PaypalService;
import com.hcmute.myanime.beans.Order;
import com.hcmute.myanime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("payment/paypal")
public class PaypalController {
    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @Autowired
    private PaypalService paypalService;

    @PostMapping("/pay")
    public ResponseEntity<?> payment(@RequestBody Order order, HttpServletRequest httpServletRequest) {
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
                .replacePath(null)
                .build()
                .toUriString();
        baseUrl += "/payment/paypal/";

        try {
            Payment payment = paypalService.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
                    order.getIntent(), order.getDescription(), baseUrl + CANCEL_URL,
                    baseUrl + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("error");
    }

    @GetMapping(value = CANCEL_URL)
    public void cancelPay(HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }

    @Autowired
    private PaypalOrderService paypalOrderService;
    @Autowired
    private PaypalOrderRepository paypalOrderRepository;
    @Autowired
    private OrderPremiumRepository orderPremiumRepository;
    @Autowired
    private UserService userService;

    @GetMapping(value = SUCCESS_URL)
    public ResponseEntity<?> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, @RequestParam("token") String token) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                // Xu ly premium va update paypal order
                Optional<PaypalOrderEntity> paypalOrderRepositoryByToken = paypalOrderRepository.findByToken(token);
                if(paypalOrderRepositoryByToken.isPresent()) {
                    PaypalOrderEntity paypalOrderEntity = paypalOrderRepositoryByToken.get();
                    if(paypalOrderEntity.getStatus().equals("paid")) {
                        return ResponseEntity.badRequest().body("This order has been delivered.");
                    }
                    paypalOrderEntity.setPayerId(payerId);
                    paypalOrderEntity.setPaymentId(paymentId);
                    paypalOrderEntity.setStatus("paid");
                    PaypalOrderEntity paypalOrderEntitySaved = paypalOrderRepository.save(paypalOrderEntity);

                    OrderPremiumEntity orderPremiumEntity = paypalOrderEntity.getOrderPremiumEntity();
                    orderPremiumEntity.setStatus("paid");
                    OrderPremiumEntity orderPremiumEntitySaved = orderPremiumRepository.save(orderPremiumEntity);
                    // Xu ly premium
                    userService.createPremium(orderPremiumEntity.getSubscriptionPackageById().getId(), orderPremiumEntity.getUsersEntityById());

                    //Gui mail hoa đơn neu user da xac thuc mail
                    userService.sendProductInvoiceMailUserLogging(
                            paypalOrderEntitySaved.getPaymentId(),
                            paypalOrderEntitySaved.getDescription(),
                            String.valueOf(paypalOrderEntitySaved.getPrice()),
                            "1",
                            "PayPal",
                            orderPremiumEntitySaved.getUsersEntityById().getUsername()
                    );
                }
                return ResponseEntity.ok("Payment success");
            }
        } catch (PayPalRESTException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Payment not done");
    }
}

