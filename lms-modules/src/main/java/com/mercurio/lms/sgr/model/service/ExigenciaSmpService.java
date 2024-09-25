package com.mercurio.lms.sgr.model.service;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.sgr.model.ExigenciaSmp;
import com.mercurio.lms.sgr.model.dao.ExigenciaSmpDAO;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sgr.exigenciaSmpService"
 */
public class ExigenciaSmpService extends CrudService<ExigenciaSmp, Long> {

	private static final String TP_MANUT_REGISTRO_MANUAL ="M";
	private static final String TP_MANUT_REGISTRO_PGR ="P";
	private static final String TP_MANUT_REGISTRO_PGR_ALTERADO ="A";

	private static final String SGR_PERFIL_USUARIO_CEMOP = "SGR_PERFIL_USUARIO_CEMOP";

	private GerarEnviarSMPService gerarEnviarSMPService;
	private ConfiguracoesFacade configuracoesFacade;
	private PerfilUsuarioService perfilUsuarioService;

	private GerarEnviarSMPService getGerarEnviarSMPService() {
		return gerarEnviarSMPService;
	}

	public void setGerarEnviarSMPService(GerarEnviarSMPService gerarEnviarSMPService) {
		this.gerarEnviarSMPService = gerarEnviarSMPService;
	}

	/**
	 * Recupera uma inst�ncia de <code>ExigenciaSmp</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public ExigenciaSmp findById(java.lang.Long id) {
        return (ExigenciaSmp)super.findById(id);
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
    public java.io.Serializable store(ExigenciaSmp bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setExigenciaSmpDAO(ExigenciaSmpDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ExigenciaSmpDAO getExigenciaSmpDAO() {
        return (ExigenciaSmpDAO) getDao();
    }

	/**
	 * M�todo para buscar as ExigenciasSmp pelo id SMP (solicMonitPreventivo)
	 * 
	 * @param idSmp
	 * @param fd
	 * @return
	 */
	public ResultSetPage findPaginatedExigenciaSmpByIdSmp(Long idSmp, FindDefinition fd) {
		return this.getExigenciaSmpDAO().findPaginatedExigenciaSmpByIdSmp(idSmp, fd, isPerfilUsuarioCemop());
	}

	/**
	 * Row count para findPaginatedExigenciaSmpByIdSmp
	 * 
	 * @param idSmp
	 * @return
	 */
	public Integer getRowCountExigenciaSmpByIdSmp(Long idSmp) {
		return this.getExigenciaSmpDAO().getRowCountExigenciaSmpByIdSmp(idSmp, isPerfilUsuarioCemop());
	}

