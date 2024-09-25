package com.mercurio.lms.util;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * 
 * Classe para funções utilitárias de manipulação de objetos que representem datas
 * usando a biblioteca JodaTime.
 * 
 * @author Anibal Maffioletti de Deus
 *
 */
public class JTDateTimeUtils {

	public static final YearMonthDay MIN_YEARMONTHDAY = new YearMonthDay(1,1,1);
	public static final YearMonthDay MAX_YEARMONTHDAY = new YearMonthDay(4000,1,1);
	public static final DateTime MIN_DATETIME = yearMonthDayToDateTime(MIN_YEARMONTHDAY);
	public static final DateTime MAX_DATETIME = yearMonthDayToDateTime(MAX_YEARMONTHDAY).plusDays(1).minus(1);
	
	public static final String DATETIME_WITH_SECONDS_PATTERN = "dd/MM/yyyy HH:mm:ss";
	public static final String DATETIME_WITH_WITHOUT_SECONDS_PATTERN = "dd/MM/yyyy HH:mm";
	public static final String DATETIME_WITH_WITHOUT_TIME_PATTERN = "dd/MM/yyyy";
	public static final String DATETIME_WITH_WITHOUT_DATE_PATTERN = "HH:mm";
	
	public static final String DATE_DAY_MONTH_YEAR_FORMAT = "ddMMyyyy";
	
	/**
	 * Indica o tipo de formato 'dd/MM/aaaa' ou 'hh:mm'.
	 */
	public static final int SHORT = JTFormatUtils.SHORT;
	
	/**
	 * Função disponibilizada para facilitar a construcao de query's com yearmonthday_v
	 * Quando recebe um valor null retorna o MAX_YEARMONTHDAY, caso contrário o proprio
	 * valor recebido
	 * @param YearMonthDay
	 * @return YearMonthDay
	 */
	public static final YearMonthDay maxYmd(YearMonthDay ymd) {
		if (ymd == null) {
			return MAX_YEARMONTHDAY.toDateMidnight().toYearMonthDay();
		}
		return ymd;
	}
	
	public static final DateTime getInicioDoDia(DateTime dateTime) {
	    return getDateTime(dateTime, 0, 0, 0);
	}

    public static final DateTime getFimDoDia(DateTime dateTime) {
        return getDateTime(dateTime, 23, 59, 59);
    }

    public static final DateTime getDateTime(DateTime dateTime, int hour, int minute, int second) {
        if (dateTime != null) {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(dateTime.getMillis());
            date.set(Calendar.HOUR, hour);
            date.set(Calendar.MINUTE, minute);
            date.set(Calendar.SECOND, second);

            return new DateTime(date.getTimeInMillis());
        }

        return null;
    }

    /**
     * Retorna null caso a data informada seja igual a MAX_YEARMONTHDAY, caso contrário retorna a própria data.
     *  
     * @param yearMonthDay
     * @return
     */
    public static final YearMonthDay getNullMaxYearMonthDay(YearMonthDay yearMonthDay){
    	if(MAX_YEARMONTHDAY.equals(yearMonthDay)){
    		return null;
    	} else {
    		return yearMonthDay;
    	}
    }
    
	/**
	 * Função disponibilizada para facilitar a construcao de query's com yearmonthday_v
	 * Quando recebe um valor null retorna o MAX_YEARMONTHDAY, caso contrário o próprio
	 * valor recebido transformado em YearMonthDay  
	 * @param String
	 * @return YearMonthDay
	 */
	public static final YearMonthDay maxYmd(String value) {
		if (StringUtils.isNotBlank(value)) 
			return maxYmd((YearMonthDay)ReflectionUtils.toObject(value, YearMonthDay.class));
		else
			return MAX_YEARMONTHDAY;
		
	}
	
	public static List getTimeZones() {
		List result = new ArrayList(32);
		for(Iterator i = DateTimeZone.getAvailableIDs().iterator(); i.hasNext();) {
			HashMap row = new HashMap(1);
			String next = (String) i.next();
			if (next.indexOf("America")>-1) {
				row.put("value",next);
				result.add(row);
			}
		}
		return result; 
	}
	
	

