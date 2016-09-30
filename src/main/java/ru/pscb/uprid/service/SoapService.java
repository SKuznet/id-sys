package ru.pscb.uprid.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.pscb.uprid.controller.AppController;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * SOAP service for checking correct data with passport and snils
 *
 * @ Component required for correct working with @PropertySource
 */
@Component
@PropertySource(value = {"classpath:soap.properties"})
public class SoapService {
    private final static Logger log = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private Environment environment;

    public SoapService() {
    }

    /**
     * Send soap message to specific URL
     */
    private SOAPMessage sendSOAP(String url, SOAPMessage message) throws MalformedURLException, SOAPException {

        SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
        SOAPConnection connection = factory.createConnection();

        URL endPoint = new URL(url);
        SOAPMessage response = null;
        try {
            response = connection.call(message, endPoint);
        } catch (SOAPException ex) {
            log.error("Soap Error: ", ex);
            ex.printStackTrace();
        }

        connection.close();

        return response;
    }

    /**
     * String to XML with UTF-8 encoding
     */
    private SOAPMessage getSoapMessageFromString(String xml) throws SOAPException, IOException {
        MessageFactory factory = MessageFactory.newInstance();
        return factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
    }

    /**
     * Get response result for passport with SOAP
     * login and password placed at soap.properties
     */
    public String resultPassport(String series, String number, String dateIssue) throws SOAPException, IOException, URISyntaxException {

        /**
         * Environment properties places at soap.properties
         * Do not use hardcode for placing params
         * */
        String xmlPassport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://www.id-sys.ru/schemas/idbank/common/2012/0.01/\" xmlns:id=\"http://www.id-sys.ru/schemas/idbank/fms/2013/1.00/\" xmlns:xd=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "    <soapenv:Header>\n" +
                "        <wsse:Security soapenv:mustUnderstand=\"1\">\n" +
                "            <wsse:UsernameToken wsu:Id=\"uuid_96cb9d66-2781-4989-b36b-583091729b25\" xmlns:ns13=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns14=\"http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512\">\n" +
                "                <wsse:Username>" + environment.getRequiredProperty("soap.user") + "</wsse:Username>\n" +
                "                <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">" + environment.getProperty("soap.pwd") + "</wsse:Password>\n" +
                "            </wsse:UsernameToken>\n" +
                "        </wsse:Security>\n" +
                "    </soapenv:Header>\n" +
                "    <soapenv:Body>\n" +
                "        <id:GetIdentificationValidityRq>\n" +
                "            <id:Identification>\n" +
                "                <ns1:series>" + series + "</ns1:series>\n" +
                "                <ns1:number>" + number + "</ns1:number>\n" +
                "                <ns1:issueDate>" + dateIssue + "</ns1:issueDate>\n" +
                "            </id:Identification>\n" +
                "        </id:GetIdentificationValidityRq>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>\n";
        SOAPMessage msgInput = getSoapMessageFromString(xmlPassport);
        log.info("Soap passport request: ", msgInput);

        /**
         * Send SOAP message to id-sys for passport validate
         * */
        SOAPMessage messageOut = sendSOAP(environment.getRequiredProperty("url.passport"), msgInput);

        log.info("Soap passport response: ", messageOut);

        /**
         * Receive element with status from soap response "300"
         * */
        return messageOut.getSOAPBody().getElementsByTagName("ns6:code").item(0).getFirstChild().getNodeValue();
    }

    /**
     * Get result for snils with SOAP
     * login and password placed at soap.properties
     */
    public String resultSnils(String surname, String firstName, String patronymic, String birthDate, String snils, String gender) throws SOAPException, IOException, URISyntaxException {

        /**
         * Environment properties places at soap.properties
         * Do not use hardcode for placing params
         * */
        String xmlSnils = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:soapenv=\"soapenv\">\n" +
                "    <S:Header>\n" +
                "        <wsse:Security soapenv:mustUnderstand=\"1\">\n" +
                "            <wsse:UsernameToken wsu:Id=\"uuid_96cb9d66-2781-4989-b36b-583091729b25\" xmlns:ns13=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns14=\"http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512\">\n" +
                "                <wsse:Username>" + environment.getRequiredProperty("soap.user") + "</wsse:Username>\n" +
                "                <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">" + environment.getProperty("soap.pwd") + "</wsse:Password>\n" +
                "            </wsse:UsernameToken>\n" +
                "        </wsse:Security>\n" +
                "    </S:Header>\n" +
                "    <S:Body>\n" +
                "        <tns:LookupCustomersRq xmlns:tns=\"http://www.id-sys.ru/schemas/idbank/customer/2015/0.01/\" xmlns:id=\"http://www.id-sys.ru/schemas/idbank/common/2012/0.01/\">\n" +
                "            <tns:DataFilter>\n" +
                "                <tns:FilterItem>SNILS_VALIDITY</tns:FilterItem>\n" +
                "            </tns:DataFilter>\n" +
                "            <tns:Customers>\n" +
                "                <tns:Customer>\n" +
                "                    <tns:FIO>\n" +
                "                        <id:Surname>" + surname + "</id:Surname>\n" +
                "                        <id:FirstName>" + firstName + "</id:FirstName>\n" +
                "                        <id:Patronymic>" + patronymic + "</id:Patronymic>\n" +
                "                    </tns:FIO>\n" +
                "                    <tns:BirthDate>" + birthDate + "</tns:BirthDate>\n" +
                "                    <tns:SNILS>" + snils + "</tns:SNILS>\n" +
                "                    <tns:Gender>" + gender + "</tns:Gender>\n" +
                "                </tns:Customer>\n" +
                "            </tns:Customers>\n" +
                "        </tns:LookupCustomersRq>\n" +
                "    </S:Body>\n" +
                "</S:Envelope>";

        SOAPMessage msgInput = getSoapMessageFromString(xmlSnils);
        log.info("Soap snils request", msgInput);

        /**
         * Send SOAP message to id-sys for snils validate
         * */
        SOAPMessage messageOut = sendSOAP(environment.getRequiredProperty("url.snils"), msgInput);

        log.info("Soap snils response", messageOut);

        /**
         * Receive element with status from soap response with "VALID"
         * */
        return messageOut.getSOAPBody().getElementsByTagName("SNILSCheckResults").item(0).getAttributes()
                .getNamedItem("objectStatus").getNodeValue();
    }

}
