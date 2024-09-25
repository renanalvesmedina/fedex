package com.mercurio.lms.sim.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sim.model.PedidoCompra;
import com.mercurio.lms.sim.model.dao.PedidoCompraDAO;
import com.mercurio.lms.util.FormatUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:    
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sim.pedidoCompraService"
 */
public class PedidoCompraService extends CrudService<PedidoCompra, Long> {


	/**
	 * Recupera uma instância de <code>PedidoCompra</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public PedidoCompra findById(java.lang.Long id) {
        return (PedidoCompra)super.findById(id);
    }

    
    
    public List findByNrPedido(Long nrPedido){   	
    	return getPedidoCompraDAO().findByNrPedido(nrPedido);
    }
    
    public List findByNrPedidoInternacional(String nrPedido){
    	return getPedidoCompraDAO().findByNrPedidoInternacional(nrPedido);
    }
    
    public List findDocumentosServico(Long idPedidoCompra, Long codTipoAnexoFatura){
    	return getPedidoCompraDAO().findDocumentosServico(idPedidoCompra, codTipoAnexoFatura);
    }
    public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getPedidoCompraDAO().getRowCountCustom(criteria);
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

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(PedidoCompra bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPedidoCompraDAO(PedidoCompraDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PedidoCompraDAO getPedidoCompraDAO() {
        return (PedidoCompraDAO) getDao();
    }
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) { 
		 return getPedidoCompraDAO().findPaginatedCustom(criteria);
	}



    public TypedFlatMap findByIdDetalhamento(java.lang.Long id) {
    	
    	PedidoCompra oe = findById(id);
    	TypedFlatMap tfm = new TypedFlatMap();
    	tfm.put("filial.idFilial",oe.getFilial().getIdFilial());
    	tfm.put("filial.sgFilial",oe.getFilial().getSgFilial());
    	tfm.put("filial.pessoa.nmFantasia",oe.getFilial().getPessoa().getNmFantasia());
		tfm.put("idPedidoCompra",oe.getIdPedidoCompra());
    	tfm.put("remetente.idCliente",oe.getRemetente().getPessoa().getIdPessoa());
		tfm.put("remetente.pessoa.nmPessoa",oe.getRemetente().getPessoa().getNmPessoa() );
 		String nrIdentificacaoRemetente = FormatUtils.formatIdentificacao(oe.getRemetente().getPessoa().getTpIdentificacao(),oe.getRemetente().getPessoa().getNrIdentificacao());
 		tfm.put("remetente.pessoa.nrIdentificacao",nrIdentificacaoRemetente );
        tfm.put("remetente.pessoa.nrIdentificacaoFormatado",nrIdentificacaoRemetente);

		
    	tfm.put("destinatario.idCliente",oe.getDestinatario().getPessoa().getIdPessoa());
 		tfm.put("destinatario.pessoa.nmPessoa",oe.getDestinatario().getPessoa().getNmPessoa() );
 		String nrIdentificacaoProprietario = FormatUtils.formatIdentificacao(oe.getDestinatario().getPessoa().getTpIdentificacao(),oe.getDestinatario().getPessoa().getNrIdentificacao());
 		tfm.put("destinatario.pessoa.nrIdentificacao",nrIdentificacaoProprietario );
        tfm.put("destinatario.pessoa.nrIdentificacaoFormatado",nrIdentificacaoProprietario);
 		
 		tfm.put("pesoBruto",oe.getNrPesoBruto());
 		tfm.put("pesoLiquido",oe.getNrPesoLiquido());
 		tfm.put("quantidadeVolumes",oe.getNrQuantidadeVolumes());
 		tfm.put("valorExportacao",oe.getVlExportacao());
 		tfm.put("tpModalExterior",oe.getTpModalExterior().getValue());
 		tfm.put("tpModalBrasil",oe.getTpMotalBrasil().getValue());

 		tfm.put("tpOrigem",oe.getTpOrigem().getValue());
 		
 		tfm.put("pagtoCartao",oe.getBlCartao());
 		
 		if (oe.getMoeda() != null)
 			tfm.put("pedidoCompra.idMoeda",oe.getMoeda().getIdMoeda());
 		
 		
 		tfm.put("previsaoEntregaExterior",oe.getDtPrevisaoEntregaExterior());
 		tfm.put("previsaoEntregaBrasil",oe.getDtPrevisaoEntregaBrasil());
 		tfm.put("emissaoPedido",oe.getDhEmissao());
 		
 		tfm.put("pedido",oe.getNrPedido());
 		tfm.put("pedidoInternacional",oe.getNrPedidoInternacional());
 		
 		tfm.put("fatura",oe.getNrFatura());
 		tfm.put("notaFiscal",oe.getNrNotaFiscal());
 		tfm.put("cartao",oe.getBlCartao());
 		tfm.put("emissao",oe.getDhEmissao());
 		
 		tfm.put("dataHoraInclusao", oe.getDhInclusao());
 		tfm.put("usuarioInclusao",oe.getUsuario().getNmUsuario());
 		
    	return tfm;
    }



	
	public PedidoCompra beforeStore(PedidoCompra bean) {
		PedidoCompra pc = (PedidoCompra)bean;
		if(pc.getRemetente()== null)
			throw new BusinessException("LMS-10044");
		if(pc.getDestinatario()== null)
			throw new BusinessException("LMS-10043");
		return super.beforeStore(bean);
		
	}
   }