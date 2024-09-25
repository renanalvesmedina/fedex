package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.tntbrasil.integracao.domains.edi.NotaFiscalWebServiceDto;
import br.com.tntbrasil.integracao.domains.expedicao.NotaFiscalDMN;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.edi.model.LogEDI;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiComplemento;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;
import com.mercurio.lms.edi.model.NotaFiscalEdiVolume;
import com.mercurio.lms.edi.model.dao.NotaFiscalEdiDAO;
import com.mercurio.lms.edi.util.NotaFiscalEDIDTO2EntityConverter;
import com.mercurio.lms.municipios.model.service.McdService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.ValidateUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.notaFiscalEDIService"
 */

public class NotaFiscalEDIService extends CrudService<NotaFiscalEdi, Long> {

	private NotaFiscalEDIItemService notaFiscalEDIItemService;
	private NotaFiscalEDIVolumeService notaFiscalEDIVolumeService;
	private NotaFiscalEDIComplementoService notaFiscalEDIComplementoService;
	private PessoaService pessoaService;
	private ClienteService clienteService;
	private InformacaoDoctoClienteService informacaoDoctoClienteService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private McdService mcdService;
	private ParametroGeralService parametroGeralService;
	
	private static final Log logger = LogFactory.getLog(NotaFiscalEDIService.class);

	public Long findSequenciaAgrupamentoSq(){
		return getNotaFiscalEdiDAO().findSequenciaAgrupamentoSq();
	}
	
	public Serializable store(NotaFiscalEdi notaFiscalEDI, 
			List<NotaFiscalEdiComplemento> notaFiscalEdiComplementoList,
			List<NotaFiscalEdiVolume> notaFiscalEdiVolumeList,
			List<NotaFiscalEdiItem> notaFiscalEdiItemList
			) {
		return store(notaFiscalEDI, notaFiscalEdiComplementoList, notaFiscalEdiVolumeList, notaFiscalEdiItemList, false);
	}

	private Serializable store(NotaFiscalEdi notaFiscalEDI, List<NotaFiscalEdiComplemento> notaFiscalEdiComplementoList,
			List<NotaFiscalEdiVolume> notaFiscalEdiVolumeList, List<NotaFiscalEdiItem> notaFiscalEdiItemList,
			boolean blNatura) {
		if(notaFiscalEDI.getIdNotaFiscalEdi() == null){
			notaFiscalEDI.setIdNotaFiscalEdi(getNotaFiscalEdiDAO().findSequence());
		}
		
		if (!validateStoreNotaFiscalEdi(notaFiscalEDI, blNatura)) {
		    return null;
		}
		
		Long idNota = (Long)super.store(notaFiscalEDI);
		notaFiscalEDI.setIdNotaFiscalEdi(idNota);
		for (NotaFiscalEdiItem item : notaFiscalEdiItemList) {		
			item.setNotaFiscalEdi(notaFiscalEDI);
			this.notaFiscalEDIItemService.store(item);
		}
		for (NotaFiscalEdiVolume item : notaFiscalEdiVolumeList) {		
			item.setNotaFiscalEdi(notaFiscalEDI);
			this.notaFiscalEDIVolumeService.store(item);
		}
		for (NotaFiscalEdiComplemento item : notaFiscalEdiComplementoList) {		
			item.setNotaFiscalEdi(notaFiscalEDI);
			this.notaFiscalEDIComplementoService.store(item);
		}
		return idNota;
	}
	
	public  void storeNotaFiscalEdi(NotaFiscalEdi notaFiscalEDI) {
		getNotaFiscalEdiDAO().store(notaFiscalEDI, true);		
	}

	public LogEDI storeLogEDI(String nrIdentificacao) {
		return getNotaFiscalEdiDAO().storeLogEDI(nrIdentificacao);
	}
	
	public LogEDI storeLogEDISemLayout(String nrIdentificacao, String nmArquivo) {
        return getNotaFiscalEdiDAO().storeLogEDISemLayout(nrIdentificacao, nmArquivo);
    }