	/**
	 * Compara se uma data é maior que a outra data.
	 * Não considera horas na comparação.
	 * 
	 * @param date Data a ser comparada.
	 * @param dateRef Data de referência.
	 * @return <code>0</code> se as datas forem iguais. Valor maior que <code>0</code> se a 
	 * 		   data informada no parametro <code>date</code> for maior que a data informada no parâmetro <code>dateRef</code>.
	 * 		   Retorna um valor negativo caso contrário. 
	 */
	public static int comparaData(DateTime date, DateTime dateRef) {
		if (date == null) {
			throw new IllegalArgumentException("date não pode ser null.");
		}
		if (dateRef == null) {
			throw new IllegalArgumentException("dateRef não pode ser null.");
		}
		return date.compareTo(dateRef);
	}
	
	public static boolean comparaDatas(DateTime dt1, DateTime dt2) {
        if (dt1 != null && dt2 != null) {
            return dt1.compareTo(dt2) > 0;
        }

        return false;
    }

	/**
	 * Compara se uma data é maior que a outra data.
	 * Não considera horas na comparação.
	 * 
	 * @param date Data a ser comparada.
	 * @param dateRef Data de referência.
	 * @return <code>0</code> se as datas forem iguais. Valor maior que <code>0</code> se a 
	 * 		   data informada no parametro <code>date</code> for maior que a data informada no parâmetro <code>dateRef</code>.
	 * 		   Retorna um valor negativo caso contrário. 
	 */
	public static int comparaData(YearMonthDay date, YearMonthDay dateRef) {
		if (date == null) {
			throw new IllegalArgumentException("date não pode ser null.");
		}
		if (dateRef == null) {
			throw new IllegalArgumentException("dateRef não pode ser null.");
		}
		return date.compareTo(dateRef);
	}	
	
    /**
     * Método especifico para o financeiro.
     * Retorna a data informada com o dia de vencimento.
     * Se o dia da data informada mais o número de dias de tolerância 
     * for menor que o número do dia do vencimento, só trocar o número 
     * de dia da data informada. Se o dia da data informada mais os 
     * dias de toleranca for maior que o número do dia de vencimento, 
     * atribuir o numero do dia do proximo mes. 
     * 
     * @param YearMonthDay data
     * @param int nrDiasToleranca
     * @param int nrDiaVencimento
     * @return YearMonthDay
     */

    public static YearMonthDay proximaDataDoNumeroDiaVencimento(YearMonthDay data, int nrDiasToleranca, int nrDiaVencimento) {
    	
    	//Se o dia mais 7 dias da data é maior que o número de dias, trocar para o póximo mes.
    	if (data.get(DateTimeFieldType.dayOfMonth())+nrDiasToleranca > nrDiaVencimento) {
    		data.plusMonths(1);
    	}
    	
    	//Trocar o dia para o dia informado
    	data.plusDays(nrDiaVencimento);
    	
    	return data;
    }
    
    /**
     * Função criada para uso excluiso no relatorio,
     * para zerar a data quando for igual a maior data.
     * @param date
     * @return 
     */
	public static Date setNullMaxDate(Date date) {
		if (date != null) {
			if (YearMonthDay.fromDateFields(date).compareTo(MAX_YEARMONTHDAY) == 0)
				return null;
			else
				return date;
		}
		return null;
	}
    
    /**
     * Retorna data atual segundo o timezone do usuário
     * @return YearMonthDay
     */
	public static YearMonthDay getDataAtual(){
		return new YearMonthDay(SessionUtils.getFilialSessao() != null ? SessionUtils.getFilialSessao().getDateTimeZone() : null);
	}
	
	/**
	 * Retorna hora atual segundo o timezone do usuário
	 * @return TimeOfDay
	 */
	public static TimeOfDay getHorarioAtual() {
		return new TimeOfDay(SessionUtils.getFilialSessao() != null ? SessionUtils.getFilialSessao().getDateTimeZone() : null);
	}
	
	/**
	 * Retorna data + hora segundo o timezone do usuário
	 * @return DateTime
	 */
	public static DateTime getDataHoraAtual() {
		return new DateTime(SessionUtils.getFilialSessao() != null ? SessionUtils.getFilialSessao().getDateTimeZone() : null);
	}
	
	public static DateTime getDataHoraAtual(Filial filial) {
		return new DateTime(filial != null ? filial.getDateTimeZone() : null);
	}

