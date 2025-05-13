package com.hcmute.myanime.controller;

import com.hcmute.myanime.config.EmailTemplate;
import com.hcmute.myanime.model.CategoryEntity;
import com.hcmute.myanime.model.UsersEntity;
import com.hcmute.myanime.repository.CategoryRepository;
import com.hcmute.myanime.service.EmailSenderService;
import com.hcmute.myanime.service.UserService;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
public class testController {

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private UserService userService;

    @GetMapping("/test/demo")
    public ResponseEntity<?> demo() {
        try {
            emailSenderService.sendAsHTML(
                    "quachdieukhanh@gmail.com",
                    "[My anime E-Bill] Thanks for your subscribe " + "quachkhanh2",
                    EmailTemplate.TemplateProductInvoice(
                            "PAYID-MN47GSQ02C85057CM515013L",
                            "Order Premium Package: 1 month premium",
                            "$2",
                            "1",
                            "PayPal",
                            "quachkhanh2"
                    )
            );
            return ResponseEntity.ok("Send");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not send");
        }
    }

    //CMND ID:
//    X coordinate: 470
//    y coordinate: 510
//    width: 70
//    height: 430
    //Name:
//    X coordinate: 376
//    y coordinate: 380
//    width: 60
//    height: 900
    //Ngay sinh:
//    X coordinate: 330
//    y coordinate: 740
//    width: 50
//    height: 400
    //Que quan:
//    X coordinate: 180
//    y coordinate: 380
//    width: 55
//    height: 300

    @GetMapping("/test/tesseract")
    public ResponseEntity<?> testTesseract(@RequestParam Map<String, String> requestParams) {
        String marginTop = requestParams.get("marginTop");
        String marginLeft = requestParams.get("marginLeft");
        String width = requestParams.get("width");
        String height = requestParams.get("height");

        File image = new File("C:/Users/admin/Downloads/test/8261a5f3b964633a3a75.jpg");
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Users/admin/Downloads/lang");
        tesseract.setLanguage("vie");
        tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO_OSD);
        tesseract.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_TESSERACT_LSTM_COMBINED);
        String result = "Text";
        try {
            result = tesseract.doOCR(
                    image,
                    new Rectangle(
                            Integer.parseInt(marginTop),
                            Integer.parseInt(marginLeft),
                            Integer.parseInt(width),
                            Integer.parseInt(height)
                    )
            );
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }


}