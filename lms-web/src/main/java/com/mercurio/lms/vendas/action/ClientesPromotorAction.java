package com.mercurio.lms.vendas.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.report.ClientesPromotorService;
/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.clientesPromotorAction"
 */

public class ClientesPromotorAction extends ReportActionSupport  {
    
    private RegionalService regionalService;
    private FilialService filialService;
    private RegionalFilialService regionalFilialService;
	
	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
	}

	public void setClientesPromotorService(ClientesPromotorService clientesPromotorService) {
		this.reportServiceSupport = clientesPromotorService;
	}
    
    /**
     * Busca as regionais para o combo de regionais
     * @param criterios
     * @return Lista de regionais
     */
    public List findComboRegional(Map criterios){
    	List filiais = filialService.findFiliaisByUsuarioEmpresa(SessionUtils.getUsuarioLogado(), SessionUtils.getEmpresaSessao(), null);
        return this.getRegionalService().findRegionaisByFiliais(filiais);
    }
    
    /**
     * Busca as filiais para a lookup de filial
     * @param criterios Crit�rios de pesquisa
     * @return Lista de filiais
     */
    public List findLookupBySgFilial(TypedFlatMap tfm){
        return this.getFilialService().findLookupBySgFilial(tfm.getString("sgFilial"),tfm.getString("tpAcesso"));
    } 

    /**
     * @return Returns the regionalService.
     */
    public RegionalService getRegionalService() {
        return regionalService;
    }

    /**
     * @param regionalService The regionalService to set.
     */
    public void setRegionalService(RegionalService regionalService) {
        this.regionalService = regionalService;
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
    
    /**
     * Busca os funcion�rios promotores de acordo com os crit�rios passados por par�metro
     * @param map Crit�rios de pesquisa para funcion�rios promotores
     * @return Lista contendo os funcion�rios promotores encontrados na pesquisa
     */
    public List findLookupFuncionarioPromotor(Map map){
    	return null;
    }
    
    /**
	 * Busca a filial e a regional do usu�rio logado
	 * 
	 * @author Jos� Rodrigo Moraes
	 * @since 18/12/2006
	 * @param tfm Neste caso sem nenhum crit�rio
	 * @return 
	 */
	public TypedFlatMap findFilialAndRegionalUsuario(TypedFlatMap tfm){
		
		TypedFlatMap map = new TypedFlatMap();
		
		Filial filial = SessionUtils.getFilialSessao();
		
		map.put("filial.idFilial",filial.getIdFilial());
		map.put("filial.sgFilial",filial.getSgFilial());
		map.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
		 
		Regional regional = regionalFilialService.findLastRegionalVigente(filial.getIdFilial());
		
		map.put("regional.idRegional",regional.getIdRegional());
		map.put("regional.sgRegional",regional.getSgRegional());
		map.put("regional.siglaDescricao",regional.getSiglaDescricao());
		
		return map;
	}
   
}
