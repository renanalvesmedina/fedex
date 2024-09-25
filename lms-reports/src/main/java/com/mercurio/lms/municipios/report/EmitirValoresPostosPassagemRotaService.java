package com.mercurio.lms.municipios.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.TipoPagamentoPosto;
import com.mercurio.lms.municipios.model.dao.TipoPagamentoPostoDAO;
import com.mercurio.lms.municipios.model.service.PostoPassagemService;
import com.mercurio.lms.util.JTDateTimeUtils;
/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.report.emitirValoresPostosPassagemRotaService"
 * 
 * @spring.property name="reportName" value="/com/mercurio/lms/municipios/report/emitirValoresPostosPassagemRota.vm"
 *
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="259"
 * @spring.property name="crossTabBandWidths" value="527"
 * @spring.property name="numbersOfCrossTabColumns" value="11"
 * 
 */		 
public class EmitirValoresPostosPassagemRotaService extends ReportServiceSupport {

	
	private DomainValueService domainValueService;
	private PostoPassagemService postoPassagemService;
	private TipoPagamentoPostoDAO tipoPagamentoPostoDAO;
	
	/* Nome da Tebela temporaria */
	private static final String NOME_TABELA = "TMP_POSTOPASSAGEM";	
	
	/* Defini��o das colunas de header. */
	private ColumnsDefinition columnsDefinition;
	
	/* Inner Class utilizada para a defini��o de uma coluna */
	private class ColumnsDefinition
	{
		
		private int totColumns;
		private Set columns = new LinkedHashSet();
		private String sqlNameColumns;
		private HashMap tabela;
		private List registros;
		
		public ColumnsDefinition(){}
		
		public ColumnsDefinition(int totColumns, Set columns, String sqlNameColumns,List registros) {
			this.totColumns = totColumns;
			this.columns = columns;
			this.sqlNameColumns = sqlNameColumns;
			this.registros = registros;
		}
		
		public List getRegistros() {
			return registros;
		}

		public void setRegistros(List registros) {
			this.registros = registros;
		}

		
		public Set getColumns() {
			return columns;
		}

		public void setColumns(Set columns) {
			this.columns = columns;
		}

		public String getSqlNameColumns() {
			return sqlNameColumns;
		}

		public void setSqlNameColumns(String sqlNameColumns) {
			this.sqlNameColumns = sqlNameColumns;
		}

		public int getTotColumns() {
			return totColumns;
		}

		public void setTotColumns(int totColumns) {
			this.totColumns = totColumns;
		}

		public HashMap getTabela() {
			return tabela;
		}

		public void setTabela(HashMap tabela) {
			this.tabela = tabela;
		}		
		
	}
	
