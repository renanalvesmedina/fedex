package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.pojo.Sistema;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.adsm.framework.security.Resource;
import com.mercurio.adsm.framework.security.model.service.AuthorizationService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.MotivoProibidoEmbarque;
import com.mercurio.lms.vendas.model.ProibidoEmbarque;
import com.mercurio.lms.vendas.model.dao.ProibidoEmbarqueDAO;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.proibidoEmbarqueService"
 */
public class ProibidoEmbarqueService extends CrudService<ProibidoEmbarque, Long> {
	
	private ParametroGeralService parametroGeralService;
	private PerfilUsuarioService perfilUsuarioService; 
	private UsuarioService usuarioService;
	private AuthorizationService authorizationService;
	private MotivoProibidoEmbarqueService motivoProibidoEmbarqueService;

	/**
	 * M�todo utilizado pela Integra��o
	 * @author Andre Valadas
	 * 
	 * @param idCliente
	 * @param dtBloqueio
	 * @param dsBloqueio
	 * @return <ProibidoEmbarque> 
	 */
	public ProibidoEmbarque findProibidoEmbarque(Long idCliente, YearMonthDay dtBloqueio, String dsBloqueio) {
		return getProibidoEmbarqueDAO().findProibidoEmbarque(idCliente, dtBloqueio, dsBloqueio);
	}

	/**
	 * Recupera uma inst�ncia de <code>ProibidoEmbarque</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ProibidoEmbarque findById(java.lang.Long id) {
		return (ProibidoEmbarque) super.findById(id);
	}

	public ResultSetPage findPaginated(Map criteria) {
		
		if ( ("".equals(criteria.get("cliente.idCliente"))) || (criteria.get("cliente.idCliente") == null)) {
			throw new IllegalArgumentException("Obrigat�rio informar Cliente!");
		}
		
		ResultSetPage rsp = super.findPaginated(criteria);

		List<String> included = new ArrayList<String>(8);
		included.add("idProibidoEmbarque");
		included.add("dtBloqueio");
		included.add("dtDesbloqueio");
		included.add("dsDesbloqueio");
		included.add("dsBloqueio");
		included.add("motivoProibidoEmbarque.dsMotivoProibidoEmbarque");
		included.add("usuarioByIdUsuarioBloqueio.nmUsuario");
		included.add("usuarioByIdUsuarioDesbloqueio.nmUsuario");

		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));
		return rsp;
	}

	public ResultSetPage findPaginatedByIdCliente(Long idCliente, FindDefinition findDefinition) {
		ResultSetPage rsp = this.getProibidoEmbarqueDAO().findPaginatedByIdCliente(idCliente, findDefinition);
		return rsp;
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {		
		validateDataDesbloqueioInformada(ids);
		validaExclusaoMotivos(ids);
	}
	
	
	/**
	 * Valida a exclusao dos motivos verificando
	 * se o id pertence ao parametrop geral ID_MOTIVO_TRIBUT_PROIB_EMB e o us�ario logado
	 * possuir um pertil tribut�rio da MTZ
	 * 
	 * @param ids
	 */
	public void validaExclusaoMotivos(List ids){
		
		String idsProibido = String.valueOf(parametroGeralService.findConteudoByNomeParametro("ID_MOTIVO_TRIBUT_PROIB_EMB", false));		
		
		List<Long> idsMotivos = new ArrayList<Long>();
		if(ids != null && StringUtils.isNotBlank(idsProibido)){
			Boolean perfilTributarioMTZ  = this.perfilMatrizUsuarioLogado();
			List<String> listIdsProibido = Arrays.asList(idsProibido.split(";"));
			String idProibidoEmbraque = null;
			ProibidoEmbarque pe = null;
			for(Object id : ids){
				pe = getProibidoEmbarqueDAO().findProibidoEmbarque(Long.valueOf(id.toString()));
				if(pe != null && pe.getMotivoProibidoEmbarque() != null){ 
					idProibidoEmbraque = String.valueOf(pe.getMotivoProibidoEmbarque().getIdMotivoProibidoEmbarque());
					if(listIdsProibido.contains(idProibidoEmbraque) && !perfilTributarioMTZ){
						throw new BusinessException("LMS-01179");
					}
					idsMotivos.add(LongUtils.getLong(id));
				}
			}
			super.removeByIds(idsMotivos);
		}		
	}		
	
	
	/**
	 * 
	 * Se usu�rio logado estiver ligado aos perfis tribut�rios da �MTZ�: 
	 * Acessa tabela PERFIL_USUARIO onde ID_USUARIO seja igual ao ID do usu�rio logado e 
	 * verifica se PERFIL_USUARIO.ID_PERFIL pertence ao par�metro geral �ID_PERFIS_TRIBUTOS_MTZ� 
	 * 
	 * @return
	 */
	public Boolean perfilMatrizUsuarioLogado(){
		
		Boolean valido = Boolean.FALSE;
		
		List<PerfilUsuario> listaPerfisUsuario = perfilUsuarioService. findByIdUsuarioPerfilUsuario(SessionUtils
				.getUsuarioLogado().getIdUsuario());
		
		String perfisMatriz = String.valueOf(parametroGeralService.findConteudoByNomeParametro("ID_PERFIS_TRIBUTOS_MTZ", false));
		
		if(listaPerfisUsuario != null && !listaPerfisUsuario.isEmpty() && StringUtils.isNotBlank(perfisMatriz) ){
									
			List<String> listIdsPerfisMatriz = Arrays.asList(perfisMatriz.split(";"));
						
			String idPerfilLogado = null;
			for(PerfilUsuario perfil : listaPerfisUsuario){
				if(perfil.getPerfil() != null && perfil.getPerfil().getIdPerfil() != null){
					idPerfilLogado = String.valueOf(perfil.getPerfil().getIdPerfil());
					if(idPerfilLogado != null && listIdsPerfisMatriz.contains(idPerfilLogado)){
						valido = Boolean.TRUE;
						break;
					}
				}
			}
		}
		return valido;
	}	
	
