package com.mercurio.lms.expedicao.edi.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.dto.ManifestoFedexSegundoCarregamentoDTO;
import com.mercurio.lms.edi.model.ConhecimentoFedex;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.dao.NotaFiscalEdiDAO;
import com.mercurio.lms.edi.model.service.ConhecimentoFedexService;
import com.mercurio.lms.edi.model.service.NotaFiscalEDIComplementoService;
import com.mercurio.lms.edi.model.service.NotaFiscalEDIVolumeService;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.edi.notaFiscalExpedicaoEDIService"
 */
public class NotaFiscalExpedicaoEDIService extends CrudService<NotaFiscalEdi, Long> {
	
	private ClienteService clienteService;

	// LMSA-6520
	private ConhecimentoFedexService conhecimentoFedexService;
	private NotaFiscalEDIVolumeService notaFiscalEDIVolumeService;
	private NotaFiscalEDIComplementoService notaFiscalEDIComplementoService;
		
	@SuppressWarnings("rawtypes")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean storeCliente(final Cliente clienteRemetente, final Long nrProcessamento, final Map mapCliente, String tpCliente) {
		final TypedFlatMap dados = new TypedFlatMap();
		dados.putAll(mapCliente);
		if(mapCliente.get("nrIdentificacao") != null) {
			final String nrIdentificacao = ((String) mapCliente.get("nrIdentificacao")).replaceAll("[^\\p{Digit}]", "");
			Cliente cliente = clienteService.findByNrIdentificacao(nrIdentificacao); 
			if(cliente == null) {
				clienteService.saveClienteBasico(dados);
			}
			
		}
		return true;
	}

	/**
	 * Recupera uma instância de <code>NotaFiscalEdi</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public NotaFiscalEdi findById(java.lang.Long id) {
		return (NotaFiscalEdi)super.findById(id);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	public java.io.Serializable store(NotaFiscalEdi bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setNotaFiscalEdiDAO(NotaFiscalEdiDAO dao) {
		setDao( dao );
	}
	
	public void setConhecimentoFedexService(ConhecimentoFedexService service) {
	    this.conhecimentoFedexService = service;
	}
	public void setNotaFiscalEDIVolumeService(NotaFiscalEDIVolumeService notaFiscalEDIVolumeService) {
		this.notaFiscalEDIVolumeService = notaFiscalEDIVolumeService;
	}
	public void setNotaFiscalEDIComplementoService(NotaFiscalEDIComplementoService notaFiscalEDIComplementoService) {
		this.notaFiscalEDIComplementoService = notaFiscalEDIComplementoService;
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private NotaFiscalEdiDAO getNotaFiscalEdiDAO() {
		return (NotaFiscalEdiDAO) getDao();
	}

	@SuppressWarnings("rawtypes")
    public List findAllEntities() {
		return getNotaFiscalEdiDAO().findAllEntities();
	}
	
	@SuppressWarnings("rawtypes")
    public List findByNrIdentificacao(String nrIdentificacao) {
		return findByNrIdentificacaoByTpAgrupamentoEdiByTpOrdemEmissaoEdi(nrIdentificacao, null, null, null, null);
	}

	public List<NotaFiscalEdi> findByNrIdentificacaoByTpAgrupamentoEdiByTpOrdemEmissaoEdi(String nrIdentificacao, String tpAgrupamentoEdi, String tpOrdemEmissaoEdi, String processarPor, String tpProcessamento) {
		return getNotaFiscalEdiDAO().findByNrIdentificacaoByTpAgrupamentoEdiByTpOrdemEmissaoEdi(nrIdentificacao, tpAgrupamentoEdi, tpOrdemEmissaoEdi, processarPor, tpProcessamento);
	}

	public NotaFiscalEdi findByNrIdentificacaoByNrNotaFiscal(String nrIdentificacao, Integer nrNotaFiscal, String processarPor) {
		return getNotaFiscalEdiDAO().findByNrIdentificacaoByNrNotaFiscal(nrIdentificacao, nrNotaFiscal, processarPor);
	}

	public List<NotaFiscalEdi> findByNrIdentificacaoByIntervaloNotaFiscal
			(String nrIdentificacao, Integer nrNotaFiscalInicial, Integer nrNotaFiscalFinal) {
		return getNotaFiscalEdiDAO()
			.findByNrIdentificacaoByIntervaloNotaFiscal(nrIdentificacao, nrNotaFiscalInicial, nrNotaFiscalFinal);
	}

	@SuppressWarnings("rawtypes")
    public List findIdsByNrIdentificacaoByNrNotaFiscal(String nrIdentificacao, Integer nrNotaFiscal) {
		return getNotaFiscalEdiDAO().findIdsByNrIdentificacaoByNrNotaFiscal(nrIdentificacao, nrNotaFiscal);
	}
	
	/**
	 * Busca as Notas Fiscais EDI relacionadas ao Docto do Cliente
	 * @param idCliente
	 * @param valorComplemento
	 * @return
	 */
	public List<Integer> findByInformacaoDoctoCliente(final Long idCliente, final String valorComplemento) {
		final Cliente cliente = clienteService.findByIdInitLazyProperties(idCliente, false);
		final InformacaoDoctoCliente informacaoDoctoClienteEDI = cliente.getInformacaoDoctoClienteEDI();
		return getNotaFiscalEdiDAO().findByInformacaoDoctoCliente(informacaoDoctoClienteEDI.getIdInformacaoDoctoCliente(), valorComplemento);
	}
	
