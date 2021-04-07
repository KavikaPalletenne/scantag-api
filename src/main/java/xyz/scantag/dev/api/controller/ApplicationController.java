package xyz.scantag.dev.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplicationController {

    @RequestMapping("/")
    public String Welcome() {

        return "index";
    }

    @RequestMapping("/google95919a7921b7e9dd.html")
    public String GoogleSEOVerification() {

        return "google95919a7921b7e9dd";
    }

    @RequestMapping("/google95919a7921b7e9dd")
    public String GoogleSEOVerificationNoFileType() {

        return "google95919a7921b7e9dd";
    }
}
