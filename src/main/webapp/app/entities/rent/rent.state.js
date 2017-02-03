(function() {
    'use strict';

    angular
        .module('carrentalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rent', {
            parent: 'entity',
            url: '/rent?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'carrentalApp.rent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rent/rents.html',
                    controller: 'RentController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rent');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('rent-detail', {
            parent: 'entity',
            url: '/rent/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'carrentalApp.rent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rent/rent-detail.html',
                    controller: 'RentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rent');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Rent', function($stateParams, Rent) {
                    return Rent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rent',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rent-detail.edit', {
            parent: 'rent-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rent/rent-dialog.html',
                    controller: 'RentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rent', function(Rent) {
                            return Rent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rent.new', {
            parent: 'rent',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rent/rent-dialog.html',
                    controller: 'RentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                price: null,
                                deposit: null,
                                rent_date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rent', null, { reload: 'rent' });
                }, function() {
                    $state.go('rent');
                });
            }]
        })
        .state('rent.edit', {
            parent: 'rent',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rent/rent-dialog.html',
                    controller: 'RentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rent', function(Rent) {
                            return Rent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rent', null, { reload: 'rent' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rent.delete', {
            parent: 'rent',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rent/rent-delete-dialog.html',
                    controller: 'RentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Rent', function(Rent) {
                            return Rent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rent', null, { reload: 'rent' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
