package com.agh.mallet.infrastructure.mapper;

import com.agh.mallet.domain.group.entity.PermissionType;
import com.agh.mallet.infrastructure.exception.MalletIllegalArgumentException;

import java.util.Arrays;

public class PermissionTypeMapper {

    private PermissionTypeMapper() {
    }

    public static com.agh.api.PermissionType from(PermissionType permissionType) {
        return Arrays.stream(com.agh.api.PermissionType.values())
                .filter(value -> value.name().equalsIgnoreCase(permissionType.name()))
                .findAny()
                .orElseThrow(() -> new MalletIllegalArgumentException("Unknown permission typ"));
    }

    public static PermissionType from(com.agh.api.PermissionType permissionType) {
        return Arrays.stream(PermissionType.values())
                .filter(value -> value.name().equalsIgnoreCase(permissionType.name()))
                .findAny()
                .orElseThrow(() -> new MalletIllegalArgumentException("Unknown permission typ"));
    }

}
