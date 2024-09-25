package com.mercurio.lms.tributos.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;


/**
 * @author Robson Edemar Gehl
 * @spring.bean id="lms.tributos.buscarCfopService"
 *
 */
public class BuscarCfopService{

	private static final String TP_DOCUMENTO_NFE_NF = "01";

	private CodigoFiscalOperacaoService codigoFiscalOperacaoService;

	private UnidadeFederativaService unidadeFederativaService;
	
	private PessoaService pessoaService;
	
	private InscricaoEstadualService inscricaoEstadualService;
	
	private ConfiguracoesFacade configuracoesFacade;
	
	private ClienteService clienteService;
	
	private RemetenteExcecaoICMSCliService remetenteExcecaoICMSCliService;
	
	/**
	 * Para testes JSP 
	 * @param parameters
	 * @return
	 */
	public String teste(TypedFlatMap parameters){
		try{
			return findCFOP( parameters.getLong("idUfRemetente"),
					parameters.getLong("idUfFilialOrigemDocto"),
					parameters.getLong("idUfDestinatario"),
					parameters.getLong("idUfDestino"),
					parameters.getLong("idUfConsignatario"),
					parameters.getString("tpCtrcParceria"),
					parameters.getLong("pessoa.idPessoa"),
					parameters.getLong("idInscricaoEstadualTomador"),
					parameters.getBoolean("blConhecimentoSemNF"),
					parameters.getString("tpDocumento"),					
					parameters.getLong("idTipoTributacaoIcms"),
					parameters.getLong("idFilialOrigemDocto"),
					parameters.getLong("idRedespacho"),
					parameters.getString("tpDocumento"),
					parameters.getLong("idUFMunicipioColeta"));
			
		}catch(Exception e){
			if (e instanceof BusinessException){
				throw (BusinessException)e;
			}else{
				throw new BusinessException(e.getMessage());
			}
		}
	}
	
