package com.mercurio.lms.tabelaprecos.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.map.ListOrderedMap;

import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;
import com.mercurio.lms.vendas.util.TabelasClienteUtil;

/**
 * 
 * @spring.bean id="lms.tabelaprecos.report.emitirTabelaPropostaFOBService"
 * @spring.property name="reportName" value="com/mercurio/lms/tabelaprecos/report/emitirTabelaPropostaFOB.vm"
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="35"
 * @spring.property name="crossTabBandWidths" value="439"
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class EmitirTabelaPropostaFOBService extends ReportServiceSupport {
	
	private static final String NOME_TABELA = "TMP_E_DIFERENCIADAS";
	
    private ConfiguracoesFacade configuracoesFacade;
    private DomainValueService  domainValueService;
	private TabelasClienteService tabelasClienteService;

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}	
	
	@Override
	public JRReportDataObject execute(Map params) throws Exception {
		
		Integer nrcolumns = 0;
		
		/*Obtem a tabela preco*/
		Long idTabelaPreco = MapUtils.getLong(params,"tabelaPreco.idTabelaPreco");
		if(idTabelaPreco == null){
			return emptyReport();
		}
		
		/*Remover os dados da tabela temporária*/
		getJdbcTemplate().execute("DELETE FROM "+ NOME_TABELA);
		
		/*Obtem o simbolo da moeda utilizada*/
		String dsSimbolo = TabelasClienteUtil.getMoeda(idTabelaPreco, getJdbcTemplate());
		params.put("DS_SIMBOLO", dsSimbolo);
		
		/*Lista com todas as faixas*/
		Long idPropostaFOB = MapUtils.getLong(params,"idPropostaFOB");
		List<ListOrderedMap> result = getJdbcTemplate().queryForList(getSQL(idPropostaFOB).toString());
		if(CollectionUtils.isNotEmpty(result)){
			
			Long psMin = getMaxFaixaProgressiva(idTabelaPreco,false);
			params.put("PS_MINIMO", new BigDecimal(psMin));
			
			List<TabelaFOB> list = new ArrayList<TabelaFOB>();
			for(ListOrderedMap map : result){
				
				list.add( new TabelaFOB(
							MapUtils.getString(map, "DS_SIMBOLO"), 
							MapUtils.getString(map, "SG_FILIAL_ORIGEM"), 
							MapUtils.getString(map, "NM_MUNICIPIO_ORIGEM"), 
							MapUtils.getString(map, "NM_MUNICIPIO_DESTINO"),
							MapUtilsPlus.getBigDecimal(map, "VL_FAIXA_PROGRESSIVA"),
							MapUtilsPlus.getBigDecimal(map, "VL_FATOR_MULTIPLICACAO"),
							MapUtilsPlus.getBigDecimal(map, "VL_FIXO"),
							MapUtilsPlus.getBigDecimal(map, "VL_PESO_MINIMO")
						) ); 
				
			}/*for*/
			
			nrcolumns = executeInsertReportRecords(list);
		}
		
		/*Numero de colunas*/
		params.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[]{nrcolumns});
		
		/*Parâmetros do relatório - Sub reports*/
		parametersReport(params,idTabelaPreco);
		
		/*Gera o relatório*/
		JRReportDataObject jr = executeQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY TARIFA, ORIGEM, DESTINO ",params);
		jr.setParameters(params);
		
		return jr;				
	}	
	
	/**
	 * Parametros do relatorio
	 * 
	 * @param parameters
	 * @param idTabelaPreco
	 */
	private void parametersReport(Map parameters,Long idTabelaPreco) {
		
		Map servico = getTabelasClienteService().getServicoTabela(idTabelaPreco, getJdbcTemplate());
		
		String dsSimbolo   = getTabelasClienteService().getMoeda(idTabelaPreco,getJdbcTemplate());
    	String tipoServico = getTabelasClienteService().getTipoServicoTabela(idTabelaPreco,getJdbcTemplate());
    	
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
			-1};
		if(ConstantesExpedicao.MODAL_AEREO.equals(tpModal)) {
			subReports[3] = TabelasClienteUtil.SUBREPORT_LEGENDAS;
		}

		getTabelasClienteService().montaSubReportsTabelaPreco(idTabelaPreco,subReports,configuracoesFacade,getJdbcTemplate(),parameters);
		
		int totRegistros = getJdbcTemplate().queryForInt("Select COUNT(*) from " + NOME_TABELA);
		parameters.put("TOTAL", Integer.valueOf(totRegistros));
		
		getTabelasClienteService().setEspacoQuebra(31,3,totRegistros,0,8,parameters);
	}	
	
	
	/**
	 * Insere os registros na tabela temporária
	 * 
	 */
	public Integer executeInsertReportRecords(List<TabelaFOB> list){
		
		Integer nrColumns = 0; 
		
		TabelaFOB temp = null;
		for (final TabelaFOB tabelaFOB : list) {
			
			if(tabelaFOB.equals(temp)){
				continue;
			}
			
			Collection<TabelaFOB> filter = CollectionUtils.select(list, new Predicate() {
				
				public boolean evaluate(Object object) {
					TabelaFOB tabela = (TabelaFOB)object;  
					return tabelaFOB.equals(tabela);
				}
			}); 
			
			/*Insere os registros na tabela temporária*/
			nrColumns = executeInsert(filter);
			
			temp = tabelaFOB;
			
		}/*for*/
		
		return nrColumns;
	}
	
	/**
	 * Gera o SQL para inserção na tabela TMP
	 * 
	 */
	public Integer executeInsert(Collection<TabelaFOB> list){
		
		int nrColumn = 1;	
		
		StringBuilder sqlParam = new StringBuilder("?,?,?,?");
		StringBuilder sql      = new StringBuilder("INSERT INTO TMP_E_DIFERENCIADAS (TARIFA,FRETEPORKG,ORIGEM,DESTINO");
		
		List parameters = null;
		if(CollectionUtils.isNotEmpty(list)){
			for (TabelaFOB tabelaFOB : list) {
				
				if(parameters == null){
					parameters = new ArrayList();
					parameters.add("CD");
					parameters.add(tabelaFOB.getVlFaixaProgressiva());
					parameters.add(tabelaFOB.getNmMunicipioOrigem());
					parameters.add(tabelaFOB.getNmMunicipioDestino());
				}
				
				parameters.add(tabelaFOB.getVlFixo());
				
				sql.append(",COLUMN").append(nrColumn);	
				sqlParam.append(",?");
				
				nrColumn++;
				
			}/*for*/
			
			sql.append(") VALUES (").append(sqlParam).append(")");
			
			/*Insere o registro na tabela tmp*/
			getJdbcTemplate().update(sql.toString(), parameters.toArray());
			
		}
		
		return nrColumn;
	}
	
	/**
	 * Obtem o valor maximo da faixa progressiva
	 * 
	 * @param idTabelaPreco
	 * @param emitirTonelada
	 * @param jdbcTemplate
	 * @return Long
	 */
	private Long getMaxFaixaProgressiva(Long idTabelaPreco, Boolean emitirTonelada){
    	String sql = "select max(FAIXA_PROGRESSIVA) from " + getSubQueryFaixa(emitirTonelada);
    	return getJdbcTemplate().queryForLong(sql, new Long[]{idTabelaPreco});
    }
	
	
    /**
     * Retorna sql da subquery de faixas do relatorio
     * @return
     */
    private String getSubQueryFaixa(Boolean emitirTonelada){
    	
    	StringBuffer sql = new StringBuffer();
		sql.append("(select  F.VL_FAIXA_PROGRESSIVA AS FAIXA_PROGRESSIVA, ") 
		.append(" vfp.NR_FATOR_MULTIPLICACAO AS FATOR_MULTIPLICACAO, ")
		.append(" vfp.PC_DESCONTO, ")
		.append(" TAB.PS_MINIMO AS PS_MINIMO,  ")
		.append(" MO.DS_SIMBOLO AS DS_SIMBOLO ")
		.append(" from    TABELA_PRECO TAB, "  )
		.append(" TABELA_PRECO_PARCELA T, " )
		.append(" PARCELA_PRECO P, ")
		.append(" FAIXA_PROGRESSIVA F,  ")
		.append(" VALOR_FAIXA_PROGRESSIVA VFP, ")
		.append(" MOEDA MO ")
		.append(" where T.ID_TABELA_PRECO = ? ")
		.append(" AND  TAB.ID_TABELA_PRECO = T.ID_TABELA_PRECO  ")
		.append(" AND  T.ID_PARCELA_PRECO = P.ID_PARCELA_PRECO ")
		.append(" AND  P.TP_PARCELA_PRECO = 'P'  ")
		.append(" AND  P.TP_PRECIFICACAO = 'M' ")
		.append(" AND  VFP.ID_FAIXA_PROGRESSIVA = F.ID_FAIXA_PROGRESSIVA ")
		.append(" AND TAB.ID_MOEDA = MO.ID_MOEDA ");
		
		if(!emitirTonelada.booleanValue())
		{
			sql.append(" AND F.VL_FAIXA_PROGRESSIVA <= TAB.PS_MINIMO");
		}
			
		sql.append(" AND  T.ID_TABELA_PRECO_PARCELA = F.ID_TABELA_PRECO_PARCELA) FAIXA ");
		
		return sql.toString();
    }	
	
	/**
	 * Retorna um JRReportDataObject vazio pois nao tera dados na tabela temporaria
	 * @param parametersReport
	 * @return
	 */
	private JRReportDataObject emptyReport(){
		JRReportDataObject jr = executeQuery("SELECT * FROM " + NOME_TABELA, new HashMap());
		Map map = new HashMap();
		map.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[] {Integer.valueOf(0)});
		jr.setParameters(map);
		return jr;
	}	
	
	/**
	 * SQL responsável por gerar os dados através da proposta FOB
	 * 
	 * @return StringBuilder
	 */
	private StringBuilder getSQL(Long idPropostaFOB){

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		sql.append(" FP.ID_FAIXA_PROGRESSIVA, ");
		sql.append(" MU.ID_UNIDADE_FEDERATIVA AS ID_UF_ORIGEM, "); 
		sql.append(" MD.ID_UNIDADE_FEDERATIVA AS ID_UF_DESTINO, ");
		sql.append(" FL.ID_FILIAL AS ID_FILIAL_ORIGEM, ");
		sql.append(" FL.SG_FILIAL AS SG_FILIAL_ORIGEM, ");
		sql.append(" MU.NM_MUNICIPIO AS NM_MUNICIPIO_ORIGEM, ");
		sql.append(" MD.NM_MUNICIPIO AS NM_MUNICIPIO_DESTINO, ");
		sql.append(" FP.VL_FAIXA_PROGRESSIVA    AS VL_FAIXA_PROGRESSIVA, "); 
		sql.append(" VFP.NR_FATOR_MULTIPLICACAO AS FATOR_MULTIPLICACAO, ");
		sql.append(" TP.PS_MINIMO  AS PS_MINIMO,  ");
		sql.append(" MO.DS_SIMBOLO AS DS_SIMBOLO, ");
		sql.append(" VFP.VL_FIXO   AS VL_FAIXA ");
		sql.append(" FROM  ");
		sql.append(" PROPOSTA_FOB PF, "); 
		sql.append(" ORIGEM_PROPOSTA_FOB OP, "); 
		sql.append(" TABELA_PRECO_PARCELA TPP,  ");
		sql.append(" TABELA_PRECO TP,  ");
		sql.append(" PARCELA_PRECO PP,  ");
		sql.append(" FAIXA_PROGRESSIVA FP,  ");
		sql.append(" VALOR_FAIXA_PROGRESSIVA VFP, "); 
		sql.append(" MOEDA MO, ");
		sql.append(" PESSOA PS, ");
		sql.append(" ENDERECO_PESSOA EP, ");
		sql.append(" MUNICIPIO MU,  ");
		sql.append(" MUNICIPIO MD, ");
		sql.append(" FILIAL FL  ");
		sql.append(" WHERE  ");
		sql.append(" PF.ID_PROPOSTA_FOB = OP.ID_PROPOSTA_FOB "); 
		sql.append(" AND   OP.ID_FILIAL = FL.ID_FILIAL ");
		sql.append(" AND   FL.ID_FILIAL = PS.ID_PESSOA ");
		sql.append(" AND   PS.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA ");
		sql.append(" AND   EP.ID_MUNICIPIO = MU.ID_MUNICIPIO ");
		sql.append(" AND   PF.MUNI_ID_MUNICIPIO = MD.ID_MUNICIPIO ");
		sql.append(" AND   OP.ID_PROPOSTA_FOB = ").append(idPropostaFOB);
		sql.append(" AND   PF.TBPR_ID_TABELA_PRECO = TPP.ID_TABELA_PRECO "); 
		sql.append(" AND   TPP.ID_TABELA_PRECO = TP.ID_TABELA_PRECO  ");
		sql.append(" AND   TP.ID_MOEDA = MO.ID_MOEDA  ");
		sql.append(" AND   TPP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ");
		sql.append(" AND   TPP.ID_TABELA_PRECO_PARCELA = FP.ID_TABELA_PRECO_PARCELA ");
		sql.append(" AND   FP.ID_FAIXA_PROGRESSIVA = VFP.ID_FAIXA_PROGRESSIVA ");
		sql.append(" ORDER BY  MU.NM_MUNICIPIO, FP.VL_FAIXA_PROGRESSIVA "); 		
		
		return sql;
	}
	
	class TabelaFOB{
		
		private String dsSimbolo;
		private String sgFilialOrigem;
		private String nmMunicipioOrigem;
		private String nmMunicipioDestino;
		private BigDecimal vlFaixaProgressiva;
		private BigDecimal vlFatorMultiplicacao;
		private BigDecimal vlFixo;
		private BigDecimal vlPesoMunimo;
		
		public TabelaFOB() {
			
		}
		
		public TabelaFOB(String dsSimbolo, String sgFilialOrigem,
				String nmMunicipioOrigem, String nmMunicipioDestino,
				BigDecimal vlFaixaProgressiva, BigDecimal vlFatorMultiplicacao,
				BigDecimal vlFixo, BigDecimal vlPesoMunimo) {
			super();
			this.dsSimbolo = dsSimbolo;
			this.sgFilialOrigem = sgFilialOrigem;
			this.nmMunicipioOrigem = nmMunicipioOrigem;
			this.nmMunicipioDestino = nmMunicipioDestino;
			this.vlFaixaProgressiva = vlFaixaProgressiva;
			this.vlFatorMultiplicacao = vlFatorMultiplicacao;
			this.vlFixo = vlFixo;
			this.vlPesoMunimo = vlPesoMunimo;
		}

	
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime
					* result
					+ ((nmMunicipioOrigem == null) ? 0 : nmMunicipioOrigem
							.hashCode());
			result = prime
					* result
					+ ((sgFilialOrigem == null) ? 0 : sgFilialOrigem.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TabelaFOB other = (TabelaFOB) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (nmMunicipioOrigem == null) {
				if (other.nmMunicipioOrigem != null)
					return false;
			} else if (!nmMunicipioOrigem.equals(other.nmMunicipioOrigem))
				return false;
			if (sgFilialOrigem == null) {
				if (other.sgFilialOrigem != null)
					return false;
			} else if (!sgFilialOrigem.equals(other.sgFilialOrigem))
				return false;
			return true;
		}

		public String getDsSimbolo() {
			return dsSimbolo;
		}
		public void setDsSimbolo(String dsSimbolo) {
			this.dsSimbolo = dsSimbolo;
		}
		public String getSgFilialOrigem() {
			return sgFilialOrigem;
		}
		public void setSgFilialOrigem(String sgFilialOrigem) {
			this.sgFilialOrigem = sgFilialOrigem;
		}
		public String getNmMunicipioOrigem() {
			return nmMunicipioOrigem;
		}
		public void setNmMunicipioOrigem(String nmMunicipioOrigem) {
			this.nmMunicipioOrigem = nmMunicipioOrigem;
		}
		public String getNmMunicipioDestino() {
			return nmMunicipioDestino;
		}
		public void setNmMunicipioDestino(String nmMunicipioDestino) {
			this.nmMunicipioDestino = nmMunicipioDestino;
		}
		public BigDecimal getVlFaixaProgressiva() {
			return vlFaixaProgressiva;
		}
		public void setVlFaixaProgressiva(BigDecimal vlFaixaProgressiva) {
			this.vlFaixaProgressiva = vlFaixaProgressiva;
		}
		public BigDecimal getVlFatorMultiplicacao() {
			return vlFatorMultiplicacao;
		}
		public void setVlFatorMultiplicacao(BigDecimal vlFatorMultiplicacao) {
			this.vlFatorMultiplicacao = vlFatorMultiplicacao;
		}
		public BigDecimal getVlFixo() {
			return vlFixo;
		}
		public void setVlFixo(BigDecimal vlFixo) {
			this.vlFixo = vlFixo;
		}
		public BigDecimal getVlPesoMunimo() {
			return vlPesoMunimo;
		}
		public void setVlPesoMunimo(BigDecimal vlPesoMunimo) {
			this.vlPesoMunimo = vlPesoMunimo;
		}

		private EmitirTabelaPropostaFOBService getOuterType() {
			return EmitirTabelaPropostaFOBService.this;
		}
		
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
}
