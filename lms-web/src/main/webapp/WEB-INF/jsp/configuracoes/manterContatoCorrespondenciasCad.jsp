<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.contatoCorrespondenciaService" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterContatoCorrespondencias" idProperty="idContatoCorrespondencia">
		<adsm:label key="branco" style="width='0'"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>
		<adsm:hidden property="idContatoTemp" serializable="false"/>		
		<td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>
		
        <adsm:textbox dataType="text" property="contato.pessoa.nrIdentificacao" size="20" maxLength="20" disabled="true" width="85%" serializable="false" >
	        <adsm:textbox dataType="text" property="contato.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox>
	                 
        <adsm:hidden property="contato.pessoa.idPessoa" serializable="false"/>        
		<adsm:combobox property="contato.idContato" autoLoad="false" optionLabelProperty="nmContato" optionProperty="idContato" service="lms.configuracoes.contatoService.find" label="contato" required="true" boxWidth="230"/>
		<adsm:combobox property="tpCorrespondencia" label="tipoCorrespondencia" domain="DM_TIPO_CORRESPONDENCIA" labelWidth="20%" width="20%" required="true"/>		
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
	
	addServiceDataObject(createServiceDataObject("lms.configuracoes.contatoService.find", "contato.idContato", {pessoa:{idPessoa:getElementValue('contato.pessoa.idPessoa')}}));
	xmit({onXmitDone:"atribuirValorCombos"});	
}

function atribuirValorCombos_cb(){
	setElementValue('contato.idContato',getElementValue('idContatoTemp'));
}
</script>