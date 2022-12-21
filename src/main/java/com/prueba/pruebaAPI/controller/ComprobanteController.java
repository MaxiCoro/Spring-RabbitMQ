package com.prueba.pruebaAPI.controller;

import com.prueba.pruebaAPI.dominio.Comprobante;
import com.prueba.pruebaAPI.services.ComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
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
