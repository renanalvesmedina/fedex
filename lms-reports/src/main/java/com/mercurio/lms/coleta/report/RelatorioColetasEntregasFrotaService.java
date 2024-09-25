package com.mercurio.lms.coleta.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.coleta.model.dao.RelatorioColetasEntregasFrotaDAO;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.ArrayUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * @author luisfco 
 *
 * @spring.bean id="lms.coleta.relatorioColetasEntregasFrotaService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/emitirColetasEntregasFrota.jasper"
 */
public class RelatorioColetasEntregasFrotaService extends ReportServiceSupport{

	
	private FilialService filialService;
	private ControleCargaService controleCargaService;
	private MeioTransporteService meioTransporteService;
	private RelatorioColetasEntregasFrotaDAO relatorioColetasEntregasFrotaDAO;
	private ConfiguracoesFacade configuracoesFacade;
	
	private static final Integer ENTREGA_REALIZADA = 21;
	private static final Integer ENTREGA_AEROPORTO = 85;

	
	private Map getCompiledQueryAndCriteriaObject(TypedFlatMap tfm, boolean blColeta, boolean blEntrega, String stringColeta, String stringEntrega, String stringEntregaAeroporto) {

		String query = null;
		Object[] criteria = null;
		SqlTemplate sqlColeta = makeReportQueryColeta(tfm, true, stringColeta);
		SqlTemplate sqlEntrega = makeReportQueryEntrega(tfm, true, stringEntrega, stringEntregaAeroporto);

		// se coleta e entrega, então faz union de ambas        	
        if (blColeta && blEntrega) {
        	query = sqlColeta.getSql() + "\n UNION \n" + sqlEntrega.getSql();
        	criteria = ArrayUtils.joinArrays(sqlColeta.getCriteria(), sqlColeta.getCriteria().length, sqlEntrega.getCriteria(), sqlEntrega.getCriteria().length);
        	
        // se coleta ou entrega
        } else if (blColeta || blEntrega) { 
	        if (blColeta) {
	        	query = sqlColeta.getSql();
	        	criteria = sqlColeta.getCriteria();
	        } else if (blEntrega) {
	        	query = sqlEntrega.getSql();
	        	criteria = sqlEntrega.getCriteria();
	        }

	    // senao, retorna null
        } else { 
        	return null;
        }
        
        Map map = new HashMap(2);
        map.put("query", query);
        map.put("criteria", criteria);
        return map;
	}
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap typedFlatMap = (TypedFlatMap) parameters;
		
		executeRegrasEmissaoConsulta(typedFlatMap);
		
		JRReportDataObject jr = null;
		String query = null;
        Object[] criteria = null;
        
