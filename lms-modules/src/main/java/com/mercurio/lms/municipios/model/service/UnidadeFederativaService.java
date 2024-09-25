package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.dao.UnidadeFederativaDAO;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.unidadeFederativaService"
 */
public class UnidadeFederativaService extends CrudService<UnidadeFederativa, Long> {
    
	/**
	 * Recupera uma instância de <code>UnidadeFederativa</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public UnidadeFederativa findById(java.lang.Long id) {
    	return (UnidadeFederativa)super.findById(id);
    }
    
    public UnidadeFederativa findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
    	return (UnidadeFederativa)super.findByIdInitLazyProperties(id, initializeLazyProperties);
    }
    
    /**
     * Busca dados para Combobox da UnidadeFederativa
     *@author Robson Edemar Gehl
     * @param map
     * @return
     */
    public List findCombo(Map map){
    	return getUnidadeFederativaDAO().findCombo(map);
    }
    
    /**
     * Busca dados ativos para Combobox da UnidadeFederativa
     * @param map
     * @return
     */
    public List findComboAtivo(Map map){
    	map.put("tpSituacao", ConstantesVendas.SITUACAO_ATIVO);
    	return getUnidadeFederativaDAO().findCombo(map);
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
    public java.io.Serializable store(UnidadeFederativa bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setUnidadeFederativaDAO(UnidadeFederativaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private UnidadeFederativaDAO getUnidadeFederativaDAO() {
        return (UnidadeFederativaDAO) getDao();
    }
    
    /**
	 * Identifica quais as UFs possuem atendimento pela empresa passada como parâmetro.
	 * @author Andre Valadas
	 * @param sgPais
	 * @param tpEmpresa
	 * @return
	 */
	public List<TypedFlatMap> findUnidadeFederativa(String sgPais, String tpEmpresa) {
		List<TypedFlatMap> toReturn = new ArrayList<TypedFlatMap>();
		List<Object[]> result = getUnidadeFederativaDAO().findUnidadeFederativa(sgPais, tpEmpresa);
		for (Object[] objects : result) {
			TypedFlatMap data = new TypedFlatMap();
			Long idUnidadeFederativa = LongUtils.getLong(objects[0]);
			String sgUnidadeFederativa = String.valueOf(objects[1]);
			String nmUnidadeFederativa = String.valueOf(objects[2]);
			data.put("idUnidadeFederativa", idUnidadeFederativa);
			data.put("sgUnidadeFederativa", sgUnidadeFederativa);
			data.put("nmUnidadeFederativa", nmUnidadeFederativa);
			data.put("siglaDescricao", sgUnidadeFederativa.concat(" - ").concat(nmUnidadeFederativa));
			toReturn.add(data);
		}
		return toReturn;
	}

	/**
 	* Obtem as unidades federativas através da sigls do pais
 	* @param sgPais
 	* @return List
 	*/
    public List findUfsBySgPais(String sgPais){
    	return getUnidadeFederativaDAO().findByPais(null, sgPais.toUpperCase(), null);
    } 	

    public List findUfBySgAndMunicipio(String sgUF,Long idMunicipio) {
    	return getUnidadeFederativaDAO().findUfBySgAndMunicipio(sgUF,idMunicipio);
    }
    
    public List<Map<String, Object>> findUfBySgAndPais(String sgUF,Long idPais) {
    	return getUnidadeFederativaDAO().findUfBySgAndPais(sgUF,idPais);
    }
    
    public List findUnidadeFederativaBySgPaisAndSgUf(String sgPais,String uf) {
    	return getUnidadeFederativaDAO().findUfBySgPaisAndSgUf(sgPais,uf);
    }
    
    public List findByPais(Map criteria) {
    	if(criteria == null) {
    		return null;
    	}

    	Object objIdPais = ReflectionUtils.getNestedBeanPropertyValue(criteria, "pais.idPais");
    	Object objSgPais = ReflectionUtils.getNestedBeanPropertyValue(criteria, "pais.sgPais");
    	Object objTpSituacao = ReflectionUtils.getNestedBeanPropertyValue(criteria, "tpSituacao");

    	Long idPais = null;
    	if(objIdPais != null) {
    		idPais = LongUtils.getLong(objIdPais);
    	}
    	String sgPais = null;
    	if(objSgPais != null) {
    		sgPais = String.valueOf(objSgPais);
    	}
    	String tpSituacao = null;
    	if(objTpSituacao != null) {
    		tpSituacao = String.valueOf(objTpSituacao);
    	}

    	List result = getUnidadeFederativaDAO().findByPais(idPais, sgPais, tpSituacao);
    	for (Iterator iter = result.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			String sigla = (String)map.get("sgUnidadeFederativa");
			String nome = (String)map.get("nmUnidadeFederativa");
			map.put("siglaDescricao", sigla.concat(" - ").concat(nome));
		}
    	return result;
    }

    public List<Map> findUfsAtivasByPais(Long idPais) {
    	return getUnidadeFederativaDAO().findUfsAtivasByPais(idPais);
    }
    
    public List findUfsByPais(Long idPais, String tpSituacao){
    	return getUnidadeFederativaDAO().findUfsByPais(idPais, tpSituacao);
    }

    public UnidadeFederativa findUnidadeFederativa(Long idUnidadeFederativa, Long idPais){
    	return getUnidadeFederativaDAO().findUnidadeFederativa(idUnidadeFederativa, idPais);
    }

    /**
     * Valida o intervalo de ceps apartir de uma chamada do javascript.
     *   
     * @param ceps Map contendo nrCepInicial e nrCepFinal
     * @return Map <code>resultado</code> contendo 
     * <code>true</code> ou <code>false</code>. 
     */
    public Map validaIntervaloCeps(Map map) {
    	Map resultado = new HashMap();
    	
    	/**
    	 * representa o último campo da tela que foi alterado.
    	 * será necessário para saber em qual dos dois campos
    	 * de cep (incial, final) será setado o foco.
    	 */ 
    	String focusField = (String) map.get("focusField");
    	String nrCepInicial = (String) map.get("nrCepInicial");
    	String nrCepFinal = (String) map.get("nrCepFinal");

    	resultado.put("focusField", focusField);
    	
    	if (nrCepInicial.compareToIgnoreCase(nrCepFinal) <= 0 ) 
    		resultado.put("resultado", "TRUE");    	
    	else resultado.put("resultado", "FALSE");
    	
    	return resultado;
    }


	protected UnidadeFederativa beforeStore(UnidadeFederativa bean) {
		UnidadeFederativa uf = (UnidadeFederativa) bean;
			
		uf.getPais().setSgPais(null);
		System.out.println("11");
		
		if(bean.getNrPrazoCancCte() != null && bean.getNrPrazoCancCte().longValue()== 0){
			bean.setNrPrazoCancCte(null);
		}
		
    	return super.beforeStore(bean);
	}
	
	/**
	 * Compara os paises das Unidades Federativas.<BR>
	 *@author Robson Edemar Gehl
	 * @param idUf1 identificador da unidade federativa
	 * @param idUf2 identificador da unidade federativa
	 * @return true, se o Pais for o mesmo nas unidades federativas; false, para diferentes.
	 */
	public boolean validateEqualsPais(Long idUf1, Long idUf2){
		
		if (idUf1 == null || idUf2 == null){
			throw new IllegalArgumentException("Os dois identificadores devem ser informados.");
		}
		
		if (idUf1.equals(idUf2)) return true;

		return getUnidadeFederativaDAO().validateEqualsPais(idUf1, idUf2);
	}

	public Integer getRowCountIncideIss(Long idUf, Boolean incideIss) {
		return getUnidadeFederativaDAO().getRowCountIncideIss(idUf, incideIss);
	}

	/**
	 * Busca a Unidade Federativa de acordo com o id do Município Sede
	 * @param idMunicipioSede Identificador do Município
	 * @return UnidadeFederativa
	 */
	public UnidadeFederativa findUFByIdMunicipio(Long idMunicipioSede) {
		return getUnidadeFederativaDAO().findUFByIdMunicipio(idMunicipioSede);
	}
	
	/**
	 * Retorna a unidade federativa do endereço padrão da pessoa informada.
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/08/2006
	 * 
	 * @param Long idPessoa
	 * @return UnidadeFederativa
	 */
	public UnidadeFederativa findByIdPessoa(Long idPessoa) {
		return getUnidadeFederativaDAO().findByIdPessoa(idPessoa);
	}	
	
	/**
	 * Busca o identificador da unidade federativa a partir do endereco da
	 * pessoa para ser utilizado no processo de confirmação de saída de veiculo
	 * da portaria.
	 * 
	 * @author Luis Carlos Poletto
	 * @param idPessoa
	 *            identificador da pessoa usada como filtro
	 * @return identificador da unidade federativa do endereço padrão
	 */
	public Long findConfirmacaoSaida(Long idPessoa) {
		return getUnidadeFederativaDAO().findConfirmacaoSaida(idPessoa);
	}

	/**
	 * @see 
	 *      getUnidadeFederativaDAO().findBySgUnidadeFederativa(sgUnidadeFederativa
	 *      )
	 * @param findBySgUnidadeFederativa
	 * @return
	 */
	public UnidadeFederativa findBySgUnidadeFederativa(String sgUnidadeFederativa){
		return getUnidadeFederativaDAO().findBySgUnidadeFederativa(sgUnidadeFederativa);
	}
        
	public UnidadeFederativa findBySgUnidadeFederativaSgPais(String sgUnidadeFederativa, String sgPais){
		return getUnidadeFederativaDAO().findBySgUnidadeFederativaSgPais(sgUnidadeFederativa, sgPais);
	}
	
	public UnidadeFederativa findBySgUnidadeFederativaIdPais(String sgUnidadeFederativa, Long idPais){
		return getUnidadeFederativaDAO().findBySgUnidadeFederativaIdPais(sgUnidadeFederativa, idPais);
	}

	public List<UnidadeFederativa> findUnidadesFederativasByAereportosAtivos(){
		return getUnidadeFederativaDAO().findUnidadesFederativasByAereportosAtivos();
	}
	
	/**
	 * Efetua pesquisa para a suggest de unidade federativa.
	 * 
	 * @param idPais
	 * @param sgUnidadeFederativa
	 * @param nmUnidadeFederativa
	 * @param limiteRegistros
	 * 
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> findUnidadeFederativaSuggest(Long idPais, String sgUnidadeFederativa, String nmUnidadeFederativa, Integer limiteRegistros) {
		return getUnidadeFederativaDAO().findUnidadeFederativaSuggest(idPais, sgUnidadeFederativa, nmUnidadeFederativa, limiteRegistros);
	}

	public String findSgUnidadeFederativaByIdDoctoServico(Long idDoctoServico) {
		return getUnidadeFederativaDAO().findSgUnidadeFederativaByIdDoctoServico(idDoctoServico);
	}
        
}
