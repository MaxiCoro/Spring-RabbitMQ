package com.prueba.pruebaAPI.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.pruebaAPI.dominio.Comprobante;
import com.prueba.pruebaAPI.services.ComprobanteService;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/obtenerCAE")
public class ComprobanteController {

    @Autowired
    private ComprobanteService comprobanteService;

    public ComprobanteController() {
        this.comprobanteService = new ComprobanteService();
    }
    
    @GetMapping("/getCAE")
    public Comprobante obtenerCae(@RequestBody Comprobante comprobante) {
        return comprobanteService.obtenerJson(comprobante);
    }

}
