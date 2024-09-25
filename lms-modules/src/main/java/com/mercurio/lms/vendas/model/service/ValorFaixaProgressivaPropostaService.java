package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.DestinoProposta;
import com.mercurio.lms.vendas.model.ValorFaixaProgressivaProposta;
import com.mercurio.lms.vendas.model.dao.ValorFaixaProgressivaPropostaDAO;

public class ValorFaixaProgressivaPropostaService extends CrudService<ValorFaixaProgressivaProposta, Long> {

	public ValorFaixaProgressivaProposta findById(java.lang.Long id) {
		return (ValorFaixaProgressivaProposta) super.findById(id);
	}

	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
	    
        while (ids.size() >= 1000){
            List removeList = ids.subList(ids.size() - 1000, ids.size());
            super.removeByIds(removeList);
            ids.removeAll(removeList);
        }
	    
		super.removeByIds(ids);
	}

	public java.io.Serializable store(ValorFaixaProgressivaProposta bean) {
		return super.store(bean);
	}

	public void setValorFaixaProgessivaPropostaDAO(ValorFaixaProgressivaPropostaDAO dao) {
		setDao(dao);
	}

	private ValorFaixaProgressivaPropostaDAO getValorFaixaProgressivaPropostaDAO(){
	    return (ValorFaixaProgressivaPropostaDAO)getDao();
	}
	
    public List<ValorFaixaProgressivaProposta> findByDestinoPropostaFretePeso(DestinoProposta destinoProposta) {
        return getValorFaixaProgressivaPropostaDAO().findByDestinoPropostaFretePeso(destinoProposta);
    }

    public List<ValorFaixaProgressivaProposta> findByDestinoPropostaProdutoEspecifico(DestinoProposta destinoProposta) {
        return getValorFaixaProgressivaPropostaDAO().findByDestinoPropostaProdutoEspecifico(destinoProposta);
    }

    public List<ValorFaixaProgressivaProposta> findByDestinoProposta(DestinoProposta destinoProposta) {
        return getValorFaixaProgressivaPropostaDAO().findByDestinoProposta(destinoProposta);
    }
    
}
