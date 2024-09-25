package com.mercurio.lms.contasreceber.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.param.GenerateFaturaParam;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * @author Mickaël Jalbert
 * @since 05/01/2007
 * 
 * @spring.bean id="lms.contasreceber.validateItemFaturaService"
 */
public class ValidateItemFaturaService {
	
	private ConhecimentoService conhecimentoService;
	
	private DevedorDocServFatService devedorDocServFatService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	
	private BloqueioFaturamentoService bloqueioFaturamentoService;
	
	/**
	 * Valida cada item fatura
	 */
	public void validateItemFatura(Fatura fatura, ItemFatura itemFatura, GenerateFaturaParam generateFaturaParam) {
		DevedorDocServFat devedorDocServFat = itemFatura.getDevedorDocServFat();
		String tpDocumento = devedorDocServFat.getDoctoServico().getTpDocumentoServico().getValue();
		Map mapConhecimento = null;		
		
		if (tpDocumento.equals("CTR") || tpDocumento.equals("NFT") || tpDocumento.equals("NTE")
				|| tpDocumento.equals("CTE")) {
			mapConhecimento = conhecimentoService.findTpFreteTpSituacaoByIdConhecimento(devedorDocServFat.getDoctoServico().getIdDoctoServico());
		}

		/**
		 * Regra 2.1 / 2.2
		 */
		if (itemFatura.getIdItemFatura() == null) {
			devedorDocServFatService.validateDisponibilidadeDevedorDocServFat(devedorDocServFat);
		}

		// Caso o documento seja conhecimento ou nota fiscal de transporte, valida se o mesmo foi entregue.
		if ((tpDocumento.equals("CTR") || tpDocumento.equals("NFT") || tpDocumento.equals("NTE") || tpDocumento
				.equals("CTE"))
				&& (fatura.getTpOrigem() == null || !"I".equals(fatura.getTpOrigem().getValue()))) {
			if (itemFatura.getIdItemFatura() == null)
			validateDocumentoEntregue(devedorDocServFat, devedorDocServFat.getCliente(), mapConhecimento);
		}
		
		validateDhEmissao(fatura, devedorDocServFat);

		validateCliente(fatura, devedorDocServFat);

		validateIgualdade(fatura, devedorDocServFat, generateFaturaParam, mapConhecimento);
		
		bloqueioFaturamentoService.validateByIdDevedorDocServFat(devedorDocServFat.getIdDevedorDocServFat());
	}
	


	/**
	 * Valida se o conhecimento está bloqueado
	 * 
	 * Regra 2.3
	 */
	public void validateDocumentoEntregue(DevedorDocServFat devedorDocServFat, Cliente cliente, Map mapConhecimento) {
		boolean blValido = false;
		
		//Se é um conhecimento
		if (mapConhecimento != null){
			//Se o o documento é 'CIF' e não foi entregue
			if (cliente.getBlFaturaDocsEntregues().equals(Boolean.FALSE)){
				blValido = true;
			}
		
			//Se o documento já foi entregue
			if (devedorDocServFat.getDoctoServico().getNrDiasRealEntrega() != null){
				blValido = true;
			}
			List<OcorrenciaDoctoServico> ocorrenciasbloqueios205 = ocorrenciaDoctoServicoService.findOcorrenciaBloqueioDoctoServicoByCdOcorrencia(devedorDocServFat.getDoctoServico().getIdDoctoServico(),  new Short[]{Short.valueOf("205")});
			//Se não tem evento de entrega
			if (eventoDocumentoServicoService.findEventoDoctoServico(devedorDocServFat.getDoctoServico().getIdDoctoServico(), new Short[]{Short.valueOf("7"), Short.valueOf("90"), Short.valueOf("91")}).size() > 0 || !ocorrenciasbloqueios205.isEmpty()){//LMS-1652 alterado conforme ET 36.02.01.04 acrescentando evento 205
				blValido = true;
			}
			
			//Se o documento não está válido
			if (!blValido){
				throw new BusinessException("LMS-36124");
			}
		}
	}

	/**
	 * Valida a data hora de emissão
	 * 
	 * Regra 3.1
	 */
	private void validateDhEmissao(Fatura fatura, DevedorDocServFat devedorDocServFat) {
		// Se a data de emissao do documento de servço é depois de hoje e for
		// atrasado de 3 dias

		/**@author Mickaël Jalbert
		 * @since 14/06/2006
		 * 
		 * Pode cadastrar uma fatura com qualquer data de emissão, modificação
		 * passado por Joelson e Rita*/
		
		YearMonthDay dt = fatura.getDtEmissao();
		if(dt == null){
			dt = JTDateTimeUtils.getDataAtual();
	}
		if (devedorDocServFat.getDoctoServico().getDhEmissao() != null &&
			devedorDocServFat.getDoctoServico().getDhEmissao().toLocalDate().isAfter(dt.toLocalDate())){
			throw new BusinessException("LMS-36122"); 
		 }

	}

