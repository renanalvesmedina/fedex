package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.MunicipioFilialIntervCep;
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
public class MunicipioFilialIntervCepDAO extends BaseCrudDao<MunicipioFilialIntervCep, Long> {

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("municipioFilial", FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio", FetchMode.JOIN);
	}
	
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("municipioFilial", FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial", FetchMode.JOIN);
		fetchModes.put("municipioFilial.filial.pessoa", FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio", FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa", FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.unidadeFederativa.pais", FetchMode.JOIN);
		fetchModes.put("municipioFilial.municipio.municipioDistrito", FetchMode.JOIN);
	}
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return MunicipioFilialIntervCep.class;
	}

	/**
	 * Verifica se o intervalo de cep ja nao esta cadastrado em qualque outro atendimento vigente
	 * @param mc
	 * @return
	 */
	public boolean verificaIntervaloCepAtendido(MunicipioFilialIntervCep mc){

		DetachedCriteria dc = createDetachedCriteria();

		dc.setProjection(Projections.rowCount());
		if (mc.getIdMunicipioFilialIntervCep() != null){
			dc.add(Restrictions.ne("idMunicipioFilialIntervCep",mc.getIdMunicipioFilialIntervCep()));
		}

		dc = JTVigenciaUtils.getDetachedVigencia(dc, mc.getDtVigenciaInicial(), mc.getDtVigenciaFinal());

		dc.add(Restrictions.le("nrCepInicial",mc.getNrCepFinal()));
		dc.add(Restrictions.ge("nrCepFinal",mc.getNrCepInicial()));

		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}

	/**
	 * Consulta registros vigentes para o municipio X Filial informado
	 * @param idMunicipioFilial
	 * @param dtVigenciaFinal 
	 * @param dtVigenciaInicial 
	 * @return
	 */
	public List findIntervCepVigenteByMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();

		dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
		JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);

		return findByDetachedCriteria(dc);
	}

	/**
	 * Verifica se existe registro para o atendimento e cep informados, dentro da vigencia informada
	 * @param idMunicipioFilial
	 * @param cep
	 * @param dtVigencia
	 * @return
	 */
	public boolean verificaExisteMunicipioFilialCep(Long idMunicipioFilial, String cep, YearMonthDay dtVigencia){
		DetachedCriteria dc = createDetachedCriteria();

		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("municipioFilial.idMunicipioFilial", idMunicipioFilial));
		dc.add(Restrictions.le("nrCepInicial", cep));
		dc.add(Restrictions.ge("nrCepFinal", cep));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigencia));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigencia)));
		
		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}

	public Integer findRowCountMunicipioFilialIntervaloCep(Long idMunicipioFilial, YearMonthDay dtVigencia) {
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

	//busca todos os ceps vigentes de um municipio atendido 
	public List findCepAtendidoByMunicipioFilial(Long idMunicipioFilial) {
		StringBuffer hql = new StringBuffer()
			.append("select new Map(mfic.nrCepInicial as nrCepInicial, ") 
			.append("mfic.nrCepFinal as nrCepFinal) ")
			.append("from "+MunicipioFilialIntervCep.class.getName()+" as mfic ")
			.append("where mfic.municipioFilial.idMunicipioFilial = ? ")
			.append("and mfic.dtVigenciaInicial <= ? ")
			.append("and mfic.dtVigenciaFinal >= ?");

		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();		
		List lista = getAdsmHibernateTemplate().find(hql.toString(),new Object[]{idMunicipioFilial,dataAtual,dataAtual});
		return lista; 
	}
}