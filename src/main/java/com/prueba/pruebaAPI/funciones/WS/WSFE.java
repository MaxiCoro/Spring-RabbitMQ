package com.prueba.pruebaAPI.funciones.WS;

import com.prueba.pruebaAPI.dominio.Comprobante;
import com.prueba.pruebaAPI.validacion.*;
import com.prueba.pruebaAPI.funciones.WSAA.*;
import com.prueba.pruebaAPI.funciones.generacionPDF.GeneracionPDF;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WSFE {

    private String resultadoFinal;

    public WSFE() {
        this.resultadoFinal = "";
    }

    public synchronized Comprobante conexion(Comprobante informacion) {
        Validacion validacion = new Validacion(informacion);
        FECAESolicitar feCAE = new FECAESolicitar();
        ConnectAndResponseWSAA con = new ConnectAndResponseWSAA();
        String[] resWSAA = null;
        String[] responseFeCAESolicitar = null;
        GeneracionPDF generacionPDF = new GeneracionPDF();

        boolean resValidacion = false;
        try {
            resValidacion = validacion.validar();
        } catch (ParseException ex) {
            Logger.getLogger(WSFE.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (resValidacion) {
            System.out.println("validacion true");
            //verificar token and sign
            if (informacion.getAuth().getToken().isEmpty() || informacion.getAuth().getSign().isEmpty()) {
                System.out.println("Token y sign vacios");
                //llamar al wsaa
                resWSAA = con.obtenerRespuesta();
                if (resWSAA != null) {
                    //resWSAA[3] exptime tiene diferente formato -> 2002­01­01T00:00:02­03:00
                    //armar json response (agregar token, sign y exptime)
                    informacion.getAuth().setToken(resWSAA[0]);
                    System.out.println("getToken: " + informacion.getAuth().getToken());
                    informacion.getAuth().setSign(resWSAA[1]);
                    System.out.println("getSign: " + informacion.getAuth().getSign());
                    responseFeCAESolicitar = feCAE.callSoapWebService(informacion);
                    if (informacion.getGenerarPdf()) {
                        if (!responseFeCAESolicitar[5].equals("")) {
                            try {
                                generacionPDF.generarPDF(informacion, responseFeCAESolicitar);
                                //generacionPDF.generarPDF(informacion, responseFeCAESolicitar);
                                informacion.setUrl(generacionPDF.getUrlPublica());
                            } catch (IOException ex) {
                                Logger.getLogger(WSFE.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            informacion.setErrores("Error en la generacion del PDF, no se obtuvo la respuesta esperada por parte del WS");
                        }
                    }
                    //generar respuesta json
                    informacion.setLog(responseFeCAESolicitar[0]);
                } else {
                    validacion.agregarError("\" Error \" : \"El CEE ya posee un Ticket de Acceso valido para el WS solicitado\"");
                    if (validacion.getError().size() > 0) {
                        if (validacion.getError().size() == 1) {
                            resultadoFinal = "{ " + validacion.getError().get(0) + " }";
                        } else {
                            resultadoFinal = "{";
                            for (int i = 0; i < validacion.getError().size() - 1; i++) {
                                resultadoFinal += (validacion.getError().get(i) + ",");
                                System.out.println(resultadoFinal);
                            }
                            resultadoFinal += validacion.getError().get(validacion.getError().size() - 1);
                            resultadoFinal += "}";
                        }
                        informacion.setErrores(resultadoFinal);
                    }
                }
            } else {
                //verificar que tEnvio no esta caducado
                LocalDateTime tEnvio = LocalDateTime.parse(informacion.getAuth().getTEnvio());
                LocalDateTime dateTime = LocalDateTime.now();
                if (dateTime.isBefore(tEnvio)) {
                    responseFeCAESolicitar = feCAE.callSoapWebService(informacion);
                    System.out.println("Tiempo correcto");
                    System.out.println("Solicita PDF: " + informacion.getGenerarPdf());
                    if (informacion.getGenerarPdf()) {
                        if (!responseFeCAESolicitar[5].equals("")) {
                            try {
                                generacionPDF.generarPDF(informacion, responseFeCAESolicitar);
                                //generacionPDF.generarPDF(informacion, responseFeCAESolicitar);
                                informacion.setUrl(generacionPDF.getUrlPublica());
                            } catch (IOException ex) {
                                Logger.getLogger(WSFE.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            informacion.setErrores("Error en la generacion del PDF, no se obtuvo la respuesta esperada por parte del WS");
                        }
                    }
                    informacion.setLog(responseFeCAESolicitar[0]);
                } else {
                    //llamar al wsaa
                    resWSAA = con.obtenerRespuesta();
                    if (resWSAA != null) {
                        //resWSAA[3] exptime tiene diferente formato
                        //armar json response (agregar token, sign y exptime)
                        informacion.getAuth().setToken(resWSAA[0]);
                        informacion.getAuth().setSign(resWSAA[1]);
                        responseFeCAESolicitar = feCAE.callSoapWebService(informacion);
                        System.out.println("Solicita PDF: " + informacion.getGenerarPdf());
                        if (!responseFeCAESolicitar[5].equals("")) {
                            try {
                                generacionPDF.generarPDF(informacion, responseFeCAESolicitar);
                                //generacionPDF.generarPDF(informacion, responseFeCAESolicitar);
                                informacion.setUrl(generacionPDF.getUrlPublica());
                            } catch (IOException ex) {
                                Logger.getLogger(WSFE.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            informacion.setErrores("Error en la generacion del PDF, no se obtuvo la respuesta esperada por parte del WS");
                        }
                        System.out.println("Tiempo incorrecto");
                    } else {
                        validacion.agregarError("\" Error \" : \"El CEE ya posee un Ticket de Acceso valido para el WS solicitado\"");
                        if (validacion.getError().size() > 0) {
                            if (validacion.getError().size() == 1) {
                                resultadoFinal = "{ " + validacion.getError().get(0) + " }";
                            } else {
                                resultadoFinal = "{";
                                for (int i = 0; i < validacion.getError().size() - 1; i++) {
                                    resultadoFinal += (validacion.getError().get(i) + ",");
                                    System.out.println(resultadoFinal);
                                }
                                resultadoFinal += validacion.getError().get(validacion.getError().size() - 1);
                                resultadoFinal += "}";
                                informacion.setErrores(resultadoFinal);
                            }
                        }
                    }

                }
            }
        } else {
            if (validacion.getError().size() > 0) {
                if (validacion.getError().size() == 1) {
                    resultadoFinal = "{ " + validacion.getError().get(0) + " }";
                } else {
                    resultadoFinal = "{";
                    for (int i = 0; i < validacion.getError().size() - 1; i++) {
                        resultadoFinal += (validacion.getError().get(i) + ",");
                        System.out.println(resultadoFinal);
                    }
                    resultadoFinal += validacion.getError().get(validacion.getError().size() - 1);
                    resultadoFinal += "}";
                }
                informacion.setErrores(resultadoFinal);
            }
        }
        return informacion;
    }

}