	public static DateTime getDataHoraAtualPadrao() {
		Filial filial = new Filial();
		filial.setDsTimezone("America/Fortaleza");
		return new DateTime(filial.getDateTimeZone());
	}

	
    /**
     * Converte o String recebido no padrão ADSM (yyyy-MM-dd) para o formato especificado. 
     * @param frameworkFormatDate Data no formato (yyyy-MM-dd).
     * @param format Novo formato. 
     * @return String com a data convertida para o formato escolhido.
     * @author luisfco
     */
    public static String convertFrameworkDateToFormat(String frameworkFormatDate, String format) {
    	Integer year  = Integer.valueOf(frameworkFormatDate.substring(0,4));
    	Integer month = Integer.valueOf(frameworkFormatDate.substring(5,7));
    	Integer day   = Integer.valueOf(frameworkFormatDate.substring(8,10));
    	YearMonthDay ymd = new YearMonthDay(year.intValue(), month.intValue(), day.intValue());
    	return JTFormatUtils.format(yearMonthDayToDateTime(ymd), format);
    }
    
    /**
     * Converte o String recebido no padrão DDMMYYYY para um YearMonthDay 
     * @param data Data no formato DDMMYYYY
     * @param format Novo formato. 
     * @return YearMonthDay
     * @author JoseMR
     */
    public static YearMonthDay convertDataStringToYearMonthDay(String data) {
    	return DateTimeFormat.forPattern("ddMMyyyy").parseDateTime(data).toYearMonthDay();
    }

    public static TimeOfDay convertDataStringToTimeOfDay(String data) {
        return convertDataStringToDateTime(data).toTimeOfDay();
    }

    public static DateTime convertDataStringToDateTime(String data) {
        return DateTimeFormat.forPattern("HH:mm:ss").parseDateTime(data);
    }
    
    public static YearMonthDay convertDateToYearMonthDay(Date data){
		return new YearMonthDay((data).getTime());
	}

	/**
     * Converte o String no pattern recebido para um YearMonthDay
     * @param data
     * @param pattern
     * @return YearMonthDay
     * @author DouglasSM
     */
    public static YearMonthDay convertDataStringToYearMonthDay(String data, String pattern) {
    	return DateTimeFormat.forPattern(pattern).parseDateTime(data).toYearMonthDay();
    }

	/**
	 * Retorna o número representando o dia da semana referente à data passada por parâmetro.
	 * Exemplo: a data 02/11/2005 retornará 3, pois cai numa quarta-feira.
	 * Valores de retorno: 1 (segunda-feira), 2 (terça-feira), 3 (quarta-feira),
	 * 4 (quinta-feira), 5 (sexta-feira), 6 (sábado), 7 (domingo).
	 * DICA: ao comparar o retorno, utilizar DateTimeConstants. Exemplo: DateTimeConstants.SATURDAY para ver se é sexta-feira.
	 * @return int -> Número que representa o dia da semana de acordo com a data informada.
	 */
	public static int getNroDiaSemana(YearMonthDay date){
		//Esta sendo transformado para o meio-dia para evitar datas nao existentes quando estamos sob horario de verao.
		return date.toDateTime(new TimeOfDay(12, 0)).get(DateTimeFieldType.dayOfWeek());
	}

    /**
     * Retorna um objeto DateTime com a ultima hora do dia 
     * @param date
     * @author Rodrigo Antunes
     */
    public static DateTime setLastHourOfDayDateTime(YearMonthDay ymd) {
        return yearMonthDayToDateTime(ymd).plusDays(1).minusMillis(1);
    }
    
    /**
     * Retorna a data formatada por extenso no formato de acordo com o idioma do
     * usuário logado. Exemplo para formato brasileiro: 11 de janeiro de 2006
     * @deprecated Utilizar método formatDiaMesAnoPorExtenso da classe JTFormatUtils.
     * @param data
     * @return
     */
    @Deprecated
	public static String getDataFormatadaPorExtenso(DateTime data) {
    	return JTFormatUtils.formatExtenso(data);
    }

    /**
     * Retorna a data atual formatada por extenso.
     * 
     * @see DateTimeUtils#getDataFormatadaPorExtenso(Date)
     * @return
     */
    public static String getDataFormatadaPorExtenso() {
    	return JTFormatUtils.formatExtenso(new DateTime(SessionUtils.getFilialSessao() != null ? SessionUtils.getFilialSessao().getDateTimeZone() : null));    	
    }
    
