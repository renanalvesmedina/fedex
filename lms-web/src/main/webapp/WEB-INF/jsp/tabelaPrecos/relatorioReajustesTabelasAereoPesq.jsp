<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.relatorioReajustesTabelasAereoAction">
	<adsm:form action="/tabelaPrecos/relatorioReajustesTabelasAereo">
		<adsm:hidden property="ciaAerea.empresa.nmPessoa"/>
		<adsm:combobox label="ciaAerea" 
					   property="empresa.idEmpresa" 
					   optionProperty="idEmpresa" 
					   service="lms.municipios.empresaService.findCiaAerea" 
	    			   optionLabelProperty="pessoa.nmPessoa"
	    			   labelWidth="15%" width="33%" boxWidth="220">
			<adsm:propertyMapping relatedProperty="ciaAerea.empresa.nmPessoa"
								  modelProperty="pessoa.nmPessoa"/>
		</adsm:combobox>
		
		<adsm:range label="periodo" labelWidth="10%" width="30%">
			<adsm:textbox dataType="JTDate" property="dtInicialEmissaoAWB" />
			<adsm:textbox dataType="JTDate" property="dtFinalEmissaoAWB" />
		</adsm:range>
		
		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   required="true"
    				   labelWidth="15%"
    				   width="80%"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.tabelaprecos.relatorioReajustesTabelasAereoAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>