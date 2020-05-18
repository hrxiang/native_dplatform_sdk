package org.dplatform.sdk;

public enum DPlatformEvn {
    DEBUG(0, "联调"),
    TEST(1, "测试"),
    PRO(2, "预发"),
    RELEASE(3, "生产");


    String description;
    int index;

    DPlatformEvn(int index, String description) {
        this.index = index;
        this.description = description;
    }
}
