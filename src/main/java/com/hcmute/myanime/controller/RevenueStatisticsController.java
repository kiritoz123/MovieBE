package com.hcmute.myanime.controller;

import com.hcmute.myanime.dto.CategoryViewDTO;
import com.hcmute.myanime.service.PaypalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Year;
import java.util.List;

@Controller
@RequestMapping("admin/statistics")
public class RevenueStatisticsController {
    @Autowired
    private PaypalOrderService paypalOrderService;

    //Region admin api
    @GetMapping("/revenue/categories")
    public ResponseEntity<?> getRevenueInYear(@RequestParam Integer year)
    {
        if(year == null) {          //Get default value year is current year if parameter year is null
            year = Year.now().getValue();
        }
        return ResponseEntity.ok(paypalOrderService.getAllRevenueInMonthByYear(year));
    }
    //End region admin api
}
