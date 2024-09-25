package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.model.Filial;

@Entity
@Table(name = "CREDITO_BANCARIO")
@SequenceGenerator(name = "CREDITO_BANCARIO_SEQ", sequenceName = "CREDITO_BANCARIO_SQ", allocationSize=1)
public class CreditoBancarioEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String TP_SITUACAO_DIGITADO = "D";
	public static final String TP_SITUACAO_LIBERADO = "L";
	
	public CreditoBancarioEntity() {
	}

	public CreditoBancarioEntity(Long idCreditoBancario, Filial filial, Banco banco,
			UsuarioLMS usuario, YearMonthDay dtCredito, BigDecimal vlCredito,
			DomainValue tpModalidade, DomainValue tpOrigem,  DomainValue tpSituacao, 
			String dsCpfCnpj, String dsNomeRazaoSocial, String dsBoleto, 
			String obCreditoBancario, DateTime dhAlteracao) {
		
		super();
		this.idCreditoBancario = idCreditoBancario;
		this.filial = filial;
		this.banco = banco;
		this.usuario = usuario;
		this.dtCredito = dtCredito;
		this.vlCredito = vlCredito;
		this.tpModalidade = tpModalidade;
		this.tpOrigem = tpOrigem;
		this.tpSituacao = tpSituacao;
		this.dsCpfCnpj = dsCpfCnpj;
		this.dsNomeRazaoSocial = dsNomeRazaoSocial;
		this.dsBoleto = dsBoleto;
		this.obCreditoBancario = obCreditoBancario;
		this.dhAlteracao = dhAlteracao;
	}

	public CreditoBancarioEntity(Long idCreditoBancario, Filial filial, Banco banco,
			UsuarioLMS usuario, YearMonthDay dtCredito, BigDecimal vlCredito,
			DomainValue tpModalidade, DomainValue tpOrigem,  DomainValue tpClassificacao,
			DomainValue tpSituacao, String dsCpfCnpj, String dsNomeRazaoSocial,
			String dsBoleto, String obCreditoBancario, DateTime dhAlteracao) {
		
		super();
		this.idCreditoBancario = idCreditoBancario;
		this.filial = filial;
		this.banco = banco;
		this.usuario = usuario;
		this.dtCredito = dtCredito;
		this.vlCredito = vlCredito;
		this.tpModalidade = tpModalidade;
		this.tpOrigem = tpOrigem;
		this.tpClassificacao = tpClassificacao;
		this.tpSituacao = tpSituacao;
		this.dsCpfCnpj = dsCpfCnpj;
		this.dsNomeRazaoSocial = dsNomeRazaoSocial;
		this.dsBoleto = dsBoleto;
		this.obCreditoBancario = obCreditoBancario;
		this.dhAlteracao = dhAlteracao;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREDITO_BANCARIO_SEQ")
	@Column(name = "ID_CREDITO_BANCARIO", nullable = false)
	private Long idCreditoBancario;
	
	@ManyToOne
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	private Filial filial;
	
	@ManyToOne
	@JoinColumn(name = "ID_BANCO", nullable = false)
	private Banco banco;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuario;
	
	@Column(name = "DT_CREDITO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtCredito;
	
	@Column(name = "VL_CREDITO", nullable = false)
	private BigDecimal vlCredito;
	
	@Column(name = "TP_MODALIDADE")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_MODALIDADE_CRED_BANC") })
	private DomainValue tpModalidade;
	
	@Column(name = "TP_ORIGEM")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_ORIGEM_CRED_BANC") })
	private DomainValue tpOrigem;
	
	@Column(name = "TP_CLASSIFICACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_CLASSIFICACAO_CRED_BANC") })
	private DomainValue tpClassificacao;

	@Column(name = "TP_SITUACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_SITUACAO_CRED_BANC") })
	private DomainValue tpSituacao;
	
	@Column(name = "DS_CPF_CNPJ", nullable = true)
	private String dsCpfCnpj;
	
	@Column(name = "DS_NOME_RAZAO_SOCIAL", nullable = true)
	private String dsNomeRazaoSocial;
	
	@Column(name = "DS_BOLETO", nullable = true)
	private String dsBoleto;
	
	@Column(name = "OB_CREDITO_BANCARIO", nullable = true)
	private String obCreditoBancario;
	
	@Columns(columns = { @Column(name = "DH_ALTERACAO", nullable = true), @Column(name = "DH_ALTERACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;

	@Transient
	private BigDecimal total;

	@Transient
	private BigDecimal vlSomaSaldo;

	public Long getIdCreditoBancario() {
		return idCreditoBancario;
	}

	public void setIdCreditoBancario(Long idCreditoBancario) {
		this.idCreditoBancario = idCreditoBancario;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public YearMonthDay getDtCredito() {
		return dtCredito;
	}

	public void setDtCredito(YearMonthDay dtCredito) {
		this.dtCredito = dtCredito;
	}

	public BigDecimal getVlCredito() {
		return vlCredito;
	}

	public void setVlCredito(BigDecimal vlCredito) {
		this.vlCredito = vlCredito;
	}

	public DomainValue getTpModalidade() {
		return tpModalidade;
	}

	public void setTpModalidade(DomainValue tpModalidade) {
		this.tpModalidade = tpModalidade;
	}

	public DomainValue getTpOrigem() {
		return tpOrigem;
	}

	public void setTpOrigem(DomainValue tpOrigem) {
		this.tpOrigem = tpOrigem;
	}
	
	public DomainValue getTpClassificacao() {
		return tpClassificacao;
	}

	public void setTpClassificacao(DomainValue tpClassificacao) {
		this.tpClassificacao = tpClassificacao;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	
	public void setTpSituacao(String tpSituacao) {
		this.tpSituacao = new DomainValue(tpSituacao);
	}

	public String getDsCpfCnpj() {
		return dsCpfCnpj;
	}

	public void setDsCpfCnpj(String dsCpfCnpj) {
		this.dsCpfCnpj = dsCpfCnpj;
	}

	public String getDsNomeRazaoSocial() {
		return dsNomeRazaoSocial;
	}

	public void setDsNomeRazaoSocial(String dsNomeRazaoSocial) {
		this.dsNomeRazaoSocial = dsNomeRazaoSocial;
	}

	public String getDsBoleto() {
		return dsBoleto;
	}

	public void setDsBoleto(String dsBoleto) {
		this.dsBoleto = dsBoleto;
	}

	public String getObCreditoBancario() {
		return obCreditoBancario;
	}

	public void setObCreditoBancario(String obCreditoBancario) {
		this.obCreditoBancario = obCreditoBancario;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idCreditoBancario == null) ? 0 : idCreditoBancario
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditoBancarioEntity other = (CreditoBancarioEntity) obj;
		if (idCreditoBancario == null) {
			if (other.idCreditoBancario != null)
				return false;
		} else if (!idCreditoBancario.equals(other.idCreditoBancario))
			return false;
		return true;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("idCreditoBancario", idCreditoBancario);
		map.put("filial", filial.getSgFilial());
		map.put("banco", banco.getNmBanco());
		map.put("usuario", usuario.getUsuarioADSM().getNmUsuario());
		map.put("dtCredito", dtCredito);
		map.put("vlCredito", NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(vlCredito).substring(3));
		map.put("vlSaldo", NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(getSaldo()).substring(3));
		map.put("tpModalidade", tpModalidade);
		map.put("tpOrigem", tpOrigem);
		map.put("tpClassificacao", tpClassificacao);
		map.put("tpSituacao", tpSituacao);
		map.put("dsCpfCnpj", dsCpfCnpj);
		map.put("dsNomeRazaoSocial", dsNomeRazaoSocial);
		map.put("dsBoleto", dsBoleto);
		map.put("obCreditoBancario", obCreditoBancario);
		map.put("dhAlteracao", dhAlteracao);

		return map;
	}
	
	public BigDecimal getSaldo() {
		if(total!=null){
			return vlCredito.subtract(total);
		}
		return vlCredito;
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getTotal() {
		return total;
	}

	public BigDecimal getVlSomaSaldo() {
		return vlSomaSaldo;
}

	public void setVlSomaSaldo(BigDecimal vlSomaSaldo) {
		this.vlSomaSaldo = vlSomaSaldo;
	}

}
