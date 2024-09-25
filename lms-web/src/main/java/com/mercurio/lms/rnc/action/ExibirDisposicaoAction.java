package com.mercurio.lms.rnc.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.rnc.model.AcaoCorretiva;
import com.mercurio.lms.rnc.model.service.AcaoCorretivaService;
import com.mercurio.lms.rnc.model.service.DisposicaoService;

/**
 * Generated by: ADSM ActionGenerator 
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.rnc.exibirDisposicaoAction"
 */

public class ExibirDisposicaoAction extends CrudAction {

	private DisposicaoService disposicaoService;
	private AcaoCorretivaService acaoCorretivaService;
	
	public DisposicaoService getDisposicaoService() {
		return disposicaoService;
	}

	public void setDisposicaoService(DisposicaoService disposicaoService) {
		this.disposicaoService = disposicaoService;
	}
    
    public AcaoCorretivaService getAcaoCorretivaService() {
		return acaoCorretivaService;
	}

	public void setAcaoCorretivaService(AcaoCorretivaService acaoCorretivaService) {
		this.acaoCorretivaService = acaoCorretivaService;
	}

	/**
     * Retorna um TypedFlatMap  contendo o dados necessarios para popular a tela.
     * 
     * @param criteria
     * @return
     */
    public TypedFlatMap findDisposicaoByIdOcorrenciaNaoConformidade(TypedFlatMap criteria) {
    	Long idOcorrenciaNaoConformidade = criteria.getLong("idOcorrenciaNaoConformidade");
    	TypedFlatMap map = this.getDisposicaoService().findDisposicaoByIdOcorrenciaNaoConformidade(idOcorrenciaNaoConformidade);
    	List acoesCorretivas = acaoCorretivaService.findAcoesCorretivasByIdOcorrenciaNC(idOcorrenciaNaoConformidade);
    	List retornoAcoes = new ArrayList();
    	for (Iterator iter = acoesCorretivas.iterator(); iter.hasNext();) {
			Map mapAcaoCorretiva = new TypedFlatMap();
    		AcaoCorretiva acaoCorretiva = (AcaoCorretiva) iter.next();
    		mapAcaoCorretiva.put("idAcaoCorretiva", acaoCorretiva.getIdAcaoCorretiva());
    		mapAcaoCorretiva.put("dsAcaoCorretiva" , acaoCorretiva.getDsAcaoCorretiva());
    		retornoAcoes.add(mapAcaoCorretiva);
		}
    	map.put("acoesCorretivas", retornoAcoes);
    	return map;
    }
}
