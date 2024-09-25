<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script type="text/javascript">

	function pageLoad() {
		onPageLoad();
		setMasterLink(this.document, true);	
		setFocus(document.getElementById("cliente.pessoa.nrIdentificacao"));
		var objTextAreaEndereco = document.getElementById("endereco");
		objTextAreaEndereco.readOnly="true";
		setElementValue("labelCliente", labelCliente);
	}
	
	function pageLoad_cb(data, error) {
		onPageLoad_cb(data, error);
		var url = new URL(parent.location.href);

		var idEnderecoPessoa = url.parameters['idEnderecoPessoa'];
		var nrIdentificacaoCliente = url.parameters['nrIdentificacaoCliente'];
		
		// força a chamada da lookup de endereco pessoa, buscando pelo idEnderecoPessoa
		if (idEnderecoPessoa) {
			setDisabled('endereco', true);
			setDisabled('enderecoPessoa.idEnderecoPessoa', true);
			setElementValue('enderecoPessoa.idEnderecoPessoa', idEnderecoPessoa);
			setElementValue('enderecoPessoa.pessoa.idPessoa', '00.000.000/0000-00');
			var x = document.getElementById("enderecoPessoa.idEnderecoPessoa");
			x.propertyMappings[x.propertyMappings.length] = { modelProperty:"idEnderecoPessoa", criteriaProperty:"enderecoPessoa.idEnderecoPessoa", inlineQuery:true, disable:true };
	    	lookupChange({e:document.getElementById("enderecoPessoa.idEnderecoPessoa"), forceChange:true});
	    	
	    	document.getElementById("enderecoPessoa.idEnderecoPessoa").masterLink = "true";
	    	document.getElementById("enderecoPessoa.pessoa.idPessoa").masterLink = "true";
	    	document.getElementById("edColeta").masterLink = "true";
			document.getElementById("nrEndereco").masterLink = "true";
			document.getElementById("dsComplementoEndereco").masterLink = "true";
			document.getElementById("dsBairro").masterLink = "true";			
			document.getElementById("nrCep").masterLink = "true";
			document.getElementById("endereco").masterLink = "true";
			document.getElementById("enderecoPessoa.municipio.nmMunicipio").masterLink = "true";
			document.getElementById("enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa").masterLink = "true";
	    			
		}
		
		// força a chamada da lookup de cliente, buscando pelo idCliente
		if (nrIdentificacaoCliente) {
			setDisabled('cliente.idCliente', true);
			setElementValue('cliente.pessoa.nrIdentificacao', nrIdentificacaoCliente);
			var x = document.getElementById("cliente.idCliente");
			x.propertyMappings[x.propertyMappings.length] = { modelProperty:"idCliente", criteriaProperty:"cliente.idCliente", inlineQuery:true, disable:true };
	    	lookupChange({e:document.getElementById("cliente.idCliente"), forceChange:true});		
	    	
	    	document.getElementById("cliente.idCliente").masterLink = "true";
	    	document.getElementById("cliente.pessoa.nrIdentificacao").masterLink = "true";
	    	document.getElementById("cliente.pessoa.nmPessoa").masterLink = "true";
	    	
		}
	}
</script>

