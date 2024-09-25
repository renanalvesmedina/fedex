package com.mercurio.lms.tabelaprecos.util;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.vendas.model.ParametroCliente;

public class RotaPrecoUtils {

	public static RestricaoRota getRestricaoRota(
		Long idZona,
		Long idPais,
		Long idUnidadeFederativa,
		Long idFilial,
		Long idMunicipio,
		Long idTipoLocalizacao,
		Long idAeroporto
	) {
		RestricaoRota restricaoRota = new RestricaoRota();
		restricaoRota.setIdZona(idZona);
		restricaoRota.setIdPais(idPais);
		restricaoRota.setIdUnidadeFederativa(idUnidadeFederativa);
		restricaoRota.setIdFilial(idFilial);
		restricaoRota.setIdMunicipio(idMunicipio);
		restricaoRota.setIdTipoLocalizacao(idTipoLocalizacao);
		restricaoRota.setIdAeroporto(idAeroporto);
		return restricaoRota;
	}

	public static void getRotaPrecoRestricaoRota(DetachedCriteria detachedCriteria, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		
		// ---- Origem ----
		detachedCriteria.add(Restrictions.eq("rp.zonaByIdZonaOrigem.id", restricaoRotaOrigem.getIdZona()));
		detachedCriteria.add(getCriterion("rp.paisByIdPaisOrigem.id", restricaoRotaOrigem.getIdPais()));
		detachedCriteria.add(getCriterion("rp.unidadeFederativaByIdUfOrigem.id", restricaoRotaOrigem.getIdUnidadeFederativa()));
		detachedCriteria.add(getCriterion("rp.aeroportoByIdAeroportoOrigem.id", restricaoRotaOrigem.getIdAeroporto()));
		detachedCriteria.add(getCriterion("rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.id", restricaoRotaOrigem.getIdTipoLocalizacao()));
		detachedCriteria.add(getCriterion("rp.filialByIdFilialOrigem.id", restricaoRotaOrigem.getIdFilial()));
		detachedCriteria.add(getCriterion("rp.municipioByIdMunicipioOrigem.id", restricaoRotaOrigem.getIdMunicipio()));

		// ---- Destino ----
		detachedCriteria.add(Restrictions.eq("rp.zonaByIdZonaDestino.id", restricaoRotaDestino.getIdZona()));
		detachedCriteria.add(getCriterion("rp.paisByIdPaisDestino.id", restricaoRotaDestino.getIdPais()));
		detachedCriteria.add(getCriterion("rp.unidadeFederativaByIdUfDestino.id", restricaoRotaDestino.getIdUnidadeFederativa()));
		detachedCriteria.add(getCriterion("rp.aeroportoByIdAeroportoDestino.id", restricaoRotaDestino.getIdAeroporto()));
		detachedCriteria.add(getCriterion("rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.id", restricaoRotaDestino.getIdTipoLocalizacao()));
		detachedCriteria.add(getCriterion("rp.filialByIdFilialDestino.id", restricaoRotaDestino.getIdFilial()));
		detachedCriteria.add(getCriterion("rp.municipioByIdMunicipioDestino.id", restricaoRotaDestino.getIdMunicipio()));
	}

