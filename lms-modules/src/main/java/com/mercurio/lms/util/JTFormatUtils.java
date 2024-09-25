package com.mercurio.lms.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.i18n.LocaleContextHolder;

public class JTFormatUtils {
	
	public static final int FULLTZ  = 32;
	public static final int SHORTTZ = 16;
	public static final int OFFSET  = 8;
	public static final int FULL    = 4;
	public static final int LONG    = 3;
	public static final int MEDIUM  = 2;
	public static final int SHORT   = 1;
    public static final int CUSTOM	= 0;
    public static final int DEFAULT = CUSTOM;
	
    public static final int SECONDS	= 0;
    public static final int MINUTES = 1;
    public static final int HOURS	= 2;
    public static final int DAYS	= 3;
    
    public static final int DATETIME 	 = 64;
    public static final int YEARMONTHDAY = 128;
    public static final int TIMEOFDAY 	 = 256;
    public static final int MONTHYEAR	 = 512;
    public static final int DAYMONTH	 = 1024;

    private static final long MINUTE_SECONDS = 60;
    private static final long HOUR_SECONDS	 = MINUTE_SECONDS * 60;
    private static final long DAY_SECONDS 	 = HOUR_SECONDS * 24; 

    /**
	 * Formata um objeto DateTime de acordo com uma mascara passada de forma explicita.
	 * Só deve ser usado quando as mascaras implicitas não atenderem e comunicando Arquitetura/Analistas
	 * @param dt Objeto DateTime do JodaTime a ser formatado
	 * @param format formato desejado
	 * @return String formatado conforme valor do DateTime e mascara recebida.
	 */
	public static String format(DateTime dt, String format) {
    	return DateTimeFormat.forPattern(format).print(dt);
    }
	
	/**
	 * Formata um valor numérico (em segundos) para um formato de data
	 * @param seconds: valor a ser formatado em segundos
	 * @param precision: a partir de que grandeza deve ser exibido
	 * @return
	 */
	public static String formatTime(long seconds, int precision) {
		return formatTime(seconds, precision, SECONDS);
	}
	/**
	 * Formata um valor numérico (em segundos) para um formato de data
	 * @param seconds: valor a ser formatado em segundos
	 * @param precision: a partir de que grandeza deve ser exibido
	 * @param downto: até que grandeza deve ser exibido
	 * @return
	 */
	public static String formatTime(long seconds, int precision, int downto) {
		//TODO: validar formato com usuários / analistas
		StringBuffer output = new StringBuffer();
		switch (precision) {
		case DAYS:
			long days = (seconds / DAY_SECONDS);
			output.append(days).append("d ");
			if (downto == DAYS) break;
			seconds -= days * DAY_SECONDS;
		case HOURS:
			long hours = (seconds / HOUR_SECONDS);
			output.append(hours).append("h ");
			if (downto == HOURS) break;
			seconds -= hours * HOUR_SECONDS;
		case MINUTES:
			long minutes = (seconds / MINUTE_SECONDS);
			output.append(minutes).append("min ");
			if (downto == MINUTES) break;
			seconds -= minutes * MINUTE_SECONDS;
		case SECONDS:
			output.append(seconds).append("seg");
		}
		return output.toString();
	}
	
	/**
	 * Formata um valor numérico (em segundos) para um formato de HH:mm
	 * @param seconds: valor a ser formatado em segundos
	 * @return
	 */
	public static String formatTimeSecondsToHoursMinutes(long seconds) {
		StringBuffer output = new StringBuffer();
		long hours = (seconds / HOUR_SECONDS);
		if(hours < 10)
			output.append("0");
		output.append(hours).append(":");
		seconds -= hours * HOUR_SECONDS;
		long minutes = (seconds / MINUTE_SECONDS);
		if(minutes < 10)
			output.append("0");
		output.append(minutes);
		return output.toString();
	}
	
    public static final String format(Date date, int style, int kind) {
    	DateTime dt = new DateTime(date.getTime());
    	return format(dt, style, kind);    	
    }
	
