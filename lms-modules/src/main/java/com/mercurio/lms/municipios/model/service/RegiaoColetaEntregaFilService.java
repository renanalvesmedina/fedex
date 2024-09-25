package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil;
import com.mercurio.lms.municipios.model.dao.RegiaoColetaEntregaFilDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.regiaoColetaEntregaFilService"
 */
public class RegiaoColetaEntregaFilService extends CrudService<RegiaoColetaEntregaFil, Long> {
	private FilialService filialService;
	private RegiaoFilialRotaColEntService regiaoFilialRotaColEntService;
	private VigenciaService vigenciaService;
	
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public RegiaoFilialRotaColEntService getRegiaoFilialRotaColEntService() {
		return regiaoFilialRotaColEntService;
	}

	public void setRegiaoFilialRotaColEntService(
			RegiaoFilialRotaColEntService regiaoFilialRotaColEntService) {
		this.regiaoFilialRotaColEntService = regiaoFilialRotaColEntService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	// 1 -verifica se a data inicio e data fim vigencia >= dtAtual
	// 2- Verificar se a filial est� vigente. Chamar M�todo � filialService.VerificaVigenciasEmHistoricoFilial()
	// 3- Caso a data de final de vig�ncia for preenchida: Verificar se existe alguma rota de coleta entrega (REIAO_FILIAL_ROTA_COL_ENT) associada a regi�o e vigente na data de fim de vig�ncia informada. Se existir algum registro mostrar a mensagem LMS-29031 e abortar a opera��o.
	protected RegiaoColetaEntregaFil beforeStore(RegiaoColetaEntregaFil bean) {
		RegiaoColetaEntregaFil regiaoColetaEntregaFil = (RegiaoColetaEntregaFil)bean;
			
		filialService.verificaExistenciaHistoricoFilial(regiaoColetaEntregaFil.getFilial().getIdFilial(),regiaoColetaEntregaFil.getDtVigenciaInicial(), regiaoColetaEntregaFil.getDtVigenciaFinal());
		
		//verifica se existe uma mesma regiaoColetaEntregaFilial vigente
		if(getRegiaoColetaEntregaFilDAO().findRegiaoColetaEntregaVigente(regiaoColetaEntregaFil)){
			throw new BusinessException("LMS-00003");
		}
		
		if(regiaoColetaEntregaFil.getIdRegiaoColetaEntregaFil()!= null){
			regiaoFilialRotaColEntService.verificaVigenciaRegiaoFilialRota(regiaoColetaEntregaFil.getIdRegiaoColetaEntregaFil(),regiaoColetaEntregaFil.getDtVigenciaInicial(),regiaoColetaEntregaFil.getDtVigenciaFinal());
		}
		return super.beforeStore(bean);
	}	

	protected void beforeRemoveById(Long id) {
		RegiaoColetaEntregaFil regiaoColetaEntregaFil = (RegiaoColetaEntregaFil)findById(id);
		if(regiaoColetaEntregaFil != null){
			JTVigenciaUtils.validaVigenciaRemocao(regiaoColetaEntregaFil);
		}
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		for(int i=0; i< ids.size(); i++){
			RegiaoColetaEntregaFil regiaoColetaEntregaFil = (RegiaoColetaEntregaFil)findById((Long)ids.get(i));
			if (regiaoColetaEntregaFil != null){
				JTVigenciaUtils.validaVigenciaRemocao(regiaoColetaEntregaFil);
			}
		}
		super.beforeRemoveByIds(ids);
	}
	
	public TypedFlatMap findByIdEValidaVigencias(Long id){
		RegiaoColetaEntregaFil regiaoColetaEntregaFil = (RegiaoColetaEntregaFil)findById(id);
		
		TypedFlatMap mapRegiaoColetaEntregaFil = new TypedFlatMap();
		
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(regiaoColetaEntregaFil);
		mapRegiaoColetaEntregaFil.put("acaoVigenciaAtual",acaoVigencia);
		
		final Filial filial = regiaoColetaEntregaFil.getFilial();
		mapRegiaoColetaEntregaFil.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
		mapRegiaoColetaEntregaFil.put("filial.idFilial",filial.getIdFilial());
		mapRegiaoColetaEntregaFil.put("filial.sgFilial",filial.getSgFilial());
		mapRegiaoColetaEntregaFil.put("dsRegiaoColetaEntregaFil",regiaoColetaEntregaFil.getDsRegiaoColetaEntregaFil());
		mapRegiaoColetaEntregaFil.put("dtVigenciaInicial",regiaoColetaEntregaFil.getDtVigenciaInicial());
		mapRegiaoColetaEntregaFil.put("dtVigenciaFinal",regiaoColetaEntregaFil.getDtVigenciaFinal());
		mapRegiaoColetaEntregaFil.put("idRegiaoColetaEntregaFil", regiaoColetaEntregaFil.getIdRegiaoColetaEntregaFil());
		
		return mapRegiaoColetaEntregaFil;
		
	}

	/**
	 * Recupera uma inst�ncia de <code>RegiaoColetaEntregaFil</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public RegiaoColetaEntregaFil findById(java.lang.Long id) {
        return (RegiaoColetaEntregaFil)super.findById(id);
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
    public java.io.Serializable store(RegiaoColetaEntregaFil bean) {
        return super.store(bean);
    }
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Map storeMap(Map bean) {
    	RegiaoColetaEntregaFil regiaoColetaEntregaFil = new RegiaoColetaEntregaFil();

        ReflectionUtils.copyNestedBean(regiaoColetaEntregaFil,bean);

        vigenciaService.validaVigenciaBeforeStore(regiaoColetaEntregaFil);

        super.store(regiaoColetaEntregaFil);
        Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(regiaoColetaEntregaFil);
        bean.put("acaoVigenciaAtual", acaoVigencia);
        bean.put("idRegiaoColetaEntregaFil",regiaoColetaEntregaFil.getIdRegiaoColetaEntregaFil());
        
        return bean;
    }

	public List findListRegiaoVigente(TypedFlatMap criteria, boolean onlyVigentes) {
		return getRegiaoColetaEntregaFilDAO().consultaRegiaoColetaEntregaFilialVigentes(criteria,onlyVigentes);
		
	}
	
	public boolean findVigenciaRegiaoColetaEntrega(Long idRegiaoColetaEntregaFil){
		return getRegiaoColetaEntregaFilDAO().findVigenciaRegiaoColetaEntrega(idRegiaoColetaEntregaFil);
	}
	
	public boolean findIntervaloRegiaoColetaEntrega(Long idRegiaoColetaEntregaFil, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return getRegiaoColetaEntregaFilDAO().findIntervaloRegiaoColetaEntrega(idRegiaoColetaEntregaFil, dtVigenciaInicial, dtVigenciaFinal);
	}
	
	
    
	public List findListRegiaoVigente(TypedFlatMap criteria) {
		return getRegiaoColetaEntregaFilDAO().consultaRegiaoColetaEntregaFilialVigentes(criteria,true);
	}
	
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setRegiaoColetaEntregaFilDAO(RegiaoColetaEntregaFilDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private RegiaoColetaEntregaFilDAO getRegiaoColetaEntregaFilDAO() {
        return (RegiaoColetaEntregaFilDAO) getDao();
    }

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rs = getRegiaoColetaEntregaFilDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List lista = AliasToNestedMapResultTransformer.getInstance().transformListResult(rs.getList());
		rs.setList(lista);
		return rs;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getRegiaoColetaEntregaFilDAO().getRowCount(criteria);
	}

	
   }