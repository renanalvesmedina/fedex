<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirUtilizacaoDocasBoxes" service="lms.portaria.emitirUtilizacaoDocasBoxesAction">
	<adsm:form action="/portaria/emitirUtilizacaoDocasBoxes">
		<adsm:hidden property="filial.empresa.tpEmpresa" value="M"/>
		<adsm:lookup service="lms.portaria.emitirUtilizacaoDocasBoxesAction.findLookupFilial" dataType="text"
					 property="filial" labelWidth="17%" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
					 width="83%" action="/municipios/manterFiliais" idProperty="idFilial" required="true"
					 criteriaSerializable="true" >
        	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
        	<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="filial.empresa.tpEmpresa"/>
        	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="40" disabled="true" serializable="true"/>
        </adsm:lookup>
        <adsm:combobox property="terminal.id" optionLabelProperty="pessoa.nmPessoa" optionProperty="idTerminal"  service="lms.portaria.emitirUtilizacaoDocasBoxesAction.findComboTerminal" label="terminal" labelWidth="17%" width="68%" boxWidth="200">
        	<adsm:propertyMapping relatedProperty="terminal.ds" modelProperty="pessoa.nmPessoa"/>
        	<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
        </adsm:combobox>
		<adsm:combobox property="doca.id" optionLabelProperty="dsDoca" optionProperty="idDoca" service="lms.portaria.emitirUtilizacaoDocasBoxesAction.findComboDoca" label="doca" labelWidth="17%" width="68%" boxWidth="200">
			<adsm:propertyMapping relatedProperty="doca.ds" modelProperty="dsDoca"/>
			<adsm:propertyMapping criteriaProperty="terminal.id" modelProperty="terminal.idTerminal"/>
		</adsm:combobox>
		<adsm:combobox property="box.id" optionLabelProperty="dsBox" optionProperty="idBox" service="lms.portaria.emitirUtilizacaoDocasBoxesAction.findComboBox" label="box" labelWidth="17%" width="68%" boxWidth="200">
			<adsm:propertyMapping relatedProperty="box.ds" modelProperty="dsBox"/>
			<adsm:propertyMapping criteriaProperty="doca.id" modelProperty="doca.idDoca"/>
		</adsm:combobox>
		<adsm:hidden property="box.ds"/>
		<adsm:hidden property="doca.ds"/>
		<adsm:hidden property="terminal.ds"/>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="17%" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
        <adsm:buttonBar>
        	<adsm:reportViewerButton service="lms.portaria.emitirUtilizacaoDocasBoxesAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
	var idFilialLogado;
	var sgFilialLogado;
	var nmFilialLogado; 
	
	function findFilialCallBack_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
			sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
			nmFilialLogado = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
			setValues();
		}
	}

	var sdo = createServiceDataObject("lms.portaria.manterTerminaisAction.findFilialUsuarioLogado","findFilialCallBack",null);
	xmit({serviceDataObjects:[sdo]});
	
	function setValues() {
		if (idFilialLogado != undefined &&
			sgFilialLogado != undefined &&
			nmFilialLogado != undefined) {
			setElementValue("filial.idFilial",idFilialLogado);
			setElementValue("filial.sgFilial",sgFilialLogado);
			setElementValue("filial.pessoa.nmFantasia",nmFilialLogado);
			notifyElementListeners({e:document.getElementById("filial.idFilial")});
		}
		
	}
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click")
			setValues();
	}
</script>