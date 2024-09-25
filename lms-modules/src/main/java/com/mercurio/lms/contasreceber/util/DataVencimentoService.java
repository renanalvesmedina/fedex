package com.mercurio.lms.contasreceber.util;

import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.contasreceber.model.param.DataVencimentoParam;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.DiaVencimento;
import com.mercurio.lms.vendas.model.PrazoVencimento;
import com.mercurio.lms.vendas.model.service.DiaVencimentoService;
import com.mercurio.lms.vendas.model.service.PrazoVencimentoService;

/**
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.contasreceber.util.dataVencimentoService"
 */

public class DataVencimentoService {

	private PrazoVencimentoService prazoVencimentoService;

	private DiaVencimentoService diaVencimentoService;

	private FilialService filialService;

	public YearMonthDay generateDataVencimento(final Long idFilial, final Long idDivisao, final String tpFrete, final YearMonthDay dtEmissao,
			final String tpModal, final String tpAbrangencia, final Long idServico) {
		YearMonthDay dtVencimento = null;
		if((dtEmissao != null) && (idFilial != null)) {
			final Filial filial = filialService.findById(idFilial);

			// Esses 3 parametros são obrigatório para buscar o prazo
			if(idDivisao != null) {
				dtVencimento = calcularDtVencimentoPrazo(new DataVencimentoParam(idServico, idDivisao, tpFrete, dtEmissao, tpModal, tpAbrangencia));
			}

			// Se não tinha prazo cadastrado, buscar a data de vencimento da
			// filial
			if(dtVencimento == null) {
				dtVencimento = dtEmissao.plusDays(filial.getNrPrazoCobranca().intValue());
			}

			}
		return dtVencimento;
	}

	private YearMonthDay calcularDtVencimentoPrazo(final DataVencimentoParam dataVencimentoParam) {
		YearMonthDay dtVencimento = null;
		PrazoVencimento prazoVencimento = prazoVencimentoService.findPrazoVencimentoByCriteria(dataVencimentoParam);

		if(prazoVencimento == null) {
			prazoVencimento = prazoVencimentoService.findPrazoVencimentoPadrao(dataVencimentoParam.getIdDivisao());
		}

		// Se existe um prazo de vencimento
		if(prazoVencimento != null) {
			// Adicionar o número de dias a data de emissao
			dtVencimento = dataVencimentoParam.getDtEmissao().plusDays(prazoVencimento.getNrPrazoPagamento().intValue());

			// Buscar o proximo dia de vencimento do prazo informado
			DiaVencimento diaVencimento = diaVencimentoService.findByPrazoVencimentoAndDiaMinimo(prazoVencimento.getIdPrazoVencimento(),
					dtVencimento.getDayOfMonth());

			// Se existe
			if(diaVencimento != null) {
				if(diaVencimento.getNrDiaVencimento() > dtVencimento.property(DateTimeFieldType.dayOfMonth()).withMaximumValue().getDayOfMonth()) {
					dtVencimento = dtVencimento.withField(DateTimeFieldType.dayOfMonth(),
							dtVencimento.property(DateTimeFieldType.dayOfMonth()).withMaximumValue().getDayOfMonth());
				} else {
					dtVencimento = dtVencimento.withField(DateTimeFieldType.dayOfMonth(), diaVencimento.getNrDiaVencimento().intValue());
				}
			} else { // Senão buscar o menor dia de vencimento
				diaVencimento = diaVencimentoService.findByPrazoVencimentoAndDiaMinimo(prazoVencimento.getIdPrazoVencimento(), 1);

				if(diaVencimento != null) {
					// Trocar para o proximo mes
					dtVencimento = dtVencimento.plusMonths(1);
					if(diaVencimento.getNrDiaVencimento() > dtVencimento.property(DateTimeFieldType.dayOfMonth()).withMaximumValue().getDayOfMonth()) {
						dtVencimento = dtVencimento.withField(DateTimeFieldType.dayOfMonth(),
								dtVencimento.property(DateTimeFieldType.dayOfMonth()).withMaximumValue().getDayOfMonth());
					} else {
						dtVencimento = dtVencimento.withField(DateTimeFieldType.dayOfMonth(), diaVencimento.getNrDiaVencimento().intValue());
					}
				}
			}
		
			if (prazoVencimento.getTpDiaSemana() != null && !"null".equals(prazoVencimento.getTpDiaSemana().getValue())){
				int nrDiaPrazoVencimento = JTDateTimeUtils.getNroDiaSemana(dtVencimento);
				int nrDiaSemana = Integer.parseInt(prazoVencimento.getTpDiaSemana().getValue());
				if (nrDiaPrazoVencimento != nrDiaSemana){
					if (nrDiaSemana > nrDiaPrazoVencimento){
						dtVencimento = dtVencimento.plusDays(nrDiaSemana - nrDiaPrazoVencimento);		
					} else {
						dtVencimento = dtVencimento.plusDays(nrDiaSemana + (7 - nrDiaPrazoVencimento));		
					}
				}
			}
		
		}

		return dtVencimento;
	}

	public void setPrazoVencimentoService(
			final PrazoVencimentoService prazoVencimentoService) {
		this.prazoVencimentoService = prazoVencimentoService;
	}

	public void setDiaVencimentoService(final DiaVencimentoService diaVencimentoService) {
		this.diaVencimentoService = diaVencimentoService;
	}

	public void setFilialService(final FilialService filialService) {
		this.filialService = filialService;
	}

}