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
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.operadoraCartaoPedagioService"
 */
public class OperadoraCartaoPedagioService extends CrudService<OperadoraCartaoPedagio, Long> {
	private PessoaService pessoaService;

	/**
	 * Recupera uma instância de <code>OperadoraCartaoPedagio</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public OperadoraCartaoPedagio findById(java.lang.Long id) {
		return (OperadoraCartaoPedagio)super.findById(id);
	}

	/**
	 * Recupera uma instância de <code>OperadoraCartaoPedagio</code> a partir do ID.
	 * E formata o número de identificação.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
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
	 * Implementação publica dos métodos da CrudService por necessidade da Action
	 */
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Implementação publica dos métodos da CrudService por necessidade da Action
	 */
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Remove a operadora e tenta remover a pessoa.
	 *
	 * @param id indica a entidade que deverá ser removida.
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
	 * @param ids indica as entidades que deverão ser removidas.
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
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
	 * Sobrescrito método que retorna um resultSet para a grid.
	 * Retira máscara do múmero de identificação antes de realizar a consulta.
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
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setOperadoraCartaoPedagioDAO(OperadoraCartaoPedagioDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private OperadoraCartaoPedagioDAO getOperadoraCartaoPedagioDAO() {
		return (OperadoraCartaoPedagioDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}