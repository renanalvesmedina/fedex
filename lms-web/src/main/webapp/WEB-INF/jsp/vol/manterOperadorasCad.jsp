<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<script>
	function myOnPageLoad(){
		onPageLoad();
		
	     initPessoaWidget({ 
	     	 tpTipoElement:document.getElementById("pessoa.tpPessoa"),
	         tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"),
	         numberElement:document.getElementById("pessoa.idPessoa") }
	     );		
	     
	     changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), 
	     	tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), 
	     	numberElement:document.getElementById('pessoa.nrIdentificacao'), 
	     	tabCmd:'cad'}
	     );
	}
	function initWindow(eventObj) {
		var event = eventObj.name;
		if (event == "gridRow_click"){
			setDisabled("pessoa.tpIdentificacao",true);
			setDisabled("pessoa.nrIdentificacao",true);							
		} else {
			setDisabled("pessoa.tpIdentificacao",false);
			setDisabled("pessoa.nrIdentificacao",false);					
		}
		if (event!='storeButton')
			setFocusOnFirstFocusableField(document);			
	}
	function myDataLoadCallBack_cb(data, exception){
		onDataLoad_cb(data, exception);		
		onDataLoadCallbackPessoaWidget({
			tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), 
			numberElement:document.getElementById("pessoa.nrIdentificacao")}
		);	
		
		setDisabled("pessoa.tpIdentificacao",true);
		setDisabled("pessoa.nrIdentificacao",true);
		setFocusOnFirstFocusableField(document);
		
	}
</script>
<adsm:window service="lms.vol.manterOperadorasAction" onPageLoad="myOnPageLoad" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/vol/manterOperadoras" idProperty="idOperadora" onDataLoadCallBack="myDataLoadCallBack">
		<!-- onPageLoadCallBack="myPageLoad" -->
		<adsm:hidden property="labelOperadora"  serializable="false"/>
		<adsm:i18nLabels>
			<adsm:include key="fabricante"/>
		</adsm:i18nLabels>
	
		<adsm:complement label="identificacao" required="true" labelWidth="18%" width="82%">
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad"/>
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" 
					onDataLoadCallBack="pessoa_nrIdentificacao"
					service="lms.vol.manterOperadorasAction.findPessoa"/>
		</adsm:complement>
		
				<!-- Atributos para salvar/atualizar em Pessoa -->
		<adsm:hidden property="pessoa.tpPessoa" value="J"/>
				
		<adsm:textbox dataType="text" size="70" labelWidth="18%" width="82%"
			property="pessoa.nmPessoa" label="nome" maxLength="50" required="true"/>
		
		<adsm:combobox required="true" property="tpSituacao" domain="DM_STATUS_PESSOA" 
			label="situacao" labelWidth="18%" width="32%" />	
				
		<adsm:textbox dataType="email" property="pessoa.dsEmail" label="email" 
			size="70" maxLength="60" labelWidth="18%" width="82%" />		

		<adsm:hidden property="labelFabricante" serializable="false"/>
	
		<%--
		<adsm:lookup service="lms.vol.manterFabricantesAction.findLookupContatos" dataType="text" property="contato" 
				idProperty="idContato" criteriaProperty="nmContato" label="contato" size="40" maxLength="40" required="false"
				action="/configuracoes/manterContatos" width="82%" exactMatch="true" labelWidth="18%"/>
		--%>		
	
		<%--	
		<adsm:textbox dataType="text" property="dsUrlSms" label="urlSMS" width="35%" size="30" maxLength="255" required="true" labelWidth="18%" />
		<adsm:textarea property="dsCorpoSms" label="corpoSMS" width="60%" maxLength="255" columns="40" rows="3" labelWidth="18%" />
		<adsm:textbox dataType="text" property="dsUsuario" label="usuario" width="35%" size="30" maxLength="10" labelWidth="18%" />
		<adsm:textbox dataType="password" property="dsSenha" label="senha" width="35%" size="30" maxLength="10" labelWidth="18%" />
		--%>
						
		<adsm:buttonBar>
		
			<adsm:button caption="telefones" action="/configuracoes/manterTelefonesPessoa" cmd="main">
				<adsm:linkProperty src="idOperadora" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelOperadora" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="contatos" action="/configuracoes/manterContatos" cmd="main">
				<adsm:linkProperty src="idOperadora" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelOperadora" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="enderecos" action="/configuracoes/manterEnderecoPessoa" cmd="main">
				<adsm:linkProperty src="idOperadora" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelOperadora" target="labelPessoaTemp" />
			</adsm:button>		
			<adsm:button id="cadEquipamentos" caption="equipamentos" action="/vol/manterEquipamentos" cmd="main" disabled="false" boxWidth="150">
				<adsm:linkProperty src="idOperadora" target="volOperadorasTelefonia.idOperadora" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="volOperadorasTelefonia.pessoa.nmPessoa" />	
						
			</adsm:button> 
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
		<script>			
			document.getElementById("labelOperadora").masterLink = "true";		    
		    function myOnPageLoad_cb(data, errorMessage, errorMsg, eventObj){
			    onPageLoad_cb(data, errorMessage, errorMsg, eventObj);
		   	    var labelTemp = '<adsm:label key="operadora"/>';
		  	  	setElementValue("labelOperadora",labelTemp);  		  	  	
		  	}
	function pessoa_nrIdentificacao_cb(data, erro){
		//Erro do Service
		if (erro != undefined){
			alert(erro);
			cleanButtonScript(this.document);
			return false;
		}

		// Se Pessoa cadastrada
		if (data != undefined && data.length == 1){	
			if(data[0].idPessoa != undefined && data[0].idPessoa != ""){
				setElementValue("pessoa.idPessoa",data[0].idPessoa);
			}else{
				setElementValue("pessoa.idPessoa","");
			}
			
			if(data[0].nmPessoa != undefined && data[0].nmPessoa != ""){
				setElementValue("pessoa.nmPessoa",data[0].nmPessoa);
			}else{
				setElementValue("pessoa.nmPessoa","");
			}
			
			if(data[0].dsEmail != undefined && data[0].dsEmail != ""){
				setElementValue("pessoa.dsEmail",data[0].dsEmail);
			}else{
				setElementValue("pessoa.dsEmail","");
			}			
		}
	
	}
		</script>
	</adsm:form>
</adsm:window>
