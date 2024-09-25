package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.MunicipioFilialCliOrigem;
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
public class MunicipioFilialCliOrigemDAO extends BaseCrudDao<MunicipioFilialCliOrigem, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return MunicipioFilialCliOrigem.class;
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("municipioFilial",FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial",FetchMode.JOIN);
		fetchModes.put("cliente",FetchMode.JOIN);
		fetchModes.put("cliente.pessoa",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa.pais",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.municipioDistrito",FetchMode.JOIN);
		fetchModes.put("cliente",FetchMode.JOIN);
	}
	
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("municipioFilial",FetchMode.JOIN);
		fetchModes.put("cliente",FetchMode.JOIN);
		fetchModes.put("cliente.pessoa",FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial",FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial.pessoa",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa.pais",FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.municipioDistrito",FetchMode.JOIN);
	}
	
	protected void initFindLookupLazyProperties(Map fetchModes) {
		fetchModes.put("cliente",FetchMode.JOIN);
	}
	
	public boolean verificaRemetentesAtendidos(MunicipioFilialCliOrigem municipioFilialCliOrigem){
		DetachedCriteria dc = createDetachedCriteria();
		if(municipioFilialCliOrigem.getIdMunicipioFilialCliOrigem() != null){
			dc.add(Restrictions.ne("idMunicipioFilialCliOrigem",municipioFilialCliOrigem.getIdMunicipioFilialCliOrigem()));
		}

		dc = JTVigenciaUtils.getDetachedVigencia(dc, municipioFilialCliOrigem.getDtVigenciaInicial(), municipioFilialCliOrigem.getDtVigenciaFinal());

		dc.add(Restrictions.eq("cliente.idCliente",municipioFilialCliOrigem.getCliente().getIdCliente()));

		DetachedCriteria dcMunicipioFilial = dc.createCriteria("municipioFilial");
		dcMunicipioFilial.add(Restrictions.eq("idMunicipioFilial",municipioFilialCliOrigem.getMunicipioFilial().getIdMunicipioFilial()));

		return !(findByDetachedCriteria(dcMunicipioFilial).size() > 0);
	}
	
	/**
	 * Consulta registros vigentes para o municipio X Filial informado
	 * @param idMunicipioFilial
	 * @param dtVigenciaFinal 
	 * @param dtVigenciaInicial 
	 * @return
	 */
	public List findCliOrigemVigenteByMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();

		dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
		JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);

		return findByDetachedCriteria(dc);
	}

	/**
	 * Verifica se existem registros para o atendimento e cliente informados, dentro da vigencia informada
	 * @param idMunicipioFilial
	 * @param idCliente
	 * @param dtVigencia
	 * @return
	 */
	public boolean verificaExisteMunicipioFilialCliente(Long idMunicipioFilial, Long idCliente, YearMonthDay dtVigencia) {
		DetachedCriteria dc = createDetachedCriteria();

		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
		dc.add(Restrictions.eq("cliente.idCliente", idCliente));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigencia)));

		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}

	public Integer findRowCountMunicipioFilialClienteOrigem(Long idMunicipioFilial, YearMonthDay dtVigencia) {
		DetachedCriteria dc = createDetachedCriteria();

		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
		if(dtVigencia != null) {
			dc.add(Restrictions.le("dtVigenciaInicial", dtVigencia));
			dc.add(Restrictions.gt("dtVigenciaFinal", JTDateTimeUtils.maxYmd(dtVigencia)));
		}
		return (Integer)findByDetachedCriteria(dc).get(0);
	}

	public boolean verificaMunicipioVigenciaFutura(Long idMunicipio, Long idFilial, YearMonthDay dtVigencia){
		DetachedCriteria dc = createDetachedCriteria();

		dc.setProjection(Projections.rowCount());
		dc.createAlias("municipioFilial", "mf");
		dc.add(Restrictions.eq("mf.municipio.id", idMunicipio));
		dc.add(Restrictions.eq("mf.filial.id", idFilial));
		dc.add(Restrictions.ge("dtVigenciaFinal", JTDateTimeUtils.maxYmd(dtVigencia)));

		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}

	// busca todos clientes vigentes de um municipio atendido 
	public List findCliAtendidosByMunicipioFilial(Long idMunicipioFilial) {
		StringBuffer hql = new StringBuffer()
			.append("select new Map(pes.nmPessoa as nmPessoa, pes.nrIdentificacao as nrIdentificacao, pes.tpIdentificacao as tpIdentificacao) ")
			.append("from "+MunicipioFilialCliOrigem.class.getName()+" as mfco ")
			.append("left outer join mfco.cliente as cli ")
			.append("left outer join cli.pessoa as pes ")
			.append("where mfco.municipioFilial.idMunicipioFilial = ? ")
			.append("and mfco.dtVigenciaInicial <= ? ")
			.append("and mfco.dtVigenciaFinal >= ?");

		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();		
		List lista = getAdsmHibernateTemplate().find(hql.toString(),new Object[]{idMunicipioFilial,dataAtual,dataAtual});
		return lista; 
	}

}