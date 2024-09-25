package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.BeneficiarioProprietario;
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
public class BeneficiarioProprietarioDAO extends BaseCrudDao<BeneficiarioProprietario, Long> {

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("beneficiario", FetchMode.JOIN);
		lazyFindById.put("beneficiario.pessoa", FetchMode.JOIN);
		lazyFindById.put("proprietario", FetchMode.JOIN);
		lazyFindById.put("proprietario.pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("beneficiario", FetchMode.JOIN);
		lazyFindPaginated.put("beneficiario.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("proprietario", FetchMode.JOIN);
		lazyFindPaginated.put("proprietario.pessoa", FetchMode.JOIN);
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("beneficiario", FetchMode.JOIN);
		lazyFindLookup.put("beneficiario.pessoa", FetchMode.JOIN);
	}

	public boolean verificaExisteVigencia(BeneficiarioProprietario beneficiarioProprietario){
		StringBuffer hql = new StringBuffer();
		ArrayList values = new ArrayList();

		hql.append("select count(*) ");
		hql.append("from BeneficiarioProprietario bp ");
		hql.append("where bp.proprietario.idProprietario = ? ");
		values.add(beneficiarioProprietario.getProprietario().getIdProprietario());

		if (beneficiarioProprietario.getIdBeneficiarioProprietario() != null) {
			hql.append("and bp.idBeneficiarioProprietario != ? ");
			values.add(beneficiarioProprietario.getIdBeneficiarioProprietario());
		}

		if (beneficiarioProprietario.getDtVigenciaFinal() != null) {
			values.add(beneficiarioProprietario.getDtVigenciaInicial());
			values.add(beneficiarioProprietario.getDtVigenciaFinal());
			hql.append("and not((bp.dtVigenciaFinal < ? ) or bp.dtVigenciaInicial > ?) ");
		} else {
			values.add(beneficiarioProprietario.getDtVigenciaInicial());
			hql.append("and not(bp.dtVigenciaFinal < ? ) ");
		}

		Long result = (Long)this.getAdsmHibernateTemplate().findUniqueResult(hql.toString(), values.toArray());
		return (result.intValue() > 0);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return BeneficiarioProprietario.class;
	}

	//verifica se as datas informadas em Dados da conta bancária do beneficiario estão no intervalo de vigencia do beneficiario - LMS-00052
	public boolean findBeneficiarioProprietarioByVigencias(Long idBeneficiario,YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("beneficiario.idBeneficiario",idBeneficiario));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal",dtVigenciaFinal));

		dc = JTVigenciaUtils.getDetachedVigencia(dc,dtVigenciaInicial,dtVigenciaFinal);

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc).size()==0;
	}

	/**
	 * Retorna BeneficiarioProprietario a partir da vigência.
	 * @param idBeneficiario
	 * @param idProprietario
	 * @return
	 */
	public List findBeneficiarioProprietario(
		Long idBeneficiario,
		Long idProprietario,
		YearMonthDay dtVigenciaInicial,
		YearMonthDay dtVigenciaFinal
	) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "bp");
		dc.createAlias("bp.proprietario", "p");
		dc.createAlias("bp.beneficiario", "b");

		if (idProprietario != null)
			dc.add(Restrictions.eq("p.id", idProprietario));
		if (idBeneficiario != null)
			dc.add(Restrictions.eq("b.id", idBeneficiario));

		dc.add(Restrictions.le("bp.dtVigenciaInicial", dtVigenciaInicial));
		dc.add(Restrictions.ge("bp.dtVigenciaFinal", dtVigenciaFinal));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * 
	 * @param idProprietario
	 * @return
	 */
	public List findBeneficiarioByIdProprietario(Long idProprietario){
		StringBuffer sql = new StringBuffer()
		.append("select new Map(pessoaBenef.nrIdentificacao as nrIdentificacaoBeneficiario, ")
		.append("pessoaBenef.tpIdentificacao as tpIdentificacaoBeneficiario, ")
		.append("pessoaBenef.idPessoa as idPessoa, ")
		.append("pessoaBenef.nmPessoa as nmPessoaBeneficiario, ")
		.append("cb.nrContaBancaria as nrContaBancaria, ")
		.append("cb.dvContaBancaria as dvContaBancaria, ")
		.append("ab.nrAgenciaBancaria as nrAgenciaBancaria, ")
		.append("ab.nmAgenciaBancaria as nmAgenciaBancaria, ")
		.append("banco.nrBanco as nrBanco, ")
		.append("banco.nmBanco as nmBanco ")
		.append(") ")
		.append("from ")
		.append(getPersistentClass().getName()).append(" bp ")
		.append("inner join bp.beneficiario benef ")
		.append("inner join benef.pessoa pessoaBenef ")
		.append("left join pessoaBenef.contaBancarias cb ")
		.append("left join cb.agenciaBancaria ab ")
		.append("left join ab.banco banco ")
		.append("where ")
		.append("bp.proprietario.id = ? ")
		.append("and ? between bp.dtVigenciaInicial and bp.dtVigenciaFinal ")
		.append("and ? between cb.dtVigenciaInicial and cb.dtVigenciaFinal ");

		List param = new ArrayList();
		param.add(idProprietario);
		param.add(JTDateTimeUtils.getDataAtual());
		param.add(JTDateTimeUtils.getDataAtual());

		List retorno = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		return retorno;
	}

	//###################### *** INTEGRAÇÃO *** ################################//
	/**
	 * find para buscar o BeneficiarioProprietario, com restrição em idProprietario e dtVigenciaFinal 
	 * Método solicitado pela equipe de integracao
	 * @param idProprietario
	 * @param dtAtual
	 * @return BeneficiarioProprietario
	 */
	public BeneficiarioProprietario findBeneficiarioProprietarioByProprietarioVigenciaFinal(Long idProprietario, YearMonthDay dtAtual){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"BP");
		dc.createAlias("BP.proprietario","P");

		if (idProprietario != null)
			dc.add(Restrictions.eq("P.idProprietario",idProprietario));
		if (dtAtual != null)
		dc.add(Restrictions.ge("dtVigenciaFinal",dtAtual));

		List lstBeneficiarioProprietario = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (lstBeneficiarioProprietario.size() == 1) {
			return (BeneficiarioProprietario)lstBeneficiarioProprietario.get(0);
		} else {
			return null;
		}
	}

}