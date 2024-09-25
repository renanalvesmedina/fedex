<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.parametroGeralService">
	<adsm:form action="/configuracoes/manterParametrosGerais" idProperty="idParametroGeral">
		<adsm:textbox dataType="text" property="nmParametroGeral" label="nome" maxLength="60" required="true" size="60" width="85%"/>
		<adsm:textbox dataType="text" size="60" width="85%" property="dsParametro" label="descricao" maxLength="60" required="true"/>
		<adsm:textarea size="90" width="85%" property="dsConteudo" rows="4" label="conteudo" maxLength="200" required="true"/>
		<adsm:textbox dataType="integer" property="nrTamanho" minValue="1" label="tamanho" maxLength="4" size="2" required="true"/>
		<adsm:combobox property="tpFormato" label="formato" domain="DM_FORMATO_PARAMETRO" required="true" />
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>