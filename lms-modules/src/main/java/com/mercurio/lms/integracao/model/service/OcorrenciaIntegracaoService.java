package com.mercurio.lms.integracao.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.integration.model.IRegistroGeral;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.integracao.model.OcorrenciaIntegracao;
import com.mercurio.lms.integracao.model.dao.OcorrenciaIntegracaoDAO;
import com.mercurio.lms.integracao.util.MessageConverter;
import com.mercurio.lms.integration.pilmsc001e.ACBAA;
import com.mercurio.lms.integration.pilmsc001e.ACCAA;
import com.mercurio.lms.integration.pilmsc001e.ACP;
import com.mercurio.lms.integration.pilmsc001e.ACSAA;
import com.mercurio.lms.integration.pilmsc001e.ACZ;
import com.mercurio.lms.integration.pilmsc001e.AEDCAA;
import com.mercurio.lms.integration.pilmsc001e.AEPAAA;
import com.mercurio.lms.integration.pilmsc001e.AEPDAA;
import com.mercurio.lms.integration.pilmsc001e.CEAA;
import com.mercurio.lms.integration.pilmsc001e.CT50AA;
import com.mercurio.lms.integration.pilmsc001e.CT55AA;
import com.mercurio.lms.integration.pilmsc001e.CT60AA;
import com.mercurio.lms.integration.pilmsc001e.CT65AA;
import com.mercurio.lms.integration.pilmsc001e.FC01AA;
import com.mercurio.lms.integration.pilmsc001e.FC02AA;
import com.mercurio.lms.integration.pilmsc001e.ICAA;
import com.mercurio.lms.integration.pilmsc019e.CL;
import com.mercurio.lms.integration.pilmsc019e.CO01;
import com.mercurio.lms.integration.pilmsc019e.CO02;
import com.mercurio.lms.integration.pilmsc019e.DC01;
import com.mercurio.lms.integration.pilmsc019e.DC02;
import com.mercurio.lms.integration.pilmsc019e.EM;
import com.mercurio.lms.integration.pilmsc019e.ID;
import com.mercurio.lms.integration.pilmsc019e.MA;
import com.mercurio.lms.integration.pilmsc019e.MO00;
import com.mercurio.lms.integration.pilmsc019e.MO01;
import com.mercurio.lms.integration.pilmsc019e.NCB;
import com.mercurio.lms.integration.pilmsc019e.NCD;
import com.mercurio.lms.integration.pilmsc019e.NCP;
import com.mercurio.lms.integration.pilmsc019e.NF;
import com.mercurio.lms.integration.pilmsc019e.OB;
import com.mercurio.lms.integration.pilmsc019e.PC;
import com.mercurio.lms.integration.pilmsc019e.PR;
import com.mercurio.lms.integration.pilmsc019e.RegistroGeral;
import com.mercurio.lms.integration.pilmsc019e.TK;
import com.mercurio.lms.integration.pilmsc019e.VE;
import com.mercurio.lms.integration.pilmsf26e.ACBAT;
import com.mercurio.lms.integration.pilmsf26e.ACCAT;
import com.mercurio.lms.integration.pilmsf26e.ACPAT;
import com.mercurio.lms.integration.pilmsf26e.ACSAT;
import com.mercurio.lms.integration.pilmsf26e.AG1AT;
import com.mercurio.lms.integration.pilmsf26e.AW01AT;
import com.mercurio.lms.integration.pilmsf26e.AWAT;
import com.mercurio.lms.integration.pilmsf26e.CEAT;
import com.mercurio.lms.integration.pilmsf26e.CLAT;
import com.mercurio.lms.integration.pilmsf26e.CO01AT;
import com.mercurio.lms.integration.pilmsf26e.DCAT;
import com.mercurio.lms.integration.pilmsf26e.EMAT;
import com.mercurio.lms.integration.pilmsf26e.IDAT;
import com.mercurio.lms.integration.pilmsf26e.MAAT;
import com.mercurio.lms.integration.pilmsf26e.MBAT;
import com.mercurio.lms.integration.pilmsf26e.MEAT;
import com.mercurio.lms.integration.pilmsf26e.MO00AT;
import com.mercurio.lms.integration.pilmsf26e.NCBAT;
import com.mercurio.lms.integration.pilmsf26e.NCDAT;
import com.mercurio.lms.integration.pilmsf26e.NCNAT;
import com.mercurio.lms.integration.pilmsf26e.NCPAT;
import com.mercurio.lms.integration.pilmsf26e.NFAT;
import com.mercurio.lms.integration.pilmsf26e.PCAT;
import com.mercurio.lms.integration.pilmsf26e.PRAT;
import com.mercurio.lms.integration.pilmsf26e.RCAT;
import com.mercurio.lms.integration.pilmsf26e.SCAT;
import com.mercurio.lms.integration.pilmsf26e.SNAT;
import com.mercurio.lms.integration.pilmsf26e.TEAT;
import com.mercurio.lms.integration.pilmsf26e.TKAT;
import com.mercurio.lms.integration.pilmsf26e.VCAT;
import com.mercurio.lms.integration.pilmsf26e.VEAT;
import com.mercurio.lms.integration.pilmsf27e.BD00AT;
import com.mercurio.lms.integration.pilmsf27e.BD01AT;
import com.mercurio.lms.integration.pilmsf27e.BLAT;
import com.mercurio.lms.integration.pilmsf27e.FBAT;
import com.mercurio.lms.integration.pilmsf27e.MR00AT;
import com.mercurio.lms.integration.pilmsf27e.MR01AT;
import com.mercurio.lms.integration.pilmsf27e.MT00AT;
import com.mercurio.lms.integration.pilmsf27e.MT01AT;
import com.mercurio.lms.integration.pilmsf27e.OB00AT;
import com.mercurio.lms.integration.pilmsf27e.OB01AT;
import com.mercurio.lms.integration.pilmsf27e.RE00AT;
import com.mercurio.lms.integration.pilmsf27e.RE01AT;
import com.mercurio.lms.integration.pilmsf27e.RO00AT;
import com.mercurio.lms.integration.pilmsf27e.RO01AT;
import com.mercurio.lms.integration.pilmsj006e.JDRP;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.integracao.ocorrenciaIntegracaoService"
 */
