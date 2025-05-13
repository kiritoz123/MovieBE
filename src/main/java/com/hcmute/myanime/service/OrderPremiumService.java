package com.hcmute.myanime.service;

import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.dto.OrderPremiumDTO;
import com.hcmute.myanime.model.OrderPremiumEntity;
import com.hcmute.myanime.model.SubscriptionPackageEntity;
import com.hcmute.myanime.model.UsersEntity;
import com.hcmute.myanime.repository.OrderPremiumRepository;
import com.hcmute.myanime.repository.SubscriptionPackageRepository;
import com.hcmute.myanime.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderPremiumService {
    @Autowired
    private OrderPremiumRepository orderPremiumRepository;
    @Autowired
    private SubscriptionPackageRepository subscriptionPackageRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ApplicationUserService applicationUserService;

    public int Create(Map<String, Object> request, StringBuilder message)
    {
        if(!request.containsKey("method")) {
            message.append("Key method missing");
            return -1;
        } else if (!request.containsKey("subcription_package_id")) {
            message.append("Key subcription_package_id missing");
            return -1;
        }

        String description = "Order Premium Package: ";
        String method = request.get("method").toString();

        int packageId = -1;
        try {
            packageId = (int)request.get("subcription_package_id");
        } catch (Exception ex) {}

        // Check package is exist
        Optional<SubscriptionPackageEntity> subscriptionPackageEntityOptional = subscriptionPackageRepository.findById(packageId);
        if(!subscriptionPackageEntityOptional.isPresent()) {
            message.append("Package Not Exist");
            return -1;
        }
        description += subscriptionPackageEntityOptional.get().getName();

        if(request.containsKey("description")) {
            description = request.get("description").toString();
        }

        // Check user is logged
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        Optional<UsersEntity> userByUsername = usersRepository.findByUsername(usernameLoggedIn);
        if (!userByUsername.isPresent()) {
            message.append("User logged is error");
            return -1;
        }

        try {
            OrderPremiumEntity orderPremiumEntity = new OrderPremiumEntity();
            orderPremiumEntity.setStatus("pending");
            orderPremiumEntity.setSubscriptionPackageById(subscriptionPackageEntityOptional.get());
            orderPremiumEntity.setMethod(method);
            orderPremiumEntity.setUsersEntityById(userByUsername.get());
            orderPremiumEntity.setDescription(description);

            orderPremiumEntity = orderPremiumRepository.save(orderPremiumEntity);
            message.append("Order premium success");
            return orderPremiumEntity.getId();
        } catch (Exception ex) {
            message.append(ex.getMessage());
            return -1;
        }
    }

    public boolean Cancel(int orderPremiumId, StringBuilder message)
    {
        // Check order premium
        Optional<OrderPremiumEntity> orderPremiumEntityOptional = orderPremiumRepository.findById(orderPremiumId);
        if(!orderPremiumEntityOptional.isPresent()) {
            message.append("Order is not exist");
            return false;
        }

        // Check user is logged
        String usernameLoggedIn = applicationUserService.getUsernameLoggedIn();
        Optional<UsersEntity> userByUsername = usersRepository.findByUsername(usernameLoggedIn);
        if (!userByUsername.isPresent()) {
            message.append("User logged is error");
            return false;
        }

        OrderPremiumEntity orderPremiumEntity = orderPremiumEntityOptional.get();
        UsersEntity usersEntity = userByUsername.get();

        if(!orderPremiumEntity.getStatus().equals("pending")) {
            message.append("Status now is not a pending");
            return false;
        }

        if(orderPremiumEntity.getUsersEntityById().getId() != usersEntity.getId()) {
            message.append("You do not have permission to perform this action");
            return false;
        }

        try {
            orderPremiumEntity.setStatus("cancel");
            orderPremiumRepository.save(orderPremiumEntity);
            message.append("Cancel order premium success");
            return true;
        } catch (Exception exception) {
            message.append(exception.getMessage());
            return false;
        }
    }


    @Autowired
    private PaypalService paypalService;
    @Autowired
    private PaypalOrderService paypalOrderService;
    public boolean CreatePay(int orderPremiumId, String baseUrl, StringBuilder message)
    {
        // Check order premium
        Optional<OrderPremiumEntity> orderPremiumEntityOptional = orderPremiumRepository.findById(orderPremiumId);
        if(!orderPremiumEntityOptional.isPresent()) {
            message.append("Order is not exist");
            return false;
        }

        OrderPremiumEntity orderPremiumEntity = orderPremiumEntityOptional.get();
        if(!orderPremiumEntity.getStatus().equals("pending")) {
            message.append("Status now is not a pending");
            return false;
        }

        // INSERT Paypal order
        if(orderPremiumEntity.getMethod().equals("paypal")) {
            try {
                double price = orderPremiumEntity.getSubscriptionPackageById().getPrice();
                String desc = orderPremiumEntity.getDescription();
                Payment payment = paypalService.createPayment(price, "USD", "paypal",
                        "sale", desc, baseUrl + "pay/cancel",
                        baseUrl + "pay/success");
                for(Links link:payment.getLinks()) {
                    if(link.getRel().equals("approval_url")) {
                        String urlPayment = link.getHref();
                        int indexToken = urlPayment.indexOf("token=");
                        String token = urlPayment.substring(indexToken).replace("token=", "");

                        if(paypalOrderService.Create(orderPremiumEntity, token)) {
                            message.append(urlPayment);
                            return true;
                        } else {
                            message.append("Create Paypal Order ID error");
                            return false;
                        }
                    }
                }
            } catch (PayPalRESTException e) {
                message.append(e.getMessage());
                return false;
            }
        }

        message.append("Sorry. Payment method is not support");
        return false;
    }
}
