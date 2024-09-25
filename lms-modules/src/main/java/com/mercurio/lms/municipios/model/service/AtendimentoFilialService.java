package com.mercurio.lms.municipios.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.AtendimentoFilial;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.dao.AtendimentoFilialDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.atendimentoFilialService"
 */
public class AtendimentoFilialService extends CrudService<AtendimentoFilial, Long> {

	private FilialService filialService;
	private VigenciaService vigenciaService;

	/**
	 * Recupera uma instância de <code>AtendimentoFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public AtendimentoFilial findById(java.lang.Long id) {
        return (AtendimentoFilial)super.findById(id);
    }
    
	/**
	 * Recupera uma instância de <code>AtendimentoFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public Map findByIdDetalhamento(java.lang.Long id) {
    	AtendimentoFilial atendimentoFilial = (AtendimentoFilial)super.findById(id);
    	Filial filial = atendimentoFilial.getFilial();
    	
    	TypedFlatMap retorno = new TypedFlatMap();
    	retorno.put("idAtendimentoFilial",atendimentoFilial.getIdAtendimentoFilial());
    	
    	retorno.put("filial.idFilial",filial.getIdFilial());
    	retorno.put("filial.sgFilial",filial.getSgFilial());
    	retorno.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
    	
    	retorno.put("tpAtendimento",atendimentoFilial.getTpAtendimento().getValue());
    	retorno.put("blDomingo",atendimentoFilial.getBlDomingo());
    	retorno.put("blSegunda",atendimentoFilial.getBlSegunda());
    	retorno.put("blTerca",atendimentoFilial.getBlTerca());
        retorno.put("blQuarta",atendimentoFilial.getBlQuarta());
        retorno.put("blQuinta",atendimentoFilial.getBlQuinta());
        retorno.put("blSexta",atendimentoFilial.getBlSexta());
        retorno.put("blSabado",atendimentoFilial.getBlSabado());
    	retorno.put("hrAtendimentoInicial",atendimentoFilial.getHrAtendimentoInicial());
    	retorno.put("hrAtendimentoFinal",atendimentoFilial.getHrAtendimentoFinal());
    	retorno.put("obAtendimento",atendimentoFilial.getObAtendimento());
    	retorno.put("dtVigenciaInicial",atendimentoFilial.getDtVigenciaInicial());
    	retorno.put("dtVigenciaFinal",atendimentoFilial.getDtVigenciaFinal());
    	
    	Integer integerAcaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(atendimentoFilial);
    	retorno.put("acaoVigenciaAtual",integerAcaoVigencia);
        
    	return retorno;
    }

    public ResultSetPage findPaginated(Map criteria) {
    	ResultSetPage rsp = super.findPaginated(criteria);
    	
    	FilterResultSetPage frsp = new FilterResultSetPage(rsp) {

			public Map filterItem(Object item) {
				AtendimentoFilial atendimentoFilial = (AtendimentoFilial)item;
				Filial filial = atendimentoFilial.getFilial();
				
				TypedFlatMap row = new TypedFlatMap();
				row.put("idAtendimentoFilial",atendimentoFilial.getIdAtendimentoFilial());
				
				row.put("filial.sgFilial",filial.getSgFilial());	
		        
		        row.put("tpAtendimento",atendimentoFilial.getTpAtendimento());
		        row.put("blDomingo",atendimentoFilial.getBlDomingo());
		        row.put("blSegunda",atendimentoFilial.getBlSegunda());
		        row.put("blTerca",atendimentoFilial.getBlTerca());
		        row.put("blQuarta",atendimentoFilial.getBlQuarta());
		        row.put("blQuinta",atendimentoFilial.getBlQuinta());
		        row.put("blSexta",atendimentoFilial.getBlSexta());
		        row.put("blSabado",atendimentoFilial.getBlSabado());
		        row.put("hrAtendimentoInicial",atendimentoFilial.getHrAtendimentoInicial());
		        row.put("hrAtendimentoFinal",atendimentoFilial.getHrAtendimentoFinal());
		        row.put("dtVigenciaInicial",atendimentoFilial.getDtVigenciaInicial());
		        row.put("dtVigenciaFinal",atendimentoFilial.getDtVigenciaFinal());
				
		        return row;
			}
    		
    	};
    	
    	return (ResultSetPage)frsp.doFilter();
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
	// FIXME corrigir para retornar o ID
    public TypedFlatMap store(AtendimentoFilial bean) {
    	super.store(bean);
    	
    	TypedFlatMap retorno = new TypedFlatMap();
    	retorno.put("idAtendimentoFilial",bean.getIdAtendimentoFilial());
    	retorno.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean));
    	
    	return retorno;
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setAtendimentoFilialDAO(AtendimentoFilialDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private AtendimentoFilialDAO getAtendimentoFilialDAO() {
        return (AtendimentoFilialDAO) getDao();
    }
    
    
    protected AtendimentoFilial beforeStore(AtendimentoFilial bean) {
    	AtendimentoFilial beanReturn = super.beforeStore(bean);
    	AtendimentoFilial atendimentoFilial = (AtendimentoFilial)bean;
    	
    	vigenciaService.validaVigenciaBeforeStore(atendimentoFilial);
    	
    	if (!verificaPeloMenosUmDiaChecado(atendimentoFilial))
    		throw new BusinessException("LMS-29041");
    	
    	getFilialService().verificaExistenciaHistoricoFilial(atendimentoFilial.getFilial().getIdFilial(),
    														  atendimentoFilial.getDtVigenciaInicial(),
    														  atendimentoFilial.getDtVigenciaFinal());
    	
    	if (getAtendimentoFilialDAO().verificaAtendimentoFilialVigentes((AtendimentoFilial)bean)) {
			throw new BusinessException("LMS-29012");
		}
    	
    	return beanReturn;
    }
    
    /**
     * Valida a remoção de um registro de acordo com o padrão de comportamento de vigências.
     * @param id Id do registro a ser validado.
     */
	private void validaRemoveById(Long id) {
		AtendimentoFilial atendimentoFilial = findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(atendimentoFilial);
	}
	
	protected void beforeRemoveById(Long id) {
		validaRemoveById((Long)id);
		super.beforeRemoveById(id);
	}
	
	protected void beforeRemoveByIds(List ids) {
		for (int i = 0; i < ids.size() ; i++ )
	        validaRemoveById((Long)ids.get(i));
		super.beforeRemoveByIds(ids);
	}
	
	/**
	 * Verifica se pelo menos um dia da semana foi checado.
	 * @param bean Um objeto.
	 * @return True se pelo menos um dia da semana 
	 * foi checado ou false em caso contrario.
	 */
	public boolean verificaPeloMenosUmDiaChecado(AtendimentoFilial atendimentoFilial) {
		Map frequencia = new HashMap(7);
		
		frequencia.put("blDomingo",atendimentoFilial.getBlDomingo());
		frequencia.put("blSegunda",atendimentoFilial.getBlSegunda());
		frequencia.put("blTerca",atendimentoFilial.getBlTerca());
		frequencia.put("blQuarta",atendimentoFilial.getBlQuarta());
		frequencia.put("blQuinta",atendimentoFilial.getBlQuinta());
		frequencia.put("blSexta",atendimentoFilial.getBlSexta());
		frequencia.put("blSabado",atendimentoFilial.getBlSabado());
		
        return JTVigenciaUtils.verificaPeloMenosUmDiaChecado(frequencia);
	}

	
	
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

}