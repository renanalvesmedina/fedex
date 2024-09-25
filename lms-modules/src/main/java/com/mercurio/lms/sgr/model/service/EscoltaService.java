package com.mercurio.lms.sgr.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.sgr.model.Escolta;
import com.mercurio.lms.sgr.model.dao.EscoltaDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.escoltaService"
 */
public class EscoltaService extends CrudService<Escolta, Long> {
	private PessoaService pessoaService;
	
	/**
	 * Recupera uma instância de <code>Escolta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
	public Escolta findById(java.lang.Long id) {
		return (Escolta)super.findById(id);
	}

	/**
	 * Recupera uma instância de <code>Escolta</code> a partir do ID.
	 * E formata o número de identificação.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
	public Escolta findByIdDetalhamento(java.lang.Long id) {
		Escolta escolta = (Escolta)super.findById(id);
		escolta.getPessoa().setNrIdentificacao(
				FormatUtils.formatIdentificacao(
						escolta.getPessoa().getTpIdentificacao().getValue(),
						escolta.getPessoa().getNrIdentificacao()));
		return escolta;
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
	 * Remove a escolta e tenta remover a pessoa.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 * 
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removeEscoltaById(Long id) {
		this.removeById(id);
		try {
			pessoaService.removeById(id); 
		} catch (Exception e) {
			// ignora erros na pessoa
		}	
	}

	/**
	 * Remove as escoltas e tenta remover as pessoas.
	 *
	 * @param ids indica as entidades que deverão ser removidas.
	 * 
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeEscoltaByIds(List<Long> ids) {
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
	@Override
	public Escolta store(Escolta bean) {
		super.store(bean);
		return bean;
	}

	/**
	 * Retorna o Object antes de ele ser salvo
	 * 
	 * @return Object
	 */
	@Override
	protected Escolta beforeStore(Escolta bean) {
		pessoaService.store(bean);
		return super.beforeStore(bean);
	}

	@Override
	public ResultSetPage findPaginated(Map criteria) {
		criteria = this.removeMaskNrIdentificacao(criteria);
		ResultSetPage rsp = super.findPaginated(criteria);
		return rsp;
	}

	@Override
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
	public void setEscoltaDAO(EscoltaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private EscoltaDAO getEscoltaDAO() {
		return (EscoltaDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}