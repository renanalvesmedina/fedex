package com.mercurio.lms.expedicao.model.service;

import java.sql.Blob;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.AnexoAwb;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.dao.AnexoAwbDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.FileUtils;

public class AnexoAwbService extends CrudService<AnexoAwb, Long>{
		
	public void store(List<byte[]> anexos, Awb awb, Boolean insereAwb) {
		for (byte[] anexo : anexos){
			String extensao = FileUtils.getFileExtensionFromBlob(anexo);
			AnexoAwb anexoAwb = new AnexoAwb();
			anexoAwb.setAwb(awb);
			anexoAwb.setDcAnexo(anexo);
			if(insereAwb){
				if(ConstantesExpedicao.TP_ANEXO_XML.equalsIgnoreCase(extensao)){
					anexoAwb.setDsAnexo(ConstantesExpedicao.INCLUSAO_XML_AWB);
					anexoAwb.setTpAnexo(ConstantesExpedicao.SG_ANEXO_XML);
				}else if(ConstantesExpedicao.TP_ANEXO_PDF.equalsIgnoreCase(extensao)){
					anexoAwb.setDsAnexo(ConstantesExpedicao.INCLUSAO_PDF_AWB);
					anexoAwb.setTpAnexo(ConstantesExpedicao.SG_ANEXO_PDF);
				}else{
					anexoAwb.setDsAnexo(ConstantesExpedicao.INCLUSAO_OUTRO_ANEXO_AWB);
					anexoAwb.setTpAnexo(ConstantesExpedicao.SG_ANEXO_OUTRO);
				}
			}else{
				anexoAwb.setDsAnexo(ConstantesExpedicao.CANCELAMENTO_XML_AWB);
				anexoAwb.setTpAnexo(ConstantesExpedicao.SG_ANEXO_XML);
			}
			
			super.store(anexoAwb);
		}
		
	}
		
	public void setParcelaAwbDao(AnexoAwbDAO dao) {
		setDao( dao );
	}
	
	public AnexoAwbDAO getParcelaAwbDao() {
		return (AnexoAwbDAO) this.getDao();
	}

	public Blob findPdfAnexo(Long idAwb){
		return getParcelaAwbDao().findPdfAnexo(idAwb);		
	}
}