	/* Metodo que obtem as defini��es das colunas */
	private ColumnsDefinition getColumns(List listDados, Long idMoedaPais)
	{
		Set setCrosstab = new LinkedHashSet();	
		
		
		//map para inserir os idpostos nao repetidos
		Map mapIdPostoPassagemTrecho = new HashMap();
		
		//map que armazena o registro a ser inserido na tabela tempor�ria
		Map mapRegistroBanco = new HashMap();
		
		Map mapListaTipoPagamento = new HashMap();
		
		List listaValores = new ArrayList();
		List listaTipoPagamentoPorPosto = new ArrayList();
		 
		List registros = new ArrayList();
		
		String names = "";
		
		int i = 0;
		
		//******************************headers dinamicos***********************************************
		/* Descobre as Colunas(Headers) do relatorio.*/ 
		for (Iterator it1 = listDados.iterator(); it1.hasNext();) { 
			Map map = (Map)it1.next();
			String dsTipoMeioTransporte = (String)map.get("DS_TIPO_MEIO_TRANSPORTE");
			setCrosstab.add(dsTipoMeioTransporte);	
		}
		
		
		
		//***********************************manipula��o dos dados******************************************
		
		for (Iterator it = listDados.iterator(); it.hasNext(); i++) { 
			Map map = (Map)it.next();
			Object idPostoPassagemTrecho = map.get("ID_POSTO_PASSAGEM_TRECHO");
			if(!mapIdPostoPassagemTrecho.containsKey(idPostoPassagemTrecho)){
				mapIdPostoPassagemTrecho.put(idPostoPassagemTrecho,idPostoPassagemTrecho);
				
				listaValores = new ArrayList();
				listaTipoPagamentoPorPosto = new ArrayList();
				mapRegistroBanco = new HashMap();
				
				String sgFilialOrigem = map.get("SG_FILIAL_ORIGEM").toString();
				String sgFilialDestino = map.get("SG_FILIAL_DESTINO").toString();
				//verifica o registro anterior, para nao haver trechos repetidos
				if(i>0){
					Map mapAnterior =(Map)listDados.get(i-1);
					String sgFilialOrigemAnt = mapAnterior.get("SG_FILIAL_ORIGEM").toString();
					String sgFilialDestinoAnt = mapAnterior.get("SG_FILIAL_DESTINO").toString();
					if(sgFilialOrigemAnt.equals(sgFilialOrigem) && sgFilialDestinoAnt.equals(sgFilialDestino)){
						sgFilialDestino=" ";
						sgFilialOrigem =" ";
					}
				}
				
				
				String tpPosto = map.get("TP_POSTO_PASSAGEM").toString();
				String localizacao = map.get("NM_MUNICIPIO").toString();
				String rodovia = map.get("SG_RODOVIA").toString();
				Integer km = null;
				if(map.get("NR_KM")!= null)
					km = Integer.valueOf(map.get("NR_KM").toString());
				String sentidoCobranca = map.get("TP_SENTIDO_COBRANCA").toString();
				String formaCobranca = map.get("TP_FORMA_COBRANCA").toString();
				
				mapRegistroBanco.put("sgFilialOrigem",sgFilialOrigem);
				mapRegistroBanco.put("sgFilialDestino",sgFilialDestino);
				mapRegistroBanco.put("tpPostoPassagem",tpPosto);
				mapRegistroBanco.put("nmMunicipio",localizacao);
				mapRegistroBanco.put("tpSentidoCobranca",sentidoCobranca);
				mapRegistroBanco.put("tpFormaCobranca",formaCobranca);
				mapRegistroBanco.put("sgRodovia",rodovia);
				mapRegistroBanco.put("nrKm",km);
				
			}
			if (map.get("ID_TIPO_MEIO_TRANSPORTE")!= null){	
				Long idPostoPassagem = Long.valueOf(map.get("ID_POSTO_PASSAGEM").toString());
				Long idTipoMeioTransporte = Long.valueOf(map.get("ID_TIPO_MEIO_TRANSPORTE").toString());
				Integer qtEixos = Integer.valueOf(map.get("QT_EIXOS").toString());
				 
				TypedFlatMap typedFlatMap = postoPassagemService.findVlByTpMeioTransporte(idPostoPassagem,idTipoMeioTransporte,qtEixos,JTDateTimeUtils.getDataAtual(),idMoedaPais, false);
				
				BigDecimal vlTarifa = typedFlatMap.getBigDecimal("vlPostoPassagem");
				Long idTipoPagamentoPosto = typedFlatMap.getLong("idTipoPagamentoPosto");
				TipoPagamentoPosto tipoPagamentoPosto = (TipoPagamentoPosto)tipoPagamentoPostoDAO.getAdsmHibernateTemplate().load(TipoPagamentoPosto.class,idTipoPagamentoPosto);
				
				//adiciona os valores por tipo de meio de transporte
				listaValores.add(vlTarifa);
				
				//adiciona os tipos de pagamento por posto
				String tipoPagamento = tipoPagamentoPosto.getTipoPagamPostoPassagem().getDsTipoPagamPostoPassagem().toString();
				listaTipoPagamentoPorPosto.add(tipoPagamento);
				
				//obtem um map com os tipos de pagamento, para cada tipo, cria um array
				if(!mapListaTipoPagamento.containsKey(tipoPagamento)) {
					mapListaTipoPagamento.put(tipoPagamento,new Object[setCrosstab.size()]);
				}
					
				
				//verifica se � o ultimo registro do agrupamento por posto_passagem_trecho
				Object idPostoPassagemProximo = null;
				int k = i+1;
				if(k < listDados.size()){
					Map mapProximoRegistro = (Map)listDados.get(k);
					idPostoPassagemProximo = mapProximoRegistro.get("ID_POSTO_PASSAGEM_TRECHO");
				}	
				if (!mapIdPostoPassagemTrecho.containsKey(idPostoPassagemProximo) || idPostoPassagemProximo == null){
					mapRegistroBanco.put("listaValoresTarifa",listaValores);
					registros.add(mapRegistroBanco);
					for(int j=0; j< listaTipoPagamentoPorPosto.size(); j++){
						String tpPag = (String)listaTipoPagamentoPorPosto.get(j);
						Object[] listaTotalizadoraTipoPagamento =((Object[])mapListaTipoPagamento.get(tpPag));
						BigDecimal valorExistente = new BigDecimal(0);
						
						if(listaTotalizadoraTipoPagamento[j]!= null){
							valorExistente = (BigDecimal)listaTotalizadoraTipoPagamento[j];
							valorExistente = valorExistente.add((BigDecimal)listaValores.get(j));
							listaTotalizadoraTipoPagamento[j]=valorExistente;
						}else{
							valorExistente = valorExistente.add((BigDecimal)listaValores.get(j));
							listaTotalizadoraTipoPagamento[j]=valorExistente;
						}
						
					}
					
				}
			}
			//verifica se � o ultimo registro para colocar no map final os totalizadores
			int m = i+1;
			if(listDados.size() == m){
				Object[] listaTotalizadoraFinal  = new Object[setCrosstab.size()];
				for(Iterator chaves = mapListaTipoPagamento.keySet().iterator(); chaves.hasNext();) {
					String chave =(String) chaves.next();
					Object[] pagamentos = (Object[])mapListaTipoPagamento.get(chave);
											
					Map mapTransferencia = new HashMap();
					mapTransferencia.put("sgFilialOrigem"," ");
					mapTransferencia.put("nmFilial"," ");
					mapTransferencia.put("dsRota"," ");
					mapTransferencia.put("tpPostoPassagem","");
					mapTransferencia.put("nmMunicipio","");
					mapTransferencia.put("tpSentidoCobranca","");
					mapTransferencia.put("tpFormaCobranca",chave);
					mapTransferencia.put("sgRodovia","");
					mapTransferencia.put("nrKm",null);
					mapTransferencia.put("totalizador","T");
					mapTransferencia.put("sgFilialDestino"," ");
					mapTransferencia.put(chave,pagamentos);
					
					registros.add(mapTransferencia);
					
					
					for(int pos=0;pos<pagamentos.length;pos++){
						BigDecimal valorNovo =(BigDecimal)pagamentos[pos];
						 
						if(listaTotalizadoraFinal[pos]== null)
							listaTotalizadoraFinal[pos] = new BigDecimal(0);
						BigDecimal valorExistente = (BigDecimal)listaTotalizadoraFinal[pos];
						BigDecimal totalPorMeioTransporte = valorExistente.add(valorNovo);
						listaTotalizadoraFinal[pos]= totalPorMeioTransporte;
					}
					
				}
				
				Map mapTotal = new HashMap();
				mapTotal.put("sgFilialOrigem"," ");
				mapTotal.put("nmFilial"," ");
				mapTotal.put("dsRota"," ");
				mapTotal.put("tpPostoPassagem","");
				mapTotal.put("nmMunicipio","");
				mapTotal.put("tpSentidoCobranca","");
				mapTotal.put("tpFormaCobranca","Total");
				mapTotal.put("sgRodovia","");
				mapTotal.put("nrKm",null);
				mapTotal.put("totalizador","T");
				mapTotal.put("sgFilialDestino"," ");
				mapTotal.put("Total",listaTotalizadoraFinal);
				registros.add(mapTotal);
				
			}
		}	
			
		/* Monta o SQL com os nomes das colunas */
		for(int j=1;j<=setCrosstab.size();j++)
		{
			names += ", COLUMN" +j; 
		}
		
		return new ColumnsDefinition(setCrosstab.size(),setCrosstab,names,registros);
	}
	
