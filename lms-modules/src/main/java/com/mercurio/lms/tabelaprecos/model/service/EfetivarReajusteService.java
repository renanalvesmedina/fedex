package com.mercurio.lms.tabelaprecos.model.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tabelaprecos.model.CloneTabelaPrecoDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteParametroParcelaDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.ReajusteTabelaPrecoEntity;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.tabelaprecos.model.dao.ReajusteTabelaPrecoDAO;
import com.mercurio.lms.tabelaprecos.model.dao.ReajusteTabelaPrecoParcelaDAO;
import com.mercurio.lms.tabelaprecos.model.dao.TabelaPrecoDAO;


public class EfetivarReajusteService  extends CrudService<ReajusteTabelaPrecoEntity, Long>{

	private static final int YEAR_IN_FUTURE = 4000;

	private ReajusteTabelaPrecoParcelaDAO reajusteTabelaPrecoParcelaDAO;
    private ReajusteTabelaPrecoDAO reajusteTabelaPrecoDAO;

	private TarifaPrecoRotaService tarifaPrecoRotaService;
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;
	private TabelaPrecoService tabelaPrecoService;
	private TabelaPrecoDAO tabelaPrecoDAO;
	
	private CloneTabelaPrecoService cloneTabelaPrecoService;
	
	private CalculoSimulacaoNovosPrecosParametrizadoService calculoSimulacaoNovosPrecosParametrizadoService;
	
	
	public ReajusteTabelaPreco executeEfetivarReajuste(Map<String, Object> dadosTelaReajuste) throws Throwable{

		Long idReajuste = Long.valueOf((Integer) dadosTelaReajuste.get("id"));
		Map<String, Object> reajuste = reajusteTabelaPrecoDAO.findReajusteById(idReajuste);
		ReajusteTabelaPreco reajusteDTO = convertToReajusteTabelaPreco(reajuste);
		
		return efetivarReajuste(reajusteDTO);
    }

	private ReajusteTabelaPreco convertToReajusteTabelaPreco(
			Map<String, Object> map) {
		ReajusteTabelaPreco reajuste = new ReajusteTabelaPreco();
		reajuste.setId((Long) map.get("id"));
		reajuste.setIdTabelaBase((Long) map.get("idTabelaBase"));
		reajuste.setIdTabelaNova((Long) map.get("idTabelaNova"));
		reajuste.setTipo(map.get("tipo").toString().toUpperCase());
		reajuste.setSubTipo(map.get("subTipo").toString().toUpperCase());
		reajuste.setDtVigenciaInicialDate((Date) map.get("dtVigenciaInicialDate"));
		reajuste.setDtAgendamento((YearMonthDay) map.get("dtAgendamento"));
		reajuste.setDtVigenciaInicial((YearMonthDay) map.get("dtVigenciaInicial"));
		reajuste.setDtVigenciaFinal((YearMonthDay) map.get("dtVigenciaFinal"));
		reajuste.setPercentualReajusteGeral( new BigDecimal( (String) map.get("percentualReajusteGeral")));

		return reajuste;
	}


	private ReajusteTabelaPreco efetivarReajuste(ReajusteTabelaPreco reajuste) throws Throwable {
		Long idReajuste = reajuste.getId();
		Long idTabelaBase = reajuste.getIdTabelaBase();
		Long idTabelaNova = reajuste.getIdTabelaNova();
		
    	List<ReajusteParametroParcelaDTO> parametrosParcelas = findParametrosParcelas(idReajuste);
    	
    	reajustarTabelaPreco(idTabelaBase,idTabelaNova, parametrosParcelas, true);
    	
    	YearMonthDay dataAtual = reajusteTabelaPrecoDAO.efetivarReajuste(idReajuste);

    	getTabelaPrecoDAO().updateEfetivarTabelaPreco(idTabelaNova);

    	reajuste.setDtEfetivacao(dataAtual);
    	
    	return reajuste;
	}

	private YearMonthDay convertDateToYearMonthDay(Date data){
		return new YearMonthDay((data).getTime());
	}

	private void copyTarifaPrecoRotas(Long idTabelaBase, TabelaPreco tabelaNova) {
		List<TarifaPrecoRota> tarifaPrecoRotas = tarifaPrecoRotaService.findByIdTabelaPreco(idTabelaBase);
		for (TarifaPrecoRota tarifaPrecoRota : tarifaPrecoRotas) {
			TarifaPrecoRota copia = copyTarifaPrecoRota(tarifaPrecoRota, tabelaNova);

			tarifaPrecoRotaService.validate(copia, idTabelaBase);
			tarifaPrecoRotaService.basicStore(copia);
		}
		getTabelaPrecoDAO().getAdsmHibernateTemplate().flush();
	}

