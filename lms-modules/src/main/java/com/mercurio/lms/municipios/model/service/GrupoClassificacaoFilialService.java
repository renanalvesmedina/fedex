package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.DivisaoGrupoClassificacao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.GrupoClassificacaoFilial;
import com.mercurio.lms.municipios.model.dao.GrupoClassificacaoFilialDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.grupoClassificacaoFilialService"
 */
public class GrupoClassificacaoFilialService extends CrudService<GrupoClassificacaoFilial, Long> {
	
	private FilialService filialService;
	private VigenciaService vigenciaService;
	
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public ResultSetPage findPaginated(Map criteria) {
        
        List included = new ArrayList();
        included.add("idGrupoClassificacaoFilial");
        included.add("divisaoGrupoClassificacao.grupoClassificacao.dsGrupoClassificacao");
        included.add("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao");
        included.add("divisaoGrupoClassificacao.grupoClassificacao.tpSituacao");
        included.add("divisaoGrupoClassificacao.dsDivisaoGrupoClassificacao");
        included.add("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao");
        included.add("divisaoGrupoClassificacao.tpSituacao");
        included.add("filial.pessoa.nmPessoa");
        included.add("filial.sgFilial");
        included.add("filial.siglaNomeFilial");
        included.add("filial.pessoa.nmFantasia");
        included.add("filial.idFilial");
        included.add("dtVigenciaInicial");
        included.add("dtVigenciaFinal");
         
        ResultSetPage rsp = super.findPaginated(criteria);
        rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));
 
        return rsp;
	}

	protected GrupoClassificacaoFilial beforeStore(GrupoClassificacaoFilial bean) {
		GrupoClassificacaoFilial grupoClassificacaoFilial= (GrupoClassificacaoFilial)bean;
		Long idFilial = grupoClassificacaoFilial.getFilial().getIdFilial();
		YearMonthDay dtVigenciaInicial = grupoClassificacaoFilial.getDtVigenciaInicial();
				
		//EXCECAO 29039 verifica se os intervalos do grupo de classificacao de filial está no intervalo do historico da filial
		filialService.verificaExistenciaHistoricoFilial(idFilial,dtVigenciaInicial,grupoClassificacaoFilial.getDtVigenciaFinal());
		
		//uma filial nao pode estar vigente em duas divisoes de um mesmo grupo
		if(getGrupoClassificacaoFilialDAO().verificaFilialGrupoClassificacao(grupoClassificacaoFilial)){
			throw new BusinessException("LMS-29030");
		}
		
		
		return super.beforeStore(bean);
	}
	/**
	 * Recupera uma instância de <code>GrupoClassificacaoFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public GrupoClassificacaoFilial findById(java.lang.Long id) {
        return (GrupoClassificacaoFilial)super.findById(id);
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
    public java.io.Serializable store(GrupoClassificacaoFilial bean) {
        return super.store(bean);
    }
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Map storeMap(Map bean) {
    	GrupoClassificacaoFilial grupoClassificacaoFilial = new GrupoClassificacaoFilial();

        ReflectionUtils.copyNestedBean(grupoClassificacaoFilial,bean);
        
        vigenciaService.validaVigenciaBeforeStore(grupoClassificacaoFilial);

        super.store(grupoClassificacaoFilial);
        Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(grupoClassificacaoFilial);
        bean.put("acaoVigenciaAtual", acaoVigencia);
        bean.put("idGrupoClassificacaoFilial", grupoClassificacaoFilial.getIdGrupoClassificacaoFilial());
        
        return bean;
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setGrupoClassificacaoFilialDAO(GrupoClassificacaoFilialDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private GrupoClassificacaoFilialDAO getGrupoClassificacaoFilialDAO() {
        return (GrupoClassificacaoFilialDAO) getDao();
    }

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
//metodo chamado ao detalhar um registro, verifica se as vigencias estao vigentes e passa um flag para o javascript
	public TypedFlatMap findByIdEValidaDtVigencia(java.lang.Long id) {
		GrupoClassificacaoFilial grupoClassificacaoFilial = (GrupoClassificacaoFilial)findById(id);
		TypedFlatMap mapGrupoClassificacaoFilial = new TypedFlatMap();
		
		Integer acaoVigencia =  JTVigenciaUtils.getIntegerAcaoVigencia(grupoClassificacaoFilial);
		
		mapGrupoClassificacaoFilial.put("acaoVigenciaAtual", acaoVigencia);
		
		final DivisaoGrupoClassificacao divisaoGrupoClassificacao = grupoClassificacaoFilial.getDivisaoGrupoClassificacao();
		mapGrupoClassificacaoFilial.put("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao", divisaoGrupoClassificacao.getGrupoClassificacao().getIdGrupoClassificacao());
		mapGrupoClassificacaoFilial.put("divisaoGrupoClassificacao.grupoClassificacao.tpSituacao.description", divisaoGrupoClassificacao.getGrupoClassificacao().getTpSituacao().getDescription().getValue());
		mapGrupoClassificacaoFilial.put("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao",divisaoGrupoClassificacao.getIdDivisaoGrupoClassificacao());
		mapGrupoClassificacaoFilial.put("divisaoGrupoClassificacao.tpSituacao.description", divisaoGrupoClassificacao.getTpSituacao().getDescription().getValue());
		
		final Filial filial = grupoClassificacaoFilial.getFilial();
		mapGrupoClassificacaoFilial.put("filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		mapGrupoClassificacaoFilial.put("filial.sgFilial", filial.getSgFilial());
		mapGrupoClassificacaoFilial.put("filial.idFilial", filial.getIdFilial());
		mapGrupoClassificacaoFilial.put("dtVigenciaInicial", grupoClassificacaoFilial.getDtVigenciaInicial());
		mapGrupoClassificacaoFilial.put("dtVigenciaFinal", grupoClassificacaoFilial.getDtVigenciaFinal());
		mapGrupoClassificacaoFilial.put("idGrupoClassificacaoFilial", grupoClassificacaoFilial.getIdGrupoClassificacaoFilial());
		
		return mapGrupoClassificacaoFilial;
	}

	protected void beforeRemoveById(Long id) {
		GrupoClassificacaoFilial grupoClassificacaoFilial = (GrupoClassificacaoFilial)findById(id);
		if(grupoClassificacaoFilial.getDtVigenciaInicial()!= null){
			JTVigenciaUtils.validaVigenciaRemocao(grupoClassificacaoFilial);
		}
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		if (!ids.isEmpty()) {
			for (int i = 0; i < ids.size(); i++) {
				GrupoClassificacaoFilial grupoClassificacaoFilial = (GrupoClassificacaoFilial)findById((Long)ids.get(0));
				if(grupoClassificacaoFilial.getDtVigenciaInicial() != null){
					JTVigenciaUtils.validaVigenciaRemocao(grupoClassificacaoFilial);
				}
			}
		}
		super.beforeRemoveByIds(ids);
	}
	
	public Long findIdDivisaoByFilialGrupoClassificacao(Long idFilial, Long idGrupoClassificacao) {
		return getGrupoClassificacaoFilialDAO().findIdDivisaoByFilialGrupoClassificacao(idFilial, idGrupoClassificacao);
	}
	
   }