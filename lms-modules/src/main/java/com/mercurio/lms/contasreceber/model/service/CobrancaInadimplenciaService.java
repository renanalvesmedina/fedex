package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.contasreceber.model.CobrancaInadimplencia;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemCobranca;
import com.mercurio.lms.contasreceber.model.Redeco;
import com.mercurio.lms.contasreceber.model.TratativaCobInadimplencia;
import com.mercurio.lms.contasreceber.model.dao.CobrancaInadimplenciaDAO;
import com.mercurio.lms.contasreceber.util.SituacaoFaturaLookup;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.cobrancaInadimplenciaService"
 */
public class CobrancaInadimplenciaService extends CrudService<CobrancaInadimplencia, Long> {

	private FaturaService faturaService;
	private UsuarioLMSService usuarioLMSService;

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public void setFaturaService(FaturaService faturaService){
		this.faturaService = faturaService;
	}
	
	private EnderecoPessoaService enderecoPessoaService;
	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	
	private RedecoService redecoService;
	public void setRedecoService(RedecoService redecoService){
		this.redecoService = redecoService;
	}
	
	
	/**
	 * Recupera uma inst�ncia de <code>CobrancaInadimplencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    @Override
	public CobrancaInadimplencia findById(java.lang.Long id) {
        return getCobrancaInadimplenciaDAO().findById(id);
    }
    
    /**
	 * Recupera uma inst�ncia de <code>CobrancaInadimplencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public CobrancaInadimplencia findByIdBasic(java.lang.Long id) {
        return getCobrancaInadimplenciaDAO().findByIdBasic(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    @Override
	public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }
    
    @Override
	public java.io.Serializable store(CobrancaInadimplencia bean) {
    	return super.store(bean);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public CobrancaInadimplencia store(CobrancaInadimplencia bean, ItemList items) {
    	boolean rollbackMasterId = bean.getIdCobrancaInadimplencia() == null;
    	
    	try {
			if (!items.hasItems())
				throw new BusinessException("LMS-36064");
			
			if (bean.getBlCobrancaEncerrada())
				throw new BusinessException("LMS-36138");
			
			this.beforeStore(bean);
			bean = this.getCobrancaInadimplenciaDAO().store(bean,items);
			
		} catch (RuntimeException e) {
			this.rollbackMasterState(bean, rollbackMasterId, e);
            items.rollbackItemsState();
            throw e;			
		}
		
		return bean;
     }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setCobrancaInadimplenciaDAO(CobrancaInadimplenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private CobrancaInadimplenciaDAO getCobrancaInadimplenciaDAO() {
        return (CobrancaInadimplenciaDAO) getDao();
    }
    
    /**
     * M�todo respons�vel por carregar dados p�ginados de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage contendo o resultado do hql.
     */
	public ResultSetPage findPaginatedByCobrancaInadimplencia(TypedFlatMap tfm) throws Exception{
		return getCobrancaInadimplenciaDAO().findPaginatedByCobrancaInadimplencia(tfm);
	}
	
	/**
     * M�todo respons�vel por fazer a contagem dos registros que retornam do hql para pagina��o.
     * @param criteria
     * @return Integer contendo o n�mero de registros retornados.
     */
    public Integer getRowCountByCobrancaInadimplencia(TypedFlatMap tfm) throws Exception{
    	return getCobrancaInadimplenciaDAO().getRowCountByCobrancaInadimplencia(tfm);
    }

    /**
     * M�todo respons�vel por carregar dados p�ginados de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage contendo o resultado do hql.
     */
    public List findPaginatedByFatura(Long idPai){
    	return getCobrancaInadimplenciaDAO().findPaginatedByFatura(idPai);
    }
    
    public List findPaginatedByTratativa(Long idPai){
    	return getCobrancaInadimplenciaDAO().findPaginatedByTratativa(idPai);
    }
    
    /**
     * M�todo respons�vel por fazer a contagem dos registros que retornam do hql.
     * @param criteria
     * @return Integer contendo o n�mero de registros retornados.
     */
    public Integer getRowCountByFatura(Long idPai){
    	return getCobrancaInadimplenciaDAO().getRowCountByFatura(idPai);
    }
    
    public Integer getRowCountByTratativa(Long idPai){
    	return getCobrancaInadimplenciaDAO().getRowCountByTratativa(idPai);
    }
    
