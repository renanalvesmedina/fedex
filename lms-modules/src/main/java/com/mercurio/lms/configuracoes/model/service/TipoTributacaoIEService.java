package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.dao.TipoTributacaoIEDAO;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.configuracoes.tipoTributacaoIEService"
 */
public class TipoTributacaoIEService extends
		CrudService<TipoTributacaoIE, Long> {
	private EspecializacaoPessoaService especializacaoPessoaService;
	private PessoaService pessoaService;
	private DomainValueService domainValueService;
	private ParametroGeralService parametroGeralService;

	/**
	 * Recupera uma instância de <code>Object</code> a partir do ID.
	 *
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public TipoTributacaoIE findById(java.lang.Long id) {
		return (TipoTributacaoIE)super.findById(id);
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
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		TipoTributacaoIE ttie;
		Long idTipoTributacao;
		for (Iterator<Long> iter = ids.iterator(); iter.hasNext();) {
			idTipoTributacao = iter.next();
			ttie = findById(idTipoTributacao);
			validateBlAtualizacaoCountasse(ttie.getInscricaoEstadual()
					.getPessoa().getIdPessoa());
			removeById(idTipoTributacao);
		}
	}

	/**
	 * Método invocado antes de realizar um update
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/09/2006
	 *
	 * @param bean
	 * @return
	 *
	 */
	protected TipoTributacaoIE beforeUpdate(TipoTributacaoIE bean) {
		TipoTributacaoIE ttie = (TipoTributacaoIE)bean;

		// Caso seja a última tipoTributacaoIE vigente, não permite que a mesma
		// seja editada para não vigente
		if (isLastTipoTributacaoIE(ttie.getInscricaoEstadual()
				.getIdInscricaoEstadual(), ttie.getIdTipoTributacaoIE())
				&& (ttie.getDtVigenciaFinal() != null
						&& ttie.getDtVigenciaFinal().isBefore(
								JTDateTimeUtils.getDataAtual()) || ttie
						.getDtVigenciaInicial().isAfter(
								JTDateTimeUtils.getDataAtual()))) {
			throw new BusinessException("LMS-27078");
		}

		return super.beforeUpdate(bean);
	}

	/**
	 * Método invocado antes do store 
	 */
	public TipoTributacaoIE beforeStore(TipoTributacaoIE bean) {
		TipoTributacaoIE ttie = (TipoTributacaoIE)bean;

		/**
		 * Valida a regra 3.1
		 */
/*
		 * if(isInscricaoEstadualIsento(ttie) &&
		 * !ttie.getTpSituacaoTributaria().getValue().equals("OP") &&
		 * !ttie.getTpSituacaoTributaria().getValue().equals("NC") ) { throw new
		 * BusinessException("LMS-27062"); }
*/		
		/*
		 * else if(!isInscricaoEstadualIsento(ttie) &&
		 * (ttie.getTpSituacaoTributaria().getValue().equals("OP") ||
		 * ttie.getTpSituacaoTributaria().getValue().equals("NC"))){ throw new
		 * BusinessException("LMS-27062"); }
		 */

		/**
		 * Valida a regra 3.1
		 * 
		 * Busca registros na base com intervalos de vigência iguais
		 */
		List<TipoTributacaoIE> lst = getTipoTributacaoIEDAO()
				.findTipoTributacaoIEByVigenciaEquals(
						ttie.getDtVigenciaInicial(), ttie.getDtVigenciaFinal(),
				ttie.getInscricaoEstadual().getIdInscricaoEstadual(),
						ttie.getIdTipoTributacaoIE());

		/**
		 * Verifica se não já não existe nenhum registro na base com o mesmo
		 * intervalo de vigência
		 */
		if(!lst.isEmpty()){
			throw new BusinessException("LMS-00047");
		}

		//Valida se o usuario logado pode alterar a pessoa
		pessoaService.validateAlteracaoPessoa(pessoaService
				.findIdPessoaByIdInscricaoEstadual(ttie.getInscricaoEstadual()
						.getIdInscricaoEstadual()));

		return super.beforeStore(bean);
	}

	private boolean isInscricaoEstadualIsento(TipoTributacaoIE tipoTributacaoIE) {
		return tipoTributacaoIE.getInscricaoEstadual().getNrInscricaoEstadual()
				.equalsIgnoreCase("isento");
	}

	/**
	 * 
	 * @param bean
	 * @return
	 */
	public java.io.Serializable store(TipoTributacaoIE bean) {
		return store(bean, Boolean.TRUE);
	}

	/**
	 * Metodo chamado pelo edi, para atualizacao automatica da iscricao estadual
	 * do cliente. Salva diretamente o registro sem validar se a filial do
	 * usuario eh a mesma do cliente destinatario.
	 *  
	 * @param bean
	 * @param skipValidations
	 * @return
	 */
	public java.io.Serializable storeSkipValidations(TipoTributacaoIE bean){
		getDao().store(bean);
		return getDao().getIdentifier(bean);
	}
	
	/**
	 * 
	 * @param bean
	 * @param confirmado
	 *            False caso não precise confirmação, True caso o usuário clicou
	 *            ok na tela
	 * @return
	 */
	public java.io.Serializable store(TipoTributacaoIE bean, boolean confirmado) {
		
		String tpSituacaoTributaria = bean.getTpSituacaoTributaria().getValue();

		validateBlAtualizacaoCountasse(bean.getInscricaoEstadual().getPessoa()
				.getIdPessoa());

		Boolean isIsento = isInscricaoEstadualIsento(bean);
		
		validateParametroGeralTpSituacaoTributariaAceitaIsento(
				tpSituacaoTributaria, isIsento);
		
		if(!confirmado)			
			validateParametroGeralTpSituacaoTributariaAlerta(
					tpSituacaoTributaria, isIsento);
		
		return super.store(bean);
	}

	public Boolean validateParametroGeralTpSituacaoTributariaAlerta(
			String tpSituacaoTributaria, Boolean isIsento) {
		Object parametro = parametroGeralService.findConteudoByNomeParametro(
				"TP_SIT_TRIBUT_ALERTA", false);
		if(!isIsento && parametro.toString().contains(tpSituacaoTributaria)){
			throw new BusinessException("LMS-27095",
					new String[] { domainValueService
							.findDomainValueDescription(
									"DM_SIT_TRIBUTARIA_CLIENTE",
									tpSituacaoTributaria) });
		}
		return Boolean.TRUE;
	}

	public Boolean validateParametroGeralTpSituacaoTributariaAceitaIsento(
			String tpSituacaoTributaria, Boolean isIsento) {
		if(isIsento){
			Object parametro = parametroGeralService
					.findConteudoByNomeParametro("TP_SIT_TRIBUT_ACEITA_ISENTO",
							false);
			if(parametro == null){
				throw new BusinessException("LMS-27051");
			}else{
				if(!parametro.toString().contains(tpSituacaoTributaria)){					
					throw new BusinessException("LMS-27062");
				}
			}
		}
		return Boolean.TRUE;
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 31/07/2007
	 *
	 * @param ie
	 * @throws BusinessException
	 */
	public void validateBlAtualizacaoCountasse(Long id) {
		Pessoa p = pessoaService.findById(id);
		// Caso o blAtualizacaoCountasse seja true e a filial do usuario logado
		// não seja MTZ, lança a exception.
		if (p.getBlAtualizacaoCountasse()
				&& !SessionUtils.isFilialSessaoMatriz()) {
			throw new BusinessException("LMS-27096");
		}
		this.getTipoTributacaoIEDAO().getAdsmHibernateTemplate().evict(p);
	}

	/**
	 * @author José Rodrigo Moraes
	 * @since 02/06/2006
	 * 
	 * Busca os tipos Tributação IE vigentes na data passada
	 * 
	 * @param idInscricaoEstadual
	 *            Identificador da Inscrição Estadual associada ao tipo de
	 *            Tributação
	 * @param data
	 *            Data para teste de vigência
	 * @param vigenciaFutura
	 *            Booleano que indica se o teste deve ser feito para vigências
	 *            futuras <br>
	 * <br>
	 * <code>true</code> Para vigência futura e<br> 
	 * <code>false</code> Para vigência na data atual 
	 * @return Lista de Tipos Tributação IE
	 */
	public List<TipoTributacaoIE> findTiposTributacaoIEVigente(
			Long idInscricaoEstadual, YearMonthDay data, Boolean vigenciaFutura) {
		Validate.notNull(idInscricaoEstadual,
				"idInscricaoEstadual cannot be null.");
		return getTipoTributacaoIEDAO().findTiposTributacaoIEVigente(
				idInscricaoEstadual, data, vigenciaFutura);
	}

	/**
	 * 
	 * @see findTiposTributacaoIEVigente(Long idInscricaoEstadual, YearMonthDay
	 *      data, Boolean vigenciaFutura)
	 */
	public TipoTributacaoIE findTiposTributacaoIEVigente(
			Long idInscricaoEstadual, YearMonthDay data) {
		List<TipoTributacaoIE> lstTipoTrib = getTipoTributacaoIEDAO()
				.findTiposTributacaoIEVigente(idInscricaoEstadual, data, false);
		if (lstTipoTrib.size() == 1) {
			return lstTipoTrib.get(0);
		} else {
			return null;
		}
	}

	public List findVigentesByIdPessoa(Long idPessoa) {
		return getTipoTributacaoIEDAO().findVigentesByIdPessoa(idPessoa);
	}

	public Boolean validateTipotributacaoIENaoContribuinte(Long idPessoa) {
		List<String> listTpSituacaoTributaria = new ArrayList<String>();
		listTpSituacaoTributaria
				.add(ConstantesConfiguracoes.TP_SITUACAO_TRIBUTARIA_NAO_CONTRIBUINTE);
		listTpSituacaoTributaria
				.add(ConstantesConfiguracoes.TP_SITUACAO_TRIBUTARIA_ORGAO_PUBLICO_NAO_CONTRIBUINTE);
		listTpSituacaoTributaria
				.add(ConstantesConfiguracoes.TP_SITUACAO_TRIBUTARIA_PRODUTOR_RURAL_NAO_CONTRIBUINTE);
		listTpSituacaoTributaria
				.add(ConstantesConfiguracoes.TP_SITUACAO_TRIBUTARIA_ME_EPP_SIMPLES_NAO_CONTRIBUINTE);
		listTpSituacaoTributaria
				.add(ConstantesConfiguracoes.TP_SITUACAO_TRIBUTARIA_CIA_MISTA_NAO_CONTRIBUINTE);
		
		return getTipoTributacaoIEDAO()
				.findByIdPessoaAndDtVigenciaAndTpSituacaoTributaria(idPessoa,
						JTDateTimeUtils.getDataAtual(),
						listTpSituacaoTributaria) != null;
	}
	
	@SuppressWarnings("unchecked")
	public void atualizarDataFimVigencia(Long idInscricaoEstadual){
		List<TipoTributacaoIE> list = getTipoTributacaoIEDAO()
				.findByDtVigenciaFinalNula(idInscricaoEstadual);
		for (int i = 0; i < list.size(); i++) {
			TipoTributacaoIE ttie = list.get(i);
			ttie.setDtVigenciaFinal(new YearMonthDay());
			getTipoTributacaoIEDAO().store(ttie);
		}
	}

	protected void beforeRemoveById(Long id) {
		TipoTributacaoIE tipoTributacaoIE = (TipoTributacaoIE)findById(id);

		validateBlAtualizacaoCountasse(tipoTributacaoIE.getInscricaoEstadual()
				.getPessoa().getIdPessoa());

		// Caso seja a última inscricaoEstadual vigente , não permite excluir
		if (isLastTipoTributacaoIE(tipoTributacaoIE.getInscricaoEstadual()
				.getIdInscricaoEstadual(),
				tipoTributacaoIE.getIdTipoTributacaoIE())) {
			throw new BusinessException("LMS-27078");
		}

		//Valida se o usuario logado pode alterar a pessoa
		pessoaService.validateAlteracaoPessoa(pessoaService
				.findIdPessoaByIdInscricaoEstadual(tipoTributacaoIE
						.getInscricaoEstadual().getIdInscricaoEstadual()));

		super.beforeRemoveById(id);
	}

	/**
	 * Se o tipo de tributação pretence a um dos tipo de pessoa informado,
	 * lançar uma exception.
	 * 
	 * @author Mickaël Jalbert
	 * @since 24/08/2006
	 *
	 * @param Long
	 *            idPessoa
	 */
	public void validateTipoTributacaoPessoa(Long idPessoa) {
		List<Short> lstCdEspecializacao = new ArrayList<Short>();

		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_EMPRESA);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_FILIAL);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_FILIAL_CIA_AEREA);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_CON_POSTO_PASSAGEM);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_OPERADORA_MCT);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_CLIENTE);

		Short cdEspecializacao = especializacaoPessoaService.isEspecializado(
				idPessoa, lstCdEspecializacao);
		if (cdEspecializacao != null){
			throw new BusinessException("LMS-27071",
					new Object[] { especializacaoPessoaService
							.getLabel(cdEspecializacao) });
		}
	}	

	/**
	 * Informa se é o último tipo de tributação da pessoa.
	 * 
	 * @author Mickaël Jalbert
	 * @since 24/08/2006
	 * 
	 * @param Long
	 *            idPessoa
	 * @return boolean
	 */
	public boolean isLastTipoTributacaoIE(Long idInscricaoEstadual,
			Long idTipoTributacaoIE) {
		List<TipoTributacaoIE> lstTipoTributacao = getTipoTributacaoIEDAO()
				.findTiposTributacaoIEVigente(idInscricaoEstadual,
						idTipoTributacaoIE, JTDateTimeUtils.getDataAtual());

		// Caso lstTipoTributacao.size() == 0, significa que o tipoTributacaoIE
		// em questão, é o último vigente
		if (lstTipoTributacao.size() == 0){
			return true;
		}
		return false;
	}

	/***
	 * Retorna os tipos de tributações que devem ser bloqueadas para frete tipo
	 * FOB
	 * 
	 * @author Gabriel.Scossi
	 * @since 15/02/2016
	 * @param idInscricaoEstadual
	 * @return boolean
	 */
	public boolean validateTipoTributacaoFOB(Long idInscricaoEstadual) {
		List<String> tipoBloqueioTributacaoFobList = getTiposTributacaoBolquearFOB();
		return getTipoTributacaoIEDAO()
				.findByIdIEAndDtVigenciaAndTpSituacaoTributaria(
						idInscricaoEstadual, JTDateTimeUtils.getDataAtual(),
						tipoBloqueioTributacaoFobList) != null;

	}

	private List<String> getTiposTributacaoBolquearFOB() {
		ParametroGeral tipoBloqueioTributacaoFobParam = parametroGeralService
				.findByNomeParametro("TP_SIT_TRIBUT_BLOQUEIA_FOB");
		String[] tipoBloqueioTributacaoFobArray = tipoBloqueioTributacaoFobParam
				.getDsConteudo().split(";");
		List<String> tipoBloqueioTributacaoFobList = new ArrayList<String>();
		for (String v : tipoBloqueioTributacaoFobArray) {
			tipoBloqueioTributacaoFobList.add(v);
		}
		return tipoBloqueioTributacaoFobList;
	}

	public boolean validateFOBByTipoTributacao(String tipoTributacao){
		List<String> tipoBloqueioTributacaoFobList = getTiposTributacaoBolquearFOB();
		return tipoBloqueioTributacaoFobList.contains(tipoTributacao);
	}
	
	public Boolean validateTipotributacaoIEContribuinteByIE(Long idInscricaoEstadual) {
		String parametoGeralSituacaoContribuinte =  (String) parametroGeralService.findConteudoByNomeParametro("TP_SIT_TRIBUT_CONTRIB", false);
		List<String> listTpSituacaoContribuinte = splitParametroGeral(parametoGeralSituacaoContribuinte);
		
		return getTipoTributacaoIEDAO().findByIdIEAndDtVigenciaAndTpSituacaoTributaria(idInscricaoEstadual, JTDateTimeUtils.getDataAtual(), listTpSituacaoContribuinte) != null;
	}
	
	public Boolean validateTipotributacaoIENaoContribuinteByIE(Long idInscricaoEstadual) {
		String parametoGeralSituacaoNaoContribuinte =  (String) parametroGeralService.findConteudoByNomeParametro("TP_SIT_TRIBUT_NAO_CONTRIB", false);
		List<String> listTpSituacaoNaoContribuinte = splitParametroGeral(parametoGeralSituacaoNaoContribuinte);
		
		return getTipoTributacaoIEDAO().findByIdIEAndDtVigenciaAndTpSituacaoTributaria(idInscricaoEstadual, JTDateTimeUtils.getDataAtual(), listTpSituacaoNaoContribuinte) != null;
	}
	
	private List<String> splitParametroGeral(String conteudoParametroGeral){
		List<String> retorno = new ArrayList<String>();
		String[] parametros = conteudoParametroGeral != null ? conteudoParametroGeral.split(";") : new String[0];
		
		for (String parametro : parametros) {
			retorno.add(parametro);
		}
		
		return retorno;
	}

	public void setTipoTributacaoIEDAO(TipoTributacaoIEDAO dao) {
		setDao( dao );
	}

	private TipoTributacaoIEDAO getTipoTributacaoIEDAO() {
		return (TipoTributacaoIEDAO) getDao();
	}

	public void setEspecializacaoPessoaService(
			EspecializacaoPessoaService especializacaoPessoaService) {
		this.especializacaoPessoaService = especializacaoPessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}