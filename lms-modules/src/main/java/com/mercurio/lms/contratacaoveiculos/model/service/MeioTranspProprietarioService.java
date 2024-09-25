package com.mercurio.lms.contratacaoveiculos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.dao.MeioTranspProprietarioDAO;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.DescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.DescontoRfcService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.meioTranspProprietarioService"
 */
public class MeioTranspProprietarioService extends CrudService<MeioTranspProprietario, Long> {
	private static final String LMS_26138 = "LMS-26138";
	private static final String NR_MANIFESTO = "nrManifesto";
	private static final int LIMIT_QUERY = 999;
	private static final String SIM = "S";
	private static final String PARAMETRO_FILIAL = "VALIDACAO_PROPRIETAR";
	private static final String LMS_00003 = "LMS-00003";
	private static final String LMS_26118 = "LMS-26118";
	private static final String LMS_26119 = "LMS-26119";
	private static final String LMS_26120 = "LMS-26120";
	private static final String LMS_26121 = "LMS-26121";
	private static final String LMS_26122 = "LMS-26122";
	private static final String LMS_26123 = "LMS-26123";
	private static final String LMS_26124 = "LMS-26124";
	private static final String NR_NOTA_CREDITO = "nrNotaCredito";
	private static final String LMS_26161 = "LMS-26161";
	private MeioTransporteService meioTransporteService;
	private VigenciaService vigenciaService;
	private BloqueioMotoristaPropService bloqueioMotoristaPropService;
	private ProprietarioService proprietarioService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private DescontoRfcService descontoRfcService;
	private NotaCreditoService notaCreditoService;
	

	/**
	 * Recupera uma instância de <code>MeioTranspProprietario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MeioTranspProprietario findById(java.lang.Long id) {
		return (MeioTranspProprietario)super.findById(id);
	}

	/**
	 * Recupera uma instância de <code>MeioTranspProprietario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Map findByIdDetalhamento(java.lang.Long id) {
		MeioTranspProprietario meioTranspProprietario = (MeioTranspProprietario)super.findById(id);
		MeioTransporte meioTransporte = meioTranspProprietario.getMeioTransporte();
		Proprietario proprietario = meioTranspProprietario.getProprietario();
		Pessoa pessoa = proprietario.getPessoa();

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idMeioTranspProprietario",meioTranspProprietario.getIdMeioTranspProprietario());

		retorno.put("proprietario.idProprietario",proprietario.getIdProprietario());
		String nrIdentificacao = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(),pessoa.getNrIdentificacao());
		retorno.put("proprietario.pessoa.nrIdentificacaoFormatado",nrIdentificacao);
		retorno.put("proprietario.pessoa.nrIdentificacao",pessoa.getNrIdentificacao());
		retorno.put("proprietario.pessoa.nmPessoa",pessoa.getNmPessoa());

		retorno.put("meioTransporte2.idMeioTransporte",meioTransporte.getIdMeioTransporte());
		retorno.put("meioTransporte2.nrFrota",meioTransporte.getNrFrota());
		retorno.put("meioTransporte.idMeioTransporte",meioTransporte.getIdMeioTransporte());
		retorno.put("meioTransporte.nrIdentificador",meioTransporte.getNrIdentificador());

		retorno.put("dtVigenciaInicial",meioTranspProprietario.getDtVigenciaInicial());
		retorno.put("dtVigenciaFinal",meioTranspProprietario.getDtVigenciaFinal());

		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(meioTranspProprietario);
		retorno.put("acaoVigenciaAtual",acaoVigencia);

		return retorno;
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
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(MeioTranspProprietario bean) {
		return super.store(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public java.io.Serializable storeMap(Map bean) {
		MeioTranspProprietario meioTranspProprietario = new MeioTranspProprietario();
		MeioTransporte meioTransporte = meioTransporteService.findByIdInitLazyProperties(MapUtils.getLong(MapUtils.getMap(bean, "meioTransporte"), "idMeioTransporte"), false);

		ReflectionUtils.copyNestedBean(meioTranspProprietario, bean);
		meioTranspProprietario.setMeioTransporte(meioTransporte);

		vigenciaService.validaVigenciaBeforeStore(meioTranspProprietario);
		
		
		validateBloqueioAlteracao(meioTranspProprietario);			
		

		super.store(meioTranspProprietario);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idMeioTranspProprietario",meioTranspProprietario.getIdMeioTranspProprietario());   	
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(meioTranspProprietario);
		retorno.put("acaoVigenciaAtual",acaoVigencia);
		return retorno;
	}

	/**
	 * LMS-6599 Impedir que seja alterado proprietário do meio de transporte se existir nota de crédito ou recibos pendentes
	 * 
	 * @param meioTranspProprietario
	 */
	private void validateBloqueioAlteracao(MeioTranspProprietario meioTranspProprietario) {
		if(!isLiberaValidacao()){
			return;	
		}		
		
		validateBloqueioMeioTransporte(meioTranspProprietario);			
		verificaSituacaoMeioTransporte(meioTranspProprietario);			
		verificaBloqueioProprietario(meioTranspProprietario);
				
		verificaSituacaoProprietario(meioTranspProprietario);
		if(continuaValidacao(meioTranspProprietario)){
			verificaVinculoMeioTransporte(meioTranspProprietario);		
		}
		
		if(meioTranspProprietario.getIdMeioTranspProprietario() != null && descontoRfcService.isDescontoAtivo()){
			verificaDescontoAtivo(meioTranspProprietario);
		}
	}