	public List<NotaFiscalEdi> findNotaFiscalByDoctoCliente(final Long idCliente, final String valorComplemento) {
		final Cliente cliente = clienteService.findByIdInitLazyProperties(idCliente, false);
		final InformacaoDoctoCliente informacaoDoctoClienteEDI = cliente.getInformacaoDoctoClienteEDI();
		return getNotaFiscalEdiDAO().findNotaFiscalByDoctoCliente(informacaoDoctoClienteEDI.getIdInformacaoDoctoCliente(), valorComplemento);
	}
	
	/**
	 * Busca as Notas Fiscais EDI relacionadas ao Docto do Cliente
	 * @param idCliente
	 * @param valorComplemento
	 * @return
	 */
	public List<Integer> findByInformacaoDoctoClienteConsolidado(final Long idCliente, final String valorComplemento) {
		final Cliente cliente = clienteService.findByIdInitLazyProperties(idCliente, false);
		final InformacaoDoctoCliente informacaoDoctoClienteEDI = cliente.getInformacaoDoctoClienteConsolidado();
		return getNotaFiscalEdiDAO().findByInformacaoDoctoCliente(informacaoDoctoClienteEDI.getIdInformacaoDoctoCliente(), valorComplemento);
	}
	
	public List<NotaFiscalEdi> findNotaFiscalByDoctoClienteConsolidado(final Long idCliente, final String valorComplemento) {
		final Cliente cliente = clienteService.findByIdInitLazyProperties(idCliente, false);
		final InformacaoDoctoCliente informacaoDoctoClienteEDI = cliente.getInformacaoDoctoClienteConsolidado();
		return getNotaFiscalEdiDAO().findNotaFiscalByDoctoCliente(informacaoDoctoClienteEDI.getIdInformacaoDoctoCliente(), valorComplemento);
	}

