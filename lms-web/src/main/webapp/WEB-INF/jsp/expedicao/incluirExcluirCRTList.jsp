<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/expedicao/incluirExcluirCRT" >
		<adsm:textbox property="numeroMIC" dataType="text" label="numeroMIC" size="4" maxLength="3" disabled="true">
			<adsm:textbox property="numeroMIC" dataType="text" size="10" maxLength="6" disabled="true"/>
		</adsm:textbox>

		<adsm:buttonBar freeLayout="true">
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" unique="true" >
		<adsm:gridColumn title="filialOrigem" property="filOri" width="15%" />
		<adsm:gridColumn title="filialDestino" property="filDes" width="15%" />
		<adsm:gridColumn title="numeroCRT" property="nro" width="15%" align="right"/>
		<adsm:gridColumn title="dataEmissao" property="data" width="15%" />
		<adsm:gridColumn title="remetente" property="reme" width="20%" />
		<adsm:gridColumn title="destinatario" property="dest" width="20%" />

		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
