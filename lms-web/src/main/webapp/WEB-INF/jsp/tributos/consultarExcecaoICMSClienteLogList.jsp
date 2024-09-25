<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.consultarExcecaoICMSClienteLogAction">
	<adsm:form action="/tributos/consultarExcecaoICMSClienteLog" >
		
		<adsm:hidden property="excecaoICMSCliente.idExcecaoICMSCliente"/>
		
		<adsm:lookup 
  	    			property="unidadeFederativa"
				    idProperty="idUnidadeFederativa" 
				    criteriaProperty="sgUnidadeFederativa"
					service="lms.municipios.unidadeFederativaService.findLookup" 
					dataType="text" 
					labelWidth="20%" 
					maxLength="3"
					width="7%" 
					label="uf" 
					size="3"
					action="/municipios/manterUnidadesFederativas" 
					minLengthForAutoPopUpSearch="2" 
					exactMatch="true">
					 
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.siglaDescricao" modelProperty="siglaDescricao" />
			
		</adsm:lookup>
		
		<adsm:hidden
					property="unidadeFederativa.siglaDescricao" 
					serializable="false"/>			
		
		<adsm:textbox
					dataType="text" 
					disabled="true"
					property="unidadeFederativa.nmUnidadeFederativa" 
					serializable="false"
					width="23%" 
					size="20" />
					
		<adsm:combobox  property="tipoCnpj" 
						domain="DM_TIPO_CNPJ"
						labelWidth="20%" 
						defaultValue="P"
						onchange="onChangeComboTipoCnpj(this);"
						width="10%" 
						label="nrCNPJParcialDev">
		</adsm:combobox>
		<adsm:textbox   property="nrCNPJParcialDev" 
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
			idProperty="idExcecaoICMSClienteLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="excecaoIcmsCliente.idExcecaoIcmsCliente" title="excecaoIcmsCliente.idExcecaoIcmsCliente" dataType="integer"/>
		<adsm:gridColumn property="tipoTributacaoIcms.idTipoTributacaoIcms" title="tipoTributacaoIcms.idTipoTributacaoIcms" dataType="integer"/>
		<adsm:gridColumn property="unidadeFederativa.idUnidadeFederativa" title="unidadeFederativa.idUnidadeFederativa" dataType="integer"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="tpFrete" title="tpFrete" isDomain="true"/>
		<adsm:gridColumn property="blSubcontratacao" title="blSubcontratacao" renderMode="image-check"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="blObrigaCtrcSubcontratante" title="blObrigaCtrcSubcontratante" renderMode="image-check"/>
		<adsm:gridColumn property="nrCnpjParcialDev" title="nrCnpjParcialDev"/>
		<adsm:gridColumn property="dsRegimeEspecial" title="dsRegimeEspecial"/>
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
		if (getElementValue("nrCNPJParcialDev") != '') {
			getElement("tipoCnpj").required = true;
		} else {
			getElement("tipoCnpj").required = false;
		}
		findButtonScript('excecaoICMSCliente', document.forms[0]);
	}
	
	/**
	  * Chamado no onChange da combo de tipo de cnpj.
	  * Ao selecionar um tipo de cnpj (parcial | completo) deve alterar a mascara.
	  */
	function onChangeComboTipoCnpj(elem){
		comboboxChange({e:elem});
		setMaskCnpj(elem.options[elem.selectedIndex].value);
		setElementValue("nrCNPJParcialDev", "");
	}
	
	/**
	  * Modifica a mascara do cnpj de acordo com o tipo de cnpj.
	  */ 
	function setMaskCnpj(tpCnpj) {
		if (tpCnpj == "P") {
			getElement("nrCNPJParcialDev").mask = "00000000";
			getElement("nrCNPJParcialDev").maxLength = 8;
		} else {
			getElement("nrCNPJParcialDev").mask = "00000000000000";
			getElement("nrCNPJParcialDev").maxLength = 14;
		}
	}
	
	function initWindow(event) {
		if (event.name == "cleanButton_click") {
			setMaskCnpj("P");
		}
	}

</script>