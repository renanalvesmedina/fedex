package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;

import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

public class CalculoParcelaC1DHStrategy extends CalculoParcelaC1Strategy {

    private static final Long DIARIA_MAXIMA = 12L;
    private static final BigDecimal DIARIA_PADRAO = BigDecimal.valueOf(0.5);
    private List<ControleCarga> controlesCargaNoPeriodo;
    
	private FilialService filialService;

    @Override
    public void executeCalculo() {
        controlesCargaNoPeriodo = getControlesCargaProprietarioNoPeriodo();

        setParcela(getNotaCreditoParcela(TipoParcela.DIARIA), parcelaTabela, calculaDiarias(),
                parcelaTabela.getVlDefinido(), hasNotaCreditoParcela(TipoParcela.DIARIA));
    }

    private BigDecimal calculaDiarias() {
        BigDecimal diarias = calculaDiariasEmHoras();

		//Jira LMS-3581
		Long idFilial = getCalculo().getNotaCredito().getFilial().getIdFilial();
		Boolean filialPagaDiariaExcedente = filialService.findById(idFilial).getBlPagaDiariaExcedente();
		if (BooleanUtils.isFalse(filialPagaDiariaExcedente)) {
			if (diarias.doubleValue() > 1) {
				diarias = BigDecimal.ONE;
			}
		}
        
        diarias = diarias.subtract(calculaDiariasAnteriores(parcelaTabela));

        if (diarias == null || diarias.compareTo(BigDecimal.ZERO) < 0) {
            diarias = BigDecimal.ZERO;
        }

        return diarias;
    }

    private BigDecimal calculaDiariasAnteriores(ParcelaTabelaCe parcelaTabela) {
        BigDecimal diariasAnteriores = BigDecimal.ZERO;

        for (ControleCarga controleCarga : controlesCargaNoPeriodo) {
            for (NotaCredito nota : controleCarga.getNotasCredito()) {
                if (!nota.equals(getCalculo().getNotaCredito()) && hasNotaCreditoDiariaInferior(nota)) {
                    diariasAnteriores = diariasAnteriores.add(calculaDiariasEfetuadas(nota));
                }
            }
        }

        return diariasAnteriores;
    }

    private BigDecimal calculaDiariasEfetuadas(NotaCredito nota) {
        BigDecimal totalDiarias = BigDecimal.ZERO;

        for (NotaCreditoParcela parcela : nota.getNotaCreditoParcelas()) {
            if (isParcelaDiaria(parcela.getParcelaTabelaCe())) {
                totalDiarias = totalDiarias.add(parcela.getQtNotaCreditoParcela());
            }
        }

        return totalDiarias;
    }

    private BigDecimal calculaHorasExcedentes() {
        return BigDecimal.valueOf(Math.floor(getTotalHorasCarreteiro() / DAY_IN_HOURS)).multiply(
                parcelaTabela.getPcSobreValor().divide(PERCENTUAL));
    }

    private BigDecimal calculaDiariasEmHoras() {
    	Double horasViagem = getTotalHorasCarreteiro();
    	
    	BigDecimal diarias = BigDecimal.ZERO;

        if (horasViagem > getTotalHorasDiaria()) {
        	diarias = BigDecimal.ONE;
            
            if (horasViagem > DIARIA_MAXIMA){
            	BigDecimal horasAdicionais = BigDecimal.valueOf(Math.ceil((horasViagem - DIARIA_MAXIMA) / DIARIA_MAXIMA));
            	diarias = diarias.add(horasAdicionais.multiply(DIARIA_PADRAO));
            }
        }else{
        	return isTurnoExcedido() ? BigDecimal.ONE : DIARIA_PADRAO;
        }
        
        if (BigDecimalUtils.ltZero(diarias)){
        	return BigDecimal.ZERO;
        }
        
        return diarias;
    }

    private boolean hasCalculoPercentualSobreValor() {
        return hasPercentualSobreValorParcela(parcelaTabela) && hasPernoiteNaViagem(getTotalHorasCarreteiro());
    }

    private boolean isTurnoExcedido() {
        DateTime horarioTrocaTurno = getHorarioTrocaTurno();

        if (horarioTrocaTurno != null) {
            DateTime primeiraSaida = getCalculo().getNotaCredito().getControleCarga().getDhSaidaColetaEntrega();
            DateTime ultimaChegada = getCalculo().getNotaCredito().getControleCarga().getDhChegadaColetaEntrega();
            
            if (primeiraSaida != null && ultimaChegada != null) {
                for (ControleCarga controleCarga : controlesCargaNoPeriodo) {
                    if (isControleCargaValidoParaTurnoExcedido(controleCarga)) {
                        primeiraSaida = getPrimeiraSaida(primeiraSaida, controleCarga.getDhSaidaColetaEntrega());
                        ultimaChegada = getUltimaChegada(ultimaChegada, controleCarga.getDhChegadaColetaEntrega());
                    }
                }

                DateTime turnoPrimeiraSaida = JTDateTimeUtils.getFirstHourOfDay(primeiraSaida);
                DateTime turnoUltimaChegada = JTDateTimeUtils.getFirstHourOfDay(ultimaChegada);
                turnoPrimeiraSaida = turnoPrimeiraSaida.plusHours(horarioTrocaTurno.getHourOfDay());
                turnoPrimeiraSaida = turnoPrimeiraSaida.plusMinutes(horarioTrocaTurno.getMinuteOfHour());
                turnoPrimeiraSaida = turnoPrimeiraSaida.plusSeconds(horarioTrocaTurno.getSecondOfMinute());
                turnoUltimaChegada = turnoUltimaChegada.plusHours(horarioTrocaTurno.getHourOfDay());
                turnoUltimaChegada = turnoUltimaChegada.plusMinutes(horarioTrocaTurno.getMinuteOfHour());
                turnoUltimaChegada = turnoUltimaChegada.plusSeconds(horarioTrocaTurno.getSecondOfMinute());                      
                 
                return primeiraSaida.compareTo(turnoPrimeiraSaida) < 0 && ultimaChegada.compareTo(turnoUltimaChegada) > 0;
            }
        }

        return false;
    }

    private DateTime getPrimeiraSaida(DateTime primeiraSaida, DateTime saida) {
        if (primeiraSaida == null || JTDateTimeUtils.comparaDatas(primeiraSaida, saida)) {
            return saida;
        }

        return primeiraSaida;
    }

    private DateTime getUltimaChegada(DateTime ultimaChegada, DateTime chegada) {
        if (ultimaChegada == null || JTDateTimeUtils.comparaDatas(chegada, ultimaChegada)) {
            return chegada;
        }

        return ultimaChegada;
    }

    private boolean isControleCargaValidoParaTurnoExcedido(ControleCarga controleCarga) {
        return controleCarga.getDhSaidaColetaEntrega() != null && controleCarga.getDhChegadaColetaEntrega() != null;
    }

    private Double getTotalHorasCarreteiro() {
    	Double horasViagem = 0D;
  
        for (ControleCarga controleCarga : controlesCargaNoPeriodo) {
            horasViagem += getTempoViagemEmHoras(controleCarga);
        }

        return horasViagem;
    }

    private Long getTotalHorasDiaria() {
        return getCalculo().notaCreditoService.findTotalHorasDiaria();
    }

    private DateTime getHorarioTrocaTurno() {
        return getCalculo().notaCreditoService.findHorarioTrocaTurno();
    }

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

}
