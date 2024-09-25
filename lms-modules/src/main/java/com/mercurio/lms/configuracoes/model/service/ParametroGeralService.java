package com.mercurio.lms.configuracoes.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.com.tntbrasil.integracao.domains.jms.HeaderParam;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.utils.EventoSistemaLmsDMN;
import br.com.tntbrasil.integracao.domains.utils.EventoSistemaLmsType;
import br.com.tntbrasil.integracao.domains.utils.MethodRefreshCacheLms;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.dao.ParametroGeralDAO;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.util.LongUtils;

/**
 * Classe de serviço para CRUD: 
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.parametroGeralService"
 */
public class ParametroGeralService extends CrudService<ParametroGeral, Long> {

	private static final Long ANO_BASE = 2019L;
	private static final String PC_ICMS_UF_FIM = "PC_ICMS_UF_FIM";
	private static final String PC_ICMS_UF_FIM_2015 = "PC_ICMS_UF_FIM_2015";
	private static final String PC_ICMS_UF_FIM_2016 = "PC_ICMS_UF_FIM_2016";
	private static final String PC_ICMS_UF_FIM_2017 = "PC_ICMS_UF_FIM_2017";
	private static final String PC_ICMS_UF_FIM_2018 = "PC_ICMS_UF_FIM_2018";
	
	private IntegracaoJmsService integracaoJmsService;