public class OcorrenciaIntegracaoService extends CrudService<OcorrenciaIntegracao, Long>{

	public static enum piTypes {PILMSC001E, PILMSC019E, PILMSF26E, PILMSF27E, PILMSJ006E};
	
	public static enum PILMSC001E {ACBAA, ACCAA, ACP, ACSAA, ACZ, AEDCAA, AEPAAA, AEPDAA, CEAA, CT50AA, CT55AA, CT60AA, CT65AA, FC01AA, FC02AA, ICAA, NCP};							 
								 
	public static enum PILMSC019E {CL, CO01, CO02, DC01, DC02, EM, ID, MA, MO00, MO01, NCB, NCD, NCP, NF, OB, PC, PR, VE, TK};
									
	public static enum PILMSF26E {ACBAT, ACCAT, ACPAT, ACSAT, AG1AT, AW01AT, AWAT, CEAT, CLAT, CO01AT, DCAT, EMAT, IDAT, MAAT, MBAT, MEAT, MO00AT, NCBAT, NCDAT, NCNAT, NCPAT, NFAT, PCAT, PRAT, RCAT, SCAT, SNAT, TEAT, VCAT, VEAT, TKAT};
	
	public static enum PILMSF27E {BD00AT, BD01AT, BLAT, FBAT, MR00AT, MR01AT, MT00AT, MT01AT, OB00AT, OB01AT, RE00AT, RE01AT, RO00AT, RO01AT};
	
	public static enum PILMSJ006E{JDRP};
	
	public Map<String, Object> findDadosOcorrenciaById(Long id, String layout) {
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("idOcorrenciaIntegracao", ocorrenciaIntegracao.getIdOcorrenciaIntegracao());
		result.put("listLayout", getLayoutList(ocorrenciaIntegracao, layout));
		return result;
	}
	
	public ResultSetPage findPaginatedLayoutList(String layout, String pi, String sgFilial, Integer nrDocumento, FindDefinition findDef) {
		return this.findPaginatedLayoutList(layout, pi, sgFilial, nrDocumento, null, null, findDef);
	}
	public ResultSetPage findPaginatedLayoutListOB(String layout, String pi, String sgFilial, String nrDocumento, FindDefinition findDef) {
		return this.findPaginatedLayoutListOB(layout, pi, sgFilial, nrDocumento, null, null, findDef);
	}
	