    public ResultSetPage findFaturaByIdItemCobranca(Map parameters, ResultSetPage rspTmp) {
    	
    	List itemCobrancas = new ArrayList();
    	
    	List<ItemCobranca> faturas = (List <ItemCobranca>)rspTmp.getList();
    	
    	OrdenaFaturaItemCobranca ordem = new OrdenaFaturaItemCobranca();
    	Collections.sort(faturas, ordem); 

    	for (Iterator iter = faturas.iterator(); iter.hasNext();) {
			ItemCobranca ic = (ItemCobranca) iter.next();
			Map element = new HashMap();
			
			Fatura fatura = faturaService.findById(ic.getFatura().getIdFatura());
			Moeda moeda = fatura.getMoeda();
			
			//Juros at� a data atual
			BigDecimal jurosAteData = fatura.getVlJuroCalculado();
			
			//Valor total da fatura
			BigDecimal valorTotal = fatura.getVlTotal(); 
			
			//Dias de atraso (dataAtual - dataVencimento)
			BigDecimal diasAtraso = calculaDiasAtraso(fatura);
			
			BigDecimal jurosDia = null;
			BigDecimal percJurosMes = null;
			
			if( diasAtraso != null && valorTotal.longValue() > 0){
				//Juros ao dia (jurosAteData / diasAtraso)
				jurosDia = jurosAteData.divide(diasAtraso, 2, BigDecimal.ROUND_HALF_UP);
				
				//% de juros ao m�s (jurosDia  / valorTotal * 100 * 30)
				percJurosMes = jurosDia.divide(valorTotal, 15, BigDecimal.ROUND_HALF_UP);
				percJurosMes = percJurosMes.multiply(new BigDecimal(100));
				percJurosMes = percJurosMes.multiply(new BigDecimal(30));
				
				element.put("moedaJurosDia", moeda.getSiglaSimbolo());			
			}
			
			//Calcula Percentual Juro Di�rio
			BigDecimal percentualJurosMes = this.buscaJuroDiario(fatura);
			
			//Insere no Map os Atributo calculados acima
			
			element.put("jurosDia", jurosDia);
			element.put("diasAtraso", diasAtraso);
			element.put("percJurosMes", percentualJurosMes);
			element.put("nrFatura", fatura.getFilialByIdFilial().getSgFilial() + " " + FormatUtils.completaDados(ic.getFatura().getNrFatura()
					,"0", 10, 0, true)) ;
			element.put("moedaVlTotal", moeda.getSiglaSimbolo());
			element.put("vlTotal", valorTotal);			
			element.put("moedaJuroCalculado", moeda.getSiglaSimbolo());
			element.put("vlJuroCalculado", jurosAteData);
			element.put("dtVencimento", fatura.getDtVencimento());
			element.put("idItemCobranca",ic.getIdItemCobranca());
			element.put("tpSituacaoFatura",fatura.getTpSituacaoFatura());
			element.put("cliente",fatura.getCliente().getPessoa().getNmPessoa());
			
			itemCobrancas.add(element);
			
		}
    	
    	rspTmp.setList(itemCobrancas);
    	return rspTmp;
	}
    
    public ResultSetPage findTrataticaByIdItemCobranca(Map parameters, ResultSetPage rspTmp) {
    	List itemCobrancas = new ArrayList();
    	
    	List<ItemCobranca> tratativas = (List <ItemCobranca>)rspTmp.getList();
    	
    	for (Iterator iter = tratativas.iterator(); iter.hasNext();) {
    		TratativaCobInadimplencia trat = (TratativaCobInadimplencia) iter.next();
    		
			Map element = new HashMap();
    		
			element.put("idTratativaCobInadimplencia", trat.getIdTratativaCobInadimplencia());
			element.put("dtTratativa", trat.getDhTratativa());
			UsuarioLMS usuario = usuarioLMSService.findById(trat.getUsuario().getIdUsuario());
			element.put("usuarioTratativa", usuario.getUsuarioADSM().getNmUsuario());
			element.put("motivoInadimplencia", trat.getMotivoInadimplencia().getDescricao());
			element.put("dsPlanoAcao", trat.getDsPlanoAcao());
			element.put("dtPrevisaoTratativa", trat.getDtPrevSolucao());
			
			itemCobrancas.add(element);
		}
    	
    	rspTmp.setList(itemCobrancas);
    	return rspTmp;
	}
    
    
    /**
     * Busca Juro di�rio
     */
    private BigDecimal buscaJuroDiario(Fatura fatura) {
		
    	Filial fi = SessionUtils.getFilialSessao();
    	EnderecoPessoa ep = enderecoPessoaService.findEnderecoPessoaPadrao(fi.getPessoa().getIdPessoa());
    	Long pa = ep.getMunicipio().getUnidadeFederativa().getPais().getIdPais();
    	
		BigDecimal cotacaoIndFin = getCobrancaInadimplenciaDAO().buscaCotacaoIndFinanceiro(fatura.getDtEmissao(), pa);
		
		BigDecimal percentualJuros = null;
		BigDecimal percentualJurosMes = null;

		if (cotacaoIndFin != null) {
			if (fatura.getCliente().getPcJuroDiario() != null &&
				fatura.getCliente().getPcJuroDiario().compareTo(new BigDecimal(0.0)) != 0) { // != 0
				
				percentualJuros = fatura.getCliente().getPcJuroDiario();
			} else if (fatura.getFilialByIdFilialCobradora().getPcJuroDiario() != null &&
					   fatura.getFilialByIdFilialCobradora().getPcJuroDiario().compareTo(new BigDecimal(0.0)) != 0) { // != 0)
				
				percentualJuros = fatura.getFilialByIdFilialCobradora().getPcJuroDiario();
			} else {
				percentualJuros = cotacaoIndFin;
			}
			percentualJurosMes = percentualJuros.multiply(new BigDecimal(30)); 
		}    

		return percentualJurosMes;
    }
    
