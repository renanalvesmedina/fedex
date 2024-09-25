package com.mercurio.lms.carregamento.model.service;

import java.util.StringTokenizer;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.dao.GerarDadosEstoqueDispUnitizacaoDAO;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Service responsável pelo processo de Geração de Dados de Estoque dos 
 * Dispositivos de Unitização de acordo com o item de número 05.03.01.12
 * @spring.bean id="lms.carregamento.gerarDadosEstoqueDispUnitizacaoService"
 */

public class GerarDadosEstoqueDispUnitizacaoService {

	GerarDadosEstoqueDispUnitizacaoDAO gerarDadosEstoqueDispUnitizacaoDAO;
	
	/**
	 * Efetua parse na string de data recebida, retornando TypedFlatMap com
	 * as datas inicial e final do mês posterior ao mês referenciado na data.
	 * @param date String de data no formato MM/YYYY
	 * @return
	 */
    private TypedFlatMap parseDateForNextMonth(String date) {
    	YearMonthDay currentMonth;
    	YearMonthDay nextMonth;
    	
    	// efetuando o parse da data em mes/ano
    	StringTokenizer st = new StringTokenizer(date,"/");
    	if(st.countTokens()!=2) {
    		throw new BusinessException("LMS-05125");
    	}
    	try {
	    	int mes = Integer.valueOf((String)st.nextElement()).intValue();
	    	int ano = Integer.valueOf((String)st.nextElement()).intValue();
	    	currentMonth = new YearMonthDay(ano, mes, 1);
    	} catch (NumberFormatException e) {
    		throw new BusinessException("LMS-05125");
    	} catch (IllegalArgumentException e) {
    		throw new BusinessException("LMS-05126");
    	}

    	// determinando o próximo mes
    	nextMonth = currentMonth.plusMonths(1);
    	
    	// determinando data inicial e data final do mes próximo
    	YearMonthDay nextMonthInicial = JTDateTimeUtils.getFirstDayOfYearMonthDay(nextMonth);
    	YearMonthDay nextMonthFinal = JTDateTimeUtils.getLastDayOfYearMonthDay(nextMonth);    	
    	
    	TypedFlatMap tfm = new TypedFlatMap();
    	tfm.put("nextMonthInicial", nextMonthInicial);
    	tfm.put("nextMonthFinal",   nextMonthFinal);
    	return tfm;
    }
    
    /**
     * Método responsável pela Geração de Dados de Estoque dos Dispositivos 
     * de Unitização de acordo com o item de número 05.03.01.12 
     */
    public void generateDadosEstoqueDispositivoUnitizacao() {

    	// obtendo data atual dos dispositivos de unitizacao na tabela de parametro geral MM/YYYY
    	ParametroGeral parametroGeral = gerarDadosEstoqueDispUnitizacaoDAO.findParametroGeralByNomeParametro("DT_ESTOQUE_CORRENTE_DISP_UNIT");
    	TypedFlatMap tfm = parseDateForNextMonth(parametroGeral.getDsConteudo());

    	YearMonthDay nextMonthInicial = tfm.getYearMonthDay("nextMonthInicial");
    	YearMonthDay nextMonthFinal   = tfm.getYearMonthDay("nextMonthFinal");    	
    	
    	// copiando os dados do mês de ESTOQUE_DISPOSITIVO_QTDE para o próximo mês em ESTOQUE_DISP_QTDE_HIST    	
    	gerarDadosEstoqueDispUnitizacaoDAO.removeEstoqueDispQtdeHistBetweenDtInicialFinal(nextMonthInicial, nextMonthFinal);
    	gerarDadosEstoqueDispUnitizacaoDAO.executeCopyEstoqueDispositivoQtdeForMonth(nextMonthInicial);

    	// copiando os dados do mês de ESTOQUE_DISP_IDENTIFICADO para o próximo mês em ESTOQUE_DISP_IDENT_HIST
    	gerarDadosEstoqueDispUnitizacaoDAO.removeEstoqueDispIdentHistBetweenDtInicialFinal(nextMonthInicial, nextMonthFinal);
    	gerarDadosEstoqueDispUnitizacaoDAO.executeCopyEstoqueDispIdentificadoForMonth(nextMonthInicial);
    	
    	// atualizando no parametro geral a data do mes seguinte
    	int nextYear = nextMonthInicial.getYear();
    	int nextMonth = nextMonthInicial.getMonthOfYear();
    	String dtParametro = String.valueOf(nextMonth)+"/"+String.valueOf(nextYear);
    	gerarDadosEstoqueDispUnitizacaoDAO.updateConteudoParametroGeralByIdParametroGeral(dtParametro, parametroGeral.getIdParametroGeral());
    }

	public void setGerarDadosEstoqueDispUnitizacaoDAO(
			GerarDadosEstoqueDispUnitizacaoDAO gerarDadosEstoqueDispUnitizacaoDAO) {
		this.gerarDadosEstoqueDispUnitizacaoDAO = gerarDadosEstoqueDispUnitizacaoDAO;
	}    

	
}
