package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.fretecarreteirocoletaentrega.dto.DadoNotaCreditoDto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoNotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.NotaCreditoDAO;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy.NotaCreditoCalculoDoisStrategy;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy.NotaCreditoCalculoStrategy;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy.NotaCreditoCalculoUmStrategy;
import com.mercurio.lms.fretecarreteirocoletaentrega.utils.ConstantesEventosNotaCredito;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.fretecarreteirocoletaentrega.notaCreditoService"
 */
public class NotaCreditoService extends CrudService<NotaCredito, Long> {
	
    private static final String PARAMETRO_VALOR_MAXIMO_PERMITIDO = "VALOR_MAX_NOTA_CR";
    private static final String PARAMETRO_TOTAL_HORAS_DIARIA = "TOTAL_HORAS_DIARIA";
    private static final String PARAMETRO_HORARIO_TROCA_TURNO = "HORARIO_TROCA_TURNO";

    private ControleCargaService controleCargaService;
	private WorkflowPendenciaService workflowPendenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private PerfilUsuarioService perfilUsuarioService;
    private ParametroGeralService parametroGeralService;
    private NotaCreditoCalculoUmStrategy notaCreditoCalculoUmStrategy;
    private NotaCreditoCalculoDoisStrategy notaCreditoCalculoDoisStrategy;
    private NotaCreditoColetaService notaCreditoColetaService;
    private NotaCreditoDoctoService notaCreditoDoctoService;
    private EventoNotaCreditoService eventoNotaCreditoService;
    private ConteudoParametroFilialService conteudoParametroFilialService;
	private static final String PARAMETRO_FILIAL = "ATIVA_CALCULO_PADRAO";
	private static final String SIM = "S";

	 /**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idFilial
	 * @param nrNotaCredito
	 * @return <b>NotaCredito</b>
	 */
	public NotaCredito findNotaCredito(Long idFilial, Long nrNotaCredito) {
		return getNotaCreditoDAO().findNotaCredito(idFilial, nrNotaCredito);
	}

