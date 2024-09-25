<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterObservacoesICMSPessoa" type="main">
		<adsm:tabGroup selectedTab="0" >
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterDescricaoTributacaoICMSPessoa.do" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterDescricaoTributacaoICMSPessoa.do" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>