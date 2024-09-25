<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterControleFormularios" type="main">
		<adsm:i18nLabels>
			<adsm:include key="LMS-27034" />
		</adsm:i18nLabels>
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterControleFormularios" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterControleFormularios" cmd="cad" onShow="myOnShow"/>
		</adsm:tabGroup>
</adsm:window>
