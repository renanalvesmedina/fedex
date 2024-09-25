package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.MotivoOcorrenciaBanco;
import com.mercurio.lms.contasreceber.model.dao.MotivoOcorrenciaBancoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.motivoOcorrenciaBancoService"
 */
public class MotivoOcorrenciaBancoService extends CrudService<MotivoOcorrenciaBanco, Long> {


	
	/**
     * Método que busca as MotivoOcorrenciasBanco de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage
     */
	public ResultSetPage findPaginatedByMotivoOcorrenciaRemessaRetornoBancos(TypedFlatMap criteria){
		return getMotivoOcorrenciaBancoDAO().findPaginatedByMotivoOcorrenciaRemessaRetornoBancos(criteria);
	}
	
	 /**
     * Método que retorna o número de registros de acordo com os filtros passados
     * @param criteria
     * @return
     */
	public Integer getRowCountByMotivoOcorrenciaRemessaRetornoBancos(TypedFlatMap criteria){
		return getMotivoOcorrenciaBancoDAO().getRowCountByMotivoOcorrenciaRemessaRetornoBancos(criteria);
	}
	
	/**
	 * Recupera uma instância de <code>MotivoOcorrenciaBanco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public MotivoOcorrenciaBanco findById(java.lang.Long id) {
        return (MotivoOcorrenciaBanco)super.findById(id);
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
    public java.io.Serializable store(MotivoOcorrenciaBanco bean) {
        return super.store(bean);
    }

    /**
     * Carregra um motivoOcorrenciaBanco de acordo com o nrMotivoOcorrenciaBanco e o idOcorrencia.
     *
     * @author Hector Julian Esnaola Junior
     * @since 11/07/2007
     *
     * @param nrMotivo
     * @param idOcorrencia
     * @return
     *
     */
    public MotivoOcorrenciaBanco findMotivoByNrMotivoAndOcorrencia(Short nrMotivo, Long idOcorrencia){
    	return getMotivoOcorrenciaBancoDAO().findMotivoByNrMotivoAndOcorrencia(nrMotivo, idOcorrencia);
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMotivoOcorrenciaBancoDAO(MotivoOcorrenciaBancoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MotivoOcorrenciaBancoDAO getMotivoOcorrenciaBancoDAO() {
        return (MotivoOcorrenciaBancoDAO) getDao();
    }
    
    public List<MotivoOcorrenciaBanco> findMotivoOcorrenciaForRetornoBanco(Short nrOcorrenciaBanco, Long idCedente, List<Short> motivosOcorrenciaBancos) {
    	List<MotivoOcorrenciaBanco> retorno = new ArrayList<MotivoOcorrenciaBanco>();
    	if(CollectionUtils.isNotEmpty(motivosOcorrenciaBancos)){
    		for (Short motivo : motivosOcorrenciaBancos) {
    			List<MotivoOcorrenciaBanco> motivoOcorrenciaBanco = getMotivoOcorrenciaBancoDAO().findMotivoOcorrenciaForRetornoBanco(nrOcorrenciaBanco, idCedente, motivo);
    			if(CollectionUtils.isNotEmpty(motivoOcorrenciaBanco)){
    				retorno.addAll(motivoOcorrenciaBanco);
    			}
			}
    	}
		return retorno;
	}

	public List<MotivoOcorrenciaBanco> findMotivoOcorrenciaForRetornoBanco(Short nrOcorrenciaBanco, Long idCedente, Short motivoOcorrenciaBancos) {
		return getMotivoOcorrenciaBancoDAO().findMotivoOcorrenciaForRetornoBanco(nrOcorrenciaBanco, idCedente, motivoOcorrenciaBancos);
	}
		
   }