	/**
	 *  Metodo que grava os dados na base.
	 *  
	 * @param JdbcTemplate instancia de JdbcTemplate, List com os dados a serem inseridos.
	 * @return void  
	 */
	private void populateTable(JdbcTemplate jdbcTemplate, List listDados, Long idMoedaPais) {	
		
		columnsDefinition = getColumns(listDados, idMoedaPais);
		int cont = 0;
		
		/* Monta os inserts e insere na base. */
		
		for (Iterator it = columnsDefinition.getRegistros().iterator(); it.hasNext(); ) {
			Map mapRegistros = (Map)it.next();
			List valoresDinamicos = ((ArrayList)mapRegistros.get("listaValoresTarifa"));
			if(valoresDinamicos==null){
				Object[] valoresTotalizadores =((Object[])mapRegistros.get(mapRegistros.get("tpFormaCobranca")));
				valoresDinamicos = new ArrayList();
				for(int p =0; p< valoresTotalizadores.length;p++){
					valoresDinamicos.add(valoresTotalizadores[p]);
				}
				
				
				
			}
			String sgFilialOrigem = mapRegistros.get("sgFilialOrigem").toString();
			String sgFilialDestino = mapRegistros.get("sgFilialDestino").toString();
			String tpPostoPassagem = mapRegistros.get("tpPostoPassagem").toString();
			String nmMunicipio = mapRegistros.get("nmMunicipio").toString();
			
			String tpSentidoCobranca = mapRegistros.get("tpSentidoCobranca").toString();
			String tpFormaCobranca = mapRegistros.get("tpFormaCobranca").toString();
			
			
			String sgRodovia = mapRegistros.get("sgRodovia").toString();
			Integer nrKm = null;
			if(mapRegistros.get("nrKm")!=null)
				nrKm= Integer.valueOf(mapRegistros.get("nrKm").toString());
			
			String totalizador = "";
			if(mapRegistros.get("totalizador")!=null){
				if(cont == 0)
					totalizador= "P";
				else
					totalizador= mapRegistros.get("totalizador").toString();
				cont++;
			}
			StringBuffer sb = new StringBuffer("INSERT INTO " + NOME_TABELA +
					"(" +
					"SG_FILIAL, " +
					"NM_FILIAL," +
					"DS_ROTA, " +
					"TP_POSTO_PASSAGEM, " +
					"NM_MUNICIPIO, " +
					"SG_UNIDADE_FEDERATIVA, " +
					"NM_PAIS, " +
					"TP_SENTIDO_COBRANCA, " +
					"TP_FORMA_COBRANCA," +
					"ID_ROTA_COLETA_ENTREGA, " +
					"ID_FILIAL, " +
					"DS_RODOVIA," +
					"SG_RODOVIA ,  " +
					"NR_KM,  " +
					"NM_PESSOA, " +
					"DS_SIMBOLO, " +
					"BL_TOTAL, " +
					"SG_FILIAL_DESTINO" +
					columnsDefinition.getSqlNameColumns() + ") " +
					"VALUES("
							+"'"+sgFilialOrigem+"',"
							+"'"+"teste"+"',"
							+"'"+"teste"+"',"
							+"'"+tpPostoPassagem+"',"
							+"'"+nmMunicipio+"',"
							+"'"+"teste"+"',"
							+"'"+ "teste" +"',"
							+"'"+tpSentidoCobranca+"',"
							+"'"+tpFormaCobranca+"',"
							+new BigDecimal(1)+","
							+new BigDecimal(1)+","
							+"'"+"teste"+"',"
							+"'"+sgRodovia+"',"
							+nrKm+","
							+"'"+"teste"+"',"
							+null+","
							+"'"+totalizador+"' ,"
							+"'"+sgFilialDestino+"'");
			
			for (Iterator it2 = valoresDinamicos.iterator(); it2.hasNext(); ) {
				sb.append(", "+it2.next());
			}			
			
			sb.append(")\n");
			jdbcTemplate.execute(sb.toString());
		}
		
	}
	
