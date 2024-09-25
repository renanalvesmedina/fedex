package com.mercurio.lms.tabelaprecos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mercurio.lms.tabelaprecos.model.dao.TabelaPrecoDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.dto.FiltroPaginacaoDto;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.tabelaprecos.model.ParametroReajusteTabPreco;
import com.mercurio.lms.tabelaprecos.model.ReajusteTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.ReajusteTabelaPrecoEntity;
import com.mercurio.lms.tabelaprecos.model.ReajusteTabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.ParametroReajusteTabelaPrecoDAO;
import com.mercurio.lms.tabelaprecos.model.dao.ReajusteTabelaPrecoDAO;
import com.mercurio.lms.tabelaprecos.model.dao.ReajusteTabelaPrecoParcelaDAO;
import com.mercurio.lms.tabelaprecos.util.ReajusteTabelaPrecoValidate;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class ReajusteTabelaPrecoService extends CrudService<ReajusteTabelaPrecoEntity, Long> {
	

	private static final int YEAR_VIGENCIA_FINAL_DEFAULT = 4000;
	private static final  String OPERACAO_SUCESSO = "LMS-00054";
	private static final  String ADVALOREM = "IDAdvalorem";
	private static final  String FRETE_QUILO = "IDFreteQuilo";
	private static final  String DD_MM_YYYY = "dd/MM/yyyy";
	private static final  String TNT = "T";
	private static final  String MERCURIO = "M";
	private static final  String AEREO = "A";
	private static final  String CIA_AEREA = "C";
	private static final  boolean NAO_EFETIVADA = false;
	private static final  String NAO_EFETIVADO = "N";
	private static final  Long ID_MOEDA = 1L;
	private static final  String TABELA_BRASIL_UNIFICADA = "Tabela Brasil Unificada";
	private static final  String TABELA_REFERENCIAL = "Referencial";
	private static final  String TABELA_GERAL = "Tabela Geral";
	private static final  Long COD_RODOVIARIO = 1L;
	private static final  Long COD_AEREO = 2L;
	private static final  Long ID_EMPRESA = 361L;
	private static final  String ATIVO = "A";
	private RecursoMensagemService recursoMensagemService;
	private ReajusteTabelaPrecoValidate reajusteTabelaPrecoValidate;
	private TipoTabelaPrecoService tipoTabelaPrecoService;	
	private TabelaPrecoService tabelaPrecoService;
	private ReajusteTabelaPrecoParcelaDAO reajusteTabelaPrecoParcelaDAO;
	private TabelaPrecoDAO tabelaPrecoDAO;
    private ParametroReajusteTabelaPrecoDAO parametroReajusteTabelaPrecoDAO;
    
	public Map<String, Object> findReajuste(FiltroPaginacaoDto filtro) {
		if(!reajusteTabelaPrecoValidate.isConsultaValida(filtro.getFiltros())){
			throw new BusinessException("LMS-00055");
		}
		
		List<Map<String, Object>> listFind = getReajusteTabelaPrecoDAO().find(filtro.getFiltros());
		Map<String, Object> toReturn = new HashMap<String, Object>();
		toReturn.put("list", listFind);
		toReturn.put("qtRegistros", listFind.size());
		return toReturn;
	}
	
	public void removeByIdReajuste(Long idReajuste){
		List<Long> idsReajusteTabelaPrecoParcela   = getListIdsReajusteTabelaPrecoParcela(idReajuste);
		List<Long> idsParametroReajusteTabelaPreco = getListIdsParametroReajusteTabelaPreco(idsReajusteTabelaPrecoParcela);
		Map<String, Object> map = getReajusteTabelaPrecoDAO().findReajusteById(idReajuste);
		parametroReajusteTabelaPrecoDAO.removeAllIds(idsParametroReajusteTabelaPreco);
		reajusteTabelaPrecoParcelaDAO.removeByIds(idsReajusteTabelaPrecoParcela);
		this.removeById(idReajuste);
		tabelaPrecoService.removeById((Long)map.get("idTabelaNova"));
	}

	public List<Map<String, Object>> listParcelas(Long idTabelaBase) {
		return getReajusteTabelaPrecoDAO().listParcelas(idTabelaBase);
	}
	
	public List<Map<String, Object>> listParcelaFreteQuilo(Long idReajuste) {
		return parametroReajusteTabelaPrecoDAO.listParcelaByCodParcela(idReajuste, FRETE_QUILO);
	}
	
	public List<Map<String, Object>> listParcelaFretePesoAereo(Integer idReajuste, Integer idOrigem, Integer idDestino) {
		if(idOrigem == null && idDestino == null){
			throw new BusinessException("LMS-00070");
		}
		
		return parametroReajusteTabelaPrecoDAO.listFretePesoByTabelaAereo(idReajuste, idOrigem, idDestino);
	}
	
	public List<Map<String, Object>> listParcelaTaxaCombustivel(Integer idReajuste, Integer idOrigem, Integer idDestino) {
		if(idOrigem == null && idDestino == null){
			throw new BusinessException("LMS-00070");
		}
		
		return parametroReajusteTabelaPrecoDAO.listParcelaTaxaCombustivel(idReajuste, idOrigem, idDestino);
	}
	
	public List<Map<String, Object>> listParcelaTarifaMinima(Integer idReajuste, Integer idOrigem, Integer idDestino) {
		if(idOrigem == null && idDestino == null){
			throw new BusinessException("LMS-00070");
		}
		
		return parametroReajusteTabelaPrecoDAO.listParcelaTarifaMinima(idReajuste, idOrigem, idDestino);
	}

	public List<Map<String, Object>> listParcelaFretePesoDiferenciada(Long idReajuste) {
		return parametroReajusteTabelaPrecoDAO.listFretePesoByTabelaDiferenciada(idReajuste);
	}
	
	public List<LinkedHashMap<String, Object>> listParcelaFretePesoRodoviario(Long idReajuste) {
		List<Map<String, Object>> listFreteQuilo = parametroReajusteTabelaPrecoDAO.listParcelaByCodParcela(idReajuste, FRETE_QUILO);
		List<Map<String, Object>> listFretePeso  = parametroReajusteTabelaPrecoDAO.listFretePesoByTabelaReferencialRodoviario(idReajuste);
		return createReferencialRodoviario(listFreteQuilo, listFretePeso);
	}
	

	public List<Map<String, Object>> listParcelaTaxaTerrestre(Long idReajuste) {
		return parametroReajusteTabelaPrecoDAO.listParcelaTaxaTerrestre(idReajuste);
	}
	
	private List<LinkedHashMap<String, Object>> createReferencialRodoviario(List<Map<String, Object>> listFreteQuilo, List<Map<String, Object>> listFretePeso){
		List<LinkedHashMap<String,Object>> retVal = new ArrayList<LinkedHashMap<String,Object>>();
		for (Map<String, Object> mapQuilo : listFreteQuilo) {
			LinkedHashMap values = new LinkedHashMap<String, Object>();
			values.put("TARIFA", mapQuilo.get("CD_TARIFA_PRECO"));
			for (Map<String, Object> mapPeso : listFretePeso) {
				String 	   key 	 = new StringBuilder().append(mapPeso.get("FAIXA_PROGRESSIVA")).append(" Kg").toString();
				BigDecimal value = calculoFretePesoRodoviario(mapQuilo.get("VALOR_CALCULADO"), mapPeso.get("VALOR"));
				values.put(key, value);
			}
			retVal.add(values);
		}
		return retVal;
	}
	
	private BigDecimal calculoFretePesoRodoviario(Object valorQuilo, Object valorPeso){
		if(valorQuilo == null || valorPeso == null) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(valorQuilo.toString()).multiply(new BigDecimal(valorPeso.toString()));
	}
	
	public List<Map<String, Object>> listParcelaAdvalorem(Long idReajuste) {
		return parametroReajusteTabelaPrecoDAO.listParcelaByCodParcela(idReajuste, ADVALOREM);
	}
	
	public List<Map<String, Object>> listParcelaGeneralidade(Long idReajuste) {
		return parametroReajusteTabelaPrecoDAO.listParcelaByGeneralidade(idReajuste);
	}
	
	public List<Map<String, Object>> listParcelaServicoAdicional(Long idReajuste) {
		return parametroReajusteTabelaPrecoDAO.listParcelaByServicoAdicional(idReajuste);
	}
	
	public List<Map<String, Object>> listParcelaTaxas(Long idReajuste) {
		return parametroReajusteTabelaPrecoDAO.listParcelaByTaxas(idReajuste);
	}
	
	public String salvarPercValor(List<Map<String, Object>> listParametros) {
		for (Map<String, Object> param : listParametros) {
			parametroReajusteTabelaPrecoDAO.updatePercentualValor(getLongValue(param, "ID_PARAMETRO_REAJUSTE"), getBigDecimalValue(param,"PERCENTUAL"));
		}
		return recursoMensagemService.findByChave(OPERACAO_SUCESSO, null);
	}
	
	public String salvarPercValorAndPercValorMin(List<Map<String, Object>> listParametros) {
		for (Map<String, Object> param : listParametros) {
			parametroReajusteTabelaPrecoDAO.updatePercValorAndPercValorMin(getLongValue(param, "ID_PARAMETRO_REAJUSTE"), getBigDecimalValue(param, "PERCENTUAL_VALOR"), getBigDecimalValue(param, "PERCENTUAL_VALOR_MIN"));
		}
		return recursoMensagemService.findByChave(OPERACAO_SUCESSO, null);
	}
	
	public Map<String, Object> findReajusteById(Long id) {
		Map<String, Object> map = getReajusteTabelaPrecoDAO().findReajusteById(id);
		map.put("listParcelas", getReajusteTabelaPrecoDAO().listParcelas(Long.valueOf(map.get("idTabelaBase").toString())));
		List<Long> parcelasSelected = reajusteTabelaPrecoParcelaDAO.listParcelasReajusteById(id);
		map.put("selection", parcelasSelected == null ? new Long[]{} : parcelasSelected.toArray());
		map.put("descricao", this.findDescricaoTabelaPreco(Long.valueOf(map.get("idTabelaNova").toString())).values());
		return map;
	}

	public Map<String, Object> findReajusteByIdTabelaNova(Long id) {
		try {
			return getReajusteTabelaPrecoDAO().findReajusteByIdTabelaNova(id);
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	public List<Map<String, Object>> findSuggestTabelaBase(String subString){
		return getReajusteTabelaPrecoDAO().findSuggestTabelaBase(subString);
	}
	
	public List<Map<String, Object>> findSuggestTabelaNova(String subString){
		return getReajusteTabelaPrecoDAO().findSuggestTabelaNova(subString);
	}
	
	public List<Map<String, Object>> findSuggestTabelaNovaEfetivada(String subString){
		return getReajusteTabelaPrecoDAO().findSuggestTabelaNovaEfetivada(subString);
	}

	public Long store(Map<String, Object> map){
		ReajusteTabelaPreco reajuste = populateReajusteTabelaPreco(map);
		
		reajusteTabelaPrecoValidate.validateBeforeStore(reajuste);
		
		verifyDataAgendamento(reajuste.getId(), reajuste.getIdTabelaBase(), reajuste.getDtAgendamento());

		if(map.containsKey("idTabelaNova")) {
			Long idTabelaNova = Long.valueOf((Integer) map.get("idTabelaNova"));
			String novaDescricaoTabelaPreco = map.get("descricao").toString();
			if (!novaDescricaoTabelaPreco.isEmpty() && novaDescricaoTabelaPreco != null) {
				getTabelaPrecoDAO().updateDescricaoTabelaPreco(idTabelaNova, novaDescricaoTabelaPreco);
			}
		}
		List<Long> parcelasSelected  	 = getListParcelasSelecionadas((List) map.get("selection"));  
		Long idTipoTabelaPreco    	 	 = findIdTipoTabelaPreco(reajuste.getId(), reajuste.getTipo(), reajuste.getVersao());
		Long idSubTipoTabelaPreco 		 = findIdSubTipoTabelaPreco(reajuste.getSubTipo());  		
		TabelaPreco dadosTabelaPrecoData = getReajusteTabelaPrecoDAO().findTabelaPreco(reajuste.getIdTabelaBase());

		if(!dadosTabelaPrecoData.getDsDescricao().equals(map.get("descricao").toString())){
			dadosTabelaPrecoData.setDsDescricao(map.get("descricao").toString());
		}
		Long idTabelaPreco            	 = findIdTabelaPreco(reajuste, idTipoTabelaPreco, idSubTipoTabelaPreco, reajuste.getTipo(), dadosTabelaPrecoData);
		Long idReajusteTabelaPreco    	 = saveReajusteTabela(reajuste, idTabelaPreco);
		
		processParcelas(idReajusteTabelaPreco, parcelasSelected, reajuste.getIdTabelaBase(), reajuste.getPercentualReajusteGeral());
		
		return idReajusteTabelaPreco;
	}
	
	private boolean verifyDataAgendamento(Long idReajusteTabelaPreco, Long idTabelaPrecoBase, YearMonthDay dataAgendamento){
		if(dataAgendamento == null){
			return Boolean.TRUE;
		}
		
		Long count = getReajusteTabelaPrecoDAO().executeCountDataAgendamento(idReajusteTabelaPreco, idTabelaPrecoBase);
		if(count != null && count > 0){
			throw new BusinessException("LMS-30075");
		}
		
		return Boolean.TRUE;
	}
	
	private Long saveReajusteTabela(ReajusteTabelaPreco reajuste, Long idTabelaPreco){
		ReajusteTabelaPrecoEntity entity = createReajusteTabelaPreco(reajuste, idTabelaPreco);
		store(entity);
		return entity.getId(); 
	}
	
	private void processParcelas(Long idReajuste, List<Long> parcelasSelected, Long idTabelaBase, BigDecimal percentualGeral){
		List<Long> parcelasSaved     = reajusteTabelaPrecoParcelaDAO.listParcelasReajusteById(idReajuste);
		Set<Long>  intersection  	 = new HashSet<Long>(parcelasSaved);
		
		intersection.retainAll(parcelasSelected);
		parcelasSaved.removeAll(intersection);
		parcelasSelected.removeAll(intersection);

		List<Long> idsReajusteTabelaPrecoParcela   = getListIdsReajusteTabelaPrecoParcela(idReajuste, parcelasSaved);
		List<Long> idsParametroReajusteTabelaPreco = getListIdsParametroReajusteTabelaPreco(idsReajusteTabelaPrecoParcela);
		removeParametroReajusteTabelaPreco(idsParametroReajusteTabelaPreco);
		removeReajusteTabelaPrecoParcela(idsReajusteTabelaPrecoParcela); 
		
		addListParcelas(idReajuste, parcelasSelected);
		addParametroReajusteTabPreco(listParametroReajusteTabPreco(idReajuste, idTabelaBase, percentualGeral, parcelasSelected));
	}
	
	private void addListParcelas(Long idReajuste, List<Long> parcelasSelected){
		for (Long idParcela : parcelasSelected) {
			reajusteTabelaPrecoParcelaDAO.store(new ReajusteTabelaPrecoParcela(idReajuste, idParcela)); 
		}
		reajusteTabelaPrecoParcelaDAO.flush();
	}
	
	private void addParametroReajusteTabPreco(List<ParametroReajusteTabPreco> listParametro){
		for (ParametroReajusteTabPreco parametroReajusteTabPreco : listParametro) {
			parametroReajusteTabelaPrecoDAO.store(parametroReajusteTabPreco);
		}
	}
	
	private List<Long> getListIdsReajusteTabelaPrecoParcela(Long idReajuste){
		return reajusteTabelaPrecoParcelaDAO.listIdsReajusteTabelaPrecoParcela(idReajuste);
	}
	
	private List<Long> getListIdsReajusteTabelaPrecoParcela(Long idReajuste, List<Long> ids){
		if(CollectionUtils.isEmpty(ids)) {
			return new ArrayList<Long>();
		}
		
		return reajusteTabelaPrecoParcelaDAO.listIdsReajusteTabelaPrecoParcela(idReajuste, ids);
	}
	
	private List<Long> getListIdsParametroReajusteTabelaPreco(List<Long> idsReajusteTabelaPrecoParcela){
		if(CollectionUtils.isEmpty(idsReajusteTabelaPrecoParcela)){
			return new ArrayList<Long>();
		}
		return parametroReajusteTabelaPrecoDAO.listIdsParametroReajusteTabelaPreco(idsReajusteTabelaPrecoParcela);
	}
	
	private void removeReajusteTabelaPrecoParcela(List<Long> idsReajusteTabelaPrecoParcela){
		if(CollectionUtils.isNotEmpty(idsReajusteTabelaPrecoParcela)){
			reajusteTabelaPrecoParcelaDAO.removeByIds(idsReajusteTabelaPrecoParcela);
		}
	}
	
	private void removeParametroReajusteTabelaPreco(List<Long> idsParametroReajusteTabelaPreco){
		if(CollectionUtils.isNotEmpty(idsParametroReajusteTabelaPreco)){
			parametroReajusteTabelaPrecoDAO.removeAllIds(idsParametroReajusteTabelaPreco);
		}
	}
	
	private Long findIdTipoTabelaPreco(Long idReajuste, String tpTabelaPreco, String versao){
		Long idTipoTabelaPreco = getReajusteTabelaPrecoDAO().getIdTipoTabelaPreco(tpTabelaPreco, versao);
		
		if(isInsert(idReajuste) && reajusteTabelaPrecoValidate.validateInsertTipoTabela(idTipoTabelaPreco, tpTabelaPreco)){
			TipoTabelaPreco tipoTabelaPreco = createTipoTabelaPreco(idReajuste, tpTabelaPreco, versao);
			tipoTabelaPrecoService.store(tipoTabelaPreco);
			return tipoTabelaPreco.getIdTipoTabelaPreco();
		}
		
		return idTipoTabelaPreco;
	}
	
	private Long findIdTabelaPreco(ReajusteTabelaPreco reajuste, Long idTipoTabelaPreco, Long idSubTipoTabelaPreco, String tipoTabela, TabelaPreco dadosTabelaPrecoData){
		Long idTabelaPreco = getReajusteTabelaPrecoDAO().getIdTabelaPreco(idTipoTabelaPreco, idSubTipoTabelaPreco);
		
		if(isInsert(reajuste.getId()) && reajusteTabelaPrecoValidate.validateInsertTabelaPreco(reajuste, idTabelaPreco, tipoTabela)){
			TabelaPreco tabPreco = createTabelaPreco(idTipoTabelaPreco, idSubTipoTabelaPreco, reajuste, dadosTabelaPrecoData);
			tabelaPrecoService.store(tabPreco);
			return tabPreco.getIdTabelaPreco();
		}
		
		return idTabelaPreco;
	}
	
	private Long findIdSubTipoTabelaPreco(String subtipoTabelaPreco){
		Long idSubTipoTabelaPreco = getReajusteTabelaPrecoDAO().getIdSubTipoTabelaPreco(subtipoTabelaPreco);
		
		if(idSubTipoTabelaPreco == null){
			throw new BusinessException("LMS-01254"); 
		}
		
		return idSubTipoTabelaPreco;
	}
	
	private String getIdentificacao(String tipoTabelaPreco){
		if(TNT.equals(tipoTabelaPreco)){
			return TABELA_BRASIL_UNIFICADA;
		}
		
		if(MERCURIO.equals(tipoTabelaPreco)){
			return TABELA_REFERENCIAL;
		}
		
		if(AEREO.equals(tipoTabelaPreco)){
			return TABELA_GERAL;
		}
		
		return null; 
	}
	
	private boolean isInsert(Long id){
		return id == null;
	}
	
	private Long getIdServico(String tipoTabelaPreco){
		if(TNT.equals(tipoTabelaPreco) || MERCURIO.equals(tipoTabelaPreco)){
			return COD_RODOVIARIO;
		}
		
		if(AEREO.equals(tipoTabelaPreco) || CIA_AEREA.equals(tipoTabelaPreco)){ 
			return COD_AEREO;
		}
		
		return null;
	}
	
	private ReajusteTabelaPreco populateReajusteTabelaPreco(Map<String, Object> map){
		ReajusteTabelaPreco reajuste = new ReajusteTabelaPreco();
		reajuste.setId(map.get("id") != null ? Long.valueOf(map.get("id").toString()) : null);
		reajuste.setVersao(map.get("versao").toString());
		reajuste.setTipo(map.get("tipo").toString().toUpperCase());
		reajuste.setIdFilial(SessionUtils.getFilialSessao().getIdFilial());
		reajuste.setSubTipo(map.get("subTipo").toString().toUpperCase());
		reajuste.setDtVigenciaInicial((YearMonthDay) map.get("dtVigenciaInicial"));
		reajuste.setDtVigenciaFinal((YearMonthDay) map.get("dtVigenciaFinal"));
		reajuste.setDtEfetivacao((YearMonthDay) map.get("dtEfetivacao"));
		reajuste.setDtAgendamento((YearMonthDay) map.get("dtAgendamento"));
		reajuste.setPercentualReajusteGeral(new BigDecimal(map.get("percentualReajusteGeral").toString()));
		reajuste.setIdTabelaBase(Long.valueOf(map.get("idTabelaBase").toString()));
		reajuste.setTabelaBase(map.get("tabelaBase").toString()); 
		reajuste.setCheckedFechaVigenciaTabelaBase(Boolean.TRUE.equals(map.get("fechaVigenciaTabelaBase")));

		return reajuste;
	}
	
	
	private TipoTabelaPreco createTipoTabelaPreco(Long idReajuste, String tpTabelaPreco, String versao){
		TipoTabelaPreco tipoTabelaPreco = new TipoTabelaPreco();
		Empresa empresa = new Empresa();
		empresa.setIdEmpresa(ID_EMPRESA);
		Servico serv = new Servico();
		serv.setIdServico(getIdServico(tpTabelaPreco));
		
		tipoTabelaPreco.setDsIdentificacao(getIdentificacao(tpTabelaPreco));
		tipoTabelaPreco.setTpTipoTabelaPreco(new DomainValue(tpTabelaPreco));
		tipoTabelaPreco.setTpSituacao(new DomainValue(ATIVO)); 
		tipoTabelaPreco.setEmpresaByIdEmpresaCadastrada(empresa);
		tipoTabelaPreco.setEmpresaByIdEmpresaLogada(empresa);
		tipoTabelaPreco.setCliente(null);
		tipoTabelaPreco.setServico(serv);
		tipoTabelaPreco.setNrVersao(Integer.valueOf(versao));

		return tipoTabelaPreco;
	}
	
	private ReajusteTabelaPrecoEntity createReajusteTabelaPreco(ReajusteTabelaPreco reajuste, Long idTabelaPreco){
		ReajusteTabelaPrecoEntity entity = new ReajusteTabelaPrecoEntity();
		entity.setDataGeracao(JTDateTimeUtils.getDataAtual());
		entity.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		entity.setIsEfetivado(NAO_EFETIVADO);
		entity.setIdTabelaPrecoBase(reajuste.getIdTabelaBase());
		entity.setIdTabelaNova(idTabelaPreco);
		entity.setIdFilial(reajuste.getIdFilial());
		Long nrReajuste = getReajusteTabelaPrecoDAO().getMaxNrReajuste(reajuste.getIdFilial());
		entity.setNrReajuste(nrReajuste == null ? 1 : nrReajuste + 1);
		entity.setId(reajuste.getId());
		entity.setPercentualReajusteGeral(reajuste.getPercentualReajusteGeral());
		entity.setDataVigenciaInicial(reajuste.getDtVigenciaInicial());
		entity.setDataVigenciaFinal(reajuste.getDtVigenciaFinal());
		entity.setDataAgendamento(reajuste.getDtAgendamento());
		entity.setFechaVigenciaTabelaBase(reajuste.isCheckedFechaVigenciaTabelaBase() ? "S" : "N");
		return entity;
	}
	
	private TabelaPreco createTabelaPreco(Long idTipoTabelaPreco, Long idSubtipoTabelaPreco, ReajusteTabelaPreco reajusteTabelaPreco,TabelaPreco dadosTabelaPrecoData){
		TipoTabelaPreco tpTabPreco = new TipoTabelaPreco();
		tpTabPreco.setIdTipoTabelaPreco(idTipoTabelaPreco);
		
		SubtipoTabelaPreco subTipoTabPreco = new SubtipoTabelaPreco();
		subTipoTabPreco.setIdSubtipoTabelaPreco(idSubtipoTabelaPreco);
		
		Moeda moeda = new Moeda();
		moeda.setIdMoeda(ID_MOEDA);
		
		Usuario user = new Usuario();
		user.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		TabelaPreco tabPreco = new TabelaPreco();
		tabPreco.setIdTabelaPreco(reajusteTabelaPreco.getIdTabelaBase());
		
		TabelaPreco tp = new TabelaPreco();
		tp.setDtGeracao(JTDateTimeUtils.getDataAtual());
		tp.setTipoTabelaPreco(tpTabPreco);
		tp.setSubtipoTabelaPreco(subTipoTabPreco);
		tp.setMoeda(moeda);
		tp.setUsuario(user);
		tp.setDtVigenciaInicial(reajusteTabelaPreco.getDtVigenciaInicial());
		tp.setBlEfetivada(NAO_EFETIVADA);
		tp.setPcReajuste(reajusteTabelaPreco.getPercentualReajusteGeral());
		tp.setPsMinimo(dadosTabelaPrecoData.getPsMinimo());
		tp.setTabelaPreco(tabPreco);
		tp.setTpCalculoFretePeso(dadosTabelaPrecoData.getTpCalculoFretePeso());
		tp.setDsDescricao(dadosTabelaPrecoData.getDsDescricao()); 
		tp.setTpTarifaReajuste(dadosTabelaPrecoData.getTpTarifaReajuste());
		tp.setTpCalculoPedagio(dadosTabelaPrecoData.getTpCalculoPedagio());
		tp.setPcDescontoFreteMinimo(dadosTabelaPrecoData.getPcDescontoFreteMinimo());
		tp.setTpCategoria(dadosTabelaPrecoData.getTpCategoria());
		tp.setTpServico(dadosTabelaPrecoData.getTpServico());
		tp.setDtVigenciaFinal(getVigenciaFinalTabPreco(reajusteTabelaPreco.getDtVigenciaFinal()));
		tp.setBlImprimeTabela(false);
		tp.setBlIcmsDestacado(dadosTabelaPrecoData.getBlIcmsDestacado());
		
		return tp;
	}
	
	private List<ParametroReajusteTabPreco> listParametroReajusteTabPreco(Long idReajuste, Long idTabelaBase, BigDecimal percentualGeral, List<Long> idsParcelas){
		List<ParametroReajusteTabPreco> list = new ArrayList<ParametroReajusteTabPreco>();
		List<Map<String,Object>> listParametros = new ArrayList<Map<String,Object>>();
		
		if(CollectionUtils.isNotEmpty(idsParcelas)){
			listParametros = parametroReajusteTabelaPrecoDAO.listParametrosReajuste(idReajuste, idTabelaBase, idsParcelas);
		}
		
		for (Map<String,Object> map : listParametros) {
			list.add(createParamtroReajusteTabPreco(percentualGeral, map));
		}
		
		return list;
	}

	private ParametroReajusteTabPreco createParamtroReajusteTabPreco(BigDecimal percentual, Map<String, Object> map) {
		ParametroReajusteTabPreco parametroReajuste = new ParametroReajusteTabPreco();
		parametroReajuste.setIdReajusteTabPrecoParcela(getLongValue(map, "ID_REAJUSTE_TAB_PRECO_PARCELA"));
		parametroReajuste.setIdPrecoFrete(getLongValue(map, "ID_PRECO_FRETE"));
		parametroReajuste.setIdValorFaixaProgressiva(getLongValue(map, "ID_VALOR_FAIXA_PROGRESSIVA"));
		parametroReajuste.setPercentualParcela(percentual);
		parametroReajuste.setPercentualMinimoParcela(percentual);
		parametroReajuste.setValorParcela(getBigDecimalValue(map, "VALOR_PARCELA"));
		parametroReajuste.setValorMinimoParcela(getBigDecimalValue(map, "VL_MINIMO_PARCELA"));
		return parametroReajuste;
	}
	
	private Long getLongValue(Map<String,Object> map, String key){
		if(map.get(key) == null){
			return null;
		}
		
		return Long.valueOf(map.get(key).toString());
	}
	
	private BigDecimal getBigDecimalValue(Map<String,Object> map, String key){
		if(map.get(key) == null){
			return null;
		}
		
		return new BigDecimal(map.get(key).toString());
	}
	
	private String transformDatas(Object data){
		DateTimeFormatter formatterYearMonthDay = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		DateTimeFormatter formatterDayMonthYear = DateTimeFormat.forPattern(DD_MM_YYYY);
		Pattern patternDayMonthYear = Pattern.compile("^([\\d]{2})/([\\d]{2})/([\\d]{4})$");
		
		if(data == null || StringUtils.isBlank(data.toString())){
			return null;
		}
		
		Matcher matcher = patternDayMonthYear.matcher(data.toString());
		if (matcher.matches()) {
			return data.toString();
		}
		
		return formatterDayMonthYear.print(formatterYearMonthDay.parseDateTime(data.toString()));
	}
	
	private YearMonthDay getVigenciaFinalTabPreco(YearMonthDay vigenciaFinal){
		if(vigenciaFinal == null){
			return new YearMonthDay(YEAR_VIGENCIA_FINAL_DEFAULT,1,1);
		}
		
		return vigenciaFinal;
	}
	
	private List<Long> getListParcelasSelecionadas(List<Integer> ids){
		List<Long> list = new ArrayList<Long>();

		if(ids == null || ids.isEmpty()){
			throw new BusinessException("LMS-25033");
		}
		
		for (Integer integer : ids) {
			list.add(integer.longValue());
		}
		return list;
	}

	public Map<String, Object> findDescricaoTabelaPreco(Long idTabelaBase){
		TabelaPreco dsDescricaoTabelaPreco = getReajusteTabelaPrecoDAO().findTabelaPreco(idTabelaBase);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("descricao", dsDescricaoTabelaPreco.getDsDescricao());

		return map;
	}
	
	@Override
	public java.io.Serializable store(ReajusteTabelaPrecoEntity bean) {
		return super.store(bean);
	}
		
    private ReajusteTabelaPrecoDAO getReajusteTabelaPrecoDAO() {
        return (ReajusteTabelaPrecoDAO) getDao();
    }
    
    public void setReajusteTabelaPrecoDAO(ReajusteTabelaPrecoDAO dao) {
        setDao( dao );
    }
    
    public void setReajusteTabelaPrecoValidate(ReajusteTabelaPrecoValidate reajusteTabelaPrecoValidate) {
		this.reajusteTabelaPrecoValidate = reajusteTabelaPrecoValidate;
	}
    
    public void setTipoTabelaPrecoService(TipoTabelaPrecoService tipoTabelaPrecoService) {
		this.tipoTabelaPrecoService = tipoTabelaPrecoService;
	}

    public void setReajusteTabelaPrecoParcelaDAO(ReajusteTabelaPrecoParcelaDAO reajusteTabelaPrecoParcelaDAO) {
		this.reajusteTabelaPrecoParcelaDAO = reajusteTabelaPrecoParcelaDAO;
	}
    
    public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
    
    public void setParametroReajusteTabelaPrecoDAO(ParametroReajusteTabelaPrecoDAO parametroReajusteTabelaPrecoDAO) {
		this.parametroReajusteTabelaPrecoDAO = parametroReajusteTabelaPrecoDAO;
	}
    
    public void setRecursoMensagemService(RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}

	public TabelaPrecoDAO getTabelaPrecoDAO() {
		return tabelaPrecoDAO;
	}

	public void setTabelaPrecoDAO(TabelaPrecoDAO tabelaPrecoDAO) {
		this.tabelaPrecoDAO = tabelaPrecoDAO;
	}
}
