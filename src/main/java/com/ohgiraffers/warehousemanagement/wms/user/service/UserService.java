package com.ohgiraffers.warehousemanagement.wms.user.service;

import com.ohgiraffers.warehousemanagement.wms.user.model.dto.LogUserDTO;

public interface UserService {

    LogUserDTO getUserInfoForLogging(Integer userId);
}