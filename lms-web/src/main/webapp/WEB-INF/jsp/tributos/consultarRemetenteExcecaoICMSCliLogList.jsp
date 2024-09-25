<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.consultarRemetenteExcecaoICMSCliLogAction">
	<adsm:form action="/tributos/consultarRemetenteExcecaoICMSCliLog" >
		
		<adsm:combobox  property="tipoCnpj" 
						domain="DM_TIPO_CNPJ"
						labelWidth="20%" 
						defaultValue="P"
						onchange="onChangeComboTipoCnpj(this);"
						width="10%" 
						label="nrCNPJParcialRem">
		</adsm:combobox>
		<adsm:textbox   property="nrCNPJParcialRem" 
						maxLength="8"
						size="14"
						mask="00000000"
						dataType="integer"
						width="20%">
		</adsm:textbox>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog" onclick="findButtonClick();"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idRemetenteExcecaoICMSCliLog"
			service="lms.tributos.consultarRemetenteExcecaoICMSCliLogAction.findPaginatedTela"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="remetenteExcecaoIcmsCli.idRemetenteExcecaoIcmsCli" title="remetenteExcecaoIcmsCli.idRemetenteExcecaoIcmsCli" dataType="integer"/>
		<adsm:gridColumn property="excecaoIcmsCliente.idExcecaoIcmsCliente" title="excecaoIcmsCliente.idExcecaoIcmsCliente" dataType="integer"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="nrCnpjParcialRem" title="nrCnpjParcialRem"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>
<script>

	/**
	  * Chamado ao clicar em consultar.
	  */
	function findButtonClick() {
		// Caso o cnpj seja informado, deve-se infirmar tmb o tipo de cnpj.
		if (getElementValue("nrCNPJParcialRem") != '') {
			getElement("tipoCnpj").required = true;
		} else {
			getElement("tipoCnpj").required = false;
		}
		findButtonScript('remetenteExcecaoICMSCli', document.forms[0]);
	}
	
	/**
	  * Chamado no onChange da combo de tipo de cnpj.
	  * Ao selecionar um tipo de cnpj (parcial | completo) deve alterar a mascara.
	  */
	function onChangeComboTipoCnpj(elem){
		comboboxChange({e:elem});
		setMaskCnpj(elem.options[elem.selectedIndex].value);
		setElementValue("nrCNPJParcialRem", "");
	}
	
	/**
	  * Modifica a mascara do cnpj de acordo com o tipo de cnpj.
	  */ 
	function setMaskCnpj(tpCnpj) {
		if (tpCnpj == "P") {
			getElement("nrCNPJParcialRem").mask = "00000000";
			getElement("nrCNPJParcialRem").maxLength = 8;
		} else {
			getElement("nrCNPJParcialRem").mask = "00000000000000";
			getElement("nrCNPJParcialRem").maxLength = 14;
		}
	}
	
	function initWindow(event) {
		if (event.name == "cleanButton_click") {
			setMaskCnpj("P");
		}
	}
	
</script>