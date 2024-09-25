
var AppController = [ 
	"$rootScope", 
	"$scope", 
	"$state", 
	"$modal", 
	"modalService", 
	"$timeout", 
	"$q", 
	"translateService", 
	"$interval", 
	"$locale", 
	"SecurityFactory",
	"SystemInfoFactory",
	"MenuFactory",
	function($rootScope, $scope, $state, $modal, modalService, $timeout, $q, translateService, $interval, $locale, SecurityFactory, SystemInfoFactory, MenuFactory) {
		/* --------LMS--------------------------------------------------------------------------------- */
		$scope.menuModel = MenuFactory.menu();
		
		$rootScope.isSwt = isSwt;
		
		var SUCCESS_MESSAGE_KEY = "LMS-00054";
		
		SecurityFactory.getCurrentUser().then(function(currentUser) {
			$rootScope.usuarioLogado = currentUser;
		});
		
		appControllerUtils($rootScope, $scope, $state, $timeout, $q, modalService, translateService, $interval, $locale);
		
		if (translateService) {
			$rootScope.loadMensagens("LMS-30017,LMS-05330,grid.paginacao.nenhum-registro,LMS-00077,LMS-00078,ADSM_SESSION_EXPIRED_EXCEPTION_KEY");
		}
		
		$rootScope.onClickMenuLms = function(item) {
   		 	if (item.novo) {
            	return item.acao;
            }
            $rootScope.openLms(item.acao);
            return;
    	};

    	$rootScope.openLms = function(url) {
			url = url.replace("?", "&");
			
			var port = window.location.port == "80" ? "" : ":" + window.location.port;
			
			var host = window.location.protocol + "//" + window.location.hostname + port + contextPath + "view/frameset?action=";
			
    		$.ajax("http://"+window.location.hostname+contextPath+"jsonbroker", {
    			type: "POST",
                accepts: "application/json",
                contentType: "text/plain; charset=iso-8859-1",
    			data: "<formBeans><formBean service=\"adsm.angular.security.usuarioService.findDadosUsuarioLogado\" callId=\"loadUserInfo\" callTS=\""+ (new Date()-0) +"\"></formBean></formBeans>",
                xhrFields: {
                    withCredentials: true
                }
            }).then(function(data, status, xhr) {
            	if($rootScope.isSwt){
        			openLmsSwt(host + url);
        		} else {
        			window.open(host + url);
        		}
            }, function(jqXHR, textStatus, errorThrown) {
            	if($rootScope.isSwt){
        			openLmsSwt(host + url);
        		} else {
        			window.open(host + url);
        		}
            });
    	};
		
		/* --------fim LMS--------------------------------------------------------------------------------- */
    	
    	$scope.showTabLoading = false;
		$scope.$on("showTabLoading", function(event, value) {
			$scope.showTabLoading = value;
		});
		
		$rootScope.showMessage = function (key, type) {
			$rootScope.addAlerts([{msg: key, type: type}]);
		};
		
		$rootScope.showSuccessMessage = function () {
			$rootScope.showMessage(SUCCESS_MESSAGE_KEY, MESSAGE_SEVERITY.SUCCESS); 
		};

		var formatarDataHora = function() {
			var date = new Date();
			$rootScope.dataHora = Utils.Date.formatDateTime(date, $locale);
		};

		$interval(formatarDataHora, 60000);
		formatarDataHora();


		$scope.menuAberto = false;

    	$scope.toggleMenu = function() {
    		$scope.menuAberto = !$scope.menuAberto;
    	};

    	$scope.classConteudo = function(menuAberto) {
    		return menuAberto ? 'col-sm-9 col-sm-offset-3' : 'col-sm-12';
    	};

    	$scope.classMenu = function(menuAberto) {
    		return menuAberto ? 'col-sm-3' : 'hide';
    	};

    	var waitLmsTimes = 100;
    	
    	var waitBody = function(page, w) {
    		if (waitLmsTimes < 1) {
    			return;
    		}
    		waitLmsTimes--;
    		var p = page;
    		if (w.document.frames && w.document.frames[1] && w.document.frames[0] && w.document.frames[0].RP) {
    			$timeout(function () {
        			w.document.frames[0].RP(page);
        			w.document.swtFrameSet = 'swt';
    			}, 1000);
    		} else {
    			$timeout(function () {waitBody(p, w);}, 100);
    		}
    	};
    	
    	/*----- LOGIN ---------*/
    	
    	$rootScope.openModalLogin = function() {
    		$rootScope.isModalLoginOpened = true;
    		$rootScope.clearAlerts();
    		$rootScope.isPopup = true;

    		var modalInstance = $modal.open({
				templateUrl: contextPath+"template/login/popupLogin.html",
				controller: PopupLoginController,
				windowClass: "popupLogin",
				backdrop: "static",
				resolve: {
					usuario: function () {
						return {usuario: "", senha: ""};
    				}
				}
			});
    		
    		return modalInstance.result.then(function(){
    			$rootScope.isPopup = false;
    			$rootScope.isModalLoginOpened = false;
    		}, function() {
    			$rootScope.isPopup = false;
    			$rootScope.isModalLoginOpened = false;
       		});
    	};
    	
    	$rootScope.$on("sessionExpired", function(event, args) {
    		modalService.closeAll();
    		if ($rootScope.isSwt){
				if ($rootScope.popup) {
					var msg = $rootScope.$eval("'ADSM_SESSION_EXPIRED_EXCEPTION_KEY' | translate");
					$rootScope.popup(msg);
				} else {
					console.log("Sua sessão expirou!");
				}
    		} else {
    			if (!$rootScope.isModalLoginOpened ) {
    				$rootScope.openModalLogin();
    			}
    		}
    	});
    	
    	$rootScope.$on("$stateChangeStart", function(event, next) {
    		var home = "app.home";
    		if (next.data && next.data.viewAction) {
	    		var tela = next.data.viewAction;
	    		$rootScope.showLoading = true;
	    		SecurityFactory.getPermissions(tela).then(function () {
	    			if (!SecurityFactory.hasPermission(tela)) {
	    				modalService.open({title: "Controle de acesso", message: "Sem acesso a este recurso"});
	    				$state.go(home);
	    			}
	    		})['finally'](function () {
	    			$rootScope.showLoading = false;
	    		});
    		} else if (next.name !== home){
    			$state.go(home);
    		}
    	});
	}
];
