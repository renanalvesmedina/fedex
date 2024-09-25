package com.mercurio.lms.expedicao.util.nfe;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.util.FormatUtils;


public class NfeConverterUtils {
	//Regra de formatacao da NDD, com virgula, ao inves de ponto, por isso foi utilizado locale pt_BR.
    private static DecimalFormat formatDoisDecimais = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(new Locale("pt","BR")));
    private static DecimalFormat formatQuatroDecimais = new DecimalFormat("0.0000", DecimalFormatSymbols.getInstance(new Locale("pt","BR")));
    
    public static String formatDoisDecimais(BigDecimal value) {
        return value == null ? "0,00" : formatDoisDecimais.format(value);
    }

    public static String formatQuatroDecimais(BigDecimal value) {
        return value == null ? "0,0000" : formatQuatroDecimais.format(value);
    }
    
    public static String formatCMun(Municipio municipio) {
        String nrUFIbge = municipio.getUnidadeFederativa().getNrIbge() == null ? "" : municipio.getUnidadeFederativa().getNrIbge().toString();
        String nrMunIbge = municipio.getCdIbge() == null ? "" : municipio.getCdIbge().toString();
        return FormatUtils.fillNumberWithZero(nrUFIbge, 2)+FormatUtils.fillNumberWithZero(nrMunIbge, 5);
    }

	public static TelefoneEndereco getTelefoneEndereco(EnderecoPessoa endereco, String tpPessoa) {
		return getTelefoneEndereco(endereco.getTelefoneEnderecos(), tpPessoa, Boolean.TRUE);
	}

	public static TelefoneEndereco getTelefoneEndereco(List<TelefoneEndereco> telefoneEnderecos, String tpPessoa, Boolean possuiEnderecoPessoa) {
		TelefoneEndereco telefone = null;
		String tpTelefone = "J".equals(tpPessoa) ? "C" : "R";
		
		for (TelefoneEndereco te: telefoneEnderecos) {
			if (possuiEnderecoPessoa.equals(te.getEnderecoPessoa() != null) && tpTelefone.equals(te.getTpTelefone().getValue())) {
                if ("FO".equals(te.getTpUso().getValue()) || "FF".equals(te.getTpUso().getValue())) {
                    if ((te.getDddTelefone()+te.getNrTelefone()).length() >= 7) {
                        telefone = te;
                        break;
                    }
                }
            }
        }
		
		return telefone;
	}
	
	public static List<Contato> getContatos(Pessoa pessoa) {
		List<Contato> toReturn = new ArrayList<Contato>();
		if (pessoa.getContatosByIdPessoaContatado() != null) {
			for (Contato c: (List<Contato>)pessoa.getContatosByIdPessoaContatado()) {
				if ("CT".equals(c.getTpContato().getValue())) {
					toReturn.add(c);
				}
			}
		}
		return toReturn;
	}
    
    /**
     * Retorna a string tratada com o tamanho máximo definido
     * 
     * @param s
     * @return
     */
    public static String tratarString(String s, int maxLength) {
        if (s == null) {
            return null;
        }
        return StringUtils.substring(StringUtils.defaultString(s, "").trim(), 0, maxLength);
    }
    
    public static String objectToString(Object o, String defaultValue) {
    	return o == null ? defaultValue : o.toString();
    }

    public static String formatTelefone(String telefone){
        if(telefone == null){
            return null;
        }
        String fone = telefone.replaceAll("[^0-9]+","");
        if(fone.length() > 6){
            return fone;
        }
        return null;
    }
    
}
