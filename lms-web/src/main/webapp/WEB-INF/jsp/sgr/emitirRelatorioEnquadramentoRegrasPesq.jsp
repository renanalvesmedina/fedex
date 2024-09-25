<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.sgr.emitirRelatorioEnquadramentoRegrasAction">

	<adsm:form action="/sgr/emitirRelatorioEnquadramentoRegras">
		
		<adsm:hidden property="idEnquadramentoRegra" />
		<adsm:combobox label="enquadramento" property="enquadramentoRegra" 
					   service="lms.sgr.emitirRelatorioEnquadramentoRegrasAction.findEnquadramentoRegra" 
					   optionProperty="idEnquadramentoRegra"
					   optionLabelProperty="dsEnquadramentoRegra" 
					   onlyActiveValues="true" 
					   required="true" 
					   labelWidth="18%" width="82%"/>
		
		<adsm:combobox property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" renderOptions="true"
		               defaultValue="pdf"
		               label="formatoRelatorio" labelWidth="18%" width="82%" required="true"/>		
		
		<adsm:buttonBar>
			<adsm:button buttonType="reportViewerButton" id="btnVisualizar" caption="visualizar" onclick="imprimeRelatorio()"  />
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>

<script type="text/javascript">
		
	/**
	 * Função que imprime o relatório
	 */
	function imprimeRelatorio() {		   
	    setElementValue("idEnquadramentoRegra", getElementValue("enquadramentoRegra"));
	    reportButtonScript('lms.sgr.emitirRelatorioEnquadramentoRegrasAction', 'openPdf', document.forms[0]);
	}	
	
</script>