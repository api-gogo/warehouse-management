package com.ohgiraffers.warehousemanagement.wms.user.service;

import com.ohgiraffers.warehousemanagement.wms.user.model.dto.LogUserDTO;

public interface UserLoggingService {

    LogUserDTO getUserInfoForLogging(Integer userId);
}