	/**
	 * Recupera uma instância de <code>NotaCredito</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public NotaCredito findById(java.lang.Long id) {
		return (NotaCredito)super.findById(id);
	}

    public List<NotaCredito> findByIdControleCarga(Long idControleCarga) {
        List<NotaCredito> notasCredito = getNotaCreditoDAO().findByIdControleCarga(idControleCarga);

        if (notasCredito == null) {
            return new ArrayList<NotaCredito>();
        }

        return notasCredito;
    }

    public String findTpCalculoNotaCredito(Long idNotaCredito) {
        return findTpCalculoNotaCredito(findById(idNotaCredito)).getValue();
    }

    public DomainValue findTpCalculoNotaCredito(NotaCredito notaCredito) {
        if (notaCredito != null && controleCargaService.hasTabelaColetaEntregaCC(notaCredito.getControleCarga())) {
            return NotaCreditoCalculoDoisStrategy.TIPO_CALCULO;
        }

        return NotaCreditoCalculoUmStrategy.TIPO_CALCULO;
    }

    public boolean hasNotaCreditoEmitidaControleCarga(Long idControleCarga) {
        return getNotaCreditoDAO().hasNotaCreditoEmitidaControleCarga(idControleCarga);
    }

    public boolean hasTipoParcelaNoControleCarga(Long idControleCarga, DomainValue tipoParcela) {
        return getNotaCreditoDAO().hasTipoParcelaNoControleCarga(idControleCarga, tipoParcela);
    }

	public ResultSetPage findPaginated(TypedFlatMap criteria,FindDefinition findDefinition) {
		return getNotaCreditoDAO().findPaginated(criteria,findDefinition);
	}

	public NotaCredito beforeStore(NotaCredito bean) {
		//Regra 3.6
		if (bean.getVlAcrescimoSugerido() != null &&
				bean.getVlDescontoSugerido() != null)
			throw new BusinessException("LMS-25022");
		//Regra 3.7
		if ((bean.getVlAcrescimoSugerido() != null ||
				bean.getVlDescontoSugerido() != null) &&
				bean.getObNotaCredito() == null)
			throw new BusinessException("LMS-25011");

		return super.beforeStore(bean);
	}

	public String executeWorkflow(List<Long> idsNotaCredito, List<String> tpStituacoes) {
		Long idNotaCredito = (Long)idsNotaCredito.get(0);
		String tpStituacao = (String)tpStituacoes.get(0);
		String eventoNotaCredito = null;
		String eventoFluxoNotaCredito = null;
		
		NotaCredito bean = findById(idNotaCredito);
		
		if(bean.getVlAcrescimoSugerido() != null){
			eventoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_ACRESCIMO;
		}else if(bean.getVlDescontoSugerido() != null){
			eventoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_DESCONTO;
		}
		
		if (tpStituacao.equalsIgnoreCase("A")) {
			bean.setTpSituacaoAprovacao(new DomainValue("A"));
			bean.setVlAcrescimo(bean.getVlAcrescimoSugerido());
			bean.setVlDesconto(bean.getVlDescontoSugerido());
			eventoFluxoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_APROVADO;
		} else if (tpStituacao.equalsIgnoreCase("R")) {
			bean.setTpSituacaoAprovacao(null);
			bean.setVlAcrescimoSugerido(null);
			bean.setVlDescontoSugerido(null);
			bean.setPendencia(null);
			eventoFluxoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_REPROVADO;
		}else
			throw new IllegalArgumentException("A Situação deve ser Reprovado ou Aprovado");

		super.store(bean);
		
		if(StringUtils.isNotBlank(eventoNotaCredito)){
			eventoNotaCreditoService.storeEventoNotaCredito(bean, eventoNotaCredito, eventoFluxoNotaCredito);
		}
		
		return null;
	}

	public List<TypedFlatMap> findLookupCuston(TypedFlatMap criteria) {
		List<Object[]> projections = getNotaCreditoDAO().findLookupCuston(criteria);
		List<TypedFlatMap> filteredList = new ArrayList<TypedFlatMap>(projections.size());
		for(Object[] projection : projections) {
			TypedFlatMap result = new TypedFlatMap();

			result.put("filial.sgFilial",projection[0]);
			result.put("filial.idFilial",projection[1]);
			result.put("filial.pessoa.nmFantasia",projection[2]);
			//MEIO TRANSPORTE
			result.put("controleCargas.meioTransporte.idMeioTransporte",projection[3]);
			result.put("controleCargas.meioTransporte.nrIdentificador",projection[4]);
			result.put("controleCargas.meioTransporte2.nrFrota",projection[5]);
			//PROPRIETARIO
			result.put("controleCargas.proprietario.idProprietario",projection[6]);
			result.put("controleCargas.proprietario.pessoa.tpIdentificacao",projection[8]);
			result.put("controleCargas.proprietario.pessoa.nmPessoa",projection[9]);
			result.put("controleCargas.proprietario.pessoa.nrIdentificacao",
					FormatUtils.formatIdentificacao((DomainValue)projection[8],(String)projection[7]));
			result.put("controleCargas.proprietario.pessoa.nrIdentificacaoFormatado",
					FormatUtils.formatIdentificacao((DomainValue)projection[8],(String)projection[7]));

			result.put("nrNotaCredito",projection[10]);
			result.put("idNotaCredito",projection[11]);

			result.put("controleCargas.meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte",projection[12]);
			result.put("controleCargas.meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte",projection[13]);

			filteredList.add(result);
		}
		return filteredList;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getNotaCreditoDAO().getRowCount(criteria);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	public List findByIdCustom(Long idNotaCredito){
		return getNotaCreditoDAO().findByIdCustom(idNotaCredito);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
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
	public Serializable store(NotaCredito beanScreen) {
		
		/*Regras 3.6 e 3.7*/
		beforeStore(beanScreen);
		
		Boolean existeValor = (beanScreen.getVlAcrescimoSugerido() != null || beanScreen.getVlDescontoSugerido() != null);
		Boolean naoAprovado = (beanScreen.getTpSituacaoAprovacao() == null || StringUtils.isBlank(beanScreen.getTpSituacaoAprovacao().getValue()));
		
