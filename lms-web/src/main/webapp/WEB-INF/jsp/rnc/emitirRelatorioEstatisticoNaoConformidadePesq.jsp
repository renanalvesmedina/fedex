<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function loadButton() {
		setDisabled('visualizar', false);
		onPageLoad();
	}
</script>

<adsm:window onPageLoad="loadButton" service="lms.rnc.emitirRelatorioEstatisticoNaoConformidadeAction">
	<adsm:form action="/rnc/emitirRelatorioEstatisticoNaoConformidade" >
	
		<adsm:range label="periodo" labelWidth="20%" width="80%" required="true">
			<adsm:textbox dataType="JTDate" property="dataInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dataFinal" picker="true"/>
		</adsm:range>
		
		<adsm:lookup dataType="text"  property="rota" idProperty="idRota" criteriaProperty="dsRota" criteriaSerializable="true"
					 service="lms.rnc.emitirRelatorioEstatisticoNaoConformidadeAction.findLookupRota" action="/municipios/manterPostosPassagemRotasViagem" cmd="list" 
					 label="rotaViagem" labelWidth="20%" width="80%" maxLength="60" size="30"/>
		 	
		<adsm:lookup label="rotaColetaEntrega" property="rotaColetaEntrega" 
			 		 idProperty="idRotaColetaEntrega" 
			 		 criteriaProperty="nrRota"
					 action="/municipios/manterRotaColetaEntrega" serializable="true"
					 service="lms.rnc.emitirRelatorioEstatisticoNaoConformidadeAction.findLookupRotaColetaEntrega" 
					 dataType="integer" size="3" maxLength="3" labelWidth="20%" width="80%"   >
			<adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota" />
			<adsm:textbox property="rotaColetaEntrega.dsRota" dataType="text" size="30" disabled="true" serializable="true" />
		</adsm:lookup>		 	
		
		<adsm:lookup dataType="text" property="filialAbertura" idProperty="idFilial" criteriaProperty="sgFilial" 
					service="lms.rnc.emitirRelatorioEstatisticoNaoConformidadeAction.findLookupFilial" action="/municipios/manterFiliais" 
					label="filialAbertura" size="3" maxLength="3" labelWidth="20%" width="80%">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialAbertura.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialAbertura.pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="filialResponsavel" idProperty="idFilial" criteriaProperty="sgFilial" 
					service="lms.rnc.emitirRelatorioEstatisticoNaoConformidadeAction.findLookupFilial" action="/municipios/manterFiliais" 
					label="filialResponsavel" size="3" maxLength="3" labelWidth="20%" width="80%">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialResponsavel.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialResponsavel.pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="ocorrenciaNaoConformidade.motivoAberturaNc" 
					   label="motivoAbertura"  optionLabelProperty="dsMotivoAbertura" optionProperty="idMotivoAberturaNc" 
					   labelWidth="20%" width="80%" service="lms.rnc.emitirRelatorioEstatisticoNaoConformidadeAction.findOrderByDsMotivoAbertura" onchange="setMotivoAberturaNCValue()"/>
					   
		<adsm:hidden property="motivoAberturaNc.dsMotivoAbertura"/>
		
		<adsm:combobox property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" renderOptions="true"
		               defaultValue="pdf"
		               label="formatoRelatorio" labelWidth="20%" width="80%" required="true"/>
		
		<adsm:buttonBar>
			<adsm:button caption="visualizar" id="visualizar" onclick="validateFilial()" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
		<script>
			function lms_12005() {
				alert("<adsm:label key='LMS-12005'/>");
			}
		</script>
		
	</adsm:form>
</adsm:window>

<script>	

	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			document.getElementById("visualizar").disabled = false;
		}
	}
		
	function validateFilial() {
		if (!validateForm(this.document.forms[0])) {
			return false;
		}
		if ((getElementValue("rota.idRota")!="") && (getElementValue("rotaColetaEntrega.idRotaColetaEntrega")!="")) {
			lms_12005();
		} else  {
			executeReportWithCallback('lms.rnc.emitirRelatorioEstatisticoNaoConformidadeAction', 'printReport', this.document.forms[0]);
		}
		setFocusOnFirstFocusableField();
	}
	
	/**
	 * Captura o retorno da validacao do form
	 *
	 * @param data
	 * @param error
	 */
	function printReport_cb(data, error) {
		if (data._exception!=undefined) {
			if (data._exception._key=="LMS-12006") {
				alert(error);
				setFocus(document.getElementById("filialAbertura.sgFilial"));	
			} else {
				alert(error);
			}
		} else {
			openReportWithLocator(data, error, null, false);		
		}
	}
	
	function setMotivoAberturaNCValue() {
		//Busca o valor de uma combo
		var selectedIndex = document.getElementById('ocorrenciaNaoConformidade.motivoAberturaNc').selectedIndex; 
		var texto = document.getElementById('ocorrenciaNaoConformidade.motivoAberturaNc').options[selectedIndex].text;
		
		setElementValue("motivoAberturaNc.dsMotivoAbertura", texto);
	}
	

</script>