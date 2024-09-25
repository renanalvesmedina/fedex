package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.criterion.DetachedCriteria;
import org.joda.time.DateTimeConstants;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.BloqueioMotoristaPropService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.SolicitacaoContratacaoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.FaixaPesoParcelaTabelaCE;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega.DM_TP_CALCULO_TABELA_COLETA_ENTREGA;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.TabelaColetaEntregaDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.service.AcaoService;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.fretecarreteirocoletaentrega.tabelaColetaEntregaService"
 */
public class TabelaColetaEntregaService extends CrudService<TabelaColetaEntrega, Long> {
	private VigenciaService vigenciaService;
	private SolicitacaoContratacaoService solicitacaoContratacaoService;
	private ParcelaTabelaCeService parcelaTabelaCeService;
	private MeioTranspProprietarioService meioTranspProprietarioService;
	private MeioTransporteService meioTransporteService;
	private AcaoService acaoService;
	private BloqueioMotoristaPropService bloqueioMotoristaPropService;
	private FaixaPesoParcelaTabelaCEService faixaPesoParcelaTabelaCEService;

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idTipoTabelaColetaEntrega
	 * @param dtVigenciaInicial
	 * @param idTipoMeioTransporte
	 * @param idFilial
	 * @param tpRegistro
	 * @return <b>TabelaColetaEntrega</b>
	 */
	public TabelaColetaEntrega findTabelaColetaEntrega(
		Long idTipoTabelaColetaEntrega,
		YearMonthDay dtVigenciaInicial,
		Long idTipoMeioTransporte,
		Long idFilial,
		String tpRegistro
	) {
		return getTabelaColetaEntregaDAO().findTabelaColetaEntrega(null, idTipoMeioTransporte, idTipoTabelaColetaEntrega, idFilial, dtVigenciaInicial, tpRegistro);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idMeioTransporte
	 * @param idTipoMeioTransporte
	 * @param idTipoTabelaColetaEntrega
	 * @param idFilial
	 * @param dtVigenciaInicial
	 * @param tpRegistro
	 * @return <b>TabelaColetaEntrega</b>
	 */
	public TabelaColetaEntrega findTabelaColetaEntrega(
		Long idMeioTransporte,
		Long idTipoMeioTransporte,
		Long idTipoTabelaColetaEntrega,
		Long idFilial,
		YearMonthDay dtVigenciaInicial,
		String tpRegistro
	) {
		return getTabelaColetaEntregaDAO().findTabelaColetaEntrega(idMeioTransporte, idTipoMeioTransporte, idTipoTabelaColetaEntrega, idFilial, dtVigenciaInicial, tpRegistro);
	}

	/**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idSolicitacaoContratacao
     * @return <b>TabelaColetaEntrega</b>
     */
    public TabelaColetaEntrega findBySolicitacaoContratacao(Long idSolicitacaoContratacao) {
    	return getTabelaColetaEntregaDAO().findBySolicitacaoContratacao(idSolicitacaoContratacao);
    }

	/**
	 * Recupera uma instância de <code>TabelaColetaEntrega</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public TabelaColetaEntrega findById(java.lang.Long id) {
		return (TabelaColetaEntrega)super.findById(id);
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getTabelaColetaEntregaDAO().getRowCountCustom(criteria);
	}
	public TypedFlatMap findByIdDetalhamento(java.lang.Long id) {
		
		TabelaColetaEntrega tabelaColetaEntrega = new TabelaColetaEntrega();
		tabelaColetaEntrega.setIdTabelaColetaEntrega(id);
		List lista = getTabelaColetaEntregaDAO().findByObject(tabelaColetaEntrega);
		if (lista != null){
			tabelaColetaEntrega = (TabelaColetaEntrega)lista.get(0);
		}
		
		TipoMeioTransporte tipoMeioTransporte = tabelaColetaEntrega.getTipoMeioTransporte();
		TipoTabelaColetaEntrega tipoTabelaColetaEntrega = tabelaColetaEntrega.getTipoTabelaColetaEntrega();
		Filial filial = tabelaColetaEntrega.getFilial();
		
		// Organizando o retorno para a tela.
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idTabelaColetaEntrega", tabelaColetaEntrega.getIdTabelaColetaEntrega());
		if (filial != null){
			retorno.put("filial.idFilial",filial.getIdFilial());
			retorno.put("filial.sgFilial", filial.getSgFilial());
			retorno.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		}
		if (tipoTabelaColetaEntrega != null) {
			retorno.put("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega",tipoTabelaColetaEntrega.getIdTipoTabelaColetaEntrega());
			retorno.put("tipoTabelaColetaEntrega.dsTipoTabelaColetaEntrega",tipoTabelaColetaEntrega.getDsTipoTabelaColetaEntrega());
		}

		if (tipoMeioTransporte != null) {
			retorno.put("tipoMeioTransporte.idTipoMeioTransporte",tipoMeioTransporte.getIdTipoMeioTransporte());
			retorno.put("tipoMeioTransporte.dsTipoMeioTransporte",tipoMeioTransporte.getDsTipoMeioTransporte());
		}

		if(tabelaColetaEntrega.getCliente() != null){
			retorno.put("cliente.idCliente", tabelaColetaEntrega.getCliente().getIdCliente());
			retorno.put("cliente.pessoa.nrIdentificacao", tabelaColetaEntrega.getCliente().getPessoa().getNrIdentificacao());
			retorno.put("cliente.pessoa.nmPessoa", tabelaColetaEntrega.getCliente().getPessoa().getNmPessoa());
		}
		
		if(tabelaColetaEntrega.getRotaColetaEntrega() != null){
			retorno.put("rotaColetaEntrega.idRotaColetaEntrega", tabelaColetaEntrega.getRotaColetaEntrega().getIdRotaColetaEntrega());
			retorno.put("rotaColetaEntrega.nrRota", tabelaColetaEntrega.getRotaColetaEntrega().getNrRota());
			retorno.put("rotaColetaEntrega.dsRota", tabelaColetaEntrega.getRotaColetaEntrega().getDsRota());
		}
		
		retorno.put("tpCalculo", tabelaColetaEntrega.getTpCalculo());

		retorno.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(tabelaColetaEntrega));
		retorno.put("dtVigenciaInicial",tabelaColetaEntrega.getDtVigenciaInicial());
		retorno.put("dtVigenciaFinal",tabelaColetaEntrega.getDtVigenciaFinal());

		return retorno;
	}

	// Valida uma vigencia recebendo idTabela idMeioTransporte.
	private List validateVigenciaTipoTabelaMeioTransporte(Long idFilial, Long idTabelaColetaEntrega , Long idTipoTabelaColetaEntrega, Long idTipoMeioTransporte, String tpCalculo, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, String tpRegistro){
		return getTabelaColetaEntregaDAO().validateVigenciaTipoTabelaMeioTransporte(idFilial, idTabelaColetaEntrega , idTipoTabelaColetaEntrega, idTipoMeioTransporte, tpCalculo, dtVigenciaInicial, dtVigenciaFinal, tpRegistro);
	}

	private List validateVigenciaTipoTabelaMeioTransporte(Long idFilial, Long idTabelaColetaEntrega , Long idTipoTabelaColetaEntrega, Long idTipoMeioTransporte, String tpCalculo, Long idRotaColetaEntrega, Long idCliente, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return getTabelaColetaEntregaDAO().validateVigenciaTipoTabelaMeioTransporte(idFilial, idTabelaColetaEntrega , idTipoTabelaColetaEntrega, idTipoMeioTransporte, tpCalculo, idRotaColetaEntrega, idCliente, dtVigenciaInicial, dtVigenciaFinal);
	}

	public boolean validateVigencia(TabelaColetaEntrega tabelaColetaEntrega) {
        YearMonthDay hoje = JTDateTimeUtils.getDataAtual();

        if (hoje == null || !hasVigencia(tabelaColetaEntrega)) {
	        return false;
	    }

	    return hoje.compareTo(tabelaColetaEntrega.getDtVigenciaInicial()) >= 0
	            && (tabelaColetaEntrega.getDtVigenciaFinal() == null
	                    || hoje.compareTo(tabelaColetaEntrega.getDtVigenciaFinal()) <= 0);
	}

	private boolean hasVigencia(TabelaColetaEntrega tabelaColetaEntrega) {
	    return tabelaColetaEntrega != null && tabelaColetaEntrega.getDtVigenciaInicial() != null;
	}

	// Este método valida se esta vigente para qualquer consulta
	public boolean validateVigenciaTabelaColetaEntrega(Long idFilial, Long idTabelaColetaEntrega, DetachedCriteria dc,YearMonthDay dtVigenciaInicial,YearMonthDay dtVigenciaFinal){

		// Monta vigencia padrao
		dc = JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);
		boolean retorno = false;
		int totalRegistrosEncontrados = ((List)( getTabelaColetaEntregaDAO().getAdsmHibernateTemplate().findByDetachedCriteria(dc))).size();
		if (totalRegistrosEncontrados > 0){
			retorno = true;
		} 
		if ((idTabelaColetaEntrega != null) && (totalRegistrosEncontrados > 0)){
			retorno = false;
		}

		return retorno;
	}

	protected void beforeRemoveById(Long id) {
		validaRemoveById((Long) id);
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		for (Iterator i = ids.iterator() ; i.hasNext() ;)
			validaRemoveById((Long)i.next());
		super.beforeRemoveByIds(ids);
	}

	private void validaRemoveById(Long id) {
		TabelaColetaEntrega tabelaColetaEntrega = findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(tabelaColetaEntrega);
	}

	protected TabelaColetaEntrega beforeStore(TabelaColetaEntrega bean) {
		
		if (SessionUtils.isIntegrationRunning()){
			return bean;
		}
		
		TabelaColetaEntrega tabelaColetaEntrega = (TabelaColetaEntrega)super.beforeStore(bean);
		vigenciaService.validaVigenciaBeforeStore(tabelaColetaEntrega);
		return tabelaColetaEntrega;
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
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getTabelaColetaEntregaDAO().getRowCount(criteria);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public TypedFlatMap store(TabelaColetaEntrega bean, ItemList faixaPesoIL, Iterator<FaixaPesoParcelaTabelaCE> faixasPeso , boolean isAgregado) {
		if(isAgregado){
			validateTabelaColetaEntregaAgregado(bean, faixasPeso);
		} else {
		    validateTabelaColetaEntregaEventual(bean);
		}

		Long idTabelaColetaEntrega = (Long) store(bean);
		TypedFlatMap result = new TypedFlatMap();
		
		if ( isAgregado || validadeStatusWorkflow(bean) ) {
		if(faixaPesoIL != null){
			for(Object ofpr : faixaPesoIL.getRemovedItems()){
				FaixaPesoParcelaTabelaCE faixaPeso = (FaixaPesoParcelaTabelaCE) ofpr;
				faixaPesoParcelaTabelaCEService.removeById(faixaPeso.getIdFaixaPesoParcelaTabelaCE());
			}
			
			for (Object ofpr : faixaPesoIL.getNewOrModifiedItems()) {
				FaixaPesoParcelaTabelaCE faixaPeso = (FaixaPesoParcelaTabelaCE) ofpr;
				faixaPesoParcelaTabelaCEService.store(faixaPeso);
			}
		}
		
		List parcelas = new ArrayList();
		Iterator ie = bean.getParcelaTabelaCes().iterator();
		while (ie.hasNext()) {
			TypedFlatMap parcela = new TypedFlatMap(); 
			ParcelaTabelaCe parcelaTabelaCe = (ParcelaTabelaCe)ie.next();
			if(parcelaTabelaCeService.validateParcela(parcelaTabelaCe)){
			if(parcelaTabelaCe.getIdParcelaTabelaCe() == null ){
				parcela.put("id",parcelaTabelaCeService.store(parcelaTabelaCe));
					parcela.put("tpParcela", parcelaTabelaCe.getTpParcela().getValue());
			}else{
				parcela.put("id",parcelaTabelaCe.getIdParcelaTabelaCe());
					parcela.put("tpParcela", parcelaTabelaCe.getTpParcela().getValue());
			}
			parcelas.add(parcela);
		}
		}
			result.put("ParcelaTabelaCe",parcelas);
		}
		
		result.put("idTabelaColetaEntrega",idTabelaColetaEntrega);
		
		return result;
	}

	private boolean validadeStatusWorkflow(TabelaColetaEntrega bean){
		return (bean.getTpSituacaoAprovacao() == null || !bean.getTpSituacaoAprovacao().getValue().equals("A") 
		&& !bean.getTpSituacaoAprovacao().getValue().equals("R"));
	}
	private void validateTabelaColetaEntregaAgregado(TabelaColetaEntrega bean, Iterator<FaixaPesoParcelaTabelaCE> faixasPesoIt) {
		
		// esse mapa serve para facilitar as validações sobre as parcelas. Nesse mapa vão estar todas as parcelas que vão ter valor.
		Map<String, ParcelaTabelaCe> parcelas = new HashMap<String, ParcelaTabelaCe>();
		for (Object objectParcelaTabelaCE : bean.getParcelaTabelaCes()) {
			ParcelaTabelaCe parcelaTabelaCe = (ParcelaTabelaCe) objectParcelaTabelaCE;
			if(BigDecimalUtils.defaultBigDecimal(parcelaTabelaCe.getVlDefinido()).compareTo(BigDecimal.ZERO) != 0 || parcelaTabelaCe.getIdParcelaTabelaCe() != null){
				parcelas.put(parcelaTabelaCe.getTpParcela().getValue(), parcelaTabelaCe);
	}
		}
		
		// Verificar se pelo menos uma parcela do grid editavel esta preenchida 3.4
    	if (parcelas.isEmpty() &&( faixasPesoIt == null || !faixasPesoIt.hasNext()) ){
             throw new BusinessException("LMS-25003");
    	}
    	
    	 if(DM_TP_CALCULO_TABELA_COLETA_ENTREGA.CALCULO_1.equals(bean.getTpCalculo())){
    		 if(faixasPesoIt != null && faixasPesoIt.hasNext()){
    			 throw new BusinessException("LMS-25056");
    		 }
			 
			 
			/*
			 * Busca a lista de tabelas coletas entrega vigentes para o filtro */
			List listaResultado = validateVigenciaTipoTabelaMeioTransporte( bean.getFilial().getIdFilial(),
																			bean.getIdTabelaColetaEntrega(),
																			bean.getTipoTabelaColetaEntrega().getIdTipoTabelaColetaEntrega(), 
																			bean.getTipoMeioTransporte().getIdTipoMeioTransporte(),
																			bean.getTpCalculo().getValue(),
																			bean.getDtVigenciaInicial(),
																			bean.getDtVigenciaFinal(),
																			"A");
			
			/* Se o resultado for maior que 0 e o idTabelaColetaEntrega for diferente de null é porque esta editando, entao pode continuar
			 * se o resultado for maior que 0, mas o id for nulo é porque esta inserindo um objeto NOVO e ja esta vigente. */
			
			if (listaResultado.size() > 0) {
				throw new BusinessException("LMS-00003");
			}
 
    	 } else {
 			/*
 			 * Busca a lista de tabelas coletas entrega vigentes para o filtro 
 			 */
 			List listaResultado = validateVigenciaTipoTabelaMeioTransporte( bean.getFilial().getIdFilial(),
 																			bean.getIdTabelaColetaEntrega(),
 																			bean.getTipoTabelaColetaEntrega().getIdTipoTabelaColetaEntrega(), 
 																			bean.getTipoMeioTransporte() != null ? bean.getTipoMeioTransporte().getIdTipoMeioTransporte():null,
 																			bean.getTpCalculo().getValue(),
 																			bean.getRotaColetaEntrega() != null ? bean.getRotaColetaEntrega().getIdRotaColetaEntrega():null,
 																			bean.getCliente() != null ? bean.getCliente().getIdCliente() : null,
 																			bean.getDtVigenciaInicial(),
 																			bean.getDtVigenciaFinal() );
 			
 			/* Se o resultado for maior que 0 e o idTabelaColetaEntrega for diferente de null é porque esta editando, entao pode continuar
 			 * se o resultado for maior que 0, mas o id for nulo é porque esta inserindo um objeto NOVO e ja esta vigente. */
 			
 			if (listaResultado.size() > 0) {
 				throw new BusinessException("LMS-00003");
 			}
  
 			
 			
    		 
    		// para facilitar, vou passar o itterator para uma lista
    		List<FaixaPesoParcelaTabelaCE> faixasPeso = IteratorUtils.toList(faixasPesoIt);
    		 
    		if(!faixasPeso.isEmpty()){
    			//cria uma parcela de faixa peso falça com valor zerado. Isso é necessário para o peso entrar no calculo da nota credito
     			if(parcelas.get("FP") == null){
    				ParcelaTabelaCe parcelaTabelaCe = new ParcelaTabelaCe();
    				parcelaTabelaCe.setTabelaColetaEntrega(bean);
    				parcelaTabelaCe.setTpParcela(new DomainValue("FP"));
    				parcelaTabelaCe.setVlDefinido(BigDecimal.ZERO);
    				parcelas.put("FP", parcelaTabelaCe);
    			}

	    		Collections.sort(faixasPeso,new Comparator<FaixaPesoParcelaTabelaCE>() {
	    			public int compare(FaixaPesoParcelaTabelaCE o1, FaixaPesoParcelaTabelaCE o2) {
	    				return  o1.getPsInicial().compareTo(o2.getPsInicial());
					}
	    		});
	
				for (int i = 0; i < faixasPeso.size(); i++) {
					FaixaPesoParcelaTabelaCE faixaPesoParcelaTabelaCE = faixasPeso.get(i);
					
					// peso inicial nao pode ser maior que final
				if(faixaPesoParcelaTabelaCE.getPsInicial().compareTo(faixaPesoParcelaTabelaCE.getPsFinal()) > 0){
					throw new BusinessException("LMS-25059");
				}
				
				// primeira faixa vai começar em zero (js esta fando isso tb). e sera do tipo documento. restante KG
				if(i == 0){
					faixaPesoParcelaTabelaCE.setPsInicial(BigDecimal.ZERO);
					faixaPesoParcelaTabelaCE.setTpFator("Documento");
				} else {
					faixaPesoParcelaTabelaCE.setTpFator("KG");
					}
				}
	    		
	    		// A faixa inicial não pode ser inferior à faixa final do registro anterior.
	    		for (int i = 0; i < faixasPeso.size() - 1; i++) {
	    			FaixaPesoParcelaTabelaCE faixaPesoCeInf = faixasPeso.get(i);
	    			FaixaPesoParcelaTabelaCE faixaPesoCeSup = faixasPeso.get(i+1);
	    			
					if(faixaPesoCeInf.getPsFinal().compareTo(faixaPesoCeSup.getPsInicial()) >=0){
						throw new BusinessException("LMS-25059");
					}
					
				}
    		}
    		
			// A parcela FP não deve possuir “valor definido”. 
			if(parcelas.get("FP") != null && BigDecimalUtils.gtZero(parcelas.get("FP").getVlDefinido())){
				throw new BusinessException("LMS-25055");
			}
			
			// Verificar que apenas um dos tipos de parcela PF e PV possuam valor > 0 ou nenhum delas
			if(parcelas.get("PF") != null && parcelas.get("PV") != null){
				throw new BusinessException("LMS-25057");
			}

			bean.setParcelaTabelaCes(new ArrayList<ParcelaTabelaCe>(parcelas.values()) );
		}
	}
    		
	private void validateTabelaColetaEntregaEventual(TabelaColetaEntrega bean) {
		if ( validadeStatusWorkflow(bean) ) {
        List<ParcelaTabelaCe> parcelasTabelaCe = new ArrayList<ParcelaTabelaCe>();
        parcelasTabelaCe.addAll(bean.getParcelaTabelaCes());
    	
        for (ParcelaTabelaCe parcelaTabelaCe : parcelasTabelaCe) {
            if(parcelaTabelaCeService.validateParcela(parcelaTabelaCe) == false){
                bean.getParcelaTabelaCes().remove(parcelaTabelaCe);
            }
        }
	}
	}

	public java.io.Serializable store(TabelaColetaEntrega bean) {

		if (bean.getParcelaTabelaCesRemove() != null) {
			for (ParcelaTabelaCe parcela: bean.getParcelaTabelaCesRemove()) {
				parcelaTabelaCeService.removeById(parcela.getIdParcelaTabelaCe());
			}
		}

		Serializable toReturn = super.store(bean);
		return toReturn;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDefinition) {
		ResultSetPage rsp = getTabelaColetaEntregaDAO().findPaginated(criteria,findDefinition);
		List fixmedList = new ArrayList();
		for(Iterator i = rsp.getList().iterator(); i.hasNext();) {
			Map result = (Map)i.next();
			result.put("proprietarioNrIdentificacao",FormatUtils.formatIdentificacao((DomainValue)result.get("proprietarioTpIdentificacao"),
					(String)result.get("proprietarioNrIdentificacao")));
			result.put("nrFrota",meioTransporteService.findNrFrotaByNrIdentificacao((String)result.get("nrIdentificador")));
			fixmedList.add(result);
		}
		rsp.setList(fixmedList);
		return rsp;
	}

	public ResultSetPage findOrdenedPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getTabelaColetaEntregaDAO().findOrdenedPaginated(criteria);
		List rs = rsp.getList();
		List newList = new ArrayList();
		for(Iterator i = rs.iterator(); i.hasNext();) {
			Map map = (Map)i.next();
			TypedFlatMap flat = new TypedFlatMap();
			for(Iterator i2 = map.keySet().iterator(); i2.hasNext();) {
				String key = (String)i2.next();
				flat.put(key.replace('_','.'),map.get(key));
			}

			flat.put("cliente.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(flat.getDomainValue("cliente.tpIdentificacao"), flat.getString("cliente.nrIdentificacao")));
			
			newList.add(flat);
		}
		rsp.setList(newList);
		return rsp;
	}

	public TabelaColetaEntrega findByIdCustom(Long id) {
		return getTabelaColetaEntregaDAO().findByIdCustom(id);
	}

	/**
	 * Método para execução do workflow
	 * @param idsTabelaColetaEntrega
	 * @param tpStituacoes
	 * @return
	 */
	public String executeWorkflow(List idsTabelaColetaEntrega, List tpStituacoes) {
		Long idTabelaColetaEntrega = (Long)idsTabelaColetaEntrega.get(0);
		String tpStituacao = (String)tpStituacoes.get(0);

		TabelaColetaEntrega bean = findById(idTabelaColetaEntrega);
		SolicitacaoContratacao bean2 = solicitacaoContratacaoService.findById(bean.getSolicitacaoContratacao().getIdSolicitacaoContratacao());

		if (tpStituacao.equalsIgnoreCase("A")) {

			if (bean.getParcelaTabelaCes().size() == 0)
				throw new BusinessException("LMS-25037");
			else{
				boolean blRecebido = true;
				for(Iterator i = bean.getParcelaTabelaCes().iterator(); i.hasNext();) {
					ParcelaTabelaCe parcela = (ParcelaTabelaCe)i.next();
					if (parcela.getVlDefinido() != null) {
						blRecebido = false;
						break;
					}
				}
				if (blRecebido)
					throw new BusinessException("LMS-25037");
			}
			bean.setTpSituacaoAprovacao(new DomainValue("A"));
			bean2.setTpSituacaoContratacao(new DomainValue("AP"));
			getTabelaColetaEntregaDAO().store(bean2);
		} else if (tpStituacao.equalsIgnoreCase("R")) {
			bean.setTpSituacaoAprovacao(new DomainValue("R"));
			bean2.setTpSituacaoContratacao(new DomainValue("RE"));
		}
		bean2.setAcao(acaoService.findLastAcaoByPendencia(bean.getPendencia().getIdPendencia()));
		getTabelaColetaEntregaDAO().store(bean2);
		getTabelaColetaEntregaDAO().store(bean);
		return null;
	}

	/**
	 * Validar tabela Coleta/Entrega
	 * Descrição: Verificar se o meio de transporte possui uma tabela de coleta/entrega válida
	 * para poder efetuar serviços de coleta/entrega, retornando todos os tipos possíveis
	 * 
	 * Parametros: Filial(idFilial), 
	 * Meio de Transporte(idMeioTransporte) 
	 *
	 * Retorno: List com idTabelaColetaEntrega e idTipoTabelaColetaEntrega
	 * */
	public List<TabelaColetaEntrega> validateTabelaColetaEntrega(Long idFilial, Long idMeioTransporte, Long idRotaColetaEntrega){
		List<TabelaColetaEntrega> retornoList = new ArrayList<TabelaColetaEntrega>();

		if (idFilial == null || idMeioTransporte == null)
			throw new IllegalArgumentException("As propriedades \"idFilial\" e \"idMeioTransporte\" são obrigatórias.");

		//Regra 1
		validateDadosIniciais(idMeioTransporte);

		//Regra 3
		MeioTransporte meioTransporte = meioTransporteService.findById(idMeioTransporte);
		//Se for vinculo proprio ou agregado de ce
		if (  ( "A".equals(meioTransporte.getTpVinculo().getValue()) && meioTransporte.getFilialAgregadoCe() != null && meioTransporte.getFilialAgregadoCe().getIdFilial().equals(idFilial)) ||
				"P".equals(meioTransporte.getTpVinculo().getValue()) )  {
			//procura por tabelas do tipo 2
			retornoList = getTabelaColetaEntregaDAO().findTabelaColetaEntregaVigentes(idFilial, "A", null, JTDateTimeUtils.getDataAtual(), meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte(),idRotaColetaEntrega, "C2");
						
			// se não retornou para o tipo 2, procuro no tipo 1
			if(retornoList.isEmpty()){
				retornoList = getTabelaColetaEntregaDAO().findTabelaColetaEntregaVigentes(idFilial, "A", null, JTDateTimeUtils.getDataAtual(), meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte(),null, "C1");
			}

			Collections.sort(retornoList, new Comparator<TabelaColetaEntrega>() {

				public int compare(TabelaColetaEntrega o1, TabelaColetaEntrega o2) {
					
					if(o1 != null && o1.getTipoTabelaColetaEntrega() != null  
							&& o2 != null && o2.getTipoTabelaColetaEntrega() != null){
						
					return o1.getTipoTabelaColetaEntrega().getDsTipoTabelaColetaEntrega().compareTo(
							o2.getTipoTabelaColetaEntrega().getDsTipoTabelaColetaEntrega());
				}
					return -1;
				}
			});
			
			if (retornoList.isEmpty()){
				throw new BusinessException("LMS-25006");
			}
		} else {
			//Regra 4
			retornoList = getTabelaColetaEntregaDAO().findTabelaColetaEntregaVigentes(idFilial, "E", idMeioTransporte, JTDateTimeUtils.getDataAtual(), null,null, "C1");
			if (retornoList.size() == 0 ){
				throw new BusinessException("LMS-25007");
			} else{
				for (TabelaColetaEntrega tce : retornoList) {
					int diaSemana = JTDateTimeUtils.getNroDiaSemana(JTDateTimeUtils.getDataAtual());
					
					if (diaSemana == DateTimeConstants.SUNDAY && tce.getBlDomingo().booleanValue()){
					} else if (diaSemana == DateTimeConstants.MONDAY && tce.getBlSegunda().booleanValue()){
					} else if (diaSemana == DateTimeConstants.TUESDAY && tce.getBlTerca().booleanValue()){
					} else if (diaSemana == DateTimeConstants.WEDNESDAY && tce.getBlQuarta().booleanValue()){
					} else if (diaSemana == DateTimeConstants.THURSDAY && tce.getBlQuinta().booleanValue()){
					} else if (diaSemana == DateTimeConstants.FRIDAY && tce.getBlSexta().booleanValue()){
					} else if (diaSemana == DateTimeConstants.SATURDAY && tce.getBlSabado().booleanValue()){
					} else {
						throw new BusinessException("LMS-25008");
					}
				}
			}
		}	
		
		boolean existeTabelaSemClienteEspecifico = false;
		for (TabelaColetaEntrega tabelaColetaEntrega : retornoList) {
			if (tabelaColetaEntrega.getCliente() == null){
				existeTabelaSemClienteEspecifico = true;
				break;
			}
		}
		
		if(existeTabelaSemClienteEspecifico){
		return retornoList;
		}else{
			throw new BusinessException("LMS-25071");
	}
	}

	/**
	 * Retorna a tabela de Coleta/Entrega vigente para o meio de transporte
	 * @param idFilial
	 * @param tpRegistro
	 * @param idMeioTransporte
	 * @param dtConsulta
	 * @param idTipoMeioTransporte
	 * @param idRotaColetaEntrega
	 * @param tpCalculo
	 * @return
	 */
	public List<TabelaColetaEntrega> findTabelaColetaEntregaVigentes(Long idFilial, String tpRegistro, Long idMeioTransporte, 
			YearMonthDay dtConsulta, Long idTipoMeioTransporte, Long idRotaColetaEntrega, String tpCalculo){
		
		List<TabelaColetaEntrega> retornoList = getTabelaColetaEntregaDAO().findTabelaColetaEntregaVigentes(idFilial, tpRegistro,
				idMeioTransporte, dtConsulta, idTipoMeioTransporte,idRotaColetaEntrega, tpCalculo);
		
		return retornoList;
	}

	/**
	 * Método de validação da regra 1
	 * @param idMeioTransporte
	 */
	private void validateDadosIniciais(Long idMeioTransporte){
		if (meioTranspProprietarioService.findProprietarioByIdMeioTransporte(idMeioTransporte,JTDateTimeUtils.getDataAtual()) == null)
			throw new BusinessException("LMS-25036");

		if (!meioTransporteService.validateMeioTransporteAtivo(idMeioTransporte))
			throw new BusinessException("LMS-25009");	
		
		List listaBloqueios = bloqueioMotoristaPropService.findBloqueiosVigentesByMeioTransporte(idMeioTransporte);
		if (listaBloqueios.size() > 0){
			throw new BusinessException("LMS-25010");
		}
	}

	public List<Map<String, Long>> findTabelaColetaEntregaTipo2ByIdControleCarga(Long idControleCarga){
		return getTabelaColetaEntregaDAO().findTabelaColetaEntregaTipo2ByIdControleCarga(idControleCarga);
	}

	public List<TabelaColetaEntrega> findByIdNotaCreditoAndVlDefinidoParcelaCE(Long idNotaCredito, BigDecimal vlDefinido) {
		return getTabelaColetaEntregaDAO().findByIdNotaCreditoAndVlDefinidoParcelaCE(idNotaCredito, vlDefinido);
	}
	
	public DomainValue findTpRegistroTabelaColetaEntregaByIdNotaCredito(Long idNotaCredito) {
		return getTabelaColetaEntregaDAO().findTpRegistroTabelaColetaEntregaByIdNotaCredito(idNotaCredito);
	}
	
	public List<TabelaColetaEntrega> findTabelaColetaEntregaComParcelaPFOrPVSemEntregaByIdControleCarga(ControleCarga controleCarga) {
		return getTabelaColetaEntregaDAO().findTabelaColetaEntregaComParcelaPFOrPVSemEntregaByIdControleCarga(controleCarga);
	}
	
    // LMS-4153 - item 19
 	public List<TabelaColetaEntrega> findTabelaColetaEntregaComParcelaDHByControleCarga(ControleCarga controleCarga) {
 		return getTabelaColetaEntregaDAO().findTabelaColetaEntregaComParcelaDHByControleCarga(controleCarga);
 	}
	
	public Serializable store(TypedFlatMap parameters) {
		TabelaColetaEntrega tabelaColetaEntrega;
    	if(parameters.getLong("idTabelaColetaEntrega") != null){
    		tabelaColetaEntrega = findById(parameters.getLong("idTabelaColetaEntrega"));
    	} else {
    		tabelaColetaEntrega  = new TabelaColetaEntrega();
    	}
	    tabelaColetaEntrega.setBlDomingo(parameters.getBoolean("blDomingo"));
	    tabelaColetaEntrega.setBlSegunda(parameters.getBoolean("blSegunda"));
	    tabelaColetaEntrega.setBlTerca(parameters.getBoolean("blTerca"));
	    tabelaColetaEntrega.setBlQuarta(parameters.getBoolean("blQuarta"));
	    tabelaColetaEntrega.setBlQuinta(parameters.getBoolean("blQuinta"));
	    tabelaColetaEntrega.setBlSexta(parameters.getBoolean("blSexta"));
	    tabelaColetaEntrega.setBlSabado(parameters.getBoolean("blSabado"));
	    tabelaColetaEntrega.setHrDiariaInicial(parameters.getTimeOfDay("hrDiariaInicial"));
	    tabelaColetaEntrega.setDtVigenciaInicial(parameters.getYearMonthDay("dtVigenciaInicial"));
	    tabelaColetaEntrega.setDtVigenciaFinal(parameters.getYearMonthDay("dtVigenciaFinal"));
	    tabelaColetaEntrega.setTpSituacaoAprovacao(parameters.getDomainValue("tpSituacaoAprovacao"));

		if ( validadeStatusWorkflow(tabelaColetaEntrega) ) {
	    List parcelas = parameters.getList("ParcelaTabelaCe");
	    if (parcelas != null) {
	    	for (Iterator ie = parcelas.iterator(); ie.hasNext();) {
	    		TypedFlatMap parcelasParameters = (TypedFlatMap)ie.next();
		    	ParcelaTabelaCe parcelaTabelaCe = new ParcelaTabelaCe();
		    	if(parcelasParameters.getLong("id") != null){
		    		try{
		    			parcelaTabelaCe = parcelaTabelaCeService.findById(parcelasParameters.getLong("id"));
		    		}catch (ObjectNotFoundException onf) {}
		    		
		    	}
		    		
			    parcelaTabelaCe.setTpParcela(parcelasParameters.getDomainValue("tpParcela.value"));
			    parcelaTabelaCe.setVlSugerido(parcelasParameters.getBigDecimal("vlSugerido"));
			    parcelaTabelaCe.setVlMaximoAprovado(parcelasParameters.getBigDecimal("vlMaximoAprovado"));
			    parcelaTabelaCe.setVlNegociado(parcelasParameters.getBigDecimal("vlNegociado"));
			    parcelaTabelaCe.setVlDefinido(parcelasParameters.getBigDecimal("vlDefinido"));
			    parcelaTabelaCe.setPcSobreValor(parcelasParameters.getBigDecimal("pcSobreValor"));
			    parcelaTabelaCe.setVlReferencia(parcelasParameters.getBigDecimal("vlReferencia"));
			    parcelaTabelaCe.setTabelaColetaEntrega(tabelaColetaEntrega);
			    
			    if(parcelasParameters.getLong("id") == null && parcelaTabelaCeService.validateParcela(parcelaTabelaCe)){
			    	parcelaTabelaCeService.store(parcelaTabelaCe);
			    	tabelaColetaEntrega.getParcelaTabelaCes().add(parcelaTabelaCe);
			    }
	    	}
	    }
		}
    	return store(tabelaColetaEntrega,null, null, false);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setTabelaColetaEntregaDAO(TabelaColetaEntregaDAO dao) {
		setDao( dao );
	}
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private final TabelaColetaEntregaDAO getTabelaColetaEntregaDAO() {
		return (TabelaColetaEntregaDAO) getDao();
	}
	public void setSolicitacaoContratacaoService(SolicitacaoContratacaoService solicitacaoContratacaoService) {
		this.solicitacaoContratacaoService = solicitacaoContratacaoService;
	}
	public void setParcelaTabelaCeService(ParcelaTabelaCeService parcelaTabelaCeService) {
		this.parcelaTabelaCeService = parcelaTabelaCeService;
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}
	public void setBloqueioMotoristaPropService(BloqueioMotoristaPropService bloqueioMotoristaPropService) {
		this.bloqueioMotoristaPropService = bloqueioMotoristaPropService;
	}

	public void setFaixaPesoParcelaTabelaCEService(FaixaPesoParcelaTabelaCEService faixaPesoParcelaTabelaCEService) {
		this.faixaPesoParcelaTabelaCEService = faixaPesoParcelaTabelaCEService;
}

}
