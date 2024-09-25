package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

/** @author LMS Custom Hibernate CodeGenerator */
public class Pessoa implements Serializable {
	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPessoa;

    /** persistent field */
    private DateTime dhInclusao;

    /** persistent field */
    private String nmPessoa;

    /** persistent field */
    private String nmFantasia;

    /** nullable persistent field */
    private YearMonthDay dtEmissaoRg;

    /** nullable persistent field */
    private DomainValue tpIdentificacao;

    /** nullable persistent field */
    private String nrIdentificacao;

    /** nullable persistent field */
    private DomainValue tpPessoa;

    /** nullable persistent field */
    private String dsEmail;

    /** nullable persistent field */
    private String nrRg;

    /** nullable persistent field */
    private String dsOrgaoEmissorRg;
    
    /** atributo para devolver à lookup apenas um número de inscrição estadual */
    private String nrInscricaoEstadual;   
    
    /** nullable persistent field */
    private String nrInscricaoMunicipal;      
    
    private Boolean blAtualizacaoCountasse;
    
    /** nullable persistent field */
    private Long nrCnae;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa;

    /** persistent field */
    private List checklistMeioTransportesByIdSegundoMotorista;

    /** persistent field */
    private List checklistMeioTransportesByIdPrimeiroMotorista;

    /** persistent field */
    private List parametroClientes;

    /** persistent field */
    private List contatosByIdPessoaIndicacao;

    /** persistent field */
    private List contatosByIdPessoaContatado;

    /** persistent field */
    private List inscricaoEstaduais;

    /** persistent field */
    private List integranteEquipes;

    /** persistent field */
    private List reciboIndenizacoesByIdFavorecido;

    /** persistent field */
    private List reciboIndenizacoesByIdBeneficiario;

    /** persistent field */
    private List integranteEqOperacs;

    /** persistent field */
    private List simulacoes;

    /** persistent field */
    private List contaBancarias;

    /** persistent field */
    private List telefoneEnderecos;

    /** persistent field */
    private List enderecoPessoas;

    /** persistent field */
    private List impostoCalculados;

    /** persistent field */
    private List mdasByIdRemetente;

    /** persistent field */
    private List mdasByIdDestinatario;

    /** persistent field */
    private List mdasByIdConsignatario;

    /** persistent field */
    private List cotacoes;
    
    private List volContatos;
    
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaExpedicaoRg;
        