	public void storeLogEDI(LogEDI logEDI, boolean sucesso, int qtdePartes) {
		getNotaFiscalEdiDAO().storeLogEDI(logEDI, sucesso, qtdePartes);
	}
	
	
/**
 *  Usar este aqui para as chamadas via rest
 * 
 * @param dtos
 * @param logEDI
 */
public void storeNotasFiscais(List<NotaFiscalWebServiceDto> dtos, LogEDI logEDI) {
       NotaFiscalEDIDTO2EntityConverter converter =
               new NotaFiscalEDIDTO2EntityConverter(this, pessoaService, informacaoDoctoClienteService,parametroGeralService);
       for (NotaFiscalWebServiceDto dto : dtos) {
           if (validateStoreNotaFiscalEdi(dto.getRemetente().getIdentificacao(),dto.getNumero().toString(),dto.getSerie())) {
               NotaFiscalEdi nota = converter.convert(dto);
               store(nota, nota.getComplementos(), nota.getVolumes(), nota.getItens(), false);
               getNotaFiscalEdiDAO().storeLogEDIDetalhe(logEDI, nota);
           }
       }
   }
	
	public void storeNotasFiscais(List<NotaFiscalWebServiceDto> dtos, LogEDI logEDI, boolean blNatura) {
	    NotaFiscalEDIDTO2EntityConverter converter =
                new NotaFiscalEDIDTO2EntityConverter(this, pessoaService, informacaoDoctoClienteService,parametroGeralService);
        for (NotaFiscalWebServiceDto dto : dtos) {
            NotaFiscalEdi nota = converter.convert(dto, blNatura);
            if (validateStoreNotaFiscalEdi(nota, blNatura)) {
                store(nota, nota.getComplementos(), nota.getVolumes(), nota.getItens(), blNatura);
                getNotaFiscalEdiDAO().storeLogEDIDetalhe(logEDI, nota);
            }
        }
	}
	
	
	
	
	/**
	 * 
	 * @param notaFiscalEdi
	 * @param blNatura
	 * @return
	 */
	private boolean validateStoreNotaFiscalEdi(NotaFiscalEdi notaFiscalEdi, boolean blNatura) {
	    Cliente clienteRemetente = findClienteByNrIdentificacaoEDI(notaFiscalEdi.getCnpjReme().toString());
		if (blNatura || (clienteRemetente != null && BooleanUtils.isTrue(clienteRemetente.getBlClientePostoAvancado()))) {
	        Map<String, Object> criteria = new HashMap<String, Object>();
	        criteria.put("cnpjReme", notaFiscalEdi.getCnpjReme());
	        criteria.put("nrNotaFiscal", notaFiscalEdi.getNrNotaFiscal());
	        criteria.put("serieNf", notaFiscalEdi.getSerieNf());
	        if (getRowCount(criteria) > 0){
	            return false;
	        }
	    }
	    return true;
	}
	
	/**
	 * Valida se existe uma nota determinada fiscal pode ser inserida no banco.
	 * 
	 * @param cnpjRemetente
	 * @param nrNotaFiscal
	 * @param serieNotaFiscal
	 * @return
	 */
	public boolean validateStoreNotaFiscalEdi(String cnpjRemetente, String nrNotaFiscal, String serieNotaFiscal) {
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("cnpjReme", cnpjRemetente);
        criteria.put("nrNotaFiscal", nrNotaFiscal);
        criteria.put("serieNf", serieNotaFiscal);
        return getRowCount(criteria) == 0;
    }
	
	
	/**
     * Faz a busca do cliente baseando-se no nrIdentificacao do EDI, que sendo do tipo numerico na
     * base de dados omite os zeros a esquerda. 
     * 
     * @param nrIdentificacaoEDI CPF ou CNPJ em formato numerico sem zeros a esquerda. 
     * @return
     */
    private Cliente findClienteByNrIdentificacaoEDI(String nrIdentificacaoEDI){
        String nrIdentificacao = FormatUtils.fillNumberWithZero(nrIdentificacaoEDI, 14);
        Cliente cliente = null;
        if(ValidateUtils.validateCpfOrCnpj(nrIdentificacao)) {
            cliente = clienteService.findByNrIdentificacao(nrIdentificacao);
        }
        //Faz a verificação se o cliente é nulo, pois há casos onde mesmo acrescentando zeros,
        //o metodo validateCpfCnpj retorna true. Entao busca novamente formatando o nrIdentificação
        //para 11 digitos.
        if (cliente == null){
            nrIdentificacao = FormatUtils.fillNumberWithZero(nrIdentificacaoEDI, 11);
            cliente = clienteService.findByNrIdentificacao(nrIdentificacao);
        }
        
        return cliente;

    }
    
