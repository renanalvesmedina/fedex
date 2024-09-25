package com.mercurio.lms.contasreceber.model.service;

import java.awt.Color;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.lowagie.text.pdf.BarcodeInter25;
import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.DateTimeUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroBoletoFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.HistoricoBoleto;
import com.mercurio.lms.contasreceber.model.OcorrenciaBanco;
import com.mercurio.lms.contasreceber.model.dao.BoletoDAO;
import com.mercurio.lms.contasreceber.model.param.BoletoParam;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

import br.com.tntbrasil.integracao.domains.financeiro.BoletoDMN;
import br.com.tntbrasil.integracao.domains.financeiro.BoletoDetalheDMN;

/**
 * Classe de serviï¿½o para CRUD:   
 *
 * 
 * Nï¿½o inserir documentaï¿½ï¿½o apï¿½s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviï¿½o.
 * @spring.bean id="lms.contasreceber.boletoService"
 */
public class BoletoService extends CrudService<Boleto, Long> {

	private CalcularJurosDiarioService calcularJurosDiarioService;
	private ConfiguracoesFacade configuracoesFacade;
	private FaturaService faturaService;
	private GerarBoletoBoletoService gerarBoletoFaturaService;
	private GerarBoletoIntegracaoService gerarBoletoIntegracaoService;
	private GerarBoletoFaturamentoService gerarBoletoFaturamentoService;
	private CedenteService cedenteService;
	private HistoricoBoletoService historicoBoletoService;
	private ProrrogarVencimentoBoletoService prorrogarVencimentoBoletoService;
	private ParametroGeralService parametroGeralService;	
	private ParametroBoletoFilialService parametroBoletoFilialService;
	private EnderecoPessoaService enderecoPessoaService;
	
	private JdbcTemplate jdbcTemplate;
	private static final Logger LOGGER = LogManager.getLogger(BoletoService.class);
	
	/**
	 * Recupera uma instï¿½ncia de <code>Boleto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instï¿½ncia que possui o id informado.
	 */
    @Override
	public Boleto findById(java.lang.Long id) {
        return (Boleto)super.findById(id);
    }
    
    /**
     * Retorna o boleto do id informado com relacionamento para a tela
     * 
     * @author Mickaï¿½l Jalbert
     * @since 19/04/2006
     * 
     * @param Long idBoleto
     * @return Boleto
     * */
    public Boleto findByIdTela(Long idBoleto) {
    	
    	
        return getBoletoDAO().findByIdTela(idBoleto);
    }    
    
    /**
     * Retorna o boleto ativo por fatura
     * 
     * @param Long idFatura
     * @return Boleto
     * */
    public Boleto findByFatura(Long idFatura){
    	List lstBoleto = this.getBoletoDAO().findByFatura(idFatura);
    	
    	if (lstBoleto.size() == 1) {
    		return (Boleto) lstBoleto.get(0);
    	} else {
    		return null;
    	}
    }     

    public List<Boleto> findByFaturas(List<Long> idFaturas){
    	return getBoletoDAO().findByFaturas(idFaturas);
    }     

    public List<Boleto> findByRedeco(Long idRedeco){
    	return getBoletoDAO().findByRedeco(idRedeco);
    }     
    
    
    /**
     * Retorna a situaï¿½ï¿½o do boleto diferente de cancelado.
     * 
     * @author Mickaï¿½l Jalbert
     * @since 29/08/2006
     * 
     * @param Long idFatura
     * @return String
     * */
    public String findTpSituacaoByFatura(Long idFatura){
    	return getBoletoDAO().findTpSituacaoByFatura(idFatura);
    }     
    
    public ResultSetPage findPaginated(BoletoParam boletoParam, FindDefinition findDef) {
    	return getBoletoDAO().findPaginated(boletoParam, findDef);
    }
    
    public Integer getRowCount(BoletoParam boletoParam) {
    	return getBoletoDAO().getRowCount(boletoParam);
    }
    
	/**
	 * Apaga uma entidade atravï¿½s do Id.
	 *
	 * @param id indica a entidade que deverï¿½ ser removida.
	 */
    @Override
	public void removeById(java.lang.Long id) {
    	Boleto boleto = findById(id);
        getDao().remove(boleto);
    }

