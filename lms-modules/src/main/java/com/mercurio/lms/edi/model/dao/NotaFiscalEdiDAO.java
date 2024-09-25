package com.mercurio.lms.edi.model.dao;


import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.springframework.beans.BeanUtils;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.edi.enums.CampoNotaFiscalEdiComplementoFedex;
import com.mercurio.lms.edi.model.ClienteEDIFilialEmbarcadora;
import com.mercurio.lms.edi.model.LogEDI;
import com.mercurio.lms.edi.model.LogEDIComplemento;
import com.mercurio.lms.edi.model.LogEDIDetalhe;
import com.mercurio.lms.edi.model.LogEDIItem;
import com.mercurio.lms.edi.model.LogEDIVolume;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiComplemento;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;
import com.mercurio.lms.edi.model.NotaFiscalEdiVolume;
import com.mercurio.lms.expedicao.model.CCE;
import com.mercurio.lms.expedicao.model.CCEItem;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
@SuppressWarnings("deprecation")
public class NotaFiscalEdiDAO extends BaseCrudDao<NotaFiscalEdi, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    @SuppressWarnings("rawtypes")
    protected final Class getPersistentClass() {
        return NotaFiscalEdi.class;
    }

	public Long findSequenciaAgrupamentoSq(){		
		return Long.valueOf(getSession().createSQLQuery("select SEQUENCIA_AGRUPAMENTO_SQ.nextval from dual").uniqueResult().toString());
	}
	
	public Long findSequence(){		
		return Long.valueOf(getSession().createSQLQuery("select NOTA_FISCAL_EDI_SQ.nextval from dual").uniqueResult().toString());
	}
    
	public NotaFiscalEdi findById(Long idNotaFiscalEdi){
		return super.findById(idNotaFiscalEdi);
	}
	
    @SuppressWarnings("unchecked")
    public List<NotaFiscalEdi> findByNrIdentificacaoByTpAgrupamentoEdiByTpOrdemEmissaoEdi(String nrIdentificacao, String tpAgrupamentoEdi, String tpOrdemEmissaoEdi, String processarPor, String tpProcessamento) {
    	StringBuilder query = new StringBuilder();
    	
    	query.append("select nofe from " + getPersistentClass().getName() + " as nofe "); 
    	
    	if ("E".equals(tpAgrupamentoEdi)) {
    		query.append(" , NotaFiscalEdiComplemento as nfec ")
    			 .append(" , Cliente as clieRemetente ");
    	}
    	
    	if(processarPor!= null && "T".equals(processarPor)){
    		query.append(" where nofe.cnpjTomador = :nrIdentificacaoNofe " );
    	}else{
    		query.append(" where nofe.cnpjReme = :nrIdentificacaoNofe " );
    	}

    	Map<String, Object> criteria = new HashMap<String, Object>();
    	criteria.put("nrIdentificacaoNofe", Long.valueOf(nrIdentificacao));
    	
    	//TODO Utilizar constante para tpOrdemEmissaoEdi, valores "N", "D"
    	if (tpAgrupamentoEdi != null) {
    		if ("N".equals(tpAgrupamentoEdi)) {
        		if ("N".equals(tpOrdemEmissaoEdi)) {
            		query.append(" order by nofe.nrNotaFiscal ");
            	} else if ("D".equals(tpOrdemEmissaoEdi)) {
            		query.append(" order by nofe.cnpjDest ");
            	}
    		} else if ("P".equals(tpAgrupamentoEdi)) {
        		query.append(" order by nofe.cnpjDest, nofe.tipoFrete, nofe.nrNotaFiscal ");
    		} else if ("E".equals(tpAgrupamentoEdi)) {
        		query.append(" and clieRemetente.pessoa.nrIdentificacao = :nrIdentificacaoCliRemetente ");
        		if(ConstantesExpedicao.TP_PROCESSAMENTO_POR_MANIFESTO_CONSOLIDADO.equals(tpProcessamento)){
        			query.append(" and nfec.indcIdInformacaoDoctoClien = clieRemetente.informacaoDoctoClienteConsolidado.idInformacaoDoctoCliente ");
        		}else{
        			query.append(" and nfec.indcIdInformacaoDoctoClien = clieRemetente.informacaoDoctoCliente.idInformacaoDoctoCliente ");
        		}
        		query.append(" and nfec.notaFiscalEdi.idNotaFiscalEdi = nofe.idNotaFiscalEdi ")
        		.append(" order by nofe.cnpjDest, nfec.valorComplemento, nofe.nrNotaFiscal ");
        		
        		criteria.put("nrIdentificacaoCliRemetente", nrIdentificacao);
    		} else if ("C".equals(tpAgrupamentoEdi)) {
        		query.append(" order by nofe.sequenciaAgrupamento ");
    		}
    	}
    	
    	return getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public NotaFiscalEdi findByNrIdentificacaoByNrNotaFiscal(String nrIdentificacao, Integer nrNotaFiscal, String processarPor) {
    	StringBuilder query = new StringBuilder()
    	.append(" from " + getPersistentClass().getName() + " as nofe ")
    	.append(" where ");
    	
    	if(processarPor!= null && "T".equals(processarPor)){
    		query.append("nofe.cnpjTomador = :nrIdentificacao ");
    	}else{
    		query.append("nofe.cnpjReme = :nrIdentificacao ");
    	}
    	
    	query.append("and nofe.nrNotaFiscal = :nrNotaFiscal ")
    	.append("and nofe.idNotaFiscalEdi = ")
    	.append("(select max(nofe1.idNotaFiscalEdi) ")
    	.append("from " + getPersistentClass().getName() + " as nofe1 ")
    	.append("where ");
    	
    	if(processarPor!= null && "T".equals(processarPor)){
    		query.append("nofe1.cnpjTomador = :nrIdentificacao and nofe1.nrNotaFiscal = :nrNotaFiscal) ");
    	}else{
    		query.append("nofe1.cnpjReme = :nrIdentificacao and nofe1.nrNotaFiscal = :nrNotaFiscal) ");
    	}
    	
    	Map criteria = new HashMap();
    	criteria.put("nrIdentificacao", Long.valueOf(nrIdentificacao));
    	criteria.put("nrNotaFiscal", nrNotaFiscal);
    	
    	 List lista = getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
    	 return lista.isEmpty() ? null : (NotaFiscalEdi)lista.get(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findIdsByNrIdentificacaoByNrNotaFiscal(String nrIdentificacao, Integer nrNotaFiscal) {
    	StringBuilder query = new StringBuilder()
    	.append("select max(nofe.idNotaFiscalEdi) ")
    	.append(" from " + getPersistentClass().getName() + " as nofe ")
    	.append(" where nofe.cnpjReme = :nrIdentificacao " )
    	.append(" and nofe.nrNotaFiscal = :nrNotaFiscal " );
    	
    	Map criteria = new HashMap();
    	criteria.put("nrIdentificacao", Long.valueOf(nrIdentificacao));
    	criteria.put("nrNotaFiscal", nrNotaFiscal);
    	
    	return getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
    }
    
	/**
	 * Busca os maiores identificadores das notas fiscais recebidas como
	 * parametro para o cliente informado.
	 * 
	 * @param cnpj identificador do cliente
	 * @param nrNotasFiscais
	 *            numeros das notas a serem buscadas
	 * @return lista das notas correspondentes
	 * @author Luis Carlos Poletto
	 * @param idInformacaoDoctoCliente 
	 */
	@SuppressWarnings("unchecked")
    public List<NotaFiscalEdi> findNotas(String cnpj, List<Integer> nrNotasFiscais, final Long idInformacaoDoctoCliente, String processarPor) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select nf1 from ");
		hql.append(getPersistentClass().getName());
		hql.append(" nf1 where nf1.id in (select max(nf.id) from ");
		hql.append(getPersistentClass().getName());
		
		if(processarPor!= null && "T".equals(processarPor)){
			hql.append(" nf where nf.cnpjTomador = :cnpj and nf.nrNotaFiscal in (:nrNotasFiscais) ");
		}else{
			hql.append(" nf where nf.cnpjReme = :cnpj and nf.nrNotaFiscal in (:nrNotasFiscais) ");
		}
		
		ArrayList<String> parameterNames = new ArrayList<String>();
		parameterNames.add("cnpj");
		parameterNames.add("nrNotasFiscais");
		
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(Long.valueOf(cnpj));
		parameters.add(nrNotasFiscais);
		
		if(idInformacaoDoctoCliente != null){
			hql.append(" and (exists (from NotaFiscalEdiComplemento nfec where nfec.indcIdInformacaoDoctoClien = :idInformacaoDoctoCliente and nfec.notaFiscalEdi = nf)");
			
			if(processarPor!= null && "T".equals(processarPor)){
				hql.append(" or not exists (from NotaFiscalEdiComplemento nfec join nfec.notaFiscalEdi nfe where nfec.indcIdInformacaoDoctoClien = :idInformacaoDoctoCliente and nfe.cnpjTomador = nf.cnpjTomador and nfe.nrNotaFiscal = nf.nrNotaFiscal))");
			}else{
				hql.append(" or not exists (from NotaFiscalEdiComplemento nfec join nfec.notaFiscalEdi nfe where nfec.indcIdInformacaoDoctoClien = :idInformacaoDoctoCliente and nfe.cnpjReme = nf.cnpjReme and nfe.nrNotaFiscal = nf.nrNotaFiscal))");
			}
			
			parameters.add(idInformacaoDoctoCliente);
			parameterNames.add("idInformacaoDoctoCliente");
		}
		hql.append(" group by nf.nrNotaFiscal) ");

		String[] parameterNamesArray = new String[parameterNames.size()];
		parameterNamesArray = parameterNames.toArray(parameterNamesArray);
		
		Object[] parametersArray = new Object[parameters.size()];
		parametersArray = parameters.toArray(parametersArray);

		return getHibernateTemplate().findByNamedParam(hql.toString(), parameterNamesArray, parametersArray);
	}

	/**
	 * Busca todos os identificadores de notas fiscais remetidas pelo cnpj
	 * recebido como parametro.
	 * 
	 * @param cnpj identificador do cliente
	 * @return lista das notas correspondentes
	 * @author Luis Carlos Poletto
	 * @param idInformacaoDoctoCliente 
	 */
	@SuppressWarnings("unchecked")
    public List<NotaFiscalEdi> find(String cnpj, final Long idInformacaoDoctoCliente, String processarPor) {
		final Map criteria = new HashMap();
		StringBuilder hql = new StringBuilder();
		hql.append(" select nf from ");
		hql.append(getPersistentClass().getName());
		if(processarPor!= null && "T".equals(processarPor)){
			hql.append(" nf where nf.cnpjTomador = :cnpj ");
		}else{
			hql.append(" nf where nf.cnpjReme = :cnpj ");
		}
		
		criteria.put("cnpj", Long.valueOf(cnpj));
		
		if(idInformacaoDoctoCliente != null){
			hql.append(" and exists (from NotaFiscalEdiComplemento nfec where nfec.indcIdInformacaoDoctoClien = :idInformacaoDoctoCliente and nfec.notaFiscalEdi = nf)");
			criteria.put("idInformacaoDoctoCliente", idInformacaoDoctoCliente);			
			return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), criteria);
		} else {
			return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), criteria);
		}
	}
	
	/**
	 * Busca todos os identificadores de notas fiscais remetidas pelo cnpj
	 * recebido como parametro.
	 * 
	 * @param cnpjTomador identificador do tomador
	 * @return lista das notas correspondentes
	 * @param idInformacaoDoctoCliente 
	 */
	@SuppressWarnings("unchecked")
    public List<NotaFiscalEdi> findByTomador(String cnpjTomador, final Long idInformacaoDoctoCliente) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select nf from ");
		hql.append(getPersistentClass().getName());
		hql.append(" nf where nf.cnpjTomador = ? ");
		if(idInformacaoDoctoCliente != null){
			hql.append(" and exists (from NotaFiscalEdiComplemento nfec where nfec.indcIdInformacaoDoctoClien = ? and nfec.notaFiscalEdi = nf)");
			return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{cnpjTomador, idInformacaoDoctoCliente});
		} else {
			return getAdsmHibernateTemplate().find(hql.toString(), cnpjTomador);
		}
	}

	/**
	 * Busca as Notas Fiscais EDI relacionadas ao Docto do Cliente
	 * @param idInformacaoDoctoCliente
	 * @param valorComplemento
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Integer> findByInformacaoDoctoCliente(final Long idInformacaoDoctoCliente, final String valorComplemento) {
		final StringBuilder query = new StringBuilder();
		query.append(" select distinct nofe.nrNotaFiscal ");
		query.append(" from " + getPersistentClass().getName() + " as nofe ");
		query.append(" ," + NotaFiscalEdiComplemento.class.getName() + " as nfec ");

		query.append(" where nfec.notaFiscalEdi.idNotaFiscalEdi = nofe.idNotaFiscalEdi ");
		query.append(" and nfec.indcIdInformacaoDoctoClien = :idInformacaoDoctoCliente ");
		query.append(" and nfec.valorComplemento = :valorComplemento ");

		final Map criteria = new HashMap();
		criteria.put("idInformacaoDoctoCliente", idInformacaoDoctoCliente);
		criteria.put("valorComplemento", valorComplemento);
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List<NotaFiscalEdi> findNotaFiscalByDoctoCliente(final Long idInformacaoDoctoCliente, final String valorComplemento) {
		final StringBuilder query = new StringBuilder();
		query.append(" select nofe ");
		query.append(" from " + getPersistentClass().getName() + " as nofe ");
		
		query.append(" where (nofe.nrNotaFiscal, nofe.idNotaFiscalEdi) in " + subQueryNotaFiscalByDoctoCliente());

		final Map criteria = new HashMap();
		criteria.put("idInformacaoDoctoCliente", idInformacaoDoctoCliente);
		criteria.put("valorComplemento", valorComplemento);
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
	}
	
	private String subQueryNotaFiscalByDoctoCliente() {
		final StringBuilder subQuery = new StringBuilder();
		subQuery.append("(select nofe2.nrNotaFiscal, max(nofe2.idNotaFiscalEdi) ");
		
		subQuery.append(" from " + getPersistentClass().getName() + " as nofe2 ");
		subQuery.append(" ," + NotaFiscalEdiComplemento.class.getName() + " as nfec2 ");
		
		subQuery.append(" where nfec2.notaFiscalEdi.idNotaFiscalEdi = nofe2.idNotaFiscalEdi ");
		subQuery.append(" and nfec2.indcIdInformacaoDoctoClien = :idInformacaoDoctoCliente ");
		subQuery.append(" and nfec2.valorComplemento = :valorComplemento ");
		
		subQuery.append(" group by nofe2.nrNotaFiscal )");
		return subQuery.toString();
	}

    /**
     * Retorna as notas fiscais relacionadas ao numero do manifesto consolidado
     * @paramchaveMDFEFedEX
     * @return
     * LMSA-6520: LMSA-6534
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<NotaFiscalEdi> findByChaveMdfeFedex(final String chaveMdfeFedex) {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT nfec.notaFiscalEdi ");
        query.append("FROM " + NotaFiscalEdiComplemento.class.getName() + " AS nfec ");
        query.append("," + InformacaoDoctoCliente.class.getName() + " AS idc ");
        query.append("WHERE idc.idInformacaoDoctoCliente = nfec.indcIdInformacaoDoctoClien ");
        query.append("AND idc.dsCampo = '").append(CampoNotaFiscalEdiComplementoFedex.CHAVE_MDFE_FEDEX.getNomeCampo()).append("' ");
        query.append("AND nfec.valorComplemento = :chaveMdfeFedex");

        final Map criteria = new HashMap();
        criteria.put("chaveMdfeFedex", chaveMdfeFedex);
        List result = getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
        return result;
    }
    //private static final String NOME_CAMPO_MANIFESTO_FEDEX = "MANIFESTO FEDEX";
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public NotaFiscalEdi findByChaveMdfeFedexENumeroNotaFiscal(final String chaveMdfeFedex, final Integer numeroNotaFiscal) {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT nfe ");
        query.append("FROM " + NotaFiscalEdiComplemento.class.getName() + " AS nfec ");
        query.append(", " + NotaFiscalEdi.class.getName() + " AS nfe ");
        query.append("," + InformacaoDoctoCliente.class.getName() + " AS idc ");
        query.append("WHERE nfec.notaFiscalEdi.idNotaFiscalEdi = nfe.idNotaFiscalEdi ");
        query.append("AND idc.idInformacaoDoctoCliente = nfec.indcIdInformacaoDoctoClien ");
        query.append("AND idc.dsCampo = '").append(CampoNotaFiscalEdiComplementoFedex.CHAVE_MDFE_FEDEX.getNomeCampo()).append("' ");
        query.append("AND nfec.valorComplemento = :chaveMdfeFedex ");
        query.append("AND nfe.nrNotaFiscal = :numeroNotaFiscal ");

        final Map criteria = new HashMap();
        criteria.put("chaveMdfeFedex", chaveMdfeFedex);
        criteria.put("numeroNotaFiscal", numeroNotaFiscal);
        NotaFiscalEdi result = (NotaFiscalEdi) getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
        return result;
    }

	public void updateNotaFiscal(Long cnpjRemetente){
		try {
	    	Connection connection = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().connection();
			CallableStatement statement = connection.prepareCall("{call P_ACERTO_NOTA_FISCAL_EDI(?) }");
			statement.setLong(1, cnpjRemetente);
			statement.executeUpdate();
		} catch (Exception e) {
			throw new InfrastructureException(e);
		}
	}
    
    /**
     * Busca os ids das notas que possuem o mesmo nr_nota e cnpj_reme da NotaFiscalEdi recebida por parâmetro.
     *  
     * @param idNotaFiscalEdi
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List findIdsByIdNotaFiscalEdi(Long idNotaFiscalEdi) {
    	StringBuilder query = new StringBuilder()
    	.append("select nfe.id ")
    	.append("from " + getPersistentClass().getName() + " as nfe ")
    	.append("where (nfe.cnpjReme, nfe.nrNotaFiscal) in ")
    		.append("(select nfe1.cnpjReme, nfe1.nrNotaFiscal from " + getPersistentClass().getName() + " as nfe1 where nfe1.id = :idNotaFiscalEdi )");

    	Map criteria = new HashMap();
    	criteria.put("idNotaFiscalEdi", idNotaFiscalEdi);
    	
    	return getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
    }

    /**
     * Remove todas as notas fiscais que são iguais as notas dos ids recebidos.
     * 
     * @param list lista de ids de idNotaFiscalEdi
     */
    public void removeByIdNotaFiscalEdi(List<Long> list) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("delete from " + getPersistentClass().getName() + " as nfe ");
    	sb.append("where (nfe.nrNotaFiscal, nfe.cnpjReme) in ");
    	sb.append("(select nfe1.nrNotaFiscal, nfe1.cnpjReme from " + getPersistentClass().getName() + " as nfe1 where nfe1.idNotaFiscalEdi in (:id)) ");
    	while(list.size() > 1000){
    		List<Long> sublist = new ArrayList<Long>(list.subList(0, 999));
    		getAdsmHibernateTemplate().removeByIds(sb.toString(), sublist);
    		list.removeAll(sublist);
    	}
    	getAdsmHibernateTemplate().removeByIds(sb.toString(), list);
    }
    
	public Long findNextNrProcessamento() { 
		return Long.valueOf(getSession().createSQLQuery("select LOG_ATU_EDI_PROCESSAMENTO_SQ.nextval from dual").uniqueResult().toString());
	}
    
	public Long findNextNrOrdemEmissaoEDI() { 
		return Long.valueOf(getSession().createSQLQuery("select NR_ORDEM_EMISSAO_EDI_SQ.nextval from dual").uniqueResult().toString());
	}

	public Integer findParametroGeralThreads() {
		try {
			String hql = "from "+ParametroGeral.class.getName()+
			" pg where pg.nmParametroGeral = ?";
			ParametroGeral param = (ParametroGeral)getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{"NUMERO_THREADS_EDI"});;
			if(param == null){
				return null;
			} else {
				return Integer.valueOf(param.getDsConteudo());
			}
		} catch (Exception e) {
		}
		return null;
	}

	public LogEDI storeLogEDI(String nrIdentificacao) {
		LogEDI logEDI = new LogEDI();
		logEDI.setIdLogEdi(findSequence("log_arquivo_edi_sq"));
		Date date = new Date();
		Object[] dados = findDadosLogEDI(nrIdentificacao);
		if (dados != null) {
			logEDI.setFilial(new Filial((Long) dados[0]));
			ClienteEDIFilialEmbarcadora cliente = new ClienteEDIFilialEmbarcadora();
			cliente.setIdClienteEDIFilialEmbarcadora((Long) dados[1]);
			logEDI.setClienteEDIFilialEmbarcadora(cliente);
			SimpleDateFormat sdf = new SimpleDateFormat("ddMM_HHmmssSSS");
			logEDI.setNome(dados[2] + "_" + dados[3] + "_" + sdf.format(date) + ".txt");
		}
		logEDI.setData(new YearMonthDay(date));
		logEDI.setHoraInicio(new TimeOfDay(date));
		logEDI.setStatus("Em processamento");
		store(logEDI);
		return logEDI;
	}

	public LogEDI storeLogEDISemLayout(String nrIdentificacao, String nomeArquivo) {
        LogEDI logEDI = new LogEDI();
        logEDI.setIdLogEdi(findSequence("log_arquivo_edi_sq"));
        Date date = new Date();
        Object[] dados = findDadosLogEDISemLayout(nrIdentificacao);
        if (dados != null) {
            logEDI.setFilial(new Filial((Long) dados[0]));
            ClienteEDIFilialEmbarcadora cliente = new ClienteEDIFilialEmbarcadora();
            cliente.setIdClienteEDIFilialEmbarcadora((Long) dados[1]);
            logEDI.setClienteEDIFilialEmbarcadora(cliente);
        }
        logEDI.setNome(nomeArquivo);
        logEDI.setData(new YearMonthDay(date));
        logEDI.setHoraInicio(new TimeOfDay(date));
        logEDI.setStatus("Em processamento");
        store(logEDI);
        return logEDI;
    }
	

	public void storeLogEDI(LogEDI logEDI, boolean sucesso, int qtdePartes) {
		logEDI.setHoraFim(new TimeOfDay());
		logEDI.setStatus(sucesso ? "Concluído sem erros" : "Erro inesperado");
		logEDI.setQtdePartes(qtdePartes);
		store(logEDI);
	}

	private Object[] findDadosLogEDI(String nrIdentificacao) {
		StringBuilder sql = new StringBuilder()
				.append("SELECT e.fili_id_filial, e.id_cliente_edi_filial_embarc, l.id_cliente_edi_layout, l.nome_pasta ")
				.append("FROM cliente_edi_layout l, cliente_edi_filial_embarc e, pessoa p ")
				.append("WHERE l.ceid_cliente_edi_filial_embarc = e.id_cliente_edi_filial_embarc ")
				.append("AND e.ce_clie_pess_id_pessoa = p.id_pessoa ")
				.append("AND p.tp_identificacao = 'CNPJ' ")
				.append("AND p.nr_identificacao = :nrIdentificacao ");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("nrIdentificacao", nrIdentificacao);
		ConfigureSqlQuery config = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("fili_id_filial", Hibernate.LONG);
				sqlQuery.addScalar("id_cliente_edi_filial_embarc", Hibernate.LONG);
				sqlQuery.addScalar("id_cliente_edi_layout", Hibernate.LONG);
				sqlQuery.addScalar("nome_pasta", Hibernate.STRING);
			}
		};
		List<Object[]> result = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, config);
		return result.isEmpty() ? null : result.get(0);
	}

	
	private Object[] findDadosLogEDISemLayout(String nrIdentificacao) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT e.fili_id_filial, e.id_cliente_edi_filial_embarc ")
                .append("FROM cliente_edi_filial_embarc e, pessoa p ")
                .append("WHERE ")
                .append("e.ce_clie_pess_id_pessoa = p.id_pessoa ")
                .append("AND p.tp_identificacao = 'CNPJ' ")
                .append("AND p.nr_identificacao = :nrIdentificacao ");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("nrIdentificacao", nrIdentificacao);
        ConfigureSqlQuery config = new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("fili_id_filial", Hibernate.LONG);
                sqlQuery.addScalar("id_cliente_edi_filial_embarc", Hibernate.LONG);
            }
        };
        List<Object[]> result = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, config);
        return result.isEmpty() ? null : result.get(0);
    }
	
	
	public Pessoa findDadosRemetente(String nrIdentificacao) {
		StringBuilder hql = new StringBuilder()
				.append("SELECT p ")
				.append("FROM Pessoa p ")
				.append("JOIN FETCH p.inscricaoEstaduais ie ")
				.append("JOIN FETCH p.enderecoPessoa ep ")
				.append("JOIN FETCH ep.municipio m ")
				.append("JOIN FETCH m.unidadeFederativa ")
				.append("WHERE p.tpIdentificacao = 'CNPJ' ")
				.append("AND p.nrIdentificacao = :nrIdentificacao ");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("nrIdentificacao", nrIdentificacao);
		return (Pessoa) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), parameters);
	}

	public void storeLogEDIDetalhe(LogEDI logEDI, NotaFiscalEdi notaFiscalEdi) {
		LogEDIDetalhe logEDIDetalhe = new LogEDIDetalhe();
		logEDIDetalhe.setIdLogEdiDetalhe(findSequence("log_arquivo_edi_detalhe_sq"));
		logEDIDetalhe.setLogEDI(logEDI);
		BeanUtils.copyProperties(notaFiscalEdi, logEDIDetalhe, new String[] {
				"cepEnderRedesp", "cepMunicRedesp", "dataEmissaoNf"
		});
		if (notaFiscalEdi.getCepEnderRedesp() != null) {
			logEDIDetalhe.setCepEnderRedesp(notaFiscalEdi.getCepEnderRedesp().toString());
		}
		if (notaFiscalEdi.getCepMunicRedesp() != null) {
			logEDIDetalhe.setCepMunicRedesp(notaFiscalEdi.getCepMunicRedesp().toString());
		}
		if (notaFiscalEdi.getDataEmissaoNf() != null) {
			logEDIDetalhe.setDataEmissaoNf(new YearMonthDay(notaFiscalEdi.getDataEmissaoNf().getTime()));
		}
		logEDIDetalhe.setStatus("OK");
		store(logEDIDetalhe);

		for (NotaFiscalEdiItem notaFiscalEdiItem : notaFiscalEdi.getItens()) {
			LogEDIItem logEDIItem = new LogEDIItem();
			logEDIItem.setIdItem(findSequence("log_arquivo_edi_det_item_sq"));
			logEDIItem.setLogEDIDetalhe(logEDIDetalhe);
			BeanUtils.copyProperties(notaFiscalEdiItem, logEDIItem);
			store(logEDIItem);
		}
		for (NotaFiscalEdiComplemento notaFiscalEdiComplemento : notaFiscalEdi.getComplementos()) {
			LogEDIComplemento logEDIComplemento = new LogEDIComplemento();
			logEDIComplemento.setIdComplemento(findSequence("log_arquivo_edi_det_compl_sq"));
			logEDIComplemento.setLogEDIDetalhe(logEDIDetalhe);
			BeanUtils.copyProperties(notaFiscalEdiComplemento, logEDIComplemento);
			logEDIComplemento.setNomeComplemento(findNomeComplemento(notaFiscalEdiComplemento.getIndcIdInformacaoDoctoClien()));
			logEDIComplemento.setDtLog(new YearMonthDay());
			store(logEDIComplemento);
		}
		for (NotaFiscalEdiVolume notaFiscalEdiVolume : notaFiscalEdi.getVolumes()) {
			LogEDIVolume logEDIVolume = new LogEDIVolume();
			logEDIVolume.setIdVolume(findSequence("log_arquivo_edi_det_vol_sq"));
			logEDIVolume.setLogEDIDetalhe(logEDIDetalhe);
			BeanUtils.copyProperties(notaFiscalEdiVolume, logEDIVolume);
			store(logEDIVolume);
		}
	}

	private Map<Long, String> nomeComplementoMap = new HashMap<Long, String>();

	private String findNomeComplemento(Long idInfoComplemento) {
		if (nomeComplementoMap.containsKey(idInfoComplemento)) {
			return nomeComplementoMap.get(idInfoComplemento);
		}
		String nomeComplemento = (String) getSession().createSQLQuery(
				  "SELECT ds_campo "
				+ "FROM informacao_docto_cliente "
				+ "WHERE id_informacao_docto_cliente = :idInfoComplemento")
				.setParameter("idInfoComplemento", idInfoComplemento)
				.uniqueResult();
		nomeComplementoMap.put(idInfoComplemento, nomeComplemento);
		return nomeComplemento;
	}

	private Long findSequence(String name) {
		BigDecimal result = (BigDecimal) getSession().createSQLQuery("SELECT " + name + ".nextval FROM dual").uniqueResult();
		return result.longValue();
	}

    @SuppressWarnings({ "unchecked" })
    public NotaFiscalEdi findByOutpostData(String chaveNfe,String cnpj, String serieNotaFiscal,
            String numeroNotaFiscal) {
        
        SqlTemplate sqlTemplate = new SqlTemplate();
        
        sqlTemplate.addProjection("n");
        
        sqlTemplate.addFrom(getPersistentClass().getName(), "n");
        
        
        if (chaveNfe!=null){
            sqlTemplate.addCriteria("n.chaveNfe", "=", chaveNfe);
        }else{
            sqlTemplate.addCriteria("n.cnpjReme", "=", Long.valueOf(cnpj));
            sqlTemplate.addCriteria("n.serieNf", "=", serieNotaFiscal);
            sqlTemplate.addCriteria("n.nrNotaFiscal", "=", Integer.valueOf(numeroNotaFiscal));
        }
        
        sqlTemplate.addOrderBy("n.id", "desc");
        
        List<NotaFiscalEdi> list = getAdsmHibernateTemplate().find(sqlTemplate.getSql(), sqlTemplate.getCriteria());
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        
        return null;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<NotaFiscalEdi> findByCCE(Long cce) {
		final StringBuilder query = new StringBuilder();
		query.append(" select nofe ");
		query.append(" from " + getPersistentClass().getName() + " as nofe ");
		query.append(" ," + CCEItem.class.getName() + " as item ");
		query.append(" ," + CCE.class.getName() + " as cce ");
		query.append(" where nofe.chaveNfe = item.nrChave ");
		query.append(" and item.cce.idCCE = cce.idCCE ");
		query.append(" and cce.idCCE = :nrCCE");

		final Map criteria = new HashMap();
		criteria.put("nrCCE", cce);

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<Long, List<NotaFiscalEdiVolume>> findVolumesByNotasFiscaisEdi(List<Long> ids) {
		StringBuilder query = new StringBuilder();
		query.append(" select n ");
		query.append(" from NotaFiscalEdi n join fetch n.volumes v ");
		query.append(" where n.idNotaFiscalEdi in (:ids) ");
		query.append(" and v.codigoVolume is not null " );
		final Map criteria = new HashMap();
		criteria.put("ids", ids);
		List<NotaFiscalEdi> notas = getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
		Map<Long, List<NotaFiscalEdiVolume>> retorno = new HashMap<Long, List<NotaFiscalEdiVolume>>();
		for (NotaFiscalEdi nota : notas) {
			retorno.put(nota.getIdNotaFiscalEdi(), nota.getVolumes());
		}
		return retorno;
	}


	public NotaFiscalEdi findNotaByCnpj(String cnpj, String idNotaFiscalEdi) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT NFEV.* FROM NOTA_FISCAL_EDI_VOLUME nfev , NOTA_FISCAL_EDI nfe ");
		sql.append(" WHERE nfe.CNPJ_REME = :cnpj AND nfev.NFED_ID_NOTA_FISCAL_EDI = nfe.ID_NOTA_FISCAL_EDI ");
		sql.append(" AND nfev.NFED_ID_NOTA_FISCAL_EDI = :idNotaFiscalEdi AND nfev.CODIGO_VOLUME IS NOT NULL ");

		Map<String, Object> parametersValues = new HashMap<>();
		parametersValues.put("cnpj", cnpj);
		parametersValues.put("idNotaFiscalEdi", idNotaFiscalEdi);
		return (NotaFiscalEdi) getAdsmHibernateTemplate().find(sql.toString(), parametersValues).get(0);
	}
	
	
	public List<Object[]> findNotaFiscalEdiByUltimoIdImportado(Long idNotaFiscalEdi, List<Long> cnpjs, Long idServico, Integer limite) {
	
		StringBuilder inCnpjs = new StringBuilder();
		for (int i = 0; i < cnpjs.size(); i++) {
			inCnpjs.append(cnpjs.get(i).toString());
			if(i < cnpjs.size() - 1){
				inCnpjs.append(",");
			}
		}
		
		StringBuilder sql = new StringBuilder()
		.append("SELECT * FROM (")
		.append("SELECT ")
		.append("  NFE.ID_NOTA_FISCAL_EDI, ")
	    .append("  NFE.CHAVE_NFE, ")
	    .append("  NFE.CNPJ_DEST, ")
	    .append("  (SELECT FF.DS_FLUXO_FILIAL DS_FLUXO_FILIAL ")
	    .append("      FROM FLUXO_FILIAL FF ")
	    .append("    WHERE FF.ID_FILIAL_DESTINO = FILIAL_DESTINO.ID_FILIAL ")
	    .append("        AND FF.ID_FILIAL_ORIGEM = CREMETENTE.ID_FILIAL_ATENDE_OPERACIONAL ")
	    .append("        AND (FF.ID_SERVICO = :idServico OR FF.ID_SERVICO IS NULL) ")
	    .append("        AND TRUNC(SYSDATE) BETWEEN FF.DT_VIGENCIA_INICIAL AND FF.DT_VIGENCIA_FINAL ")
	    .append("        AND ROWNUM=1) AS DS_FLUXO_FILIAL, ")
	    .append("  NFE.CEP_ENDER_DEST, ")
	    .append("  CDESTINATARIO.ID_CLIENTE, ")
	    .append("  ENDERECO_DESTINATARIO.ID_ENDERECO_PESSOA, ")
	    .append("  FILIAL_DESTINO.ID_FILIAL AS ID_FILIAL_DESTINO, ")
	    .append("  FILIAL_ORIGEM.SG_FILIAL AS SG_FILIAL_ORIGEM, ")
	    .append("  REMETENTE.NM_PESSOA AS NM_REMETENTE, ")
	    .append("  DESTINATARIO.NM_PESSOA AS NM_DESTINATARIO, ")
	    .append("  ENDERECO_DESTINATARIO.DS_ENDERECO || ")
	    .append("	  (CASE WHEN ENDERECO_DESTINATARIO.NR_ENDERECO IS NULL OR TRIM(ENDERECO_DESTINATARIO.NR_ENDERECO) = '' ")
        .append("   	THEN '' ")
        .append("   	ELSE ', ' || ENDERECO_DESTINATARIO.NR_ENDERECO ")
        .append(" 	   END) || ")
        .append("     (CASE WHEN ENDERECO_DESTINATARIO.DS_COMPLEMENTO IS NULL OR TRIM(ENDERECO_DESTINATARIO.DS_COMPLEMENTO) = '' ")
        .append("   	THEN '' ")
        .append("  		ELSE ', ' || ENDERECO_DESTINATARIO.DS_COMPLEMENTO ")
        .append(" 	   END) AS DS_ENDERECO_DESTINATARIO, ")
        .append("  ENDERECO_DESTINATARIO.DS_BAIRRO AS DS_BAIRRO_DESTINATARIO, ")
        .append("  MUNICIPIO_DESTINATARIO.NM_MUNICIPIO AS NM_MUNICIPIO_DESTINATARIO, ")
        .append("  UF_DESTINATARIO.SG_UNIDADE_FEDERATIVA AS SG_UF_DESTINATARIO, ")
        .append("  " + PropertyVarcharI18nProjection.createProjection("PAIS_DESTINATARIO.NM_PAIS_I") + " AS NM_PAIS_DESTINATARIO, ")
        .append("  ENDERECO_DESTINATARIO.NR_CEP AS NR_CEP_DESTINATARIO, ")
        .append("  FILIAL_DESTINO.SG_FILIAL AS SG_FILIAL_DESTINO, ")
	    .append("  FILIAL_ORIGEM.ID_FILIAL AS ID_FILIAL_ORIGEM, ")
	    .append("  FILIAL_DESTINO.CD_FILIAL AS CD_FILIAL_DESTINO, ")
	    .append("  FILIAL_ORIGEM.CD_FILIAL AS CD_FILIAL_ORIGEM, ")
		.append("  NFE.NR_NOTA_FISCAL ")
	    .append("FROM NOTA_FISCAL_EDI NFE ")
	    .append("JOIN PESSOA REMETENTE ")
	    .append("  ON REMETENTE.NR_IDENTIFICACAO = ")
	    .append("    CASE WHEN LENGTH(NFE.CNPJ_REME) <= 11 ")
	    .append("      THEN LPAD(NFE.CNPJ_REME, 11, '0') ") 
	    .append("      ELSE LPAD(NFE.CNPJ_REME, 14, '0') ")
	    .append("    END ")
	    .append("JOIN CLIENTE CREMETENTE ")
	    .append("  ON REMETENTE.ID_PESSOA = CREMETENTE.ID_CLIENTE ")
	    .append("JOIN FILIAL FILIAL_ORIGEM ")
	    .append("  ON CREMETENTE.ID_FILIAL_ATENDE_OPERACIONAL = FILIAL_ORIGEM.ID_FILIAL ")
	    .append("JOIN PESSOA DESTINATARIO ")
	    .append("  ON DESTINATARIO.NR_IDENTIFICACAO = ")
	    .append("    CASE WHEN NFE.CNPJ_DEST <= 11 ")
	    .append("      THEN LPAD(NFE.CNPJ_DEST, 11, '0') ")
	    .append("      ELSE LPAD(NFE.CNPJ_DEST, 14, '0') ")
	    .append("    END ")
	    .append("JOIN CLIENTE CDESTINATARIO ")
	    .append("  ON DESTINATARIO.ID_PESSOA = CDESTINATARIO.ID_CLIENTE ")
	    .append("JOIN ENDERECO_PESSOA ENDERECO_DESTINATARIO ")
	    .append("  ON DESTINATARIO.ID_ENDERECO_PESSOA = ENDERECO_DESTINATARIO.ID_ENDERECO_PESSOA ")
	    .append("JOIN MUNICIPIO MUNICIPIO_DESTINATARIO ")
		.append("  ON ENDERECO_DESTINATARIO.ID_MUNICIPIO = MUNICIPIO_DESTINATARIO.ID_MUNICIPIO ")
		.append("JOIN UNIDADE_FEDERATIVA UF_DESTINATARIO ")
		.append("  ON MUNICIPIO_DESTINATARIO.ID_UNIDADE_FEDERATIVA = UF_DESTINATARIO.ID_UNIDADE_FEDERATIVA ")
		.append("JOIN PAIS PAIS_DESTINATARIO ")
		.append("  ON UF_DESTINATARIO.ID_PAIS = PAIS_DESTINATARIO.ID_PAIS ")
		.append("JOIN MUNICIPIO_FILIAL MF ")
		.append("  ON MF.ID_MUNICIPIO = MUNICIPIO_DESTINATARIO.ID_MUNICIPIO ")
		.append(" AND TRUNC(SYSDATE) BETWEEN MF.DT_VIGENCIA_INICIAL AND MF.DT_VIGENCIA_FINAL ")
		.append(" AND (MF.BL_RESTRICAO_ATENDIMENTO = 'N' OR MF.BL_PADRAO_MCD = 'S') ")
		.append("JOIN OPERACAO_SERVICO_LOCALIZA OSL ")
		.append("  ON OSL.ID_MUNICIPIO_FILIAL = MF.ID_MUNICIPIO_FILIAL ")
		.append(" AND TRUNC(SYSDATE) BETWEEN OSL.DT_VIGENCIA_INICIAL AND OSL.DT_VIGENCIA_FINAL ")
		.append(" AND (OSL.TP_OPERACAO = 'A' OR OSL.TP_OPERACAO = 'E') ")
		.append(" AND (OSL.ID_SERVICO = 1 OR OSL.ID_SERVICO IS NULL) ")
		.append(" AND OSL.BL_ATENDIMENTO_GERAL = 'S' ")
		.append("JOIN FILIAL FILIAL_DESTINO ")
		.append("  ON MF.ID_FILIAL = FILIAL_DESTINO.ID_FILIAL ")
	    .append("WHERE NFE.ID_NOTA_FISCAL_EDI > :idNotaFiscalEdi AND NFE.CNPJ_REME IN (" + inCnpjs + ") ")
		.append("ORDER BY NFE.ID_NOTA_FISCAL_EDI ASC) ")
		.append("WHERE ROWNUM < " + limite);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idServico", idServico);
		parameters.put("idNotaFiscalEdi", idNotaFiscalEdi);
		ConfigureSqlQuery config = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_NOTA_FISCAL_EDI", Hibernate.LONG);
				sqlQuery.addScalar("CHAVE_NFE", Hibernate.STRING);
				sqlQuery.addScalar("CNPJ_DEST", Hibernate.STRING);
				sqlQuery.addScalar("DS_FLUXO_FILIAL", Hibernate.STRING);
				
				sqlQuery.addScalar("CEP_ENDER_DEST", Hibernate.STRING);
				sqlQuery.addScalar("ID_CLIENTE", Hibernate.LONG);
				sqlQuery.addScalar("ID_ENDERECO_PESSOA", Hibernate.LONG);
				sqlQuery.addScalar("ID_FILIAL_DESTINO", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_ORIGEM", Hibernate.STRING);
				sqlQuery.addScalar("NM_REMETENTE", Hibernate.STRING);
				sqlQuery.addScalar("NM_DESTINATARIO", Hibernate.STRING);
				sqlQuery.addScalar("DS_ENDERECO_DESTINATARIO", Hibernate.STRING);
				sqlQuery.addScalar("DS_BAIRRO_DESTINATARIO", Hibernate.STRING);
				sqlQuery.addScalar("NM_MUNICIPIO_DESTINATARIO", Hibernate.STRING);
				sqlQuery.addScalar("SG_UF_DESTINATARIO", Hibernate.STRING);
				sqlQuery.addScalar("NM_PAIS_DESTINATARIO", Hibernate.STRING);
				sqlQuery.addScalar("NR_CEP_DESTINATARIO", Hibernate.STRING);
				sqlQuery.addScalar("SG_FILIAL_DESTINO", Hibernate.STRING);
				sqlQuery.addScalar("ID_FILIAL_ORIGEM", Hibernate.LONG);
				sqlQuery.addScalar("CD_FILIAL_DESTINO", Hibernate.LONG);
				sqlQuery.addScalar("CD_FILIAL_ORIGEM", Hibernate.LONG);
				sqlQuery.addScalar("NR_NOTA_FISCAL", Hibernate.STRING);
			}
		};
		List<Object[]> result = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, config);
		return result;
	}

	public List<NotaFiscalEdi> findByNrIdentificacaoByIntervaloNotaFiscal
			(String nrIdentificacao, Integer nrNotaFiscalInicial, Integer nrNotaFiscalFinal) {

		StringBuilder query = new StringBuilder();
		query.append(" from " + getPersistentClass().getName() + " as nofe ");
		query.append(" where ");
		query.append("nofe.cnpjReme = :nrIdentificacao ");
		query.append("and nofe.nrNotaFiscal >= :nrNotaFiscalInicial ");
		query.append("and nofe.nrNotaFiscal <= :nrNotaFiscalFinal ");
		query.append("and nofe.idNotaFiscalEdi = ");
		query.append("(select max(nofe1.idNotaFiscalEdi) ");
		query.append("from " + getPersistentClass().getName() + " as nofe1 ");
		query.append("where ");

		query.append("nofe1.cnpjReme = :nrIdentificacao and nofe1.nrNotaFiscal  >= :nrNotaFiscalInicial ");
		query.append("and nofe1.nrNotaFiscal  <= :nrNotaFiscalFinal )");

		Map criteria = new HashMap();
		criteria.put("nrIdentificacao", Long.valueOf(nrIdentificacao));
		criteria.put("nrNotaFiscalInicial", nrNotaFiscalInicial);
		criteria.put("nrNotaFiscalFinal", nrNotaFiscalFinal);

		List lista = getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
		return (List<NotaFiscalEdi>)lista;
	}

}