	/**
	 * O usu�rio logado deve ter permiss�o de acesso � filial respons�vel comercialmente pelo cliente (ID_FILIAL_ATENDE_COMERCIAL)
	 * @author Robson Edemar Gehl
	 * @exception LMS-01007 usu�rio logado n�o tem permiss�o
	 */
	public void validatePermissaoUsuarioAcessoFilial(Long idFilial){
		Boolean error = Boolean.TRUE;
		Boolean filialUsuarioMatriz   = SessionUtils.isFilialSessaoMatriz();
		Boolean acessoFilialComercial = usuarioService.verificaAcessoFilialRegionalUsuarioLogado(idFilial);				
		if(acessoFilialComercial){
			error = Boolean.FALSE;
		}else{
			if(filialUsuarioMatriz){
				error = Boolean.FALSE;
			}
		}
		
		if(error){
			throw new BusinessException("LMS-01007");
		}
	}

	/**
	 * Recebe um Map com idFilial e idUsuario para validar o acesso a Filial Regional
	 *@author Robson Edemar Gehl
	 * @param map {idFilial=?, idUsuario=?}
	 * @exception LMS-01007 usu�rio e filial n�o tem acesso
	 */
	public void validatePermissaoFuncionario(Map<String, Object> map) {
		Boolean filialUsuarioMatriz = SessionUtils.isFilialSessaoMatriz();
		if(!filialUsuarioMatriz){
			if (map.get("idFilial") != null && map.get("idUsuario") != null 
					&& !usuarioService.validateAcessoFilialRegionalUsuario(Long.valueOf((String)map.get("idFilial")),Long.valueOf((String)map.get("idUsuario")))) {
			throw new BusinessException("LMS-01007");
		}
	}
	}

	/**
	 *  A data do bloqueio deve ser maior ou igual � data do dia, ao inserir o ProibidoEmbarque
	 * @author Robson Edemar Gehl
	 * @exception LMS-01008 data bloqueio menor que dia corrente
	 * @param proibidoEmbarque
	 */
	public void validateDataBloqueio(ProibidoEmbarque proibidoEmbarque) {
		//Verifica se o usuario vem da integracao
		if(SessionUtils.isIntegrationRunning()) {
			return;
		}

		YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();
		if (proibidoEmbarque.getDtBloqueioAnterior() != null) {
			if((CompareUtils.ne(proibidoEmbarque.getDtBloqueio(), proibidoEmbarque.getDtBloqueioAnterior())) && 
				(CompareUtils.lt(proibidoEmbarque.getDtBloqueio(), dtToday))) {
				throw new BusinessException("LMS-01008");
			}
		} else if(CompareUtils.lt(proibidoEmbarque.getDtBloqueio(), dtToday)) {
			throw new BusinessException("LMS-01008");
		}
	}

	/**
	 * Valida��o da data de desbloqueio.<BR>
	 * Se informada, o respons�vel do bloqueio deve ser informado tamb�m, vice versa.
	 * @author Robson Edemar Gehl
	 * @param pe ProibidoEmbarque
	 * @exception LMS-01005 data de desbloqueio � menor que data do bloqueio
	 * @exception LMS-01010 data de desbloqueio � menor que data do dia corrente
	 * @exception LMS-01011 data de desbloqueio informada e respons�vel do desbloqueio n�o
	 * @exception LMS-01012 respons�vel do desbloqueio informado e data de desbloqueio n�o
	 */
	public void validateDataDesbloqueio(ProibidoEmbarque pe) {
		if (pe.getDtDesbloqueio() != null && pe.getUsuarioByIdUsuarioDesbloqueio() == null){
			throw new BusinessException("LMS-01011");
		} else if (pe.getDtDesbloqueio() == null && pe.getUsuarioByIdUsuarioDesbloqueio() != null){
			throw new BusinessException("LMS-01012");
		}

		//Verifica se o usuario vem da integracao
		if(SessionUtils.isIntegrationRunning()) {
			return;
		}

		if(pe.getDtDesbloqueio() == null)
			return;

		//Data Desbloqueio n�o pode ser menor que a data do bloqueio
		if(CompareUtils.lt(pe.getDtDesbloqueio(), pe.getDtBloqueio())) {
			throw new BusinessException("LMS-01005");
		}

		//Data do Desbloqueio n�o pode ser menor que o dia corrente
		if(CompareUtils.lt(pe.getDtDesbloqueio(), JTDateTimeUtils.getDataAtual())) {
			throw new BusinessException("LMS-01010");
		}
	}

