<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>

	function carregaDados_cb(data, error) {
		onPageLoad_cb(data, error);
		var data = new Array();
	    var sdo = createServiceDataObject("lms.sim.manterPedidosComprasAction.findInformacoesUsuarioLogado", "filialSession",data);
	    xmit({serviceDataObjects:[sdo]});
	}
	function pageLoad_cb(data, error) {
		enableFields();
		// popula na sessao, variaveis hidden
		onDataLoad_cb(data,error);
		//loadGrid();
		//populaFilial();
		
		var idDoctoServicoConsulta = getNestedBeanPropertyValue(data,"idDoctoServicoConsulta");
		if(idDoctoServicoConsulta != null)
			setDisabled("buttonInfDocSer",false);
		else
		 	setDisabled("buttonInfDocSer", true);	
		
		var dtVigenciaInicialDetalhe = getNestedBeanPropertyValue(data, "dtVigenciaInicial");
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");

		
	} 
	// Este método é chamado assim que o lookup retorna da pesquisa
	function dataLoadRemetente_cb(data) {
		if (data != undefined && data.length == 1 || data.length == 2) { 
			enableFields();
			remetente_pessoa_nrIdentificacao_exactMatch_cb(data);
			var idCliente = getNestedBeanPropertyValue(data,":0.idCliente");
			if(idCliente != null)
				setDisabled("remetenteButtom",true);
			else 
				setDisabled("remetenteButtom",false);	
		}else{
			setDisabled("remetenteButtom",false);
			setFocus(document.getElementById("remetenteButtom"),false);
			disableFields();
		}
	}
	function popUpSetRemetente(data) {
		setDisabled("remetenteButtom",true);
		return true;
	}
	// quando é alterado o valor do campo da lookup	
	function changeRemetente(elem) {
		setElementValue("remetente.pessoa.nmPessoa","");
		setElementValue("remetente.idCliente","");
		var flag = remetente_pessoa_nrIdentificacaoOnChangeHandler();
		
		if (elem.value == ""){
			setDisabled("remetenteButtom",false);
			enableFields();	
		}	
					
		return flag;
	}
	
	function dataLoadDestinatario_cb(data) {
		if (data != undefined && data.length == 1 || data.length == 2) {
		    destinatario_pessoa_nrIdentificacao_exactMatch_cb(data);
			enableFields();
			var idCliente = getNestedBeanPropertyValue(data,":0.idCliente");
			if(idCliente != null)
				setDisabled("destinatarioButtom",true);
			else 
				setDisabled("destinatarioButtom",false);	
		}else{
			setDisabled("destinatarioButtom",false);
			setFocus("destinatarioButtom",false);
			disableFields();
		}
	}
	function popUpSetDestinatario(data) {
		setDisabled("destinatarioButtom",true);
		return true;
	}
	function changeDestinatario(elem) {
		setElementValue("destinatario.pessoa.nmPessoa","");
		setElementValue("destinatario.idCliente","");
		var flag = destinatario_pessoa_nrIdentificacaoOnChangeHandler();
		
		if (elem.value == ""){
			setDisabled("destinatarioButtom",false);
			enableFields();	
		}	
		
		return flag;
	}
