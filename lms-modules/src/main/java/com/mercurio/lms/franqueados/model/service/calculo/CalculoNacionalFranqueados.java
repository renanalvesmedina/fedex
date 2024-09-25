package com.mercurio.lms.franqueados.model.service.calculo;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.DistanciaColetaEntregaFranqueado;
import com.mercurio.lms.franqueados.model.DistanciaTransferenciaFranqueado;
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.FixoFranqueado;
import com.mercurio.lms.franqueados.model.MunicipioNaoAtendidoFranqueado;
import com.mercurio.lms.franqueados.model.RepasseFranqueado;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.vendas.model.Cliente;

public class CalculoNacionalFranqueados extends CalculoDocumentoFranqueados {

	@Override
	protected void calcularValorParticipacao() {
		getDoctoServicoFranqueado().setVlParticipacao(getDoctoServicoFranqueado().getVlFixoColetaEntrega().setScale(2, FranqueadoUtils.ROUND_TYPE));
		calcularParticipacaoTransferencia();
		calculoPcLimite();
		calcularParticipacaoRepasse();
		calcularParticipacaoDesconto();
		calcularParticipacaoGeneralidades();
	}

	@Override
	protected void setTipoFrete() {
		Long idFilialOrigem = getDTO().getIdFilialOrigem();
		Long idFilialDestino = getDTO().getIdFilialDestino();
		
		boolean isExpedido = getIdFranquia().equals(idFilialOrigem);
		boolean isRecebido = getIdFranquia().equals(idFilialDestino);

		boolean isCIF = ConstantesExpedicao.TP_FRETE_CIF.equals(getDTO().getTpFrete());
		boolean isFOB = ConstantesExpedicao.TP_FRETE_FOB.equals(getDTO().getTpFrete());
		
		if(isExpedido && isCIF){
			getDoctoServicoFranqueado().setTpFrete(new DomainValue(ConstantesFranqueado.CIF_EXPEDIDO));
			return; 
		}
		if(isExpedido && isFOB){
			getDoctoServicoFranqueado().setTpFrete(new DomainValue(ConstantesFranqueado.FOB_EXPEDIDO));
			return;
		}
		if(isRecebido && isCIF){
			getDoctoServicoFranqueado().setTpFrete(new DomainValue(ConstantesFranqueado.CIF_RECEBIDO));
			return;
		}
		if(isRecebido && isFOB){
			getDoctoServicoFranqueado().setTpFrete(new DomainValue(ConstantesFranqueado.FOB_RECEBIDO));
			return;
		}
		
		throw new BusinessException("LMS-46001");
	}

	@Override
	protected void setGris() {
		getDoctoServicoFranqueado().setVlGris(BigDecimal.ZERO);
	}

	@Override
	protected void setGeneralidade() {
		List<TypedFlatMap> vlParcelaPcParticipacaoList = getParametrosFranqueado().getVlParcelaPcParticipacaoList(getDoctoServicoFranqueado().getTpFrete().getValue(), getDoctoServicoFranqueado().getConhecimento().getIdDoctoServico());
		BigDecimal generalidade = BigDecimal.ZERO;
		if(vlParcelaPcParticipacaoList != null){
			for (TypedFlatMap vlParcelaPcParticipacao : vlParcelaPcParticipacaoList) {
				generalidade = generalidade.add(vlParcelaPcParticipacao.getBigDecimal("VL_PARCELA")).setScale(2, FranqueadoUtils.ROUND_TYPE );
			}
		}
		getDoctoServicoFranqueado().setVlGeneralidade(generalidade);
	}

	@Override
	protected void setTipoOperacao() {
		if( getDTO().getIdFilialOrigem() != null ){
			
			Boolean isTpOperacaoEntreFranquias = getDTO().getQtFranquias() == 2;
					
			if (isTpOperacaoEntreFranquias){
				getDoctoServicoFranqueado().setTpOperacao(new DomainValue(ConstantesFranqueado.TP_OPERACAO_LOCAL_ENTRE_FRANQUIAS));
				return;
			}
			getDoctoServicoFranqueado().setTpOperacao(new DomainValue(ConstantesFranqueado.TP_OPERACAO_FRETE_NACIONAL));
			return;
		}
	}

