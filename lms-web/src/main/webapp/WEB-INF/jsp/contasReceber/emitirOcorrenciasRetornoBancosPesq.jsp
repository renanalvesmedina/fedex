<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window>
	<adsm:form action="/contasReceber/emitirOcorrenciasRetornoBancos">
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true"/>
		<adsm:hidden property="dsCedente" />
		<adsm:hidden property="dsOcorrenciaBanco" />
		<!-- Lookup de filial -->
		<adsm:lookup
			label="filialCobranca"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterFiliais" 
			service="lms.contasreceber.emitirOcorrenciasRetornoBancosAction.findLookupFilial"
			dataType="text"
			labelWidth="18%"
			width="32%"
			size="3" 
			maxLength="3"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3"
			criteriaSerializable="true"
			required="true">
			
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			
			<adsm:textbox 
				property="filial.pessoa.nmFantasia" 
				dataType="text" 
				serializable="true" 
				size="30" 
				disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox 
			service="lms.contasreceber.emitirOcorrenciasRetornoBancosAction.findComboCedentes" 
			optionLabelProperty="comboText" 
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			label="banco"
			labelWidth="18%"
			width="32%" 
			boxWidth="150"
			onchange="return changeCedentes(this);">
			<adsm:propertyMapping relatedProperty="dsCedente" modelProperty="comboText"/>
		</adsm:combobox>
			
		<adsm:range label="periodo" labelWidth="18%" width="32%" required="true">
			<adsm:textbox property="dhOcorrenciaInicial" dataType="JTDate"/>
			<adsm:textbox property="dhOcorrenciaFinal" dataType="JTDate"/>
		</adsm:range>
		
		<adsm:combobox 
			service="lms.contasreceber.emitirOcorrenciasRetornoBancosAction.findComboOcorrenciasBanco" 
			optionLabelProperty="dsOcorrenciaBanco" 
			optionProperty="idOcorrenciaBanco" 
			property="ocorrenciaBanco.idOcorrenciaBanco" 
			label="ocorrencia"
			labelWidth="18%"
			width="32%" 
			disabled="true" 
			autoLoad="false"
			boxWidth="230" 
			onDataLoadCallBack="ocorrenciaBanco">
			<adsm:propertyMapping relatedProperty="dsOcorrenciaBanco" modelProperty="dsOcorrenciaBanco"/>
		</adsm:combobox>
			
		<adsm:combobox 
			property="tpFormatoRelatorio"
			labelWidth="18%"
			width="32%" 
			label="formatoRelatorio"
			domain="DM_FORMATO_RELATORIO" 
			serializable="true" 
			required="true" 
			defaultValue="pdf"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirOcorrenciasRetornoBancosAction"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
	function changeCedentes(combo) {
		comboboxChange({e:combo});
		if (getElementValue("cedente.idCedente") != "") {
			var data = {idCedente : getElementValue("cedente.idCedente")};
			var sdo = createServiceDataObject("lms.contasreceber.emitirOcorrenciasRetornoBancosAction.findComboOcorrenciasBanco", "ocorrenciaBanco", data);
			xmit({serviceDataObjects:[sdo]});
		} else {
			resetOcorrencias();
		}
		return true;
	}
	
	function ocorrenciaBanco_cb(data) {
		ocorrenciaBanco_idOcorrenciaBanco_cb(data);
		setDisabled("ocorrenciaBanco.idOcorrenciaBanco", false);
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			resetOcorrencias();
			findDadosSessao();
		} else if (eventObj.name == "tab_load") {
			findDadosSessao();
		}
	}
	
	function resetOcorrencias() {
		resetValue("ocorrenciaBanco.idOcorrenciaBanco");
		setDisabled("ocorrenciaBanco.idOcorrenciaBanco", true);
	}
	
	function findDadosSessao() {
		var service = "lms.contasreceber.emitirOcorrenciasRetornoBancosAction.findDadosSessao";
		var sdo = createServiceDataObject(service, "findDadosSessao");
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findDadosSessao_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		setElementValue("filial.idFilial", data.filial.idFilial);
		setElementValue("filial.sgFilial", data.filial.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);
	}
//-->
</script>
