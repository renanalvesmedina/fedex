package com.mercurio.lms.entrega.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.entrega.model.DocumentoMir;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.Mir;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.entrega.model.RegistroDocumentoEntrega;
import com.mercurio.lms.entrega.model.TipoDocumentoEntrega;
import com.mercurio.lms.entrega.model.dao.MirDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.mirService"
 */
public class MirService extends CrudService<Mir, Long> {
	private Logger log = LogManager.getLogger(this.getClass());
	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Recupera uma instância de <code>Mir</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public Mir findById(java.lang.Long id) {
		return (Mir)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeByIdsComplete(List<Long> ids) {
		for(Long id : ids)
			removeByIdComplete(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)   
	public void removeByIdComplete(Long id) {
		Mir mir = (Mir)getMirDAO().getAdsmHibernateTemplate().load(Mir.class, id);
		if (mir.getDhEnvio() != null)
			throw new BusinessException("LMS-09098");

		mir.getDocumentoMirs().clear();
		getMirDAO().getAdsmHibernateTemplate().delete(mir);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Mir bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMirDAO(MirDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MirDAO getMirDAO() {
		return (MirDAO) getDao();
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		FindDefinition fDef = FindDefinition.createFindDefinition(criteria);
		return getMirDAO().findPaginatedCustom(criteria,fDef);
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getMirDAO().getRowCountCustom(criteria);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param mir entidade a ser armazenada.
	 * @param items ItemList
	 * @return Mir entidade que foi armazenada.
	 */
	public Serializable storeWithItems(Mir mir, ItemList items) {
		boolean masterIdIsNull = mir.getIdMir() == null;

		try {
			this.beforeStore(mir);					
			getMirDAO().storeWithItems(mir,items);
		} catch (HibernateOptimisticLockingFailureException e) {
			throw new BusinessException("LMS-00012");
		} catch (RuntimeException e) {
			this.rollbackMasterState(mir, masterIdIsNull, e);
			items.rollbackItemsState();

			throw e;
		}

		return mir;
	}

	@Override
	protected Mir beforeInsert(Mir bean) {
		Long nrMir = configuracoesFacade.incrementaParametroSequencial(bean.getFilialByIdFilialOrigem().getIdFilial(), "NR_MIR", true);
		bean.setNrMir(Integer.valueOf(nrMir.intValue()));
		return bean;
	}

	@Override
	protected Mir beforeStore(Mir bean) {
		Mir mir = (Mir)super.beforeStore(bean);

		if (mir.getTpMir().getValue().equals("DO")) {
			if (mir.getFilialByIdFilialOrigem().getIdFilial().equals(
					mir.getFilialByIdFilialDestino().getIdFilial())) {
				throw new BusinessException("LMS-09012");
			}
		}

		return mir;
	}

	public DocumentoMir findReciboReembolsoById(Long id) {
		//----------------------------Recibo PERSISTIDO----------------------------------------------
		ReciboReembolso reciboReembolsoOld = (ReciboReembolso)getMirDAO().getAdsmHibernateTemplate()
				.get(ReciboReembolso.class,id);

		Filial filialReciboOld = reciboReembolsoOld.getFilial();
		Pessoa pessoaReciboOld = filialReciboOld.getPessoa();
		DoctoServico doctoServicoReembolsadoOld = reciboReembolsoOld.getDoctoServicoByIdDoctoServReembolsado();
		Filial filialDoctoServicoReembolsadoOld = doctoServicoReembolsadoOld.getFilialByIdFilialOrigem();
		Cliente clienteRemetenteOld = reciboReembolsoOld.getClienteByIdClienteRemetente();
		Pessoa pessoaRemetenteOld = clienteRemetenteOld.getPessoa();

		//----------------------------NOVO POJO----------------------------------------------
		DocumentoMir documentoMir = new DocumentoMir();
		ReciboReembolso reciboReembolso = new ReciboReembolso();
		documentoMir.setReciboReembolso(reciboReembolso);

		Filial filialRecibo = new Filial();
		reciboReembolso.setFilial(filialRecibo);
		Pessoa pessoaRecibo = new Pessoa();
		filialRecibo.setPessoa(pessoaRecibo);
		DoctoServico doctoServicoReembolsado = new DoctoServico();
		reciboReembolso.setDoctoServicoByIdDoctoServReembolsado(doctoServicoReembolsado);
		Filial filialDoctoServicoReembolsado = new Filial();
		doctoServicoReembolsado.setFilialByIdFilialOrigem(filialDoctoServicoReembolsado);

		Cliente clienteRemetente = new Cliente();
		reciboReembolso.setClienteByIdClienteRemetente(clienteRemetente);
		Pessoa pessoaRemetente = new Pessoa();
		clienteRemetente.setPessoa(pessoaRemetente);

		Moeda moeda = new Moeda();
		reciboReembolso.setMoeda(moeda);

		// Populando novo POJO
		reciboReembolso.setIdDoctoServico(reciboReembolsoOld.getIdDoctoServico());
		reciboReembolso.setTpDocumentoServico(reciboReembolsoOld.getTpDocumentoServico());

		filialRecibo.setIdFilial(filialReciboOld.getIdFilial());
		filialRecibo.setSgFilial(filialReciboOld.getSgFilial());
		pessoaRecibo.setNmFantasia(pessoaReciboOld.getNmFantasia());
		reciboReembolso.setNrReciboReembolso(reciboReembolsoOld.getNrReciboReembolso());

		reciboReembolso.setVlReembolso(reciboReembolsoOld.getVlReembolso());
		reciboReembolso.setVlAplicado(reciboReembolsoOld.getVlAplicado());
		reciboReembolso.setTpValorAtribuidoRecibo(reciboReembolsoOld.getTpValorAtribuidoRecibo());
		moeda.setDsSimbolo(reciboReembolsoOld.getMoeda().getDsSimbolo());

		doctoServicoReembolsado.setIdDoctoServico(doctoServicoReembolsadoOld.getIdDoctoServico());
		doctoServicoReembolsado.setTpDocumentoServico(doctoServicoReembolsadoOld.getTpDocumentoServico());
		doctoServicoReembolsado.setNrDoctoServico(doctoServicoReembolsadoOld.getNrDoctoServico());
		filialDoctoServicoReembolsado.setSgFilial(filialDoctoServicoReembolsadoOld.getSgFilial());

		clienteRemetente.setIdCliente(clienteRemetenteOld.getIdCliente());
		pessoaRemetente.setTpIdentificacao(pessoaRemetenteOld.getTpIdentificacao());
		pessoaRemetente.setNrIdentificacao(pessoaRemetenteOld.getNrIdentificacao());
		pessoaRemetente.setNmPessoa(pessoaRemetenteOld.getNmPessoa());

		Cliente clienteDestinatarioOld = reciboReembolsoOld.getClienteByIdClienteDestinatario();
		if (clienteDestinatarioOld != null) {
			Pessoa pessoaDestinatarioOld = clienteDestinatarioOld.getPessoa();

			Cliente clienteDestinatario = new Cliente();
			reciboReembolso.setClienteByIdClienteDestinatario(clienteDestinatario);
			Pessoa pessoaDestinatario = new Pessoa();
			clienteDestinatario.setPessoa(pessoaDestinatario);

			clienteDestinatario.setIdCliente(clienteDestinatarioOld.getIdCliente());
			pessoaDestinatario.setTpIdentificacao(pessoaDestinatarioOld.getTpIdentificacao());
			pessoaDestinatario.setNrIdentificacao(pessoaDestinatarioOld.getNrIdentificacao());
			pessoaDestinatario.setNmPessoa(pessoaDestinatarioOld.getNmPessoa());
		}

		return documentoMir;		
	}

	public DocumentoMir findComprovanteById(Long id) {
		// ----------------------------Comprovante Persistido----------------------------------------------
		RegistroDocumentoEntrega registroDocumentoEntregaOld = (RegistroDocumentoEntrega)getMirDAO().getAdsmHibernateTemplate()
				.get(RegistroDocumentoEntrega.class,id);

		DoctoServico doctoServicoOld = registroDocumentoEntregaOld.getDoctoServico();
		Filial filialDoctoServicoOld = doctoServicoOld.getFilialByIdFilialOrigem();
		Pessoa pessoaDoctoServicoOld = filialDoctoServicoOld.getPessoa();

		Cliente clienteRemetenteOld = doctoServicoOld.getClienteByIdClienteRemetente();
		Pessoa pessoaRemetenteOld = clienteRemetenteOld.getPessoa();

		TipoDocumentoEntrega tipoDocumentoEntregaOld = registroDocumentoEntregaOld.getTipoDocumentoEntrega();

		// ----------------------------NOVO POJO----------------------------------------------
		DocumentoMir documentoMir = new DocumentoMir();
		RegistroDocumentoEntrega registroDocumentoEntrega = new RegistroDocumentoEntrega();
		documentoMir.setRegistroDocumentoEntrega(registroDocumentoEntrega);

		DoctoServico doctoServico = new DoctoServico();
		registroDocumentoEntrega.setDoctoServico(doctoServico);
		Filial filialDoctoServico = new Filial();
		doctoServico.setFilialByIdFilialOrigem(filialDoctoServico);
		Pessoa pessoaDoctoServico = new Pessoa();
		filialDoctoServico.setPessoa(pessoaDoctoServico);

		Cliente clienteRemetente = new Cliente();
		doctoServico.setClienteByIdClienteRemetente(clienteRemetente);
		Pessoa pessoaRemetente = new Pessoa();
		clienteRemetente.setPessoa(pessoaRemetente);

		TipoDocumentoEntrega tipoDocumentoEntrega = new TipoDocumentoEntrega();
		registroDocumentoEntrega.setTipoDocumentoEntrega(tipoDocumentoEntrega);

		// Populando novo POJO
		registroDocumentoEntrega.setIdRegistroDocumentoEntrega(registroDocumentoEntregaOld.getIdRegistroDocumentoEntrega());
		doctoServico.setIdDoctoServico(doctoServicoOld.getIdDoctoServico());

		doctoServico.setTpDocumentoServico(doctoServicoOld.getTpDocumentoServico());

		filialDoctoServico.setIdFilial(filialDoctoServicoOld.getIdFilial());
		filialDoctoServico.setSgFilial(filialDoctoServicoOld.getSgFilial());
		pessoaDoctoServico.setNmFantasia(pessoaDoctoServicoOld.getNmFantasia());
		doctoServico.setNrDoctoServico(doctoServicoOld.getNrDoctoServico());

		clienteRemetente.setIdCliente(clienteRemetenteOld.getIdCliente());
		pessoaRemetente.setTpIdentificacao(pessoaRemetenteOld.getTpIdentificacao());	
		pessoaRemetente.setNrIdentificacao(pessoaRemetenteOld.getNrIdentificacao());
		pessoaRemetente.setNmPessoa(pessoaRemetenteOld.getNmPessoa());

		Cliente clienteDestinatarioOld = doctoServicoOld.getClienteByIdClienteDestinatario();
		if (clienteDestinatarioOld != null) {
			Pessoa pessoaDestinatarioOld = clienteDestinatarioOld.getPessoa();

			Cliente clienteDestinatario = new Cliente();
			doctoServico.setClienteByIdClienteDestinatario(clienteDestinatario);
			Pessoa pessoaDestinatario = new Pessoa();
			clienteDestinatario.setPessoa(pessoaDestinatario);

			clienteDestinatario.setIdCliente(clienteDestinatarioOld.getIdCliente());
			pessoaDestinatario.setTpIdentificacao(pessoaDestinatarioOld.getTpIdentificacao());
			pessoaDestinatario.setNrIdentificacao(pessoaDestinatarioOld.getNrIdentificacao());
			pessoaDestinatario.setNmPessoa(pessoaDestinatarioOld.getNmPessoa());
		}

		tipoDocumentoEntrega.setIdTipoDocumentoEntrega(tipoDocumentoEntregaOld.getIdTipoDocumentoEntrega());
		tipoDocumentoEntrega.setDsTipoDocumentoEntrega(tipoDocumentoEntregaOld.getDsTipoDocumentoEntrega());

		return documentoMir;
	}

	/**
	 * Validação de lookup de Documento de Serviço na tela de Comprovantes.
	 * Foi implementada na Service para poder criar transações de leitura de dados. 
	 * 
	 * @param idDoctoServico
	 * @param idFilialDestino
	 * @param tpMir
	 * 
	 * @return TypedFlatMap com atributos restritos.
	 */	
	public TypedFlatMap validateComprovantes(Long idDoctoServico, Long idFilialOrigem, Long idFilialDestino,
			String tpMir, Long idDocumentoMirAtual ) {
		RegistroDocumentoEntrega registroDocumentoEntrega = validateDoctoServicoInComprovantes(idDoctoServico, 
				idFilialOrigem, idFilialDestino, tpMir, idDocumentoMirAtual);
		TypedFlatMap retorno = bean2MapComprovantes(registroDocumentoEntrega);

		return retorno;
	}

	/**
	 * @param registroDocumentoEntrega
	 * @return
	 */
	private TypedFlatMap bean2MapComprovantes(RegistroDocumentoEntrega registroDocumentoEntrega) {
		TipoDocumentoEntrega tipoDocumentoEntrega = registroDocumentoEntrega.getTipoDocumentoEntrega();
		DoctoServico doctoServico = registroDocumentoEntrega.getDoctoServico();
		Cliente remetente = doctoServico.getClienteByIdClienteRemetente();
		Pessoa pessoaRemetente = remetente.getPessoa();
		Cliente destinatario = doctoServico.getClienteByIdClienteDestinatario();

		TypedFlatMap retorno = new TypedFlatMap();

		retorno.put("nrDoctoServicoHidden",doctoServico.getNrDoctoServico());

		retorno.put("registroDocumentoEntrega.idRegistroDocumentoEntrega",registroDocumentoEntrega.getIdRegistroDocumentoEntrega());

		retorno.put("registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.tpIdentificacao.value",
				pessoaRemetente.getTpIdentificacao().getValue());
		retorno.put("registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao",
				pessoaRemetente.getNrIdentificacao());
		retorno.put("registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado",
				FormatUtils.formatIdentificacao(pessoaRemetente.getTpIdentificacao(),pessoaRemetente.getNrIdentificacao()));
		retorno.put("registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa",
				pessoaRemetente.getNmPessoa());

		if (destinatario != null) {
			Pessoa pessoaDestinatario = destinatario.getPessoa();
			retorno.put("registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao",
					pessoaDestinatario.getNrIdentificacao());

			DomainValue tpIdentificacao = pessoaDestinatario.getTpIdentificacao();
			if (tpIdentificacao != null) {
				retorno.put("registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.tpIdentificacao.value",
						tpIdentificacao.getValue());
				retorno.put("registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado",
						FormatUtils.formatIdentificacao(tpIdentificacao,pessoaDestinatario.getNrIdentificacao()));	
			}

			retorno.put("registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa",
					pessoaDestinatario.getNmPessoa());
		}		

		retorno.put("registroDocumentoEntrega.tipoDocumentoEntrega.idTipoDocumentoEntrega",
				tipoDocumentoEntrega.getIdTipoDocumentoEntrega());
		retorno.put("registroDocumentoEntrega.tipoDocumentoEntrega.dsTipoDocumentoEntrega",
				tipoDocumentoEntrega.getDsTipoDocumentoEntrega().getValue());
		return retorno;
	}

	/**
	 * Validação de um documento de serviço a ser registrado como comprovante de uma MIR.
	 * 
	 * Todos os parâmetros são obrigatórios:
	 * @param idDoctoServico id do Documento de Serviço.
	 * @param idFilialDestino id da Filial de destino da MIR.
	 * @param tpMir tipo da Mir
	 * 
	 * @return entidade de RegistroDocumentoEntrega.
	 */
	public RegistroDocumentoEntrega validateDoctoServicoInComprovantes(
		Long idDoctoServico, 
		Long idFilialOrigem,
		Long idFilialDestino,
		String tpMir,
		Long idMirAtual
	) {

		if (idDoctoServico == null || tpMir == null || idFilialDestino == null)
			throw new IllegalArgumentException("Argumentos são obrigatórios.");

		DoctoServico doctoServico = (DoctoServico)getMirDAO().getAdsmHibernateTemplate()
				.get(DoctoServico.class,idDoctoServico);

		List<RegistroDocumentoEntrega> registros = this.findRegistroEntregaRecolhidoByDocto(idDoctoServico);
		if (registros.size() == 0)		
			throw new BusinessException("LMS-09020");

		RegistroDocumentoEntrega registroDocumentoEntrega = (RegistroDocumentoEntrega)registros.get(0);
		List<DocumentoMir> documentosMir = registroDocumentoEntrega.getDocumentoMirs();
		for(DocumentoMir documentoMir : documentosMir) {
			Mir mir = documentoMir.getMir();
			if (mir.getIdMir().equals(idMirAtual))
				continue;
			if (mir.getTpMir().getValue().equals(tpMir))
				throw new BusinessException("LMS-09013");
		}

		// EA: Entrega para o administrativo
		if (tpMir.equals("EA")) {
			this.validateFilialEntregaManifesto(doctoServico, idFilialDestino);
		} else {
			Mir miraux = null;
			for(DocumentoMir documentoMir : documentosMir) {
				miraux = documentoMir.getMir();
				if (miraux.getIdMir().equals(idMirAtual))
					continue;
				if (miraux.getTpMir().getValue().equals("EA") && miraux.getDhRecebimento() == null)
					throw new BusinessException("LMS-09028");
			}

			if (tpMir.equals("DO")) {
				Filial filialOrigemDoctoServico = doctoServico.getFilialByIdFilialOrigem();
				if (!filialOrigemDoctoServico.getIdFilial().equals(idFilialDestino)) {
					throw new BusinessException("LMS-09023",new Object[]{filialOrigemDoctoServico.getSiglaNomeFilial()});
				}
			} else if (tpMir.equals("AE")) {
				for(DocumentoMir documentoMir : documentosMir) {
					miraux = documentoMir.getMir();
					if (miraux.getIdMir().equals(idMirAtual))
						continue;					
					if (miraux.getTpMir().getValue().equals("DO") && miraux.getDhRecebimento() == null)
						throw new BusinessException("LMS-09029");
				}
			}	
		}
		return registroDocumentoEntrega;
	}

	/**
	 * Validação de lookup de Documento de Serviço na tela de Reembolsos.
	 * Foi implementada na Service para poder criar transações de leitura de dados. 
	 * 
	 * @param idDoctoServico
	 * @param idFilialDestino
	 * @param tpMir
	 * 
	 * @return TypedFlatMap com atributos restritos.
	 */	
	public TypedFlatMap validateReembolsos(Long idDoctoServico, Long idFilialDestino, String tpMir, Long idDocumentoMirAtual) {
		ReciboReembolso reciboReembolso = validateDoctoServicoInReembolsos(idDoctoServico, idFilialDestino, tpMir, idDocumentoMirAtual);
		TypedFlatMap retorno = bean2MapReembolsos(reciboReembolso);		

		return retorno;
	}

	/**
	 * @param reciboReembolso
	 * @return
	 */
	private TypedFlatMap bean2MapReembolsos(ReciboReembolso reciboReembolso) {
		DoctoServico doctoServicoReembolsado = reciboReembolso.getDoctoServicoByIdDoctoServReembolsado();
		Cliente remetente = reciboReembolso.getClienteByIdClienteRemetente();
		Pessoa pessoaRemetente = remetente.getPessoa();
		Cliente destinatario = reciboReembolso.getClienteByIdClienteDestinatario();

		TypedFlatMap retorno = new TypedFlatMap();

		retorno.put("reciboReembolso.doctoServicoByIdDoctoServReembolsado.tpDocumentoServico.value",
				doctoServicoReembolsado.getTpDocumentoServico().getValue());
		retorno.put("reciboReembolso.doctoServicoByIdDoctoServReembolsado.tpDocumentoServico.description",
				doctoServicoReembolsado.getTpDocumentoServico().getDescription());
		retorno.put("reciboReembolso.doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.sgFilial",
				doctoServicoReembolsado.getFilialByIdFilialOrigem().getSgFilial());
		retorno.put("reciboReembolso.doctoServicoByIdDoctoServReembolsado.nrDoctoServico",
				doctoServicoReembolsado.getNrDoctoServico());

		DomainValue tpValorAtribuidoRecibo = reciboReembolso.getTpValorAtribuidoRecibo();
		BigDecimal vlReembolsoCalculado = this.getVlReembolsoCalculado(
			tpValorAtribuidoRecibo,
			reciboReembolso.getVlReembolso(),
			reciboReembolso.getVlAplicado()
		);

		String dsSimbolo = reciboReembolso.getMoeda().getDsSimbolo();
		retorno.put("reciboReembolso.dsSimbolo",dsSimbolo);
		retorno.put("reciboReembolso.vlReembolsoCalculado",vlReembolsoCalculado);
		retorno.put("reciboReembolso.vlReembolsoCalculadoView",dsSimbolo + " " + 
				FormatUtils.formatDecimal("#,##0.00",vlReembolsoCalculado));
		retorno.put("reciboReembolso.vlReembolso",reciboReembolso.getVlReembolso());
		retorno.put("reciboReembolso.vlAplicado",reciboReembolso.getVlAplicado());

		if (tpValorAtribuidoRecibo != null)
			retorno.put("reciboReembolso.tpValorAtribuidoRecibo",tpValorAtribuidoRecibo.getValue());

		retorno.put("reciboReembolso.clienteByIdClienteRemetente.pessoa.nrIdentificacao",pessoaRemetente.getNrIdentificacao());
		retorno.put("reciboReembolso.clienteByIdClienteRemetente.pessoa.tpIdentificacao",pessoaRemetente.getTpIdentificacao().getValue());
		retorno.put("reciboReembolso.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado",
				FormatUtils.formatIdentificacao(pessoaRemetente.getTpIdentificacao(),pessoaRemetente.getNrIdentificacao()));
		retorno.put("reciboReembolso.clienteByIdClienteRemetente.pessoa.nmPessoa",
				pessoaRemetente.getNmPessoa());

		if (destinatario != null) {
			Pessoa pessoaDestinatario = destinatario.getPessoa();
			retorno.put("reciboReembolso.clienteByIdClienteDestinatario.pessoa.nrIdentificacao",pessoaDestinatario.getNrIdentificacao());
			DomainValue tpIdentificacao = pessoaDestinatario.getTpIdentificacao();
			if (tpIdentificacao != null) {
				retorno.put("reciboReembolso.clienteByIdClienteDestinatario.pessoa.tpIdentificacao",tpIdentificacao.getValue());
				retorno.put("reciboReembolso.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado",
						FormatUtils.formatIdentificacao(tpIdentificacao,pessoaDestinatario.getNrIdentificacao()));				
			}			
			retorno.put("reciboReembolso.clienteByIdClienteDestinatario.pessoa.nmPessoa",
					pessoaDestinatario.getNmPessoa());
		}
		return retorno;
	}

	/**
	 * Validação de um documento de serviço a ser registrado como reembolso de uma MIR.
	 * 
	 * Todos os parâmetros são obrigatórios:
	 * @param idDoctoServico id do Documento de Serviço.
	 * @param idFilialDestino id da Filial de destino da MIR.
	 * @param tpMir tipo da Mir
	 * 
	 * @return entidade de RegistroDocumentoEntrega.
	 */
	public ReciboReembolso validateDoctoServicoInReembolsos(Long idDoctoServico, Long idFilialDestino, String tpMir,
			Long idMirAtual) {

		if ( idDoctoServico == null || tpMir == null || idFilialDestino == null)
			throw new IllegalArgumentException("Argumentos são obrigatórios.");

		ReciboReembolso reciboReembolso = (ReciboReembolso)getMirDAO().getAdsmHibernateTemplate()
				.get(ReciboReembolso.class,idDoctoServico);

		String tpSituacaoRecibo = reciboReembolso.getTpSituacaoRecibo().getValue();

		List<DocumentoMir> documentosMir = reciboReembolso.getDocumentoMirs();
		for(DocumentoMir documentoMir : documentosMir) {
			Mir mir = documentoMir.getMir();
			if (mir.getIdMir().equals(idMirAtual))
				continue;
			if (mir.getTpMir().getValue().equals(tpMir))
				throw new BusinessException("LMS-09014");
		}

		// EA: Entrega para o administrativo
		if (tpMir.equals("EA")) {
			// Situação do reembolso deve ser Cheques Digitados ou Recolhidos.
			if (!tpSituacaoRecibo.equals("CD") && !tpSituacaoRecibo.equals("CR")) {
				throw new BusinessException("LMS-09093");
			}

			this.validateFilialEntregaManifesto(reciboReembolso.getDoctoServicoByIdDoctoServReembolsado(), idFilialDestino);
		} else {
			// Situação do reembolso deve ser Cheques Digitados
			if (!tpSituacaoRecibo.equals("CD")) {
				throw new BusinessException("LMS-09095");
			}
			
			Mir miraux = null;
			for(DocumentoMir documentoMir : documentosMir) {
				miraux = documentoMir.getMir();
				if (miraux.getIdMir().equals(idMirAtual))
					continue;
				if (miraux.getTpMir().getValue().equals("EA") && miraux.getDhRecebimento() == null)
					throw new BusinessException("LMS-09018");
			}

			if (tpMir.equals("DO")) {
				Filial filialOrigemDoctoServico = reciboReembolso.getFilialByIdFilialOrigem();
				if (!filialOrigemDoctoServico.getIdFilial().equals(idFilialDestino)) {
					throw new BusinessException("LMS-09015",new Object[]{filialOrigemDoctoServico.getSiglaNomeFilial()});
				}

				Filial filialDestinoDoctoServico = reciboReembolso.getFilialByIdFilialDestino();
				if (filialDestinoDoctoServico != null) {
					if (filialOrigemDoctoServico.getIdFilial().equals(filialDestinoDoctoServico.getIdFilial())) {
						throw new BusinessException("LMS-09096");
					}
				}
			} else if (tpMir.equals("AE")) {
				// flag determina se recibo está em outra MIR de Destino para Origem.
				boolean isInDO = false;

				for(DocumentoMir documentoMir : documentosMir) {
					miraux = documentoMir.getMir();
					if (miraux.getIdMir().equals(idMirAtual))
						continue;
					
					if (miraux.getTpMir().getValue().equals("DO")) {
						if (miraux.getDhRecebimento() == null)
							throw new BusinessException("LMS-09019");
						// Se ainda não passou por uma mir Destino para Origem:
						if (!isInDO)
							isInDO = true;
					}
				}

				Filial filialOrigemDoctoServico = reciboReembolso.getFilialByIdFilialOrigem(); 
				Filial filialDestinoDoctoServico = reciboReembolso.getFilialByIdFilialDestino();

				// Se filial de origem for diferente da de destino, valida se reembolso está associado a uma MIR Destino para Origem.
				if (filialDestinoDoctoServico != null) {
					if (!filialOrigemDoctoServico.getIdFilial().equals(filialDestinoDoctoServico.getIdFilial())) {
						if (!isInDO) {
							throw new BusinessException("LMS-09097");
						}						
					}
				}
			}
		}
		return reciboReembolso;
	}

	/**
	 * Valida se a filial destino recebida como parâmetro é a mesma filial que
	 * efetuou a entrega da mercadoria.
	 * 
	 * Caso não, lança exceção LMS-09016.
	 * 
	 * @param doctoServico
	 * @param idFilialDestino
	 */
	private void validateFilialEntregaManifesto(DoctoServico doctoServico, Long idFilialDestino) {
		boolean hasFilial = false;
		List<ManifestoEntregaDocumento> manifestos = doctoServico.getManifestoEntregaDocumentos();
		for(ManifestoEntregaDocumento manifestoEntregaDocumento : manifestos) {
			OcorrenciaEntrega ocorrenciaEntrega = manifestoEntregaDocumento.getOcorrenciaEntrega();
			if (ocorrenciaEntrega != null) {
				if (ocorrenciaEntrega.getTpOcorrencia().getValue().equals("E")) {
					ManifestoEntrega manifestoEntrega = manifestoEntregaDocumento.getManifestoEntrega();
					Filial filialManifesto = manifestoEntrega.getFilial();
					if (filialManifesto.getIdFilial().equals(idFilialDestino)) {
						hasFilial = true;
						break;
					}	
				}
			}
		}
		if (!hasFilial)
			throw new BusinessException("LMS-09016");
	}

	/**
	 * Retorna valor do reembolso de um Recibo considerando acréscimos e descontos.
	 * @param tpValorAtribuidoRecibo
	 * @param vlReembolso
	 * @param vlAplicado
	 * @return BigDecimal valor calculado.
	 */
	public BigDecimal getVlReembolsoCalculado(DomainValue tpValorAtribuidoRecibo, BigDecimal vlReembolso, BigDecimal vlAplicado) {
		BigDecimal vlReembolsoCalculado = vlReembolso;
		if (tpValorAtribuidoRecibo != null && vlAplicado != null) {
			if (tpValorAtribuidoRecibo.getValue().equals("A")) {
				vlReembolsoCalculado = vlReembolsoCalculado.add(vlAplicado);
			} else {
				vlReembolsoCalculado = vlReembolsoCalculado.subtract(vlAplicado);
			}
		}
		return vlReembolsoCalculado;
	}

	/**
	 * Atualiza dhEnvio de uma entidade de Mir.
	 * @param idMir
	 * @param dhEnvio
	 * @return
	 */
	public Mir updateConfirmarEnvio(Long idMir, DateTime dhEnvio) {
		Mir mir = (Mir)getMirDAO().getAdsmHibernateTemplate().get(Mir.class,idMir);
		mir.setDhEnvio(dhEnvio);

		super.store(mir);

		return mir;
	}

	/**
	 * Atualiza dhRecebimento de uma instância de Mir.
	 * @param idMir
	 * @param dhRecebimento
	 * @return
	 */
	public Mir updateConfirmarRecebimento(Long idMir, DateTime dhRecebimento) {
		Mir mir = (Mir)getMirDAO().getAdsmHibernateTemplate().get(Mir.class,idMir);
		mir.setDhRecebimento(dhRecebimento);

		super.store(mir);

		return mir;
	}

	/**
	 * Atualiza dhEmissao de uma instância de Mir.
	 * @param idMir
	 * @param dhEmissao
	 * @return
	 */
	public Mir updateConfirmarEmissao(Long idMir, DateTime dhEmissao) {
		Mir mir = (Mir)getMirDAO().getAdsmHibernateTemplate().get(Mir.class,idMir);
		mir.setDhEmissao(dhEmissao);

		super.store(mir);

		return mir;
	}

	public List<RegistroDocumentoEntrega> findComprovantesPendentes(
		String tpMir, 
		Long idClienteRemetente,
		Long idClienteDestinatario,
		Long idFilialOrigem,
		Long idFilialDestino
	) {
		return getMirDAO().findComprovantesPendentes(tpMir, idClienteRemetente, idClienteDestinatario, idFilialOrigem, idFilialDestino);
	}

	public List<ReciboReembolso> findReembolsosPendentes(
		String tpMir, 
		Long idClienteRemetente,
		Long idClienteDestinatario,
		Long idFilialOrigem,
		Long idFilialDestino
	) {
		return getMirDAO().findReembolsosPendentes(tpMir, idClienteRemetente, idClienteDestinatario, idFilialOrigem, idFilialDestino);
	}

	/**
	 * Clona uma MIR alterando usuário de destino
	 * @param idMir
	 * @param idUsuarioDestino
	 * @return
	 */
	public Long storeClonarMirRecebimento(Long idMir, Long idUsuarioDestino) {
		Mir mirOld = (Mir)getMirDAO().getHibernateTemplate().get(Mir.class,idMir);
		Mir mir = new Mir();

		// Clona-se todas propriedade do antigo bean para o novo:
		try {
			mir = (Mir)BeanUtils.cloneBean(mirOld);
		} catch (Exception e) {
			log.error(e);
		}

		mir.setIdMir(null);

		mir.setTpMir(new DomainValue("AE"));

		if (idUsuarioDestino != null) {
			Usuario usuario = new Usuario();
			usuario.setIdUsuario(idUsuarioDestino);
			mir.setUsuarioByIdUsuarioRecebimento(usuario);	
		}		

		// Reseta as datas de controle do recibo.
		mir.setDhEmissao(null);
		mir.setDhEnvio(null);
		mir.setDhRecebimento(null);

		mir.setDocumentoMirs(null);

		Long retorno = (Long)super.store(mir);

		Iterator<DocumentoMir> i = mirOld.getDocumentoMirs().iterator();
		while (i.hasNext()) {
			DocumentoMir documentoMirOld = (DocumentoMir)getMirDAO().getHibernateTemplate().get(DocumentoMir.class,
					(i.next()).getIdDocumentoMir());

			ReciboReembolso reciboReembolso = documentoMirOld.getReciboReembolso();
			if (reciboReembolso != null && !Hibernate.isInitialized(reciboReembolso)) {
				Hibernate.initialize(reciboReembolso);
			}

			RegistroDocumentoEntrega registroDocumentoEntrega = documentoMirOld.getRegistroDocumentoEntrega();
			if (registroDocumentoEntrega != null && !Hibernate.isInitialized(registroDocumentoEntrega)) {
				Hibernate.initialize(registroDocumentoEntrega);
			}

			DocumentoMir documentoMir = new DocumentoMir();			
			try {
				documentoMir = (DocumentoMir)BeanUtils.cloneBean(documentoMirOld);
			} catch (Exception e) {
				log.error(e);
			}
			documentoMir.setIdDocumentoMir(null);
			documentoMir.setVersao(Integer.valueOf(0));
			documentoMir.setMir(mir);

			getDao().store(documentoMir);
		}
		return retorno;
	}

	/**
	 * Consulta se há um registro de documento de entrega com situação de Recibo Recolhido
	 * associado a um Documento de Serviço informado.
	 * @param idDoctoServico
	 * @return List do pojo de RegistroDocumentoEntrega
	 */
	public List<RegistroDocumentoEntrega> findRegistroEntregaRecolhidoByDocto(Long idDoctoServico) {
		return getMirDAO().findRegistroEntregaRecolhidoByDocto(idDoctoServico);
	}

	public List<DocumentoMir> findDocsByMir(Long masterId, String tpDocumentoMir) {
		return getMirDAO().findDocsByMir(masterId, tpDocumentoMir);
	}

	public Integer getRowCountDocsByMir(Long masterId, String tpDocumentoMir) {
		return getMirDAO().getRowCountDocsByMir(masterId, tpDocumentoMir);
	}

	/**
	 * Retorna MIR para os parâmetros informados
	 * Método utilizado pela Integração
	 * 
	 * @author Felipe Ferreira
	 * @param nrMir
	 * @param idFilialOrigem
	 * @return MIR
	 */
	public Mir findMir(Integer nrMir, Long idFilialOrigem) {
		return getMirDAO().findMir(nrMir,idFilialOrigem);
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
