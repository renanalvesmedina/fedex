package com.mercurio.lms.tabelaprecos.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.ListOrderedMap;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.OperacaoServicoLocalizaService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.service.GrupoRegiaoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.dto.ParametroRotaDTO;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;
import com.mercurio.lms.vendas.util.TabelasClienteUtil;

/**
 * 30.03.02.08 Tabela E-commerce / Diferenciada
 * 
 * @spring.bean id="lms.tabelaprecos.report.emitirTabelaEcommerceDiferenciadaService"
 * @spring.property name="reportName" value="/com/mercurio/lms/tabelaprecos/report/emitirTabelaEcommerceDiferenciada.vm"
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="280"
 * @spring.property name="crossTabBandWidths" value="396"
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class EmitirTabelaEcommerceDiferenciadaService extends ReportServiceSupport { 

	private static final String INTERIOR = "Interior";
	/* Nome da Tebela temporaria */
	private static final String NOME_TABELA = "TMP_E_DIFERENCIADAS";
	private static final String LOCALIZACAO_CAPITAL = "Capital";

	private UnidadeFederativaService unidadeFederativaService;
	private OperacaoServicoLocalizaService operacaoServicoLocalizaService;
	private MunicipioFilialService municipioFilialService;
	private GrupoRegiaoService grupoRegiaoService; 
	private TabelasClienteService tabelasClienteService;
	
	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}
	
	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}	
	
	/* Para utilização de RecursosMensagens*/
    private ConfiguracoesFacade configuracoesFacade;
    private DomainValueService domainValueService;

	public JRReportDataObject execute(Map parameters) throws Exception {
    	return null;
    }

	public List<Map<String, String>> findDados(Map parameters) throws Exception {
		String 	tpTabelaPreco = MapUtils.getString(parameters, "tpTabelaPreco");
		Long idTabelaPreco = MapUtils.getLong(parameters,"tabelaPreco.idTabelaPreco");
    	if(idTabelaPreco == null){
    		return null;
    	}

    	/*Obtem a localizacao comercial do cliente*/   	
    	String localComercial = MapUtils.getString(parameters, "dsTipoLocalizacaoMunicipio");    	
    	
    	/** Monta Ids */
    	Long[] ids = null;
    	Long idUnidadeFederativaDestino = MapUtils.getLong(parameters, "idUnidadeFederativaDestino");
    	Long idTipoLocalizacaoMunicipioDestino = MapUtils.getLong(parameters, "idTipoLocalizacaoMunicipioDestino");
    	Long idUnidadeFederativaOrigem = MapUtils.getLong(parameters, "idUnidadeFederativaOrigem");
    	Long idTipoLocalizacaoMunicipioOrigem = MapUtils.getLong(parameters, "idTipoLocalizacaoMunicipioOrigem");
		
    	/*Necessário para corrigir  a emissao da tabela frete pacotinho*/
    	if(ConstantesExpedicao.TP_FRETE_PACOTINHO.equals(tpTabelaPreco) 
    			&& localComercial != null 
    				&& localComercial.contains(INTERIOR)){
    		idTipoLocalizacaoMunicipioOrigem = null;
    		parameters.put("idTipoLocalizacaoMunicipioOrigem", null);
    	}
    	
    	
		if(LongUtils.hasValue(idUnidadeFederativaDestino) && LongUtils.hasValue(idTipoLocalizacaoMunicipioDestino)) {
			ids = new Long[]{idTabelaPreco,idTabelaPreco,idUnidadeFederativaDestino, idTipoLocalizacaoMunicipioDestino, idTabelaPreco,idTabelaPreco};
			
    	} else 
    		if(LongUtils.hasValue(idUnidadeFederativaDestino)) {
    		ids = new Long[]{idTabelaPreco,idTabelaPreco,idUnidadeFederativaDestino, idTabelaPreco,idTabelaPreco};
    		
    	} else 
    		if(LongUtils.hasValue(idTipoLocalizacaoMunicipioDestino)) {
    		ids = new Long[]{idTabelaPreco,idTabelaPreco, idTipoLocalizacaoMunicipioDestino, idTabelaPreco,idTabelaPreco};
    		
    	} else 
    		if(LongUtils.hasValue(idUnidadeFederativaOrigem) && LongUtils.hasValue(idTipoLocalizacaoMunicipioOrigem)) {    			
			ids = new Long[]{idTabelaPreco,idTabelaPreco,idUnidadeFederativaOrigem, idTipoLocalizacaoMunicipioOrigem, idTabelaPreco,idTabelaPreco};
			
    	} else 
    		if(LongUtils.hasValue(idUnidadeFederativaOrigem)) {
    		ids = new Long[]{idTabelaPreco,idTabelaPreco,idUnidadeFederativaOrigem, idTabelaPreco,idTabelaPreco};
    		
    	} else 
    		if(LongUtils.hasValue(idTipoLocalizacaoMunicipioOrigem)) {
    		ids = new Long[]{idTabelaPreco,idTabelaPreco, idTipoLocalizacaoMunicipioOrigem, idTabelaPreco,idTabelaPreco};
    		
    	} else ids = new Long[]{idTabelaPreco,idTabelaPreco,idTabelaPreco,idTabelaPreco};

    	List result = getJdbcTemplate().queryForList(createQuery(parameters), ids);


    	if(ConstantesExpedicao.TP_FRETE_PACOTINHO.equals(tpTabelaPreco)) {
    		result = listaDadosPacotinho(localComercial,result);
    	}

    	if(result != null && !result.isEmpty()){
    	Set setCrosstab = new LinkedHashSet();
		montaParametersReport(parameters,idTabelaPreco);
    		result = populateTable(setCrosstab,result,parameters);    		
    	}

    	ordenaParametroRota(parameters, result);
    	
    	for (Object object : result) {
			Map map = (Map)object;
			map.put("VALOR_FIXO", FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VALOR_FIXO")));
			map.put("FRETEPORKG", FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_5_CASAS, MapUtilsPlus.getBigDecimal(map,"FRETEPORKG")));
			map.put("VL_ADVALOREM", FormatUtils.formatDecimal(TabelasClienteUtil.FORMATO_2_CASAS, MapUtilsPlus.getBigDecimal(map,"VL_ADVALOREM")));
	}

     	return result;
	}
	
	private void ordenaParametroRota(Map parametros, List<ListOrderedMap> dados) {
		final Map<ParametroRotaDTO, ListOrderedMap> mapDados = new HashMap<ParametroRotaDTO, ListOrderedMap>();
		List<ParametroRotaDTO> listParametroRotaDTO = new LinkedList<ParametroRotaDTO>();
	
		
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		UnidadeFederativa uf = unidadeFederativaService.findByIdPessoa(idFilial);
		for (ListOrderedMap map : dados) {
			ParametroRotaDTO rota = toParametroClienteRotaDTO(map,idFilial,uf.getIdUnidadeFederativa());
			listParametroRotaDTO.add(rota);
			mapDados.put(rota, map);
		}

		Collections.sort(listParametroRotaDTO);

		dados.clear();
		for (ParametroRotaDTO parametroRotaDTO : listParametroRotaDTO) {
			ListOrderedMap listOrderedMap = mapDados.get(parametroRotaDTO);
			dados.add(listOrderedMap);
		}
	}
	
	
	
	private ParametroRotaDTO toParametroClienteRotaDTO(ListOrderedMap map, Long idFilial, Long idUf) {	
		ParametroRotaDTO parametro = new ParametroRotaDTO();
		
		parametro.setParametroClienteAttrs(new Object[]{map});
		parametro.setSgUnidadeFederativaOrigem(MapUtilsPlus.getString(map,"UF_ORIGEM"));
		parametro.setSgUnidadeFederativaDestino(MapUtilsPlus.getString(map,"UF_DESTINO"));
		parametro.setIdUnidadeFederativaOrigem(MapUtilsPlus.getLong(map,"ID_UF_ORIGEM"));
		parametro.setIdUnidadeFederativaDestino(MapUtilsPlus.getLong(map,"ID_UF_DESTINO"));
		parametro.setIdFilialOrigem(MapUtilsPlus.getLong(map,"ID_FILIAL_ORIGEM"));
		parametro.setIdFilialDestino(MapUtilsPlus.getLong(map,"ID_FILIAL_DESTINO"));
		parametro.setSgFilialDestino(MapUtilsPlus.getString(map,"SG_FILIAL_DESTINO"));
		parametro.setSgFilialOrigem(MapUtilsPlus.getString(map,"SG_FILIAL_ORIGEM"));
		parametro.setIdFilialReferencia(idFilial);

		parametro.setIdUnidadeFederativaReferencia(idUf);
		parametro.setDsTipoLocalizacaoMunicipioDestino((String) MapUtilsPlus.getObject(map,"DS_TIPO_LOCAL_DESTINO"));
		parametro.setDsTipoLocalizacaoMunicipioOrigem((String) MapUtilsPlus.getObject(map,"DS_TIPO_LOCAL_ORIGEM"));
		parametro.setDsGrupoRegiaoDestino(MapUtilsPlus.getString(map,"DS_GRUPO_REGIAO_D"));
		parametro.setDsGrupoRegiaoOrigem(MapUtilsPlus.getString(map,"DS_GRUPO_REGIAO_O"));
		
		return parametro;
	}

	private List listaDadosPacotinho(String localComercial, List result){
		
		List listResult = new ArrayList();
		if(LOCALIZACAO_CAPITAL.equals(localComercial)){    			
			for(Object mp : result){
				Map map = (Map)mp;
				String lc = MapUtils.getString(map,"ORIGEM");
				if(localComercial.equals(lc)){
					listResult.add(mp);
				}
			}    		
		}else{
			for(Object mp : result){
				Map map = (Map)mp;
				String lc = MapUtils.getString(map,"ORIGEM");
				if(!LOCALIZACAO_CAPITAL.equals(lc)){
					listResult.add(mp);
				}
			}    			
		}	
		return  listResult;					
	}


    /**
	 *  Metodo que grava os dados na base.
	 *  
	 * @author Alexandre Poletto - LMS - GT5
	 * @param JdbcTemplate instancia de JdbcTemplate, List com os dados a serem inseridos.
	 * @return void  
	 */
	public List<Map> populateTable(Set setCrosstab, List listDados, Map parameters) {	

		List listaUFOrigem = new ArrayList();
		List listaUFDestino = new ArrayList();
        HashMap tabelas = new HashMap();
		String sql = "";
		List sqlRegistros = new ArrayList();
		StringBuffer colunas = null;
		StringBuffer values = null;
		String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
		String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");
		String cdMinimoProgressivo = null; 
		int i = 1;
		/* Descobre as Colunas(Headers) do relatorio. */
		List gruposRegiao = new ArrayList();
		BigDecimal anterior = null;
		BigDecimal ultima = null;
		BigDecimal primeira = BigDecimal.TEN;
		String des = "";
		String tarifaOriginal = "";
		BigDecimal idTabelaPreco = null;
		BigDecimal idUnidadeFederativa = null;
		boolean grupo = false; 
		for (Iterator it = listDados.iterator(); it.hasNext();) { 
			Map map = (Map)it.next();
			idUnidadeFederativa =  (BigDecimal) map.get("ID_UF_DESTINO");
			if(anterior == null){
				primeira = (BigDecimal) map.get("VL_FAIXA_PROGRESSIVA");
				tarifaOriginal = MapUtils.getString(map,"CD_TARIFA");
			if(tarifaOriginal == null){
				tarifaOriginal = MapUtils.getString(map,"TARIFA");
			}
				idTabelaPreco  = (BigDecimal) map.get("ID_TABELA_PRECO");
				anterior = idUnidadeFederativa;
			}

			if(idUnidadeFederativa.equals(anterior)){
				gruposRegiao.add(map);
				anterior = idUnidadeFederativa;
				des = MapUtils.getString(map,"UF_DESTINO");
				idTabelaPreco  = (BigDecimal) map.get("ID_TABELA_PRECO");
				anterior = idUnidadeFederativa;
				if(map.get("DS_GRUPO_REGIAO_D") != null){
					grupo = true;
				}
				continue;
			}

			if(primeira.equals((BigDecimal) map.get("VL_FAIXA_PROGRESSIVA"))&& !grupo){
				cdMinimoProgressivo = buscaGrupos(listDados,cdMinimoProgressivo, gruposRegiao, anterior, primeira,	des, tarifaOriginal, idTabelaPreco, idUnidadeFederativa);
			}
			anterior = null;
			tarifaOriginal = MapUtils.getString(map,"CD_TARIFA");
			if(tarifaOriginal == null){
				tarifaOriginal = MapUtils.getString(map,"TARIFA");
			}
			grupo = false;
		
			gruposRegiao.add(map);
			anterior = idUnidadeFederativa;
		}
		cdMinimoProgressivo = buscaGrupos(listDados,cdMinimoProgressivo, gruposRegiao, anterior, primeira,	des, tarifaOriginal, idTabelaPreco, idUnidadeFederativa);
		List<Map> dados =  gruposRegiao;
		List controle  = new ArrayList();
		for (Map map : dados ) { 
			BigDecimal valor   	= BigDecimalUtils.getBigDecimal(map.get("VALOR_FIXO")); 
			BigDecimal adv   		= BigDecimalUtils.getBigDecimal(map.get("VL_ADVALOREM"));
	   			String ufOrigem 	= MapUtils.getString(map,"UF_ORIGEM");
				String ufDestino 	= MapUtils.getString(map,"UF_DESTINO");
		        String mOrigem 		= MapUtils.getString(map,"ORIGEM");
		        String mDestino 	= MapUtils.getString(map,"DESTINO");
		        cdMinimoProgressivo = MapUtils.getString(map,"CD_MINIMO_PROGRESSIVO");

	        String grupoRegiaoOrigem = (String) map.get("DS_GRUPO_REGIAO_O");
	        String grupoRegiaoDestino = (String) map.get("DS_GRUPO_REGIAO_D");
	        
				if(grupoRegiaoOrigem != null){
					mOrigem = grupoRegiaoOrigem;
				}
				if(grupoRegiaoDestino != null){
					mDestino = grupoRegiaoDestino;
				}

		        if(mOrigem == null){
					mOrigem = !listaUFOrigem.contains(ufOrigem) ? msgTodoEstado : msgDemaisLocalidades;
				}

				if(mDestino == null){
					mDestino = !listaUFDestino.contains(ufDestino) ? msgTodoEstado : msgDemaisLocalidades;
				}


				String origem = mOrigem;
	   			String destino = mDestino;	
				if(!mOrigem.startsWith(ufOrigem)){
					 origem = ufOrigem + " - " + mOrigem;
					 destino = ufDestino + " - " + mDestino;	
	   		}

	   			map.put("ORIGEM",origem);
	   			map.put("DESTINO",destino);

	   			if(valor != null){	   				
	   				map.put("VALOR_FIXO", valor);
		}
	   			if(adv != null){
	   				map.put("VL_ADVALOREM", adv);
		}
		}

		return  dados;
		}
	
	private String buscaGrupos(List listDados, String cdMinimoProgressivo,
			List gruposRegiao, BigDecimal anterior, BigDecimal primeira,
			String des, String tarifaOriginal, BigDecimal idTabelaPreco,
			BigDecimal idUnidadeFederativa) {
		List<GrupoRegiao> grupos =  grupoRegiaoService.findGruposRegiao(idTabelaPreco.longValue(), anterior.longValue()) ;
		
		String capitalAnte = "";
		String estadoAnte = "";
		String filialAnte = "";
		for (GrupoRegiao gr : grupos) {		
			
			List<Map> dados =  listDados;
				
			List capital = new ArrayList();
			List todoEstado = new ArrayList();
			List filial = new ArrayList();
			for (Map map2 : dados) { 
				String ufDestino 	= MapUtils.getString(map2,"UF_DESTINO");
				String mDestino 	= MapUtils.getString(map2,"DESTINO");
				BigDecimal idUF = (BigDecimal) map2.get("ID_UF_DESTINO");
				
				if(gr.getUnidadeFederativa().getIdUnidadeFederativa().equals(anterior.longValue()) && idUF.longValue() == anterior.longValue()){
					String tarifa = MapUtils.getString(map2,"CD_TARIFA");

					if(!tarifaOriginal.equals(tarifa)){
						continue;
					}
					
										
					Object column 		= BigDecimalUtils.getBigDecimal(map2.get("VL_FAIXA_PROGRESSIVA"));
					Object frete 		= BigDecimalUtils.getBigDecimal(map2.get("FRETEPORKG"));
					BigDecimal valor   	= BigDecimalUtils.getBigDecimal(map2.get("VALOR_FIXO")); 
					BigDecimal adv   		= BigDecimalUtils.getBigDecimal(map2.get("VL_ADVALOREM"));
					Object rotaPreco	= map2.get("ID_ROTA_PRECO");
					Object nrColunaDin  = map2.get("NR_COLUNA_DINAMICA");
					String ufOrigem 	= MapUtils.getString(map2,"UF_ORIGEM");
					
					String mOrigem 		= MapUtils.getString(map2,"ORIGEM");
					cdMinimoProgressivo = MapUtils.getString(map2,"CD_MINIMO_PROGRESSIVO");
					
					String grupoRegiaoOrigem = (String) map2.get("DS_GRUPO_REGIAO_O");
					String grupoRegiaoDestino = (String) map2.get("DS_GRUPO_REGIAO_D");		
					
					ListOrderedMap novoRegistro = new ListOrderedMap();
					if("A".equals(gr.getTpAjuste().getValue()) ){
						if("P".equals(gr.getTpValorAjuste().getValue()) ){
							valor = valor.add(valor.multiply(gr.getVlAjustePadrao().divide(new BigDecimal(100),4,RoundingMode.HALF_EVEN))); 								
						}
					}
					if("D".equals(gr.getTpAjuste().getValue()) ){
						if("P".equals(gr.getTpValorAjuste().getValue()) ){
							valor = valor.subtract(valor.multiply(gr.getVlAjustePadrao().divide(new BigDecimal(100),4,RoundingMode.HALF_EVEN))); 								
						}
					}
					
					if("A".equals(gr.getTpAjusteAdvalorem().getValue()) ){
						if("P".equals(gr.getTpValorAjusteAdvalorem().getValue()) ){
							adv  = 	adv.add(valor.multiply(gr.getVlAjusteMinimoAdvalorem().divide(new BigDecimal(100),4,RoundingMode.HALF_EVEN))); 
						}else if("T".equals(gr.getTpValorAjusteAdvalorem().getValue()) ){
							adv  = 	adv.add(gr.getVlAjustePadraoAdvalorem()); 
						}
					}else if("D".equals(gr.getTpAjusteAdvalorem().getValue()) ){
						if("P".equals(gr.getTpValorAjusteAdvalorem().getValue()) ){
							adv  = 	adv.subtract(valor.multiply(gr.getVlAjustePadraoAdvalorem().divide(new BigDecimal(100),4,RoundingMode.HALF_EVEN))); 
						}else if("T".equals(gr.getTpValorAjusteAdvalorem().getValue()) ){
							adv  = 	adv.subtract(gr.getVlAjustePadraoAdvalorem()); 
						}
					}
					novoRegistro.put("grupo_regiao",true);
					novoRegistro.put("CD_TARIFA",tarifa);
					novoRegistro.put("VL_FAIXA_PROGRESSIVA",column);
					novoRegistro.put("FRETEPORKG",frete);
					novoRegistro.put("VALOR_FIXO",valor); 
					novoRegistro.put("VL_ADVALOREM",adv);
					novoRegistro.put("ID_ROTA_PRECO",rotaPreco);
					novoRegistro.put("NR_COLUNA_DINAMICA",nrColunaDin);
					novoRegistro.put("ID_TABELA_PRECO",idTabelaPreco); 
					novoRegistro.put("ID_UF_DESTINO",idUnidadeFederativa);
					novoRegistro.put("UF_ORIGEM",ufOrigem);
					novoRegistro.put("UF_DESTINO",ufDestino);
					novoRegistro.put("ORIGEM",mOrigem);
					novoRegistro.put("DESTINO",mDestino);
					novoRegistro.put("CD_MINIMO_PROGRESSIVO",cdMinimoProgressivo);				
					novoRegistro.put("DS_GRUPO_REGIAO_O",grupoRegiaoOrigem);
					novoRegistro.put("DS_GRUPO_REGIAO_D",gr.getDsGrupoRegiao());
					novoRegistro.put("PS_MINIMO",BigDecimalUtils.getBigDecimal(String.valueOf(map2.get("PS_MINIMO"))));
					
					if("Capital".equals(mDestino)){
						
						if(capitalAnte.equals((String) novoRegistro.get("DS_GRUPO_REGIAO_D")) && primeira.equals((BigDecimal) novoRegistro.get("VL_FAIXA_PROGRESSIVA"))){
							break;
						}
						capitalAnte = (String) novoRegistro.get("DS_GRUPO_REGIAO_D");
						capital.add(novoRegistro);
					}else if(mDestino == null || mDestino.contains("Estado")){
						if(estadoAnte.equals((String) novoRegistro.get("DS_GRUPO_REGIAO_D"))&& primeira.equals((BigDecimal) novoRegistro.get("VL_FAIXA_PROGRESSIVA"))){
							break;
						}
						estadoAnte = (String) novoRegistro.get("DS_GRUPO_REGIAO_D");
						todoEstado.add(novoRegistro);
					}else if(!mDestino.contains("Estado")){
						if(filialAnte.equals((String) novoRegistro.get("DS_GRUPO_REGIAO_D"))&& primeira.equals((BigDecimal) novoRegistro.get("VL_FAIXA_PROGRESSIVA"))){
							break;
						}
						filialAnte = (String) novoRegistro.get("DS_GRUPO_REGIAO_D");
						filial.add(novoRegistro);
					}
				}
			}
			if(!capital.isEmpty()){
				gruposRegiao.addAll(capital);					
			}else if(!filial.isEmpty()){
				gruposRegiao.addAll(filial);					
			}else{
				gruposRegiao.addAll(todoEstado);
			}
		}
		return cdMinimoProgressivo;
 	}

	/* Metodo que monta os parameters do relatorio. */
	private void montaParametersReport(Map parameters,Long idTabelaPreco) {		 
		String dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco,getJdbcTemplate());
    	String tipoServico = getTabelasClienteService().getTipoServicoTabela(idTabelaPreco,getJdbcTemplate());
    	Map servico = getTabelasClienteService().getServicoTabela(idTabelaPreco, getJdbcTemplate());

		parameters.put("usuarioEmissor",SessionUtils.getUsuarioLogado().getNmUsuario());
		parameters.put("MOEDA", dsSimbolo);
		parameters.put("SERVICO", tipoServico);
		
		String tpModal = MapUtils.getString(servico, "TP_MODAL");
		parameters.put("MODAL", domainValueService.findDomainValueDescription("DM_MODAL", tpModal));
		parameters.put("ABRANGENCIA", domainValueService.findDomainValueDescription("DM_ABRANGENCIA", MapUtils.getString(servico, "TP_ABRANGENCIA")));

		int[] subReports = {
			TabelasClienteUtil.SUBREPORT_GENERALIDADES_TABELA_PRECO,
			TabelasClienteUtil.SUBREPORT_FORMALIDADES,
			TabelasClienteUtil.SUBREPORT_SERVICOSAD,
			TabelasClienteUtil.SUBREPORT_GENERALIDADE_DIFICULDADE_ENTREGA};
		if(ConstantesExpedicao.MODAL_AEREO.equals(tpModal)) {
			subReports[3] = TabelasClienteUtil.SUBREPORT_LEGENDAS;
		}

		getTabelasClienteService().montaSubReportsTabelaPreco(idTabelaPreco,subReports,configuracoesFacade,getJdbcTemplate(),parameters);
	}

    public String createQuery(Map data){
    	StringBuffer sql = new StringBuffer(); 
    	sql.append(" SELECT TARIFA.CD_TARIFA, ")
	    	.append("       TARIFA.VALOR_FIXO, FRETE.FRETEPORKG, TARIFA.ID_ROTA_PRECO, ")
	    	.append("     	TARIFA.UF_ORIGEM, ")
			.append("     	TARIFA.UF_DESTINO, ")
			.append("       ADVALOREM.VL_ADVALOREM, ")
			.append("       TARIFA.VL_FAIXA_PROGRESSIVA, ")
			.append("       TARIFA.CD_MINIMO_PROGRESSIVO, ")
			.append("       TARIFA.PS_MINIMO, ")
			.append(" 		TARIFA.NR_COLUNA_DINAMICA, ")
			.append("     	TARIFA.ORIGEM, ")
			.append("     	TARIFA.DESTINO, ")
			.append("     	TARIFA.DS_GRUPO_REGIAO_O, ")
			.append("     	TARIFA.DS_GRUPO_REGIAO_D, ")
			.append("     	TARIFA.ID_UF_ORIGEM, ")
			.append("     	TARIFA.ID_UF_DESTINO, ")
			.append("     	TARIFA.ID_TABELA_PRECO, ")
			.append("     	TARIFA.ID_FILIAL_DESTINO, ") 
			.append("     	TARIFA.SG_FILIAL_DESTINO, ")
			.append("     	TARIFA.ID_FILIAL_ORIGEM, ")
			.append("     	TARIFA.SG_FILIAL_ORIGEM, ")
			.append("     	VI18N(TARIFA.DS_TIPO_LOCAL_DESTINO) AS DS_TIPO_LOCAL_DESTINO, ")
			.append("     	VI18N(TARIFA.DS_TIPO_LOCAL_ORIGEM) AS DS_TIPO_LOCAL_ORIGEM ")			
			.append(" FROM  ") 
			.append("      (SELECT TP.CD_TARIFA_PRECO AS CD_TARIFA, TP.ID_TARIFA_PRECO as ID_TARIFA, VFP.VL_FIXO AS VALOR_FIXO,   ") 	
			.append("       	FP.VL_FAIXA_PROGRESSIVA, FP.CD_MINIMO_PROGRESSIVO, RP.ID_ROTA_PRECO, RP.ID_GRUPO_REGIAO_ORIGEM, RP.ID_GRUPO_REGIAO_DESTINO, GRO.DS_GRUPO_REGIAO DS_GRUPO_REGIAO_O ,GRD.DS_GRUPO_REGIAO DS_GRUPO_REGIAO_D, ")
			.append("       	TAB.PS_MINIMO AS PS_MINIMO, uf_origem.id_unidade_federativa ID_UF_ORIGEM , uf_destino.id_unidade_federativa ID_UF_DESTINO, TAB.ID_TABELA_PRECO, ")
			.append("     		UF_ORIGEM.SG_UNIDADE_FEDERATIVA as UF_ORIGEM, ")
			.append("     	    UF_DESTINO.SG_UNIDADE_FEDERATIVA as UF_DESTINO, ")
			.append("			COLUNA_DINAMICA.NR_COLUNA_DINAMICA, ")
			 .append("     F_ORIGEM.ID_FILIAL AS ID_FILIAL_ORIGEM, ") 
			 .append("     F_ORIGEM.SG_FILIAL AS SG_FILIAL_ORIGEM, ") 
			 .append("     TLMC_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I AS DS_TIPO_LOCAL_ORIGEM, ") 
			.append(" 		decode(rp.ID_MUNICIPIO_ORIGEM,null, ")
			.append("				decode(F_ORIGEM.SG_FILIAL,null, ")
			.append("					decode(").append(PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",null,")
			.append("						decode(").append(PropertyVarcharI18nProjection.createProjection("TLMC_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",null,")
			.append("							decode(po_filial.nm_fantasia,null,F_ORIGEM.SG_FILIAL,F_ORIGEM.SG_FILIAL || ' - ' || po_filial.nm_fantasia),")
			.append("							").append(PropertyVarcharI18nProjection.createProjection("TLMC_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append("), ")
			.append("						").append(PropertyVarcharI18nProjection.createProjection("TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I")).append("), ")
			.append("					decode(po_filial.nm_fantasia,null,F_ORIGEM.SG_FILIAL,F_ORIGEM.SG_FILIAL || ' - ' || po_filial.nm_fantasia)),")
			.append("			    M_ORIGEM.NM_MUNICIPIO) as ORIGEM, ")
			.append("     F_DESTINO.ID_FILIAL AS ID_FILIAL_DESTINO, ") 
			.append("     F_DESTINO.SG_FILIAL AS SG_FILIAL_DESTINO, ") 
			.append("     TLMC_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I AS DS_TIPO_LOCAL_DESTINO, ") 
			.append(" 		decode(rp.ID_MUNICIPIO_DESTINO,null, ")
			.append("				decode(F_DESTINO.SG_FILIAL,null, ")
			.append("					decode(").append(PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",null,")
			.append("						decode(").append(PropertyVarcharI18nProjection.createProjection("TLMC_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append(",null,")
			.append("							decode(pd_filial.nm_fantasia,null,F_DESTINO.SG_FILIAL,F_DESTINO.SG_FILIAL || ' - ' || pd_filial.nm_fantasia),")
			.append("							").append(PropertyVarcharI18nProjection.createProjection("TLMC_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append("), ")
			.append("						").append(PropertyVarcharI18nProjection.createProjection("TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I")).append("), ")
			.append("					decode(pd_filial.nm_fantasia,null,F_DESTINO.SG_FILIAL,F_DESTINO.SG_FILIAL || ' - ' || pd_filial.nm_fantasia)),")
			.append("			    M_DESTINO.NM_MUNICIPIO) as DESTINO ")

			.append("       FROM ") 	
			.append("      		 TABELA_PRECO TAB, ") 	
			.append("      		 TABELA_PRECO_PARCELA TPP, ") 	
			.append("      		 PARCELA_PRECO PP, ") 	
			.append("      		 FAIXA_PROGRESSIVA FP, ") 	
			.append("      		 TARIFA_PRECO TP, ") 	
			.append("      		 VALOR_FAIXA_PROGRESSIVA VFP, ") 	
			.append("      		 TARIFA_PRECO_ROTA TPR, ") 	
			.append("     		 MUNICIPIO M_ORIGEM, ")
			.append("     		 MUNICIPIO M_DESTINO, ")
			.append("     		 UNIDADE_FEDERATIVA UF_ORIGEM, ")
			.append("     		 UNIDADE_FEDERATIVA UF_DESTINO, ")
			.append("     		 TIPO_LOCALIZACAO_MUNICIPIO TLM_ORIGEM, ")
			.append("     		 TIPO_LOCALIZACAO_MUNICIPIO TLM_DESTINO, ")
			.append("     		 TIPO_LOCALIZACAO_MUNICIPIO TLMC_ORIGEM, ")
			.append("     		 TIPO_LOCALIZACAO_MUNICIPIO TLMC_DESTINO, ")
			.append("     		 FILIAL F_ORIGEM, ")
			.append("     		 FILIAL F_DESTINO, ")
			.append(" 			 PESSOA po_filial, ")
			.append(" 			 PESSOA pd_filial, ")		
			.append("      		 ROTA_PRECO RP, ")
			.append("      		 GRUPO_REGIAO GRO, ")
			.append("      		 GRUPO_REGIAO GRD, ")
			.append("(select TABCOLDIN.*, rownum as nr_coluna_dinamica ")
			.append(" from (select tp.id_tabela_preco, fp.id_faixa_progressiva, fp.vl_faixa_progressiva, ")
			.append(" fp.cd_minimo_progressivo, fp.id_tabela_preco_parcela ")
			.append(" from faixa_progressiva fp, ")
			.append(" tabela_preco_parcela tpp, tabela_preco tp, parcela_preco pp ")
			.append(" where  fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela  ")
			.append(" and tpp.id_parcela_preco = pp.id_parcela_preco ")
			.append(" and pp.CD_PARCELA_PRECO = 'IDFretePeso' ")
			.append(" and tpp.id_tabela_preco = tp.id_tabela_preco  and tp.id_tabela_preco = ? ")
			.append(" order by fp.vl_faixa_progressiva) TABCOLDIN) COLUNA_DINAMICA ")

			.append("       WHERE   ")
			.append("      		 TAB.ID_TABELA_PRECO = ? ");
    	/** Valida IDs */
    	if(LongUtils.hasValue(MapUtils.getLong(data, "idUnidadeFederativaDestino"))) {
    		sql.append("			 AND RP.ID_UF_DESTINO = ? ");
    	}
    	if(LongUtils.hasValue(MapUtils.getLong(data, "idTipoLocalizacaoMunicipioDestino"))) {
    		sql.append("			 AND RP.ID_TIPO_LOCAL_COMERCIAL_DESTIN = ? ");
    	}
    	if(LongUtils.hasValue(MapUtils.getLong(data, "idUnidadeFederativaOrigem"))) {
    		sql.append("			 AND RP.ID_UF_ORIGEM = ? ");
    	}
    	if(LongUtils.hasValue(MapUtils.getLong(data, "idTipoLocalizacaoMunicipioOrigem"))) {
    		sql.append("			 AND RP.ID_TIPO_LOCAL_COMERCIAL_ORIGEM = ? ");
    	}
    	sql.append("      		 AND TPP.ID_TABELA_PRECO = TAB.ID_TABELA_PRECO ") 	
			.append("      		 AND TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ") 	
			.append("      		 AND PP.CD_PARCELA_PRECO = 'IDFretePeso' ")
			.append("      		 AND PP.TP_PARCELA_PRECO = 'P' ")
			.append("      		 AND PP.TP_PRECIFICACAO = 'M' ")

			.append("			 AND COLUNA_DINAMICA.id_faixa_progressiva = fp.id_faixa_progressiva ")
			.append("			 AND COLUNA_DINAMICA.id_tabela_preco_parcela = TPP.ID_TABELA_PRECO_PARCELA ")
			.append(" 			 AND  VFP.ID_FAIXA_PROGRESSIVA = FP.ID_FAIXA_PROGRESSIVA  ")
			.append(" 			 AND  TPP.ID_TABELA_PRECO_PARCELA = FP.ID_TABELA_PRECO_PARCELA  ")
			.append(" 			 AND  VFP.ID_TARIFA_PRECO = TP.ID_TARIFA_PRECO  ")
			.append(" 			 AND  TP.ID_TARIFA_PRECO = TPR.ID_TARIFA_PRECO  ")
			.append(" 			 AND  TPR.ID_ROTA_PRECO = RP.ID_ROTA_PRECO  ")
			.append("			 AND  TPR.ID_TABELA_PRECO = TAB.ID_TABELA_PRECO  ")
			.append("			 AND  RP.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA  ")
			.append("			 AND  RP.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA  ")
			.append("     		 AND  RP.ID_MUNICIPIO_ORIGEM = M_ORIGEM.ID_MUNICIPIO (+) ")
			.append("     		 AND  RP.ID_MUNICIPIO_DESTINO = M_DESTINO.ID_MUNICIPIO (+) ")
			.append("     		 AND  RP.ID_FILIAL_ORIGEM = F_ORIGEM.ID_FILIAL (+) ")
			.append("     		 AND  RP.ID_FILIAL_DESTINO = F_DESTINO.ID_FILIAL (+) ")
			.append(" 			 AND  RP.ID_FILIAL_ORIGEM = po_FILIAL.ID_Pessoa (+) ")
			.append(" 			 AND  RP.ID_FILIAL_DESTINO = pd_FILIAL.ID_Pessoa (+) ")		
			.append("     		 AND  RP.ID_TIPO_LOCALIZACAO_ORIGEM = TLM_ORIGEM.ID_TIPO_LOCALIZACAO_MUNICIPIO (+) ")
			.append("     		 AND  RP.ID_TIPO_LOCALIZACAO_DESTINO = TLM_DESTINO.ID_TIPO_LOCALIZACAO_MUNICIPIO (+) ")
			.append("     		 AND  RP.ID_TIPO_LOCAL_COMERCIAL_ORIGEM = TLMC_ORIGEM.ID_TIPO_LOCALIZACAO_MUNICIPIO (+) ")
			.append("     		 AND  RP.ID_TIPO_LOCAL_COMERCIAL_DESTIN = TLMC_DESTINO.ID_TIPO_LOCALIZACAO_MUNICIPIO (+)" )
			.append("			 AND RP.ID_GRUPO_REGIAO_ORIGEM = GRO.ID_GRUPO_REGIAO(+)")
			.append("			 AND RP.ID_GRUPO_REGIAO_DESTINO = GRD.ID_GRUPO_REGIAO (+)) TARIFA,  ")
			.append("      (SELECT  PF.VL_PRECO_FRETE as FRETEPORKG, TP.ID_TARIFA_PRECO AS ID_FRETE   ") 	
			.append("       FROM   ")
			.append("      		 TABELA_PRECO TAB,   ") 
			.append("      		 TABELA_PRECO_PARCELA TPP,   ") 	
			.append("      		 PARCELA_PRECO PP,   ") 	
			.append("  			 PRECO_FRETE PF,  ")
			.append("      		 TARIFA_PRECO TP   ") 	
			.append("       WHERE   ")
			.append("      		 TAB.ID_TABELA_PRECO = ?   ") 	
			.append("      		 AND TPP.ID_TABELA_PRECO = TAB.ID_TABELA_PRECO   ") 	
			.append("      		 AND  TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO   ") 	
			.append("      		 AND  PP.CD_PARCELA_PRECO = 'IDFreteQuilo'   ") 	
			.append("      		 AND  PP.TP_PARCELA_PRECO = 'P'   ") 	
			.append("      		 AND  PP.TP_PRECIFICACAO = 'P'   ")
			.append(" 			 AND  TPP.ID_TABELA_PRECO_PARCELA = PF.ID_TABELA_PRECO_PARCELA  ")
			.append(" 			 AND  Pf.ID_TARIFA_PRECO = TP.ID_TARIFA_PRECO) FRETE ,  ")
			.append("      (SELECT  PF.VL_PRECO_FRETE as VL_ADVALOREM, TP.ID_TARIFA_PRECO AS ID_ADVALOREM   ") 	
			.append("       FROM   ")
			.append("             TABELA_PRECO TAB, ")	
			.append("      		 TABELA_PRECO_PARCELA TPP,  ") 	
			.append("      		 PARCELA_PRECO PP,  ") 	
			.append("  			 PRECO_FRETE PF,  ")
			.append("      		 TARIFA_PRECO TP   ") 	
			.append("       WHERE   ") 	
			.append("      		 TAB.ID_TABELA_PRECO = ?   ") 	
			.append("      		 AND TPP.ID_TABELA_PRECO = TAB.ID_TABELA_PRECO   ") 	
			.append("      		 AND  TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO   ") 	
			.append("      		 AND  PP.CD_PARCELA_PRECO = 'IDAdvalorem'   ") 	
			.append("      		 AND  PP.TP_PARCELA_PRECO = 'P'  ") 	
			.append("      		 AND  PP.TP_PRECIFICACAO = 'P'   ")
			.append(" 			 AND  TPP.ID_TABELA_PRECO_PARCELA = PF.ID_TABELA_PRECO_PARCELA  ")
			.append(" 			 AND  Pf.ID_TARIFA_PRECO = TP.ID_TARIFA_PRECO) ADVALOREM   ")
			.append(" WHERE  ")
			.append("       FRETE.ID_FRETE = ADVALOREM.ID_ADVALOREM  ")
			.append("       AND  TARIFA.ID_TARIFA = FRETE.ID_FRETE   ") 	
			.append(" ORDER BY  TARIFA.CD_TARIFA, TARIFA.ID_ROTA_PRECO, TARIFA.VL_FAIXA_PROGRESSIVA, TARIFA.ORIGEM, TARIFA.DESTINO  ");

    	return sql.toString();
    }

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
}

	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public OperacaoServicoLocalizaService getOperacaoServicoLocalizaService() {
		return operacaoServicoLocalizaService;
	}

	public void setOperacaoServicoLocalizaService(
			OperacaoServicoLocalizaService operacaoServicoLocalizaService) {
		this.operacaoServicoLocalizaService = operacaoServicoLocalizaService;
	}

	public MunicipioFilialService getMunicipioFilialService() {
		return municipioFilialService;
	}

	public void setMunicipioFilialService(
			MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	public GrupoRegiaoService getGrupoRegiaoService() {
		return grupoRegiaoService;
}

	public void setGrupoRegiaoService(GrupoRegiaoService grupoRegiaoService) {
		this.grupoRegiaoService = grupoRegiaoService;
	}


}