	/**
	 * Função disponibilizada para formatar objetos do tipo YearMonthDay / TimeOfDay
	 * quando obtidos por reports. Não deve ser usada pelo LMS!!!
	 * @param date Objeto do tipo java.sql.Date
     * @param style tipo de formata a ser usado, definido implicitamente por SHORT | MEDIUM | LONG | FULL | CUSTOM
     * @return String com o objeto Date formatado de acordo com o style informado.
	 */
    public static final String format(Date date, int style) {
    	DateTime dt = new DateTime(date.getTime());
    	return format(dt, style, YEARMONTHDAY);    	
    }
	
	/**
	 * Função disponibilizada para formatar objetos do tipo YearMonthDay / TimeOfDay
	 * quando obtidos por reports. Não deve ser usada pelo LMS!!!
	 * @param date Objeto do tipo java.sql.Date
     * @param style tipo de formata a ser usado, definido implicitamente por SHORT | MEDIUM | LONG | FULL | CUSTOM
     * @return String com o objeto Date formatado de acordo com o padrão do sistema.
	 */
    public static final String format(Date date) {
    	DateTime dt = new DateTime(date.getTime());
    	return format(dt, DEFAULT, YEARMONTHDAY);    	
    }

    /**
	 * Função disponibilizada para formatar objetos do tipo YearMonthDay / TimeOfDay
	 * quando obtidos por reports. Não deve ser usada pelo LMS!!!
	 * @param date Objeto do tipo java.sql.Timestamp
     * @param style tipo de formata a ser usado, definido implicitamente por SHORT | MEDIUM | LONG | FULL | CUSTOM
     * @return String com o objeto Time formatado de acordo com o style.
	 */
    public static final String format(Timestamp date, int style) {
    	DateTime dt = new DateTime(date.getTime());
    	return format(dt, style, TIMEOFDAY);    	
    }

	/**
	 * Função disponibilizada para formatar objetos do tipo YearMonthDay / TimeOfDay
	 * quando obtidos por reports. Não deve ser usada pelo LMS!!!
	 * @param date Objeto do tipo java.sql.Timestamp
     * @return String com o objeto Time formatado de acordo com o padrão do sistema.
	 */
    public static final String format(Timestamp date) {
    	DateTime dt = new DateTime(date.getTime());
    	return format(dt, DEFAULT, TIMEOFDAY);    	
    }