	/**
	 * Apaga vï¿½rias entidades atravï¿½s do Id.
	 *
	 * @param ids lista com as entidades que deverï¿½o ser removida.
	 *
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
		for (Object idBoleto : ids) {
			removeById((Long)idBoleto);
    }
    }
    
    @Override
    protected Boleto beforeInsert(Boleto bean) {
    	Boleto boleto = bean;
    	
    	//Sï¿½ pode gerar boleto com cedente ativo se nï¿½o estiver rodando pela Integraï¿½ï¿½o
    	if(!SessionUtils.isIntegrationRunning()){
    	if (!cedenteService.isCedenteAtivo(boleto.getCedente().getIdCedente())){
    		throw new BusinessException("LMS-36234");
    	}
    	}
    	
    	return super.beforeInsert(bean);
    }
    
    @Override
	protected Boleto beforeStore(Boleto bean) {
	    
    	Boleto boleto = validateBeforeStore(bean);
    	
    	if(!SessionUtils.isIntegrationRunning() || boleto.getTpSituacaoBoleto().getValue().equals("LI")){
    		atualizaFatura(boleto);
    	}
    	
    	return super.beforeStore(bean);
    }

	private Boleto validateBeforeStore(Boleto bean) {
    	Boleto boleto = (Boleto)bean;
    	
    	// Datas que no request
    	YearMonthDay dtVencimentoNovo = boleto.getDtVencimentoNovo();
    	YearMonthDay dtVencimento = boleto.getDtVencimento();    
	    
    	// Caso esteja sendo feito update
    	if( boleto.getIdBoleto() != null ){
    		
    		// Busca as datas de vencimento do BD
    		Object[] vencimentosOld = getBoletoDAO().findDtVencimentoBoleto(boleto.getIdBoleto());
    		
    		Boolean throwException = false;
    		
    		// Datas que vem do banco
    		YearMonthDay dtVencimentoNovoOld = (YearMonthDay) vencimentosOld[1];
    		YearMonthDay dtVencimentoOld = (YearMonthDay) vencimentosOld[0];
    		
    		// Caso a data de vencimento que estï¿½ no banco e a data que vem da tela nï¿½o sejam iguais, lanï¿½a a exception
    		if( JTDateTimeUtils.comparaData(dtVencimento, dtVencimentoOld) != 0 
    				&& JTDateTimeUtils.comparaData(dtVencimento, JTDateTimeUtils.getDataAtual().plusDays(5)) < 0 ){
    			throwException = true;
    		}
    		
    		// Caso a data de vencimentoNovo que estï¿½ no banco e a data que vem da tela nï¿½o sejam nulas nem iguais, lanï¿½a a exception
    		if( (dtVencimentoNovo != null && dtVencimentoNovoOld != null) 
    				&& (JTDateTimeUtils.comparaData(dtVencimentoNovo, dtVencimentoNovoOld) != 0)
    				&& JTDateTimeUtils.comparaData(dtVencimentoNovo, JTDateTimeUtils.getDataAtual().plusDays(5)) < 0 ){
    			throwException = true;
    		
    		// Caso apenas a data de vencimentoNovo que vem da tela nï¿½o seja nula
    		}else if( (dtVencimentoNovo != null) 
    				&& JTDateTimeUtils.comparaData(dtVencimentoNovo, JTDateTimeUtils.getDataAtual().plusDays(5)) < 0 ){
    			throwException = true;
    		}
    		
    		if(!SessionUtils.isIntegrationRunning() && !boleto.isBrokerIntegration()){
	    		// Caso a variavel seja truee nao estiver rodando pela integracao, lanï¿½a a exception
    		if( throwException ){
    			throw new BusinessException("LMS-36232");
    		}
    		}
    	// Caso esteja inserindo um novo boleto
    	}else{
    		if(!SessionUtils.isIntegrationRunning() && !boleto.isBrokerIntegration())
	    		if( JTDateTimeUtils.comparaData(dtVencimento, JTDateTimeUtils.getDataAtual().plusDays(5)) < 0 ){
    		// A data de vencimento do boleto maior ou igual que a data atual acrescida de cinco dias
    			throw new BusinessException("LMS-36232");
    		}
    		
    	}
		return boleto;
    	}
    	
    public Boleto storeProrrogarVencimentoBoleto(Boleto bean) {
    	
    	validateBeforeStore(bean);
    	
    	return super.beforeStore(bean);
    }
    
    /**
     * 
     * 
     * Hector Julian Esnaola Junior
     * 07/01/2008
     *
     * @param tpSituacaoBoleto
     * @param tpFinalidade 
     * @return
     *
     * Boolean
     *
     */
    public Boolean validateTpSituacaoBoleto(String tpSituacaoBoleto, String tpFinalidade){
    	Boolean retorno = Boolean.FALSE;
    	// Caso a situaï¿½ï¿½o seja BN, BP, EM ou DI retorna true.
    	if ("BN".equals(tpSituacaoBoleto) 
    			|| "BP".equals(tpSituacaoBoleto)
    			|| "DI".equals(tpSituacaoBoleto)
    			|| "EM".equals(tpSituacaoBoleto)) {
    		retorno = Boolean.TRUE;
    	}
		if (("LI".equals(tpSituacaoBoleto)) && 
			("PR".equals(tpFinalidade)
				|| "DR".equals(tpFinalidade)
				|| "JU".equals(tpFinalidade)
				|| "OR".equals(tpFinalidade))) {
			retorno = Boolean.TRUE;
		}
    	return retorno;
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrï¿½rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    @Override
	public Boleto store(Boleto boleto) {
        super.store(boleto);
        
        boleto.getFatura().setBoleto(boleto);
        
        faturaService.storeBasic(boleto.getFatura());
        
        return boleto;
    }    
    

    /**
     * Overload do mï¿½todo para manter a integridade
     * 
     * @author ï¿½caro Damiani
     * @since 22/09/2014
     * @param fatura
     */
    public void generateBoletoDeFatura(Fatura fatura){
    	generateBoletoDeFatura(fatura,null,null,false);
    }
    
    /**
     * Gera uma boleto para a fatura informada. CUIDADO, o objeto Fatura tem
     * que ser persistente!
     * 
     * @author Mickaï¿½l Jalbert
     * @since 20/03/2006
     * 
     * @param Fatura fatura
     * @param YearMonthDay emissao
     * @param YearMonthDay vencimento
     * @return 
     * 
     * @return Boleto
     */
    public Boleto generateBoletoDeFatura(Fatura fatura, YearMonthDay emissao, YearMonthDay vencimento,boolean isBatch){
    	Boleto boleto = findByFatura(fatura.getIdFatura());
    	
    	//Se jï¿½ tem boleto, nï¿½o precisa gerar
    	if (boleto != null){
    		return null;
    	}
    	
    	boleto = prepareBoleto(fatura,emissao,vencimento);
    	
    	return this.gerarBoletoFaturaService.insertBoleto(boleto,isBatch);
    }

    /**
     * Mï¿½todo utilizado pela integraï¿½ï¿½o
     *
     * @author Hector Julian Esnaola Junior
     * @since 26/06/2007
     *
     * @param fatura
     *
     */
    public Boleto generateBoletoDeFaturaIntegracao(Boleto boleto, Fatura fatura){
    	
    	boleto.setBlBoletoReemitido(Boolean.FALSE);
    	boleto.setCedente(fatura.getCedente());
    	boleto.setDtEmissao(fatura.getDtEmissao());
    	boleto.setDtVencimento(fatura.getDtVencimento());
    	boleto.setTpSituacaoBoleto(new DomainValue("EM"));
    	boleto.setVlTotal(fatura.getVlTotal());
    	boleto.setVlDesconto(fatura.getVlDesconto());
    	boleto.setVlJurosDia(this.calcularJurosDiarioService.calcularVlJuros(fatura, new BigDecimal(1)));
    	boleto.setFatura(fatura);

    	return this.gerarBoletoIntegracaoService.insertBoleto(boleto);
    }

    
    /**
     * Gera uma boleto para a fatura informada. CUIDADO, o objeto Fatura tem
     * que ser persistente!
     * 
     * @author Mickaï¿½l Jalbert
     * 20/03/2006
     * 
     * @param Fatura fatura
     * @param Long nrItemFatura
     * @return Boleto
     * */
    public void generateBoletoDeFaturamento(Fatura fatura){
    	Boleto boleto = findByFatura(fatura.getIdFatura());
    	
    	//Se jï¿½ tem boleto, nï¿½o precisa gerar
    	if (boleto != null){
    		return;
    	}
    	
    	boleto = prepareBoleto(fatura);
    	
    	this.gerarBoletoFaturamentoService.store(boleto);
    } 
    
    
    private Boleto prepareBoleto(Fatura fatura) {
    	return prepareBoleto(fatura,null,null);
    }
	/**
	 * @param fatura
	 * @param nrItemFatura
	 * @return
	 */
	private Boleto prepareBoleto(Fatura fatura, YearMonthDay emissao, YearMonthDay vencimento) {
		Boleto boleto;
		boleto = new Boleto();
    	
    	boleto.setBlBoletoReemitido(Boolean.FALSE);
    	
    	if (fatura.getQtDocumentos() != null && fatura.getQtDocumentos().intValue() == 1){
    		boleto.setBlBoletoConhecimento(Boolean.TRUE);
    	} else {
    		boleto.setBlBoletoConhecimento(Boolean.FALSE);
    	}
    	
    	boleto.setCedente(fatura.getCedente());
    	boleto.setDtEmissao(emissao == null?fatura.getDtEmissao():emissao);
    	boleto.setDtVencimento(vencimento == null?fatura.getDtVencimento():vencimento);
    	boleto.setTpSituacaoBoleto(new DomainValue("DI"));
    	boleto.setVlTotal(fatura.getVlTotal());
    	boleto.setVlDesconto(fatura.getVlDesconto());
    	boleto.setVlJurosDia(this.calcularJurosDiarioService.calcularVlJuros(fatura, new BigDecimal(1)));
    	boleto.setNrSequenciaFilial(this.findNextNrSequenciaFilial(fatura.getFilialByIdFilial().getIdFilial()));
    	boleto.setFatura(fatura);
    	boleto.setVersao(0);
    	
    	// O usuï¿½rio reemissï¿½o sï¿½ deve ser setado na reemissï¿½o de boletos
    	//boleto.setUsuario(SessionUtils.getUsuarioLogado());
    	
		return boleto;
	}    
    
    /**
     * Atualizar a fatura, setar a situaï¿½ï¿½o para 'Em Boleto', a data de vencimento igual a do boleto e 
     * a data de transmissï¿½o igual null 
     * 
     * @author Mickaï¿½l Jalbert
     * @since 20/04/2006
     * 
     * @param Boleto boleto
     */
    private void atualizaFatura(Boleto boleto){
    	Fatura fatura = boleto.getFatura();    	
    	fatura.setTpSituacaoFatura(new DomainValue("BL"));
    	fatura.setDtVencimento(boleto.getDtVencimento());
    	fatura.setDhTransmissao(null);
    	faturaService.store(fatura);
    }    
    
    /**
	 * Atualiza o campo tpSituacaoAntBoleto com o campo tpSituacaoBoleto do boleto da fatura informada.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 02/03/2007
	 *
	 * @param idBoleto
	 * @param tpSituacaoBoleto
	 *
	 */
	public void updateSituacaoBoletoByIdBoleto(Long idBoleto, String tpSituacaoBoleto){
		getBoletoDAO().updateSituacaoBoletoByIdBoleto(idBoleto, tpSituacaoBoleto);
	}
	/**
	 * Retorna o novo nï¿½mero de Boleto a partir da filial informada.
	 * 
	 * @author Mickaï¿½l Jalbert
	 * @since 20/03/2006
	 * 
	 * @param Long idFilial
	 * @return Long
	 */
	public Long findNextNrSequenciaFilial(Long idFilial) {
		return this.configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_BOLETO", false);
	}
	
	/**
	 * Atualiza o campo tpSituacaoAntBoleto com o campo tpSituacaoBoleto do boleto da fatura informada.
	 * 
	 * @author Mickaï¿½l Jalbert
	 * @since 23/10/2006
	 * 
	 * @param Long idFatura
	 */
	public void updateSituacaoBoleto(Long idFatura, String tpSituacaoBoleto){
		getBoletoDAO().updateSituacaoBoleto(idFatura, tpSituacaoBoleto);
	}	
	
	/**
	 * Atualiza o campo tpSituacaoBoleto com o campo tpSituacaoAntBoleto do boelto da fatura informada.
	 * 
	 * @author Mickaï¿½l Jalbert
	 * @since 23/10/2006
	 * 
	 * @param Long idFatura
	 */
	public void updateSituacaoBoletoAnterior(Long idFatura){
		getBoletoDAO().updateSituacaoBoletoAnterior(idFatura);
	}
	
    
    /**
     * Salva o boleto usando o hibernate puro para optimizar o tempo de inserï¿½ï¿½o 
     * 
     * @author Hector Junior
     * @since 07/08/2006
     * 
     * @param boleto
     * 
     * @return boleto
     */    
    public Boleto storeBasic(Boleto boleto){
    	return getBoletoDAO().storeBasic(boleto);
    }
    
    /**
	 * Busca o boleto filtrando pelo idFilialOrigem da fatura e pelo nrSequenciaFilial
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 30/05/2007
	 *
	 * @param idFilialOrigem
	 * @param nrSequenciaFilial
	 * @return
	 *
	 */
	public Boleto findByIdFilialOrigemAndNrSequenciaFilial(Long idFilialOrigem, Long nrSequenciaFilial){
		return getBoletoDAO().findByIdFilialOrigemAndNrSequenciaFilial(idFilialOrigem, nrSequenciaFilial);
	}
	
	public void evict(Boleto boleto){
		getBoletoDAO().getAdsmHibernateTemplate().evict(boleto);
	}
	
	/**
	 * Busca o boleto filtrando pelo nrBoleto
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 21/06/2007
	 *
	 * @param nrBoleto
	 * @return
	 *
	 */
	public Boleto findByNrBoleto(String nrBoleto){
		return getBoletoDAO().findByNrBoleto(nrBoleto);
	}

	public List<Long> findFaturasBoletoByCriteria(TypedFlatMap parameters){
		try {
			return getBoletoDAO().findFaturasBoletoByCriteria(parameters);
			
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new InfrastructureException("Erro ao gerar a query do relatorio: "+e.getMessage());
			
		}
	}

	/**
	 * LMS-6107
	 * 
	 * @author ï¿½caro Damiani
	 * 
	 * @param fatura
	 */
	public void criaBoleto(Fatura fatura) {
		YearMonthDay hoje = new YearMonthDay();
		YearMonthDay emissaoNova = hoje;
		YearMonthDay vencimentoNovo = hoje;
		
		Long diferenca = buscaDiferencaDias(fatura.getDtVencimento(), hoje);
		BigDecimal diasAntesVencimento =  (BigDecimal) configuracoesFacade.getValorParametro("NR_DIAS_ANTES_VENCIMENTO");
		
		if(diferenca.compareTo(diasAntesVencimento.longValue()) <= 0){
			BigDecimal diasVenciamento =(BigDecimal) configuracoesFacade.getValorParametro("NR_DIAS_NOVO_VENCIMENTO");
			
			vencimentoNovo = vencimentoNovo.plusDays(diasVenciamento.intValue());
		} else {
			vencimentoNovo = fatura.getDtVencimento();
		}
		
		generateBoletoDeFatura(fatura,emissaoNova,vencimentoNovo,false);
			
		fatura.setDtVencimento(vencimentoNovo);
		fatura.setTpSituacaoFatura(new DomainValue("BL"));
			
		faturaService.store(fatura);

	}

	private long buscaDiferencaDias(YearMonthDay vencimento, YearMonthDay hoje) {
		Long vencimentoMillis = vencimento.toDateTimeAtCurrentTime().getMillis();
		Long todayMillis = hoje.toDateTimeAtCurrentTime().getMillis();
		Long diferenca  = vencimentoMillis - todayMillis;
		
		return TimeUnit.MILLISECONDS.toDays(diferenca);
	}
	
	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}

	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public HistoricoBoletoService getHistoricoBoletoService() {
		return historicoBoletoService;
	}

	public void setProrrogarVencimentoBoletoService(
			ProrrogarVencimentoBoletoService prorrogarVencimentoBoletoService) {
		this.prorrogarVencimentoBoletoService = prorrogarVencimentoBoletoService;
	}

	public ProrrogarVencimentoBoletoService getProrrogarVencimentoBoletoService() {
		return prorrogarVencimentoBoletoService;
	}
	
	/**
	 * Atribui o DAO responsï¿½vel por tratar a persistï¿½ncia dos dados deste serviï¿½o.
	 * 
	 * @param Instï¿½ncia do DAO.
	 */
	public void setBoletoDAO(BoletoDAO dao) {
		setDao( dao );
        }
	
	/**
	 * Retorna o DAO deste serviï¿½o que ï¿½ responsï¿½vel por tratar a persistï¿½ncia dos dados deste serviï¿½o.
	 *
	 * @return Instï¿½ncia do DAO.
	 */
	private BoletoDAO getBoletoDAO() {
		return (BoletoDAO) getDao();
	}
	
	
	public Boleto findFirstByFaturaAndSituacaoBoleto(Long idFatura, String situacaoBoleto) {
		return getBoletoDAO().findFirstByFaturaAndSituacaoBoleto(idFatura, situacaoBoleto);
	}
	
