package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.SeguroCliente;
import com.mercurio.lms.vendas.model.dao.SeguroClienteDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.seguroClienteService"
 */
@Assynchronous(name = "SeguroClienteService" )
public class SeguroClienteService extends CrudService<SeguroCliente, Long> {

	private UsuarioService usuarioService;

	/**
	 * Verifica permissão de usuário logado
	 * @author Robson Edemar Gehl
	 * @return
	 */
	public Boolean validatePermissaoUsuarioLogado(Long id){
		if (id == null) {
			id = SessionUtils.getFilialSessao().getIdFilial();
		}
		
		return usuarioService.verificaAcessoFilialRegionalUsuarioLogado(id);
	}
	
	/**
	 * Recupera uma instância de <code>SeguroCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public SeguroCliente findById(java.lang.Long id) {
		return (SeguroCliente)super.findById(id);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage result = super.findPaginated(criteria);
		
		List resultList = result.getList();
		
		if (resultList != null && !resultList.isEmpty()) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String identClienteFormatado = "";
			String tpIdentificacao = "";
			
			for (Iterator it = resultList.iterator(); it.hasNext(); ) {
				resultMap = (Map<String, Object>) it.next();
				
				identClienteFormatado = (String) resultMap.get("identCliente");
				tpIdentificacao = ((DomainValue)resultMap.get("tpIdentificacao")).getValue();
				identClienteFormatado = FormatUtils.formatIdentificacao(tpIdentificacao, identClienteFormatado);
				
				resultMap.put("identClienteFormatado", identClienteFormatado);

				// LMS-7285 - incluir propriedade "seguradora.pessoa.nmPessoa"
				Map<String, Object> seguradoraMap = new HashMap<String, Object>();
				Map<String, Object> pessoaMap = new HashMap<String, Object>();
				resultMap.put("seguradora", seguradoraMap);
				seguradoraMap.put("pessoa", pessoaMap);
				pessoaMap.put("nmPessoa", resultMap.get("infSeguradora"));
			}
		}
		
		return result;
    }
	
	/**
	 * Recupera informação do responsável comercial
	 * 
	 * Jira LMS-6148
	 * 
	 * @param id
	 * @return
	 */
	public List findPromotorClientes(java.lang.Long id) {
		return getSeguroClienteDAO().findPromotorClientes(id);
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
	public java.io.Serializable store(SeguroCliente bean) {
		//LMS-6148
		if (bean.getBlEmEmissao()) {
			if (StringUtils.isNotEmpty(bean.getDsApolice())) {
				throw new BusinessException("LMS-22026");
			}
		}
		else {
			if (StringUtils.isEmpty(bean.getDsApolice())) {
				throw new BusinessException("LMS-22025");
			}
		}
		
		if (bean.getUsuarioAviso() == null || bean.getUsuarioAviso().getIdUsuario() == null) {
			bean.setUsuarioAviso(null);
		}
		
		return super.store(bean);
	}
	
	/**
	 * Rotina chamada para execução de tarefas agendadas para seguro cliente.
	 */
	@AssynchronousMethod(name="seguroCliente.ReajusteDiarioSeguroCliente",
			type=BatchType.BATCH_SERVICE)
	public void executeReajusteSeguro() {
		List<SeguroCliente> list = getSeguroClienteDAO().findAllEntities();
		for (SeguroCliente seCliente : list) {
			//FIXME: Ajustar método deprecated de YearMonthDay.
			if ((JTDateTimeUtils.comparaData(seCliente.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual().plusDays(30)) > 0) && (Boolean.TRUE.equals(seCliente.getBlFimVigenciaInformado()))) {
				
				seCliente.setBlFimVigenciaInformado(Boolean.FALSE);
				getSeguroClienteDAO().store(seCliente, true);
				
			} else if((JTDateTimeUtils.comparaData(seCliente.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual().plusDays(30)) < 0) && (!Boolean.TRUE.equals(seCliente.getBlFimVigenciaInformado()))){
				seCliente.setBlFimVigenciaInformado(Boolean.TRUE);
				getSeguroClienteDAO().store(seCliente, true);
			} else if((JTDateTimeUtils.comparaData(seCliente.getDtVigenciaFinal(), JTDateTimeUtils.getDataAtual()) < 0) && (Boolean.TRUE.equals(seCliente.getBlFimVigenciaInformado()))){
				seCliente.setBlFimVigenciaInformado(Boolean.FALSE);
				getSeguroClienteDAO().store(seCliente, true);
			}
		}
	}
	
	/**
	 * Retorna valores para geração obrigatória das informações 
	 * de Seguros no XML do CT-e.
	 * 
	 * Jira LMS-3996
	 * 
	 * @return List
	 */
	public List findSegValues(Long idServico, Long idCliente) {
		return getSeguroClienteDAO().findSegValues(idServico, idCliente);
	}

	/**
	 * Retorna valores para geração obrigatória das informações 
	 * de Seguros no XML do CT-e.
	 * 
	 * Jira LMS-3996
	 * 
	 * @return List
	 */
	public List findSegValues(Long idServico, Long idCliente, DateTime dhEmissao) {
		return getSeguroClienteDAO().findSegValues(idServico, idCliente, dhEmissao);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * @param dao
	 */
	public void setSeguroClienteDAO(SeguroClienteDAO dao) {
		setDao( dao );
	}
	private SeguroClienteDAO getSeguroClienteDAO() {
		return (SeguroClienteDAO) getDao();
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public List findByClienteTpModalTpAbrangencia(Long idCliente, String tpModal, String tpAbrangencia) {
		return getSeguroClienteDAO().findByClienteTpModalTpAbrangencia(idCliente, tpModal, tpAbrangencia);
	}

	public List findVigentesByClienteTpModalTpAbrangencia(Long idCliente, String tpModal, String tpAbrangencia) {
		return getSeguroClienteDAO().findVigentesByClienteTpModalTpAbrangencia(idCliente, tpModal, tpAbrangencia);
	}

	/**
	 * LMS-7285 - Atualiza valor limite para controle de carga de determinado
	 * {@link SeguroCliente}.
	 * 
	 * @param idSeguroCliente
	 *            id da {@link SeguroCliente}
	 * @param vlLimiteControleCarga
	 *            valor limite para controle
	 */
	public void storeVlLimiteControleCarga(Long idSeguroCliente, BigDecimal vlLimiteControleCarga) {
		getSeguroClienteDAO().storeVlLimiteControleCarga(idSeguroCliente, vlLimiteControleCarga);
	}

}
