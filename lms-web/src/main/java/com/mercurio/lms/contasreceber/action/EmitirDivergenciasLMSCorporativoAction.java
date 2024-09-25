package com.mercurio.lms.contasreceber.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.report.EmitirDivergenciasLmsCorporativoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.emitirDivergenciasLMSCorporativoAction"
 */

public class EmitirDivergenciasLMSCorporativoAction extends ReportActionSupport {

	/** Inversion of control - Spring (DocumentosFaturarService) */
	public void setEmitirDivergenciasLmsCorporativoService(EmitirDivergenciasLmsCorporativoService emitirDivergenciasLmsCorporativoService) {
		this.reportServiceSupport = emitirDivergenciasLmsCorporativoService;
	}
	
	private FilialService filialService;
	public void setFilialService(FilialService filialService){
		this.filialService = filialService;
	}
	
	/**
     * Busca a filial do usuario logado
     * @return filial
     */
    public Map findFilialUsuario(){
    	
    	TypedFlatMap mapRetorno = new TypedFlatMap();
    	
    	mapRetorno.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
    	mapRetorno.put("sgFilial", SessionUtils.getFilialSessao().getSgFilial());
    	mapRetorno.put("nmFantasia", SessionUtils.getFilialSessao().getPessoa().getNmFantasia());
    	mapRetorno.put("blDisableFilial", !SessionUtils.getFilialSessao().getSgFilial().equals("MTZ"));

    	return mapRetorno;
    } 
    
    public List findLookupFilial(TypedFlatMap criteria) {
		return filialService.findLookup(criteria);
	}
    
    /**
     * M�todo respons�vel por buscar a data atual e decrescer um dia a mesma
     * @return Data atual menos um dia
     */
    public Object getDataAtualMenosUmDia(){
    	TypedFlatMap tfm = new TypedFlatMap();
    	
    	//Se � domingo, deixar assim
    	if (JTDateTimeUtils.getDataHoraAtual().getDayOfWeek() == 7){
    		tfm.put("emissaoAte", JTDateTimeUtils.getDataAtual());
    	} else {
    		//Voltar o dia para o �ltimo domingo
    		tfm.put("emissaoAte", JTDateTimeUtils.getDataAtual().minusDays(JTDateTimeUtils.getDataHoraAtual().getDayOfWeek()));	
    	}
    	
    	return tfm;   	
    }
	
}
