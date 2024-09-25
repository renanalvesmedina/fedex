package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.contasreceber.model.NotaDebitoNacional;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.dao.DoctoServicoDAO;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.ImpostoServicoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.FormatUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.consultarDadosCobrancaDocumentoServicoService"
 */
public class ConsultarDadosCobrancaDocumentoServicoService {

	private DevedorDocServFatService devedorDocServFatService;
	private ImpostoServicoService impostoServicoService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private CtoInternacionalService ctoInternacionalService;
	private ConhecimentoService conhecimentoService;
	private NotaDebitoNacionalService notaDebitoNacionalService;
	private NotaFiscalServicoService notaFiscalServicoService;
	
	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService){
		this.devedorDocServFatService = devedorDocServFatService;
	}
		
	private EnderecoPessoaService enderecoPessoaService;
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService){
		this.enderecoPessoaService = enderecoPessoaService;
	}
	
	private DomainValueService domainValueService;
	public void setDomainValueService(DomainValueService domainValueService){
		this.domainValueService = domainValueService;
	}
	
	private TransferenciaService transferenciaService;
	public void setTransferenciaService(TransferenciaService transferenciaService){
		this.transferenciaService = transferenciaService;
	}
	
	private ItemTransferenciaService itemTransferenciaService;
	public void setItemTransferenciaService(ItemTransferenciaService itemTransferenciaService){
		this.itemTransferenciaService = itemTransferenciaService;
	}
	
	private DoctoServicoDAO doctoServicoDAO;

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getDoctoServicoDAO().findPaginatedDoctoServicos(criteria);
		Iterator iter = rsp.getList().iterator();
		List newList = new ArrayList(rsp.getList().size());
		while (iter.hasNext()){

			Object[] obj = (Object[]) iter.next();

			Map map = new HashMap();

			map.put("idDoctoServico",obj[0]);
			map.put("sgFilialOrigem",obj[2]);
			map.put("tpDocumentoServico",obj[4]);
			map.put("nrDocumentoServico",FormatUtils.formataNrDocumento((String)obj[3],(String)obj[1]));
			
			String tpDoc = (String)obj[1];
			
			if(tpDoc.equals("CTR")||tpDoc.equals("CTE")){
				map.put("tpSituacao", domainValueService.findDomainValueByValue("DM_STATUS_CONHECIMENTO", (String)obj[5]));
			}else if(tpDoc.equals("CRT")){
				map.put("tpSituacao", domainValueService.findDomainValueByValue("DM_STATUS_CRT", (String)obj[5]));
			}else if(tpDoc.equals("NFS") ||tpDoc.equals(ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA)){
				map.put("tpSituacao", domainValueService.findDomainValueByValue("DM_STATUS_NOTA_FISCAL", (String)obj[5]));
			}else if(tpDoc.equals("NFT") || tpDoc.equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA)){
				map.put("tpSituacao", domainValueService.findDomainValueByValue("DM_STATUS_CONHECIMENTO", (String)obj[5]));
			}else if(tpDoc.equals("NDN")){
				map.put("tpSituacao", domainValueService.findDomainValueByValue("DM_STATUS_RECIBO_FRETE", (String)obj[5]));
			}
			
			map.put("dhEmissao", obj[6]);
			map.put("valorTotal", obj[7]);
			
			map.put("siglaSimbolo", (String)obj[8] + " " + (String)obj[9]);
			
			newList.add(map);
		
		}
		
		rsp.setList(newList);
		
		
		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getDoctoServicoDAO().getRowCountDoctoServicos(criteria);
	}
	
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDoctoServicoDAO(DoctoServicoDAO dao) {
       this.doctoServicoDAO = dao;
    }
    
    /**
     * Método responsável por popular a grid da aba de devedores
     * 
     * @param criteria
     * @return List
     */
    public List findDevedorDocServFatByDoctoServico(TypedFlatMap criteria){
    	List list = devedorDocServFatService.findDevedorDocServFatByDoctoServico(criteria.getLong("idDoctoServico"));
    	
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		
			Map element = (Map) iter.next();
			
			DomainValue tpIdentificacao = (DomainValue)element.remove("tpIdentificacao");
			String nmPessoa = (String)element.remove("nmPessoa");
			String nrIdentificacao = (String)element.remove("nrIdentificacao");
			
			if(tpIdentificacao != null)
				element.put("identificacao", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao) + " - " + nmPessoa);
			else	
				element.put("identificacao", nmPessoa);
		}
    	
    	return list;
    }
    
    /**
	 * Método responsável por buscar o detalhamento de um devedorDocServfat para a tela de devedores
	 * 
	 * @param idDevedorDocServFat
	 * @return Map
	 */
    public Map findDevedorDocServFatDetail(TypedFlatMap criteria){
    	
    	// Carrega os dados principais da tela
    	Map ddsf = devedorDocServFatService.findDevedorDocServFatDetail(criteria);
    	
    	String dsTipoLogradouro = (String)ddsf.get("dsTipoLogradouro");
    	String dsEndereco = (String)ddsf.get("dsEndereco");
    	String nrEndereco = (String)ddsf.get("nrEndereco");
    	String dsComplemento = (String)ddsf.get("dsComplemento");
    	
    	String nrTelefone = (String)ddsf.get("nrTelefone");
    	String nrDdd = (String)ddsf.get("nrDdd");
    	String nrDdi = (String)ddsf.get("nrDdi");

    	ddsf.put("telefoneEndereco", FormatUtils.formatTelefone(nrTelefone, nrDdd, nrDdi));
    	ddsf.put("dsEndereco", enderecoPessoaService.formatEnderecoPessoaComplemento(dsTipoLogradouro, dsEndereco, nrEndereco, dsComplemento));
    	
    	return ddsf;
    }
    
    /**
	 * Recupera uma instância de <code>Cedente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public DoctoServico findById(java.lang.Long id) {
        return getDoctoServicoDAO().findDoctoServico(id);
    }
    
    
    /**
	 * Recupera uma instância de <code>Cedente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public Map findDocumentoWithTipoFrete(Long id) {
        Map<String, Object> doc = getDoctoServicoDAO().findDocumentoWithTipoFrete(id);
        
        if( doc != null ){
			DomainValue tpDoctoServico = (DomainValue) doc.get("tpDocumentoServico");
			if(tpDoctoServico.getValue().equalsIgnoreCase("CRT")){
				CtoInternacional ctoInternacional = ctoInternacionalService.findById((Long) doc.get("idDoctoServico"));
				doc.put("tpSituacao",ctoInternacional.getTpSituacaoCrt().getDescription());
			}else if(tpDoctoServico.getValue().equalsIgnoreCase("NDN")){
				NotaDebitoNacional ndn = notaDebitoNacionalService.findById((Long) doc.get("idDoctoServico"));
				doc.put("tpSituacao",ndn.getTpSituacaoNotaDebitoNac().getDescription());	
			}else if(tpDoctoServico.getValue().equalsIgnoreCase("NFS") || tpDoctoServico.getValue().equalsIgnoreCase(ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA)){
				NotaFiscalServico nfs = notaFiscalServicoService.findById((Long) doc.get("idDoctoServico"));
				doc.put("tpSituacao",nfs.getTpSituacaoNf().getDescription());
			}else if(tpDoctoServico.getValue().equalsIgnoreCase("CTR") || tpDoctoServico.getValue().equalsIgnoreCase("NFT") || 
					tpDoctoServico.getValue().equalsIgnoreCase("CTE") || tpDoctoServico.getValue().equalsIgnoreCase(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA)){
				Conhecimento con = conhecimentoService.findByIdInitLazyProperties((Long) doc.get("idDoctoServico"), false);
				doc.put("tpSituacao",con.getTpSituacaoConhecimento().getDescription());
				doc.put("tpFrete",con.getTpFrete().getDescription());
			}
		}
        
        
        String[] tipos = {"NFS","NSE","NFT","NTE"};
        String tpDocumentoServico = ((DomainValue)doc.get("tpDocumentoServico")).getValue();
		if (ArrayUtils.contains(tipos, tpDocumentoServico)) {
        	doc.put("vlIcmsSubstituicaoTributaria", impostoServicoService.somaISSDoctoServico(id));
    }
        MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(id);
        if (monitoramentoDocEletronico != null) {
        	doc.put("numeroNfe", monitoramentoDocEletronico.getNrDocumentoEletronico());
        }
		return doc;
    }
    
    
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DoctoServicoDAO getDoctoServicoDAO() {
        return this.doctoServicoDAO;
    }
    
    /**
     * 
     * @param idDevedor
     * @return
     */
    public List findByIdDevedorDocServFat(Long idDevedor){
    	return itemTransferenciaService.findItemTransferenciaByDevedorDocServFat(idDevedor);
    }
    
    /**
     * 
     * @param idItemTransferencia
     * @return
     */
    public Map findDadosTransferencia(Long idItemTransferencia){
    	return transferenciaService.findDadosTransferencia(idItemTransferencia);
    }
    
    public void setImpostoServicoService(ImpostoServicoService impostoServicoService) {
		this.impostoServicoService = impostoServicoService;
	}
    
    public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}
    
    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}

	public void setNotaDebitoNacionalService(NotaDebitoNacionalService notaDebitoNacionalService) {
		this.notaDebitoNacionalService = notaDebitoNacionalService;
	}

	public void setNotaFiscalServicoService(NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}
    
}