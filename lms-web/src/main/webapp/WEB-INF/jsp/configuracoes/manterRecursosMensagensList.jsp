<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.recursoMensagemService">
	<adsm:form action="/configuracoes/manterRecursosMensagens" idProperty="idRecursoMensagem">
		<adsm:textbox dataType="text" property="chave" label="chave" size="35" width="85%" maxLength="100"/>
		<adsm:textbox dataType="text" property="texto" label="texto" size="95" width="85%" maxLength="1000"/>
		<adsm:combobox property="idioma" label="idioma" domain="TP_LINGUAGEM" width="85%" boxWidth="300"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="recursoMensagem" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid defaultOrder="texto" idProperty="idRecursoMensagem" property="recursoMensagem" selectionMode="check" unique="true" rows="12">
		<adsm:gridColumn title="chave" property="chave" width="15%" />
		<adsm:gridColumn title="texto" property="texto" />
		<adsm:gridColumn title="idioma" property="idioma" width="10%"/>
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window> 