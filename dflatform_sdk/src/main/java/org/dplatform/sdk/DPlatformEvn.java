package org.dplatform.sdk;

public enum DPlatformEvn {
    TEST(0, "测试"),
    PRO(1, "预发"),
    RELEASE(2, "生产");


    String description;
    int index;

    DPlatformEvn(int index, String description) {
        this.index = index;
        this.description = description;
    }
}