	@Override
	protected void calcularParticipacaoDesconto() {
		DoctoServicoFranqueado documentoFranqueado = getDoctoServicoFranqueado();
		BigDecimal pcLimite = getPcLimite();
		BigDecimal vlLimite = FranqueadoUtils.calcularPercentual(documentoFranqueado.getVlBaseCalculo(), pcLimite);
		if (documentoFranqueado.getVlParticipacao() != null && documentoFranqueado.getVlParticipacao().compareTo(vlLimite) > 0) {
			if( (ConstantesFranqueado.CIF_RECEBIDO.equalsIgnoreCase(documentoFranqueado.getTpFrete().getValue()) 
					|| ConstantesFranqueado.FOB_EXPEDIDO.equalsIgnoreCase(documentoFranqueado.getTpFrete().getValue()) )
						&& vlLimite.compareTo(BigDecimal.ZERO) == 0){
				documentoFranqueado.setVlDescontoLimitador(BigDecimal.ZERO);
			} else if (vlLimite.compareTo(BigDecimal.ZERO) == 0){
				documentoFranqueado.setVlDescontoLimitador(buscarDescontoLimitador(documentoFranqueado.getTpFrete().getValue(),documentoFranqueado.getVlParticipacao(), vlLimite));
				documentoFranqueado.setVlParticipacao(BigDecimal.ZERO);
			}else if (pcLimite.compareTo(BigDecimal.valueOf(100)) < 0){
				documentoFranqueado.setVlDescontoLimitador(buscarDescontoLimitador(documentoFranqueado.getTpFrete().getValue(),documentoFranqueado.getVlParticipacao(), vlLimite));
				documentoFranqueado.setVlParticipacao(vlLimite.setScale(2, FranqueadoUtils.ROUND_TYPE));
			}
		}
	}

	
	private BigDecimal buscarDescontoLimitador(String tpFrete, BigDecimal vlParticipacao, BigDecimal vlLimite) {
		BigDecimal descontoLimitador = BigDecimal.ZERO;
		if(tpFrete != null){
			if(ConstantesFranqueado.CIF_EXPEDIDO.equalsIgnoreCase(tpFrete)
					|| ConstantesFranqueado.FOB_EXPEDIDO.equalsIgnoreCase(tpFrete)
					|| ConstantesFranqueado.CIF_RECEBIDO.equalsIgnoreCase(tpFrete)
					|| ConstantesFranqueado.FOB_RECEBIDO.equalsIgnoreCase(tpFrete)){
				
				descontoLimitador = vlParticipacao.subtract(vlLimite).setScale(2, FranqueadoUtils.ROUND_TYPE);
				
			}
		}
		return descontoLimitador;
	}
	
	@Override
	protected void setCustoCarreteiro() {
		getDoctoServicoFranqueado().setVlCustoCarreteiro(getDTO().getVlCustoCarreteiro());
	}

	@Override
	protected void setCustoAereo() {
		getDoctoServicoFranqueado().setVlCustoAereo(getDTO().getVlCustoAereo());
	}

	@Override
	protected void setMunicipio() {
		
		Municipio municipio = new Municipio();
		Cliente cliente = new Cliente();

		if (getDTO().getIdClienteRemetente() != null) {
			cliente.setIdCliente(getDTO().getIdClienteRemetente());
		}
		if (getDoctoServicoFranqueado().getTpFrete().getValue().equals(ConstantesFranqueado.CIF_EXPEDIDO)) {
			if (getDTO().getIdMunicipioColeta() != null) {
				municipio.setIdMunicipio(getDTO().getIdMunicipioColeta());
			}

		} else if (getDoctoServicoFranqueado().getTpFrete().getValue().equals(ConstantesFranqueado.FOB_EXPEDIDO)) {
			if (getDTO().getIdMunicipioColeta() != null) {
				municipio.setIdMunicipio(getDTO().getIdMunicipioColeta());
			}

		} else if (getDoctoServicoFranqueado().getTpFrete().getValue().equals(ConstantesFranqueado.CIF_RECEBIDO)) {
			if (getDTO().getIdMunicipioEntrega() != null) {
				municipio.setIdMunicipio(getDTO().getIdMunicipioEntrega());
			}

		} else if (getDoctoServicoFranqueado().getTpFrete().getValue().equals(ConstantesFranqueado.FOB_RECEBIDO)) {
			if (getDTO().getIdMunicipioEntrega() != null) {
				municipio.setIdMunicipio(getDTO().getIdMunicipioEntrega());
			}
		}

		getDoctoServicoFranqueado().setMunicipio(municipio);

		FixoFranqueado fixoFranqueado = getFixoFranqueadoVigente(cliente, municipio);

		if (getDoctoServicoFranqueado().getTpFrete().getValue().equals(ConstantesFranqueado.CIF_EXPEDIDO)
				|| getDoctoServicoFranqueado().getTpFrete().getValue().equals(ConstantesFranqueado.FOB_EXPEDIDO)) {
			getDoctoServicoFranqueado().setVlFixoColetaEntrega(fixoFranqueado.getVlColeta());
		} else {
			getDoctoServicoFranqueado().setVlFixoColetaEntrega(fixoFranqueado.getVlEntrega());
		}

	}