	public void setCalcularJurosDiarioService(
			CalcularJurosDiarioService calcularJurosDiarioService) {
		this.calcularJurosDiarioService = calcularJurosDiarioService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
	
	public void setGerarBoletoFaturamentoService(
			GerarBoletoFaturamentoService gerarBoletoFaturamentoService) {
		this.gerarBoletoFaturamentoService = gerarBoletoFaturamentoService;
	}
	
	public void setGerarBoletoFaturaService(
			GerarBoletoBoletoService gerarBoletoFaturaService) {
		this.gerarBoletoFaturaService = gerarBoletoFaturaService;
	}
	
	public void setGerarBoletoIntegracaoService(GerarBoletoIntegracaoService gerarBoletoIntegracaoService) {
		this.gerarBoletoIntegracaoService = gerarBoletoIntegracaoService;
	}

	//------------------------------ M�todos relacionados a gera��o dos boletos (Report)

	/**
	 * Retorna dados para o Report interno (LMS) 	
	 * @param criteria
	 * 
	 * @return {@link SqlTemplate}
	 */
	public SqlTemplate mountSqlBoleto(TypedFlatMap criteria) {
		return getBoletoDAO().mountSqlBoleto(criteria);
	}
	
	public List executeReport(Map parametersReport) {
		
		SqlTemplate sql = (SqlTemplate)parametersReport.get("sql");
		Boolean reemissao = (Boolean)parametersReport.get("reemissao");
		
		List<Map> mapBoletos = getBoletoDAO().iteratorSelectBoleto(sql);
		for (Map mapBoleto : mapBoletos) {
			
			Long idBoleto = (Long) mapBoleto.get("ID_BOLETO");
			Short nrBanco = (Short) mapBoleto.get("NR_BANCO");
			Long qtDocumentos = (Long) mapBoleto.get("QT_DOCUMENTOS");
			String tpSituacaoBoleto = (String) mapBoleto.get("TP_SITUACAO_BOLETO");
			
			Boleto boleto = findById(idBoleto);

			/** Caso a situação do boleto seja DI, gera um histórico */
			if( "DI".equals(tpSituacaoBoleto)){
				boleto.setTpSituacaoAntBoleto(boleto.getTpSituacaoBoleto());
				boleto.setTpSituacaoBoleto(new DomainValue("EM"));

				/** Salva um registro na tabela historicoBoleto */
				storeHistoricoBoleto(boleto, nrBanco);
			}

			/**
			 * VALIDA SE É REEMISSÃO PARA REALIZAR UPDATE NO BOLETO
			 */
			if (reemissao) {
				boleto.setBlBoletoReemitido(Boolean.TRUE);
				boleto.setUsuario(SessionUtils.getUsuarioLogado());
				boleto.setDhReemissao(JTDateTimeUtils.getDataHoraAtual());
			}
			getBoletoDAO().store(boleto);
			
			Object[] parameters = {
				idBoleto, 
				nrBanco, 
				qtDocumentos, 
				tpSituacaoBoleto, 
				reemissao, 
				(Date)parametersReport.get("DT_VENCIMENTO_FUTURO"), 
				parametersReport.get("VALOR_COBRADO"),
				(Boolean)parametersReport.get("BL_ATUALIZA_VCTO_CARTA_COB"),
				parametersReport.get("PC_MULTA_ATRASO_BOLETO")
			};
			
			mapBoleto.put("DADOS", iteratorSubSelectBoleto(mountSqlSubReportBoleto(idBoleto), qtDocumentos, parameters));
		}
		return mapBoletos;
	}
	
	public SqlTemplate mountSqlSubReportBoleto(Long idBoleto) {
		return getBoletoDAO().mountSqlSubReportBoleto(idBoleto);
	}
	
	/* ##################### FATOR VENCIMENTO BOLETO #################### */

	/**
	 * M�todo responsável por calcular o fator de vencimento do boleto
	 * 
	 * @author HectorJ
	 * @since 09/06/2006
	 * 
	 * @param YearMonthDay dtVencimento
	 * @return String fatorVencBoleto
	 * 
	 */
	private String calcFatorVencimentoBoleto(YearMonthDay dtVencimento){
		/** Subatrai a data de vencimento do boleto de 07/10/1997 e retorna a diferen�a em dias */
		String fatorVencBoleto = String.valueOf(JTDateTimeUtils.getIntervalInDays(new YearMonthDay(1997, 10, 7), dtVencimento));

		/** Extrai os quatro primerios digitos caso tenha mais */
		if(StringUtils.isNotBlank(fatorVencBoleto) && fatorVencBoleto.length() > 4){
			fatorVencBoleto = fatorVencBoleto.substring(0, 4);
			
		}

		/** Formata a diferença de dias obtida anteriormente no formatato 0000 */
		fatorVencBoleto = FormatUtils.completaDados(fatorVencBoleto, "0", 4, 0, true);

		return fatorVencBoleto;
		
	}

	/* ##################### MODULO 10 #################### */
	
	/**
	 * M�todo responsável por calcular o dígito utilizando o módulo 10
	 * 
	 * @author HectorJ
	 * @since 09/06/2006
	 * 
	 * @param String tpCalculo
	 * @param Long codigo
	 * 
	 * @return Integer digito
	 */
	private Integer calcDigitoModDez(Integer tpCalculo, String codigoStr){
		/** Vari�vel que armazena o digito */
		Integer digito;

		/** Resultado de todo calculo */
		BigDecimal result = iterateByTpCalculo(codigoStr, tpCalculo);

		/** Realiza um calculo para chegar ao valor do digito */
		digito = Integer.valueOf(new BigDecimal(10).subtract(result.remainder(new BigDecimal(10))).toString());

		/** Caso o digito seja maior que nove, lhe � atribuído o valor zero */
		if(digito.intValue() > 9){
			digito = Integer.valueOf(0);
		}

		return digito;
	}

	/**
	 * M�todo responsável por calcular o dígito por partes 
	 * 
	 * @author HectorJ
	 * @since 09/06/2006
	 * 
	 * @param String codigoStr
	 * @param Integer tpCalculo
	 * 
	 * @return BigDecimal result
	 */
	private BigDecimal iterateByTpCalculo(String codigoStr, Integer tpCalculo) {
		BigDecimal result = new BigDecimal(0);

		// Se for cálculo 10, inicia multiplicando por 1, caso contrário inicia por 2
		int multiply = 2;
		if (tpCalculo.intValue() == 10) {
			multiply = 1;
		}

		/** Realiza o número de iterações de tpCalculo */
		for(int i = 0; i < tpCalculo.intValue(); i++){

			/** Soma a variavel result o valor retornado do M�todo calcSumDigitoModDez() */
			result = result.add(calcSomaDigitoModDez(codigoStr
					, new BigDecimal(multiply)
					, i
					, i + 1
					, new BigDecimal(9)
					, new BigDecimal(10)));

			// Linha a linha, alterna o multiplicador
			multiply = (multiply==1) ? 2 : 1;
		}
		return result;
	}

	/**
	 * Calcula o dígito por partes 
	 * 
	 * @author HectorJ
	 * @since 09/06/2006
	 * 
	 * @param String codigoStr
	 * @param BigDecimalnr Multiply
	 * @param int substringBegin
	 * @param int substringEnd
	 * @param BigDecimal nrSubtract
	 * @param BigDecimal nrCompare
	 * 
	 * @return BigDecimal codigo
	 */
	private BigDecimal calcSomaDigitoModDez(
			String codigoSt,
			BigDecimal nrMultiply,
			int substringBegin,
			int substringEnd,
			BigDecimal nrSubtract,
			BigDecimal nrCompare
	) {
		/** Substring para extrair a parte necess�ria do c�digo */
		String codigoStr = codigoSt.substring(substringBegin, substringEnd);

		/** Cria um BigDecimal a partir do codigo(String) */
		BigDecimal codigo = new BigDecimal(codigoStr);

		/** Avalia se codigo multiplicado por nrMultiply, � menor que nrCompare*/
		if(codigo.multiply(nrMultiply).compareTo(nrCompare) == -1){
			/** codigo multiplicado por nrMultiply */
			codigo = codigo.multiply(nrMultiply);
		} else {
			/** codigo multiplicado por nrMultiply menos nrSubtract */
			codigo = codigo.multiply(nrMultiply).subtract(nrSubtract);
		}

		return codigo;
	}

	/* ##################### MODULO 11 #################### */

	/**
	 * M�todo responsável por calcular o dígito utilizando o módulo 11
	 * 
	 * @author HectorJ
	 * @since 09/06/2006
	 * 
	 * @param String codigoStr
	 * @param Integer digitoDez
	 * 
	 * @return Integer digito
	 */
	private String calcDigitoModOnze(String codigoStr, Integer digitoDez){
		/** Vari�vel que armazena o digito */
		Integer digito = digitoDez;

		/** Resultado de todo calculo */
		BigDecimal result = iterateDigitoModOnze(codigoStr);

		/** Atribui ao digitoDez o resultado do calculo */
		Integer digitoDz = Integer.valueOf(new BigDecimal(11).subtract(result.remainder(new BigDecimal(11))).toString());

		/** Valida se a Vari�vel digitoDez � maior que 10 */
		if(digitoDz.intValue() > 10){
			digitoDz = Integer.valueOf(0);
		}

		/** Executa o bloco de c�digo enquanto digitoDez for igual a 10 */
		while(digitoDz.intValue() == 10){
			/** Se digito for igual 9 */
			if(digito.intValue() == 9){
				/** Seta o valor 0 a Vari�vel digito */
				digito = IntegerUtils.ZERO;
			} else {
				/** Incrementa digito */
				digito = IntegerUtils.incrementValue(digito);
			}

			/** Substitui a �ltima posi��o de codigoStr por digito */
			String codigoSt = codigoStr.substring(0, 23) + digito;

			/** Resultado de todo calculo */
			result = iterateDigitoModOnze(codigoSt);

			/** Atribui ao digitoDez o resultado do calculo */
			digitoDz = Integer.valueOf(new BigDecimal(11).subtract(result.remainder(new BigDecimal(11))).toString());
		}

		/** Extrai a primeira posi��o das variaveis digito e dititoDez para serem somadas posteriormente */
		int digitoTmp = Integer.parseInt(digito.toString().substring(0, 1));
		int digitoDezTmp = Integer.parseInt(digitoDz.toString().substring(0, 1));

		return ("" + digitoTmp + digitoDezTmp);
	}

	/**
	 * M�todo responsável por calcular o dígito por partes 
	 * 
	 * @author HectorJ
	 * @since 09/06/2006
	 * 
	 * @param String codigoStr
	 * @param Integer digitoDez
	 * 
	 * @return BigDecimal result
	 */
	private BigDecimal iterateDigitoModOnze(String codigoStr){
		BigDecimal result = new BigDecimal(0);
		BigDecimal nrMultiply = new BigDecimal(2);

		/** Realiza o número de iterações de tpCalculo */
		for(int i = 24; i > 0; i--){
			/** Valida se o nrMultiply não � maior que sete, caso seja, atribui o valor dois a mesmo */
			if(nrMultiply.compareTo(new BigDecimal(7)) == 1){
				nrMultiply = new BigDecimal(2);
			}

			/** Soma a variavel result o valor retornado do M�todo calcSumDigitoModDez() */
			result = result.add(calcSomaDigitoModOnze(codigoStr
					  , i -1
					  , i
					  , nrMultiply));

			/** Incrementa a Vari�vel nrMultiply */
			nrMultiply = nrMultiply.add(new BigDecimal(1));

		}

		return result;
	}

	/**
	 * Calcula o dígito por partes 
	 * 
	 * @author HectorJ
	 * @since 09/06/2006
	 * 
	 * @param String codigoStr
	 * @param BigDecimalnr Multiply
	 * @param int substringBegin
	 * @param int substringEnd
	 * 
	 * @return BigDecimal codigo
	 */
	private BigDecimal calcSomaDigitoModOnze(String codigoStr
								, int substringBegin
								, int substringEnd
								, BigDecimal nrMultiply){

		/** Substring para extrair a parte necess�ria do c�digo */
		
		String codigoSt = codigoStr.substring(substringBegin, substringEnd);

		/** Cria um BigDecimal a partir do codigo(String) */
		BigDecimal codigo = new BigDecimal(codigoSt);

		/** Multiplica o codigo por nrMultiply */
		codigo = codigo.multiply(nrMultiply);

		return codigo;
	}

	/* ##################### MODULO 11 DAC #################### */

	/**
	 * M�todo responsável por calcular o dígito utilizando o módulo 11 DAC
	 * 
	 * @author HectorJ
	 * @since 09/06/2006
	 * 
	 * @param String codigoStr
	 * 
	 * @return Integer digito
	 */
	private Integer calcDigitoModOnzeDac(String codigoStr){
		/** Resultado de todo calculo */
		BigDecimal result = iterateDigitoModOnzeDac(codigoStr);

		/** Atribui a Vari�vel digito o resultado do calculo */
		Integer digito = Integer.valueOf(result.remainder(new BigDecimal(11)).toString());

		/** Caso seja igual a um ou zero */
		if(digito.intValue() == 0 || digito.intValue() == 1){
			digito = Integer.valueOf(1);
		/** Caso seja diferente de um e de zero */
		} else {
			digito = Integer.valueOf(11 - digito.intValue());
		}

		return digito;
	}

	/**
	 * M�todo responsável por calcular o dígito por partes 
	 * 
	 * @author HectorJ
	 * @since 09/06/2006
	 * 
	 * @param String codigoStr
	 * 
	 * @return BigDecimal result
	 */
	private BigDecimal iterateDigitoModOnzeDac(String codigoStr){
		BigDecimal result = new BigDecimal(0);
		BigDecimal nrMultiply = new BigDecimal(2);

		/** Realiza o número de iterações de tpCalculo */
		for(int i = 43; i > 0; i--){
			/** Valida se o nrMultiply não � maior que sete, caso seja, atribui o valor dois a mesmo */
			if(nrMultiply.compareTo(new BigDecimal(9)) == 1){
				nrMultiply = new BigDecimal(2);
			}

			/** Soma a variavel result o valor retornado do M�todo calcSumDigitoModDez() */
			result = result.add(calcSomaDigitoModOnzeDac(codigoStr
					  , i -1
					  , i
					  , nrMultiply));

			/** Incrementa a Vari�vel nrMultiply */
			nrMultiply = nrMultiply.add(new BigDecimal(1));
		}

		return result;
	}

	/**
	 * Calcula o dígito por partes 
	 * 
	 * @author HectorJ
	 * @since 09/06/2006
	 * 
	 * @param String codigoStr
	 * @param BigDecimalnr Multiply
	 * @param int substringBegin
	 * @param int substringEnd
	 * 
	 * @return BigDecimal codigo
	 */
	private BigDecimal calcSomaDigitoModOnzeDac(String codigoStr
								, int substringBegin
								, int substringEnd
								, BigDecimal nrMultiply){

		/** Substring para extrair a parte necess�ria do c�digo */
		String codigoSt = codigoStr.substring(substringBegin, substringEnd);

		/** Cria um BigDecimal a partir do codigo(String) */
		BigDecimal codigo = new BigDecimal(codigoSt);

		/** Multiplica o codigo por nrMultiply */
		codigo = codigo.multiply(nrMultiply);

		return codigo;
	}
	
	/* ##################### C�DIGO BARRAS E LINHA DIGIT�VEL BANRISUL #################### */

	/**
	 * Forma o c�digo de barras e a linha digitável Banrisul
	 * 
	 * @author HectorJ
	 * @since 13/06/2006
	 * 
	 * @param Short nrBanco
	 * @param YearMonthDay dtVencBoleto
	 * @param BigDecimal vlTotalRecibo
	 * @param Short nrAgenciaBancaria
	 * @param Long cdCedente
	 * @param String nrBoleto
	 * 
	 * @return List lst
	 */
	private List codBarrasAndLinhaDigitavelBanrisul(Short nrBanco
								, YearMonthDay dtVencBoleto
								, BigDecimal vlTotalRecibo
								, Short nrAgenciaBancaria
								, Long cdCedente
								, String nrBoleto){

		List lst = new ArrayList();
		String nrFatVencimento;
		StringBuilder cdBarras = new StringBuilder();
		StringBuilder cdLinhaDigitavel = new StringBuilder();
		String dvBarras1;
		String dvBarras2;
		String nrDac;
		String dvLinha1;
		String dvLinha2;
		String dvLinha3;

		String nrBancoStr = FormatUtils.completaDados(nrBanco, "0", 3, 0, true);
		String nrAgenciaBancariaStr = FormatUtils.completaDados(nrAgenciaBancaria, "0", 3, 0, true);
		String cdCedenteStr = FormatUtils.completaDados(cdCedente, "0", 12, 0, true);
		String vlTotalReciboStr = FormatUtils.completaDados(vlTotalRecibo.multiply(new BigDecimal(100)).longValue(), "0", 10, 0, true);

		nrFatVencimento = calcFatorVencimentoBoleto(dtVencBoleto);

		/** CÒDIGO DE BARRAS */
		cdBarras.append(nrBancoStr);
		cdBarras.append("90");
		cdBarras.append(nrFatVencimento);
		cdBarras.append(vlTotalReciboStr);
		cdBarras.append("21");
		cdBarras.append(nrAgenciaBancariaStr);
		cdBarras.append(cdCedenteStr.substring(3, 10));
		cdBarras.append(nrBoleto.substring(3, 11));
		cdBarras.append(nrBancoStr);

		/** Digitos verificador */
		dvBarras1 = calcDigitoModDez(Integer.valueOf(23), cdBarras.substring(19, 42)).toString();
		dvBarras2 = calcDigitoModOnze((cdBarras.substring(19, 42) + dvBarras1.substring(0, 1)), Integer.valueOf(dvBarras1));
		dvBarras1 = dvBarras2.substring(0, 1);
		dvBarras2 = dvBarras2.substring(1, 2);

		cdBarras.append((dvBarras1 + dvBarras2));

		nrDac = calcDigitoModOnzeDac((cdBarras.substring(0, 4) + cdBarras.substring(5, cdBarras.length()))).toString(); 

		cdBarras = new StringBuilder(cdBarras.substring(0, 4) + nrDac + cdBarras.substring(5, 44));

		dvLinha1 = calcDigitoModDez(Integer.valueOf(9), (nrBancoStr + "921" + nrAgenciaBancariaStr)).toString();
		dvLinha2 = calcDigitoModDez(Integer.valueOf(10), (cdCedenteStr.substring(3, 10) + nrBoleto.substring(3, 6))).toString();
		dvLinha3 = calcDigitoModDez(Integer.valueOf(10), (nrBoleto.substring(6, 11) + nrBancoStr + dvBarras1 + dvBarras2)).toString();

		/** LINHA DIGIT�VEL */
		cdLinhaDigitavel.append(nrBancoStr);
		cdLinhaDigitavel.append("92.1");
		cdLinhaDigitavel.append(nrAgenciaBancariaStr);
		cdLinhaDigitavel.append(dvLinha1.substring(0, 1));
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(cdCedenteStr.substring(3, 8));
		cdLinhaDigitavel.append(".");
		cdLinhaDigitavel.append(cdCedenteStr.substring(8, 10));
		cdLinhaDigitavel.append(nrBoleto.substring(3, 6));
		cdLinhaDigitavel.append(dvLinha2.substring(0, 1));
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrBoleto.substring(6, 11));
		cdLinhaDigitavel.append(".");
		cdLinhaDigitavel.append(nrBancoStr);
		cdLinhaDigitavel.append(dvBarras1);
		cdLinhaDigitavel.append(dvBarras2);
		cdLinhaDigitavel.append(dvLinha3.substring(0, 1));
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrDac.substring(0, 1));
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrFatVencimento);
		cdLinhaDigitavel.append(vlTotalReciboStr);

		/** Adiciona linha digitavel e codigo de barras ao List */
		lst.add(cdBarras);
		lst.add(cdLinhaDigitavel);

		return lst;
	}

	/* ##################### C�DIGO BARRAS E LINHA DIGIT�VEL BRADESCO #################### */

	/**
	 * Forma o c�digo de barras e a linha digitável Bradesco
	 * 
	 * @author HectorJ
	 * @since 13/06/2006
	 * 
	 * @param Short nrBanco
	 * @param YearMonthDay dtVencBoleto
	 * @param BigDecimal vlTotalRecibo
	 * @param Short nrAgenciaBancaria
	 * @param Long cdCedente
	 * @param Short nrCarteiraCedente
	 * @param String nrContaCorrenteCedente
	 * @param String nrBoleto
	 * 
	 * @return List lst
	 */
	private List codBarrasAndLinhaDigitavelBradesco(Short nrBanco
								, YearMonthDay dtVencBoleto
								, BigDecimal vlTotalRecibo
								, Short nrAgenciaBancaria
								, Short nrCarteiraCedente
								, String nrContaCorrenteCedente
								, String nrBoleto){

		List lst = new ArrayList();
		String nrFatVencimento;
		StringBuilder cdBarras = new StringBuilder();
		StringBuilder cdLinhaDigitavel = new StringBuilder();
		String nrDac;
		String dvLinha1;
		String dvLinha2;
		String dvLinha3;

		String nrBancoStr = FormatUtils.completaDados(nrBanco, "0", 3, 0, true);
		String nrAgenciaBancariaStr = FormatUtils.completaDados(nrAgenciaBancaria, "0", 4, 0, true);
		String nrBoletoStr = nrBoleto.substring(1, 12);
		String nrCarteiraCedenteStr = FormatUtils.completaDados(nrCarteiraCedente, "0", 2, 0, true).substring(0, 2);
		String nrContaCorrenteCedenteStr = FormatUtils.completaDados(nrContaCorrenteCedente, "0", 12, 0, true).substring(4, 11);
		String vlTotalReciboStr = FormatUtils.completaDados(vlTotalRecibo.multiply(new BigDecimal(100)).longValue(), "0", 10, 0, true);

		nrFatVencimento = calcFatorVencimentoBoleto(dtVencBoleto);

		/** CÒDIGO DE BARRAS */
		cdBarras.append(nrBancoStr);
		cdBarras.append("90");
		cdBarras.append(nrFatVencimento);
		cdBarras.append(vlTotalReciboStr);
		cdBarras.append(nrAgenciaBancariaStr);
		cdBarras.append(nrCarteiraCedenteStr);
		cdBarras.append(nrBoletoStr);
		cdBarras.append(nrContaCorrenteCedenteStr);
		cdBarras.append("0");

		nrDac = calcDigitoModOnzeDac((cdBarras.substring(0, 4) + cdBarras.substring(5, cdBarras.length()))).toString();

		cdBarras = new StringBuilder(cdBarras.substring(0, 4) + nrDac + cdBarras.substring(5, 44));

		dvLinha1 = calcDigitoModDez(Integer.valueOf(9), (cdBarras.substring(0, 4) + cdBarras.substring(19, 24))).toString();
		dvLinha2 = calcDigitoModDez(Integer.valueOf(10), (cdBarras.substring(24, 34))).toString();
		dvLinha3 = calcDigitoModDez(Integer.valueOf(10), (cdBarras.substring(34, 44))).toString();

		/** LINHA DIGIT�VEL */
		cdLinhaDigitavel.append(cdBarras.substring(0, 4));
		cdLinhaDigitavel.append(cdBarras.substring(19, 20));
		cdLinhaDigitavel.append(".");
		cdLinhaDigitavel.append(cdBarras.substring(20, 24));
		cdLinhaDigitavel.append(dvLinha1);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(cdBarras.substring(24, 29));
		cdLinhaDigitavel.append(".");
		cdLinhaDigitavel.append(cdBarras.substring(29, 34));
		cdLinhaDigitavel.append(dvLinha2);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(cdBarras.substring(34, 39));
		cdLinhaDigitavel.append(".");
		cdLinhaDigitavel.append(cdBarras.substring(39, cdBarras.length()));
		cdLinhaDigitavel.append(dvLinha3);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrDac);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrFatVencimento);
		cdLinhaDigitavel.append(vlTotalReciboStr);

		/** Adiciona linha digitavel e codigo de barras ao List */
		lst.add(cdBarras);
		lst.add(cdLinhaDigitavel);

		return lst;
	}

	/* ##################### C�DIGO BARRAS E LINHA DIGIT�VEL ITA� #################### */

	/**
	 * Forma o c�digo de barras e a linha digitável Bradesco
	 * 
	 * @author HectorJ
	 * @since 14/06/2006
	 * 
	 * @param Short nrBanco
	 * @param YearMonthDay dtVencBoleto
	 * @param BigDecimal vlTotalRecibo
	 * @param Short nrAgenciaBancaria
	 * @param Long cdCedente
	 * @param Short nrCarteiraCedente
	 * @param String nrContaCorrenteCedente
	 * @param String nrBoleto
	 * 
	 * @return List lst
	 */
	private List codBarrasAndLinhaDigitavelItau(Short nrBanco
			, YearMonthDay dtVencBoleto
			, BigDecimal vlTotalRecibo
			, Short nrAgenciaBancaria
			, Short nrCarteiraCedente
			, String nrContaCorrenteCedente
			, String nrBoleto){

		List lst = new ArrayList();
		String nrFatVencimento;
		StringBuilder cdBarras = new StringBuilder();
		StringBuilder cdLinhaDigitavel = new StringBuilder();
		String nrDac;
		String dvLinha1;
		String dvLinha2;
		String dvLinha3;

		String nrBancoStr = FormatUtils.completaDados(nrBanco, "0", 3, 0, true);
		String nrAgenciaBancariaStr = FormatUtils.completaDados(nrAgenciaBancaria, "0", 4, 0, true);
		String nrCarteiraCedenteStr = FormatUtils.completaDados(nrCarteiraCedente, "0", 3, 0, true);
		String nrContaCorrenteCedenteStr = FormatUtils.completaDados(nrContaCorrenteCedente, "0", 12, 0, true);
		String vlTotalReciboStr = FormatUtils.completaDados(vlTotalRecibo.multiply(new BigDecimal(100)).longValue(), "0", 10, 0, true);

		nrFatVencimento = calcFatorVencimentoBoleto(dtVencBoleto);

		/** CÒDIGO DE BARRAS */
		cdBarras.append(nrBancoStr);
		cdBarras.append("90");
		cdBarras.append(nrFatVencimento);
		cdBarras.append(vlTotalReciboStr);
		cdBarras.append(nrCarteiraCedenteStr);
		cdBarras.append(nrBoleto.substring(4, 12));
		cdBarras.append(nrBoleto.substring((nrBoleto.length() - 1), nrBoleto.length()));
		cdBarras.append(nrAgenciaBancariaStr);
		cdBarras.append(nrContaCorrenteCedenteStr.substring(6, 12));
		cdBarras.append("000");

		nrDac = calcDigitoModOnzeDac((cdBarras.substring(0, 4) + cdBarras.substring(5, cdBarras.length()))).toString();

		cdBarras = new StringBuilder(cdBarras.substring(0, 4) + nrDac + cdBarras.substring(5, 44));

		dvLinha1 = calcDigitoModDez(Integer.valueOf(9)
				, (nrBancoStr 
						+ "9" 
						+ nrCarteiraCedenteStr 
						+ nrBoleto.substring(4, 6))).toString();
		dvLinha2 = calcDigitoModDez(Integer.valueOf(10)
				, (nrBoleto.substring(6, 12) 
						+ nrBoleto.substring((nrBoleto.length() - 1), nrBoleto.length())
						+ FormatUtils.fillNumberWithZero(nrAgenciaBancaria.toString(), 4).substring(0, 3))).toString();
		dvLinha3 = calcDigitoModDez(Integer.valueOf(10)
				, (nrAgenciaBancariaStr.substring((nrAgenciaBancariaStr.length() - 1), nrAgenciaBancariaStr.length())
						+ nrContaCorrenteCedenteStr.substring(6, 12)
						+ "000")).toString();

		/** LINHA DIGIT�VEL */
		cdLinhaDigitavel.append(nrBancoStr);
		cdLinhaDigitavel.append("9");
		cdLinhaDigitavel.append(nrCarteiraCedenteStr.substring(0, 1) + "." + nrCarteiraCedenteStr.substring(1, 3));
		cdLinhaDigitavel.append(nrBoleto.substring(4, 6));
		cdLinhaDigitavel.append(dvLinha1);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrBoleto.substring(6, 11));
		cdLinhaDigitavel.append(".");
		cdLinhaDigitavel.append(nrBoleto.substring(11, 12));
		cdLinhaDigitavel.append(nrBoleto.substring((nrBoleto.length() - 1), nrBoleto.length()));
		cdLinhaDigitavel.append(FormatUtils.fillNumberWithZero(nrAgenciaBancaria.toString(), 4).substring(0, 3));
		cdLinhaDigitavel.append(dvLinha2);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrAgenciaBancariaStr.substring((nrAgenciaBancariaStr.length() - 1), nrAgenciaBancariaStr.length()));
		cdLinhaDigitavel.append(nrContaCorrenteCedenteStr.substring(6, 10));
		cdLinhaDigitavel.append(".");
		cdLinhaDigitavel.append(nrContaCorrenteCedenteStr.substring(10, 12));
		cdLinhaDigitavel.append("000");
		cdLinhaDigitavel.append(dvLinha3);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrDac);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrFatVencimento);
		cdLinhaDigitavel.append(vlTotalReciboStr);

		/** Adiciona linha digitavel e codigo de barras ao List */
		lst.add(cdBarras);
		lst.add(cdLinhaDigitavel);

		return lst;
	}

	/* ##################### C�DIGO BARRAS E LINHA DIGIT�VEL HSBC #################### */

	/**
	 * Forma o c�digo de barras e a linha digitável Bradesco
	 * 
	 * @author HectorJ
	 * @since 14/06/2006
	 * 
	 * @param Short nrBanco
	 * @param YearMonthDay dtVencBoleto
	 * @param BigDecimal vlTotalRecibo
	 * @param String nrContaCorrenteCedente
	 * @param String nrBoleto
	 * 
	 * @return List lst
	 */
	private List codBarrasAndLinhaDigitavelHsbc(Short nrBanco
								, YearMonthDay dtVencBoleto
								, BigDecimal vlTotalRecibo
								, String nrContaCorrenteCedente
								, Short nrAgencia
								, String nrBoleto){

		List lst = new ArrayList();
		String nrFatVencimento;
		StringBuilder cdBarras = new StringBuilder();
		StringBuilder cdLinhaDigitavel = new StringBuilder();
		String nrDac;
		String dvLinha1;
		String dvLinha2;
		String dvLinha3;

		String nrBancoStr = FormatUtils.completaDados(nrBanco, "0", 3, 0, true);
		String nrContaCorrenteCedenteStr = "0" + FormatUtils.fillNumberWithZero(nrAgencia.toString(), 4) + FormatUtils.fillNumberWithZero(nrContaCorrenteCedente, 7);
		String vlTotalReciboStr = FormatUtils.completaDados(vlTotalRecibo.multiply(new BigDecimal(100)).longValue(), "0", 10, 0, true);
		String nrBoletoStr = nrBoleto.substring(2, 13);

		nrFatVencimento = calcFatorVencimentoBoleto(dtVencBoleto);

		/** CÒDIGO DE BARRAS */
		cdBarras.append(nrBancoStr);
		cdBarras.append("90");
		cdBarras.append(nrFatVencimento);
		cdBarras.append(vlTotalReciboStr);
		cdBarras.append(nrBoletoStr);
		cdBarras.append(nrContaCorrenteCedenteStr.substring(1, 12));
		cdBarras.append("001");

		nrDac = calcDigitoModOnzeDac(cdBarras.substring(0, 4) + cdBarras.substring(5, cdBarras.length())).toString();

		cdBarras = new StringBuilder(cdBarras.substring(0, 4) + nrDac + cdBarras.substring(5, 44));

		dvLinha1 = calcDigitoModDez(Integer.valueOf(9)
				, cdBarras.substring(0, 4) 
						+ nrBoletoStr.substring(0, 5)).toString();
		dvLinha2 = calcDigitoModDez(Integer.valueOf(10)
				, nrBoletoStr.substring(5, 10) 
						+ nrBoletoStr.substring((nrBoletoStr.length() - 1), nrBoletoStr.length())
						+ nrContaCorrenteCedenteStr.substring(1, 5)).toString();
		dvLinha3 = calcDigitoModDez(Integer.valueOf(10)
				, nrContaCorrenteCedenteStr.substring(5, 12)
						+ "001").toString();

		/** LINHA DIGIT�VEL */
		cdLinhaDigitavel.append(cdBarras.substring(0, 4));
		cdLinhaDigitavel.append(nrBoletoStr.substring(0, 1));
		cdLinhaDigitavel.append(".");
		cdLinhaDigitavel.append(nrBoletoStr.substring(1, 5));
		cdLinhaDigitavel.append(dvLinha1);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrBoletoStr.substring(5, 10));
		cdLinhaDigitavel.append(".");
		cdLinhaDigitavel.append(nrBoletoStr.substring((nrBoletoStr.length() - 1), nrBoletoStr.length()));
		cdLinhaDigitavel.append(nrContaCorrenteCedenteStr.substring(1, 5));
		cdLinhaDigitavel.append(dvLinha2);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrContaCorrenteCedenteStr.substring(5, 10));
		cdLinhaDigitavel.append(".");
		cdLinhaDigitavel.append(nrContaCorrenteCedenteStr.substring(10, 12));
		cdLinhaDigitavel.append("001");
		cdLinhaDigitavel.append(dvLinha3);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrDac);
		cdLinhaDigitavel.append(" ");
		cdLinhaDigitavel.append(nrFatVencimento);
		cdLinhaDigitavel.append(vlTotalReciboStr);

		/** Adiciona linha digitavel e codigo de barras ao List */
		lst.add(cdBarras);
		lst.add(cdLinhaDigitavel);

		return lst;
	}

	/**
	 * M�todo que implementa a regra 2.3 da especificação
	 * 
	 * @author HectorJ
	 * @since 16/06/2006
	 * 
	 * @param nrBanco
	 * @param dtVencBoleto
	 * @param vlTotalRecibo
	 * @param nrAgenciaBancaria
	 * @param cdCedente
	 * @param nrCarteiraCedente
	 * @param nrContaCorrenteCedente
	 * @param nrBoleto
	 * 
	 * @return List lst
	 */
	public List calcCodBarrasAndLinhaDigByBanco(
			Short nrBanco,
			YearMonthDay dtVencBoleto,
			BigDecimal vlTotalRecibo,
			Short nrAgenciaBancaria,
			Long cdCedente,
			Short nrCarteiraCedente,
			String nrContaCorrenteCedente,
			String nrBoleto
	) {
		List lst = null;

		/* BANRISUL */
		if (nrBanco.equals(ConstantesConfiguracoes.COD_BANRISUL)) {
			lst = codBarrasAndLinhaDigitavelBanrisul(nrBanco
					 , dtVencBoleto
					 , vlTotalRecibo
					 , nrAgenciaBancaria
					 , cdCedente
					 , nrBoleto);
		}
		/* BRADESCO */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_BRADESCO)) {
			lst = codBarrasAndLinhaDigitavelBradesco(nrBanco
					 , dtVencBoleto
					 , vlTotalRecibo
					 , nrAgenciaBancaria
					 , nrCarteiraCedente
					 , nrContaCorrenteCedente
					 , nrBoleto);
		}
		/* HSBC */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_HSBC)) {
			lst = codBarrasAndLinhaDigitavelHsbc(nrBanco
					 , dtVencBoleto
					 , vlTotalRecibo
					 , nrContaCorrenteCedente
					 , nrAgenciaBancaria
					 , nrBoleto);
		}
		/* ITA� */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_ITAU)) {
			lst = codBarrasAndLinhaDigitavelItau(nrBanco
					 , dtVencBoleto
					 , vlTotalRecibo
					 , nrAgenciaBancaria
					 , nrCarteiraCedente
					 , nrContaCorrenteCedente
					 , nrBoleto);
		}	

		return lst;
	}

	private boolean isMaiorQueZero(Map map) {
		BigDecimal valorCobrado = (BigDecimal)map.get("VALOR_COBRADO");
		return valorCobrado.doubleValue() > BigDecimalUtils.ZERO.doubleValue();
	}

	/**
	 *	Gera o c�digo de barras
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/02/2007
	 *
	 * @param imprimeCodBarras
	 * @param rs
	 * @param map
	 * @param vlDocumento
	 * @throws SQLException
	 * @throws NumberFormatException
	 *
	 */
	private void geraCodigoBarras(ResultSet rs, Map map, BigDecimal vlDoc, Object[] parameters) throws SQLException, NumberFormatException {
		YearMonthDay vencimento;
		BigDecimal vlDocumento = vlDoc;
		
		if (map.get("VALOR_COBRADO") != null && map.containsKey("VALOR_COBRADO") && isMaiorQueZero(map)) {
			vlDocumento = (BigDecimal) map.get("VALOR_COBRADO");

		}
		
		if (map.get("DT_VENCIMENTO_FUTURO") != null && map.containsKey("DT_VENCIMENTO_FUTURO")) {
			vencimento = new YearMonthDay(map.get("DT_VENCIMENTO_FUTURO"));

		} else {
			vencimento = new YearMonthDay(map.get("DATA_VENCIMENTO"));

		}
		
		/**
		 * Monta a linha digitável e o c�digo de barras
		 */
		List lst = calcCodBarrasAndLinhaDigByBanco(Short.valueOf(rs.getShort("NR_BANCO"))
													, vencimento
												 , vlDocumento
												 , Short.valueOf(rs.getShort("NR_AGENCIA_BANCARIA"))
												 , Long.valueOf(rs.getString("CD_CEDENTE"))
												 , Short.valueOf(rs.getShort("NR_CARTEIRA"))
												 , rs.getString("NR_CONTA_CORRENTE")
												 , rs.getString("NOSSO_NUMERO"));

		map.put("CODIGO_BARRAS", lst.get(0).toString());
		map.put("LINHA_DIGITAVEL", lst.get(1).toString());

	   /** 
	    * Faz a gera��o do c�digo de barras nesse ponto
	    * pois no jasperReport estava gerando a imagem com problemas
	    */
 	   BarcodeInter25 barCode = new BarcodeInter25();

 	   barCode.setCode(lst.get(0).toString());
	   barCode.setX(1.0f);
	   barCode.setSize(0);
	   barCode.setBarHeight(40);		            

	   java.awt.Image barCodeImage = barCode.createAwtImage(new Color(0,0,0), new Color(255,255,255));

	   map.put("IMAGE_CODIGO_BARRAS", barCodeImage);
	}

	/**
	 * 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 15/09/2006
	 *
	 * @param nrBanco
	 * @param nrAgencia
	 * @param cdCedente
	 * @return
	 *
	 */
	private String formatCedenteByBanco(Short nrBanco, Short nrAgencia, String cdCedente, String nrContaCorrente, String nrDigitoAgencia){
		StringBuilder cedente = new StringBuilder();

		/* BANRISUL */
		if (nrBanco.equals(ConstantesConfiguracoes.COD_BANRISUL)) {
			cedente.append(FormatUtils.completaDados(nrAgencia, "0", 3, 0, true));
			cedente.append(".27 0");
			cedente.append(cdCedente.substring(0, cdCedente.length() - 3));
			cedente.append(".");
			cedente.append(cdCedente.substring(cdCedente.length() - 3, cdCedente.length() - 2));
			cedente.append(".");
			cedente.append(cdCedente.substring(cdCedente.length() - 2, cdCedente.length()));
		}
		/* BRADESCO */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_BRADESCO)) {
			cedente.append(FormatUtils.fillNumberWithZero(nrAgencia.toString() ,4));
			cedente.append("-");
			cedente.append(nrDigitoAgencia);
			cedente.append("/");
			cedente.append(FormatUtils.fillNumberWithZero(nrContaCorrente.substring(0, nrContaCorrente.length() - 1), 7));
			cedente.append("-");
			cedente.append(nrContaCorrente.substring(nrContaCorrente.length() - 1, nrContaCorrente.length()));
		}
		/* HSBC */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_HSBC)) {
			cedente.append(nrAgencia);
			cedente.append("/");
			cedente.append(FormatUtils.fillNumberWithZero(cdCedente, 7).substring(0, 5));
			cedente.append("-");
			cedente.append(FormatUtils.fillNumberWithZero(cdCedente, 7).substring(5, 7));
		}
		/* ITA� */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_ITAU)) {
			cedente.append(FormatUtils.fillNumberWithZero(nrAgencia.toString(),4));
			cedente.append("/");
			cedente.append(cdCedente.substring(0, cdCedente.length()-1));
			cedente.append("-");
			cedente.append(cdCedente.substring(cdCedente.length()-1, cdCedente.length()));
		}	

		return cedente.toString();
	}

	/**
	 * 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 15/09/2006
	 *
	 * @param nrBanco
	 * @param nrAgencia
	 * @param cdCedente
	 * @return
	 *
	 */
	private String formatBoletoByBanco(Short nrBanco, String nrBoleto, String nrCarteira){
		StringBuilder boleto = new StringBuilder();

		/* BANRISUL */
		if (nrBanco.equals(ConstantesConfiguracoes.COD_BANRISUL)) {
			// 0000000001234 para 0000001234 
			boleto.append(nrBoleto.substring(3, 13));
			// 0000001234 para 00000012.34
			boleto.insert(8, ".");
		}
		/* BRADESCO */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_BRADESCO)) {
			// 09   
			boleto.append(FormatUtils.fillNumberWithZero(nrCarteira,3).substring(1,3));
			// 09 para 09/
			boleto.append("/");
			// 09/ para 09/06000000026P
			boleto.append(nrBoleto.substring(1, nrBoleto.length()));
			// 09/06000000026P para 09/06000000026-P
			boleto.insert(boleto.length()-1, "-");
		}
		/* HSBC */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_HSBC)) {
			// 57607000273
			boleto.append(nrBoleto.substring(2, 13));
			// 57607000273 para 57 607000273
			boleto.insert(2," ");
			// 57 607000273 para 57 607 000273
			boleto.insert(6," ");
			// 57 607 000273 para 57 607 000 273
			boleto.insert(10," ");
			// 57 607 000 273 para 57 607 000 27 3 
			boleto.insert(13," ");
		}
		/* ITA� */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_ITAU)) {
			// 109   
			boleto.append(FormatUtils.fillNumberWithZero(nrCarteira,3).substring(0,3));
			// 109 para 109/
			boleto.append("/");
			// 109/ para 109/002000301
			boleto.append(nrBoleto.substring(4, nrBoleto.length()));
			// 109/002000301 para 109/00200030-1
			boleto.insert(boleto.length()-1, "-");
		}	

		return boleto.toString();
	}

	private BigDecimal getMulta() {
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro("PC_MULTA_ATRASO_BOLETO");
		BigDecimal percetualMulta = new BigDecimal(parametroGeral.getDsConteudo());
		return percetualMulta.divide(BigDecimalUtils.HUNDRED);
	}
	
	private List executeSubSubReportBoleto(Object[] parameters) { 
		Long idBoleto = (Long) parameters[0];  
		Long qtDocumentos = (Long) parameters[1];

		/** Instância a classe SqlTemplate, que retorna o sql para gera��o do relatório */
		StringBuilder sb = getBoletoDAO().getSqlTemplateSubSubReportBoleto(idBoleto, qtDocumentos);
		List lst;

		/** Caso a fatura tenha mais de 12 documentos */
		if(qtDocumentos.longValue() > 12){
			lst = getBoletoDAO().iteratorResultSetSubSubReportBoleto(sb.toString(), new Object[]{idBoleto});
		/** Caso a fatura não tenha mais de 12 documentos */
		} else {
			lst = getBoletoDAO().iteratorResultSetSubSubReportBoleto(sb.toString(), new Object[]{idBoleto, idBoleto, idBoleto});
		}

		return lst;
	}

	public List iteratorSubSelectBoleto(SqlTemplate sql, final Long qtDocumentos, final Object[] parameters) {
		List retorno = getJdbcTemplate().query(sql.getSql(), sql.getCriteria(), new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map map = new HashMap();

				List dataSource = executeSubSubReportBoleto(new Object[]{Long.valueOf(rs.getString("ID_BOLETO")), qtDocumentos});
				map.put("DATASOURCE1", dataSource);
				map.put("DATASOURCE2", dataSource);

				map.put("ID_CEDENTE", Long.valueOf(rs.getString("ID_CEDENTE")));
				map.put("ID_BOLETO", Long.valueOf(rs.getString("ID_BOLETO")));
				
				map.put("DT_VENCIMENTO_FUTURO", parameters[5]);
				map.put("VALOR_COBRADO", parameters[6]);
				
				map.put("DATA_VENCIMENTO", rs.getDate("DATA_VENCIMENTO"));
				
				Boolean blAtualizaVctoCartaCob = false;
				if (parameters[7] != null) {
					blAtualizaVctoCartaCob = (Boolean) parameters[7];
				}
				
				BigDecimal nrDiasVencimentoCargaCob = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("NR_DIAS_VCTO_CARTA_COB", false);
				if (blAtualizaVctoCartaCob) {
					YearMonthDay vencimento = new YearMonthDay();
					java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
					vencimento = vencimento.plusDays(nrDiasVencimentoCargaCob.intValue());
					Date date = new Date();
					try {
						date = formatter.parse(vencimento.toString());
					} catch (ParseException e) {
						LOGGER.error(e);
					}
					if (map.get("DT_VENCIMENTO_FUTURO") != null) {
						map.put("DT_VENCIMENTO_FUTURO", date);
					} else if (map.get("DATA_VENCIMENTO") != null) {
						map.put("DATA_VENCIMENTO", date);
					}
				}
				
				map.put("CEDENTE_NOME", rs.getString("CEDENTE_NOME"));
				map.put("NR_IDENTIFICACAO_CEDENTE", FormatUtils.formatIdentificacao(rs.getString("TP_IDENTIFICACAO"), rs.getString("NR_IDENTIFICACAO")));
				map.put("CIDADE_CEDENTE", rs.getString("NM_FILIAL"));

				String telefoneFilial = rs.getString("TELEFONE_FIL_COB");
				
				if (telefoneFilial != null && telefoneFilial.length() >0){
					Matcher matcher = Pattern.compile("(\\(\\d+\\))?(\\d{2,4})(\\d{4})").matcher(telefoneFilial);
					if (matcher.matches()){
						telefoneFilial = matcher.group(1) + matcher.group(2) + "-" +matcher.group(3);
					}
				}
				map.put("CEDENTE_TELEFONE", telefoneFilial);
				
				map.put("DATA_DOCUMENTO", rs.getDate("DATA_DOCUMENTO"));
				map.put("NR_DOCUMENTO", "FAT " 
							+ rs.getString("SG_FILIAL") 
							+ " " + FormatUtils.completaDados(rs.getString("NR_DOCUMENTO"), "0", 10, 0 ,true));

				map.put("SACADO_IDENTIFICACAO", rs.getString("SACADO_TP_IDENTIFICACAO") + ": " 
											  + FormatUtils.formatIdentificacao(rs.getString("SACADO_TP_IDENTIFICACAO")
													  						  , rs.getString("SACADO_NR_IDENTIFICACAO")));
				map.put("SACADO_NOME", rs.getString("SACADO_NOME")); 

				map.put("SACADO_ENDERECO", enderecoPessoaService.formatEnderecoPessoaComplemento(rs.getString("DS_TIPO_LOG_CLIENTE_FAT")
																							   , rs.getString("DS_ENDERECO_CLIENTE_FAT")
																							   , rs.getString("NR_ENDERECO_CLIENTE_FAT")
																							   , rs.getString("DS_COMPLEMENTO_CLIENTE_FAT")));

				map.put("SACADO_BAIRRO", rs.getString("SACADO_BAIRRO"));

				String telefone = rs.getString("TELEFONE");

				if (telefone != null && telefone.length() >0){
					Matcher matcher = Pattern.compile("(\\(\\d+\\))?(\\d{2,4})(\\d{4})").matcher(telefone);
					if (matcher.matches()){
						telefone = matcher.group(1) + matcher.group(2) + "-" +matcher.group(3);
					}
				}
 				map.put("SACADO_TELEFONE", telefone);

				map.put("SACADO_CEP", FormatUtils.formatCep("BRA", rs.getString("NR_CEP")));
				map.put("SACADO_CIDADE", rs.getString("NM_MUNICIPIO"));
				map.put("SACADO_UF", rs.getString("SG_UF"));
				
				map.put("FIL_ENDERECO", enderecoPessoaService.formatEnderecoPessoaComplemento(rs.getString("DS_TIPO_LOG_FIL_FAT")
						   , rs.getString("DS_ENDERECO_FIL_FAT")
						   , rs.getString("NR_ENDERECO_FIL_FAT")
						   , rs.getString("DS_COMPLEMENTO_FIL_FAT")));
				map.put("FIL_BAIRRO", rs.getString("FIL_BAIRRO"));
				map.put("FIL_CEP", FormatUtils.formatCep("BRA", rs.getString("FIL_NR_CEP")));
				map.put("FIL_CIDADE", rs.getString("FIL_NM_MUNICIPIO"));
				map.put("FIL_UF", rs.getString("FIL_SG_UF"));
				
				map.put("REF_BOSCH", rs.getString("REF_BOSCH"));
				map.put("JUROS_DIA", rs.getBigDecimal("JUROS_DIA"));
				map.put("VL_JURO_CALCULADO", rs.getBigDecimal("VL_JURO_CALCULADO"));
				map.put("VL_IMPOSTOS", rs.getBigDecimal("VL_IMPOSTOS"));
				map.put("FRETE", rs.getBigDecimal("FRETE"));
				map.put("VL_COTACAO_DOLAR", rs.getBigDecimal("VL_COTACAO_DOLAR") != null ? rs.getBigDecimal("VL_COTACAO_DOLAR") : null);
				map.put("NR_MANIFESTO", rs.getString("NR_MANIFESTO") != null ? Long.valueOf(rs.getString("NR_MANIFESTO")) : null);

				map.put("CODIGO_CEDENTE", formatCedenteByBanco(
												Short.valueOf(rs.getShort("NR_BANCO"))
											  , Short.valueOf(rs.getShort("NR_AGENCIA_BANCARIA"))
											  , rs.getString("CD_CEDENTE")
											  , rs.getString("NR_CONTA_CORRENTE")
											  , rs.getString("NR_DIGITO_AGENCIA_BANCARIA")));

				map.put("NOSSO_NUMERO", formatBoletoByBanco( 
												Short.valueOf(rs.getShort("NR_BANCO"))
											  , rs.getString("NOSSO_NUMERO")
											  , rs.getString("NR_CARTEIRA")));

				if(ConstantesConfiguracoes.COD_BRADESCO.equals(rs.getShort("NR_BANCO")))
					map.put("NR_CARTEIRA", FormatUtils.fillNumberWithZero(rs.getString("NR_CARTEIRA"), 3).substring(1, 3));

			 	/**
			 	  * Verifica se existe configuração vigente indicando que
				  * essa filial emite boleto com valor líquido
				  */
				List lstParam = parametroBoletoFilialService.findParametroBoletoFilialVigenteByFilial(rs.getLong("ID_FILIAL_COBRADORA"), new YearMonthDay(rs.getDate("DATA_DOCUMENTO")), Boolean.TRUE);

				BigDecimal vlDocumento;

				/**
				  * Se emite boleto com valor líquido, 
				  * valor do documento deve ser VALOR - DESCONTO
				  * e valor do desconto não deve aparecer no boleto
				  */
				if (lstParam!=null && !lstParam.isEmpty()) {
					vlDocumento = rs.getBigDecimal("VALOR").subtract(rs.getBigDecimal("DESCONTO_VENCIMENTO"));
				} else {
					vlDocumento = rs.getBigDecimal("VALOR");
				}
				map.put("DESCONTO_VENCIMENTO", rs.getBigDecimal("DESCONTO_VENCIMENTO"));
				
				if (blAtualizaVctoCartaCob) {
					BigDecimal multa = getMulta();
					BigDecimal juros = (BigDecimal) map.get("VL_JURO_CALCULADO");
					BigDecimal valorJurosDia = (BigDecimal) map.get("JUROS_DIA");
					vlDocumento = new BigDecimal(vlDocumento.doubleValue() + juros.doubleValue() + (vlDocumento.doubleValue() * multa.doubleValue()) + (valorJurosDia.doubleValue() * nrDiasVencimentoCargaCob.doubleValue()));
					vlDocumento = BigDecimalUtils.round(vlDocumento);
				}
				
				map.put("VALOR", vlDocumento.doubleValue());
				
				BigDecimal pcMultaAtraso = (BigDecimal) parameters[8];
				if(ConstantesConfiguracoes.COD_BRADESCO.equals(rs.getShort("NR_BANCO"))){
					map.put("VALOR_MULTA_APOS_VENCIMENTO", pcMultaAtraso);
				}else{
					BigDecimal valorMultaAposVencimento = vlDocumento.multiply(pcMultaAtraso.divide(BigDecimalUtils.HUNDRED));
					map.put("VALOR_MULTA_APOS_VENCIMENTO", valorMultaAposVencimento);
				}
				
				

				// Gera o c�digo de barras
				geraCodigoBarras(rs, map, vlDocumento, parameters);
				return map;
			}
		});

		return retorno;
	}
	
	/**
	 * Salva um HistoricoBoleto
	 * 
	 * @author HectorJ
	 * @since 19/06/2006
	 * 
	 * @param idBoleto
	 * @param nrBanco
	 * 
	 */
	public void storeHistoricoBoleto(Boleto boleto, Short nrBanco) {
		HistoricoBoleto hb = new HistoricoBoleto();

		hb.setBoleto(boleto);

		BigDecimal idOcorrenciaBanco = null;

		/* BANRISUL */
		if (nrBanco.equals(ConstantesConfiguracoes.COD_BANRISUL)) {
			idOcorrenciaBanco = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesConfiguracoes.PARAM_GERAL_BANRISUL);
		}
		/* BRADESCO */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_BRADESCO)) {
			idOcorrenciaBanco = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesConfiguracoes.PARAM_GERAL_BRADESCO);
		}
		/* HSBC */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_HSBC)) {
			idOcorrenciaBanco = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesConfiguracoes.PARAM_GERAL_HSBC);
		}
		/* ITA� */
		else if (nrBanco.equals(ConstantesConfiguracoes.COD_ITAU)) {
			idOcorrenciaBanco = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesConfiguracoes.PARAM_GERAL_ITAU);
		}	

		OcorrenciaBanco ob = new OcorrenciaBanco();
		ob.setIdOcorrenciaBanco(null != idOcorrenciaBanco ? idOcorrenciaBanco.longValue() : null);
		hb.setOcorrenciaBanco(ob);

		hb.setUsuario(SessionUtils.getUsuarioLogado());
		hb.setDhOcorrencia(JTDateTimeUtils.getDataHoraAtual());
		hb.setTpSituacaoHistoricoBoleto(new DomainValue("A"));

		historicoBoletoService.store(hb);
	}
	
	/**
	 * Valida se � emiss�o ou reemiss�o
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/08/2006
	 *
	 * @param tfm
	 * @param parametersReport
	 * @return
	 *
	 */
	public Short validateEmissaoReemissao(TypedFlatMap tfm, Map parametersReport) {
		Short nrBanco = null;

		if(tfm.getString("idFaturas") == null) {
			if(tfm.getBoolean("reemissao") != null && tfm.getBoolean("reemissao").equals(Boolean.TRUE)){
				parametersReport.put("reemissao", Boolean.TRUE);

				/** Busca o número do banco para abrir o relatório correto */

				/** retorna null caso a fatura nao possua boleto*/
				Boleto boleto = findByFatura(tfm.getLong("fatura.idFatura")); 
				if ( boleto == null ){
					return null;
				}
				nrBanco = boleto.getCedente().getAgenciaBancaria().getBanco().getNrBanco();
			} else {
				parametersReport.put("reemissao", Boolean.FALSE);

				/** Busca o número do banco para abrir o relatório correto */
				nrBanco = cedenteService.findById(tfm.getLong("cedente.idCedente")).getAgenciaBancaria().getBanco().getNrBanco();
			}
		} else {
			parametersReport.put("reemissao", Boolean.FALSE);
		}
		
		return nrBanco;
	}
	
	public Map getParametersReport(Map parameters) {
		TypedFlatMap tfm = (TypedFlatMap) parameters;

		Short nrBanco = null;
		
		if (parameters.get("idFaturas") != null){
			String[] idFaturas = StringUtils.split((String) parameters.get("idFaturas"), ",");
			
			Boleto boleto = findByFatura(Long.valueOf(idFaturas[0]));
			if (boleto != null){
				nrBanco = boleto.getCedente().getAgenciaBancaria().getBanco().getNrBanco();
			}
		}
		
		
		boolean reemitir = parameters.get("reemissao") != null && tfm.getBoolean("reemissao").booleanValue();
		if (reemitir) {
			Boleto bol = findByFatura(tfm.getLong("fatura.idFatura"));
			if (bol == null || bol.getBlBoletoReemitido().booleanValue()) {
				throw new BusinessException("LMS-36105");
			}
		}
		
		Map parametersReport = new HashMap();
		parametersReport.put("reemissao", reemitir); 
		if (nrBanco == null && parameters.get("idFaturas") != null) {
			nrBanco = ConstantesConfiguracoes.COD_BRADESCO;
		} else if ( nrBanco == null ) {
			nrBanco = ConstantesConfiguracoes.COD_BANRISUL ;
		}
		parametersReport.put("NR_BANCO", nrBanco);
		
		SqlTemplate sql = mountSqlBoleto(tfm);
		
		/** Valida se não foi informado um intervalo de boletos */
		if(tfm.getLong("boletoInicial") == null && tfm.getLong("boletoFinal") == null){
			parametersReport.put("intervaloBoletoNaoInformado", Boolean.TRUE);
		} else {
			parametersReport.put("intervaloBoletoNaoInformado", Boolean.FALSE);
		}
		
		/** Adiciona os parâmetros de pesquisa no Map */
		// parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("sql", sql);
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());

		/** Adiciona o tipo de relatório no Map */
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		
		/** Multa do HSBC */
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro("PC_MULTA_ATRASO_BOLETO");
		BigDecimal multa = new BigDecimal(parametroGeral.getDsConteudo());
		parametersReport.put("PC_MULTA_ATRASO_BOLETO", multa);
		Date data = null;
		if(parameters.get("boleto.dtVencFut") != null){
			data = DateTimeUtils.convertStringToCalendar((String)parameters.get("boleto.dtVencFut")).getTime();
		}
		parametersReport.put("BL_ATUALIZA_VCTO_CARTA_COB", parameters.get("BL_ATUALIZA_VCTO_CARTA_COB"));
		parametersReport.put("DT_VENCIMENTO_FUTURO", data);
		parametersReport.put("MORA_MULTA", parameters.get("moraMulta"));
		parametersReport.put("VALOR_COBRADO", parameters.get("valorCobrado"));

		return parametersReport; 
	}
	
	public BoletoDMN executeReportByIdFatura(Long idFatura) {
		Fatura fatura = faturaService.findById(idFatura);
		if (fatura != null) {
			
			// Monta parâmetros para a consulta do relatório
			Map parameters = new TypedFlatMap();
			parameters.put("nrFatura", fatura.getNrFatura());
			if (fatura.getFilialByIdFilial() != null) {
				parameters.put("siglaFilial"    , fatura.getFilialByIdFilial().getSgFilial());
				parameters.put("filial.idFilial", fatura.getFilialByIdFilial().getIdFilial());
			}
			parameters.put("fatura.idFatura", idFatura);
			Boleto boleto = findByFatura(idFatura);
			if ((boleto != null) && (boleto.getCedente() != null)) {
				parameters.put("cedente.idCedente", boleto.getCedente().getIdCedente());
			}

			// Faz a chamada ao relatório
			Map parametersReport = getParametersReport(parameters);
			List reportList = executeReport(parametersReport);
			
			if (reportList != null && reportList.size() > 0){
				Map boletoMap = (Map)reportList.get(0);
				Short nrBanco = (Short) boletoMap.get("NR_BANCO"); 
				parametersReport.put("NR_BANCO", nrBanco);
			}

			// Monta o objeto de integração
			return getBoletoDMN(parametersReport, reportList, fatura.getNrFatura(), fatura.getFilialByIdFilial().getSgFilial());
		}
		return null;
	}
	
	private BoletoDMN getBoletoDMN(Map parametersReport, List reportList, Long nrFatura, String sgFilialFatura) {
		BoletoDMN boletoDMN = new BoletoDMN();

		Short nrBanco = (Short)parametersReport.get("NR_BANCO");
		boletoDMN.setNrBanco(Long.valueOf(nrBanco));

		boletoDMN.setNrFatura(nrFatura);
		boletoDMN.setSgFilialFatura(sgFilialFatura);
		boletoDMN.setBlAtualizaVctoCartaCob((Boolean)parametersReport.get("BL_ATUALIZA_VCTO_CARTA_COB"));
		boletoDMN.setMoraMulta((BigDecimal)parametersReport.get("MORA_MULTA"));
		boletoDMN.setDtVencimentoFuturo((Date)parametersReport.get("DT_VENCIMENTO_FUTURO"));
		boletoDMN.setReemissao((Boolean)parametersReport.get("reemissao"));
		boletoDMN.setVlCobrado((BigDecimal)parametersReport.get("VALOR_COBRADO"));
		boletoDMN.setIntervaloBoletoNaoInformado((Boolean)parametersReport.get("intervaloBoletoNaoInformado"));
		
		if (!reportList.isEmpty()) {
			Map reportMap = (Map)reportList.get(0);
			boletoDMN.setIdBoleto((Long)reportMap.get("ID_BOLETO"));
			boletoDMN.setQtDocumentos((Long)reportMap.get("QT_DOCUMENTOS"));
			boletoDMN.setTpSituacaoBoleto((String)reportMap.get("TP_SITUACAO_BOLETO"));
			
			List<Map> dadosList = (List)reportMap.get("DADOS");
			if (!dadosList.isEmpty()) {
				Map dadosMap = dadosList.get(0);
				boletoDMN.setVlJuroCalculado((BigDecimal)dadosMap.get("VL_JURO_CALCULADO"));
				boletoDMN.setIdCedente((Long)dadosMap.get("ID_CEDENTE"));
				boletoDMN.setDtVencimento((Date)dadosMap.get("DATA_VENCIMENTO"));
				boletoDMN.setCedenteNome((String)dadosMap.get("CEDENTE_NOME"));
				boletoDMN.setCodigoCedente((String)dadosMap.get("CODIGO_CEDENTE"));
				boletoDMN.setDtDocumento((Date)dadosMap.get("DATA_DOCUMENTO"));
				boletoDMN.setNossoNumero((String)dadosMap.get("NOSSO_NUMERO"));
				boletoDMN.setSacadoNome((String)dadosMap.get("SACADO_NOME"));
				boletoDMN.setSacadoEndereco((String)dadosMap.get("SACADO_ENDERECO")); 
				boletoDMN.setSacadoCidade((String)dadosMap.get("SACADO_CIDADE"));
				boletoDMN.setSacadoUf((String)dadosMap.get("SACADO_UF"));
				boletoDMN.setSacadoCep((String)dadosMap.get("SACADO_CEP"));
				boletoDMN.setValor((Double)dadosMap.get("VALOR"));
				boletoDMN.setCidadeCedente((String)dadosMap.get("CIDADE_CEDENTE"));
				boletoDMN.setCedenteTelefone((String)dadosMap.get("CEDENTE_TELEFONE"));
				boletoDMN.setSacadoTelefone((String)dadosMap.get("SACADO_TELEFONE"));
				boletoDMN.setRefBosch((String)dadosMap.get("REF_BOSCH"));
				boletoDMN.setJurosDia((BigDecimal)dadosMap.get("JUROS_DIA"));
				boletoDMN.setVlImpostos((BigDecimal)dadosMap.get("VL_IMPOSTOS"));
				boletoDMN.setVlCotacaoDolar((BigDecimal)dadosMap.get("VL_COTACAO_DOLAR"));
				boletoDMN.setVlDescontoVencimento((BigDecimal)dadosMap.get("DESCONTO_VENCIMENTO"));
				boletoDMN.setNrDocumento((String)dadosMap.get("NR_DOCUMENTO"));
				boletoDMN.setNrManifesto((Long)dadosMap.get("NR_MANIFESTO"));
				boletoDMN.setVlFrete((BigDecimal)dadosMap.get("FRETE"));
				boletoDMN.setCodigoBarras((String)dadosMap.get("CODIGO_BARRAS"));
				boletoDMN.setLinhaDigitavel((String)dadosMap.get("LINHA_DIGITAVEL"));
				boletoDMN.setSacadoIdentificacao((String)dadosMap.get("SACADO_IDENTIFICACAO"));
				boletoDMN.setSacadoBairro((String)dadosMap.get("SACADO_BAIRRO"));
				boletoDMN.setNrIdentificacaoCedente((String)dadosMap.get("NR_IDENTIFICACAO_CEDENTE"));
				boletoDMN.setFilEndereco((String)dadosMap.get("FIL_ENDERECO"));
				boletoDMN.setFilBairro((String)dadosMap.get("FIL_BAIRRO"));
				boletoDMN.setFilCep((String)dadosMap.get("FIL_CEP"));
				boletoDMN.setFilUf((String)dadosMap.get("FIL_UF"));
				boletoDMN.setNrCarteira((String)dadosMap.get("NR_CARTEIRA"));
				boletoDMN.setFilCidade((String)dadosMap.get("FIL_CIDADE"));
				
				BigDecimal pcMultaAtraso = (BigDecimal)parametersReport.get("PC_MULTA_ATRASO_BOLETO");
				if(ConstantesConfiguracoes.COD_BRADESCO.equals(nrBanco)){
					boletoDMN.setVlMulta(pcMultaAtraso);
				}else{
					BigDecimal valorCobrado = new BigDecimal((Double)dadosMap.get("VALOR")); 
					BigDecimal vlMulta = valorCobrado.multiply(pcMultaAtraso.divide(BigDecimalUtils.HUNDRED));
					vlMulta = BigDecimalUtils.round(vlMulta);
					boletoDMN.setVlMulta(vlMulta);
				}

				List<BoletoDetalheDMN> detalhe1List = new ArrayList<BoletoDetalheDMN>(); 
				List<Map> datasource1 = (List)dadosMap.get("DATASOURCE1");
				for (Map map : datasource1) {
					BoletoDetalheDMN detalheDMN = new BoletoDetalheDMN();
					detalheDMN.setFilialDoctoServico((String)map.get("FILIAL_DOCTO_SERVICO"));
					detalheDMN.setDoctoServico((Long)map.get("DOCTO_SERVICO"));
					detalheDMN.setTpDoctoServico((String)map.get("TP_DOCTO_SERVICO"));
					
					Double bdFrete = (Double)map.get("FRETE");
					if (bdFrete != null) {
						detalheDMN.setFrete(bdFrete);
						
					}					
					detalheDMN.setDesconto((Double)map.get("DESCONTO"));
					detalheDMN.setNotaFiscal((Long)map.get("NOTA_FISCAL"));
					detalheDMN.setMaiorDoze((String)map.get("MAIOR_DOZE"));
					detalhe1List.add(detalheDMN);
					
				}
				boletoDMN.setDetalhe1List(detalhe1List);

				List<BoletoDetalheDMN> detalhe2List = new ArrayList<BoletoDetalheDMN>();
				List<Map> datasource2 = (List)dadosMap.get("DATASOURCE2");
				for (Map map : datasource2) {
					BoletoDetalheDMN detalheDMN = new BoletoDetalheDMN();
					detalheDMN.setFilialDoctoServico((String)map.get("FILIAL_DOCTO_SERVICO"));
					detalheDMN.setDoctoServico((Long)map.get("DOCTO_SERVICO"));
					detalheDMN.setTpDoctoServico((String)map.get("TP_DOCTO_SERVICO"));
					
					Double bdFrete = (Double)map.get("FRETE");
					if (bdFrete != null) {
						detalheDMN.setFrete(bdFrete);
						
					}
					
					detalheDMN.setDesconto((Double)map.get("DESCONTO"));
					detalheDMN.setNotaFiscal((Long)map.get("NOTA_FISCAL"));
					detalheDMN.setMaiorDoze((String)map.get("MAIOR_DOZE"));
					detalhe2List.add(detalheDMN);
					
				}
				boletoDMN.setDetalhe2List(detalhe2List);
				
			}
		}
		
		return boletoDMN;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ParametroBoletoFilialService getParametroBoletoFilialService() {
		return parametroBoletoFilialService;
	}

	public void setParametroBoletoFilialService(ParametroBoletoFilialService parametroBoletoFilialService) {
		this.parametroBoletoFilialService = parametroBoletoFilialService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//------------------------------ M�todos relacionados a gera��o dos boletos (Report)

}
