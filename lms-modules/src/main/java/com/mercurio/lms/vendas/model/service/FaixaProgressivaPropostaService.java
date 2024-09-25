package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.FaixaProgressivaProposta;
import com.mercurio.lms.vendas.model.dao.FaixaProgressivaPropostaDAO;

public class FaixaProgressivaPropostaService extends CrudService<FaixaProgressivaProposta, Long> {
	
	public FaixaProgressivaProposta findById(java.lang.Long id) {
		return (FaixaProgressivaProposta)super.findById(id);
	}
	
	public BigDecimal findVlFaixaProgressivaProposta(Long idParametroCliente, Long idSimulacao, BigDecimal psReferencia) {
		return getFaixaProgressivaPropostaDAO().findVlFaixaProgressivaProposta(idParametroCliente, idSimulacao, psReferencia);
	}
	
	public BigDecimal findVlFaixaProgressivaByIdProdutoEspecifico(Long idParametroCliente, Long idSimulacao, Long idProdutoEspecifico) {
		return getFaixaProgressivaPropostaDAO().findVlFaixaProgressivaByIdProdutoEspecifico(idParametroCliente, idProdutoEspecifico, idSimulacao);
	}

	public List<Map> findByIdSimulacao(Long idSimulacao) {
		return getFaixaProgressivaPropostaDAO().findByIdSimulacao(idSimulacao);
	}
	
	public void removeByIdsNaoUtilizados(List<Long> idsFaixaProgressivaProposta) {
	    while (idsFaixaProgressivaProposta.size() >= 1000){
            List removeList = idsFaixaProgressivaProposta.subList(idsFaixaProgressivaProposta.size() - 1000, idsFaixaProgressivaProposta.size());
            getFaixaProgressivaPropostaDAO().removeByIdsNaoUtilizados(removeList);
            idsFaixaProgressivaProposta.removeAll(removeList);
        }
	    
		getFaixaProgressivaPropostaDAO().removeByIdsNaoUtilizados(idsFaixaProgressivaProposta);
	}
	
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public java.io.Serializable store(FaixaProgressivaProposta bean) {
		return super.store(bean);
	}

	public void setFaixaProgressivaPropostaDAO(FaixaProgressivaPropostaDAO dao) {
		setDao( dao );
	}

	public FaixaProgressivaPropostaDAO getFaixaProgressivaPropostaDAO() {
		return (FaixaProgressivaPropostaDAO) getDao();
	}

	

}
