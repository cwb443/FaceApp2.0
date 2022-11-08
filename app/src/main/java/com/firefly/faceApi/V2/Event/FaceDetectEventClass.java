package com.firefly.faceApi.V2.Event;

/**
 * @ProjectName: FaceApiDemoExternal_mast29
 * @Package: com.firefly.faceApi.V2.Event
 * @ClassName: FaceDetectEventClass
 * @Description: java类作用描述
 * @Author: SQL
 * @CreateDate: 2022/10/31 15:40
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/10/31 15:40
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class FaceDetectEventClass {
    private Long id;
    private String name;
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