    /**
     * Calcula os dias de atraso para faturas nos seguintes casos : 
     * 	RC - Em recibo
     *  EM - Emitido
     *  BL - Em boleto
     *  RE - Em redeco
     *  e
     *  DT_VENCIMENTO < HOJE
     * @param fatura Fatura a ser analisada para o calculo dos dias de atraso
     * @return N�mero de dias de atraso
     */
    private BigDecimal calculaDiasAtraso(Fatura fatura) {
    	
    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		YearMonthDay dataVencimento = fatura.getDtVencimento();
		
		String situacaoFatura = fatura.getTpSituacaoFatura().getValue();
		
		BigDecimal retorno = null;
		
		if( (situacaoFatura.equals("RC") || situacaoFatura.equals("EM") || 
			 situacaoFatura.equals("BL") || situacaoFatura.equals("RE") || 
			 situacaoFatura.equals("LI")) &&
			 JTDateTimeUtils.comparaData(dataAtual,dataVencimento) > 0 ){
			
			if( situacaoFatura.equals("LI") ){				
				retorno = new BigDecimal(JTDateTimeUtils.getIntervalInDays(dataVencimento, fatura.getDtLiquidacao()));				
			} else {
				retorno = new BigDecimal(JTDateTimeUtils.getIntervalInDays(dataVencimento, dataAtual));	
			}			
			 
			//Retornar a quantidade de dias de atraso s� quando � maior que zero
			if (retorno.longValue() > 0){
				return retorno;	
			}
		}
		
		return null;
	}


	public void validateMoedaFatura(ItemList items, ItemListConfig config, Long idMaster, Long idFatura){
    	
    	Fatura faturaTela = faturaService.findByIdFatura(idFatura);
    	
    	boolean notEqualsMoeda = false;
    	
    	for (Iterator iter = items.iterator(idMaster, config); iter.hasNext();) {
			Fatura fatura = ((ItemCobranca) iter.next()).getFatura();
			
			if(!fatura.getMoeda().getIdMoeda().equals(faturaTela.getMoeda().getIdMoeda())){
				notEqualsMoeda = true;
			}
			
			break;
		}
    	
    	/** Caso as moedas n�o sejam iguais, lan�a exce��o */
    	if(notEqualsMoeda){
    		throw new BusinessException("LMS-36027");
    	}
    
	}
	
	/**
	 * M�todo respons�vel por buscar a cobrancaInadimplencia do itemCobranca passado por parametro
	 * 
	 * @param idItemCobranca
	 * @return CobrancaInadimplencia do itemCobranca
	 */
	public CobrancaInadimplencia findCobrancaInadimplenciaByIdItemCobranca(Long idItemCobranca){
		return getCobrancaInadimplenciaDAO().findCobrancaInadimplenciaByIdItemCobranca(idItemCobranca);
	}
	
	/**
	 * Retorna a lista de cobranca de inadimplencia que estam com blCobrancaEncerrada = 'N' e
	 * que os itens de cobran�a estam TODOS liquidados
	 * 
	 * @author Micka�l Jalbert
	 * @since 14/07/2006
	 * 
	 * @return List
	 */
	public List findCobrancaNaoEncerrado(){
		return getCobrancaInadimplenciaDAO().findCobrancaNaoEncerrado();
	}
	
