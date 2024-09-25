package com.mercurio.lms.services.coleta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.pedidocoleta.*;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.configuracoes.model.*;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.joda.time.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.EventoColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TipoLogradouroService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.municipios.model.service.RotaIntervaloCepService;
import com.mercurio.lms.services.coleta.helper.PedidoColetaIntegracaoRestPopulateHelper;
import com.mercurio.lms.services.coleta.validate.PedidoColetaIntegracaoValidate;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vol.model.service.VolDadosSessaoService;

/**
 * LMSA-684: LMSA-6786
 */
@Path("/coleta/pedidoColetaIntegracao")
public class PedidoColetaIntegracaoRest extends BaseRest {

    @InjectInJersey
    private ParametroGeralService parametroGeralService;
    
    @InjectInJersey
    private ConteudoParametroFilialService conteudoParametroFilialService;
    
    @InjectInJersey
    private FilialService filialService;

    @InjectInJersey
    private MunicipioService municipioService;

    @InjectInJersey
    private MunicipioFilialService municipioFilialService;
    
    @InjectInJersey
    private PpeService ppeService;
    
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;
	
	@InjectInJersey
	private PessoaService pessoaService;
	
	@InjectInJersey
	private ClienteService clienteService;
	
	@InjectInJersey
	private EnderecoPessoaService enderecoPessoaService;
	
	@InjectInJersey
	private TipoLogradouroService tipoLogradouroService;

	@InjectInJersey
	private UsuarioService usuarioService;
	
	@InjectInJersey
	private PedidoColetaService pedidoColetaService;
	
	@InjectInJersey
	private DetalheColetaService detalheColetaService;
	
	@InjectInJersey
	private EventoColetaService eventoColetaService;
	
	@InjectInJersey
	private InscricaoEstadualService inscricaoEstadualService;

	@InjectInJersey
	private VolDadosSessaoService volDadosSessaoService;
	
	@InjectInJersey
	private RotaIntervaloCepService rotaIntervaloCepService;

    private static final String ParametroEmailErrosGenericos = "EMAIL_ERROS_EDI_FEDEX";
    private static final String ParametroEmailPorFilialErros = "DEST_EMAIL_ERROS_FDX";
    private static final String ParametroNumeroColetaPorFilial = "NR_COLETA";
    
    private PedidoColetaIntegracaoRestPopulateHelper populate = new PedidoColetaIntegracaoRestPopulateHelper();

    private Filial filialColeta = null;
    private Filial filialMatriz = null;
    private Cliente clienteColeta = null;
    private Municipio municipio = null;
    private EnderecoPessoa enderecoColeta = null;
    private Usuario usuarioIntegracao = null;
    private RotaIntervaloCep rotaColetaEntrega = null;

    private List<String> erros = null;
    private void addErro(String mensagem) {
    	if (mensagem != null && !mensagem.isEmpty()) {
	    	if (erros == null) {
	    		erros = new ArrayList<String>();
	    	}
	    	erros.add(mensagem);
    	}
    }
    private boolean isProcessadoComSucesso() {
    	return erros == null || erros.isEmpty();
    }

	@POST
    @Path("generatePedidoColetaFedex")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public Response generatePedidoColetaFedex(PedidoColetaIntegracaoEntradaDMN input) {
		erros = new PedidoColetaIntegracaoValidate(configuracoesFacade).validar(input);

		PedidoColetaIntegracaoRetornoDMN output = new PedidoColetaIntegracaoRetornoDMN();

		if (isProcessadoComSucesso()) {
			try {
				session = pedidoColetaService.getPedidoColetaDAO().getAdsmHibernateTemplate().getSessionFactory().openSession();			
				session.beginTransaction();
				output.setPedidoColetaRetorno(processarPedidosColeta(input.getPedidoColeta()));
				if (isProcessadoComSucesso()) {
					if (session.isOpen()) {
						session.getTransaction().commit();
					}
				} else {
					if (session.isOpen()) {
						session.getTransaction().rollback();
					}
				}
			} catch (Exception e) {
				// nada
			} finally {
				if (session != null && session.isOpen()) {
					try {
						session.flush();
						session.close();
					} catch (Exception e) {
						// nada
					}
				}
			}
		}

		output.setNomeArquivo(input.getNomeArquivo());
		output.setDataHoraProcessamento(input.getDataHoraProcessamento());
   		output.setEmailRetornoErro(recuperarEmailEnvioErrosProcessamento());
		output.setMensagensErro(erros);
		output.setProcessadoComSucesso(isProcessadoComSucesso());

    	return Response.ok(output).build();
    }
	
