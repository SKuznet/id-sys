package ru.pscb.uprid.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pscb.uprid.service.SoapService;

/**
 * Controller for tests
 * Default binding "/" return OK status if project is running
 */
@Controller
public class MainController {
    private final static Logger log = LoggerFactory.getLogger(AppController.class);
    @Autowired
    SoapService soapService;

    @RequestMapping("/")
    public String rest(Model model) {
        log.info("Someone try to open a default service page");
        model.addAttribute("info", "OK");
        return "index";
    }

}