	/* Metodo que monta os parameters do relatorio. */
	private void montaParametersReport(Map parameters,ColumnsDefinition cd)
	{		 
		parameters.put("usuarioEmissor",SessionContext.getUser().getNmUsuario());		
		
		Set c = cd.getColumns();
		Iterator it = c.iterator();
		int i=1;
		while(it.hasNext())
		{			
			parameters.put("PCOLUMN" + i,it.next().toString());
			i++;
		}	
	}
	public TypedFlatMap montaParametrosRota(List listaRotas){
		String sgParametrosRota = "";
		TypedFlatMap parametrosRotas = new TypedFlatMap();
		for(int i = 0; i< listaRotas.size();i++){
			TypedFlatMap mapSgFilialOrigem = (TypedFlatMap)listaRotas.get(i);
			if(sgParametrosRota.equals(""))
				sgParametrosRota = mapSgFilialOrigem.getString("filial.sgFilial");
			else
				sgParametrosRota = sgParametrosRota+"-"+mapSgFilialOrigem.getString("filial.sgFilial");
			
		}
		parametrosRotas.put("parametrosRota",sgParametrosRota);
		
		return parametrosRotas;
	}
	public List retornaListMapFilialOrigemDestino(List listaRotas){
		List listaMapFiliais = new ArrayList();
		for(int i = 0; i< listaRotas.size()-1;i++){
			TypedFlatMap tfmFiliais = new TypedFlatMap();
			TypedFlatMap mapIdFilialOrigem = (TypedFlatMap)listaRotas.get(i);
			tfmFiliais.put("idFilialOrigem",mapIdFilialOrigem.getLong("filial.idFilial"));
			for(int h = i+1 ; h<= i+1;h++){
				TypedFlatMap mapIdFilialDestino = (TypedFlatMap)listaRotas.get(h);
				tfmFiliais.put("idFilialDestino",mapIdFilialDestino.getLong("filial.idFilial"));
			}
			
			listaMapFiliais.add(tfmFiliais);
		}
		
		return listaMapFiliais;
	}
	
