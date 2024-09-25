<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.telefoneContatoService" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterTelefonesContatos" idProperty="idTelefoneContato">
		<adsm:label key="branco" style="width='0'"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>
		<adsm:hidden property="idContatoTemp" serializable="false"/>		

 		<adsm:label key="branco" style="width:0"/>
		<td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>
        <adsm:textbox property="telefoneEndereco.pessoa.nrIdentificacao" serializable="false" dataType="integer" size="20" maxLength="20" width="85%" disabled="true">
	        <adsm:textbox property="telefoneEndereco.pessoa.nmPessoa" serializable="false" dataType="text" size="60" disabled="true"/>
        </adsm:textbox>

        <adsm:hidden property="telefoneEndereco.pessoa.idPessoa" serializable="false"/>        
		<adsm:combobox property="contato.idContato" autoLoad="false" optionLabelProperty="nmContato" optionProperty="idContato" service="lms.configuracoes.contatoService.find" label="contato" style="width:230px" required="true"/>
		<adsm:combobox property="telefoneEndereco.idTelefoneEndereco" autoLoad="false" optionLabelProperty="dddTelefone" optionProperty="idTelefoneEndereco" service="lms.configuracoes.telefoneEnderecoService.find" label="telefone" required="true"/>
		<adsm:textbox dataType="text" property="nrRamal" label="ramal" maxLength="10" size="10"/>		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function myOnPageLoad_cb(data, erro){
		onPageLoad_cb();	
		
		document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
		
		addServiceDataObject(createServiceDataObject("lms.configuracoes.contatoService.find", "contato.idContato", {pessoa:{idPessoa:getElementValue('telefoneEndereco.pessoa.idPessoa')}}));
		addServiceDataObject(createServiceDataObject("lms.configuracoes.telefoneEnderecoService.find", "telefoneEndereco.idTelefoneEndereco", {pessoa:{idPessoa:getElementValue('telefoneEndereco.pessoa.idPessoa')}}));
		xmit({onXmitDone:"atribuirValorCombos"});	
	}

	function atribuirValorCombos_cb(){
		setElementValue('contato.idContato',getElementValue('contato.idContato'));
	}
	
	function setaDados(data){
		onDataLoad_cb(data);
	}
</script>