package com.ohgiraffers.warehousemanagement.wms.purchases.config;

import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseStatus;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "com.ohgiraffers.warehousemanagement.wms.purchases")
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // String → PurchaseStatus Enum으로 변환하는 컨버터 등록
        registry.addConverter(String.class, PurchaseStatus.class, source -> {
            try {
                // 1. 먼저 직접 Enum 이름으로 변환 시도 (대기, 완료, 거절, 취소)
                return PurchaseStatus.valueOf(source);
            } catch (IllegalArgumentException e) {
                // 2. 실패하면 라벨(발주대기, 발주완료 등)로 변환 시도
                for (PurchaseStatus status : PurchaseStatus.values()) {
                    if (status.getLabel().equals(source)) {
                        return status;
                    }
                }
                // 3. 기본값 반환 (발주대기)
                return PurchaseStatus.대기;
            }
        });
    }
}