	public JRReportDataObject execute(Map parameters) throws Exception {	
		
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		SqlTemplate sql = null;
		
		int totalRotasLista = tfm.getList("filialRotas").size();
		if(totalRotasLista <= 1)
			throw new BusinessException("LMS-29157");
		
		List listMapFiliais = retornaListMapFilialOrigemDestino(tfm.getList("filialRotas"));
		TypedFlatMap parametrosRota = montaParametrosRota(tfm.getList("filialRotas"));
		tfm.put("parametrosRota",parametrosRota.getString("parametrosRota"));
		
		if(!tfm.getString("tpPostoPassagem").equals(""))
			tfm.put("tpPostoPassagemParameter",domainValueService.findDomainValueDescription("DM_POSTO_PASSAGEM",tfm.getString("tpPostoPassagem")));
		
		List listaFinal = new ArrayList();
		for (Iterator iter = listMapFiliais.iterator(); iter.hasNext();){
			TypedFlatMap mapFiliais = (TypedFlatMap) iter.next();
			Long idFilialOrigem = mapFiliais.getLong("idFilialOrigem");
			Long idFilialDestino = mapFiliais.getLong("idFilialDestino");
			tfm.put("idFilialOrigem",idFilialOrigem);
			tfm.put("idFilialDestino",idFilialDestino);
			
			
	        sql = getSqlTemplate(tfm);
	        
	         /*Query geral que retorna os dados para serem inseridos na tempor�ria*/
	         List result = getJdbcTemplate().queryForList(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()));
	         if(!result.isEmpty())
	        	 listaFinal.addAll(result);
		}
		
        /* Insere os dados na tebela temporaria do banco */
        Long idMoedaPais = null;
        if(tfm.getLong("moedaPais.idMoedaPais")!= null){
        	idMoedaPais = tfm.getLong("moedaPais.idMoedaPais");
        }
        populateTable(getJdbcTemplate(),listaFinal,idMoedaPais);
        
