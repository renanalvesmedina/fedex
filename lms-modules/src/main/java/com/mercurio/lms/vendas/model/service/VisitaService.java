package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.EtapaVisita;
import com.mercurio.lms.vendas.model.FuncionarioVisita;
import com.mercurio.lms.vendas.model.ServicoOferecido;
import com.mercurio.lms.vendas.model.Visita;
import com.mercurio.lms.vendas.model.dao.VisitaDAO;


/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.visitaService"
 */
public class VisitaService extends CrudService<Visita, Long> {
	private ServicoOferecidoService servicoOferecidoService;
	private EtapaVisitaService etapaVisitaService;
	private FuncionarioVisitaService funcionarioVisitaService;

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getVisitaDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));		
		return rsp;
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		Integer rowCountCustom = this.getVisitaDAO().getRowCount(criteria);
		return rowCountCustom;
	}

	/**
	 * Recupera uma instância de <code>Visita</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public Visita findById(java.lang.Long id) {
		return (Visita)super.findById(id);
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
	public java.io.Serializable store(Visita bean) {
		boolean isInsertMode = bean.getIdVisita() == null;

		//Verifica se a data da visita não é superior à data atual
		if (isInsertMode){
			if (bean.getDtVisita().isAfter(new YearMonthDay())){
				throw new BusinessException("LMS-01058");
			}
		}
		
		Serializable id = super.store(bean);
		getVisitaDAO().getAdsmHibernateTemplate().flush();

		/*
		 * Todos os FuncionarioVisita pertencentes a esta visita
		 * são excluídos da base de dados e inseridos novamente
		 */
		funcionarioVisitaService.removeByIdVisita(bean.getIdVisita());		

		/* grava os aconpanhantes da visita. */
		List<FuncionarioVisita> funcionariosVisita = bean.getFuncionarioVisitas();
		if (funcionariosVisita != null) {
			for(FuncionarioVisita funcionarioVisita : funcionariosVisita) {
				funcionarioVisitaService.store(funcionarioVisita);
			}
		}

		List<EtapaVisita> etapasVisita = bean.getEtapaVisitas();
		if (etapasVisita != null) {
			for(EtapaVisita etapaVisita : etapasVisita) {
				etapaVisita.setVisita(bean);

				//remove os servicos oferecidos se nao for uma etapa nova
				if(etapaVisita.getIdEtapaVisita() != null) {
					servicoOferecidoService.removeByIdEtapaVisita(etapaVisita.getIdEtapaVisita());
				}

				//salva a etapa da visita
				etapaVisitaService.store(etapaVisita);

				//insere os servicos oferecidos
				List<ServicoOferecido> servicosOferecidos = etapaVisita.getServicoOferecidos(); 
				if(servicosOferecidos != null) {
					for(ServicoOferecido servicoOferecido : servicosOferecidos) {
						servicoOferecidoService.store(servicoOferecido);
					}	
				}
			}
		}

		/* remove do banco os itens(Etapas de visita) removidos */
		List<EtapaVisita> etapasRemove = bean.getEtapasRemovidas();
		if(etapasRemove != null){
			for(EtapaVisita etapaVisita : etapasRemove) {
				servicoOferecidoService.removeByIdEtapaVisita(etapaVisita.getIdEtapaVisita());
				etapaVisitaService.removeById(etapaVisita.getIdEtapaVisita());
			}
		}

		return id;
	}

	private final VisitaDAO getVisitaDAO() {
		return (VisitaDAO) getDao();
	}
	public void setVisitaDAO(VisitaDAO dao) {
		setDao( dao );
	}
	public void setFuncionarioVisitaService(FuncionarioVisitaService funcionarioVisitaService) {
		this.funcionarioVisitaService = funcionarioVisitaService;
	}
	public void setEtapaVisitaService(EtapaVisitaService etapaVisitaService) {
		this.etapaVisitaService = etapaVisitaService;
	}
	public void setServicoOferecidoService(ServicoOferecidoService servicoOferecidoService) {
		this.servicoOferecidoService = servicoOferecidoService;
	}
}