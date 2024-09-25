package com.mercurio.lms.seguros.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.ApoliceSeguroAnexo;
import com.mercurio.lms.seguros.model.ApoliceSeguroParcela;
import com.mercurio.lms.seguros.model.dao.ApoliceSeguroDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.apoliceSeguroService"
 */
public class ApoliceSeguroService extends CrudService<ApoliceSeguro, Long> {

	private static final String NR_APOLICE = "nrApolice";
	private static final String SEGURADO = "segurado";
	private static final String DT_VIGENCIA = "dtVigencia";
	private static final String ID_PESSOA = "idPessoa";
	private static final String REGULADORA_SEGURO = "reguladoraSeguro";
	private static final String ID_REGULADORA = "idReguladora";
	private static final String ID_SEGURADORA = "idSeguradora";
	private static final String SEGURADORA = "seguradora";
	private static final String TIPO_SEGURO = "tipoSeguro";
	private static final String ID_TIPO_SEGURO = "idTipoSeguro";
	private static final String MOEDA = "moeda";
	private static final String ID_MOEDA = "idMoeda";
	private static final String VL_LIMITE_APOLICE = "vlLimiteApolice";
	private static final String NM_SEGURADORA = "nmSeguradora";
	private static final String ID_APOLICE_SEGURO = "idApoliceSeguro";
	private ApoliceSeguroParcelasService apoliceSeguroParcelasService;
	private ApoliceSeguroAnexoService apoliceSeguroAnexoService;