	/**
	 * N�o deve existir nenhum registro de ProibidoEmbarque sem data de desbloqueio por cliente.
	 * @author Robson Edemar Gehl
	 * @param proibidoEmbarque
	 * @exception LMS-01006 existe ProibidoEmbarque sem data de desbloqueio
	 */
	public void validateProibidoEmbarquePorCliente(ProibidoEmbarque proibidoEmbarque) {
		if(!getProibidoEmbarqueDAO().validateDataDesbloqueioByCliente(proibidoEmbarque)) {
			throw new BusinessException("LMS-01006");
		}
	}

	/**
	 * verifica se existe registro de ProibidoEmbarque sem data de desbloqueio por cliente.
	 * @author Salete
	 * @param proibidoEmbarque
	 */
	public boolean validateDataDesbloqueioByCliente(ProibidoEmbarque proibidoEmbarque) {
		return getProibidoEmbarqueDAO().validateDataDesbloqueioByCliente(proibidoEmbarque); 
		
	}
	/**
	 * Regra para exclus�o.<BR>
	 * N�o permitir a exclus�o caso a data do desbloqueio esteja informada.<BR>
	 * @author Robson Edemar Gehl
	 * @param pe
	 * @exception LMS-01009
	 */
	public void validateDataDesbloqueioInformada(List<Long> ids) {
		if (!getProibidoEmbarqueDAO().validateDataDesbloqueioInformada(ids)) {
			throw new BusinessException("LMS-01009");
		}
	}

	@Override
	protected void beforeRemoveByIds(List<Long> ids) {		
		validateDataDesbloqueioInformada(ids);				
		super.beforeRemoveByIds(ids);
	}

	@Override
	protected void beforeRemoveById(Long id) {
		List<Long> ids = new ArrayList<Long>(1);
		ids.add(id);
		validateDataDesbloqueioInformada(ids);
		super.beforeRemoveById(id);
	}

	@Override
	protected ProibidoEmbarque beforeStore(ProibidoEmbarque bean) {
		validateDataBloqueio(bean);
		validateDataDesbloqueio(bean);
		validateProibidoEmbarquePorCliente(bean);
		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(ProibidoEmbarque bean) {
		return super.store(bean);
	}

	/**
	 * Retorna os registros de ProibidoEmbarque passando o ID do Cliente
	 */	
	public List<ProibidoEmbarque> findProibidoEmbarqueByIdCliente(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(ProibidoEmbarque.class);
		dc.add(Restrictions.eq("cliente.idCliente", idCliente));
		return getProibidoEmbarqueDAO().findByDetachedCriteria(dc);
	}

	public Integer getRowCountProibidoByIdCliente(Long idCliente) {
		return getProibidoEmbarqueDAO().getRowCountProibidoByIdCliente(idCliente);
	}

	public Integer getRowCountProibidoBloqueioTributarioByIdCliente(Long idCliente) {
		return getProibidoEmbarqueDAO().getRowCountProibidoBloqueioTributarioByIdCliente(idCliente);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setProibidoEmbarqueDAO(ProibidoEmbarqueDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ProibidoEmbarqueDAO getProibidoEmbarqueDAO() {
		return (ProibidoEmbarqueDAO) getDao();
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setPerfilUsuarioService(PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
	}
	
	public void setAuthorizationService(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}
	
	public void setMotivoProibidoEmbarqueService(MotivoProibidoEmbarqueService motivoProibidoEmbarqueService) {
		this.motivoProibidoEmbarqueService = motivoProibidoEmbarqueService;
	}	

	public boolean validaPermissaoRecurso(Long idMotivoProibidoEmbarque, Resource resource) {
		MotivoProibidoEmbarque motivoProibidoEmbarque = motivoProibidoEmbarqueService.findById(idMotivoProibidoEmbarque);
		
		if (motivoProibidoEmbarque.getBlFinanceiro()) {
			Sistema sistema = new Sistema();
			sistema.setNmSistema(resource.getSystemName());
			
			Map permissions = (Map) authorizationService.findAllPermissions(sistema, SessionUtils.getUsuarioLogado());
			String permission = (String) permissions.get(resource.toString());
			
			if (permission != null)
				return Byte.parseByte(permission) != 0;
			return false;
		}
		
		return true;
	}
}