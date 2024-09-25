<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.recursoMensagemService">
	<adsm:form idProperty="idRecursoMensagem" action="/configuracoes/manterRecursosMensagens">
		<adsm:textbox dataType="text" property="chave" label="chave" size="35" width="85%" required="true" maxLength="100"/>
		<adsm:textbox dataType="text" property="texto" label="texto" size="95" width="85%" required="true" maxLength="1000"/>
		<adsm:combobox property="idioma" label="idioma" domain="TP_LINGUAGEM" width="85%" required="true" boxWidth="300" onlyActiveValues="true"/>
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   