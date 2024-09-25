package com.mercurio.lms.vendas.model.service;

import java.util.List;

import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.vendas.model.HorarioCorteCliente;
import com.mercurio.lms.vendas.model.dao.HorarioCorteClienteDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.horarioCorteClienteService"
 */
public class HorarioCorteClienteService extends CrudService<HorarioCorteCliente, Long> {

	private UsuarioService usuarioService;
	private MunicipioFilialService municipioFilialService;

	public void setMunicipioFilialService(
			MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	/**
	 * Verifica permiss�o de usu�rio logado
	 * @author Robson Edemar Gehl
	 * @return
	 */
	public Boolean validatePermissaoUsuarioLogado(Long id){
		return getUsuarioService().verificaAcessoFilialRegionalUsuarioLogado(id);
	}
	
	/**
	 * Recupera uma inst�ncia de <code>HorarioCorteCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	@Override
	public HorarioCorteCliente findById(Long id) {
		return (HorarioCorteCliente)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(HorarioCorteCliente bean) {
		return super.store(bean);
	}
	
	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
		getHorarioCorteClienteDAO().removeByIdCliente(idCliente);
    }

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setHorarioCorteClienteDAO(HorarioCorteClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private HorarioCorteClienteDAO getHorarioCorteClienteDAO() {
		return (HorarioCorteClienteDAO) getDao();
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	/**
	 * M�todo que busca um HorarioCorteCliente a partir do ID do EnderecoPessoa e da Hora Atual.
	 * @param idEnderecoPessoa
	 * @param horaAtual
	 * @return HorarioCorteCliente
	 */
	public HorarioCorteCliente findHorarioCorteClienteByIdEnderecoPessoaByHoraAtual(Long idEnderecoPessoa, TimeOfDay horaAtual) {
		return this.getHorarioCorteClienteDAO().findHorarioCorteClienteByIdEnderecoPessoaByHoraAtual(idEnderecoPessoa, horaAtual);
	}

	/**
	 * Busca o Hor�rio de Corte de Coleta do Cliente
	 * @author Andre Valadas
	 * 
	 * @param idCliente
	 * @param idServico
	 * @param restricaoRota
	 * @return
	 */
	public List<HorarioCorteCliente> findHorarioCorteColeta(Long idCliente, Long idServico, RestricaoRota restricaoRota) {
		return getHorarioCorteClienteDAO().findHorarioCorteColeta(idCliente, idServico, restricaoRota);
	}

	/** 
	 * @author Jos� Rodrigo Moraes
	 * @since  15/08/2006
	 * 
	 * Metodo chamado antes do store ser executado
	 * 
	 * @param bean Objeto a ser salvo
	 * @return Objeto a ser salvo
	 */
	@Override
	protected HorarioCorteCliente beforeStore(HorarioCorteCliente bean) {
		// Valida se os munic�pios de origem e destino est�o vigentes 
		if (bean.getMunicipioOrigem() != null && bean.getMunicipioDestino() != null 
				&& bean.getFilialOrigem() != null && bean.getFilialDestino() != null) {
			if (!municipioFilialService
					.verificaVigenciaMunicipioByFilialMunicipio(bean.getFilialOrigem().getIdFilial()
							, bean.getMunicipioOrigem().getIdMunicipio())) {
				throw new BusinessException("LMS-29022");
			}
			
			if (!municipioFilialService
					.verificaVigenciaMunicipioByFilialMunicipio(bean.getFilialDestino().getIdFilial()
							, bean.getMunicipioDestino().getIdMunicipio())) {
				throw new BusinessException("LMS-29022");
		}
		}

		return bean;
	}

}
