package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoDescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.DescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaDescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoDescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.DescontoRfcDAO;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Service responsável por operações de descontos relativos ao recibo frete
 * carreteiro. <br>
 * Compreende também operações de manipulação nas tabelas de desconto
 * relacionadas, tais como parcelas do desconto e tipo de desconto.
 * 
 */
public class DescontoRfcService extends CrudService<DescontoRfc, Long> {

	private static final String VALOR_MAXIMO_DESCONTO = "VALOR_MAXIMO_DESCONTO";
	private static final String PROGRAMADO = "P";
	private static final String FINALIZADO = "F";
	private static final String ATIVO = "A";
	private static final String PARAMETRO_FILIAL = "ATIVA_DESCONTO_RFC";
	private static final String SIM = "S";
	private static final String CANCELADO = "C";
	private ConfiguracoesFacade configuracoesFacade;
	private ExecuteWorkflowDescontoFreteCarreteiroService executeWorkflowDescontoFreteCarreteiroService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private ParametroGeralService parametroGeralService;
	
	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	private DescontoRfcDAO getDescontoRfcDAO() {
        return (DescontoRfcDAO) getDao();
    }
	
	public void setDescontoRfcDAO(DescontoRfcDAO dao) {
        setDao(dao);
    }
	
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setExecuteWorkflowDescontoFreteCarreteiroService(
			ExecuteWorkflowDescontoFreteCarreteiroService executeWorkflowDescontoFreteCarreteiroService) {
		this.executeWorkflowDescontoFreteCarreteiroService = executeWorkflowDescontoFreteCarreteiroService;
	}
	
	@Override
	protected Serializable store(DescontoRfc bean) {
		return super.store(bean);
	}
	
	public DescontoRfc findById(java.lang.Long id) {
		return (DescontoRfc) super.findById(id);
	}
	
	/**
	 * Não é possível excluir um desconto.
	 */
	public void removeById(java.lang.Long id) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Não é possível excluir um desconto.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@SuppressWarnings({ "rawtypes" })
	public void removeByIds(List ids) {
		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria){
		return getDescontoRfcDAO().findPaginatedCustom(criteria, FindDefinition.createFindDefinition(criteria));
	}	
	
	public Integer getRowCountCustom(TypedFlatMap criteria){
		return getDescontoRfcDAO().getRowCountCustom(criteria);
	}
	
	public List<Map<String, Object>> findAnexoDescontoRfcByIdDescontoRfc(Long idDescontoRfc) {
		return getDescontoRfcDAO().findAnexoDescontoRfcByIdDescontoRfc(idDescontoRfc);
    }

	/**
	 * Atualiza um desconto, grava seus anexos e gera workflow.
	 * 
	 * @param descontoRfc
	 * @param listAnexoDescontoRfc
	 * 
	 * @return Map<String, Object>
	 */
	public DescontoRfc storeDescontoRfc(DescontoRfc descontoRfc,
			List<AnexoDescontoRfc> listAnexoDescontoRfc) {
		validateDescontoRfc(descontoRfc);
		validateVigenciaProprietarioParcelas(descontoRfc);		
	
		prepare(descontoRfc);
		
		/*
		 * Grava desconto.
		 */
		getDescontoRfcDAO().store(descontoRfc);
		
		/*
		 * Se foram inseridos novos anexos, salva todos eles.
		 */
		storeAnexoDescontoRfc(descontoRfc, listAnexoDescontoRfc);

		/*
		 * Executa workflow ao efetuar alguma alteração no cadastro, de acordo
		 * com as regras estabelecidas.
		 */
		if(!SessionUtils.isFilialSessaoMatriz()){
			executeWorkflowDescontoFreteCarreteiroService.executeWorkflowPendencia(descontoRfc);
		}

		return descontoRfc;
	}

	/**
	 * @param descontoRfc
	 */
	private void prepare(DescontoRfc descontoRfc) {		
		if(descontoRfc.getIdDescontoRfc() == null){				
			/*
			 * Define número do próximo desconto.
			 */
			descontoRfc.setNrDescontoRfc(configuracoesFacade.incrementaParametroSequencial(descontoRfc.getFilial().getIdFilial(), "NR_DESCONTO_RFC", true));						
		} else {
			/*
			 * Remove parcelas do desconto previamente salvas. 
			 */
			getDescontoRfcDAO().removeAllParcelaDescontoRfc(descontoRfc.getIdDescontoRfc());
		}
		
		if(SessionUtils.isFilialSessaoMatriz()){
			DomainValue tpSituacao = new DomainValue(PROGRAMADO);
			descontoRfc.setTpSituacao(tpSituacao);
		}
		
		descontoRfc.setVlSaldoDevedor(descontoRfc.getVlTotalDesconto());
	}
	
