package com.mercurio.lms.expedicao.model.service;

import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.conhecimentoExcluirService"
 */

public class ConhecimentoExcluirService {
	private ConhecimentoService conhecimentoService;
	private FilialService filialService;
	
	public void removePreCTRC(Long idConhecimento, Long idFilial) {
		Conhecimento cto = conhecimentoService.findByIdInitLazyProperties(idConhecimento, false);
		if (!"P".equals(cto.getTpSituacaoConhecimento().getValue())) {
			throw new BusinessException("LMS-04086");
		}
		Filial filialUL = filialService.findFilialUsuarioLogado();
		if (!filialUL.getIdFilial().equals(idFilial)) {
			throw new BusinessException("LMS-04087");
		}

		conhecimentoService.removeAllObjectsRelatedWithConhecimento(idConhecimento);
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return conhecimentoService.findPaginatedExcluirPreCtrc(criteria);
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return conhecimentoService.getRowCountExcluirPreCtrc(criteria);
	}

	public Map findById(Long id) {
		return conhecimentoService.findByIdExcluirPreCtrc(id);	
	}

}