	/**
	 * LMS-6853 - Verifica se {@link Usuario} logado tem {@link Perfil} CEMOP
	 * conforme definido no {@link ParametroGeral} "SGR_PERFIL_USUARIO_CEMOP".
	 * 
	 * @return
	 */
	private Boolean isPerfilUsuarioCemop() {
		final String perfil = (String) configuracoesFacade.getValorParametro(SGR_PERFIL_USUARIO_CEMOP);
		Usuario usuario = SessionUtils.getUsuarioLogado();
		@SuppressWarnings("unchecked")
		List<PerfilUsuario> perfis = perfilUsuarioService.findByIdUsuarioPerfilUsuario(usuario.getIdUsuario());
		return CollectionUtils.exists(perfis, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return perfil.equals(((PerfilUsuario) object).getPerfil().getDsPerfil());
			}
		});
	}

	/**
	 * Remove a Exig�ncia de SMP pelo id da SMP
	 * @param idSolicMonitPreventivo
	 */
	public void removeByIdSolicMonitPreventivo(Long idSolicMonitPreventivo) {
		getExigenciaSmpDAO().removeByIdSolicMonitPreventivo(idSolicMonitPreventivo);
	}

	/**
	 * Rotina responsavel pelas regras do item Salvar providencias da ET Manter SMP
	 * @author Rodrigo Antunes 
	 * @param bean
	 * @return
	 */
	public java.io.Serializable storeExigenciaSmpToManterSmp(ExigenciaSmp bean) {
		beforeStoreSmp(bean);
		Serializable sb = super.store(bean); 
		// Flush foi inserido para for�ar o salvar. Sem ele n�o estava enviando os dados corretos por email.
		getExigenciaSmpDAO().getAdsmHibernateTemplate().flush();
		Long idSmp = bean.getSolicMonitPreventivo().getIdSolicMonitPreventivo();
		this.generateGerarEnviarSMP(idSmp);
		return sb;
	}

	
	
	private void beforeStoreSmp(ExigenciaSmp bean){
		verificarTipoRegistro( bean);
		
	}
	
	
	/**
	 * se tipo origem for diferente de manual
	 * verificar se a qtde pode ser alterada, gerando mensagem de alteracao
	 * somente pode alterar se qtd original for menor que a qtde a ser alterada 
	 * 
	 * @param bean
	 */
	private void verificarTipoRegistro(ExigenciaSmp bean) {
		Integer qtdeOriginal = bean.getQtExigidaOriginal() != null ? bean.getQtExigidaOriginal() : 0;
		Integer qtde = bean.getQtExigida() != null ? bean.getQtExigida() : 0;
		
		if(bean.getIdExigenciaSmp()!=null){
			ExigenciaSmp smp = findById(bean.getIdExigenciaSmp());
			bean.setTpManutRegistro(smp.getTpManutRegistro());
			bean.setDsHistoricoAlteracao(smp.getDsHistoricoAlteracao());
			qtdeOriginal = smp.getQtExigidaOriginal() != null ? smp.getQtExigidaOriginal():0;
			bean.setQtExigidaOriginal(qtdeOriginal);
			bean.setFilialInicioOriginal(smp.getFilialInicioOriginal());
			bean.setVlKmFranquiaOriginal(smp.getVlKmFranquiaOriginal());
			qtde = smp.getQtExigida();
			getExigenciaSmpDAO().getAdsmHibernateTemplate().evict(smp);
		}
		
		verificaTipoRegistro(bean, qtdeOriginal, qtde);
	}

	private void verificaTipoRegistro(ExigenciaSmp bean, Integer qtdeOriginal, Integer qtde) {
		if (bean.getTpManutRegistro() == null) {
			bean.setTpManutRegistro(new DomainValue(TP_MANUT_REGISTRO_MANUAL));
		} else if (TP_MANUT_REGISTRO_PGR.equals(bean.getTpManutRegistro().getValue())
				|| TP_MANUT_REGISTRO_PGR_ALTERADO.equals(bean.getTpManutRegistro().getValue())) {
			Integer qtdeAlterada = bean.getQtExigida();

			if (qtdeAlterada < qtdeOriginal) {
				throw new BusinessException("LMS-11350");
			} else if (!(qtde.equals(qtdeAlterada))) {
				gerarHistoricoAlteracao(bean, qtde, qtdeAlterada);
				bean.setTpManutRegistro(new DomainValue(TP_MANUT_REGISTRO_PGR_ALTERADO));
			}
		} else {
			bean.setTpManutRegistro(new DomainValue(TP_MANUT_REGISTRO_MANUAL));
		}
	}

	private void gerarHistoricoAlteracao(ExigenciaSmp bean, Integer qtdeOriginal, Integer qtde) {
		String stringHistorico = this.configuracoesFacade.getMensagem("LMS-11351", new Object[] { qtdeOriginal,qtde });
		VarcharI18n newHistorico = null;
		if (bean.getDsHistoricoAlteracao() != null) {
			String oldHistorico = bean.getDsHistoricoAlteracao().getValue();
			String newStringHistorico = new StringBuffer(oldHistorico).append("\n").append(stringHistorico)
					.toString();
			newHistorico = new VarcharI18n(newStringHistorico);
		} else {
			newHistorico = new VarcharI18n(stringHistorico);
		}
		bean.setDsHistoricoAlteracao(newHistorico);
	}
	
	/**
	 * @param idSmp
	 * @param idControleCarga
	 */
	private void generateGerarEnviarSMP(Long idSmp) {
		getGerarEnviarSMPService().generateAlterarSMP(idSmp);
	}

    public void removeByIdsToManterSmp(List<Long> ids, Long idSmp) {
    	verificarRemoverSMPSomenteOrigemManual(ids);
    	this.removeByIds(ids);
    	getExigenciaSmpDAO().getAdsmHibernateTemplate().flush();
    	this.generateGerarEnviarSMP(idSmp);
    }

	private void verificarRemoverSMPSomenteOrigemManual(List<Long> ids) {
		for (Long id : ids) {
			ExigenciaSmp smp = this.findById(id);
			if (!TP_MANUT_REGISTRO_MANUAL.equals(smp.getTpManutRegistro().getValue())) {
				throw new BusinessException("LMS-11349");
			}
		}
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setPerfilUsuarioService(PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
	}

}
