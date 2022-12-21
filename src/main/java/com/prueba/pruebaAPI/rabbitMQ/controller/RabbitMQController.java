package com.prueba.pruebaAPI.rabbitMQ.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.pruebaAPI.dominio.Comprobante;
import com.prueba.pruebaAPI.rabbitMQ.services.RabbitMQSender;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping(value = "/rabbit")
public class RabbitMQController {

    @Autowired
    RabbitMQSender rabbitMQSender;

    public RabbitMQController() {
    }

    @GetMapping("/productor")
    public ResponseEntity<?> productor(@RequestBody Comprobante comprobante) {
        Map<String, Object> respuesta = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        
        Comprobante informacion = rabbitMQSender.send(comprobante);

        if (informacion != null) {
            respuesta.put("Token Recibido", informacion.getAuth().getToken());
            respuesta.put("Sign Recibida", informacion.getAuth().getSign());

            if (informacion.getURL() != null) {
                respuesta.put("URL", informacion.getURL());
            }
            
            if(informacion.getErrores() != null){
                if (!informacion.getErrores().equals("")) {
                    respuesta.put("Errores", informacion.getErrores());
                }
            }
            
            
            try {
                JsonNode nodoRespuesta = mapper.readTree(generateJsonFromXML(informacion.getLogRespuesta()));
                
                //Crear Json de errores
                if(nodoRespuesta.get("soap:Envelope").get("soap:Body")
                        .get("FECAESolicitarResponse").get("FECAESolicitarResult").has("Errors")){
                    respuesta.put("Error", nodoRespuesta.get("soap:Envelope")
                                                        .get("soap:Body").get("FECAESolicitarResponse")
                                                        .get("FECAESolicitarResult").get("Errors"));
                }
                
                //Crear Json de respuesta exitosa
                if (nodoRespuesta.get("soap:Envelope").get("soap:Body")
                        .get("FECAESolicitarResponse").get("FECAESolicitarResult").has("FeDetResp")) {
                    respuesta.put("Resultado", nodoRespuesta.get("soap:Envelope")
                                                        .get("soap:Body").get("FECAESolicitarResponse")
                                                        .get("FECAESolicitarResult"));
                    
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            respuesta.put("Error", "Error en la generacion de factura");
        }

        return new ResponseEntity(respuesta, HttpStatus.OK);
    }

    private String generateJsonFromXML(String log) {
        JSONObject xmlJsonObj = XML.toJSONObject(log);
        String json = xmlJsonObj.toString();
        System.out.println(json);
        return json;
    }

}