    /**
     * Converte uma data sem horário (YearMonthDay) em um DateTime completo com
     * o timezone do usuário da sessão
     * @param YearMonthDay valor original
     * @return DateTime valor convertido para a meia noite do dia informado
     */
	public static DateTime yearMonthDayToDateTime(YearMonthDay baseDate) {
		DateTimeZone dtz = DateTimeZone.forID("America/Sao_Paulo");//DEFAULT_TMZ
		try {
			if (!SessionContext.isHttpSessionValid() && SessionUtils.getFilialSessao() != null) {
				dtz = SessionUtils.getFilialSessao().getDateTimeZone();
			}
		} catch (com.mercurio.adsm.core.InfrastructureException e) {
		}
		DateTime dt = null;
		try {
			dt = baseDate.toDateTimeAtMidnight(dtz);
		} catch (IllegalArgumentException ex) {
			dt = baseDate.plusDays(1).toDateTimeAtMidnight(dtz);
			long adjustedMillis = dtz.previousTransition(dt.getMillis()) + 1;
			dt = new DateTime(adjustedMillis, dtz);
		}
		return dt;
	}
    
    /**
     * Converte uma data sem horário (YearMonthDay) em um DateTime completo com
     * o timezone do usuário da sessão (nullsafe)
     * @param YearMonthDay valor original
     * @return DateTime valor convertido para a meia noite do dia informado
     */
    public static DateTime yearMonthDayToDateTimeNullSafe(YearMonthDay baseDate) {
    	if (baseDate == null) {
    		return null;
    	}
    	return yearMonthDayToDateTime(baseDate);
    }
    
    /**
     * Converte uma data sem horário (YearMonthDay) em um DateTime completo com
     * o timezone passado por parâmetro.
     * @param baseDate YearMonthDay valor original
     * @param dtz DateTimeZone
     * @return DateTime valor convertido para a meia noite do dia informado
     */
    public static DateTime yearMonthDayToDateTime(YearMonthDay baseDate, DateTimeZone dtz) {
    	DateTime dt = null;
    	try {
    		dt = baseDate.toDateTimeAtMidnight(dtz);
    	} catch (IllegalArgumentException ex) {
    		dt = baseDate.plusDays(1).toDateTimeAtMidnight(dtz);
    		long adjustedMillis = dtz.previousTransition(dt.getMillis()) + 1;
    		dt = new DateTime(adjustedMillis, dtz); 
    	}
    	return dt;
    }
    
    /**
     * Retorna um YearMonthDay com o valor de dia alterado para o valor informado
     * @param YearMonthDay Valor original
     * @param int Valor desejado de dia
     * @return YearMonthDay
     */
    public static YearMonthDay setDay(YearMonthDay ymd, int day) {
    	return ymd.withField(DateTimeFieldType.dayOfMonth(), day);
    }
    
    /**
     * Retorna um YearMonthDay com o valor de mês alterado para o valor informado
     * @param YearMonthDay Valor original
     * @param int Valor desejado de mês
     * @return YearMonthDay
     */
    public static YearMonthDay setMonth(YearMonthDay ymd, int month) {
    	return ymd.withField(DateTimeFieldType.monthOfYear(), month);
    }

    /**
     * Retorna um YearMonthDay com o valor de ano alterado para o valor informado
     * @param YearMonthDay Valor original
     * @param int Valor desejado de ano
     * @return YearMonthDay
     */
    public static YearMonthDay setYear(YearMonthDay ymd, int year) {
    	return ymd.withField(DateTimeFieldType.year(), year);
    }
    
    /**
     * Retorna um TimeOfDay com o valor de horas alterado para o valor informado
     * @param TimeOfDay Valor original
     * @param int Valor desejado de horas
     * @return TimeOfDay
     */
    public static TimeOfDay setHour(TimeOfDay tod, int hour) {
    	return tod.withField(DateTimeFieldType.hourOfDay(), hour);
    }
    
    /**
     * Retorna um TimeOfDay com o valor de minutos alterado para o valor informado
     * @param TimeOfDay Valor original
     * @param int Valor desejado de minutos
     * @return TimeOfDay
     */
    public static TimeOfDay setMinute(TimeOfDay tod, int minute) {
    	return tod.withField(DateTimeFieldType.minuteOfHour(), minute);
    }
    
    /**
     * Retorna um TimeOfDay com o valor de segundos alterado para o valor informado
     * @param TimeOfDay Valor original
     * @param int Valor desejado de segundos
     * @return TimeOfDay
     */
    public static TimeOfDay setSecond(TimeOfDay tod, int second) {
    	return tod.withField(DateTimeFieldType.secondOfMinute(), second);
    }
    
