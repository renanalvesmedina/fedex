package com.mercurio.lms.franqueados.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.util.CsvUtils;
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.ReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.SimulacaoDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.SimulacaoReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.municipios.model.Filial;

public class FranqueadoUtils {
	
	private static final Logger LOGGER = LogManager.getLogger(FranqueadoUtils.class);
	
	public static final BigDecimal MULTIPLICADOR_PERCENTUAL = new BigDecimal(100);
	public static final int ROUND_TYPE = BigDecimal.ROUND_HALF_UP;
	public static final RoundingMode ROUND_MODE = RoundingMode.HALF_UP;
	public static final int ROUND_TYPE_TRUNC = BigDecimal.ROUND_DOWN;
	public static final int SCALE = 2;
	public static final int SCALE_TRUNC = 0;  
	public static final int MAXIMUM_FRACTION_DIGITS = 10;

	private FranqueadoUtils() {
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * Verifica se o range de novas datas não está entre o range de datas antigas
	 * 
	 * @param novoInicio
	 * @param novoFim
	 * @param oldInicio
	 * @param oldFim
	 * @return
	 */
	public static boolean isVigenciaEntreVigenciasAntigas(YearMonthDay novoInicio, YearMonthDay novoFim, YearMonthDay oldInicio, YearMonthDay oldFim){
		if(novoInicio.isBefore(oldInicio) && novoFim.isBefore(oldInicio) ){
			return false;
		} else if(novoInicio.isAfter(oldFim) && novoFim.isAfter(oldFim) ){			
			return false;
		}
		return true;
	}
	
	public static BigDecimal calcularPercentual(BigDecimal vlTotal, BigDecimal percentual) {
		return vlTotal.multiply(percentual.divide(MULTIPLICADOR_PERCENTUAL)).setScale(SCALE, ROUND_TYPE);
	}
	
	public static BigDecimal calcularPercentualTruncado(BigDecimal vlTotal, BigDecimal percentual) {
		return vlTotal.multiply(percentual.divide(MULTIPLICADOR_PERCENTUAL)).setScale(SCALE, ROUND_TYPE_TRUNC);
	}
	
	public static YearMonthDay buscarPrimeiroDiaAno(YearMonthDay data){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, data.getYear());
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return new YearMonthDay(calendar);
	}

