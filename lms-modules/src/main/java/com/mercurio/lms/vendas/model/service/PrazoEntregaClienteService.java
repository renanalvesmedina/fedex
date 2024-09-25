package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.vendas.model.PrazoEntregaCliente;
import com.mercurio.lms.vendas.model.dao.PrazoEntregaClienteDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.prazoEntregaClienteService"
 */
public class PrazoEntregaClienteService extends CrudService<PrazoEntregaCliente, Long> {

	private Logger log = LogManager.getLogger(this.getClass());
	/** usando a service de municipio */ 
	private MunicipioFilialService municipioFilialService;

	public MunicipioFilialService getMunicipioFilialService() {
		return municipioFilialService;
	}

	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	/**
	 * Recupera uma inst�ncia de <code>PrazoEntregaCliente</code> a partir do ID.
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public PrazoEntregaCliente findById(java.lang.Long id) {
		return (PrazoEntregaCliente)super.findById(id);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getPrazoEntregaClienteDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List<PrazoEntregaCliente> resultado = rsp.getList();
		TypedFlatMap tfm = new TypedFlatMap();
		List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>(resultado.size());

		for(PrazoEntregaCliente pec : resultado) {
			tfm = new TypedFlatMap();
			tfm.put("idPrazoEntregaCliente",pec.getIdPrazoEntregaCliente());
			if( pec.getUnidadeFederativaByIdUfOrigem() != null) {
				tfm.put("unidadeFederativaByIdUfOrigem.sgUnidadeFederativa",pec.getUnidadeFederativaByIdUfOrigem().getSgUnidadeFederativa());
			}
			if( pec.getFilialByIdFilialOrigem() != null) {
				tfm.put("filialByIdFilialOrigem.sgFilial",pec.getFilialByIdFilialOrigem().getSgFilial());
			}
			if( pec.getMunicipioByIdMunicipioOrigem() != null) {
				tfm.put("municipioByIdMunicipioOrigem.nmMunicipio",pec.getMunicipioByIdMunicipioOrigem().getNmMunicipio());
			}
			if( pec.getAeroportoByIdAeroportoOrigem() != null) {
				tfm.put("aeroportoByIdAeroportoOrigem.pessoa.nmPessoa",pec.getAeroportoByIdAeroportoOrigem().getPessoa().getNmPessoa());
			}
			if( pec.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem() != null) {
				tfm.put("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio",pec.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem().getDsTipoLocalizacaoMunicipio());
			}
			if( pec.getUnidadeFederativaByIdUfDestino() != null) {
				tfm.put("unidadeFederativaByIdUfDestino.sgUnidadeFederativa",pec.getUnidadeFederativaByIdUfDestino().getSgUnidadeFederativa());
			}
			if( pec.getFilialByIdFilialDestino() != null) {
				tfm.put("filialByIdFilialDestino.sgFilial",pec.getFilialByIdFilialDestino().getSgFilial());
			}
			if( pec.getMunicipioByIdMunicipioDestino() != null) {
				tfm.put("municipioByIdMunicipioDestino.nmMunicipio",pec.getMunicipioByIdMunicipioDestino().getNmMunicipio());
			}
			if( pec.getAeroportoByIdAeroportoDestino() != null) {
				tfm.put("aeroportoByIdAeroportoDestino.pessoa.nmPessoa",pec.getAeroportoByIdAeroportoDestino().getPessoa().getNmPessoa());
			}
			if( pec.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino() != null) {
				tfm.put("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio",pec.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino().getDsTipoLocalizacaoMunicipio());
			}
			if( pec.getZonaByIdZonaOrigem() != null) {
				tfm.put("zonaByIdZonaOrigem.dsZona",pec.getZonaByIdZonaOrigem().getDsZona());
			}
			if( pec.getPaisByIdPaisOrigem() != null) {
				tfm.put("paisByIdPaisOrigem.nmPais",pec.getPaisByIdPaisOrigem().getNmPais());
			}			
			if( pec.getZonaByIdZonaDestino() != null) {
				tfm.put("zonaByIdZonaDestino.dsZona",pec.getZonaByIdZonaDestino().getDsZona());
			}
			if( pec.getPaisByIdPaisDestino() != null) {					
				tfm.put("paisByIdPaisDestino.nmPais",pec.getPaisByIdPaisDestino().getNmPais());
			}
			retorno.add(tfm);
		}

		rsp.setList(retorno);
		return rsp;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(PrazoEntregaCliente bean) {
		return super.store(bean);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 * @param id indica a entidade que dever� ser removida.
	 */
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @param Inst�ncia do DAO.
	 */
	public void setPrazoEntregaClienteDAO(PrazoEntregaClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @return Inst�ncia do DAO.
	 */
	private PrazoEntregaClienteDAO getPrazoEntregaClienteDAO() {
		return (PrazoEntregaClienteDAO) getDao();
	}

	/**
	 * Valida se Zona, Pais e Unidade Federativa do objeto PrazoEntregueCliente
	 * informado s�o diferente de nullo, sen�o retorna false.
	 * */
	private boolean validarPrazoEntregueCliente(PrazoEntregaCliente pec, String strInterface) {
		try {
			Object[] args = null;
			//Se o municipio foi informado
			Method getMunicipioByIdMunicipio = ReflectionUtils.getMethod(pec.getClass(), "getMunicipioByIdMunicipio" + strInterface);
			if(getMunicipioByIdMunicipio.invoke(pec, args) != null) {
				//Se a filial n�o foi informado
				Method getFilialByIdFilial = ReflectionUtils.getMethod(pec.getClass(), "getFilialByIdFilial" + strInterface);
				if (getFilialByIdFilial == null) {
					//Dar erro
					return false;
				}
			}
			//Se a filial foi informado
			Method getFilialByIdFilial = ReflectionUtils.getMethod(pec.getClass(), "getFilialByIdFilial" + strInterface);
			if(getFilialByIdFilial.invoke(pec, args) != null) {
				//Se o municipio foi informado
				if (getMunicipioByIdMunicipio == null) {
					//Dar erro
					return false;
				}
			}
			Method getAeroportoByIdAeroporto = ReflectionUtils.getMethod(pec.getClass(), "getAeroportoByIdAeroporto" + strInterface);
			Method getTipoLocalizacaoMunicipioByIdTipoLocalizacao = ReflectionUtils.getMethod(pec.getClass(),"getTipoLocalizacaoMunicipioByIdTipoLocalizacao"+strInterface);
			if(getAeroportoByIdAeroporto.invoke(pec, args) != null || getFilialByIdFilial.invoke(pec, args) != null || getTipoLocalizacaoMunicipioByIdTipoLocalizacao.invoke(pec, args) != null) {
				if (validarObrigatoriedadeZonaPaisUF(pec,strInterface) == false){
					return false;
				}
			}	
		} catch (Exception e) {
			log.error(e);
		}
		return true;
	}

	/**
	 * Valida se Zona, Pais e Unidade Federativa do objeto PrazoEntregueCliente
	 * informado s�o diferente de nullo, sen�o retorna false.
	 * */
	private boolean validarObrigatoriedadeZonaPaisUF(PrazoEntregaCliente pec, String strInterface) {
		try {
			Object[] args = null;
			Method getZonaByIdZona = ReflectionUtils.getMethod(pec.getClass(),"getZonaByIdZona"+strInterface);
			if(getZonaByIdZona.invoke(pec, args) == null) {
				return false;
			}

			Method getPaisByIdPais = ReflectionUtils.getMethod(pec.getClass(),"getPaisByIdPais"+strInterface);
			if(getPaisByIdPais.invoke(pec, args) == null) {
				return false;
			}

			Method getUnidadeFederativaByIdUf = ReflectionUtils.getMethod(pec.getClass(),"getUnidadeFederativaByIdUf"+strInterface);
			if(getUnidadeFederativaByIdUf.invoke(pec, args) == null) {
				return false;
			}
		} catch (Exception e) {
			log.error(e);
		}
		return true;
	}

	public PrazoEntregaCliente findPrazoEntregaCliente(
		Long idCliente,
		Long idServico,
		RestricaoRota restricaoRotaOrigem,
		RestricaoRota restricaoRotaDestino
	) {
		return getPrazoEntregaClienteDAO().findPrazoEntregaCliente(idCliente, idServico, restricaoRotaOrigem, restricaoRotaDestino);
	}

	/**
	 * Metodo chamado antes do store ser executado
	 */
	@Override
	protected PrazoEntregaCliente beforeStore(PrazoEntregaCliente bean) {
		boolean blValido = validarPrazoEntregueCliente(bean,"Origem");
		if (blValido == true) {
			blValido = validarPrazoEntregueCliente(bean,"Destino");
		}
		if (blValido == false){
			throw new BusinessException("LMS-00041");
		}

		//VALIDA SE OS MUNICIPIOS ESTAO VIGENTES
		//origem
		if(bean.getMunicipioByIdMunicipioOrigem() != null && bean.getFilialByIdFilialOrigem() != null) {
			boolean blMunicipioOrigemVigente = getMunicipioFilialService().verificaVigenciaMunicipioByFilialMunicipio(
					bean.getFilialByIdFilialOrigem().getIdFilial(),
					bean.getMunicipioByIdMunicipioOrigem().getIdMunicipio());
			if(!blMunicipioOrigemVigente)
				throw new BusinessException("LMS-29022");
		}

		//destino
		if(bean.getMunicipioByIdMunicipioDestino() != null && bean.getFilialByIdFilialDestino() != null) {
			boolean blMunicipioDestinoVigente = getMunicipioFilialService().verificaVigenciaMunicipioByFilialMunicipio(
					bean.getFilialByIdFilialDestino().getIdFilial(),
					bean.getMunicipioByIdMunicipioDestino().getIdMunicipio());
			if(!blMunicipioDestinoVigente)
				throw new BusinessException("LMS-29022");
		}
		//LMS-344 - Esta permitindo incluir prazo de entrega zero para os clientes.
		if(bean.getNrPrazo() == 0L){
			throw new BusinessException("LMS-01200");
		}
		
		return bean;
	}
	
}