    /**
     * Retorna um DateTime com o valor de dia alterado para o valor informado
     * @param DateTime Valor original
     * @param int Valor desejado de dia
     * @return DateTime
     */
    public static DateTime setDay(DateTime dt, int day) {
    	return dt.withField(DateTimeFieldType.dayOfMonth(), day);
    }
    
    /**
     * Retorna um DateTime com o valor de mês alterado para o valor informado
     * @param DateTime Valor original
     * @param int Valor desejado de mês
     * @return DateTime
     */
    public static DateTime setMonth(DateTime dt, int month) {
    	return dt.withField(DateTimeFieldType.monthOfYear(), month);
    }
    
    /**
     * Retorna um DateTime com o valor de ano alterado para o valor informado
     * @param DateTime Valor original
     * @param int Valor desejado de ano
     * @return DateTime
     */
    public static DateTime setYear(DateTime dt, int year) {
    	return dt.withField(DateTimeFieldType.year(), year);
    }
    
    /**
     * Retorna um DateTime com o valor de horas alterado para o valor informado
     * @param DateTime Valor original
     * @param int Valor desejado de horas
     * @return DateTime
     */
    public static DateTime setHour(DateTime dt, int hour) {
    	return dt.withField(DateTimeFieldType.hourOfDay(), hour);
    }
    
    /**
     * Retorna um DateTime com o valor de minuto alterado para o valor informado
     * @param DateTime Valor original
     * @param int Valor desejado de minutos
     * @return DateTime
     */
    public static DateTime setMinute(DateTime dt, int minute) {
    	return dt.withField(DateTimeFieldType.minuteOfHour(), minute);
    }
    
    /**
     * Retorna o intervalo em dias entre dois YearMonthDay
     * @param YearMonthDay Primeira data do intervalo
     * @param YearMonthDay Segunda data do intervalo
     * @return int com a diferença em dias
     */
    public static int getIntervalInDays(YearMonthDay ymd1, YearMonthDay ymd2) {
    	DateTime dt1 = JTDateTimeUtils.yearMonthDayToDateTime(ymd1);
    	DateTime dt2 = JTDateTimeUtils.yearMonthDayToDateTime(ymd2);
    	Duration d = new Duration(dt1, dt2);
    	int diffOffset = (dt2.getZone().getOffset(dt2) -dt1.getZone().getOffset(dt1));
    	return (int)((d.getMillis() + diffOffset)/ (1000 * 60 * 60 * 24));
    }
    
    /**
     * Retorna o intervalo em horas entre dois DateTime
     * @param DateTime Primeira data do intervalo
     * @param DateTime Segunda data do intervalo
     * @return int com a diferença em horas
     */
    public static int getIntervalInHours(DateTime dt1, DateTime dt2) {
    	Period p = new Period(dt1, dt2);
    	return p.getHours();
    }
    
    /**
     * Retorna o intervalo em minutos entre dois DateTime
     * @param DateTime Primeira data do intervalo
     * @param DateTime Segunda data do intervalo
     * @return int com a diferença em minutos
     */
    public static int getIntervalInMinutes(DateTime dt1, DateTime dt2) {
    	Period p = new Period(dt1, dt2);
    	return p.getMinutes();
    }

    /**
     * Retorna o intervalo em milisegundos entre dois DateTime
     * @param DateTime Primeira data do intervalo
     * @param DateTime Segunda data do intervalo
     * @return int com a diferença em milisegundos
     */
    public static int getIntervalInMillis(DateTime dt1, DateTime dt2) {
    	Period p = new Period(dt1, dt2);
    	return p.getMillis();
    }
    
    /**
     * Retorna a semana do mês, a partir de um YearMonthDay usando domingo 
     * como primeiro dia da semana 
     * @param YearMonthDay
     * @return int entre 1-6
     */
    public static int getWeekOfMonth(YearMonthDay ymd) {
    	return getWeekOfMonth(ymd, DateTimeConstants.SUNDAY);
    }
    
    /**
     * Retorna a semana do mês, a partir de um YearMonthDay usando o inteiro 
     * recebido como segundo parametro como primeiro dia da semana 
     * @param YearMonthDay, int
     * @return int entre 1-6
     */
    public static int getWeekOfMonth(YearMonthDay ymd, int firstDayOfWeek) {
    	return getWeekOfMonth(JTDateTimeUtils.yearMonthDayToDateTime(ymd), firstDayOfWeek);
    }
    
