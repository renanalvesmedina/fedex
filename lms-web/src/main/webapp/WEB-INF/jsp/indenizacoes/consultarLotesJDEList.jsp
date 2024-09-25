<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.indenizacoes.ConsultarLotesJDEAction">
	<adsm:form action="/indenizacoes/consultarLotesJDE"  height="90" idProperty="idLoteJdeRim">
	
		<adsm:textbox dataType="integer" property="nrLoteJdeRim" width="34%" maxLength="10" disabled="false" labelWidth="6%" label="lote" />
	
		<adsm:range label="periodo" width="34%" labelWidth="16%" required="false">
			<adsm:textbox dataType="JTDate" property="dtInicial" required="false"/>
			<adsm:textbox dataType="JTDate" property="dtFinal"   required="false"/>
		</adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="loteJdeRim"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="loteJdeRim" 
				selectionMode="radio"
				idProperty="idLoteJdeRim" 
				service="lms.indenizacoes.consultarLotesJDEAction.findPaginated"
				rowCountService="lms.indenizacoes.consultarLotesJDEAction.getRowCount"
				unique="true" 
				onRowClick="nilfunction"
				onSelectRow="selectRow"
				gridHeight="200">
		<adsm:gridColumn property="nrLoteJdeRim"                   	title="lote"		    dataType="integer" 			width="100" align="center"/>
		<adsm:gridColumn property="dhLoteJdeRim"               		title="dhLoteJdeRim" 	dataType="JTDateTimeZone"   width="100"/>
		<adsm:gridColumn property="vlIndenizacao"              		title="valorReais" 		dataType="currency"   		width="100"/>
		<adsm:buttonBar>
			<adsm:button buttonType="reportViewerButton" id="btnVisualizar" caption="visualizar" onclick="imprimeRelatorio()" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
/**
 * Emiti o relatorio caso seja possivel.
 */
	var mapa = null;
	
	function nilfunction(){
		return false;
	}
	 
 	function selectRow(tr){
 		mapa = {};
 		mapa["nrLoteJdeRim"] = tr.children[1].children[0].innerHTML.replace(tr.children[1].children[0].children[0].outerHTML, "");
 	 }
 
	function imprimeRelatorio() {
		if(mapa) {
			var form = document.forms[0];
			var tab = getTab(form.document);
			
			// apenas executa a validação se achar uma tab na tela.
			if (tab != null) {
				var valid = tab.validate({name:"reportButton_click"});
			
				// apenas prossegue se a validação dos dados foi realizada com sucesso.
				if (valid == false) {
					return false;
				}
			}

			_serviceDataObjects = new Array(); 
			var data = buildFormBeanFromForm(form);
				data.nrLoteJdeRim = mapa["nrLoteJdeRim"];
			var sdo = createServiceDataObject("lms.indenizacoes.consultarLotesJDEAction.execute", "imprimeRelatorio", data); 
			addServiceDataObject(sdo); 
			xmit(false);
		}
	}

	/**
	 * Retorno da geração de SMP e inicio do processo de gerar o relatório.
	 */
	function imprimeRelatorio_cb(strFile, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		
		openReportWithLocator(strFile, null, "RELATORIO_RECIBO_INDENIZACAO", true);
	}
	
</script>


