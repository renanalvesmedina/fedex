package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class DestinoPropostaRegiaoDTO extends BaseDTO {
    private static final long serialVersionUID = 5167553016937590374L;
    
    private String dsRegiao;
    
    public DestinoPropostaRegiaoDTO(){
        super();
    }
    
    public DestinoPropostaRegiaoDTO(Long id,String dsRegiao) {
        super();
        this.setId(id);
        this.dsRegiao = dsRegiao;
    }

    public String getDsRegiao() {
        return dsRegiao;
    }
    public void setDsRegiao(String dsRegiao) {
        this.dsRegiao = dsRegiao;
    }
    
    
}
