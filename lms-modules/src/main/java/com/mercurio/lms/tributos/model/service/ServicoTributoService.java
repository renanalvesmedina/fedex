package com.mercurio.lms.tributos.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.ServicoTributo;
import com.mercurio.lms.tributos.model.dao.ServicoTributoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tributos.servicoTributoService"
 */
public class ServicoTributoService extends CrudService<ServicoTributo, Long> {

    protected ServicoTributo beforeStore(ServicoTributo bean) {
    	
		if ( this.verificaExisteVigencia((ServicoTributo)bean)) {
			throw new BusinessException("LMS-00047");
		}
		return super.beforeStore(bean);
	}

    private boolean verificaExisteVigencia(ServicoTributo bean){
    	return this.getServicoTributoDAO().verificaExisteVigencia(bean);
    }

  
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rs = super.findPaginated(criteria);
		List result = new ArrayList(rs.getList().size());
		for (Iterator iter = rs.getList().iterator(); iter.hasNext();) {
			ServicoTributo st = (ServicoTributo) iter.next();
			Map map = new HashMap(4);
			map.put("idServicoTributo", st.getIdServicoTributo());
			map.put("dsServicoTributo", st.getDsServicoTributo());
			map.put("dtVigenciaInicial", st.getDtVigenciaInicial());
			map.put("dtVigenciaFinal", st.getDtVigenciaFinal());
			result.add(map);
		}
		rs.setList(result);
		return rs;
	}

	/**
	 * Recupera uma instância de <code>ServicoTributo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public ServicoTributo findById(java.lang.Long id) {
        return (ServicoTributo)super.findById(id);
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
    public java.io.Serializable store(ServicoTributo bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setServicoTributoDAO(ServicoTributoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ServicoTributoDAO getServicoTributoDAO() {
        return (ServicoTributoDAO) getDao();
    }
    
    /**
     * Busca os Serviços Tributos Ativos, ou seja, com a vigência ativa
     * @param criterios Critérios de pesquisa
     * @return Lista contendo os Serviços Tributos retornados da pesquisa
     */
    public List findServicosTributosAtivos(TypedFlatMap criterios) {
        return this.getServicoTributoDAO().findServicosTributosAtivos(criterios);
    }
    
    /**
     * Busca os Serviços Tributos Ativos e Futuros, ou seja, vigente ou vão ficar vigente no futuro
     * @param criterios Critérios de pesquisa
     * @return Lista contendo os Serviços Tributos retornados da pesquisa
     */
    public List findServicosTributosAtivosFuturos(TypedFlatMap tfm) {
        return this.getServicoTributoDAO().findServicosTributosAtivosFuturos(tfm);
    }   
    
    /**
     * Busca o id_servico_oficial_tributo relacionado com o servico tributo
     * vigente conforme a data Base
     * @param idServicoTributo Identificador do Serviço Tributo
     * @param dtBase Data Base para teste de vigência
     * @return Identificador do Serviço Oficial Tributo relacionado ao Serviço Tributo
     */
    public Long findIdentificadorServicoOficialTributo(Long idServicoTributo, YearMonthDay dtBase) {
        return this.getServicoTributoDAO().findIdentificadorServicoOficialTributo(idServicoTributo, dtBase);
    }
    
    
    
    /**
     * Retorna a lista de servico tributo com o tp_situacao para ser usado nas combos
     * 
     * @author Diego Umpierre
     * @since 07/07/2006
     * 
     * @param Map criteria
     * @return List
     * */
 	public List findCombo(Map criteria) {
     	List rs = getServicoTributoDAO().findCombo();
     	List list = new ArrayList();
     	
     	if (rs.size() > 0) {
     		for (Iterator iter = rs.iterator(); iter.hasNext();) {
 				Object[] element = (Object[]) iter.next();
 				
     			TypedFlatMap mapResult = new TypedFlatMap();
     			mapResult.put("idServicoTributo",element[0]);
     			mapResult.put("dsServicoTributo",element[1]);
 			
 				YearMonthDay vigenciaInicial = (YearMonthDay) element[2];
 				YearMonthDay vigenciaFinal = (YearMonthDay) element[3];
 				YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
 				

 					boolean vigente = true;
 					
 					if (vigenciaInicial != null && vigenciaFinal == null) {
 						vigente = JTDateTimeUtils.comparaData(dataAtual, vigenciaInicial) >= 0;
 					} else if (vigenciaInicial == null && vigenciaFinal != null) {
 						vigente = JTDateTimeUtils.comparaData(vigenciaFinal, dataAtual) >= 0;
 					} else if (vigenciaInicial != null && vigenciaFinal != null)  {
 						vigente = JTDateTimeUtils.comparaData(dataAtual, vigenciaInicial) >= 0
 								&& JTDateTimeUtils.comparaData(vigenciaFinal, dataAtual) >= 0;						
 					}

 					if (vigente) {
 						mapResult.put("tpSituacao", new DomainValue("A"));
 					} else {
 						mapResult.put("tpSituacao", new DomainValue("I"));
 					}

 					list.add(mapResult);
 				}
     			
     			
     		}
     	
     	return list;

 		
 		
 		
 		
 		
 	}
    
    
    
   }