package com.mercurio.lms.rest.edi.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EdiTabelaOcorenDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idEdiTabelaOcoren;
    private String nmTabelaOcoren;
    @JsonIgnore
    private Boolean blWebservice;
    private String webService;

    public EdiTabelaOcorenDTO() {
    }

    public EdiTabelaOcorenDTO(Long idEdiTabelaOcoren, String nmTabelaOcoren, Boolean blWebservice) {
        this.idEdiTabelaOcoren = idEdiTabelaOcoren;
        this.nmTabelaOcoren = nmTabelaOcoren;
        this.blWebservice = blWebservice;
    }

    public Long getIdEdiTabelaOcoren() {
        return idEdiTabelaOcoren;
    }

    public void setIdEdiTabelaOcoren(Long idEdiTabelaOcoren) {
        this.idEdiTabelaOcoren = idEdiTabelaOcoren;
    }

    public String getNmTabelaOcoren() {
        return nmTabelaOcoren;
    }

    public void setNmTabelaOcoren(String nmTabelaOcoren) {
        this.nmTabelaOcoren = nmTabelaOcoren;
    }

    public void setBlWebservice(Boolean blWebservice) {
        this.blWebservice = blWebservice;
    }

    @JsonIgnore
    public Boolean getBlWebservice() {
        if(this.webService != null) {
            blWebservice = "S".equals(this.webService);
        }
        return blWebservice;
    }

    public String getWebService(){
        webService = "N";
       if (blWebservice != null){
           if(blWebservice){
               webService = "S";
           }
       }
       return webService;
    }

    public void setWebService(String webService) {
        this.webService = webService;
    }
}
