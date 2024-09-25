<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterBeneficiariosFreteCarreteiro" service="lms.contratacaoveiculos.manterBeneficiariosFreteCarreteiroAction" onPageLoadCallBack="beneficiario_pageLoad" onPageLoad="pageLoadBeneficiario">

	<adsm:form action="/contratacaoVeiculos/manterBeneficiariosFreteCarreteiro" idProperty="idBeneficiarioProprietario">
		
		<adsm:hidden property="proprietario.pessoa.tpPessoa" serializable="false"/>
		<adsm:hidden property="proprietario.pessoa.tpIdentificacao" serializable="false"/>
		<adsm:hidden property="proprietario.idProprietario" />
		
		<adsm:complement width="77%" labelWidth="17%" label="proprietario">
	        <adsm:textbox label="proprietario" dataType="text" property="proprietario.pessoa.nrIdentificacao" serializable="false" size="20" disabled="true" width="20%"/>
    		<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="50" width="40%" serializable="false" disabled="true"/>
        </adsm:complement>
			 
		<adsm:combobox definition="TIPO_PESSOA.list" property="beneficiario.pessoa.tpPessoa" required="false" labelWidth="17%" width="33%" label="tipoPessoa" 
					   onchange="return changeTypePessoaWidget({tpTipoElement:this, tpIdentificacaoElement:document.getElementById('beneficiario.pessoa.tpIdentificacao'), numberElement:document.getElementById('beneficiario.pessoa.nrIdentificacao'), tabCmd:'list'})"/>		
		<adsm:complement width="33%" labelWidth="17%" label="identificacao">
				<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list" property="beneficiario.pessoa.tpIdentificacao" 
				onchange="return changeIdentificationTypePessoaWidget({tpIdentificacaoElement:document.getElementById('beneficiario.pessoa.tpIdentificacao'), numberElement:document.getElementById('beneficiario.pessoa.nrIdentificacao'), tabCmd:'list'})" />
	       		<adsm:textbox definition="IDENTIFICACAO_PESSOA" property="beneficiario.pessoa.nrIdentificacao"/>
        </adsm:complement>
        
		<adsm:textbox dataType="text" labelWidth="17%" width="33%" label="nome" property="beneficiario.pessoa.nmPessoa"  size="40" maxLength="50"/>	

		<adsm:range label="vigencia" labelWidth="17%" width="33%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/> 
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="beneficiarioProprietario"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		 
	</adsm:form> 
	 
	<adsm:grid idProperty="idBeneficiarioProprietario" defaultOrder="beneficiario_pessoa_.nmPessoa, dtVigenciaInicial"
			property="beneficiarioProprietario" unique="true" rows="12" >
		<adsm:gridColumn title="identificacao" property="beneficiario.pessoa.tpIdentificacao" isDomain="true" width="60" align="left" />		
		<adsm:gridColumn title="" property="beneficiario.pessoa.nrIdentificacaoFormatado" width="130" align="right" />
		<adsm:gridColumn title="nome" property="beneficiario.pessoa.nmPessoa"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="90"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="90" align="center"/>
		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
	
</adsm:window>
<script>
 
 	isLookup = window.dialogArguments && window.dialogArguments.window;
	if (isLookup) {
		document.getElementById('proprietario.pessoa.nmPessoa').required = "false";
	} else {
		document.getElementById('proprietario.pessoa.nmPessoa').required = "true";
	}
 
	function initWindow(event){
		if (event.name == 'cleanButton_click')
			document.getElementById("beneficiario.pessoa.nrIdentificacao").dataType = "text";	
	}

	function beneficiario_pageLoad_cb(data){
		onPageLoad_cb(data);
		var tpPessoa = document.getElementById("proprietario.pessoa.tpPessoa").value;
		var tpId = document.getElementById("proprietario.pessoa.tpIdentificacao").value;
		if (tpId == "C" ) {			
			if (tpPessoa == "F") {
				document.getElementById("proprietario.pessoa.nrIdentificacao").dataType = "CPF";					
			} else if (tpPessoa == "J") {
				document.getElementById("proprietario.pessoa.nrIdentificacao").dataType = "CNPJ";
			}
		} else {
			document.getElementById("proprietario.pessoa.nrIdentificacao").dataType = "text";
		}
	}
	
	function pageLoadBeneficiario() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("beneficiario.pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("beneficiario.pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("beneficiario.pessoa.nrIdentificacao")});
	}
</script>