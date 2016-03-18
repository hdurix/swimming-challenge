'use strict';

angular.module('swimmingchallengeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('administration', {
                parent: 'entity',
                url: '/administration',
                data: {
                    authorities: [],
                    pageTitle: 'Administration'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/administration/administration.html',
                        controller: 'AdministrationController as vm'
                    }
                },
                resolve: {
                }
            });
    })  ;
