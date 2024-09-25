package com.mercurio.lms.municipios.model.service;

import java.util.*;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.municipios.model.Feriado;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.dao.FeriadoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.feriadoService"
 */
public class FeriadoService extends CrudService<Feriado, Long> {

	private VigenciaService vigenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	
	public static final int ANO_DT_FERIADO =  2000;
	
	public ResultSetPage findPaginated(Map criteria) {
		
        ResultSetPage rsp = getFeriadoDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
        rsp.setList(defineAbrangencia(rsp.getList()));
	    return rsp;			
	}

	/**
	 * Recupera uma instância de <code>Feriado</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public Feriado findById(java.lang.Long id) {
        return (Feriado)super.findById(id);
    }

    public Map findByIdDetalhamento(java.lang.Long id) {
        Feriado feriado = (Feriado) super.findById(id);
        
        TypedFlatMap map = bean2map(feriado);        
        map.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(feriado));
        
        return map;
    }
               
    private TypedFlatMap bean2map(Feriado feriado){
    	TypedFlatMap map = new TypedFlatMap();
    	
    	map.put("idFeriado", feriado.getIdFeriado());
    	map.put("dtFeriado", feriado.getDtFeriado());
    	map.put("dsFeriado", feriado.getDsFeriado());
    	map.put("tpFeriado", feriado.getTpFeriado().getValue());
    	map.put("blFacultativo", feriado.getBlFacultativo());
    	map.put("dtVigenciaInicial", feriado.getDtVigenciaInicial());
    	map.put("dtVigenciaFinal", feriado.getDtVigenciaFinal());
    	
    	UnidadeFederativa uf = feriado.getUnidadeFederativa();    	
    	if (uf != null){
    		map.put("unidadeFederativa.sgUnidadeFederativa", uf.getSgUnidadeFederativa()); 
    		map.put("unidadeFederativa.idUnidadeFederativa", uf.getIdUnidadeFederativa());
    		map.put("unidadeFederativa.nmUnidadeFederativa", uf.getNmUnidadeFederativa());
    	}
    	
    	Pais pais = feriado.getPais();
    	if (pais != null){
    		map.put("pais.idPais", pais.getIdPais());
    		map.put("pais.nmPais", pais.getNmPais());
    	}
    	
    	Municipio municipio = feriado.getMunicipio();
    	if (municipio != null){
    		map.put("municipio.nmMunicipio", municipio.getNmMunicipio());
    		map.put("municipio.idMunicipio", municipio.getIdMunicipio());
    	}
    	    	
    	return map;
    }
    
    
	private List defineAbrangencia(List rspList){
		
		List newList = new ArrayList();   
		Iterator it = rspList.iterator();

		String abrangencia;
		String local = "local";
		String token = " - ";
		String municipal = configuracoesFacade.getMensagem("municipal");
		String estadual = configuracoesFacade.getMensagem("estadual");
		String nacional = configuracoesFacade.getMensagem("nacional");
		String mundial = configuracoesFacade.getMensagem("mundial");

		while (it.hasNext()){
    	  
			TypedFlatMap row = new TypedFlatMap();
			Feriado feriado = (Feriado) it.next();
        	row.put("idFeriado",feriado.getIdFeriado());
        	row.put("dtFeriado",feriado.getDtFeriado());
        	row.put("dsFeriado",feriado.getDsFeriado());
        	row.put("tpFeriado",feriado.getTpFeriado());
        	row.put("dtVigenciaInicial",feriado.getDtVigenciaInicial());
        	row.put("dtVigenciaFinal",feriado.getDtVigenciaFinal());

			if (feriado.getMunicipio() != null) {

				abrangencia = municipal;
				row.put(local, new StringBuilder(feriado.getMunicipio().getNmMunicipio()).append(token)
								.append(feriado.getUnidadeFederativa().getSgUnidadeFederativa()).append(token)
								.append(feriado.getPais().getNmPais()).toString());
				
			} else if (feriado.getUnidadeFederativa() != null) {
				
				abrangencia = estadual;		
				row.put(local, new StringBuilder(feriado.getUnidadeFederativa().getSgUnidadeFederativa())
									.append(token).append(feriado.getPais().getNmPais()).toString());
				
			} else if (feriado.getPais() != null) {
				
				abrangencia = nacional;
				row.put(local, feriado.getPais().getNmPais());
				
			} else{
				
				abrangencia = mundial;

			}
		
			row.put("abrangencia",abrangencia);
			
			newList.add(row);
		}
		
		return newList;
	
	}
	
	protected void beforeRemoveById(Long id) {
		Feriado feriado = findById((Long)id);
		JTVigenciaUtils.validaVigenciaRemocao(feriado);
		super.beforeRemoveById(id);
	}
    protected void beforeRemoveByIds(List ids) {
    	Feriado feriado = null;
    	for(int x = 0; x < ids.size(); x++) {
    		feriado = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(feriado);
    	}
    	super.beforeRemoveByIds(ids);
    }

	public Integer getRowCount(Map criteria) {
		Integer rowCountCustom = this.getFeriadoDAO().getRowCount(criteria);
		return rowCountCustom;
	}
	
	/**
	 * Valida se existe um feriado vigente para o municipio
	 * @param idMunicipio
	 * @param dtFeriado
	 * @return
	 */
	public boolean validateExisteFeriadoByMunicipio(Long idMunicipio, YearMonthDay dtFeriado){
		return getFeriadoDAO().validateExisteFeriadoByMunicipio(idMunicipio, dtFeriado, dtFeriado, dtFeriado);
	}
	