	private Session session = null;
	private void criarDadosSessaoIntegracao() {
		if (isProcessadoComSucesso()) {
			volDadosSessaoService.executeDadosSessaoBanco(usuarioIntegracao, filialMatriz, municipio.getUnidadeFederativa().getPais());
		}
		if (session == null) {
			addErro(configuracoesFacade.getMensagem("LMS-02134"));
		} else {
			try {
				session.close();
			} catch (Exception ignored) {
			}
		}
	}

    private String recuperarEmailEnvioErrosProcessamento() {
		String emailParaEnvioErros = null; 
		if (!isProcessadoComSucesso()) {
			if (filialColeta != null) {
				ConteudoParametroFilial emailFilialParametrizado = conteudoParametroFilialService.findByNomeParametro(
						filialColeta.getIdFilial(), ParametroEmailPorFilialErros, false, true);
				if (emailFilialParametrizado != null) {
					emailParaEnvioErros = emailFilialParametrizado.getVlConteudoParametroFilial();
				}
			}
			if (emailParaEnvioErros == null) {
				emailParaEnvioErros = (String) parametroGeralService.findConteudoByNomeParametro(ParametroEmailErrosGenericos, false);
			}
		}
		return emailParaEnvioErros;
    }

    private Long recuperarParametroFilialNumeroColeta() {
		Long numeroColeta = null; 
		if (isProcessadoComSucesso()) {
			if (filialColeta != null) {
				numeroColeta = configuracoesFacade.incrementaParametroSequencial(
						filialColeta.getIdFilial(), ParametroNumeroColetaPorFilial, true);
			}
		}
		if (numeroColeta == null) {
			addErro(configuracoesFacade.getMensagem("LMS-00075", 
					new Object[] {filialColeta.getIdFilial(), filialColeta.getSgFilial(), ParametroNumeroColetaPorFilial}) );
		}
		return numeroColeta;
    }
    
    private PedidoColetaRetornoDMN processarPedidosColeta(PedidoColetaDMN pedido) {
    	PedidoColetaRetornoDMN pedidoProcessado = new PedidoColetaRetornoDMN(
    			pedido.getNumeroColeta(),
    			pedido.getCnpjFilialColeta(),
    			pedido.getCnpjRemetente(),
    			pedido.getCepColeta(),
    			pedido.getCepDestino()
    			);

		recuperarUsuarioIntegracao();
		
        if (isProcessadoComSucesso()) {
            recuperarFilialDeColetaAPartirDoCepInformado(pedido.getCepColeta());
        }
		
        if (isProcessadoComSucesso()) {
            recuperarFilialMatriz();
        }
        
        if (isProcessadoComSucesso()) {
				criarDadosSessaoIntegracao();
				
		        if (isProcessadoComSucesso()) {
		            recuperarRotaColetaEntrega(pedido.getCepColeta());
		        }
		
				if (isProcessadoComSucesso()) {
					recuperarClienteColeta(
							pedido.getCnpjRemetente(), 
							pedido.getNomeRemetente(), 
							pedido.getTipoPessoa(),
							pedido.getCepColeta(), 
							pedido.getTipoLogradouro(),
							pedido.getLogradouroColeta(), 
							pedido.getNumeroLogradouroColeta(), 
							pedido.getNumeroIE());
				}
		
				if (isProcessadoComSucesso()) {
					criarPedidoColeta(pedido);
				}
		}
        
        if (!isProcessadoComSucesso()) {
			tratarMensagens();
        }

		return pedidoProcessado;
    }
    