	/**
	 * Validações gerais do desconto.
	 * 
	 * @param descontoRfc
	 */
	private void validateDescontoRfc(DescontoRfc descontoRfc){
		validateValorTotalDesconto(descontoRfc);		
		validateParcelaDesconto(descontoRfc);
	}

	/**
	 * Deve haver no mínimo uma parcela com valor superior a zero.
	 * 
	 * @param descontoRfc
	 */
	private void validateParcelaDesconto(DescontoRfc descontoRfc) {
		List<ParcelaDescontoRfc> listParcelaDescontoRfc = descontoRfc.getListParcelaDescontoRfc();
		
		if(listParcelaDescontoRfc == null || listParcelaDescontoRfc.isEmpty()){
			throw new BusinessException("LMS-25094");
		}
		
		ParcelaDescontoRfc parcelaDescontoRfc = listParcelaDescontoRfc.get(0);
		
		if(parcelaDescontoRfc.getVlParcela() == null){
			throw new BusinessException("LMS-25094");
		}
		
		if(BigDecimal.ZERO.equals(parcelaDescontoRfc.getVlParcela())){
			throw new BusinessException("LMS-25092");
		}
	}

	/**
	 * O valor total do desconto deve ter valor superior a zero.
	 * 
	 * @param descontoRfc
	 */
	private void validateValorTotalDesconto(DescontoRfc descontoRfc) {
		if(BigDecimal.ZERO.equals(descontoRfc.getVlTotalDesconto())){
			throw new BusinessException("LMS-25092");
		}
	}
	
	/**
	 * Valida se é possivel cadastrar o proprietário no desconto segundo o seu
	 * periodo de vigencia.
	 * 
	 * @param descontoRfc
	 */
	private void validateVigenciaProprietarioParcelas(DescontoRfc descontoRfc){
		Proprietario proprietario = descontoRfc.getProprietario();
		
		validateProprietarioVigenciaInicial(proprietario);
		validateProprietarioStatusProprietario(proprietario);
		validateProprietarioVigencia(descontoRfc, proprietario);		
		validateProprietarioVigenciaFinal(descontoRfc, proprietario);		
		validateProprietarioDescontoExistente(descontoRfc);
	}

	private void validateProprietarioStatusProprietario(Proprietario proprietario) {
		if(!"A".equals(proprietario.getTpSituacao().getValue())){
			throw new BusinessException("LMS-25103");
		}
	}

	/**
	 * Valida se existe algum outro desconto já cadastrado para o proprietário. 
	 * 
	 * @param descontoRfc
	 */
	private void validateProprietarioDescontoExistente(DescontoRfc descontoRfc) {
		Boolean descontoRfcExistente = getDescontoRfcDAO().isDescontoExistenteByProprietario(descontoRfc);
		
		if(descontoRfcExistente){
			if("S".equals(descontoRfc.getBlPrioritario().getValue())){
				throw new BusinessException("LMS-25106");				
			}else{
				throw new BusinessException("LMS-25101");
			}
		}
	}

	/**
	 * Valida se a última parcela está no periodo de vigência do proprietário.
	 * 
	 * @param descontoRfc
	 * @param proprietario
	 */
	private void validateProprietarioVigenciaFinal(DescontoRfc descontoRfc,	Proprietario proprietario) {
		List<ParcelaDescontoRfc> listParcelaDescontoRfc = descontoRfc.getListParcelaDescontoRfc();
		
		if(listParcelaDescontoRfc.size() > 0){
			ParcelaDescontoRfc parcelaDescontoRfc = listParcelaDescontoRfc.get(listParcelaDescontoRfc.size()-1);
			YearMonthDay dtVigenciaFinal = proprietario.getDtVigenciaFinal();
			
			if(dtVigenciaFinal != null && parcelaDescontoRfc.getDtParcela().compareTo(dtVigenciaFinal) > 0){
				throw new BusinessException("LMS-25099");				
			}
		}
	}

