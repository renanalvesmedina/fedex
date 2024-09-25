package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.TelefoneContatoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DescritivoPce;
import com.mercurio.lms.vendas.model.HistoricoPce;
import com.mercurio.lms.vendas.model.VersaoDescritivoPce;
import com.mercurio.lms.vendas.model.dao.VersaoDescritivoPceDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.versaoDescritivoPceService"
 */
public class VersaoDescritivoPceService extends CrudService<VersaoDescritivoPce, Long> {

	private TelefoneContatoService telefoneContatoService;
	private HistoricoPceService historicoPceService;
	private ConfiguracoesFacade configuracoesFacade;
	private VersaoPceService versaoPceService;
	/**
	 * Recupera uma instância de <code>VersaoDescritivoPce</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public VersaoDescritivoPce findById(java.lang.Long id) {
		return (VersaoDescritivoPce)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public List findEnderecoByContanto(Long idContato) {
		if (idContato == null)
			return new ArrayList();
		return getVersaoDescritivoPceDAO().findEnderecoByContanto(idContato);
	}

	//FIXME Não é necessário utilizar um Map na assinatura do método. Trocar para Long.
	public TypedFlatMap findDataAlertPCE(TypedFlatMap criteria) {
		TypedFlatMap retorno = new TypedFlatMap();
		List rs = getVersaoDescritivoPceDAO().findDataAlertPCE(criteria.getLong("idVersaoDescritivoPce"));
		if (rs.size() > 0) {
			Object[] projections = (Object[])rs.get(0);
			retorno.put("versaoPce.cliente.idCliente",((Long)projections[0]).toString());
			retorno.put("versaoPce.cliente.pessoa.nrIdentificacao",FormatUtils.formatIdentificacao(((DomainValue)projections[10]).getValue(),(String)projections[1]));
			retorno.put("versaoPce.cliente.pessoa.nmPessoa",(String)projections[2]);
			retorno.put("dsDescritivoPce",((VarcharI18n)projections[3]).getValue(LocaleContextHolder.getLocale()));
			retorno.put("usuario.nmUsuario",SessionUtils.getUsuarioLogado().getNmUsuario());
			retorno.put("idVersaoDescritivoPce",((Long)projections[9]).toString());

			Iterator ie = rs.iterator();
			StringBuffer sbC = new StringBuffer();
			int x = 0;
			while(ie.hasNext()) {
			projections = (Object[])ie.next();
			StringBuffer sb = new StringBuffer();
			if (projections[5] != null) {
			  //Concatena informações do usuário
			  if (projections[11] != null){
				  sb.append((String)projections[12]+" - "+(String)projections[13]);
				  sb.append(((projections[14] != null) ? " - "+((DomainValue)projections[5]).getDescription().getValue(LocaleContextHolder.getLocale())+ ": "+(String)projections[14] : ""));
			  } else {  
				  //Concatena informações do contato
					sb.append(((projections[6] != null) ? (String)projections[6] : ""));
					if (projections[4] != null)
						sb.append(" - ").append(configuracoesFacade.getMensagem("regiao")).append(": ").append(projections[4]);

					sb.append(" - ").append(((DomainValue)projections[5]).getDescription().getValue(LocaleContextHolder.getLocale()))
						.append(": ");

					if (((DomainValue)projections[5]).getValue().equalsIgnoreCase("T")) {
						//TELEFONE
						List rsC = telefoneContatoService.findTelefoneByContatoAndTpUso((Long)projections[8],new String[] {"FO","FF"});
						if (rsC.size() > 0) {
							Object[] pC = (Object[])rsC.get(0);
							if (pC[0] != null)
								sb.append("+").append(pC[0]).append(" ");
							if (pC[1] != null)
								sb.append("(").append(pC[1]).append(") ");
							if (pC[2] != null)
								sb.append(pC[2]);
						}
					}else if (((DomainValue)projections[5]).getValue().equalsIgnoreCase("F")) {
						//FAX
						List rsC = telefoneContatoService.findTelefoneByContatoAndTpUso((Long)projections[8],new String[] {"FA","FF"});
						if (rsC.size() > 0) {
							Object[] pC = (Object[])rsC.get(0);
							if (pC[0] != null)
								sb.append("+").append(pC[0]).append(" ");
							if (pC[1] != null)
								sb.append("(").append(pC[1]).append(") ");
							if (pC[2] != null)
								sb.append(pC[2]);
						}
					}else if (((DomainValue)projections[5]).getValue().equalsIgnoreCase("C")) {
						//CORRESPONDENCIA
						List rsE = findEnderecoByContanto((Long)projections[8]);
						if (rsE.size() > 0) {
							Object[] pE = (Object[]) rsE.get(0);
							if (pE[8] != null)
								sb.append(pE[8]).append(" ");
							if (pE[0] != null)
								sb.append(pE[0]);
							if (pE[1] != null)
								sb.append(", ").append(pE[1]);
							if (pE[2] != null)
								sb.append(" - ").append(configuracoesFacade.getMensagem("complemento")).append(": ").append(pE[2]);
							if (pE[3] != null)
								sb.append(" - ").append(configuracoesFacade.getMensagem("bairro")).append(": ").append(pE[3]);
							if (pE[4] != null)
								sb.append(" - ").append(configuracoesFacade.getMensagem("cep")).append(": ").append(pE[4]);
							if (pE[5] != null)
								sb.append(" - ").append(configuracoesFacade.getMensagem("municipio")).append(": ").append(pE[5]);
							if (pE[6] != null)
								sb.append(" - ").append(configuracoesFacade.getMensagem("uf")).append(": ").append(pE[6]);
							if (pE[7] != null)
								sb.append(" - ").append(configuracoesFacade.getMensagem("pais")).append(": ").append(pE[7]);
						}
						
					}else if (((DomainValue)projections[5]).getValue().equalsIgnoreCase("E")) {
						//EMAIL
						if (projections[7] != null)
							sb.append(projections[7]);
					}else if (((DomainValue)projections[5]).getValue().equalsIgnoreCase("S")) {
						List telefonesContato = versaoPceService.findTelefoneContatoByTpTelefone((Long)projections[8],new String[] {"E"});
						if (telefonesContato.size() > 0) {
							TelefoneEndereco bean = ((TelefoneContato)telefonesContato.get(0)).getTelefoneEndereco();
							sb.append(FormatUtils.formatTelefone(bean.getNrTelefone(),bean.getNrDdd(),bean.getNrDdi()));
						}
					}
					//sbC.append(((x == 0) ? "" : "\n")).append(sb.toString());
					//x++;
				}//fim teste para saber se era usuario
			  	sbC.append(((x == 0) ? "" : "\n")).append(sb.toString());
			  	x++;
			  }//fim teste projeção do tpComunicacao
			}//fim while
			retorno.put("versaoContatoPces",sbC.toString());
		}
		return retorno;
	}

	public void executeConfirmaRecebimentoDoAlerta(Long idVersaoDescritivoPce,Long idFilial) {
		if (idVersaoDescritivoPce == null)
			throw new IllegalArgumentException("idVersaoDescritivoPce deve ser informado obrigatóriamente!");
		if (idFilial == null)
			idFilial = ((Filial)SessionUtils.getFiliaisUsuarioLogado().get(0)).getIdFilial();
		List rs = getVersaoDescritivoPceDAO().findToConfirmaRecebimentoDoAlerta(idVersaoDescritivoPce);
		if (rs.size() > 0) {
			Object[] projections = (Object[])rs.get(0);
			Cliente cliente = new Cliente();
					cliente.setIdCliente((Long)projections[0]);
			DescritivoPce descritivoPce = new DescritivoPce();
						  descritivoPce.setIdDescritivoPce((Long)projections[1]);
			Filial filial = new Filial();
				   filial.setIdFilial(idFilial);
			Usuario usuario = new Usuario();
					usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());

			HistoricoPce hp = new HistoricoPce();
						 hp.setCliente(cliente);
						 hp.setDescritivoPce(descritivoPce);
						 hp.setFilial(filial);
						 hp.setUsuario(usuario);
						 hp.setDhOcorrencia(JTDateTimeUtils.getDataHoraAtual());

			historicoPceService.store(hp);
		}
	}

	public String executeFindMensagemPce(Long idCliente, Long cdProcesso, Long cdEvento, Long cdOcorrencia) {
		try {
			validateifExistPceByCriteria(idCliente, cdProcesso, cdEvento, cdOcorrencia);
			return "";
		}catch (BusinessException be) {
			Long idVersaoDescritivoPce = ((Long[])be.getMessageArguments())[0];
			executeConfirmaRecebimentoDoAlerta(idVersaoDescritivoPce,null);
			List rs = findDsDescritoById(idVersaoDescritivoPce);
			return ((VarcharI18n)rs.get(0)).getValue(LocaleContextHolder.getLocale());
		}
	}

	public List findDsDescritoById(Long idVersaoDescritivoPce) {
		return getVersaoDescritivoPceDAO().findDsDescritoById(idVersaoDescritivoPce);
	}

	public Long validateifExistPceByCriteria(Long idCliente, Long cdProcesso, Long cdEvento, Long cdOcorrencia) {
		List rs = getVersaoDescritivoPceDAO().findToifExistPceByCriteria(idCliente,cdProcesso,cdEvento,cdOcorrencia);
		if (rs.size() == 0)
			return null;
		Object[] projections = (Object[])rs.get(0);
		if (((DomainValue)projections[1]).getValue().equalsIgnoreCase("B"))
			throw new BusinessException("LMS-01097",new Long[]{(Long)projections[0]});
		return ((Long)projections[0]);
	}
	
	/**
	 * Busca um objeto VersaoDescritivoPce com base nos critérios informados.
	 * @param idCliente
	 * @param cdProcesso
	 * @param cdEvento
	 * @param cdOcorrencia
	 * @return
	 */
	public VersaoDescritivoPce findVersaoDescritivoPceByCriteria(Long idCliente, Long cdProcesso, Long cdEvento, Long cdOcorrencia) {
		return getVersaoDescritivoPceDAO().findVersaoDescritivoPceByCriteria(idCliente,cdProcesso,cdEvento,cdOcorrencia);
	}
	
