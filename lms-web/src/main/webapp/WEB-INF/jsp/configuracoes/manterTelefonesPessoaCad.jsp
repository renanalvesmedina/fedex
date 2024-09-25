<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.manterTelefonesAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterTelefonesPessoa" idProperty="idTelefoneEndereco" onDataLoadCallBack="desabilitaBotoes">
		<adsm:hidden property="flagAbaIntegrantes"/>
		<adsm:hidden property="pessoa.idPessoa"/>
		<adsm:label key="branco" style="width:0"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>		
        <td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>

        <adsm:textbox property="pessoa.nrIdentificacao" serializable="false" dataType="text" size="20" maxLength="20" width="16%" disabled="true">
                <adsm:textbox property="pessoa.nmPessoa" serializable="false" dataType="text" size="60" width="69%" disabled="true"/>
        </adsm:textbox>
		
        <adsm:combobox property="enderecoPessoa.idEnderecoPessoa" boxWidth="442" width="85%" autoLoad="false"
					   optionLabelProperty="enderecoCompleto" optionProperty="idEnderecoPessoa"
		               service="lms.configuracoes.enderecoPessoaService.findEnderecoCompletoCombo" 
		               onDataLoadCallBack="myEnderecoPessoa"
		               label="endereco" required="false">
		</adsm:combobox>		               
        
		<adsm:combobox property="tpTelefone" label="tipoTelefone" domain="DM_TIPO_TELEFONE" required="true"/>		
		<adsm:combobox property="tpUso" label="usoTelefone" domain="DM_USO_TELEFONE" required="true"/>
		
        <adsm:textbox label="ddi" dataType="integer" property="nrDdi" maxLength="5" size="5" minValue="0"/>
        
		<adsm:complement label="numero" width="35%">
            <adsm:textbox dataType="integer" property="nrDdd" maxLength="5" size="5" required="true" minValue="0"/>
        	<adsm:textbox dataType="text" property="nrTelefone" maxLength="10" size="10" required="true" minValue="0"/>
        </adsm:complement>

		<adsm:buttonBar>
			<adsm:button id="contatos" caption="contatos" action="/configuracoes/manterContatos" cmd="main">
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa"/>
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoaTemp" target="labelPessoaTemp"/>
			</adsm:button>
			<adsm:button caption="salvar" id="btnSalvar" buttonType="storeButton" 
			             service="lms.configuracoes.manterTelefonesAction.store" 
			             callbackProperty="myRetornoBtnSalvar" disabled="false"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function myRetornoBtnSalvar_cb(data, error){
		store_cb(data,error);
		if( error != undefined ){
			setFocus('enderecoPessoa.idEnderecoPessoa');
		}
	}

	function desabilitaBotoes_cb(data, exception){
		onDataLoad_cb(data, exception);
		findDados();
		
		
	}
	
	function findDados() {

		var u = new URL(parent.location.href);
		
		var idEnderecoPessoa = u.parameters["idEnderecoPessoa"];
		var flagAbaIntegrantes = u.parameters["flagAbaIntegrantes"];
		var nrIdentificacao  = u.parameters["nrIdentificacao"];
   		var nmPessoa = u.parameters["nmPessoa"];
   		var tipoCliente = u.parameters["tipoCliente"];
   		var idCliente = u.parameters["idCliente"];
   	
	   	if(nrIdentificacao!= null){
			setElementValue("pessoa.nrIdentificacao",nrIdentificacao);
			document.getElementById("pessoa.nrIdentificacao").masterLink="true";
		}
			
	    if(nmPessoa!= null){
	    	setElementValue("pessoa.nmPessoa",nmPessoa);
	    	document.getElementById("pessoa.nmPessoa").masterLink="true";
	    }
	    	
	    if(tipoCliente != null)	{
			setElementValue("labelPessoaTemp",tipoCliente);
			document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
		}	
		
		 if(idCliente != null)	{
			setElementValue("pessoa.idPessoa",idCliente);
			document.getElementById("pessoa.idPessoa").masterLink="true";
		}	
		
		if(idEnderecoPessoa != null){
			setElementValue("enderecoPessoa.idEnderecoPessoa", idEnderecoPessoa);
			document.getElementById("enderecoPessoa.idEnderecoPessoa").masterLink="true";
			
		}	
		
		if(flagAbaIntegrantes != null){
			setElementValue("flagAbaIntegrantes", flagAbaIntegrantes);
        	document.getElementById("flagAbaIntegrantes").masterLink="true";
       	}
       	
       	if(getElementValue("flagAbaIntegrantes") != ""){
				setDisabled(document, true);
				setDisabled("btnSalvar",true);
	        	setDisabled("__buttonBar:0.removeButton",true);
	        	setDisabled("__buttonBar:0.newButton",true);
	        	setDisabled("contatos",true);
		}	
       	
       
		
	}	
	
	/**
	* Ao abrir a aba de detalhamento é necessário carregar os endereços da
	* pessoa em questão.
	*/
	function initWindow(eventObj){
		
		if(eventObj.name != 'gridRow_click' && eventObj.name != 'storeButton'){
			findDados();
			var data = new Array();	   
		    var idPessoa = getElementValue("pessoa.idPessoa");      
			  
			if (idPessoa != undefined){				
				setNestedBeanPropertyValue(data, "pessoa.idPessoa", idPessoa);
			}
			   
		    var sdo = createServiceDataObject("lms.configuracoes.enderecoPessoaService.findEnderecoCompletoCombo", "myEnderecoPessoa", data);
			xmit({serviceDataObjects:[sdo]});	
		}
		
		
	}
	
	/**
	* Função de Retorno do método de busca da combo de endereços da pessoa
	* Se existe um endereço passado por parâmetro então seta na combo após a carga dos endereços.
	*/
	function myEnderecoPessoa_cb(data,erro){
		
		enderecoPessoa_idEnderecoPessoa_cb(data,erro);
		
		
		var idEnderecoPessoaTmp = getTabGroup(document).getTab("pesq").getElementById("idEnderecoPessoaTmp");
		
		if( idEnderecoPessoaTmp != undefined && idEnderecoPessoaTmp.value != '' ){
			setElementValue("enderecoPessoa.idEnderecoPessoa",idEnderecoPessoaTmp.value);		
			setDisabled("enderecoPessoa.idEnderecoPessoa",true);
		}
		
				
	}
	
	/**
	* Esta função seta os dados nos devidos campos conforme a onPageLoad_cb()
	* Seta o label dinâmico de pessoa e depois busca os endereços desta pessoa
	*/
	function myOnPageLoad_cb(data, erro){
	      onPageLoad_cb();
	      document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
	      
	}
</script>