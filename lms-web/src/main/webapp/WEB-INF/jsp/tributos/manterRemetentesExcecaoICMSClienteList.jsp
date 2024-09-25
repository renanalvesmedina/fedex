<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.tributos.manterRemetentesExcecaoICMSCienteAction">

	<adsm:form action="/tributos/manterRemetentesExcecaoICMSCliente">

		<adsm:hidden property="excecaoICMSCliente.idExcecaoICMSCliente" serializable="true"/>
		
  	    <adsm:hidden property="excecaoICMSCliente.unidadeFederativa.idUnidadeFederativa" serializable="true"/>
  	    <adsm:textbox label="uf" labelWidth="20%" size="5" property="excecaoICMSCliente.unidadeFederativa.sgUnidadeFederativa" 
  	    				dataType="text" disabled="true" width="6%"/>
		
		<adsm:hidden
					property="unidadeFederativa.siglaDescricao" 
					serializable="false"/>			
		
		<adsm:textbox
					dataType="text" 
					disabled="true"
					property="excecaoICMSCliente.unidadeFederativa.nmUnidadeFederativa" 
					width="24%" 
					size="20" />
					  
		<adsm:hidden property="excecaoICMSCliente.tipoTributacaoIcms.idTipoTributacaoIcms" serializable="true"/>	
		<adsm:textbox
					label="tipoTributacao" 
					dataType="text" 
					property="excecaoICMSCliente.tipoTributacaoIcms.dsTipoTributacaoIcms" 
					disabled="true"
					labelWidth="20%" 
					width="30%" 
					size="20" />	  
					
		
					
		<adsm:textbox 
					property="excecaoICMSCliente.nrCNPJParcialDev" 
					dataType="text" 
					maxLength="14"
					serializable="false"
					label="nrCNPJParcialDev"
					labelWidth="20%"
					width="30%"/>
					
		<adsm:textbox 
					property="nmDevedor" 
					dataType="text" 
					serializable="false"
					label="nomeDevedor"
					labelWidth="20%"
					disabled="true"
					size="35%"
					width="30%"/>					
		
					
		<adsm:combobox 
					property="excecaoICMSCliente.tpFrete" 
					label="tipoFrete" 
					domain="DM_TIPO_FRETE"
					labelWidth="20%" 
					width="30%"/>			
		
		<adsm:textbox 
					label="vigencia" 
					dataType="JTDate" 
					property="dtVigencia"
					labelWidth="20%"
					width="30%"/>
		
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
			<adsm:button buttonType="findButton" onclick="findButtonClick();" caption="consultar"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idRemetenteExcecaoICMSCli" 
			   property="remetenteExcecaoICMSCli" 
			   defaultOrder="nrCnpjParcialRem, dtVigenciaInicial" 
			   service="lms.tributos.manterRemetentesExcecaoICMSCienteAction.findPaginatedTela"
			   rowCountService="lms.tributos.manterRemetentesExcecaoICMSCienteAction.getRowCountTela"
			   selectionMode="check" 
			   rows="10"
			   unique="true">
		
		<adsm:gridColumn title="nrCNPJParcialRem" property="nrCnpjParcialRem" width="" dataType="text"/>
		<adsm:gridColumn title="vigencia" property="dtVigenciaInicial" dataType="JTDate" width="200"/>
		<adsm:gridColumn title="" property="dtVigenciaFinal" dataType="JTDate" width="200"/>
		
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