package com.mercurio.lms.tabelaprecos.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tabelaprecos.model.Observacao;
import com.mercurio.lms.tabelaprecos.model.dao.ObservacaoDAO;

public class ObservacaoService  extends CrudService<Observacao, Long>{

	private static final String TABELA_PRECO = "TABELA_PRECO";
	private static final String ERRO_TABELA = "LMS-30084";
	
	public String findObservacao(Long idTabela){
		
		List<Observacao> tabelaList = ((ObservacaoDAO)getDao()).findByIdTabelaPreco(idTabela);
		
		if(tabelaList.size() == 0){
			return "";
		}
		
		if(tabelaList.size() > 1){
			throw new BusinessException(ERRO_TABELA);
		}
		
		return tabelaList.get(0).getDescricao();
	}
	
	
	public Serializable store(String descricao, Long idTabela) {

		List<Observacao> obsList = ((ObservacaoDAO)getDao()).findByIdTabelaPreco(idTabela);

		if(obsList!=null && obsList.size() > 1){
			throw new BusinessException(ERRO_TABELA);
		}

		removeObservacao(obsList);

		if(descricao == null || descricao.equals("")){
			return null;
		}

		Observacao obs = new Observacao();
		obs.setIdTabela(idTabela);
		obs.setNomeTabela(TABELA_PRECO);
		obs.setDescricao(descricao);

		return super.store(obs);
	}


	private void removeObservacao(List<Observacao> obsList) {
		if(obsList!=null && obsList.size() == 1){
			super.removeById(obsList.get(0).getId());
		}
	}

	public void setObservacaoDAO(ObservacaoDAO dao) {
		setDao(dao);
	}
}