	protected FixoFranqueado getFixoFranqueadoVigente(Cliente cliente, Municipio municipio){ 
		return getParametrosFranqueado().findFixoFranqueado(cliente.getIdCliente(),
			municipio.getIdMunicipio(), null);
	}
	
	
	private void calcularParticipacaoTransferencia() {
		
			DoctoServicoFranqueado doc = getDoctoServicoFranqueado();
			try {
			int nrKm = doc.getNrKmTransferencia();

			DistanciaTransferenciaFranqueado distanciaTransferencia = getParametrosFranqueado()
					.getDistanciaTransferenciaFranqueado(doc.getTpFrete().getValue(), doc.getNrKmTransferencia());

			BigDecimal vlKmTransferencia = BigDecimal.valueOf(0);
			BigDecimal vlParticipacao = BigDecimal.valueOf(0);

			if (distanciaTransferencia != null) {

				vlKmTransferencia = doc.getVlBaseCalculo()
						.multiply((distanciaTransferencia.getPcParticipacao()
								.subtract(distanciaTransferencia.getVlMultiplicador().multiply(new BigDecimal(nrKm))))
								.divide(new BigDecimal(100)))
						.setScale(2, FranqueadoUtils.ROUND_TYPE);

				vlParticipacao = doc.getVlParticipacao().add(vlKmTransferencia).setScale(2,
						FranqueadoUtils.ROUND_TYPE);
			}

			Integer nrKmAsfaltoCalculado = Integer.valueOf(0);
			Integer nrKmTerraCalculado = Integer.valueOf(0);
			BigDecimal vlKmColetaEntrega = BigDecimal.ZERO;
			Integer nrKmAsfalto = Integer.valueOf(0);
			Integer nrKmTerra = Integer.valueOf(0);
			MunicipioFilial municipioFilial = getParametrosFranqueado()
					.getMunicipioFilial(getDoctoServicoFranqueado().getMunicipio().getIdMunicipio());
			MunicipioNaoAtendidoFranqueado municipioNaoAtendidoFranqueado = getParametrosFranqueado()
					.getMunicipioNaoAtendidoFranqueado(getDoctoServicoFranqueado().getMunicipio().getIdMunicipio());

			if (getParametrosFranqueado().isPercentualTerra()) {
				if (municipioFilial != null) {
					nrKmAsfalto = municipioFilial.getNrDistanciaAsfalto().intValue();
					nrKmTerra = municipioFilial.getNrDistanciaChao().intValue();
				} else if (municipioNaoAtendidoFranqueado != null) {
					nrKmAsfalto = municipioNaoAtendidoFranqueado.getNrKmAsfalto();
					nrKmTerra = municipioNaoAtendidoFranqueado.getNrKmTerra();
				}
			} else {
				if (municipioFilial != null) {
					nrKmAsfalto = municipioFilial.getNrDistanciaAsfalto() + municipioFilial.getNrDistanciaChao();
				} else if (municipioNaoAtendidoFranqueado != null) {
					nrKmAsfalto = municipioNaoAtendidoFranqueado.getNrKmAsfalto()
							+ municipioNaoAtendidoFranqueado.getNrKmTerra();
				}
			}

			BigDecimal nrKmMinimoColetaEntrega = getParametrosFranqueado().getNrKmMinimoColetaEntrega();
			if (nrKmMinimoColetaEntrega != null) {
				if (nrKmMinimoColetaEntrega.compareTo(new BigDecimal(nrKmAsfalto + nrKmTerra)) > 0) {
					nrKmAsfalto = nrKmMinimoColetaEntrega.intValue();
				}
			}

			// 4.7
			for (DistanciaColetaEntregaFranqueado distanciaColetaEntregaFranqueado : getParametrosFranqueado()
					.getLstDistanciaColetaEntrega()) {

				// 4.7.1
				if (distanciaColetaEntregaFranqueado.getNrKm().intValue() <= (nrKmAsfalto + nrKmAsfaltoCalculado)
						&& distanciaColetaEntregaFranqueado.getTpPavimento().getValue()
						.equals(ConstantesFranqueado.TP_PAVIMENTO_ASFALTO)) {

					vlKmColetaEntrega = vlKmColetaEntrega.add((doc.getVlBaseCalculo()
							.multiply((distanciaColetaEntregaFranqueado.getVlMultiplicador()
									.multiply((new BigDecimal(distanciaColetaEntregaFranqueado.getNrKm())
											.subtract(new BigDecimal(nrKmAsfaltoCalculado))))))).divide(new BigDecimal(100))
							.setScale(2, FranqueadoUtils.ROUND_TYPE));

					nrKmAsfaltoCalculado = nrKmAsfaltoCalculado.intValue()
							+ distanciaColetaEntregaFranqueado.getNrKm().intValue();
					nrKmAsfalto = nrKmAsfalto.intValue() - distanciaColetaEntregaFranqueado.getNrKm();

				} else if (nrKmAsfalto > 0 && distanciaColetaEntregaFranqueado.getTpPavimento().getValue()
						.equals(ConstantesFranqueado.TP_PAVIMENTO_ASFALTO)) {

					vlKmColetaEntrega = vlKmColetaEntrega.add((doc.getVlBaseCalculo()
							.multiply((distanciaColetaEntregaFranqueado.getVlMultiplicador()
									.multiply(new BigDecimal(nrKmAsfalto))))).divide(new BigDecimal(100)).setScale(2,
							FranqueadoUtils.ROUND_TYPE));

					nrKmAsfaltoCalculado = nrKmAsfaltoCalculado.intValue() + nrKmAsfalto.intValue();
					nrKmAsfalto = Integer.valueOf(0);
				}

				// 4.7.2
				if (distanciaColetaEntregaFranqueado.getNrKm().intValue() <= (nrKmTerra + nrKmTerraCalculado)
						&& distanciaColetaEntregaFranqueado.getTpPavimento().getValue()
						.equals(ConstantesFranqueado.TP_PAVIMENTO_TERRA)) {

					vlKmColetaEntrega = vlKmColetaEntrega
							.add((doc.getVlBaseCalculo()
									.multiply((distanciaColetaEntregaFranqueado.getVlMultiplicador()
											.multiply((new BigDecimal(distanciaColetaEntregaFranqueado.getNrKm())
													.subtract(new BigDecimal(nrKmTerraCalculado))))))))
							.divide(new BigDecimal(100)).setScale(2, FranqueadoUtils.ROUND_TYPE);

					nrKmTerraCalculado = nrKmTerraCalculado.intValue()
							+ distanciaColetaEntregaFranqueado.getNrKm().intValue();
					nrKmTerra = nrKmTerra.intValue() - distanciaColetaEntregaFranqueado.getNrKm();

				} else if (nrKmTerra.intValue() > 0 && distanciaColetaEntregaFranqueado.getTpPavimento().getValue()
						.equals(ConstantesFranqueado.TP_PAVIMENTO_TERRA)) {

					vlKmColetaEntrega = vlKmColetaEntrega
							.add((doc.getVlBaseCalculo()
									.multiply((distanciaColetaEntregaFranqueado.getVlMultiplicador()
											.multiply(new BigDecimal(nrKmTerra))))))
							.divide(new BigDecimal(100)).setScale(2, FranqueadoUtils.ROUND_TYPE);

					nrKmTerraCalculado = nrKmTerraCalculado.intValue() + nrKmTerra.intValue();
					nrKmTerra = Integer.valueOf(0);
				}
			}

			doc.setNrKmColetaEntrega(nrKmAsfaltoCalculado.intValue() + nrKmTerraCalculado.intValue());
			doc.setVlParticipacao(vlParticipacao.add(vlKmColetaEntrega));
			doc.setVlKmColetaEntrega(vlKmColetaEntrega);
			doc.setVlKmTransferencia(vlKmTransferencia);
		}catch(Exception e){
			throw new BusinessException(e.getMessage() + " " + doc.getConhecimento().getNrDoctoServico().longValue());
		}

	}