	public static void addRotaPrecoRestricaoRota(DetachedCriteria detachedCriteria, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		
		/*------------------------------------------------- ORIGEM --------------------------------------------------*/
		
		detachedCriteria.add(Restrictions.eq("rp.zonaByIdZonaOrigem.id", restricaoRotaOrigem.getIdZona()));
		detachedCriteria.add(getCriterion("rp.paisByIdPaisOrigem.id", restricaoRotaOrigem.getIdPais()));
		detachedCriteria.add(getCriterion("rp.unidadeFederativaByIdUfOrigem.id", restricaoRotaOrigem.getIdUnidadeFederativa()));
		
		if(restricaoRotaOrigem.getIdAeroporto() != null) {
			detachedCriteria.add(getCriterion("rp.aeroportoByIdAeroportoOrigem.id", restricaoRotaOrigem.getIdAeroporto()));
		}
		
		if("P".equals(restricaoRotaOrigem.getSbTabelaPreco())){
			
			if(restricaoRotaOrigem.getIsLocalComercialCapital() != null 
					&& restricaoRotaOrigem.getIsLocalComercialCapital()){
				detachedCriteria.add(Restrictions.eq("rp.tipoLocalizacaoMunicipioComercialOrigem.id", restricaoRotaOrigem.getIdTipoLocalizacaoComercial()));						
			}else{
				detachedCriteria.add(Restrictions.isNull("rp.tipoLocalizacaoMunicipioComercialOrigem.id"));						
			}
			if(restricaoRotaDestino.getIsLocalComercialCapital() != null 
					&& restricaoRotaDestino.getIsLocalComercialCapital()){
				detachedCriteria.add(Restrictions.eq("rp.tipoLocalizacaoMunicipioComercialDestino.id", restricaoRotaDestino.getIdTipoLocalizacaoComercial()));						
			}else{
				detachedCriteria.add(Restrictions.isNull("rp.tipoLocalizacaoMunicipioComercialDestino.id"));						
			}
			
		}else if("F".equals(restricaoRotaOrigem.getSbTabelaPreco())){			
			
			detachedCriteria.add(Restrictions.eq("rp.tipoLocalizacaoMunicipioComercialOrigem.id", restricaoRotaOrigem.getIdTipoLocalizacaoComercial()));
			detachedCriteria.add(getCriterion("rp.tipoLocalizacaoMunicipioComercialDestino.id", restricaoRotaDestino.getIdTipoLocalizacaoComercial()));
			
		}else{
			
			RotaPrecoUtils.addTipoLocalizacaoMunicipio(restricaoRotaOrigem, detachedCriteria, "rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.id");
			RotaPrecoUtils.addTipoLocalizacaoMunicipio(restricaoRotaDestino, detachedCriteria, "rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.id");			
		}
		
		detachedCriteria.add(getCriterion("rp.filialByIdFilialOrigem.id", restricaoRotaOrigem.getIdFilial()));
		detachedCriteria.add(getCriterion("rp.municipioByIdMunicipioOrigem.id", restricaoRotaOrigem.getIdMunicipio()));

		/*Grupo região origem*/
		if(restricaoRotaOrigem.getIdGrupoRegiao() != null){
			detachedCriteria.add(getCriterion("rp.grupoRegiaoOrigem.id", restricaoRotaOrigem.getIdGrupoRegiao()));
		}else{
			detachedCriteria.add(Restrictions.isNull("rp.grupoRegiaoOrigem.id"));
		}		
		
		/*------------------------------------------------- DESTINO --------------------------------------------------*/
		
		detachedCriteria.add(Restrictions.eq("rp.zonaByIdZonaDestino.id", restricaoRotaDestino.getIdZona()));
		detachedCriteria.add(getCriterion("rp.paisByIdPaisDestino.id", restricaoRotaDestino.getIdPais()));
		
		/*Se filtra por aeroporto nao deve filtrar por estado*/
		if(restricaoRotaDestino.getIdAeroporto() == null) {
			detachedCriteria.add(getCriterion("rp.unidadeFederativaByIdUfDestino.id", restricaoRotaDestino.getIdUnidadeFederativa()));
		} else {
			detachedCriteria.add(getCriterion("rp.aeroportoByIdAeroportoDestino.id", restricaoRotaDestino.getIdAeroporto()));
		}
				
		detachedCriteria.add(getCriterion("rp.filialByIdFilialDestino.id", restricaoRotaDestino.getIdFilial()));
		detachedCriteria.add(getCriterion("rp.municipioByIdMunicipioDestino.id", restricaoRotaDestino.getIdMunicipio()));

		/*Grupo região destino*/
		if(restricaoRotaDestino.getIdGrupoRegiao() != null){
			detachedCriteria.add(getCriterion("rp.grupoRegiaoDestino.id",restricaoRotaDestino.getIdGrupoRegiao()));
		}else{
			detachedCriteria.add(Restrictions.isNull("rp.grupoRegiaoDestino.id"));
		}		
		
		
		/*------------------------------------------------- ORDER --------------------------------------------------*/
		detachedCriteria.addOrder(Order.asc("rp.zonaByIdZonaOrigem.id"));
		detachedCriteria.addOrder(Order.asc("rp.paisByIdPaisOrigem.id"));
		detachedCriteria.addOrder(Order.asc("rp.unidadeFederativaByIdUfOrigem.id"));
		detachedCriteria.addOrder(Order.asc("rp.filialByIdFilialOrigem.id"));
		detachedCriteria.addOrder(Order.asc("rp.municipioByIdMunicipioOrigem.id"));
		detachedCriteria.addOrder(Order.asc("rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.id"));
		detachedCriteria.addOrder(Order.asc("rp.aeroportoByIdAeroportoOrigem.id"));

		detachedCriteria.addOrder(Order.asc("rp.zonaByIdZonaDestino.id"));
		detachedCriteria.addOrder(Order.asc("rp.paisByIdPaisDestino.id"));
		detachedCriteria.addOrder(Order.asc("rp.unidadeFederativaByIdUfDestino.id"));
		detachedCriteria.addOrder(Order.asc("rp.filialByIdFilialDestino.id"));
		detachedCriteria.addOrder(Order.asc("rp.municipioByIdMunicipioDestino.id"));
		detachedCriteria.addOrder(Order.asc("rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.id"));
		detachedCriteria.addOrder(Order.asc("rp.aeroportoByIdAeroportoDestino.id"));
		
	}