    /**
     * Retorna a semana do mês, a partir de um DateTime usando domingo 
     * como primeiro dia da semana 
     * @param DateTime
     * @return int entre 1-6
     */
    public static int getWeekOfMonth(DateTime dt) {
    	return getWeekOfMonth(dt, DateTimeConstants.SUNDAY);
    }
    
    /**
     * Retorna a semana do mês, a partir de um DateTime usando o inteiro 
     * recebido como segundo parametro como primeiro dia da semana 
     * @param DateTime, int
     * @return int entre 1-6
     */
    public static int getWeekOfMonth(DateTime dt, int firstDayOfWeek) {
        int weekmonth = 0;
        if (setDay(dt, 1).getDayOfWeek() != firstDayOfWeek) weekmonth++;
        weekmonth += dt.getDayOfMonth() / 7;
    	return weekmonth;
    }
    
    /**
     * Retorna a data com o primeiro dia do mês corrente a partir de um YearMonthDay
     * @param ymd
     * @return
     */
    public static YearMonthDay getFirstDayOfYearMonthDay(YearMonthDay ymd){
    	return ymd.dayOfMonth().withMinimumValue();
    }
    
    /**
     * Retorna a data com o ultimo dia do mês corrente a partir de um YearMonthDay
     * @param ymd
     * @return
     */
    public static YearMonthDay getLastDayOfYearMonthDay(YearMonthDay ymd){
    	return ymd.dayOfMonth().withMaximumValue();
    }
    
    /**
     * Retorna a data com a primeiro hora do dia a partir de um DateTime
     * @param ymd
     * @return
     */
    public static DateTime getFirstHourOfDay(DateTime dt) {
    	if (dt!=null){
    		DateTimeZone dateTimeZone = dt.getZone();
    		return JTDateTimeUtils.yearMonthDayToDateTime(dt.toYearMonthDay(), dateTimeZone);	
    	}
    	return null;
    	
    }

    /**
     * Retorna a data com a última hora do dia a partir de um DateTime
     * @param ymd
     * @return
     */
    public static DateTime getLastHourOfDay(DateTime dt) {
    	if (dt!=null){
    		DateTimeZone dateTimeZone = dt.getZone();
    		return JTDateTimeUtils.yearMonthDayToDateTime(dt.toYearMonthDay(),dateTimeZone).plusDays(1).minus(1);	
    	}
    	return null;
    }
    /**
     * Função criada parar cópiar o objeto DateTime com a hora alterada para 23:59:59 999
     * @param dt
     * @return 
     */
    public static DateTime copyWithMaxTime(DateTime dt) {
    	return dt.withTime(23,59,59,999);
    }
    
    /**
     * Função criada para criar um DateTime apartir de um YearMonthDay com a hora setada em 23:59:59 999
     * @param ymd
     * @return 
     */
    public static DateTime createWithMaxTime(YearMonthDay ymd) {
    	return ymd.toDateTime(new TimeOfDay(23,59,59,999), SessionUtils.getFilialSessao() != null ? SessionUtils.getFilialSessao().getDateTimeZone() : null);
    }
    
    /**
     * Função criada verificar se o intervalo das datas é maior que a quantidade de meses informado
     * @param dt1, dt2, month
     * @return true or false
     */
    public static boolean validateIntervalDateInMonth(YearMonthDay dt1, YearMonthDay dt2, int month){
		return dt1.isBefore(dt2.minusMonths(month));
    }
    
    /**
     * Método para calcular a diferença em horas e minutos entre dois DateTime.
     * O calculo é b(data maior) - a (data menor)
     * @param a
     * @param b
     * @return Uma string formatada hh:mmm
     * @author Rodrigo Antunes
     */
    public static String calculaDiferencaEmHoras(DateTime a, DateTime b) {
    	String retorno = null;
		Duration d = new Duration(a.getMillis(), b.getMillis());
		if (a!=null && b!=null) {
			// divisao para gerar segundos
			long seconds = d.getMillis()/1000;
			// pois neste caso, são apenas horas positivas
			if (seconds > 0) {
				retorno = JTFormatUtils.formatTime(seconds, JTFormatUtils.HOURS, JTFormatUtils.MINUTES);	
			}
			
		}
		return retorno;
    }


    /**
     * Retorna o TimeZone do usuário logado.
     * @return DateTimeZone
     */
	public static DateTimeZone getUserDtz() {
		return SessionUtils.getFilialSessao() != null ? SessionUtils.getFilialSessao().getDateTimeZone() : null;
	}
	
