'use strict';

angular.module('swimmingchallengeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('challenge', {
                parent: 'entity',
                url: '/challenge',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Challenge'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/challenge/challenge.html',
                        controller: 'ChallengeController as vm'
                    }
                },
                resolve: {
                }
            })
            .state('challenge.edit', {
                parent: 'challenge',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/timeslot/timeslot-dialog.html',
                        controller: 'TimeslotDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Timeslot', function(Timeslot) {
                                return Timeslot.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('challenge', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    })  ;