	/**
	 * Busca o código do PCE com base nos critérios informados.
	 * @param idCliente
	 * @param cdProcesso
	 * @param cdEvento
	 * @param cdOcorrencia
	 * @return
	 */
	public Long findCodigoVersaoDescritivoPceByCriteria(Long idCliente, Long cdProcesso, Long cdEvento, Long cdOcorrencia) {
		return getVersaoDescritivoPceDAO().findIdVersaoDescritivoPceByCriteria(idCliente,cdProcesso,cdEvento,cdOcorrencia);
		}

	/**
	 * <p>Faz a validacao do PCE. Retorna uma <code>TypedFlatMap</code> contendo</p>
	 * <p>
	 * <b>id</b> - id do cliente a ser validado;<br>
	 * <b>codigo</b> - codigo informado pela valição;<br>
	 * <b>valido</b> - caso ele seja valido ou não;
	 * </p>
	 * 
	 * @param idPedidoColeta
	 * @return TypedFlatMap contendo a estrutura acima.
	 */
	public TypedFlatMap validatePCE(Long idCliente, Long idProcesso, Long idEvento, Long idOcorrencia) {
		TypedFlatMap retorno = new TypedFlatMap();
		try {
			Long codigo = this.validateifExistPceByCriteria(idCliente, idProcesso, idEvento, idOcorrencia);
			if (codigo != null) {
				retorno.put("id", idCliente);
				retorno.put("codigo", codigo);
				retorno.put("valido", true);
			}
		} catch(BusinessException bex) {
			Long[] codigo = (Long[]) bex.getMessageArguments();
			if (codigo.length>=1) {
				retorno.put("id", idCliente);
				retorno.put("codigo", codigo[0]);
				retorno.put("valido", false);
			}
		}
		return retorno;
	}

	
	