	public void validateFaturaByCobrancaInadimplencia(TypedFlatMap parameters){
		
		Fatura fatura = faturaService.findById(parameters.getLong("fatura.idFatura"));
    	validateFaturaByTpSituacaoFatura(fatura);
		
		/**
		 * Resgata a filial da sess�o do usu�rio.
		 * Valida se a filial do usu�rio � igual a filial de cobranca ou a filial de faturamento da fatura
		 */
		Filial filial = (Filial)SessionContext.get(SessionKey.FILIAL_KEY);
		if(!fatura.getFilialByIdFilial().getIdFilial().equals(filial.getIdFilial()) 
					&& !fatura.getFilialByIdFilialCobradora().getIdFilial().equals(filial.getIdFilial())
					&& !filial.getSgFilial().equals("MTZ")){
			throw new BusinessException("LMS-36141");
		}
		
		/** Se a situa��o da fatura for em redeco e a finalidade for diferente de 'controle da
		 * filial, lan�ar a exce��o */
		if(fatura.getTpSituacaoFatura().getValue().equals("RE")){
			
			Redeco redeco = redecoService.findByFatura(fatura.getIdFatura());

			if(!redeco.getTpFinalidade().getValue().equals("CF")){
				throw new BusinessException("LMS-36209");
			}
			
		}
		
		if(parameters.getLong("masterId")!= null){
		/** Testa se a cobrancaInadimplencia pode ser exclu�da. (REGRA 4.2) */
			CobrancaInadimplencia ci = getCobrancaInadimplenciaDAO().findCobrancaInadimplenciaByIdBlEncerrada(parameters.getLong("masterId"), Boolean.FALSE);
			if(ci != null){
			throw new BusinessException("LMS-36138");
		}
		}
	}
	
	/**
	 * Retorna a lista de cobran�a de inadimplenca aberta da fatura informada onde o
	 * id da cobran�a � diferente do id de cobran�a informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 18/12/2006
	 * 
	 * @param Long idFatura
	 * @param Long idCobrancaInadimplenca
	 * @return List
	 */
    public List findByIdFatura(Long idFatura, Long idCobrancaInadimplenca){
		return getCobrancaInadimplenciaDAO().findByIdFatura(idFatura, idCobrancaInadimplenca);

    }	
    
    /**
     * Valida se o usu�rio pode efetuar alguma a��o(excluir, editar etc...) nas cobrancas de inadimpl�ncia 
     * 
     * RETURN TRUE -> O usu�rio pode realizar a��es sobre as cobrancas de inadimplencia
     * RETURN FALSE -> O usu�rio n�o pode realizar a��es sobre alguma das cobrancas de inadimplencia
     *
     * @author Hector Julian Esnaola Junior
     * @since 20/12/2006
     *
     * @param idsCobrancaInadimplencia
     * @return
     *
     */
    public void validateCobrancaInadimplencia(List idsCobrancaInadimplencia){
    	
    		//Boolean permissao = getCobrancaInadimplenciaDAO().validateCobrancaInadimplencia(idsCobrancaInadimplencia);
    		Boolean permissao = (SessionUtils.getFilialSessao().getSgFilial().equals("MTZ")) || getCobrancaInadimplenciaDAO().validateCobrancaInadimplencia(idsCobrancaInadimplencia);

	    	/** Caso a filial das faturas n�o sejam iguais a filial da sess�o do usu�rio, lan�a uma exception */
	    	if(!permissao)
	    		throw new BusinessException("LMS-36141");
	    
    }
    
    /**
     * Valida se o usu�rio pode efetuar alguma a��o(excluir, editar etc...) nas cobrancas de inadimpl�ncia 
     * 
     * @author Hector Julian Esnaola Junior
     * @since 20/12/2006
     *
     * @param idCobrancaInadimplencia
     * @return
     *
     */
    public void validateCobrancaInadimplencia(Long idCobrancaInadimplencia){
    	
    	/** S� testa quando j� existe uma cobranca de inadimplentes */ 
    	if( idCobrancaInadimplencia != null ){
	    
    		ArrayList<Long> idsCobrancaInadimplencia = new ArrayList();
	    	idsCobrancaInadimplencia.add(idCobrancaInadimplencia);
	    	
	    	validateCobrancaInadimplencia(idsCobrancaInadimplencia);
	    	
    	}
    	
    }

	public void validateFaturaByTpSituacaoFatura(Fatura fatura) {
	    	
	    if(!new SituacaoFaturaLookup(SituacaoFaturaLookup.DISPONIVEL_REDECO).validateTpSituacaoFatura(fatura.getTpSituacaoFatura().getValue())){
			throw new BusinessException("LMS-36111");
		}
		
	}
}
