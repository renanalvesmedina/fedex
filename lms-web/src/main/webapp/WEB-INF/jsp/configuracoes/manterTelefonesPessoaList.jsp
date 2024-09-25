<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.telefoneEnderecoService" onPageLoadCallBack="myOnPageLoad" >
	<adsm:form action="/configuracoes/manterTelefonesPessoa">
		
		<adsm:hidden property="pessoa.idPessoa"/>
		<adsm:hidden property="isTelaLocMerc"/>
		
		<adsm:label key="branco" style="width:0"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>		
        <td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>

        <adsm:textbox property="pessoa.nrIdentificacao" serializable="false" dataType="text" size="20" maxLength="20" width="16%" disabled="true">
                <adsm:textbox property="pessoa.nmPessoa" serializable="false" dataType="text" size="60" width="69%" disabled="true"/>
        </adsm:textbox>
        
        <adsm:hidden property="idEnderecoPessoaTmp" serializable="false"/>
		
        <adsm:combobox property="enderecoPessoa.idEnderecoPessoa" boxWidth="442" width="85%" autoLoad="false"
        			   optionLabelProperty="enderecoCompleto" optionProperty="idEnderecoPessoa"
                       service="lms.configuracoes.enderecoPessoaService.findEnderecoCompletoCombo" label="endereco">
		</adsm:combobox>	                       
                       
        <adsm:combobox property="tpTelefone" label="tipoTelefone" domain="DM_TIPO_TELEFONE"/>		
		<adsm:combobox property="tpUso" label="usoTelefone" domain="DM_USO_TELEFONE"/>
		
        <adsm:textbox label="ddi" dataType="integer" property="nrDdi" maxLength="5" size="5" minValue="0"/>
        
		<adsm:complement label="numero" width="35%">
            <adsm:textbox dataType="integer" property="nrDdd" maxLength="5" size="5" minValue="0"/>
        	<adsm:textbox dataType="text" property="nrTelefone" maxLength="10" size="10" minValue="0"/>
        </adsm:complement>               	
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="telefoneEndereco"/>
			<adsm:button id="btnLimpar" onclick="limpaCampos()" caption="limpar" />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTelefoneEndereco" property="telefoneEndereco" rows="11" onDataLoadCallBack="callBackGrid" onRowClick="rowClickTelefone();">
		<adsm:gridColumn width="33%" title="tipoTelefone" property="tpTelefone" isDomain="true" align="left"/>
		<adsm:gridColumn width="33%" title="usoTelefone" property="tpUso" isDomain="true" align="left"/>
        <adsm:gridColumn width="10%" title="ddi" property="nrDdi" align="right"/>
		<adsm:gridColumn width="24%" title="numero" property="dddTelefone"/>
		<adsm:buttonBar>
			<% if (!"modal".equals(request.getParameter("mode"))) { %>
				<adsm:removeButton/>
			<% } else { %>
				<adsm:button id="botaoFechar" caption="fechar" onclick="self.close();" disabled="false"/>
			<% } %>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
<script>
	
	function rowClickTelefone(id){
		if(getElementValue("isTelaLocMerc") != ""){
			return false;
		}else{
			return true;
		}
	}

	function callBackGrid_cb(data,errorMessage){
 	   if(getElementValue("isTelaLocMerc") != ""){
 		    telefoneEnderecoGridDef.disabled = true;
 		    setDisabled(document.getElementById("telefoneEndereco"+".form"),true);
 		    setDisabled(document.getElementById("telefoneEndereco"+".chkSelectAll"),true);
 		    setDisabled("botaoFechar", false);
 	   	
 	   }     
   }

	function findDados() {

		var u = new URL(parent.location.href);
		
		var idEnderecoPessoa = u.parameters["idEnderecoPessoa"];
		var isTelaLocMerc = u.parameters["isTelaLocMerc"];
		var nrIdentificacao  = u.parameters["nrIdentificacao"];
   		var nmPessoa = u.parameters["nmPessoa"];
   		var idPessoa = u.parameters["pessoa.idPessoa"];
   		var tipoCliente = u.parameters["tipoCliente"];
   		var idCliente = u.parameters["idCliente"];
   	
   		if(idPessoa != null)	{
			setElementValue("pessoa.idPessoa",idPessoa);
			document.getElementById("pessoa.idPessoa").masterLink="true";
		}	
   	   	
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
			setElementValue("idEnderecoPessoaTmp", idEnderecoPessoa);
			document.getElementById("idEnderecoPessoaTmp").masterLink="true";
			//setDisabled("enderecoPessoa.idEnderecoPessoa", true);
		}
		if(isTelaLocMerc != null){
			setElementValue("isTelaLocMerc", isTelaLocMerc);
			document.getElementById("isTelaLocMerc").masterLink="true";
			setDisabled(document.getElementById("telefoneEndereco"+".form"),true);
 		    setDisabled(document.getElementById("telefoneEndereco"+".chkSelectAll"),true);
 		    setDisabled("botaoFechar", false);
		}
			
		
	}	

	function initWindow(eventObj) {
    	if ( eventObj.name != undefined  ){
	    	setDisabled("btnLimpar",false);
	    }	
    }

	function limpaCampos(){

		var idPessoa = getElementValue("pessoa.idPessoa");      
		var idEnderecoPessoa = getElementValue("idEnderecoPessoaTmp");
		
		cleanButtonScript();
		
		if(getElementValue("isTelaLocMerc") != ""){
			setDisabled("botaoFechar", false);
		}
		
		if( idEnderecoPessoa != "" ){
			setElementValue("enderecoPessoa.idEnderecoPessoa",idEnderecoPessoa);		
			setElementValue("pessoa.idPessoa",idPessoa);
			setDisabled("enderecoPessoa.idEnderecoPessoa",true);
		}
	}

	//if( document.getElementById("idEnderecoPessoaTmp") != '' && document.getElementById("idEnderecoPessoaTmp") != undefined ){
	//	document.getElementById("idEnderecoPessoaTmp").masterLink = "true";
	//}
	
	/**
	* Esta função seta os dados nos devidos campos conforme a onPageLoad_cb()
	* Seta o label dinâmico de pessoa e depois busca os endereços desta pessoa
	*/
	function myOnPageLoad_cb(data, erro){
	      
	      onPageLoad_cb();
	      
	      findDados();
	        
	      document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
	      
	      var data = new Array();	   
	      var idPessoa = getElementValue("pessoa.idPessoa");      
	      var idEnderecoPessoa = getElementValue("idEnderecoPessoaTmp");
	      
		
	      
		  if (idPessoa != undefined){
			setNestedBeanPropertyValue(data, "pessoa.idPessoa", idPessoa);
		  }
		  
		   
	      var sdo = createServiceDataObject("lms.configuracoes.enderecoPessoaService.findEnderecoCompletoCombo", "myEnderecoPessoa", data);
		  xmit({serviceDataObjects:[sdo]});
	
	}
	
	/**
	* Função de Retorno do método de busca da combo de endereços da pessoa
	* Se existe um endereço passado por parâmetro então seta na combo após a carga dos endereços.
	*/
	function myEnderecoPessoa_cb(data,erro){
		enderecoPessoa_idEnderecoPessoa_cb(data,erro);
		
		if( getElementValue("idEnderecoPessoaTmp") != "" ){
			setElementValue("enderecoPessoa.idEnderecoPessoa",getElementValue("idEnderecoPessoaTmp"));		
			setDisabled("enderecoPessoa.idEnderecoPessoa",true);
		}
		
		if(getElementValue("isTelaLocMerc")!= null){
			setDisabled("btnLimpar",false);
		}
		
	}
</script>
