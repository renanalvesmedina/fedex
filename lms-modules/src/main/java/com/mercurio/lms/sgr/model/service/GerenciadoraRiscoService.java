package com.mercurio.lms.sgr.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.sgr.model.GerenciadoraRisco;
import com.mercurio.lms.sgr.model.dao.GerenciadoraRiscoDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.gerenciadoraRiscoService"
 */
public class GerenciadoraRiscoService extends CrudService<GerenciadoraRisco, Long> {
	private PessoaService pessoaService;

	/**
	 * Recupera uma instância de <code>GerenciadoraRisco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
	public GerenciadoraRisco findById(java.lang.Long id) {
		return (GerenciadoraRisco)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(GerenciadoraRisco bean) {
		return super.store(bean);
	}

	/**
	 * Faz validações antes de chamar o método store() 
	 */
	@Override
	protected GerenciadoraRisco beforeStore(GerenciadoraRisco bean) {
		// Remove os espaços em branco
		if (bean.getDsEnderecoWeb()!=null) {
			bean.setDsEnderecoWeb(bean.getDsEnderecoWeb().trim());
		}

		pessoaService.store(bean);
		return super.beforeStore(bean);
	}

	/**
	 * Sobrescrito método que retorna um resultSet para a grid.
	 * Retira máscara do múmero de identificação antes de realizar a consulta. 
	 */
	public ResultSetPage findPaginated(Map criteria) {
		criteria = this.removeMaskNrIdentificacao(criteria);
		return super.findPaginated(criteria);
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
		if(pessoa != null) {
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
	 * Método que sobreescreve o findById para setar o nrIdentificacao com 
	 * a mascara na tela 
	 * de detalhamento 
	 * @see findById.
	 */
	public GerenciadoraRisco findByIdDetalhamento(java.lang.Long id) {
		GerenciadoraRisco gerenciadoraRisco = (GerenciadoraRisco) findById(id);
		gerenciadoraRisco.getPessoa().setNrIdentificacao(
				FormatUtils.formatIdentificacao(
						gerenciadoraRisco.getPessoa().getTpIdentificacao().getValue(),
						gerenciadoraRisco.getPessoa().getNrIdentificacao()));

		return gerenciadoraRisco;
	}

	/**
	 * Remove a seguradora e tenta remover a pessoa.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 * 
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removeGerenciadoraById(Long id) {
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
	public void removeGerenciadorasByIds(List<Long> ids) {
		this.removeByIds(ids);
		try {
			pessoaService.removeByIds(ids); 
		} catch (Exception e) {
			// ignora erros na pessoa
		}
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setGerenciadoraRiscoDAO(GerenciadoraRiscoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private GerenciadoraRiscoDAO getGerenciadoraRiscoDAO() {
		return (GerenciadoraRiscoDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}