	public static Criterion getCriterion(String propertyName, Long value) {
		if(value != null)
			return Restrictions.or(Restrictions.eq(propertyName, value), Restrictions.isNull(propertyName));
		else
			return Restrictions.isNull(propertyName);
	}

	public static Criterion getCriterionForceNull(String propertyName, Long value) {
		if(value != null)
			return Restrictions.eq(propertyName, value);
		else
			return Restrictions.isNull(propertyName);
	}

	public static RestricaoRota getRestricaoRotaOrigem(ParametroCliente parametroCliente) {
		RestricaoRota restricaoRota = new RestricaoRota();

		if(parametroCliente.getZonaByIdZonaOrigem() != null) {
			restricaoRota.setIdZona(parametroCliente.getZonaByIdZonaOrigem().getIdZona());
		}
		if(parametroCliente.getPaisByIdPaisOrigem() != null) {
			restricaoRota.setIdPais(parametroCliente.getPaisByIdPaisOrigem().getIdPais());
		}
		if(parametroCliente.getUnidadeFederativaByIdUfOrigem() != null) {
			restricaoRota.setIdUnidadeFederativa(parametroCliente.getUnidadeFederativaByIdUfOrigem().getIdUnidadeFederativa());
		}
		if(parametroCliente.getFilialByIdFilialOrigem() != null) {
			restricaoRota.setIdFilial(parametroCliente.getFilialByIdFilialOrigem().getIdFilial());
		}
		if(parametroCliente.getMunicipioByIdMunicipioOrigem() != null) {
			restricaoRota.setIdMunicipio(parametroCliente.getMunicipioByIdMunicipioOrigem().getIdMunicipio());
		}
		if(parametroCliente.getAeroportoByIdAeroportoOrigem() != null) {
			restricaoRota.setIdAeroporto(parametroCliente.getAeroportoByIdAeroportoOrigem().getIdAeroporto());
		}
		if(parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem() != null) {
			restricaoRota.setIdTipoLocalizacao(parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem().getIdTipoLocalizacaoMunicipio());
		}

		return restricaoRota;
	}

