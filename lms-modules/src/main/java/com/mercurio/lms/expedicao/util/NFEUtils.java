package com.mercurio.lms.expedicao.util;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;

public class NFEUtils {
    
	private static final Integer POSICAO_INICIAL_NR_NOTA = 25;
	private static final Integer POSICAO_FINAL_NR_NOTA = 34;
	
    public static String[] getDestinatarios(DoctoServico doctoServico) {
        
        List<String> toReturn = new ArrayList<String>();
        
        if (doctoServico.getDevedorDocServs() != null) {

            for (DevedorDocServ devedor: doctoServico.getDevedorDocServs()) {
                
                if (devedor.getCliente().getPessoa().getContatosByIdPessoaContatado() != null) {
                    
                    for (Object o: devedor.getCliente().getPessoa().getContatosByIdPessoaContatado()) {
                        Contato contato = (Contato)o;
                        if ("CT".equals(contato.getTpContato().getValue())) {
                            if (org.apache.commons.lang.StringUtils.isNotBlank(contato.getDsEmail())) {
                                toReturn.add(contato.getDsEmail());
                            }
                        }
                    }
                    
                    
                }
                if (toReturn.isEmpty()) {
                    if (org.apache.commons.lang.StringUtils.isNotBlank(devedor.getCliente().getPessoa().getDsEmail())) {
                        toReturn.add(devedor.getCliente().getPessoa().getDsEmail());
                    }
                }
                
                
            }
            
        }
        
        return toReturn.toArray(new String[] {});
        
    }
    
    /**
     * Retorna o número da nota fiscal contido na chave da NFe.
     * 
     * @param nrChave
     * @return
     */
    public static String getNumeroNotaFiscalByChave(String nrChave){
    	if(nrChave != null){
    		return nrChave.substring(POSICAO_INICIAL_NR_NOTA, POSICAO_FINAL_NR_NOTA);
    	} else{
    		return nrChave;
    	}
    	
    }

}
