package com.mercurio.lms.pendencia.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.pendencia.model.LiberacaoBloqueio;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.LiberacaoBloqueioService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.pendencia.manterOcorrenciasLiberacaoOcorrenciasBloqueioAction"
 */

public class ManterOcorrenciasLiberacaoOcorrenciasBloqueioAction extends CrudAction {

	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	
	public Serializable storeLiberacaoBloqueio(TypedFlatMap tfm) {
		LiberacaoBloqueio liberacaoBloqueio = new LiberacaoBloqueio();
		liberacaoBloqueio.setIdLiberacaoBloqueio(tfm.getLong("idLiberacaoBloqueio"));
		
		OcorrenciaPendencia liberacao = this.ocorrenciaPendenciaService.findById(tfm.getLong("ocorrenciaPendenciaByIdOcorrenciaLiberacao.idOcorrenciaPendencia"));
		liberacaoBloqueio.setOcorrenciaPendenciaByIdOcorrenciaLiberacao(liberacao);
		
		OcorrenciaPendencia bloqueio = this.ocorrenciaPendenciaService.findById(tfm.getLong("ocorrenciaPendenciaByIdOcorrenciaBloqueio.idOcorrenciaPendencia"));
		liberacaoBloqueio.setOcorrenciaPendenciaByIdOcorrenciaBloqueio(bloqueio);

		return getService().store(liberacaoBloqueio);
	}
	
	public ResultSetPage findPaginatedCustom(TypedFlatMap tfm) {
		return getService().findPaginatedLiberacaoBloqueio(tfm);
	}

	public TypedFlatMap findByIdCustom(Long idBloqueioLiberacao) {
		
		LiberacaoBloqueio liberacaoBloqueio = getService().findById(idBloqueioLiberacao);

		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("idLiberacaoBloqueio", liberacaoBloqueio.getIdLiberacaoBloqueio());
		tfm.put("ocorrenciaPendenciaByIdOcorrenciaBloqueio.idOcorrenciaPendencia", liberacaoBloqueio.getOcorrenciaPendenciaByIdOcorrenciaBloqueio().getIdOcorrenciaPendencia());
		tfm.put("ocorrenciaPendenciaByIdOcorrenciaBloqueio.cdOcorrencia", liberacaoBloqueio.getOcorrenciaPendenciaByIdOcorrenciaBloqueio().getCdOcorrencia());
		tfm.put("ocorrenciaPendenciaByIdOcorrenciaBloqueio.dsOcorrencia", liberacaoBloqueio.getOcorrenciaPendenciaByIdOcorrenciaBloqueio().getDsOcorrencia());
		tfm.put("ocorrenciaPendenciaByIdOcorrenciaLiberacao.idOcorrenciaPendencia", liberacaoBloqueio.getOcorrenciaPendenciaByIdOcorrenciaLiberacao().getIdOcorrenciaPendencia());
		tfm.put("ocorrenciaPendenciaByIdOcorrenciaLiberacao.cdOcorrencia", liberacaoBloqueio.getOcorrenciaPendenciaByIdOcorrenciaLiberacao().getCdOcorrencia());
		tfm.put("ocorrenciaPendenciaByIdOcorrenciaLiberacao.dsOcorrencia", liberacaoBloqueio.getOcorrenciaPendenciaByIdOcorrenciaLiberacao().getDsOcorrencia());
		
		return tfm;
	}

	public List findLookupOcorrenciaPendencia(TypedFlatMap tfm) {
		return this.ocorrenciaPendenciaService.findLookupOcorrenciaPendencia(tfm);
	}
	
	public void removeById(java.lang.Long id) {
        this.getService().removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	this.getService().removeByIds(ids);
    }
	
	public void setService(LiberacaoBloqueioService liberacaoBloqueioService) {
		super.defaultService = liberacaoBloqueioService;
	}
	
	private LiberacaoBloqueioService getService() {
		return (LiberacaoBloqueioService) super.defaultService;
	}

	public void setOcorrenciaPendenciaService(
			OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}
	

}