    private static final String messageKey = "MESSAGEKEY";
    private static final String args = "ARGS";
    private void tratarMensagens() {
    	if (erros != null && !erros.isEmpty()) {
    		for (int indice = 0; indice < erros.size(); indice++) {
    			try {
	    			String msg = erros.get(indice);
					String[] argumentos = null;
	    			String key = msg.toUpperCase();
	    			if (key.contains(messageKey)) {
	    				if (key.contains(args)) {
	    					String auxiliar = msg.substring(key.indexOf(args)+6, key.indexOf("]"));
	    					argumentos = auxiliar.split(",");
	    				}
	    				// utilizar msg para substring sob key upper case
	   					key = msg.substring(key.indexOf(messageKey)+11);
	    				if (key.contains(" ")) {
	    					key = key.substring(0, key.indexOf(" "));
	    				} 

	    				if (argumentos != null) {
	    					key = configuracoesFacade.getMensagem(key, argumentos);
	    				} else {
	    					key = configuracoesFacade.getMensagem(key);
	    				}
	    				erros.set(indice, key);
	    			}
    			} catch (Exception ignored) {
    			}
    		}
    	}
    }
    
	private void recuperarFilialDeColetaAPartirDoCepInformado(String cepColeta) {
		try {
	    	municipio = municipioFilialService.findIdMunicipioFromViewV_CEP(cepColeta);
	    	if (municipio != null) {
	    		Map<String, Object> filialMunicipio = ppeService.findAtendimentoMunicipio(
	    				municipio.getIdMunicipio(),
						null,
						Boolean.TRUE,
						null,
						cepColeta,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null
					);
	    		
	            if (filialMunicipio != null && filialMunicipio.size() > 0) {
	                filialColeta = filialService
							.findByIdInitLazyProperties( (Long) filialMunicipio.get("idFilial"), false );
	            }
	        	if (filialColeta == null) {
	        		addErro(configuracoesFacade.getMensagem("LMS-29080", new Object[] {municipio.getNmMunicipio()}));
	        	}
	    	} else {
	    		addErro(configuracoesFacade.getMensagem("LMS-02133", new Object[] {cepColeta}));
	    	}
		} catch (Exception e) {
			addErro(e.getMessage());
		}
    }

	private static final String siglaFilialMatriz = "MTZ";
	private static final Long idEmpresaMatriz = 361L;
    private void recuperarFilialMatriz() {
        filialMatriz = filialService.findBySgFilialAndIdEmpresa(siglaFilialMatriz, idEmpresaMatriz);
        if (filialMatriz == null) {
            addErro(configuracoesFacade.getMensagem("LMS-02131", new Object[] {"Filial Matriz"}));
        }
    }

    private static final String LOGIN_USUARIO_INTEGRACAO = "integracao";
	private void recuperarUsuarioIntegracao() {
    	usuarioIntegracao = usuarioService.findUsuarioByLogin(LOGIN_USUARIO_INTEGRACAO);
    	if (usuarioIntegracao == null) {
    		addErro(configuracoesFacade.getMensagem("ADSM_INVALID_USER_EXCEPTION_KEY", new Object[] {LOGIN_USUARIO_INTEGRACAO}));
    	}
    }