<adsm:window service="lms.coleta.cadastrarPedidoColetaSelecionarTelefoneAction" onPageLoad="pageLoad" onPageLoadCallBack="pageLoad">
			 
	<adsm:form action="/coleta/cadastrarPedidoColeta">

		<adsm:hidden property="labelCliente" />
	
		<adsm:hidden property="idEnderecoPessoa" />
		<adsm:hidden property="pessoa.idPessoa" />
		<adsm:hidden property="cliente.tpSituacao" value="A" serializable="false"/>		
		<adsm:hidden property="nrIdentificacaoFormatado" serializable="false" />
 		<adsm:lookup label="cliente" dataType="text" property="cliente" idProperty="idCliente" 
 					 criteriaProperty="pessoa.nrIdentificacao" 
 					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
 					 service="lms.coleta.cadastrarPedidoColetaAction.findLookupCliente" 
					 action="/coleta/cadastrarPedidoColeta"
					 cmd="consultarClientes"
					 labelWidth="15%" width="85%"
					 size="19" maxLength="18" serializable="false">
			
			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />			
			<adsm:propertyMapping relatedProperty="pessoa.idPessoa" modelProperty="idCliente" /> 
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado" />
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" 
						  serializable="false" />
		</adsm:lookup>
		
		<adsm:hidden property="edColeta" serializable="false"/>
		<adsm:hidden property="nrEndereco" serializable="false"/>
		<adsm:hidden property="dsComplementoEndereco" serializable="false"/>
		<adsm:hidden property="dsBairro" serializable="false"/>
		<adsm:hidden property="nrCep" serializable="false"/>
		<adsm:textarea  label="enderecosColeta" property="endereco" maxLength="300" 
						columns="90" rows="3" labelWidth="15%" width="85%" serializable="false" >
			<adsm:lookup style="visibility: hidden;font-size: 1px" size="1" maxLength="1" dataType="text" 
						 idProperty="idEnderecoPessoa" 
						 property="enderecoPessoa" 
						 action="/configuracoes/manterEnderecoPessoa"
						 service="lms.coleta.cadastrarPedidoColetaSelecionarTelefoneAction.findLookupEnderecoPessoa"
						 criteriaProperty="pessoa.idPessoa" onPopupSetValue="enderecoColetaPopSetValue" onDataLoadCallBack="enderecoColetaCallback">
						 
				<adsm:propertyMapping criteriaProperty="pessoa.idPessoa"           modelProperty="pessoa.idPessoa" />
				<adsm:propertyMapping criteriaProperty="cliente.pessoa.nrIdentificacao"  modelProperty="pessoa.nrIdentificacao" />	
				<adsm:propertyMapping criteriaProperty="cliente.pessoa.nmPessoa"   modelProperty="pessoa.nmPessoa" />	
				<adsm:propertyMapping criteriaProperty="labelCliente"              modelProperty="labelPessoaTemp"/>
				<adsm:propertyMapping relatedProperty="edColeta"                   modelProperty="dsEndereco" />
				<adsm:propertyMapping relatedProperty="nrEndereco"                 modelProperty="nrEndereco" />
				<adsm:propertyMapping relatedProperty="dsComplementoEndereco"      modelProperty="dsComplemento" />
				<adsm:propertyMapping relatedProperty="dsBairro"                   modelProperty="dsBairro" />
				<adsm:propertyMapping relatedProperty="nrCep"                      modelProperty="nrCep" />
				<adsm:propertyMapping relatedProperty="enderecoPessoa.municipio.nmMunicipio"                           modelProperty="municipio.nmMunicipio" />
				<adsm:propertyMapping relatedProperty="enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
			</adsm:lookup>									
		</adsm:textarea>
		
		<adsm:textbox label="municipio" property="enderecoPessoa.municipio.nmMunicipio" dataType="text" 
					 size="34" labelWidth="15%" width="35%" disabled="true" serializable="false"/>
					 					 
		<adsm:textbox label="uf" property="enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa" 
					  dataType="text" size="3" disabled="true" serializable="false"/>

        <adsm:combobox property="tpTelefone" label="tipoTelefone" domain="DM_TIPO_TELEFONE" renderOptions="true"
        			   labelWidth="15%" width="35%"/>		
		<adsm:combobox property="tpUso" label="usoTelefone" domain="DM_USO_TELEFONE" renderOptions="true"/>
		
        <adsm:textbox label="ddi" dataType="integer" property="nrDdi" maxLength="5" size="5" 
        			  minValue="0" labelWidth="15%" width="35%"/>
        
		<adsm:complement label="numero" width="35%">
            <adsm:textbox dataType="integer" property="nrDdd" maxLength="5" size="5" minValue="0"/>
        	<adsm:textbox dataType="text" property="nrTelefone" maxLength="10" size="10" minValue="0"/>
        </adsm:complement>               	
        
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="findButton" disabled="false" onclick="carregaGrid()"/>
			<adsm:resetButton caption="limpar"/>
		</adsm:buttonBar>  
		
		<script>
			var lms_02074 = '<adsm:label key="LMS-02074"/>';
			var labelCliente = '<adsm:label key="cliente"/>';
		</script>		      
	</adsm:form>
	
	<adsm:grid idProperty="idTelefoneEndereco" property="telefoneEndereco" onRowClick="onRowClick"
			   rows="8" gridHeight="260" selectionMode="none" autoSearch="false" unique="true"
			   service="lms.coleta.cadastrarPedidoColetaSelecionarTelefoneAction.findPaginatedTelefoneEndereco"
			   rowCountService="lms.coleta.cadastrarPedidoColetaSelecionarTelefoneAction.getRowCountTelefoneEndereco">
	    <adsm:gridColumn title="cliente" property="pessoa.nmPessoa" width="210"/>
		<adsm:gridColumn title="tipoTelefone" property="tpTelefone.description" width="85"/>
		<adsm:gridColumn title="usoTelefone" property="tpUso.description" width="85"/>
		<adsm:gridColumn title="ddi" property="nrDdi" width="55" align="right"/>
		<adsm:gridColumn title="ddd" property="nrDdd" width="55" align="right"/>
		<adsm:gridColumn title="numero" property="nrTelefone" width="70" align="right"/>
		<adsm:gridColumn title="municipio" property="enderecoPessoa.municipio.nmMunicipio" />
		<adsm:gridColumn title="uf" property="enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa" width="50"/>
		
		<adsm:buttonBar> 
			<adsm:button caption="fechar" id="closeButton" disabled="false" onclick="window.close()"/>
		</adsm:buttonBar>
	</adsm:grid>	
	
</adsm:window>

<script type="text/javascript">

	function initWindow(eventObj) {
		setDisabled("findButton", false);
		setDisabled("closeButton", false);
	}	

	function enderecoColetaPopSetValue(data) {
		formataEndereco(data);
	}
	
	function enderecoColetaCallback_cb(data, error) {
		if (data.length==1) 
			setEndereco(data[0].endereco);
		enderecoPessoa_pessoa_idPessoa_exactMatch_cb(data);
	}
	
	function formataEndereco(data) {
		var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaSelecionarTelefoneAction.formataEndereco", "formataEndereco", data);
		xmit({serviceDataObjects:[sdo]});	
	}

	function formataEndereco_cb(data, error) {
		if (!error) {
			setEndereco(data.endereco);
		}
	}
	
	function setEndereco(endereco) {
		setElementValue("endereco", endereco);
	}
	
	function carregaGrid() {
		if(getElementValue("enderecoPessoa.idEnderecoPessoa") != ""	|| getElementValue("nrTelefone") != "") {
			var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END'); 
			telefoneEndereco_cb(fb);
		} else {
			alert(lms_02074);
		}
	}
	
	function onRowClick(){
		var url = new URL(parent.location.href);
		var rowClick = url.parameters['rowClick'];
		if (rowClick=="false"){
			return false;
		}
		return true;
	}
		
</script>