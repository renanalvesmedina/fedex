package com.mercurio.lms.recepcaodescarga.action;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.*;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.BASE64Decoder;
import com.mercurio.lms.carregamento.model.*;
import com.mercurio.lms.carregamento.model.service.*;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Foto;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.FuncionarioService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.portaria.model.ControleQuilometragem;
import com.mercurio.lms.portaria.model.service.BoxService;
import com.mercurio.lms.portaria.model.service.ControleQuilometragemService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.recepcaodescarga.iniciarDescargaAction"
 */

public class IniciarDescargaAction extends MasterDetailAction {
	private static final String ID_FOTO_CARREGMTO_DESCARGA = "idFotoCarregmtoDescarga";
	private static final String VERSAO = "versao";
	private static final String CARREGAMENTO_DESCARGA = "carregamentoDescarga ";
	private static final String ID_LACRE_CONTROLE_CARGA = "idLacreControleCarga";
	private static final String CONTROLE_CARGA = "controleCarga";
	private static final String TP_STATUS_LACRE = "tpStatusLacre";
	private static final String NR_LACRES = "nrLacres";
	private static final String OB_CONFERENCIA_LACRE = "obConferenciaLacre";
	private static final String ID_INTEGRANTE_EQ_OPERAC = "idIntegranteEqOperac";
	private static final String EQUIPE_OPERACAO = "equipeOperacao";
	private static final String DM_STATUS_LACRE_VEICULO = "DM_STATUS_LACRE_VEICULO";
	private static final String NM_INTEGRANTE_EQUIPE = "nmIntegranteEquipe";
	private static final String TP_INTEGRANTE = "tpIntegrante";
	private static final String USUARIO_NR_MATRICULA = "usuario.nrMatricula";
	private static final String CARGO_OPERACIONAL_DS_CARGO = "cargoOperacional.dsCargo";
	private static final String PESSOA_NR_IDENTIFICACAO_FORMATADO = "pessoa.nrIdentificacaoFormatado";
	private static final String EMPRESA_PESSOA_NM_PESSOA = "empresa.pessoa.nmPessoa";
	private static final String ID_EQUIPE = "idEquipe";
	private static final String INTEGRANTE_EQ_OPERAC = "integranteEqOperac";
	private static final String FOTO_CARREGMTO_DESCARGA = "fotoCarregmtoDescarga";
	private static final String LACRE_CONTROLE_CARGA = "lacreControleCarga";
	private static final String ID_EQUIPE_OPERACAO = "idEquipeOperacao";
	private static final String EQUIPE_ID_EQUIPE = "equipe.idEquipe";
	private static final String NR_MATRICULA = "nrMatricula";
	private static final int FILL_WITH_ZERO_NR_MATRICULA_SIZE = 9;
	private static final String ID_USUARIO = "idUsuario";
	private static final String FILIAL_SESSAO_ID_FILIAL = "filialSessao.idFilial";
	private static final String TP_PESSOA = "tpPessoa";
	private static final String NR_IDENTIFICACAO = "nrIdentificacao";
	private static final String TP_IDENTIFICACAO = "tpIdentificacao";
	private static final String PESSOA_NR_IDENTIFICACAO = "pessoa.nrIdentificacao";
	private static final String PESSOA_TP_IDENTIFICACAO = "pessoa.tpIdentificacao";
	private static final String PESSOA_TP_PESSOA = "pessoa.tpPessoa";
	private static final String ID_CONTROLE_CARGA = "idControleCarga";
	private static final String USUARIO_ID_USUARIO = "usuario.idUsuario";
	private static final String DM_INTEGRANTE_EQUIPE = "DM_INTEGRANTE_EQUIPE";
	private static final String USUARIO_DS_FUNCAO = "usuario.dsFuncao";
	private static final String PESSOA_ID_PESSOA = "pessoa.idPessoa";
	private static final String CARGO_OPERACIONAL_ID_CARGO_OPERACIONAL = "cargoOperacional.idCargoOperacional";
	private static final String EMPRESA_ID_EMPRESA = "empresa.idEmpresa";

	private static final Log LOGGER = LogFactory.getLog(IniciarDescargaAction.class);