		/*Verifica se existe valor de desconto ou acrescimo e se a situação nao é aprovada*/ 
		if (existeValor &&  naoAprovado){
			
			NotaCredito bean = findById(beanScreen.getIdNotaCredito());
			Short nrEvento;
			String texto;
			String eventoNotaCredito;

			if (StringUtils.isBlank(beanScreen.getObNotaCredito())){
				throw new BusinessException("LMS-25011");
			}	

			if (beanScreen.getVlAcrescimoSugerido() != null) {
				/*Acréscimo*/
				texto = configuracoesFacade.getMensagem("aprovarAcrescimoAtribuidoNotaCredito");
				nrEvento = ConstantesWorkflow.NR2503_ACRE_NOTCRE;
				eventoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_ACRESCIMO;
				bean.setVlAcrescimoSugerido(beanScreen.getVlAcrescimoSugerido());
				bean.setVlDescontoSugerido(null);
			} else {
				/*Desconto*/
				texto = configuracoesFacade.getMensagem("aprovarDescontoAtribuidoNotaCredito");
				nrEvento = ConstantesWorkflow.NR2502_DESC_NOTCRE;
				eventoNotaCredito = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_DESCONTO;
				bean.setVlDescontoSugerido(beanScreen.getVlDescontoSugerido());
				bean.setVlAcrescimoSugerido(null);
			}
			
			// LMS 4050 -- Atender os eventos 2502 e 2503
			texto += " " + SessionUtils.getFilialSessao().getSgFilial() + " " + new DecimalFormat("0000000000").format(beanScreen.getNrNotaCredito());
			
			bean.setObNotaCredito(beanScreen.getObNotaCredito());
			
			/*Gera a pendência no workflow*/
			Pendencia pendencia = workflowPendenciaService.generatePendencia(SessionUtils.getFilialSessao().getIdFilial(), nrEvento, beanScreen.getIdNotaCredito(), texto, JTDateTimeUtils.getDataHoraAtual());
			
			bean.setPendencia(pendencia);
			bean.setTpSituacaoAprovacao(new DomainValue("S"));
			
			/*Salva a nota de credito*/
			super.store(bean);
			
			/* Gera evento de nota / LMS-2975 */
			eventoNotaCreditoService.storeEventoNotaCredito(bean, eventoNotaCredito, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_SOLICITADO);
			
			return bean;
			
		}else{
			
			/*Salva a nota de crédito anulando o valor de desconto e acrescimo*/
			NotaCredito bean = findById(beanScreen.getIdNotaCredito());
			bean.setObNotaCredito(beanScreen.getObNotaCredito());
			bean.setVlAcrescimoSugerido(null);
			bean.setVlDescontoSugerido(null);
			
			/*Salva NotaCredito*/
			super.store(bean);
			
			if (beanScreen.getNotaCreditoParcelas() != null && beanScreen.getNotaCreditoParcelas().size() > 0){
				getNotaCreditoDAO().executeUpdateParcelas(beanScreen.getNotaCreditoParcelas());
		}
			
		}
		