	/**
	 * Valida se a primeira parcela está no periodo de vigência do proprietário.
	 * 
	 * @param descontoRfc
	 * @param proprietario
	 */
	private void validateProprietarioVigencia(DescontoRfc descontoRfc,	Proprietario proprietario) {
		if(descontoRfc.getDtInicioDesconto().compareTo(proprietario.getDtVigenciaInicial()) < 0){
			throw new BusinessException("LMS-25099");
		}
	}

	/**
	 * Valida se proprietário possui data de vigência inicial informada. 
	 * 
	 * @param proprietario
	 */
	private void validateProprietarioVigenciaInicial(Proprietario proprietario) {
		if(proprietario.getDtVigenciaInicial() == null){
			throw new BusinessException("LMS-25100");
		}
	}
	
	/**
	 * Grava os anexos do desconto, se houverem.
	 * 
	 * @param DescontoRfc
	 * @param listAnexoDescontoRfc
	 */
	private void storeAnexoDescontoRfc(DescontoRfc descontoRfc, List<AnexoDescontoRfc> listAnexoDescontoRfc) {
		if(listAnexoDescontoRfc == null || listAnexoDescontoRfc.isEmpty()){
			return;
		}
		
		/*
		 * Define a entidade do desconto atualizada para cada anexo.
		 */
		for (AnexoDescontoRfc anexoDescontoRfc : listAnexoDescontoRfc) {
			 anexoDescontoRfc.setDescontoRfc(descontoRfc);
		}
		
		getDescontoRfcDAO().store(listAnexoDescontoRfc);
	}
	
	public Integer getRowCountAnexoDescontoRfcByIdDescontoRfc(Long idDescontoRfc) {
		return getDescontoRfcDAO().getRowCountAnexoDescontoRfcByIdDescontoRfc(idDescontoRfc);
	}	
	
	public AnexoDescontoRfc findAnexoDescontoRfcById(Long idAnexoDescontoRfc) {
		AnexoDescontoRfc anexoDescontoRfc = getDescontoRfcDAO().findAnexoDescontoRfcById(idAnexoDescontoRfc);
		
		if(anexoDescontoRfc != null){
			Hibernate.initialize(anexoDescontoRfc);
		}
		
		return anexoDescontoRfc;
	}
	
	public void removeByIdsAnexoDescontoRfc(List<Long> ids) {		
		getDescontoRfcDAO().removeByIdsAnexoDescontoRfc(ids);
	}
	
	/**
	 * Retorna todos os tipos de descontos rfc cadastrados no banco.
	 * 
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> findAllTipoDescontoRfc(){
		return getDescontoRfcDAO().findAllTipoDescontoRfc();
	}
	
	/**
	 * Verifica se é possivel cancelar o desconto.
	 * 
	 * @param idDescontoRfc
	 * @return Boolean
	 */
	public Boolean validateCancelarDesconto(Long idDescontoRfc){
		DescontoRfc descontoRfc = findById(idDescontoRfc);
		
		validaPermissaoCancelamento(descontoRfc);
		
		/*
		 * Caso exista alguma parcela paga, retorna para usuário confirmar se
		 * mesmo assim deseja prosseguir com o cancelamento.
		 */
		return !getDescontoRfcDAO().isParcelaDescontoRfcPaga(descontoRfc);
	}
	
	/**
	 * Executa um cancelamento de um desconto.
	 * 
	 * @param idDescontoRfc
	 */
	public void cancelarDesconto(Long idDescontoRfc){
		DescontoRfc descontoRfc = findById(idDescontoRfc);
				
		/*
		 * Efetua o cancelamento do desconto. 
		 */
		UsuarioLMS usuarioLMS = new UsuarioLMS();
    	usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
    	
		descontoRfc.setTpSituacao(new DomainValue("C"));
		descontoRfc.setDtAtualizacao(JTDateTimeUtils.getDataAtual());
    	descontoRfc.setUsuario(usuarioLMS);
		
    	this.store(descontoRfc);
	}

	/**
	 * Apenas matriz pode cancelar em todas as situações, caso seja filial
	 * apenas pode cancelar se a situação da pendência for reprovada.
	 * 
	 * @param descontoRfc
	 */
	private void validaPermissaoCancelamento(DescontoRfc descontoRfc) {
		if(!SessionUtils.isFilialSessaoMatriz()){
			Pendencia pendencia = descontoRfc.getPendencia();
			if(pendencia == null || !ConstantesWorkflow.REPROVADO.equals(pendencia.getTpSituacaoPendencia().getValue())){
				throw new BusinessException("LMS-25086");
			}			
		}
	}
	
