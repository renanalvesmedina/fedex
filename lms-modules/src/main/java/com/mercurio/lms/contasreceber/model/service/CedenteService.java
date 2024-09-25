package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.dao.CedenteDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.cedenteService"
 */
public class CedenteService extends CrudService<Cedente, Long> {

	/**
	 * M�todo que busca as Cedente de acordo com os filtros passados
	 * @param criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedByCedente(TypedFlatMap criteria){
		return getCedenteDAO().findPaginatedByCedente(criteria);
	}

	 /**
	 * M�todo que retorna o n�mero de registros de acordo com os filtros passados
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountByCedente(TypedFlatMap criteria){
		return getCedenteDAO().getRowCountByCedente(criteria);
	}

	/**
	 * Recupera uma inst�ncia de <code>Cedente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public Cedente findById(java.lang.Long id) {
		return (Cedente)super.findById(id);
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
	 * Find da combo padr�o
	 * 
	 * Micka�l Jalbert - 19/10/2005
	 */
	public List findComboPadrao() {
		List list = getCedenteDAO().findComboPadrao();
		return list;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Cedente bean) {
		return super.store(bean);
	}

	public List findCombo(Map map) {
		List rs = getCedenteDAO().findCombo(map);
		List list = new ArrayList();

		if (rs.size() > 0) {
			for(int x = 0; x < rs.size(); x++) {
				HashMap mapResult = new HashMap();

				mapResult.put("idCedente",((Object[])rs.get(x))[0]);
				mapResult.put("comboText",((Object[])rs.get(x))[1]);

				String tpSituacaoBanco = ((DomainValue) ((Object[])rs.get(x))[2]).getValue();
				String tpSituacaoAgencia = ((DomainValue) ((Object[]) rs.get(x))[3]).getValue();
				if (tpSituacaoBanco.equalsIgnoreCase("I") || tpSituacaoAgencia.equalsIgnoreCase("I")) {
					mapResult.put("tpSituacao", new DomainValue("I"));
				} else {
					YearMonthDay vigenciaInicial = (YearMonthDay) ((Object[]) rs.get(x))[4];
					YearMonthDay vigenciaFinal = (YearMonthDay) ((Object[]) rs.get(x))[5];
					YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();

					boolean vigente = true;
					if (vigenciaFinal == null) {
						vigente = JTDateTimeUtils.comparaData(vigenciaInicial,dataAtual) <= 0;
					} else {
						vigente = JTDateTimeUtils.comparaData(vigenciaInicial,dataAtual) <= 0
								&& JTDateTimeUtils.comparaData(vigenciaFinal, dataAtual) >= 0;						
					}

					if (vigente) {
						mapResult.put("tpSituacao", new DomainValue("A"));
					} else {
						mapResult.put("tpSituacao", new DomainValue("I"));
					}

				}
				list.add(mapResult);
			}
		}
		return list;
	}

	/**
	 * M�todo respons�vel por retornar os Cedentes ativos para popular a combo
	 * @param criteria
	 * @return List 
	 */
	public List findComboByActiveValues(TypedFlatMap criteria){
		Long idCedente = null;
		if (criteria != null){
			idCedente = criteria.getLong("cedente.idCedente");
		}
		List rs = getCedenteDAO().findComboByActiveValues(idCedente);
		List list = new ArrayList();
		for(Iterator iter = rs.iterator(); iter.hasNext();) {
			Map mapResult = (Map)iter.next();
			Map mapRetorno = new HashMap();
			mapRetorno.put("idCedente",mapResult.get("idCedente"));
			mapRetorno.put("comboText",mapResult.get("dsCedente"));
			list.add(mapRetorno);
		}
		return list;
	}

	/**
	 * M�todo respons�vel por retornar os Cedentes ativos para popular a combo
	 * @param criteria
	 * @return List 
	 */
	public List findCedenteAtivoByIdFatura(Long idFatura){
		return getCedenteDAO().findCedenteAtivoByIdFatura(idFatura);
	}