</script>
<adsm:window title="manterPedidosCompras" 
		service="lms.sim.manterPedidosComprasAction" 
		onPageLoadCallBack="carregaDados">
		
	<adsm:form 	idProperty="idPedidoCompra"	action="/sim/manterPedidosCompras"
				onDataLoadCallBack="pageLoad" service="lms.sim.manterPedidosComprasAction.findByIdDetalhamento" >
	
		<adsm:hidden property="idFilialSessao" />
		<adsm:hidden property="sgFilialSessao" />
		<adsm:hidden property="nmFilialSessao" />

		<adsm:hidden property="idUsuario" />
		<adsm:hidden property="usuarioSessao" />
		<adsm:hidden property="dataHoraInclusaoSessao" />
		<adsm:hidden property="dtEmissaoPedido"/>
		<adsm:hidden property="tpSituacao" value="A"/>
  		
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		
		<adsm:hidden property="idDoctoServicoConsulta" />
		
		
		
		<adsm:lookup label="filialInclusao" dataType="text"  maxLength="3" labelWidth="15%" width="77%" size="3"
				     service="lms.sim.manterPedidosComprasAction.findLookupFilial" property="filial" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais" required="true" >
  		            <adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
					<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
					<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="35" disabled="true"/> 
		</adsm:lookup>



		<adsm:lookup property="remetente" idProperty="idCliente" criteriaProperty="pessoa.nrIdentificacao"  
					 onDataLoadCallBack="dataLoadRemetente" 
					 onPopupSetValue="popUpSetRemetente"
					 onchange="return changeRemetente(this);" allowInvalidCriteriaValue="true" 
					 service="lms.sim.manterPedidosComprasAction.findLookupCliente" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/vendas/manterDadosIdentificacao" dataType="text" 
					 label="remetente"  size="20" maxLength="20" width="56%" labelWidth="15%" >	
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" />
		</adsm:lookup>
		<td colspan="12">
		<adsm:button  style="FmLbSection" 
							id="remetenteButtom" 
							caption="incluir"  
							onclick="cadastrarCliente('remetente');" disabled="false" />
		</td>					
							
		
		<adsm:hidden property="destinatario.nrIdentificacao" serializable="false"/>
		<adsm:hidden property="destinatario.tpCliente"/>
		<adsm:lookup 
					 action="/vendas/manterDadosIdentificacao" 	
					 service="lms.sim.manterPedidosComprasAction.findLookupCliente"  
					 onchange="return changeDestinatario(this);"
				
				 	 property="destinatario" 
					 onDataLoadCallBack="dataLoadDestinatario" 
					 idProperty="idCliente"  
					 criteriaProperty="pessoa.nrIdentificacao" 
					 onPopupSetValue="popUpSetDestinatario"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 
					 allowInvalidCriteriaValue="true"
					 dataType="text" 
					 label="destinatario"  size="20" maxLength="20" width="56%" labelWidth="15%" >	
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="destinatario.pessoa.nmPessoa"/>
		   	<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" relatedProperty="destinatario.nrIdentificacao"/>
		   	<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
			<adsm:propertyMapping modelProperty="tpCliente.value" relatedProperty="destinatario.tpCliente"/>
			<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" />
		</adsm:lookup>	
		<td colspan="12">
		<adsm:button  	style="FmLbSection" 
						id="destinatarioButtom" 
						caption="incluir"  
						onclick="cadastrarCliente('destinatario');" disabled="false"/>
		</td>				
		<adsm:textbox dataType="integer" property="pedido" label="pedido" labelWidth="15%"  required="true" maxLength="10"/>
		<adsm:textbox dataType="text" property="pedidoInternacional" label="pedidoInternacional" labelWidth="15%"  required="true" maxLength="20"/>
		
		<adsm:combobox domain="DM_TIPO_ORIGEM_PEDIDO_COMPRA" property="tpOrigem" label="origem" 
						labelWidth="15%"  required="true"
		/>		
		<adsm:textbox dataType="text" property="fatura" label="fatura" labelWidth="15%"  required="true" maxLength="20"/>
		<adsm:textbox dataType="text" property="notaFiscal" label="notaFiscal" labelWidth="15%" required="true" maxLength="20"/>

		<adsm:textbox dataType="decimal" mask="#,###,###,###,###,##0.00" property="pesoBruto" label="pesoBruto" labelWidth="15%"  unit="kg" />
		<adsm:textbox dataType="decimal" mask="#,###,###,###,###,##0.00" property="pesoLiquido" label="pesoLiquido" labelWidth="15%"  unit="kg" />

		<adsm:textbox dataType="integer" property="quantidadeVolumes" label="quantidadeVolumes" labelWidth="15%" maxLength="5"/>
		
		<adsm:combobox label="moeda" property="pedidoCompra.idMoeda" 
					   optionProperty="idMoeda" optionLabelProperty="siglaSimbolo" 
					   service="lms.sim.manterPedidosComprasAction.findMoeda" 
					    labelWidth="15%" />
		<adsm:textbox dataType="decimal" mask="#,###,###,###,###,##0.00" property="valorExportacao" label="valorExportacao" labelWidth="15%"  />

		<adsm:checkbox property="pagtoCartao" label="pagamentoCartao" labelWidth="15%" width="82%"/>
		
		<adsm:combobox property="tpModalBrasil" label="modalBrasil" domain="DM_MODAL" required="true" labelWidth="15%"   />	
		<adsm:combobox property="tpModalExterior" label="modalExterior" domain="DM_MODAL" required="true" labelWidth="15%"   />	

		<adsm:textbox dataType="JTDateTimeZone" property="emissaoPedido" label="dataHoraEmissao" labelWidth="15%" width="82%" required="true" size="40"/> 

		<adsm:textbox dataType="JTDate" property="previsaoEntregaBrasil" label="dataPrevisaoEntregaBrasil" labelWidth="15%"  cellStyle="vertical-Align:bottom;"/>
		<adsm:textbox dataType="JTDate" property="previsaoEntregaExterior" label="dataPrevisaoEntregaExterior" labelWidth="15%"  cellStyle="vertical-Align:bottom;"/>
		<adsm:textbox dataType="JTDateTimeZone" property="dataHoraInclusao" 
		label="dataHoraInclusao"  labelWidth="15%" required="true" disabled="true" />
		<adsm:textbox dataType="text" property="usuarioInclusao"  label="usuarioInclusao"  labelWidth="15%" required="true" disabled="true"/>
		<adsm:buttonBar>
			<adsm:button caption="informacoesDocumentoServico" id="buttonInfDocSer" action="/sim/consultarLocalizacoesMercadorias" cmd="main" >
				<adsm:linkProperty src="idDoctoServicoConsulta" target="idDoctoServicoConsulta"/>
			</adsm:button>
			<adsm:button id="botaoSalvar" caption="salvar"  service="lms.sim.manterPedidosComprasAction.storeCustom" callbackProperty="afterStore" />
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script>
	document.getElementById("idFilialSessao").masterLink = true;
	document.getElementById("sgFilialSessao").masterLink = true;
	document.getElementById("nmFilialSessao").masterLink = true;

	document.getElementById("dataHoraInclusaoSessao").masterLink = true;
	document.getElementById("usuarioSessao").masterLink = true;
	document.getElementById("idUsuario").masterLink = true;
	
	document.getElementById("dtEmissaoPedido").masterLink = true;
	
	
	function initWindow(evento){
		if (evento.name == "tab_click" || evento.name == "newButton_click" || evento.name == "removeButton") {
			populaFilial();
			setElementValue("tpModalBrasil","R");
			setElementValue("tpModalExterior","R");
			setElementValue("tpOrigem","B");
			setElementValue("emissaoPedido",getElementValue("dtEmissaoPedido"));
			
			enableFields();
			setDisabled("dataHoraInclusao",true);
			setDisabled("usuarioInclusao",true);
			
			setDisabled("remetenteButtom",false);
			setDisabled("destinatarioButtom",false);
		}
	}
	function populaFilial() {
		setElementValue("filial.idFilial",getElementValue("idFilialSessao"));	
		setElementValue("filial.sgFilial",getElementValue("sgFilialSessao"));	
		setElementValue("filial.pessoa.nmFantasia",getElementValue("nmFilialSessao"));
		
		setElementValue("usuarioInclusao",getElementValue("usuarioSessao"));
		setElementValue("dataHoraInclusao",getElementValue("dataHoraInclusaoSessao"));
		
		setDisabled("botaoSalvar",false);
	 	
	}
	
	function filialSession_cb(data) {
		setElementValue("idFilialSessao",getNestedBeanPropertyValue(data,"filial.idFilial"));	
		setElementValue("sgFilialSessao",getNestedBeanPropertyValue(data,"filial.sgFilial"));	
		setElementValue("nmFilialSessao",getNestedBeanPropertyValue(data,"pessoa.nmFantasia"));
		setElementValue("idUsuario",getNestedBeanPropertyValue(data,"idUsuarioSessao"));
		setElementValue("usuarioSessao",getNestedBeanPropertyValue(data,"usuarioSessao"));
		setElementValue("dataHoraInclusaoSessao",getNestedBeanPropertyValue(data,"dataHoraAtual"));
		
		var dhEmissao = setFormat(document.getElementById("emissaoPedido"),getNestedBeanPropertyValue(data,"dataHoraEmissao"));
		setElementValue("dtEmissaoPedido",dhEmissao);
		setElementValue("emissaoPedido",dhEmissao);
		setElementValue("tpModalBrasil","R");
		setElementValue("tpModalExterior","R");
		setElementValue("tpOrigem","B");
		

		populaFilial();
		setDisabled("remetenteButtom",false);
		setDisabled("destinatarioButtom",false);
		
	}
	function cadastrarCliente(tipo){
		var data = showModalDialog('expedicao/cadastrarClientes.do?cmd=main&origem=exp',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:750px;dialogHeight:300px;');
		if(data) {
		    if(tipo == "remetente"){
		    	var idCliente = getNestedBeanPropertyValue(data,"idCliente");
		    	var nmPessoa = getNestedBeanPropertyValue(data,"pessoa.nmPessoa");
		    	var nrIdentificacao = getNestedBeanPropertyValue(data,"nrIdentificacaoFormatado");
		    	setElementValue("remetente.idCliente",idCliente);
		    	setElementValue("remetente.pessoa.nmPessoa",nmPessoa);
		    	setElementValue("remetente.pessoa.nrIdentificacao",nrIdentificacao);
		    	setDisabled("remetenteButtom",true);
		    }else if (tipo == "destinatario"){
		    	var idCliente = getNestedBeanPropertyValue(data,"idCliente");
		    	var nmPessoa = getNestedBeanPropertyValue(data,"pessoa.nmPessoa");
		    	var nrIdentificacao = getNestedBeanPropertyValue(data,"nrIdentificacaoFormatado");
		    	setElementValue("destinatario.idCliente",idCliente);
		    	setElementValue("destinatario.pessoa.nmPessoa",nmPessoa);
		    	setElementValue("destinatario.pessoa.nrIdentificacao",nrIdentificacao);
		    	setDisabled("destinatarioButtom",true);
		    }
		    enableFields();
			
		} else{
			disableFields();
			
		}
	}
	
	function enableFields(){
		setDisabled("botaoSalvar",false);
		setDisabled("pedido",false);
		setDisabled("pedidoInternacional",false);
		setDisabled("tpOrigem",false);
		setDisabled("fatura",false);
		setDisabled("notaFiscal",false);
		setDisabled("pesoBruto",false);
		setDisabled("pesoLiquido",false);
		setDisabled("quantidadeVolumes",false);
		setDisabled("pedidoCompra.idMoeda",false);
		setDisabled("valorExportacao",false);
		setDisabled("pagtoCartao",false);
		setDisabled("tpModalBrasil",false);
		setDisabled("tpModalExterior",false);
		setDisabled("emissaoPedido",false);
		setDisabled("previsaoEntregaBrasil",false);
		setDisabled("previsaoEntregaExterior",false);
		setDisabled("dataHoraInclusao",true);
		setDisabled("usuarioInclusao",true);
	}
	
	function disableFields(){
		setDisabled("botaoSalvar",true);
		setDisabled("pedido",true);
		setDisabled("pedidoInternacional",true);
		setDisabled("tpOrigem",true);
		setDisabled("fatura",true);
		setDisabled("notaFiscal",true);
		setDisabled("pesoBruto",true);
		setDisabled("pesoLiquido",true);
		setDisabled("quantidadeVolumes",true);
		setDisabled("pedidoCompra.idMoeda",true);
		setDisabled("valorExportacao",true);
		setDisabled("pagtoCartao",true);
		setDisabled("tpModalBrasil",true);
		setDisabled("tpModalExterior",true);
		setDisabled("emissaoPedido",true);
		setDisabled("previsaoEntregaBrasil",true);
		setDisabled("previsaoEntregaExterior",true);
		setDisabled("dataHoraInclusao",true);
		setDisabled("usuarioInclusao",true);
	}
	function afterStore_cb(data,error,key) {
		store_cb(data,error,key);
		if(error == undefined){
		    setDisabled("remetenteButtom",true);
			setDisabled("destinatarioButtom",true);
			var idDoctoServicoConsulta = getNestedBeanPropertyValue(data,"idDoctoServicoConsulta");
			if(idDoctoServicoConsulta != undefined)
				setDisabled("buttonInfDocSer", false);
			else
				setDisabled("buttonInfDocSer", true);	
		}else if (error != undefined){
			if(key =="LMS-10044"){
		    	setFocus(document.getElementById("remetente.pessoa.nrIdentificacao"));
		    }	
		    if(key =="LMS-10043")
		    	setFocus(document.getElementById("destinatario.pessoa.nrIdentificacao"));	
		}
		
	}
	
</script>