    private void recuperarClienteColeta(String documento, String nome, String tipoPessoa, String cep, String tipoLogradouro, String logradouro, String numero, String numeroIE) {
    	clienteColeta = clienteService.findByNrIdentificacao(documento);
    	boolean gravarClienteColeta = clienteColeta == null;
    	if (gravarClienteColeta) {
			try {
				Pessoa pessoa = pessoaService.findByNrIdentificacao(documento);
				if (pessoa == null) {
					pessoa = new Pessoa();
					pessoa.setNrIdentificacao(documento);
					pessoa.setNmPessoa(nome);
					pessoa.setTpIdentificacao(
							new DomainValue(
									PedidoColetaIntegracaoValidate.TIPO_PESSOA_FISICA.equals(tipoPessoa) 
											? PedidoColetaIntegracaoValidate.TIPO_IDENTIFICACAO_PF : 
												PedidoColetaIntegracaoValidate.TIPO_IDENTIFICACAO_PJ)
							);
					pessoa.setTpPessoa(new DomainValue(tipoPessoa.toUpperCase()));
					pessoa.setIdPessoa((Long) pessoaService.storeSimple(pessoa));
				}

				if (isProcessadoComSucesso()) {
		    		clienteColeta = populate.getCliente(filialColeta, documento, nome);
					clienteColeta.setUsuarioByIdUsuarioInclusao(usuarioIntegracao);
					InscricaoEstadual ie = populate.getInscricaoEstadual(pessoa, numeroIE, municipio.getUnidadeFederativa());

					if (ie != null) {
						try {
							inscricaoEstadualService.store(ie);
						} catch (Exception e) {
							if (!e.getMessage().endsWith("LMS-00002")) {
								throw e;
							}
						}
					}
				    pessoa.setInscricaoEstaduais(new ArrayList<InscricaoEstadual>());
				    pessoa.getInscricaoEstaduais().add(ie);
				    //
					clienteColeta.setPessoa(pessoa);
				}
			} catch (Exception e) {
				addErro(configuracoesFacade.getMensagem("erroInesperado")+". Mensagem original: "+e.getMessage());
			}
    	}
    	
    	if (isProcessadoComSucesso()) {
    	    gravarClienteColeta = recuperarEnderecoColetaCliente(clienteColeta.getPessoa(), cep, tipoLogradouro, logradouro, numero);
			clienteColeta.getPessoa().setEnderecoPessoa(enderecoColeta);
    	}
    	
    	if (isProcessadoComSucesso() && gravarClienteColeta) {
    		try {
    			clienteColeta.setIdCliente((Long) clienteService.saveClienteBasico(clienteColeta));
			} catch (Exception e) {
				addErro(configuracoesFacade.getMensagem("erroInesperado")+". Mensagem original: "+e.getMessage());
			}
    	}
    }

    private String tipoLogradouro = null;
	private boolean recuperarEnderecoColetaCliente(Pessoa pessoa, String cep, String tipoLogradouro, String logradouro, String numero) {
		Long idTipoLogradouro = null;
    	if (pessoa != null && pessoa.getIdPessoa() != null) {
    		enderecoColeta = enderecoPessoaService.findEnderecosVigentesByIdPessoaAndCep(pessoa.getIdPessoa(), cep, true);
    		if (enderecoColeta != null) {
    			idTipoLogradouro = enderecoColeta.getTipoLogradouro().getIdTipoLogradouro();
    		}
    	}
    	
        boolean novoEndereco = enderecoColeta == null;
    	if (novoEndereco) {
			List<TipoLogradouro> tiposLogradouro = tipoLogradouroService.findListByDescricaoTipoLogradouro(tipoLogradouro);
			if (tiposLogradouro != null && !tiposLogradouro.isEmpty()) {
				try {
					idTipoLogradouro = tiposLogradouro.get(0).getIdTipoLogradouro();
					this.tipoLogradouro = tiposLogradouro.get(0).getDsTipoLogradouro() != null  
								? tiposLogradouro.get(0).getDsTipoLogradouro().toString() : tipoLogradouro;
					popularEnderecoColetaCliente(cep, tiposLogradouro.get(0), logradouro, numero);
					enderecoColeta.setPessoa(pessoa);
					enderecoPessoaService.store(enderecoColeta);
					pessoa.setEnderecoPessoa(enderecoColeta);
					pessoaService.store(pessoa);
				} catch (Exception e) {
					addErro(configuracoesFacade.getMensagem("erroInesperado")+". Mensagem original: "+e.getMessage());
				}
			} else {
				addErro(configuracoesFacade.getMensagem("LMS-02132", new Object[] {"'tipoLogradouro'"}));
			}
    	}
    	
    	if (isProcessadoComSucesso()) {
    		TipoLogradouro tipoLog = tipoLogradouroService.findById(idTipoLogradouro);
			this.tipoLogradouro = tipoLog != null && tipoLog.getDsTipoLogradouro() != null  
					? tipoLog.getDsTipoLogradouro().toString() : tipoLogradouro;
    	}
    	
    	return novoEndereco;
    }