	/**
	 * Retorna o nome do dia da Semana a partir de uma data
	 * @param DateTime
	 * @return String
	 */
	public static String getWeekdayName(DateTime dt) {
		return DateTimeFormat.forPattern("EE").withLocale(LocaleContextHolder.getLocale()).print(dt);
	}

	/**
	 * Retorna o nome do dia da Semana a partir de uma data
	 * @param YearMonthDay
	 * @return String
	 */
	public static String getWeekdayName(YearMonthDay ymd) {
		return getWeekdayName(JTDateTimeUtils.yearMonthDayToDateTime(ymd));
	}

	/**
	 * Retorna o nome Completo do dia da Semana a partir de uma data
	 * @param DateTime
	 * @return String
	 */
	public static String getWeekdayNameFull(DateTime dt) {
		return DateTimeFormat.forPattern("EEEE").withLocale(LocaleContextHolder.getLocale()).print(dt);
	}	

	/**
	 * Retorna o nome Completo do dia da Semana a partir de uma data
	 * @param YearMonthDay
	 * @return String
	 */
	public static String getWeekdayNameFull(YearMonthDay ymd) {
		return getWeekdayNameFull(JTDateTimeUtils.yearMonthDayToDateTime(ymd));
	}

	/**
	 * Converte número de horas em número de dias.
	 * @author Claiton Grings
	 * @param nrHours
	 * @return Retorna o número de dias correspondente ao número de horas informado.
	 */
	public static long getDaysFromHours(long nrHours) {
		return new BigDecimal(nrHours).divide(new BigDecimal("24"), BigDecimal.ROUND_CEILING).longValue();
	}

	/**
	 * Retorna o último dia do mes informado.
	 * 
	 * Exp: Ano=2000, Mes=Janeiro
	 * 31 = getLastDayOfMonth(2000, 0)
	 * 
	 * @author Mickaël Jalbert
	 * @since 03/08/2006
	 * 
	 * @param int year
	 * @param int month
	 * 
	 * @return int
	 */
	public static int getLastDayOfMonth(int year, int month) {
		DateTime dt = new DateTime(DateTimeUtils.currentTimeMillis());
		
		dt = dt.withYear(year);
		dt = dt.withMonthOfYear(month);
		dt = dt.dayOfMonth().withMaximumValue();
		return dt.getDayOfMonth();
	}	
	
	/**
	 * Converte número Year Month Day para String.
	 * @author Vinicius Bevilacqua
	 * @param YearMonthDay
	 * @return Retorna o String do Year Month Day.
	 */	
	public static String formatDateYearMonthDayToString(YearMonthDay date) {
		if(date != null) { 
			return date.toString("dd/MM/yyyy");
		}
		return null;
	}
	
	/**
	 * Converte número Year Month Day para String em formato ISO 8601
	 * @param YearMonthDay
	 * @return Retorna o String do Year Month Day.
	 */
	public static String convertToUniversalDateTime(YearMonthDay date) {
		if(date != null) { 
			return date.toString("yyyy/MM/dd");
		}
		return null;
	}
	
	/**
	 * Converte número DateTime para String
	 * @author Vinicius Bevilacqua
	 * @param DateTime
	 * @return Retorna o String do DateTime.
	 */	
	public static String formatDateTimeToString(DateTime date) {
		if(date != null) { 
			return date.toString("dd/MM/yyyy HH:mm");
		}
		return null;
	}
	
	/**
	 * Converte DateTime para String de acordo com o padrão
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDateTimeToString(DateTime date, String pattern) {
		if(date != null) { 
			return date.toString(pattern);
		}
		return null;
	}

	/**
	 * Converte uma String data/hora em DateTime de acordo com o TimeZone
	 * @param dataString
	 * @return DateTime
	 */
	public static DateTime formatStringToDateTimeWithTimeZone(String dataString){
		return formatStringToDateTimeWithTimeZone(dataString, SessionUtils.getFilialSessao());
	}

	/**
	 * 
	 * @param dataString
	 * @param filial, dataString
	 * @return DateTime
	 */
	public static DateTime formatStringToDateTimeWithTimeZone(String dataString, Filial filial){
		return DateTimeFormat.forPattern(DATETIME_WITH_SECONDS_PATTERN).withZone(filial.getDateTimeZone()).parseDateTime(dataString);
	}	
	
