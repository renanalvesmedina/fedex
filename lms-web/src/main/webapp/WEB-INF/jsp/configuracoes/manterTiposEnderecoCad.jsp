<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.tipoEnderecoPessoaService" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterTiposEndereco" idProperty="idTipoEnderecoPessoa" onDataLoadCallBack="myDataLoad">

		<adsm:hidden property="enderecoPessoa.pessoa.idPessoa"/>
		<adsm:label key="branco" style="width:0"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>		
        <td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>

        <adsm:textbox property="enderecoPessoa.pessoa.nrIdentificacaoFormatado" serializable="false" dataType="text" size="20" maxLength="20" width="13%" disabled="true">
                <adsm:textbox property="enderecoPessoa.pessoa.nmPessoa" serializable="false" dataType="text" size="60" width="74%" disabled="true"/>
        </adsm:textbox> 

		<adsm:hidden property="enderecoPessoa.idEnderecoPessoa"/>
		<adsm:textbox property="enderecoPessoa.enderecoCompleto" dataType="text" label="endereco" size="100" serializable="false" disabled="true" width="85%"/>

		<adsm:combobox property="tpEndereco" 
					   optionProperty="value" 
					   autoLoad="false"
					   optionLabelProperty="description"
					   label="tipoEndereco" 
					   service="lms.configuracoes.tipoEnderecoPessoaService.findTipoEnderecoPessoa" 
					   required="true">
					   
	 	</adsm:combobox>

		<adsm:buttonBar>
				<adsm:storeButton/>
				<adsm:newButton/>
				<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function myDataLoad_cb(dados, erros) {
		onDataLoad_cb(dados);
		validateTpEndereco(dados.tpEndereco.value);
	}
	
	function validateTpEndereco(tpEndereco){
		if (tpEndereco == "COM" || tpEndereco == "RES") {
			setDisabled("tpEndereco", true);
		} else {
			setDisabled("tpEndereco", false);
		}
	}
	
	function myOnPageLoad_cb(dados, erros){
	  onPageLoad_cb(dados,erros);
      document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
     
      var dados = new Array();
	  setNestedBeanPropertyValue(dados, "idPessoa", getElementValue("enderecoPessoa.pessoa.idPessoa"));
	  var sdo = createServiceDataObject("lms.configuracoes.tipoEnderecoPessoaService.findTipoEnderecoPessoa",
	                                             "tpEndereco",
	                                             dados);
 	  xmit({serviceDataObjects:[sdo]});
	 
	}

	function initWindow(event){
		validateTpEndereco(getElementValue("tpEndereco"));
	}
	
	function tpEnderecoPessoa_onLoad(oldFunction){
		oldFunction();
	}
	
</script>