	private void popularEnderecoColetaCliente(String cep, TipoLogradouro tipoLogradouro, String logradouro, String numero) {
		enderecoColeta = populate.getEnderecoColetaCliente(enderecoColeta, cep, logradouro, numero);
		enderecoColeta.setMunicipio(municipio);

		TipoEnderecoPessoa tipoEnderecoPessoa = populate.getTipoEnderecoColeta();
		tipoEnderecoPessoa.setEnderecoPessoa(enderecoColeta);
		
		enderecoColeta.setTipoLogradouro(tipoLogradouro);
	}

	private void criarPedidoColeta(PedidoColetaDMN pedido) {
		Long numeroColetaFilial = recuperarParametroFilialNumeroColeta();
		if (isProcessadoComSucesso()) {
			try {
				PedidoColeta coleta = populate.getPedidoColeta(
						numeroColetaFilial, 
						clienteColeta,
						enderecoColeta, this.tipoLogradouro,
						filialColeta, municipio, usuarioIntegracao, rotaColetaEntrega, pedido);
				pedidoColetaService.store(coleta);
				detalheColetaService.store(
						populate.getDetalheColeta(
								coleta, municipio, pedido.getPesoTotal(), pedido.getQuantidadeTotalVolumes(), filialColeta)
						);
				eventoColetaService.store(populate.getEventoColeta(coleta, usuarioIntegracao, pedido.getComentario()));
			} catch (Exception e) {
				addErro(configuracoesFacade.getMensagem("erroInesperado")+". Mensagem original: "+e.getMessage());
			}

		}
	}

	private void recuperarRotaColetaEntrega(String cep) {
		List<RotaIntervaloCep> resultList = rotaIntervaloCepService.findRotaIntervaloCepByCep(filialColeta, cep);
		if (resultList != null && !resultList.isEmpty()) {
			rotaColetaEntrega = resultList.get(0);
		} else {
			addErro(configuracoesFacade.getMensagem("LMS-02135", new Object[] {cep}));
		}
    }

	@POST
	@Path("generatepedidocoletabosh")
	@Produces("application/json;charset=utf-8")
	@Consumes("application/json;charset=utf-8")
	public Response generatePedidoColetaBosh(PedidoColetaBoschDMN pedidoColetaBosch){

		PedidoColetaBoschRetornoDMN pedidoColetaBoschRetorno = new PedidoColetaBoschRetornoDMN();
		erros = new ArrayList<>();
		Response response = null;

		try {
			buscarRelacionamentoPedidoColeta(pedidoColetaBosch);
			String protocolo = storePedidoColetaBosch(pedidoColetaBosch);
			pedidoColetaBoschRetorno.setProtocolo(protocolo);
			response = Response.ok(pedidoColetaBoschRetorno).build();
		}catch (Exception e){
			response = Response.status(Response.Status.BAD_REQUEST).build();
		}

		return response;
	}

	private void buscarRelacionamentoPedidoColeta(PedidoColetaBoschDMN pedidoColetaBosch) throws Exception {
		try {
			String cep = pedidoColetaBosch.getNrCep();
			clienteColeta = clienteService.findByNrIdentificacao(pedidoColetaBosch.getNrIdentificacaoCliente());
			Pessoa pessoa = clienteColeta.getPessoa();
			enderecoColeta = enderecoPessoaService
					.findEnderecosVigentesByIdPessoaAndCep(pessoa.getIdPessoa(), cep, false);
			if(enderecoColeta == null){
				throw new Exception("Ocorreu um erro ao buscar endereco pessoa");
			}
			pessoa.setEnderecoPessoa(enderecoColeta);
			usuarioIntegracao = usuarioService.findUsuarioByLogin(LOGIN_USUARIO_INTEGRACAO);
			recuperarFilialDeColetaAPartirDoCepInformado(cep);
			if(!erros.isEmpty()){
				throw new Exception("Ocorreu um erro ao buscar Filial");
			}
			recuperarRotaColetaEntrega(cep);
			if(!erros.isEmpty()){
				throw new Exception("Ocorreu um erro ao buscar Rota Coleta Entrega");
			}
		}catch (Exception e){
			throw new Exception(e);
		}

	}

