package com.dev.NT_Badminton.entities.upload_file.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.dev.NT_Badminton.dto.constant.BaseEnum;

public enum UploadFileType implements BaseEnum<Integer> {
    IMAGE(0),
    VIDEO(1);

    final int value;

    UploadFileType(int value) {this.value = value;}

    @JsonCreator
    public static UploadFileType fromValue(int value) {
        for(UploadFileType column : UploadFileType.values()) {
            if(column.value == value) {
                return column;
            }
        }
        return null;
    }

    @Override
    public Integer toValue() {
        return 0;
    }
}