        /* passar para o relatorio qual o numero totsl de colunas(CrossTab) que ele ter�. */
		parameters.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[]{Integer.valueOf((columnsDefinition.getTotColumns()))});
		
		/* Monta os parrameters desse relatorio , para serem mandados ao jasper. */
		montaParametersReport(parameters,columnsDefinition);
		
		parameters.put("parametrosPesquisa", sql.getFilterSummary());
		parameters.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
	    
        /*Query que l� da tempor�ria e envia para o jasper*/
		JRReportDataObject jr = executeQuery("select * from " + NOME_TABELA,parameters);	
		
		jr.setParameters(parameters);		
		
		return jr;
	}
	
	private SqlTemplate getSqlTemplate(TypedFlatMap  criteria) {
		SqlTemplate sql = createSqlTemplate();
		//PROJE��O
		sql.addProjection("DISTINCT FILOR.SG_FILIAL","SG_FILIAL_ORIGEM");
		sql.addProjection("FILDEST.SG_FILIAL","SG_FILIAL_DESTINO");
		sql.addProjection("PPT.ID_POSTO_PASSAGEM_TRECHO");
		sql.addProjection("PP.ID_POSTO_PASSAGEM");
		sql.addProjection("PP.TP_POSTO_PASSAGEM");
		sql.addProjection("MPP.NM_MUNICIPIO");
		
		sql.addProjection("RO.SG_RODOVIA");
		sql.addProjection("RO.DS_RODOVIA");
		sql.addProjection("PP.NR_KM");
		sql.addProjection("PP.TP_SENTIDO_COBRANCA");
		sql.addProjection("TPP.TP_FORMA_COBRANCA");
		
		sql.addProjection("TMT.ID_TIPO_MEIO_TRANSPORTE");
		sql.addProjection("SUBSTR(TMT.DS_TIPO_MEIO_TRANSPORTE,0,3)||DECODE (TMT_COMP.DS_TIPO_MEIO_TRANSPORTE, null, '(' || Etmt.QT_EIXOS || ')',  '+'|| SUBSTR(TMT_COMP.DS_TIPO_MEIO_TRANSPORTE,0,3) || '(' || (NVL(Etmt.QT_EIXOS,0) + NVL(ETmtcomp.QT_EIXOS,0)) || ')')","DS_TIPO_MEIO_TRANSPORTE");
		
		sql.addProjection("NVL(Etmt.QT_EIXOS,0) + NVL(ETmtcomp.QT_EIXOS,0)","QT_EIXOS");
		
		sql.addProjection("TMT.DS_TIPO_MEIO_TRANSPORTE","DS_TIPO_MEIO_TRANSPORTE_N");
		
		sql.addProjection("TMT_COMP.DS_TIPO_MEIO_TRANSPORTE","DS_TIPO_MEIO_TRANSPORTE_COMP");
		
		
		//FROM
		sql.addFrom("POSTO_PASSAGEM_TRECHO","PPT");
		sql.addFrom("FILIAL","FILOR"); 
		sql.addFrom("FILIAL","FILDEST");
		sql.addFrom("POSTO_PASSAGEM","PP");
		sql.addFrom("MUNICIPIO","MPP");
		
		sql.addFrom("RODOVIA","RO");
				
		sql.addFrom("TARIFA_POSTO_PASSAGEM","TPP");
		sql.addFrom("TIPO_MEIO_TRANSPORTE","TMT");
		sql.addFrom("TIPO_MEIO_TRANSPORTE","TMT_COMP");
		sql.addFrom("EIXOS_TIPO_MEIO_TRANSPORTE","ETmt");
        sql.addFrom("EIXOS_TIPO_MEIO_TRANSPORTE","ETmtcomp");
		
			
		//JOIN
        
        

		sql.addJoin("PPT.ID_FILIAL_ORIGEM","FILOR.ID_FILIAL");
		sql.addJoin("PPT.ID_FILIAL_DESTINO","FILDEST.ID_FILIAL");
		sql.addJoin("PPT.ID_POSTO_PASSAGEM","PP.ID_POSTO_PASSAGEM");
		
		
		sql.addJoin("PP.ID_MUNICIPIO","MPP.ID_MUNICIPIO");
				
		sql.addJoin("PP.ID_RODOVIA","RO.ID_RODOVIA(+)");
		
		sql.addJoin("TPP.ID_POSTO_PASSAGEM","PP.ID_POSTO_PASSAGEM");
		
		sql.addJoin("TMT.ID_TIPO_MEIO_TRANSPORTE_COMPOS","TMT_COMP.ID_TIPO_MEIO_TRANSPORTE(+)");
		
		sql.addJoin("TMT.ID_TIPO_MEIO_TRANSPORTE" , "ETmt.ID_TIPO_MEIO_TRANSPORTE");

		sql.addJoin("TMT.ID_TIPO_MEIO_TRANSPORTE_COMPOS" , "ETmtcomp.ID_TIPO_MEIO_TRANSPORTE(+)");
		
		
		//CRITERIAS
		sql.addCriteria("TMT.TP_MEIO_TRANSPORTE","=","R");
		sql.addCriteria("TMT.TP_SITUACAO","=","A");
		
		sql.addCustomCriteria("NOT EXISTS(SELECT 1 FROM  TIPO_MEIO_TRANSPORTE TMTin WHERE TMTin.ID_TIPO_MEIO_TRANSPORTE_COMPOS = TMT.ID_TIPO_MEIO_TRANSPORTE AND TMTin.TP_SITUACAO = 'A')");
					
		sql.addCriteria("PPT.ID_FILIAL_ORIGEM","=",criteria.getLong("idFilialOrigem"));
		sql.addCriteria("PPT.ID_FILIAL_DESTINO","=",criteria.getLong("idFilialDestino"));
		sql.addFilterSummary("rota",criteria.getString("parametrosRota"));
				
		if (!criteria.getString("tpPostoPassagem").equals("")){
			sql.addCriteria("PP.TP_POSTO_PASSAGEM","=",criteria.getString("tpPostoPassagem"));
			sql.addFilterSummary("tipoPostoPassagem", criteria.getString("tpPostoPassagemParameter"));
		}	
		
		if(criteria.getLong("postoPassagem.idPostoPassagem")!= null){
			sql.addCriteria("PP.ID_POSTO_PASSAGEM","=",criteria.getLong("postoPassagem.idPostoPassagem"));
			sql.addFilterSummary("postoPassagem",criteria.getString("postoPassagem.tpPostoPassagem.description"));
		} 
		
		if(criteria.getString("descricaoMoeda")!= null)
			sql.addFilterSummary("converterParaMoeda",criteria.getString("descricaoMoeda"));
		
		sql.addCriteria("PPT.DT_VIGENCIA_INICIAL","<=",JTDateTimeUtils.getDataAtual());
		sql.addCriteria("PPT.DT_VIGENCIA_FINAL",">=",JTDateTimeUtils.getDataAtual());
		
		sql.addCriteria("TPP.DT_VIGENCIA_INICIAL","<=",JTDateTimeUtils.getDataAtual());
		sql.addCriteria("TPP.DT_VIGENCIA_FINAL",">=",JTDateTimeUtils.getDataAtual());
		
		sql.addOrderBy("PP.TP_POSTO_PASSAGEM");
		sql.addOrderBy("MPP.NM_MUNICIPIO");
		sql.addOrderBy("RO.DS_RODOVIA");
		sql.addOrderBy("PP.NR_KM");
		sql.addOrderBy("QT_EIXOS");
		sql.addOrderBy("TMT.DS_TIPO_MEIO_TRANSPORTE");
		sql.addOrderBy("TMT_COMP.DS_TIPO_MEIO_TRANSPORTE");
		
		return sql; 
	} 
	
	//imprime o rodap� dos tipos de meio de transporte rodoviarios e ativos
	public JRDataSource executeTipoMeioTransporteAtivo() throws Exception {
		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("SUBSTR(TMT.DS_TIPO_MEIO_TRANSPORTE,0,3)||' = '||TMT.DS_TIPO_MEIO_TRANSPORTE","DS_TIPO_MEIO_TRANSPORTE");
		 
		sql.addFrom("TIPO_MEIO_TRANSPORTE","TMT");
		sql.addCriteria("TMT.TP_SITUACAO","=","A");
		sql.addCriteria("TMT.TP_MEIO_TRANSPORTE","=","R");
				
		sql.addOrderBy("TMT.DS_TIPO_MEIO_TRANSPORTE");
		
		
		List result = getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());
		
		if (result==null) return null;

        Map mapDados = new HashMap();          

        List newList = new ArrayList(4);

        int i=0;

        ListIterator lit = result.listIterator();

        while(lit.hasNext())

        {

              Map aux  = (Map)lit.next();            

              mapDados.put("DS_TIPO_MEIO_TRANSPORTE" + i++,aux.get("DS_TIPO_MEIO_TRANSPORTE"));

              if (i==4)

              {                                                  

                    newList.add(mapDados);

                    mapDados = new HashMap();

                    i=0;

              }

        }

        if (i!=0) newList.add(mapDados); /* No caso do ultimo registro nao chegar a 3 */
        
        return new JRMapCollectionDataSource(newList);
	}
	
	@Override
	public void postExecute(JdbcTemplate jdbcTemplate, Map parameters  ){
		jdbcTemplate.execute("delete from " + NOME_TABELA);
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setPostoPassagemService(PostoPassagemService postoPassagemService) {
		this.postoPassagemService = postoPassagemService;
	}

	public void setTipoPagamentoPostoDAO(TipoPagamentoPostoDAO tipoPagamentoPostoDAO) {
		this.tipoPagamentoPostoDAO = tipoPagamentoPostoDAO;
	}


}