	/**
	 * Recupera uma instância de <code>ApoliceSeguro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public ApoliceSeguro findById(java.lang.Long id) {
        return (ApoliceSeguro)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ApoliceSeguro bean) {
        return super.store(bean);
    }

    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable storeByManterApoliceSeguro(ApoliceSeguro bean, ItemList listParcelas, ItemList listAnexos) {
    	verificaApoliceVigente(bean);
    	    	
    	store(bean);
    	
		if(listParcelas != null){
    		for (Object parcelas : listParcelas.getNewOrModifiedItems()) {
    			ApoliceSeguroParcela apoliceParcela = (ApoliceSeguroParcela) parcelas;
    			apoliceParcela.setApoliceSeguro(bean);
    			apoliceSeguroParcelasService.store(apoliceParcela);
    }

    		for (Object parcelas : listParcelas.getRemovedItems()) {
    			ApoliceSeguroParcela apoliceParcela = (ApoliceSeguroParcela) parcelas;
    			apoliceSeguroParcelasService.removeById(apoliceParcela.getIdApoliceSeguroParcela());
    		}
    	}
    
		if(listAnexos != null){
    		for (Object anexos : listAnexos.getNewOrModifiedItems()) {
    			ApoliceSeguroAnexo apoliceAnexos = (ApoliceSeguroAnexo) anexos;
    			apoliceAnexos.setApoliceSeguro(bean);
    			apoliceSeguroAnexoService.store(apoliceAnexos);
    		}
    		
    		for (Object anexos : listAnexos.getRemovedItems()) {
    			ApoliceSeguroAnexo apoliceAnexos = (ApoliceSeguroAnexo) anexos;
    			apoliceSeguroAnexoService.removeById(apoliceAnexos.getIdApoliceSeguroAnexo());
    		}
    	}
    	
        return bean.getIdApoliceSeguro();
    }

    
    /**
     * Verifica se já existe uma apólice vigente p/ o mesmo tipo de seguro com a mesma reguladora.
     * 
     * @param bean
     */
    private void verificaApoliceVigente(ApoliceSeguro bean) {
		if (getApoliceSeguroDAO().verificaApoliceVigente(	bean.getIdApoliceSeguro(), 
															bean.getTipoSeguro().getIdTipoSeguro(), 
															bean.getReguladoraSeguro().getIdReguladora(), 
															bean.getDtVigenciaInicial(),
															bean.getDtVigenciaFinal())){
			Object[] obj = {bean.getTipoSeguro().getSgTipo(), bean.getReguladoraSeguro().getPessoa().getNmPessoa()};
			throw new BusinessException("LMS-22006", obj);
		}
    }

    

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setApoliceSeguroDAO(ApoliceSeguroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ApoliceSeguroDAO getApoliceSeguroDAO() {
        return (ApoliceSeguroDAO) getDao();
    }
    
    
    /**
     * 
     * @param criteria
     * @param isRowCount
     * @return
     */
    //FIXME Melhorar código. Comparações podem não estar funcionando. Trocar para TypedFlatMap
    private Object addCriteriaByApoliceSeguro(Map criteria, boolean isRowCount) {
    	YearMonthDay dtVigencia  = (!"".equals(criteria.get(DT_VIGENCIA)))? (YearMonthDay)ReflectionUtils.toObject((String)criteria.get(DT_VIGENCIA), YearMonthDay.class) : null;
    	
    	Long idSegurado = (!"".equals(((Map) criteria.get(SEGURADO)).get(ID_PESSOA)))? Long.valueOf(((Map) criteria.get(SEGURADO)).get(ID_PESSOA).toString()): null;
    	
    	String nrApolice = (!"".equals(criteria.get(NR_APOLICE)))? criteria.get(NR_APOLICE).toString(): null;
    	Long idReguladora = (!"".equals(((Map) criteria.get(REGULADORA_SEGURO)).get(ID_REGULADORA)))? Long.valueOf(((Map) criteria.get(REGULADORA_SEGURO)).get(ID_REGULADORA).toString()): null;
    	Long idSeguradora = (!"".equals(((Map) criteria.get(SEGURADORA)).get(ID_SEGURADORA)))? Long.valueOf(((Map) criteria.get(SEGURADORA)).get(ID_SEGURADORA).toString()): null;
    	Long idTipoSeguro = (!"".equals(((Map) criteria.get(TIPO_SEGURO)).get(ID_TIPO_SEGURO)))? Long.valueOf(((Map) criteria.get(TIPO_SEGURO)).get(ID_TIPO_SEGURO).toString()): null;
    	Long idMoeda = (!"".equals(((Map) criteria.get(MOEDA)).get(ID_MOEDA)))? Long.valueOf(((Map) criteria.get(MOEDA)).get(ID_MOEDA).toString()): null;
    	BigDecimal vlLimiteApolice = (!"".equals(criteria.get(VL_LIMITE_APOLICE)))? new BigDecimal(criteria.get(VL_LIMITE_APOLICE).toString()): null;
    	
    	if (isRowCount) {
			return getApoliceSeguroDAO().getRowCountByDtVigencia(idSegurado, dtVigencia, nrApolice, idReguladora, idSeguradora, idTipoSeguro, idMoeda, vlLimiteApolice);
		} else {
			return getApoliceSeguroDAO().findPaginatedByRotaColetaEntrega(idSegurado, dtVigencia, nrApolice, idReguladora, idSeguradora, idTipoSeguro, idMoeda, vlLimiteApolice, FindDefinition.createFindDefinition(criteria));
		}
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
     * parametros.
     * 
     * @param criteria Map
     * @return Integer numero de registros
     */
    public Integer getRowCountApolicesSeguro(Map criteria) {
    	return (Integer)addCriteriaByApoliceSeguro(criteria, true);
    } 
    
    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * 
     * @param criteria TypedFlatMap
     * @return ResultSetPage com os dados da grid.
     */
    public ResultSetPage findPaginatedApolicesSeguro(Map criteria) {
    	return (ResultSetPage)addCriteriaByApoliceSeguro(criteria, false);
    }
    
    /**
     * LMS-6146 - Retorna um map com os objetos a serem mostrados na grid da aba Parcelas
     * 
     * @param criteria TypedFlatMap
     * @return ResultSetPage com os dados da grid.
     */
    public ResultSetPage findPaginatedParcela(TypedFlatMap criteria) {
    	return getApoliceSeguroDAO().findPaginatedParcelaByIdApoliceSeguro(criteria.getLong(ID_APOLICE_SEGURO), FindDefinition.createFindDefinition(criteria));
    }
    
    
    /**
     * Retorna as apólices de seguro cfe tpModal e dtVigenciaInicial e dtVigenciaFinal
     * @param tpModal modal da apólice
     * @param dtVigencia dtVigenciaInicial e dtVigenciaFinal
     * @return List
     */
	public List<ApoliceSeguro> retornaApolices(String tpModal, Date dtVigencia){
		return getApoliceSeguroDAO().retornaApolices(tpModal, dtVigencia);
	}


	/*
	 * Retorna valores para geração obrigatória das informações 
	 * de Seguros no XML do CT-e.
	 * 
	 * Jira LMS-3996
	 * 
	 * @param idServico
	 * @param sgTipo
	 * @return List
	 * @return
	 */
	public List findSegValues(String sgTipo) {
		return getApoliceSeguroDAO().findSegValues(sgTipo);
	}

	public TypedFlatMap findSeguroPorTipo() {
		List<ApoliceSeguro> apolices = getApoliceSeguroDAO().findSeguroPorTipo();
		TypedFlatMap map = new TypedFlatMap();
		String nmPessoa = "";
		String nrApolice = null;
		Long idApoliceSeguro = null;
		
		if (apolices != null && !apolices.isEmpty()){
			ApoliceSeguro apoliceSeguro = (ApoliceSeguro) apolices.get(0);
			nmPessoa = apoliceSeguro.getSeguradora().getPessoa().getNmPessoa();
			nrApolice = apoliceSeguro.getNrApolice();
			idApoliceSeguro = apoliceSeguro.getIdApoliceSeguro();
		}
		
		map.put(NM_SEGURADORA, nmPessoa);
		map.put(NR_APOLICE, nrApolice);
		map.put(ID_APOLICE_SEGURO, idApoliceSeguro);
		
		return map;
	}
	
	public TypedFlatMap findSeguroPorAwb(String dsApolice){
		List<ApoliceSeguro> apolices = new ArrayList<ApoliceSeguro>();
		
		if (dsApolice != null){
			apolices = getApoliceSeguroDAO().findApoliceSeguroByNrApolice(dsApolice);
		}
		  
		TypedFlatMap map = new TypedFlatMap();
		String nmPessoa = new String();
		String nrApolice = null;
		Long idApolice = null;
		
		if (apolices != null && !apolices.isEmpty()){
			ApoliceSeguro apoliceSeguro = (ApoliceSeguro) apolices.get(0);
			idApolice = apoliceSeguro.getIdApoliceSeguro();
			nrApolice = apoliceSeguro.getNrApolice();
			nmPessoa = apoliceSeguro.getSeguradora().getPessoa().getNmPessoa();
		}
		
		map.put(NM_SEGURADORA, nmPessoa);
		map.put(NR_APOLICE, nrApolice);
		map.put(ID_APOLICE_SEGURO, idApolice);
		
		return map;
	}
	
	public ApoliceSeguroParcelasService getApoliceSeguroParcelasService() {
		return apoliceSeguroParcelasService;
	}

	public void setApoliceSeguroParcelasService(
			ApoliceSeguroParcelasService apoliceSeguroParcelasService) {
		this.apoliceSeguroParcelasService = apoliceSeguroParcelasService;
	}

	public ApoliceSeguroAnexoService getApoliceSeguroAnexoService() {
		return apoliceSeguroAnexoService;
	}

	public void setApoliceSeguroAnexoService(
			ApoliceSeguroAnexoService apoliceSeguroAnexoService) {
		this.apoliceSeguroAnexoService = apoliceSeguroAnexoService;
	}
	
	//LMS-6178
	public String findCorretaByTipoSeguro(Long idTipoSeguro, String dhSinistro) {
		return getApoliceSeguroDAO().findCorretoraByIdTipoSeguro(idTipoSeguro, dhSinistro);
	}
	
	//LMS-6178
	public String findSeguradoraByTipoSeguro(Long idTipoSeguro, String dhSinistro) {
		return getApoliceSeguroDAO().findSeguradoraByIdTipoSeguro(idTipoSeguro, dhSinistro);
	}

	/**
	 * LMS-7285 - Atualiza valor limite para controle de carga de determinada
	 * {@link ApoliceSeguro}.
	 * 
	 * @param idApoliceSeguro
	 *            id da {@link ApoliceSeguro}
	 * @param vlLimiteControleCarga
	 *            valor limite para controle
	 */
	public void storeVlLimiteControleCarga(Long idApoliceSeguro, BigDecimal vlLimiteControleCarga) {
		getApoliceSeguroDAO().storeVlLimiteControleCarga(idApoliceSeguro, vlLimiteControleCarga);
	}

}
