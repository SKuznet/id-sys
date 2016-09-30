package ru.pscb.uprid.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.pscb.uprid.entity.Passport;
import ru.pscb.uprid.entity.Snils;
import ru.pscb.uprid.service.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Controller for REST API
 * Receive snils and passport data
 * at JSON format, adding data to DB
 * and return response:
 * 200 - OK, 403 - wrong data, 500 - wrong format
 */
@RestController
@RequestMapping("/user")
public class AppController {

    private final static Logger log = LoggerFactory.getLogger(AppController.class);
    /**
     * status  0 - snils valid
     * status -1 - incorrect snils
     */
    private final int STATE_OK = 0;
    private final int STATE_INCORRECT_VALIDATE = -1;

    @Autowired
    SnilsService snilsService;
    @Autowired
    PassportService passportService;
    @Autowired
    SoapService soapService;
    @Autowired
    DateCheck dateCheck;
    @Autowired
    NumberCheck numberCheck;

    /**
     * /user/snils - binding for snils check
     */
    @RequestMapping(value = "/snils", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object addSnils(@RequestBody Snils snils) {
        try {
            log.info("Get snils data", snils);
            if (snils.getSurname() != null && snils.getBirthday() != null && snils.getFirstName() != null && snils.getPatronymic() != null) {
                if (soapService.resultSnils(snils.getSurname(), snils.getFirstName(), snils.getPatronymic(), snils.getBirthday()
                        , snils.getSnils(), snils.getGender()).equals("VALID") && dateCheck.checkBirthdayDate(snils.getBirthday()) &&
                        dateCheck.checkGender(snils.getGender())) {
                    snils.setState(STATE_OK);
                    snils.setStateDesc("");
                    snilsService.addSnils(snils);
                    log.info("Set state to OK, request check valid");
                    return snils;
                } else {
                    snils.setState(STATE_INCORRECT_VALIDATE);
                    snils.setStateDesc("Validate failed. Snils incorrect.");
                    snilsService.addSnils(snils);
                    log.error("Incorrect SNILS");
                    return snils;
                }
            } else {
                snils.setState(STATE_INCORRECT_VALIDATE);
                snils.setStateDesc("Wrong request. Not all required field exist.");
                snilsService.addSnils(snils);
                log.error("Incorrect SNILS validation - some field empty");
                return snils;
            }
        } catch (IOException | SOAPException | URISyntaxException | IllegalArgumentException e) {
            log.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR + e.getMessage());
            return "Wrong request, please API specification";
        }
    }

    /**
     * /user/snils - binding for passport check
     */
    @RequestMapping(value = "/passport", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object addPassport(@RequestBody Passport passport) {
        try {
            log.info("Get passport data", passport);
            if (passport.getNumber() != null && passport.getSeries() != null && passport.getIssueDate() != null && numberCheck.numberPassportCheck(passport.getNumber(), passport.getSeries())) {
                if (soapService.resultPassport(passport.getSeries(), passport.getNumber(), passport.getIssueDate()).equals("300")
                        && dateCheck.checkIssuePassportDate(passport.getIssueDate())) {
                    passport.setState(STATE_OK);
                    passport.setStateDesc("");
                    passportService.addPassport(passport);
                    log.info("Set state to OK, request check valid");
                    return passport;
                } else {
                    passport.setState(STATE_INCORRECT_VALIDATE);
                    passport.setStateDesc("Validate failed, password not valid.");
                    passportService.addPassport(passport);
                    log.error("Incorrect passport");
                    return passport;
                }
            } else {
                passport.setState(STATE_INCORRECT_VALIDATE);
                passport.setStateDesc("Some field is empty, not all required field exist at request");
                passportService.addPassport(passport);
                log.error("Incorrect password validation - some field empty");
                return passport;
            }
        } catch (IOException | SOAPException | URISyntaxException | IllegalArgumentException e) {
            log.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " " + e.getMessage());
            return "Wrong request, please API specification";
        }
    }

}