    public void generatePaletizacaoNotasEdi(NotaFiscalDMN notaDMN) {
    	NotaFiscalEdi notaFiscalEdi = getNotaFiscalEdiDAO().findByNrIdentificacaoByNrNotaFiscal(notaDMN.getCNPJRemetente(), Integer.valueOf(notaDMN.getNrNotaFiscal()), null);
    	if(notaFiscalEdi != null) {
	    	// remover os volumes dessa nota
	    	notaFiscalEDIVolumeService.removeByIdNotaFiscalEdi(notaFiscalEdi.getIdNotaFiscalEdi());
	    	// Inserir os Volumes referentes aos pallets
	    	notaFiscalEDIVolumeService.generateVolumes(notaDMN.getVolumes(), notaFiscalEdi);
    	}
    }
    
     
    
    public NotaFiscalEdi findByNrNotaIdentificado(String nrIdentificacao, String nrNotaFiscal) {    	
    	return  getNotaFiscalEdiDAO().findByNrIdentificacaoByNrNotaFiscal(nrIdentificacao, Integer.valueOf(nrNotaFiscal), null);
    }

    public void storeLogEDIDetalhe(LogEDI logEDI, NotaFiscalEdi nota){
        getNotaFiscalEdiDAO().storeLogEDIDetalhe(logEDI, nota);
    }
    
	public Pessoa findDadosRemetente(String nrIdentificacao) {
		return getNotaFiscalEdiDAO().findDadosRemetente(nrIdentificacao);
	}

	private NotaFiscalEdiDAO getNotaFiscalEdiDAO() {
        return (NotaFiscalEdiDAO) getDao();
    }
    
    public void setNotaFiscalEdiDAO(NotaFiscalEdiDAO dao) {
        setDao(dao);
    }

	public NotaFiscalEDIItemService getNotaFiscalEDIItemService() {
		return notaFiscalEDIItemService;
	}

	public void setNotaFiscalEDIItemService(NotaFiscalEDIItemService notaFiscalEDIItemService) {
		this.notaFiscalEDIItemService = notaFiscalEDIItemService;
	}

	public NotaFiscalEDIVolumeService getNotaFiscalEDIVolumeService() {
		return notaFiscalEDIVolumeService;
	}

	public void setNotaFiscalEDIVolumeService(NotaFiscalEDIVolumeService notaFiscalEDIVolumeService) {
		this.notaFiscalEDIVolumeService = notaFiscalEDIVolumeService;
	}

	public NotaFiscalEDIComplementoService getNotaFiscalEDIComplementoService() {
		return notaFiscalEDIComplementoService;
	}

	public void setNotaFiscalEDIComplementoService(NotaFiscalEDIComplementoService notaFiscalEDIComplementoService) {
		this.notaFiscalEDIComplementoService = notaFiscalEDIComplementoService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setInformacaoDoctoClienteService(InformacaoDoctoClienteService informacaoDoctoClienteService) {
		this.informacaoDoctoClienteService = informacaoDoctoClienteService;
	}

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }


}
