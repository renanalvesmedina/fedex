package com.mercurio.lms.sim.model.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.sim.model.DocumentoServicoRetirada;
import com.mercurio.lms.sim.model.RegistroPriorizacaoDocto;
import com.mercurio.lms.sim.model.RegistroPriorizacaoEmbarq;
import com.mercurio.lms.sim.model.SolicitacaoRetirada;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegistroPriorizacaoEmbarqDAO extends BaseCrudDao<RegistroPriorizacaoEmbarq, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return RegistroPriorizacaoEmbarq.class;
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("remetente",FetchMode.JOIN);
		fetchModes.put("remetente.pessoa",FetchMode.JOIN);
		fetchModes.put("destinatario",FetchMode.JOIN);
		fetchModes.put("destinatario.pessoa",FetchMode.JOIN);
		fetchModes.put("filial",FetchMode.JOIN);
		fetchModes.put("filial.pessoa",FetchMode.JOIN);
		fetchModes.put("usuarioCriacao",FetchMode.JOIN);
		fetchModes.put("usuarioCancelamento",FetchMode.JOIN);
		fetchModes.put("registroPriorizacaoDocto",FetchMode.SELECT);
	}

	public int removeByIds(List ids) {
		for(Iterator<Long> i = ids.iterator(); i.hasNext();) {
			Long id = (Long)i.next();
			removeById(id);
		}
		return ids.size();
	}

	public void cancelaRegistro(Long idRegistroPriorizacaoEmbarque, String obCancelamento) {
		RegistroPriorizacaoEmbarq bean = (RegistroPriorizacaoEmbarq)getAdsmHibernateTemplate().load(getPersistentClass(),idRegistroPriorizacaoEmbarque);
		bean.setObCancelamanto(obCancelamento);
		bean.setUsuarioCancelamento(SessionUtils.getUsuarioLogado());
		bean.setDhCancelamento(JTDateTimeUtils.getDataHoraAtual());
		getAdsmHibernateTemplate().update(bean);
		getAdsmHibernateTemplate().flush();

		List<DoctoServico> doctoServicos = findDoctoServicoByIdRegistro(idRegistroPriorizacaoEmbarque);
		for(DoctoServico docto : doctoServicos) {
			docto.setBlPrioridadeCarregamento(Boolean.FALSE);
			getAdsmHibernateTemplate().update(docto);
		}
	}

	public SolicitacaoRetirada findSolicitacaoRetiradaById(Long idSolicitacao) {
		return (SolicitacaoRetirada)DetachedCriteria.forClass(SolicitacaoRetirada.class,"SR")
			.createAlias("SR.documentoServicoRetiradas","DSR")
			.add(Restrictions.eq("SR.id",idSolicitacao)).getExecutableCriteria(getSession()).uniqueResult();
	}

	public List<DocumentoServicoRetirada> findDocumentoServicoRetirada(Long idDocto,String tpSituacao,Boolean isJoinManifesto) {
		DetachedCriteria dc = DetachedCriteria.forClass(DocumentoServicoRetirada.class,"DSR")
			.createAlias("DSR.doctoServico","DS")
			.add(Restrictions.eq("DS.id",idDocto));

		if (tpSituacao != null)
			dc.createAlias("DSR.solicitacaoRetirada","SR").add(Restrictions.eq("SR.tpSituacao",tpSituacao));
		if (isJoinManifesto != null && isJoinManifesto.booleanValue())
			dc.createAlias("DSR.solicitacaoRetirada","SR").createAlias("SR.manifestos","M");
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public RegistroPriorizacaoEmbarq updateBean(RegistroPriorizacaoEmbarq beanView) {
		if (beanView.getIdRegistroPriorizacaoEmbarq() != null) {
			RegistroPriorizacaoEmbarq bean = (RegistroPriorizacaoEmbarq)getAdsmHibernateTemplate().load(getPersistentClass(),beanView.getIdRegistroPriorizacaoEmbarq());
			bean.setFilial(bean.getFilial());
			bean.setUsuarioCriacao(beanView.getUsuarioCriacao());
			bean.setRemetente(beanView.getRemetente());
			bean.setDestinatario(beanView.getDestinatario());
			bean.setNmSolicitante(beanView.getNmSolicitante());
			bean.setObPriorizacao(beanView.getObPriorizacao());
			bean.setDhRegistroEmbarque(beanView.getDhRegistroEmbarque());
			bean.getRegistroPriorizacaoDocto().clear();
			updateDoctoServicoBlPrioridade(beanView.getRegistroPriorizacaoDocto());
			if (beanView.getRegistroPriorizacaoDocto() != null)
				bean.getRegistroPriorizacaoDocto().addAll(beanView.getRegistroPriorizacaoDocto());

			return bean;
		}
		updateDoctoServicoBlPrioridade(beanView.getRegistroPriorizacaoDocto());
		return beanView;
	}

	private void updateDoctoServicoBlPrioridade(List<RegistroPriorizacaoDocto> doctos) {
		for(RegistroPriorizacaoDocto registroPriorizacaoDocto : doctos) {
			DoctoServico docto = (DoctoServico)getAdsmHibernateTemplate().load(DoctoServico.class, registroPriorizacaoDocto.getDoctoServico().getIdDoctoServico());
			docto.setBlPrioridadeCarregamento(Boolean.TRUE);
			getAdsmHibernateTemplate().update(docto);
			getAdsmHibernateTemplate().flush();
			getAdsmHibernateTemplate().evict(docto);
		}
	}

	public List<NotaFiscalConhecimento> findNotaFiscaisByIdConhecimento(Long idConhecimento) {
		DetachedCriteria dc = DetachedCriteria.forClass(NotaFiscalConhecimento.class,"NFC")
			.createAlias("NFC.conhecimento","C")
			.add(Restrictions.eq("C.id",idConhecimento));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public Integer getRowCountDoctoServiceFaltaEntreguar(Long idRegistroPriorizacaoEmbarque) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(new StringBuffer(RegistroPriorizacaoDocto.class.getName()).append(" AS RPD ")
			.append("INNER JOIN RPD.doctoServico AS DS ")
			.append("INNER JOIN DS.localizacaoMercadoria AS LM ").toString());

		hql.addCriteria("LM.cdLocalizacaoMercadoria","<>",Short.valueOf("1"));
		hql.addCriteria("RPD.registroPriorizacaoEmbarq.id","=",idRegistroPriorizacaoEmbarque);

		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
	}

	public List<NotaFiscalConhecimento> findNotasFiscaisByIdDocto(Long[] idDocto) {
		DetachedCriteria dc = DetachedCriteria.forClass(NotaFiscalConhecimento.class, "nfc");
		dc.createAlias("nfc.conhecimento", "c");
		dc.add(Restrictions.in("c.id", idDocto));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public Integer getRowCountDoctoServicoManifesto(Long idDoctoServico) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(new StringBuffer(DoctoServico.class.getName()).append(" AS DS ")
			.append("INNER JOIN DS.manifestoEntregaDocumentos AS MED ")
			.append("INNER JOIN MED.manifestoEntrega AS ME ")
			.append("INNER JOIN ME.manifesto AS M ").toString());

		hql.addCustomCriteria("M.tpStatusManifesto not in (?,?,?,?,?)");
		hql.addCriteriaValue("CA");
		hql.addCriteriaValue("FE"); 
		hql.addCriteriaValue("AD");
		hql.addCriteriaValue("DC");
		hql.addCriteriaValue("ED");

		hql.addCriteria("DS.id","=",idDoctoServico);

		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
	}

	private List<DoctoServico> findDoctoServicoByIdRegistro(Long idRegistro) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("DS");
		hql.addFrom(new StringBuffer(RegistroPriorizacaoDocto.class.getName()).append(" AS RPD ")
			.append("INNER JOIN RPD.registroPriorizacaoEmbarq AS RPE ")
			.append("INNER JOIN RPD.doctoServico AS DS ").toString());
		hql.addCriteria("RPE.id","=",idRegistro);
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public void removeById(Long id) {
		RegistroPriorizacaoEmbarq bean = (RegistroPriorizacaoEmbarq)getAdsmHibernateTemplate().get(getPersistentClass(),id);
		bean.getRegistroPriorizacaoDocto().clear();
		getAdsmHibernateTemplate().delete(bean);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = createSqlTemplate(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),hql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate hql = createSqlTemplate(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
	}

	public List findLookup(TypedFlatMap criteria) {
		SqlTemplate hql = createSqlTemplate(criteria);
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(new StringBuffer(getPersistentClass().getName()).append(" AS RPE ")
			.append("INNER JOIN FETCH RPE.filial AS F ")
			.append("LEFT JOIN FETCH RPE.remetente AS R ")
			.append("LEFT JOIN FETCH R.pessoa AS RP ")
			.append("LEFT JOIN FETCH RPE.destinatario AS D ")
			.append("LEFT JOIN FETCH D.pessoa AS DP ").toString());

		hql.addCriteria("F.id","=",criteria.getLong("filial.idFilial"));
		hql.addCriteria("R.id","=",criteria.getLong("remetente.idCliente"));
		hql.addCriteria("D.id","=",criteria.getLong("destinatario.idCliente"));

		if (criteria.getYearMonthDay("dtRegistroInicial") != null)
			hql.addCriteria("RPE.dhRegistro.value",">=",JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dtRegistroInicial")));

		if (criteria.getYearMonthDay("dtRegistroFinal") != null)
			hql.addCriteria("RPE.dhRegistro.value","<",JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dtRegistroFinal").plusDays(1)));

		if (criteria.getYearMonthDay("dhRegistroEmbarqueInicial") != null)
			hql.addCriteria("RPE.dhRegistroEmbarque.value",">=",JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dhRegistroEmbarqueInicial")));

		if (criteria.getYearMonthDay("dhRegistroEmbarqueFinal") != null)
			hql.addCriteria("RPE.dhRegistroEmbarque.value","<",JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dhRegistroEmbarqueFinal").plusDays(1)));

		if (criteria.getLong("idDoctoServico") != null || criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial") != null ||
				StringUtils.isNotBlank(criteria.getString("doctoServico.tpDocumentoServico"))) { 

			StringBuffer subSelect = new StringBuffer("EXISTS (SELECT RPD.id FROM ").append(RegistroPriorizacaoDocto.class.getName()).append(" AS RPD, ")
			.append(RegistroPriorizacaoEmbarq.class.getName()).append(" AS RPE2, ")
			.append(DoctoServico.class.getName()).append(" AS DS ")
			.append("WHERE RPE2.id = RPE.id ")
			.append("AND RPD.registroPriorizacaoEmbarq.id = RPE2.id ");

			List<Object> criterias = new ArrayList<Object>();

			if (criteria.getLong("idDoctoServico") != null) {
				subSelect.append("AND DS.id = ? ");
				criterias.add(criteria.getLong("idDoctoServico"));
			}
			if (criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial") != null) {
				subSelect.append("AND DS.filialByIdFilialOrigem.id = ? ");
				criterias.add(criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial"));
			}
			if (StringUtils.isNotBlank(criteria.getString("doctoServico.tpDocumentoServico"))) {
				subSelect.append("AND DS.tpDocumentoServico = ? ");
				criterias.add(criteria.getString("doctoServico.tpDocumentoServico"));
			}

			subSelect.append("AND RPD.doctoServico.id = DS.id)");

			hql.addCustomCriteria(subSelect.toString());

			for(Iterator<Object> i = criterias.iterator(); i.hasNext();)
				hql.addCriteriaValue(i.next());
		}

		if (criteria.getInteger("notaFiscalConhecimento.nrNotaFiscal") != null) { 

			hql.addCustomCriteria(new StringBuffer("EXISTS (SELECT RPD.id FROM ").append(RegistroPriorizacaoDocto.class.getName()).append(" AS RPD, ")
			.append(RegistroPriorizacaoEmbarq.class.getName()).append(" AS RPE2, ")
			.append(DoctoServico.class.getName()).append(" AS DS, ")
			.append(NotaFiscalConhecimento.class.getName()).append(" AS NFC ")
			.append("WHERE RPE2.id = RPE.id ")
			.append("AND RPD.registroPriorizacaoEmbarq.id = RPE2.id ")
			.append("AND NFC.conhecimento.id = DS.id ")
			.append("AND NFC.nrNotaFiscal = ? ")
			.append("AND RPD.doctoServico.id = DS.id)").toString());

			hql.addCriteriaValue(criteria.getInteger("notaFiscalConhecimento.nrNotaFiscal"));
		}

		if (StringUtils.isNotBlank(criteria.getString("isSomenteEntregasNaoEfetuadas"))) {
			hql.addCustomCriteria(new StringBuffer(((criteria.getString("isSomenteEntregasNaoEfetuadas").equalsIgnoreCase("S")) ? "NOT" : "")).append(" EXISTS (SELECT RPD.id FROM ")
			.append(RegistroPriorizacaoDocto.class.getName()).append(" AS RPD, ")
			.append(RegistroPriorizacaoEmbarq.class.getName()).append(" AS RPE2, ")
			.append(DoctoServico.class.getName()).append(" AS DS, ")
			.append(ManifestoEntregaDocumento.class.getName()).append(" AS MED, ")
			.append(OcorrenciaEntrega.class.getName()).append(" AS OE ")
			.append("where OE.tpOcorrencia = ? AND RPE2.id = RPE.id AND ")
			.append("RPD.doctoServico.id = DS.id AND ")
			.append("RPD.registroPriorizacaoEmbarq.id = RPE2.id AND ")
			.append("MED.doctoServico.id = DS.id AND ")
			.append("MED.ocorrenciaEntrega.id = OE.id )").toString());
			hql.addCriteriaValue("F");
		}

		hql.addOrderBy("RP.nmPessoa");
		hql.addOrderBy("DP.nmPessoa");
		hql.addOrderBy("RPE.dhRegistro.value");

		return hql;
	}
}
