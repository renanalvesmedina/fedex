<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.contatoService" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterContatos" idProperty="idContato">
	    <adsm:hidden property="pessoa.idPessoa" />
	    <adsm:hidden property="labelPessoaTemp" serializable="false"/>
 		<adsm:label key="branco" style="width:0"/>
		<td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>
		<adsm:textbox dataType="text" property="pessoa.nrIdentificacao" size="20" maxLength="20" disabled="true" width="85%" serializable="false">
	        <adsm:textbox dataType="text" property="pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox>
		
		<adsm:textbox dataType="text" property="nmContato" label="nome" size="40" maxLength="60" required="true"/>
		<adsm:textbox dataType="email" property="dsEmail" label="email" labelWidth="22%" width="28%" size="30" maxLength="60"/>
		<adsm:textbox dataType="text" property="dsDepartamento" label="departamento" size="40" maxLength="60"/>
		<adsm:textbox dataType="text" property="dsFuncao" label="funcao" size="30" maxLength="60" labelWidth="22%" width="28%"/>
		<adsm:combobox property="tpContato" domain="DM_TIPO_CONTATO_PESSOA" label="tipoContato" required="true"/>
		<adsm:textbox dataType="integer" property="nrRepresentante" label="numeroRepresentante" size="10" maxLength="10" labelWidth="22%" width="28%"/>
		<adsm:textbox dataType="JTDate" property="dtAniversario" label="dataAniversario" picker="false" mask="dd/MM"  width="85%"/>
		<adsm:textarea columns="100" rows="3" maxLength="255" property="obContato" label="observacao"  width="85%"/>
		<adsm:buttonBar>
			<adsm:button caption="telefone" action="/configuracoes/manterTelefonesContatos" cmd="main">
			   <adsm:linkProperty src="pessoa.idPessoa" target="telefoneEndereco.pessoa.idPessoa" />
			   <adsm:linkProperty src="pessoa.nrIdentificacao" target="telefoneEndereco.pessoa.nrIdentificacao" />
			   <adsm:linkProperty src="pessoa.nmPessoa" target="telefoneEndereco.pessoa.nmPessoa" />
			   <adsm:linkProperty src="idContato" target="idContatoTemp" />
			   <adsm:linkProperty src="labelPessoaTemp" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="correspondencias" action="/configuracoes/manterContatoCorrespondencias" cmd="main">
			   <adsm:linkProperty src="pessoa.idPessoa" target="contato.pessoa.idPessoa"/>
			   <adsm:linkProperty src="pessoa.nrIdentificacao" target="contato.pessoa.nrIdentificacao" />
			   <adsm:linkProperty src="pessoa.nmPessoa" target="contato.pessoa.nmPessoa"/>
			   <adsm:linkProperty src="idContato" target="idContatoTemp"/>
			   <adsm:linkProperty src="labelPessoaTemp" target="labelPessoaTemp" />
			</adsm:button>
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
}
</script>