	private void calcularParticipacaoRepasse() {
		DoctoServicoFranqueado doc = getDoctoServicoFranqueado();
		if (doc.getVlBaseCalculo().compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal pcParticipacao = doc.getVlParticipacao().divide(doc.getVlDoctoServico(), 5,
					FranqueadoUtils.ROUND_TYPE);

			doc.setVlRepassePis(calcularRepasseImposto(doc.getVlPis(), getParametrosFranqueado().getRepasseFranqueado().getPcRepassePis(),
					pcParticipacao));

			doc.setVlRepasseCofins(calcularRepasseImposto(doc.getVlCofins(),
					getParametrosFranqueado().getRepasseFranqueado().getPcRepasseCofins(), pcParticipacao));

			doc.setVlRepasseIcms(calcularRepasseImposto(doc.getVlIcms(),
					getParametrosFranqueado().getRepasseFranqueado().getPcRepasseIcms(), pcParticipacao));

			doc.setVlParticipacao(doc.getVlParticipacao().add(doc.getVlRepasseIcms()).add(doc.getVlRepasseCofins())
					.add(doc.getVlRepassePis()));
		}

	}
	
	private BigDecimal calcularRepasseImposto(BigDecimal vlImposto, BigDecimal pcRepasseImposto,
			BigDecimal pcParticipacao) {
		if( vlImposto == null ){
			vlImposto = BigDecimal.ZERO;
		}
		BigDecimal vlRepasseImposto = FranqueadoUtils.calcularPercentual(vlImposto, pcRepasseImposto).setScale(2,
				FranqueadoUtils.ROUND_TYPE);
		return vlRepasseImposto.multiply(pcParticipacao).setScale(2, FranqueadoUtils.ROUND_TYPE);
	}

