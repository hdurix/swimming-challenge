(function () {
    'use strict';

    angular.module('swimmingchallengeApp')
        .controller('ChallengeController', ChallengeController);

    function ChallengeController(Timeslot, Principal, User, AlertService, $rootScope, $state, $filter) {

        var vm = this;

        var userCreationTypes = {
            CURRENT: {id: "CURRENT", label: "Utilisateur connecté"},
            EXISTING: {id: "EXISTING", label: "Utilisateur enregistré"},
            NEW: {id: "NEW", label: "Nouvel utilisateur"}
        };

        vm.clear = clear;
        vm.eraseTimeslot = eraseTimeslot;
        vm.getClass = getClass;
        vm.getTooltip = getTooltip;
        vm.goToLogin = goToLogin;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadAll = loadAll;
        vm.refresh = refresh;
        vm.saveTimeslot = saveTimeslot;
        vm.timeslotsByTime = {};
        vm.userCreationType = userCreationTypes.CURRENT;
        vm.userCreationTypes = userCreationTypes;

        activate();

        function activate() {
            loadAll();
            loadUser();
            loadUsers();
        }

        function loadAll() {
            Timeslot.query(function (timeslots) {
                timeslots = $filter('orderBy')(timeslots, 'startTime');
                var hours = ["18", "19", "20"];
                _.each(hours, function(hour) {
                    var hourTS = _.filter(timeslots, function(ts) { return ts.startTime.substring(0, 2) == hour; });
                    var byTime = _.groupBy(hourTS, function(ts) { return ts.startTime + ' - ' + ts.endTime; });
                    var byLine = _.groupBy(hourTS, 'line');
                    vm.timeslotsByTime["time" + hour] = {byTime : byTime, byLine: byLine};
                });
                loadTimeslot(timeslots);
            });
        }

        function loadTimeslot(timeslots) {
            if ($rootScope.editingTimeslot) {
                vm.currentTimeslot = _.findWhere(timeslots, {id: $rootScope.editingTimeslot});
                $rootScope.editingTimeslot = undefined;
            }
        }

        function loadUser() {
            Principal.identity().then(function(account) {
                vm.user = account;
                vm.connected = account !== null;
            });
        }

        function loadUsers() {
            if (vm.isAuthenticated()) {
                vm.users = User.query();
            }
        }

        function refresh() {
            loadAll();
            clear();
        }

        function goToLogin() {
            $rootScope.editingTimeslot = vm.currentTimeslot.id;
            $state.go("login");
        }

        function getClass(timeslot) {
            if (timeslot.payed) {
                return "payed";
            }
            if (timeslot.reserved) {
                return "reserved";
            }
            return "free";
        }

        function getTooltip(timeslot) {
            if (timeslot.payed) {
                return "Equipe " + timeslot.teamName + " (payé)";
            }
            if (timeslot.reserved) {
                return "Equipe " + timeslot.teamName + " (réservé)";
            }
            return "Créneau libre";
        }

        function onSaveSuccess(result) {
            vm.currentTimeslot = result;
            vm.isSaving = false;
            if (vm.userCreationType === userCreationTypes.CURRENT) {
                vm.user.nbReserved++;
            }
            loadUsers();
        }

        function onSaveError(result) {
            vm.isSaving = false;
            AlertService.error("Une erreur est survenue. Veuillez réessayer.");
            vm.currentTimeslot.reserved = false;
            vm.currentTimeslot.reservedDate = null;
        }

        function saveTimeslot() {
            vm.isSaving = true;
            vm.currentTimeslot.reserved = true;
            vm.currentTimeslot.reservedDate = new Date();
            if (vm.userCreationType !== userCreationTypes.NEW) {
                if (vm.userCreationType === userCreationTypes.CURRENT) {
                    vm.currentTimeslot.user = vm.user;
                }
                vm.currentTimeslot.user = {id: vm.currentTimeslot.user.id};
            } else {
                vm.currentTimeslot.user.external = true;
            }
            Timeslot.update(vm.currentTimeslot, onSaveSuccess, onSaveError);
        }

        function eraseTimeslot() {
            if (confirm('Etes-vous sûr de vouloir supprimer cette réservation ?')) {
                Timeslot.erase({id: vm.currentTimeslot.id}, {}, function(timeslot) {
                    for(var p in timeslot) {
                        vm.currentTimeslot[p] = timeslot[p];
                    }
                });
            }
        }

        function clear() {
            vm.timeslot = {
                startTime: null,
                endTime: null,
                payed: false,
                reserved: false,
                teamName: null,
                swimmer1: null,
                swimmer2: null,
                swimmer3: null,
                swimmer4: null,
                reservedDate: null,
                line: null,
                version: null,
                id: null
            };
        }
    }
})();