	private IntegranteEqOperacService integranteEqOperacService;
	private PessoaService pessoaService;
	private EquipeService equipeService;
	private BoxService boxService;
	private CargoOperacionalService cargoOperacionalService;
	private EmpresaService empresaService;
	private UsuarioService usuarioService;
	private FuncionarioService funcionarioService;
	private LacreControleCargaService lacreControleCargaService;
	private FotoCarregmtoDescargaService fotoCarregmtoDescargaService;
	private CarregamentoDescargaService carregamentoDescargaService;
	private ControleQuilometragemService controleQuilometragemService;
	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Declaração serviço principal da Action.
	 */
	public EquipeOperacaoService getEquipeOperacaoService() {
		return (EquipeOperacaoService) super.getMasterService();
	}
	public void setEquipeOperacaoService(EquipeOperacaoService equipeOperacaoService) {
		this.setMasterService(equipeOperacaoService);
	}


	/**
	 * Chamadas para metodos diversos da tela
	 */


	public void validateWorkFlowInicioDescarga(TypedFlatMap params) {
		Long idControleCarga = params.getLong(ID_CONTROLE_CARGA);
		if (idControleCarga != null) {
			carregamentoDescargaService.storeAndValidateDescargaVeiculo(idControleCarga, EventoDescargaVeiculo.INICIAR_DESCARGAR);
		}
	}

	public List findLookupUsuarioFuncionario(TypedFlatMap tfm) {
		String nrMatricula = tfm.getString(NR_MATRICULA);
		if (nrMatricula.length() < FILL_WITH_ZERO_NR_MATRICULA_SIZE) {
			nrMatricula = FormatUtils.fillNumberWithZero(nrMatricula, FILL_WITH_ZERO_NR_MATRICULA_SIZE);
		}

		return this.getUsuarioService().findLookupUsuarioFuncionario(tfm.getLong(ID_USUARIO),
				nrMatricula,
				tfm.getLong(FILIAL_SESSAO_ID_FILIAL),
				null, null, null, true);
	}

	public List findLookupPessoa(TypedFlatMap criteria) {
		Map pessoa = new HashMap();
		pessoa.put(TP_PESSOA, criteria.getString(PESSOA_TP_PESSOA));
		pessoa.put(NR_IDENTIFICACAO, criteria.getString(PESSOA_NR_IDENTIFICACAO));
		pessoa.put(TP_IDENTIFICACAO,  criteria.getString(PESSOA_TP_IDENTIFICACAO));

		pessoa.remove(PESSOA_NR_IDENTIFICACAO);
		pessoa.remove(PESSOA_TP_IDENTIFICACAO);

		return this.getPessoaService().findLookup(pessoa);
	}

	public List findLookupEquipe(Map criteria) {
		return this.getEquipeService().findLookup(criteria);
	}

	public List findBox(Map criteria) {
		return this.getBoxService().findBoxVigentePorFilial(SessionUtils.getFilialSessao().getIdFilial());
	}

	public List findCargoOperacional(Map criteria) {
		return this.getCargoOperacionalService().findCargo(criteria);
	}

	public List findLookupEmpresa(Map criteria) {
		return this.getEmpresaService().findLookup(criteria);
	}

	/**
	 * Pega os dados do domínio de Status Lacre Veiculo, filtra para CA e VI
	 * e retorna a list com os registros.
	 *
	 * @param criteria
	 * @return
	 */
	public List findStatusLacre(Map criteria) {
		List listStatusLacre = new ArrayList();
		List listDomains = this.getDomainValueService().findDomainValues(DM_STATUS_LACRE_VEICULO);
		for (int i = 0; i < listDomains.size(); i++) {
			DomainValue statusLacre = (DomainValue) listDomains.get(i);
			if ("CA".equals(statusLacre.getValue()) || "VI".equals(statusLacre.getValue())) {
				listStatusLacre.add(statusLacre);
			}
		}
		return listStatusLacre;
	}