	/**
	 * Valida o cliente do devedor doc serv fat
	 * 
	 * Regra 3.2
	 */
	private void validateCliente(Fatura fatura, DevedorDocServFat devedorDocServFat) {
		// Se o cliente é um CNPJ
		if (fatura.getCliente() != null && fatura.getCliente().getPessoa().getTpIdentificacao().getValue().equals("CNPJ")) {
			String cnpjCliente = fatura.getCliente().getPessoa().getNrIdentificacao().substring(0, 8);
			String cnpjDevedorDocServFat = devedorDocServFat.getCliente().getPessoa().getNrIdentificacao().substring(0, 8);
			// Se os 8 primeiros digitos do cnpj foram diferente, lançar uma
			// exception
			if (!cnpjCliente.equals(cnpjDevedorDocServFat)) {
				throw new BusinessException("LMS-36123");
			}
		} else if (fatura.getCliente() != null) {

			Long idCliente = fatura.getCliente().getIdCliente();
			Long idClienteDevedorDocServFat = devedorDocServFat.getCliente().getIdCliente();

			// Se cliente da fatura for diferente do cliente do
			// devedorDocServFat
			if (!idCliente.equals(idClienteDevedorDocServFat)) {
				throw new BusinessException("LMS-36123");
			}
		}
	}

	/**
	 * Valida a homogenidade dos itens da fatura e da fatura
	 * 
	 * Regra 3.3 / 3.4
	 */
	private void validateIgualdade(Fatura fatura,DevedorDocServFat devedorDocServFat, GenerateFaturaParam generateFaturaParam, Map mapConhecimento) {
		// Se a fatura não é da integração
		if (generateFaturaParam != null && (fatura.getTpOrigem() == null || (fatura.getTpOrigem() != null && !fatura.getTpOrigem().getValue().equals("I") && !fatura.getTpOrigem().getValue().equals("P")))) {
			
			boolean separaFaturaModal = devedorDocServFat.getCliente().getBlSeparaFaturaModal();
			
			// O tipo de documento tem que ser o mesmo para todos os documentos
			// de serviço
			if (!devedorDocServFat.getDoctoServico().getTpDocumentoServico().getValue().equals(generateFaturaParam.getTpDoctoServico())) {
				throw new BusinessException("LMS-36008");
			}

			// O modal e a abrangencia tem que ser a mesma para todos os
			// documentos de servico
			// exceto se o cliente separaFaturaModal = n
			if (devedorDocServFat.getDoctoServico().getServico() != null && 
				(!generateFaturaParam.getTpAbrangencia().equals(devedorDocServFat.getDoctoServico().getServico().getTpAbrangencia().getValue()) || 
				(!(generateFaturaParam.getTpModal().equals(devedorDocServFat.getDoctoServico().getServico().getTpModal().getValue()) || !separaFaturaModal) ))) {
				throw new BusinessException("LMS-36008");
			}

			// A moeda tem que ser a mesma para todos os documentos de serviço
			if (!devedorDocServFat.getDoctoServico().getMoeda().getIdMoeda().equals(generateFaturaParam.getIdMoeda())) {
				throw new BusinessException("LMS-36008");
			}
			
			if (!devedorDocServFat.getCliente().getPessoa().getTpIdentificacao().getValue().equals(generateFaturaParam.getTpIdentificacao())){
				throw new BusinessException("LMS-36008");					
			}			
			
			// Se a pessoa é jurídica, validar só os primeiros dígitos da identificação 
			if (devedorDocServFat.getCliente().getPessoa().getTpIdentificacao().getValue().equals("CNPJ")){
				if (!devedorDocServFat.getCliente().getPessoa().getNrIdentificacao().substring(0,8).equals(generateFaturaParam.getNrIdentificacaoParcial())) {
					throw new BusinessException("LMS-36008");
				}							
			} else {
				if (!devedorDocServFat.getCliente().getIdCliente().equals(generateFaturaParam.getIdCliente())) {
					throw new BusinessException("LMS-36008");
				}
			}
			
			// O tipo de frete tem que ser o mesmo para todos os documentos de
			// serviço e a fatura
			if (mapConhecimento != null && generateFaturaParam.getTpFrete() != null && !((DomainValue)mapConhecimento.get("tpFrete")).getValue().equals(generateFaturaParam.getTpFrete()) && separaFaturaModal) {
				throw new BusinessException("LMS-36127");
			}

			// O serviço tem que ser o mesmo para todos os documentos de serviço
			// e a fatura
			if (devedorDocServFat.getDoctoServico().getServico() != null && !devedorDocServFat.getDoctoServico().getServico().getIdServico().equals(generateFaturaParam.getIdServico()) && separaFaturaModal) {
				throw new BusinessException("LMS-36127");
			}

			// Caso o indicador do cliente do cliente BL_SEPARA_FATURA_MODAL seja igual a S
			// Se a divisao do documento anterior é diferente da divisão do documento atual 
			if(devedorDocServFat.getCliente().getBlSeparaFaturaModal() != null && devedorDocServFat.getCliente().getBlSeparaFaturaModal()){
			if ((devedorDocServFat.getDivisaoCliente() != null && generateFaturaParam.getIdDivisaoCliente() == null) || 
				(devedorDocServFat.getDivisaoCliente() == null && generateFaturaParam.getIdDivisaoCliente() != null) ||
				(devedorDocServFat.getDivisaoCliente() != null && !devedorDocServFat.getDivisaoCliente().getIdDivisaoCliente().equals(generateFaturaParam.getIdDivisaoCliente()))){
				throw new BusinessException("LMS-36223");
			}
		}
	}	
	}	

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}



	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}



	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}	
	
	

	public void setBloqueioFaturamentoService(BloqueioFaturamentoService bloqueioFaturamentoService) {
			this.bloqueioFaturamentoService = bloqueioFaturamentoService;
}



	public OcorrenciaDoctoServicoService getOcorrenciaDoctoServicoService() {
		return ocorrenciaDoctoServicoService;
	}



	public void setOcorrenciaDoctoServicoService(
			OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}




	
}