	/**
	 * M�todo respons�vel por retornar os Cedentes ativos para popular a combo
	 * @param criteria
	 * @return List 
	 */
	public List findMapCedenteAtivoByIdCedente(Long idCedente){
		return getCedenteDAO().findCedenteAtivoByIdCedente(idCedente);
	}

	/**
	 * M�todo respons�vel por retornar os Cedentes para popular a combo
	 * @param criteria
	 * @return List 
	 */
	public List findComboValues(TypedFlatMap criteria){
		List rs = getCedenteDAO().findCombo(criteria);
		List list = new ArrayList();
		if (rs.size() > 0) { 
			for(int x = 0; x < rs.size(); x++) {
				HashMap mapResult = new HashMap();

				Object[] dados = ((Object[])rs.get(x));
				mapResult.put("idCedente", dados[0]);
				mapResult.put("comboText", dados[1]);

				if (((DomainValue)dados[2]).getValue().equals("I")){
					mapResult.put("tpSituacao", (DomainValue)dados[2]);
				} else if (((DomainValue)dados[3]).getValue().equals("I")) {
					mapResult.put("tpSituacao", (DomainValue)dados[3]);
				} else if ( JTDateTimeUtils.comparaData(((YearMonthDay)dados[4]), JTDateTimeUtils.getDataAtual()) <= 0 ){
					if (dados[5] != null) {
						if ( JTDateTimeUtils.comparaData(((YearMonthDay)dados[5]), JTDateTimeUtils.getDataAtual()) >= 0 ){
							mapResult.put("tpSituacao", new DomainValue("A"));
						} else {
							mapResult.put("tpSituacao", new DomainValue("I"));
						}
					} else {
						mapResult.put("tpSituacao", new DomainValue("A"));
					}
				} else {
					mapResult.put("tpSituacao", new DomainValue("I"));
				}
				list.add(mapResult);
			}
		}

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				Map map1 = (Map) o1;
				Map map2 = (Map) o2;

				String valor1 = (String) map1.get("comboText");
				String valor2 = (String) map2.get("comboText");

				return valor1.compareToIgnoreCase(valor2);
			}
		});

		return list;
	}

	/**
	 * M�todo invocado antes do store 
	 */
	public Cedente beforeStore(Cedente bean) {
		Cedente cedente = (Cedente) bean;

		if(validaVigenciaCedente(cedente)){
			throw new BusinessException("LMS-36069");
		}

		//N�o pode existir um cedente do banco Itau ou Bradesco sem n�mero de carteira
		if (cedente.getAgenciaBancaria().getBanco().getNrBanco().equals(ConstantesConfiguracoes.COD_ITAU) || cedente.getAgenciaBancaria().getBanco().getNrBanco().equals(ConstantesConfiguracoes.COD_BRADESCO)){
			if (cedente.getNrCarteira() == null) {
				throw new BusinessException("LMS-36136");
			}
		}

		return super.beforeStore(bean);
	}

	/**
	 * M�todo que valida se a vig�ncia do cedente altual
	 * @param cedente
	 * @return
	 */
	public boolean validaVigenciaCedente(Cedente cedente){
		boolean vigInvalida = false;

		YearMonthDay ymdInicial = cedente.getDtVigenciaInicial();
		YearMonthDay ymdFinal = cedente.getDtVigenciaFinal();

		List cedentes = findCedenteByVigencia(
				ymdInicial,
				ymdFinal,
				cedente.getAgenciaBancaria().getIdAgenciaBancaria(),
				cedente.getAgenciaBancaria().getBanco().getIdBanco(),
				cedente.getIdCedente(),
				cedente.getNrCarteira()
		);

		if(cedentes != null && !cedentes.isEmpty()){
			vigInvalida = true;
		}

		return vigInvalida;
	}	
	
	public List findCedenteByVigencia(YearMonthDay dtInicial, YearMonthDay dtFinal, Long idAgenciaBancaria, Long idBanco){
		return findCedenteByVigencia(dtInicial, dtFinal, idAgenciaBancaria, idBanco, null, null);
	}

	public List findCedenteByVigencia(YearMonthDay dtInicial, YearMonthDay dtFinal, Long idAgenciaBancaria, Long idBanco, Long idCedente, Short nrCarteira){
		return getCedenteDAO().findCedenteByVigencia(dtInicial, dtFinal, idAgenciaBancaria, idBanco, idCedente, nrCarteira);
	}	

	public List findCedenteByVigencia(YearMonthDay dtInicial, YearMonthDay dtFinal, Long idAgenciaBancaria, Long idBanco, Long idCedente){
		return findCedenteByVigencia(dtInicial, dtFinal, idAgenciaBancaria, idBanco, idCedente, null);
	}	

	/**
	 * Busca uma lista de cedentes de acordo com a contaCorrente, n�mero da agencia e n�mero do banco
	 * @param contaCorrente
	 * @param nrAgencia
	 * @param nrBanco
	 * @return Lista de Cedentes
	 */
	public List findCedentes(String contaCorrente, Long nrAgencia, Long nrBanco) {
		return getCedenteDAO().findCedentes(contaCorrente, nrAgencia, nrBanco);
	}

	/**
	 * Retorna o cedente do cliente informado se existe, sen�o retorna null.
	 * 
	 * @author Micka�l Jalbert
	 * @since 29/06/2006
	 * 
	 * @param Long idCliente
	 * @return Cedente
	 * */
	public Cedente findByIdCliente(Long idCliente){
		return getCedenteDAO().findByIdCliente(idCliente);
	}

	/**
	 * Retorna o cedente da filial informada se existe, sen�o retorna null.
	 * 
	 * @author Micka�l Jalbert
	 * @since 03/07/2006
	 * 
	 * @param Long idFilial
	 * @return Cedente
	 * */
	public Cedente findByIdFilial(Long idFilial){
		return getCedenteDAO().findByIdFilial(idFilial);
	}

	/**
	 * @author Jos� Rodrigo Moraes
	 * @since  17/08/2006
	 * 
	 * Se o cliente possui um cedente, retorna este para a combo "Cedente", 
	 * caso contr�rio, traz o cedente da filial de cobran�a do cliente, ou seja utilizar 
	 * FILIAL.ID_CEDENTE_BLOQUETO onde FILIAL.ID_FILIAL = CLIENTE.ID_FILIAL_COBRANCA.
	 * 
	 * @param tfm IdCliente
	 * @return Cedente
	 */
	public Cedente findCedenteByFilialCobrancaCliente(Long idCliente) {		
		return getCedenteDAO().findCedenteByFilialCobrancaCliente(idCliente);
	}  	

	/**
	 * M�todo respons�vel por retornar os Cedentes ativos 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 07/11/2006
	 *
	 * @return
	 *
	 */
	public List<Map> findCedentesAtivos(){
		return getCedenteDAO().findCedentesAtivos();
	}

	/**
	 * Retorna true se o cedente informado � ativo
	 * 
	 * @author Micka�l Jalbert
	 * @since 01/03/2007
	 * 
	 * @param idCedente
	 * @return
	 */
	public boolean isCedenteAtivo(Long idCedente){
		return getCedenteDAO().isCedenteAtivo(idCedente);
	}

	/**
	 * Carrega um cedente de acordo com o banco, a ag�ncia, o n�mero da conta corrente ou o c�digo do cedente.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 03/07/2007
	 *
	 * @param contaCorrente
	 * @param cdCedente
	 * @param nrAgencia
	 * @param nrBanco
	 * @return
	 *
	 */
	public Cedente findCedenteByAgenciaAndBanco(String contaCorrente, Long cdCedente, Short nrAgencia, Short nrBanco){
		return getCedenteDAO().findCedenteByAgenciaAndBanco(contaCorrente, cdCedente, nrAgencia, nrBanco);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setCedenteDAO(CedenteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private CedenteDAO getCedenteDAO() {
		return (CedenteDAO) getDao();
	}
}