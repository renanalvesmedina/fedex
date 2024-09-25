<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.tributos.manterExcecaoICMSClienteAction">

	<adsm:form action="/tributos/manterExcecaoICMSCliente">

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
					  
		<adsm:combobox 
					label="tipoTributacao" 
					property="tipoTributacaoIcms.idTipoTributacaoIcms" 
					service="lms.tributos.manterExcecaoICMSClienteAction.findComboTipoTributacaoIcms" 
					onlyActiveValues="true" 
					optionLabelProperty="dsTipoTributacaoIcms" 
					optionProperty="idTipoTributacaoIcms" 
					labelWidth="20%" 
					width="30%" 
					boxWidth="200"/>	  
					
		<adsm:combobox 
					property="tpFrete"  
					label="tipoFrete" 
					domain="DM_TIPO_FRETE"
					labelWidth="20%" 
					width="30%"/>
					
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
					
		<adsm:combobox 
					property="blSubcontratacao" 
					label="subcontratacao" 
					domain="DM_SIM_NAO"
					labelWidth="20%" 
					width="30%"/>  
					
		<adsm:textbox 
					label="vigencia" 
					dataType="JTDate" 
					property="dtVigencia"
					labelWidth="20%"
					width="30%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button buttonType="findButton" onclick="findButtonClick();" caption="consultar"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idExcecaoICMSCliente" 
			   property="excecaoICMSCliente" 
			   defaultOrder="unidadeFederativa_.sgUnidadeFederativa, tipoTributacaoIcms_.dsTipoTributacaoIcms, tpFrete, nrCNPJParcialDev, dtVigenciaInicial" 
			   selectionMode="check" 
			   rows="11"
			   gridHeight="200" 
			   unique="true">
		
		<adsm:gridColumn title="uf" property="unidadeFederativa.sgUnidadeFederativa" width="" dataType="text" />
		<adsm:gridColumn title="tipoTributacao" property="tipoTributacaoIcms.dsTipoTributacaoIcms" width="130" dataType="text"/>
		<adsm:gridColumn title="tipoFrete" property="tpFrete"  width="100" align="left" isDomain="true" dataType="text"/>
		<adsm:gridColumn title="nrCNPJParcialDev" property="nrCNPJParcialDev"  width="150" dataType="text"/>
		<adsm:gridColumn title="vigencia" property="dtVigenciaInicial" dataType="JTDate" width="90"/>
		<adsm:gridColumn title="" property="dtVigenciaFinal" dataType="JTDate" width="90"/>
		<adsm:gridColumn title="subcontratacao" property="blSubcontratacao"  width="100" align="center" renderMode="image-check"/>
        
        <adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
	
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