    /**
     * Busca as Notas Fiscais EDI relacionadas ao numero do manifesto
     * @param chaveMDFEFedEX
     * @return
     * LMSA-6520: LMSA-6534
     * 
     * CENARIOS de validacao:
     * 1: havendo notas EDI para a chave MDFE informada
     * 1.1 sem existir notas LMS para a chave MDFE = ok
     * 1.2 existe notas LMS para a chave MDFE, mas nenhuma associada a CC = ok
     * 1.3 existe notas LMS para a chave MDFE, com CC associado = erro de negocio
     * 1.4 existe notas LMS para a chave MDFE, mas nao em sua totalidade (ex: EDI 10 nf, LMS 7 nf) = erro de negocio
     * 
     * 2: nao existe notas EDI para a chave MDFE informada, mas existem notas LMS para esta chave
     * 2.1 nenhum nota LMS esta vinculada a um CC = ok
     * 2.2 todas ou alguma nota LMS esta vinculada a um CC = erro de negocio
     * 
     * 3: nao foi encontrado nenhuma nota EDI e LMS para a chave mdfe em questao = erro de negocio
     */
    public List<ManifestoFedexSegundoCarregamentoDTO> findNotaFiscalByChaveMdfeFedex(final String chaveMdfeFedex) {
        List<NotaFiscalEdi> notasEDI = getNotaFiscalEdiDAO().findByChaveMdfeFedex(chaveMdfeFedex);
        List<ConhecimentoFedex> conhecimentosLMS = conhecimentoFedexService.findByChaveMdfeFedex(chaveMdfeFedex);
        
        // testar CENARIO 1.3 e CENARIO 2.2
        if (conhecimentosLMS != null && !conhecimentosLMS.isEmpty()) {
            for (ConhecimentoFedex cFedex : conhecimentosLMS) {
                if (cFedex.getControleCarga() != null) {
                    throw new BusinessException("LMS-05418", new String[] {cFedex.getSiglaFilialOrigem() + cFedex.getControleCarga().getNrControleCarga()});
                }
            }
        }

        // testar CENARIO 3
        if ((notasEDI == null || notasEDI.isEmpty()) && (conhecimentosLMS == null || conhecimentosLMS.isEmpty())) {
            throw new BusinessException("LMS-05419");
        }

        // testar CENARIO 1.4
        if (notasEDI != null && conhecimentosLMS != null) {
            // **no teste anterior garantiu-se que as notas LMS nao estao vinculadas a nenhuma CC
            // cenario valido: nao tem notas EDI e tem notas LMS
            boolean ok = notasEDI.isEmpty() && !conhecimentosLMS.isEmpty();
            // se cenario anterior nao aplicou-se, aplica novo teste
            // cenario valido: tem notas EDI e nao tem notas LMS
            ok = !ok ? !notasEDI.isEmpty() && conhecimentosLMS.isEmpty() : ok;
            // se cenario anterior nao aplicou-se, aplica novo teste
            // cenario valido: ambas as listas possuem dados, mas em quantidades diferentes
            ok = !ok ? notasEDI.size() != conhecimentosLMS.size() : ok;            
            if (!ok) {
                throw new BusinessException("LMS-05420");
            }
        }
        
        // retornar somente o numero da nota de cada lista
        List<Integer>  inotasEDI = null;
        List<String>  iconhecimentosLMS = null;
        
        if (notasEDI != null) {
            inotasEDI = new ArrayList<Integer>(notasEDI.size());
            for (NotaFiscalEdi notaEDI : notasEDI) {
                inotasEDI.add(notaEDI.getNrNotaFiscal());
            }
        }
        
        if (conhecimentosLMS != null) {
            iconhecimentosLMS = new ArrayList<String>(conhecimentosLMS.size());
            for (ConhecimentoFedex conhecimentoFedex : conhecimentosLMS) {
                iconhecimentosLMS.add(conhecimentoFedex.getNumeroConhecimento());
            }
        }
        
        List<ManifestoFedexSegundoCarregamentoDTO> result = new ArrayList<ManifestoFedexSegundoCarregamentoDTO>(1); 
        result.add(
        		new ManifestoFedexSegundoCarregamentoDTO(
        				chaveMdfeFedex, inotasEDI, iconhecimentosLMS)
        		);
        
        return result;
    }
    public NotaFiscalEdi findNotaFiscalEdiByChaveMdfeFedexENumeroNotaFiscal(final String chaveMdfeFedex, final Integer numeroNotaFsical) {
         return getNotaFiscalEdiDAO().findByChaveMdfeFedexENumeroNotaFiscal(chaveMdfeFedex, numeroNotaFsical);
    }