    /**
	 * Função para formatar um DataTime recebido como String
	 * @see #format(DateTime, int, int)
	 */
	public static String format(String dt, int style, int kind) {
		return format(buildDateTimeFromTimestampTzString(dt), style, kind);
	}
    /**
     * Formata um objeto DateTime de acordo com um estilo [SHORT | MEDIUM | LONG | FULL | CUSTOM]
     * e um tipo [TIMEOFDAY | YEARMONTHDAY | DATETIME]. Esta função deve ser usada sempre que se
     * desejar formatar um objeto DateTime usando outro tipo de formatação (apenas hora / apenas
     * data). Esta função é chamada por todas as format que recebem inteiros como parâmetro para
     * realizar a a formatação. 
     * @param dt valor a ser formatado
     * @param style tipo de formata a ser usado, definido implicitamente por SHORT | MEDIUM | LONG | FULL | CUSTOM
     * @param kind  tipo de dado que deve ser considerado TIMEOFDAY | YEARMONTHDAY | DATETIME
     * @return String com o objeto DateTime formatado de acordo com o style e kind informados..
     */
    public static String format(DateTime dt, int style, int kind) {
    	if (dt.toYearMonthDay().equals(JTDateTimeUtils.MAX_YEARMONTHDAY))
    		return ""; //Workaround para não exibir vigencia em 01/01/4000
    	String tz = " ";
    	String retorno = "";
		if ((style & OFFSET) == OFFSET) {
			tz += DateTimeFormat.forPattern("ZZ").print(dt);
		} else if ((style & FULLTZ) == FULLTZ) {
			tz += DateTimeFormat.forPattern("ZZZ").print(dt);
		} else if ((style & SHORTTZ) == SHORTTZ) {
			tz += DateTimeFormat.forPattern("zz").print(dt);
		}
		
		switch (style + kind) {
			case DATETIME + SHORT:
				retorno = DateTimeFormat.shortDateTime().withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case DATETIME + MEDIUM:
				retorno = DateTimeFormat.mediumDateTime().withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case DATETIME + LONG:
				retorno = DateTimeFormat.longDateTime().withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case DATETIME + FULL:
				retorno = DateTimeFormat.fullDateTime().withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case YEARMONTHDAY + SHORT:
				retorno = DateTimeFormat.shortDate().withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case YEARMONTHDAY + MEDIUM:
				retorno = DateTimeFormat.mediumDate().withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case YEARMONTHDAY + LONG:
				retorno = DateTimeFormat.longDate().withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case YEARMONTHDAY + FULL:
				retorno = DateTimeFormat.fullDate().withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case TIMEOFDAY + SHORT:
				retorno = DateTimeFormat.shortTime( ).withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case TIMEOFDAY + MEDIUM:
				retorno = DateTimeFormat.mediumTime().withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case TIMEOFDAY + LONG:
				retorno = DateTimeFormat.longTime().withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case TIMEOFDAY + FULL:
				retorno = DateTimeFormat.fullTime().withLocale(LocaleContextHolder.getLocale()).print(dt);
				break;
			case CUSTOM + TIMEOFDAY:
			case CUSTOM + YEARMONTHDAY: 
			case CUSTOM + DATETIME:
				retorno = DateTimeFormat.forPattern(customFormat(kind)).print(dt);
				break;
			case MONTHYEAR + SHORT:
			case MONTHYEAR + MEDIUM:
			case MONTHYEAR + FULL:
			case MONTHYEAR + CUSTOM:
				retorno = DateTimeFormat.forPattern("MM/yyyy").print(dt);
				break;
			case MONTHYEAR + LONG:
			    retorno = DateTimeFormat.forPattern("dd/MM/yy HH:mm:ss,SSSSSSSSS ZZ").print(dt);
			    break;
			case DAYMONTH + SHORT:
			case DAYMONTH + MEDIUM:
			case DAYMONTH + FULL:
			case DAYMONTH + LONG:
			case DAYMONTH + CUSTOM:
				retorno = DateTimeFormat.forPattern("dd/MM").print(dt);
				break;
		}
    	if (tz.length() > 1) retorno += tz; 
		return retorno; 
    }
    
    /**
	 * Formata um objeto YearMonthDay de acordo com uma mascara passada de forma explicita.
	 * Só deve ser usado quando as mascaras implicitas não atenderem e comunicando Arquitetura/Analistas
	 * @param yearMonthDay Objeto YearMonthDay do JodaTime a ser formatado
	 * @param format formato desejado
	 * @return String formatado conforme valor do YearMonthDay e mascara recebida.
	 */
    
	public static String format(YearMonthDay yearMonthDay, String style) {
		return format(JTDateTimeUtils.yearMonthDayToDateTime(yearMonthDay), style);
	}
	
    /**
     * Formata um objeto DateTime usando uma mascara implicita (style)
	 * @param dt Objeto DateTime do JodaTime a ser formatado
     * @param style a ser aplicado [SHORT | MEDIUM | LONG | FULL | CUSTOM]  
     * @return 
     */
    public static String format(DateTime dt, int style) {
    	return format(dt, style, DATETIME);
    }
    /**
     * Formata um objeto YearMonthDay usando uma mascara implicita (style)
     * e um formato implicito (deve usar-se esta função apenas para formatar
     * dia+mês e mês+ano).
	 * @param ymd Objeto YearMonthDay do JodaTime a ser formatado
     * @param style a ser aplicado [SHORT | MEDIUM | LONG | FULL | CUSTOM]  
     * @param kind a ser aplicado [SHORT | MEDIUM | LONG | FULL | CUSTOM]  
     */
    public static String format(YearMonthDay ymd, int style, int kind) {
    	return format(JTDateTimeUtils.yearMonthDayToDateTime(ymd), style, kind);
    }
    /**
     * Formata um objeto YearMonthDay usando uma mascara implicita (style)
	 * @param ymd Objeto YearMonthDay do JodaTime a ser formatado
     * @param style a ser aplicado [SHORT | MEDIUM | LONG | FULL | CUSTOM]  
     * @return 
     */
    public static String format(YearMonthDay ymd, int style) {
    	return format(JTDateTimeUtils.yearMonthDayToDateTime(ymd), style, YEARMONTHDAY);
    }
    