	/**
	 * reajustarTabelaPreco
	 * 
	 * @param idTabelaBase
	 * @param idTabelaNova - tabela que foi criada, na qual vai ser salvo o reajuste
	 * @param parametrosParcelas - parametros que definem quais parcelas serao reajustadas
	 * @param isReajuste - se for true reajusta, senão somente copia a tabela base para a nova
	 * @return 
	 * @throws Throwable 
	 */
	public TabelaPreco reajustarTabelaPreco(Long idTabelaBase, Long idTabelaNova, List<ReajusteParametroParcelaDTO> parametrosParcelas, Boolean isReajuste) throws Throwable {

		List<TabelaPrecoParcela> taxas=null;
		List<TabelaPrecoParcela> generalidades=null;
		
    	CloneTabelaPrecoDTO cloneTabelaPreco = new CloneTabelaPrecoDTO(idTabelaBase, idTabelaNova);
		cloneTabelaPrecoService.executeClonarGrupoRegiao(cloneTabelaPreco );
		
		TabelaPreco simulada = generateSimulacao(idTabelaBase, taxas, generalidades, parametrosParcelas, isReajuste);
		TabelaPreco tabelaPreco = tabelaPrecoService.getTabelaPrecoReajuste(idTabelaNova, simulada);
    	tabelaPrecoService.store(tabelaPreco);
    	
    	cloneTabelaPrecoService.executeClonarRotasETRT(cloneTabelaPreco);
    	
    	return tabelaPreco;
	}

	public TabelaPreco generateSimulacao(Long idTabelaBase, List<TabelaPrecoParcela> taxas, List<TabelaPrecoParcela> generalidades, List<ReajusteParametroParcelaDTO> parametrosParcelas, Boolean isReajuste) {
		TabelaPreco tabelaPreco = new TabelaPreco();
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();
		List<TabelaPrecoParcela> tabelaPrecoParcelas = null;
		List<TabelaPrecoParcela> reajustes = null;

		tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, "G");
		reajustes = calculoSimulacaoNovosPrecosParametrizadoService.executeReajusteGeneralidade(tabelaPrecoParcelas, generalidades, parametrosParcelas, isReajuste);
		if (reajustes != null) {
			result.addAll(reajustes);
		}

		tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, "T");
		reajustes = calculoSimulacaoNovosPrecosParametrizadoService.executeReajusteValorTaxa(tabelaPrecoParcelas,  taxas, parametrosParcelas, isReajuste);
		if (reajustes != null) {
			result.addAll(reajustes);
		}

		tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, "S");
		reajustes = calculoSimulacaoNovosPrecosParametrizadoService.executeReajusteServicoAdicional(tabelaPrecoParcelas , parametrosParcelas, isReajuste);
		if (reajustes != null) {
			result.addAll(reajustes);
		}

		tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, "P");
		reajustes = calculoSimulacaoNovosPrecosParametrizadoService.executeReajustePrecoFrete(tabelaPrecoParcelas, parametrosParcelas, isReajuste);
		if (reajustes != null) {
			result.addAll(reajustes);
		}

		tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, "M");
		reajustes = calculoSimulacaoNovosPrecosParametrizadoService.executeReajusteMinimoProgressivo(tabelaPrecoParcelas, parametrosParcelas, isReajuste);
		if (reajustes != null) {
			result.addAll(reajustes);
		}

		tabelaPreco.setTabelaPrecoParcelas(result);
		return tabelaPreco;
	}
    
    private List<ReajusteParametroParcelaDTO> findParametrosParcelas(Long idReajuste){
		return reajusteTabelaPrecoParcelaDAO.findIdsParcelasReajuste(idReajuste);
    }
    

	private TarifaPrecoRota copyTarifaPrecoRota(TarifaPrecoRota original, TabelaPreco tabelaPreco) {
		TarifaPrecoRota result = new TarifaPrecoRota();
		result.setRotaPreco(original.getRotaPreco());
		result.setTabelaPreco(tabelaPreco);
		result.setTarifaPreco(original.getTarifaPreco());
		return result;
	}

    public void setReajusteTabelaPrecoParcelaDAO(ReajusteTabelaPrecoParcelaDAO reajusteTabelaPrecoParcelaDAO) {
		this.reajusteTabelaPrecoParcelaDAO = reajusteTabelaPrecoParcelaDAO;
	}
    
	public void setTarifaPrecoRotaService(TarifaPrecoRotaService tarifaPrecoRotaService) {
		this.tarifaPrecoRotaService = tarifaPrecoRotaService;
	}
	

	public void setTabelaPrecoParcelaService(TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}
	public void setTabelaPrecoDAO(TabelaPrecoDAO tabelaPrecoDAO) {
		this.tabelaPrecoDAO = tabelaPrecoDAO;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public void setReajusteTabelaPrecoDAO(
			ReajusteTabelaPrecoDAO reajusteTabelaPrecoDAO) {
		this.reajusteTabelaPrecoDAO = reajusteTabelaPrecoDAO;
	}

	public void setCalculoSimulacaoNovosPrecosParametrizadoService(
			CalculoSimulacaoNovosPrecosParametrizadoService calculoSimulacaoNovosPrecosParametrizadoService) {
		this.calculoSimulacaoNovosPrecosParametrizadoService = calculoSimulacaoNovosPrecosParametrizadoService;
	}
	
	public void setCloneTabelaPrecoService(
			CloneTabelaPrecoService cloneTabelaPrecoService) {
		this.cloneTabelaPrecoService = cloneTabelaPrecoService;
	}

	public TabelaPrecoDAO getTabelaPrecoDAO() {
		return tabelaPrecoDAO;
	}
}