	private BigDecimal calcularParticipacaoPorGeneralidade(BigDecimal vlParcela, BigDecimal pcAliqIcms,
			BigDecimal pcParticipacao, RepasseFranqueado repasseFranqueado) {

		BigDecimal vlPisGeneralidade = FranqueadoUtils.calcularPercentual(vlParcela, repasseFranqueado.getPcAliqPis())
				.setScale(2, FranqueadoUtils.ROUND_TYPE);
		BigDecimal vlCofinsGeneralidade = FranqueadoUtils
				.calcularPercentual(vlParcela, repasseFranqueado.getPcAliqCofins())
				.setScale(2, FranqueadoUtils.ROUND_TYPE);
		BigDecimal vlIcmsGeneralidade = FranqueadoUtils.calcularPercentual(vlParcela, pcAliqIcms).setScale(2,
				FranqueadoUtils.ROUND_TYPE);
		vlParcela = vlParcela.subtract(vlPisGeneralidade).setScale(2, FranqueadoUtils.ROUND_TYPE);
		vlParcela = vlParcela.subtract(vlCofinsGeneralidade).setScale(2, FranqueadoUtils.ROUND_TYPE);
		vlParcela = vlParcela.subtract(vlIcmsGeneralidade).setScale(2, FranqueadoUtils.ROUND_TYPE);

		return FranqueadoUtils.calcularPercentual(vlParcela, pcParticipacao);
	}
	
	protected void calcularParticipacaoGeneralidades() {
		DoctoServicoFranqueado doc = getDoctoServicoFranqueado();
		if (doc.getVlGeneralidade().compareTo(BigDecimal.ZERO) > 0) {

			BigDecimal vlGeneralidade = BigDecimal.ZERO;
			BigDecimal pcParticipacao = BigDecimal.ZERO;
			BigDecimal vlParcela = BigDecimal.ZERO;

			BigDecimal pcAliqIcms = doc.getVlIcms().divide(doc.getVlDoctoServico(), 4,
					FranqueadoUtils.ROUND_TYPE);
			pcAliqIcms = pcAliqIcms.multiply(FranqueadoUtils.MULTIPLICADOR_PERCENTUAL).setScale(2,
					FranqueadoUtils.ROUND_TYPE);

			for (TypedFlatMap vlParcelaParticipacaoMap : getParametrosFranqueado().getVlParcelaParticipacaoList(doc.getTpFrete().getValue(), doc.getConhecimento().getIdDoctoServico())) {
				vlParcela = vlParcelaParticipacaoMap.getBigDecimal("VL_PARCELA");
				pcParticipacao = vlParcelaParticipacaoMap.getBigDecimal("PC_PARTICIPACAO");

				vlGeneralidade = vlGeneralidade.add(calcularParticipacaoPorGeneralidade(vlParcela, pcAliqIcms,
						pcParticipacao, getParametrosFranqueado().getRepasseFranqueado()));
			}

			doc.setVlRepasseGeneralidade(vlGeneralidade);

			doc.setVlParticipacao(doc.getVlParticipacao().add(vlGeneralidade));
		}

	}
	
}
