package com.mercurio.lms.tributos.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.util.SqlTemplate;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class GeraFronteiraRapidaDestinoDAO extends AdsmDao {
    
    private JdbcTemplate jdbcTemplate;
    
	/**
     * Regra 1
     * Monta a query principal
     * @param idManifesto
     * @return Map dos dados principais : idFilialDestino   - Identificado da Filial Destino
     *                                    pfNrIdentificacao - Número de Identificação da filial
     *                                    pcNmPessoa        - Nome da Pessoa (Cliente)
     *                                    sgUF              - Sigla da Unidade Federativa
     *                                    idCliente         - Identificador do cliente
     *                                    nrManifestoOrigem - Número do Manifesto Origem
	 */
    public List findInformacoesPrincipais(Long idManifesto){
        
        List resultado = null;
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("M.ID_FILIAL_DESTINO, " +
                          "PF.NR_IDENTIFICACAO, " +
                          "PC.NM_PESSOA, " +
                          "UF.SG_UNIDADE_FEDERATIVA, " +
                          "C.ID_CLIENTE, " +
                          "MVN.NR_MANIFESTO_ORIGEM, " +
                          "F_ORIGEM.SG_FILIAL");
        
        sql.addFrom("MANIFESTO M " +
        		    "	INNER JOIN      FILIAL                    F_ORIGEM ON M.ID_FILIAL_ORIGEM          = F_ORIGEM.ID_FILIAL" +
                    "   INNER JOIN      PESSOA                    PF  ON F_ORIGEM.ID_FILIAL               = PF.ID_PESSOA" + 
                    "   INNER JOIN      MANIFESTO_VIAGEM_NACIONAL MVN ON M.ID_MANIFESTO                   = MVN.ID_MANIFESTO_VIAGEM_NACIONAL " +
                    "   INNER JOIN      MANIFESTO_NACIONAL_CTO    MNC ON MVN.ID_MANIFESTO_VIAGEM_NACIONAL = MNC.ID_MANIFESTO_VIAGEM_NACIONAL " +
                    "   INNER JOIN      CONHECIMENTO              CON ON MNC.ID_CONHECIMENTO              = CON.ID_CONHECIMENTO " +
                    "   INNER JOIN      DOCTO_SERVICO             DS  ON DS.ID_DOCTO_SERVICO              = CON.ID_CONHECIMENTO " +
                    "   INNER JOIN 		CLIENTE                   C   ON C.ID_CLIENTE 					  = DS.ID_CLIENTE_DESTINATARIO " +
                    "   INNER JOIN      PESSOA                    PC  ON C.ID_CLIENTE                     = PC.ID_PESSOA " +
                    "   INNER JOIN      MUNICIPIO                 MUN ON CON.ID_MUNICIPIO_ENTREGA         = MUN.ID_MUNICIPIO " +
                    "   INNER JOIN      UNIDADE_FEDERATIVA        UF  ON MUN.ID_UNIDADE_FEDERATIVA        = UF.ID_UNIDADE_FEDERATIVA ");
        
        sql.addCriteria("C.BL_FRONTEIRA_RAPIDA" ,"=","S");
        sql.addCriteria("UF.BL_FRONTEIRA_RAPIDA","=","S");
        sql.addCriteria("M.ID_MANIFESTO"        ,"=",idManifesto);
        sql.addCriteria("ROWNUM"                ,"<","2");
        
        Object[] values = JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),  sql.getCriteria());
        
        resultado = jdbcTemplate.query(sql.getSql(),values, new RowMapper(){

            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                
                Map map = new HashMap();
                
                map.put("idFilialDestino"   ,(rs.getString("ID_FILIAL_DESTINO") != null ? Long.valueOf(rs.getString("ID_FILIAL_DESTINO")):null));
                map.put("pfNrIdentificacao" , rs.getString("NR_IDENTIFICACAO"));//AVISO: PODE SER NULO
                map.put("pcNmPessoa"        , rs.getString("NM_PESSOA"));
                map.put("sgUF"              , rs.getString("SG_UNIDADE_FEDERATIVA"));
                map.put("idCliente"         ,(rs.getString("ID_CLIENTE") != null ? Long.valueOf(rs.getString("ID_CLIENTE")):null));
                map.put("nrManifestoOrigem" , rs.getString("NR_MANIFESTO_ORIGEM"));
                map.put("sgFilialOrigem"    , rs.getString("SG_FILIAL"));
                                
                return map;
                
            }
            
        });        
        
        if( resultado == null || resultado.isEmpty() ){
            return null;
        }     
        
		return resultado;
	}

	/**
     * Regra 1
     * Método que monta o nome do arquivo a ser gerado 
     * @param idCliente Identificador do cliente
     * @return String Valor do campo complementar
	 */
    public String findInformacoesNomeArquivo(Long idCliente){
	    
        List retorno = null;
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("VCC.VL_VALOR");
        
        sql.addFrom("CAMPO_COMPLEMENTAR CC, " +
                    "VALOR_CAMPO_COMPLEMENTAR VCC");
        
        sql.addJoin("CC.ID_CAMPO_COMPLEMENTAR"  ,"VCC.ID_CAMPO_COMPLEMENTAR");
        sql.addJoin("CC.NM_CAMPO_COMPLEMENTAR"     ,"'SG_ARQUIVO_FRONTEIRA_RAPIDA'");
        
        sql.addCriteria("VCC.ID_CLIENTE","=",idCliente);
        
        retorno = jdbcTemplate.queryForList(sql.getSql(), sql.getCriteria());
        
        if( retorno == null || retorno.isEmpty() ){
            return null;
        } else {            
            
            Map map = (Map) retorno.get(0);
            
            
            return (String) map.get("VL_VALOR");            
        } 
	}
	
    /**
     * Regra 2
     * Busca os dados do Conhecimento
     * @param idManifesto Identificador do manifesto
     * @return Lista de Conhecimentos
     */
	public List findConhecimentos(Long idManifesto){
        
        List resultado = null;        
        
	    SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("C.ID_FILIAL_ORIGEM, " +
                          "C.NR_CONHECIMENTO, " +
                          "C.TP_FRETE, " +                          
                          "C.ID_CONHECIMENTO, " +
                          "PF.NR_IDENTIFICACAO, " +
                          "DS.DH_EMISSAO, " +
                          "DS.VL_TOTAL_DOC_SERVICO, " +
                          "DS.VL_BASE_CALC_IMPOSTO, " +
                          "DS.PC_ALIQUOTA_ICMS, " +
                          "DS.VL_IMPOSTO, " +                       
                          "DS.NR_CFOP, " +
                          "DS.VL_MERCADORIA, " +
                          "PR.ID_PESSOA ID_REMETENTE, " +
                          "PD.ID_PESSOA ID_DESTINATARIO, " +
                          "PR.NR_IDENTIFICACAO NR_IDENT_REMETENTE, " +
                          "PD.NR_IDENTIFICACAO NR_IDENT_DESTINATARIO");
        
        sql.addFrom("MANIFESTO M" +
                    "   INNER JOIN PESSOA PF                     ON M.ID_FILIAL_ORIGEM                  = PF.ID_PESSOA " +
                    "   INNER JOIN MANIFESTO_VIAGEM_NACIONAL MVN ON M.ID_MANIFESTO                      = MVN.ID_MANIFESTO_VIAGEM_NACIONAL " +
                    "   INNER JOIN MANIFESTO_NACIONAL_CTO MNC    ON MVN.ID_MANIFESTO_VIAGEM_NACIONAL    = MNC.ID_MANIFESTO_VIAGEM_NACIONAL " +
                    "   INNER JOIN CONHECIMENTO C                ON MNC.ID_CONHECIMENTO                 = C.ID_CONHECIMENTO " +
                    "   INNER JOIN DOCTO_SERVICO DS              ON C.ID_CONHECIMENTO                   = DS.ID_DOCTO_SERVICO " +
                    "   INNER JOIN PESSOA PR                     ON DS.ID_CLIENTE_REMETENTE             = PR.ID_PESSOA " +
                    "   LEFT OUTER JOIN PESSOA PD                ON DS.ID_CLIENTE_DESTINATARIO          = PD.ID_PESSOA");
        
        sql.addCriteria("M.ID_MANIFESTO","=",idManifesto);
        
        Object[] values = JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),  sql.getCriteria());
        
        resultado = jdbcTemplate.query(sql.getSql(), values, new RowMapper(){
            
            public Object mapRow(java.sql.ResultSet rs, int arg1) throws java.sql.SQLException {
                
                Map map = new HashMap();
                    
                map.put("serie",              Long.valueOf(rs.getLong("ID_FILIAL_ORIGEM")));
                map.put("nrConhecimento",     rs.getString("NR_CONHECIMENTO"));      //AVISO: Pode ser nulo
                map.put("cnpjTransportadora", rs.getString("NR_IDENTIFICACAO"));     //AVISO: Pode ser nulo
                
               
                map.put("dtEmissao",          YearMonthDay.fromDateFields(rs.getDate("DH_EMISSAO")));
                map.put("vlFrete",            rs.getString("VL_TOTAL_DOC_SERVICO"));
                map.put("vlBaseICMS",         rs.getString("VL_BASE_CALC_IMPOSTO")); //AVISO: Pode ser nulo
                map.put("aliquotaICMS",       rs.getString("PC_ALIQUOTA_ICMS"));     //AVISO: Pode ser nulo
                map.put("vlICMS",             rs.getString("VL_IMPOSTO"));
                map.put("idConhecimento",     rs.getString("ID_CONHECIMENTO"));                                    
                map.put("idRemetente",        rs.getString("ID_REMETENTE"));
                map.put("idDestinatario",     rs.getString("ID_DESTINATARIO"));      //AVISO: Pode ser nulo
                map.put("nrIdentRemetente",   rs.getString("NR_IDENT_REMETENTE") != null ? Long.valueOf(rs.getString("NR_IDENT_REMETENTE")): null);       //AVISO: Pode ser nulo
                map.put("nrIdentDestinatario",rs.getString("NR_IDENT_DESTINATARIO") != null ? Long.valueOf(rs.getString("NR_IDENT_DESTINATARIO")): null); //AVISO: Pode ser nulo
                map.put("vlTotalMercadoria"  ,rs.getString("VL_MERCADORIA"));        //AVISO: Pode ser nulo
                map.put("CFOP"               ,rs.getString("NR_CFOP"));              //AVISO: Pode ser nulo
                map.put("tpFrete"            ,rs.getString("TP_FRETE"));                    
                
                return map;
            };
            
        });
        
        return resultado;
        
	}
	
    /**
     * Regra 2
     * Busca o valor da parcela de Desconto
     * @param idConhecimento Identificador do Documento de Serviço
     * @return Valor da Parcela
     */
	public String findConhecimentoParcelaDesconto(Long idConhecimento){
        
	    SqlTemplate sql = new SqlTemplate();
        List retorno         = null;
        String vlParcela = null;
        
        sql.addProjection("PDS.VL_PARCELA");
        
        sql.addFrom("PARCELA_DOCTO_SERVICO PDS, " +
                    "PARCELA_PRECO PP");
        
        sql.addJoin("PDS.ID_PARCELA_PRECO","PP.ID_PARCELA_PRECO");
        
        sql.addCriteria("PDS.ID_DOCTO_SERVICO","=", idConhecimento);
        
        retorno = jdbcTemplate.queryForList(sql.getSql(), sql.getCriteria());
        
        if( retorno != null && !retorno.isEmpty() ){
            vlParcela = (String)retorno.get(0);
        }
        
        return vlParcela;
        
	}
	
    /**
     * Regra 2
     * Busca as notas fiscais do conhecimento
     * @param idManifesto    Identificador do Manifesto
     * @param idConhecimento Identificador do Conhecimento
     * @return Lista de Notas Fiscais
     */
	public List findNotasFiscaisConhecimento(Long idManifesto, Long idConhecimento){
	   
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("NFC.DS_SERIE, " +
                          "NFC.NR_NOTA_FISCAL, " +
                          "PR.NR_IDENTIFICACAO, " +
                          "DS.VL_MERCADORIA");
        
        sql.addFrom("MANIFESTO M, " +
                    "MANIFESTO_VIAGEM_NACIONAL MVN, " +
                    "MANIFESTO_NACIONAL_CTO MNC, " +
                    "CONHECIMENTO C, " +
                    "DOCTO_SERVICO DS, " +
                    "NOTA_FISCAL_CONHECIMENTO NFC, " +
                    "PESSOA PR" );
        
        sql.addJoin("M.ID_MANIFESTO",                   "MVN.ID_MANIFESTO_VIAGEM_NACIONAL");
        sql.addJoin("MVN.ID_MANIFESTO_VIAGEM_NACIONAL", "MNC.ID_MANIFESTO_VIAGEM_NACIONAL");
        sql.addJoin("MNC.ID_CONHECIMENTO",              "C.ID_CONHECIMENTO");
        sql.addJoin("C.ID_CONHECIMENTO",                "DS.ID_DOCTO_SERVICO");
        sql.addJoin("C.ID_CONHECIMENTO",                "NFC.ID_CONHECIMENTO");
        sql.addJoin("DS.ID_CLIENTE_REMETENTE",          "PR.ID_PESSOA");
        
        sql.addCriteria("M.ID_MANIFESTO",   "=", idManifesto );
        sql.addCriteria("C.ID_CONHECIMENTO","=", idConhecimento );
        
        Object[] values = JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),  sql.getCriteria());
        
        List resultado = jdbcTemplate.query(sql.getSql(), values, new RowMapper(){
              
              public Object mapRow(java.sql.ResultSet rs, int arg1) throws java.sql.SQLException {
                  
                  Map map = new HashMap();
                      
                  map.put("serie",                 rs.getString("DS_SERIE"));             //AVISO: Pode ser nulo
                  map.put("nrDocumento",           rs.getString("NR_NOTA_FISCAL"));       
                  map.put("remetenteConhecimento", rs.getString("NR_IDENTIFICACAO"));     //AVISO: Pode ser nulo
                  map.put("vlTotalMercadoria"  ,   rs.getBigDecimal("VL_MERCADORIA"));        //AVISO: Pode ser nulo                  
                      
                  return map;
              };
              
          });
        
        return resultado;
        
	}

    /**
     * Busca os dados do Manifesto, motorista, veículo e rota
     * @param idManifesto Identificador do Manifesto
     * @return Map com os dados do manifesto, motorista, veículo e rota
     */
	public Map findManifestoMotoristaVeiculo(Long idManifesto){
        
	    SqlTemplate sql = new SqlTemplate();
        List resultado = null;
        Map retorno = null;
        
        sql.addProjection("MVN.NR_MANIFESTO_ORIGEM, " +
                          "M.VL_TOTAL_MANIFESTO, " +
                          "M.PS_TOTAL_MANIFESTO, " +
                          "MOTOR.NR_CARTEIRA_HABILITACAO, " +
                          "UFM.SG_UNIDADE_FEDERATIVA, " +
                          "PM.NM_PESSOA, " +
                          "PM.DS_ORGAO_EMISSOR_RG, " +
                          "PM.NR_RG, " +
                          "PM.NR_IDENTIFICACAO, " +
                          "MTR.CD_RENAVAM, " +
                          "MT.NR_IDENTIFICADOR, " +
                          "MTR.PS_TARA, " +
                          "MT.NR_CAPACIDADE_KG, " +
                          "MT.NR_CAPACIDADE_M3, " +
                          "LCC.NR_LACRE, " +
                          "M.TP_MODAL");
        
        sql.addFrom("MANIFESTO M " +
                    "     INNER JOIN MANIFESTO_VIAGEM_NACIONAL MVN  ON M.ID_MANIFESTO                     = MVN.ID_MANIFESTO_VIAGEM_NACIONAL" +
                    "     INNER JOIN CONTROLE_CARGA CC              ON M.ID_CONTROLE_CARGA                = CC.ID_CONTROLE_CARGA " +
                    "     INNER JOIN LACRE_CONTROLE_CARGA LCC       ON CC.ID_CONTROLE_CARGA               = LCC.ID_CONTROLE_CARGA " +
                    "     INNER JOIN MEIO_TRANSPORTE MT             ON LCC.ID_MEIO_TRANSPORTE             = MT.ID_MEIO_TRANSPORTE " +
                    "     INNER JOIN MEIO_TRANSPORTE_RODOVIARIO MTR ON MT.ID_MEIO_TRANSPORTE              = MTR.ID_MEIO_TRANSPORTE " +
                    "     LEFT OUTER JOIN MOTORISTA MOTOR           ON CC.ID_MOTORISTA                    = MOTOR.ID_MOTORISTA " +
                    "     LEFT OUTER JOIN PESSOA PM                 ON MOTOR.ID_MOTORISTA                 = PM.ID_PESSOA " +
                    "     LEFT OUTER JOIN UNIDADE_FEDERATIVA UFM    ON MOTOR.ID_LOCAL_EMISSAO_IDENTIDADE  = UFM.ID_UNIDADE_FEDERATIVA");
        
        sql.addCriteria("M.ID_MANIFESTO","=",idManifesto);
        sql.addCriteria("ROWNUM",        "<","2");
        
        Object[] values = JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),  sql.getCriteria());
        
        resultado = jdbcTemplate.query(sql.getSql(), values, new RowMapper(){
                   
            public Object mapRow(java.sql.ResultSet rs, int arg1) throws java.sql.SQLException {
                   
                Map map = new HashMap();                                    
                
                map.put("nrManifestoCarga"   , rs.getString("NR_MANIFESTO_ORIGEM"));
                map.put("vlTotalMercadorias" , rs.getBigDecimal("VL_TOTAL_MANIFESTO"));
                map.put("pesoTotalCargaKg"   , rs.getBigDecimal("PS_TOTAL_MANIFESTO"));
                map.put("nrHabilitacao"      , rs.getString("NR_CARTEIRA_HABILITACAO"));
                map.put("UFEmissaoRG"        , rs.getString("SG_UNIDADE_FEDERATIVA"));
                map.put("nomeMotorista"      , rs.getString("NM_PESSOA"));
                map.put("orgaoExpedicaoRG"   , rs.getString("DS_ORGAO_EMISSOR_RG"));     //AVISO: Pode ser nulo
                map.put("nrRG"               , rs.getString("NR_RG"));                   //AVISO: Pode ser nulo
                map.put("nrCPF"              , rs.getString("NR_IDENTIFICACAO"));        //AVISO: Pode ser nulo
                map.put("renavamVeiculo"     , rs.getString("CD_RENAVAM"));
                
                if( rs.getString("TP_MODAL").equals("A") ){//aéreo
                    map.put("placaVeiculo"       , "AEREO");
                } else {
                    map.put("placaVeiculo"       , rs.getString("NR_IDENTIFICADOR"));
                }                

                map.put("taraVeiculoKg"      , rs.getBigDecimal("PS_TARA"));             //AVISO: Pode ser nulo
                map.put("capacidadeVeiculoKg", rs.getBigDecimal("NR_CAPACIDADE_KG"));
                map.put("capacidadeVeiculoM3", rs.getBigDecimal("NR_CAPACIDADE_M3"));
                map.put("nrLacre"            , rs.getString("NR_LACRE"));
                 
                return map;
                
            };
               
        });
        
        if( resultado != null && !resultado.isEmpty() ){
            retorno = (Map) resultado.get(0);
        } 
         
        return retorno;
        
	}

    /**
     * @return Returns the jdbcTemplate.
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * @param jdbcTemplate The jdbcTemplate to set.
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
}