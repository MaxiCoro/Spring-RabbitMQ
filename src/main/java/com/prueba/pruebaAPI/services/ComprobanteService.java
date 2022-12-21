package com.prueba.pruebaAPI.services;

import com.prueba.pruebaAPI.dominio.Comprobante;
import com.prueba.pruebaAPI.funciones.WS.WSFE;
import org.springframework.stereotype.Service;

@Service
public class ComprobanteService {
    
    private WSFE wsfe;
    
    public Comprobante obtenerJson(Comprobante comprobante){
        wsfe = new WSFE();
        return wsfe.conexion(comprobante);
    }
    
    
}
