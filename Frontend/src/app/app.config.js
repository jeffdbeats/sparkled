/*global angular*/
(function () {
    'use strict';

    function stateConfig($stateProvider, $urlRouterProvider) {

        addState($stateProvider, '/selector', 'selector', 'Selector');
        addState($stateProvider, '/editor/:id', 'editor', 'Editor');

        $urlRouterProvider.otherwise('/selector');
    }

    function addState($stateProvider, url, name, title) {
        $stateProvider.state(name, {
            url: url,
            templateUrl: 'app/' + name + '/' + name + '.html',
            data: {
                pageTitle: title
            }
        });
    }

    function resourceConfig($resourceProvider) {
        $resourceProvider.defaults.timeout = 5000;
    }

    angular.module('ledStripAnimator').config(stateConfig, resourceConfig);
}());

