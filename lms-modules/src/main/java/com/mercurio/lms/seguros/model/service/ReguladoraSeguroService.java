package com.mercurio.lms.seguros.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.seguros.model.ReguladoraSeguro;
import com.mercurio.lms.seguros.model.dao.ReguladoraSeguroDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.reguladoraSeguroService"
 */
public class ReguladoraSeguroService extends CrudService<ReguladoraSeguro, Long> {
	private PessoaService pessoaService;

	/**
	 * Implementação publica dos métodos da CrudService por necessidade da Action
	 */
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Implementação publica dos métodos da CrudService por necessidade da Action
	 */
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Remove a reguladora e tenta remover a pessoa.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 * 
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removeReguladoraSeguroById(Long id) {
		this.removeById(id);
		try {
			pessoaService.removeById(id); 
		} catch (Exception e) {
			// ignora erros na pessoa
		}
	}

	/**
	 * Remove as reguladoras e tenta remover as pessoas.
	 *
	 * @param ids indica as entidades que deverão ser removidas.
	 * 
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeReguladoraSeguroByIds(List ids) {
		this.removeByIds(ids);
		try {
			pessoaService.removeByIds(ids); 
		} catch (Exception e) {
			// ignora erros na pessoa
		}	
	}
	
	/**
	 * Recupera uma instância de <code>ReguladoraSeguro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
	public ReguladoraSeguro findById(java.lang.Long id) {
		return (ReguladoraSeguro)super.findById(id);
	}

	/**
	 * Recupera uma instância de <code>ReguladoraSeguro</code> a partir do ID.
	 * E formata o número de identificação.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
	public ReguladoraSeguro findByIdDetalhamento(java.lang.Long id) {
		ReguladoraSeguro reguladoraSeguro = (ReguladoraSeguro)super.findById(id);
		reguladoraSeguro.getPessoa().setNrIdentificacao(FormatUtils.formatIdentificacao(reguladoraSeguro.getPessoa().getTpIdentificacao().getValue(), reguladoraSeguro.getPessoa().getNrIdentificacao()));
		return reguladoraSeguro;
	}


	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public ReguladoraSeguro store(ReguladoraSeguro bean) {
		super.store(bean);
		return bean;
	}

	protected ReguladoraSeguro beforeStore(ReguladoraSeguro bean) {
		ReguladoraSeguro reguladoraSeguro = (ReguladoraSeguro)bean;
		pessoaService.store(reguladoraSeguro);
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
	private Map removeMaskNrIdentificacao(Map criteria) {
		Map pessoa = (Map)criteria.get("pessoa");
		
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
	
	public List findCombo(String tpSituacao) {
		return getReguladoraSeguroDAO().findCombo(tpSituacao);
	}
	
	/**
	 * Método para retornar uma list ordenada.
	 * Utilizado em combobox.
	 * @param criteria
	 * @return
	 */
	public List findOrderByNmPessoa(Map criteria){
		List campoOrdenacao = new ArrayList();
		campoOrdenacao.add("pessoa_.nmPessoa:asc");
		return getDao().findListByCriteria(criteria, campoOrdenacao);
	}

	/**
	 * Método para retornar uma list ordenada.
	 * Utilizado em combobox.
	 * @param criteria
	 * @return
	 */
	public List findOrderByNmServicoLiberacaoPrestado(Map criteria){
		List campoOrdenacao = new ArrayList();
		campoOrdenacao.add("nmServicoLiberacaoPrestado:asc");
		return getDao().findListByCriteria(criteria, campoOrdenacao);
	}

	/**
	 * Obtém as reguladoras de seguro associadas ao controle de carga,
	 * buscando através de conhecimento nacional e internacional.<br>
	 * Filtra os endereços, pois vêm de quatro relacionamentos diferentes,
	 * que podem possuir as mesmas reguladoras de seguro, trazendo apenas
	 * os não repetidos.
	 * @param idControleCarga
	 * @return
	 * @author luisfco
	 */
	public List findEmailReguladoraByIdControleCarga(Long idControleCarga, YearMonthDay data) {
		List mailList = getReguladoraSeguroDAO().findEmailReguladoraByIdControleCarga(idControleCarga, data);
		List filteredList = new ArrayList();

		for (Iterator it = mailList.iterator();it.hasNext(); ) {
			Object obj = it.next();
			if (!filteredList.contains(obj))
				filteredList.add(obj);
		}

		return filteredList;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setReguladoraSeguroDAO(ReguladoraSeguroDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ReguladoraSeguroDAO getReguladoraSeguroDAO() {
		return (ReguladoraSeguroDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}