package com.mercurio.lms.rest.entrega;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.entrega.model.DoctoServicoIroad;
import com.mercurio.lms.entrega.model.service.DoctoServicoIroadService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/entrega/uploadRelatorioIRoad")
public class UploadRelatorioIRoadRest extends BaseRest{
	
	private static final String LINE_FEED = "\n";
	private static final short PATHFIND_CELl_DS_ROTA = 2;
	private static final short PATHFIND_CELl_CD_FILIAL = 4;
	private static final short PATHFIND_CELl_NR_DOCTO = 5;
	private static final short PATHFIND_CELl_NR_SEQ_ROTA = 23;
	
	private static final short ROUTEASY_CELl_DS_ROTA = 0;
	private static final short ROUTEASY_CELl_CD_FILIAL = 25;
	private static final short ROUTEASY_CELl_NR_DOCTO = 26;
	private static final short ROUTEASY_CELl_NR_SEQ_ROTA = 33;
	
	
	
	@InjectInJersey
	DoctoServicoIroadService doctoServicoIroadService;
	
	@InjectInJersey
	ConfiguracoesFacade configuracoesFacade;
 	
	

	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(FormDataMultiPart formData) throws IOException{
		String tipoArquivo = formData.getField("tpArquivo").getValue();
		
		String result = null;
		try{
			result = importFile(formData.getField("arquivoUpload"),tipoArquivo);
		}catch(Exception exception){
			result = configuracoesFacade.getMensagem("LMS-09167");
		}
			
		return Response.ok(result).build();
	}
	
	
	private String importFile(FormDataBodyPart filePart,String tipoArquivo) throws IOException{
		ContentDisposition headerOfFilePart = filePart.getContentDisposition();
		String fileName = headerOfFilePart.getFileName();
		StringBuilder result = new StringBuilder();
		
		HSSFWorkbook workbook = new HSSFWorkbook(filePart.getValueAs(InputStream.class));
		HSSFSheet hssfSheet = workbook.getSheetAt(0);
		
		int rownum = 1;
		int errorCount = 0;
	
		do{
			HSSFRow row = hssfSheet.getRow(rownum++);
			
			try{
				
				if (row != null){
					importRow(row, tipoArquivo);
				}
			}catch (BusinessException be ) {				
				String error = be.getMessageKey()+": "+configuracoesFacade.getMensagem(be.getMessageKey(),be.getMessageArguments());
				result.append("Linha ").append(rownum).append(": ").append(error).append(LINE_FEED);
				errorCount++;
			}catch(Exception e){
				result.append("Linha ").append(rownum).append(": ").append(e.getMessage()).append(LINE_FEED);
				errorCount++;
				e.printStackTrace();
			}
			
		}while (rownum <= hssfSheet.getLastRowNum());
		
		
		result.append(LINE_FEED);
		result.append(configuracoesFacade.getMensagem("processamentoRealizadoSucesso")).append(LINE_FEED);
		result.append(LINE_FEED);
		result.append(configuracoesFacade.getMensagem("arquivoProcessado",new Object[]{fileName})).append(LINE_FEED);
		result.append(configuracoesFacade.getMensagem("totalProcessados",new Object[]{new Long(rownum-1)})).append(LINE_FEED);		
		result.append(configuracoesFacade.getMensagem("totalErros",new Object[]{new Long(errorCount)})).append(LINE_FEED);
		return result.toString();
	}
	
	
	private void importRow(HSSFRow row, String tipoArquivo) throws BusinessException {
		DoctoServicoIroad doctoServicoIroad = null;
		if("PATHFIND".equals(tipoArquivo)){
			 doctoServicoIroad = buildDoctoServicoIroadPathfind(row);
		}else{
			doctoServicoIroad = buildDoctoServicoIroadRouteEasy(row);
		}

		if (doctoServicoIroad != null){
			doctoServicoIroadService.store(doctoServicoIroad);
		}
		
	}


	private DoctoServicoIroad buildDoctoServicoIroadRouteEasy(HSSFRow row) {
		DoctoServicoIroad doctoServicoIroad = null;
		Long idDoctoServico = doctoServicoIroadService.findDoctoServico(getCellString(ROUTEASY_CELl_CD_FILIAL, row),getCellString(ROUTEASY_CELl_NR_DOCTO, row));
		if (idDoctoServico != null){
			DoctoServico doctoServico = new DoctoServico();
			doctoServico.setIdDoctoServico(idDoctoServico);
					
			doctoServicoIroad = getDoctoServicoIroad(doctoServico);
			doctoServicoIroad.setDoctoServico(doctoServico);
			doctoServicoIroad.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
			
			UsuarioLMS usuarioLMS = new UsuarioLMS();
			usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
			doctoServicoIroad.setUsuarioInclusao(usuarioLMS);
			doctoServicoIroad.setDsRotaIroad(getCellString(ROUTEASY_CELl_DS_ROTA, row));
			String nrSeqRota = getCellString(ROUTEASY_CELl_NR_SEQ_ROTA, row);
			doctoServicoIroad.setNrSequenciaRota(nrSeqRota);
		}else{
			throw new BusinessException("LMS-09168", new Object[]{getCellString(ROUTEASY_CELl_NR_DOCTO, row)});
		}
		
		return doctoServicoIroad;
	}


	private DoctoServicoIroad buildDoctoServicoIroadPathfind(HSSFRow row) {
		DoctoServicoIroad doctoServicoIroad = null;
		Long idDoctoServico = doctoServicoIroadService.findDoctoServico(getCellString(PATHFIND_CELl_CD_FILIAL, row),getCellString(PATHFIND_CELl_NR_DOCTO, row));
		if (idDoctoServico != null){
			DoctoServico doctoServico = new DoctoServico();
			doctoServico.setIdDoctoServico(idDoctoServico);
					
			doctoServicoIroad = getDoctoServicoIroad(doctoServico);
			doctoServicoIroad.setDoctoServico(doctoServico);
			doctoServicoIroad.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
			
			UsuarioLMS usuarioLMS = new UsuarioLMS();
			usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
			doctoServicoIroad.setUsuarioInclusao(usuarioLMS);
			doctoServicoIroad.setDsRotaIroad(getCellString(PATHFIND_CELl_DS_ROTA, row));
			String nrSeqRota = getCellString(PATHFIND_CELl_NR_SEQ_ROTA, row);
			doctoServicoIroad.setNrSequenciaRota(nrSeqRota);
		}else{
			throw new BusinessException("LMS-09168", new Object[]{getCellString(PATHFIND_CELl_NR_DOCTO, row)});
		}
		
		return doctoServicoIroad;
	}

	
	private DoctoServicoIroad getDoctoServicoIroad(DoctoServico doctoServico){
		DoctoServicoIroad doctoServicoIroad = doctoServicoIroadService.findByDoctoServico(doctoServico);
		if (doctoServicoIroad != null){
			return doctoServicoIroad;
		}else{
			return new DoctoServicoIroad();
		}
	}

	private String getCellString(short cellNum, HSSFRow row){
		HSSFCell cell = row.getCell(cellNum) ;

		
		if (cell!= null){
			switch(cell.getCellType()){
			
				case HSSFCell.CELL_TYPE_NUMERIC:
					
					Double value = new Double(cell.getNumericCellValue());
					return ""+value.longValue();
				
				case HSSFCell.CELL_TYPE_STRING:
					return cell.getStringCellValue();
			}
		}
		return null;
	}
}