	public ResultSetPage findPaginatedLayoutList(String layout, String pi, String sgFilial, Integer nrDocumento, String sgFilial2, Integer nrDocumento2, FindDefinition findDef) {
		ResultSetPage rsp = getOcorrenciasIntegracaoDAO().findPaginated(layout, pi, false, findDef, sgFilial, nrDocumento, sgFilial2, nrDocumento2);
		List<Map> result = new LinkedList<Map>();
		List<OcorrenciaIntegracao> queryResult = rsp.getList();
		
		if(queryResult != null) {
			for (OcorrenciaIntegracao ocorrenciaIntegracao : queryResult) {
				Map<String, Object> record = new HashMap<String, Object>();
				record.put("idOcorrenciaIntegracao", ocorrenciaIntegracao.getIdOcorrenciaIntegracao());
				record.put("filial", ocorrenciaIntegracao.getFilial());
				record.put("codigoDocumento", ocorrenciaIntegracao.getCodigoDocumento());
				record.put("filial2", ocorrenciaIntegracao.getFilial2());
				record.put("codigoDocumento2", ocorrenciaIntegracao.getCodigoDocumento2());
				record.put("errorMessage", ocorrenciaIntegracao.getDsErro());
				result.add(record);
			}
		}
		rsp.setList(result);
		return rsp;
	}	
	public ResultSetPage findPaginatedLayoutListOB(String layout, String pi, String sgFilial, String nrDocumento, String sgFilial2, String nrDocumento2, FindDefinition findDef) {
		ResultSetPage rsp = getOcorrenciasIntegracaoDAO().findPaginatedOB(layout, pi, false, findDef, sgFilial, nrDocumento, sgFilial2, nrDocumento2);
		List<Map> result = new LinkedList<Map>();
		List<OcorrenciaIntegracao> queryResult = rsp.getList();
		
		if(queryResult != null) {
			for (OcorrenciaIntegracao ocorrenciaIntegracao : queryResult) {
				Map<String, Object> record = new HashMap<String, Object>();
				record.put("idOcorrenciaIntegracao", ocorrenciaIntegracao.getIdOcorrenciaIntegracao());
				record.put("filial", ocorrenciaIntegracao.getFilial());
				record.put("codigoDocumento", ocorrenciaIntegracao.getCodigoDocumento());
				record.put("filial2", ocorrenciaIntegracao.getFilial2());
				record.put("codigoDocumento2", ocorrenciaIntegracao.getCodigoDocumento2());
				record.put("errorMessage", ocorrenciaIntegracao.getDsErro());
				result.add(record);
			}
		}
		rsp.setList(result);
		return rsp;
	}
	
	public Integer getRowCount(String layout, String pi, String filial, Integer codigoDocumento) {
		return this.getRowCount(layout, pi, filial, codigoDocumento, null, null);
	}
	public Integer getRowCountOB(String layout, String pi, String filial, String codigoDocumento) {
		return this.getRowCountOB(layout, pi, filial, codigoDocumento, null, null);
	}
	
	public Integer getRowCount(String layout, String pi, String filial, Integer codigoDocumento, String filial2, Integer codigoDocumento2) {
		return getOcorrenciasIntegracaoDAO().getRowCount(layout, pi, false, filial, codigoDocumento, filial2, codigoDocumento2);
	}
	public Integer getRowCountOB(String layout, String pi, String filial, String codigoDocumento, String filial2, String codigoDocumento2) {
		return getOcorrenciasIntegracaoDAO().getRowCountOB(layout, pi, false, filial, codigoDocumento, filial2, codigoDocumento2);
	}
	