	public static YearMonthDay formatStringToYearMonthDayWithSeconds(String dtPrevistaEntrega) {
		DateFormat df = new SimpleDateFormat(DATETIME_WITH_SECONDS_PATTERN);
		try {
			Date data = df.parse(dtPrevistaEntrega);
			if (Integer.valueOf(dtPrevistaEntrega.split("/")[1]  ) > 12 ){
				return null;
			}
			return YearMonthDay.fromDateFields(data);
		} catch (ParseException e) {
			return null;
		}
	}
	public static YearMonthDay formatStringToYearMonthDayWithoutSeconds(String dtPrevistaEntrega) {
		DateFormat df = new SimpleDateFormat(DATETIME_WITH_WITHOUT_SECONDS_PATTERN);
		try {
			Date data = df.parse(dtPrevistaEntrega);
			if (Integer.valueOf(dtPrevistaEntrega.split("/")[1]  ) > 12 ){
				return null;
			}
			return YearMonthDay.fromDateFields(data);
		} catch (ParseException e) {
			return null;
		}
	}
	public static YearMonthDay formatStringToYearMonthDayWithoutTime(String dtPrevistaEntrega) {
		DateFormat df = new SimpleDateFormat(DATETIME_WITH_WITHOUT_TIME_PATTERN);
		try {
			Date data = df.parse(dtPrevistaEntrega);
			if (Integer.valueOf(dtPrevistaEntrega.split("/")[1]  ) > 12 ){
				return null;
			}
			return YearMonthDay.fromDateFields(data);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * @param dtPrevistaEntrega
	 * @return
	 */
	public static YearMonthDay formatStringToYearMonthDay(String dtPrevistaEntrega) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date data = df.parse(dtPrevistaEntrega);
			return YearMonthDay.fromDateFields(data);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String validateStringDate(String date) {
	    if(StringUtils.isBlank(date)){
	        return null;
	    }
        try {
            date = date.trim().replace("-", "").replace("/", "");
            return DateTimeFormat.forPattern(DATE_DAY_MONTH_YEAR_FORMAT).parseDateTime(date).toString(DATE_DAY_MONTH_YEAR_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }
	
	
	public static DateTime formatStringToDateTimeWithSeconds(String dhReceb) {
        DateTime ret = null;
        if (dhReceb != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date date = df.parse(dhReceb);
                ret = new DateTime(date);
            } catch (ParseException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return ret;

    }
	
	/**
	 * Retorna o dia anterior ou corrente referente à data e dia da semana passada por parâmetro.
	 * Exemplo: passando a data 18/09/2015 e dia da semana DateTimeConstants.SUNDAY retornará 13/09/2015 (ultimo domigo que passou).
	 * @param ymd -> Data referencia 
	 * @param dayOfWeew -> Dia da semana. Utilizar DateTimeConstants. Exemplo: DateTimeConstants.SUNDAY para retornar o domingo passado ou corrente.
	 * @return YearMonthDay -> data representando o dia anterior ou corrente, referente à data e dia da semana passada por parâmetro.
	 */
	public static YearMonthDay getLastDay(YearMonthDay ymd, int dayOfWeek) {
		int currentDayOfWeek = ymd.toDateTimeAtCurrentTime().dayOfWeek().get();		
		int minusDays = currentDayOfWeek - dayOfWeek;		
		return ymd.minusDays(minusDays < 0 ? minusDays + 7 : minusDays);
	}
	
	/**
	 * Retorna o mês/ano conforme o YearMonthDay passado como parâmetro.
	 * Exemplo: passando a data 12/11/2015 terá como retorno 11/2015.
	 * @param ymd -> data de referencia
	 * @return String representando mês/ano
	 */
	public static String getYearMonth(YearMonthDay ymd){
		int month = ymd.getMonthOfYear();
		int year =  ymd.getYear();
		return String.format("%02d", month) +"/"+ String.format("%02d", year);
	}
	
	/**
	 * Retorna o m?s/ano conforme o YearMonthDay passado como par?metro.
	 * Exemplo: passando a data 12/11/2015 ter? como retorno 1511.
	 * @param ymd -> data de referencia
	 * @return String representando ano+mes
	 */
	public static String getFormattedYearMonth(YearMonthDay ymd){
		DateTimeFormatter fmtAnoMes = DateTimeFormat.forPattern("yyMM");
		return fmtAnoMes.print(ymd.toDateTimeAtMidnight());
	}
}
