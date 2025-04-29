package com.ohgiraffers.warehousemanagement.wms.sales.model.entity;

public enum SalesStatus {
    PENDING {
        @Override
        public boolean transition(SalesStatus status) {
            return status == APPROVED || status == CANCELED;
        }
    },
    APPROVED{
        @Override
        public boolean transition(SalesStatus status) {
            return false;
        }
    },
    CANCELED{
        @Override
        public boolean transition(SalesStatus status) {
            return false;
        }
    };

    public abstract boolean transition(SalesStatus status);
}
