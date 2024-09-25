package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class OperacaoServicoLocalizaDAO extends BaseCrudDao<OperacaoServicoLocaliza, Long> {

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("municipioFilial", FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial", FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial.empresa", FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial.pessoa", FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio", FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.municipioDistrito", FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa", FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa.pais", FetchMode.JOIN);
		fetchModes.put("tipoLocalizacaoMunicipio", FetchMode.JOIN);
		fetchModes.put("tipoLocalizacaoMunicipioFob", FetchMode.JOIN);
		fetchModes.put("servico", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("municipioFilial",FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial",FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial.pessoa",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.municipioDistrito",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa.pais",FetchMode.JOIN);
		fetchModes.put("tipoLocalizacaoMunicipio",FetchMode.JOIN);
		fetchModes.put("servico",FetchMode.JOIN);
	}

	/**
	 * FindPaginated Customizado
	 * @param criteria
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = getSql(criteria);
		ResultSetPage findPaginated = getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
		return findPaginated;
	}

	/**
	 * GetRowCount Customizado
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountCustom(TypedFlatMap criteria){
		SqlTemplate sql = getSql(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

	//Método private para geração do sql da grid
	private SqlTemplate getSql(TypedFlatMap criteria) {
		StringBuilder projection = new StringBuilder()
		.append("new Map( osl.idOperacaoServicoLocaliza as idOperacaoServicoLocaliza, \n")
		.append("  mun.nmMunicipio as municipioFilial_municipio_nmMunicipio, \n")
		.append("  (uf.sgUnidadeFederativa || ' - ' || uf.nmUnidadeFederativa) as municipioFilial_municipio_unidadeFederativa_siglaDescricao, \n")
		.append("  uf.nmUnidadeFederativa as municipioFilial_municipio_unidadeFederativa_nmUnidadeFederativa, \n")
		.append("  uf.sgUnidadeFederativa as municipioFilial_municipio_unidadeFederativa_sgUnidadeFederativa, \n")
		.append("  p.nmPais as municipioFilial_municipio_unidadeFederativa_pais_nmPais, \n")
		.append("  mun.blDistrito as municipioFilial_municipio_blDistrito, \n")
		.append("  md.nmMunicipio as municipioFilial_municipio_municipioDistrito_nmMunicipio, \n")
		.append("  (fil.sgFilial || ' - ' || pessoaFilial.nmPessoa) as municipioFilial_filial_siglaNomeFilial, \n")
		.append("  fil.sgFilial as municipioFilial_filial_sgFilial, \n")
		.append("  pessoaFilial.nmPessoa as municipioFilial_filial_pessoa_nmPessoa, \n")
		.append("  osl.blAtendimentoGeral as blAtendimentoGeral, \n")
		.append("  osl.tpOperacao as tpOperacao, \n")
		.append("  ser.dsServico as servico_dsServico, \n")
		.append("  tlm.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipio_dsTipoLocalizacaoMunicipio, \n")
		.append("  tlmf.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioFob_dsTipoLocalizacaoMunicipio, \n")
		.append("  osl.nrTempoColeta as nrTempoColeta, \n")
		.append("  osl.nrTempoEntrega as nrTempoEntrega, \n")
		.append("  osl.blDomingo as blDomingo, osl.blSegunda as blSegunda, osl.blTerca as blTerca, \n")
		.append("  osl.blQuarta as blQuarta, osl.blQuinta as blQuinta, osl.blSexta as blSexta, osl.blSabado as blSabado, \n")
		.append("  osl.dtVigenciaInicial as dtVigenciaInicial, osl.dtVigenciaFinal as dtVigenciaFinal )\n");

		StringBuilder from = new StringBuilder()
		.append(OperacaoServicoLocaliza.class.getName()).append(" as osl \n")
		.append("left join osl.servico as ser \n")
		.append("join osl.municipioFilial as mf \n")
		.append("join mf.filial as fil \n")
		.append("join fil.pessoa as pessoaFilial \n")
		.append("join mf.municipio as mun \n")
		.append("join mun.unidadeFederativa as uf \n")
		.append("join uf.pais as p \n")
		.append("left join mun.municipioDistrito as md \n")
		.append("join osl.tipoLocalizacaoMunicipio as tlm \n")
		.append("join osl.tipoLocalizacaoMunicipioFob as tlmf \n");

		SqlTemplate t = new SqlTemplate();

		t.addProjection(projection.toString());
		t.addFrom(from.toString());

		t.addCriteria("osl.dtVigenciaInicial", ">=", criteria.getYearMonthDay("dtVigenciaInicial"), YearMonthDay.class);
		t.addCriteria("osl.dtVigenciaFinal",  "<=", criteria.getYearMonthDay("dtVigenciaFinal"), YearMonthDay.class);
		t.addCriteria("osl.blAceitaFreteFob", "=", criteria.getBoolean("blAceitaFreteFob"));
		t.addCriteria("osl.tpOperacao", "=", criteria.getDomainValue("tpOperacao").getValue());
		t.addCriteria("osl.blAtendimentoGeral","=", criteria.getBoolean("blAtendimentoGeral"));
		t.addCriteria("osl.blCobraTaxaFluvial", "=", criteria.getBoolean("blCobraTaxaFluvial"));
		t.addCriteria("tlm.id", "=", criteria.getLong("tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio"), Long.class);
		t.addCriteria("tlmf.id", "=", criteria.getLong("tipoLocalizacaoMunicipioFob.idTipoLocalizacaoMunicipio"), Long.class);
		t.addCriteria("mf.id", "=", criteria.getLong("municipioFilial.idMunicipioFilial"), Long.class);
		t.addCriteria("mun.blDistrito", "=", criteria.getBoolean("municipioFilial.municipio.blDistrito"));
		t.addCriteria("md.id", "=", criteria.getLong("municipioFilial.municipio.municipioDistrito.idMunicipio"), Long.class);
		t.addCriteria("uf.id", "=", criteria.getLong("municipioFilial.municipio.unidadeFederativa.idUnidadeFederativa"), Long.class);
		t.addCriteria("p.id", "=", criteria.getLong("municipioFilial.municipio.unidadeFederativa.pais.idPais"), Long.class);
		t.addCriteria("ser.id", "=", criteria.getLong("servico.idServico"), Long.class);
		t.addCriteria("fil.idFilial", "=", criteria.getLong("municipioFilial.filial.idFilial"), Long.class);

		t.addOrderBy("fil.sgFilial");
		t.addOrderBy("fil.pessoa.nmPessoa");
		t.addOrderBy("mun.nmMunicipio");
		t.addOrderBy("osl.dtVigenciaInicial");

		return t;
	}

	/**
	 * Retorna uma lista de operacaoXservicoXlocalizacao vigentes
	 * @param criteria
	 * @return
	 */
	public List findOperacaoServicoPorMunicipio(Long idMunicipio, Long idFilial){
		SqlTemplate t = new SqlTemplate();

		t.addProjection("new Map( osl.idOperacaoServicoLocaliza as idOperacaoServicoLocaliza, " +
					" ser.dsServico as servico_dsServico, " + 
					" osl.nrTempoColeta as nrTempoColeta, " +
					" osl.nrTempoEntrega as nrTempoEntrega, " +
					" osl.blDomingo as blDomingo, osl.blSegunda as blSegunda, osl.blTerca as blTerca, " +
					" osl.blQuarta as blQuarta, osl.blQuinta as blQuinta, osl.blSexta as blSexta, osl.blSabado as blSabado, " +
					" osl.dtVigenciaInicial as dtVigenciaInicial, osl.dtVigenciaFinal as dtVigenciaFinal, osl.tpOperacao as tpOperacao)");

		t.addFrom(OperacaoServicoLocaliza.class.getName() + " as osl left join osl.servico as ser ");

		t.addCriteria("osl.municipioFilial.municipio.idMunicipio",  "=", idMunicipio, Long.class);
		t.addCriteria("osl.municipioFilial.filial.idFilial",  "=", idFilial, Long.class);

		t.addCustomCriteria("((osl.dtVigenciaFinal >= ? and osl.dtVigenciaInicial <= ?) or osl.dtVigenciaInicial>?)");
		t.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		t.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		t.addCriteriaValue(JTDateTimeUtils.getDataAtual());

		t.addOrderBy(OrderVarcharI18n.hqlOrder("ser.dsServico", LocaleContextHolder.getLocale()));

		return getAdsmHibernateTemplate().find(t.getSql(true), t.getCriteria());
	}

	/**
	 * Retorna registro de Operacao X Servico X Localizacao vigente para o Municipio X Filial
	 * @param idMunicipioFilial
	 * @param dtVigenciaFinal 
	 * @param dtVigenciaInicial 
	 * @return
	 */
	public OperacaoServicoLocaliza findOperacaoServicoPorMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();

		dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		if (dtVigenciaFinal != null) {
			dc.add(Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));
		} else dc.add(Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaInicial)));

		List<OperacaoServicoLocaliza> result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (result.size() > 0)		
			return result.get(0);
		else
			return null;
	}

	public Object[] findFrequenciaPorMunicipioFilial(Long idMunicipioFilial, Long idServico, YearMonthDay dtVigencia){
		DetachedCriteria dc = createDetachedCriteria();

		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("blDomingo"))
		.add(Projections.property("blSegunda"))
		.add(Projections.property("blTerca"))
		.add(Projections.property("blQuarta"))
		.add(Projections.property("blQuinta"))
		.add(Projections.property("blSexta"))
		.add(Projections.property("blSabado")); 

		dc.setProjection(pl);
		dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
		dc.add(Restrictions.or(
				Restrictions.isNull("servico.idServico"),
				Restrictions.eq("servico.idServico", idServico)));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigencia)));

		List<Object[]> result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (result.size() > 0)		
			return result.get(0);
		else 
			return null;
	}
	
	/**
	 * Método que retorno operacaoServicoLocaliza pelo municipioFilialOrigem e 
	 * @param idMunicipioFilialOrigem
	 * @param idMunicipioFilialDestino
	 * @param idServico
	 * @param dtVigencia
	 * @return
	 */
	public List<OperacaoServicoLocaliza> findOperacaoServicoLocalizacao(Long idMunicipioFilialOrigem, Long idMunicipioFilialDestino, Long idServico, YearMonthDay dtVigencia){
		DetachedCriteria dc = createDetachedCriteria();

		dc.add(Restrictions.or(
				Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilialOrigem),
				Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilialDestino)));
		dc.add(Restrictions.or(
				Restrictions.isNull("servico.idServico"),
				Restrictions.eq("servico.idServico", idServico)));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigencia)));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return OperacaoServicoLocaliza.class;
	}

	/**
	 * Verifica a existencia de um registro que possua pelo menos os mesmos dias de freqüência,
	 * para o mesmo municipioFilial, para o mesmo servico, mesmo tipoLocalizacao e mesmo tipoOperacao.
	 *  
	 * @param osl 
	 * @return True, caso seja possível inserir e false em caso contrário.
	 */
	public boolean verificaDiasFrequenciaVigentes(OperacaoServicoLocaliza osl) {
		DetachedCriteria dc = createDetachedCriteria();
	
		if (osl.getIdOperacaoServicoLocaliza() != null) { 
			dc.add(Restrictions.ne("idOperacaoServicoLocaliza", osl.getIdOperacaoServicoLocaliza()));
		}

		// verificando registros que possuam blAtendimentoGeral marcado
		dc.add(Restrictions.eq("blAtendimentoGeral", osl.getBlAtendimentoGeral()));

		// verificando o intervalo de vigencia
		dc = JTVigenciaUtils.getDetachedVigencia(dc, osl.getDtVigenciaInicial(), osl.getDtVigenciaFinal());

		// verificando dias checados
		dc.add(getCriterionDiasChecados(osl));

		// verificando tpOperacao
		dc.add(Restrictions.eq("tpOperacao", osl.getTpOperacao()));

		// verificando municipioFilial
		if (osl.getMunicipioFilial() != null) {
			dc.createAlias("municipioFilial", "mf");
			dc.add( Restrictions.eq("mf.idMunicipioFilial", osl.getMunicipioFilial().getIdMunicipioFilial()) );
		}

		// verificando tipoLocalizacaoMunicipio
		if (osl.getTipoLocalizacaoMunicipio() != null && osl.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio() != null) {
			dc.createAlias("tipoLocalizacaoMunicipio", "tlm");		
			dc.add(Restrictions.eq("tlm.idTipoLocalizacaoMunicipio", osl.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio()));
		}

		// verificando servico
		if (osl.getServico()!= null && osl.getServico().getIdServico() != null) {
			dc.createAlias("servico", "s");
			dc.add(Restrictions.eq("s.idServico", osl.getServico().getIdServico()));
		}
		return !(findByDetachedCriteria(dc).size() > 0);
	}

	/**
	 * Verifica a existencia de um registro que possua o mesmo municipioFilial e serviço vigentes
	 * @param osl 
	 * @return True, caso seja possível inserir e false em caso contrário.
	 */
	public boolean findOperacaoServicoLocalizaVigente(OperacaoServicoLocaliza osl) {
		DetachedCriteria dc = createDetachedCriteria();

		dc.setProjection(Projections.rowCount());

		if (osl.getIdOperacaoServicoLocaliza() != null) { 
			dc.add(Restrictions.ne("idOperacaoServicoLocaliza", osl.getIdOperacaoServicoLocaliza()));
		}
		// verificando o intervalo de vigencia
		dc = JTVigenciaUtils.getDetachedVigencia(dc, osl.getDtVigenciaInicial(), osl.getDtVigenciaFinal());
		// verificando municipioFilial
		if (osl.getMunicipioFilial() != null) {
			dc.createAlias("municipioFilial", "mf");
			dc.add( Restrictions.eq("mf.idMunicipioFilial", osl.getMunicipioFilial().getIdMunicipioFilial()) );
		}

		// verificando servico
		dc.createAlias("servico", "s");
		if (osl.getServico()!= null && osl.getServico().getIdServico() != null) {
			dc.add(Restrictions.eq("s.id", osl.getServico().getIdServico()));
		} else {
			dc.add(Restrictions.isNull("s.id"));
		}

		if (!osl.getBlAtendimentoGeral().booleanValue()){			
			if (!osl.getTpOperacao().getValue().equals("A")){
				dc.add(Restrictions.eq("blAtendimentoGeral", Boolean.FALSE));
				Criterion tpOperacao = Restrictions.or(
												Restrictions.ne("tpOperacao", osl.getTpOperacao().getValue()),
												Restrictions.eq("tpOperacao", "A"));				
				dc.add(Restrictions.not(tpOperacao));
			}
		}
		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}

	/**
	 * Verifica a existencia de um registro que possua o mesmo municipioFilial e serviço vigentes
	 * @param osl 
	 * @return True, caso seja possível inserir e false em caso contrário.
	 */
	public boolean findOperacaoServicoLocalizaVigenteByOperacao(OperacaoServicoLocaliza osl) {
		DetachedCriteria dc = createDetachedCriteria();

		if (osl.getIdOperacaoServicoLocaliza() != null) { 
			dc.add(Restrictions.ne("idOperacaoServicoLocaliza", osl.getIdOperacaoServicoLocaliza()));
		}

		// verificando o intervalo de vigencia
		dc = JTVigenciaUtils.getDetachedVigencia(dc, osl.getDtVigenciaInicial(), osl.getDtVigenciaFinal());

		// verificando municipioFilial.municipio
		if (osl.getMunicipioFilial() != null) {
			dc.createAlias("municipioFilial", "mf");
			dc.createAlias("mf.municipio", "mfm");
			dc.add( Restrictions.eq("mfm.idMunicipio", osl.getMunicipioFilial().getMunicipio().getIdMunicipio()));
		}

		// verificando servico
		if (osl.getServico()!= null && osl.getServico().getIdServico() != null) {
			dc.createAlias("servico", "s");
			dc.add(Restrictions.eq("s.idServico", osl.getServico().getIdServico()));
		} else {
			dc.add(Restrictions.isNull("servico.idServico"));
		}

		//verifica se existe a operação "Ambos"
		dc.add(Restrictions.eq("tpOperacao","A"));

		return findByDetachedCriteria(dc).size() > 0;
	}

	/**
	 * Obtém o critério de pesquisa em DetachedCriteria da pesquisa
	 * que compara os dias checados com os dias marcados no banco.
	 * 
	 * @param bean O bean.
	 * @return O critério devidamente montado.
	 */
	public Criterion getCriterionDiasChecados(OperacaoServicoLocaliza osl) {
		return getCriterionDiasChecados(osl, "");
	}

	/**
	 * Obtém o critério de pesquisa em Criteria da pesquisa
	 * que compara os dias checados com os dias marcados no banco 
	 * apartir de um contexto especificado.
	 * 
	 * @param bean O bean.
	 * @param context Contexto da pesquisa (alias antes do atributo).
	 * @return O critério devidamente montado.
	 * @author luisfco
	 */
	public Criterion getCriterionDiasChecados(OperacaoServicoLocaliza osl, String context) {
		if (!"".equals(context))
			context += ".";

		Criterion orDiasSemana = null;

		// Problema: precisa-se de avliar um critério de (OR) que 
		// avalie todos os dias da semana que foram checados na tela.
		// No entanto o método Restrictions.or() só aceita dois critérios e não (n) critérios.
		// Dessa forma, a solução adotada foi criar um mapa contendo todos os dias da semana que
		// foram checados.
		// Para cada dia da semana é criada uma Restrictions.EQ()
		// Após isso, são tomadas as Restrictions duas a duas e adicionadas a um Restrictions.OR() 
		// A restriction que sobrar é aninhada ao conjunto de Restrictions montado.

		// Map que possuirá todos os dias checados
		Map<String, Boolean> diasSemana = new HashMap<String, Boolean>();

		if (osl.getBlDomingo().equals(Boolean.TRUE)) {
			diasSemana.put(context + "blDomingo", osl.getBlDomingo());
		}
		if (osl.getBlSegunda().equals(Boolean.TRUE)) {
			diasSemana.put(context + "blSegunda", osl.getBlSegunda());
		}
		if (osl.getBlTerca().equals(Boolean.TRUE)) {
			diasSemana.put(context + "blTerca", osl.getBlTerca());
		}
		if (osl.getBlQuarta().equals(Boolean.TRUE)) {
			diasSemana.put(context + "blQuarta", osl.getBlQuarta());
		}
		if (osl.getBlQuinta().equals(Boolean.TRUE)) {
			diasSemana.put(context + "blQuinta", osl.getBlQuinta());
		}
		if (osl.getBlSexta().equals(Boolean.TRUE)) {
			diasSemana.put(context + "blSexta", osl.getBlSexta());
		}
		if (osl.getBlSabado().equals(Boolean.TRUE)) {
			diasSemana.put(context + "blSabado", osl.getBlSabado());
		}

		// lista de Restrictions individuais que farão parte do (OR)
		List restricoes = new ArrayList();

		// para cada dia da semana, adiciona uma restrição de (EQ) 
		// Ex.: Restrictions.eq( "blDomingo", osl.getBlDomingo() )
		for (Iterator it = diasSemana.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry en = (Entry) it.next();
			restricoes.add(Restrictions.eq((String)en.getKey(), en.getValue()));
		}

		Integer ultimaRestricaoImpar = null;

		// se número ímpar de restricoes, então a última restrição
		// é a (ímpar) que será aninhada às demais restrições irmãs
		if (restricoes.size() % 2 == 1) {
			ultimaRestricaoImpar = Integer.valueOf( restricoes.size() - 1 );
		}

		int i = 0;
		while (i < restricoes.size()) {

			// se é uma restrição par normal
			if (! Integer.valueOf(i).equals(ultimaRestricaoImpar)) {
				if (orDiasSemana == null)
					orDiasSemana = Restrictions.or((Criterion) restricoes.get(i), (Criterion) restricoes.get(i+1));
				else 
					orDiasSemana = Restrictions.or(orDiasSemana, 
									Restrictions.or((Criterion) restricoes.get(i), (Criterion) restricoes.get(i+1)));

			} else {
			// se é a última restrição (ímpar)
				if (orDiasSemana == null)
					orDiasSemana = (Criterion) restricoes.get(i);
				else 
					orDiasSemana = Restrictions.or((Criterion) restricoes.get(i), orDiasSemana);
			}

			// as restrições de (OR) são avaliadas duas a duas
			i += 2;
		}

		return orDiasSemana;
	}

	/**
	 * Verifica se o intervalo de datas é vigente.
	 * @param osl OperacaoServicoLocaliza
	 * @return True se for vigente.
	 */
	public boolean validaVigenciaAtendimento(Long idOperacaoServicoLocaliza, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idOperacaoServicoLocaliza",idOperacaoServicoLocaliza));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));
				
		return (findByDetachedCriteria(dc).size()>0);
	}

	
	/**
	 * Verifica se existe algum registro de OperacaoXServicoXLocalizacao vigente no dia atual ou no dia atual + 1 para o 
	 * MunicipioXFilial informado
	 * @param idMunicipioFilialTroca
	 * @return TRUE se existe algum registro, FALSE caso contrario
	 */
	public boolean verificaExisteOperacaoServicoParaFilialTroca(Long idMunicipioFilialTroca){
		DetachedCriteria dc = createDetachedCriteria();

		dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial",idMunicipioFilialTroca));

		Restrictions.or(
			Restrictions.and(
					Restrictions.le("dtVigenciaInicial",JTDateTimeUtils.getDataAtual()),		
					Restrictions.or(
							Restrictions.isNull("dtVigenciaFinal"),
							Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.getDataAtual()))
					),			
			Restrictions.and(
					Restrictions.le("dtVigenciaInicial",JTDateTimeUtils.getDataAtual().plusDays(1)),		
					Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.getDataAtual().plusDays(1))
					)
			);

		return (findByDetachedCriteria(dc).size()>0);
	}

	public boolean verificaAtendimentoRestricoesServicoOperacaoVigente(Long idOperacaoServicoLocaliza, Long idMunicipioFilial, String tpOperacao, Long idServico, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("1");
		sql.addFrom("MunicipioFilial mf " +
					" left outer join mf.municipioFilialIntervCeps cep" +
					" left outer join mf.municipioFilialFilOrigems fil" +
					" left outer join mf.municipioFilialSegmentos seg" +
					" left outer join mf.municipioFilialUfOrigems uf" +
					" left outer join mf.municipioFilialCliOrigems cli" +
					" inner join mf.operacaoServicoLocalizas osl");

		sql.addFrom("MunicipioFilial mf2");

		sql.addCriteria("mf2.idMunicipioFilial", "=", idMunicipioFilial);
		sql.addJoin("mf.municipio.idMunicipio", "mf2.municipio.idMunicipio");
		if (idServico != null)
			sql.addCriteria("osl.servico.idServico","=", idServico);
		else 
			sql.addCustomCriteria("osl.servico.idServico is null");
		sql.addCriteria("osl.idOperacaoServicoLocaliza","!=", idOperacaoServicoLocaliza);
		if (tpOperacao != null && !"A".equals(tpOperacao)){
			sql.addCustomCriteria("(osl.tpOperacao = ? or osl.tpOperacao = 'A')", tpOperacao);
		}

		sql.addCustomCriteria("osl.blAtendimentoGeral = 'S'");		
		sql.addCustomCriteria("osl.dtVigenciaInicial <= ?", dtVigenciaInicial);
		sql.addCustomCriteria("osl.dtVigenciaFinal >= ?", JTDateTimeUtils.maxYmd(dtVigenciaFinal));

		sql.addCustomCriteria("mf.dtVigenciaInicial <= ?", dtVigenciaInicial);
		sql.addCustomCriteria("mf.dtVigenciaFinal >= ?", JTDateTimeUtils.maxYmd(dtVigenciaFinal));

		sql.addCustomCriteria("(seg.idMunicipioFilialSegmento is null " +
										"or exists (select 1 " +
										"			from  MunicipioFilialSegmento ms " +
										"			where ms.municipioFilial.idMunicipioFilial = mf2.idMunicipioFilial" +
										"				  and ms.segmentoMercado.idSegmentoMercado = seg.segmentoMercado.idSegmentoMercado" +
										"				  and ms.dtVigenciaInicial <= ? and ms.dtVigenciaFinal >= ?" +
									 "))", new Object[]{dtVigenciaInicial,JTDateTimeUtils.maxYmd(dtVigenciaFinal)});

		sql.addCustomCriteria("(fil.idMunicipioFilialFilOrigem is null " +
										"or exists (select 1 "+
										"			from  MunicipioFilialFilOrigem mff "+
										"			where mff.municipioFilial.idMunicipioFilial = mf2.idMunicipioFilial "+
										"				 and mff.filial.idFilial = fil.filial.idFilial "+
										"				 and mff.dtVigenciaInicial <= ? and mff.dtVigenciaFinal >= ?" +
									 "))", new Object[]{dtVigenciaInicial,JTDateTimeUtils.maxYmd(dtVigenciaFinal)});

		sql.addCustomCriteria("(cep.idMunicipioFilialIntervCep is null " +
										"or exists (select 1 "+
										"			from  MunicipioFilialIntervCep mi "+
										"			where mi.municipioFilial.idMunicipioFilial = mf2.idMunicipioFilial "+
										"				 and ((mi.nrCepInicial <= cep.nrCepInicial and mi.nrCepFinal >= cep.nrCepFinal)"+
										"					  or (mi.nrCepInicial between cep.nrCepInicial and cep.nrCepFinal or"+
										"				      mi.nrCepFinal between cep.nrCepInicial and cep.nrCepFinal))"+
										"				 and mi.dtVigenciaInicial <= ? and mi.dtVigenciaFinal >= ?" +
									"))", new Object[]{dtVigenciaInicial,JTDateTimeUtils.maxYmd(dtVigenciaFinal)});

		sql.addCustomCriteria("(uf.idMunicipioFilialUFOrigem is null " +
										"or exists (select 1 "+
										"            from MunicipioFilialUFOrigem mu "+
										"           where mu.municipioFilial.idMunicipioFilial = mf2.idMunicipioFilial "+
										"              and mu.unidadeFederativa.idUnidadeFederativa = uf.unidadeFederativa.idUnidadeFederativa "+
										"              and mu.dtVigenciaInicial <= ? and mu.dtVigenciaFinal >= ?" +
									 "))", new Object[]{dtVigenciaInicial,JTDateTimeUtils.maxYmd(dtVigenciaFinal)});

		sql.addCustomCriteria("(cli.idMunicipioFilialCliOrigem is null " +
							"or exists (select 1 "+
							"			from  MunicipioFilialCliOrigem mc "+
							"			where mc.municipioFilial.idMunicipioFilial = mf2.idMunicipioFilial "+
							"				 and mc.cliente.idCliente = cli.cliente.idCliente "+
							"				 and mc.dtVigenciaInicial <= ? and mc.dtVigenciaFinal >= ?" +
						"))", new Object[]{dtVigenciaInicial,JTDateTimeUtils.maxYmd(dtVigenciaFinal)});

		List result = getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
		
		return result.isEmpty();
	} 
	
	public List findOperacaoServicoLocalVigenteByMunFilial(Long idMunicipioFilial){
		StringBuffer hql = new StringBuffer()
			.append("select new Map( osl.idOperacaoServicoLocaliza as idOperacaoServicoLocaliza, ")
			.append("osl.blAtendimentoGeral as blAtendimentoGeral, ")
			.append("osl.blCobraTaxaFluvial as blCobraTaxaFluvial, ")
			.append("osl.blAceitaFreteFob as blAceitaFreteFob, ")
			.append("osl.tpOperacao as tpOperacao, ")
			.append("ser.dsServico as servico_dsServico, ")
			.append("ser.sgServico as servico_sgServico, ")			
			.append("tlm.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipio_dsTipoLocalizacaoMunicipio, ") 
			.append("osl.nrTempoColeta as nrTempoColeta, ")
			.append("osl.nrTempoEntrega as nrTempoEntrega, ")
			.append("osl.blDomingo as blDomingo, osl.blSegunda as blSegunda, osl.blTerca as blTerca, ")
			.append("osl.blQuarta as blQuarta, osl.blQuinta as blQuinta, osl.blSexta as blSexta, osl.blSabado as blSabado) ")
			.append("from "+OperacaoServicoLocaliza.class.getName()+" as osl ") 
			.append("left join osl.servico as ser ")
			.append("left join osl.tipoLocalizacaoMunicipio as tlm ")
			.append("where osl.municipioFilial.idMunicipioFilial= ? ")
			.append("and osl.dtVigenciaInicial <= ? ")
			.append("and osl.dtVigenciaFinal >= ? ")
			.append("order by osl.tpOperacao,"+OrderVarcharI18n.hqlOrder("ser.dsServico",LocaleContextHolder.getLocale()));

		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		List lista = getAdsmHibernateTemplate().find(hql.toString(),new Object[]{idMunicipioFilial,dataAtual,dataAtual});
		return lista;
	}	

	public List<OperacaoServicoLocaliza> findOperacaoServicoLocaliza(Long idMunicipio, Boolean blIndicadorColeta, Long idServico, YearMonthDay dtVigencia) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "osl");
		dc.createAlias("osl.municipioFilial", "mf");
		dc.setFetchMode("osl.municipioFilial", FetchMode.JOIN);
		dc.setFetchMode("osl.tipoLocalizacaoMunicipio", FetchMode.JOIN);
		dc.setFetchMode("osl.tipoLocalizacaoMunicipioFob", FetchMode.JOIN);

		dc.add(Restrictions.eq("mf.municipio.id", idMunicipio));
		dc.add(Restrictions.le("osl.dtVigenciaInicial", dtVigencia));
		dc.add(Restrictions.ge("osl.dtVigenciaFinal", dtVigencia));
		if(idServico != null) {
			dc.add(Restrictions.or(Restrictions.isNull("osl.servico.id"), Restrictions.eq("osl.servico.id", idServico)));
		}
		if(blIndicadorColeta) {
			dc.add(Restrictions.in("osl.tpOperacao", new Object[]{"A", "C"}));
		} else {
			dc.add(Restrictions.in("osl.tpOperacao", new Object[]{"A", "E"}));
		}
		dc.addOrder(Order.desc("osl.servico"));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Método que retorna a lista de OperacaoServicoLocaliza
	 * onde OperacaoServicoLocaliza.tpOperacao seja 'A' ou 'E' 
	 * o idMunicipio da municipioFilial relacionado a operacaoServicoLocaliza seja igual 
	 * idMunicipioEntrega passado como parametro
	 * a municipioFilial.idFilial igual ao idFilial passado como prametro 
	 * a data ocorrencia passada esteja entre as datas de vigencia do municipioFilial
	 * e municipioFilial.blPadraoMcd = true
	 * 
	 * @param idFilial
	 * @param idMunicipioEntrega
	 * @param dhOcorrencia
	 * @return
	 */
	public List<OperacaoServicoLocaliza> findOperacaoServicoLocalizaByIdFilialDestinoEIdMunicipioEntregaDoctoServico(Long idFilial, Long idMunicipioEntrega, DateTime dhOcorrencia, Servico servico) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("osl");
		sql.addFrom(OperacaoServicoLocaliza.class.getName()+" osl "
				+"INNER JOIN osl.municipioFilial mf");
		sql.addCriteria("mf.municipio.idMunicipio", "=", idMunicipioEntrega);
		sql.addCriteria("mf.filial.idFilial", "=", idFilial);
		sql.addCriteriaIn("osl.tpOperacao", new Object[]{"A", "E"});

		//LMS-5273
		//2.2. Das operações serviço localização retornadas no passo anterior, selecionar a que tenha o mesmo serviço do documento de serviço 
		if (servico != null) {
			sql.addCriteria("osl.servico", "=", servico);
		}else { //Se não existir nenhuma operação com o mesmo serviço, selecionar a operação que tenha o serviço nulo 
			sql.addCustomCriteria("osl.servico IS NULL");
		}
				
		sql.addCustomCriteria("? BETWEEN osl.dtVigenciaInicial AND osl.dtVigenciaFinal");
		sql.addCriteriaValue(dhOcorrencia.toYearMonthDay());
		sql.addCriteria("mf.blPadraoMcd", "=", Boolean.TRUE);
		
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

}