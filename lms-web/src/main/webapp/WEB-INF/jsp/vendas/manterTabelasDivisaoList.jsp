<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterTabelasDivisaoAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterTabelasDivisao" idProperty="idTabelaDivisaoCliente">

		<!-- Campos hidden para valida��o de permiss�es -->
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
				size="30" 
				disabled="true"
				serializable="false"
			/>
		</adsm:complement>

		<adsm:textbox 
			label="divisao"
			dataType="text" 
			maxLength="60" 
			property="divisaoCliente.dsDivisaoCliente"
			size="20" 
			disabled="true"
			serializable="false"
			labelWidth="19%"
			width="70%"
		/>

		<adsm:lookup 
			action="/tabelaPrecos/manterTabelasPreco" 
			criteriaProperty="tabelaPrecoString" 
			dataType="text" 
			exactMatch="true" 
			idProperty="idTabelaPreco" 
			label="tabela" 
			onclickPicker="onclickPickerLookupTabelaPreco()"
			property="tabelaPreco" 
			service="lms.vendas.manterTabelasDivisaoAction.findLookupTabelaPreco" 
			size="10"
			maxLength="9"
			labelWidth="19%" 
			width="38%"
		>
			<adsm:propertyMapping relatedProperty="tabelaPreco.dsDescricao"	modelProperty="dsDescricao" />

			<adsm:textbox dataType="text" disabled="true" property="tabelaPreco.dsDescricao" size="30" />
		</adsm:lookup>


		<adsm:combobox 
			property="blAtualizacaoAutomatica"
			label="atualizacaoAutomatica"
			domain="DM_SIM_NAO" 
			labelWidth="19%" 
			width="31%"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tabelasDivisao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid selectionMode="check" onSelectAll="validaPermissaoGrid" onSelectRow="validaPermissaoGrid" idProperty="idTabelaDivisaoCliente" property="tabelasDivisao" unique="true" rows="11">
		<adsm:gridColumn title="tabela" property="tabelaPreco.tabelaPrecoString" width="15%" />
		<adsm:gridColumn title="servico" property="servico.dsServico" width="45%" />
		<adsm:gridColumn title="atualizacaoAutomatica" property="blAtualizacaoAutomatica" renderMode="image-check" width="25%" />
		<adsm:gridColumn title="aumento" property="pcAumento" unit="percent" dataType="percent" width="15%" align="right"/>

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
	 Criada para validar acesso do usu�rio 
	 logado � filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();	   
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.manterTabelasDivisaoAction.validatePermissao", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/*
	 Criada para validar acesso do usu�rio 
	 logado � filial do cliente
	*/
	function validarPermissoes_cb(data, error){
		setElementValue("permissao", data._value);
		if(data._value!="true") {
			setDisabled("removeButton", true);
		}
	}


	function onclickPickerLookupTabelaPreco()  {
		var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
		if(tabelaPrecoString != "")	{
			setElementValue("tabelaPreco.tabelaPrecoString","");
		}
		lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});

		if(getElementValue("tabelaPreco.tabelaPrecoString")=='' && tabelaPrecoString != "") {
			setElementValue("tabelaPreco.tabelaPrecoString",tabelaPrecoString);
		}
	}
	
	
	// Seta a propriedade masterLink para true para que o campo n�o seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";
	
	
	

</script>