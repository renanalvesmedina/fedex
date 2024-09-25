package com.mercurio.lms.expedicao.swt.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.service.DigitarDadosNotaNormalNotasFiscaisRefaturadasService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.swt.DigitarDadosNotaNormalNotasFiscaisRefaturadaAction"
 */
public class DigitarDadosNotaNormalNotasFiscaisRefaturadasAction extends CrudAction {

    private DigitarDadosNotaNormalNotasFiscaisRefaturadasService digitarDadosNotaNormalNotasFiscaisRefaturadasService;

	/**
     * Armazena os campos adicionais preenchidos
     * na lista de campos adicionais do conhecimento que se 
     * encontra na sess�o
     * @param parameters
     */
    public Map storeInSession(Map parameters) {
    	return digitarDadosNotaNormalNotasFiscaisRefaturadasService.storeInSession(parameters);
	}
    
    public List<Map<String, Object>> findNotasFiscaisDisponiveisByIdDoctoServico(Long idDoctoServico) {
        List<Map<String, Object>> result = digitarDadosNotaNormalNotasFiscaisRefaturadasService.findNotasFiscaisDisponiveisByIdDoctoServico(idDoctoServico);
        for (Map nota : result) {
            nota.put("blRefaturarNf", false);
        }
        return result;
    }
    
    public void setDigitarDadosNotaNormalNotasFiscaisRefaturadasService(
            DigitarDadosNotaNormalNotasFiscaisRefaturadasService digitarDadosNotaNormalNotasFiscaisRefaturadasService) {
        this.digitarDadosNotaNormalNotasFiscaisRefaturadasService = digitarDadosNotaNormalNotasFiscaisRefaturadasService;
    }
}