    /**
     * Formata um objeto TimeOfDay usando uma mascara implicita (style)
	 * @param tod Objeto TimeOfDay do JodaTime a ser formatado
     * @param style a ser aplicado [SHORT | MEDIUM | LONG | FULL | CUSTOM]  
     * @return 
     */
   public static String format(TimeOfDay tod, int style) {
    	return format(tod.toDateTimeToday(), style, TIMEOFDAY);
    }
   
   /**
    * Formata um objeto String (obtido através de um getTIMESTAMPTZ) 
    * usando a mascara padrão do sistema
    * @param tstz String no formato "yyyy-M-d H.m.s.SSSSSSSSS TZR" 
    * @return String com o valor formatado.
    */
   public static String format(String tstz) {
	   return format(buildDateTimeFromTimestampTzString(tstz));
   }
   
   /**
    * Formata um objeto String (obtido através de um getTIMESTAMPTZ) usando a mascara
    * passada de forma implicita.
    * @param tstz String no formato "yyyy-M-d H.m.s.SSSSSSSSS TZR" 
    * @param style a ser aplicado [SHORT | MEDIUM | LONG | FULL | CUSTOM]  
    * @return String com o valor formatado.
    */
   public static String format(String tstz, int style) {
	   return format(buildDateTimeFromTimestampTzString(tstz), style);
   }
   
   /**
    * Formata um objeto DateTime usando a mascara padrão do sistema
    * @param dt Objeto DateTime a ser formatado
    * @return String com o objeto formatado conforme máscara padrão
    */
    public static String format(DateTime dt) {
    	return format(dt, DEFAULT, DATETIME);
    }

    /**
     * Formata um objeto YearMonthDay usando a mascara padrão do sistema
     * @param ymd Objeto YearMonthDay a ser formatado
     * @return String com o objeto formatado conforme máscara padrão
     */
    public static String format(YearMonthDay ymd) {
    	return format(JTDateTimeUtils.yearMonthDayToDateTime(ymd), DEFAULT, YEARMONTHDAY);
    }
    
    /**
     * Formata um objeto TimeOfDay usando a mascara padrão do sistema
     * @param tod Objeto TimeOfDay a ser formatado
     * @return String com o objeto formatado conforme máscara padrão
     */
    public static String format(TimeOfDay tod) {
    	return format(tod.toDateTimeToday(), DEFAULT, TIMEOFDAY);
    }

    public static String formatExtenso(DateTime dt) {
    	DateTimeFormatter formato = DateTimeFormat.fullDateTime().withLocale(LocaleContextHolder.getLocale());
    	if (formato == null) {
    		throw new IllegalStateException("Locale não reconhecido para formatação de data ("+LocaleContextHolder.getLocale()+").");
    	}
    	return formato.print(dt);    	
    }

    public static String formatDiaMesAnoPorExtenso(DateTime dt) {
    	DateTimeFormatter formato = DateTimeFormat.fullDate().withLocale(LocaleContextHolder.getLocale());
    	if (formato == null) {
    		throw new IllegalStateException("Locale não reconhecido para formatação de data ("+LocaleContextHolder.getLocale()+").");
    	}
    	return formato.print(dt);    	
    }

    /**
     * Formata DateTime no formato:
     * 21 de Janeiro de 2006.
     * @param dt
     * @return
     */
    public static String formatDiaMesAno(YearMonthDay dt) {
    	DateTimeFormatter formato = DateTimeFormat.longDate().withLocale(LocaleContextHolder.getLocale());
    	if (formato == null) {
    		throw new IllegalStateException("Locale não reconhecido para formatação de data ("+LocaleContextHolder.getLocale()+").");
    	}
    	return formato.print(dt);    	
    }
    
