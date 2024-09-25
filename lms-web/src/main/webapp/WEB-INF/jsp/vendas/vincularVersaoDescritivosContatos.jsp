<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/vincularVersaoDescritivos">
		<adsm:masterLink>
			<adsm:masterLinkItem property="cliente" label="cliente" />
			<adsm:masterLinkItem property="numeroVersao" label="versao" />
			<adsm:masterLinkItem property="processo" label="processo" />
			<adsm:masterLinkItem property="evento" label="evento" />
			<adsm:masterLinkItem property="ocorrencia" label="ocorrencia" />
			<adsm:masterLinkItem property="codigoDescritivo" label="codigoDescritivo" />
		</adsm:masterLink>
		<adsm:combobox property="contato" optionLabelProperty="label" optionProperty="1" service="" label="contato" prototypeValue="" prototypeValue="" required="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novoContato"/>
			<adsm:button caption="salvarContato"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="200" unique="true">
		<adsm:gridColumn title="contato" property="contato" width="100%" />
		<adsm:buttonBar>
			<adsm:button caption="excluirContato"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