	@ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolContatos.class)     
	public List getVolContatos() {
		return this.volContatos;
	}

	public void setVolContatos(List volContatos) {
		this.volContatos = volContatos;
	}

	public Long getIdPessoa() {
        return this.idPessoa;
    }

    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public DateTime getDhInclusao() {
        return this.dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

    public String getNmPessoa() {
        return this.nmPessoa;
    }

    public void setNmPessoa(String nmPessoa) {
        this.nmPessoa = nmPessoa;
    }

    public YearMonthDay getDtEmissaoRg() {
        return this.dtEmissaoRg;
    }

    public void setDtEmissaoRg(YearMonthDay dtEmissaoRg) {
        this.dtEmissaoRg = dtEmissaoRg;
    }

    public DomainValue getTpIdentificacao() {
        return this.tpIdentificacao;
    }

    public void setTpIdentificacao(DomainValue tpIdentificacao) {
        this.tpIdentificacao = tpIdentificacao;
    }

    public String getNrIdentificacao() {
        return this.nrIdentificacao;
    }
    
    public Long getNrCnae() {
		return nrCnae;
	}

	public void setNrCnae(Long nrCnae) {
		this.nrCnae = nrCnae;
	}
    
    /**
     * Usado para apresentar um número de identificação já com sua máscara.
	 * 
     * @return
	 * @deprecated Não usar este método, utilizar a
	 *             FormatUtils.formatIdentificacao
     */
    public String getNrIdentificacaoFormatado() {
		return formatIdentificacao((this.tpIdentificacao == null) ? null
				: this.tpIdentificacao.getValue(), this.nrIdentificacao);
    }

    public void setNrIdentificacao(String nrIdentificacao) {
        this.nrIdentificacao = nrIdentificacao;
    }

    public DomainValue getTpPessoa() {
        return this.tpPessoa;
    }

    public void setTpPessoa(DomainValue tpPessoa) {
        this.tpPessoa = tpPessoa;
    }

    public String getDsEmail() {
        return this.dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    public String getNrRg() {
        return this.nrRg;
    }

    public void setNrRg(String nrRg) {
        this.nrRg = nrRg;
    }

    public String getDsOrgaoEmissorRg() {
        return this.dsOrgaoEmissorRg;
    }

    public void setDsOrgaoEmissorRg(String dsOrgaoEmissorRg) {
        this.dsOrgaoEmissorRg = dsOrgaoEmissorRg;
    }    

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.ChecklistMeioTransporte.class)     
    public List getChecklistMeioTransportesByIdSegundoMotorista() {
        return this.checklistMeioTransportesByIdSegundoMotorista;
    }

	public void setChecklistMeioTransportesByIdSegundoMotorista(
			List checklistMeioTransportesByIdSegundoMotorista) {
        this.checklistMeioTransportesByIdSegundoMotorista = checklistMeioTransportesByIdSegundoMotorista;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.ChecklistMeioTransporte.class)     
    public List getChecklistMeioTransportesByIdPrimeiroMotorista() {
        return this.checklistMeioTransportesByIdPrimeiroMotorista;
    }

	public void setChecklistMeioTransportesByIdPrimeiroMotorista(
			List checklistMeioTransportesByIdPrimeiroMotorista) {
        this.checklistMeioTransportesByIdPrimeiroMotorista = checklistMeioTransportesByIdPrimeiroMotorista;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ParametroCliente.class)     
    public List getParametroClientes() {
        return this.parametroClientes;
    }

    public void setParametroClientes(List parametroClientes) {
        this.parametroClientes = parametroClientes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.Contato.class)     
    public List getContatosByIdPessoaIndicacao() {
        return this.contatosByIdPessoaIndicacao;
    }

    public void setContatosByIdPessoaIndicacao(List contatosByIdPessoaIndicacao) {
        this.contatosByIdPessoaIndicacao = contatosByIdPessoaIndicacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.Contato.class)     
    public List getContatosByIdPessoaContatado() {
        return this.contatosByIdPessoaContatado;
    }

    public void setContatosByIdPessoaContatado(List contatosByIdPessoaContatado) {
        this.contatosByIdPessoaContatado = contatosByIdPessoaContatado;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.InscricaoEstadual.class)     
    public List<InscricaoEstadual> getInscricaoEstaduais() {
        return this.inscricaoEstaduais;
    }

    public void setInscricaoEstaduais(List inscricaoEstaduais) {
        this.inscricaoEstaduais = inscricaoEstaduais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.IntegranteEquipe.class)     
    public List getIntegranteEquipes() {
        return this.integranteEquipes;
    }

    public void setIntegranteEquipes(List integranteEquipes) {
        this.integranteEquipes = integranteEquipes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.ReciboIndenizacao.class)     
    public List getReciboIndenizacoesByIdFavorecido() {
        return this.reciboIndenizacoesByIdFavorecido;
    }

	public void setReciboIndenizacoesByIdFavorecido(
			List reciboIndenizacoesByIdFavorecido) {
        this.reciboIndenizacoesByIdFavorecido = reciboIndenizacoesByIdFavorecido;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.ReciboIndenizacao.class)     
    public List getReciboIndenizacoesByIdBeneficiario() {
        return this.reciboIndenizacoesByIdBeneficiario;
    }

	public void setReciboIndenizacoesByIdBeneficiario(
			List reciboIndenizacoesByIdBeneficiario) {
        this.reciboIndenizacoesByIdBeneficiario = reciboIndenizacoesByIdBeneficiario;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.IntegranteEqOperac.class)     
    public List getIntegranteEqOperacs() {
        return this.integranteEqOperacs;
    }

    public void setIntegranteEqOperacs(List integranteEqOperacs) {
        this.integranteEqOperacs = integranteEqOperacs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Simulacao.class)     
    public List getSimulacoes() {
        return this.simulacoes;
    }

    public void setSimulacoes(List simulacoes) {
        this.simulacoes = simulacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.ContaBancaria.class)     
    public List getContaBancarias() {
        return this.contaBancarias;
    }

    public void setContaBancarias(List contaBancarias) {
        this.contaBancarias = contaBancarias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.TelefoneEndereco.class)     
    public List getTelefoneEnderecos() {
        return this.telefoneEnderecos;
    }

    public void setTelefoneEnderecos(List telefoneEnderecos) {
        this.telefoneEnderecos = telefoneEnderecos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.EnderecoPessoa.class)     
    public List<EnderecoPessoa> getEnderecoPessoas() {
        return this.enderecoPessoas;
    }

    public void setEnderecoPessoas(List enderecoPessoas) {
        this.enderecoPessoas = enderecoPessoas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.ImpostoCalculado.class)     
    public List getImpostoCalculados() {
        return this.impostoCalculados;
    }

    public void setImpostoCalculados(List impostoCalculados) {
        this.impostoCalculados = impostoCalculados;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.Mda.class)     
    public List getMdasByIdRemetente() {
        return this.mdasByIdRemetente;
    }

    public void setMdasByIdRemetente(List mdasByIdRemetente) {
        this.mdasByIdRemetente = mdasByIdRemetente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.Mda.class)     
    public List getMdasByIdDestinatario() {
        return this.mdasByIdDestinatario;
    }

    public void setMdasByIdDestinatario(List mdasByIdDestinatario) {
        this.mdasByIdDestinatario = mdasByIdDestinatario;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.Mda.class)     
    public List getMdasByIdConsignatario() {
        return this.mdasByIdConsignatario;
    }

    public void setMdasByIdConsignatario(List mdasByIdConsignatario) {
        this.mdasByIdConsignatario = mdasByIdConsignatario;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Cotacao.class)     
    public List getCotacoes() {
        return this.cotacoes;
    }

    public void setCotacoes(List cotacoes) {
        this.cotacoes = cotacoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPessoa", getIdPessoa())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Pessoa))
			return false;
        Pessoa castOther = (Pessoa) other;
		return new EqualsBuilder().append(this.getIdPessoa(),
				castOther.getIdPessoa()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPessoa()).toHashCode();
    }

	public String getNrInscricaoEstadual() {
		return nrInscricaoEstadual;
	}

	public void setNrInscricaoEstadual(String nrInscricaoEstadual) {
		this.nrInscricaoEstadual = nrInscricaoEstadual;
	}

	public String getNmFantasia() {
		return nmFantasia;
	}

	public void setNmFantasia(String nmFantasia) {
		this.nmFantasia = nmFantasia;
	}

	/**
	 * 
	 * @param tpIdentificacao
	 * @param conteudo
	 * @return
	 * @deprecated Usar a formatutils
	 */
    private String formatIdentificacao(String tpIdentificacao, String conteudo){
        
    	//Se não existir tipo de identificação, não faz nenhuma formatação
    	if (StringUtils.isBlank(tpIdentificacao)) {
    		return conteudo;
    	}

    	if (conteudo != null && !"".equals(conteudo)){
  
    		//Se tipo de identificação for CPF        		
    		if (tpIdentificacao.equals("CPF") && conteudo.length() == 11){
    			return formatCPF(conteudo);
    		}
    		//Se tipo de identificação for DNI        		
    		if (tpIdentificacao.equals("DNI") && conteudo.length() == 8){
    			return formatDNI(conteudo);
    		}    		

    		//Se tipo de identificação for CNPJ
    		if (tpIdentificacao.equals("CNPJ") && conteudo.length() == 14){
    			return formatCNPJ(conteudo);
    		}
    		//Se tipo de identificação for CUIT        		
    		if (tpIdentificacao.equals("CUIT") && conteudo.length() == 11){
    			return formatCUIT(conteudo);
    		}    		
        	
    		//Se tipo de identificação for RUT        	
    		if ("RUT".equals(tpIdentificacao)){
    			return formatRUT(conteudo);
    		}        	
    		//Se tipo de identificação for RUC    		
    		if ("RUC".equals(tpIdentificacao)){
    			return formatRUC(conteudo);
    		}

    	}

    	return conteudo;

    }

    /**
     * Formatação para RUC.<BR>
     * Exemplo de formatação:<BR>
     * <ul>
     * 	<li>entrada: 212307530015</li>
     * 	<li>saída: 21-230753-0015</li>
     * </ul>
     * <i>Formata apenas quando o tamanho da String for igual a 11.<i>  
	 * 
     * @author Anibal Maffioletti de Deus
     * @param rut
     * @return rut formatado
     * @deprecated
     */
    private static String formatRUC(String ruc){
    	if (ruc != null && ruc.length() ==  12){
        	StringBuffer format = new StringBuffer()
	    		.append(ruc.substring(0,2)).append("-")
	    		.append(ruc.substring(2,8)).append("-")
	    		.append(ruc.substring(8));
        	return format.toString();	
    	}

    	return ruc;
    }
    
    /**
     * Formatação de CPF.
	 * 
     *@author Robson Edemar Gehl
     * @param cpf
     * @return
     * @deprecated
     */
    private static String formatCPF(String cpf){
    	
    	if (cpf != null && cpf.length() > 8){
        	StringBuffer format = new StringBuffer()
	    		.append(cpf.substring(0,3)).append(".")
	    		.append(cpf.substring(3,6)).append(".")
	    		.append(cpf.substring(6,9)).append("-")
	    		.append(cpf.substring(9));
        	return format.toString();	
    	}
    		
    	return cpf;
    }
    
    /**
     * Formatação de CNPJ.
	 * 
     * @author Robson Edemar Gehl
     * @param cnpj
     * @return String
     * @deprecated
     */
    private static String formatCNPJ(String cnpj){
    	
    	if(cnpj != null && cnpj.length() >= 12){
	    	StringBuffer format = new StringBuffer()
	    		.append(cnpj.substring(0,2)).append(".")
	    		.append(cnpj.substring(2,5)).append(".")
	    		.append(cnpj.substring(5,8)).append("/")
	    		.append(cnpj.substring(8,12)).append("-")
	    		.append(cnpj.substring(12));
	    	return format.toString();
    	}
    	
    	return cnpj;
    }
    
    /**
     * Formatação para CUIT.<BR>
     * Exemplo de formatação:<BR>
     * <ul>
     * 	<li>entrada: 30680560109</li>
     * 	<li>saída: 30-68056010-9</li>
     * </ul>
     * <i>Formata apenas quando o tamanho da String for igual a 12.<i>  
	 * 
     *@author Robson Edemar Gehl
     * @param cuit
     * @return cuit formatado
     * @deprecated
     */
    private static String formatCUIT(String cuit){
    	StringBuffer format = new StringBuffer();
    	if (cuit.length() == 11){
    		format.append(cuit.substring(0,2)).append("-")
    			.append(cuit.substring(2,10)).append("-")
    			.append(cuit.substring(10));
    		return format.toString();
    	}
    	return cuit;
    }
    
    /**
     * Formatação para RUT.<BR>
     * Exemplo de formatação:<BR>
     * <ul>
     * 	<li>entrada: 590991406</li>
     * 	<li>saída: 59.099.140-6</li>
     * </ul>
     * <i>Formata apenas quando o tamanho da String for igual a 9.<i>  
	 * 
     *@author Robson Edemar Gehl
     * @param rut
     * @return rut formatado
     * @deprecated
     */
    private static String formatRUT(String rut){
    	StringBuffer format = new StringBuffer();
    	if (rut.length() == 9){
    		format.append(rut.substring(0,2)).append(".")
    			.append(rut.substring(2,5)).append(".")
    			.append(rut.substring(5,8)).append("-")
    			.append(rut.substring(8));
    		return format.toString();
    	}
    	return rut;
    }
    
    /**
     * Formatação para DNI.<BR>
     * Exemplo de formatação:<BR>
     * <ul>
     * 	<li>entrada: 26223237</li>
     * 	<li>saída: 26.223.237</li>
     * </ul>
     * <i>Formata apenas quando o tamanho da String for igual a 8.<i>  
	 * 
     *@author Mickaël Jalbert
     * @param DNI
     * @return DNI formatado
     * @deprecated
     */
    private static String formatDNI(String dni){
    	StringBuffer format = new StringBuffer();
    	if (dni.length() == 8){
    		format.append(dni.substring(0,2)).append(".")
    			.append(dni.substring(2,5)).append(".")
    			.append(dni.substring(5));
    		return format.toString();
    	}
    	return dni;
    }

	public EnderecoPessoa getEnderecoPessoa() {
		return enderecoPessoa;
	}

	public void setEnderecoPessoa(EnderecoPessoa enderecoPessoa) {
		this.enderecoPessoa = enderecoPessoa;
	}

	public String getNrInscricaoMunicipal() {
		return nrInscricaoMunicipal;
	}

	public void setNrInscricaoMunicipal(String nrInscricaoMunicipal) {
		this.nrInscricaoMunicipal = nrInscricaoMunicipal;
	}

	public Boolean getBlAtualizacaoCountasse() {
		return blAtualizacaoCountasse;
	}

	public void setBlAtualizacaoCountasse(Boolean blAtualizacaoCountasse) {
		this.blAtualizacaoCountasse = blAtualizacaoCountasse;
	}    
	
	public UnidadeFederativa getUnidadeFederativaExpedicaoRg() {
		return unidadeFederativaExpedicaoRg;
	}

	public void setUnidadeFederativaExpedicaoRg(
			UnidadeFederativa unidadeFederativaExpedicaoRg) {
		this.unidadeFederativaExpedicaoRg = unidadeFederativaExpedicaoRg;
	}

}
