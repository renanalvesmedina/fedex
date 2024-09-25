package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.Concessionaria;
import com.mercurio.lms.municipios.model.dao.ConcessionariaDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.concessionariaService"
 */
public class ConcessionariaService extends CrudService<Concessionaria, Long> {
	private PessoaService pessoaService;
	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Recupera uma instância de <code>Concessionaria</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Concessionaria findById(java.lang.Long id) {
		return (Concessionaria)super.findById(id); 	
	}

	public List findLookup(Map criteria) {
		String nrIdentificacao = MapUtils.getString(criteria, "pessoa.nrIdentificacao");
		if (!StringUtils.isBlank(nrIdentificacao)) {
			criteria.put("pessoa.nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));
		}
		return super.findLookup(criteria);
	}

	/**
	 * Sobrescrito método que retorna um resultSet para a grid.
	 * Retorna no XML apenas os campos que serão visualizados no grid.
	 * Retira máscara do múmero de identificação antes de realizar a consulta. 
	 */
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = super.findPaginated(criteria);

		FilterResultSetPage frsp = new FilterResultSetPage(rsp) {
			public Map filterItem(Object item) {
				Concessionaria concessionaria = (Concessionaria)item;
				Pessoa pessoa = (Pessoa)concessionaria.getPessoa();
				Map row = new TypedFlatMap();

				row.put("idConcessionaria",concessionaria.getIdConcessionaria());
				row.put("pessoa.tpIdentificacao",pessoa.getTpIdentificacao());
				String nrIdentificacao = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(),pessoa.getNrIdentificacao());
				row.put("pessoa.nrIdentificacaoFormatado",nrIdentificacao);
				row.put("pessoa.nrIdentificacao",pessoa.getNrIdentificacao());
				row.put("pessoa.nmPessoa",pessoa.getNmPessoa());
				row.put("dsHomePage",concessionaria.getDsHomePage());
				row.put("pessoa.dsEmail",pessoa.getDsEmail());
				row.put("tpSituacao",concessionaria.getTpSituacao());
				return row;
			}
		};
		return (ResultSetPage)frsp.doFilter();
	} 

	public List findLookupConcessionaria(String nrIdentificacao) {
		PessoaUtils.validateIdentificacao(nrIdentificacao);
		return getConcessionariaDAO().findLookupConcessionaria(nrIdentificacao);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades quee deverão ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeByIds(List ids) {
		for (Iterator iterIds = ids.iterator(); iterIds.hasNext();) {
			Long id = (Long) iterIds.next();
			this.removeConcessionariaById(id);
		}
	}

	/**
	 * Remove a concessionaria e tenta remover a pessoa.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 * 
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removeConcessionariaById(Long id) {
		this.removeById(id);
		try {
			pessoaService.removeById(id); 
		} catch (Exception e) {
			// ignora erros de FK na pessoa
		}	
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	// FIXME corrigir para retornar o ID
	public Concessionaria store(Concessionaria bean) {
		Concessionaria concessionaria = (Concessionaria) bean;
		pessoaService.store(concessionaria);

		super.store(bean);
		return concessionaria;
	}

	/**
	 * Verifica se a pessoa especificada é Jurídica.
	 * Em uma inclusão: Verifica se existe outra pessoa com o mesmo tipo e número de identificação informados.
	 * @param criteria
	 * @return
	 */
	public List validateIdentificacao(TypedFlatMap map) {
		String tpIdentificacao = map.getString("tpIdentificacao");
		String nrIdentificacao = map.getString("nrIdentificacao");

		List retorno = new ArrayList(1);
		Pessoa pessoa = validateEspecializacao(Concessionaria.class, tpIdentificacao, nrIdentificacao);
		if (pessoa != null) {
			TypedFlatMap row = new TypedFlatMap();
			row.put("idPessoa",pessoa.getIdPessoa());
			row.put("nmPessoa",pessoa.getNmPessoa());
			row.put("dsEmail",pessoa.getDsEmail());
			row.put("tpIdentificacao",pessoa.getTpIdentificacao());
			row.put("nrIdentificacao",pessoa.getNrIdentificacao());
			retorno.add(row);
		}
		return retorno;
	}

	public Pessoa validateEspecializacao(Class clazz, String tpIdentificacao, String nrIdentificacao) {
		Pessoa pessoa = configuracoesFacade.getPessoa(nrIdentificacao,tpIdentificacao);

		if (pessoa != null) {
			Object pessoaEspecializada = configuracoesFacade.getPessoa(pessoa.getIdPessoa(),clazz,false);
			if (pessoaEspecializada != null) {
				throw new BusinessException("LMS-29002");
			}
		}
		return pessoa;
	}

	/**
	 * Retorna 'true' se a pessoa informada é uma concessionaria ativa senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isConcessionaria(Long idPessoa){
		return getConcessionariaDAO().isConcessionaria(idPessoa);
	} 

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setConcessionariaDAO(ConcessionariaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ConcessionariaDAO getConcessionariaDAO() {
		return (ConcessionariaDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}