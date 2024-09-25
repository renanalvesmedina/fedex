package com.mercurio.lms.expedicao.util;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.util.FormatUtils;

/**
 * 
 * @autor Cleveland Júnior Soares
 * @since 04/11/2009
 */
public class EnderecoPessoaUtils {
	//Caches
	private static Map<String, Pattern> cachePatterns = new WeakHashMap<String, Pattern>();

	//Patterns
	private static Pattern NR_KM = Pattern.compile("KM\\s+\\d|KM\\)\\d|KM\\.\\d|KM:\\d|KM-\\d", Pattern.CASE_INSENSITIVE);
	private static Pattern BEGINNING_NO_WORD = Pattern.compile("^\\W+");
	
	private static final List<TokenGroup> GROUPS = new ArrayList<TokenGroup>(16);

	static {
		GROUPS.add(new TokenGroup(1, "RUA", "RUAS","R"));
		GROUPS.add(new TokenGroup(2, "AVENIDA", "AVENIDAS", "AV"));
		GROUPS.add(new TokenGroup(3, "PRACA", "PRACAS"));
		GROUPS.add(new TokenGroup(4, "TRAVESSA", "TRAV"));
		GROUPS.add(new TokenGroup(5, "BECO", "TRAV"));
		GROUPS.add(new TokenGroup(6, "ALAMEDA", "ALAM"));
		GROUPS.add(new TokenGroup(7, "RODOVIA", "RODOVIAS", "ROD", "RODO", "RODOV"));
		GROUPS.add(new TokenGroup(8, "ESTRADA", "EST", "ESTR", "ES"));
		GROUPS.add(new TokenGroup(9, "FAZENDA", "FAZ", "ESTANCIA"));
		GROUPS.add(new TokenGroup(10, "GALERIA", "GAL"));
		GROUPS.add(new TokenGroup(11, "LARGO"));
		GROUPS.add(new TokenGroup(12, "VIELA"));
		GROUPS.add(new TokenGroup(13, "VILA"));
		GROUPS.add(new TokenGroup(14, "COLONIA AGRICOLA", "COL AGRICOLA", "COL.AGRICOLA", "COL. AGRICOLA", "COLONIA AGRIC", "COL AGR", "COL AGRIC", "COLONIA AGR", "COL.AGR", "COL. AGR", "COL.AGRIC", "COL. AGRIC"));
		GROUPS.add(new TokenGroup(15, "ACESSO", "AC"));
		GROUPS.add(new TokenGroup(16, "AEROPORTO", "AEROP", "AERO"));
	}

	/**
	 * Retorna o tipo de logradouro de acordo com palavras chaves encontradas no 
	 * endereço passado por parametro.
	 * @param dsEndereco
	 * @return
	 */
	public static int getTipoLogradouro(StringBuilder dsEndereco){
		int codInexistente = 41;
		String[] variations = {"", "\\.", ",", ";", ":", " ", "\\. ", ", ", "; ", ": "};
		String text = dsEndereco == null ? "" : dsEndereco.toString();

		if(isBlank(text)) return codInexistente;

		text = text
			.trim()
			.toUpperCase()
			.replaceAll("Í", "I")
			.replaceAll("Ô", "O")
			.replaceAll("Â", "A");

		for(TokenGroup tokenGroup : GROUPS){
			for(int i = 0; i < tokenGroup.size(); i++){
				String token = tokenGroup.get(i);
				for(String variation : variations) {
					String strPattern = "^" + token + variation;

					if(token.length() == 1 || "EST|ES".indexOf(token) > -1) {
						strPattern += "\\b";//Nao deixa pegar uma palavra que esteja grudada em outra
					}

					Pattern pattern = getPattern(strPattern);
					Matcher matcher = pattern.matcher(text); 
					if(matcher.find()){
						dsEndereco.delete(0, dsEndereco.length());
						text = matcher.replaceAll("");
						text = BEGINNING_NO_WORD.matcher(text).replaceFirst("");

						dsEndereco.append(text);
						return tokenGroup.getKey(); 
					}
				}
			}
		}

		return codInexistente;
	}

	/**
	 * 'Cacheia' os patterns para não precisar compilá-los a cada chamada.
	 * @param strPattern
	 * @return
	 */
	private static Pattern getPattern(String strPattern){
		if(!cachePatterns.containsKey(strPattern)){
			cachePatterns.put(strPattern, Pattern.compile(strPattern));
		}

		return cachePatterns.get(strPattern);
	}
	
	/**
	 * Retorna o número do endereço encontrado na String de consulta. Se
	 * for achado apenas um número na String e que não possua a palavra 'KM'
	 * antecedendo o mesmo, este será devolvido. Caso contrário será devolvido
	 * um '.'(ponto).
	 * @param dsEndereco
	 * @return
	 */
	public static String getNumeroEndereco(StringBuilder dsEndereco){
		String returnValue = ".";
		if(dsEndereco != null){
			String text = dsEndereco.toString();
			if(!NR_KM.matcher(text).find()) {
				int coutNumbers = StringUtils.countNumbers(text); 
				if(coutNumbers == 1) {
					String result = StringUtils.getFirstNumber(text);
					text = text.replaceAll("\\s+\\d+\\s+", " ");
					text = text.replaceAll("\\s+\\d+", "");
					text = text.replaceAll("\\d+\\s+", "");
					dsEndereco.setLength(0);
					dsEndereco.append(text);
					returnValue = result;
				}
			}
		}
		return returnValue;
	}
	
	public static String mountEnderecoFilial(EnderecoPessoa enderecoPessoa){
		StringBuilder endereco = buildEndereco(enderecoPessoa);
		
		TelefoneEndereco fone = null;
		TelefoneEndereco fax = null;
		TelefoneEndereco foneFax = null;
		
		for (Object otelefoneEndereco : enderecoPessoa.getPessoa().getTelefoneEnderecos()) {
			TelefoneEndereco telefoneEndereco = (TelefoneEndereco) otelefoneEndereco;
			if(fax == null && "FA".equals(telefoneEndereco.getTpUso().getValue())){
				fax = telefoneEndereco;
			} else if (fone == null && "FO".equals(telefoneEndereco.getTpUso().getValue())){
				fone = telefoneEndereco;
			}else if(foneFax == null && "FF".equals(telefoneEndereco.getTpUso().getValue())){
				foneFax = telefoneEndereco;
			}
			
			if(fone != null && fax != null){
				break;
			}
		}
		if(fone == null){
			fone = foneFax;
		}
		if(fax == null){
			fax = foneFax;
		}
		endereco.append(buildTelefoneEndereco(fone, " - Telefone "));
		endereco.append(buildTelefoneEndereco(fax, " - Fax "));
		return endereco.toString();
	}
	
	private static String buildTelefoneEndereco(TelefoneEndereco fone, String tipo) {
		if(fone != null){
			return tipo + FormatUtils.formatTelefone(fone.getNrTelefone(), fone.getNrDdd(), fone.getNrDdi());
		}
		return "";
	}
    
	private static StringBuilder buildEndereco(EnderecoPessoa enderecoPessoa) {
		StringBuilder endereco = new StringBuilder();
		endereco.append(  enderecoPessoa.getPessoa().getNmFantasia()).append(": ")
				.append(enderecoPessoa.getEnderecoCompleto())
				.append(" CEP ").append( FormatUtils.formatCep(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getSgPais(), enderecoPessoa.getNrCep()) )
				.append(" - ").append(enderecoPessoa.getMunicipio().getNmMunicipio())
				.append(" - ").append(enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		return endereco;
	}
	
}