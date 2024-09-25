package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.NfDadosComp;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;


/**
 * Classe de serviço para CRUD:
 * <p>
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.expedicao.digitarDadosNotaNormalNotasFiscaisRefaturadasService"
 */
public class DigitarDadosNotaNormalNotasFiscaisRefaturadasService {
    
    private NotaFiscalConhecimentoService notaFiscalConhecimentoService;

    public Map storeInSession(Map parameters) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        return result;
    }
    
    public List<Map<String, Object>> findNotasFiscaisDisponiveisByIdDoctoServico(Long idDoctoServico) {
       return notaFiscalConhecimentoService.findNotasFiscaisDisponiveisByIdDoctoServico(idDoctoServico);
    }
    
    public void setNotaFiscalConhecimentoService(
            NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
        this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
    }
}