    /**
     * Função interna usada para gerar as máscaras para os padrões definidos pelo LMS
     * @param kind  tipo de dado que deve ser considerado TIMEOFDAY | YEARMONTHDAY | DATETIME 
     * @return String com a máscara definida para o Locale recebido.
     */
    private static String customFormat(int kind) {
    	Locale locale = LocaleContextHolder.getLocale();
    	String language = locale.getLanguage();
    	String country = locale.getCountry(); 
    	String mask = "";
    	if (language.equalsIgnoreCase("pt")) {
    		if (country.equalsIgnoreCase("br")) {
    			switch (kind) {
					case DATETIME  :
						mask = "dd/MM/yyyy HH:mm ZZ";
						break;
					case YEARMONTHDAY  :
						mask = "dd/MM/yyyy";
						break;
					case TIMEOFDAY  :
						mask = "HH:mm";
						break;
					}
    		}
    	} else if (language.equalsIgnoreCase("en")) {
    		if (country.equalsIgnoreCase("us")) {
    			switch (kind) {
				case DATETIME  :
					mask = "MM/dd/yyyy h:mm a ZZ";
					break;
				case YEARMONTHDAY  :
					mask = "MM/dd/yyyy";
					break;
				case TIMEOFDAY  :
					mask = "h:mm a";
					break;
				}
    		}
    	} else if (language.equalsIgnoreCase("es")) {
    		if (country.equalsIgnoreCase("ar")) {
    			switch (kind) {
					case DATETIME  :
						mask = "dd/MM/yyyy HH:mm ZZ";
						break;
					case YEARMONTHDAY  :
						mask = "dd/MM/yyyy";
						break;
					case TIMEOFDAY  :
						mask = "HH:mm";
						break;
    	}
    		}
    	}
    	return mask;
    }
    
    public static final DateTime buildDateTimeFromTimestampTzString(String sdt) {
		DateTime dt = null;
		DateTimeZone dtz = null;
		String zone = sdt.substring(sdt.lastIndexOf(32) + 1);
		sdt = sdt.substring(0, sdt.lastIndexOf(32));
		try {
			dtz = DateTimeZone.forID(zone);
		} catch (IllegalArgumentException ile) {
			int h = Integer.parseInt(zone.substring(0, zone.indexOf(':')));
			int m = Integer.parseInt(zone.substring(zone.indexOf(':') + 1));
			dtz = DateTimeZone.forOffsetHoursMinutes(h, m);
		}
		try {
		dt = DateTimeFormat.forPattern("yyyy-M-d H.m.s.SSSSSSSSS").withZone(dtz).parseDateTime(sdt);
		} catch(IllegalArgumentException e) {
			dt = DateTimeFormat.forPattern("yyyy-M-d H:m:s.SSSSSSSSS").withZone(dtz).parseDateTime(sdt);
		}		
		return dt;
    }

    /**
     * Converte um objeto String para DateTime.
     * @param sdt
     * @return
     */
    public static final DateTime stringToDateTime(String sdt) {
    	return buildDateTimeFromTimestampTzString(sdt);
    }
    
    /**
     * Converte um objeto tipo Date para YearMonthDay. Só deve ser usado em relatórios.
     * NÃO PODE SER USADO PELOS SERVICES DO LMS.
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
	public static final YearMonthDay buildYmd(Date date) {
    	//Esta forma de obter o YearMonthDay é necessária devido a problemas de conversão
    	//quando a data refere-se a entrada em horário de verão.
    	return new YearMonthDay(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
    }
    
    /**
     * Converte um objeto tipo Time para TimeOfDay. Só deve ser usado em relatórios.
     * NÃO PODE SER USADO PELOS SERVICES DO LMS.
     * @param time
     * @return
     */
    public static final TimeOfDay buildTod(Time time) {
    	return new TimeOfDay(time.getTime());
    }
    
    public static void main(String[] args) {
    	YearMonthDay yearMonthDay = new YearMonthDay();
    	System.out.println(JTFormatUtils.format(yearMonthDay, 128));
    }
	
}