	private PedidoColeta carregarPedidoColeta(PedidoColetaBoschDMN pedidoColetaBosch, DateTime dateTimeNow) throws Exception {
		PedidoColeta pedidoColeta = new PedidoColeta();
		DateTime dateTimePrevisaoColeta = dateTimeNow.plusDays(1);
		int hour = dateTimeNow.getHourOfDay();
		int minute = dateTimeNow.getMinuteOfHour();
		int second = dateTimeNow.getSecondOfMinute();

		Long numeroColetaFilial = recuperarParametroFilialNumeroColeta();
		if(!erros.isEmpty()){
			throw new Exception("Ocorreu um erro ao gerar numero coleta filial");
		}

		pedidoColeta.setFilialByIdFilialSolicitante(filialColeta);
		pedidoColeta.setFilialByIdFilialResponsavel(filialColeta);
		pedidoColeta.setCliente(clienteColeta);
		pedidoColeta.setMunicipio(municipio);
		pedidoColeta.setUsuario(usuarioIntegracao);

		pedidoColeta.setMoeda(new Moeda(1L));
		pedidoColeta.setNrColeta(numeroColetaFilial);
		pedidoColeta.setNrDddCliente(pedidoColetaBosch.getNrDdd());
		pedidoColeta.setNrTelefoneCliente(pedidoColetaBosch.getNrTelefone());
		pedidoColeta.setNrCep(pedidoColetaBosch.getNrCep());
		pedidoColeta.setDhPedidoColeta(dateTimeNow);
		pedidoColeta.setDhColetaDisponivel(dateTimeNow);
		pedidoColeta.setBlProdutoDiferenciado(false);

		pedidoColeta.setDtPrevisaoColeta(dateTimePrevisaoColeta.toYearMonthDay());
		if(rotaColetaEntrega != null) {
			TimeOfDay hrPedidoColeta = new TimeOfDay(hour, minute, second);
			TimeOfDay hrCorteSolicitacao = rotaColetaEntrega.getHrCorteSolicitacao();
			if(hrCorteSolicitacao.compareTo(hrPedidoColeta) == 1) {
				pedidoColeta.setDtPrevisaoColeta(dateTimeNow.toYearMonthDay());
			}
			pedidoColeta.setRotaColetaEntrega(rotaColetaEntrega.getRotaColetaEntrega());
		}

		pedidoColeta.setHrLimiteColeta(new TimeOfDay(hour, minute, second));
		pedidoColeta.setTpModoPedidoColeta(new DomainValue("TE"));
		pedidoColeta.setTpPedidoColeta(new DomainValue("DE"));
		pedidoColeta.setTpStatusColeta(new DomainValue("AB"));
		pedidoColeta.setEdColeta(pedidoColetaBosch.getDsEnderecoCliente());
		pedidoColeta.setDsBairro(pedidoColetaBosch.getDsBairro());
		pedidoColeta.setNmSolicitante(pedidoColetaBosch.getNmPessoa());
		pedidoColeta.setNmContatoCliente(pedidoColetaBosch.getNmContado());
		pedidoColeta.setVersao(0);
		pedidoColeta.setVlTotalInformado(pedidoColetaBosch.getVlAprovado());
		pedidoColeta.setVlTotalVerificado(new BigDecimal(0));
		pedidoColeta.setQtTotalVolumesInformado(pedidoColetaBosch.getQtVolume());
		pedidoColeta.setQtTotalVolumesVerificado(pedidoColetaBosch.getQtVolume());
		pedidoColeta.setPsTotalInformado(pedidoColetaBosch.getPsTotal());
		pedidoColeta.setPsTotalVerificado(pedidoColetaBosch.getPsTotal());
		pedidoColeta.setBlClienteLiberadoManual(false);
		pedidoColeta.setBlAlteradoPosProgramacao(false);
		pedidoColeta.setEnderecoPessoa(enderecoColeta);
		pedidoColeta.setCdColetaCliente(pedidoColetaBosch.getNotaQm());
		pedidoColeta.setNmClienteIntegracao("BOSCH");
		pedidoColeta.setObPedidoColeta(pedidoColetaBosch.getDsObservacaoCliente());

		return pedidoColeta;
	}

