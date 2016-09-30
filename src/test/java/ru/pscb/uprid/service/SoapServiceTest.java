package ru.pscb.uprid.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SoapService.class})
public class SoapServiceTest {
    @Autowired
    SoapService soapService;

    @Test
    public void testPassportService() throws IOException, SOAPException, URISyntaxException {
        assertEquals("300", soapService.resultPassport("0000", "0000", "0000-00-00"));
    }

    @Test
    public void testSnilsService() throws IOException, SOAPException, URISyntaxException {
        assertEquals("VALID", soapService.resultSnils("Кузнецов", "Сергей", "Сергеевич", "0000-00-00", "00", "М"));
    }

    @Test
    public void testIncorrectServiceRequest() throws IOException, SOAPException, URISyntaxException {
        assertNotEquals("300", soapService.resultPassport("00", "000", "0000-00-00"));
    }

    @Test
    public void testIncorrectSnilsService() throws IOException, SOAPException, URISyntaxException {
        assertNotEquals("VALID", soapService.resultSnils("Кузнецов", "Сергей", "Сергеевич", "1915-03-05", "114-625-00 50", "М"));
    }

}

