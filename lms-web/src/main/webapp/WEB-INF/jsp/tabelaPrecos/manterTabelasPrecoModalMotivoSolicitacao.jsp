<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.manterTabelasPrecoAction">
	<adsm:form action="/tabelaPrecos/manterTabelasPreco" onDataLoadCallBack="myPageLoadb">
		<adsm:lookup property="filial" onchange="return resetFilial();" 
					 idProperty="idFilial" required="true" criteriaProperty="sgFilial" 
					 maxLength="3" service="lms.tabelaprecos.manterTabelasPrecoAction.findLookupFilial" dataType="text" 
					 label="filial" size="3" action="/municipios/manterFiliais" criteriaSerializable="true"
					 labelWidth="10%" width="82%" minLengthForAutoPopUpSearch="3" exactMatch="true" disabled="false">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
		<adsm:textarea 
			property="dsMotivoSolicitacao"
			maxLength="500"
			required="true"
			columns="80"
			rows="9"
			label="motivo" 
			labelStyle="vertical-align:top"
			cellStyle="vertical-align:top"
			labelWidth="10%" 
			width="82%"/>
			
		<adsm:buttonBar>
			<adsm:button 
				buttonType="storeButton"
				caption="salvar" 
				onclick="setMotivoSolicitacao(this.form)"
				disabled="false" />
			<adsm:button id="btnLimpar" caption="limpar" onclick="limpar()" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	checkFilial();
	
	var deveLimparFilial = true;
	
	function checkFilial() {
		var url = new URL(parent.location.href);
		var tpTipoTabelaPreco = url.parameters["tpTipoTabelaPreco"];
		var data = new Array();
		setNestedBeanPropertyValue(data, "tpTipoTabelaPreco", tpTipoTabelaPreco);
		var sdo = createServiceDataObject("lms.tabelaprecos.manterTabelasPrecoAction.checkFilial","checkFilial", data);
	    xmit({serviceDataObjects:[sdo]});
	}
	
	function checkFilial_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		fillFilialData(data);
	}
	
	function fillFilialData(filialData) {
		if (filialData.filial) {
			setElementValue("filial.idFilial", filialData.filial.idFilial);
			setElementValue("filial.sgFilial", filialData.filial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", filialData.filial.pessoa.nmFantasia);
			setDisabled("filial.idFilial", true);
			deveLimparFilial = false;
		}
	}
	
	function setMotivoSolicitacao(form) {
		if (!validateForm(form)) {
			return "false";
		}
		var dsMotivoSolicitacao = getElementValue("dsMotivoSolicitacao");
		var idFilialSolicitacao = getElementValue("filial.idFilial");
		var parentWindow = dialogArguments.window.document;
		setElementValue(parentWindow.getElementById("dsMotivoSolicitacao"), dsMotivoSolicitacao);
		setElementValue(parentWindow.getElementById("idFilialSolicitacao"), idFilialSolicitacao);
		self.close();
	}
	function resetFilial() {
		if (getElementValue("filial.sgFilial") == "" ) {
			setElementValue("filial.pessoa.nmFantasia", "");
			setElementValue("filial.idFilial","");
		}
		return filial_sgFilialOnChangeHandler();
	}
	function limpar() {
		if (deveLimparFilial) {
			newButtonScript();
		} else {
			setElementValue("dsMotivoSolicitacao", '');
		}
		setDisabled("btnLimpar", false);
	}
</script>