<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.cadastrarPedidoColetaConsultarClientesAction" >
	
	<adsm:i18nLabels>
		<adsm:include key="cliente"/>
    	<adsm:include key="LMS-01104"/>
    </adsm:i18nLabels>
    
	<adsm:form action="/coleta/cadastrarPedidoColeta" idProperty="idCliente">
		
		<adsm:combobox property="pessoa.tpPessoa" labelWidth="15%" width="35%" label="tipoPessoa" domain="DM_TIPO_PESSOA" renderOptions="true" />

		<adsm:complement label="identificacao" >
			<adsm:combobox property="pessoa.tpIdentificacao" width="15%" domain="DM_TIPO_IDENTIFICACAO" onchange="trocaTpIdentificacao(this);" renderOptions="true" />	
			<adsm:textbox dataType="text" property="pessoa.nrIdentificacao" width="20%" size="20"/>
		</adsm:complement>	

		<adsm:textbox label="telefone" property="nrDdd" dataType="integer"
						  size="5" maxLength="5" labelWidth="15%" width="81%" >
			<adsm:textbox property="nrTelefone" dataType="integer"
						  size="10" maxLength="10" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" size="53" property="pessoa.nmPessoa" label="nomeRazaoSocial" 
					  maxLength="50" labelWidth="15%" width="81%" />
		<adsm:textbox dataType="text" property="nmFantasia" label="nomeFantasia" labelWidth="15%" 
					  width="35%" maxLength="50" size="36"/>
		<adsm:textbox dataType="integer" property="nrConta" label="numeroConta" labelWidth="15%" 
					  width="35%" size="13" maxLength="10"/>

		<adsm:combobox property="tpCliente" domain="DM_TIPO_CLIENTE" label="tipoCliente" labelWidth="15%" width="35%" renderOptions="true"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" onclick="consultar(this);" buttonType="findButton" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>		
	</adsm:form>
	
	<adsm:grid idProperty="idCliente" property="cliente" gridHeight="180" unique="true" scrollBars="horizontal"
			   service="lms.coleta.cadastrarPedidoColetaConsultarClientesAction.findPaginatedClientesByTelefone"
			   rowCountService="lms.coleta.cadastrarPedidoColetaConsultarClientesAction.getRowCountClientesByTelefone" >
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" isDomain="true" width="70" />
		<adsm:gridColumn title="" property="pessoa.nrIdentificacaoFormatado" width="100" align="right"/>
		<adsm:gridColumn title="nome" property="pessoa.nmPessoa" width="160" />
		<adsm:gridColumn title="nomeFantasia" property="pessoa.nmFantasia" width="115" />
		<adsm:gridColumn title="numeroConta" property="nrConta" width="115" align="right"/>
		<adsm:gridColumn title="tipoCliente" property="tpCliente" isDomain="true" width="115" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="100"/>
		<adsm:gridColumn title="endereco" property="pessoa.enderecoFormatado" width="350"/>
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="closeButton" disabled="false" onclick="self.close()"/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
<!-- Removido botão para adicionar novo cliente pois essa tela foi disponibilizada no SWT.
			<adsm:button caption="novoCliente" id="novoCliente" disabled="false" 
						 onclick="showModalDialog('expedicao/cadastrarClientes.do?cmd=main&origem=col',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:750px;dialogHeight:300px;');"/>

-->
<script type="text/javascript">
	function initWindow(eventObj) {
		setDisabled("closeButton", false);
		//setDisabled("novoCliente", false);
	}	
	
	/*
	 * Validação da consulta de cliente.<BR>
	 * Não pode consultar sem antes ter informado Identificação, Nome/Razão social, Nome fantasia ou Número da conta.
	 * Se não informado nenhum mostrar mensagem LMS-01104
	 */
	function validateConsultar() {
		if(getElementValue("nrDdd") == "") {
			if(getElementValue("nrTelefone") == "") {
				if ( getElementValue("pessoa.nrIdentificacao") == "" &&
						getElementValue("pessoa.nmPessoa") == "" &&
						getElementValue("nmFantasia") == "" &&
						getElementValue("nrConta") == ""){
					alert(i18NLabel.getLabel("LMS-01104"));
					return false;
				}
			}		
		}
		return true;
	}	
	
	function consultar(eThis) {	
		if (validateConsultar()){
			findButtonScript('cliente', eThis.form);
		}
	}	
	
	function trocaTpIdentificacao(obj) {
	    var tpPessoa = getElementValue("pessoa.tpPessoa");
	    var tpIdentificacao = getElementValue("pessoa.tpIdentificacao");
	    resetValue("pessoa.nrIdentificacao");
			 
		if (tpPessoa == "F" && tpIdentificacao == "CPF" ) {
			document.getElementById("pessoa.nrIdentificacao").dataType = "CPF";
		} else if (tpPessoa == "J" && tpIdentificacao == "CNPJ" ){
		    document.getElementById("pessoa.nrIdentificacao").dataType = "CNPJ";
		} else {
			document.getElementById("pessoa.nrIdentificacao").dataType = "text";
		}
	}	
</script>