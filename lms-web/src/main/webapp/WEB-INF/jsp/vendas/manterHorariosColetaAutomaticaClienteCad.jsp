<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.coletaAutomaticaClienteService" onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/vendas/manterHorariosColetaAutomaticaCliente" idProperty="idColetaAutomaticaCliente" onDataLoadCallBack="myDataLoad">
	
		<adsm:hidden property="idFilial" serializable="false"/>
	
		<adsm:hidden property="cliente.idCliente"/>
		
		<adsm:textbox dataType="text" property="cliente.pessoa.nrIdentificacao" label="cliente" size="20" maxLength="20" disabled="true" width="13%" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" maxLength="60" disabled="true" width="61%" serializable="false"/>
		</adsm:textbox>

		<adsm:combobox property="tpDiaSemana" label="diaSemana" width="35%" domain="DM_DIAS_SEMANA"/>

		<adsm:range label="horario" width="30%">
			<adsm:textbox property="hrChegada" dataType="JTTime" required="true"/>
			<adsm:textbox property="hrSaida" dataType="JTTime" required="true"/>
		</adsm:range>
		
		<adsm:combobox property="servico.idServico"
					   label="servico"
					   boxWidth="240"
					   onlyActiveValues="true"
					   optionLabelProperty="dsServico"
					   optionProperty="idServico"
					   service="lms.configuracoes.servicoService.find"
					   required="true"/>
					   
		<adsm:combobox property="naturezaProduto.idNaturezaProduto"
					   label="naturezaProduto"
					   boxWidth="200"
					   onlyActiveValues="true"
					   optionLabelProperty="dsNaturezaProduto"
					   optionProperty="idNaturezaProduto"
					   service="lms.expedicao.naturezaProdutoService.find"
					   required="true"/>
		
		<adsm:hidden property="idEnderecoPessoaTmp" serializable="false"/>

		<adsm:combobox property="enderecoPessoa.idEnderecoPessoa" 
		 			   label="endereco" 
		 			   optionLabelProperty="enderecoCompleto" 
		 			   optionProperty="idEnderecoPessoa" 
		 			   service="lms.vendas.manterHorariosColetaAutomaticaClienteAction.findEnderecoMunicipioUFComboVigenteFuturo" 
		 			   width="85%" 
		 			   boxWidth="442"
		 			   autoLoad="false"
		 			   required="true"/>

		<adsm:buttonBar>
			<adsm:storeButton  id="storeButton"/>
			<adsm:newButton    id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script>

	/*
	* Criada para validar acesso do usuário 
	* logado à filial do cliente
	*/
	function myDataLoad_cb(data, error, erromsg, eventObj) {
		onDataLoad_cb(data, error, erromsg, eventObj);
		setElementValue("idEnderecoPessoaTmp", getNestedBeanPropertyValue(data, "enderecoPessoa.idEnderecoPessoa"));
		initWindow(eventObj);
		var data = new Array();	   
		var idPessoa = getElementValue("cliente.idCliente");      
		var idEnderecoPessoa = getElementValue("idEnderecoPessoaTmp");
		if (idPessoa != undefined){			
			setNestedBeanPropertyValue(data, "idPessoa", idPessoa);
		}
		
		if (idEnderecoPessoa != undefined){			
			setNestedBeanPropertyValue(data, "idEnderecoPessoa", idEnderecoPessoa);			
		}		
		  
		var sdo = createServiceDataObject("lms.vendas.manterHorariosColetaAutomaticaClienteAction.findEnderecoMunicipioUFComboVigenteFuturo", "enderecoPessoaidEnderecoPessoa", data);
		xmit({serviceDataObjects:[sdo]});		
	}

	/*
	Callback da lookup EndereçoPessoa
	*/
	function enderecoPessoaidEnderecoPessoa_cb(data){
		comboboxLoadOptions({e:document.getElementById("enderecoPessoa.idEnderecoPessoa"), data:data});	
		setElementValue("enderecoPessoa.idEnderecoPessoa",getElementValue("idEnderecoPessoaTmp"));
	}
	
	/*
	* Criada para validar acesso do usuário 
	* logado à filial do cliente
	*/
	function initWindow(eventObj){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
		if (eventObj != undefined){
			if (eventObj.name=='newButton_click'){
				var data = new Array();	   
				var idPessoa = getElementValue("cliente.idCliente");      
				if (idPessoa != undefined){			
					setNestedBeanPropertyValue(data, "idPessoa", idPessoa);
				}
						   
				var sdo = createServiceDataObject("lms.vendas.manterHorariosColetaAutomaticaClienteAction.findEnderecoMunicipioUFComboVigenteFuturo", "enderecoPessoa_idEnderecoPessoa", data);
				xmit({serviceDataObjects:[sdo]});
			}
		}
	}	
	
	/**
	* Esta função seta os dados nos devidos campos conforme a onPageLoad_cb()
	* Seta o label dinâmico de pessoa e depois busca os endereços desta pessoa
	*/
	function myOnPageLoad_cb(data, erro){
	      onPageLoad_cb(data, erro);	      
	      
	      var data = new Array();	   
	      var idPessoa = getElementValue("cliente.idCliente");      
		  if (idPessoa != undefined){			
			setNestedBeanPropertyValue(data, "idPessoa", idPessoa);
		  }
		   
	      var sdo = createServiceDataObject("lms.vendas.manterHorariosColetaAutomaticaClienteAction.findEnderecoMunicipioUFComboVigenteFuturo", "enderecoPessoa_idEnderecoPessoa", data);
		  xmit({serviceDataObjects:[sdo]});
	}
		
</script>