package com.mercurio.lms.prestcontasciaaerea.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;

import com.mercurio.lms.prestcontasciaaerea.model.FaturamentoCiaAerea;
import com.mercurio.lms.prestcontasciaaerea.model.dao.FaturamentoCiaAereaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
/**
* @spring.bean id="lms.prestcontasciaaerea.faturamentoCiaAereaService"
*/
public class FaturamentoCiaAereaService extends CrudService<FaturamentoCiaAerea, Long>{
	
	@Override
	public FaturamentoCiaAerea findById(Long id) {
		return (FaturamentoCiaAerea) super.findById(id);
	}

	@Override
	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}
	
	public Serializable store(FaturamentoCiaAerea bean) {
		// Verifica se nao existe outro faturamento vigente para os compos informados
		if (getFaturamentoCiaAereaDAO().hasFaturamentoInPeriod(bean.getCiaFilialMercurio().getIdCiaFilialMercurio(), bean.getDtVigenciaInicial(), bean.getDtVigenciaFinal(), bean.getIdFaturamentoCiaAerea())){
			throw new BusinessException("LMS-00047");
		}
		
		// regras para o campo periocidade
		if(bean.getTpPeriodicidade().getValue().equals("D"))
		{
			bean.setDdFaturamento((byte)0);
		}
		else if(bean.getTpPeriodicidade().getValue().equals("E"))
		{
			if(bean.getDdFaturamento() < 1 || bean.getDdFaturamento() > 10)
			{
				throw new BusinessException("LMS-27011");
			}			
		}
		else if(bean.getTpPeriodicidade().getValue().equals("Q"))
		{
			if(bean.getDdFaturamento() < 1 || bean.getDdFaturamento() > 15)
			{
				throw new BusinessException("LMS-27012");
			}			
		}
		else if(bean.getTpPeriodicidade().getValue().equals("M"))
		{
			if(bean.getDdFaturamento() < 1 || bean.getDdFaturamento() > 31)
			{
				throw new BusinessException("LMS-27013");
			}			
		}
		return super.store(bean);
	}
	
	/**
	 * Metodo encontra a proxima data de faturamento apartor da data informada como parametro
	 * 
	 * @param idCiaAerea  Cia aérea a ser prestada as contas
	 * @param idFilial  Filial prestadora de contas
	 * @param dataReferencia data de partida para encontrar o procimo faturamento
	 * @param dataVigencia  data de vigencia
	 * @param tpEmissao ??
	 * @return data proximo faturamento
	 * @author Daniel Tavares 
	 */
	public YearMonthDay findDtFaturamento(Long idEmpresaCiaAerea, Long idFilial, YearMonthDay dataReferencia, String tpEmissao,YearMonthDay dataVigencia){
		FaturamentoCiaAerea bean = ((FaturamentoCiaAereaDAO)getDao()).findFaturamentoCiaAereaVigente(idEmpresaCiaAerea, idFilial, dataVigencia);
		
		if(bean == null){
			throw new BusinessException("LMS-37005");
		}

		if(bean.getTpPeriodicidade().getValue().equals("D")) {
			return dataReferencia;
		} else if(bean.getTpPeriodicidade().getValue().equals("S")) {
			YearMonthDay ymd = dataReferencia;
			
			while(JTDateTimeUtils.getNroDiaSemana(ymd) != bean.getDdFaturamento().intValue() ){
				ymd = ymd.plusDays(1);
			}
			
			return ymd;

		} else {
			YearMonthDay ymd = dataReferencia;
			
			while(ymd.getDayOfMonth() != bean.getDdFaturamento().intValue() ){
				ymd = ymd.plusDays(1);
			}
			
			return ymd;
		}
	}
	
	/**
	 * Calcula data final para fatura
	 * 
	 * @param idCiaAerea  Cia aérea a ser prestada as contas
	 * @param idFilial  Filial prestadora de contas
	 * @param dataVigencia  data de vigencia
	 * @param tpEmissao ??
	 * @return data final periodo fatura.
	 * @author Daniel Tavares
	 */
	public YearMonthDay findDtFinalPeriodoFatura(Long idEmpresaCiaAerea, Long idFilial, YearMonthDay dtinicio, String tpEmissao){
		FaturamentoCiaAerea bean = ((FaturamentoCiaAereaDAO)getDao()).findFaturamentoCiaAereaVigente(idEmpresaCiaAerea, idFilial);
		
		if(bean == null){
			throw new BusinessException("LMS-37005");
		}
		
		YearMonthDay dtReturn = dtinicio;
		
		if(bean.getTpPeriodicidade().getValue().equals("D")) {
			dtReturn = dtReturn.plusDays(1);
		} else if(bean.getTpPeriodicidade().getValue().equals("S")) {
			dtReturn = dtReturn.plusDays(7);
		} else if(bean.getTpPeriodicidade().getValue().equals("E")) {
			dtReturn = dtReturn.plusDays(10);
		} else if(bean.getTpPeriodicidade().getValue().equals("Q")) {
			dtReturn = dtReturn.plusDays(15);
		} else if(bean.getTpPeriodicidade().getValue().equals("M")) {
			dtReturn = dtReturn.plusDays(30);
		}
		
		return dtReturn;
	}
	
	public BigDecimal findPcComissaoCiaAerea(Long idEmpresaCiaAerea, Long idFilial){
		FaturamentoCiaAerea bean = ((FaturamentoCiaAereaDAO)getDao()).findFaturamentoCiaAereaVigente(idEmpresaCiaAerea, idFilial);
		
		if(bean == null){
			throw new BusinessException("LMS-37005");
		}
		return bean.getPcComissao();
	}
	
	public void setFaturamentoCiaAereaDAO(FaturamentoCiaAereaDAO faturamentoCiaAereaDAO) {
		setDao(faturamentoCiaAereaDAO);
	}

	public FaturamentoCiaAereaDAO getFaturamentoCiaAereaDAO() {
		return (FaturamentoCiaAereaDAO) getDao();
	}

	public FaturamentoCiaAerea findFaturamentoCiaAereaVigente(Long idEmpresaCiaAerea, Long idFilial) {		
		return getFaturamentoCiaAereaDAO().findFaturamentoCiaAereaVigente(idEmpresaCiaAerea, idFilial);
	}

	public int getPrazoPagamento(Long idEmpresaCiaAerea, Long idfilial,
			YearMonthDay vigencia) {
		FaturamentoCiaAerea bean = getFaturamentoCiaAereaDAO().findFaturamentoCiaAereaVigente(idEmpresaCiaAerea, idfilial, vigencia);
		if(bean == null){
			throw new BusinessException("LMS-37005");
		}
		return bean.getNrPrazoPagamento();
	}
	
}