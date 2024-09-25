package com.mercurio.lms.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.DiaSemana;
import com.mercurio.lms.configuracoes.model.service.DiaSemanaService;
import com.mercurio.lms.municipios.model.service.FeriadoService;

/**
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.util.DiaUtils"
 */

public class DiaUtils {
	
	private FeriadoService feriadoService;
	
	/**
	 * Retorna o pr�ximo dia util da data informada.
	 * 
	 * @author Micka�l Jalbert
	 * @since 27/03/2006
	 * 
	 * @param YearMonthDay dtUtil
	 * @param Long idMunicipio
	 * @return YearMonthDay
	 * */
	public YearMonthDay getNextDiaUtil(YearMonthDay dtUtil, Long idMunicipio){
		return getNextDiaUtil(JTDateTimeUtils.yearMonthDayToDateTime(dtUtil), idMunicipio).toYearMonthDay();
	}
	
	/**
	 * Retorna o pr�ximo dia util da data informada se ela n�o for um dia �til.
	 * 
	 * @author Micka�l Jalbert
	 * @since 10/04/2006
	 * 
	 * @param YearMonthDay dtUtil
	 * @param Long idMunicipio
	 * @return YearMonthDay
	 * */
	public YearMonthDay getNextDiaUtilIfNotUtil(YearMonthDay dtUtil, Long idMunicipio){
		if (isDiaUtil(dtUtil, idMunicipio) == false){
			return getNextDiaUtil(JTDateTimeUtils.yearMonthDayToDateTime(dtUtil), idMunicipio).toYearMonthDay();				
		} else {
			return dtUtil;
		}
	}	
	
	/**
	 * Retorna o pr�ximo dia util da data informada.
	 * 
	 * @author Micka�l Jalbert
	 * @since 27/03/2006
	 * 
	 * @param DateTime dtUtil
	 * @param Long idMunicipio
	 * @return YearMonthDay
	 * */
	public DateTime getNextDiaUtil(DateTime dtUtil, Long idMunicipio){
		//Adicionar um dia para n�o retornar a mesma data que aquela informada
		dtUtil = dtUtil.plusDays(1);
		
		//At� encontrar uma data util
		while (!isDiaUtil(dtUtil.toYearMonthDay(), idMunicipio)){
			dtUtil = dtUtil.plusDays(1);
		}
		
		return dtUtil;
	}
	
	/**
	 * Retorna um boolean informando se a data informada � um dia util
	 * 
	 * @since 19/08/2011
	 * 
	 * @param DateTime dtUtil
	 * @param Long idMunicipio
	 * @return boolean
	 * */
	public boolean isDiaUtil(YearMonthDay dtUtil, Long idMunicipio){
		return feriadoService.validateDiaUtil(dtUtil, idMunicipio);
	}	
	
	/**
	 * @author Jos� Rodrigo Moraes
	 * 
	 * Soma N dias �teis a uma determinada data de acordo com o Munic�pio da filial Destino
	 * @param data Data base 
	 * @param numeroDias N�mero de dias �teis a ser adicionados
	 * @param idMunicipio Identificador da filial destino
	 * @return Data N dias ap�s data base
	 */
	public YearMonthDay somaNDiasUteis(YearMonthDay data, int numeroDias, Long idMunicipio){
		
		for (int i = 0; i < numeroDias; i++) {
			data = this.getNextDiaUtil(data,idMunicipio); 
		}
		
		return data;
		
	}
	
	/**
	 * @author Edenilson Fornazari
	 * 
	 * Subtrai N dias �teis a uma determinada data de acordo com o Munic�pio passado por par�metro
	 * @param data Data base 
	 * @param numeroDias N�mero de dias �teis a serem subtra�dos
	 * @param idMunicipio Munic�pio base para validar feriados
	 * @return Data N dias �teis antes data base
	 */
	public YearMonthDay subtraiNDiasUteis(YearMonthDay data, int numeroDias, Long idMunicipio){
		
		int qtDiasUteis = 0;
		YearMonthDay novaData = data;

		while (qtDiasUteis < numeroDias) {
			novaData = novaData.minusDays(1); 
			if (this.isDiaUtil(novaData, idMunicipio)) { 
				qtDiasUteis++;
			}
		}
		
		return novaData;
		
	}

	/**
	 * @author Jos� Rodrigo Moraes
	 * 
	 * Soma N dias �teis a uma determinada data de acordo com o Munic�pio da filial Destino
	 * @param data Data base 
	 * @param numeroDias N�mero de dias �teis a ser adicionados
	 * @param idMunicipio Identificador da filial destino
	 * @return Data N dias ap�s data base
	 */
	public DateTime somaNDiasUteis(DateTime data, int numeroDias, Long idMunicipio){
		
		for (int i = 0; i < numeroDias; i++) {
			data = this.getNextDiaUtil(data,idMunicipio); 
		}
		
		return data;
		
	}
	
	/**
	 * Obt�m o n�mero de dias �teis entre duas datas, descartando s�bados, domingos e feriados para o munic�pio em quest�o.
	 * Caso munic�pio seja nulo, descarta apenas s�bados e domingos.
	 * @param dtInicial
	 * @param dtFinal
	 * @param idMunicipio
	 * @return
	 */
	public int findQtdeDiasUteisEntreDatas(YearMonthDay dtInicial, YearMonthDay dtFinal, Long idMunicipio){
		List ids;
		if (idMunicipio==null)
			ids = null;
		else {
			ids = new ArrayList();
			ids.add(idMunicipio);
		}
		return findQtdeDiasUteisEntreDatas(dtInicial, dtFinal, ids);
	}
	
	/**
	 * Obt�m o n�mero de dias �teis entre duas datas, descartando s�bados, domingos e feriados para os munic�pios em quest�o.
	 * Caso munic�pio seja nulo, descarta apenas s�bados e domingos. 
	 * @param dtInicial
	 * @param dtFinal
	 * @param idsMunicipio
	 * @return
	 */
	public int findQtdeDiasUteisEntreDatas(YearMonthDay dtInicial, YearMonthDay dtFinal, List<Long>idsMunicipio){
		BigDecimal intervalo = BigDecimal.valueOf(JTDateTimeUtils.getIntervalInDays(dtInicial,dtFinal));
		int nrDias = intervalo.intValue();
		List dates = new ArrayList();
		YearMonthDay tmp = new YearMonthDay(dtInicial);

		// descarta s�bados(6) e domingos(7) 
		for(int i=0; i<intervalo.intValue(); i++) {
			if(JTDateTimeUtils.getNroDiaSemana(tmp) == DateTimeConstants.SATURDAY || JTDateTimeUtils.getNroDiaSemana(tmp) == DateTimeConstants.SUNDAY) {
				nrDias--;
			}else{
				dates.add(tmp);					
			}
			tmp = tmp.plusDays(1);				
		}
		
		// descarta os feriados
		if (!dates.isEmpty() && idsMunicipio!=null) {
			List feriados = feriadoService.findFeriadoByMunicipio(idsMunicipio,dates);
			if (!feriados.isEmpty()) {
				nrDias = (nrDias - feriados.size());
			}
		}
		return nrDias;
	}
	

	public void setFeriadoService(FeriadoService feriadoService) {
		this.feriadoService = feriadoService;
	}
	}	
