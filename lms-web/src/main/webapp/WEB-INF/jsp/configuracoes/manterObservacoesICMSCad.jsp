<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window>
	<adsm:form action="/configuracoes/manterObservacoesICMS">
		<adsm:textbox dataType="text" property="ie" size="60" width="85%" label="ie" disabled="true"/>
		<adsm:textbox dataType="JTDateTimeZone" property="vigencia" width="85%" label="vigencia"/>
		<adsm:textarea property="observacao" maxLength="255" columns="58" rows="3" label="observacao"  />
		<adsm:buttonBar>
				<adsm:button caption="novo"/>
				<adsm:button caption="salvar"/>
				<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>