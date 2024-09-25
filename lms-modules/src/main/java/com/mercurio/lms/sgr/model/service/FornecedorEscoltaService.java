package com.mercurio.lms.sgr.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.sgr.model.FornecedorEscolta;
import com.mercurio.lms.sgr.model.FornecedorEscoltaImpedido;
import com.mercurio.lms.sgr.model.FranquiaFornecedorEscolta;
import com.mercurio.lms.sgr.model.dao.FornecedorEscoltaDAO;

public class FornecedorEscoltaService extends CrudService<FornecedorEscolta, Long> {

	PessoaService pessoaService;

	public Integer getRowCount(TypedFlatMap criteria) {
		return getFornecedorEscoltaDAO().getRowCountByFilter(criteria);
	}

	public List<FornecedorEscolta> find(TypedFlatMap criteria) {
		return getFornecedorEscoltaDAO().findByFilter(criteria);
	}

	public List<FornecedorEscolta> findSuggest(String value) {
		return getFornecedorEscoltaDAO().findSuggest(value);
	}

	@Override
	public FornecedorEscolta findById(Long id) {
		return (FornecedorEscolta) super.findById(id);
	}

	@Override
	protected FornecedorEscolta beforeStore(FornecedorEscolta bean) {
		Pessoa pessoa = pessoaService.findByNrIdentificacao(bean.getPessoa().getNrIdentificacao());
		if (pessoa != null) {
			pessoa.setNmPessoa(bean.getPessoa().getNmPessoa());
			bean.setPessoa(pessoa);
		} else {
			pessoaService.store(bean.getPessoa());
		}
		return super.beforeStore(bean);
	}

	@Override
	public Long store(FornecedorEscolta bean) {
		return (Long) super.store(bean);
	}

	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	public Integer getRowCountFranquia(TypedFlatMap criteria) {
		return getFornecedorEscoltaDAO().getRowCountFranquiaByIdFornecedorEscolta(criteria);
	}
	
	public Integer getRowCountBlacklist(TypedFlatMap criteria) {
		return getFornecedorEscoltaDAO().getRowCountFranquiaByIdFornecedorEscolta(criteria);
	}

	public List<FranquiaFornecedorEscolta> findFranquia(TypedFlatMap criteria) {
		return getFornecedorEscoltaDAO().findFranquiaByIdFornecedorEscolta(criteria);
	}

	public FranquiaFornecedorEscolta findFranquiaById(Long id) {
		return getFornecedorEscoltaDAO().findFranquiaById(id);
	}

	public void storeFranquia(FranquiaFornecedorEscolta franquia) {
		getFornecedorEscoltaDAO().storeFranquia(franquia);
	}

	public void removeFranquiaByIds(List<Long> ids) {
		getFornecedorEscoltaDAO().removeFranquiaByIds(ids);
	}

	public List<FornecedorEscoltaImpedido> findBlacklist(TypedFlatMap criteria) {
		return getFornecedorEscoltaDAO().findBlacklistByIdFornecedorEscolta(criteria);
	}
	
	public void storeBlacklist(FornecedorEscoltaImpedido impedido) {
		getFornecedorEscoltaDAO().store(impedido);
	}

	public void removeBlacklistByIds(List<Long> ids) {
		getFornecedorEscoltaDAO().removeBlacklistByIds(ids);
	}

	private FornecedorEscoltaDAO getFornecedorEscoltaDAO() {
		return (FornecedorEscoltaDAO) getDao();
	}

	public void setFornecedorEscoltaDAO(FornecedorEscoltaDAO dao) {
		setDao(dao);
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

}