	public Boolean isDescontoByProprietariostatus(Proprietario proprietario) {
		return getDescontoRfcDAO().isDescontoByProprietarioStatus(proprietario);
	}
	
	
	public ParcelaDescontoRfc storeAplicarAjuste(ReciboFreteCarreteiro recibo){
		if(!isDescontoAtivo() || isComplementar(recibo)){
			return null;
		}
		
		ParcelaDescontoRfc parcela = null;
		List<ParcelaDescontoRfc> parcelas = null;
		
		DescontoRfc desconto =  getDescontoRfcDAO().findDescontoByProprietarioPrioritario(recibo);
		
		parcelas = getParcelaPrioritario(desconto,recibo);
		
		if(parcelas == null){		
			List<DescontoRfc> descontos = new ArrayList<DescontoRfc>();
			descontos =  getDescontoRfcDAO().findDescontoByProprietario(recibo);
			
			if(descontos == null || descontos.isEmpty()){
				return null;
			}
			if(descontos.size() > 1){
				throw new BusinessException("LMS-25119");
			}
			
			desconto = descontos.get(0);
			
			parcelas = getParcelasDesconto(desconto);
			
			if(parcelas == null || parcelas.isEmpty()){
				return null;
			}
		}
		parcela = parcelas.get(0);
		
		if(parcela.getDtParcela().compareTo(JTDateTimeUtils.getDataAtual()) == 1){
			return null;
		}
		
		List<ParcelaDescontoRfc> parcelasSalvar = new ArrayList<ParcelaDescontoRfc>();
		
		
		BigDecimal valor = BigDecimal.ZERO;
		
		boolean valorCheio = false;
		
		TipoDescontoRfc tipoDescontoRfc = parcela.getDescontoRfc().getTipoDescontoRfc();
		
		BigDecimal valorReferencia = BigDecimal.ZERO;
		
		if(new DomainValue(SIM).equals(tipoDescontoRfc.getRecalculaInss())){
			valorReferencia = recibo.getVlLiquido().add(recibo.getVlInss() != null ? recibo.getVlInss() : BigDecimal.ZERO);
		}else{
			valorReferencia = recibo.getVlLiquido();
		}
		
		Object parametro = parametroGeralService.findConteudoByNomeParametro(VALOR_MAXIMO_DESCONTO, false);
		if(parametro != null){
			BigDecimal valorMaximoDesconto = (BigDecimal) parametro; 
			valorMaximoDesconto = valorMaximoDesconto.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
			valorReferencia = valorReferencia.multiply(valorMaximoDesconto);		
		}
		
		
		if(parcela.getVlParcela().compareTo(valorReferencia) <= 0){
			valor = parcela.getVlParcela();
			valorCheio = true;
			parcela.setObParcela("");
		}else{
			valor = valorReferencia;			
			
			BigDecimal valorAnterior = parcela.getVlParcela();
			
			parcela.setVlParcela(valor);
			
			String mensagemPagamentoParcial  = configuracoesFacade.getMensagem("LMS-26162");
			String adicionadoValorParcelaAtual  = configuracoesFacade.getMensagem("LMS-26163");
			
			parcela.setObParcela(mensagemPagamentoParcial);
			
			ParcelaDescontoRfc parcelaProxima = getProximaParcela(parcelas);
			parcelaProxima.setVlParcela(parcelaProxima.getVlParcela().add(valorAnterior.subtract(valor)));
			
			parcelaProxima.setObParcela(adicionadoValorParcelaAtual);
			
			parcelasSalvar.add(parcelaProxima);
		}		
		
		parcelasSalvar.add(parcela);
		
		DomainValue tpSituacao = new DomainValue(ATIVO);
		
		if(valorCheio && parcelas.size() == 1){
			tpSituacao = new DomainValue(FINALIZADO);			
		}
			
		desconto.setTpSituacao(tpSituacao); 
		
		getDescontoRfcDAO().store(parcelasSalvar);
		
		return parcela;
	}

	/**
	 * @param recibo
	 * @return
	 */
	private boolean isComplementar(ReciboFreteCarreteiro recibo) {
		return recibo.getReciboComplementado() != null;
	}

