package com.mercurio.lms.carregamento.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio;
import com.mercurio.lms.carregamento.model.dao.OperadoraCartaoPedagioDAO;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;

/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.operadoraCartaoPedagioService"
 */
public class OperadoraCartaoPedagioService extends CrudService<OperadoraCartaoPedagio, Long> {
	private PessoaService pessoaService;

	/**
	 * Recupera uma inst�ncia de <code>OperadoraCartaoPedagio</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public OperadoraCartaoPedagio findById(java.lang.Long id) {
		return (OperadoraCartaoPedagio)super.findById(id);
	}

	/**
	 * Recupera uma inst�ncia de <code>OperadoraCartaoPedagio</code> a partir do ID.
	 * E formata o n�mero de identifica��o.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public OperadoraCartaoPedagio findByIdDetalhamento(java.lang.Long id) {
		OperadoraCartaoPedagio operadoraCartaoPedagio = (OperadoraCartaoPedagio)super.findById(id);
		operadoraCartaoPedagio.getPessoa().setNrIdentificacao(
				FormatUtils.formatIdentificacao(
					operadoraCartaoPedagio.getPessoa().getTpIdentificacao().getValue(),
					operadoraCartaoPedagio.getPessoa().getNrIdentificacao()
				)
			);
		return operadoraCartaoPedagio;
	}

	/**
	 * Implementa��o publica dos m�todos da CrudService por necessidade da Action
	 */
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Implementa��o publica dos m�todos da CrudService por necessidade da Action
	 */
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Remove a operadora e tenta remover a pessoa.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 * 
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removeOperadoraCartaoPedagioById(Long id) {
		this.removeById(id);
		try {	
			pessoaService.removeById(id);
		} catch (Exception e) {
			// ignora erros na pessoa
		}	
	}

	/**
	 * Remove as operadoras e tenta remover as pessoas.
	 *
	 * @param ids indica as entidades que dever�o ser removidas.
	 * 
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeOperadoraCartaoPedagioByIds(List<Long> ids) {
		this.removeByIds(ids);
		try {
			pessoaService.removeByIds(ids); 
		} catch (Exception e) {
			// ignora erros na pessoa
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public OperadoraCartaoPedagio store(OperadoraCartaoPedagio bean) {
		super.store(bean);
		return bean;
	}

	@Override
	protected OperadoraCartaoPedagio beforeStore(OperadoraCartaoPedagio bean) {
		pessoaService.store(bean);
		return super.beforeStore(bean);
	}

	/**
	 * Sobrescrito m�todo que retorna um resultSet para a grid.
	 * Retira m�scara do m�mero de identifica��o antes de realizar a consulta.
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginated(Map criteria) {
		criteria = this.removeMaskNrIdentificacao(criteria);
		return super.findPaginated(criteria);
	}

	/**
	 * 
	 * @param criteria
	 * @param campoOrdenacao
	 * @return
	 */
	public List findListByCriteria(Map<String, Object> criteria, List campoOrdenacao) {
		return getOperadoraCartaoPedagioDAO().findListByCriteria(criteria, campoOrdenacao);
	}

	/**
	 *
	 * @param criteria
	 * @return
	 */
	public Integer getRowCount(Map criteria) {
		criteria = this.removeMaskNrIdentificacao(criteria);
		return super.getRowCount(criteria);
	}
	
	/**
	 * Remove a mascara do nrIdentificacao
	 * 
	 * @param criteria
	 * @return
	 */
	private Map<String, Object> removeMaskNrIdentificacao(Map<String, Object> criteria) {
		Map<String, Object> pessoa = (Map<String, Object>)criteria.get("pessoa");
		if (pessoa!=null) {
			String nrIdentificacao = (String)pessoa.get("nrIdentificacao");
			if (nrIdentificacao != null && !nrIdentificacao.equals("") && nrIdentificacao.length() > 1) {
				String nrSemMascara = PessoaUtils.clearIdentificacao(nrIdentificacao);
				pessoa.put("nrIdentificacao",nrSemMascara);
				criteria.put("pessoa", pessoa);
			}
		}
		return criteria;
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setOperadoraCartaoPedagioDAO(OperadoraCartaoPedagioDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private OperadoraCartaoPedagioDAO getOperadoraCartaoPedagioDAO() {
		return (OperadoraCartaoPedagioDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}