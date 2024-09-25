package com.mercurio.lms.entrega.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.Turno;
import com.mercurio.lms.entrega.model.dao.TurnoDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.turnoService"
 */
public class TurnoService extends CrudService<Turno, Long> {

	private VigenciaService vigenciaService;
	/**
	 * Recupera uma instância de <code>Turno</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public Turno findById(java.lang.Long id) {
        return (Turno)super.findById(id);
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
    public java.io.Serializable store(Turno bean) {
    	return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTurnoDAO(TurnoDAO dao) {
        setDao( dao );
    }
    
    public ResultSetPage findPaginated(Map criteria) {
    	FilterResultSetPage rsp = new FilterResultSetPage(super.findPaginated(criteria)){
	    	public Map filterItem(Object item) {
				Turno bean = (Turno)item;
				TypedFlatMap flat = new TypedFlatMap();
								
				flat.put("dsTurno",bean.getDsTurno());
				flat.put("hrTurnoInicial",bean.getHrTurnoInicial());
				flat.put("hrTurnoFinal",bean.getHrTurnoFinal());
				flat.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
				flat.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
				flat.put("idTurno",bean.getIdTurno());
				
				return flat;
			}
		};
		return (ResultSetPage)rsp.doFilter();
    	
    	
    }
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TurnoDAO getTurnoDAO() {
        return (TurnoDAO) getDao();
    }
    public void beforeRemoveById(Long id) {
    	validaRemoveById((Long) id);
		super.beforeRemoveById(id);
	}
    
    public void beforeRemoveByIds(List ids) {
		for (Iterator i = ids.iterator() ; i.hasNext() ;)
	        validaRemoveById((Long)i.next());
		super.beforeRemoveByIds(ids);
	}
    public void validaRemoveById(Long id) {
		Turno turno   = findById(id);
        JTVigenciaUtils.validaVigenciaRemocao(turno);
	}
	public Turno beforeStore(Turno bean) {
    	Turno turno = (Turno)bean;
    	if(getTurnoDAO().findIntervalosTurnosVigentes(turno))
    		throw new BusinessException("LMS-00003");
    	vigenciaService.validaVigenciaBeforeStore(turno);
    	
    	return super.beforeStore(bean);
    }
	public List validateVigenciaCustom(TypedFlatMap mapa){
		return getTurnoDAO().validateVigenciaCustom(mapa);
		
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	/**
	 * Retorna uma Lista de Turnos Vigentes, onde DataAtual esteja entre DT_VIGENCIA_INICIAL and DT_VIGENCIA_FINAL
	 * @param idFilial
	 * @return
	 */
	public List findTurnosVigentes(Long idFilial) {
		return getTurnoDAO().findTurnosVigentes(idFilial); 
	}
	
	/**
	 * @see getTurnoDAO().findTurnoByIdFilialDsTurno(idFilial, dsTurno, data)
	 * @param findTurnoByIdFilialDsTurno
	 * @return
	 */
	public Turno findTurnoByIdFilialDsTurno(String idFilial, String dsTurno, YearMonthDay data){
		return getTurnoDAO().findTurnoByIdFilialDsTurno(idFilial, dsTurno, data);
	}
	
   }