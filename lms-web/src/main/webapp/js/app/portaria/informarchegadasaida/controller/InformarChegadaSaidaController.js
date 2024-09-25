'use strict';

goog.provide('app.global.viewDefinition.portaria.InformarChegadaSaidaController');

var InformarChegadaSaidaController = [
    '$scope',
    'translateService',
    'FilialFactory',
    '$rootScope',
    'modalService',
    function ($scope, translateService, FilialFactory, $rootScope, modalService) {

        $scope.filter = {};
        $scope.status = {
            open: false
        };
        $scope.data = {};

        var titles = {
            'CHEGADA': 'informarChegada',
            'SAIDA': 'informarSaida'
        };

        var subTitles = {
            'VIAGEM': 'viagem',
            'COLETAENTREGA': 'coletaEntrega',
            'ORDEMSAIDA': 'ordemDeSaida'
        };

        $scope.setConstructor({
            rest: '/portaria/informarChegadaSaida'
        });

        FilialFactory.findFilialUsuarioLogado().then(function (filial) {
            $scope.idFilial = filial.idFilial;
        });

        $scope.clear = function () {
            closeAccordion();
            setDefaultTitleAccordion();
            $scope.filter = {};
            clearData();
            limpaAlerts();
        };

        $scope.clear();

        function clearData() {
            $scope.data = {};
        }

        function setDefaultTitleAccordion() {
            $scope.title = translateService.getMensagem('chegadaSaida');
        }

        function setTitleAccordion(title) {
            $scope.title = '';
            $scope.title = translateService.getMensagem(title);
        }

        function setSubTitleAccordion(subTitle) {
            $scope.title += ' - ' + translateService.getMensagem(subTitle);
        }

        function limpaAlerts() {
            $rootScope.clearAlerts();
        }

        function openAccordion() {
            $scope.status.open = true;
        }

        function closeAccordion() {
            $scope.status.open = false;
        }

        $scope.$watch('filter.resultSuggest', function (suggestModel) {
            if (suggestModel) {
                limpaAlerts();
                startLoading();
                $scope.rest.doPost('validarEBuscarDados', suggestModel).then(function (data) {

                    setTitleAccordion(titles[suggestModel.tipo]);
                    setSubTitleAccordion(subTitles[suggestModel.subTipo]);
                    $scope.data = angular.copy(data);
                    angular.extend($scope.data, suggestModel);

                    setVisibleCampos($scope.data);
                    openAccordion();

                    if (data.mensagemConfirmacao !== undefined) {
                        modalService.open({confirm: true,  message: data.mensagemConfirmacao}).then(
                            function () {
                            }
                            , function () {
                                clearData();
                                setDefaultTitleAccordion();
                                closeAccordion();
                            }
                        );
                    }

                    stopLoading();
                }, function (reason) {
                    stopLoading();
                    clearData();
                    setDefaultTitleAccordion();
                    closeAccordion();
                })['finally'](function () {
                    stopLoading();
                });
            }
        });

        function isViagem(subTipo) {
            return 'VIAGEM' === subTipo;
        }

        function isColetaEntrega(subTipo) {
            return 'COLETAENTREGA' === subTipo;
        }

        function isOrdemSaida(subTipo) {
            return 'ORDEMSAIDA' === subTipo;
        }

        function mostrarCamposQuilometragem(value) {
            $scope.mostrarCamposQuilometragem = value;
        }

        function mostrarCampoLacres(value) {
            $scope.mostrarCampoLacres = value;
        }

        function mostrarCampoControleCarga(value) {
            $scope.mostrarCampoControleCarga = value;
        }

        function mostrarCampoRota(value) {
            $scope.mostrarCampoRota = value;
        }

        function setVisibleCampos(data) {
            $scope.habilitarCamposQuilometragem = data.blInformaKmPortaria;
            definirVisibilidadeCampoControleCarga(data.subTipo);
            definirVisibilidadeCampoRota(data.subTipo);
            definirVisibilidadeCamposQuilometragem(data.subTipo);
            definirVisibilidadeCampoLacre(data.subTipo);
        }

        function definirVisibilidadeCampoControleCarga(subtipo) {
            if (isViagem(subtipo) || isColetaEntrega(subtipo)) {
                mostrarCampoControleCarga(true);
            } else if (isOrdemSaida(subtipo)) {
                mostrarCampoControleCarga(false);
            }
        }

        function definirVisibilidadeCampoRota(subtipo) {
            if (isViagem(subtipo) || isColetaEntrega(subtipo)) {
                mostrarCampoRota(true);
            } else if (isOrdemSaida(subtipo)) {
                mostrarCampoRota(false);
            }
        }

        function definirVisibilidadeCamposQuilometragem(subtipo) {
            if (isColetaEntrega(subtipo) || isOrdemSaida(subtipo)) {
                mostrarCamposQuilometragem(true);
            } else if (isViagem(subtipo)) {
                mostrarCamposQuilometragem(false);
            }
        }

        function definirVisibilidadeCampoLacre(subtipo) {
            if (isViagem(subtipo) || isColetaEntrega(subtipo)) {
                mostrarCampoLacres(true);
            } else if (isOrdemSaida(subtipo)) {
                mostrarCampoLacres(false);
            }
        }

        $scope.dataIsEmpty = function () {
            return angular.equals($scope.data, {});
        };

        function startLoading() {
            $rootScope.showLoading = true;
        }

        function stopLoading() {
            $rootScope.showLoading = false;
        }

        $scope.salvar = function () {
            startLoading();
            delete $scope.data.mensagemConfirmacao;
            $scope.rest.doPost('salvar', $scope.data).then(function () {
                $scope.clear();
                $scope.showSuccessMessage();
            }, function () {
                stopLoading();
            })['finally'](function () {
                stopLoading();
            });
        };

        $scope.formataLacres = function (lacres) {
            var nrLacres = [];
            angular.forEach(lacres, function (lacre) {
                nrLacres.push(lacre.nrLacres);
            });
            return nrLacres.join(', ');
        };

    }];
