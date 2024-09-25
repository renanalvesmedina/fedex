<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterImpressorasAction">
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
			onchange="return filialChange();">
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" disable="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresaPadrao" modelProperty="empresa.tpEmpresaPadrao" inlineQuery="false"/>
         	<adsm:propertyMapping relatedProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa.value"/>
		 	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="22" maxLength="50" disabled="true" width="62%" serializable="false" />

		<adsm:textbox dataType="text" label="descricao" property="dsCheckIn" size="50" maxLength="50" labelWidth="16%" width="84%" />	
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="impressora"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idImpressora" property="impressora" defaultOrder="filial_.sgFilial,dsCheckIn" gridHeight="170" unique="true" rows="13">
		<adsm:gridColumn title="filial" property="filial.sgFilial" width="10%"/>
		<adsm:gridColumn title="descricao" property="dsCheckIn" width="30%"/>
		<adsm:gridColumn title="localizacao" property="dsLocalizacao" width="20%"/>
		<adsm:gridColumn title="tipoImpressora" property="tpImpressora" isDomain="true"/>
		<adsm:gridColumn title="endereco" property="dsEnderecoFisico" width="15%"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	function initWindow(eventObj) {
		defaultParameters();
	}

	/* Dados Default da Tela. */
	function defaultParameters() {
		//setDisabled("resetButton", false);
		setElementValue("filial.empresa.tpEmpresa", getElementValue("empresa.tpEmpresaPadrao"));
		// Executa o metodo da service que retorna os dados da filial associado ao usuario
		var sdo = createServiceDataObject("lms.expedicao.manterImpressorasAction.filialFindByUser","filialUsuario", {});
		xmit({serviceDataObjects:[sdo]});
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
			var sgFilial = getElementValue("filial.sgFilial");
			if(sgFilial == "") {
				setElementValue("filial.empresa.tpEmpresa", getElementValue("empresa.tpEmpresaPadrao"));
			}
		}
		return retorno;
	}

</script>