	private void verificaDescontoAtivo(	MeioTranspProprietario meioTranspProprietario) {
		Proprietario p = proprietarioService.findById(meioTranspProprietario.getProprietario().getIdProprietario());
		
		
		Boolean desconto = descontoRfcService.isExistenteDescontoByProprietario(p.getIdProprietario());
		
		if(Boolean.TRUE.equals(desconto)){
			
			Integer count = getMeioTranspProprietarioDAO().getRowCountMeioTransporteByProprietarioVigencia(p.getIdProprietario());
			if(count == null || count <= 1 ){
				throw new BusinessException(LMS_26161);				
			}
		}
		
	}

	/***
	 * Verifica pelo parametro da filial se pode executar as validações.
	 * <p><i>Obs.: Estas regras podem bloquear a edição e inclusão, portando deve ser liberado pelo parametro de filial após treinamento.</i></p> 
	 * 
	 * @return
	 */
	private boolean isLiberaValidacao() {
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
		 if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			 return true;
		 }
		return false;
	}

	/**
	 * Verifica se a ação é de postergar a vigencia se sim pula as validações
	 * @param meioTranspProprietario
	 */
	private boolean continuaValidacao(MeioTranspProprietario meioTranspProprietario) {
		boolean continua = true;
		if(meioTranspProprietario.getIdMeioTranspProprietario() == null || meioTranspProprietario.getDtVigenciaFinal() == null){
			return continua;
		}
		YearMonthDay dtVigenciaFinal = getMeioTranspProprietarioDAO().findDtVigenciaFinalById(meioTranspProprietario.getIdMeioTranspProprietario());
		
		if(dtVigenciaFinal != null && JTDateTimeUtils.comparaData(meioTranspProprietario.getDtVigenciaFinal(),dtVigenciaFinal) > 0){
			continua = false;
		}
		return continua;
		
	}

	/**
	 * Verificar qual o vínculo do meio de transporte.
	 * @param meioTranspProprietario
	 */
	private void verificaVinculoMeioTransporte(MeioTranspProprietario meioTranspProprietario) {
		
		if(new DomainValue("P").equals(meioTranspProprietario.getMeioTransporte().getTpVinculo())){
			return;
		}
		
		buscaNCPendente(meioTranspProprietario);
		buscaNCSemVinculo(meioTranspProprietario);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void buscaNCSemVinculo(MeioTranspProprietario meioTranspProprietario) {
		Long idMeioTransporte = meioTranspProprietario.getMeioTransporte().getIdMeioTransporte();
		
		List<Map> retorno = getMeioTranspProprietarioDAO().findNCSemVinculoTransportado(idMeioTransporte);
		
		List<Map> retornoComValor = new ArrayList<Map>();
		
		for (Map nota : (List<Map>)retorno) {
			BigDecimal valorNota =  notaCreditoService.findValorTotalNotaCredito((Long)nota.get("idNotaCredito"));
			if(valorNota.compareTo(BigDecimal.ZERO) > 0){
				retornoComValor.add(nota);
			}
		}
		
		if(!retornoComValor.isEmpty()){
			throw new BusinessException(LMS_26124, new Object[]{ajusteRetornoProprietario(retornoComValor),""});
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String ajusteRetornoProprietario(List retorno) {
		StringBuilder sbf = new StringBuilder();
		
		boolean primeiro = true;
		
		for (Map m : (List<Map>)retorno) {			
			if(!primeiro){
				sbf.append(", ");
			}
			sbf.append("Filial: ").append(m.get("sgFilial")).append(", Proprietário: ").append(m.get("nrIdentificacao")).append(" ").append(m.get("nmPessoa"));
			primeiro = false;
		}
		
		return sbf.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String ajusteRetorno(List retorno,String chave) {
		StringBuilder sbf = new StringBuilder();
		
		boolean primeiro = true;
		
		for (Map m : (List<Map>)retorno) {			
			if(!primeiro){
				sbf.append(", ");
			}
			sbf.append(m.get("sgFilial")).append(" ").append(m.get(chave));
			primeiro = false;
		}
		
		return sbf.toString();
	}

	/**
	 * Verifica se o meio de transporte possui nota de crédito pendente. 
	 * @param meioTranspProprietario
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void buscaNCPendente(MeioTranspProprietario meioTranspProprietario) {
		Long idMeioTransporte = meioTranspProprietario.getMeioTransporte().getIdMeioTransporte();
		
		List retorno = getMeioTranspProprietarioDAO().findNCPendenteTransportado(idMeioTransporte);
		List<Map> retornoComValor = new ArrayList<Map>();
		
		for (Map nota : (List<Map>)retorno) {
			BigDecimal valorNota =  notaCreditoService.findValorTotalNotaCredito((Long)nota.get("idNotaCredito"));
			if(valorNota.compareTo(BigDecimal.ZERO) > 0){
				retornoComValor.add(nota);
			}
		}
		
		
		if(!retornoComValor.isEmpty()){
			
			validaManifestoEntregaNormalParceiraPendente(retornoComValor);
			
			String notas = ajusteRetorno(retornoComValor,NR_NOTA_CREDITO);	
			throw new BusinessException(LMS_26123,new Object[]{notas});
		}
		
	}
	
	/**
	 * Verifica se existem manifestos de entrega normal pendentes de fechamento
	 * <b>(LMS-6926)</b>
	 * 
	 * @param retorno
	 */
	@SuppressWarnings("rawtypes")
	private void validaManifestoEntregaNormalParceiraPendente(List retorno) {
		
		List<Long> ids = getIdsNotaCredito(retorno);
		
		List manifestos = getMeioTranspProprietarioDAO().findManifestoEntregaNormalParceiraPendente(ids);
		
		if(!manifestos.isEmpty()){
			String mensagem = ajusteRetorno(manifestos,NR_MANIFESTO);	
			throw new BusinessException(LMS_26138,new Object[]{mensagem});			
		}
		
		
	}

	@SuppressWarnings("rawtypes")
	private List<Long> getIdsNotaCredito( List retorno) {
		List<Long> ids = new ArrayList<Long>();
		
		for (int i = 0; i < retorno.size(); i++) {
			if(i == LIMIT_QUERY){
				break;				
			}
			ids.add(MapUtils.getLong((Map)retorno.get(i),"idNotaCredito"));
		}
		return ids;
	}

	/**
	 * Verifica qual a situação do proprietário a ser inserido.
	 * @param meioTranspProprietario
	 */
	private void verificaSituacaoProprietario(MeioTranspProprietario meioTranspProprietario) {
		Proprietario p = proprietarioService.findById(meioTranspProprietario.getProprietario().getIdProprietario());
		meioTranspProprietario.setProprietario(p);
		
		if(new DomainValue("I").equals(meioTranspProprietario.getProprietario().getTpSituacao())){
			throw new BusinessException(LMS_26121);
		}
		if(new DomainValue("N").equals(meioTranspProprietario.getProprietario().getTpSituacao())){
			throw new BusinessException(LMS_26122);
		}
	}

	/**
	 * Verifica se o proprietário a ser inserido possui registro de bloqueio em aberto.
	 * @param meioTranspProprietario
	 */
	private void verificaBloqueioProprietario(MeioTranspProprietario meioTranspProprietario) {
		Map<String, Object> dados = new HashMap<String, Object>();
		dados.put("idProprietario",String.valueOf(meioTranspProprietario.getProprietario().getIdProprietario()));
		@SuppressWarnings("rawtypes")
		Map bloqueios = bloqueioMotoristaPropService.findDadosBloqueioProprietarioVigente(dados);
		if(!bloqueios.isEmpty()){
			throw new BusinessException(LMS_26120);
		}		
	}

	/**
	 * Verifica a situação do cadastro do meio de transporte
	 * @param meioTranspProprietario
	 */
	public void verificaSituacaoMeioTransporte(MeioTranspProprietario meioTranspProprietario) {
		if(new DomainValue("I").equals(meioTranspProprietario.getMeioTransporte().getTpSituacao())){
			throw new BusinessException(LMS_26119);
		}
	}

	/**
	 * Verifica se o meio de transporte possui registro de bloqueio em aberto.
	 * @param meioTranspProprietario
	 */
	public void validateBloqueioMeioTransporte(MeioTranspProprietario meioTranspProprietario) {
		Map<String, Object> dados = new HashMap<String, Object>();
		dados.put("idMeioTransporte",String.valueOf(meioTranspProprietario.getMeioTransporte().getIdMeioTransporte()));
		
		@SuppressWarnings("rawtypes")
		Map bloqueios = bloqueioMotoristaPropService.findDadosBloqueioMeioTransporteVigente(dados);
		if(!bloqueios.isEmpty()){
			throw new BusinessException(LMS_26118);
		}
	}


	protected MeioTranspProprietario beforeStore(MeioTranspProprietario bean) {
		MeioTranspProprietario meioTranspProprietario = (MeioTranspProprietario)bean;

		if (validateApenasUmVigente(
				meioTranspProprietario.getIdMeioTranspProprietario(),
				meioTranspProprietario.getMeioTransporte().getIdMeioTransporte(),
				meioTranspProprietario.getDtVigenciaInicial(),
				meioTranspProprietario.getDtVigenciaFinal()))
			throw new BusinessException(LMS_00003);

		if (JTVigenciaUtils.getIntegerAcaoVigencia(meioTranspProprietario).intValue() == 1) {
			if (meioTransporteService.verificaSituacaoMeioTransporteForProprietarios(meioTranspProprietario.getMeioTransporte().getIdMeioTransporte())) {
				meioTransporteService.updateSituacaoParaAtivo(meioTranspProprietario.getMeioTransporte().getIdMeioTransporte());
			}	
		}

		return super.beforeStore(bean);
	}

	/**
	 * Valida a remoção de um registro de acordo com o padrão de comportamento de vigências.
	 * @param id Id do registro a ser validado.
	 */
	private void validaRemoveById(Long id) {
		MeioTranspProprietario meioTranspProprietario = findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(meioTranspProprietario);
	}

	protected void beforeRemoveById(Long id) {
		validaRemoveById((Long)id);
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		for (int i = 0; i < ids.size() ; i++ )
			validaRemoveById((Long)ids.get(i));
		super.beforeRemoveByIds(ids);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMeioTranspProprietarioDAO(MeioTranspProprietarioDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MeioTranspProprietarioDAO getMeioTranspProprietarioDAO() {
		return (MeioTranspProprietarioDAO) getDao();
	}

	/**
	 * Valida se já existe algum registro vigente com mesmo Meio de Transporte para um Proprietário.
	 * 
	 * @param idmeioTranspProprietario Caso deseja-se excluir uma associação da validação.
	 * @param idMeioTransporte
	 * @return
	 */
	public boolean validateApenasUmVigente(Long idMeioTranspProprietario, Long idMeioTransporte,
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getMeioTranspProprietarioDAO().validateApenasUmVigente(idMeioTranspProprietario,idMeioTransporte,
				dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Retorna informações básicas de um meio de Transporte a partir de seu id.
	 * 
	 * @author Felipe Ferreira
	 * @param idMeioTransporte
	 * @return Map com diversas informações do meio de transporte.
	 */
	public Map findInfoMeioTransporte(Long idMeioTransporte) {
		List l = getMeioTranspProprietarioDAO().findInfoMeioTransporte(idMeioTransporte);
		if (!l.isEmpty())
			return (Map)l.get(0);
		else return null;
	}

	/**
	 * Retorna o proprietario vigente de um meio de Transporte a partir de seu id.
	 * 
	 * @author Salete
	 * @param idMeioTransporte
	 * @return Map com informacões do proprietario.
	 */
	public Map findProprietarioByMeioTransporte(Long idMeioTransporte) {
		List l = getMeioTranspProprietarioDAO().findProprietarioByMeioTransporte(idMeioTransporte);
		List listaNova = AliasToNestedMapResultTransformer.getInstance().transformListResult(l);
		if (!listaNova.isEmpty())
			return (Map)listaNova.get(0);
		else return null;
	}

	public Proprietario findProprietarioByIdMeioTransporte(Long idMeioTransporte,YearMonthDay vigenteEm) {
		List rs = getMeioTranspProprietarioDAO().findProprietarioByIdMeioTransporte(idMeioTransporte,vigenteEm);
		if (!rs.isEmpty())
			return (Proprietario)rs.get(0);
		else return null;
	}

	public Integer getRowCountMeioTransporteByProprietario(Long idProprietario, String tpSituacao){
		return getMeioTranspProprietarioDAO().getRowCountMeioTransporteByProprietario(idProprietario,tpSituacao);
	}
	
	public Integer getRowCountMeioTransporteByProprietario(Long idProprietario){
		return getMeioTranspProprietarioDAO().getRowCountMeioTransporteByProprietarioVigencia(idProprietario);
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	 public MeioTranspProprietario findMeioTransporteBetweenDate(Long idMeioTransporte, YearMonthDay data){
		 return getMeioTranspProprietarioDAO().findMeioTransporteBetweenDate(idMeioTransporte, data);
	 }
	 
	 public void setBloqueioMotoristaPropService(BloqueioMotoristaPropService bloqueioMotoristaPropService) {
			this.bloqueioMotoristaPropService = bloqueioMotoristaPropService;
	 }
	 public void setProprietarioService(ProprietarioService proprietarioService) {
			this.proprietarioService = proprietarioService;
	 }
	 
	 public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
			this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	 
	 public void setDescontoRfcService(DescontoRfcService descontoRfcService) {
			this.descontoRfcService = descontoRfcService;
		}

	public void setNotaCreditoService(NotaCreditoService notaCreditoService) {
		this.notaCreditoService = notaCreditoService;
	}

}