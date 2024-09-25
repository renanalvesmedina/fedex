package com.mercurio.lms.rest.recepcaodescarga.exemplo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.municipios.EmpresaDTO;
import com.mercurio.lms.rest.recepcaodescarga.exemplo.dto.TelaComponentesDTO;
import com.mercurio.lms.rest.recepcaodescarga.exemplo.dto.TelaComponentesFilterDTO;
 
@Path("/recepcaodescarga/telaComponentes") 
public class TelaComponentesRest extends LmsBaseCrudReportRest<TelaComponentesDTO, TelaComponentesDTO, TelaComponentesFilterDTO> {
	
	private static enum DTO_ATTRIBUTES {
		DATA("dt_data", "Data"),
		DATA_HORA("dt_data_hora", "Data e Hora");
		//TODO: adicionar demais atributos do DTO
		
		private String reportAttributeKey;
		private String columnHeader;
		
		DTO_ATTRIBUTES(String reportAttributeKey, String columnHeader) {
			this.reportAttributeKey = reportAttributeKey;
			this.columnHeader = columnHeader;
		}
	};
	
	private TelaComponentesFakeService service;
	
	public TelaComponentesRest() {
		super();
		service = TelaComponentesFakeService.getInstance();
	}
	
	@GET
	@Path("getOpcoesChosen")
	public Response getOpcoesChosen() {
		return Response.ok(mockEmpresas()).build();
	}
	
	@POST
	@Path("storeFiles")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@SuppressWarnings("unchecked")
	public Response storeFiles(FormDataMultiPart formDataMultiPart) throws IOException {
		/* Popula o modelo/dto/entidade/map/etc */
		TelaComponentesDTO sampleDto = getModelFromForm(formDataMultiPart, TelaComponentesDTO.class, "dados");

		/* Popula o campo com a quantidade de arquivos. */
		Integer qtdArquivos = getModelFromForm(formDataMultiPart, Integer.class, "qtdArquivos");

		if (qtdArquivos != null) {
			/*
			 * Para cada arquivo que foi submetido teremos um byte[] que é o
			 * padrão atual do sistema para salvar arquivos no banco de dados.
			 * Neste exemplo teremos apenas 1.
			 */
			for (int i = 0; i < qtdArquivos; i++) {
				byte[] byteArrayParaBlob = getBinaryBlobUserTypeFromForm(formDataMultiPart, "arquivo_" + i);
				sampleDto.setDcArquivo(byteArrayParaBlob);
			}
		}

		/*
		 * Em um caso real seguir com o processo normal, que seria algo tipo:
		 * "MeuService.store(sampleDto)".
		 */

		return Response.ok("Ok").build();
    }
	
	@POST
	@Path("findDataForCustomTable")
	public Response findDataForCustomTable() {
		List<EmpresaDTO> empresas = mockEmpresas();
		return getReturnFind(empresas, empresas.size());
	}
	
	@Override 
	protected List<Map<String, String>> getColumns() { 
		List<Map<String, String>> columns = new ArrayList<Map<String,String>>();
		
		for (DTO_ATTRIBUTES dtoAttribute : DTO_ATTRIBUTES.values()) {
			columns.add(getColumn(dtoAttribute.columnHeader, dtoAttribute.reportAttributeKey));
		}
		
		return columns;
	} 
 