	/**
	 * Método que retorna o ID do box do CarregamentoDescarga caso exista. 
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap getBoxFromCarregamentoDescarga(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();

		List listCarregamentoDescarga = this.getCarregamentoDescargaService().
				findCarregamentoDescargaByIdFilialByIdControleCarga(criteria.getLong("idFilial"),
						criteria.getLong(ID_CONTROLE_CARGA));
		if (!listCarregamentoDescarga.isEmpty()) {
			for (Iterator iter = listCarregamentoDescarga.iterator(); iter.hasNext();) {
				CarregamentoDescarga carregamentoDescarga = (CarregamentoDescarga) iter.next();
				if ("D".equals(carregamentoDescarga.getTpOperacao().getValue()) &&
						"P".equals(carregamentoDescarga.getTpStatusOperacao().getValue())) {
					map.put("idBox", carregamentoDescarga.getBox().getIdBox());
				}
			}

		}

		return map;
	}

	/**
	 * Método que retorna um Controle de Quilometragem a partir do ID do Controle de Carga e do ID da Filial.
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap getControleQuilometragem(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		ControleQuilometragem controleQuilometragem = this.getControleQuilometragemService().
				findControleQuilometragemByIdControleCargaByIdFilial(criteria.getLong(ID_CONTROLE_CARGA),
						criteria.getLong("idFilial"), Boolean.FALSE);
		if (controleQuilometragem == null) {
			throw new BusinessException("LMS-05103");
		} else {
			map.put("nrQuilometragem", controleQuilometragem.getNrQuilometragem());
			map.put("blVirouHodometro", controleQuilometragem.getBlVirouHodometro());
		}
		return map;
	}

	/**
	 * Método que valida o status do Lacre do Controle de Carga.
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap validaLacreControleCarga(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		LacreControleCarga lacreControleCarga = this.getLacreControleCargaService().
				findLacreControleCargaByIdControleCargaAndNrLacre(criteria.getLong(ID_CONTROLE_CARGA),
						criteria.getString(NR_LACRES));
		if (lacreControleCarga != null) {
			if (!"FE".equals(lacreControleCarga.getTpStatusLacre().getValue())) {
				map.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05104") +
						lacreControleCarga.getTpStatusLacre().getDescription() + ".");
			}
		} else {
			map.put("mensagem", this.getConfiguracoesFacade().getMensagem("LMS-05105"));
			map.put("limpaNrLacre", "true");
		}

		return map;
	}


	/**
	 * #######################################
	 * # Inicio dos métodos para tela de DF2 #
	 * #######################################
	 */

	public Serializable store(TypedFlatMap parameters) {
		MasterEntry entry = getMasterFromSession(parameters.getLong(ID_EQUIPE_OPERACAO), true);
		EquipeOperacao equipeOperacao = (EquipeOperacao) entry.getMaster();

		ItemList itemsIntegranteEqOperac = getItemsFromSession(entry, INTEGRANTE_EQ_OPERAC);
		ItemList itemsLacreControleCarga = getItemsFromSession(entry, LACRE_CONTROLE_CARGA);
		ItemList itemsFotoCarregmtoDescarga = getItemsFromSession(entry, FOTO_CARREGMTO_DESCARGA);

		if (!itemsIntegranteEqOperac.isInitialized()) {
			List integrantesEqOperac = getIntegranteEqOperacService().findIntegranteEqOperacao(parameters.getLong(EQUIPE_ID_EQUIPE));
			itemsIntegranteEqOperac.initialize(integrantesEqOperac);
		}

		ItemListConfig itemsIntegranteEqOperacConfig = getMasterConfig().getItemListConfig(INTEGRANTE_EQ_OPERAC);
		ItemListConfig itemsLacreControleCargaConfig = getMasterConfig().getItemListConfig(LACRE_CONTROLE_CARGA);
		ItemListConfig itemsFotoCarregmtoDescargaConfig = getMasterConfig().getItemListConfig(FOTO_CARREGMTO_DESCARGA);

		Serializable id = this.getCarregamentoDescargaService().storeInicioDescarga(parameters,
				equipeOperacao,
				itemsIntegranteEqOperac,
				itemsIntegranteEqOperacConfig,
				itemsLacreControleCarga,
				itemsLacreControleCargaConfig,
				itemsFotoCarregmtoDescarga,
				itemsFotoCarregmtoDescargaConfig);

		itemsIntegranteEqOperac.resetItemsState();
		itemsLacreControleCarga.resetItemsState();
		itemsFotoCarregmtoDescarga.resetItemsState();
		updateMasterInSession(entry);

		return id;
	}

	/**
	 * Salva a referencia do objeto Master na sessão.
	 * não devem ser inicializadas as coleções que representam os filhos
	 * já que o usuário pode vir a não utilizar a aba de filhos, evitando assim
	 * a carga desnecessária de objetos na sessão e a partir do banco de dados.
	 *
	 * @param id
	 */
	public Object findById(java.lang.Long id) {
		Object masterObj = this.getEquipeOperacaoService().findById(id);
		putMasterInSession(masterObj);
		return masterObj;
	}

	/**
	 * Remoção de um conjunto de registros Master.
	 */
	public void removeByIds(List ids) {
		this.getEquipeOperacaoService().removeByIds(ids);
	}

