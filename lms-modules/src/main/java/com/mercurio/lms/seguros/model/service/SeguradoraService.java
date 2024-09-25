package com.mercurio.lms.seguros.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.seguros.model.Seguradora;
import com.mercurio.lms.seguros.model.dao.SeguradoraDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.seguradoraService"
 */
public class SeguradoraService extends CrudService<Seguradora, Long> {
	private PessoaService pessoaService;

	/**
	 * Implementação publica dos métodos da CrudService por necessidade da Action
	 */
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
	/**
	 * Implementação publica dos métodos da CrudService por necessidade da Action
	 */
	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Remove a seguradora e tenta remover a pessoa.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 * 
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removeSeguradoraById(Long id) {
		this.removeById(id);
		try {
			pessoaService.removeById(id); 
		} catch (Exception e) {
			// ignora erros na pessoa
		}	
	}

	/**
	 * Remove as seguradoras e tenta remover as pessoas.
	 *
	 * @param ids indica as entidades que deverão ser removidas.
	 * 
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeSeguradoraByIds(List<Long> ids) {
		this.removeByIds(ids);
		try {
			pessoaService.removeByIds(ids); 
		} catch (Exception e) {
			// ignora erros na pessoa
		}	
	}
	
	/**
	 * Recupera uma instância de <code>Seguradora</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
	public Seguradora findById(java.lang.Long id) {
		return (Seguradora)super.findById(id);
	}

	/**
	 * Recupera uma instância de <code>Seguradora</code> a partir do ID.
	 * E formata o número de identificação.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
	public Seguradora findByIdDetalhamento(java.lang.Long id) {
		Seguradora seguradora = (Seguradora)super.findById(id);
		seguradora.getPessoa().setNrIdentificacao(FormatUtils.formatIdentificacao(seguradora.getPessoa().getTpIdentificacao().getValue(), seguradora.getPessoa().getNrIdentificacao()));
		return seguradora;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Seguradora store(Seguradora bean) {
		super.store(bean);
		return bean;
	}

	@Override
	protected Seguradora beforeStore(Seguradora bean) {
		pessoaService.store(bean);
		return super.beforeStore(bean);
	}

	/**
	 * Sobrescrito método que retorna um resultSet para a grid.
	 * Retira máscara do múmero de identificação antes de realizar a consulta. 
	 */
	public ResultSetPage findPaginated(Map criteria) {
		criteria = this.removeMaskNrIdentificacao(criteria);
		ResultSetPage rsp = super.findPaginated(criteria);
		return rsp;
	}
	
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
		if (pessoa != null) {
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
	 * Método para retornar uma list ordenada.
	 * Utilizado em combobox.
	 * @param criteria
	 * @return
	 */
	public List findOrderByNmPessoa(Map<String, Object> criteria){
		List<String> campoOrdenacao = new ArrayList<String>(1);
		campoOrdenacao.add("pessoa_.nmPessoa:asc");
		return getDao().findListByCriteria(criteria, campoOrdenacao);
	}

	@Override
	public List findLookup(Map criteria) {
		Map<String, Object> pessoa = (Map<String, Object>)criteria.get("pessoa");
		pessoa.put("nrIdentificacao", PessoaUtils.validateIdentificacao(MapUtils.getString(pessoa, "nrIdentificacao")));
		return super.findLookup(criteria);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setSeguradoraDAO(SeguradoraDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private SeguradoraDAO getSeguradoraDAO() {
		return (SeguradoraDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}