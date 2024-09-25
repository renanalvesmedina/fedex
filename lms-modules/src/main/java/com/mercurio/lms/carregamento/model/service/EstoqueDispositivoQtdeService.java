package com.mercurio.lms.carregamento.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.EstoqueDispositivoQtde;
import com.mercurio.lms.carregamento.model.dao.EstoqueDispositivoQtdeDAO;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.estoqueDispositivoQtdeService"
 */
public class EstoqueDispositivoQtdeService extends CrudService<EstoqueDispositivoQtde, Long> {
	private ParametroGeralService parametroGeralService;
	private EmpresaService empresaService;
	private FilialService filialService;

	/**
	 * Parametro do sistema que serve para buscar nrIdenificação da Mercúrio,
	 * conforme especificação 05.01.02.06
	 */
	private static final String PARAMETRO_SISTEMA = "NR_IDENTIFICACAO_MERCURIO";

	/**
	 * Recupera uma instância de <code>EstoqueDispositivoQtde</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
	public EstoqueDispositivoQtde findById(Long id) {
		return (EstoqueDispositivoQtde)super.findById(id);
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
	 * @param ids lista com as entidades que deverão ser removida.
	 */
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
	public java.io.Serializable store(EstoqueDispositivoQtde bean) {
		return super.store(bean);
	}


	

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setEstoqueDispositivoQtdeDAO(EstoqueDispositivoQtdeDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private EstoqueDispositivoQtdeDAO getEstoqueDispositivoQtdeDAO() {
		return (EstoqueDispositivoQtdeDAO) getDao();
	}

	/**
	 * Verifica se o usuario tem permissão para a filial selecionada. 
	 */
	@Override
	protected EstoqueDispositivoQtde beforeStore(EstoqueDispositivoQtde bean) {
		if(bean.getFilial()!=null){
			if (!SessionUtils.isFilialAllowedByUsuario(bean.getFilial())) {
				throw new BusinessException("LMS-00050");
			}
		}
		return super.beforeStore(bean);
	}

	/**
	 * Realiza as validações necessárias antes da
	 * inserção de um novo registro 
	 */
	@Override
	protected EstoqueDispositivoQtde beforeInsert(EstoqueDispositivoQtde bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("filial.idFilial", bean.getFilial().getIdFilial());
		map.put("tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao", bean.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao());

		List<EstoqueDispositivoQtde> searchReturn = find(map);
		for(EstoqueDispositivoQtde oldBean : searchReturn) {
			if (oldBean != null) 
				// Verifica se a filial é a mesma
				if(oldBean.getFilial().getIdFilial().longValue() == bean.getFilial().getIdFilial().longValue() )
					// Verifica se a empresa que mantem o tipo dispositivo é a mesma
					if(oldBean.getEmpresa().getIdEmpresa().longValue() == bean.getEmpresa().getIdEmpresa().longValue() )
						// Verifica se o Tipo Dispositivo é o mesmo
						if(oldBean.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao().longValue() == bean.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao().longValue() ) 
							throw new BusinessException("LMS-05003");
		}
		return super.beforeInsert(bean);
	}

	/**
	 * Faz a pesquisa padrão da tela, agora sem restrição de data.
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedWithDateRestriction(Map<String, Object> criteria) {
		Map<String, Object> filialMap = (Map<String, Object>) criteria.get("filial");
		String idFilial = (String)filialMap.get("idFilial");

		if (!idFilial.trim().equals("") && idFilial.length() > 0 ) {
			Filial filial = new Filial();
			filial.setIdFilial(Long.valueOf(idFilial));
			if (!SessionUtils.isFilialAllowedByUsuario(filial)) {
				throw new BusinessException("LMS-00050");
			} 
		}
		return getEstoqueDispositivoQtdeDAO().findPaginatedWithDateRestriction(criteria, FindDefinition.createFindDefinition(criteria));
	}

	/**
	 * 
	 * Este método é utilizado em conjunto com o findPaginated para obter o total
	 * de linhas que a consulta retorna e calcula o total de paginas.
	 * Retorna o total de páginas baseado na restrição de data,
	 * conforme especificação 05.01.02.06
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountWithDateRestriction(Map<String, Object> criteria) {
		return getEstoqueDispositivoQtdeDAO().getRowCountWithDateRestriction(criteria);
	}

	/**
	 * Método para verificar se o usuario logado tem acesso a filial selecionada 
	 */
	public List findLookupFiliaisPorUsuario(Map<String, Object> map) {
		return filialService.findLookupBySgFilial((String)map.get("sgFilial"),(String)map.get("tpAcesso"));
	}
	
	/**
	 * Este método irá retornar a empresa Mercurio como 
	 * campo default para a lookup de empresa da página de cad 
	 */
	public TypedFlatMap findEmpresaDefault(Map<String, Object> map) {
		// Criado critério de pesquisa para buscar o nrIdentificacao da empresa
		// Realiza a pesquisa baseado no nome do parametro
		map = new HashMap<String, Object>();

		TypedFlatMap tfm = null;
		Empresa empresa = null;

		map.put("nmParametroGeral",PARAMETRO_SISTEMA);
		ParametroGeral parametroGeral = null;
		try {
			parametroGeral = parametroGeralService.findByNomeParametro("PARAMETRO_SISTEMA", false);
		} catch (BusinessException e) {}
		if (parametroGeral != null) {
			map = new HashMap<String, Object>();
			// Aninha as propriedades, no caso {pessoa{nrIdentificacao=parametroGeral.getDsConteudo()}}
			Map<String, Object> pessoa = new HashMap<String, Object>(2);

			pessoa.put("nrIdentificacao", parametroGeral.getDsConteudo());
			map.put("pessoa",pessoa);

			List<Empresa> empresaList = empresaService.find(map);
			if (empresaList!=null && !empresaList.isEmpty()) {
				empresa = empresaList.get(0);
				 tfm = new TypedFlatMap();
				//Copia od dados utilizados da empresa para um typedFlatMap de retorno
				tfm.put("empresa.idEmpresa", empresa.getIdEmpresa());
				tfm.put("empresa.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(
						empresa.getPessoa().getTpIdentificacao(),
						empresa.getPessoa().getNrIdentificacao()));
				tfm.put("empresa.pessoa.nmPessoa", empresa.getPessoa().getNmPessoa());
			} 
		}
		return tfm;
	}

	/**
	 * Verifica se o usuario tem acesso a uma única filial e retorna essa
	 * filial para preencher a lookup de filiais 
	 */
	public TypedFlatMap verificaAcessoFilial(Map<String, Object> map) {
		TypedFlatMap retorno = new TypedFlatMap();
		
		if (SessionUtils.getFiliaisUsuarioLogado().size()==1) {
			Filial filial = (Filial)SessionUtils.getFiliaisUsuarioLogado().get(0);
			retorno.put("filial.idFilial", filial.getIdFilial() );
			retorno.put("filial.sgFilial", filial.getSgFilial());
			retorno.put("filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		}
		return retorno;
	}

	/**
	 * Retorna um POJO de EstoqueDispositivoQtde
	 *
	 * @param idTipoDispositivoUnitizacao
	 * @param idEmpresa
	 * @param idControleCarga
	 * @param idFilial
	 * @return
	 */
	public EstoqueDispositivoQtde findEstoqueDispositivoQtde(Long idTipoDispositivoUnitizacao, Long idEmpresa, Long idControleCarga, Long idFilial) {
		return this.getEstoqueDispositivoQtdeDAO().findEstoqueDispositivoQtde(idTipoDispositivoUnitizacao, idEmpresa, idControleCarga, idFilial);
	} 

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}