<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.manterObservacaoICMSPessoaAction" onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/configuracoes/manterDescricaoTributacaoICMSPessoa" >
	
		<adsm:hidden property="inscricaoEstadual.idInscricaoEstadual"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>
		<adsm:label key="branco" style="width:0"/>
		<td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>
	    <adsm:textbox dataType="text" property="inscricaoEstadual.pessoa.nrIdentificacao" size="20" maxLength="20" disabled="true" width="85%" serializable="false" >
	        <adsm:textbox dataType="text" property="inscricaoEstadual.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox>
	    
		<adsm:textbox label="ie" property="inscricaoEstadual.nrInscricaoEstadual" dataType="text" size="40" width="35%" maxLength="20" disabled="true" serializable="false"/>
		
		<adsm:combobox label="tipo" property="tpObservacaoICMSPessoa" domain="DM_TIPO_OBSERVACAO_ICMS_CLIENTE" width="35%" boxWidth="100"/>
		
		<adsm:textbox label="vigencia" width="85%" dataType="JTDate" property="dtVigencia" size="10" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="observacaoICMSPessoa"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idObservacaoICMSPessoa" property="observacaoICMSPessoa" defaultOrder="dtVigenciaInicial" rows="12">
		<adsm:gridColumn title="tipo" property="tpObservacaoICMSPessoa" isDomain="true" width="90"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="110" dataType="JTDate"/>
        <adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="110" dataType="JTDate"/>
		<adsm:gridColumn title="observacao" property="obObservacaoICMSPessoa" width=""/>
		<adsm:gridColumn title="ordem" property="nrOrdemImpressao" dataType="integer" width="50"/>
	    <adsm:buttonBar> 
	   		<adsm:removeButton/>
	   	</adsm:buttonBar>	
    </adsm:grid>	
    
</adsm:window>
<script>
function myOnPageLoad_cb(data, erro){
	onPageLoad_cb();
	document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
}
</script>