    public void removerNotaFiscalEdiProcessadaByChaveMdfeFedexENumeroNotaFiscal(final String chaveMdfeFedex, final Integer numeroNotaFsical) {
    	 NotaFiscalEdi notaFiscalEdi = findNotaFiscalEdiByChaveMdfeFedexENumeroNotaFiscal(chaveMdfeFedex, numeroNotaFsical);
    	 if (notaFiscalEdi != null) {
    		 notaFiscalEDIVolumeService.removeByIdNotaFiscalEdi(notaFiscalEdi.getIdNotaFiscalEdi());
    		 notaFiscalEDIComplementoService.removeByIdNotaFiscalEdi(notaFiscalEdi.getIdNotaFiscalEdi());
    	 }
    }
    
	
	/**
	 * Busca os maiores identificadores das notas fiscais recebidas como
	 * parametro para o cliente informado.
	 * 
	 * @param cnpj
	 *            identificador do remetente
	 * @param nrNotasFiscais
	 *            numeros das notas a serem buscadas
	 * @return lista de identificadores das notas
	 * @author Luis Carlos Poletto
	 * @author André Valadas
	 * @param idInformacaoDoctoCliente 
	 */
	public List<NotaFiscalEdi> findNotas(final String cnpj, final List<Integer> nrNotasFiscais, final Long idInformacaoDoctoCliente, String processarPor) {
		final List<NotaFiscalEdi> result = new ArrayList<NotaFiscalEdi>();
		final List<Integer> nrRecebidas = new ArrayList<Integer>(nrNotasFiscais);
		/* Regra que valida limite da clausula IN */
		while (!nrRecebidas.isEmpty()) {
			/* Quebra lista em no máximo 999 IDs */
			final List<Integer> subList = nrRecebidas.subList(0, Math.min(999, nrRecebidas.size()));
			/* Adiciona resultados */
			result.addAll(getNotaFiscalEdiDAO().findNotas(cnpj, subList, idInformacaoDoctoCliente, processarPor));
			/* Remove IDS pesquisados anteriormente */
			nrRecebidas.subList(0, Math.min(999, nrRecebidas.size())).clear();
	}
		return result;
	}
	
	/**
	 * Busca todos os identificadores de notas fiscais remetidas pelo cnpj
	 * recebido como parametro.
	 * 
	 * @param cnpj identificador do cliente
	 * @return lista de identificadores das notas
	 * @author Luis Carlos Poletto
	 * @param idInformacaoDoctoCliente 
	 */
	public List<NotaFiscalEdi> find(String cnpj, final Long idInformacaoDoctoCliente, String processarPor) {
		return getNotaFiscalEdiDAO().find(cnpj, idInformacaoDoctoCliente, processarPor);
	}

	public void updateAcertoNotaFiscal(Long cnpjRemetente){
		getNotaFiscalEdiDAO().updateNotaFiscal(cnpjRemetente);
	}
	
    /**
     * Busca os ids das notas que possuem o mesmo nr_nota e cnpj_reme da NotaFiscalEdi recebida por parâmetro.
     *  
     * @param idNotaFiscalEdi
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findIdsByIdNotaFiscalEdi(Long idNotaFiscalEdi) {
    	return getNotaFiscalEdiDAO().findIdsByIdNotaFiscalEdi(idNotaFiscalEdi);
    }
    
    /**
     * Remove todas as notas fiscais que são iguais as notas dos ids recebidos.
     * 
     * @param list lista de ids de idNotaFiscalEdi
     */
    public void removeByIdNotaFiscalEdi(List<Long> list) {
    	getNotaFiscalEdiDAO().removeByIdNotaFiscalEdi(list);
    }

    public Long findNextNrProcessamento(){
    	return getNotaFiscalEdiDAO().findNextNrProcessamento();
    }

    public Long findNextNrOrdemEmissaoEDI(){
    	return getNotaFiscalEdiDAO().findNextNrOrdemEmissaoEDI();
    }
    
    public Integer findParametroGeralThreads(){
    	return getNotaFiscalEdiDAO().findParametroGeralThreads();
    }

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public List<NotaFiscalEdi> findByCCE(String nrCCE) {
		Long cce = Long.valueOf(nrCCE);
		return getNotaFiscalEdiDAO().findByCCE(cce);
	}

}