		boolean blColeta = typedFlatMap.getBoolean("blColeta").booleanValue();
		boolean blEntrega = typedFlatMap.getBoolean("blEntrega").booleanValue();
		String stringColeta = typedFlatMap.getString("stringColeta");
		String stringEntrega = typedFlatMap.getString("stringEntrega");
		String stringEntregaAeroporto = typedFlatMap.getString("stringEntregaAeroporto");

        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa", getFilterSummary(typedFlatMap, true));
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());

        Map compiledQueryAndCriteria = getCompiledQueryAndCriteriaObject(typedFlatMap, blColeta, blEntrega, stringColeta, stringEntrega, stringEntregaAeroporto);
        if (compiledQueryAndCriteria == null) {
			JRDataSource ds = new JREmptyDataSource(0);
			return createReportDataObject(ds, parametersReport);
        }
        
        query = (String)compiledQueryAndCriteria.get("query");
        criteria = (Object[])compiledQueryAndCriteria.get("criteria");
        
        query += " " + getOrderByClause();
        jr = executeQuery(query,criteria);
        jr.setParameters(parametersReport);
        return jr; 
	}
	

	/**
	 * Qyery de coleta.
	 * @param typedFlatMap
	 * @param allowProjection
	 * @return
	 * @throws Exception
	 */
    private SqlTemplate makeReportQueryColeta(TypedFlatMap typedFlatMap, boolean allowProjection, String stringColeta) {
  	  
        SqlTemplate s = createSqlTemplate();
        
        if (allowProjection) {
	        s.addProjection("FILIAL_CC.SG_FILIAL", "FILIAL_CONTROLE_CARGA");
	        s.addProjection("CC.NR_CONTROLE_CARGA");
	        s.addProjection("'" + stringColeta + "'", "PROCESSO");
	        s.addProjection("1", "ORDEM_PROCESSO");
	        s.addProjection("FILIAL_PC.SG_FILIAL", "FILIAL_PEDIDO_COLETA");
	        s.addProjection("PC.NR_COLETA");
	        s.addProjection("DS.TP_DOCUMENTO_SERVICO", "TP_DOCUMENTO_SERVICO");
	        s.addProjection("DS.NR_DOCTO_SERVICO", "NR_DOCUMENTO_SERVICO");
	        s.addProjection("FILIAL_DS.SG_FILIAL", "FILIAL_DOCUMENTO_SERVICO");
	        s.addProjection("PC.DH_PEDIDO_COLETA", "DH_EMISSAO");
	        s.addProjection("FILIAL_MC.SG_FILIAL", "FILIAL_MANIFESTO");
	        s.addProjection("MC.NR_MANIFESTO");
	        s.addProjection("PESSOA_CLIENTE.NM_PESSOA", "CLIENTE_DESTINATARIO");
	        s.addProjection("DC.QT_VOLUMES", "QT_VOLUMES");
	        s.addProjection("DC.PS_MERCADORIA", "PESO");
	        s.addProjection("MO.SG_MOEDA || ' ' || MO.DS_SIMBOLO", "MOEDA");
	        
	        s.addProjection("DC.VL_MERCADORIA", "VALOR");
	        
	        String tpStatus = new StringBuffer()
	        	.append(" (SELECT ").append(PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I"))
	        	.append("    FROM DOMINIO D, VALOR_DOMINIO VD")
	        	.append("   WHERE VD.ID_DOMINIO = D.ID_DOMINIO")
	        	.append("     AND D.NM_DOMINIO = ?")
	        	.append("     AND VD.VL_VALOR_DOMINIO = PC.TP_STATUS_COLETA)").toString();
	      s.addProjection(tpStatus, "TP_STATUS");
	      s.addCriteriaValue("DM_STATUS_COLETA");
	      
	      String qtRetornos = new StringBuffer() 
	        	.append("(SELECT COUNT(*) ")
	        	.append("FROM EVENTO_COLETA EC ")
	        	.append("WHERE EC.TP_EVENTO_COLETA = ? ")
	        	.append("AND EC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA) ").toString();
	      s.addProjection(qtRetornos, "QT_RETORNOS");
	      s.addCriteriaValue("RC");
        }

        s.addFrom("CONTROLE_CARGA","CC");
        s.addFrom("FILIAL","FILIAL_CC");
        s.addFrom("MANIFESTO_COLETA","MC");
        s.addFrom("FILIAL","FILIAL_MC");
        s.addFrom("PEDIDO_COLETA","PC");
        s.addFrom("CLIENTE", "CLI");
        s.addFrom("PESSOA", "PESSOA_CLIENTE");
        s.addFrom("FILIAL","FILIAL_PC");
        s.addFrom("MOEDA", "MO");
        s.addFrom("DETALHE_COLETA", "DC");
        s.addFrom("DOCTO_SERVICO","DS");
        s.addFrom("FILIAL","FILIAL_DS");
        s.addJoin("CC.ID_FILIAL_ORIGEM", "FILIAL_CC.ID_FILIAL");
        s.addJoin("MC.ID_CONTROLE_CARGA", "CC.ID_CONTROLE_CARGA");
        s.addJoin("MC.ID_FILIAL_ORIGEM", "FILIAL_MC.ID_FILIAL");
        s.addJoin("PC.ID_MANIFESTO_COLETA", "MC.ID_MANIFESTO_COLETA");
        s.addJoin("PC.ID_CLIENTE", "CLI.ID_CLIENTE");
        s.addJoin("CLI.ID_CLIENTE", "PESSOA_CLIENTE.ID_PESSOA");
        s.addJoin("FILIAL_PC.ID_FILIAL", "PC.ID_FILIAL_RESPONSAVEL");
        s.addJoin("PC.ID_MOEDA", "MO.ID_MOEDA");
        s.addJoin("PC.ID_PEDIDO_COLETA","DC.ID_PEDIDO_COLETA");
        s.addJoin("DC.ID_DOCTO_SERVICO", "DS.ID_DOCTO_SERVICO(+)");
        s.addJoin("DS.ID_FILIAL_ORIGEM", "FILIAL_DS.ID_FILIAL(+)");
        
        if (typedFlatMap.getLong("meioTransporteRodoviario.idMeioTransporte") != null) {
        	s.addCriteria("CC.ID_TRANSPORTADO", "=", typedFlatMap.getLong("meioTransporteRodoviario.idMeioTransporte"));
        }
        
        if (typedFlatMap.getLong("controleCarga.idControleCarga") != null) {
        	s.addCriteria("CC.ID_CONTROLE_CARGA", "=", typedFlatMap.getLong("controleCarga.idControleCarga"));
        }
        
        if(StringUtils.isNotBlank(typedFlatMap.get("servico.idServico").toString())){
        	Long idServico = Long.valueOf(typedFlatMap.get("servico.idServico").toString());
    		s.addCriteria("DC.ID_SERVICO", "=", idServico);
    	}
    	
        String tpPedidoColeta = String.valueOf(typedFlatMap.get("tpPedidoColeta"));
    	if(StringUtils.isNotBlank(tpPedidoColeta)){
    		s.addCriteria("PC.TP_PEDIDO_COLETA", "=", tpPedidoColeta);
    	}

        s.addCriteria("CC.TP_CONTROLE_CARGA", "=", "C");
        s.addCriteria("trunc (CAST ( CC.DH_SAIDA_COLETA_ENTREGA AS DATE))", "=", typedFlatMap.getYearMonthDay("dtConsulta"));
        
        if (Boolean.TRUE.equals(typedFlatMap.getBoolean("blSomenteRealizados"))) {
        	String somenteRealizados = new StringBuffer()
        		.append("EXISTS (SELECT 1 ") 
	    				.append(" FROM EVENTO_COLETA EC2 ")
	    				.append("WHERE EC2.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA ")
	    				.append("  AND EC2.TP_EVENTO_COLETA = ?)").toString();
        	s.addCustomCriteria(somenteRealizados, "EX");
        }
        
      return s;        
    }

    /**
     * Query de entrega.
     * @param typedFlatMap
     * @param allowProjection 
     * @return
     * @throws Exception
     */
    private SqlTemplate makeReportQueryEntrega(TypedFlatMap typedFlatMap, boolean allowProjection, String stringEntrega, String stringEntregaAeroporto) {
  	  
        SqlTemplate s = createSqlTemplate();
        
        if (allowProjection) {
	        s.addProjection("FILIAL_CC.SG_FILIAL", "FILIAL_CONTROLE_CARGA");
	        s.addProjection("CC.NR_CONTROLE_CARGA", "");
	        s.addProjection("DECODE(MED.ID_AWB, NULL, '" + stringEntrega  + "', '" + stringEntregaAeroporto  + "' )", "PROCESSO");
	        s.addProjection("2", "ORDEM_PROCESSO");
	        s.addProjection("NULL", "FILIAL_PEDIDO_COLETA");
	        s.addProjection("NULL", "NR_COLETA");
	        s.addProjection("DS.TP_DOCUMENTO_SERVICO", "TP_DOCUMENTO_SERVICO");
	        s.addProjection("DS.NR_DOCTO_SERVICO", "NR_DOCUMENTO_SERVICO");
	        s.addProjection("FILIAL_DS.SG_FILIAL", "FILIAL_DOCUMENTO_SERVICO");
	        s.addProjection("DS.DH_EMISSAO", "DH_EMISSAO");
	        s.addProjection("FILIAL_MANIF.SG_FILIAL", "FILIAL_MANIFESTO");
	        s.addProjection("ME.NR_MANIFESTO_ENTREGA", "NR_MANIFESTO");
	        s.addProjection("CLIENTE_PESSOA.NM_PESSOA", "CLIENTE_DESTINATARIO");
	        s.addProjection("DS.QT_VOLUMES", "QT_VOLUMES");
	        s.addProjection("DS.PS_REAL", "PESO");
	        s.addProjection("MOEDA_DS.SG_MOEDA || ' ' || MOEDA_DS.DS_SIMBOLO", "MOEDA");
	        s.addProjection("DS.VL_MERCADORIA", "VALOR");
	        s.addProjection(PropertyVarcharI18nProjection.createProjection("LM.DS_LOCALIZACAO_MERCADORIA_I"), "TP_STATUS");
	        
	        String qtRetornos = new StringBuffer()
	        	.append("(SELECT COUNT(*) FROM EVENTO_DOCUMENTO_SERVICO EDS, EVENTO EV")
	        	.append("  WHERE EDS.ID_EVENTO = EV.ID_EVENTO")
	        	.append("    AND EDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO")
	        	.append("    AND EV.CD_EVENTO = ?)").toString();
	        s.addProjection(qtRetornos, "QT_RETORNOS");
	        s.addCriteriaValue(Long.valueOf(59));
        }
        
        s.addFrom("CONTROLE_CARGA","CC");
        s.addFrom("FILIAL","FILIAL_CC");
        s.addFrom("MANIFESTO","M");
        s.addFrom("FILIAL","FILIAL_MANIF");
        s.addFrom("MANIFESTO_ENTREGA","ME");
        s.addFrom("MANIFESTO_ENTREGA_DOCUMENTO","MED");
        s.addFrom("DOCTO_SERVICO","DS");
        s.addFrom("FILIAL","FILIAL_DS");
        s.addFrom("MOEDA","MOEDA_DS");
        s.addFrom("LOCALIZACAO_MERCADORIA", "LM");
        s.addFrom("CLIENTE","CLI");
        s.addFrom("PESSOA","CLIENTE_PESSOA");
        s.addJoin("CC.ID_FILIAL_ORIGEM", "FILIAL_CC.ID_FILIAL");
        s.addJoin("M.ID_CONTROLE_CARGA", "CC.ID_CONTROLE_CARGA");
        s.addJoin("M.ID_FILIAL_ORIGEM", "FILIAL_MANIF.ID_FILIAL");
        s.addJoin("ME.ID_MANIFESTO_ENTREGA", "M.ID_MANIFESTO");
        s.addJoin("MED.ID_MANIFESTO_ENTREGA", "ME.ID_MANIFESTO_ENTREGA");
        s.addJoin("MED.ID_DOCTO_SERVICO", "DS.ID_DOCTO_SERVICO");
        s.addJoin("DS.ID_FILIAL_ORIGEM", "FILIAL_DS.ID_FILIAL");
        s.addJoin("DS.ID_MOEDA", "MOEDA_DS.ID_MOEDA");
        s.addJoin("CLI.ID_CLIENTE (+)", "DS.ID_CLIENTE_DESTINATARIO");
        s.addJoin("CLIENTE_PESSOA.ID_PESSOA(+)", "CLI.ID_CLIENTE");
        s.addJoin("DS.ID_LOCALIZACAO_MERCADORIA", "LM.ID_LOCALIZACAO_MERCADORIA(+)");
        
        if (Boolean.TRUE.equals(typedFlatMap.getBoolean("blSomenteRealizados"))) {

        	String somenteRealizados = new StringBuffer()
       		.append("EXISTS (SELECT 1 FROM ")
	        		.append("EVENTO_DOCUMENTO_SERVICO EDS3, EVENTO EV3 ")
	        		.append("WHERE EDS3.ID_EVENTO = EV3.ID_EVENTO ")
	        		.append("AND EDS3.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ")
	        		.append("AND EDS3.BL_EVENTO_CANCELADO = 'N' ")
	        		.append("AND EV3.CD_EVENTO IN (?, ?))").toString();
        	s.addCustomCriteria(somenteRealizados);
        	s.addCriteriaValue(Long.valueOf(ENTREGA_REALIZADA));
        	s.addCriteriaValue(Long.valueOf(ENTREGA_AEROPORTO));
        }
        
        String filtroTpDocumento = "DS.TP_DOCUMENTO_SERVICO IN (?, ?, ?, ?, ?, ?, ?, ?)";
        s.addCustomCriteria(filtroTpDocumento);
        s.addCriteriaValue("CTE");
        s.addCriteriaValue("CTR");
        s.addCriteriaValue("CRT");
        s.addCriteriaValue("NFT");
        s.addCriteriaValue("MDA");
        s.addCriteriaValue("RRE");
        s.addCriteriaValue("NTE");
        s.addCriteriaValue("NSE");

        if (typedFlatMap.getLong("meioTransporteRodoviario.idMeioTransporte") != null) {
        	s.addCriteria("CC.ID_TRANSPORTADO", "=", typedFlatMap.getLong("meioTransporteRodoviario.idMeioTransporte"));
        }
        
        if (typedFlatMap.getLong("controleCarga.idControleCarga") != null) {
        	s.addCriteria("CC.ID_CONTROLE_CARGA", "=", typedFlatMap.getLong("controleCarga.idControleCarga"));
        }

        s.addCriteria("CC.TP_CONTROLE_CARGA", "=", "C");
        s.addCriteria("trunc (CAST(CC.DH_SAIDA_COLETA_ENTREGA AS DATE))", "=", typedFlatMap.getYearMonthDay("dtConsulta"));
        
      return s;        
    }
    
    private String getOrderByClause() {
		SqlTemplate s = createSqlTemplate();
		s.addOrderBy("FILIAL_CONTROLE_CARGA");
		s.addOrderBy("NR_CONTROLE_CARGA");
		s.addOrderBy("ORDEM_PROCESSO");
		s.addOrderBy("FILIAL_PEDIDO_COLETA");
		s.addOrderBy("NR_COLETA");
		s.addOrderBy("TP_DOCUMENTO_SERVICO");
		s.addOrderBy("FILIAL_DOCUMENTO_SERVICO");
		s.addOrderBy("NR_DOCUMENTO_SERVICO");
		return s.getSql();
    }
    
	/**
	 * Obtem os parametros comuns a ambas as consultas de coleta e entrega
	 * @param tfm
	 * @return
	 */
	private String getFilterSummary(TypedFlatMap tfm, boolean addFilterSummary) {
		
		SqlTemplate s = createSqlTemplate();
		
		if (tfm.getLong("meioTransporteRodoviario.idMeioTransporte") != null) {
			MeioTransporte meioTransporte = this.meioTransporteService.findByIdInitLazyProperties(tfm.getLong("meioTransporteRodoviario.idMeioTransporte"), false); 
			s.addFilterSummary("veiculo", meioTransporte.getNrFrota() + " - " + meioTransporte.getNrIdentificador());
		}
		
		if ((tfm.getLong("controleCarga.filialByIdFilialOrigem.idFilial") != null) 
				&& (tfm.getLong("controleCarga.idControleCarga") != null)) {

			Filial filialControleCarga = this.filialService.findById(tfm.getLong("controleCarga.filialByIdFilialOrigem.idFilial"));
			ControleCarga controleCarga = this.controleCargaService.findByIdInitLazyProperties(tfm.getLong("controleCarga.idControleCarga"), false);
			if (addFilterSummary)
				s.addFilterSummary("controleCarga", FormatUtils.formatSgFilialWithLong(filialControleCarga.getSgFilial(),controleCarga.getNrControleCarga())); 
		}
		
		if (tfm.getYearMonthDay("dtConsulta") != null) {
			if (addFilterSummary)			
				s.addFilterSummary("dataConsulta", JTFormatUtils.format(tfm.getYearMonthDay("dtConsulta")));
		}
	
		Map result = new HashMap(2);
		result.put("order", s.getSql());

		if (addFilterSummary) {
			s.addFilterSummary("coleta", Boolean.TRUE.equals(tfm.getBoolean("blColeta")) ? configuracoesFacade.getMensagem("sim") : configuracoesFacade.getMensagem("nao"));
			s.addFilterSummary("entrega", Boolean.TRUE.equals(tfm.getBoolean("blEntrega")) ? configuracoesFacade.getMensagem("sim") : configuracoesFacade.getMensagem("nao"));
			s.addFilterSummary("somenteRealizados", Boolean.TRUE.equals(tfm.getBoolean("blSomenteRealizados")) ? configuracoesFacade.getMensagem("sim") : configuracoesFacade.getMensagem("nao"));
		}
		
		if(StringUtils.isNotBlank(tfm.getString("servico.dsServico"))){
			s.addFilterSummary("servico", tfm.getString("servico.dsServico"));
		}
		if(StringUtils.isNotBlank(tfm.getString("dsTpPedidoColeta"))){
			s.addFilterSummary("tipoColeta", tfm.getString("dsTpPedidoColeta"));
		}
		
		return s.getFilterSummary();
	}


	public Integer getRowCount(TypedFlatMap tfm) {
		
		int resultado = 0;
		
		boolean blColeta = tfm.getBoolean("blColeta").booleanValue();
		boolean blEntrega = tfm.getBoolean("blEntrega").booleanValue();

		SqlTemplate sqlColeta = makeReportQueryColeta(tfm, false, "");
		SqlTemplate sqlEntrega = makeReportQueryEntrega(tfm, false, "", "");
		
		int rowCountColeta;
		int rowCountEntrega;
		
		if (blColeta) {
			rowCountColeta = this.relatorioColetasEntregasFrotaDAO.getRowCountCustom(sqlColeta.getSql(false), sqlColeta.getCriteria()).intValue();
			resultado += rowCountColeta;
		}
		
		if (blEntrega) {
			rowCountEntrega = this.relatorioColetasEntregasFrotaDAO.getRowCountCustom(sqlEntrega.getSql(false), sqlEntrega.getCriteria()).intValue();
			resultado += rowCountEntrega;
		}
		
		return Integer.valueOf(resultado);
	}
	
	private void executeRegrasEmissaoConsulta(TypedFlatMap typedFlatMap){
		if (typedFlatMap.getLong("meioTransporteRodoviario.idMeioTransporte") == null && typedFlatMap.getLong("controleCarga.idControleCarga") == null)
			throw new BusinessException("LMS-05140");
	}
	
	public ResultSetPage findPaginated(TypedFlatMap typedFlatMap) {
		executeRegrasEmissaoConsulta(typedFlatMap);
		
		ResultSetPage rsp = null;
		boolean blColeta = typedFlatMap.getBoolean("blColeta").booleanValue();
		boolean blEntrega = typedFlatMap.getBoolean("blEntrega").booleanValue();
		String stringColeta = typedFlatMap.getString("stringColeta");
		String stringEntrega = typedFlatMap.getString("stringEntrega");
		String stringEntregaAeroporto = typedFlatMap.getString("stringEntregaAeroporto");
		
        Map compiledQueryAndCriteria = getCompiledQueryAndCriteriaObject(typedFlatMap, blColeta, blEntrega, stringColeta, stringEntrega, stringEntregaAeroporto);
        if (compiledQueryAndCriteria != null) {
	        String query = (String)compiledQueryAndCriteria.get("query")+ " " + getOrderByClause();
	        Object[] criteria = (Object[])compiledQueryAndCriteria.get("criteria");

	        List list = new ArrayList();
	        
			rsp = this.relatorioColetasEntregasFrotaDAO.findPaginatedCustom(FindDefinition.createFindDefinition(typedFlatMap), query, criteria);
			
			for(Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
	    		
				Object[] item = (Object[])it.next();
	    		
				Map map = new HashMap();
	    		map.put("sgFilialControleCarga",item[0]);
	    		map.put("nrControleCarga",item[1]);
	    		map.put("processo",item[2]);
	    		map.put("sgFilialColeta",item[4]);
	    		map.put("nrColeta",item[5]);
	    		map.put("tpDocumentoServico",item[6]);
	    		map.put("nrDocumentoServico",item[7]);
	    		map.put("sgFilialDocumentoServico",item[8]);
	    		map.put("dhEmissao",item[9]);
	    		map.put("sgFilialManifesto",item[10]);
	    		map.put("nrManifesto",item[11]);    		
	    		map.put("nmClienteDestinatario",item[12]);
	    		map.put("qtVolumes",item[13]);
	    		map.put("peso",item[14]);
	    		map.put("sgSimboloMoeda", item[15]);
	    		map.put("valor", item[16]);
	    		map.put("tpStatus",item[17]);
	    		map.put("qtRetornos",item[18]);
	    		list.add(map);
			}
			
			rsp.setList(list);
        }
		
		return rsp;
	}
	
	public void setMeioTransporteService(
			MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setRelatorioColetasEntregasFrotaDAO(
			RelatorioColetasEntregasFrotaDAO relatorioColetasEntregasFrotaDAO) {
		this.relatorioColetasEntregasFrotaDAO = relatorioColetasEntregasFrotaDAO;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
}
