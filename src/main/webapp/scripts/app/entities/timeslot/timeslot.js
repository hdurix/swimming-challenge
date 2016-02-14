'use strict';

angular.module('swimmingchallengeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('timeslot', {
                parent: 'entity',
                url: '/timeslots',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Timeslots'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/timeslot/timeslots.html',
                        controller: 'TimeslotController as vm'
                    }
                },
                resolve: {
                }
            })
            .state('timeslot.detail', {
                parent: 'entity',
                url: '/timeslot/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Timeslot'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/timeslot/timeslot-detail.html',
                        controller: 'TimeslotDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Timeslot', function($stateParams, Timeslot) {
                        return Timeslot.get({id : $stateParams.id});
                    }]
                }
            })
            .state('timeslot.edit', {
                parent: 'timeslot',
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
                        $state.go('timeslot', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
