package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.ObjectNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.vendas.model.Despachante;
import com.mercurio.lms.vendas.model.dao.DespachanteDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.despachanteService"
 */
public class DespachanteService extends CrudService<Despachante, Long> {
	private PessoaService pessoaService;
	
	/**
	 * Recupera uma instância de <code>Despachante</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public Despachante findById(Long id) {
		Despachante despachante = (Despachante)super.findById(id);
		String nrIdentificacao = FormatUtils.formatIdentificacao(despachante.getPessoa().getTpIdentificacao(), despachante.getPessoa().getNrIdentificacao());
		despachante.getPessoa().setNrIdentificacao(nrIdentificacao);
		//CUIDADO: NÃO APAGAR LINHAS ABAIXO
		/*
		 * @author José Rodrigo Moraes
		 * @since 24/07/2006
		 * 
		 * Linhas adicionadas para garantir que despachantes sem tpIdentifacao e nrIdentificacao
		 * sejam detalhados corretamente. (Necessário devido ao funcionamento da tag de identificação)
		 */
		if(despachante.getPessoa().getTpIdentificacao() == null) {
			despachante.getPessoa().setTpIdentificacao(new DomainValue(""));
			if(despachante.getPessoa().getNrIdentificacao() == null) {
				despachante.getPessoa().setNrIdentificacao("");
			}
		}
		return despachante;
	}

	public Integer getRowCount(Map criteria) {
		Map<String, Object> pessoa = (Map<String, Object>) criteria.get("pessoa");
		String nrIdentificacao = (String)pessoa.get("nrIdentificacao");
		if(StringUtils.isNotBlank(nrIdentificacao)) {
			pessoa.put("nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));
		}
		return super.getRowCount(criteria);
	}

	public ResultSetPage findPaginated(Map criteria) {
		Map<String, Object> pessoa = (Map<String, Object>) criteria.get("pessoa");
		String nrIdentificacao = (String)pessoa.get("nrIdentificacao");
		if(StringUtils.isNotBlank(nrIdentificacao)) {
			pessoa.put("nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));
		}
		return super.findPaginated(criteria);
	}

	/**
	 * Retorna os despachantes com número de telefone.
	 * CUIDADO: existe ume regra que retorna um telefone 
	 * ou o outro
	 *
	 * @param criteria Map de valores da tela.
	 * @return List de despachantes
	 */
	public List findLookupWithPhone(TypedFlatMap criteria) {
		return getDespachanteDAO().findLookupByCriteriaWithPhone(criteria);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * @param id
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removeDespachanteById(Long id) {
		this.removeById(id);
		try {
			pessoaService.removeById(id);	
		} catch (Exception e){
			//ignora de Pessoa
		}
	}

	/**
	 * @param id
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeDespachanteByIds(List<Long> ids) {
		this.removeByIds(ids);
		for(Long id : ids) {
			try {
				pessoaService.removeById(id);
			}catch(Exception e) {
				//sem tratamentos, simplesmente ignora. Pois, provavelmente, há dependencias da Pessoa.
			}
		}
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(final List<Long> ids) {
		super.removeByIds(ids);
	}

	/*
	 * Seta a data atual no campo dhInclusao
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeInsert(java.lang.Object)
	 */
	@Override
	protected Despachante beforeInsert(Despachante bean) {
		bean.getPessoa().setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		validateNovoDespachante(bean.getPessoa().getIdPessoa());
		return super.beforeInsert(bean);
	}

	/**
	 * Salva pessoa antes de salvar despachante
	 * @param bean entidade a ser armazenada (Despachante)
	 * @return entidade que foi armazenada. 
	 */
	protected Despachante beforeStore(Despachante bean) {
		if (bean.getIdDespachante()== null) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (bean.getPessoa().getNrIdentificacao() != null && !bean.getPessoa().getNrIdentificacao().equals( "" )) {
				map.put("nrIdentificacao",bean.getPessoa().getNrIdentificacao());
				map.put("tpIdentificacao",bean.getPessoa().getTpIdentificacao().getValue());				
			}
		}

		//Executar o beforeStore padrão (beforeInsert e beforeUpdate)
		bean = (Despachante)super.beforeStore(bean);

		//Salvar a pessoa
		pessoaService.store(bean);

		return bean;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public HashMap<String, Object>store(Despachante bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> mapRetorno = new HashMap<String, Object>();

		// os eventos before podem ter criado um objeto diferente para
		// ser persistido portando salva este objeto.
		Object storeBean = beforeStore(bean);
		this.getDespachanteDAO().store(storeBean);

		// retorna o Id do POJO salvo ou atualizado.
		Serializable idDespachante = this.getDespachanteDAO().getIdentifier(storeBean);

		mapRetorno.put("idDespachante", idDespachante);
		if (((Despachante)storeBean).getPessoa().getDhInclusao() != null) {
			map.put("dhInclusao", ((Despachante)storeBean).getPessoa().getDhInclusao());
		}
		map.put("idPessoa",idDespachante);		
		mapRetorno.put("pessoa", map);		

		return mapRetorno; 
	}

	/**
	 * Verifica existência de Pessoa.
	 * É lançada uma Exception se despachante já estiver cadastrado.
	 * 
	 * @param Long idDespachante
	 */
	public void validateNovoDespachante(Long idDespachante){
		if (idDespachante != null){
			Despachante despachante = null;
			try {
				despachante = findById(idDespachante);
			} catch (ObjectNotFoundException e){				
			}

			if (despachante != null) {
				throw new BusinessException("LMS-27022");
			}
		}
	}

	/**
	 * Retorna 'true' se a pessoa informada é um despachante ativo senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isDespachante(Long idPessoa){
		return getDespachanteDAO().isDespachante(idPessoa);
	} 	

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private DespachanteDAO getDespachanteDAO() {
		return (DespachanteDAO) getDao();
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setDespachanteDAO(DespachanteDAO dao) {
		setDao( dao );
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

}