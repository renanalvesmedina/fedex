<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterFatorCubagemDivisaoAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterFatorCubagemDivisao" idProperty="idFatorCubagemDivisao">

		<!-- Campos hidden para validação de permissões -->
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>

		<adsm:hidden property="divisaoCliente.idDivisaoCliente" />

		<adsm:complement
			label="cliente"
			width="70%"
			labelWidth="19%"
			separator="branco"
			>
			<adsm:textbox 
				dataType="text"
				property="divisaoCliente.cliente.pessoa.nrIdentificacao"
				size="20" 
				maxLength="20"
				disabled="true" 
				serializable="false"
			/>
			<adsm:textbox 
				dataType="text" 
				maxLength="50" 
				property="divisaoCliente.cliente.pessoa.nmPessoa"
				size="47" 
				disabled="true"
				serializable="false"
			/>
		</adsm:complement>

		<adsm:textbox 
        	dataType="integer" 
        	property="divisaoCliente.cdDivisaoCliente" 
        	label="codigo" 
        	disabled="true"
			labelWidth="19%"
			width="28%"
        	maxLength="10" 
        	size="10"
        />
        
		<adsm:textbox 
			dataType="text" 
			property="divisaoCliente.dsDivisaoCliente" 
			label="divisao" 
			disabled="true"
			maxLength="60" 
			labelWidth="10%"
			size="40"
		/>
		
        <adsm:range label="vigencia" labelWidth="19%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/> 
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridFatorCubagemDivisao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid selectionMode="check" onSelectAll="validaPermissaoGrid" onSelectRow="validaPermissaoGrid" idProperty="idFatorCubagemDivisao" property="gridFatorCubagemDivisao" unique="true" rows="11">
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="30%" dataType="JTDate" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="30%" dataType="JTDate" />
		<adsm:gridColumn title="fatorCubagemReal" property="nrFatorCubagemReal" dataType="decimal" width="40%" align="right"/>

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
		var sdo = createServiceDataObject("lms.vendas.manterFatorCubagemDivisaoAction.validatePermissao", "validarPermissoes", data);
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