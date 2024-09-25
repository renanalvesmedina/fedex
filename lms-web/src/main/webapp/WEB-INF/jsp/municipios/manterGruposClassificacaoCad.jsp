<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
//funcao chamada no callback do form e do store
  function grupoClassificacaoOnDataLoad_cb(data,exception){
    onDataLoad_cb(data,exception);
    setaDsTpSituacao(data, exception);
    setFocusOnFirstFocusableField(document);
  }
 
 function setaDsTpSituacao(data, exception){
 	var dsTpSituacaoZona = getNestedBeanPropertyValue(data,"tpSituacao.value");
    if (dsTpSituacaoZona == 'I'){
    	document.getElementById("dsTpSituacaoGrupo").value = "Inativo";
    }else{
    	document.getElementById("dsTpSituacaoGrupo").value = "Ativo";
    }
 }
 
 function myStoreButton_cb(data,exception){
 	store_cb(data,exception);
 	setaDsTpSituacao(data, exception);
 }
 
 </script>

<adsm:window service="lms.municipios.grupoClassificacaoService">
	<adsm:form action="/municipios/manterGruposClassificacao"  idProperty="idGrupoClassificacao" onDataLoadCallBack="grupoClassificacaoOnDataLoad">
		<adsm:textbox dataType="text" property="dsGrupoClassificacao" label="descricao" required="true" maxLength="60" size="60" labelWidth="15%" width="85%" />
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true" labelWidth="15%" width="85%"/>
		<adsm:hidden property="dsTpSituacaoGrupo"/>
		<adsm:buttonBar>
			<adsm:button caption="divisoes" action="/municipios/manterDivisoesGrupoClassificacao" cmd="main" >
				<adsm:linkProperty src="idGrupoClassificacao,dsGrupoClassificacao" target="grupoClassificacao.idGrupoClassificacao" disabled="true"/>
				<adsm:linkProperty src="dsTpSituacaoGrupo" target="dsTpSituacaoGrupo" disabled="true"/>
			</adsm:button>
			<adsm:storeButton callbackProperty="myStoreButton"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   