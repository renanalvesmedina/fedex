<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.contatoService" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterContatos" idProperty="idContato">
	    <adsm:hidden property="pessoa.idPessoa"/>
	    <adsm:hidden property="labelPessoaTemp" serializable="false"/>
 		<adsm:label key="branco" style="width:0"/>
		<td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>
        <adsm:textbox dataType="text" property="pessoa.nrIdentificacao" size="20" maxLength="20" disabled="true" width="85%" serializable="false" >
	        <adsm:textbox dataType="text" property="pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox>
	    
		<adsm:textbox dataType="text" property="nmContato" label="nome" size="40" maxLength="60" />
		<adsm:textbox dataType="text" property="dsEmail" label="email" labelWidth="22%" width="28%" size="30" maxLength="60"/>
		<adsm:textbox dataType="text" property="dsDepartamento" label="departamento" size="40" maxLength="60"/>
		<adsm:textbox dataType="text" property="dsFuncao" label="funcao" size="30" maxLength="60" labelWidth="22%" width="28%" />
		<adsm:combobox property="tpContato" domain="DM_TIPO_CONTATO_PESSOA" label="tipoContato" />
		<adsm:textbox dataType="integer" property="nrRepresentante" label="numeroRepresentante" size="10" maxLength="10" labelWidth="22%" width="28%"/>
		<adsm:textbox dataType="JTDate" property="dtAniversario" label="dataAniversario" mask="dd/MM" picker="false"  width="85%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="contatos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idContato" defaultOrder="nmContato" property="contatos" selectionMode="check" >
		<adsm:gridColumn width="20%" title="nome" property="nmContato" />
		<adsm:gridColumn width="20%" title="email" property="dsEmail" />
		<adsm:gridColumn width="20%" title="funcao" property="dsFuncao" />
		<adsm:gridColumn width="20%" title="departamento" property="dsDepartamento"/>
		<adsm:gridColumn width="20%" title="tipoContato" property="tpContato" isDomain="true"/>
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
