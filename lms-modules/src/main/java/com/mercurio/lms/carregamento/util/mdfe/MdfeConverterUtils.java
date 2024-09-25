package com.mercurio.lms.carregamento.util.mdfe;

import com.mercurio.lms.carregamento.model.ControleCarga;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspConteudoAtrib;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100.types.TUf;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.util.FormatUtils;
import org.apache.commons.collections.CollectionUtils;


public class MdfeConverterUtils {
    private static DecimalFormat formatDoisDecimais = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(new Locale("pt_br")));
    private static DecimalFormat formatQuatroDecimais = new DecimalFormat("0.0000", DecimalFormatSymbols.getInstance(new Locale("pt_br")));
    
    public static String formatDoisDecimais(BigDecimal value) {
        return formatDoisDecimais.format(value);
    }

    public static String formatQuatroDecimais(BigDecimal value) {
        return formatQuatroDecimais.format(value);
    }
    
    public static String formatRENAVAM(String renavam, int size){
    	if(renavam != null && renavam.length() < size){
    		return FormatUtils.fillNumberWithZero(renavam, size);
    	}
    	return renavam;
    }
    
    public static String formatCMun(Municipio municipio) {
        String nrUFIbge = municipio.getUnidadeFederativa().getNrIbge() == null ? "" : municipio.getUnidadeFederativa().getNrIbge().toString();
        String nrMunIbge = municipio.getCdIbge() == null ? "" : municipio.getCdIbge().toString();
        return FormatUtils.fillNumberWithZero(nrUFIbge, 2)+FormatUtils.fillNumberWithZero(nrMunIbge, 5);
    }

    public static String formatTelefone(EnderecoPessoa endereco) {
        String telefone = null;
        for (TelefoneEndereco te: endereco.getTelefoneEnderecos()) {
            if ("C".equals(te.getTpTelefone().getValue())) {
                if ("FO".equals(te.getTpUso().getValue()) || "FF".equals(te.getTpUso().getValue())) {
                    if ((te.getDddTelefone()+te.getNrTelefone()).length() >= 7) {
                        telefone = formatTelefone(te.getNrDdd()+te.getNrTelefone());
                        break;
                    }
                }
            }
        }
        return telefone;
    }
    
    public static Map<String, List<Conhecimento>> getConhecimentos(List<Conhecimento> conhecimentos) {
    	
    	Collections.sort(conhecimentos, new Comparator<Conhecimento>() {
    		@Override
    		public int compare(Conhecimento o1, Conhecimento o2) {
    			UnidadeFederativa ufDestino1 = getUfDestinoConhecimento(o1);
    			UnidadeFederativa ufDestino2 = getUfDestinoConhecimento(o2);
				return ufDestino1.getIdUnidadeFederativa().compareTo(ufDestino2.getIdUnidadeFederativa());
    		}
		});
    	Map<String, List<Conhecimento>> toReturn = new HashMap<String, List<Conhecimento>>();
        
        for (Conhecimento c: conhecimentos) {
        	String uf = getUfDestinoConhecimento(c).getSgUnidadeFederativa();
			List<Conhecimento> listByUf = toReturn.get(uf);
			if (listByUf == null) {
        		listByUf = new ArrayList<Conhecimento>();
        		toReturn.put(uf, listByUf);
        	}
            listByUf.add(c);
        }
        
        return toReturn;
        
    }

    public static UnidadeFederativa getUfDestinoConhecimento(Conhecimento c) {
        
        if(c.getFilialByIdFilialDestino() != null){
    	return c.getFilialByIdFilialDestino().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa();
        }else if(c.getMunicipioByIdMunicipioEntrega() != null){
            return c.getMunicipioByIdMunicipioEntrega().getUnidadeFederativa();
        }else if(c.getClienteByIdClienteDestinatario() != null){
            return c.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa();
        }else if(c.getFilialByIdFilialOrigem() != null){
            return c.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa();
    }
    	return null;
    }
    
    public static Map<String, List<Conhecimento>> getConhecimentosByMunicipioEntrega(List<Conhecimento> conhecimentos) {
    	
    	Collections.sort(conhecimentos, new Comparator<Conhecimento>() {
    		@Override
    		public int compare(Conhecimento o1, Conhecimento o2) {
    			UnidadeFederativa ufDestino1 = getUfMunicipioEntrega(o1);
    			UnidadeFederativa ufDestino2 = getUfMunicipioEntrega(o2);
				return ufDestino1.getIdUnidadeFederativa().compareTo(ufDestino2.getIdUnidadeFederativa());
    		}
		});
    	Map<String, List<Conhecimento>> toReturn = new HashMap<String, List<Conhecimento>>();
        
        for (Conhecimento c: conhecimentos) {
        	String uf = getUfMunicipioEntrega(c).getSgUnidadeFederativa();
			List<Conhecimento> listByUf = toReturn.get(uf);
			if (listByUf == null) {
        		listByUf = new ArrayList<Conhecimento>();
        		toReturn.put(uf, listByUf);
        	}
            listByUf.add(c);
        }
        
        return toReturn;
        
    }

    private static UnidadeFederativa getUfMunicipioEntrega(Conhecimento c) {
        if(c.getMunicipioByIdMunicipioEntrega() != null){
    	return c.getMunicipioByIdMunicipioEntrega().getUnidadeFederativa();
        }else if(c.getFilialByIdFilialDestino() != null){
            return c.getFilialByIdFilialDestino().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa();
    }

    	return null;
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

	public static String getTpCar(MeioTransporte meioTransporte) {
		String tpCar = "00";

		if (meioTransporte != null && meioTransporte.getMeioTranspConteudoAtribs() != null) {

			List<MeioTranspConteudoAtrib> listaMeioTranspConteudoAtrib = meioTransporte.getMeioTranspConteudoAtribs();

			for (MeioTranspConteudoAtrib meioTranspConteudoAtrib : listaMeioTranspConteudoAtrib) {
				if (meioTranspConteudoAtrib.getConteudoAtributoModelo() != null && meioTranspConteudoAtrib.getConteudoAtributoModelo().getModeloMeioTranspAtributo() != null
						&& meioTranspConteudoAtrib.getConteudoAtributoModelo().getModeloMeioTranspAtributo().getAtributoMeioTransporte() != null) {

					if (Long.valueOf(1).equals(
							meioTranspConteudoAtrib.getConteudoAtributoModelo().getModeloMeioTranspAtributo().getAtributoMeioTransporte().getIdAtributoMeioTransporte())) {
						String dsConteudoAtributoModelo = meioTranspConteudoAtrib.getConteudoAtributoModelo().getDsConteudoAtributoModelo().getValue();

						if ("Baú".equals(dsConteudoAtributoModelo)) {
							tpCar = "02";
						} else if ("Graneleira".equals(dsConteudoAtributoModelo)) {
							tpCar = "03";
						} else if ("Porta-Container".equals(dsConteudoAtributoModelo)) {
							tpCar = "04";
						} else if ("Sedan".equals(dsConteudoAtributoModelo)) {
							tpCar = "02";
						} else if ("Baú Plataforma".equals(dsConteudoAtributoModelo)) {
							tpCar = "02";
						} else if ("Aberta".equals(dsConteudoAtributoModelo)) {
							tpCar = "01";
						} else if ("Baú/PPW".equals(dsConteudoAtributoModelo)) {
							tpCar = "02";
						} else if ("Single-Pneumática".equals(dsConteudoAtributoModelo)) {
							tpCar = "00";
						} else if ("Frigorífica".equals(dsConteudoAtributoModelo)) {
							tpCar = "02";
						}
					}
				}
			}
		}

		return tpCar;
	}
    
	/**
	 * Para MTR.ID_MEIO_TRANSPORTE = CC.ID_SEMI_REBOCADO
	 * e MUN.ID_MUNICIPIO = MTR.ID_MUNICIPIO
	 * e UF.ID_UNIDADE_FEDERATIVA = MUN.ID_UNIDADE_FEDERATIVA

	 * @param controleCarga
	 * @param meioTransporte
	 * @return
	 */
	public static TUf getUF(MeioTransporte meioTransporte) {
		if (meioTransporte != null
				&& meioTransporte.getMeioTransporteRodoviario() != null
				&& meioTransporte.getMeioTransporteRodoviario().getMunicipio() != null
				&& meioTransporte.getMeioTransporteRodoviario().getMunicipio().getUnidadeFederativa() != null) {
			
			return TUf.fromValue(meioTransporte.getMeioTransporteRodoviario().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			
		}
		return null;
	}
	
	public static com.mercurio.lms.mdfe.model.v300.types.TUf get300UF(MeioTransporte meioTransporte) {
		if (meioTransporte != null
				&& meioTransporte.getMeioTransporteRodoviario() != null
				&& meioTransporte.getMeioTransporteRodoviario().getMunicipio() != null
				&& meioTransporte.getMeioTransporteRodoviario().getMunicipio().getUnidadeFederativa() != null) {
			
			return com.mercurio.lms.mdfe.model.v300.types.TUf.fromValue(meioTransporte.getMeioTransporteRodoviario().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			
		}
		return null;
	}
	
	public static com.mercurio.lms.mdfe.model.v100a.types.TUf get100aUF(MeioTransporte meioTransporte) {
		if (meioTransporte != null
				&& meioTransporte.getMeioTransporteRodoviario() != null
				&& meioTransporte.getMeioTransporteRodoviario().getMunicipio() != null
				&& meioTransporte.getMeioTransporteRodoviario().getMunicipio().getUnidadeFederativa() != null) {
			
			return com.mercurio.lms.mdfe.model.v100a.types.TUf.fromValue(meioTransporte.getMeioTransporteRodoviario().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			
		}
		return null;
	}

	public static UnidadeFederativa getUnidadeFederativaProximoDestinoControleCarga(ControleCarga controleCarga){
		try {
			return getFilialProximoDestinoControleCarga(controleCarga).getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa();
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	public static Filial getFilialProximoDestinoControleCarga(ControleCarga controleCarga){
		try {
			int positionAtual = getFilialPosition(controleCarga.getFilialByIdFilialAtualizaStatus(), controleCarga.getRota().getFilialRotas());
			FilialRota filialRota = (FilialRota) controleCarga.getRota().getFilialRotas().get(positionAtual);
			if(controleCarga.getRota().getFilialRotas().get(positionAtual+1) != null){
				filialRota = (FilialRota) controleCarga.getRota().getFilialRotas().get(positionAtual+1);
			}
			return filialRota.getFilial();
		}  catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	public static UnidadeFederativa getUnidadeFederativaDestinoByManifesto(ManifestoEletronico manifestoEletronico) {
		try {
            Municipio municipio = getMunicipioDestinoByManifesto(manifestoEletronico);
            if(municipio != null){
                return municipio.getUnidadeFederativa();
				}
			return null;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	private static int getFilialPosition(Filial filial, List<FilialRota> listFilialRotas){
		try {
			for (int i = 0; i < listFilialRotas.size(); i++) {
				if(listFilialRotas.get(i).getFilial().getIdFilial() == filial.getIdFilial()){
					return i;
				}
			}
			return 0;
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
    public static Municipio getMunicipioDestinoByManifesto(ManifestoEletronico manifestoEletronico){
        Municipio municipio = null;
        Conhecimento conhecimento = null;
        boolean hasConhecimentos = CollectionUtils.isNotEmpty(manifestoEletronico.getConhecimentos());
        if(hasConhecimentos){
            conhecimento = manifestoEletronico.getConhecimentos().get(0);
        }
        if(manifestoEletronico.getFilialDestino() != null){
            municipio = manifestoEletronico.getFilialDestino().getPessoa().getEnderecoPessoa().getMunicipio();
        }else if(hasConhecimentos
                && conhecimento.getFilialByIdFilialDestino() != null){
            municipio = conhecimento.getFilialByIdFilialDestino().getPessoa().getEnderecoPessoa().getMunicipio();
        }else if( hasConhecimentos 
                && conhecimento.getMunicipioByIdMunicipioEntrega() != null){
            municipio = conhecimento.getMunicipioByIdMunicipioEntrega();
        }else if( hasConhecimentos 
                && conhecimento.getClienteByIdClienteDestinatario()!= null){
            municipio = conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio();
        }else if( manifestoEletronico.getFilialOrigem() != null){
            municipio = manifestoEletronico.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio();
        }else if( hasConhecimentos && conhecimento.getFilialByIdFilialOrigem() != null){
            municipio = conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio();
        }
        return municipio;
    }
    
    public static UnidadeFederativa getUnidadeFederativaOrigemByManifesto(ManifestoEletronico manifestoEletronico) {
		try {
            Municipio municipio = getMunicipioOrigemByManifesto(manifestoEletronico);
            if (municipio != null){
                return municipio.getUnidadeFederativa();
            }
			return null;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	public static Municipio getMunicipioOrigemByManifesto(ManifestoEletronico manifestoEletronico) {
        Municipio municipio = null;
        Conhecimento conhecimento = null;
        boolean hasConhecimentos = CollectionUtils.isNotEmpty(manifestoEletronico.getConhecimentos());
        if(hasConhecimentos){
            conhecimento = manifestoEletronico.getConhecimentos().get(0);
}
        if(manifestoEletronico.getFilialOrigem() != null){
            municipio = manifestoEletronico.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio();
        }else if(hasConhecimentos
                && conhecimento.getFilialByIdFilialOrigem() != null){
            municipio = conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio();
        }else if( hasConhecimentos 
                && conhecimento.getMunicipioByIdMunicipioColeta() != null){
            municipio = conhecimento.getMunicipioByIdMunicipioColeta();
        }else if( manifestoEletronico.getFilialOrigem() != null){

        }
        return municipio;
	}
    
    public static Filial getFilialDestinoByManifesto(ManifestoEletronico mdfe){
        boolean hasConhecimentos = CollectionUtils.isNotEmpty(mdfe.getConhecimentos());
        Filial destino = null;
        Conhecimento conhecimento= null;
        if(hasConhecimentos){
            conhecimento = mdfe.getConhecimentos().get(0);
        }
        if(mdfe.getFilialDestino() != null){
            destino = mdfe.getFilialDestino();
        }else if(hasConhecimentos && conhecimento.getFilialByIdFilialDestino() != null){
            destino = conhecimento.getFilialByIdFilialDestino();
        }else if (mdfe.getFilialOrigem() != null){
            destino = mdfe.getFilialOrigem();
        }else if (hasConhecimentos && conhecimento.getFilialByIdFilialOrigem() != null){
            destino = conhecimento.getFilialByIdFilialOrigem();
        }
        return destino;
    }
    
	
}
