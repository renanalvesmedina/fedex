<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript">


function populaDsGrupoClassificacao() {
    var e = document.getElementById("grupoClassificacao.idGrupoClassificacao");
    var dsGrupoClassificacao = e.options[e.selectedIndex].text;
    setElementValue("dsGrupoClassificacao", dsGrupoClassificacao);
}

function divisoesGrupoClassificacaoCadDataLoad_cb(data, error) {
	onDataLoad_cb(data, error);
	var dsTpSituacaoGrupo = getNestedBeanPropertyValue(data,"grupoClassificacao.tpSituacao.description");
	var dsTpSituacaoDivisao = getNestedBeanPropertyValue(data,"tpSituacao.description");
    setElementValue("dsTpSituacaoGrupo", dsTpSituacaoGrupo);
    setElementValue("dsTpSituacaoDivisao", dsTpSituacaoDivisao);
	populaDsGrupoClassificacao();
}

</script>
<adsm:window service="lms.municipios.divisaoGrupoClassificacaoService">
	<adsm:form action="/municipios/manterDivisoesGrupoClassificacao" idProperty="idDivisaoGrupoClassificacao" onDataLoadCallBack="divisoesGrupoClassificacaoCadDataLoad">
				
		<adsm:combobox property="grupoClassificacao.idGrupoClassificacao" label="grupoClassificacao" service="lms.municipios.grupoClassificacaoService.find" optionLabelProperty="dsGrupoClassificacao" optionProperty="idGrupoClassificacao" labelWidth="18%" width="32%" required="true" boxWidth="220" >
		   <adsm:propertyMapping relatedProperty="dsTpSituacaoGrupo" modelProperty="tpSituacao.description"/>
		</adsm:combobox>
		<adsm:textbox label="situacao" dataType="text" property="dsTpSituacaoGrupo" serializable="false" disabled="true"/>
		
		<adsm:hidden property="dsGrupoClassificacao" serializable="false"/>

		<adsm:textbox dataType="text" label="descricao" property="dsDivisaoGrupoClassificacao" size="38" maxLength="60" disabled="false" width="32%" labelWidth="18%" required="true" />		
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true" labelWidth="18%" width="82%">
			<adsm:propertyMapping relatedProperty="dsTpSituacaoDivisao" modelProperty="description"/>
		</adsm:combobox>
		<adsm:hidden property="dsTpSituacaoDivisao" serializable="false"/>
		
		<adsm:buttonBar>
			<adsm:button caption="filiaisDivisao" action="/municipios/manterGruposClassificacaoFilial" cmd="main">
				<adsm:linkProperty src="grupoClassificacao.idGrupoClassificacao, dsGrupoClassificacao" target="divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao" />
				<adsm:linkProperty src="dsTpSituacaoDivisao" target="dsTpSituacaoDivisao"/>
				<adsm:linkProperty src="dsTpSituacaoGrupo" target="dsTpSituacaoGrupo"/>
				<adsm:linkProperty src="idDivisaoGrupoClassificacao" target="idDivisaoGrupoClassificacaoTemp" />
				<adsm:linkProperty src="dsDivisaoGrupoClassificacao" target="dsDivisaoGrupoClassificacaoTemp" />
			</adsm:button>
			<adsm:storeButton/> 
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>   