	/**
	 * Remoção de um registro Master.
	 */
	public void removeById(java.lang.Long id) {
		this.getEquipeOperacaoService().removeById(id);
		newMaster();
	}

	/**
	 * Salva um item Descrição Padrão na sessão.
	 */
	public Serializable saveIntegranteEqOperac(TypedFlatMap parameters) {
		return saveItemInstance(parameters, INTEGRANTE_EQ_OPERAC);
	}

	public ResultSetPage findPaginatedIntegranteEqOperac(TypedFlatMap parameters) {
		Long masterId = getMasterId(parameters);
		MasterEntry entry = getMasterFromSession(masterId, true);
		ItemList listIntegrantes = entry.getItems(INTEGRANTE_EQ_OPERAC);

		//Na primeira passada ele se obriga a carregar a tela...
		if (!listIntegrantes.isInitialized()) {
			Long idEquipe = parameters.getLong(ID_EQUIPE);
			List result = getIntegranteEqOperacService().findIntegranteEqOperacao(idEquipe);

			listIntegrantes.initialize(new ArrayList());
			for (Iterator iter = result.iterator(); iter.hasNext();) {
				IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) iter.next();
				super.saveItemInstanceOnSession(masterId, integranteEqOperac, INTEGRANTE_EQ_OPERAC);
			}
		}

		ResultSetPage rspIntegranteEqOperac = findPaginatedItemList(parameters, INTEGRANTE_EQ_OPERAC);