	public static RestricaoRota getRestricaoRotaDestino(ParametroCliente parametroCliente) {
		RestricaoRota restricaoRota = new RestricaoRota();

		if(parametroCliente.getZonaByIdZonaDestino() != null) {
			restricaoRota.setIdZona(parametroCliente.getZonaByIdZonaDestino().getIdZona());
		}
		if(parametroCliente.getPaisByIdPaisDestino() != null) {
			restricaoRota.setIdPais(parametroCliente.getPaisByIdPaisDestino().getIdPais());
		}
		if(parametroCliente.getUnidadeFederativaByIdUfDestino() != null) {
			restricaoRota.setIdUnidadeFederativa(parametroCliente.getUnidadeFederativaByIdUfDestino().getIdUnidadeFederativa());
		}
		if(parametroCliente.getFilialByIdFilialDestino() != null) {
			restricaoRota.setIdFilial(parametroCliente.getFilialByIdFilialDestino().getIdFilial());
		}
		if(parametroCliente.getMunicipioByIdMunicipioDestino() != null) {
			restricaoRota.setIdMunicipio(parametroCliente.getMunicipioByIdMunicipioDestino().getIdMunicipio());
		}
		if(parametroCliente.getAeroportoByIdAeroportoDestino() != null) {
			restricaoRota.setIdAeroporto(parametroCliente.getAeroportoByIdAeroportoDestino().getIdAeroporto());
		}
		if(parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino() != null) {
			restricaoRota.setIdTipoLocalizacao(parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino().getIdTipoLocalizacaoMunicipio());
		}

		return restricaoRota;
	}

	public static RestricaoRota getRestricaoRotaOrigem(RotaPreco rotaPreco) {
		RestricaoRota restricaoRota = new RestricaoRota();

		if(rotaPreco.getZonaByIdZonaOrigem() != null) {
			restricaoRota.setIdZona(rotaPreco.getZonaByIdZonaOrigem().getIdZona());
		}
		if(rotaPreco.getPaisByIdPaisOrigem() != null) {
			restricaoRota.setIdPais(rotaPreco.getPaisByIdPaisOrigem().getIdPais());
		}
		if(rotaPreco.getUnidadeFederativaByIdUfOrigem() != null) {
			restricaoRota.setIdUnidadeFederativa(rotaPreco.getUnidadeFederativaByIdUfOrigem().getIdUnidadeFederativa());
		}
		if(rotaPreco.getFilialByIdFilialOrigem() != null) {
			restricaoRota.setIdFilial(rotaPreco.getFilialByIdFilialOrigem().getIdFilial());
		}
		if(rotaPreco.getMunicipioByIdMunicipioOrigem() != null) {
			restricaoRota.setIdMunicipio(rotaPreco.getMunicipioByIdMunicipioOrigem().getIdMunicipio());
		}
		if(rotaPreco.getAeroportoByIdAeroportoOrigem() != null) {
			restricaoRota.setIdAeroporto(rotaPreco.getAeroportoByIdAeroportoOrigem().getIdAeroporto());
		}
		if(rotaPreco.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem() != null) {
			restricaoRota.setIdTipoLocalizacao(rotaPreco.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem().getIdTipoLocalizacaoMunicipio());
		}

		return restricaoRota;
	}

	public static RestricaoRota getRestricaoRotaDestino(RotaPreco rotaPreco) {
		RestricaoRota restricaoRota = new RestricaoRota();

		if(rotaPreco.getZonaByIdZonaDestino() != null) {
			restricaoRota.setIdZona(rotaPreco.getZonaByIdZonaDestino().getIdZona());
		}
		if(rotaPreco.getPaisByIdPaisDestino() != null) {
			restricaoRota.setIdPais(rotaPreco.getPaisByIdPaisDestino().getIdPais());
		}
		if(rotaPreco.getUnidadeFederativaByIdUfDestino() != null) {
			restricaoRota.setIdUnidadeFederativa(rotaPreco.getUnidadeFederativaByIdUfDestino().getIdUnidadeFederativa());
		}
		if(rotaPreco.getFilialByIdFilialDestino() != null) {
			restricaoRota.setIdFilial(rotaPreco.getFilialByIdFilialDestino().getIdFilial());
		}
		if(rotaPreco.getMunicipioByIdMunicipioDestino() != null) {
			restricaoRota.setIdMunicipio(rotaPreco.getMunicipioByIdMunicipioDestino().getIdMunicipio());
		}
		if(rotaPreco.getAeroportoByIdAeroportoDestino() != null) {
			restricaoRota.setIdAeroporto(rotaPreco.getAeroportoByIdAeroportoDestino().getIdAeroporto());
		}
		if(rotaPreco.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino() != null) {
			restricaoRota.setIdTipoLocalizacao(rotaPreco.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino().getIdTipoLocalizacaoMunicipio());
		}

		return restricaoRota;
	}

