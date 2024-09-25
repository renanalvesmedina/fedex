<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMemorandosInternosResposta" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="entrega/manterMemorandosInternosResposta" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="entrega/manterMemorandosInternosResposta" cmd="cad"/>
			<adsm:tab title="reembolsos" id="reem" src="entrega/manterMemorandosInternosResposta" cmd="reem"
					onShow="tabShowCustom" copyMasterTabProperties="true" masterTabId="cad" disabled="true" />
			<adsm:tab title="comprovantes" id="comp" src="entrega/manterMemorandosInternosResposta" cmd="comp"
					onShow="tabShowCustom" copyMasterTabProperties="true" masterTabId="cad" disabled="true" />
		</adsm:tabGroup>
</adsm:window>
