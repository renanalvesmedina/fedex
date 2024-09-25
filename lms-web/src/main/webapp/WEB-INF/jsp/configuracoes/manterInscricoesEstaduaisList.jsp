<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.inscricaoEstadualService" onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/configuracoes/manterInscricoesEstaduais"  >
		<adsm:hidden property="pessoa.idPessoa"/>
	    <adsm:hidden property="labelPessoaTemp" serializable="false"/>
	    
 		<adsm:label key="branco" style="width:0"/> 		
		<td colspan="20" id="labelPessoa" class="FmLbRequerido"></td>
		
        <adsm:textbox dataType="text" property="pessoa.nrIdentificacao" size="20" maxLength="20" disabled="true" width="80%" serializable="false" >
	        <adsm:textbox dataType="text" property="pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox>
	    
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="20%" width="30%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="inscricoes"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idInscricaoEstadual"  property="inscricoes" defaultOrder="nrInscricaoEstadual" gridHeight="200" rows="13">
		<adsm:gridColumn width="110" title="uf" property="unidadeFederativa.sgUnidadeFederativa"/>
		<adsm:gridColumn width="150" title="numero" property="nrInscricaoEstadual" />
		<adsm:gridColumn width="150" title="indicadorPadrao" property="blIndicadorPadrao" renderMode="image-check" />
		<adsm:gridColumn width="" title="situacao" property="tpSituacao" isDomain="true" />
	
  	    <adsm:buttonBar>
			<adsm:removeButton />
	    </adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
<script>
function myOnPageLoad_cb(data, erro){
	onPageLoad_cb();
	document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
}
</script>

