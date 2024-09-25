<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterTipoPagamentoPostoAction">
	<adsm:form action="/municipios/manterTipoPagamentoPosto" idProperty="idTipoPagamentoPosto" onDataLoadCallBack="pageLoad">
		<adsm:hidden property="postoPassagem.idPostoPassagem"/>
		<adsm:textbox dataType="text" property="tpPosto" label="tipo" maxLength="10" size="35" disabled="true" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="text" property="localizacao" label="localizacaoMunicipio" maxLength="10" size="35" disabled="true" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="text" property="rodovia" label="rodovia" maxLength="10" size="35" disabled="true" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="text" property="sentido" label="sentidoCobranca" maxLength="10" size="35" disabled="true" labelWidth="18%" width="32%" />
		<adsm:hidden property="pais.idPais" serializable="false"/>
		
		<adsm:combobox service="lms.municipios.tipoPagamPostoPassagemService.find" optionLabelProperty="dsTipoPagamPostoPassagem" optionProperty="idTipoPagamPostoPassagem" onlyActiveValues="true"  property="tipoPagamPostoPassagem.idTipoPagamPostoPassagem" label="tipoPagamentoAceito" boxWidth="198" labelWidth="18%" width="32%" required="true"/>
		<adsm:textbox dataType="integer" property="nrPrioridadeUso" label="prioridadeUso" maxLength="2" size="3" labelWidth="18%" width="32%" required="true"/>
		<adsm:range label="vigencia" labelWidth="18%" width="82%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:button id="__buttonBar:0_1" caption="tarifasPostosPassagem" action="/municipios/manterTarifaPostoPassagem" cmd="main">
				<adsm:linkProperty src="postoPassagem.idPostoPassagem" target="postoPassagem.idPostoPassagem"/>
				<adsm:linkProperty src="tpPosto" target="tpPosto"/>
				<adsm:linkProperty src="localizacao" target="localizacao"/>
				<adsm:linkProperty src="rodovia" target="rodovia"/>
				<adsm:linkProperty src="sentido" target="sentido"/>
				<adsm:linkProperty src="pais.idPais" target="pais.idPais"/>
			</adsm:button>
			<adsm:storeButton id="__buttonBar:0.storeButton"
				service="lms.municipios.tipoPagamentoPostoService.storeMap" callbackProperty="afterStore" />
			<adsm:newButton id="__buttonBar:0.newButton"/>
			<adsm:removeButton id="__buttonBar:0.removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<Script>
<!--
	function pageLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		acaoVigencia(data);		
		setDisabled("__buttonBar:0_1",false);
	}
	
	function acaoVigencia(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			  enabledFields();
			  setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 1) {
		      setDisabled(document,true);
		      setDisabled("dtVigenciaFinal",false);
		      setDisabled("__buttonBar:0.storeButton",false);
		      setDisabled("__buttonBar:0.newButton",false);
		      setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 2) {
		   	 setDisabled(document,true);
		     setDisabled("__buttonBar:0.newButton",false);
		     setFocusOnNewButton();
		}
		setDisabled("__buttonBar:0_1",false);
	}
	function enabledFields() {
		setDisabled("tipoPagamPostoPassagem.idTipoPagamPostoPassagem",false);
		setDisabled("nrPrioridadeUso",false);
		setDisabled("dtVigenciaInicial",false);
		setDisabled("dtVigenciaFinal",false);
		setDisabled("__buttonBar:0_1",false);
		setFocusOnFirstFocusableField();
	}
	
	function initWindow(eventObj) {
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click"))
			enabledFields();
	}
	
	function afterStore_cb(data,exception,key) {
		store_cb(data,exception,key);
		if (exception == undefined) {
			acaoVigencia(data);
			setFocusOnNewButton();
		}
	}
//-->
</Script>




