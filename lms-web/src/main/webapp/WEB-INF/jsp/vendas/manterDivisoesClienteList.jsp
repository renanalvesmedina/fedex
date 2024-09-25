<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterDivisoesClienteAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterDivisoesCliente" idProperty="idDivisaoCliente">

		<!-- Campos hidden para validação de permissões -->
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>

		<adsm:hidden property="cliente.idCliente"/>
		
		<adsm:complement
			label="cliente"
			width="45%"
			labelWidth="8%"
			separator="branco"
			>
			<adsm:textbox 
				dataType="text"
				property="cliente.pessoa.nrIdentificacao"
				size="20" 
				maxLength="15"
				disabled="true" 
				serializable="false"
			/>
			<adsm:textbox 
				dataType="text" 
				maxLength="30" 
				property="cliente.pessoa.nmPessoa"
				size="30" 
				disabled="true"
				serializable="false"
			/>
		</adsm:complement>

		<%--------------------%>
		<%-- SITUACAO COMBO --%>
		<%--------------------%>
		<adsm:combobox 
			label="situacao" 
			labelWidth="10%"
			property="tpSituacao" 
			width="30%"
			domain="DM_STATUS"
		/>


		<adsm:textbox 
        	dataType="integer" 
        	property="cdDivisaoCliente" 
        	label="codigo" 
        	maxLength="10" 
        	size="10"
			labelWidth="8%"
			width="45%"
        />
		<adsm:textbox 
			dataType="text" 
			property="dsDivisaoCliente" 
			label="divisao" 
			maxLength="60" 
			size="40"
			labelWidth="10%"
			width="30%"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="divisoesCliente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid selectionMode="check" 
			   onSelectAll="validaPermissaoGrid" 
			   onSelectRow="validaPermissaoGrid" 
			   idProperty="idDivisaoCliente" rows="13"
			   property="divisoesCliente" unique="true" 
			   defaultOrder="cdDivisaoCliente,dsDivisaoCliente">
		<adsm:gridColumn title="codigo" property="cdDivisaoCliente" width="10%" align="right"/>
		<adsm:gridColumn title="divisao" property="dsDivisaoCliente" width="50%" />
		<adsm:gridColumn title="quantidadeDocumentosRomaneio" property="nrQtdeDocsRomaneio" width="30%" align="right"/>
		<adsm:gridColumn title="situacao" property="tpSituacao.description" width="10%" align="left"/> 

		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	function validaPermissaoGrid(data){
		if (document.getElementById("permissao").value!="true") {
			setDisabled("removeButton", true);
			return false;
		}
	}

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();	   
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.manterDivisoesClienteAction.validatePermissao", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validarPermissoes_cb(data, error){
		setElementValue("permissao", data._value);
		if(data._value!="true") {
			setDisabled("removeButton", true);
		}
	}

	// Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";

</script>