	public void storeLayout(final String layout, final String piType, Long idOcorrenciaIntegracao, List list) throws Exception{
		IRegistroGeral registroGeral = null;
		
		if(piType.equals(piTypes.PILMSC001E.toString())){
			registroGeral = getNewIRegistroGeralLayoutPICE001();
		} else if(piType.equals(piTypes.PILMSC019E.toString())){
			registroGeral = getNewIRegistroGeralLayoutPICo19e();
		} else if(piType.equals(piTypes.PILMSF26E.toString())){ 
			registroGeral  = getNewIRegistroGeralLayoutPICo26e();
		} else if(piType.equals(piTypes.PILMSF27E.toString())){ 
			registroGeral  = getNewIRegistroGeralLayoutPICo27e();
		} else if(piType.equals(piTypes.PILMSJ006E.toString())){
			registroGeral  = getNewIRegistroGeralLayoutPIJ006e();			
		} else {
			throw new Exception("Obrigatorio informar a PI do registro");
		}
		
		registroGeral.setter(layout, list);
		
		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piType);
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutSCATPI26e(Long idOcorrenciaIntegracao, List<SCAT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.SCAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
		
	}
	
	public void storeLayoutNCBPI19e(Long idOcorrenciaIntegracao, List<NCB> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo19e();
		registroGeral.setter(PILMSC019E.NCB.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSC019E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
		}
		
	public void storeLayoutNCBATPI26e(Long idOcorrenciaIntegracao, List<NCBAT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.NCBAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
		
	}
	
	public void storeLayoutIDPI19e(Long idOcorrenciaIntegracao, List<ID> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo19e();
		registroGeral.setter(PILMSC019E.ID.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSC019E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutRE00AT26e(Long idOcorrenciaIntegracao, List<RE00AT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo27e();
		registroGeral.setter(PILMSF27E.RE00AT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF27E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutIDATPI26e(Long idOcorrenciaIntegracao, List<IDAT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.IDAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
		
	}
	
	public void storeLayoutMaPICo19e(Long idOcorrenciaIntegracao, List<MA> listMA) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo19e();
		registroGeral.setter(PILMSC019E.MA.toString(), listMA);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSC019E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
		}
		
	public void storeLayoutCO01PICo19e(Long idOcorrenciaIntegracao, List<CO01> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo19e();
		registroGeral.setter(PILMSC019E.CO01.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSC019E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutTKPI19e(Long idOcorrenciaIntegracao, List<TK> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo19e();
		registroGeral.setter(PILMSC019E.TK.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSC019E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutCEAAPI001e(Long idOcorrenciaIntegracao, List<CEAA> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICE001();
		registroGeral.setter(PILMSC001E.CEAA.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSC001E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}	
	
	public void storeLayoutCT65AAPI01e(Long idOcorrenciaIntegracao, List<CT65AA> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICE001();
		registroGeral.setter(PILMSC001E.CT65AA.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSC001E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutAG1ATe(Long idOcorrenciaIntegracao, List<AG1AT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.AG1AT.toString(), list);
		
		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutMEATPI26e(Long idOcorrenciaIntegracao, List<MEAT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.MEAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutCEATPI26e(Long idOcorrenciaIntegracao, List<CEAT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.CEAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}	
	
	public void storeLayoutMaAtPICo26e(Long idOcorrenciaIntegracao, List<MAAT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.MAAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutMBPI26e(Long idOcorrenciaIntegracao, List<MBAT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.MBAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutCO01ATPICo26e(Long idOcorrenciaIntegracao, List<CO01AT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.CO01AT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
    
	public void storeLayoutAWATPI26e(Long idOcorrenciaIntegracao, List<AWAT> list) {

		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.AWAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
		
	}
	
	public void storeLayoutTKATPI26e(Long idOcorrenciaIntegracao, List<TKAT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.TKAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}	

	public void storeLayoutRo00AtPICo27e(Long idOcorrenciaIntegracao, List<RO00AT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo27e();
		registroGeral.setter(PILMSF27E.RO00AT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF27E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutBd00AtPICo27e(Long idOcorrenciaIntegracao, List<BD00AT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo27e();
		registroGeral.setter(PILMSF27E.BD00AT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF27E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutFbAtPICo27e(Long idOcorrenciaIntegracao, List<FBAT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo27e();
		registroGeral.setter(PILMSF27E.FBAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF27E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutMr00AtPICo27e(Long idOcorrenciaIntegracao, List<MR00AT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo27e();
		registroGeral.setter(PILMSF27E.MR00AT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF27E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutMt00AtPICo27e(Long idOcorrenciaIntegracao, List<MT00AT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo27e();
		registroGeral.setter(PILMSF27E.MT00AT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF27E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutOb00AtPICo27e(Long idOcorrenciaIntegracao, List<OB00AT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo27e();
		registroGeral.setter(PILMSF27E.OB00AT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF27E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
    
	public void storeLayoutSNPI26e(Long idOcorrenciaIntegracao, List<SNAT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.SNAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutVCPI26e(Long idOcorrenciaIntegracao, List<VCAT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.VCAT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
    
	public void storeLayoutJDRPe(Long idOcorrenciaIntegracao, List<JDRP> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPIJ006e();
		registroGeral.setter(PILMSJ006E.JDRP.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSJ006E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutMO00PICo19e(Long idOcorrenciaIntegracao, List<MO00> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo19e();
		registroGeral.setter(PILMSC019E.MO00.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSC019E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
	public void storeLayoutMO00ATPICo26e(Long idOcorrenciaIntegracao, List<MO00AT> list) {
		IRegistroGeral registroGeral = getNewIRegistroGeralLayoutPICo26e();
		registroGeral.setter(PILMSF26E.MO00AT.toString(), list);

		MessageConverter messageConverter = new MessageConverter();
		String xml = messageConverter.toMessage(registroGeral, piTypes.PILMSF26E.toString());
		
		OcorrenciaIntegracao ocorrenciaIntegracao = getOcorrenciasIntegracaoDAO().findById(idOcorrenciaIntegracao);
		ocorrenciaIntegracao.setMessage(xml.getBytes());
		ocorrenciaIntegracao.setApprove(true);
		getOcorrenciasIntegracaoDAO().store(ocorrenciaIntegracao);
	}
	
    private IRegistroGeral getNewIRegistroGeralLayoutPICo27e() {
    	IRegistroGeral registroGeral = new com.mercurio.lms.integration.pilmsf27e.RegistroGeral();
		registroGeral.setter(PILMSF27E.BD00AT.toString(), new ArrayList<BD00AT>());
		registroGeral.setter(PILMSF27E.BD01AT.toString(), new ArrayList<BD01AT>());
		registroGeral.setter(PILMSF27E.BLAT.toString(), new ArrayList<BLAT>());
		registroGeral.setter(PILMSF27E.FBAT.toString(), new ArrayList<FBAT>());
		registroGeral.setter(PILMSF27E.MR00AT.toString(), new ArrayList<MR00AT>());
		registroGeral.setter(PILMSF27E.MR01AT.toString(), new ArrayList<MR01AT>());
		registroGeral.setter(PILMSF27E.MT00AT.toString(), new ArrayList<MT00AT>());
		registroGeral.setter(PILMSF27E.MT01AT.toString(), new ArrayList<MT01AT>());
		registroGeral.setter(PILMSF27E.OB00AT.toString(), new ArrayList<OB00AT>());
		registroGeral.setter(PILMSF27E.OB01AT.toString(), new ArrayList<OB01AT>());
		registroGeral.setter(PILMSF27E.RE00AT.toString(), new ArrayList<RE00AT>());
		registroGeral.setter(PILMSF27E.RE01AT.toString(), new ArrayList<RE01AT>());
		registroGeral.setter(PILMSF27E.RO00AT.toString(), new ArrayList<RO00AT>());
		registroGeral.setter(PILMSF27E.RO01AT.toString(), new ArrayList<RO01AT>());
		return registroGeral;
    }
    
    private IRegistroGeral getNewIRegistroGeralLayoutPICE001() {
    	IRegistroGeral registroGeral = new com.mercurio.lms.integration.pilmsc001e.RegistroGeral();
    	registroGeral.setter(PILMSC001E.ACBAA.toString(), new ArrayList<ACBAA>());
    	registroGeral.setter(PILMSC001E.ACCAA.toString(), new ArrayList<ACCAA>());
    	registroGeral.setter(PILMSC001E.ACP.toString(), new ArrayList<ACP>());
    	registroGeral.setter(PILMSC001E.ACSAA.toString(), new ArrayList<ACSAA>());
    	registroGeral.setter(PILMSC001E.ACZ.toString(), new ArrayList<ACZ>());
    	registroGeral.setter(PILMSC001E.AEDCAA.toString(), new ArrayList<AEDCAA>());
    	registroGeral.setter(PILMSC001E.AEPAAA.toString(), new ArrayList<AEPAAA>());
    	registroGeral.setter(PILMSC001E.AEPDAA.toString(), new ArrayList<AEPDAA>());
    	registroGeral.setter(PILMSC001E.CEAA.toString(), new ArrayList<CEAA>());
    	registroGeral.setter(PILMSC001E.CT50AA.toString(), new ArrayList<CT50AA>());
    	registroGeral.setter(PILMSC001E.CT55AA.toString(), new ArrayList<CT55AA>());
    	registroGeral.setter(PILMSC001E.CT60AA.toString(), new ArrayList<CT60AA>());
    	registroGeral.setter(PILMSC001E.CT65AA.toString(), new ArrayList<CT65AA>());
    	registroGeral.setter(PILMSC001E.FC01AA.toString(), new ArrayList<FC01AA>());
    	registroGeral.setter(PILMSC001E.FC02AA.toString(), new ArrayList<FC02AA>());
    	registroGeral.setter(PILMSC001E.ICAA.toString(), new ArrayList<ICAA>());
    	registroGeral.setter(PILMSC001E.NCP.toString(), new ArrayList<NCP>());
		return registroGeral;
    }    
        
    private IRegistroGeral getNewIRegistroGeralLayoutPICo19e() {
    	IRegistroGeral registroGeral = new RegistroGeral();
		registroGeral.setter(PILMSC019E.CL.toString(), new ArrayList<CL>());
		registroGeral.setter(PILMSC019E.CO01.toString(), new ArrayList<CO01>());
		registroGeral.setter(PILMSC019E.CO02.toString(), new ArrayList<CO02>());
		registroGeral.setter(PILMSC019E.DC01.toString(), new ArrayList<DC01>());
		registroGeral.setter(PILMSC019E.DC02.toString(), new ArrayList<DC02>());
		registroGeral.setter(PILMSC019E.EM.toString(), new ArrayList<EM>());
		registroGeral.setter(PILMSC019E.ID.toString(), new ArrayList<ID>());
		registroGeral.setter(PILMSC019E.MA.toString(), new ArrayList<MA>());
		registroGeral.setter(PILMSC019E.MO00.toString(), new ArrayList<MO00>());
		registroGeral.setter(PILMSC019E.MO01.toString(), new ArrayList<MO01>());
		registroGeral.setter(PILMSC019E.NCB.toString(), new ArrayList<NCB>());
		registroGeral.setter(PILMSC019E.NCD.toString(), new ArrayList<NCD>());
		registroGeral.setter(PILMSC019E.NCP.toString(), new ArrayList<NCP>());
		registroGeral.setter(PILMSC019E.NF.toString(), new ArrayList<NF>());
		registroGeral.setter(PILMSC019E.OB.toString(), new ArrayList<OB>());
		registroGeral.setter(PILMSC019E.PC.toString(), new ArrayList<PC>());
		registroGeral.setter(PILMSC019E.PR.toString(), new ArrayList<PR>());
		registroGeral.setter(PILMSC019E.TK.toString(), new ArrayList<TK>());
		registroGeral.setter(PILMSC019E.VE.toString(), new ArrayList<VE>());
		return registroGeral;
    }
    
    private IRegistroGeral getNewIRegistroGeralLayoutPICo26e() {
    	IRegistroGeral registroGeral = new com.mercurio.lms.integration.pilmsf26e.RegistroGeral();
		registroGeral.setter(PILMSF26E.ACBAT.toString(), new ArrayList<ACBAT>());
		registroGeral.setter(PILMSF26E.ACCAT.toString(), new ArrayList<ACCAT>());
		registroGeral.setter(PILMSF26E.ACPAT.toString(), new ArrayList<ACPAT>());
		registroGeral.setter(PILMSF26E.ACSAT.toString(), new ArrayList<ACSAT>());
		registroGeral.setter(PILMSF26E.AG1AT.toString(), new ArrayList<AG1AT>());
		registroGeral.setter(PILMSF26E.AW01AT.toString(), new ArrayList<AW01AT>());
		registroGeral.setter(PILMSF26E.AWAT.toString(), new ArrayList<AWAT>());
		registroGeral.setter(PILMSF26E.CEAT.toString(), new ArrayList<CEAT>());
		registroGeral.setter(PILMSF26E.CLAT.toString(), new ArrayList<CLAT>());
		registroGeral.setter(PILMSF26E.CO01AT.toString(), new ArrayList<CO01AT>());
		registroGeral.setter(PILMSF26E.DCAT.toString(), new ArrayList<DCAT>());
		registroGeral.setter(PILMSF26E.EMAT.toString(), new ArrayList<EMAT>());
		registroGeral.setter(PILMSF26E.IDAT.toString(), new ArrayList<IDAT>());
		registroGeral.setter(PILMSF26E.MAAT.toString(), new ArrayList<MAAT>());
		registroGeral.setter(PILMSF26E.MBAT.toString(), new ArrayList<MBAT>());
		registroGeral.setter(PILMSF26E.MEAT.toString(), new ArrayList<MEAT>());
		registroGeral.setter(PILMSF26E.MO00AT.toString(), new ArrayList<MO00AT>());
		registroGeral.setter(PILMSF26E.NCBAT.toString(), new ArrayList<NCBAT>());
		registroGeral.setter(PILMSF26E.NCDAT.toString(), new ArrayList<NCDAT>());
		registroGeral.setter(PILMSF26E.NCNAT.toString(), new ArrayList<NCNAT>());
		registroGeral.setter(PILMSF26E.NCPAT.toString(), new ArrayList<NCPAT>());
		registroGeral.setter(PILMSF26E.NFAT.toString(), new ArrayList<NFAT>());
		registroGeral.setter(PILMSF26E.PCAT.toString(), new ArrayList<PCAT>());
		registroGeral.setter(PILMSF26E.PRAT.toString(), new ArrayList<PRAT>());
		registroGeral.setter(PILMSF26E.RCAT.toString(), new ArrayList<RCAT>());
		registroGeral.setter(PILMSF26E.SCAT.toString(), new ArrayList<SCAT>());
		registroGeral.setter(PILMSF26E.TEAT.toString(), new ArrayList<TEAT>());
		registroGeral.setter(PILMSF26E.TKAT.toString(), new ArrayList<TKAT>());
		registroGeral.setter(PILMSF26E.VCAT.toString(), new ArrayList<VCAT>());
		registroGeral.setter(PILMSF26E.VEAT.toString(), new ArrayList<VEAT>());
		return registroGeral;
    }
    
    private IRegistroGeral getNewIRegistroGeralLayoutPIJ006e() {
    	IRegistroGeral registroGeral = new com.mercurio.lms.integration.pilmsj006e.RegistroGeral();
		registroGeral.setter(PILMSJ006E.JDRP.toString(), new ArrayList<JDRP>());
		return registroGeral;
    }
    	
	public void updateOcorrenciaIntegracao(OcorrenciaIntegracao ocorrencia){
		this.getOcorrenciasIntegracaoDAO().updateOcorrenciaIntegracao(ocorrencia);
	}
	
    private List getLayoutList(OcorrenciaIntegracao ocorrenciaIntegracao, String layout) {
		MessageConverter messageConverter = new MessageConverter();
    	IRegistroGeral registroGeral = (IRegistroGeral) messageConverter.fromMessage(String.valueOf(ocorrenciaIntegracao.getMessage()));
		if(registroGeral.getPojoConfigs().get(layout) != null && registroGeral.getPojoConfigs().get(layout).size() > 0) {
			return registroGeral.getPojoConfigs().get(layout);
		}
		return new LinkedList();
    }
	
	/** 
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setOcorrenciasIntegracaoDAO(OcorrenciaIntegracaoDAO dao) {
        setDao( dao );
    }
	
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos 
     * dados deste serviço.
     * 
     * @return Instância do DAO.
     */
    private OcorrenciaIntegracaoDAO getOcorrenciasIntegracaoDAO() {
        return (OcorrenciaIntegracaoDAO) getDao();
    }
}