package com.prueba.pruebaAPI.dominio;

import javax.persistence.Entity;

@Entity
public class Comprobante {

    private Auth auth;
    private FeCabReq feCabReq;
    private Empresa empresa;
    private Cliente cliente;
    private DetalleFactura[] detalleFactura;
    private boolean generarPdf;
    private FeDetReq feDetReq;
    private PeriodoFacturado periodoFacturado;
    private String url;
    private String logRespuesta;
    private String errores;
    
    public Comprobante() {
    }

    public Comprobante(Auth auth, FeCabReq feCabReq, Empresa empresa, Cliente cliente, DetalleFactura[] detalleFactura, boolean generarPDF, FeDetReq feDetReq, PeriodoFacturado periodoFacturado) {
        this.auth = auth;
        this.feCabReq = feCabReq;
        this.empresa = empresa;
        this.cliente = cliente;
        this.detalleFactura = detalleFactura;
        this.generarPdf = generarPDF;
        this.feDetReq = feDetReq;
        this.periodoFacturado = periodoFacturado;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth value) {
        this.auth = value;
    }

    public FeCabReq getFeCabReq() {
        return feCabReq;
    }

    public void setFeCabReq(FeCabReq value) {
        this.feCabReq = value;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa value) {
        this.empresa = value;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente value) {
        this.cliente = value;
    }

    public DetalleFactura[] getDetalleFactura() {
        return detalleFactura;
    }

    public void setDetalleFactura(DetalleFactura[] value) {
        this.detalleFactura = value;
    }

    public boolean getGenerarPdf() {
        return generarPdf;
    }

    public void setGenerarPdf(boolean value) {
        this.generarPdf = value;
    }

    public FeDetReq getFeDetReq() {
        return feDetReq;
    }

    public void setFeDetReq(FeDetReq value) {
        this.feDetReq = value;
    }

    public long getFacturaDetalleAlicIVA(int indice) {
        return detalleFactura[indice].getAlicuotaIVA();
    }

    @Override
    public String toString(){
        return "Token: " + auth.getToken() + "\n" +
               "Sign: " + auth.getSign() + "\n" +
               "Cuit: " + auth.getCuit()+ "\n" +
               "tEnvio: " + auth.getTEnvio()+ "\n";
    }

    public PeriodoFacturado getPeriodoFacturado() {
        return periodoFacturado;
    }

    public void setPeriodoFacturado(PeriodoFacturado periodoFacturado) {
        this.periodoFacturado = periodoFacturado;
    }
    
    public String getURL(){
        return url;
    }
    
    public void setUrl(String url){
        this.url = url;
    }
    
    public String getLogRespuesta(){
        return logRespuesta;
    }
    
    public void setLog(String log){
        this.logRespuesta = log;
    }
    
    public String getErrores(){
        return errores;
    }
    
    public void setErrores(String errores){
        this.errores = errores;
    }
}