	public void refreshAllServers() {
		EventoSistemaLmsDMN refreshCacheLmsDmn = new EventoSistemaLmsDMN();
		refreshCacheLmsDmn.setMethod(MethodRefreshCacheLms.PARAMETRO_GERAL.getName());
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.EVENTO_SISTEMA_LMS, refreshCacheLmsDmn);
		msg.addHeader(HeaderParam.EVENT_TYPE.getName(), EventoSistemaLmsType.REFRESH_CACHE.getName());
		integracaoJmsService.storeMessage(msg);
	}

	/**
	 * Recupera uma instância de <code>ParametroGeral</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ParametroGeral findById(java.lang.Long id) {
		return (ParametroGeral)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
		getParametroGeralDAO().refreshCache();
		refreshAllServers();
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
		getParametroGeralDAO().refreshCache();
		refreshAllServers();
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ParametroGeral bean) {
		Serializable result = super.store(bean);
		getParametroGeralDAO().refreshCache();
		refreshAllServers();
		return result;
	}

	/**
	 * Busca o conteúdo do parâmetro geral de acordo com o nome
	 * @param nomeParametro
	 * @deprecated @see ParametroGeralService#findConteudoByNomeParametro(String, boolean)
	 * */
	public String findDsConteudoByNmParametro(String nomeParametro) {
		ParametroGeral param = getParametroGeralDAO().findByNomeParametro(nomeParametro, false);
		return param.getDsConteudo();
	}

	/**
	 * Busca um parametro Geral através do nome do parametro.
	 * 
	 * @param nomeParametro nome do parametro
	 * @param realizaLock realiza lock pessimista no registro
	 * @throws BusinessException Com a chave LMS-27051 caso não seja localizado um ParametroGeral com nome informado.
	 * @return o ParametroGeral com o nome informado.
	 */
	public ParametroGeral findByNomeParametro(String nomeParametro, boolean realizaLock) {
		ParametroGeral param = getParametroGeralDAO().findByNomeParametro(nomeParametro, realizaLock);

		if (param == null) {
			if (log.isDebugEnabled()) {
				log.debug("Parâmetro geral não encontrado. nomeParametro='"+nomeParametro+"' - retornando exceção LMS-27051.");
			}
			throw new BusinessException("LMS-27051", new Object[]{nomeParametro});
		}

		return param;
	}

	/**
	 * Busca um parametro Geral através do nome do parametro.
	 * 
	 * @param nomeParametro nome do parametro
	 * @return o ParametroGeral com o nome informado.
	 */
	public ParametroGeral findByNomeParametro(String nomeParametro) {
		ParametroGeral param = getParametroGeralDAO().findByNomeParametro(nomeParametro, false);
		return param;
	}

	/**
	 * Retorna o conteúdo de um parâmetro geral do sistema de acordo com o tipo do formato.
	 * 
	 * @param nomeParametro nome do parametro
	 * @param realizaLock informa se deve ser feito lock pessimista no registro
	 * @return retorna o valor tipado no formato do parametro. retorna <code>null</code> caso o valor seja <code>null</code>.
	 * @throws java.lang.IllegalStateException caso o valor do parametro seja diferente de <code>null</code> e a conversão para o formato do parâmetro não seja realizada com sucesso.
	 */
	public Object findConteudoByNomeParametro(String nomeParametro, boolean realizaLock) {
		ParametroGeral parametroGeral = this.findByNomeParametro(nomeParametro, realizaLock);

		String valueStr = parametroGeral.getDsConteudo();
		Class paramClass = getParametroGeralFormatoClazz(parametroGeral);
		Object value = converteValorParaFormato(nomeParametro, paramClass, valueStr);

		return value;
	}

	/**
	 * Retorna o conteúdo de um parâmetro geral do sistema de acordo com o tipo do formato, sem lançar exceção.
	 * 
	 * @param nomeParametro nome do parametro
	 * @param realizaLock informa se deve ser feito lock pessimista no registro
	 * @return retorna o valor tipado no formato do parametro. retorna <code>null</code> caso o valor seja <code>null</code>.
	 */
	public Object findConteudoByNomeParametroWithoutException(String nomeParametro, boolean realizaLock) {
		ParametroGeral parametroGeral = getParametroGeralDAO().findByNomeParametro(nomeParametro, realizaLock);

		if (parametroGeral == null) {
			if (log.isDebugEnabled()) {
				log.debug("Parâmetro geral não encontrado. nomeParametro='" + nomeParametro + "' - retornando exceção LMS-27051.");
			}
			return null;
		}

		String valueStr = parametroGeral.getDsConteudo();
		Class paramClass = getParametroGeralFormatoClazz(parametroGeral);
		Object value = converteValorParaFormato(nomeParametro, paramClass, valueStr);
		return value;
	}
	

	/**
	 * Retorna o conteúdo puro de um parâmetro.
	 * 
	 * @param nomeParametro nome do parametro
	 * @return retorna o valor puro da parametro
	 */
	public String findSimpleConteudoByNomeParametro(String nomeParametro) {
		String value = getParametroGeralDAO().findSimpleConteudoByNomeParametro(nomeParametro);
		if (value == null) {
			throw new BusinessException("LMS-27051", new Object[]{nomeParametro});
		}
		return  value ;
	}

	private Object converteValorParaFormato(String nomeParametro, Class paramClass, String valueStr) {
		Object value = null;
		if (valueStr != null) {
			value = ReflectionUtils.getConverterInstance().convert(valueStr, paramClass);
			if (value == null) {
				throw new IllegalStateException("Não foi possivel converter o valor do parametro '"+nomeParametro+"' para o tipo de classe informado: "+valueStr+" classe: "+paramClass.getName());
			}
		}
		return value;
	}

	private Class getParametroGeralFormatoClazz(ParametroGeral parametroGeral) {
		Class paramClass = null;
		char tpFormato = parametroGeral.getTpFormato().getValue().charAt(0);

		switch (tpFormato) {
		case 'A':
			paramClass = String.class;
			break;
		case 'N':
			paramClass = BigDecimal.class;
			break;
		case 'D':
			paramClass = java.util.Date.class;
			break;

		default:
			throw new IllegalStateException("tpFormato '"+tpFormato+"' não reconhecido para o parâmetro geral: "+parametroGeral.getNmParametroGeral());
		}
		
		return paramClass;
	}

	/**
	 * Verifica se o valor informado é o mesmo que o valor contido na base de dados.<BR>
	 * Exemplo de uso:<BR>
	 * <code>
	 * String idEmpresa = tfm.getString("empresa.idEmpresa");
	 * //Validar se empresa é GOL
	 * 	if (service.validateDsConteudoByNmParametro(idEmpresa, "ID_GOL")){
	 *	//é GOL
	 * }
	 * </code>
	 * @param dsConteudo
	 * @param nomeParametro
	 * @author Robson Edemar Gehl
	 * @return
	 */
	public boolean validateDsConteudoByNmParametro(String dsConteudo, String nomeParametro){

		if (dsConteudo == null) {
			throw new IllegalArgumentException("dsConteudo não pode ser null");
		}

		ParametroGeral param = getParametroGeralDAO().findByNomeParametro(nomeParametro, false);
		boolean valido = false;
		if (param != null) {
			valido = dsConteudo.equals(param.getDsConteudo());
		}

		return valido;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setParametroGeralDAO(ParametroGeralDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ParametroGeralDAO getParametroGeralDAO() {
		return (ParametroGeralDAO) getDao();
	}

	public List findServicosAdicionais(List listaParametros){
		return getParametroGeralDAO().findServicosAdicionais(listaParametros);
	}

	public ParametroGeral findPGPcIcmsUfFimByAno(Long ano) {
		String nmParemtroGeral = getParametroGeral(ano);
		
		return getParametroGeralDAO().findByNomeParametro(nmParemtroGeral, true);
	}	
	
	private String getParametroGeral(Long anoEmissao) {
		if (anoEmissao.compareTo(ANO_BASE) < LongUtils.ZERO) {
			return new StringBuilder(PC_ICMS_UF_FIM).append("_").append(anoEmissao).toString();
		}
		
		return PC_ICMS_UF_FIM;
	}

	/**
	 * Altera o valor de um parametro geral de acordo com o valor do parametro incremento,
	 * podendo ser especificado se deve ser realizado lock pessimista no registro.
	 * Caso o parametro possua um valor <code>null</code> ou vazio inicia em zero (0) mais
	 * o valor do parametro incremento.
	 * 
	 * @param nomeParametro nome do parâmetro a ser incrementado.
	 * @param lockParametro informa se deve ser feito lock pessimista.
	 * @return numero do parâmetro de incrementado.
	 */
	public Long generateValorParametroSequencial(String nomeParametro, boolean lockParametro, long incremento) {
		return getParametroGeralDAO().generateValorParametroSequencial(nomeParametro, incremento);
		}

	/**
	 * Armazena o valor de um parametro geral. Este método não efetua lock pessimista
	 * no registro.
	 * 
	 * @param nomeParametro nome do parametro
	 * @param valorParametro valor do parametro
	 * @throws ClassCastException caso o tipo de dados do valor informado para o parametro seja incompativel com o formato do parametro. 
	 */
	public void storeValorParametro(String nomeParametro, Object valorParametro) {
		ParametroGeral paramGeral = this.findByNomeParametro(nomeParametro, false);
		
		if (valorParametro != null) {
			Class paramClass = getParametroGeralFormatoClazz(paramGeral);
			if (!paramClass.isAssignableFrom(valorParametro.getClass())) {
				throw new ClassCastException("Não é possivel salvar o parametro com a classe informada, esperava: "+paramClass.getName()+" recebeu: "+valorParametro.getClass());
			}
		}
		
		// FIX: ver se formato de armazenamento do parametro será sempre o 
		// retorno do .toString() para cada tipo de parametro
		paramGeral.setDsConteudo(valorParametro.toString()); // CJS 03/12/2009 - Modificado de value.toString() para valorParametro.toString(), pois o "value" é instanciado com NULL e nada mais é feito com ele, gerando assim uma exceção ao salvar NULL 
		
		store(paramGeral);
	}

	
	/**
	 * Armazena o valor de um parametro geral em uma nova sessão de banco de dados
	 * 
	 * @param nomeParametro nome do parametro
	 * @param valorParametro valor do parametro
	 * @throws ClassCastException caso o tipo de dados do valor informado para o parametro seja incompativel com o formato do parametro. 
	 */
	public void storeValorParametroNewSession(String nomeParametro, Object valorParametro) {
		getParametroGeralDAO().storeValorParametroNewSession(nomeParametro, valorParametro);
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
}