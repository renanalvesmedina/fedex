package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.Cep;
import com.mercurio.lms.configuracoes.model.dao.CepDAO;
import com.mercurio.lms.configuracoes.model.param.PesquisarCepParam;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.util.FormatUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * @author Robson Edemar Gehl
 * @spring.bean id="lms.configuracoes.cepService"
 */
public class CepService extends CrudService<Cep, Long> {
	private PaisService paisService;

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	/**
	 * Recupera uma inst�ncia de <code>Servico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public Cep findById(java.lang.Long id) {
        return (Cep)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    public List findLookup(Map criteria) {
    	PesquisarCepParam param = new PesquisarCepParam();
    	param.setNrCep(MapUtils.getString(criteria, "cepCriteria"));
    	Map<String, Object> municipio = MapUtils.getMap(criteria, "municipio");
    	if(municipio != null) {
    		param.setIdMunicipio(MapUtils.getLong(municipio, "idMunicipio"));
    		Map<String, Object> unidadeFederativa = MapUtils.getMap(municipio, "unidadeFederativa");
    		if(unidadeFederativa != null) {
    			param.setIdUnidadeFederativa(MapUtils.getLong(unidadeFederativa, "idUnidadeFederativa"));
    			Map<String, Object> pais = MapUtils.getMap(unidadeFederativa, "pais");
    			if(pais != null) {
    				param.setIdPais(MapUtils.getLong(pais, "idPais"));
    			}
    		}
    	}
    	return findCepLookup(param);
    }
    
    /**
	 * Busca dados do cep para pesquisa em Lookups retornando apenas
	 * o necessario na forma de Map.<br/>
	 * Este metodo foi implementado para questoes de performance.
	 * @param map
	 * @return
	 */
	public List findDadosLookup(Map map) {
		return getCepDAO().findDadosLookup(map);
	}
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(Cep bean) {
        return super.store(bean);
    }

	public void setCepDAO(CepDAO dao){
		setDao( dao );
	}

	public CepDAO getCepDAO(){
		return (CepDAO) getDao();
	}

	/**
	 * Busca o cep informado baseando-se no pais
	 *
	 * @author Jos� Rodrigo Moraes
	 * @since 23/01/2007
	 *
	 * @param nrCep N�mero do CEP
	 * @param idPais Identificador do Pa�s
	 * @return Lista de ceps
	 */
	public List<Cep> findCepLookup(PesquisarCepParam param) {

		String nrCep = param.getNrCep();
		if(!nrCep.endsWith("%")) {
			nrCep = nrCep + "%";
			param.setNrCep(nrCep);
		}
		
		if(nrCep.length() < 6) {
			throw new BusinessException("LMS-29173");
		}else if(nrCep.length() > 9){
			throw new BusinessException("LMS-29180");			
		}
		transformIdPaisSgPais(param);
		List<Cep> ceps = getCepDAO().findCepLookup(param);
		if(ceps.size() == 1) {
			Cep cep = ceps.get(0);
			Municipio municipio = cep.getMunicipio();
			
			// Caso onlyActives seja null ou true, deve retornar somente cep's ativos.
			if ( param.getOnlyActives() == null 
					|| param.getOnlyActives() ) {
				
				if("I".equals(municipio.getTpSituacao().getValue())
						|| "I".equals(municipio.getUnidadeFederativa().getTpSituacao().getValue())
						|| "I".equals(municipio.getUnidadeFederativa().getPais().getTpSituacao().getValue())
				) {
					throw new BusinessException("LMS-29172");
				}
				
			}
		}
		return ceps;
	}
	
	/**
	 * Busca as informa��es sobre o CEP informado.
	 *
	 * @author Roberto Coral Azambuja
	 * @since 27/04/2009
	 *
	 * @param nrCep N�mero do CEP
	 * @return Lista de ceps
	 */
	public List<Cep> findCepByNrCep(String nrCep) {			
		return getCepDAO().findCepByNrCep(nrCep);
	}
	