	/**
     * Verifica se o cliente do documento servico em um manifesto tem VersaoDescritivoPce
     * @param idManifesto
     * @param tpManifesto
     * @param tpAcao
     * @param cdProcesso
     * @param cdEvento
     * @param cdOcorrencia
     * @return
     */
    public List <Long> findVersaoDescritivoPcebyManifestoAndTpAcao(Long idManifesto, String tpAcao, Long cdProcesso, 
    		Long cdEvento, Long cdOcorrencia){
    	return getVersaoDescritivoPceDAO().findVersaoDescritivoPcebyManifestoAndTpAcao(idManifesto, tpAcao,
    							cdProcesso, cdEvento, cdOcorrencia);
    }
	
	
	/**
	 * Retorna mensagem PCE se encontrar, e se não for bloqueio.
	 * Se for bloqueio, uma BusinessException é lançada para ser tratada.
	 * @param idCliente
	 * @param cdProcesso
	 * @param cdEvento
	 * @param cdOcorrencia
	 * @return String com mensagem do PCE.
	 */
	public String findMensagemPceAndValidateBloqueio(Long idCliente, Long cdProcesso, Long cdEvento, Long cdOcorrencia) {
		List rs = getVersaoDescritivoPceDAO().findToifExistPceByCriteria(idCliente,cdProcesso,cdEvento,cdOcorrencia);
		if (rs.size() == 0)
			return "";

		// Se tpAcao é bloqueio, é jogado uma exeção que deve ser tratada.
		Object[] projections = (Object[])rs.get(0);
		if (((DomainValue)projections[1]).getValue().equalsIgnoreCase("B"))
			throw new BusinessException("LMS-01097",new Long[]{(Long)projections[0]});

		// Caso não seja bloqueio, é retornado a mensagem do PCE.
		rs = findDsDescritoById((Long)projections[0]);
		return ((VarcharI18n)rs.get(0)).getValue(LocaleContextHolder.getLocale());
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(VersaoDescritivoPce bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setVersaoDescritivoPceDAO(VersaoDescritivoPceDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private final VersaoDescritivoPceDAO getVersaoDescritivoPceDAO() {
		return (VersaoDescritivoPceDAO) getDao();
	}

	public void setTelefoneContatoService(TelefoneContatoService telefoneContatoService) {
		this.telefoneContatoService = telefoneContatoService;
	}

	public void setHistoricoPceService(HistoricoPceService historicoPceService) {
		this.historicoPceService = historicoPceService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setVersaoPceService(VersaoPceService versaoPceService) {
		this.versaoPceService = versaoPceService;
	}

}