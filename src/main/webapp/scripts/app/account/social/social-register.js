'use strict';

angular.module('swimmingchallengeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('social-register', {
                parent: 'account',
                url: '/social-register/:provider?{success:boolean}',
                data: {
                    authorities: [],
                    pageTitle: 'Connexion avec {{providerLabel}}'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/social/social-register.html',
                        controller: 'SocialRegisterController'
                    }
                },
                resolve: {

                }
            });
    });