	/**
	 * M�todo criado depois do findCepLookup, faz a mesma coisa, porem a valida��o do length 
	 * do nrCep leva em considera��o o pais.
	 * 
	 * TODO Deve ser melhorado para n�o ficar essa replica��o de c�digo.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/10/2007
	 *
	 * @param param
	 * @return
	 *
	 */
	public List<Cep> findCepLookupByManterEnderecoPessoa(PesquisarCepParam param) {
		
		String nrCep = param.getNrCep();
		
		Pais pais = null;
		if(param.getIdPais() != null){
			pais = paisService.findById(param.getIdPais());			
		}
		
		//independe do pa�s pois no banco o tamanho do campo � 8.
		if(nrCep.replace("%", "").length() > 8) throw new BusinessException("LMS-29180");

		if(pais !=null) {
			if("BRA".equalsIgnoreCase(pais.getSgPais())){
				if(nrCep.length() < 6) throw new BusinessException("LMS-29173");
		}
			transformIdPaisSgPais(param);
		}
		
		List<Cep> ceps = getCepDAO().findCepLookup(param);
		if(ceps.size() == 1) {
			Cep cep = ceps.get(0);
			Municipio municipio = cep.getMunicipio();
			
			// Caso onlyActives seja null ou true, deve retornar somente cep's ativos.
			if ( param.getOnlyActives() == null 
					|| param.getOnlyActives() ) {
				
				if("I".equals(municipio.getTpSituacao().getValue())
						|| "I".equals(municipio.getUnidadeFederativa().getTpSituacao().getValue())
						|| "I".equals(municipio.getUnidadeFederativa().getPais().getTpSituacao().getValue())
				) {
					throw new BusinessException("LMS-29172");
				}
				
			}
		}
		return ceps;
	}

	/**
	 * Rowcount substituto do m�todo padr�o necess�rio para complemento 
	 * do n�mero do CEP caso o pa�s informado seja BRASIL
	 *
	 * @author Jos� Rodrigo Moraes
	 * @since 23/01/2007
	 *
	 * @param param javaBean de crit�rios de pesquisa (setado na action)
	 * @return 
	 */
	public Integer getRowCountEspecific(PesquisarCepParam param) {
		return getCepDAO().getRowCountEspecific(param);
	}

	/**
	 * FindPaginated substituto do m�todo padr�o necess�rio para complemento
	 * do n�mero do CEP caso o pa�s informado seja BRASIL
	 *
	 * @author Jos� Rodrigo Moraes
	 * @since 23/01/2007
	 *
	 * @param param javaBean de crit�rios de pesquisa (setado na action)
	 * @return
	 */
	public ResultSetPage findPaginatedEspecific(PesquisarCepParam param,FindDefinition findDef) {			
		return getCepDAO().findPaginatedEspecific(param,findDef);		
	}

	/**
	 * Transforma o campo nrCep do objeto PesquisarCepParam colocando '0'
	 * na esquerda se o cep � do Brasil
	 * 
	 * @author Micka�l Jalbert
	 * @since 07/03/2007
	 * @param param
	 * @return
	 */
	private PesquisarCepParam transformIdPaisSgPais(PesquisarCepParam param){
		if (StringUtils.isBlank(param.getSgPais()) && param.getIdPais() != null){
			param.setSgPais(paisService.findById(param.getIdPais()).getSgPais());
		}

		//Tem sigla e tem numero de CEP
		if (StringUtils.isNotBlank(param.getSgPais()) && StringUtils.isNotBlank(param.getNrCep())){
			//Se � Brasil, colocar zeros na esquerda
			if (param.getSgPais().equals(ConstantesAmbiente.SG_PAIS_BRASIL)){
				int qtCharacteres = 8;

				//Se tem % diminuir de um o tamanho para fazer os c�lculos
				if (param.getNrCep().endsWith("%")){
					qtCharacteres = 9;
				}

				param.setNrCep(FormatUtils.completaDados(param.getNrCep(),"0",qtCharacteres,0,FormatUtils.ESQUERDA));
			}
		}
		return param;
	}
	
}
