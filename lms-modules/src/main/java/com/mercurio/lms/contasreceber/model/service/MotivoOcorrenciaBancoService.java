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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.motivoOcorrenciaBancoService"
 */
public class MotivoOcorrenciaBancoService extends CrudService<MotivoOcorrenciaBanco, Long> {


	
	/**
     * M�todo que busca as MotivoOcorrenciasBanco de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage
     */
	public ResultSetPage findPaginatedByMotivoOcorrenciaRemessaRetornoBancos(TypedFlatMap criteria){
		return getMotivoOcorrenciaBancoDAO().findPaginatedByMotivoOcorrenciaRemessaRetornoBancos(criteria);
	}
	
	 /**
     * M�todo que retorna o n�mero de registros de acordo com os filtros passados
     * @param criteria
     * @return
     */
	public Integer getRowCountByMotivoOcorrenciaRemessaRetornoBancos(TypedFlatMap criteria){
		return getMotivoOcorrenciaBancoDAO().getRowCountByMotivoOcorrenciaRemessaRetornoBancos(criteria);
	}
	
	/**
	 * Recupera uma inst�ncia de <code>MotivoOcorrenciaBanco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public MotivoOcorrenciaBanco findById(java.lang.Long id) {
        return (MotivoOcorrenciaBanco)super.findById(id);
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
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
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
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setMotivoOcorrenciaBancoDAO(MotivoOcorrenciaBancoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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