		return beanScreen;
	}

	/**
	 * Store utilizado pela rotina de geracao de notas de credito
	 * @param bean
	 * @return
	 */
    public NotaCredito storeNotaCredito(NotaCredito bean){
		getNotaCreditoDAO().store(bean);
		return bean;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setNotaCreditoDAO(NotaCreditoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private NotaCreditoDAO getNotaCreditoDAO() {
		return (NotaCreditoDAO) getDao();
	}

	public List<NotaCreditoParcela> findParcelasNotaCredito(Long idNotaCredito) {
		return getNotaCreditoDAO().findParcelasNotaCredito(idNotaCredito);
	}

    public BigDecimal executeCalculo(Long idNotaCredito) {
        return executeCalculo(findById(idNotaCredito));
    }

    public BigDecimal executeCalculo(NotaCredito notaCredito) {
        if (notaCredito != null) {
            NotaCreditoCalculoStrategy calculo;

            if (NotaCreditoCalculoUmStrategy.TIPO_CALCULO.equals(findTpCalculoNotaCredito(notaCredito))) {
                calculo = notaCreditoCalculoUmStrategy;
            } else {
                calculo = notaCreditoCalculoDoisStrategy;
            }

            calculo.setup(notaCredito);

            return calculo.executeCalculo();
        }

        return BigDecimal.ZERO;
    }

	public BigDecimal executeCalculoVlNota(Long idNotaCredito) {
		List<NotaCreditoParcela> parcelas = getNotaCreditoDAO().findParcelasNotaCredito(idNotaCredito);
		BigDecimal vlNota = new BigDecimal(0);
		for(NotaCreditoParcela parcela : parcelas) {
			vlNota = vlNota.add(parcela.getQtNotaCreditoParcela().multiply(parcela.getVlNotaCreditoParcela()));
		}
		return vlNota;
	}

	public BigDecimal findValorTotalNotaCredito(Long idNotaCredito) {
		NotaCredito notaCredito = findById(idNotaCredito);
		
		boolean calculoPadrao  = false;
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			calculoPadrao = true;
		}
		
		if(calculoPadrao && notaCredito.getVlTotal() != null){
			return notaCredito.getVlTotal();
		}		
		DomainValue tpCalculo = findTpCalculoNotaCredito(notaCredito);
        if (NotaCreditoCalculoUmStrategy.TIPO_CALCULO.equals(tpCalculo)) {
            return notaCreditoCalculoUmStrategy.findValorTotalNotaCredito(notaCredito);
        } else {
            return notaCreditoCalculoDoisStrategy.findValorTotalNotaCredito(notaCredito);
        }
	} 

	/**
	 * Verifica se o usúario logado possue o perfil "PERMITE_ACRESCIMO_RECIBO_PUD"
	 * para acessar informações de nota de crédito
	 * 
	 * @param  usuario
	 * @return Boolean
	 */
	public Boolean permiteAcrescimoReciboPUD(Usuario usuario){
		/*Obtem os perfis do usuario logado*/
		List<PerfilUsuario> lsPerfil = perfilUsuarioService.findByIdUsuarioPerfilUsuario(usuario.getIdUsuario());
		
		if(lsPerfil != null && !lsPerfil.isEmpty()){
			for(PerfilUsuario perfil : lsPerfil){
				if("PERMITE_ACRESCIMO_RECIBO_PUD".equals(perfil.getPerfil().getDsPerfil()) ){
					return Boolean.TRUE;
				}
			}/*for*/
		}/*if*/
		
		return Boolean.FALSE;		
	}
	
	public Boolean validateNotaCreditoNaoEmitida(final Long idFilial, final Long idProprietario, final Long idTransportado) {
		return getNotaCreditoDAO().validateNotaCreditoNaoEmitida(idFilial, idProprietario, idTransportado);
	}
	
	public Boolean validateNotaCreditoNaoEmitidaPadrao(final Long idFilial, final Long idProprietario, final Long idTransportado) {
		return getNotaCreditoDAO().validateNotaCreditoNaoEmitidaPadrao(idFilial, idProprietario, idTransportado);
	}

    public void validateValorTotalNotaCredito(BigDecimal valorNotaCredito) {
    	BigDecimal limite = findValorMaximoPermitido();

    	if (limite != null && valorNotaCredito.compareTo(limite) > 0) {
    		throw new BusinessException("LMS-25064", new Object[] { limite });
	}
    	
    	if(BigDecimal.ZERO.compareTo(valorNotaCredito) > 0){
    		throw new BusinessException("LMS-25070");
    }
    }

    public BigDecimal findValorMaximoPermitido() {
        BigDecimal valorParametro = (BigDecimal) parametroGeralService.findConteudoByNomeParametro(
                PARAMETRO_VALOR_MAXIMO_PERMITIDO, false);

        if (valorParametro == null || BigDecimal.ZERO.compareTo(valorParametro) >= 0) {
            valorParametro = BigDecimal.ZERO;
	}

        return valorParametro;
    }

    public Long findTotalHorasDiaria() {
        BigDecimal horas = (BigDecimal) parametroGeralService.findConteudoByNomeParametro(
                PARAMETRO_TOTAL_HORAS_DIARIA, false);

        if (horas == null || BigDecimal.ZERO.compareTo(horas) > 0) {
            horas = BigDecimal.ZERO;
        }

        return horas.longValue();
    }

    public DateTime findHorarioTrocaTurno() {
        String data = (String) parametroGeralService.findConteudoByNomeParametro(PARAMETRO_HORARIO_TROCA_TURNO, false);

        if (data != null) {
            return JTDateTimeUtils.convertDataStringToDateTime(data);
        }

        return null;
    }
	
    public DadoNotaCreditoDto findDadosColetaEntregaByIdNotaCredito(Long idNotaCredito) {
        DadoNotaCreditoDto result = new DadoNotaCreditoDto();
        Long qtEntregasRealizadas = notaCreditoDoctoService.findCountQtEntregasRealizadasByIdNotaCredito(idNotaCredito);
        Long qtColetasExecutadas = notaCreditoColetaService.findCountQtColetasExecutadasByIdNotaCredito(idNotaCredito);

        result.setQtEntregasRealizadas(qtEntregasRealizadas);
        result.setQtColetasExecutadas(qtColetasExecutadas);

        return result;
    }
    
	/**
	 * LMS 4153 -- Verifica se existem outras notas de crédito que tenham parcelas diárias com valor definido > 0 e quantidade maior que 0
	 * @return
	 */
	public List<NotaCredito> findNotasCreditoComParcelaDiariaEvalorDefinido(ControleCarga controleCarga, NotaCredito novaNotaCredito){
		return getNotaCreditoDAO().findNotaCreditoComParcelaDHByNotaCreditoControleCarga(controleCarga, novaNotaCredito);
	}
    
    public List<NotaCredito> findNotasCreditosByIdControleCargaAndIgnoreIdNotaCredito(Long idControleCarga, Long idNotaCredito) {
    	return getNotaCreditoDAO().findNotasCreditosByIdControleCargaAndIgnoreIdNotaCredito(idControleCarga, idNotaCredito);
	}
    
	public Map findValorAcrescimoDesconto(Long idNotaCredito){
		return getNotaCreditoDAO().findValorAcrescimoDesconto(idNotaCredito);
	}

	public ResultSetPage<AnexoNotaCredito> findPaginatedAnexoNotaCredito(PaginatedQuery paginatedQuery) {
		return getNotaCreditoDAO().findPaginatedAnexoNotaCredito(paginatedQuery);
	}
	
	public Integer getRowCountAnexoNotaCredito(TypedFlatMap criteria) {
		return getNotaCreditoDAO().getRowCountAnexoNotaCredito(criteria);
	}
	
	public AnexoNotaCredito storeAnexoNotaCredito(TypedFlatMap map){
		AnexoNotaCredito anexoNotaCredito = new AnexoNotaCredito();
		anexoNotaCredito.setIdAnexoNotaCredito(map.getLong("idAnexoNotaCredito"));
		
		NotaCredito notaCredito = new NotaCredito();
    	notaCredito.setIdNotaCredito(map.getLong("idNotaCredito"));
    	anexoNotaCredito.setNotaCredito(notaCredito);
    	
    	UsuarioLMS usuarioLMS = new UsuarioLMS();
    	usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
    	anexoNotaCredito.setUsuario(usuarioLMS);
    	
    	try{
    		anexoNotaCredito.setDcArquivo(Base64Util.decode(map.getString("dcArquivo")));
    	} catch (IOException e) {
			throw new InfrastructureException(e);
		}
    	
    	anexoNotaCredito.setDsAnexo(map.getString("dsAnexo"));    	
    	anexoNotaCredito.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
    	
    	getNotaCreditoDAO().store(anexoNotaCredito);
    	
    	return anexoNotaCredito;
	}
	
	public AnexoNotaCredito findAnexoNotaCreditoById(Long idAnexoNotaCredito) {
		AnexoNotaCredito anexoNotaCredito = getNotaCreditoDAO().findAnexoNotaCreditoById(idAnexoNotaCredito);
		if(anexoNotaCredito != null){
			Hibernate.initialize(anexoNotaCredito);
		}
		return anexoNotaCredito;
	}
	
	public void removeByIdsAnexoNotaCredito(List ids) {		
		getNotaCreditoDAO().removeByIdsAnexoNotaCredito(ids);
	}
	
    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
        this.workflowPendenciaService = workflowPendenciaService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }
	
	public PerfilUsuarioService getPerfilUsuarioService() {
		return perfilUsuarioService;
	}

	public void setPerfilUsuarioService(PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
	}

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setNotaCreditoCalculoUmStrategy(NotaCreditoCalculoUmStrategy notaCreditoCalculoUmStrategy) {
        this.notaCreditoCalculoUmStrategy = notaCreditoCalculoUmStrategy;
    }

    public void setNotaCreditoCalculoDoisStrategy(NotaCreditoCalculoDoisStrategy notaCreditoCalculoDoisStrategy) {
        this.notaCreditoCalculoDoisStrategy = notaCreditoCalculoDoisStrategy;
    }

    public void setNotaCreditoColetaService(NotaCreditoColetaService notaCreditoColetaService) {
        this.notaCreditoColetaService = notaCreditoColetaService;
    }

    public void setNotaCreditoDoctoService(NotaCreditoDoctoService notaCreditoDoctoService) {
        this.notaCreditoDoctoService = notaCreditoDoctoService;
    }

	public EventoNotaCreditoService getEventoNotaCreditoService() {
		return eventoNotaCreditoService;
	}

	public void setEventoNotaCreditoService(
			EventoNotaCreditoService eventoNotaCreditoService) {
		this.eventoNotaCreditoService = eventoNotaCreditoService;
	}


	
	

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	
	

}