package com.mercurio.lms.tabelaprecos.util;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeConstants;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.tabelaprecos.model.ReajusteTabelaPreco;
import com.mercurio.lms.util.JTDateTimeUtils;

public class ReajusteTabelaPrecoValidate {
	
	private static final  String TABELA_PRECO = "tabelaPreco=";
	private static final  String DD_MM_YYYY = "dd/MM/yyyy";
	private static final  String TNT = "T";
	private static final  String MERCURIO = "M";
	private static final  String AEREO = "A";
	private static final  String[] TABELAS_REFERENCIAIS = { TNT, MERCURIO, AEREO };
	private static final  Integer ONE_DAY = 1;
	
	
	public boolean validateBeforeStore(ReajusteTabelaPreco reajuste){
		
		if(!emptyValue(reajuste.getDtAgendamento()) && !validateDate(reajuste.getDtAgendamento())){
			throw new BusinessException("LMS-01258");
		}
		
		if(!emptyValue(reajuste.getDtVigenciaInicial()) && !emptyValue(reajuste.getDtVigenciaFinal()) && 
					   compareDataFinalMenorDataInicial(reajuste.getDtVigenciaInicial(), reajuste.getDtVigenciaFinal())){
			throw new BusinessException("LMS-00008");
		}
		
		if(!validateFieldStore(reajuste)){
			throw new BusinessException("LMS-00070");
		}
		
		if(!(reajuste.getTabelaBase().startsWith(reajuste.getTipo()) || reajuste.getTabelaBase().contains(TABELA_PRECO+reajuste.getTipo()))){  
			throw new BusinessException("LMS-01161");
		}
		
		if(reajuste.getDtAgendamento() != null && !existSundayBetweenSchedule(reajuste.getDtAgendamento())){
			throw new BusinessException("LMS-30080");
		}
		
		boolean isReferencialSubTipoX = this.isTabelaReferencial(reajuste.getTipo()) && "X".equals(reajuste.getSubTipo());		
		if (reajuste.isCheckedFechaVigenciaTabelaBase() && !isReferencialSubTipoX) {
			throw new BusinessException("LMS-30088");
		}
		
		return true;
	}
	
	
	public boolean existSundayBetweenSchedule(YearMonthDay dataAgendamento){
		YearMonthDay date = JTDateTimeUtils.getDataAtual();
		Integer intervalo = JTDateTimeUtils.getIntervalInDays(JTDateTimeUtils.getDataAtual(),dataAgendamento);

		for(int i=0 ; i < intervalo ; i++){
			if (isSunday(date)) {
				return Boolean.TRUE;
			}
			date = date.plusDays(ONE_DAY);
		}
		
		return Boolean.FALSE;
	}
	
	public boolean isSunday(YearMonthDay date){
		return JTDateTimeUtils.getNroDiaSemana(date) == DateTimeConstants.SUNDAY;
	}
	
	public boolean validateFieldStore(ReajusteTabelaPreco reajuste){
		boolean isValid = true;
		
		if(reajuste.getPercentualReajusteGeral() == null){
			isValid = false;
		}
		
		if(reajuste.getIdTabelaBase() == null){
			isValid = false;
		}
		
		if(reajuste.getDtVigenciaInicial() == null){
			isValid = false;
		}
		
		if(reajuste.getTipo() == null){
			isValid = false;
		}
		
		if(reajuste.getSubTipo() == null){
			isValid = false;
		}
		
		if(reajuste.getVersao() == null){
			isValid = false;
		}
		
		return isValid;
	}
	
	public boolean compareDataFinalMenorDataInicial(YearMonthDay dataInicial, YearMonthDay dataFinal){
		return dataFinal.isBefore(dataInicial);
	}
	
	public boolean validateDate(YearMonthDay data){
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		YearMonthDay dataInformada = data;
		return !dataAtual.isEqual(dataInformada) && dataInformada.isAfter(dataAtual);
	}
	
	public boolean validateInsertTipoTabela(Long idTipoTabelaPreco, String tipoTabelaPreco){
		if(idTipoTabelaPreco != null){
			return false;
		}
		
		if(idTipoTabelaPreco == null &&  !isTabelaReferencial(tipoTabelaPreco)){
			throw new BusinessException("LMS-01255"); 
		} 
		
		return true;
	}
	
	public boolean validateInsertTabelaPreco(ReajusteTabelaPreco reajuste, Long idTabelaPreco, String tipoTabela){
		if(idTabelaPreco != null && isTabelaReferencial(tipoTabela)){
			throw new BusinessException(
				"LMS-30087", 
				new Object[] { reajuste.getTipo(), reajuste.getVersao(), reajuste.getSubTipo()}
			); 
		}
		
		return true;
	}
	
	public boolean isTabelaReferencial(String tipoTabela){
		return Arrays.asList(TABELAS_REFERENCIAIS).contains(tipoTabela);
	}
	
	public boolean isConsultaValida(Map criteria){
		return criteria != null && containValue(criteria);
	}
	
	public boolean containValue(Map criteria){
		return  containValue(criteria, "id") ||
				containValue(criteria, "tabelaBase.idTabelaPreco") ||
				containValue(criteria, "tabelaNova.idTabelaPreco")  ||
				containValue(criteria, "dtGeracaoInicial")  ||
				containValue(criteria, "dtGeracaoFinal")  ||
				containValue(criteria, "nrReajuste")  ||
				containValue(criteria, "filial.idFilial");
	}

	private static boolean containValue(Map criteria, String key){
		return criteria.get(key) != null && StringUtils.isNotBlank(criteria.get(key).toString());
	}
	
	public boolean emptyValue(YearMonthDay value){
		 return value == null;
	}
	
	private YearMonthDay convertData(String data){
		return JTDateTimeUtils.convertDataStringToYearMonthDay(data, DD_MM_YYYY);
	}
	
}
