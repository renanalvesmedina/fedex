package com.mercurio.lms.carregamento.util.mdfe.converter.v300.rodo;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v300.Prop;
import com.mercurio.lms.mdfe.model.v300.PropChoice;
import com.mercurio.lms.mdfe.model.v300.PropSequence;
import com.mercurio.lms.mdfe.model.v300.types.TUf;
import com.mercurio.lms.mdfe.model.v300.types.TpPropType;
import com.mercurio.lms.util.FormatUtils;

public class PropConverter {

	private ManifestoEletronico mdfe;
	private String retiraZeroInicialIe;
	private static final String ISENTO = "ISENTO";
	
	public PropConverter(ManifestoEletronico mdfe, String retiraZeroInicialIe) {
		super();
		this.mdfe = mdfe;
		this.retiraZeroInicialIe = retiraZeroInicialIe;
	}

	public Prop convert() {
		Prop prop = new Prop();

		prop.setRNTRC(FormatUtils.fillNumberWithZero(mdfe.getControleCarga().getProprietario().getNrAntt() == null ? null : mdfe.getControleCarga().getProprietario()
				.getNrAntt().toString(), 8));

		Pessoa pessoa = getProprietario();
		if (pessoa != null) {
			PropChoice propChoice = new PropChoice();
			if ("F".equals(pessoa.getTpPessoa().getValue())) {
				propChoice.setCPF(FormatUtils.fillNumberWithZero(pessoa.getNrIdentificacao(), 11));
				
			} else if ("J".equals(pessoa.getTpPessoa().getValue())) {
				propChoice.setCNPJ(FormatUtils.fillNumberWithZero(pessoa.getNrIdentificacao(), 14));
			}
			prop.setPropChoice(propChoice);
		}

		prop.setXNome(getNomeProprietario());
		
		PropSequence propSequence = new PropSequence();
		
		propSequence.setIE(getIEProprietario());
		
		propSequence.setUF(getUfProprietario());

		prop.setPropSequence(propSequence);
		
		prop.setTpProp(TpPropType.fromValue(getTpProprietario()));
		
		return prop;
	}

	private Pessoa getProprietario() {

		if (mdfe.getControleCarga() != null && mdfe.getControleCarga().getProprietario() != null && mdfe.getControleCarga().getProprietario().getPessoa() != null) {
			return mdfe.getControleCarga().getProprietario().getPessoa();
		}

		return null;
	}

	private String getNomeProprietario() {

		if (mdfe.getControleCarga() != null && mdfe.getControleCarga().getProprietario() != null && mdfe.getControleCarga().getProprietario().getPessoa() != null) {
			return MdfeConverterUtils.tratarString(mdfe.getControleCarga().getProprietario().getPessoa().getNmPessoa(), 60);
		}

		return null;
	}

	/**
	 * para IE.ID_PESSOA = CC.ID_PROPRIETARIO e IE.BL_INDICADOR_PADRAO = 'S'
	 * 
	 * Consultar a UF desta Inscrição Estadual (UF.SG_UNIDADE_FEDERATIVA) no
	 * conteúdo do parâmetro geral (PARAMETRO_GERAL.DS_CONTEUDO =
	 * 'RETIRA_ZERO_INICIAL_IE') - Se a UF está no Conteúdo do parâmetro geral -
	 * Se o número de posições da IE é 9 e a primeira posição é igual à ZERO -
	 * Considerar somente as próximas 8 posições da IE;
	 * 
	 * @return
	 */
	private String getIEProprietario() {
		String ieProprietario = null;

		if (mdfe.getControleCarga() != null && mdfe.getControleCarga().getProprietario() != null && mdfe.getControleCarga().getProprietario().getPessoa() != null) {
			
			if(mdfe.getControleCarga().getProprietario().getPessoa().getTpPessoa() != null
					&& "F".equals(mdfe.getControleCarga().getProprietario().getPessoa().getTpPessoa().getValue())){
				ieProprietario = ISENTO;
			} else {
				List<InscricaoEstadual> inscricaoEstaduais = mdfe.getControleCarga().getProprietario().getPessoa().getInscricaoEstaduais();
				for (InscricaoEstadual ie : inscricaoEstaduais) {
					if (Boolean.TRUE.equals(ie.getBlIndicadorPadrao())) {
						String nrInscricaoEstadual = ie.getNrInscricaoEstadual();
	
						if (retiraZeroInicialIe != null && retiraZeroInicialIe.indexOf(ie.getUnidadeFederativa().getSgUnidadeFederativa()) >= 0) {
							if (StringUtils.isNotBlank(nrInscricaoEstadual) && nrInscricaoEstadual.length() == 9 && nrInscricaoEstadual.charAt(0) == '0') {
								nrInscricaoEstadual = nrInscricaoEstadual.substring(1);
							}
						}
	
						ieProprietario = MdfeConverterUtils.tratarString(nrInscricaoEstadual, 14);
					}
				}
			}
		}

		return ieProprietario;
	}

	/**
	 * Para UF.ID_UNIDADE_FEDERATIVA = MUN.ID_UNIDADE_FEDERATIVA e
	 * MUN.ID_MUNICIPIO = EP.ID_MUNICIPIO e EP.ID_ENDERECO_PESSOA =
	 * PESSOA.ID_ENDERECO_PESSOA
	 * 
	 * @return
	 */
	private TUf getUfProprietario() {

		if (mdfe.getControleCarga() != null && mdfe.getControleCarga().getProprietario() != null && mdfe.getControleCarga().getProprietario().getPessoa() != null
				&& mdfe.getControleCarga().getProprietario().getPessoa().getEnderecoPessoa() != null
				&& mdfe.getControleCarga().getProprietario().getPessoa().getEnderecoPessoa().getMunicipio() != null
				&& mdfe.getControleCarga().getProprietario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa() != null
				&& mdfe.getControleCarga().getProprietario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa() != null) {

			return TUf.fromValue(mdfe.getControleCarga().getProprietario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		}

		return null;
	}

	private String getTpProprietario() {
		String tpProprietario = null;

		if (mdfe.getControleCarga() != null && mdfe.getControleCarga().getProprietario() != null && mdfe.getControleCarga().getProprietario().getTpProprietario() != null) {
			if ("A".equals(mdfe.getControleCarga().getProprietario().getTpProprietario().getValue())) {
				tpProprietario = "0";
			} else if ("E".equals(mdfe.getControleCarga().getProprietario().getTpProprietario().getValue())) {
				tpProprietario = "1";
			} else {
				tpProprietario = "2";
			}
		}

		return tpProprietario;
	}
}
