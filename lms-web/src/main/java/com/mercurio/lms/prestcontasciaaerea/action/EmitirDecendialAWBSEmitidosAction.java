package com.mercurio.lms.prestcontasciaaerea.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.prestcontasciaaerea.model.service.FaturamentoCiaAereaService;
import com.mercurio.lms.prestcontasciaaerea.report.EmitirDecendialAWBsEmitidosService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.prestcontasciaaerea.emitirDecendialAWBSEmitidosAction"
 */

public class EmitirDecendialAWBSEmitidosAction extends ReportActionSupport {
    
    private EmpresaService empresaService;
    private ClienteService clienteService;
    private FilialService filialService;
    private FaturamentoCiaAereaService faturamentoCiaAereaService;


	/**
     * Setter da service padr�o deste relat�rio
	 * @param emitirDecendialAWBsEmitidosService
	 */
	public void setService(EmitirDecendialAWBsEmitidosService emitirDecendialAWBsEmitidosService) {
		this.reportServiceSupport = emitirDecendialAWBsEmitidosService;
	}
    
    /**
     * Busca as Empresas para a combo de Cia A�rea
     * @param criterios Crit�rios de pesquisa
     * @return Lista de empresas encontradas na pesquisa
     */
    public List findComboEmpresas(TypedFlatMap criterios){        
        String tpEmpresa = criterios.getString("empresa.tpEmpresa");
        return this.getEmpresaService().findByTpEmpresaTpSituacao(tpEmpresa,null);
    }
    
    /**
     * Busca os Clientes para a lookup de Remetentes do AWB
     * @param criterios Crit�rios de Pesquisa
     * @return Lista de clientes encontrados na pesquisa
     */
    public List findLookupCliente(TypedFlatMap criterios){
    	
    	List clientes = this.getClienteService().findLookupSimplificado(criterios.getString("pessoa.nrIdentificacao"),null);
        List retorno = new ArrayList();
		
    	for (Iterator iter = clientes.iterator(); iter.hasNext();) {
    			
    		Cliente element = (Cliente) iter.next();
    			
    		TypedFlatMap map = new TypedFlatMap();
    		map.put("pessoa.nrIdentificacao",element.getPessoa().getNrIdentificacao());
    		map.put("idCliente",element.getIdCliente());
    		map.put("pessoa.nmPessoa",element.getPessoa().getNmPessoa());
    		map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(element.getPessoa().getTpIdentificacao(),
    																				   element.getPessoa().getNrIdentificacao()));
    			
    		retorno.add(map);
    			
    	}
    		
    	return retorno;
    }
    
    /**
     * Busca as filiais para a lookup de Filial prestadora de contas
     * @param criterios Crit�rios de Pesquisa
     * @return Lista de filiais encontradas na pesquisa
     */
    public List findLookupFilial(TypedFlatMap criterios) {
        return this.getFilialService().findLookupBySgFilial(criterios);
    }
    
        
    /**
	 * Define a Data Final do Periodo de vendas, conforme data inicial.<BR>
	 * @param map
	 * @return
	 */
	public String findDataFinal(TypedFlatMap map){		
		return getFaturamentoCiaAereaService().findDtFinalPeriodoFatura(map.getLong("empresa.idEmpresa"), map.getLong("filial.idFilial"), map.getYearMonthDay("dtInicial"), "M").toString("dd/MM/yyyy");
	}

    /**
     * @return Returns the empresaService.
     */
    public EmpresaService getEmpresaService() {
        return empresaService;
    }

    /**
     * @param empresaService The empresaService to set.
     */
    public void setEmpresaService(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    /**
     * @return Returns the clienteService.
     */
    public ClienteService getClienteService() {
        return clienteService;
    }

    /**
     * @param clienteService The clienteService to set.
     */
    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * @return Returns the filialService.
     */
    public FilialService getFilialService() {
        return filialService;
    }

    /**
     * @param filialService The filialService to set.
     */
    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

	public FaturamentoCiaAereaService getFaturamentoCiaAereaService() {
		return faturamentoCiaAereaService;
}

	public void setFaturamentoCiaAereaService(
			FaturamentoCiaAereaService faturamentoCiaAereaService) {
		this.faturamentoCiaAereaService = faturamentoCiaAereaService;
	}
    
	/**
	 * Busca a filial do usu�rio logado 
	 * @return
	 */
	public Filial findFilialUsuarioLogado() {
		return SessionUtils.getFilialSessao(); 
	}

}
