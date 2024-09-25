package com.mercurio.lms.configuracoes.model.service;

import static java.lang.Boolean.TRUE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.configuracoes.model.dao.ServicoAdicionalDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy.TipoParcela;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.util.ConstantesTabelaPrecos;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.servicoAdicionalService"
 */
public class ServicoAdicionalService extends CrudService<ServicoAdicional, Long> {
	private TabelaPrecoService tabelaPrecoService;
	private ConfiguracoesFacade configuracoesFacade;
	private ParametroGeralService parametroGeralService;
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	
	/**
	 * Recupera uma inst�ncia de <code>ServicoAdicional</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ServicoAdicional findById(java.lang.Long id) {
		return (ServicoAdicional)super.findById(id);
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
	public java.io.Serializable store(ServicoAdicional bean) {
		return super.store(bean);
	}

	/**
	 * Filtra os Servi�os Adicionais pela situa��o ativa, 
	 * ou seja, Data de Vig�ncia Inicial do Servi�o Adicional � menor ou igual a Data atual 
	 * e a Data de Vig�ncia Final do Servi�o Adicional � nula ou maior ou igual a data atual.
	 * Se algum id de Servi�o Adicional estiver nos crit�rios o registro correspondente
	 * deve vir junto a lista de Servi�os Adicionais ativos, mesmo este sendo inativo.
	 * @return List de servi�os adicionais ativos
	 */
	public List<ServicoAdicional> findServicosAdicionaisAtivos(Map<String, Object> criterios) {
		return getServicoAdicionalDAO().findServicosAdicionaisAtivos(criterios);
	}

	/**
	 * Filtra os Servi�os Adicionais vigente ou que v�o ser vigente no futuro.
	 * @return List de servi�os adicionais ativos
	 */
	public List<ServicoAdicional> findServicosAdicionaisAtivosFuturos(Map<String, Object> criterios) {
		return getServicoAdicionalDAO().findServicosAdicionaisAtivosFuturos(criterios);
	} 

	public List findServicosAdicionaisByIds(List<Long> listaIds) {
		return getServicoAdicionalDAO().findServicosAdicionaisByIds(listaIds);
	}

	public List<TabelaPrecoParcela> findServicosAdicionaisByTabelaPreco(Long idTabelaPreco) {
		return getServicoAdicionalDAO().findServicosAdicionaisByTabelaPreco(idTabelaPreco);
	}
	
	/**
	 * Busca o id_servico_oficial_tributo relacionado com o servico adicional
	 * vigente conforme a data Base
	 * @param idServicoAdicional Identificador do Servi�o Adicional
	 * @param dtBase Data Base para teste de vig�ncia
	 * @return Identificador do Servi�o Oficial Tributo relacionado ao Servi�o Adicional
	 */
	public Long findIdentificadorServicoOficialTributo(Long idServicoAdicional, YearMonthDay dtBase) {
		return this.getServicoAdicionalDAO().findIdentificadorServicoOficialTributo(idServicoAdicional, dtBase);
	}

	/**
	* Retorna a lista de servico adicional com o tp_situacao para ser usado nas combos
	* 
	* @author Diego Umpierre
	* @since 07/07/2006
	* 
	* @param Map criteria
	* @return List
	* */
	public List<TypedFlatMap> findCombo(Map<String, Object> criteria) {
		List<Object[]> rs = getServicoAdicionalDAO().findCombo();
		List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();

		for(Object[] element : rs) {
			TypedFlatMap mapResult = new TypedFlatMap();
			mapResult.put("idServicoAdicional",element[0]);
			mapResult.put("dsServicoAdicional",element[1]);

			YearMonthDay vigenciaInicial = (YearMonthDay) element[2];
			YearMonthDay vigenciaFinal = (YearMonthDay) element[3];
			YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();

			boolean vigente = true;
			if (vigenciaInicial != null && vigenciaFinal == null) {
				vigente = JTDateTimeUtils.comparaData(dataAtual, vigenciaInicial) >= 0;
			} else if (vigenciaInicial == null && vigenciaFinal != null) {
				vigente = JTDateTimeUtils.comparaData(vigenciaFinal,dataAtual) >= 0;
			} else if (vigenciaInicial != null && vigenciaFinal != null) {
				vigente = JTDateTimeUtils.comparaData(dataAtual, vigenciaInicial) >= 0
						&& JTDateTimeUtils.comparaData(vigenciaFinal, dataAtual) >= 0;						
			}

			if (vigente) {
				mapResult.put("tpSituacao", new DomainValue("A"));
			} else {
				mapResult.put("tpSituacao", new DomainValue("I"));
			}
			list.add(mapResult);
		}
		return list;
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setServicoAdicionalDAO(ServicoAdicionalDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ServicoAdicionalDAO getServicoAdicionalDAO() {
		return (ServicoAdicionalDAO) getDao();
	}

	public TabelaPrecoService getTabelaPrecoService() {
		return tabelaPrecoService;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ServicoAdicionalClienteService getServicoAdicionalClienteService() {
		return servicoAdicionalClienteService;
	}

	public void setServicoAdicionalClienteService(
			ServicoAdicionalClienteService servicoAdicionalClienteService) {
		this.servicoAdicionalClienteService = servicoAdicionalClienteService;
	}
}