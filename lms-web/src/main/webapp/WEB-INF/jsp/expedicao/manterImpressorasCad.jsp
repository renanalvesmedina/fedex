<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.expedicao.manterImpressorasAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04021"/>
	</adsm:i18nLabels>
	<adsm:form action="/expedicao/manterImpressoras" idProperty="idImpressora">
		<adsm:hidden property="empresa.tpEmpresaPadrao" value="M" serializable="false"/>
		<adsm:hidden property="filial.empresa.tpEmpresa" serializable="false"/>
		<adsm:lookup
			service="lms.expedicao.manterImpressorasAction.filialFindLookup"
			dataType="text"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filial"
			size="3"
			maxLength="3"
			action="/municipios/manterFiliais"
			labelWidth="16%"
			width="8%"
			exactMatch="true"
			onchange="return filialChange();"
			required="true">
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" disable="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresaPadrao" modelProperty="empresa.tpEmpresaPadrao" inlineQuery="false"/>
         	<adsm:propertyMapping relatedProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa.value"/>
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>

         	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="22" maxLength="50" disabled="true" width="62%" serializable="false"/>
	    </adsm:lookup>
		<adsm:textbox dataType="text" label="descricao" property="dsCheckIn" size="60" maxLength="60" required="true" labelWidth="16%" width="84%"/>
		<adsm:textbox dataType="text" label="localizacao" property="dsLocalizacao" size="60" maxLength="60" required="true" labelWidth="16%" width="84%"/>
		<adsm:combobox property="tpImpressora" domain="DM_TIPO_IMPRESSORA" label="tipoImpressora" labelWidth="16%" width="34%"/>
		<adsm:textbox dataType="text" label="modelo" property="dsModelo" size="20" maxLength="60"/>
		<adsm:textbox dataType="text" label="fabricante" property="dsFabricante" size="20" maxLength="60" labelWidth="16%" width="34%"/>
		<adsm:textbox dataType="text" label="endereco" property="dsEnderecoFisico" size="20" maxLength="17" onchange="return validateEnderecoFisico();"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	function isInsert() {
		return (getElementValue("idImpressora") == "");
	}

	function initWindow(eventObj) {
		defaultParameters();
	}

	/* Dados Default da Tela. */
	function defaultParameters() {
		if(isInsert()) {
			setElementValue("filial.empresa.tpEmpresa", getElementValue("empresa.tpEmpresaPadrao"));
			// Executa o metodo da service que retorna os dados da filial associado ao usuario
			var sdo = createServiceDataObject("lms.expedicao.manterImpressorasAction.filialFindByUser", "filialUsuario", {});
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function filialUsuario_cb(data,exception) {
		if (exception != undefined) {
			alert(exception);
			return false;
		}
		if (data.idFilial != undefined){
			setElementValue("filial.idFilial", data.idFilial)
			setElementValue("filial.sgFilial", data.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data.nmFilial);
			setElementValue("filial.empresa.tpEmpresa", data.empresa.tpEmpresa);
			setFocusOnFirstFocusableField();
			return true;
		}	
		return true;
	}

	function filialChange() {
		var retorno = filial_sgFilialOnChangeHandler();
		if(retorno == true) {
			var sgFilial = getElementValue("filial.idFilial");
			if(sgFilial == "") {
				setElementValue("filial.empresa.tpEmpresa", getElementValue("empresa.tpEmpresaPadrao"));
			}
		}
		return retorno;
	}

	function validateTab() {
		return validateTabScript(document.forms) && validateEnderecoFisico();
	}

	function validateEnderecoFisico() {
		var field = getElement("dsEnderecoFisico")
		var dsEnderecoFisico = getElementValue(field);
		if(dsEnderecoFisico != "") {
			var reEnderecoFisico = /^[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}-[0-9a-fA-F]{2}$/;
			if(!reEnderecoFisico.test(dsEnderecoFisico)) {
				alertI18nMessage("LMS-04021", field.label, false);
				return false;
			}
			setElementValue(field, dsEnderecoFisico.toUpperCase());
		}
		return true;
	}
</script>