	public static YearMonthDay buscarPrimeiroDiaMesAtual(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return new YearMonthDay(calendar);
	}
	
	
	public static String getDateAsMonthYear(YearMonthDay data) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, data.getYear());
		calendar.set(Calendar.MONTH, data.getMonthOfYear()-1);
		
		return new SimpleDateFormat("MM/yyyy").format(calendar.getTime());
	}
    
	
	/**
	 * Busca último dia do mês a partir do parâmetro passado
	 * @param yearMonthDay
	 * @return
	 */
	public static YearMonthDay buscarUltimoDiaMes(YearMonthDay yearMonthDay){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, yearMonthDay.getYear());
		calendar.set(Calendar.MONTH, yearMonthDay.getMonthOfYear());
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		return new YearMonthDay(calendar);
	}
	
	public static YearMonthDay buscarPrimeiroDiaMes(YearMonthDay yearMonthDay){
		//FIXME franqueados. O método atual realiza a busca do primeiro dia do mês anterior ao parâmetro passado!!!	Corrigir nome ou implementação.
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, yearMonthDay.getYear());
		calendar.set(Calendar.MONTH, yearMonthDay.getMonthOfYear()-1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return new YearMonthDay(calendar);
	}

	public static YearMonthDay buscarPrimeiroDiaMesAnterior(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return new YearMonthDay(calendar);
	}
	
	public static YearMonthDay buscarUltimoDiaMesAnterior(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new YearMonthDay(calendar);
	}
	
	public static YearMonthDay buscarDiaAnterior(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return new YearMonthDay(calendar);
	}
	
	public static BigDecimal calcularTonelada(BigDecimal pcTonelada, BigDecimal pesoAferido){
		return pcTonelada.multiply(pesoAferido.divide(new BigDecimal(1000))).setScale(SCALE, ROUND_TYPE);
	}

	public static SimulacaoDoctoServicoFranqueado doctoServicoFranqueadoToSimulacaoDoctoServicoFranqueado(DoctoServicoFranqueado doctoServicoFranqueado) {
		SimulacaoDoctoServicoFranqueado simulacaoDoctoServicoFranqueado = new SimulacaoDoctoServicoFranqueado();
		Field [] attributes =  DoctoServicoFranqueado.class.getDeclaredFields();
        for (Field field : attributes) {
            try {
           		PropertyUtils.setSimpleProperty(simulacaoDoctoServicoFranqueado, field.getName(), PropertyUtils.getSimpleProperty(doctoServicoFranqueado, field.getName()));
            } catch (Exception e) {
            	LOGGER.error(e.getMessage());
            }
        }
        
        Filial filial = new Filial();
        filial.setIdFilial(doctoServicoFranqueado.getFranquia().getIdFranquia());
        simulacaoDoctoServicoFranqueado.setFilial(filial);
		return simulacaoDoctoServicoFranqueado;
	}

	public static List<SimulacaoDoctoServicoFranqueado> createListSimulacaoDoctoServicoFranqueadoByListDoctoServicoFranqueado(
			List<DoctoServicoFranqueado> doctoServicoFranqueadoList) {
		
		List<SimulacaoDoctoServicoFranqueado> simulacaoDoctoServicoFranqueados = new ArrayList<SimulacaoDoctoServicoFranqueado>();
		for (DoctoServicoFranqueado doctoServicoFranqueado : doctoServicoFranqueadoList) {
			simulacaoDoctoServicoFranqueados.add(doctoServicoFranqueadoToSimulacaoDoctoServicoFranqueado(doctoServicoFranqueado));
		}
		
		return simulacaoDoctoServicoFranqueados;
	}

	public static List<SimulacaoReembarqueDoctoServicoFranqueado> createListSimulacaoReembarqueDoctoServicoFranqueadoByListReembarqueDoctoServicoFranqueado(
			List<ReembarqueDoctoServicoFranqueado> reembarqueDoctoServicoFranqueadoList) {
		
		List<SimulacaoReembarqueDoctoServicoFranqueado> simulacaoReembarqueDoctoServicoFranqueados = new ArrayList<SimulacaoReembarqueDoctoServicoFranqueado>();
		for (ReembarqueDoctoServicoFranqueado reembarqueDoctoServicoFranqueado : reembarqueDoctoServicoFranqueadoList) {
			simulacaoReembarqueDoctoServicoFranqueados.add(reembarqueDoctoServicoFranqueadoToSimulacaoReembarqueDoctoServicoFranqueado(reembarqueDoctoServicoFranqueado));
		}
		
		return simulacaoReembarqueDoctoServicoFranqueados;
	}

	private static SimulacaoReembarqueDoctoServicoFranqueado reembarqueDoctoServicoFranqueadoToSimulacaoReembarqueDoctoServicoFranqueado(
			ReembarqueDoctoServicoFranqueado reembarqueDoctoServicoFranqueado) {
		
		SimulacaoReembarqueDoctoServicoFranqueado simulacaoReembarqueDoctoServicoFranqueado = new SimulacaoReembarqueDoctoServicoFranqueado();
		
		Field [] attributes =  ReembarqueDoctoServicoFranqueado.class.getDeclaredFields();
        for (Field field : attributes) {
            try {
            	PropertyUtils.setSimpleProperty(simulacaoReembarqueDoctoServicoFranqueado, field.getName(), 
        				PropertyUtils.getSimpleProperty(reembarqueDoctoServicoFranqueado, field.getName()));
            } catch (Exception e) {
            	LOGGER.error(e.getMessage());
            }
        }
        
        Filial filial = new Filial();
        filial.setIdFilial(reembarqueDoctoServicoFranqueado.getFranquia().getIdFranquia());
        simulacaoReembarqueDoctoServicoFranqueado.setFilial(filial);
	        
		return simulacaoReembarqueDoctoServicoFranqueado;
	}
	
	public static Map<String, Object> convertMappedListToCsv(String reportName, final List<Map<String, Object>> lista, String separador) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if (lista != null && !lista.isEmpty()) {
			formatNumber(lista);
			String csv = CsvUtils.convertMappedListToCsv(lista, separador);
			map.put(reportName, csv);
		}
		return map;
	}

	private static void formatNumber(final List<Map<String, Object>> lista) {
		for(Map<String, Object> mapAux : lista) {	
			for(Entry<String, Object> entry : mapAux.entrySet()) {
				if(entry.getValue() != null && (entry.getValue() instanceof BigDecimal || entry.getValue() instanceof Double || entry.getValue() instanceof Float)){
					NumberFormat nf = NumberFormat.getInstance(LocaleContextHolder.getLocale());
					nf.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
					entry.setValue(nf.format(entry.getValue()));
				}
			}
		}
	}
	
}