	/**
	 * Buscar o CFOP para ser atribuído no documento de serviço
	 * 
	 * @param idUfRemetente Unidade federativa do remetente [OBRIGATORIO]
	 * @param idUfFilialOrigemDocto Unidade federativa da filial do documento de origem [OBRIGATORIO]
	 * @param idUfDestinatario Unidade federativa do destinatário [OBRIGATORIO]
	 * @param idUfDestino Unidade federativa de destino (local de entrega) [OBRIGATORIO]
	 * @param long1idUfConsignatario Unidade federativa do consignatário [NÃO OBRIGATORIO]
	 * @param tpCtrcParceria Tipo de conhecimento com as parceiras: M (Master), S (Segundo percurso), P (Primeiro percurso) [NÃO OBRIGATORIO]
	 * @param idTomadorServico Tomador do serviço do documento (Remetente quando frete CIF ou Destinatário quando frete FOB) [OBRIGATORIO]
	 * @param idInscricaoEstadualTomador Inscrição estadual do tomador do serviço (Remetente quando frete CIF ou Destinatário quando frete FOB) [NÃO OBRIGATORIO]
	 * @param blConhecimentoSemNF Indicador de conhecimento sem nota fiscal [OBRIGATORIO]
	 * @param tpConhecimento Tipo de conhecimento: RE (Reentrega), RF (Refaturamento) etc. [OBRIGATORIO]
	 * @return cfop 
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String findCFOP(Long idUfRemetente, 
			               Long idUfFilialOrigemDocto,
			               Long idUfDestinatario,
			               Long idUfDestino,
			               Long idUfConsignatario, 
			               String tpCtrcParceria,
			               Long idTomadorServico, 
			               Long idInscricaoEstadualTomador,
			               Boolean blConhecimentoSemNF, 
			               String tpConhecimento, 
			               Long idTipoTributacaoIcms, 
			               Long idFilialOrigemDocto, 
			               Long idRedespacho, 
			               String tpDocumento,
			               Long idUFMunicipioColeta){
		
		StringBuffer cfop = new StringBuffer();
		
		if( idUFMunicipioColeta.equals(idUfDestino) ){
			cfop.append("5"); 
		} else {
			cfop.append("6"); 
		}
		
		//Definição dos 3 ultimos digitos
		
		if (blConhecimentoSemNF != null && blConhecimentoSemNF.booleanValue()){
			cfop.append("359");
			return cfop.toString();
		}
		
		if (getPessoaService().validateNrIdentificacaoStartsWith(idTomadorServico, ((BigDecimal)configuracoesFacade.getValorParametro("NR_CNPJ_MERCURIO")).toString()) || (tpCtrcParceria != null && tpCtrcParceria.equals("S")) || idRedespacho != null){
			cfop.append("351");
			return cfop.toString();
		}
		 
		/*Nova regra solicitada pelo Eri 30/06/2009 - Quest 19559*/
		if(LongUtils.getLong(configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_ST")).equals(idTipoTributacaoIcms)){
			cfop.append("360");
			return cfop.toString();
		}
		
		if ((ConstantesExpedicao.CONHECIMENTO_NORMAL.equals(tpConhecimento)
				|| ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equals(tpConhecimento)
				|| ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_ICMS.equals(tpConhecimento)
				|| ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_FRETE.equals(tpConhecimento))
				&& TP_DOCUMENTO_NFE_NF.equals(tpDocumento)) {
			
			if (!idUfFilialOrigemDocto.equals(idUFMunicipioColeta)) {
				cfop.append("932");
				return cfop.toString();

			}
		}		
		
		Long cdCfopRamo = getCodigoFiscalOperacaoService().findCdCfopRamoAtividadeByCliente(idTomadorServico);
		if (cdCfopRamo != null){
			cfop.append(cdCfopRamo);
			return cfop.toString();
		}
		
		/*Nova regra solicitada pelo Eri 30/06/2009 - Quest 19559*/
		Cliente cliente = clienteService.findById(idTomadorServico);
		if("CNPJ".equals(cliente.getPessoa().getTpIdentificacao().getValue()) 
				&& idFilialOrigemDocto != null && idFilialOrigemDocto.equals(cliente.getFilialByIdFilialAtendeOperacional().getIdFilial())){
			
			Long idTipoTributacaoDevido = LongUtils.getLong(configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_DEVIDO_OUTRA_UF"));
			
			List listaRemetenteICMS = remetenteExcecaoICMSCliService
					.findRemetenteExcecaoICMSByDadosCliente(cliente.getPessoa().getNrIdentificacao(), idTipoTributacaoDevido, idFilialOrigemDocto);
			
			if(listaRemetenteICMS != null && !listaRemetenteICMS.isEmpty()){
			cfop.append("932");
			return cfop.toString();
			}
		}
		
		if (idInscricaoEstadualTomador != null){
			if ( getInscricaoEstadualService().validateIsento(idInscricaoEstadualTomador) ){
				cfop.append("357");
				return cfop.toString();	
			}
		}else{
			cfop.append("357");
			return cfop.toString();
		}
		
		Pessoa pesCliente = pessoaService.findById(idTomadorServico);
		
		String param = pesCliente.getTpIdentificacao().getValue() + " " + FormatUtils.formatIdentificacao(pesCliente) + " - " + pesCliente.getNmPessoa();		
		
		throw new BusinessException("LMS-23003",new Object[]{param});
		
	}
	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public InscricaoEstadualService getInscricaoEstadualService() {
		return inscricaoEstadualService;
	}

	public void setInscricaoEstadualService(
			InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	public PessoaService getPessoaService() {
		return pessoaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public CodigoFiscalOperacaoService getCodigoFiscalOperacaoService() {
		return codigoFiscalOperacaoService;
	}

	public void setCodigoFiscalOperacaoService(
			CodigoFiscalOperacaoService codigoFiscalOperacaoService) {
		this.codigoFiscalOperacaoService = codigoFiscalOperacaoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
}

	public void setRemetenteExcecaoICMSCliService(
			RemetenteExcecaoICMSCliService remetenteExcecaoICMSCliService) {
		this.remetenteExcecaoICMSCliService = remetenteExcecaoICMSCliService;
	}

}