		List list = rspIntegranteEqOperac.getList();
		List filter = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) iter.next();

			TypedFlatMap map = new TypedFlatMap();
			map.put(ID_INTEGRANTE_EQ_OPERAC, integranteEqOperac.getIdIntegranteEqOperac());
			map.put(NM_INTEGRANTE_EQUIPE, integranteEqOperac.getNmIntegranteEquipe());
			map.put(TP_INTEGRANTE, integranteEqOperac.getTpIntegrante());

			if (integranteEqOperac.getUsuario()!=null) {
				map.put(USUARIO_NR_MATRICULA, integranteEqOperac.getUsuario().getNrMatricula());
			}

			if (integranteEqOperac.getCargoOperacional()!=null) {
				map.put(CARGO_OPERACIONAL_DS_CARGO, integranteEqOperac.getCargoOperacional().getDsCargo());
			}

			if (integranteEqOperac.getPessoa()!=null) {
				map.put(PESSOA_NR_IDENTIFICACAO_FORMATADO, FormatUtils.formatIdentificacao(integranteEqOperac.getPessoa()));
			}

			if (integranteEqOperac.getEmpresa()!=null && integranteEqOperac.getEmpresa().getPessoa()!=null) {
				map.put(EMPRESA_PESSOA_NM_PESSOA, integranteEqOperac.getEmpresa().getPessoa().getNmPessoa());
			}

			filter.add(map);
		}
		rspIntegranteEqOperac.setList(filter);
		return rspIntegranteEqOperac;
	}

	public Integer getRowCountIntegranteEqOperac(TypedFlatMap parameters){
		return getRowCountItemList(parameters, INTEGRANTE_EQ_OPERAC);
	}

	public Object findByIdIntegranteEqOperac(MasterDetailKey key) {
		IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) findItemById(key, INTEGRANTE_EQ_OPERAC);

		TypedFlatMap mapIntegranteEqOperac = new TypedFlatMap();

		mapIntegranteEqOperac.put(ID_INTEGRANTE_EQ_OPERAC, integranteEqOperac.getIdIntegranteEqOperac());
		mapIntegranteEqOperac.put(NM_INTEGRANTE_EQUIPE, integranteEqOperac.getNmIntegranteEquipe());
		mapIntegranteEqOperac.put("tpIntegrante.description", integranteEqOperac.getTpIntegrante().getDescription());
		mapIntegranteEqOperac.put("tpIntegrante.value", integranteEqOperac.getTpIntegrante().getValue());
		mapIntegranteEqOperac.put("tpIntegrante.status", integranteEqOperac.getTpIntegrante().getStatus());

		if (integranteEqOperac.getUsuario() != null) {
			mapIntegranteEqOperac.put(USUARIO_ID_USUARIO, integranteEqOperac.getUsuario().getIdUsuario());
			mapIntegranteEqOperac.put(USUARIO_NR_MATRICULA, integranteEqOperac.getUsuario().getNrMatricula());
			mapIntegranteEqOperac.put("usuario.nmUsuario", integranteEqOperac.getUsuario().getNmUsuario());
		}

		if (integranteEqOperac.getPessoa() != null) {
			mapIntegranteEqOperac.put(PESSOA_ID_PESSOA, integranteEqOperac.getPessoa().getIdPessoa());
			mapIntegranteEqOperac.put("pessoa.pessoa.nrIdentificacao", integranteEqOperac.getPessoa().getNrIdentificacao());
			mapIntegranteEqOperac.put(PESSOA_NR_IDENTIFICACAO_FORMATADO, FormatUtils.formatIdentificacao(integranteEqOperac.getPessoa().getTpIdentificacao(), integranteEqOperac.getPessoa().getNrIdentificacao()));
			mapIntegranteEqOperac.put("pessoa.nmPessoa", integranteEqOperac.getPessoa().getNmPessoa());
			mapIntegranteEqOperac.put("pessoa.dhInclusao", integranteEqOperac.getPessoa().getDhInclusao());
			mapIntegranteEqOperac.put("pessoa.tpPessoa.description", integranteEqOperac.getPessoa().getTpPessoa().getDescription());
			mapIntegranteEqOperac.put("pessoa.tpPessoa.value", integranteEqOperac.getPessoa().getTpPessoa().getValue());
			mapIntegranteEqOperac.put("pessoa.tpPessoa.status", integranteEqOperac.getPessoa().getTpPessoa().getStatus());
		}

		if (integranteEqOperac.getCargoOperacional() != null) {
			mapIntegranteEqOperac.put(CARGO_OPERACIONAL_ID_CARGO_OPERACIONAL, integranteEqOperac.getCargoOperacional().getIdCargoOperacional());
			mapIntegranteEqOperac.put(CARGO_OPERACIONAL_DS_CARGO, integranteEqOperac.getCargoOperacional().getDsCargo());
		}

		if (integranteEqOperac.getEmpresa() != null) {
			mapIntegranteEqOperac.put(EMPRESA_ID_EMPRESA, integranteEqOperac.getEmpresa().getIdEmpresa());
			mapIntegranteEqOperac.put("empresa.tpSituacao.description", integranteEqOperac.getEmpresa().getTpSituacao().getDescription());
			mapIntegranteEqOperac.put("empresa.tpSituacao.value", integranteEqOperac.getEmpresa().getTpSituacao().getValue());
			mapIntegranteEqOperac.put("empresa.tpSituacao.status", integranteEqOperac.getEmpresa().getTpSituacao().getStatus());
			mapIntegranteEqOperac.put(EMPRESA_PESSOA_NM_PESSOA, integranteEqOperac.getEmpresa().getPessoa().getNmPessoa());
		}

		return mapIntegranteEqOperac;
	}

	/***
	 * Remove uma lista de registros items.
	 *
	 * @param ids ids dos desciçoes item a serem removidos.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsIntegranteEqOperac(List ids) {
		super.removeItemByIds(ids, INTEGRANTE_EQ_OPERAC);
	}

	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {

		/**
		 * Declaracao da classe pai
		 */
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(EquipeOperacao.class, true);

		/**
		 * Esta classe e reponsavel por ordenar a List dos filhos que estao
		 * em memoria de acordo com as regras de negocio
		 */
		Comparator descComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				IntegranteEqOperac integranteEqOperac1 = (IntegranteEqOperac)obj1;
				IntegranteEqOperac integranteEqOperac2 = (IntegranteEqOperac)obj2;
				return integranteEqOperac1.getNmIntegranteEquipe().compareTo(integranteEqOperac2.getNmIntegranteEquipe());
			}
		};

		Comparator descComparator2 = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				return 1;
			}
		};



		/**
		 * Esta instancia é responsavel por carregar os
		 * items filhos na sessão a partir do banco de dados.
		 */
		ItemListConfig itemInit = new ItemListConfig() {

			/**
			 * Find paginated do filho
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
			 * Apos a primeira vez ela e carregada da memoria
			 *
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */
			public List initialize(Long masterId, Map parameters) {
				Long idEquipe = Long.valueOf(((Map)parameters.get("equipe")).get(ID_EQUIPE).toString());
				return getIntegranteEqOperacService().findIntegranteEqOperacao(idEquipe);
			}

			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
			 * Apos a primeira vez ela e carregada da memoria
			 *
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */
			public Integer getRowCount(Long masterId, Map parameters) {
				Long idEquipe = Long.valueOf(((Map)parameters.get("equipe")).get(ID_EQUIPE).toString());
				return getIntegranteEqOperacService().getRowCountIntegranteEqOperac(idEquipe);
			}

			/**
			 * Chama esta funcao depois de editar um item da grid filho
			 * E retira atributos desnecessarios para o filho
			 *
			 * @param newBean
			 * @param bean
			 */
			public void modifyItemValues(Object newBean, Object bean) {
				Set ignore = new HashSet();
				ignore.add(ID_INTEGRANTE_EQ_OPERAC); // id do filho
				ignore.add(VERSAO);
				ignore.add(EQUIPE_OPERACAO); // classe pai

				ReflectionUtils.syncObjectProperties(bean, newBean, ignore);
			}

			/**
			 * Mapeia atributos de dominio do pojo filho
			 */
			public Map configItemDomainProperties() {
				Map props = new HashMap(1);
				props.put(TP_INTEGRANTE, DM_INTEGRANTE_EQUIPE);
				return props;
			}

			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este
			 * metodo. Para se fazer uma validacao...
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 *
			 * @param parameters
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object bean) {
				IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) bean;
				TypedFlatMap param = (TypedFlatMap) parameters;

				if(param.getLong(ID_INTEGRANTE_EQ_OPERAC) != null) {
					integranteEqOperac.setIdIntegranteEqOperac(param.getLong(ID_INTEGRANTE_EQ_OPERAC));
				}
				DomainValue domainValue = getDomainValueService().findDomainValueByValue(DM_INTEGRANTE_EQUIPE, param.getString(TP_INTEGRANTE));
				integranteEqOperac.setTpIntegrante(domainValue);

				if ("F".equals(integranteEqOperac.getTpIntegrante().getValue())) {
					Usuario usuario = getUsuarioService().findById(param.getLong(USUARIO_ID_USUARIO));

					//Gera um objeto cargoOperacional apenas para visualizacao e ordenacao.
					CargoOperacional cargoOperacional = new CargoOperacional();
					cargoOperacional.setDsCargo(param.getString(USUARIO_DS_FUNCAO));

					integranteEqOperac.setCargoOperacional(cargoOperacional);
					integranteEqOperac.setNmIntegranteEquipe(usuario.getNmUsuario());
					integranteEqOperac.setUsuario(usuario);
				} else {
					//Busca os ids...
					//Busca os ids...
					final Long idPessoa = param.getLong(PESSOA_ID_PESSOA);
					final Long idCargoOperacional = param.getLong(CARGO_OPERACIONAL_ID_CARGO_OPERACIONAL);
					final Long idEmpresa = param.getLong(EMPRESA_ID_EMPRESA);

					//Busca e seta os objetos...
					if (idPessoa != null) {
						integranteEqOperac.setPessoa(getPessoaService().findById(idPessoa));
						integranteEqOperac.setNmIntegranteEquipe(integranteEqOperac.getPessoa().getNmPessoa());
					}
					if (idCargoOperacional != null) {
						integranteEqOperac.setCargoOperacional(getCargoOperacionalService().findById(idCargoOperacional));
					}
					if (idEmpresa != null) {
						integranteEqOperac.setEmpresa(getEmpresaService().findById(idEmpresa));
					}
				}

				return integranteEqOperac;
			}

		};

		/**
		 * ItemConfig para LacreControleCarga
		 */
		ItemListConfig itemInit2 = new ItemListConfig() {

			public List initialize(Long masterId, Map parameters) {
				return getLacreControleCargaService().findLacreControleCarga(masterId);
			}

			public Integer getRowCount(Long masterId, Map parameters) {
				return getLacreControleCargaService().getRowCountLacreControleCarga(masterId);
			}

			/**
			 * Seta um pai para o itemConfig de LacreControleCarga
			 */
			public void setMasterOnItem(Object master, Object itemBean) {
				ControleCarga controleCarga = new ControleCarga();
				((LacreControleCarga) itemBean).setControleCarga(controleCarga);
			}

			public void modifyItemValues(Object newBean, Object bean) {
				Set ignore = new HashSet();
				ignore.add(ID_LACRE_CONTROLE_CARGA); // id do filho
				ignore.add(VERSAO);
				ignore.add(CONTROLE_CARGA); // classe pai

				ReflectionUtils.syncObjectProperties(bean, newBean, ignore);
			}

			public Map configItemDomainProperties() {
				Map props = new HashMap(1);
				props.put(TP_STATUS_LACRE, DM_STATUS_LACRE_VEICULO);
				return props;
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				LacreControleCarga lacreControleCarga = (LacreControleCarga) bean;

				if(!"".equals((String) parameters.get(ID_LACRE_CONTROLE_CARGA))) {
					lacreControleCarga.setIdLacreControleCarga(Long.valueOf((String)parameters.get(ID_LACRE_CONTROLE_CARGA)));
				}
				lacreControleCarga.setNrLacres((String)(parameters.get(NR_LACRES)));
				DomainValue tpStatusLacre = getDomainValueService().findDomainValueByValue(DM_STATUS_LACRE_VEICULO, parameters.get(TP_STATUS_LACRE).toString());
				lacreControleCarga.setTpStatusLacre(tpStatusLacre);
				lacreControleCarga.setObInclusaoLacre(parameters.get(OB_CONFERENCIA_LACRE).toString());
				lacreControleCarga.setObConferenciaLacre(parameters.get(OB_CONFERENCIA_LACRE).toString());

				return lacreControleCarga;
			}

		};


		/**
		 * ItemConfig para FotoCarregmtoDescarga
		 */
		ItemListConfig itemInit3 = new ItemListConfig() {

			public List initialize(Long masterId, Map parameters) {
				return getFotoCarregmtoDescargaService().findFotoCarregmtoDescarga(masterId);
			}

			public Integer getRowCount(Long masterId, Map parameters) {
				return getFotoCarregmtoDescargaService().getRowCountFotoCarregmtoDescarga(masterId);
			}

			/**
			 * Seta um pai para o itemConfig de LacreControleCarga
			 */
			public void setMasterOnItem(Object master, Object itemBean) {
				CarregamentoDescarga carregamentoDescarga = new CarregamentoDescarga();
				((FotoCarregmtoDescarga) itemBean).setCarregamentoDescarga(carregamentoDescarga);
			}

			public void modifyItemValues(Object newBean, Object bean) {
				Set ignore = new HashSet();
				ignore.add(ID_FOTO_CARREGMTO_DESCARGA); // id do filho
				ignore.add(VERSAO);
				ignore.add(CARREGAMENTO_DESCARGA); // classe pai

				ReflectionUtils.syncObjectProperties(bean, newBean, ignore);
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap param = (TypedFlatMap) parameters;
				FotoCarregmtoDescarga fotoCarregmtoDescarga = (FotoCarregmtoDescarga) bean;

				if(param.getLong(ID_FOTO_CARREGMTO_DESCARGA) != null) {
					fotoCarregmtoDescarga.setIdFotoCarregmtoDescarga(param.getLong(ID_FOTO_CARREGMTO_DESCARGA));
				}

				fotoCarregmtoDescarga.setDsFoto(param.getString("dsFoto"));
				Foto foto = new Foto();
				foto.setIdFoto(null);
				try {
					foto.setFoto(new BASE64Decoder().decodeBuffer(param.getString("foto.foto")));
				} catch (IOException e) {
					LOGGER.error(e);
				}
				fotoCarregmtoDescarga.setFoto(foto);

				return fotoCarregmtoDescarga;
			}

		};

		/**
		 * Seta as configuracoes do filho.
		 */
		config.addItemConfig(INTEGRANTE_EQ_OPERAC, IntegranteEqOperac.class, itemInit, descComparator);
		config.addItemConfig(LACRE_CONTROLE_CARGA, LacreControleCarga.class, itemInit2, descComparator2);
		config.addItemConfig(FOTO_CARREGMTO_DESCARGA, FotoCarregmtoDescarga.class, itemInit3, descComparator2);
		return config;
	}

	/**
	 * ####################################
	 * # Fim dos métodos para tela de DF2 #
	 * ####################################
	 */


	/**
	 * #################################################
	 * # Inicio dos métodos para tela de DF2 de Lacres #
	 * #################################################
	 */

	/**
	 * Salva um item Descrição Padrão na sessão.
	 */
	public Serializable saveLacreControleCarga(Map parameters) {
		return saveItemInstance(parameters, LACRE_CONTROLE_CARGA);
	}

	public ResultSetPage findPaginatedLacreControleCarga(TypedFlatMap parameters) {
		return findPaginatedItemList(parameters, LACRE_CONTROLE_CARGA);
	}

	public Integer getRowCountLacreControleCarga(TypedFlatMap parameters){
		return getRowCountItemList(parameters, LACRE_CONTROLE_CARGA);
	}

	public Object findByIdLacreControleCarga(MasterDetailKey key) {
		return (LacreControleCarga) findItemById(key, LACRE_CONTROLE_CARGA);
	}

	/***
	 * Remove uma lista de registros items.
	 *
	 * @param ids ids dos desciçoes item a serem removidos.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsLacreControleCarga(List ids) {
		super.removeItemByIds(ids, LACRE_CONTROLE_CARGA);
	}

	/**
	 * ##############################################
	 * # Fim dos métodos para tela de DF2 de Lacres #
	 * ##############################################
	 */


	/**
	 * #################################################
	 * # Inicio dos métodos para tela de DF2 de Fotos #
	 * #################################################
	 */

	/**
	 * Salva um item Descrição Padrão na sessão.
	 */
	public Serializable saveFotoCarregmtoDescarga(TypedFlatMap parameters) {
		return saveItemInstance(parameters, FOTO_CARREGMTO_DESCARGA);
	}

	public ResultSetPage findPaginatedFotoCarregmtoDescarga(TypedFlatMap parameters) {
		return findPaginatedItemList(parameters, FOTO_CARREGMTO_DESCARGA);
	}

	public Integer getRowCountFotoCarregmtoDescarga(TypedFlatMap parameters){
		return getRowCountItemList(parameters, FOTO_CARREGMTO_DESCARGA);
	}

	public Object findByIdFotoCarregmtoDescarga(MasterDetailKey key) {
		return (FotoCarregmtoDescarga) findItemById(key, FOTO_CARREGMTO_DESCARGA);
	}

	public TypedFlatMap findInformacoesIniciais(TypedFlatMap tfm) {
		EquipeOperacao equipeOperacao = getEquipeOperacaoService().findLastEquipeOperacaoWithoutFechamentoByIdControleCarga(tfm.getLong(ID_CONTROLE_CARGA));
		TypedFlatMap result = new TypedFlatMap();

		if (equipeOperacao!=null) {
			result.put("idOpenEquipeOperacao", equipeOperacao.getIdEquipeOperacao());
			result.put(ID_EQUIPE, equipeOperacao.getEquipe().getIdEquipe());
			result.put("dsEquipe", equipeOperacao.getEquipe().getDsEquipe());
		}
		return result;
	}



	/***
	 * Remove uma lista de registros items.
	 *
	 * @param ids ids dos desciçoes item a serem removidos.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsFotoCarregmtoDescarga(List ids) {
		super.removeItemByIds(ids, FOTO_CARREGMTO_DESCARGA);
	}

	/**
	 * ##############################################
	 * # Fim dos métodos para tela de DF2 de Fotos #
	 * ##############################################
	 */


	public IntegranteEqOperacService getIntegranteEqOperacService() {
		return integranteEqOperacService;
	}
	public void setIntegranteEqOperacService(IntegranteEqOperacService integranteEqOperacService) {
		this.integranteEqOperacService = integranteEqOperacService;
	}
	public PessoaService getPessoaService() {
		return pessoaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public EquipeService getEquipeService() {
		return equipeService;
	}
	public void setEquipeService(EquipeService equipeService) {
		this.equipeService = equipeService;
	}
	public BoxService getBoxService() {
		return boxService;
	}
	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}
	public CargoOperacionalService getCargoOperacionalService() {
		return cargoOperacionalService;
	}
	public void setCargoOperacionalService(CargoOperacionalService cargoOperacionalService) {
		this.cargoOperacionalService = cargoOperacionalService;
	}
	public EmpresaService getEmpresaService() {
		return empresaService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public FuncionarioService getFuncionarioService() {
		return funcionarioService;
	}
	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}
	public LacreControleCargaService getLacreControleCargaService() {
		return lacreControleCargaService;
	}
	public void setLacreControleCargaService(LacreControleCargaService lacreControleCargaService) {
		this.lacreControleCargaService = lacreControleCargaService;
	}
	public CarregamentoDescargaService getCarregamentoDescargaService() {
		return carregamentoDescargaService;
	}
	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}
	public FotoCarregmtoDescargaService getFotoCarregmtoDescargaService() {
		return fotoCarregmtoDescargaService;
	}
	public void setFotoCarregmtoDescargaService(
			FotoCarregmtoDescargaService fotoCarregmtoDescargaService) {
		this.fotoCarregmtoDescargaService = fotoCarregmtoDescargaService;
	}
	public ControleQuilometragemService getControleQuilometragemService() {
		return controleQuilometragemService;
	}
	public void setControleQuilometragemService(
			ControleQuilometragemService controleQuilometragemService) {
		this.controleQuilometragemService = controleQuilometragemService;
	}
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}