	@Override 
	protected List<Map<String, Object>> findDataForReport(TelaComponentesFilterDTO filter) { 
		
		List<Map<String, Object>> reportData = new ArrayList<Map<String,Object>>();
		
		List<TelaComponentesDTO> filteredResults = find(filter);
		for (TelaComponentesDTO telaComponentesDTO : filteredResults) {
			Map<String, Object> dataMapping = new HashMap<String, Object>();

			dataMapping.put(DTO_ATTRIBUTES.DATA.reportAttributeKey, telaComponentesDTO.getData());
			dataMapping.put(DTO_ATTRIBUTES.DATA_HORA.reportAttributeKey, telaComponentesDTO.getDataHora());
			//TODO: adicionar os demais atributos do DTO
			
			reportData.add(dataMapping);
		}
		
		Collections.sort(reportData, new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				YearMonthDay y1 = (YearMonthDay) o1.get(DTO_ATTRIBUTES.DATA.reportAttributeKey);
				YearMonthDay y2 = (YearMonthDay) o2.get(DTO_ATTRIBUTES.DATA.reportAttributeKey);
				
				int comparison = y1.compareTo(y2);
				
				if (comparison != 0) {
					return comparison;
				}
				
				DateTime dt1 = (DateTime) o1.get(DTO_ATTRIBUTES.DATA_HORA.reportAttributeKey);
				DateTime dt2 = (DateTime) o2.get(DTO_ATTRIBUTES.DATA_HORA.reportAttributeKey);
				
				return dt1.compareTo(dt2);
			}
		});
		return reportData; 
	} 
 
	@Override 
	protected TelaComponentesDTO findById(Long id) { 
		return service.findById(id); 
	} 
 
	@Override 
	protected Long store(TelaComponentesDTO bean) { 
		return service.store(bean); 
	} 
 
	@Override 
	protected void removeById(Long id) { 
		service.removeById(id); 
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
		service.removeByIds(ids);
	} 
 
	@Override 
	protected List<TelaComponentesDTO> find(TelaComponentesFilterDTO filter) { 
		return service.find(filter);
	} 
 
	@Override 
	protected Integer count(TelaComponentesFilterDTO filter) { 
		return service.count(filter);
	}
	
	// Sobrescrevendo o getLabel (invocado pelo getColumn) porque o Rest em questão é apenas para fins de exemplo. 
	@Override 
	protected String getLabel(String chave) {
		return chave;
	}
	
	private List<EmpresaDTO> mockEmpresas() {
		List<EmpresaDTO> empresas = new ArrayList<EmpresaDTO>();
		
		EmpresaDTO empresa1 = new EmpresaDTO();
		empresa1.setIdEmpresa(1l);
		empresa1.setNmFantasia("Development Factory");
		empresa1.setInfo("Desenvolvimento de software.");
		empresa1.setNmPessoa("Development Factory - LTDA");
		empresa1.setNmMunicipio("Novo Hamburgo");
		empresa1.setSgUnidadeFederativa("RS");
		empresas.add(empresa1);
		
		EmpresaDTO empresa2 = new EmpresaDTO();
		empresa2.setIdEmpresa(2l);
		empresa2.setNmFantasia("Tests & Q.A.");
		empresa2.setInfo("Acompanhamento sistemático e avaliação dos diferentes aspectos de um projeto, serviço ou facilidade para garantir que os padrões de qualidade estão sendo cumpridos");
		empresa2.setNmPessoa("Tests & Q.A. - ME");
		empresa2.setNmMunicipio("Porto Alegre");
		empresa2.setSgUnidadeFederativa("RS");
		empresas.add(empresa2);
		
		EmpresaDTO empresa3 = new EmpresaDTO();
		empresa3.setIdEmpresa(3l);
		empresa3.setNmFantasia("Business Intelligence");
		empresa3.setInfo("Refere-se ao processo de coleta, organização, análise, compartilhamento e monitoramento de informações que oferecem suporte a gestão de negócios.");
		empresa3.setNmPessoa("Business Intelligence S.A.");
		empresa3.setNmMunicipio("Porto Alegre");
		empresa3.setSgUnidadeFederativa("RS");
		empresas.add(empresa3);
		
		EmpresaDTO empresa4 = new EmpresaDTO();
		empresa4.setIdEmpresa(4l);
		empresa4.setNmFantasia("Information Technology Infrastructure");
		empresa4.setInfo("Serviços aplicados na infraestrutura, operação e manutenção de serviços de tecnologia da informação");
		empresa4.setNmPessoa("Information Technology Infrastructure - EPP");
		empresa4.setNmMunicipio("Florianópolis");
		empresa4.setSgUnidadeFederativa("SC");
		empresas.add(empresa4);
		
		EmpresaDTO empresa5 = new EmpresaDTO();
		empresa5.setIdEmpresa(5l);
		empresa5.setNmFantasia("Systems analysis");
		empresa5.setInfo("Estudos de processos para a produção de software.");
		empresa5.setNmPessoa("Systems analysis - S.A.");
		empresa5.setNmMunicipio("São Leopoldo");
		empresa5.setSgUnidadeFederativa("RS");
		empresas.add(empresa5);
		
		return empresas;
	}
 
} 
