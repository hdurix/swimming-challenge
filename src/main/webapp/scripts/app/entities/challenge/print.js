'use strict';

angular.module('swimmingchallengeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('print', {
                parent: 'challenge',
                url: 'print',
                data: {
                    authorities: [],
                    pageTitle: 'Impression inscription'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/challenge/print.html',
                        controller: 'PrintController as vm'
                    }
                },
                resolve: {
                }
            });
    })  ;