	private List<ParcelaDescontoRfc> getParcelasDesconto(DescontoRfc desconto) {		
		if(desconto == null){
			return null;
		}
		
		return getDescontoRfcDAO().findParcelasDescontos(desconto);		
	}

	private List<ParcelaDescontoRfc> getParcelaPrioritario(DescontoRfc desconto,ReciboFreteCarreteiro recibo) {
	
		List<ParcelaDescontoRfc> parcelas = null;
		
		if(desconto != null){
			parcelas = getDescontoRfcDAO().findParcelasDescontos(desconto);
			
			if(parcelas == null || parcelas.isEmpty()){
				return null;
			}
			
			validaValorPrioritario(recibo, desconto, parcelas);
		}
		return parcelas;
	}

	private void validaValorPrioritario(ReciboFreteCarreteiro recibo,DescontoRfc desconto, List<ParcelaDescontoRfc> parcelas) {
		ParcelaDescontoRfc parcela = parcelas.get(0);
		
		if(recibo.getVlLiquido().compareTo(parcela.getVlParcela()) < 0){
			throw new BusinessException("LMS-25107",new Object[]{desconto.getNrDescontoRfc()});
		}
	}

	public boolean isDescontoAtivo() {
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
		 if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			 return true;
		 }
		 return false;
	}

	private ParcelaDescontoRfc getProximaParcela(List<ParcelaDescontoRfc> parcelas) {
		if(parcelas.size() > 1){
			return parcelas.get(1);
		}else{
			ParcelaDescontoRfc parcelaProxima = new ParcelaDescontoRfc();
			ParcelaDescontoRfc parcelaDescontoRfc = parcelas.get(0);
			parcelaProxima.setDtParcela(parcelaDescontoRfc.getDtParcela().plusDays(parcelaDescontoRfc.getDescontoRfc().getQtDias()));
			parcelaProxima.setDescontoRfc(parcelaDescontoRfc.getDescontoRfc());
			parcelaProxima.setNrParcela(parcelaDescontoRfc.getNrParcela()+1);
			parcelaProxima.setReciboFreteCarreteiro(parcelaDescontoRfc.getReciboFreteCarreteiro());			
			parcelaProxima.setVlParcela(BigDecimal.ZERO);		
			return parcelaProxima;
		}
	}
	
	public void vincularRecibo(ParcelaDescontoRfc parcela,	ReciboFreteCarreteiro rfc) {
		parcela.setReciboFreteCarreteiro(rfc);
		parcela.getDescontoRfc().setVlSaldoDevedor(parcela.getDescontoRfc().getVlSaldoDevedor().subtract(parcela.getVlParcela()));
		getDescontoRfcDAO().store(parcela);	
	}
	
	public void desvincular(ParcelaDescontoRfc parcela) {
		if(new DomainValue("S").equals(parcela.getDescontoRfc().getBlPrioritario())){
			parcela.getDescontoRfc().setTpSituacao(new DomainValue(CANCELADO));
			parcela.setObParcela( "Recibo cancelado!");
		}else{
			parcela.getDescontoRfc().setTpSituacao(new DomainValue(ATIVO));
			parcela.setObParcela(configuracoesFacade.getMensagem("LMS-25104"));
		}
		
		parcela.setReciboFreteCarreteiro(null);
		
		parcela.getDescontoRfc().setVlSaldoDevedor(parcela.getDescontoRfc().getVlSaldoDevedor().add(parcela.getVlParcela()));
		getDescontoRfcDAO().store(parcela);	
	}

	public void cancelarParcela(ReciboFreteCarreteiro rfc) {
		List<ParcelaDescontoRfc> parcelas = getDescontoRfcDAO().findParcelaByRecibo(rfc);
		if(parcelas != null && !parcelas.isEmpty()){
			desvincular(parcelas.get(0));
		}
	}
	
	public Boolean isExistenteDescontoByProprietario(Long idProprietario) {
		return getDescontoRfcDAO().isExistenteDescontoByProprietario(idProprietario);
	}
	
	public ParcelaDescontoRfc findParcelaByRecibo(ReciboFreteCarreteiro rfc) {
		List<ParcelaDescontoRfc> parcelas = getDescontoRfcDAO().findParcelaByRecibo(rfc);
		if(parcelas != null && !parcelas.isEmpty()){
			return parcelas.get(0);
		}
		return null;
	}
	

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}