	private void carregarDetalheColeta(PedidoColeta pedidoColeta, Cliente cliente, EnderecoPessoa enderecoPessoa){
		DetalheColeta detalheColeta = new DetalheColeta();

		pedidoColeta.addDetalheColetaeta(detalheColeta);
		Servico servico = new Servico();
		servico.setIdServico(1L);
		detalheColeta.setServico(servico);
		NaturezaProduto natureza = new NaturezaProduto();
		natureza.setIdNaturezaProduto(65L);
		detalheColeta.setNaturezaProduto(natureza);
		detalheColeta.setMoeda(new Moeda(1L));
		detalheColeta.setTpFrete(new DomainValue("C"));
		detalheColeta.setQtVolumes(pedidoColeta.getQtTotalVolumesInformado());
		detalheColeta.setVlMercadoria(new BigDecimal(0));
		detalheColeta.setPsMercadoria(pedidoColeta.getPsTotalInformado());
		detalheColeta.setPsAforado(pedidoColeta.getPsTotalInformado());
		detalheColeta.setMunicipio(enderecoColeta.getMunicipio());
		detalheColeta.setCliente(cliente);

	}

	private void carregarEventoColeta(PedidoColeta pedidoColeta, DateTime dateTimeNow){
		EventoColeta eventoColeta = new EventoColeta();

		pedidoColeta.addEventoColetaeta(eventoColeta);
		OcorrenciaColeta ocorrenciaColeta = new OcorrenciaColeta();
		ocorrenciaColeta.setIdOcorrenciaColeta(87L);
		eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
		eventoColeta.setUsuario(usuarioIntegracao);
		eventoColeta.setDhEvento(dateTimeNow);
		eventoColeta.setTpEventoColeta(new DomainValue("SO"));

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private String storePedidoColetaBosch(PedidoColetaBoschDMN pedidoColetaBoschDMN) throws Exception {

		DateTime dateTimeNow = new DateTime();

		try{
			PedidoColeta pedidoColeta = carregarPedidoColeta(pedidoColetaBoschDMN, dateTimeNow);
			Cliente cliente  = clienteService.findByNrIdentificacao(pedidoColetaBoschDMN.getNrIdentificacao());
			EnderecoPessoa enderecoPessoa = enderecoPessoaService.findByIdPessoa(cliente.getPessoa().getIdPessoa());
			carregarDetalheColeta(pedidoColeta, cliente, enderecoPessoa);
			carregarEventoColeta(pedidoColeta, dateTimeNow);
			pedidoColetaService.store(pedidoColeta);
			EventoColeta eventoColeta = (EventoColeta)pedidoColeta.getEventoColetas().get(0);
			this.eventoColetaService.storeMessageTopic(eventoColeta, pedidoColetaBoschDMN.getNrIdentificacao());
			return gerarProtocolo(pedidoColetaBoschDMN.getNotaQm(), pedidoColeta.getIdPedidoColeta(), dateTimeNow);
		}catch (Exception e){
			throw new Exception(e.getMessage());
		}

	}

	private String gerarProtocolo(String notaQM, Long nrProtocolo, DateTime dateTimeProtocolo){

		String protocolo = "PROTOCOLO_RECEBIMENTO";
		String data = dateTimeProtocolo.toString("ddMMyyyy");
		String hora = dateTimeProtocolo.toString("HHmmss");

		return protocolo.concat(leftPad(notaQM))
				.concat(leftPad(String.valueOf(nrProtocolo))).concat(data).concat(hora);
	}

	private String leftPad(String value){
		return StringUtils.leftPad(value, 12, "0");
	}
}