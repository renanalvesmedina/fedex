package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialServico;
import com.mercurio.lms.municipios.model.dao.FilialServicoDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.municipios.filialServicoService"
 */
public class FilialServicoService extends CrudService<FilialServico, Long> {
	
	private FilialService filialService;
	
	private VigenciaService vigenciaService;
    
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	/* 1 - verifica se a data inicial é >= data do sistema
	 * 2 - Verifica se a filial está vigente em FilialService
	 * 3 - Uma filial não pode possuir mais de uma vez o mesmo serviço e estar vigente.
	       Caso ocorra essa situação a seguinte mensagem deve ser exibida LMS-00003*/
	protected FilialServico beforeStore(FilialServico bean) {
		FilialServico filialServico = (FilialServico) bean;
		
		filialService.verificaExistenciaHistoricoFilial(filialServico.getFilial().getIdFilial(),filialServico.getDtVigenciaInicial(),filialServico.getDtVigenciaFinal());
		
		if (getFilialServicoDAO().verificaFilialServicoVigente(filialServico)) {
			throw new BusinessException("LMS-00003");
		}
		return super.beforeStore(bean);
	}

	/**
	 * Recupera uma instância de <code>FilialServico</code> a partir do ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws //
	 *             
	 */
	public TypedFlatMap findByIdMap(Long id) {
		FilialServico filialServico = (FilialServico) super.findById(id);
		TypedFlatMap mapFilialServico = new TypedFlatMap();
		
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(filialServico);
		
		mapFilialServico.put("acaoVigenciaAtual", acaoVigencia);
		final Filial filial = filialServico.getFilial();
		mapFilialServico.put("filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		mapFilialServico.put("filial.sgFilial", filial.getSgFilial());
		mapFilialServico.put("filial.idFilial", filial.getIdFilial());
		mapFilialServico.put("servico.idServico", filialServico.getServico().getIdServico());
		mapFilialServico.put("dtVigenciaInicial", filialServico.getDtVigenciaInicial());
		mapFilialServico.put("dtVigenciaFinal", filialServico.getDtVigenciaFinal());
		mapFilialServico.put("idFilialServico", filialServico.getIdFilialServico());
		
		
		return mapFilialServico;
	}

	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * 
	 * @param ids
	 *            lista com as entidades que deverão ser removida.
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	//nao permite que sejam removidos os registros com vigencia vigente e nem os nao vigentes 
	protected void beforeRemoveByIds(List ids) {
		for (int i = 0; i < ids.size(); i++) {
			FilialServico filialServico = (FilialServico)findById((Long)ids.get(i));
			JTVigenciaUtils.validaVigenciaRemocao(filialServico);
		}
		super.beforeRemoveByIds(ids);
	}
    
//	nao permite que sejam removidos os registros com vigencia vigente e nem os nao vigentes 
	protected void beforeRemoveById(Long id) {
		FilialServico filialServico = new FilialServico();
		ReflectionUtils.copyNestedBean(filialServico,findById((Long)id));
		JTVigenciaUtils.validaVigenciaRemocao(filialServico);		
		super.beforeRemoveById(id);
	}
	
	public Map findFilialComHistoricosFuturos(Long idFilial) {
		return getFilialService().findFilialComHistoricosFuturos(idFilial);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(FilialServico bean) {
		return super.store(bean);
	}

    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Map storeMap(Map bean) {
    	FilialServico filialServico = new FilialServico();

        ReflectionUtils.copyNestedBean(filialServico,bean);

        vigenciaService.validaVigenciaBeforeStore(filialServico);

        super.store(filialServico);
        Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(filialServico);
        bean.put("acaoVigenciaAtual", acaoVigencia);
        bean.put("idFilialServico", filialServico.getIdFilialServico());
        return bean;
    }	
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setFilialServicoDAO(FilialServicoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private FilialServicoDAO getFilialServicoDAO() {
		return (FilialServicoDAO) getDao();
	}

		

}
