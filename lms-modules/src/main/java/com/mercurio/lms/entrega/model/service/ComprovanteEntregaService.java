package com.mercurio.lms.entrega.model.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.lob.BlobImpl;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoNaturaDMN;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.entrega.model.ComprovanteEntrega;
import com.mercurio.lms.entrega.model.dao.ComprovanteEntregaDAO;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.pendencia.model.dao.OcorrenciaPendenciaDAO;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.pendencia.ocorrenciaPendenciaService"
 */
public class ComprovanteEntregaService extends CrudService<ComprovanteEntrega, Long> {

	private Logger log = LogManager.getLogger(this.getClass());
	private ConhecimentoService conhecimentoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ComprovanteEntrega bean) {
		if (bean.getIdComprovanteEntrega() == null) {
			throw new BusinessException("LMS-17001");
		}
		return super.store(bean);
	}

	public void storeAssinatura(ComprovanteEntrega comprovanteEntrega) {
		getDao().store(comprovanteEntrega);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param instância do DAO.
	 */
	public void setComprovanteEntregaDAO(ComprovanteEntregaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos
	 * dados deste serviço.
	 *
	 * @return instância do DAO.
	 */
	private OcorrenciaPendenciaDAO getOcorrenciaPendenciaDAO() {
		return (OcorrenciaPendenciaDAO) getDao();
	}

	public void updateComprovanteEntrega(EventoDocumentoServicoNaturaDMN eventoDocumentoServicoNaturaDMN) {
		ComprovanteEntrega comprovanteEntrega = retornaObjetoComprovanteEntrega(eventoDocumentoServicoNaturaDMN);
		if (comprovanteEntrega != null){
			Blob blobImg = new BlobImpl(eventoDocumentoServicoNaturaDMN.getMidia());
			comprovanteEntrega.setAssinatura(blobImg);
			comprovanteEntrega.setBlEnviado(Boolean.TRUE);
			getDao().store(comprovanteEntrega, true);
		}
	}

	public void updateTentativaEnvioComprovanteEntrega(EventoDocumentoServicoNaturaDMN eventoDocumentoServicoNaturaDMN) {
		if (eventoDocumentoServicoNaturaDMN.getIdDoctoServico() == null && eventoDocumentoServicoNaturaDMN.getIdNotaFiscalConhecimento() == null){
			return;
		}
		ComprovanteEntrega comprovanteEntrega = retornaObjetoComprovanteEntrega(eventoDocumentoServicoNaturaDMN);
		if (comprovanteEntrega != null){
			comprovanteEntrega.setNrTentativaEnvio(comprovanteEntrega.getNrTentativaEnvio() == null ? 1 : (comprovanteEntrega.getNrTentativaEnvio() + 1));
			getDao().store(comprovanteEntrega, true);
		}
	}
	
	public ComprovanteEntrega retornaObjetoComprovanteEntrega(EventoDocumentoServicoNaturaDMN eventoDocumentoServicoNaturaDMN){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idDoctoServico", eventoDocumentoServicoNaturaDMN.getIdDoctoServico());
		parameters.put("idNotaFiscalConhecimento", eventoDocumentoServicoNaturaDMN.getIdNotaFiscalConhecimento());
		
		List<ComprovanteEntrega> lista = super.find(parameters);
		
		boolean flag = lista == null || lista.size() == 0 || lista.get(0) == null;
		
		if (flag || Boolean.TRUE.equals(lista.get(0).getBlEnviado())) {
			return null;
		}else{
			return lista.get(0);
		}
	}

	
	/**
	 * LMSA-7393 - 09/07/2018 - Inicio
	 * Responsável por persistir em base a assinatura digital de determinado
	 * conhecimento e nota obtidos a partir do código de barras.
	 * @param barcode Codigo de Barras, Imagem imagem 
	 */
   public void generateAssinaturaDigital(String barcode, String imagem) {
	   
	    Conhecimento conhecimento;
	    
	    if(barcode.length() < 20) {
	    	Long nrCodigoBarras = Long.valueOf(barcode);
	    	conhecimento = conhecimentoService.findByNrCodigoBarras(nrCodigoBarras);
	    } else {
	    	conhecimento = conhecimentoService.findByNrChave(barcode);
	    }	
		
		if(conhecimento== null) {
			throw new BusinessException("LMS-45031");
		}		
	  
		if(imagem != null && !imagem.isEmpty()) {
			
			Map criteria = new HashMap<String, Object>();
			Long idDoctoServico = conhecimento.getIdDoctoServico();
			criteria.put("idDoctoServico", idDoctoServico);
			List list = this.find(criteria);
			
			if(list == null || list.isEmpty()) {
				try {
					byte[] byteImage = Base64Util.decode(imagem);					
					Blob blobImg = new BlobImpl(byteImage);	
					
					List<NotaFiscalConhecimento> notasFiscaisConhecimento = notaFiscalConhecimentoService.findByConhecimento(idDoctoServico);
					
					for (NotaFiscalConhecimento notaFiscalConhecimento : notasFiscaisConhecimento) {
						
						criteria.put("idNotaFiscalConhecimento", notaFiscalConhecimento.getIdNotaFiscalConhecimento());						
						list = this.find(criteria);
						
						if (list != null && list.size() > 0) {
							continue;
						}
						
						ComprovanteEntrega comprovanteEntrega = new ComprovanteEntrega();
						comprovanteEntrega.setAssinatura(blobImg);
						comprovanteEntrega.setBlEnviado(Boolean.FALSE);
						comprovanteEntrega.setIdDoctoServico(idDoctoServico);
						comprovanteEntrega.setIdNotaFiscalConhecimento(notaFiscalConhecimento.getIdNotaFiscalConhecimento());
						comprovanteEntrega.setIdUsuarioInclusao(SessionUtils.getUsuarioLogado().getIdUsuario());
						this.storeAssinatura(comprovanteEntrega);							
					}
				} catch(ClassCastException e) {
					log.error(e);
				} catch (IOException e) {
					log.error(e);
				}
			}
			
			
		}
	}
   
   public ComprovanteEntregaDAO getComprovanteEntregaDAO() {
       return (ComprovanteEntregaDAO) getDao();
   }
	
   public byte[] findAssinaturaByDoctoServico(Long idDoctoServico) {

       byte[] retorno = getComprovanteEntregaDAO().findAssinaturaByDoctoServico(idDoctoServico);
       
       if (retorno == null) {
           throw new BusinessException("Comprovante de entrega assinado não encontrada.");
       }

       return retorno;
   }  

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}
	
	//LMSA-7393 - 09/07/2018 - Fim
}