	public static boolean isRotaValida(RestricaoRota restricaoRota) {

		boolean zona = (restricaoRota.getIdZona() != null);
		boolean pais = (restricaoRota.getIdPais() != null);
		boolean uf = (restricaoRota.getIdUnidadeFederativa() != null);
		boolean filial = (restricaoRota.getIdFilial() != null);
		boolean municipio = (restricaoRota.getIdMunicipio() != null);
		boolean aeroporto = (restricaoRota.getIdAeroporto() != null);
		boolean tipoLocalizacao = (restricaoRota.getIdTipoLocalizacao() != null);
		boolean grupoRegiao = (restricaoRota.getIdGrupoRegiao() != null);

		if(!zona) {
			return false;
		}

		if(pais) {
			if(!zona) {
				return false;
			}
		}

		if(uf) {
			if(!zona || !pais) {
				return false;
			}
		}

		if(filial) {
			if(!zona || !pais || !uf || aeroporto || tipoLocalizacao) {
				return false;
			}
		}

		if(municipio) {
			if(!zona || !pais || !uf || !filial || aeroporto || tipoLocalizacao) {
				return false;
			}
		}

		if(aeroporto) {
			if(!zona || !pais || !uf || filial || municipio || tipoLocalizacao) {
				return false;
			}
		}

		if(tipoLocalizacao) {
			if(!zona || !pais || !uf || filial || municipio || aeroporto) {
				return false;
			}
		}

		if (grupoRegiao){
			if(!zona || !pais || !uf) {
				return false;
			}
		}

		return true;
	}
	
	public static String formatRotaPreco(String sgUfOrigem, String sgUfDestino, String sgAeroportoOrigem, String sgAeroportoDestino) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(sgUfOrigem)) {
			sb.append(sgUfOrigem);
			sb.append("/");
		}
		if (StringUtils.isNotBlank(sgAeroportoOrigem)) {
			sb.append(sgAeroportoOrigem);
		}
		sb.append(" - ");
		if (StringUtils.isNotBlank(sgUfDestino)) {
			sb.append(sgUfDestino);
			sb.append("/");
		}
		if (StringUtils.isNotBlank(sgAeroportoDestino)) {
			sb.append(sgAeroportoDestino);
		}
		
		return sb.toString();
	}

	public static Criterion getCriterionLocal(RestricaoRota restricaoRota, String propertyName){
				
		Criterion criterion = null;
		
		if(restricaoRota.getIdTipoLocalizacao() != null 
				&& restricaoRota.getListLocalOperacional() != null 
					&& !restricaoRota.getListLocalOperacional().isEmpty()){
			
			Long interior = restricaoRota.getIdByDsTipoLocalizacao("Interior");
			Long gCapital = restricaoRota.getIdByDsTipoLocalizacao("Grande Capital");
			Long capital  = restricaoRota.getIdByDsTipoLocalizacao("Capital");
			
			Long[] idLocal = null;
			if(capital.equals(restricaoRota.getIdTipoLocalizacao())){
				idLocal =  new Long[]{capital,gCapital,interior};
			}else if(gCapital.equals(restricaoRota.getIdTipoLocalizacao())){
				idLocal =  new Long[]{gCapital,interior};
			}else if(interior.equals(restricaoRota.getIdTipoLocalizacao())){
				idLocal =  new Long[]{interior};
}
			
			criterion = Restrictions.or(Restrictions.in(propertyName, idLocal), Restrictions.isNull(propertyName));
			
		}
		return criterion;
	}
	
	public static void addTipoLocalizacaoMunicipio(RestricaoRota restricaoRota, DetachedCriteria detachedCriteria, String propertyName){

		if(restricaoRota.getVerificarLocalizacaoRota()){
			
			Criterion restrictionOrigem = getCriterionLocal(restricaoRota, propertyName);
			
			if(restrictionOrigem != null){
				detachedCriteria.add(restrictionOrigem);
			}
			
		}else{			
			detachedCriteria.add(getCriterion(propertyName, restricaoRota.getIdTipoLocalizacao()));								
		}		
	}		
}