	/**
	 * Retorna uma lista com o atributo DT_FERIADO vigentes de um Município 
	 * @param idMunicipio
	 * @return
	 */
	public List findAllDtFeriadosVigentesByIdMunicipio(Long idMunicipio) {
		return getFeriadoDAO().findAllDtFeriadosVigentesByIdMunicipio(idMunicipio);
	}
	
	/**
	 * Retorna a lista de feriados encontrados de acordo com os parametros passados, O list contem as entidades de Feriado.
	 * Ela foi escrita em cima da funcao validateExisteFeriadoByMunicipio, só que dando a possibilidade de passar varias datas
	 * e com a respostas ver se é ou nao feriado interando a lista (So os feriados retornaram na lista). 
	 * Houve essa necessidade pois havia funcoes que chamavam inumeras vezes a funcao validate deixando assim o metodo bastante que chamava
	 * bastante lento.
	 * @param idMunicipio
	 * @param dtFeriados
	 * @param dtVigenciaInicials
	 * @param dtVigenciaFinals
	 * @return
	 */
	public List findFeriadoByMunicipio(List idsMunicipio, List dtFeriados, List dtVigenciaInicials, List dtVigenciaFinals) {
		return getFeriadoDAO().findFeriadoByMunicipio(idsMunicipio,dtFeriados,dtVigenciaInicials,dtVigenciaFinals);
	}
	
	public List findFeriadoByMunicipio(List idsMunicipio, List dtFeriados) {
		return findFeriadoByMunicipio(idsMunicipio,dtFeriados,dtFeriados,dtFeriados);
	}
	
	public List findFeriadoByMunicipio(Long idMunicipio, List dtFeriados) {
		List idsMunicipio = new ArrayList();
		idsMunicipio.add(idMunicipio);
		return findFeriadoByMunicipio(idsMunicipio,dtFeriados);
	}
	/**
	 * Valida se existe um feriado para o municipio dentro da vigencia informada
	 * @param idMunicipio
	 * @param dtFeriado
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public boolean validateExisteFeriadoByMunicipio(Long idMunicipio, YearMonthDay dtFeriado, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return getFeriadoDAO().validateExisteFeriadoByMunicipio(idMunicipio, dtFeriado, dtVigenciaInicial, dtVigenciaFinal);
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
    public Map store(Map map) {
    	Feriado feriado = new Feriado();
    	ReflectionUtils.copyNestedBean(feriado, map);
    	
    	feriado.setDtFeriado(JTDateTimeUtils.setYear(feriado.getDtFeriado(), FeriadoService.ANO_DT_FERIADO));
    	vigenciaService.validaVigenciaBeforeStore(feriado);
		
    	//Verifica se o feriado ja nao esta cadastrado para a mesma vigencia e abrangencia
    	if (getFeriadoDAO().verificaFeriadoAbrangencia(feriado.getIdFeriado(), 
													   feriado.getMunicipio() != null ? feriado.getMunicipio().getIdMunicipio() : null, 
													   feriado.getUnidadeFederativa() != null ? feriado.getUnidadeFederativa().getIdUnidadeFederativa() : null, 
													   feriado.getPais() != null ? feriado.getPais().getIdPais() : null, 
													   feriado.getDtFeriado(), 
													   feriado.getDtVigenciaInicial(), 
													   feriado.getDtVigenciaFinal()))
    				throw new BusinessException("LMS-00003");
    	
    	//Ao inserir um feriado, verifica se ja nao existe o mesmo em outra abrangencia
    	if (feriado.getIdFeriado() == null){
			if ("S".equals(map.get("verificaFeriadoExistente")) &&
					this.getFeriadoDAO().verificaFeriadoExistente(feriado.getIdFeriado(), 
																  feriado.getMunicipio() != null ? feriado.getMunicipio().getIdMunicipio() : null, 
																  feriado.getUnidadeFederativa() != null ? feriado.getUnidadeFederativa().getIdUnidadeFederativa() : null, 
																  feriado.getPais() != null ? feriado.getPais().getIdPais() : null, 
																  feriado.getDtFeriado(), 
																  feriado.getDtVigenciaInicial(), 
																  feriado.getDtVigenciaFinal()))
					throw new BusinessException("LMS-29032");
    	}		
				
        super.store(feriado);
        Map retorno = new HashMap();
        
        retorno.put("idFeriado", feriado.getIdFeriado());
        retorno.put("dtVigenciaInicial", feriado.getDtVigenciaInicial());
        retorno.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(feriado));
        
    	return retorno;
    }
    

	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFeriadoDAO(FeriadoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private FeriadoDAO getFeriadoDAO() {
        return (FeriadoDAO) getDao();
    }
    
    public List findFeriadosVigentesByIdMunicipio(Long idMunicipio){
    	return getFeriadoDAO().findFeriadosVigentesByIdMunicipio(idMunicipio);
    }

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
    
	public Boolean validateDiaUtil(YearMonthDay dtUtil, Long idMunicipio) {
		return getFeriadoDAO().validateDiaUtil(dtUtil, idMunicipio);
	}
    
	/**
	 * Retorna uma lista (feriados) vigentes de
	 * um Município
	 * 
	 * @param idMunicipio
	 * @return
	 */
	public List<Feriado> findAllFeriadosByIdMunicipio(Long idMunicipio) {
		return getFeriadoDAO().findAllFeriadosByIdMunicipio(idMunicipio);
	}

	/**
	 * Retorna uma lista com os feriados nacionais e mundial vigentes por Município
	 * @param idMunicipio
	 * @return
	 */
	public List findDtFeriadosNacionaisEMundialByIdMunicipio(Long idMunicipio) {
		return getFeriadoDAO().findDtFeriadosNacionaisEMundialByIdMunicipio(idMunicipio);
	}
}
